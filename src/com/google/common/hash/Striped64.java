/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ import javax.annotation.CheckForNull;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ abstract class Striped64
/*     */   extends Number
/*     */ {
/*     */   static final class Cell
/*     */   {
/*     */     volatile long p0;
/*     */     volatile long p1;
/*     */     volatile long p2;
/*     */     volatile long p3;
/*     */     volatile long p4;
/*     */     volatile long p5;
/*     */     volatile long p6;
/*     */     volatile long value;
/*     */     volatile long q0;
/*     */     volatile long q1;
/*     */     volatile long q2;
/*     */     volatile long q3;
/*     */     volatile long q4;
/*     */     volatile long q5;
/*     */     volatile long q6;
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long valueOffset;
/*     */     
/*     */     Cell(long x) {
/* 103 */       this.value = x;
/*     */     }
/*     */     
/*     */     final boolean cas(long cmp, long val) {
/* 107 */       return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       try {
/* 116 */         UNSAFE = Striped64.getUnsafe();
/* 117 */         Class<?> ak = Cell.class;
/* 118 */         valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
/* 119 */       } catch (Exception e) {
/* 120 */         throw new Error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   static final ThreadLocal<int[]> threadHashCode = (ThreadLocal)new ThreadLocal<>();
/*     */ 
/*     */   
/* 133 */   static final Random rng = new Random();
/*     */ 
/*     */   
/* 136 */   static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   volatile transient Cell[] cells;
/*     */ 
/*     */   
/*     */   volatile transient long base;
/*     */   
/*     */   volatile transient int busy;
/*     */   
/*     */   private static final Unsafe UNSAFE;
/*     */   
/*     */   private static final long baseOffset;
/*     */   
/*     */   private static final long busyOffset;
/*     */ 
/*     */   
/*     */   final boolean casBase(long cmp, long val) {
/* 155 */     return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean casBusy() {
/* 160 */     return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void retryUpdate(long x, @CheckForNull int[] hc, boolean wasUncontended) {
/*     */     int h;
/* 184 */     if (hc == null)
/* 185 */     { threadHashCode.set(hc = new int[1]);
/* 186 */       int r = rng.nextInt();
/* 187 */       h = hc[0] = (r == 0) ? 1 : r; }
/* 188 */     else { h = hc[0]; }
/* 189 */      boolean collide = false;
/*     */     
/*     */     while (true) {
/*     */       Cell[] as;
/*     */       
/*     */       int n;
/* 195 */       if ((as = this.cells) != null && (n = as.length) > 0) {
/* 196 */         Cell a; if ((a = as[n - 1 & h]) == null)
/* 197 */         { if (this.busy == 0) {
/* 198 */             Cell r = new Cell(x);
/* 199 */             if (this.busy == 0 && casBusy()) {
/* 200 */               boolean created = false; try {
/*     */                 Cell[] rs;
/*     */                 int m;
/*     */                 int j;
/* 204 */                 if ((rs = this.cells) != null && (m = rs.length) > 0 && rs[j = m - 1 & h] == null) {
/* 205 */                   rs[j] = r;
/* 206 */                   created = true;
/*     */                 } 
/*     */               } finally {
/* 209 */                 this.busy = 0;
/*     */               } 
/* 211 */               if (created)
/*     */                 break;  continue;
/*     */             } 
/*     */           } 
/* 215 */           collide = false; }
/* 216 */         else if (!wasUncontended)
/* 217 */         { wasUncontended = true; }
/* 218 */         else { long l; if (a.cas(l = a.value, fn(l, x)))
/* 219 */             break;  if (n >= NCPU || this.cells != as) { collide = false; }
/* 220 */           else if (!collide) { collide = true; }
/* 221 */           else if (this.busy == 0 && casBusy())
/*     */           { try {
/* 223 */               if (this.cells == as) {
/* 224 */                 Cell[] rs = new Cell[n << 1];
/* 225 */                 for (int i = 0; i < n; ) { rs[i] = as[i]; i++; }
/* 226 */                  this.cells = rs;
/*     */               } 
/*     */             } finally {
/* 229 */               this.busy = 0;
/*     */             } 
/* 231 */             collide = false; continue; }
/*     */            }
/*     */         
/* 234 */         h ^= h << 13;
/* 235 */         h ^= h >>> 17;
/* 236 */         h ^= h << 5;
/* 237 */         hc[0] = h; continue;
/* 238 */       }  if (this.busy == 0 && this.cells == as && casBusy())
/* 239 */       { boolean init = false;
/*     */         try {
/* 241 */           if (this.cells == as) {
/* 242 */             Cell[] rs = new Cell[2];
/* 243 */             rs[h & 0x1] = new Cell(x);
/* 244 */             this.cells = rs;
/* 245 */             init = true;
/*     */           } 
/*     */         } finally {
/* 248 */           this.busy = 0;
/*     */         } 
/* 250 */         if (init)
/* 251 */           break;  continue; }  long v; if (casBase(v = this.base, fn(v, x)))
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   final void internalReset(long initialValue) {
/* 257 */     Cell[] as = this.cells;
/* 258 */     this.base = initialValue;
/* 259 */     if (as != null) {
/* 260 */       int n = as.length;
/* 261 */       for (int i = 0; i < n; i++) {
/* 262 */         Cell a = as[i];
/* 263 */         if (a != null) a.value = initialValue;
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 275 */       UNSAFE = getUnsafe();
/* 276 */       Class<?> sk = Striped64.class;
/* 277 */       baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
/* 278 */       busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
/* 279 */     } catch (Exception e) {
/* 280 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Unsafe getUnsafe() {
/*     */     try {
/* 292 */       return Unsafe.getUnsafe();
/* 293 */     } catch (SecurityException securityException) {
/*     */       
/*     */       try {
/* 296 */         return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */             {
/*     */               public Unsafe run() throws Exception
/*     */               {
/* 300 */                 Class<Unsafe> k = Unsafe.class;
/* 301 */                 for (Field f : k.getDeclaredFields()) {
/* 302 */                   f.setAccessible(true);
/* 303 */                   Object x = f.get(null);
/* 304 */                   if (k.isInstance(x)) return k.cast(x); 
/*     */                 } 
/* 306 */                 throw new NoSuchFieldError("the Unsafe");
/*     */               }
/*     */             });
/* 309 */       } catch (PrivilegedActionException e) {
/* 310 */         throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract long fn(long paramLong1, long paramLong2);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Striped64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */