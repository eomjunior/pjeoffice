/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeEchoNotifier;
/*     */ import br.jus.cnj.pje.office.core.IPjeEchoNotifierSwitcher;
/*     */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeResponse;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequestHandler;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.IPjeWebServer;
/*     */ import br.jus.cnj.pje.office.core.imp.security.PjeComplianceFilter;
/*     */ import br.jus.cnj.pje.office.core.imp.security.PjePreflightFilter;
/*     */ import br.jus.cnj.pje.office.core.imp.security.PjePreviousPreflightCheckingFilter;
/*     */ import br.jus.cnj.pje.office.core.imp.security.bypass.PjePreviousPreflightCheckingFilterCache;
/*     */ import br.jus.cnj.pje.office.logger.PjeEvents;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.IRequestRejectNotifier;
/*     */ import com.github.utils4j.IStringDumpable;
/*     */ import com.github.utils4j.imp.Browser;
/*     */ import com.github.utils4j.imp.ContentTypes;
/*     */ import com.github.utils4j.imp.HttpToucher;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.net.httpserver.HttpsServer;
/*     */ import java.io.IOException;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeWebServer
/*     */   extends DefaultPjeCommander<IPjeHttpExchangeRequest, IPjeHttpExchangeResponse>
/*     */   implements IPjeWebServer
/*     */ {
/*     */   private HttpServer httpServer;
/*     */   private HttpsServer httpsServer;
/*     */   private PjeWebServerBuilder setup;
/*     */   
/*     */   private class FaviconRequestHandler
/*     */     extends PjeRequestHandler
/*     */   {
/*     */     FaviconRequestHandler() {
/*  73 */       super("/favicon.ico");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/*  77 */       response.write(Streams.fromResource("/welcome/favicon.ico"), ContentTypes.IMAGE_ICO.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private class PingRequestHandler extends PjeRequestHandler {
/*     */     PingRequestHandler() {
/*  83 */       super("/pjeOffice/");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/*  87 */       PjeWebServer.this.notifier.echoN2S((IStringDumpable)request);
/*  88 */       PjeWebTaskResponse.success(request.isPreflightable()).processResponse((IPjeResponse)response);
/*     */     }
/*     */   }
/*     */   
/*     */   private class AboutRequestHandler extends PjeRequestHandler {
/*     */     AboutRequestHandler() {
/*  94 */       super("/pjeOffice/versao/");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/*  98 */       AbstractPjeCommander.LOGGER.info("Recebida requisição de versão do assinador");
/*  99 */       PjeWebServer.this.notifier.echoN2S((IStringDumpable)request);
/* 100 */       response.write(PjeVersion.CURRENT.aboutBytes(PjeWebServer.this.boot.model()), ContentType.APPLICATION_JSON.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ShutdownRequestHandler extends PjeRequestHandler {
/*     */     ShutdownRequestHandler() {
/* 106 */       super("/pjeOffice/shutdown");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 110 */       AbstractPjeCommander.LOGGER.info("Recebida requisição de parada do assinador");
/* 111 */       PjeWebServer.this.notifier.echoN2S((IStringDumpable)request);
/* 112 */       PjeWebTaskResponse.success(request.isPreflightable()).processResponse((IPjeResponse)response);
/* 113 */       PjeWebServer.this.exit();
/*     */     }
/*     */   }
/*     */   
/*     */   private class LogoutRequestHandler extends PjeRequestHandler {
/*     */     LogoutRequestHandler() {
/* 119 */       super("/pjeOffice/logout");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 123 */       AbstractPjeCommander.LOGGER.info("Recebida requisição de logout do token");
/* 124 */       PjeWebServer.this.notifier.echoN2S((IStringDumpable)request);
/* 125 */       PjeWebTaskResponse.success(request.isPreflightable()).processResponse((IPjeResponse)response);
/* 126 */       PjeWebServer.this.logout();
/*     */     }
/*     */   }
/*     */   
/*     */   private class LoggerRequestHandler extends PjeRequestHandler {
/*     */     LoggerRequestHandler() {
/* 132 */       super("/pjeOffice/logger/");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 136 */       request.getParameter("windowSize").ifPresent(s -> PjeEvents.BUFFER.setMaxSize(Strings.toInt(s, 50)));
/* 137 */       PjeJsonTaskResponse.success((new JSONObject()).put("log", PjeEvents.BUFFER.pack())).processResponse((IPjeResponse)response);
/*     */     }
/*     */   }
/*     */   
/*     */   private class TaskRequestHandler extends PjeRequestHandler {
/*     */     TaskRequestHandler() {
/* 143 */       super("/pjeOffice/requisicao/");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 147 */       PjeWebServer.this.notifier.echoN2S((IStringDumpable)request);
/* 148 */       PjeWebServer.this.execute(request, response);
/*     */     }
/*     */   }
/*     */   
/*     */   private class WelcomeRequestHandler extends PjeRequestHandler {
/*     */     WelcomeRequestHandler() {
/* 154 */       super("/pjeOffice/welcome");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/*     */       try {
/* 159 */         String output = PjeWebServer.this.devOutput(request);
/* 160 */         response.write(Streams.fromResource("/welcome/" + output), ContentTypes.fromExtension(output));
/* 161 */       } catch (Exception e) {
/* 162 */         AbstractPjeCommander.LOGGER.warn("Não foi possível recuperar recurso", e);
/* 163 */         response.notFound();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class BackendEmulatorRequestHandler extends PjeRequestHandler {
/* 169 */     private byte[] DEFAULT_SUCCESS_RESPONSE = "Sucesso".getBytes(IConstants.DEFAULT_CHARSET);
/*     */     
/*     */     BackendEmulatorRequestHandler() {
/* 172 */       super("/pjeOffice/fakeBackend");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 176 */       Threads.sleep(60L);
/* 177 */       response.write(this.DEFAULT_SUCCESS_RESPONSE, ContentTypes.TEXT_HTML.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private class BackendEmulatorXmlRequestHandler extends PjeRequestHandler {
/* 182 */     private byte[] DEFAULT_XML_RESPONSE = "<hello>world</hello>".getBytes(IConstants.DEFAULT_CHARSET);
/*     */     
/*     */     BackendEmulatorXmlRequestHandler() {
/* 185 */       super("/pjeOffice/fakeBackend/xml");
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 189 */       response.write(this.DEFAULT_XML_RESPONSE, ContentType.APPLICATION_XML.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private class BackendEmulatorMinimumVersionCheckerRequestHandler extends PjeRequestHandler {
/* 194 */     private String requirements = Strings.empty();
/*     */     
/*     */     BackendEmulatorMinimumVersionCheckerRequestHandler() {
/* 197 */       super("/pjeOffice/fakeBackend/minimum");
/* 198 */       Streams.resourceLines("/welcome/devguide/minimum.json", lines -> lines.forEach(()));
/*     */     }
/*     */     
/*     */     protected void process(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) throws IOException {
/* 202 */       PjeWebServer.this.notifier.echoS2B((IStringDumpable)request);
/* 203 */       response.write(this.requirements.getBytes(), ContentType.APPLICATION_JSON.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   private final IPjeEchoNotifierSwitcher notifier = new PjeEchoNotifierSwitcher(PjeEchoNotifier.PRODUCTION);
/*     */   
/* 214 */   private final Filter comp = (Filter)new PjeComplianceFilter((IRequestRejectNotifier)this.notifier);
/* 215 */   private final Filter prfl = (Filter)new PjePreflightFilter((IRequestRejectNotifier)this.notifier);
/*     */   
/* 217 */   private final IPjeRequestHandler favi = new FaviconRequestHandler();
/* 218 */   private final IPjeRequestHandler ping = new PingRequestHandler();
/* 219 */   private final IPjeRequestHandler task = new TaskRequestHandler();
/* 220 */   private final IPjeRequestHandler lout = new LogoutRequestHandler();
/* 221 */   private final IPjeRequestHandler logg = new LoggerRequestHandler();
/* 222 */   private final IPjeRequestHandler vers = new AboutRequestHandler();
/* 223 */   private final IPjeRequestHandler exit = new ShutdownRequestHandler();
/* 224 */   private final IPjeRequestHandler back = new BackendEmulatorRequestHandler();
/* 225 */   private final IPjeRequestHandler bxml = new BackendEmulatorXmlRequestHandler();
/* 226 */   private final IPjeRequestHandler mini = new BackendEmulatorMinimumVersionCheckerRequestHandler();
/* 227 */   private final IPjeRequestHandler welc = new WelcomeRequestHandler();
/*     */   
/*     */   private final Filter pprfl;
/*     */   
/*     */   PjeWebServer(IBootable boot) {
/* 232 */     super(boot, "http://127.0.0.1:8800");
/* 233 */     this.pprfl = isSafeModel() ? (Filter)new PjePreviousPreflightCheckingFilter((IRequestRejectNotifier)this.notifier) : (Filter)new PjePreviousPreflightCheckingFilterCache((IRequestRejectNotifier)this.notifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDevModel() {
/* 239 */     return "d".equals(this.boot.model());
/*     */   }
/*     */   
/*     */   private boolean isSafeModel() {
/* 243 */     return "s".equals(this.boot.model());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void execute(IPjeHttpExchangeRequest request, IPjeHttpExchangeResponse response) {
/* 248 */     checkStarted();
/* 249 */     super.execute(request, response);
/*     */   }
/*     */   
/*     */   private void checkStarted() {
/* 253 */     if (!isStarted()) {
/* 254 */       throw new IllegalStateException("web server not started!");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeHttpExchangeResponse> getDefaultDiscardResponse(IPjeHttpExchangeRequest req, IPjeHttpExchangeResponse resp, TaskExecutorDiscartingException e) {
/* 261 */     return PjeWebTaskResponse.fail(req.isPreflightable());
/*     */   }
/*     */   
/*     */   private void startHttps() throws IOException {
/* 265 */     if (this.httpsServer == null) {
/* 266 */       this.httpsServer = PjeServerMode.newHttps(this.setup.usingPort(8801));
/* 267 */       this.httpsServer.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startHttp() throws IOException {
/* 272 */     if (this.httpServer == null) {
/* 273 */       this.httpServer = PjeServerMode.newHttp(this.setup.usingPort(8800));
/* 274 */       this.httpServer.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopHttps() {
/* 279 */     if (this.httpsServer != null) {
/* 280 */       this.httpsServer.stop(0);
/* 281 */       this.httpsServer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopHttp() {
/* 286 */     if (this.httpServer != null) {
/* 287 */       this.httpServer.stop(0);
/* 288 */       this.httpServer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startNotifiers() {
/* 293 */     this.notifier.open();
/* 294 */     PjeInterceptor.DEVMODE.setNotifier((IPjeEchoNotifier)this.notifier);
/*     */   }
/*     */   
/*     */   private void stopNotifiers() {
/* 298 */     this.notifier.close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void openRequest(String request) {
/* 303 */     HttpToucher.touch(getServerEndpoint(this.task.getEndPoint()) + request);
/*     */   }
/*     */   
/*     */   private void setProduction() {
/* 307 */     this.notifier.switchTo(PjeEchoNotifier.PRODUCTION);
/* 308 */     PjeWebGlobal.setDevmode(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showAbout() {
/* 313 */     Browser.navigateTo("http://127.0.0.1:8800" + this.welc.getEndPoint(), PjeConfig.getIcon());
/*     */   }
/*     */   
/*     */   private void setDevmode() {
/* 317 */     if (this.notifier.switchTo(PjeEchoNotifier.DEVMODE)) {
/* 318 */       PjeWebGlobal.setDevmode(true);
/* 319 */       this.notifier.show();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean setDevmode(boolean devmode) {
/* 325 */     if (devmode) {
/* 326 */       setDevmode();
/*     */     } else {
/* 328 */       setProduction();
/*     */     } 
/* 330 */     this.notifier.open();
/* 331 */     return devmode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStop(boolean kill) throws IOException {
/* 336 */     Throwables.quietly(this.setup::shutdown);
/* 337 */     this.setup = null;
/* 338 */     Throwables.quietly(this::stopHttp);
/* 339 */     Throwables.quietly(this::stopHttps);
/* 340 */     Throwables.quietly(this::stopNotifiers);
/* 341 */     super.doStop(kill);
/*     */   }
/*     */   
/*     */   private String devOutput(IPjeHttpExchangeRequest request) throws IOException {
/* 345 */     String output = request.getParameter("file").orElse("index.html");
/* 346 */     if (output.contains(".."))
/* 347 */       throw new IOException("Invalid path: " + output); 
/* 348 */     if (!isDevModel()) {
/* 349 */       if (output.endsWith("devguide.html"))
/* 350 */         throw new IOException("Guia do desenvolvedor não disponível"); 
/* 351 */       if (output.endsWith("index.html"))
/* 352 */         output = "index.nodevguide.html"; 
/*     */     } 
/* 354 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStart() throws IOException {
/*     */     try {
/* 360 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 374 */         .setup = (new PjeWebServerBuilder()).usingFilter(this.comp).usingFilter(this.prfl).usingFilter(this.pprfl).usingHandler(this.favi).usingHandler(this.ping).usingHandler(this.vers).usingHandler(this.task).usingHandler(this.exit).usingHandler(this.lout).usingHandler(this.logg).usingHandler(this.back).usingHandler(this.bxml).usingHandler(this.mini).usingHandler(this.welc);
/*     */       
/* 376 */       startHttp();
/* 377 */       startHttps();
/* 378 */       startNotifiers();
/* 379 */     } catch (IOException e) {
/* 380 */       LOGGER.warn("Não foi possível iniciar o servidor", e);
/* 381 */       doStop(false);
/* 382 */       throw e;
/*     */     } 
/* 384 */     super.doStart();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */