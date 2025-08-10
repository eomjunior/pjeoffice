/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.crypto.AESCipher;
/*     */ import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardDecryption
/*     */ {
/*     */   protected ARCFOUREncryption arcfour;
/*     */   protected AESCipher cipher;
/*     */   private byte[] key;
/*     */   private static final int AES_128 = 4;
/*     */   private static final int AES_256 = 5;
/*     */   private boolean aes;
/*     */   private boolean initiated;
/*  57 */   private byte[] iv = new byte[16];
/*     */   
/*     */   private int ivptr;
/*     */   
/*     */   public StandardDecryption(byte[] key, int off, int len, int revision) {
/*  62 */     this.aes = (revision == 4 || revision == 5);
/*  63 */     if (this.aes) {
/*  64 */       this.key = new byte[len];
/*  65 */       System.arraycopy(key, off, this.key, 0, len);
/*     */     } else {
/*     */       
/*  68 */       this.arcfour = new ARCFOUREncryption();
/*  69 */       this.arcfour.prepareARCFOURKey(key, off, len);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] update(byte[] b, int off, int len) {
/*  74 */     if (this.aes) {
/*  75 */       if (this.initiated) {
/*  76 */         return this.cipher.update(b, off, len);
/*     */       }
/*  78 */       int left = Math.min(this.iv.length - this.ivptr, len);
/*  79 */       System.arraycopy(b, off, this.iv, this.ivptr, left);
/*  80 */       off += left;
/*  81 */       len -= left;
/*  82 */       this.ivptr += left;
/*  83 */       if (this.ivptr == this.iv.length) {
/*  84 */         this.cipher = new AESCipher(false, this.key, this.iv);
/*  85 */         this.initiated = true;
/*  86 */         if (len > 0)
/*  87 */           return this.cipher.update(b, off, len); 
/*     */       } 
/*  89 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     byte[] b2 = new byte[len];
/*  94 */     this.arcfour.encryptARCFOUR(b, off, len, b2, 0);
/*  95 */     return b2;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] finish() {
/* 100 */     if (this.cipher != null && this.aes) {
/* 101 */       return this.cipher.doFinal();
/*     */     }
/*     */     
/* 104 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/StandardDecryption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */