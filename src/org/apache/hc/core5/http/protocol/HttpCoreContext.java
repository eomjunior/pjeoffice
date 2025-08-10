/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class HttpCoreContext
/*     */   implements HttpContext
/*     */ {
/*     */   public static final String CONNECTION_ENDPOINT = "http.connection-endpoint";
/*     */   public static final String SSL_SESSION = "http.ssl-session";
/*     */   public static final String HTTP_REQUEST = "http.request";
/*     */   public static final String HTTP_RESPONSE = "http.response";
/*     */   private final HttpContext context;
/*     */   
/*     */   public static HttpCoreContext create() {
/*  71 */     return new HttpCoreContext();
/*     */   }
/*     */   
/*     */   public static HttpCoreContext adapt(HttpContext context) {
/*  75 */     if (context == null) {
/*  76 */       return new HttpCoreContext();
/*     */     }
/*  78 */     if (context instanceof HttpCoreContext) {
/*  79 */       return (HttpCoreContext)context;
/*     */     }
/*  81 */     return new HttpCoreContext(context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpCoreContext(HttpContext context) {
/*  88 */     this.context = context;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpCoreContext() {
/*  93 */     this.context = new BasicHttpContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 101 */     return this.context.getProtocolVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/* 109 */     this.context.setProtocolVersion(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/* 114 */     return this.context.getAttribute(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object setAttribute(String id, Object obj) {
/* 119 */     return this.context.setAttribute(id, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/* 124 */     return this.context.removeAttribute(id);
/*     */   }
/*     */   
/*     */   public <T> T getAttribute(String attribname, Class<T> clazz) {
/* 128 */     Args.notNull(clazz, "Attribute class");
/* 129 */     Object obj = getAttribute(attribname);
/* 130 */     if (obj == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     return clazz.cast(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 140 */     return getAttribute("http.ssl-session", SSLSession.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 147 */     return getAttribute("http.connection-endpoint", EndpointDetails.class);
/*     */   }
/*     */   
/*     */   public HttpRequest getRequest() {
/* 151 */     return getAttribute("http.request", HttpRequest.class);
/*     */   }
/*     */   
/*     */   public HttpResponse getResponse() {
/* 155 */     return getAttribute("http.response", HttpResponse.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/HttpCoreContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */