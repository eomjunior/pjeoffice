/*     */ package com.itextpdf.text.pdf.collection;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfCollection
/*     */   extends PdfDictionary
/*     */ {
/*     */   public static final int DETAILS = 0;
/*     */   public static final int TILE = 1;
/*     */   public static final int HIDDEN = 2;
/*     */   public static final int CUSTOM = 3;
/*     */   
/*     */   public PdfCollection(int type) {
/*  69 */     super(PdfName.COLLECTION);
/*  70 */     switch (type) {
/*     */       case 1:
/*  72 */         put(PdfName.VIEW, (PdfObject)PdfName.T);
/*     */         return;
/*     */       case 2:
/*  75 */         put(PdfName.VIEW, (PdfObject)PdfName.H);
/*     */         return;
/*     */       case 3:
/*  78 */         put(PdfName.VIEW, (PdfObject)PdfName.C);
/*     */         return;
/*     */     } 
/*  81 */     put(PdfName.VIEW, (PdfObject)PdfName.D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialDocument(String description) {
/*  91 */     put(PdfName.D, (PdfObject)new PdfString(description, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(PdfCollectionSchema schema) {
/*  99 */     put(PdfName.SCHEMA, (PdfObject)schema);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfCollectionSchema getSchema() {
/* 107 */     return (PdfCollectionSchema)get(PdfName.SCHEMA);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSort(PdfCollectionSort sort) {
/* 115 */     put(PdfName.SORT, (PdfObject)sort);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/collection/PdfCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */