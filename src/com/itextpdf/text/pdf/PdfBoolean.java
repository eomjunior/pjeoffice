/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfBoolean
/*     */   extends PdfObject
/*     */ {
/*  61 */   public static final PdfBoolean PDFTRUE = new PdfBoolean(true);
/*  62 */   public static final PdfBoolean PDFFALSE = new PdfBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TRUE = "true";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FALSE = "false";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean value;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfBoolean(boolean value) {
/*  83 */     super(1);
/*  84 */     if (value) {
/*  85 */       setContent("true");
/*     */     } else {
/*     */       
/*  88 */       setContent("false");
/*     */     } 
/*  90 */     this.value = value;
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
/*     */   public PdfBoolean(String value) throws BadPdfFormatException {
/* 102 */     super(1, value);
/* 103 */     if (value.equals("true")) {
/* 104 */       this.value = true;
/*     */     }
/* 106 */     else if (value.equals("false")) {
/* 107 */       this.value = false;
/*     */     } else {
/*     */       
/* 110 */       throw new BadPdfFormatException(MessageLocalization.getComposedMessage("the.value.has.to.be.true.of.false.instead.of.1", new Object[] { value }));
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
/*     */   public boolean booleanValue() {
/* 123 */     return this.value;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 127 */     return this.value ? "true" : "false";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfBoolean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */