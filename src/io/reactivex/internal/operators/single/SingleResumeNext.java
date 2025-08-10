/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.observers.ResumeSingleObserver;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class SingleResumeNext<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;
/*    */   
/*    */   public SingleResumeNext(SingleSource<? extends T> source, Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction) {
/* 33 */     this.source = source;
/* 34 */     this.nextFunction = nextFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 39 */     this.source.subscribe(new ResumeMainSingleObserver<T>(observer, this.nextFunction));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ResumeMainSingleObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -5314538511045349925L;
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction;
/*    */     
/*    */     ResumeMainSingleObserver(SingleObserver<? super T> actual, Function<? super Throwable, ? extends SingleSource<? extends T>> nextFunction) {
/* 52 */       this.downstream = actual;
/* 53 */       this.nextFunction = nextFunction;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 58 */       if (DisposableHelper.setOnce(this, d)) {
/* 59 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 65 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       SingleSource<? extends T> source;
/*    */       try {
/* 73 */         source = (SingleSource<? extends T>)ObjectHelper.requireNonNull(this.nextFunction.apply(e), "The nextFunction returned a null SingleSource.");
/* 74 */       } catch (Throwable ex) {
/* 75 */         Exceptions.throwIfFatal(ex);
/* 76 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */         
/*    */         return;
/*    */       } 
/* 80 */       source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 85 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 90 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleResumeNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */