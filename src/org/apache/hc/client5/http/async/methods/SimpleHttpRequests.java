/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.Method;
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
/*     */ public final class SimpleHttpRequests
/*     */ {
/*     */   public static SimpleHttpRequest create(String method, String uri) {
/*  53 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(String method, URI uri) {
/*  64 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest delete(String uri) {
/*  68 */     return delete(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest delete(URI uri) {
/*  72 */     return create(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest delete(HttpHost host, String path) {
/*  76 */     return create(Method.DELETE, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest get(String uri) {
/*  80 */     return get(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest get(URI uri) {
/*  84 */     return create(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest get(HttpHost host, String path) {
/*  88 */     return create(Method.GET, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest head(String uri) {
/*  92 */     return head(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest head(URI uri) {
/*  96 */     return create(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest head(HttpHost host, String path) {
/* 100 */     return create(Method.HEAD, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest options(String uri) {
/* 104 */     return options(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest options(URI uri) {
/* 108 */     return create(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest options(HttpHost host, String path) {
/* 112 */     return create(Method.OPTIONS, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest patch(String uri) {
/* 116 */     return patch(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest patch(URI uri) {
/* 120 */     return create(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest patch(HttpHost host, String path) {
/* 124 */     return create(Method.PATCH, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest post(String uri) {
/* 128 */     return post(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest post(URI uri) {
/* 132 */     return create(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest post(HttpHost host, String path) {
/* 136 */     return create(Method.POST, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest put(String uri) {
/* 140 */     return put(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest put(URI uri) {
/* 144 */     return create(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest put(HttpHost host, String path) {
/* 148 */     return create(Method.PUT, host, path);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest trace(String uri) {
/* 152 */     return trace(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest trace(URI uri) {
/* 156 */     return create(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleHttpRequest trace(HttpHost host, String path) {
/* 160 */     return create(Method.TRACE, host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(Method method, String uri) {
/* 170 */     return create(method, URI.create(uri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(Method method, URI uri) {
/* 180 */     return new SimpleHttpRequest(method, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleHttpRequest create(Method method, HttpHost host, String path) {
/* 191 */     return new SimpleHttpRequest(method, host, path);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleHttpRequests.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */