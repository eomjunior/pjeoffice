/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfLiteral
/*     */   extends PdfObject
/*     */ {
/*     */   private long position;
/*     */   
/*     */   public PdfLiteral(String text) {
/*  58 */     super(0, text);
/*     */   }
/*     */   
/*     */   public PdfLiteral(byte[] b) {
/*  62 */     super(0, b);
/*     */   }
/*     */   
/*     */   public PdfLiteral(int size) {
/*  66 */     super(0, (byte[])null);
/*  67 */     this.bytes = new byte[size];
/*  68 */     Arrays.fill(this.bytes, (byte)32);
/*     */   }
/*     */   
/*     */   public PdfLiteral(int type, String text) {
/*  72 */     super(type, text);
/*     */   }
/*     */   
/*     */   public PdfLiteral(int type, byte[] b) {
/*  76 */     super(type, b);
/*     */   }
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/*  80 */     if (os instanceof OutputStreamCounter)
/*  81 */       this.position = ((OutputStreamCounter)os).getCounter(); 
/*  82 */     super.toPdf(writer, os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPosition() {
/*  90 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosLength() {
/*  98 */     if (this.bytes != null) {
/*  99 */       return this.bytes.length;
/*     */     }
/* 101 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLiteral.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */