/*     */ package com.google.common.hash;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*     */   final long fn(long v, long x) {
/*  48 */     return v + x;
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
/*     */ 
/*     */   
/*     */   public void add(long x) {
/*  67 */     boolean uncontended = true; Striped64.Cell[] as; long b, v; int[] hc; Striped64.Cell a; int n;
/*  68 */     if (((as = this.cells) != null || !casBase(b = this.base, b + x)) && ((hc = threadHashCode.get()) == null || as == null || (n = as.length) < 1 || (a = as[n - 1 & hc[0]]) == null || 
/*     */ 
/*     */ 
/*     */       
/*  72 */       !(uncontended = a.cas(v = a.value, v + x)))) retryUpdate(x, hc, uncontended);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void increment() {
/*  79 */     add(1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void decrement() {
/*  84 */     add(-1L);
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
/*     */   public long sum() {
/*  96 */     long sum = this.base;
/*  97 */     Striped64.Cell[] as = this.cells;
/*  98 */     if (as != null) {
/*  99 */       int n = as.length;
/* 100 */       for (int i = 0; i < n; i++) {
/* 101 */         Striped64.Cell a = as[i];
/* 102 */         if (a != null) sum += a.value; 
/*     */       } 
/*     */     } 
/* 105 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 115 */     internalReset(0L);
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
/*     */   public long sumThenReset() {
/* 127 */     long sum = this.base;
/* 128 */     Striped64.Cell[] as = this.cells;
/* 129 */     this.base = 0L;
/* 130 */     if (as != null) {
/* 131 */       int n = as.length;
/* 132 */       for (int i = 0; i < n; i++) {
/* 133 */         Striped64.Cell a = as[i];
/* 134 */         if (a != null) {
/* 135 */           sum += a.value;
/* 136 */           a.value = 0L;
/*     */         } 
/*     */       } 
/*     */     } 
/* 140 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     return Long.toString(sum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 160 */     return sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 166 */     return (int)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 172 */     return (float)sum();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 178 */     return sum();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 182 */     s.defaultWriteObject();
/* 183 */     s.writeLong(sum());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 187 */     s.defaultReadObject();
/* 188 */     this.busy = 0;
/* 189 */     this.cells = null;
/* 190 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/LongAdder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */