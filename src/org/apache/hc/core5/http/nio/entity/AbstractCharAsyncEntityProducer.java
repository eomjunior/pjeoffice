/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public abstract class AbstractCharAsyncEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final ByteBuffer bytebuf;
/*     */   private final int fragmentSizeHint;
/*     */   private final ContentType contentType;
/*     */   private final CharsetEncoder charsetEncoder;
/*     */   private volatile State state;
/*  54 */   private static final CharBuffer EMPTY = CharBuffer.wrap(new char[0]);
/*     */   
/*  56 */   enum State { ACTIVE, FLUSHING, END_STREAM; }
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
/*     */   public AbstractCharAsyncEntityProducer(int bufferSize, int fragmentSizeHint, ContentType contentType) {
/*  69 */     Args.positive(bufferSize, "Buffer size");
/*  70 */     this.fragmentSizeHint = (fragmentSizeHint >= 0) ? fragmentSizeHint : 0;
/*  71 */     this.bytebuf = ByteBuffer.allocate(bufferSize);
/*  72 */     this.contentType = contentType;
/*  73 */     Charset charset = ContentType.getCharset(contentType, StandardCharsets.US_ASCII);
/*  74 */     this.charsetEncoder = charset.newEncoder();
/*  75 */     this.state = State.ACTIVE;
/*     */   }
/*     */   
/*     */   private void flush(StreamChannel<ByteBuffer> channel) throws IOException {
/*  79 */     if (this.bytebuf.position() > 0) {
/*  80 */       this.bytebuf.flip();
/*  81 */       channel.write(this.bytebuf);
/*  82 */       this.bytebuf.compact();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final int writeData(StreamChannel<ByteBuffer> channel, CharBuffer src) throws IOException {
/*  88 */     int chunk = src.remaining();
/*  89 */     if (chunk == 0) {
/*  90 */       return 0;
/*     */     }
/*     */     
/*  93 */     int p = src.position();
/*  94 */     CoderResult result = this.charsetEncoder.encode(src, this.bytebuf, false);
/*  95 */     if (result.isError()) {
/*  96 */       result.throwException();
/*     */     }
/*     */     
/*  99 */     if (!this.bytebuf.hasRemaining() || this.bytebuf.position() >= this.fragmentSizeHint) {
/* 100 */       flush(channel);
/*     */     }
/*     */     
/* 103 */     return src.position() - p;
/*     */   }
/*     */   
/*     */   final void streamEnd(StreamChannel<ByteBuffer> channel) throws IOException {
/* 107 */     if (this.state == State.ACTIVE) {
/* 108 */       this.state = State.FLUSHING;
/* 109 */       if (!this.bytebuf.hasRemaining()) {
/* 110 */         flush(channel);
/*     */       }
/*     */       
/* 113 */       CoderResult result = this.charsetEncoder.encode(EMPTY, this.bytebuf, true);
/* 114 */       if (result.isError()) {
/* 115 */         result.throwException();
/*     */       }
/* 117 */       CoderResult result2 = this.charsetEncoder.flush(this.bytebuf);
/* 118 */       if (result2.isError()) {
/* 119 */         result.throwException();
/* 120 */       } else if (result.isUnderflow()) {
/* 121 */         flush(channel);
/* 122 */         if (this.bytebuf.position() == 0) {
/* 123 */           this.state = State.END_STREAM;
/* 124 */           channel.endStream();
/*     */         } 
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
/*     */   
/*     */   public final String getContentType() {
/* 153 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 163 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/* 178 */     if (this.state == State.ACTIVE) {
/* 179 */       return availableData();
/*     */     }
/* 181 */     synchronized (this.bytebuf) {
/* 182 */       return this.bytebuf.position();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void produce(final DataStreamChannel channel) throws IOException {
/* 189 */     synchronized (this.bytebuf) {
/* 190 */       if (this.state == State.ACTIVE) {
/* 191 */         produceData(new StreamChannel<CharBuffer>()
/*     */             {
/*     */               public int write(CharBuffer src) throws IOException
/*     */               {
/* 195 */                 Args.notNull(src, "Buffer");
/* 196 */                 synchronized (AbstractCharAsyncEntityProducer.this.bytebuf) {
/* 197 */                   return AbstractCharAsyncEntityProducer.this.writeData((StreamChannel<ByteBuffer>)channel, src);
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/*     */               public void endStream() throws IOException {
/* 203 */                 synchronized (AbstractCharAsyncEntityProducer.this.bytebuf) {
/* 204 */                   AbstractCharAsyncEntityProducer.this.streamEnd((StreamChannel<ByteBuffer>)channel);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 210 */       if (this.state == State.FLUSHING) {
/* 211 */         CoderResult result = this.charsetEncoder.flush(this.bytebuf);
/* 212 */         if (result.isError()) {
/* 213 */           result.throwException();
/* 214 */         } else if (result.isOverflow()) {
/* 215 */           flush((StreamChannel<ByteBuffer>)channel);
/* 216 */         } else if (result.isUnderflow()) {
/* 217 */           flush((StreamChannel<ByteBuffer>)channel);
/* 218 */           if (this.bytebuf.position() == 0) {
/* 219 */             this.state = State.END_STREAM;
/* 220 */             channel.endStream();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 231 */     this.state = State.ACTIVE;
/* 232 */     this.charsetEncoder.reset();
/*     */   }
/*     */   
/*     */   protected abstract int availableData();
/*     */   
/*     */   protected abstract void produceData(StreamChannel<CharBuffer> paramStreamChannel) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractCharAsyncEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */