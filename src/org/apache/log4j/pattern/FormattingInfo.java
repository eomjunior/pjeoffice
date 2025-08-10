/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FormattingInfo
/*     */ {
/*  34 */   private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, 2147483647);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int minLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftAlign;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattingInfo(boolean leftAlign, int minLength, int maxLength) {
/*  64 */     this.leftAlign = leftAlign;
/*  65 */     this.minLength = minLength;
/*  66 */     this.maxLength = maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FormattingInfo getDefault() {
/*  75 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftAligned() {
/*  84 */     return this.leftAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinLength() {
/*  93 */     return this.minLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLength() {
/* 102 */     return this.maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(int fieldStart, StringBuffer buffer) {
/* 113 */     int rawLength = buffer.length() - fieldStart;
/*     */     
/* 115 */     if (rawLength > this.maxLength) {
/* 116 */       buffer.delete(fieldStart, buffer.length() - this.maxLength);
/* 117 */     } else if (rawLength < this.minLength) {
/* 118 */       if (this.leftAlign) {
/* 119 */         int fieldEnd = buffer.length();
/* 120 */         buffer.setLength(fieldStart + this.minLength);
/*     */         
/* 122 */         for (int i = fieldEnd; i < buffer.length(); i++) {
/* 123 */           buffer.setCharAt(i, ' ');
/*     */         }
/*     */       } else {
/* 126 */         int padLength = this.minLength - rawLength;
/*     */         
/* 128 */         for (; padLength > 8; padLength -= 8) {
/* 129 */           buffer.insert(fieldStart, SPACES);
/*     */         }
/*     */         
/* 132 */         buffer.insert(fieldStart, SPACES, 0, padLength);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/FormattingInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */