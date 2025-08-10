/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.pdf.security.XmlLocator;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XfaXmlLocator
/*     */   implements XmlLocator
/*     */ {
/*     */   private PdfStamper stamper;
/*     */   private XfaForm xfaForm;
/*     */   private String encoding;
/*     */   
/*     */   public XfaXmlLocator(PdfStamper stamper) throws DocumentException, IOException {
/*  68 */     this.stamper = stamper;
/*     */     try {
/*  70 */       createXfaForm();
/*  71 */     } catch (ParserConfigurationException e) {
/*  72 */       throw new DocumentException(e);
/*  73 */     } catch (SAXException e) {
/*  74 */       throw new DocumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createXfaForm() throws ParserConfigurationException, SAXException, IOException {
/*  83 */     this.xfaForm = new XfaForm(this.stamper.getReader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document getDocument() {
/*  90 */     return this.xfaForm.getDomDocument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocument(Document document) throws IOException, DocumentException {
/*     */     try {
/* 102 */       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/* 103 */       TransformerFactory tf = TransformerFactory.newInstance();
/*     */ 
/*     */       
/*     */       try {
/* 107 */         tf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
/* 108 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 111 */         tf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
/* 112 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 115 */         tf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/* 116 */       } catch (Exception exception) {}
/*     */       
/* 118 */       Transformer trans = tf.newTransformer();
/*     */ 
/*     */       
/* 121 */       trans.transform(new DOMSource(document), new StreamResult(outputStream));
/*     */ 
/*     */       
/* 124 */       PdfIndirectReference iref = this.stamper.getWriter().addToBody(new PdfStream(outputStream.toByteArray())).getIndirectReference();
/* 125 */       this.stamper.getReader().getAcroForm().put(PdfName.XFA, iref);
/* 126 */     } catch (TransformerConfigurationException e) {
/* 127 */       throw new DocumentException(e);
/* 128 */     } catch (TransformerException e) {
/* 129 */       throw new DocumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getEncoding() {
/* 134 */     return this.encoding;
/*     */   }
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 138 */     this.encoding = encoding;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/XfaXmlLocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */