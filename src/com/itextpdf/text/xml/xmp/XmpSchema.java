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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class XmpSchema
/*     */   extends Properties
/*     */ {
/*     */   private static final long serialVersionUID = -176374295948945272L;
/*     */   protected String xmlns;
/*     */   
/*     */   public XmpSchema(String xmlns) {
/*  67 */     this.xmlns = xmlns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  75 */     StringBuffer buf = new StringBuffer();
/*  76 */     for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
/*  77 */       process(buf, e.nextElement());
/*     */     }
/*  79 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void process(StringBuffer buf, Object p) {
/*  87 */     buf.append('<');
/*  88 */     buf.append(p);
/*  89 */     buf.append('>');
/*  90 */     buf.append(get(p));
/*  91 */     buf.append("</");
/*  92 */     buf.append(p);
/*  93 */     buf.append('>');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXmlns() {
/*  99 */     return this.xmlns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object addProperty(String key, String value) {
/* 108 */     return setProperty(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setProperty(String key, String value) {
/* 116 */     return super.setProperty(key, XMLUtil.escapeXML(value, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setProperty(String key, XmpArray value) {
/* 127 */     return super.setProperty(key, value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object setProperty(String key, LangAlt value) {
/* 138 */     return super.setProperty(key, value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String content) {
/* 147 */     return XMLUtil.escapeXML(content, false);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpSchema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */