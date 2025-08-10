/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeEmitter;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeOnSubscribe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeCreate<T>
/*     */   extends Maybe<T>
/*     */ {
/*     */   final MaybeOnSubscribe<T> source;
/*     */   
/*     */   public MaybeCreate(MaybeOnSubscribe<T> source) {
/*  36 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  41 */     Emitter<T> parent = new Emitter<T>(observer);
/*  42 */     observer.onSubscribe(parent);
/*     */     
/*     */     try {
/*  45 */       this.source.subscribe(parent);
/*  46 */     } catch (Throwable ex) {
/*  47 */       Exceptions.throwIfFatal(ex);
/*  48 */       parent.onError(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class Emitter<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeEmitter<T>, Disposable {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     private static final long serialVersionUID = -2467358622224974244L;
/*     */     
/*     */     Emitter(MaybeObserver<? super T> downstream) {
/*  59 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  66 */       if (get() != DisposableHelper.DISPOSED) {
/*  67 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  68 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/*  70 */             if (value == null) {
/*  71 */               this.downstream.onError(new NullPointerException("onSuccess called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */             } else {
/*  73 */               this.downstream.onSuccess(value);
/*     */             } 
/*     */           } finally {
/*  76 */             if (d != null) {
/*  77 */               d.dispose();
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  86 */       if (!tryOnError(t)) {
/*  87 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/*  93 */       if (t == null) {
/*  94 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/*  96 */       if (get() != DisposableHelper.DISPOSED) {
/*  97 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  98 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/* 100 */             this.downstream.onError(t);
/*     */           } finally {
/* 102 */             if (d != null) {
/* 103 */               d.dispose();
/*     */             }
/*     */           } 
/* 106 */           return true;
/*     */         } 
/*     */       } 
/* 109 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       if (get() != DisposableHelper.DISPOSED) {
/* 115 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/* 116 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/* 118 */             this.downstream.onComplete();
/*     */           } finally {
/* 120 */             if (d != null) {
/* 121 */               d.dispose();
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDisposable(Disposable d) {
/* 130 */       DisposableHelper.set(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 135 */       setDisposable((Disposable)new CancellableDisposable(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 140 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 145 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 150 */       return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */