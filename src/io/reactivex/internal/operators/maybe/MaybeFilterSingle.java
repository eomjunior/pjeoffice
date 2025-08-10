/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Predicate;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeFilterSingle<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Predicate<? super T> predicate;
/*    */   
/*    */   public MaybeFilterSingle(SingleSource<T> source, Predicate<? super T> predicate) {
/* 34 */     this.source = source;
/* 35 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 40 */     this.source.subscribe(new FilterMaybeObserver<T>(observer, this.predicate));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class FilterMaybeObserver<T>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     final Predicate<? super T> predicate;
/*    */     Disposable upstream;
/*    */     
/*    */     FilterMaybeObserver(MaybeObserver<? super T> actual, Predicate<? super T> predicate) {
/* 52 */       this.downstream = actual;
/* 53 */       this.predicate = predicate;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 58 */       Disposable d = this.upstream;
/* 59 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 60 */       d.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 65 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 70 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 71 */         this.upstream = d;
/*    */         
/* 73 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       boolean b;
/*    */       try {
/* 82 */         b = this.predicate.test(value);
/* 83 */       } catch (Throwable ex) {
/* 84 */         Exceptions.throwIfFatal(ex);
/* 85 */         this.downstream.onError(ex);
/*    */         
/*    */         return;
/*    */       } 
/* 89 */       if (b) {
/* 90 */         this.downstream.onSuccess(value);
/*    */       } else {
/* 92 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 98 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFilterSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */