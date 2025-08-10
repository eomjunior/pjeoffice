/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PdfCopyFormsImp
/*     */   extends PdfCopyFieldsImp
/*     */ {
/*     */   PdfCopyFormsImp(OutputStream os) throws DocumentException {
/*  67 */     super(os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyDocumentFields(PdfReader reader) throws DocumentException {
/*  76 */     if (!reader.isOpenedWithFullPermissions())
/*  77 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0])); 
/*  78 */     if (this.readers2intrefs.containsKey(reader)) {
/*  79 */       reader = new PdfReader(reader);
/*     */     } else {
/*     */       
/*  82 */       if (reader.isTampered())
/*  83 */         throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0])); 
/*  84 */       reader.consolidateNamedDestinations();
/*  85 */       reader.setTampered(true);
/*     */     } 
/*  87 */     reader.shuffleSubsetNames();
/*  88 */     this.readers2intrefs.put(reader, new IntHashtable());
/*     */     
/*  90 */     this.visited.put(reader, new IntHashtable());
/*     */     
/*  92 */     this.fields.add(reader.getAcroFields());
/*  93 */     updateCalculationOrder(reader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void mergeFields() {
/* 102 */     for (int k = 0; k < this.fields.size(); k++) {
/* 103 */       Map<String, AcroFields.Item> fd = ((AcroFields)this.fields.get(k)).getFields();
/* 104 */       mergeWithMaster(fd);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfCopyFormsImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */