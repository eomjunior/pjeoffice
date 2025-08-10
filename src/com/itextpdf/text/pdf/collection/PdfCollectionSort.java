/*     */ package com.itextpdf.text.pdf.collection;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfBoolean;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfCollectionSort
/*     */   extends PdfDictionary
/*     */ {
/*     */   public PdfCollectionSort(String key) {
/*  60 */     super(PdfName.COLLECTIONSORT);
/*  61 */     put(PdfName.S, (PdfObject)new PdfName(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfCollectionSort(String[] keys) {
/*  69 */     super(PdfName.COLLECTIONSORT);
/*  70 */     PdfArray array = new PdfArray();
/*  71 */     for (int i = 0; i < keys.length; i++) {
/*  72 */       array.add((PdfObject)new PdfName(keys[i]));
/*     */     }
/*  74 */     put(PdfName.S, (PdfObject)array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSortOrder(boolean ascending) {
/*  82 */     PdfObject o = get(PdfName.S);
/*  83 */     if (o instanceof PdfName) {
/*  84 */       put(PdfName.A, (PdfObject)new PdfBoolean(ascending));
/*     */     } else {
/*     */       
/*  87 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.to.define.a.boolean.array.for.this.collection.sort.dictionary", new Object[0]));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSortOrder(boolean[] ascending) {
/*  96 */     PdfObject o = get(PdfName.S);
/*  97 */     if (o instanceof PdfArray) {
/*  98 */       if (((PdfArray)o).size() != ascending.length) {
/*  99 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.booleans.in.this.array.doesn.t.correspond.with.the.number.of.fields", new Object[0]));
/*     */       }
/* 101 */       PdfArray array = new PdfArray();
/* 102 */       for (int i = 0; i < ascending.length; i++) {
/* 103 */         array.add((PdfObject)new PdfBoolean(ascending[i]));
/*     */       }
/* 105 */       put(PdfName.A, (PdfObject)array);
/*     */     } else {
/*     */       
/* 108 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.need.a.single.boolean.for.this.collection.sort.dictionary", new Object[0]));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/collection/PdfCollectionSort.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */