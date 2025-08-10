/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.KeyStore;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpPost;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
/*     */ import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
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
/*     */ public enum PjeWebGlobal
/*     */   implements IPjeWebCodec
/*     */ {
/*  54 */   HTTP_CODEC
/*     */   {
/*     */     protected void setup(PjeInterceptor interceptor) {
/*  57 */       this
/*     */         
/*  59 */         .codec = (new PjeWebCodec.Builder<>()).setLoggerInterceptor(interceptor).build();
/*     */     }
/*     */   },
/*  62 */   HTTPS_CODEC
/*     */   {
/*     */     protected void setup(PjeInterceptor interceptor) {
/*  65 */       try (InputStream input = PjeWebGlobal.class.getResourceAsStream("/pjeoffice.jks")) {
/*  66 */         KeyStore keyStore = KeyStore.getInstance("jks");
/*  67 */         keyStore.load(input, "pjeoffice".toCharArray());
/*     */ 
/*     */         
/*  70 */         SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keyStore, null).build();
/*     */ 
/*     */         
/*  73 */         SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create().setSslContext(sslcontext).build();
/*     */ 
/*     */         
/*  76 */         PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory((LayeredConnectionSocketFactory)sslSocketFactory).build();
/*  77 */         this
/*     */ 
/*     */           
/*  80 */           .codec = (new PjeWebCodec.Builder<>()).setConnectionManager(cm).setLoggerInterceptor(interceptor).build();
/*  81 */       } catch (Exception e) {
/*  82 */         throw new RuntimeException("Impossível instanciar HTTPS_CODEC", e);
/*     */       } 
/*     */     } };
/*     */   protected static volatile PjeInterceptor INTERCEPTOR;
/*     */   static {
/*  87 */     INTERCEPTOR = PjeInterceptor.NOTHING;
/*     */   }
/*     */   protected IPjeWebCodec codec;
/*     */   
/*     */   public static void open() {}
/*     */   
/*     */   PjeWebGlobal() {
/*  94 */     setup(PjeInterceptor.NOTHING);
/*     */   }
/*     */   
/*     */   public static void recycleAll() {
/*  98 */     for (PjeWebGlobal v : values()) {
/*  99 */       Throwables.quietly(v::recycle);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void closeAll() {
/* 104 */     for (PjeWebGlobal v : values()) {
/* 105 */       Throwables.quietly(v::close);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean setDevmode(boolean devmode) {
/* 110 */     if (devmode) {
/* 111 */       if (INTERCEPTOR != PjeInterceptor.DEVMODE) {
/* 112 */         INTERCEPTOR = PjeInterceptor.DEVMODE;
/* 113 */         recycleAll();
/*     */       } 
/* 115 */     } else if (INTERCEPTOR != PjeInterceptor.NOTHING) {
/* 116 */       INTERCEPTOR = PjeInterceptor.NOTHING;
/* 117 */       recycleAll();
/*     */     } 
/* 119 */     return devmode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 126 */     return this.codec.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public PjeTaskResponse post(IProvider<HttpPost> provider, IResultChecker checker) throws Exception {
/* 131 */     States.requireFalse(isClosed(), "o codec encontra-se fechado");
/* 132 */     return (PjeTaskResponse)this.codec.post(provider, checker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void get(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/* 137 */     States.requireFalse(isClosed(), "o codec encontra-se fechado");
/* 138 */     this.codec.get(provider, status);
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(IProvider<HttpGet> provider) throws Exception {
/* 143 */     States.requireFalse(isClosed(), "o codec encontra-se fechado");
/* 144 */     return this.codec.get(provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 149 */     this.codec.close();
/*     */   }
/*     */   
/*     */   public void recycle() throws IOException {
/*     */     try {
/* 154 */       close();
/* 155 */       setup(INTERCEPTOR);
/* 156 */     } catch (IOException e) {
/* 157 */       throw e;
/* 158 */     } catch (Exception e) {
/* 159 */       throw new IOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void setup(PjeInterceptor paramPjeInterceptor);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */