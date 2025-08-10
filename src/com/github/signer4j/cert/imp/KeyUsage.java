/*     */ package com.github.signer4j.cert.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class KeyUsage
/*     */ {
/*  45 */   private static final String[] USAGES = new String[] { "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign", "encipherOnly", "decipherOnly" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] usages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTrue(int index) {
/*  60 */     return (this.usages != null && index < this.usages.length && this.usages[index]);
/*     */   }
/*     */   
/*     */   public KeyUsage(X509Certificate cert) {
/*  64 */     Args.requireNonNull(cert, "cert is null");
/*  65 */     this.usages = cert.getKeyUsage();
/*     */   }
/*     */   
/*     */   public boolean isDigitalSignature() {
/*  69 */     return isTrue(0);
/*     */   }
/*     */   
/*     */   public boolean isNonRepudiation() {
/*  73 */     return isTrue(1);
/*     */   }
/*     */   
/*     */   public boolean isKeyEncipherment() {
/*  77 */     return isTrue(2);
/*     */   }
/*     */   
/*     */   public boolean isDataEncipherment() {
/*  81 */     return isTrue(3);
/*     */   }
/*     */   
/*     */   public boolean isKeyAgreement() {
/*  85 */     return isTrue(4);
/*     */   }
/*     */   
/*     */   public boolean isKeyCertSign() {
/*  89 */     return isTrue(5);
/*     */   }
/*     */   
/*     */   public boolean isCRLSign() {
/*  93 */     return isTrue(6);
/*     */   }
/*     */   
/*     */   public boolean isEncipherOnly() {
/*  97 */     return isTrue(7);
/*     */   }
/*     */   
/*     */   public boolean isDecipherOnly() {
/* 101 */     return isTrue(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     String out = "";
/* 107 */     if (this.usages != null) {
/* 108 */       for (int i = 0; i < this.usages.length; i++) {
/* 109 */         if (this.usages[i]) {
/* 110 */           if (out.length() > 0) {
/* 111 */             out = out + ", ";
/*     */           }
/* 113 */           out = out + USAGES[i];
/*     */         } 
/*     */       } 
/*     */     }
/* 117 */     return out;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/KeyUsage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */