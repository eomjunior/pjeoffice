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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Glyph
/*     */ {
/*     */   public final int code;
/*     */   public final int width;
/*     */   public final String chars;
/*     */   
/*     */   public Glyph(int code, int width, String chars) {
/*  68 */     this.code = code;
/*  69 */     this.width = width;
/*  70 */     this.chars = chars;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  75 */     int prime = 31;
/*  76 */     int result = 1;
/*  77 */     result = 31 * result + ((this.chars == null) ? 0 : this.chars.hashCode());
/*  78 */     result = 31 * result + this.code;
/*  79 */     result = 31 * result + this.width;
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  85 */     if (this == obj)
/*  86 */       return true; 
/*  87 */     if (obj == null)
/*  88 */       return false; 
/*  89 */     if (getClass() != obj.getClass())
/*  90 */       return false; 
/*  91 */     Glyph other = (Glyph)obj;
/*  92 */     if (this.chars == null) {
/*  93 */       if (other.chars != null)
/*  94 */         return false; 
/*  95 */     } else if (!this.chars.equals(other.chars)) {
/*  96 */       return false;
/*  97 */     }  if (this.code != other.code)
/*  98 */       return false; 
/*  99 */     if (this.width != other.width)
/* 100 */       return false; 
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return Glyph.class.getSimpleName() + " [id=" + this.code + ", width=" + this.width + ", chars=" + this.chars + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Glyph.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */