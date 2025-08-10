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
/*     */ 
/*     */ 
/*     */ public class CMYKColor
/*     */   extends ExtendedColor
/*     */ {
/*     */   private static final long serialVersionUID = 5940378778276468452L;
/*     */   float cyan;
/*     */   float magenta;
/*     */   float yellow;
/*     */   float black;
/*     */   
/*     */   public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack) {
/*  66 */     this(intCyan / 255.0F, intMagenta / 255.0F, intYellow / 255.0F, intBlack / 255.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack) {
/*  77 */     super(2, 1.0F - floatCyan - floatBlack, 1.0F - floatMagenta - floatBlack, 1.0F - floatYellow - floatBlack);
/*  78 */     this.cyan = normalize(floatCyan);
/*  79 */     this.magenta = normalize(floatMagenta);
/*  80 */     this.yellow = normalize(floatYellow);
/*  81 */     this.black = normalize(floatBlack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCyan() {
/*  88 */     return this.cyan;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMagenta() {
/*  95 */     return this.magenta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYellow() {
/* 102 */     return this.yellow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlack() {
/* 109 */     return this.black;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 113 */     if (!(obj instanceof CMYKColor))
/* 114 */       return false; 
/* 115 */     CMYKColor c2 = (CMYKColor)obj;
/* 116 */     return (this.cyan == c2.cyan && this.magenta == c2.magenta && this.yellow == c2.yellow && this.black == c2.black);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 120 */     return Float.floatToIntBits(this.cyan) ^ Float.floatToIntBits(this.magenta) ^ Float.floatToIntBits(this.yellow) ^ Float.floatToIntBits(this.black);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/CMYKColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */