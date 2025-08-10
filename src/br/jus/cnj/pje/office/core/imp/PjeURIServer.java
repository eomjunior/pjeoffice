/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeContext;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import com.github.utils4j.ILifeCycle;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.ThreadContext;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PjeURIServer
/*     */   extends DefaultPjeCommander<IPjeRequest, IPjeResponse>
/*     */ {
/*     */   private final ILifeCycle<IOException> capturer;
/*     */   
/*     */   public PjeURIServer(IBootable boot, String serverAddress) {
/*  49 */     super(boot, serverAddress);
/*  50 */     this.capturer = (ILifeCycle<IOException>)new URICapturer(serverAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStart() throws IOException {
/*  55 */     this.capturer.start();
/*  56 */     super.doStart();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStop(boolean kill) throws IOException {
/*  61 */     this.capturer.stop(2000L);
/*  62 */     super.doStop(kill);
/*     */   }
/*     */   
/*     */   protected void submit(IPjeContext context) {
/*  66 */     if (context != null) {
/*  67 */       super.execute(context.getRequest(), context.getResponse());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearBuffer() {}
/*     */   
/*     */   protected final void openRequest(String request) {
/*  75 */     Throwables.quietly(() -> submit(createContext(getServerEndpoint("/") + request)));
/*     */   }
/*     */   
/*     */   private class URICapturer
/*     */     extends ThreadContext<IOException> {
/*     */     public URICapturer(String contextName) {
/*  81 */       super(contextName);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void beforeRun() {
/*  86 */       PjeURIServer.this.clearBuffer();
/*     */     }
/*     */     
/*     */     protected boolean isValid(String uri) {
/*  90 */       return uri.startsWith(PjeURIServer.this.getServerEndpoint());
/*     */     }
/*     */     
/*     */     private final IPjeContext createContext() throws Exception {
/*  94 */       String uri = Strings.trim(PjeURIServer.this.getUri());
/*  95 */       if (!isValid(uri)) {
/*  96 */         return null;
/*     */       }
/*  98 */       return PjeURIServer.this.createContext(uri);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doRun() throws Exception {
/* 103 */       int errorCount = 0;
/*     */       while (true) {
/*     */         IPjeContext context;
/*     */         try {
/* 107 */           context = createContext();
/* 108 */           errorCount = 0;
/* 109 */         } catch (InterruptedException|java.io.InterruptedIOException e) {
/* 110 */           Thread.currentThread().interrupt();
/* 111 */           AbstractPjeCommander.LOGGER.warn("Thread PjeURIServer interrompida");
/*     */           break;
/* 113 */         } catch (Exception e) {
/* 114 */           AbstractPjeCommander.LOGGER.warn("Requisição mal formada", e);
/* 115 */           if (++errorCount > 10) {
/* 116 */             AbstractPjeCommander.LOGGER.error("Total máximo de erros alcançado: {}. Thread finalizada", Integer.valueOf(errorCount));
/*     */             break;
/*     */           } 
/* 119 */           if (!Thread.interrupted()) {
/* 120 */             PjeURIServer.this.clearBuffer();
/*     */             continue;
/*     */           } 
/* 123 */           AbstractPjeCommander.LOGGER.warn("Thread interrompida", e);
/*     */           break;
/*     */         } 
/* 126 */         if (context == null) {
/* 127 */           AbstractPjeCommander.LOGGER.warn("Contexto indisponível");
/*     */           
/*     */           continue;
/*     */         } 
/* 131 */         PjeURIServer.this.async(() -> PjeURIServer.this.submit(context));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private final IPjeContext createContext(String uri) throws Exception {
/* 137 */     return SimpleContext.of(createRequest(uri), createResponse());
/*     */   }
/*     */   
/*     */   protected abstract IPjeResponse createResponse() throws Exception;
/*     */   
/*     */   protected abstract IPjeRequest createRequest(String paramString) throws Exception;
/*     */   
/*     */   protected abstract String getUri() throws InterruptedException, Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeURIServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */