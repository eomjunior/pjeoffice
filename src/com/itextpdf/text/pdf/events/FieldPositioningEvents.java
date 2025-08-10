/*     */ package com.itextpdf.text.pdf.events;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfAnnotation;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import com.itextpdf.text.pdf.PdfFormField;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfPCell;
/*     */ import com.itextpdf.text.pdf.PdfPCellEvent;
/*     */ import com.itextpdf.text.pdf.PdfPageEventHelper;
/*     */ import com.itextpdf.text.pdf.PdfRectangle;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import com.itextpdf.text.pdf.TextField;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldPositioningEvents
/*     */   extends PdfPageEventHelper
/*     */   implements PdfPCellEvent
/*     */ {
/*  72 */   protected HashMap<String, PdfFormField> genericChunkFields = new HashMap<String, PdfFormField>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected PdfFormField cellField = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected PdfWriter fieldWriter = null;
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected PdfFormField parent = null;
/*     */ 
/*     */   
/*     */   public float padding;
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldPositioningEvents() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(String text, PdfFormField field) {
/*  98 */     this.genericChunkFields.put(text, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldPositioningEvents(PdfWriter writer, PdfFormField field) {
/* 103 */     this.cellField = field;
/* 104 */     this.fieldWriter = writer;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldPositioningEvents(PdfFormField parent, PdfFormField field) {
/* 109 */     this.cellField = field;
/* 110 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldPositioningEvents(PdfWriter writer, String text) throws IOException, DocumentException {
/* 117 */     this.fieldWriter = writer;
/* 118 */     TextField tf = new TextField(writer, new Rectangle(0.0F, 0.0F), text);
/* 119 */     tf.setFontSize(14.0F);
/* 120 */     this.cellField = tf.getTextField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldPositioningEvents(PdfWriter writer, PdfFormField parent, String text) throws IOException, DocumentException {
/* 127 */     this.parent = parent;
/* 128 */     TextField tf = new TextField(writer, new Rectangle(0.0F, 0.0F), text);
/* 129 */     tf.setFontSize(14.0F);
/* 130 */     this.cellField = tf.getTextField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPadding(float padding) {
/* 137 */     this.padding = padding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(PdfFormField parent) {
/* 144 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
/* 152 */     rect.setBottom(rect.getBottom() - 3.0F);
/* 153 */     PdfFormField field = this.genericChunkFields.get(text);
/* 154 */     if (field == null) {
/* 155 */       TextField tf = new TextField(writer, new Rectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)), text);
/* 156 */       tf.setFontSize(14.0F);
/*     */       try {
/* 158 */         field = tf.getTextField();
/* 159 */       } catch (Exception e) {
/* 160 */         throw new ExceptionConverter(e);
/*     */       } 
/*     */     } else {
/*     */       
/* 164 */       field.put(PdfName.RECT, (PdfObject)new PdfRectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)));
/*     */     } 
/* 166 */     if (this.parent == null) {
/* 167 */       writer.addAnnotation((PdfAnnotation)field);
/*     */     } else {
/* 169 */       this.parent.addKid(field);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
/* 176 */     if (this.cellField == null || (this.fieldWriter == null && this.parent == null)) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.used.the.wrong.constructor.for.this.fieldpositioningevents.class", new Object[0])); 
/* 177 */     this.cellField.put(PdfName.RECT, (PdfObject)new PdfRectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)));
/* 178 */     if (this.parent == null) {
/* 179 */       this.fieldWriter.addAnnotation((PdfAnnotation)this.cellField);
/*     */     } else {
/* 181 */       this.parent.addKid(this.cellField);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/events/FieldPositioningEvents.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */