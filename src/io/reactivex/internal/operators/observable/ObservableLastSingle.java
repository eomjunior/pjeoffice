/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class ObservableLastSingle<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final T defaultItem;
/*     */   
/*     */   public ObservableLastSingle(ObservableSource<T> source, T defaultItem) {
/*  35 */     this.source = source;
/*  36 */     this.defaultItem = defaultItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  43 */     this.source.subscribe(new LastObserver<T>(observer, this.defaultItem));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LastObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final T defaultItem;
/*     */     Disposable upstream;
/*     */     T item;
/*     */     
/*     */     LastObserver(SingleObserver<? super T> actual, T defaultItem) {
/*  57 */       this.downstream = actual;
/*  58 */       this.defaultItem = defaultItem;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  63 */       this.upstream.dispose();
/*  64 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  69 */       return (this.upstream == DisposableHelper.DISPOSED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  75 */         this.upstream = d;
/*     */         
/*  77 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  83 */       this.item = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  88 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  89 */       this.item = null;
/*  90 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  96 */       T v = this.item;
/*  97 */       if (v != null) {
/*  98 */         this.item = null;
/*  99 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 101 */         v = this.defaultItem;
/* 102 */         if (v != null) {
/* 103 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 105 */           this.downstream.onError(new NoSuchElementException());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableLastSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */