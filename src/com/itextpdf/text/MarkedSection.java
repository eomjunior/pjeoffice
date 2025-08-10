/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.api.Indentable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class MarkedSection
/*     */   extends MarkedObject
/*     */   implements Indentable
/*     */ {
/*  65 */   protected MarkedObject title = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedSection(Section section) {
/*  73 */     if (section.title != null) {
/*  74 */       this.title = new MarkedObject(section.title);
/*  75 */       section.setTitle((Paragraph)null);
/*     */     } 
/*  77 */     this.element = section;
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
/*     */   public void add(int index, Element o) {
/*  90 */     ((Section)this.element).add(index, o);
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
/*     */   public boolean add(Element o) {
/* 103 */     return ((Section)this.element).add(o);
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
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 117 */       for (Iterator<Element> i = ((Section)this.element).iterator(); i.hasNext(); ) {
/* 118 */         Element element = i.next();
/* 119 */         listener.add(element);
/*     */       } 
/* 121 */       return true;
/*     */     }
/* 123 */     catch (DocumentException de) {
/* 124 */       return false;
/*     */     } 
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
/*     */   public boolean addAll(Collection<? extends Element> collection) {
/* 137 */     return ((Section)this.element).addAll(collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedSection addSection(float indentation, int numberDepth) {
/* 148 */     MarkedSection section = ((Section)this.element).addMarkedSection();
/* 149 */     section.setIndentation(indentation);
/* 150 */     section.setNumberDepth(numberDepth);
/* 151 */     return section;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedSection addSection(float indentation) {
/* 161 */     MarkedSection section = ((Section)this.element).addMarkedSection();
/* 162 */     section.setIndentation(indentation);
/* 163 */     return section;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedSection addSection(int numberDepth) {
/* 173 */     MarkedSection section = ((Section)this.element).addMarkedSection();
/* 174 */     section.setNumberDepth(numberDepth);
/* 175 */     return section;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedSection addSection() {
/* 184 */     return ((Section)this.element).addMarkedSection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(MarkedObject title) {
/* 195 */     if (title.element instanceof Paragraph) {
/* 196 */       this.title = title;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarkedObject getTitle() {
/* 205 */     Paragraph result = Section.constructTitle((Paragraph)this.title.element, ((Section)this.element).numbers, ((Section)this.element).numberDepth, ((Section)this.element).numberStyle);
/* 206 */     MarkedObject mo = new MarkedObject(result);
/* 207 */     mo.markupAttributes = this.title.markupAttributes;
/* 208 */     return mo;
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
/*     */   public void setNumberDepth(int numberDepth) {
/* 221 */     ((Section)this.element).setNumberDepth(numberDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationLeft(float indentation) {
/* 230 */     ((Section)this.element).setIndentationLeft(indentation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationRight(float indentation) {
/* 239 */     ((Section)this.element).setIndentationRight(indentation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentation(float indentation) {
/* 248 */     ((Section)this.element).setIndentation(indentation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBookmarkOpen(boolean bookmarkOpen) {
/* 256 */     ((Section)this.element).setBookmarkOpen(bookmarkOpen);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerNewPage(boolean triggerNewPage) {
/* 264 */     ((Section)this.element).setTriggerNewPage(triggerNewPage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBookmarkTitle(String bookmarkTitle) {
/* 273 */     ((Section)this.element).setBookmarkTitle(bookmarkTitle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newPage() {
/* 281 */     ((Section)this.element).newPage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationLeft() {
/* 288 */     return ((Section)this.element).getIndentationLeft();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationRight() {
/* 295 */     return ((Section)this.element).getIndentationRight();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/MarkedSection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */