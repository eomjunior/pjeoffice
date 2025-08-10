/*     */ package com.itextpdf.text.pdf.collection;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfTargetDictionary
/*     */   extends PdfDictionary
/*     */ {
/*     */   public PdfTargetDictionary(PdfTargetDictionary nested) {
/*  60 */     put(PdfName.R, (PdfObject)PdfName.P);
/*  61 */     if (nested != null) {
/*  62 */       setAdditionalPath(nested);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTargetDictionary(boolean child) {
/*  71 */     if (child) {
/*  72 */       put(PdfName.R, (PdfObject)PdfName.C);
/*     */     } else {
/*     */       
/*  75 */       put(PdfName.R, (PdfObject)PdfName.P);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmbeddedFileName(String target) {
/*  85 */     put(PdfName.N, (PdfObject)new PdfString(target, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileAttachmentPagename(String name) {
/*  95 */     put(PdfName.P, (PdfObject)new PdfString(name, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileAttachmentPage(int page) {
/* 105 */     put(PdfName.P, (PdfObject)new PdfNumber(page));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileAttachmentName(String name) {
/* 115 */     put(PdfName.A, (PdfObject)new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileAttachmentIndex(int annotation) {
/* 125 */     put(PdfName.A, (PdfObject)new PdfNumber(annotation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditionalPath(PdfTargetDictionary nested) {
/* 134 */     put(PdfName.T, (PdfObject)nested);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/collection/PdfTargetDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */