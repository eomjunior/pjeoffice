/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExtendedColor
/*     */   extends BaseColor
/*     */ {
/*     */   private static final long serialVersionUID = 2722660170712380080L;
/*     */   public static final int TYPE_RGB = 0;
/*     */   public static final int TYPE_GRAY = 1;
/*     */   public static final int TYPE_CMYK = 2;
/*     */   public static final int TYPE_SEPARATION = 3;
/*     */   public static final int TYPE_PATTERN = 4;
/*     */   public static final int TYPE_SHADING = 5;
/*     */   public static final int TYPE_DEVICEN = 6;
/*     */   public static final int TYPE_LAB = 7;
/*     */   protected int type;
/*     */   
/*     */   public ExtendedColor(int type) {
/*  79 */     super(0, 0, 0);
/*  80 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedColor(int type, float red, float green, float blue) {
/*  91 */     super(normalize(red), normalize(green), normalize(blue));
/*  92 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedColor(int type, int red, int green, int blue, int alpha) {
/* 103 */     super(normalize(red / 255.0F), normalize(green / 255.0F), normalize(blue / 255.0F), normalize(alpha / 255.0F));
/* 104 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 112 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getType(BaseColor color) {
/* 121 */     if (color instanceof ExtendedColor)
/* 122 */       return ((ExtendedColor)color).getType(); 
/* 123 */     return 0;
/*     */   }
/*     */   
/*     */   static final float normalize(float value) {
/* 127 */     if (value < 0.0F)
/* 128 */       return 0.0F; 
/* 129 */     if (value > 1.0F)
/* 130 */       return 1.0F; 
/* 131 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ExtendedColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */