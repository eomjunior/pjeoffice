/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class PdfCopyFields
/*     */   implements PdfViewerPreferences, PdfEncryptionSettings
/*     */ {
/*     */   private PdfCopyFieldsImp fc;
/*     */   
/*     */   public PdfCopyFields(OutputStream os) throws DocumentException {
/*  75 */     this.fc = new PdfCopyFieldsImp(os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfCopyFields(OutputStream os, char pdfVersion) throws DocumentException {
/*  85 */     this.fc = new PdfCopyFieldsImp(os, pdfVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDocument(PdfReader reader) throws DocumentException, IOException {
/*  94 */     this.fc.addDocument(reader);
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
/*     */   public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
/* 106 */     this.fc.addDocument(reader, pagesToKeep);
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
/*     */   public void addDocument(PdfReader reader, String ranges) throws DocumentException, IOException {
/* 118 */     this.fc.addDocument(reader, SequenceList.expand(ranges, reader.getNumberOfPages()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException {
/* 134 */     this.fc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? 1 : 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
/* 151 */     setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 158 */     this.fc.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/* 166 */     this.fc.openDoc();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJavaScript(String js) {
/* 174 */     this.fc.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutlines(List<HashMap<String, Object>> outlines) {
/* 183 */     this.fc.setOutlines(outlines);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfWriter getWriter() {
/* 190 */     return this.fc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullCompression() {
/* 198 */     return this.fc.isFullCompression();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullCompression() throws DocumentException {
/* 206 */     this.fc.setFullCompression();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
/* 213 */     this.fc.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addViewerPreference(PdfName key, PdfObject value) {
/* 220 */     this.fc.addViewerPreference(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewerPreferences(int preferences) {
/* 227 */     this.fc.setViewerPreferences(preferences);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
/* 234 */     this.fc.setEncryption(certs, permissions, encryptionType);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfCopyFields.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */