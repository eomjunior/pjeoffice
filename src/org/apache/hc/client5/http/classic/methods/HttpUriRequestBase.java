/*     */ package org.apache.hc.client5.http.classic.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.atomic.AtomicMarkableReference;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpUriRequestBase
/*     */   extends BasicClassicHttpRequest
/*     */   implements HttpUriRequest, CancellableDependency
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final AtomicMarkableReference<Cancellable> cancellableRef;
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   public HttpUriRequestBase(String method, URI requestUri) {
/*  45 */     super(method, requestUri);
/*  46 */     this.cancellableRef = new AtomicMarkableReference<>(null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/*  51 */     while (!this.cancellableRef.isMarked()) {
/*  52 */       Cancellable actualCancellable = this.cancellableRef.getReference();
/*  53 */       if (this.cancellableRef.compareAndSet(actualCancellable, actualCancellable, false, true)) {
/*  54 */         if (actualCancellable != null) {
/*  55 */           actualCancellable.cancel();
/*     */         }
/*  57 */         return true;
/*     */       } 
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  65 */     return this.cancellableRef.isMarked();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDependency(Cancellable cancellable) {
/*  73 */     Cancellable actualCancellable = this.cancellableRef.getReference();
/*  74 */     if (!this.cancellableRef.compareAndSet(actualCancellable, cancellable, false, false)) {
/*  75 */       cancellable.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*     */     boolean marked;
/*     */     Cancellable actualCancellable;
/*     */     do {
/*  86 */       marked = this.cancellableRef.isMarked();
/*  87 */       actualCancellable = this.cancellableRef.getReference();
/*  88 */       if (actualCancellable == null)
/*  89 */         continue;  actualCancellable.cancel();
/*     */     }
/*  91 */     while (!this.cancellableRef.compareAndSet(actualCancellable, null, marked, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void abort() throws UnsupportedOperationException {
/*  99 */     cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAborted() {
/* 104 */     return isCancelled();
/*     */   }
/*     */   
/*     */   public void setConfig(RequestConfig requestConfig) {
/* 108 */     this.requestConfig = requestConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestConfig getConfig() {
/* 113 */     return this.requestConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     sb.append(getMethod()).append(" ").append(getRequestUri());
/* 120 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpUriRequestBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */