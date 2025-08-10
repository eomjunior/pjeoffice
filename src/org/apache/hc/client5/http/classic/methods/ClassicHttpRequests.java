/*     */ package org.apache.hc.client5.http.classic.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.hc.core5.http.Method;
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
/*     */ @Deprecated
/*     */ public final class ClassicHttpRequests
/*     */ {
/*     */   public static HttpUriRequest create(Method method, String uri) {
/*  56 */     return create(method, URI.create(uri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpUriRequest create(Method method, URI uri) {
/*  67 */     switch ((Method)Args.notNull(method, "method")) {
/*     */       case DELETE:
/*  69 */         return delete(uri);
/*     */       case GET:
/*  71 */         return get(uri);
/*     */       case HEAD:
/*  73 */         return head(uri);
/*     */       case OPTIONS:
/*  75 */         return options(uri);
/*     */       case PATCH:
/*  77 */         return patch(uri);
/*     */       case POST:
/*  79 */         return post(uri);
/*     */       case PUT:
/*  81 */         return put(uri);
/*     */       case TRACE:
/*  83 */         return trace(uri);
/*     */     } 
/*  85 */     throw new IllegalArgumentException(method.toString());
/*     */   }
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
/*     */   public static HttpUriRequest create(String method, String uri) {
/*  99 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
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
/*     */   public static HttpUriRequest create(String method, URI uri) {
/* 112 */     return create(Method.normalizedValueOf(method), uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest delete(String uri) {
/* 116 */     return delete(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest delete(URI uri) {
/* 120 */     return new HttpDelete(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest get(String uri) {
/* 124 */     return get(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest get(URI uri) {
/* 128 */     return new HttpGet(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest head(String uri) {
/* 132 */     return head(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest head(URI uri) {
/* 136 */     return new HttpHead(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest options(String uri) {
/* 140 */     return options(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest options(URI uri) {
/* 144 */     return new HttpOptions(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest patch(String uri) {
/* 148 */     return patch(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest patch(URI uri) {
/* 152 */     return new HttpPatch(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest post(String uri) {
/* 156 */     return post(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest post(URI uri) {
/* 160 */     return new HttpPost(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest put(String uri) {
/* 164 */     return put(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest put(URI uri) {
/* 168 */     return new HttpPut(uri);
/*     */   }
/*     */   
/*     */   public static HttpUriRequest trace(String uri) {
/* 172 */     return trace(URI.create(uri));
/*     */   }
/*     */   
/*     */   public static HttpUriRequest trace(URI uri) {
/* 176 */     return new HttpTrace(uri);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/ClassicHttpRequests.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */