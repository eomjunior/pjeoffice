/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class CharEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD_MULTIPLIER = 2;
/*     */   
/*     */   public String escape(String string) {
/*  57 */     Preconditions.checkNotNull(string);
/*     */     
/*  59 */     int length = string.length();
/*  60 */     for (int index = 0; index < length; index++) {
/*  61 */       if (escape(string.charAt(index)) != null) {
/*  62 */         return escapeSlow(string, index);
/*     */       }
/*     */     } 
/*  65 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected abstract char[] escape(char paramChar);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String escapeSlow(String s, int index) {
/*  98 */     int slen = s.length();
/*     */ 
/*     */     
/* 101 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 102 */     int destSize = dest.length;
/* 103 */     int destIndex = 0;
/* 104 */     int lastEscape = 0;
/*     */ 
/*     */ 
/*     */     
/* 108 */     for (; index < slen; index++) {
/*     */ 
/*     */       
/* 111 */       char[] r = escape(s.charAt(index));
/*     */ 
/*     */       
/* 114 */       if (r != null) {
/*     */ 
/*     */ 
/*     */         
/* 118 */         int rlen = r.length;
/* 119 */         int charsSkipped = index - lastEscape;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 124 */         int sizeNeeded = destIndex + charsSkipped + rlen;
/* 125 */         if (destSize < sizeNeeded) {
/* 126 */           destSize = sizeNeeded + 2 * (slen - index);
/* 127 */           dest = growBuffer(dest, destIndex, destSize);
/*     */         } 
/*     */ 
/*     */         
/* 131 */         if (charsSkipped > 0) {
/* 132 */           s.getChars(lastEscape, index, dest, destIndex);
/* 133 */           destIndex += charsSkipped;
/*     */         } 
/*     */ 
/*     */         
/* 137 */         if (rlen > 0) {
/* 138 */           System.arraycopy(r, 0, dest, destIndex, rlen);
/* 139 */           destIndex += rlen;
/*     */         } 
/* 141 */         lastEscape = index + 1;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     int charsLeft = slen - lastEscape;
/* 146 */     if (charsLeft > 0) {
/* 147 */       int sizeNeeded = destIndex + charsLeft;
/* 148 */       if (destSize < sizeNeeded)
/*     */       {
/*     */         
/* 151 */         dest = growBuffer(dest, destIndex, sizeNeeded);
/*     */       }
/* 153 */       s.getChars(lastEscape, slen, dest, destIndex);
/* 154 */       destIndex = sizeNeeded;
/*     */     } 
/* 156 */     return new String(dest, 0, destIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 164 */     if (size < 0) {
/* 165 */       throw new AssertionError("Cannot increase internal buffer any further");
/*     */     }
/* 167 */     char[] copy = new char[size];
/* 168 */     if (index > 0) {
/* 169 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 171 */     return copy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/escape/CharEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */