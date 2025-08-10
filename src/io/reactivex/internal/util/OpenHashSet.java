/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OpenHashSet<T>
/*     */ {
/*     */   private static final int INT_PHI = -1640531527;
/*     */   final float loadFactor;
/*     */   int mask;
/*     */   int size;
/*     */   int maxSize;
/*     */   T[] keys;
/*     */   
/*     */   public OpenHashSet() {
/*  37 */     this(16, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OpenHashSet(int capacity) {
/*  45 */     this(capacity, 0.75F);
/*     */   }
/*     */ 
/*     */   
/*     */   public OpenHashSet(int capacity, float loadFactor) {
/*  50 */     this.loadFactor = loadFactor;
/*  51 */     int c = Pow2.roundToPowerOfTwo(capacity);
/*  52 */     this.mask = c - 1;
/*  53 */     this.maxSize = (int)(loadFactor * c);
/*  54 */     this.keys = (T[])new Object[c];
/*     */   }
/*     */   
/*     */   public boolean add(T value) {
/*  58 */     T[] a = this.keys;
/*  59 */     int m = this.mask;
/*     */     
/*  61 */     int pos = mix(value.hashCode()) & m;
/*  62 */     T curr = a[pos];
/*  63 */     if (curr != null) {
/*  64 */       if (curr.equals(value)) {
/*  65 */         return false;
/*     */       }
/*     */       while (true) {
/*  68 */         pos = pos + 1 & m;
/*  69 */         curr = a[pos];
/*  70 */         if (curr == null) {
/*     */           break;
/*     */         }
/*  73 */         if (curr.equals(value)) {
/*  74 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*  78 */     a[pos] = value;
/*  79 */     if (++this.size >= this.maxSize) {
/*  80 */       rehash();
/*     */     }
/*  82 */     return true;
/*     */   }
/*     */   public boolean remove(T value) {
/*  85 */     T[] a = this.keys;
/*  86 */     int m = this.mask;
/*  87 */     int pos = mix(value.hashCode()) & m;
/*  88 */     T curr = a[pos];
/*  89 */     if (curr == null) {
/*  90 */       return false;
/*     */     }
/*  92 */     if (curr.equals(value)) {
/*  93 */       return removeEntry(pos, a, m);
/*     */     }
/*     */     while (true) {
/*  96 */       pos = pos + 1 & m;
/*  97 */       curr = a[pos];
/*  98 */       if (curr == null) {
/*  99 */         return false;
/*     */       }
/* 101 */       if (curr.equals(value)) {
/* 102 */         return removeEntry(pos, a, m);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean removeEntry(int pos, T[] a, int m) {
/* 108 */     this.size--;
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       T curr;
/*     */       
/* 114 */       int last = pos;
/* 115 */       pos = pos + 1 & m;
/*     */       while (true) {
/* 117 */         curr = a[pos];
/* 118 */         if (curr == null) {
/* 119 */           a[last] = null;
/* 120 */           return true;
/*     */         } 
/* 122 */         int slot = mix(curr.hashCode()) & m;
/*     */         
/* 124 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos)) {
/*     */           break;
/*     */         }
/*     */         
/* 128 */         pos = pos + 1 & m;
/*     */       } 
/* 130 */       a[last] = curr;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void rehash() {
/* 136 */     T[] a = this.keys;
/* 137 */     int i = a.length;
/* 138 */     int newCap = i << 1;
/* 139 */     int m = newCap - 1;
/*     */     
/* 141 */     T[] b = (T[])new Object[newCap];
/*     */     
/* 143 */     for (int j = this.size; j-- != 0; ) {
/* 144 */       while (a[--i] == null);
/* 145 */       int pos = mix(a[i].hashCode()) & m;
/* 146 */       if (b[pos] != null) {
/*     */         do {
/* 148 */           pos = pos + 1 & m;
/* 149 */         } while (b[pos] != null);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 154 */       b[pos] = a[i];
/*     */     } 
/*     */     
/* 157 */     this.mask = m;
/* 158 */     this.maxSize = (int)(newCap * this.loadFactor);
/* 159 */     this.keys = b;
/*     */   }
/*     */   
/*     */   static int mix(int x) {
/* 163 */     int h = x * -1640531527;
/* 164 */     return h ^ h >>> 16;
/*     */   }
/*     */   
/*     */   public Object[] keys() {
/* 168 */     return (Object[])this.keys;
/*     */   }
/*     */   
/*     */   public int size() {
/* 172 */     return this.size;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/OpenHashSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */