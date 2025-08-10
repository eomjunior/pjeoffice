/*     */ package io.reactivex.internal.disposables;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
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
/*     */ public enum EmptyDisposable
/*     */   implements QueueDisposable<Object>
/*     */ {
/*  34 */   INSTANCE,
/*     */ 
/*     */ 
/*     */   
/*  38 */   NEVER;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  48 */     return (this == INSTANCE);
/*     */   }
/*     */   
/*     */   public static void complete(Observer<?> observer) {
/*  52 */     observer.onSubscribe((Disposable)INSTANCE);
/*  53 */     observer.onComplete();
/*     */   }
/*     */   
/*     */   public static void complete(MaybeObserver<?> observer) {
/*  57 */     observer.onSubscribe((Disposable)INSTANCE);
/*  58 */     observer.onComplete();
/*     */   }
/*     */   
/*     */   public static void error(Throwable e, Observer<?> observer) {
/*  62 */     observer.onSubscribe((Disposable)INSTANCE);
/*  63 */     observer.onError(e);
/*     */   }
/*     */   
/*     */   public static void complete(CompletableObserver observer) {
/*  67 */     observer.onSubscribe((Disposable)INSTANCE);
/*  68 */     observer.onComplete();
/*     */   }
/*     */   
/*     */   public static void error(Throwable e, CompletableObserver observer) {
/*  72 */     observer.onSubscribe((Disposable)INSTANCE);
/*  73 */     observer.onError(e);
/*     */   }
/*     */   
/*     */   public static void error(Throwable e, SingleObserver<?> observer) {
/*  77 */     observer.onSubscribe((Disposable)INSTANCE);
/*  78 */     observer.onError(e);
/*     */   }
/*     */   
/*     */   public static void error(Throwable e, MaybeObserver<?> observer) {
/*  82 */     observer.onSubscribe((Disposable)INSTANCE);
/*  83 */     observer.onError(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(Object value) {
/*  88 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(Object v1, Object v2) {
/*  93 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object poll() throws Exception {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int requestFusion(int mode) {
/* 114 */     return mode & 0x2;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/EmptyDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */