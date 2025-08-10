/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMUtils
/*     */ {
/*     */   public static Document newDocument() {
/*  44 */     return JAXPUtils.getDocumentBuilder().newDocument();
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
/*     */   public static Element createChildElement(Element parent, String name) {
/*  68 */     Document doc = parent.getOwnerDocument();
/*  69 */     Element e = doc.createElement(name);
/*  70 */     parent.appendChild(e);
/*  71 */     return e;
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
/*     */   public static void appendText(Element parent, String content) {
/*  90 */     Document doc = parent.getOwnerDocument();
/*  91 */     Text t = doc.createTextNode(content);
/*  92 */     parent.appendChild(t);
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
/*     */   public static void appendCDATA(Element parent, String content) {
/* 111 */     Document doc = parent.getOwnerDocument();
/* 112 */     CDATASection c = doc.createCDATASection(content);
/* 113 */     parent.appendChild(c);
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
/*     */   public static void appendTextElement(Element parent, String name, String content) {
/* 136 */     Element e = createChildElement(parent, name);
/* 137 */     appendText(e, content);
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
/*     */   public static void appendCDATAElement(Element parent, String name, String content) {
/* 160 */     Element e = createChildElement(parent, name);
/* 161 */     appendCDATA(e, content);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/DOMUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */