/*     */ package io.reactivex.internal.disposables;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.ProtocolViolationException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum DisposableHelper
/*     */   implements Disposable
/*     */ {
/*  30 */   DISPOSED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDisposed(Disposable d) {
/*  39 */     return (d == DISPOSED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean set(AtomicReference<Disposable> field, Disposable d) {
/*     */     while (true) {
/*  50 */       Disposable current = field.get();
/*  51 */       if (current == DISPOSED) {
/*  52 */         if (d != null) {
/*  53 */           d.dispose();
/*     */         }
/*  55 */         return false;
/*     */       } 
/*  57 */       if (field.compareAndSet(current, d)) {
/*  58 */         if (current != null) {
/*  59 */           current.dispose();
/*     */         }
/*  61 */         return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean setOnce(AtomicReference<Disposable> field, Disposable d) {
/*  78 */     ObjectHelper.requireNonNull(d, "d is null");
/*  79 */     if (!field.compareAndSet(null, d)) {
/*  80 */       d.dispose();
/*  81 */       if (field.get() != DISPOSED) {
/*  82 */         reportDisposableSet();
/*     */       }
/*  84 */       return false;
/*     */     } 
/*  86 */     return true;
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
/*     */   public static boolean replace(AtomicReference<Disposable> field, Disposable d) {
/*     */     while (true) {
/*  99 */       Disposable current = field.get();
/* 100 */       if (current == DISPOSED) {
/* 101 */         if (d != null) {
/* 102 */           d.dispose();
/*     */         }
/* 104 */         return false;
/*     */       } 
/* 106 */       if (field.compareAndSet(current, d)) {
/* 107 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean dispose(AtomicReference<Disposable> field) {
/* 118 */     Disposable current = field.get();
/* 119 */     Disposable d = DISPOSED;
/* 120 */     if (current != d) {
/* 121 */       current = field.getAndSet(d);
/* 122 */       if (current != d) {
/* 123 */         if (current != null) {
/* 124 */           current.dispose();
/*     */         }
/* 126 */         return true;
/*     */       } 
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validate(Disposable current, Disposable next) {
/* 140 */     if (next == null) {
/* 141 */       RxJavaPlugins.onError(new NullPointerException("next is null"));
/* 142 */       return false;
/*     */     } 
/* 144 */     if (current != null) {
/* 145 */       next.dispose();
/* 146 */       reportDisposableSet();
/* 147 */       return false;
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reportDisposableSet() {
/* 156 */     RxJavaPlugins.onError((Throwable)new ProtocolViolationException("Disposable already set!"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean trySet(AtomicReference<Disposable> field, Disposable d) {
/* 167 */     if (!field.compareAndSet(null, d)) {
/* 168 */       if (field.get() == DISPOSED) {
/* 169 */         d.dispose();
/*     */       }
/* 171 */       return false;
/*     */     } 
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 183 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/DisposableHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */