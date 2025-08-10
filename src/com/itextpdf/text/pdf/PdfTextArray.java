/*     */ package com.itextpdf.text.pdf;
/*     */ 
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
/*     */ public class PdfTextArray
/*     */ {
/*  59 */   ArrayList<Object> arrayList = new ArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   private String lastStr;
/*     */ 
/*     */   
/*     */   private Float lastNum;
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTextArray(String str) {
/*  71 */     add(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTextArray() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(PdfNumber number) {
/*  83 */     add((float)number.doubleValue());
/*     */   }
/*     */   
/*     */   public void add(float number) {
/*  87 */     if (number != 0.0F) {
/*  88 */       if (this.lastNum != null) {
/*  89 */         this.lastNum = new Float(number + this.lastNum.floatValue());
/*  90 */         if (this.lastNum.floatValue() != 0.0F) {
/*  91 */           replaceLast(this.lastNum);
/*     */         } else {
/*  93 */           this.arrayList.remove(this.arrayList.size() - 1);
/*     */         } 
/*     */       } else {
/*  96 */         this.lastNum = new Float(number);
/*  97 */         this.arrayList.add(this.lastNum);
/*     */       } 
/*     */       
/* 100 */       this.lastStr = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String str) {
/* 106 */     if (str.length() > 0) {
/* 107 */       if (this.lastStr != null) {
/* 108 */         this.lastStr += str;
/* 109 */         replaceLast(this.lastStr);
/*     */       } else {
/* 111 */         this.lastStr = str;
/* 112 */         this.arrayList.add(this.lastStr);
/*     */       } 
/* 114 */       this.lastNum = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ArrayList<Object> getArrayList() {
/* 120 */     return this.arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private void replaceLast(Object obj) {
/* 125 */     this.arrayList.set(this.arrayList.size() - 1, obj);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfTextArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */