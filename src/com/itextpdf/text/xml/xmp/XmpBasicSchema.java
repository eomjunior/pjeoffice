/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class XmpBasicSchema
/*     */   extends XmpSchema
/*     */ {
/*     */   private static final long serialVersionUID = -2416613941622479298L;
/*     */   public static final String DEFAULT_XPATH_ID = "xmp";
/*     */   public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/";
/*     */   public static final String ADVISORY = "xmp:Advisory";
/*     */   public static final String BASEURL = "xmp:BaseURL";
/*     */   public static final String CREATEDATE = "xmp:CreateDate";
/*     */   public static final String CREATORTOOL = "xmp:CreatorTool";
/*     */   public static final String IDENTIFIER = "xmp:Identifier";
/*     */   public static final String METADATADATE = "xmp:MetadataDate";
/*     */   public static final String MODIFYDATE = "xmp:ModifyDate";
/*     */   public static final String NICKNAME = "xmp:Nickname";
/*     */   public static final String THUMBNAILS = "xmp:Thumbnails";
/*     */   
/*     */   public XmpBasicSchema() {
/*  80 */     super("xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\"");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCreatorTool(String creator) {
/*  88 */     setProperty("xmp:CreatorTool", creator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCreateDate(String date) {
/*  96 */     setProperty("xmp:CreateDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModDate(String date) {
/* 104 */     setProperty("xmp:ModifyDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMetaDataDate(String date) {
/* 112 */     setProperty("xmp:MetadataDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIdentifiers(String[] id) {
/* 119 */     XmpArray array = new XmpArray("rdf:Bag");
/* 120 */     for (int i = 0; i < id.length; i++) {
/* 121 */       array.add(id[i]);
/*     */     }
/* 123 */     setProperty("xmp:Identifier", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNickname(String name) {
/* 130 */     setProperty("xmp:Nickname", name);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpBasicSchema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */