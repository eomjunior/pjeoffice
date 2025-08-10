/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PRIndirectReference;
/*     */ import com.itextpdf.text.pdf.PRStream;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentByteUtils
/*     */ {
/*     */   public static byte[] getContentBytesFromContentObject(PdfObject contentObject) throws IOException {
/*     */     byte[] result;
/*     */     PRIndirectReference ref;
/*     */     PdfObject directObject;
/*     */     PRStream stream;
/*     */     ByteArrayOutputStream allBytes;
/*     */     PdfArray contentArray;
/*     */     ListIterator<PdfObject> iter;
/*  76 */     switch (contentObject.type()) {
/*     */       
/*     */       case 10:
/*  79 */         ref = (PRIndirectReference)contentObject;
/*  80 */         directObject = PdfReader.getPdfObjectRelease((PdfObject)ref);
/*  81 */         result = getContentBytesFromContentObject(directObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 105 */         return result;case 7: stream = (PRStream)PdfReader.getPdfObjectRelease(contentObject); result = PdfReader.getStreamBytes(stream); return result;case 5: allBytes = new ByteArrayOutputStream(); contentArray = (PdfArray)contentObject; iter = contentArray.listIterator(); while (iter.hasNext()) { PdfObject element = iter.next(); allBytes.write(getContentBytesFromContentObject(element)); allBytes.write(32); }  result = allBytes.toByteArray(); return result;
/*     */     } 
/*     */     String msg = "Unable to handle Content of type " + contentObject.getClass();
/*     */     throw new IllegalStateException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getContentBytesForPage(PdfReader reader, int pageNum) throws IOException {
/* 117 */     PdfDictionary pageDictionary = reader.getPageN(pageNum);
/* 118 */     PdfObject contentObject = pageDictionary.get(PdfName.CONTENTS);
/* 119 */     if (contentObject == null) {
/* 120 */       return new byte[0];
/*     */     }
/* 122 */     byte[] contentBytes = getContentBytesFromContentObject(contentObject);
/* 123 */     return contentBytes;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/ContentByteUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */