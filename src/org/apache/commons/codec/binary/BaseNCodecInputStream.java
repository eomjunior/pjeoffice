/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Objects;
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
/*     */ public class BaseNCodecInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final BaseNCodec baseNCodec;
/*     */   private final boolean doEncode;
/*  40 */   private final byte[] singleByte = new byte[1];
/*     */   
/*  42 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*     */   
/*     */   protected BaseNCodecInputStream(InputStream input, BaseNCodec baseNCodec, boolean doEncode) {
/*  45 */     super(input);
/*  46 */     this.doEncode = doEncode;
/*  47 */     this.baseNCodec = baseNCodec;
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
/*     */   public int available() throws IOException {
/*  64 */     return this.context.eof ? 0 : 1;
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
/*     */   public boolean isStrictDecoding() {
/*  78 */     return this.baseNCodec.isStrictDecoding();
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
/*     */   public synchronized void mark(int readLimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 101 */     return false;
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
/*     */   public int read() throws IOException {
/* 113 */     int r = read(this.singleByte, 0, 1);
/* 114 */     while (r == 0) {
/* 115 */       r = read(this.singleByte, 0, 1);
/*     */     }
/* 117 */     if (r > 0) {
/* 118 */       byte b = this.singleByte[0];
/* 119 */       return (b < 0) ? (256 + b) : b;
/*     */     } 
/* 121 */     return -1;
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
/*     */   public int read(byte[] array, int offset, int len) throws IOException {
/* 145 */     Objects.requireNonNull(array, "array");
/* 146 */     if (offset < 0 || len < 0)
/* 147 */       throw new IndexOutOfBoundsException(); 
/* 148 */     if (offset > array.length || offset + len > array.length)
/* 149 */       throw new IndexOutOfBoundsException(); 
/* 150 */     if (len == 0) {
/* 151 */       return 0;
/*     */     }
/* 153 */     int readLen = 0;
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
/* 170 */     while (readLen == 0) {
/* 171 */       if (!this.baseNCodec.hasData(this.context)) {
/* 172 */         byte[] buf = new byte[this.doEncode ? 4096 : 8192];
/* 173 */         int c = this.in.read(buf);
/* 174 */         if (this.doEncode) {
/* 175 */           this.baseNCodec.encode(buf, 0, c, this.context);
/*     */         } else {
/* 177 */           this.baseNCodec.decode(buf, 0, c, this.context);
/*     */         } 
/*     */       } 
/* 180 */       readLen = this.baseNCodec.readResults(array, offset, len, this.context);
/*     */     } 
/* 182 */     return readLen;
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
/*     */   public synchronized void reset() throws IOException {
/* 196 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 207 */     if (n < 0L) {
/* 208 */       throw new IllegalArgumentException("Negative skip length: " + n);
/*     */     }
/*     */ 
/*     */     
/* 212 */     byte[] b = new byte[512];
/* 213 */     long todo = n;
/*     */     
/* 215 */     while (todo > 0L) {
/* 216 */       int len = (int)Math.min(b.length, todo);
/* 217 */       len = read(b, 0, len);
/* 218 */       if (len == -1) {
/*     */         break;
/*     */       }
/* 221 */       todo -= len;
/*     */     } 
/*     */     
/* 224 */     return n - todo;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/BaseNCodecInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */