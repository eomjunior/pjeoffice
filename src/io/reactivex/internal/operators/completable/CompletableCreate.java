/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableEmitter;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableOnSubscribe;
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
/*     */ public final class CompletableCreate
/*     */   extends Completable
/*     */ {
/*     */   final CompletableOnSubscribe source;
/*     */   
/*     */   public CompletableCreate(CompletableOnSubscribe source) {
/*  30 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  35 */     Emitter parent = new Emitter(observer);
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
/*     */   static final class Emitter
/*     */     extends AtomicReference<Disposable>
/*     */     implements CompletableEmitter, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2467358622224974244L;
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     Emitter(CompletableObserver downstream) {
/*  55 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  60 */       if (get() != DisposableHelper.DISPOSED) {
/*  61 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  62 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/*  64 */             this.downstream.onComplete();
/*     */           } finally {
/*  66 */             if (d != null) {
/*  67 */               d.dispose();
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  76 */       if (!tryOnError(t)) {
/*  77 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/*  83 */       if (t == null) {
/*  84 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/*  86 */       if (get() != DisposableHelper.DISPOSED) {
/*  87 */         Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/*  88 */         if (d != DisposableHelper.DISPOSED) {
/*     */           try {
/*  90 */             this.downstream.onError(t);
/*     */           } finally {
/*  92 */             if (d != null) {
/*  93 */               d.dispose();
/*     */             }
/*     */           } 
/*  96 */           return true;
/*     */         } 
/*     */       } 
/*  99 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDisposable(Disposable d) {
/* 104 */       DisposableHelper.set(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 109 */       setDisposable((Disposable)new CancellableDisposable(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 114 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 119 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 124 */       return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */