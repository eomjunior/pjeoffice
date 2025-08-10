/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Arrays;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class ReaderInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final Reader reader;
/*     */   private final CharsetEncoder encoder;
/*  53 */   private final byte[] singleByte = new byte[1];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharBuffer charBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer byteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean endOfInput;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean draining;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doneFlushing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
/*  85 */     this(reader, charset
/*     */ 
/*     */         
/*  88 */         .newEncoder()
/*  89 */         .onMalformedInput(CodingErrorAction.REPLACE)
/*  90 */         .onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
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
/*     */   ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
/* 104 */     this.reader = (Reader)Preconditions.checkNotNull(reader);
/* 105 */     this.encoder = (CharsetEncoder)Preconditions.checkNotNull(encoder);
/* 106 */     Preconditions.checkArgument((bufferSize > 0), "bufferSize must be positive: %s", bufferSize);
/* 107 */     encoder.reset();
/*     */     
/* 109 */     this.charBuffer = CharBuffer.allocate(bufferSize);
/* 110 */     Java8Compatibility.flip(this.charBuffer);
/*     */     
/* 112 */     this.byteBuffer = ByteBuffer.allocate(bufferSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 117 */     this.reader.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 122 */     return (read(this.singleByte) == 1) ? UnsignedBytes.toInt(this.singleByte[0]) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*     */     CoderResult result;
/* 130 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/* 131 */     if (len == 0) {
/* 132 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 136 */     int totalBytesRead = 0;
/* 137 */     boolean doneEncoding = this.endOfInput;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     label39: while (true) {
/* 143 */       if (this.draining) {
/* 144 */         totalBytesRead += drain(b, off + totalBytesRead, len - totalBytesRead);
/* 145 */         if (totalBytesRead == len || this.doneFlushing) {
/* 146 */           return (totalBytesRead > 0) ? totalBytesRead : -1;
/*     */         }
/* 148 */         this.draining = false;
/* 149 */         Java8Compatibility.clear(this.byteBuffer);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true)
/* 156 */       { if (this.doneFlushing) {
/* 157 */           result = CoderResult.UNDERFLOW;
/* 158 */         } else if (doneEncoding) {
/* 159 */           result = this.encoder.flush(this.byteBuffer);
/*     */         } else {
/* 161 */           result = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
/*     */         } 
/*     */         
/* 164 */         if (result.isOverflow()) {
/*     */           
/* 166 */           startDraining(true); continue label39;
/*     */         } 
/* 168 */         if (result.isUnderflow()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 173 */           if (doneEncoding) {
/* 174 */             this.doneFlushing = true;
/* 175 */             startDraining(false); continue label39;
/*     */           } 
/* 177 */           if (this.endOfInput) {
/* 178 */             doneEncoding = true; continue;
/*     */           } 
/* 180 */           readMoreChars(); continue;
/*     */         } 
/* 182 */         if (result.isError())
/*     */           break;  }  break;
/* 184 */     }  result.throwException();
/* 185 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CharBuffer grow(CharBuffer buf) {
/* 193 */     char[] copy = Arrays.copyOf(buf.array(), buf.capacity() * 2);
/* 194 */     CharBuffer bigger = CharBuffer.wrap(copy);
/* 195 */     Java8Compatibility.position(bigger, buf.position());
/* 196 */     Java8Compatibility.limit(bigger, buf.limit());
/* 197 */     return bigger;
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
/*     */   private void readMoreChars() throws IOException {
/* 210 */     if (availableCapacity(this.charBuffer) == 0) {
/* 211 */       if (this.charBuffer.position() > 0) {
/*     */         
/* 213 */         Java8Compatibility.flip(this.charBuffer.compact());
/*     */       } else {
/*     */         
/* 216 */         this.charBuffer = grow(this.charBuffer);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 221 */     int limit = this.charBuffer.limit();
/* 222 */     int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
/* 223 */     if (numChars == -1) {
/* 224 */       this.endOfInput = true;
/*     */     } else {
/* 226 */       Java8Compatibility.limit(this.charBuffer, limit + numChars);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int availableCapacity(Buffer buffer) {
/* 232 */     return buffer.capacity() - buffer.limit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startDraining(boolean overflow) {
/* 241 */     Java8Compatibility.flip(this.byteBuffer);
/* 242 */     if (overflow && this.byteBuffer.remaining() == 0) {
/* 243 */       this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
/*     */     } else {
/* 245 */       this.draining = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int drain(byte[] b, int off, int len) {
/* 254 */     int remaining = Math.min(len, this.byteBuffer.remaining());
/* 255 */     this.byteBuffer.get(b, off, remaining);
/* 256 */     return remaining;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/ReaderInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */