/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPages
/*     */ {
/*  66 */   private ArrayList<PdfIndirectReference> pages = new ArrayList<PdfIndirectReference>();
/*  67 */   private ArrayList<PdfIndirectReference> parents = new ArrayList<PdfIndirectReference>();
/*  68 */   private int leafSize = 10;
/*     */ 
/*     */   
/*     */   private PdfWriter writer;
/*     */ 
/*     */   
/*     */   private PdfIndirectReference topParent;
/*     */ 
/*     */ 
/*     */   
/*     */   PdfPages(PdfWriter writer) {
/*  79 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   void addPage(PdfDictionary page) {
/*     */     try {
/*  84 */       if (this.pages.size() % this.leafSize == 0)
/*  85 */         this.parents.add(this.writer.getPdfIndirectReference()); 
/*  86 */       PdfIndirectReference parent = this.parents.get(this.parents.size() - 1);
/*  87 */       page.put(PdfName.PARENT, parent);
/*  88 */       PdfIndirectReference current = this.writer.getCurrentPage();
/*  89 */       this.writer.addToBody(page, current);
/*  90 */       this.pages.add(current);
/*     */     }
/*  92 */     catch (Exception e) {
/*  93 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   PdfIndirectReference addPageRef(PdfIndirectReference pageRef) {
/*     */     try {
/*  99 */       if (this.pages.size() % this.leafSize == 0)
/* 100 */         this.parents.add(this.writer.getPdfIndirectReference()); 
/* 101 */       this.pages.add(pageRef);
/* 102 */       return this.parents.get(this.parents.size() - 1);
/*     */     }
/* 104 */     catch (Exception e) {
/* 105 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   PdfIndirectReference writePageTree() throws IOException {
/* 111 */     if (this.pages.isEmpty())
/* 112 */       throw new IOException(MessageLocalization.getComposedMessage("the.document.has.no.pages", new Object[0])); 
/* 113 */     int leaf = 1;
/* 114 */     ArrayList<PdfIndirectReference> tParents = this.parents;
/* 115 */     ArrayList<PdfIndirectReference> tPages = this.pages;
/* 116 */     ArrayList<PdfIndirectReference> nextParents = new ArrayList<PdfIndirectReference>();
/*     */     while (true) {
/* 118 */       leaf *= this.leafSize;
/* 119 */       int stdCount = this.leafSize;
/* 120 */       int rightCount = tPages.size() % this.leafSize;
/* 121 */       if (rightCount == 0)
/* 122 */         rightCount = this.leafSize; 
/* 123 */       for (int p = 0; p < tParents.size(); p++) {
/*     */         
/* 125 */         int count, thisLeaf = leaf;
/* 126 */         if (p == tParents.size() - 1) {
/* 127 */           count = rightCount;
/* 128 */           thisLeaf = this.pages.size() % leaf;
/* 129 */           if (thisLeaf == 0) {
/* 130 */             thisLeaf = leaf;
/*     */           }
/*     */         } else {
/* 133 */           count = stdCount;
/* 134 */         }  PdfDictionary top = new PdfDictionary(PdfName.PAGES);
/* 135 */         top.put(PdfName.COUNT, new PdfNumber(thisLeaf));
/* 136 */         PdfArray kids = new PdfArray();
/* 137 */         ArrayList<PdfObject> internal = kids.getArrayList();
/* 138 */         internal.addAll(tPages.subList(p * stdCount, p * stdCount + count));
/* 139 */         top.put(PdfName.KIDS, kids);
/* 140 */         if (tParents.size() > 1) {
/* 141 */           if (p % this.leafSize == 0)
/* 142 */             nextParents.add(this.writer.getPdfIndirectReference()); 
/* 143 */           top.put(PdfName.PARENT, nextParents.get(p / this.leafSize));
/*     */         } 
/* 145 */         this.writer.addToBody(top, tParents.get(p));
/*     */       } 
/* 147 */       if (tParents.size() == 1) {
/* 148 */         this.topParent = tParents.get(0);
/* 149 */         return this.topParent;
/*     */       } 
/* 151 */       tPages = tParents;
/* 152 */       tParents = nextParents;
/* 153 */       nextParents = new ArrayList<PdfIndirectReference>();
/*     */     } 
/*     */   }
/*     */   
/*     */   PdfIndirectReference getTopParent() {
/* 158 */     return this.topParent;
/*     */   }
/*     */   
/*     */   void setLinearMode(PdfIndirectReference topParent) {
/* 162 */     if (this.parents.size() > 1)
/* 163 */       throw new RuntimeException(MessageLocalization.getComposedMessage("linear.page.mode.can.only.be.called.with.a.single.parent", new Object[0])); 
/* 164 */     if (topParent != null) {
/* 165 */       this.topParent = topParent;
/* 166 */       this.parents.clear();
/* 167 */       this.parents.add(topParent);
/*     */     } 
/* 169 */     this.leafSize = 10000000;
/*     */   }
/*     */   
/*     */   void addPage(PdfIndirectReference page) {
/* 173 */     this.pages.add(page);
/*     */   }
/*     */   
/*     */   int reorderPages(int[] order) throws DocumentException {
/* 177 */     if (order == null)
/* 178 */       return this.pages.size(); 
/* 179 */     if (this.parents.size() > 1)
/* 180 */       throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.a.single.parent.in.the.page.tree.call.pdfwriter.setlinearmode.after.open", new Object[0])); 
/* 181 */     if (order.length != this.pages.size())
/* 182 */       throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.an.array.with.the.same.size.as.the.number.of.pages", new Object[0])); 
/* 183 */     int max = this.pages.size();
/* 184 */     boolean[] temp = new boolean[max];
/* 185 */     for (int k = 0; k < max; k++) {
/* 186 */       int p = order[k];
/* 187 */       if (p < 1 || p > max)
/* 188 */         throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.pages.between.1.and.1.found.2", new Object[] { String.valueOf(max), String.valueOf(p) })); 
/* 189 */       if (temp[p - 1])
/* 190 */         throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.no.page.repetition.page.1.is.repeated", p)); 
/* 191 */       temp[p - 1] = true;
/*     */     } 
/* 193 */     PdfIndirectReference[] copy = this.pages.<PdfIndirectReference>toArray(new PdfIndirectReference[this.pages.size()]);
/* 194 */     for (int i = 0; i < max; i++) {
/* 195 */       this.pages.set(i, copy[order[i] - 1]);
/*     */     }
/* 197 */     return max;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPages.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */