/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
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
/*     */ @Deprecated
/*     */ public final class BasicHttpRequests
/*     */ {
/*     */   public static BasicHttpRequest create(String method, String uri) {
/*  54 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicHttpRequest create(String method, URI uri) {
/*  65 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest delete(String uri) {
/*  69 */     return delete(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest delete(URI uri) {
/*  73 */     return create(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest delete(HttpHost host, String path) {
/*  77 */     return create(Method.DELETE, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest get(String uri) {
/*  81 */     return get(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest get(URI uri) {
/*  85 */     return create(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest get(HttpHost host, String path) {
/*  89 */     return create(Method.GET, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest head(String uri) {
/*  93 */     return head(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest head(URI uri) {
/*  97 */     return create(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest head(HttpHost host, String path) {
/* 101 */     return create(Method.HEAD, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest options(String uri) {
/* 105 */     return options(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest options(URI uri) {
/* 109 */     return create(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest options(HttpHost host, String path) {
/* 113 */     return create(Method.OPTIONS, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest patch(String uri) {
/* 117 */     return patch(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest patch(URI uri) {
/* 121 */     return create(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest patch(HttpHost host, String path) {
/* 125 */     return create(Method.PATCH, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest post(String uri) {
/* 129 */     return post(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest post(URI uri) {
/* 133 */     return create(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest post(HttpHost host, String path) {
/* 137 */     return create(Method.POST, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest put(String uri) {
/* 141 */     return put(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest put(URI uri) {
/* 145 */     return create(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest put(HttpHost host, String path) {
/* 149 */     return create(Method.PUT, host, path);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest trace(String uri) {
/* 153 */     return trace(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest trace(URI uri) {
/* 157 */     return create(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static BasicHttpRequest trace(HttpHost host, String path) {
/* 161 */     return create(Method.TRACE, host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicHttpRequest create(Method method, String uri) {
/* 171 */     return create(method, URI.create(uri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicHttpRequest create(Method method, URI uri) {
/* 181 */     return new BasicHttpRequest(method, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicHttpRequest create(Method method, HttpHost host, String path) {
/* 192 */     return new BasicHttpRequest(method, host, path);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/BasicHttpRequests.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */