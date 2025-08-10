/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfIndirectReference
/*     */   extends PdfObject
/*     */ {
/*     */   protected int number;
/*  71 */   protected int generation = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfIndirectReference() {
/*  76 */     super(0);
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
/*     */   PdfIndirectReference(int type, int number, int generation) {
/*  88 */     super(0, number + " " + generation + " R");
/*  89 */     this.number = number;
/*  90 */     this.generation = generation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfIndirectReference(int type, int number) {
/* 101 */     this(type, number, 0);
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
/*     */   public int getNumber() {
/* 113 */     return this.number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGeneration() {
/* 123 */     return this.generation;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 127 */     return this.number + " " + this.generation + " R";
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 132 */     os.write(PdfEncodings.convertToBytes(toString(), (String)null));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfIndirectReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */