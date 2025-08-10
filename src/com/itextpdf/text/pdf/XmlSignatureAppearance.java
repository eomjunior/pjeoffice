/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.pdf.security.XmlLocator;
/*     */ import com.itextpdf.text.pdf.security.XpathConstructor;
/*     */ import java.io.IOException;
/*     */ import java.security.cert.Certificate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlSignatureAppearance
/*     */ {
/*     */   private PdfStamperImp writer;
/*     */   private PdfStamper stamper;
/*     */   private Certificate signCertificate;
/*     */   private XmlLocator xmlLocator;
/*     */   private XpathConstructor xpathConstructor;
/*     */   private Calendar signDate;
/*     */   private String description;
/*     */   private String mimeType;
/*     */   
/*     */   XmlSignatureAppearance(PdfStamperImp writer) {
/*  79 */     this.mimeType = "text/xml";
/*     */     this.writer = writer;
/*     */   } public PdfStamperImp getWriter() {
/*  82 */     return this.writer;
/*     */   }
/*     */   
/*     */   public PdfStamper getStamper() {
/*  86 */     return this.stamper;
/*     */   }
/*     */   
/*     */   public void setStamper(PdfStamper stamper) {
/*  90 */     this.stamper = stamper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificate(Certificate signCertificate) {
/*  99 */     this.signCertificate = signCertificate;
/*     */   }
/*     */   
/*     */   public Certificate getCertificate() {
/* 103 */     return this.signCertificate;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 107 */     this.description = description;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 111 */     return this.description;
/*     */   }
/*     */   
/*     */   public String getMimeType() {
/* 115 */     return this.mimeType;
/*     */   }
/*     */   
/*     */   public void setMimeType(String mimeType) {
/* 119 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Calendar getSignDate() {
/* 127 */     if (this.signDate == null)
/* 128 */       this.signDate = Calendar.getInstance(); 
/* 129 */     return this.signDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignDate(Calendar signDate) {
/* 137 */     this.signDate = signDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlLocator getXmlLocator() {
/* 145 */     return this.xmlLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXmlLocator(XmlLocator xmlLocator) {
/* 150 */     this.xmlLocator = xmlLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XpathConstructor getXpathConstructor() {
/* 158 */     return this.xpathConstructor;
/*     */   }
/*     */   
/*     */   public void setXpathConstructor(XpathConstructor xpathConstructor) {
/* 162 */     this.xpathConstructor = xpathConstructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException, DocumentException {
/* 171 */     this.writer.close(this.stamper.getMoreInfo());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/XmlSignatureAppearance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */