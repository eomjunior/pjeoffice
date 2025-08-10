/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
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
/*     */ public final class MaybeSwitchIfEmptySingle<T>
/*     */   extends Single<T>
/*     */   implements HasUpstreamMaybeSource<T>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final SingleSource<? extends T> other;
/*     */   
/*     */   public MaybeSwitchIfEmptySingle(MaybeSource<T> source, SingleSource<? extends T> other) {
/*  34 */     this.source = source;
/*  35 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public MaybeSource<T> source() {
/*  40 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  45 */     this.source.subscribe(new SwitchIfEmptyMaybeObserver<T>(observer, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchIfEmptyMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4603919676453758899L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     final SingleSource<? extends T> other;
/*     */     
/*     */     SwitchIfEmptyMaybeObserver(SingleObserver<? super T> actual, SingleSource<? extends T> other) {
/*  59 */       this.downstream = actual;
/*  60 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  70 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       if (DisposableHelper.setOnce(this, d)) {
/*  76 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  82 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  87 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  92 */       Disposable d = get();
/*  93 */       if (d != DisposableHelper.DISPOSED && 
/*  94 */         compareAndSet(d, null)) {
/*  95 */         this.other.subscribe(new OtherSingleObserver<T>(this.downstream, this));
/*     */       }
/*     */     }
/*     */     
/*     */     static final class OtherSingleObserver<T>
/*     */       implements SingleObserver<T>
/*     */     {
/*     */       final SingleObserver<? super T> downstream;
/*     */       final AtomicReference<Disposable> parent;
/*     */       
/*     */       OtherSingleObserver(SingleObserver<? super T> actual, AtomicReference<Disposable> parent) {
/* 106 */         this.downstream = actual;
/* 107 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 112 */         DisposableHelper.setOnce(this.parent, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T value) {
/* 117 */         this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 122 */         this.downstream.onError(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeSwitchIfEmptySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */