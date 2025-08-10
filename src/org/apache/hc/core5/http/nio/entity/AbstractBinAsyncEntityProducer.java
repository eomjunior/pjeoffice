/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.StreamChannel;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public abstract class AbstractBinAsyncEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final int fragmentSizeHint;
/*     */   private final ByteBuffer byteBuffer;
/*     */   private final ContentType contentType;
/*     */   private volatile State state;
/*     */   
/*     */   enum State
/*     */   {
/*  49 */     ACTIVE, FLUSHING, END_STREAM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBinAsyncEntityProducer(int fragmentSizeHint, ContentType contentType) {
/*  58 */     this.fragmentSizeHint = (fragmentSizeHint >= 0) ? fragmentSizeHint : 0;
/*  59 */     this.byteBuffer = ByteBuffer.allocate(this.fragmentSizeHint);
/*  60 */     this.contentType = contentType;
/*  61 */     this.state = State.ACTIVE;
/*     */   }
/*     */   
/*     */   private void flush(StreamChannel<ByteBuffer> channel) throws IOException {
/*  65 */     if (this.byteBuffer.position() > 0) {
/*  66 */       this.byteBuffer.flip();
/*  67 */       channel.write(this.byteBuffer);
/*  68 */       this.byteBuffer.compact();
/*     */     } 
/*     */   }
/*     */   
/*     */   final int writeData(StreamChannel<ByteBuffer> channel, ByteBuffer src) throws IOException {
/*  73 */     int chunk = src.remaining();
/*  74 */     if (chunk == 0) {
/*  75 */       return 0;
/*     */     }
/*  77 */     if (chunk > this.fragmentSizeHint) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       flush(channel);
/*  83 */       if (this.byteBuffer.position() == 0) {
/*  84 */         return channel.write(src);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  91 */       if (this.byteBuffer.remaining() < chunk) {
/*  92 */         flush(channel);
/*     */       }
/*  94 */       if (this.byteBuffer.remaining() >= chunk) {
/*  95 */         this.byteBuffer.put(src);
/*  96 */         if (!this.byteBuffer.hasRemaining()) {
/*  97 */           flush(channel);
/*     */         }
/*  99 */         return chunk;
/*     */       } 
/*     */     } 
/* 102 */     return 0;
/*     */   }
/*     */   
/*     */   final void streamEnd(StreamChannel<ByteBuffer> channel) throws IOException {
/* 106 */     if (this.state == State.ACTIVE) {
/* 107 */       this.state = State.FLUSHING;
/* 108 */       flush(channel);
/* 109 */       if (this.byteBuffer.position() == 0) {
/* 110 */         this.state = State.END_STREAM;
/* 111 */         channel.endStream();
/*     */       } 
/*     */     } 
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
/*     */   public final String getContentType() {
/* 138 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 158 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/* 163 */     if (this.state == State.ACTIVE) {
/* 164 */       return availableData();
/*     */     }
/* 166 */     synchronized (this.byteBuffer) {
/* 167 */       return this.byteBuffer.position();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void produce(final DataStreamChannel channel) throws IOException {
/* 174 */     synchronized (this.byteBuffer) {
/* 175 */       if (this.state == State.ACTIVE) {
/* 176 */         produceData(new StreamChannel<ByteBuffer>()
/*     */             {
/*     */               public int write(ByteBuffer src) throws IOException
/*     */               {
/* 180 */                 Args.notNull(src, "Buffer");
/* 181 */                 synchronized (AbstractBinAsyncEntityProducer.this.byteBuffer) {
/* 182 */                   return AbstractBinAsyncEntityProducer.this.writeData((StreamChannel<ByteBuffer>)channel, src);
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/*     */               public void endStream() throws IOException {
/* 188 */                 synchronized (AbstractBinAsyncEntityProducer.this.byteBuffer) {
/* 189 */                   AbstractBinAsyncEntityProducer.this.streamEnd((StreamChannel<ByteBuffer>)channel);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 195 */       if (this.state == State.FLUSHING) {
/* 196 */         flush((StreamChannel<ByteBuffer>)channel);
/* 197 */         if (this.byteBuffer.position() == 0) {
/* 198 */           this.state = State.END_STREAM;
/* 199 */           channel.endStream();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 207 */     this.state = State.ACTIVE;
/*     */   }
/*     */   
/*     */   protected abstract int availableData();
/*     */   
/*     */   protected abstract void produceData(StreamChannel<ByteBuffer> paramStreamChannel) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractBinAsyncEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */