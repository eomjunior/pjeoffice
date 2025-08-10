/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.net.URIAuthority;
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
/*     */ public final class SimpleHttpRequest
/*     */   extends ConfigurableHttpRequest
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private SimpleBody body;
/*     */   
/*     */   public static SimpleHttpRequest create(String method, String uri) {
/*  64 */     return new SimpleHttpRequest(method, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(String method, URI uri) {
/*  71 */     return new SimpleHttpRequest(method, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(Method method, URI uri) {
/*  78 */     return new SimpleHttpRequest(method, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(Method method, HttpHost host, String path) {
/*  85 */     return new SimpleHttpRequest(method, host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(String method, String scheme, URIAuthority authority, String path) {
/*  92 */     return new SimpleHttpRequest(method, scheme, authority, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static SimpleHttpRequest copy(HttpRequest original) {
/* 100 */     Args.notNull(original, "HTTP request");
/* 101 */     SimpleHttpRequest copy = new SimpleHttpRequest(original.getMethod(), original.getRequestUri());
/* 102 */     copy.setVersion(original.getVersion());
/* 103 */     for (Iterator<Header> it = original.headerIterator(); it.hasNext();) {
/* 104 */       copy.addHeader(it.next());
/*     */     }
/* 106 */     copy.setScheme(original.getScheme());
/* 107 */     copy.setAuthority(original.getAuthority());
/* 108 */     return copy;
/*     */   }
/*     */   
/*     */   public SimpleHttpRequest(String method, String path) {
/* 112 */     super(method, path);
/*     */   }
/*     */   
/*     */   public SimpleHttpRequest(String method, HttpHost host, String path) {
/* 116 */     super(method, host, path);
/*     */   }
/*     */   
/*     */   public SimpleHttpRequest(String method, URI requestUri) {
/* 120 */     super(method, requestUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHttpRequest(Method method, URI requestUri) {
/* 127 */     this(method.name(), requestUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHttpRequest(Method method, HttpHost host, String path) {
/* 134 */     this(method.name(), host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleHttpRequest(String method, String scheme, URIAuthority authority, String path) {
/* 141 */     super(method, scheme, authority, path);
/*     */   }
/*     */   
/*     */   public void setBody(SimpleBody body) {
/* 145 */     this.body = body;
/*     */   }
/*     */   
/*     */   public void setBody(byte[] bodyBytes, ContentType contentType) {
/* 149 */     this.body = SimpleBody.create(bodyBytes, contentType);
/*     */   }
/*     */   
/*     */   public void setBody(String bodyText, ContentType contentType) {
/* 153 */     this.body = SimpleBody.create(bodyText, contentType);
/*     */   }
/*     */   
/*     */   public SimpleBody getBody() {
/* 157 */     return this.body;
/*     */   }
/*     */   
/*     */   public ContentType getContentType() {
/* 161 */     return (this.body != null) ? this.body.getContentType() : null;
/*     */   }
/*     */   
/*     */   public String getBodyText() {
/* 165 */     return (this.body != null) ? this.body.getBodyText() : null;
/*     */   }
/*     */   
/*     */   public byte[] getBodyBytes() {
/* 169 */     return (this.body != null) ? this.body.getBodyBytes() : null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */