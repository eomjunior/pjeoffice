/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.security.MessageDigest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImgJBIG2
/*     */   extends Image
/*     */ {
/*     */   private byte[] global;
/*     */   private byte[] globalHash;
/*     */   
/*     */   ImgJBIG2(Image image) {
/*  65 */     super(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImgJBIG2() {
/*  72 */     super((Image)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImgJBIG2(int width, int height, byte[] data, byte[] globals) {
/*  83 */     super((URL)null);
/*  84 */     this.type = 36;
/*  85 */     this.originalType = 9;
/*  86 */     this.scaledHeight = height;
/*  87 */     setTop(this.scaledHeight);
/*  88 */     this.scaledWidth = width;
/*  89 */     setRight(this.scaledWidth);
/*  90 */     this.bpc = 1;
/*  91 */     this.colorspace = 1;
/*  92 */     this.rawData = data;
/*  93 */     this.plainWidth = getWidth();
/*  94 */     this.plainHeight = getHeight();
/*  95 */     if (globals != null) {
/*  96 */       this.global = globals;
/*     */       
/*     */       try {
/*  99 */         MessageDigest md = MessageDigest.getInstance("MD5");
/* 100 */         md.update(this.global);
/* 101 */         this.globalHash = md.digest();
/* 102 */       } catch (Exception exception) {}
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
/*     */   public byte[] getGlobalBytes() {
/* 114 */     return this.global;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getGlobalHash() {
/* 122 */     return this.globalHash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ImgJBIG2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */