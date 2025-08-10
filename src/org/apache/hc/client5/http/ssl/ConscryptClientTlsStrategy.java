/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.conscrypt.Conscrypt;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class ConscryptClientTlsStrategy
/*     */   extends AbstractClientTlsStrategy
/*     */ {
/*     */   public static TlsStrategy getDefault() {
/*  55 */     return new ConscryptClientTlsStrategy(
/*  56 */         SSLContexts.createDefault(), 
/*  57 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */   
/*     */   public static TlsStrategy getSystemDefault() {
/*  61 */     return new ConscryptClientTlsStrategy(
/*  62 */         SSLContexts.createSystemDefault(), 
/*  63 */         HttpsSupport.getSystemProtocols(), 
/*  64 */         HttpsSupport.getSystemCipherSuits(), SSLBufferMode.STATIC, 
/*     */         
/*  66 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, SSLBufferMode sslBufferManagement, HostnameVerifier hostnameVerifier) {
/*  75 */     super(sslContext, supportedProtocols, supportedCipherSuites, sslBufferManagement, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslcontext, HostnameVerifier hostnameVerifier) {
/*  81 */     this(sslcontext, null, null, SSLBufferMode.STATIC, hostnameVerifier);
/*     */   }
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslcontext) {
/*  85 */     this(sslcontext, HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */   
/*     */   void applyParameters(SSLEngine sslEngine, SSLParameters sslParameters, String[] appProtocols) {
/*  90 */     if (Conscrypt.isConscrypt(sslEngine)) {
/*  91 */       sslEngine.setSSLParameters(sslParameters);
/*  92 */       Conscrypt.setApplicationProtocols(sslEngine, appProtocols);
/*     */     } else {
/*  94 */       sslParameters.setApplicationProtocols(appProtocols);
/*  95 */       sslEngine.setSSLParameters(sslParameters);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   TlsDetails createTlsDetails(SSLEngine sslEngine) {
/* 101 */     if (Conscrypt.isConscrypt(sslEngine)) {
/* 102 */       return new TlsDetails(sslEngine.getSession(), Conscrypt.getApplicationProtocol(sslEngine));
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isSupported() {
/*     */     try {
/* 109 */       Class<?> clazz = Class.forName("org.conscrypt.Conscrypt");
/* 110 */       Method method = clazz.getMethod("isAvailable", new Class[0]);
/* 111 */       return ((Boolean)method.invoke(null, new Object[0])).booleanValue();
/* 112 */     } catch (ClassNotFoundException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 113 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/ConscryptClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */