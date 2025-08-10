/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BackpressureHelper
/*     */ {
/*     */   private BackpressureHelper() {
/*  25 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long addCap(long a, long b) {
/*  35 */     long u = a + b;
/*  36 */     if (u < 0L) {
/*  37 */       return Long.MAX_VALUE;
/*     */     }
/*  39 */     return u;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long multiplyCap(long a, long b) {
/*  49 */     long u = a * b;
/*  50 */     if ((a | b) >>> 31L != 0L && 
/*  51 */       u / a != b) {
/*  52 */       return Long.MAX_VALUE;
/*     */     }
/*     */     
/*  55 */     return u;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long add(AtomicLong requested, long n) {
/*     */     while (true) {
/*  67 */       long r = requested.get();
/*  68 */       if (r == Long.MAX_VALUE) {
/*  69 */         return Long.MAX_VALUE;
/*     */       }
/*  71 */       long u = addCap(r, n);
/*  72 */       if (requested.compareAndSet(r, u)) {
/*  73 */         return r;
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
/*     */ 
/*     */   
/*     */   public static long addCancel(AtomicLong requested, long n) {
/*     */     while (true) {
/*  88 */       long r = requested.get();
/*  89 */       if (r == Long.MIN_VALUE) {
/*  90 */         return Long.MIN_VALUE;
/*     */       }
/*  92 */       if (r == Long.MAX_VALUE) {
/*  93 */         return Long.MAX_VALUE;
/*     */       }
/*  95 */       long u = addCap(r, n);
/*  96 */       if (requested.compareAndSet(r, u)) {
/*  97 */         return r;
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
/*     */   public static long produced(AtomicLong requested, long n) {
/*     */     while (true) {
/* 110 */       long current = requested.get();
/* 111 */       if (current == Long.MAX_VALUE) {
/* 112 */         return Long.MAX_VALUE;
/*     */       }
/* 114 */       long update = current - n;
/* 115 */       if (update < 0L) {
/* 116 */         RxJavaPlugins.onError(new IllegalStateException("More produced than requested: " + update));
/* 117 */         update = 0L;
/*     */       } 
/* 119 */       if (requested.compareAndSet(current, update)) {
/* 120 */         return update;
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
/*     */   
/*     */   public static long producedCancel(AtomicLong requested, long n) {
/*     */     while (true) {
/* 134 */       long current = requested.get();
/* 135 */       if (current == Long.MIN_VALUE) {
/* 136 */         return Long.MIN_VALUE;
/*     */       }
/* 138 */       if (current == Long.MAX_VALUE) {
/* 139 */         return Long.MAX_VALUE;
/*     */       }
/* 141 */       long update = current - n;
/* 142 */       if (update < 0L) {
/* 143 */         RxJavaPlugins.onError(new IllegalStateException("More produced than requested: " + update));
/* 144 */         update = 0L;
/*     */       } 
/* 146 */       if (requested.compareAndSet(current, update))
/* 147 */         return update; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/BackpressureHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */