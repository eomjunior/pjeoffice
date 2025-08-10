/*     */ package com.itextpdf.text.pdf.collection;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfDate;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.util.Calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfCollectionItem
/*     */   extends PdfDictionary
/*     */ {
/*     */   PdfCollectionSchema schema;
/*     */   
/*     */   public PdfCollectionItem(PdfCollectionSchema schema) {
/*  65 */     super(PdfName.COLLECTIONITEM);
/*  66 */     this.schema = schema;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, String value) {
/*  74 */     PdfName fieldname = new PdfName(key);
/*  75 */     PdfCollectionField field = (PdfCollectionField)this.schema.get(fieldname);
/*  76 */     put(fieldname, field.getValue(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, PdfString value) {
/*  84 */     PdfName fieldname = new PdfName(key);
/*  85 */     PdfCollectionField field = (PdfCollectionField)this.schema.get(fieldname);
/*  86 */     if (field.fieldType == 0) {
/*  87 */       put(fieldname, (PdfObject)value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, PdfDate d) {
/*  96 */     PdfName fieldname = new PdfName(key);
/*  97 */     PdfCollectionField field = (PdfCollectionField)this.schema.get(fieldname);
/*  98 */     if (field.fieldType == 1) {
/*  99 */       put(fieldname, (PdfObject)d);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, PdfNumber n) {
/* 108 */     PdfName fieldname = new PdfName(key);
/* 109 */     PdfCollectionField field = (PdfCollectionField)this.schema.get(fieldname);
/* 110 */     if (field.fieldType == 2) {
/* 111 */       put(fieldname, (PdfObject)n);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, Calendar c) {
/* 120 */     addItem(key, new PdfDate(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, int i) {
/* 128 */     addItem(key, new PdfNumber(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, float f) {
/* 136 */     addItem(key, new PdfNumber(f));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addItem(String key, double d) {
/* 144 */     addItem(key, new PdfNumber(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String key, String prefix) {
/* 153 */     PdfName fieldname = new PdfName(key);
/* 154 */     PdfObject o = get(fieldname);
/* 155 */     if (o == null)
/* 156 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.must.set.a.value.before.adding.a.prefix", new Object[0])); 
/* 157 */     PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
/* 158 */     dict.put(PdfName.D, o);
/* 159 */     dict.put(PdfName.P, (PdfObject)new PdfString(prefix, "UnicodeBig"));
/* 160 */     put(fieldname, (PdfObject)dict);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/collection/PdfCollectionItem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */