/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public final class DefaultHttpProcessor
/*     */   implements HttpProcessor
/*     */ {
/*     */   private final HttpRequestInterceptor[] requestInterceptors;
/*     */   private final HttpResponseInterceptor[] responseInterceptors;
/*     */   
/*     */   public DefaultHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
/*  56 */     if (requestInterceptors != null) {
/*  57 */       int l = requestInterceptors.length;
/*  58 */       this.requestInterceptors = new HttpRequestInterceptor[l];
/*  59 */       System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
/*     */     } else {
/*  61 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  63 */     if (responseInterceptors != null) {
/*  64 */       int l = responseInterceptors.length;
/*  65 */       this.responseInterceptors = new HttpResponseInterceptor[l];
/*  66 */       System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
/*     */     } else {
/*  68 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpProcessor(List<HttpRequestInterceptor> requestInterceptors, List<HttpResponseInterceptor> responseInterceptors) {
/*  79 */     if (requestInterceptors != null) {
/*  80 */       int l = requestInterceptors.size();
/*  81 */       this.requestInterceptors = requestInterceptors.<HttpRequestInterceptor>toArray(new HttpRequestInterceptor[l]);
/*     */     } else {
/*  83 */       this.requestInterceptors = new HttpRequestInterceptor[0];
/*     */     } 
/*  85 */     if (responseInterceptors != null) {
/*  86 */       int l = responseInterceptors.size();
/*  87 */       this.responseInterceptors = responseInterceptors.<HttpResponseInterceptor>toArray(new HttpResponseInterceptor[l]);
/*     */     } else {
/*  89 */       this.responseInterceptors = new HttpResponseInterceptor[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   public DefaultHttpProcessor(HttpRequestInterceptor... requestInterceptors) {
/*  94 */     this(requestInterceptors, (HttpResponseInterceptor[])null);
/*     */   }
/*     */   
/*     */   public DefaultHttpProcessor(HttpResponseInterceptor... responseInterceptors) {
/*  98 */     this((HttpRequestInterceptor[])null, responseInterceptors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws IOException, HttpException {
/* 106 */     for (HttpRequestInterceptor requestInterceptor : this.requestInterceptors) {
/* 107 */       requestInterceptor.process(request, entity, context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws IOException, HttpException {
/* 116 */     for (HttpResponseInterceptor responseInterceptor : this.responseInterceptors)
/* 117 */       responseInterceptor.process(response, entity, context); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/DefaultHttpProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */