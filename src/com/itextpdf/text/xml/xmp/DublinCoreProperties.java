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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DublinCoreProperties
/*     */ {
/*     */   public static final String CONTRIBUTOR = "contributor";
/*     */   public static final String COVERAGE = "coverage";
/*     */   public static final String CREATOR = "creator";
/*     */   public static final String DATE = "date";
/*     */   public static final String DESCRIPTION = "description";
/*     */   public static final String FORMAT = "format";
/*     */   public static final String IDENTIFIER = "identifier";
/*     */   public static final String LANGUAGE = "language";
/*     */   public static final String PUBLISHER = "publisher";
/*     */   public static final String RELATION = "relation";
/*     */   public static final String RIGHTS = "rights";
/*     */   public static final String SOURCE = "source";
/*     */   public static final String SUBJECT = "subject";
/*     */   public static final String TITLE = "title";
/*     */   public static final String TYPE = "type";
/*     */   
/*     */   public static void addTitle(XMPMeta xmpMeta, String title) throws XMPException {
/*  91 */     xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "title", new PropertyOptions(2048), title, null);
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
/*     */   public static void setTitle(XMPMeta xmpMeta, String title, String genericLang, String specificLang) throws XMPException {
/* 103 */     xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "title", genericLang, specificLang, title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDescription(XMPMeta xmpMeta, String desc) throws XMPException {
/* 113 */     xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "description", new PropertyOptions(2048), desc, null);
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
/*     */   public static void setDescription(XMPMeta xmpMeta, String desc, String genericLang, String specificLang) throws XMPException {
/* 126 */     xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "description", genericLang, specificLang, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addSubject(XMPMeta xmpMeta, String subject) throws XMPException {
/* 136 */     xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "subject", new PropertyOptions(512), subject, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setSubject(XMPMeta xmpMeta, String[] subject) throws XMPException {
/* 147 */     XMPUtils.removeProperties(xmpMeta, "http://purl.org/dc/elements/1.1/", "subject", true, true);
/* 148 */     for (int i = 0; i < subject.length; i++) {
/* 149 */       xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "subject", new PropertyOptions(512), subject[i], null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addAuthor(XMPMeta xmpMeta, String author) throws XMPException {
/* 160 */     xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "creator", new PropertyOptions(1024), author, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAuthor(XMPMeta xmpMeta, String[] author) throws XMPException {
/* 170 */     XMPUtils.removeProperties(xmpMeta, "http://purl.org/dc/elements/1.1/", "creator", true, true);
/* 171 */     for (int i = 0; i < author.length; i++) {
/* 172 */       xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "creator", new PropertyOptions(1024), author[i], null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPublisher(XMPMeta xmpMeta, String publisher) throws XMPException {
/* 183 */     xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "publisher", new PropertyOptions(1024), publisher, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setPublisher(XMPMeta xmpMeta, String[] publisher) throws XMPException {
/* 193 */     XMPUtils.removeProperties(xmpMeta, "http://purl.org/dc/elements/1.1/", "publisher", true, true);
/* 194 */     for (int i = 0; i < publisher.length; i++)
/* 195 */       xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "publisher", new PropertyOptions(1024), publisher[i], null); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/DublinCoreProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */