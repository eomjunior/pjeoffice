/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NameAbbreviator
/*     */ {
/*  31 */   private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getAbbreviator(String pattern) {
/*  47 */     if (pattern.length() > 0) {
/*     */ 
/*     */       
/*  50 */       String trimmed = pattern.trim();
/*     */       
/*  52 */       if (trimmed.length() == 0) {
/*  53 */         return DEFAULT;
/*     */       }
/*     */       
/*  56 */       int i = 0;
/*  57 */       if (trimmed.length() > 0) {
/*  58 */         if (trimmed.charAt(0) == '-') {
/*  59 */           i++;
/*     */         }
/*  61 */         for (; i < trimmed.length() && trimmed.charAt(i) >= '0' && trimmed.charAt(i) <= '9'; i++);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  68 */       if (i == trimmed.length()) {
/*  69 */         int elements = Integer.parseInt(trimmed);
/*  70 */         if (elements >= 0) {
/*  71 */           return new MaxElementAbbreviator(elements);
/*     */         }
/*  73 */         return new DropElementAbbreviator(-elements);
/*     */       } 
/*     */ 
/*     */       
/*  77 */       ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList(5);
/*     */ 
/*     */       
/*  80 */       int pos = 0;
/*     */       
/*  82 */       while (pos < trimmed.length() && pos >= 0) {
/*  83 */         int charCount, ellipsisPos = pos;
/*     */         
/*  85 */         if (trimmed.charAt(pos) == '*') {
/*  86 */           charCount = Integer.MAX_VALUE;
/*  87 */           ellipsisPos++;
/*     */         }
/*  89 */         else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
/*  90 */           charCount = trimmed.charAt(pos) - 48;
/*  91 */           ellipsisPos++;
/*     */         } else {
/*  93 */           charCount = 0;
/*     */         } 
/*     */ 
/*     */         
/*  97 */         char ellipsis = Character.MIN_VALUE;
/*     */         
/*  99 */         if (ellipsisPos < trimmed.length()) {
/* 100 */           ellipsis = trimmed.charAt(ellipsisPos);
/*     */           
/* 102 */           if (ellipsis == '.') {
/* 103 */             ellipsis = Character.MIN_VALUE;
/*     */           }
/*     */         } 
/*     */         
/* 107 */         fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
/* 108 */         pos = trimmed.indexOf(".", pos);
/*     */         
/* 110 */         if (pos == -1) {
/*     */           break;
/*     */         }
/*     */         
/* 114 */         pos++;
/*     */       } 
/*     */       
/* 117 */       return new PatternAbbreviator(fragments);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getDefaultAbbreviator() {
/* 132 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void abbreviate(int paramInt, StringBuffer paramStringBuffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NOPAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     public void abbreviate(int nameStart, StringBuffer buf) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MaxElementAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final int count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MaxElementAbbreviator(int count) {
/* 175 */       this.count = count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void abbreviate(int nameStart, StringBuffer buf) {
/* 188 */       int end = buf.length() - 1;
/*     */       
/* 190 */       String bufString = buf.toString();
/* 191 */       for (int i = this.count; i > 0; i--) {
/* 192 */         end = bufString.lastIndexOf(".", end - 1);
/*     */         
/* 194 */         if (end == -1 || end < nameStart) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 199 */       buf.delete(nameStart, end + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DropElementAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final int count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DropElementAbbreviator(int count) {
/* 218 */       this.count = count;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void abbreviate(int nameStart, StringBuffer buf) {
/* 228 */       int i = this.count; int pos;
/* 229 */       for (pos = buf.indexOf(".", nameStart); pos != -1; pos = buf.indexOf(".", pos + 1)) {
/* 230 */         if (--i == 0) {
/* 231 */           buf.delete(nameStart, pos + 1);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternAbbreviatorFragment
/*     */   {
/*     */     private final int charCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final char ellipsis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternAbbreviatorFragment(int charCount, char ellipsis) {
/* 262 */       this.charCount = charCount;
/* 263 */       this.ellipsis = ellipsis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int abbreviate(StringBuffer buf, int startPos) {
/* 274 */       int nextDot = buf.toString().indexOf(".", startPos);
/*     */       
/* 276 */       if (nextDot != -1) {
/* 277 */         if (nextDot - startPos > this.charCount) {
/* 278 */           buf.delete(startPos + this.charCount, nextDot);
/* 279 */           nextDot = startPos + this.charCount;
/*     */           
/* 281 */           if (this.ellipsis != '\000') {
/* 282 */             buf.insert(nextDot, this.ellipsis);
/* 283 */             nextDot++;
/*     */           } 
/*     */         } 
/*     */         
/* 287 */         nextDot++;
/*     */       } 
/*     */       
/* 290 */       return nextDot;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternAbbreviator(List fragments) {
/* 311 */       if (fragments.size() == 0) {
/* 312 */         throw new IllegalArgumentException("fragments must have at least one element");
/*     */       }
/*     */       
/* 315 */       this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[fragments.size()];
/* 316 */       fragments.toArray((Object[])this.fragments);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void abbreviate(int nameStart, StringBuffer buf) {
/* 329 */       int pos = nameStart;
/*     */       
/* 331 */       for (int i = 0; i < this.fragments.length - 1 && pos < buf.length(); i++) {
/* 332 */         pos = this.fragments[i].abbreviate(buf, pos);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 338 */       NameAbbreviator.PatternAbbreviatorFragment terminalFragment = this.fragments[this.fragments.length - 1];
/*     */       
/* 340 */       while (pos < buf.length() && pos >= 0)
/* 341 */         pos = terminalFragment.abbreviate(buf, pos); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/NameAbbreviator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */