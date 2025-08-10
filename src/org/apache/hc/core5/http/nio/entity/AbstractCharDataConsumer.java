/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCharDataConsumer
/*     */   implements AsyncDataConsumer
/*     */ {
/*     */   protected static final int DEF_BUF_SIZE = 8192;
/*  53 */   private static final ByteBuffer EMPTY_BIN = ByteBuffer.wrap(new byte[0]);
/*     */   
/*     */   private final CharBuffer charBuffer;
/*     */   
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   private volatile Charset charset;
/*     */   private volatile CharsetDecoder charsetDecoder;
/*     */   
/*     */   protected AbstractCharDataConsumer(int bufSize, CharCodingConfig charCodingConfig) {
/*  62 */     this.charBuffer = CharBuffer.allocate(Args.positive(bufSize, "Buffer size"));
/*  63 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   public AbstractCharDataConsumer() {
/*  67 */     this(8192, CharCodingConfig.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int capacityIncrement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void data(CharBuffer paramCharBuffer, boolean paramBoolean) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void completed() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setCharset(Charset charset) {
/*  91 */     this.charset = (charset != null) ? charset : this.charCodingConfig.getCharset();
/*  92 */     this.charsetDecoder = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/*  97 */     capacityChannel.update(capacityIncrement());
/*     */   }
/*     */   
/*     */   private void checkResult(CoderResult result) throws IOException {
/* 101 */     if (result.isError()) {
/* 102 */       result.throwException();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doDecode(boolean endOfStream) throws IOException {
/* 107 */     this.charBuffer.flip();
/* 108 */     data(this.charBuffer, endOfStream);
/* 109 */     this.charBuffer.clear();
/*     */   }
/*     */   
/*     */   private CharsetDecoder getCharsetDecoder() {
/* 113 */     if (this.charsetDecoder == null) {
/* 114 */       Charset charset = this.charset;
/* 115 */       if (charset == null) {
/* 116 */         charset = this.charCodingConfig.getCharset();
/*     */       }
/* 118 */       if (charset == null) {
/* 119 */         charset = StandardCharsets.US_ASCII;
/*     */       }
/* 121 */       this.charsetDecoder = charset.newDecoder();
/* 122 */       if (this.charCodingConfig.getMalformedInputAction() != null) {
/* 123 */         this.charsetDecoder.onMalformedInput(this.charCodingConfig.getMalformedInputAction());
/*     */       }
/* 125 */       if (this.charCodingConfig.getUnmappableInputAction() != null) {
/* 126 */         this.charsetDecoder.onUnmappableCharacter(this.charCodingConfig.getUnmappableInputAction());
/*     */       }
/*     */     } 
/* 129 */     return this.charsetDecoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 134 */     CharsetDecoder charsetDecoder = getCharsetDecoder();
/* 135 */     while (src.hasRemaining()) {
/* 136 */       checkResult(charsetDecoder.decode(src, this.charBuffer, false));
/* 137 */       doDecode(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 143 */     CharsetDecoder charsetDecoder = getCharsetDecoder();
/* 144 */     checkResult(charsetDecoder.decode(EMPTY_BIN, this.charBuffer, true));
/* 145 */     doDecode(false);
/* 146 */     checkResult(charsetDecoder.flush(this.charBuffer));
/* 147 */     doDecode(true);
/* 148 */     completed();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractCharDataConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */