/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class MaybePeek<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Consumer<? super Disposable> onSubscribeCall;
/*     */   final Consumer<? super T> onSuccessCall;
/*     */   final Consumer<? super Throwable> onErrorCall;
/*     */   final Action onCompleteCall;
/*     */   final Action onAfterTerminate;
/*     */   final Action onDisposeCall;
/*     */   
/*     */   public MaybePeek(MaybeSource<T> source, Consumer<? super Disposable> onSubscribeCall, Consumer<? super T> onSuccessCall, Consumer<? super Throwable> onErrorCall, Action onCompleteCall, Action onAfterTerminate, Action onDispose) {
/*  45 */     super(source);
/*  46 */     this.onSubscribeCall = onSubscribeCall;
/*  47 */     this.onSuccessCall = onSuccessCall;
/*  48 */     this.onErrorCall = onErrorCall;
/*  49 */     this.onCompleteCall = onCompleteCall;
/*  50 */     this.onAfterTerminate = onAfterTerminate;
/*  51 */     this.onDisposeCall = onDispose;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  56 */     this.source.subscribe(new MaybePeekObserver<T>(observer, this));
/*     */   }
/*     */   
/*     */   static final class MaybePeekObserver<T>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final MaybePeek<T> parent;
/*     */     Disposable upstream;
/*     */     
/*     */     MaybePeekObserver(MaybeObserver<? super T> actual, MaybePeek<T> parent) {
/*  67 */       this.downstream = actual;
/*  68 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*     */       try {
/*  74 */         this.parent.onDisposeCall.run();
/*  75 */       } catch (Throwable ex) {
/*  76 */         Exceptions.throwIfFatal(ex);
/*  77 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */       
/*  80 */       this.upstream.dispose();
/*  81 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  86 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  91 */       if (DisposableHelper.validate(this.upstream, d)) {
/*     */         try {
/*  93 */           this.parent.onSubscribeCall.accept(d);
/*  94 */         } catch (Throwable ex) {
/*  95 */           Exceptions.throwIfFatal(ex);
/*  96 */           d.dispose();
/*  97 */           this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  98 */           EmptyDisposable.error(ex, this.downstream);
/*     */           
/*     */           return;
/*     */         } 
/* 102 */         this.upstream = d;
/*     */         
/* 104 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 110 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/*     */       try {
/* 114 */         this.parent.onSuccessCall.accept(value);
/* 115 */       } catch (Throwable ex) {
/* 116 */         Exceptions.throwIfFatal(ex);
/* 117 */         onErrorInner(ex);
/*     */         return;
/*     */       } 
/* 120 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/* 122 */       this.downstream.onSuccess(value);
/*     */       
/* 124 */       onAfterTerminate();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 129 */       if (this.upstream == DisposableHelper.DISPOSED) {
/* 130 */         RxJavaPlugins.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 134 */       onErrorInner(e);
/*     */     }
/*     */     void onErrorInner(Throwable e) {
/*     */       CompositeException compositeException;
/*     */       try {
/* 139 */         this.parent.onErrorCall.accept(e);
/* 140 */       } catch (Throwable ex) {
/* 141 */         Exceptions.throwIfFatal(ex);
/* 142 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */       } 
/*     */       
/* 145 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/* 147 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/* 149 */       onAfterTerminate();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 154 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 159 */         this.parent.onCompleteCall.run();
/* 160 */       } catch (Throwable ex) {
/* 161 */         Exceptions.throwIfFatal(ex);
/* 162 */         onErrorInner(ex);
/*     */         return;
/*     */       } 
/* 165 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/* 167 */       this.downstream.onComplete();
/*     */       
/* 169 */       onAfterTerminate();
/*     */     }
/*     */     
/*     */     void onAfterTerminate() {
/*     */       try {
/* 174 */         this.parent.onAfterTerminate.run();
/* 175 */       } catch (Throwable ex) {
/* 176 */         Exceptions.throwIfFatal(ex);
/* 177 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybePeek.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */