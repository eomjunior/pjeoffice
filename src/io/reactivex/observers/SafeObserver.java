/*     */ package io.reactivex.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
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
/*     */ 
/*     */ public final class SafeObserver<T>
/*     */   implements Observer<T>, Disposable
/*     */ {
/*     */   final Observer<? super T> downstream;
/*     */   Disposable upstream;
/*     */   boolean done;
/*     */   
/*     */   public SafeObserver(@NonNull Observer<? super T> downstream) {
/*  41 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(@NonNull Disposable d) {
/*  46 */     if (DisposableHelper.validate(this.upstream, d)) {
/*  47 */       this.upstream = d;
/*     */       try {
/*  49 */         this.downstream.onSubscribe(this);
/*  50 */       } catch (Throwable e) {
/*  51 */         Exceptions.throwIfFatal(e);
/*  52 */         this.done = true;
/*     */         
/*     */         try {
/*  55 */           d.dispose();
/*  56 */         } catch (Throwable e1) {
/*  57 */           Exceptions.throwIfFatal(e1);
/*  58 */           RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, e1 }));
/*     */           return;
/*     */         } 
/*  61 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  68 */     this.upstream.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  73 */     return this.upstream.isDisposed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(@NonNull T t) {
/*  78 */     if (this.done) {
/*     */       return;
/*     */     }
/*  81 */     if (this.upstream == null) {
/*  82 */       onNextNoSubscription();
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     if (t == null) {
/*  87 */       Throwable ex = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       try {
/*  89 */         this.upstream.dispose();
/*  90 */       } catch (Throwable e1) {
/*  91 */         Exceptions.throwIfFatal(e1);
/*  92 */         onError((Throwable)new CompositeException(new Throwable[] { ex, e1 }));
/*     */         return;
/*     */       } 
/*  95 */       onError(ex);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 100 */       this.downstream.onNext(t);
/* 101 */     } catch (Throwable e) {
/* 102 */       Exceptions.throwIfFatal(e);
/*     */       try {
/* 104 */         this.upstream.dispose();
/* 105 */       } catch (Throwable e1) {
/* 106 */         Exceptions.throwIfFatal(e1);
/* 107 */         onError((Throwable)new CompositeException(new Throwable[] { e, e1 }));
/*     */         return;
/*     */       } 
/* 110 */       onError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void onNextNoSubscription() {
/* 115 */     this.done = true;
/*     */     
/* 117 */     Throwable ex = new NullPointerException("Subscription not set!");
/*     */     
/*     */     try {
/* 120 */       this.downstream.onSubscribe((Disposable)EmptyDisposable.INSTANCE);
/* 121 */     } catch (Throwable e) {
/* 122 */       Exceptions.throwIfFatal(e);
/*     */       
/* 124 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */       return;
/*     */     } 
/*     */     try {
/* 128 */       this.downstream.onError(ex);
/* 129 */     } catch (Throwable e) {
/* 130 */       Exceptions.throwIfFatal(e);
/*     */       
/* 132 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(@NonNull Throwable t) {
/* 138 */     if (this.done) {
/* 139 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 142 */     this.done = true;
/*     */     
/* 144 */     if (this.upstream == null) {
/* 145 */       Throwable npe = new NullPointerException("Subscription not set!");
/*     */       
/*     */       try {
/* 148 */         this.downstream.onSubscribe((Disposable)EmptyDisposable.INSTANCE);
/* 149 */       } catch (Throwable e) {
/* 150 */         Exceptions.throwIfFatal(e);
/*     */         
/* 152 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, npe, e }));
/*     */         return;
/*     */       } 
/*     */       try {
/* 156 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, npe }));
/* 157 */       } catch (Throwable e) {
/* 158 */         Exceptions.throwIfFatal(e);
/*     */         
/* 160 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, npe, e }));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 165 */     if (t == null) {
/* 166 */       t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     }
/*     */     
/*     */     try {
/* 170 */       this.downstream.onError(t);
/* 171 */     } catch (Throwable ex) {
/* 172 */       Exceptions.throwIfFatal(ex);
/*     */       
/* 174 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 180 */     if (this.done) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     this.done = true;
/*     */     
/* 186 */     if (this.upstream == null) {
/* 187 */       onCompleteNoSubscription();
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 192 */       this.downstream.onComplete();
/* 193 */     } catch (Throwable e) {
/* 194 */       Exceptions.throwIfFatal(e);
/* 195 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void onCompleteNoSubscription() {
/* 201 */     Throwable ex = new NullPointerException("Subscription not set!");
/*     */     
/*     */     try {
/* 204 */       this.downstream.onSubscribe((Disposable)EmptyDisposable.INSTANCE);
/* 205 */     } catch (Throwable e) {
/* 206 */       Exceptions.throwIfFatal(e);
/*     */       
/* 208 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */       return;
/*     */     } 
/*     */     try {
/* 212 */       this.downstream.onError(ex);
/* 213 */     } catch (Throwable e) {
/* 214 */       Exceptions.throwIfFatal(e);
/*     */       
/* 216 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/SafeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */