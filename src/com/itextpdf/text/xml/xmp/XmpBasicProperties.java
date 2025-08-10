/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMeta;
/*     */ import com.itextpdf.xmp.XMPUtils;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmpBasicProperties
/*     */ {
/*     */   public static final String ADVISORY = "Advisory";
/*     */   public static final String BASEURL = "BaseURL";
/*     */   public static final String CREATEDATE = "CreateDate";
/*     */   public static final String CREATORTOOL = "CreatorTool";
/*     */   public static final String IDENTIFIER = "Identifier";
/*     */   public static final String METADATADATE = "MetadataDate";
/*     */   public static final String MODIFYDATE = "ModifyDate";
/*     */   public static final String NICKNAME = "Nickname";
/*     */   public static final String THUMBNAILS = "Thumbnails";
/*     */   
/*     */   public static void setCreatorTool(XMPMeta xmpMeta, String creator) throws XMPException {
/*  79 */     xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "CreatorTool", creator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCreateDate(XMPMeta xmpMeta, String date) throws XMPException {
/*  89 */     xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "CreateDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setModDate(XMPMeta xmpMeta, String date) throws XMPException {
/*  99 */     xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "ModifyDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMetaDataDate(XMPMeta xmpMeta, String date) throws XMPException {
/* 109 */     xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "MetadataDate", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setIdentifiers(XMPMeta xmpMeta, String[] id) throws XMPException {
/* 118 */     XMPUtils.removeProperties(xmpMeta, "http://purl.org/dc/elements/1.1/", "Identifier", true, true);
/* 119 */     for (int i = 0; i < id.length; i++) {
/* 120 */       xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "Identifier", new PropertyOptions(512), id[i], null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setNickname(XMPMeta xmpMeta, String name) throws XMPException {
/* 130 */     xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "Nickname", name);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpBasicProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */