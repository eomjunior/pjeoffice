/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
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
/*     */ public abstract class AbstractClassicEntityConsumer<T>
/*     */   implements AsyncEntityConsumer<T>
/*     */ {
/*     */   private final Executor executor;
/*     */   private final SharedInputBuffer buffer;
/*     */   private final AtomicReference<State> state;
/*     */   private final AtomicReference<T> resultRef;
/*     */   private final AtomicReference<Exception> exceptionRef;
/*     */   
/*     */   private enum State
/*     */   {
/*  58 */     IDLE, ACTIVE, COMPLETED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractClassicEntityConsumer(int initialBufferSize, Executor executor) {
/*  67 */     this.executor = (Executor)Args.notNull(executor, "Executor");
/*  68 */     this.buffer = new SharedInputBuffer(initialBufferSize);
/*  69 */     this.state = new AtomicReference<>(State.IDLE);
/*  70 */     this.resultRef = new AtomicReference<>();
/*  71 */     this.exceptionRef = new AtomicReference<>();
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
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/*  85 */     this.buffer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws HttpException, IOException {
/*     */     ContentType contentType;
/*     */     try {
/*  92 */       contentType = ContentType.parse(entityDetails.getContentType());
/*  93 */     } catch (UnsupportedCharsetException ex) {
/*  94 */       throw new UnsupportedEncodingException(ex.getMessage());
/*     */     } 
/*  96 */     if (this.state.compareAndSet(State.IDLE, State.ACTIVE)) {
/*  97 */       this.executor.execute(() -> {
/*     */             try {
/*     */               T result = consumeData(contentType, new ContentInputStream(this.buffer));
/*     */               this.resultRef.set(result);
/*     */               resultCallback.completed(result);
/* 102 */             } catch (Exception ex) {
/*     */               this.buffer.abort();
/*     */               resultCallback.failed(ex);
/*     */             } finally {
/*     */               this.state.set(State.COMPLETED);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 114 */     this.buffer.fill(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 119 */     this.buffer.markEndStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 124 */     if (this.exceptionRef.compareAndSet(null, cause)) {
/* 125 */       releaseResources();
/*     */     }
/*     */   }
/*     */   
/*     */   public final Exception getException() {
/* 130 */     return this.exceptionRef.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final T getContent() {
/* 135 */     return this.resultRef.get();
/*     */   }
/*     */   
/*     */   public void releaseResources() {}
/*     */   
/*     */   protected abstract T consumeData(ContentType paramContentType, InputStream paramInputStream) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/AbstractClassicEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */