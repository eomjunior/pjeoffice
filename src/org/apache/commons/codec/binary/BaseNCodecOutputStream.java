/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseNCodecOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*     */   private final boolean doEncode;
/*     */   private final BaseNCodec baseNCodec;
/*  46 */   private final byte[] singleByte = new byte[1];
/*     */   
/*  48 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseNCodecOutputStream(OutputStream output, BaseNCodec basedCodec, boolean doEncode) {
/*  58 */     super(output);
/*  59 */     this.baseNCodec = basedCodec;
/*  60 */     this.doEncode = doEncode;
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
/*     */   public void close() throws IOException {
/*  77 */     eof();
/*  78 */     flush();
/*  79 */     this.out.close();
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
/*     */   public void eof() throws IOException {
/*  91 */     if (this.doEncode) {
/*  92 */       this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
/*     */     } else {
/*  94 */       this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
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
/*     */   public void flush() throws IOException {
/* 106 */     flush(true);
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
/*     */   private void flush(boolean propagate) throws IOException {
/* 119 */     int avail = this.baseNCodec.available(this.context);
/* 120 */     if (avail > 0) {
/* 121 */       byte[] buf = new byte[avail];
/* 122 */       int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
/* 123 */       if (c > 0) {
/* 124 */         this.out.write(buf, 0, c);
/*     */       }
/*     */     } 
/* 127 */     if (propagate) {
/* 128 */       this.out.flush();
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
/*     */   public boolean isStrictDecoding() {
/* 143 */     return this.baseNCodec.isStrictDecoding();
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
/*     */   public void write(byte[] array, int offset, int len) throws IOException {
/* 166 */     Objects.requireNonNull(array, "array");
/* 167 */     if (offset < 0 || len < 0)
/* 168 */       throw new IndexOutOfBoundsException(); 
/* 169 */     if (offset > array.length || offset + len > array.length)
/* 170 */       throw new IndexOutOfBoundsException(); 
/* 171 */     if (len > 0) {
/* 172 */       if (this.doEncode) {
/* 173 */         this.baseNCodec.encode(array, offset, len, this.context);
/*     */       } else {
/* 175 */         this.baseNCodec.decode(array, offset, len, this.context);
/*     */       } 
/* 177 */       flush(false);
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
/*     */   public void write(int i) throws IOException {
/* 191 */     this.singleByte[0] = (byte)i;
/* 192 */     write(this.singleByte, 0, 1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/BaseNCodecOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */