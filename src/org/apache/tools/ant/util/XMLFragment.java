/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.DynamicConfiguratorNS;
/*     */ import org.apache.tools.ant.DynamicElementNS;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
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
/*     */ 
/*     */ public class XMLFragment
/*     */   extends ProjectComponent
/*     */   implements DynamicElementNS
/*     */ {
/*  50 */   private Document doc = JAXPUtils.getDocumentBuilder().newDocument();
/*  51 */   private DocumentFragment fragment = this.doc.createDocumentFragment();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocumentFragment getFragment() {
/*  59 */     return this.fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String s) {
/*  67 */     addText(this.fragment, s);
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
/*     */   public Object createDynamicElement(String uri, String name, String qName) {
/*     */     Element e;
/*  80 */     if (uri.isEmpty()) {
/*  81 */       e = this.doc.createElement(name);
/*     */     } else {
/*  83 */       e = this.doc.createElementNS(uri, qName);
/*     */     } 
/*  85 */     this.fragment.appendChild(e);
/*  86 */     return new Child(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addText(Node n, String s) {
/*  95 */     s = getProject().replaceProperties(s);
/*     */     
/*  97 */     if (s != null && !s.trim().isEmpty()) {
/*  98 */       Text t = this.doc.createTextNode(s.trim());
/*  99 */       n.appendChild(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public class Child
/*     */     implements DynamicConfiguratorNS
/*     */   {
/*     */     private Element e;
/*     */     
/*     */     Child(Element e) {
/* 110 */       this.e = e;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addText(String s) {
/* 118 */       XMLFragment.this.addText(this.e, s);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDynamicAttribute(String uri, String name, String qName, String value) {
/* 131 */       if (uri.isEmpty()) {
/* 132 */         this.e.setAttribute(name, value);
/*     */       } else {
/* 134 */         this.e.setAttributeNS(uri, qName, value);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object createDynamicElement(String uri, String name, String qName) {
/* 147 */       Element e2 = null;
/* 148 */       if (uri.isEmpty()) {
/* 149 */         e2 = XMLFragment.this.doc.createElement(name);
/*     */       } else {
/* 151 */         e2 = XMLFragment.this.doc.createElementNS(uri, qName);
/*     */       } 
/* 153 */       this.e.appendChild(e2);
/* 154 */       return new Child(e2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/XMLFragment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */