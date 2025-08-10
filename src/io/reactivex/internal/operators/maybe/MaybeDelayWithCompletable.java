/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ public final class MaybeDelayWithCompletable<T>
/*     */   extends Maybe<T>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final CompletableSource other;
/*     */   
/*     */   public MaybeDelayWithCompletable(MaybeSource<T> source, CompletableSource other) {
/*  32 */     this.source = source;
/*  33 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  38 */     this.other.subscribe(new OtherObserver<T>(observer, this.source));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OtherObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements CompletableObserver, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 703409937383992161L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final MaybeSource<T> source;
/*     */     
/*     */     OtherObserver(MaybeObserver<? super T> actual, MaybeSource<T> source) {
/*  51 */       this.downstream = actual;
/*  52 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  57 */       if (DisposableHelper.setOnce(this, d))
/*     */       {
/*  59 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  65 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  70 */       this.source.subscribe(new MaybeDelayWithCompletable.DelayWithMainObserver<T>(this, this.downstream));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  75 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  80 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DelayWithMainObserver<T>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     final AtomicReference<Disposable> parent;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     DelayWithMainObserver(AtomicReference<Disposable> parent, MaybeObserver<? super T> downstream) {
/*  91 */       this.parent = parent;
/*  92 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  97 */       DisposableHelper.replace(this.parent, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 102 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 107 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 112 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDelayWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */