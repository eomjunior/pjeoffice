/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfSigLockDictionary
/*     */   extends PdfDictionary
/*     */ {
/*     */   public enum LockAction
/*     */   {
/*  59 */     ALL((String)PdfName.ALL), INCLUDE((String)PdfName.INCLUDE), EXCLUDE((String)PdfName.EXCLUDE);
/*     */     
/*     */     private PdfName name;
/*     */     
/*     */     LockAction(PdfName name) {
/*  64 */       this.name = name;
/*     */     }
/*     */     
/*     */     public PdfName getValue() {
/*  68 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum LockPermissions
/*     */   {
/*  76 */     NO_CHANGES_ALLOWED(1), FORM_FILLING(2), FORM_FILLING_AND_ANNOTATION(3);
/*     */     
/*     */     private PdfNumber number;
/*     */     
/*     */     LockPermissions(int p) {
/*  81 */       this.number = new PdfNumber(p);
/*     */     }
/*     */     
/*     */     public PdfNumber getValue() {
/*  85 */       return this.number;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfSigLockDictionary() {
/*  93 */     super(PdfName.SIGFIELDLOCK);
/*  94 */     put(PdfName.ACTION, LockAction.ALL.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfSigLockDictionary(LockPermissions p) {
/* 102 */     this();
/* 103 */     put(PdfName.P, p.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfSigLockDictionary(LockAction action, String... fields) {
/* 110 */     this(action, (LockPermissions)null, fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfSigLockDictionary(LockAction action, LockPermissions p, String... fields) {
/* 117 */     super(PdfName.SIGFIELDLOCK);
/* 118 */     put(PdfName.ACTION, action.getValue());
/* 119 */     if (p != null)
/* 120 */       put(PdfName.P, p.getValue()); 
/* 121 */     PdfArray fieldsArray = new PdfArray();
/* 122 */     for (String field : fields) {
/* 123 */       fieldsArray.add(new PdfString(field));
/*     */     }
/* 125 */     put(PdfName.FIELDS, fieldsArray);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfSigLockDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */