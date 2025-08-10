/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.http.ContentType;
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
/*     */ public abstract class AbstractClassicEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final SharedOutputBuffer buffer;
/*     */   private final ContentType contentType;
/*     */   private final Executor executor;
/*     */   private final AtomicReference<State> state;
/*     */   private final AtomicReference<Exception> exception;
/*     */   
/*     */   private enum State
/*     */   {
/*  49 */     IDLE, ACTIVE, COMPLETED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractClassicEntityProducer(int initialBufferSize, ContentType contentType, Executor executor) {
/*  58 */     this.buffer = new SharedOutputBuffer(initialBufferSize);
/*  59 */     this.contentType = contentType;
/*  60 */     this.executor = (Executor)Args.notNull(executor, "Executor");
/*  61 */     this.state = new AtomicReference<>(State.IDLE);
/*  62 */     this.exception = new AtomicReference<>();
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
/*     */   public final boolean isRepeatable() {
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/*  80 */     return this.buffer.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void produce(DataStreamChannel channel) throws IOException {
/*  85 */     if (this.state.compareAndSet(State.IDLE, State.ACTIVE)) {
/*  86 */       this.executor.execute(() -> {
/*     */             try {
/*     */               produceData(this.contentType, new ContentOutputStream(this.buffer));
/*     */               this.buffer.writeCompleted();
/*  90 */             } catch (Exception ex) {
/*     */               this.buffer.abort();
/*     */             } finally {
/*     */               this.state.set(State.COMPLETED);
/*     */             } 
/*     */           });
/*     */     }
/*  97 */     this.buffer.flush(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/* 102 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getContentType() {
/* 107 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isChunked() {
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Set<String> getTrailerNames() {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 127 */     if (this.exception.compareAndSet(null, cause)) {
/* 128 */       releaseResources();
/*     */     }
/*     */   }
/*     */   
/*     */   public final Exception getException() {
/* 133 */     return this.exception.get();
/*     */   }
/*     */   
/*     */   public void releaseResources() {}
/*     */   
/*     */   protected abstract void produceData(ContentType paramContentType, OutputStream paramOutputStream) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/AbstractClassicEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */