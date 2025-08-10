/*     */ package com.itextpdf.text.pdf.codec.wmf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputMeta
/*     */ {
/*     */   InputStream in;
/*     */   int length;
/*     */   
/*     */   public InputMeta(InputStream in) {
/*  58 */     this.in = in;
/*     */   }
/*     */   
/*     */   public int readWord() throws IOException {
/*  62 */     this.length += 2;
/*  63 */     int k1 = this.in.read();
/*  64 */     if (k1 < 0)
/*  65 */       return 0; 
/*  66 */     return k1 + (this.in.read() << 8) & 0xFFFF;
/*     */   }
/*     */   
/*     */   public int readShort() throws IOException {
/*  70 */     int k = readWord();
/*  71 */     if (k > 32767)
/*  72 */       k -= 65536; 
/*  73 */     return k;
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/*  77 */     this.length += 4;
/*  78 */     int k1 = this.in.read();
/*  79 */     if (k1 < 0)
/*  80 */       return 0; 
/*  81 */     int k2 = this.in.read() << 8;
/*  82 */     int k3 = this.in.read() << 16;
/*  83 */     return k1 + k2 + k3 + (this.in.read() << 24);
/*     */   }
/*     */   
/*     */   public int readByte() throws IOException {
/*  87 */     this.length++;
/*  88 */     return this.in.read() & 0xFF;
/*     */   }
/*     */   
/*     */   public void skip(int len) throws IOException {
/*  92 */     this.length += len;
/*  93 */     Utilities.skip(this.in, len);
/*     */   }
/*     */   
/*     */   public int getLength() {
/*  97 */     return this.length;
/*     */   }
/*     */   
/*     */   public BaseColor readColor() throws IOException {
/* 101 */     int red = readByte();
/* 102 */     int green = readByte();
/* 103 */     int blue = readByte();
/* 104 */     readByte();
/* 105 */     return new BaseColor(red, green, blue);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/InputMeta.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */