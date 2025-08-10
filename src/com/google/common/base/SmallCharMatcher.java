/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class SmallCharMatcher
/*     */   extends CharMatcher.NamedFastMatcher
/*     */ {
/*     */   static final int MAX_SIZE = 1023;
/*     */   private final char[] table;
/*     */   private final boolean containsZero;
/*     */   private final long filter;
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.5D;
/*     */   
/*     */   private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
/*  39 */     super(description);
/*  40 */     this.table = table;
/*  41 */     this.filter = filter;
/*  42 */     this.containsZero = containsZero;
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
/*     */   static int smear(int hashCode) {
/*  57 */     return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
/*     */   }
/*     */   
/*     */   private boolean checkFilter(int c) {
/*  61 */     return (1L == (0x1L & this.filter >> c));
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
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/*  77 */     if (setSize == 1) {
/*  78 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*  82 */     int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/*  83 */     while (tableSize * 0.5D < setSize) {
/*  84 */       tableSize <<= 1;
/*     */     }
/*  86 */     return tableSize;
/*     */   }
/*     */ 
/*     */   
/*     */   static CharMatcher from(BitSet chars, String description) {
/*  91 */     long filter = 0L;
/*  92 */     int size = chars.cardinality();
/*  93 */     boolean containsZero = chars.get(0);
/*     */     
/*  95 */     char[] table = new char[chooseTableSize(size)];
/*  96 */     int mask = table.length - 1; int c;
/*  97 */     for (c = chars.nextSetBit(0); c != -1; ) {
/*     */       
/*  99 */       filter |= 1L << c;
/* 100 */       int index = smear(c) & mask;
/*     */       
/*     */       for (;; c = chars.nextSetBit(c + 1)) {
/* 103 */         if (table[index] == '\000') {
/* 104 */           table[index] = (char)c;
/*     */         }
/*     */         else {
/*     */           
/* 108 */           index = index + 1 & mask; continue;
/*     */         } 
/*     */       } 
/* 111 */     }  return new SmallCharMatcher(table, filter, containsZero, description);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(char c) {
/* 116 */     if (c == '\000') {
/* 117 */       return this.containsZero;
/*     */     }
/* 119 */     if (!checkFilter(c)) {
/* 120 */       return false;
/*     */     }
/* 122 */     int mask = this.table.length - 1;
/* 123 */     int startingIndex = smear(c) & mask;
/* 124 */     int index = startingIndex;
/*     */     while (true) {
/* 126 */       if (this.table[index] == '\000')
/* 127 */         return false; 
/* 128 */       if (this.table[index] == c) {
/* 129 */         return true;
/*     */       }
/* 131 */       index = index + 1 & mask;
/*     */ 
/*     */       
/* 134 */       if (index == startingIndex)
/* 135 */         return false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void setBits(BitSet table) {
/* 140 */     if (this.containsZero) {
/* 141 */       table.set(0);
/*     */     }
/* 143 */     for (char c : this.table) {
/* 144 */       if (c != '\000')
/* 145 */         table.set(c); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/SmallCharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */