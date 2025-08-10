/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeRequestHandler;
/*     */ import com.github.utils4j.imp.HttpToucher;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.net.httpserver.HttpsConfigurator;
/*     */ import com.sun.net.httpserver.HttpsParameters;
/*     */ import com.sun.net.httpserver.HttpsServer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.BindException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.security.KeyStore;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ enum PjeServerMode
/*     */ {
/*  60 */   HTTP
/*     */   {
/*     */     protected HttpServer setup(IPjeWebServerSetup setup) throws IOException
/*     */     {
/*  64 */       HttpServer server = HttpServer.create();
/*  65 */       null.bind(server, setup);
/*  66 */       return server;
/*     */     }
/*     */   },
/*     */   
/*  70 */   HTTPS
/*     */   {
/*     */     protected HttpsServer setup(IPjeWebServerSetup setup) throws IOException
/*     */     {
/*  74 */       HttpsServer server = HttpsServer.create();
/*  75 */       null.bind(server, setup);
/*  76 */       return setupSSL(server);
/*     */     }
/*     */     
/*     */     HttpsServer setupSSL(HttpsServer server) throws IOException {
/*     */       try {
/*  81 */         SSLContext sslContext = SSLContext.getInstance("TLS");
/*  82 */         char[] password = "servercert".toCharArray();
/*  83 */         KeyStore keyStore = KeyStore.getInstance("JKS");
/*  84 */         try (InputStream input = getClass().getResourceAsStream("/shodo.jks")) {
/*  85 */           keyStore.load(input, password);
/*  86 */           KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
/*  87 */           keyManagerFactory.init(keyStore, password);
/*  88 */           sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
/*  89 */           server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
/*     */                 public void configure(HttpsParameters params) {
/*  91 */                   SSLContext sslContext = getSSLContext();
/*  92 */                   params.setNeedClientAuth(false);
/*  93 */                   SSLEngine engine = sslContext.createSSLEngine();
/*  94 */                   params.setCipherSuites(engine.getEnabledCipherSuites());
/*  95 */                   params.setProtocols(engine.getEnabledProtocols());
/*  96 */                   params.setSSLParameters(sslContext.getDefaultSSLParameters());
/*     */                 }
/*     */               });
/*     */         } 
/* 100 */         return server;
/* 101 */       } catch (Exception e) {
/* 102 */         throw new IOException("Não foi possível configurar SSL no servidor web", e);
/*     */       } 
/*     */     } };
/*     */   
/*     */   static {
/* 107 */     LOGGER = LoggerFactory.getLogger(PjeServerMode.class);
/*     */   } private static Logger LOGGER;
/*     */   protected static void bind(HttpServer server, IPjeWebServerSetup setup) {
/* 110 */     server.setExecutor(setup.getExecutor());
/* 111 */     for (IPjeRequestHandler handler : setup.getHandlers()) {
/* 112 */       HttpContext context = server.createContext(handler.getEndPoint(), (HttpHandler)handler);
/* 113 */       for (Filter filter : setup.getFilters()) {
/* 114 */         context.getFilters().add(filter);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static <T extends HttpServer> T bind(T server, int port) throws IOException {
/* 120 */     int attempt = 1;
/* 121 */     IOException exception = null;
/*     */     while (true) {
/*     */       try {
/* 124 */         server.bind(new InetSocketAddress(port), 0);
/* 125 */         LOGGER.info("Servidor operando na porta {}", Integer.valueOf(port));
/* 126 */         return server;
/* 127 */       } catch (BindException e) {
/* 128 */         exception = e;
/* 129 */         LOGGER.warn("Porta {} já está sendo utilizada (tentativa {})", Integer.valueOf(port), Integer.valueOf(attempt));
/* 130 */         sendShutdownRequest(port);
/* 131 */       } catch (IOException e) {
/* 132 */         exception = e;
/* 133 */         LOGGER.error("Não foi possível bindar na porta {}", Integer.valueOf(port));
/*     */       } 
/* 135 */       if (attempt++ >= 3) {
/* 136 */         throw exception;
/*     */       }
/* 138 */       Threads.sleep(3500L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static HttpServer newHttp(IPjeWebServerSetup setup) throws IOException {
/* 143 */     return bind(HTTP.setup(setup), setup.getPort());
/*     */   }
/*     */   
/*     */   public static HttpsServer newHttps(IPjeWebServerSetup setup) throws IOException {
/* 147 */     return bind(HTTPS.setup(setup), setup.getPort());
/*     */   }
/*     */   
/*     */   private static void sendShutdownRequest(int port) {
/* 151 */     LOGGER.info("Tentativa de shutdown da porta {}", Integer.valueOf(port));
/* 152 */     HttpToucher.touch("http://127.0.0.1:" + port + "/pjeOffice/shutdown");
/*     */   }
/*     */   
/*     */   protected abstract <T extends HttpServer> T setup(IPjeWebServerSetup paramIPjeWebServerSetup) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeServerMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */