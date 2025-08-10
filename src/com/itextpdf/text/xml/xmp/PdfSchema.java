/*    */ package com.itextpdf.text.xml.xmp;
/*    */ 
/*    */ import com.itextpdf.text.Version;
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
/*    */ @Deprecated
/*    */ public class PdfSchema
/*    */   extends XmpSchema
/*    */ {
/*    */   private static final long serialVersionUID = -1541148669123992185L;
/*    */   public static final String DEFAULT_XPATH_ID = "pdf";
/*    */   public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/pdf/1.3/";
/*    */   public static final String KEYWORDS = "pdf:Keywords";
/*    */   public static final String VERSION = "pdf:PDFVersion";
/*    */   public static final String PRODUCER = "pdf:Producer";
/*    */   
/*    */   public PdfSchema() {
/* 69 */     super("xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\"");
/* 70 */     addProducer(Version.getInstance().getVersion());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addKeywords(String keywords) {
/* 78 */     setProperty("pdf:Keywords", keywords);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addProducer(String producer) {
/* 86 */     setProperty("pdf:Producer", producer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addVersion(String version) {
/* 94 */     setProperty("pdf:PDFVersion", version);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/PdfSchema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */