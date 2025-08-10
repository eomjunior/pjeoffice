/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignaturePermissions
/*     */ {
/*     */   public class FieldLock
/*     */   {
/*     */     PdfName action;
/*     */     PdfArray fields;
/*     */     
/*     */     public FieldLock(PdfName action, PdfArray fields) {
/*  71 */       this.action = action;
/*  72 */       this.fields = fields;
/*     */     }
/*     */     public PdfName getAction() {
/*  75 */       return this.action;
/*     */     } public PdfArray getFields() {
/*  77 */       return this.fields;
/*     */     }
/*     */     public String toString() {
/*  80 */       return this.action.toString() + ((this.fields == null) ? "" : this.fields.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean certification = false;
/*     */   
/*     */   boolean fillInAllowed = true;
/*     */   
/*     */   boolean annotationsAllowed = true;
/*     */   
/*  91 */   List<FieldLock> fieldLocks = new ArrayList<FieldLock>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SignaturePermissions(PdfDictionary sigDict, SignaturePermissions previous) {
/*  99 */     if (previous != null) {
/* 100 */       this.annotationsAllowed &= previous.isAnnotationsAllowed();
/* 101 */       this.fillInAllowed &= previous.isFillInAllowed();
/* 102 */       this.fieldLocks.addAll(previous.getFieldLocks());
/*     */     } 
/* 104 */     PdfArray ref = sigDict.getAsArray(PdfName.REFERENCE);
/* 105 */     if (ref != null) {
/* 106 */       for (int i = 0; i < ref.size(); i++) {
/* 107 */         PdfDictionary dict = ref.getAsDict(i);
/* 108 */         PdfDictionary params = dict.getAsDict(PdfName.TRANSFORMPARAMS);
/* 109 */         if (PdfName.DOCMDP.equals(dict.getAsName(PdfName.TRANSFORMMETHOD))) {
/* 110 */           this.certification = true;
/*     */         }
/* 112 */         PdfName action = params.getAsName(PdfName.ACTION);
/* 113 */         if (action != null) {
/* 114 */           this.fieldLocks.add(new FieldLock(action, params.getAsArray(PdfName.FIELDS)));
/*     */         }
/* 116 */         PdfNumber p = params.getAsNumber(PdfName.P);
/* 117 */         if (p != null)
/*     */         {
/* 119 */           switch (p.intValue()) {
/*     */ 
/*     */             
/*     */             case 1:
/* 123 */               this.fillInAllowed &= 0x0;
/*     */             case 2:
/* 125 */               this.annotationsAllowed &= 0x0;
/*     */               break;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCertification() {
/* 136 */     return this.certification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFillInAllowed() {
/* 143 */     return this.fillInAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotationsAllowed() {
/* 150 */     return this.annotationsAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FieldLock> getFieldLocks() {
/* 157 */     return this.fieldLocks;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/SignaturePermissions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */