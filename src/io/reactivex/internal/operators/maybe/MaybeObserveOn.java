/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class MaybeObserveOn<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public MaybeObserveOn(MaybeSource<T> source, Scheduler scheduler) {
/*  32 */     super(source);
/*  33 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  38 */     this.source.subscribe(new ObserveOnMaybeObserver<T>(observer, this.scheduler));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ObserveOnMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 8571289934935992137L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final Scheduler scheduler;
/*     */     T value;
/*     */     Throwable error;
/*     */     
/*     */     ObserveOnMaybeObserver(MaybeObserver<? super T> actual, Scheduler scheduler) {
/*  55 */       this.downstream = actual;
/*  56 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  61 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  66 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  71 */       if (DisposableHelper.setOnce(this, d)) {
/*  72 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  78 */       this.value = value;
/*  79 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  84 */       this.error = e;
/*  85 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  90 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  95 */       Throwable ex = this.error;
/*  96 */       if (ex != null) {
/*  97 */         this.error = null;
/*  98 */         this.downstream.onError(ex);
/*     */       } else {
/* 100 */         T v = this.value;
/* 101 */         if (v != null) {
/* 102 */           this.value = null;
/* 103 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 105 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */