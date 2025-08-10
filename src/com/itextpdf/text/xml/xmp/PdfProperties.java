/*    */ package com.itextpdf.text.xml.xmp;
/*    */ 
/*    */ import com.itextpdf.xmp.XMPException;
/*    */ import com.itextpdf.xmp.XMPMeta;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfProperties
/*    */ {
/*    */   public static final String KEYWORDS = "Keywords";
/*    */   public static final String VERSION = "PDFVersion";
/*    */   public static final String PRODUCER = "Producer";
/*    */   public static final String PART = "part";
/*    */   
/*    */   public static void setKeywords(XMPMeta xmpMeta, String keywords) throws XMPException {
/* 66 */     xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "Keywords", keywords);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setProducer(XMPMeta xmpMeta, String producer) throws XMPException {
/* 76 */     xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "Producer", producer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setVersion(XMPMeta xmpMeta, String version) throws XMPException {
/* 86 */     xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "PDFVersion", version);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/PdfProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */