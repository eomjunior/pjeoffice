/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDeveloperExtension
/*     */ {
/*  63 */   public static final PdfDeveloperExtension ADOBE_1_7_EXTENSIONLEVEL3 = new PdfDeveloperExtension(PdfName.ADBE, PdfWriter.PDF_VERSION_1_7, 3);
/*     */ 
/*     */   
/*  66 */   public static final PdfDeveloperExtension ESIC_1_7_EXTENSIONLEVEL2 = new PdfDeveloperExtension(PdfName.ESIC, PdfWriter.PDF_VERSION_1_7, 2);
/*     */ 
/*     */   
/*  69 */   public static final PdfDeveloperExtension ESIC_1_7_EXTENSIONLEVEL5 = new PdfDeveloperExtension(PdfName.ESIC, PdfWriter.PDF_VERSION_1_7, 5);
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfName prefix;
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfName baseversion;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int extensionLevel;
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDeveloperExtension(PdfName prefix, PdfName baseversion, int extensionLevel) {
/*  86 */     this.prefix = prefix;
/*  87 */     this.baseversion = baseversion;
/*  88 */     this.extensionLevel = extensionLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getPrefix() {
/*  96 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getBaseversion() {
/* 104 */     return this.baseversion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExtensionLevel() {
/* 112 */     return this.extensionLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getDeveloperExtensions() {
/* 121 */     PdfDictionary developerextensions = new PdfDictionary();
/* 122 */     developerextensions.put(PdfName.BASEVERSION, this.baseversion);
/* 123 */     developerextensions.put(PdfName.EXTENSIONLEVEL, new PdfNumber(this.extensionLevel));
/* 124 */     return developerextensions;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDeveloperExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */