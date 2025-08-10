/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDestination
/*     */   extends PdfArray
/*     */ {
/*     */   public static final int XYZ = 0;
/*     */   public static final int FIT = 1;
/*     */   public static final int FITH = 2;
/*     */   public static final int FITV = 3;
/*     */   public static final int FITR = 4;
/*     */   public static final int FITB = 5;
/*     */   public static final int FITBH = 6;
/*     */   public static final int FITBV = 7;
/*     */   private boolean status = false;
/*     */   
/*     */   public PdfDestination(PdfDestination d) {
/*  88 */     super(d);
/*  89 */     this.status = d.status;
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
/*     */   public PdfDestination(int type) {
/* 104 */     if (type == 5) {
/* 105 */       add(PdfName.FITB);
/*     */     } else {
/*     */       
/* 108 */       add(PdfName.FIT);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDestination(int type, float parameter) {
/* 128 */     super(new PdfNumber(parameter));
/* 129 */     switch (type) {
/*     */       default:
/* 131 */         addFirst(PdfName.FITH);
/*     */         return;
/*     */       case 3:
/* 134 */         addFirst(PdfName.FITV);
/*     */         return;
/*     */       case 6:
/* 137 */         addFirst(PdfName.FITBH); return;
/*     */       case 7:
/*     */         break;
/* 140 */     }  addFirst(PdfName.FITBV);
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
/*     */   public PdfDestination(int type, float left, float top, float zoom) {
/* 157 */     super(PdfName.XYZ);
/* 158 */     if (left < 0.0F) {
/* 159 */       add(PdfNull.PDFNULL);
/*     */     } else {
/* 161 */       add(new PdfNumber(left));
/* 162 */     }  if (top < 0.0F) {
/* 163 */       add(PdfNull.PDFNULL);
/*     */     } else {
/* 165 */       add(new PdfNumber(top));
/* 166 */     }  add(new PdfNumber(zoom));
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
/*     */   public PdfDestination(int type, float left, float bottom, float right, float top) {
/* 186 */     super(PdfName.FITR);
/* 187 */     add(new PdfNumber(left));
/* 188 */     add(new PdfNumber(bottom));
/* 189 */     add(new PdfNumber(right));
/* 190 */     add(new PdfNumber(top));
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
/*     */   public PdfDestination(String dest) {
/* 202 */     StringTokenizer tokens = new StringTokenizer(dest);
/* 203 */     if (tokens.hasMoreTokens()) {
/* 204 */       add(new PdfName(tokens.nextToken()));
/*     */     }
/* 206 */     while (tokens.hasMoreTokens()) {
/* 207 */       String token = tokens.nextToken();
/* 208 */       if ("null".equals(token)) {
/* 209 */         add(new PdfNull()); continue;
/*     */       } 
/*     */       try {
/* 212 */         add(new PdfNumber(token));
/* 213 */       } catch (RuntimeException e) {
/* 214 */         add(new PdfNull());
/*     */       } 
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
/*     */   
/*     */   public boolean hasPage() {
/* 229 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addPage(PdfIndirectReference page) {
/* 239 */     if (!this.status) {
/* 240 */       addFirst(page);
/* 241 */       this.status = true;
/* 242 */       return true;
/*     */     } 
/* 244 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDestination.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */