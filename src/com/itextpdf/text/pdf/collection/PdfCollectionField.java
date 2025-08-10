/*     */ package com.itextpdf.text.pdf.collection;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfBoolean;
/*     */ import com.itextpdf.text.pdf.PdfDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfCollectionField
/*     */   extends PdfDictionary
/*     */ {
/*     */   public static final int TEXT = 0;
/*     */   public static final int DATE = 1;
/*     */   public static final int NUMBER = 2;
/*     */   public static final int FILENAME = 3;
/*     */   public static final int DESC = 4;
/*     */   public static final int MODDATE = 5;
/*     */   public static final int CREATIONDATE = 6;
/*     */   public static final int SIZE = 7;
/*     */   protected int fieldType;
/*     */   
/*     */   public PdfCollectionField(String name, int type) {
/*  89 */     super(PdfName.COLLECTIONFIELD);
/*  90 */     put(PdfName.N, (PdfObject)new PdfString(name, "UnicodeBig"));
/*  91 */     this.fieldType = type;
/*  92 */     switch (type) {
/*     */       default:
/*  94 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.S);
/*     */         return;
/*     */       case 1:
/*  97 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.D);
/*     */         return;
/*     */       case 2:
/* 100 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.N);
/*     */         return;
/*     */       case 3:
/* 103 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.F);
/*     */         return;
/*     */       case 4:
/* 106 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.DESC);
/*     */         return;
/*     */       case 5:
/* 109 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.MODDATE);
/*     */         return;
/*     */       case 6:
/* 112 */         put(PdfName.SUBTYPE, (PdfObject)PdfName.CREATIONDATE); return;
/*     */       case 7:
/*     */         break;
/* 115 */     }  put(PdfName.SUBTYPE, (PdfObject)PdfName.SIZE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int i) {
/* 125 */     put(PdfName.O, (PdfObject)new PdfNumber(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 133 */     put(PdfName.V, (PdfObject)new PdfBoolean(visible));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEditable(boolean editable) {
/* 141 */     put(PdfName.E, (PdfObject)new PdfBoolean(editable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollectionItem() {
/* 148 */     switch (this.fieldType) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/* 152 */         return true;
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject getValue(String v) {
/* 163 */     switch (this.fieldType) {
/*     */       case 0:
/* 165 */         return (PdfObject)new PdfString(v, "UnicodeBig");
/*     */       case 1:
/* 167 */         return (PdfObject)new PdfDate(PdfDate.decode(v));
/*     */       case 2:
/* 169 */         return (PdfObject)new PdfNumber(v);
/*     */     } 
/* 171 */     throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.not.an.acceptable.value.for.the.field.2", new Object[] { v, get(PdfName.N).toString() }));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/collection/PdfCollectionField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */