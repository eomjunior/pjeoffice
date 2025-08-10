/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ import com.itextpdf.text.xml.XMLUtil;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class LangAlt
/*     */   extends Properties
/*     */ {
/*     */   private static final long serialVersionUID = 4396971487200843099L;
/*     */   public static final String DEFAULT = "x-default";
/*     */   
/*     */   public LangAlt(String defaultValue) {
/*  63 */     addLanguage("x-default", defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LangAlt() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLanguage(String language, String value) {
/*  75 */     setProperty(language, XMLUtil.escapeXML(value, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void process(StringBuffer buf, Object lang) {
/*  82 */     buf.append("<rdf:li xml:lang=\"");
/*  83 */     buf.append(lang);
/*  84 */     buf.append("\" >");
/*  85 */     buf.append(get(lang));
/*  86 */     buf.append("</rdf:li>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     StringBuffer sb = new StringBuffer();
/*  95 */     sb.append("<rdf:Alt>");
/*  96 */     for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
/*  97 */       process(sb, e.nextElement());
/*     */     }
/*  99 */     sb.append("</rdf:Alt>");
/* 100 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/LangAlt.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */