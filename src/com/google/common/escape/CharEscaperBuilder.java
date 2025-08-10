/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class CharEscaperBuilder
/*     */ {
/*     */   private final Map<Character, String> map;
/*     */   
/*     */   private static class CharArrayDecorator
/*     */     extends CharEscaper
/*     */   {
/*     */     private final char[][] replacements;
/*     */     private final int replaceLength;
/*     */     
/*     */     CharArrayDecorator(char[][] replacements) {
/*  48 */       this.replacements = replacements;
/*  49 */       this.replaceLength = replacements.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String escape(String s) {
/*  58 */       int slen = s.length();
/*  59 */       for (int index = 0; index < slen; index++) {
/*  60 */         char c = s.charAt(index);
/*  61 */         if (c < this.replacements.length && this.replacements[c] != null) {
/*  62 */           return escapeSlow(s, index);
/*     */         }
/*     */       } 
/*  65 */       return s;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected char[] escape(char c) {
/*  71 */       return (c < this.replaceLength) ? this.replacements[c] : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private int max = -1;
/*     */ 
/*     */   
/*     */   public CharEscaperBuilder() {
/*  83 */     this.map = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscape(char c, String r) {
/*  89 */     this.map.put(Character.valueOf(c), (String)Preconditions.checkNotNull(r));
/*  90 */     if (c > this.max) {
/*  91 */       this.max = c;
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscapes(char[] cs, String r) {
/*  99 */     Preconditions.checkNotNull(r);
/* 100 */     for (char c : cs) {
/* 101 */       addEscape(c, r);
/*     */     }
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[][] toArray() {
/* 114 */     char[][] result = new char[this.max + 1][];
/* 115 */     for (Map.Entry<Character, String> entry : this.map.entrySet()) {
/* 116 */       result[((Character)entry.getKey()).charValue()] = ((String)entry.getValue()).toCharArray();
/*     */     }
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Escaper toEscaper() {
/* 128 */     return new CharArrayDecorator(toArray());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/escape/CharEscaperBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */