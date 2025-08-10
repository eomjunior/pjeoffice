/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RequestEntityProxy
/*     */   implements HttpEntity
/*     */ {
/*     */   private final HttpEntity original;
/*     */   private boolean consumed;
/*     */   
/*     */   static void enhance(ClassicHttpRequest request) {
/*  43 */     HttpEntity entity = request.getEntity();
/*  44 */     if (entity != null && !entity.isRepeatable() && !isEnhanced(entity)) {
/*  45 */       request.setEntity(new RequestEntityProxy(entity));
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean isEnhanced(HttpEntity entity) {
/*  50 */     return entity instanceof RequestEntityProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RequestEntityProxy(HttpEntity original) {
/*  58 */     this.original = original;
/*     */   }
/*     */   
/*     */   public HttpEntity getOriginal() {
/*  62 */     return this.original;
/*     */   }
/*     */   
/*     */   public boolean isConsumed() {
/*  66 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  71 */     if (!this.consumed) {
/*  72 */       return true;
/*     */     }
/*  74 */     return this.original.isRepeatable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  80 */     return this.original.isChunked();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  85 */     return this.original.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  90 */     return this.original.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  95 */     return this.original.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, IllegalStateException {
/* 100 */     return this.original.getContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/* 105 */     this.consumed = true;
/* 106 */     this.original.writeTo(outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 111 */     return this.original.isStreaming();
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/* 116 */     return this.original.getTrailers();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 121 */     return this.original.getTrailerNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 126 */     this.original.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     StringBuilder sb = new StringBuilder("RequestEntityProxy{");
/* 132 */     sb.append(this.original);
/* 133 */     sb.append('}');
/* 134 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/RequestEntityProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */