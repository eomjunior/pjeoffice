/*     */ package org.apache.hc.core5.http.support;
/*     */ 
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResponseBuilder<T>
/*     */   extends AbstractMessageBuilder<T>
/*     */ {
/*     */   private int status;
/*     */   
/*     */   protected AbstractResponseBuilder(int status) {
/*  45 */     this.status = status;
/*     */   }
/*     */   
/*     */   public int getStatus() {
/*  49 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(int status) {
/*  53 */     this.status = status;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> setVersion(ProtocolVersion version) {
/*  58 */     super.setVersion(version);
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> setHeaders(Header... headers) {
/*  64 */     super.setHeaders(headers);
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> addHeader(Header header) {
/*  70 */     super.addHeader(header);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> addHeader(String name, String value) {
/*  76 */     super.addHeader(name, value);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> removeHeader(Header header) {
/*  82 */     super.removeHeader(header);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> removeHeaders(String name) {
/*  88 */     super.removeHeaders(name);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> setHeader(Header header) {
/*  94 */     super.setHeader(header);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractResponseBuilder<T> setHeader(String name, String value) {
/* 100 */     super.setHeader(name, value);
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract T build();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/support/AbstractResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */