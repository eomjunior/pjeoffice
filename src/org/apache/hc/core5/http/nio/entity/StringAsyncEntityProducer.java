/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.nio.StreamChannel;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringAsyncEntityProducer
/*     */   extends AbstractCharAsyncEntityProducer
/*     */ {
/*     */   private final CharBuffer content;
/*     */   private final AtomicReference<Exception> exception;
/*     */   
/*     */   public StringAsyncEntityProducer(CharSequence content, int bufferSize, int fragmentSizeHint, ContentType contentType) {
/*  54 */     super(bufferSize, fragmentSizeHint, contentType);
/*  55 */     Args.notNull(content, "Content");
/*  56 */     this.content = CharBuffer.wrap(content);
/*  57 */     this.exception = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public StringAsyncEntityProducer(CharSequence content, int bufferSize, ContentType contentType) {
/*  61 */     this(content, bufferSize, -1, contentType);
/*     */   }
/*     */   
/*     */   public StringAsyncEntityProducer(CharSequence content, ContentType contentType) {
/*  65 */     this(content, 4096, contentType);
/*     */   }
/*     */   
/*     */   public StringAsyncEntityProducer(CharSequence content) {
/*  69 */     this(content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  74 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int availableData() {
/*  79 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void produceData(StreamChannel<CharBuffer> channel) throws IOException {
/*  84 */     Asserts.notNull(channel, "Channel");
/*  85 */     channel.write(this.content);
/*  86 */     if (!this.content.hasRemaining()) {
/*  87 */       channel.endStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*  93 */     if (this.exception.compareAndSet(null, cause)) {
/*  94 */       releaseResources();
/*     */     }
/*     */   }
/*     */   
/*     */   public Exception getException() {
/*  99 */     return this.exception.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 104 */     this.content.clear();
/* 105 */     super.releaseResources();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/StringAsyncEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */