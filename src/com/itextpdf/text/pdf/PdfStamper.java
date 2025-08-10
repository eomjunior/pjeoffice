/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.collection.PdfCollection;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
/*     */ import com.itextpdf.text.pdf.security.LtvVerification;
/*     */ import com.itextpdf.text.xml.xmp.XmpWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfStamper
/*     */   implements PdfViewerPreferences, PdfEncryptionSettings
/*     */ {
/*     */   protected PdfStamperImp stamper;
/*     */   private Map<String, String> moreInfo;
/*     */   protected boolean hasSignature;
/*     */   protected PdfSignatureAppearance sigApp;
/*     */   protected XmlSignatureAppearance sigXmlApp;
/*     */   private LtvVerification verification;
/*     */   
/*     */   public PdfStamper(PdfReader reader, OutputStream os) throws DocumentException, IOException {
/*  98 */     this.stamper = new PdfStamperImp(reader, os, false, false);
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
/*     */   public PdfStamper(PdfReader reader, OutputStream os, char pdfVersion) throws DocumentException, IOException {
/* 114 */     this.stamper = new PdfStamperImp(reader, os, pdfVersion, false);
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
/*     */   
/*     */   public PdfStamper(PdfReader reader, OutputStream os, char pdfVersion, boolean append) throws DocumentException, IOException {
/* 132 */     this.stamper = new PdfStamperImp(reader, os, pdfVersion, append);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getMoreInfo() {
/* 141 */     return this.moreInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMoreInfo(Map<String, String> moreInfo) {
/* 151 */     this.moreInfo = moreInfo;
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
/*     */   public void replacePage(PdfReader r, int pageImported, int pageReplaced) {
/* 164 */     this.stamper.replacePage(r, pageImported, pageReplaced);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertPage(int pageNumber, Rectangle mediabox) {
/* 175 */     this.stamper.insertPage(pageNumber, mediabox);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfSignatureAppearance getSignatureAppearance() {
/* 183 */     return this.sigApp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlSignatureAppearance getXmlSignatureAppearance() {
/* 191 */     return this.sigXmlApp;
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
/*     */   public void flush() {
/*     */     try {
/* 207 */       this.stamper.alterContents();
/* 208 */       this.stamper.pagesToContent.clear();
/* 209 */     } catch (IOException e) {
/* 210 */       throw new ExceptionConverter(e);
/*     */     } 
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
/*     */   public void close() throws DocumentException, IOException {
/* 226 */     if (this.stamper.closed)
/*     */       return; 
/* 228 */     if (!this.hasSignature) {
/* 229 */       mergeVerification();
/* 230 */       this.stamper.close(this.moreInfo);
/*     */     } else {
/*     */       
/* 233 */       throw new DocumentException("Signature defined. Must be closed in PdfSignatureAppearance.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfContentByte getUnderContent(int pageNum) {
/* 244 */     return this.stamper.getUnderContent(pageNum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfContentByte getOverContent(int pageNum) {
/* 254 */     return this.stamper.getOverContent(pageNum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRotateContents() {
/* 262 */     return this.stamper.isRotateContents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotateContents(boolean rotateContents) {
/* 271 */     this.stamper.setRotateContents(rotateContents);
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
/* 287 */     if (this.stamper.isAppend())
/* 288 */       throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status", new Object[0])); 
/* 289 */     if (this.stamper.isContentWritten())
/* 290 */       throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output", new Object[0])); 
/* 291 */     this.stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? 1 : 0);
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
/*     */   public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
/* 308 */     if (this.stamper.isAppend())
/* 309 */       throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status", new Object[0])); 
/* 310 */     if (this.stamper.isContentWritten())
/* 311 */       throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output", new Object[0])); 
/* 312 */     this.stamper.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
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
/* 329 */     setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
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
/*     */   
/*     */   public void setEncryption(int encryptionType, String userPassword, String ownerPassword, int permissions) throws DocumentException {
/* 347 */     setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, encryptionType);
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
/*     */   public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
/* 364 */     if (this.stamper.isAppend())
/* 365 */       throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status", new Object[0])); 
/* 366 */     if (this.stamper.isContentWritten())
/* 367 */       throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output", new Object[0])); 
/* 368 */     this.stamper.setEncryption(certs, permissions, encryptionType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
/* 378 */     return this.stamper.getImportedPage(reader, pageNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfWriter getWriter() {
/* 385 */     return this.stamper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfReader getReader() {
/* 392 */     return this.stamper.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AcroFields getAcroFields() {
/* 400 */     return this.stamper.getAcroFields();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormFlattening(boolean flat) {
/* 409 */     this.stamper.setFormFlattening(flat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFreeTextFlattening(boolean flat) {
/* 417 */     this.stamper.setFreeTextFlattening(flat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotationFlattening(boolean flat) {
/* 426 */     this.stamper.setFlatAnnotations(flat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotation(PdfAnnotation annot, int page) {
/* 436 */     this.stamper.addAnnotation(annot, page);
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
/*     */   public PdfFormField addSignature(String name, int page, float llx, float lly, float urx, float ury) {
/* 451 */     PdfAcroForm acroForm = this.stamper.getAcroForm();
/* 452 */     PdfFormField signature = PdfFormField.createSignature(this.stamper);
/* 453 */     acroForm.setSignatureParams(signature, name, llx, lly, urx, ury);
/* 454 */     acroForm.drawSignatureAppearences(signature, llx, lly, urx, ury);
/* 455 */     addAnnotation(signature, page);
/* 456 */     return signature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComments(FdfReader fdf) throws IOException {
/* 465 */     this.stamper.addComments(fdf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutlines(List<HashMap<String, Object>> outlines) {
/* 474 */     this.stamper.setOutlines(outlines);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThumbnail(Image image, int page) throws PdfException, DocumentException {
/* 485 */     this.stamper.setThumbnail(image, page);
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
/*     */   public boolean partialFormFlattening(String name) {
/* 499 */     return this.stamper.partialFormFlattening(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJavaScript(String js) {
/* 507 */     this.stamper.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJavaScript(String name, String js) {
/* 516 */     this.stamper.addJavaScript(name, PdfAction.javaScript(js, this.stamper, !PdfEncodings.isPdfDocEncoding(js)));
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
/*     */   public void addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay) throws IOException {
/* 529 */     addFileAttachment(description, PdfFileSpecification.fileEmbedded(this.stamper, file, fileDisplay, fileStore));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileAttachment(String description, PdfFileSpecification fs) throws IOException {
/* 538 */     this.stamper.addFileAttachment(description, fs);
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
/*     */   public void makePackage(PdfName initialView) {
/* 555 */     PdfCollection collection = new PdfCollection(0);
/* 556 */     collection.put(PdfName.VIEW, initialView);
/* 557 */     this.stamper.makePackage(collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void makePackage(PdfCollection collection) {
/* 565 */     this.stamper.makePackage(collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewerPreferences(int preferences) {
/* 574 */     this.stamper.setViewerPreferences(preferences);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addViewerPreference(PdfName key, PdfObject value) {
/* 584 */     this.stamper.addViewerPreference(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXmpMetadata(byte[] xmp) {
/* 593 */     this.stamper.setXmpMetadata(xmp);
/*     */   }
/*     */   
/*     */   public void createXmpMetadata() {
/* 597 */     this.stamper.createXmpMetadata();
/*     */   }
/*     */   
/*     */   public XmpWriter getXmpWriter() {
/* 601 */     return this.stamper.getXmpWriter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullCompression() {
/* 609 */     return this.stamper.isFullCompression();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullCompression() throws DocumentException {
/* 618 */     if (this.stamper.isAppend())
/*     */       return; 
/* 620 */     this.stamper.fullCompression = true;
/* 621 */     this.stamper.setAtLeastPdfVersion('5');
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
/*     */   public void setPageAction(PdfName actionType, PdfAction action, int page) throws PdfException {
/* 633 */     this.stamper.setPageAction(actionType, action, page);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDuration(int seconds, int page) {
/* 642 */     this.stamper.setDuration(seconds, page);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransition(PdfTransition transition, int page) {
/* 651 */     this.stamper.setTransition(transition, page);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfStamper createSignature(PdfReader reader, OutputStream os, char pdfVersion, File tempFile, boolean append) throws DocumentException, IOException {
/*     */     PdfStamper stp;
/* 695 */     if (tempFile == null) {
/* 696 */       ByteBuffer bout = new ByteBuffer();
/* 697 */       stp = new PdfStamper(reader, bout, pdfVersion, append);
/* 698 */       stp.sigApp = new PdfSignatureAppearance(stp.stamper);
/* 699 */       stp.sigApp.setSigout(bout);
/*     */     } else {
/*     */       
/* 702 */       if (tempFile.isDirectory())
/* 703 */         tempFile = File.createTempFile("pdf", ".pdf", tempFile); 
/* 704 */       FileOutputStream fout = new FileOutputStream(tempFile);
/* 705 */       stp = new PdfStamper(reader, fout, pdfVersion, append);
/* 706 */       stp.sigApp = new PdfSignatureAppearance(stp.stamper);
/* 707 */       stp.sigApp.setTempFile(tempFile);
/*     */     } 
/* 709 */     stp.sigApp.setOriginalout(os);
/* 710 */     stp.sigApp.setStamper(stp);
/* 711 */     stp.hasSignature = true;
/* 712 */     PdfDictionary catalog = reader.getCatalog();
/* 713 */     PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
/* 714 */     if (acroForm != null) {
/* 715 */       acroForm.remove(PdfName.NEEDAPPEARANCES);
/* 716 */       stp.stamper.markUsed(acroForm);
/*     */     } 
/* 718 */     return stp;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfStamper createSignature(PdfReader reader, OutputStream os, char pdfVersion) throws DocumentException, IOException {
/* 755 */     return createSignature(reader, os, pdfVersion, null, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfStamper createSignature(PdfReader reader, OutputStream os, char pdfVersion, File tempFile) throws DocumentException, IOException {
/* 794 */     return createSignature(reader, os, pdfVersion, tempFile, false);
/*     */   }
/*     */   
/*     */   public static PdfStamper createXmlSignature(PdfReader reader, OutputStream os) throws IOException, DocumentException {
/* 798 */     PdfStamper stp = new PdfStamper(reader, os);
/* 799 */     stp.sigXmlApp = new XmlSignatureAppearance(stp.stamper);
/*     */ 
/*     */     
/* 802 */     stp.sigXmlApp.setStamper(stp);
/*     */     
/* 804 */     return stp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PdfLayer> getPdfLayers() {
/* 814 */     return this.stamper.getPdfLayers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markUsed(PdfObject obj) {
/* 825 */     this.stamper.markUsed(obj);
/*     */   }
/*     */   
/*     */   public LtvVerification getLtvVerification() {
/* 829 */     if (this.verification == null)
/* 830 */       this.verification = new LtvVerification(this); 
/* 831 */     return this.verification;
/*     */   }
/*     */   
/*     */   public boolean addNamedDestination(String name, int page, PdfDestination dest) throws IOException {
/* 835 */     HashMap<Object, PdfObject> namedDestinations = this.stamper.getNamedDestinations();
/*     */     
/* 837 */     if (getReader().getNamedDestination().containsKey(name)) {
/* 838 */       return false;
/*     */     }
/* 840 */     PdfDestination d = new PdfDestination(dest);
/* 841 */     d.addPage(getReader().getPageOrigRef(page));
/* 842 */     namedDestinations.put(name, new PdfArray(d));
/* 843 */     return true;
/*     */   }
/*     */   
/*     */   void mergeVerification() throws IOException {
/* 847 */     if (this.verification == null)
/*     */       return; 
/* 849 */     this.verification.merge();
/*     */   }
/*     */   
/*     */   protected PdfStamper() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStamper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */