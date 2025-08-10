/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeTakeUntilMaybe<T, U>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<U> other;
/*     */   
/*     */   public MaybeTakeUntilMaybe(MaybeSource<T> source, MaybeSource<U> other) {
/*  35 */     super(source);
/*  36 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  41 */     TakeUntilMainMaybeObserver<T, U> parent = new TakeUntilMainMaybeObserver<T, U>(observer);
/*  42 */     observer.onSubscribe(parent);
/*     */     
/*  44 */     this.other.subscribe(parent.other);
/*     */     
/*  46 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainMaybeObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2187421758664251153L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final TakeUntilOtherMaybeObserver<U> other;
/*     */     
/*     */     TakeUntilMainMaybeObserver(MaybeObserver<? super T> downstream) {
/*  59 */       this.downstream = downstream;
/*  60 */       this.other = new TakeUntilOtherMaybeObserver<U>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       DisposableHelper.dispose(this);
/*  66 */       DisposableHelper.dispose(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  71 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  76 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  81 */       DisposableHelper.dispose(this.other);
/*  82 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/*  83 */         this.downstream.onSuccess(value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  89 */       DisposableHelper.dispose(this.other);
/*  90 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/*  91 */         this.downstream.onError(e);
/*     */       } else {
/*  93 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       DisposableHelper.dispose(this.other);
/* 100 */       if (getAndSet((Disposable)DisposableHelper.DISPOSED) != DisposableHelper.DISPOSED) {
/* 101 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     void otherError(Throwable e) {
/* 106 */       if (DisposableHelper.dispose(this)) {
/* 107 */         this.downstream.onError(e);
/*     */       } else {
/* 109 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 114 */       if (DisposableHelper.dispose(this)) {
/* 115 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     static final class TakeUntilOtherMaybeObserver<U>
/*     */       extends AtomicReference<Disposable>
/*     */       implements MaybeObserver<U>
/*     */     {
/*     */       private static final long serialVersionUID = -1266041316834525931L;
/*     */       final MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent;
/*     */       
/*     */       TakeUntilOtherMaybeObserver(MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent) {
/* 127 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 132 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(Object value) {
/* 137 */         this.parent.otherComplete();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 142 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 147 */         this.parent.otherComplete(); } } } static final class TakeUntilOtherMaybeObserver<U> extends AtomicReference<Disposable> implements MaybeObserver<U> { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -1266041316834525931L;
/*     */     final MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent;
/*     */     
/*     */     TakeUntilOtherMaybeObserver(MaybeTakeUntilMaybe.TakeUntilMainMaybeObserver<?, U> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onSuccess(Object value) {
/*     */       this.parent.otherComplete();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeTakeUntilMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */