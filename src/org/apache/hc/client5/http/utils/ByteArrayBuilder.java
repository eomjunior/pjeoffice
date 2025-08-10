/*     */ package org.apache.hc.client5.http.utils;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public final class ByteArrayBuilder
/*     */ {
/*     */   private CharsetEncoder charsetEncoder;
/*     */   private ByteBuffer buffer;
/*     */   
/*     */   public ByteArrayBuilder() {}
/*     */   
/*     */   public ByteArrayBuilder(int initialCapacity) {
/*  52 */     this.buffer = ByteBuffer.allocate(initialCapacity);
/*     */   }
/*     */   
/*     */   public int capacity() {
/*  56 */     return (this.buffer != null) ? this.buffer.capacity() : 0;
/*     */   }
/*     */   
/*     */   static ByteBuffer ensureFreeCapacity(ByteBuffer buffer, int capacity) {
/*  60 */     if (buffer == null) {
/*  61 */       return ByteBuffer.allocate(capacity);
/*     */     }
/*  63 */     if (buffer.remaining() < capacity) {
/*  64 */       ByteBuffer newBuffer = ByteBuffer.allocate(buffer.position() + capacity);
/*  65 */       buffer.flip();
/*  66 */       newBuffer.put(buffer);
/*  67 */       return newBuffer;
/*     */     } 
/*  69 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ByteBuffer encode(ByteBuffer buffer, CharBuffer in, CharsetEncoder encoder) throws CharacterCodingException {
/*  75 */     int capacity = (int)(in.remaining() * encoder.averageBytesPerChar());
/*  76 */     ByteBuffer out = ensureFreeCapacity(buffer, capacity);
/*  77 */     while (in.hasRemaining()) {
/*  78 */       CoderResult result = encoder.encode(in, out, true);
/*  79 */       if (result.isError()) {
/*  80 */         result.throwException();
/*     */       }
/*  82 */       if (result.isUnderflow()) {
/*  83 */         result = encoder.flush(out);
/*     */       }
/*  85 */       if (result.isUnderflow()) {
/*     */         break;
/*     */       }
/*  88 */       if (result.isOverflow()) {
/*  89 */         out = ensureFreeCapacity(out, capacity);
/*     */       }
/*     */     } 
/*  92 */     return out;
/*     */   }
/*     */   
/*     */   public void ensureFreeCapacity(int freeCapacity) {
/*  96 */     this.buffer = ensureFreeCapacity(this.buffer, freeCapacity);
/*     */   }
/*     */   
/*     */   private void doAppend(CharBuffer charBuffer) {
/* 100 */     if (this.charsetEncoder == null) {
/* 101 */       this
/*     */         
/* 103 */         .charsetEncoder = StandardCharsets.US_ASCII.newEncoder().onMalformedInput(CodingErrorAction.IGNORE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */     }
/* 105 */     this.charsetEncoder.reset();
/*     */     try {
/* 107 */       this.buffer = encode(this.buffer, charBuffer, this.charsetEncoder);
/* 108 */     } catch (CharacterCodingException ex) {
/*     */       
/* 110 */       throw new IllegalStateException("Unexpected character coding error", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder charset(Charset charset) {
/* 115 */     if (charset == null) {
/* 116 */       this.charsetEncoder = null;
/*     */     } else {
/* 118 */       this
/*     */         
/* 120 */         .charsetEncoder = charset.newEncoder().onMalformedInput(CodingErrorAction.IGNORE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */     } 
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(byte[] b, int off, int len) {
/* 126 */     if (b == null) {
/* 127 */       return this;
/*     */     }
/* 129 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 131 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 133 */     ensureFreeCapacity(len);
/* 134 */     this.buffer.put(b, off, len);
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(byte[] b) {
/* 139 */     if (b == null) {
/* 140 */       return this;
/*     */     }
/* 142 */     return append(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(CharBuffer charBuffer) {
/* 146 */     if (charBuffer == null) {
/* 147 */       return this;
/*     */     }
/* 149 */     doAppend(charBuffer);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(char[] b, int off, int len) {
/* 154 */     if (b == null) {
/* 155 */       return this;
/*     */     }
/* 157 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 159 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 161 */     return append(CharBuffer.wrap(b, off, len));
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(char[] b) {
/* 165 */     if (b == null) {
/* 166 */       return this;
/*     */     }
/* 168 */     return append(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public ByteArrayBuilder append(String s) {
/* 172 */     if (s == null) {
/* 173 */       return this;
/*     */     }
/* 175 */     return append(CharBuffer.wrap(s));
/*     */   }
/*     */   
/*     */   public ByteBuffer toByteBuffer() {
/* 179 */     return (this.buffer != null) ? this.buffer.duplicate() : ByteBuffer.allocate(0);
/*     */   }
/*     */   
/*     */   public byte[] toByteArray() {
/* 183 */     if (this.buffer == null) {
/* 184 */       return new byte[0];
/*     */     }
/* 186 */     this.buffer.flip();
/* 187 */     byte[] b = new byte[this.buffer.remaining()];
/* 188 */     this.buffer.get(b);
/* 189 */     this.buffer.clear();
/* 190 */     return b;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 194 */     if (this.charsetEncoder != null) {
/* 195 */       this.charsetEncoder.reset();
/*     */     }
/* 197 */     if (this.buffer != null) {
/* 198 */       this.buffer.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 204 */     return (this.buffer != null) ? this.buffer.toString() : "null";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/utils/ByteArrayBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */