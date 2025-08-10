/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ 
/*     */ public final class MaybeTimeoutMaybe<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<U> other;
/*     */   final MaybeSource<? extends T> fallback;
/*     */   
/*     */   public MaybeTimeoutMaybe(MaybeSource<T> source, MaybeSource<U> other, MaybeSource<? extends T> fallback) {
/*  38 */     super(source);
/*  39 */     this.other = other;
/*  40 */     this.fallback = fallback;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  45 */     TimeoutMainMaybeObserver<T, U> parent = new TimeoutMainMaybeObserver<T, U>(observer, this.fallback);
/*  46 */     observer.onSubscribe(parent);
/*     */     
/*  48 */     this.other.subscribe(parent.other);
/*     */     
/*  50 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutMainMaybeObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5955289211445418871L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final MaybeTimeoutMaybe.TimeoutOtherMaybeObserver<T, U> other;
/*     */     
/*     */     final MaybeSource<? extends T> fallback;
/*     */     final MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T> otherObserver;
/*     */     
/*     */     TimeoutMainMaybeObserver(MaybeObserver<? super T> actual, MaybeSource<? extends T> fallback) {
/*  68 */       this.downstream = actual;
/*  69 */       this.other = new MaybeTimeoutMaybe.TimeoutOtherMaybeObserver<T, U>(this);
/*  70 */       this.fallback = fallback;
/*  71 */       this.otherObserver = (fallback != null) ? new MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T>(actual) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  76 */       DisposableHelper.dispose(this);
/*  77 */       DisposableHelper.dispose(this.other);
/*  78 */       MaybeTimeoutMaybe.TimeoutFallbackMaybeObserver<T> oo = this.otherObserver;
/*  79 */       if (oo != null) {
/*  80 */         DisposableHelper.dispose(oo);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  86 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  91 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  96 */       DisposableHelper.dispose(this.other);
/*  97 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/*  98 */         this.downstream.onSuccess(value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 104 */       DisposableHelper.dispose(this.other);
/* 105 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 106 */         this.downstream.onError(e);
/*     */       } else {
/* 108 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       DisposableHelper.dispose(this.other);
/* 115 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 116 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     public void otherError(Throwable e) {
/* 121 */       if (DisposableHelper.dispose(this)) {
/* 122 */         this.downstream.onError(e);
/*     */       } else {
/* 124 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void otherComplete() {
/* 129 */       if (DisposableHelper.dispose(this)) {
/* 130 */         if (this.fallback == null) {
/* 131 */           this.downstream.onError(new TimeoutException());
/*     */         } else {
/* 133 */           this.fallback.subscribe(this.otherObserver);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutOtherMaybeObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 8663801314800248617L;
/*     */     final MaybeTimeoutMaybe.TimeoutMainMaybeObserver<T, U> parent;
/*     */     
/*     */     TimeoutOtherMaybeObserver(MaybeTimeoutMaybe.TimeoutMainMaybeObserver<T, U> parent) {
/* 148 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 153 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(Object value) {
/* 158 */       this.parent.otherComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 163 */       this.parent.otherError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 168 */       this.parent.otherComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimeoutFallbackMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8663801314800248617L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     TimeoutFallbackMaybeObserver(MaybeObserver<? super T> downstream) {
/* 180 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 185 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 190 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 195 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 200 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeTimeoutMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */