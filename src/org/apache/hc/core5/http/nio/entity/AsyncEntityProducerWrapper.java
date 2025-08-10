/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class AsyncEntityProducerWrapper
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final AsyncEntityProducer wrappedEntityProducer;
/*     */   
/*     */   public AsyncEntityProducerWrapper(AsyncEntityProducer wrappedEntityProducer) {
/*  53 */     this.wrappedEntityProducer = (AsyncEntityProducer)Args.notNull(wrappedEntityProducer, "Wrapped entity producer");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  58 */     return this.wrappedEntityProducer.isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  63 */     return this.wrappedEntityProducer.isChunked();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  68 */     return this.wrappedEntityProducer.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  73 */     return this.wrappedEntityProducer.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  78 */     return this.wrappedEntityProducer.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/*  83 */     return this.wrappedEntityProducer.getTrailerNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  88 */     return this.wrappedEntityProducer.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/*  93 */     this.wrappedEntityProducer.produce(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*  98 */     this.wrappedEntityProducer.failed(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 103 */     this.wrappedEntityProducer.releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return "Wrapper [" + this.wrappedEntityProducer + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AsyncEntityProducerWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */