/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleEmitter;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleOnSubscribe;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Cancellable;
/*     */ import io.reactivex.internal.disposables.CancellableDisposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class SingleCreate<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final SingleOnSubscribe<T> source;
/*     */   
/*     */   public SingleCreate(SingleOnSubscribe<T> source) {
/*  30 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  35 */     Emitter<T> parent = new Emitter<T>(observer);
/*  36 */     observer.onSubscribe(parent);
/*     */     
/*     */     try {
/*  39 */       this.source.subscribe(parent);
/*  40 */     } catch (Throwable ex) {
/*  41 */       Exceptions.throwIfFatal(ex);
/*  42 */       parent.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Emitter<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleEmitter<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2467358622224974244L;
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     Emitter(SingleObserver<? super T> downstream) {
/*  55 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  60 */       if (get() != DisposableHelper.DISPOSED) {
/*  61 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  62 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/*  64 */             if (value == null) {
/*  65 */               this.downstream.onError(new NullPointerException("onSuccess called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */             } else {
/*  67 */               this.downstream.onSuccess(value);
/*     */             } 
/*     */           } finally {
/*  70 */             if (d != null) {
/*  71 */               d.dispose();
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  80 */       if (!tryOnError(t)) {
/*  81 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/*  87 */       if (t == null) {
/*  88 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/*  90 */       if (get() != DisposableHelper.DISPOSED) {
/*  91 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  92 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/*  94 */             this.downstream.onError(t);
/*     */           } finally {
/*  96 */             if (d != null) {
/*  97 */               d.dispose();
/*     */             }
/*     */           } 
/* 100 */           return true;
/*     */         } 
/*     */       } 
/* 103 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDisposable(Disposable d) {
/* 108 */       DisposableHelper.set(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 113 */       setDisposable((Disposable)new CancellableDisposable(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 118 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 123 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 128 */       return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */