/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class CompletableToSingle<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Callable<? extends T> completionValueSupplier;
/*    */   final T completionValue;
/*    */   
/*    */   public CompletableToSingle(CompletableSource source, Callable<? extends T> completionValueSupplier, T completionValue) {
/* 31 */     this.source = source;
/* 32 */     this.completionValue = completionValue;
/* 33 */     this.completionValueSupplier = completionValueSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 38 */     this.source.subscribe(new ToSingle(observer));
/*    */   }
/*    */   
/*    */   final class ToSingle
/*    */     implements CompletableObserver {
/*    */     private final SingleObserver<? super T> observer;
/*    */     
/*    */     ToSingle(SingleObserver<? super T> observer) {
/* 46 */       this.observer = observer;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onComplete() {
/*    */       T v;
/* 53 */       if (CompletableToSingle.this.completionValueSupplier != null) {
/*    */         try {
/* 55 */           v = CompletableToSingle.this.completionValueSupplier.call();
/* 56 */         } catch (Throwable e) {
/* 57 */           Exceptions.throwIfFatal(e);
/* 58 */           this.observer.onError(e);
/*    */           return;
/*    */         } 
/*    */       } else {
/* 62 */         v = CompletableToSingle.this.completionValue;
/*    */       } 
/*    */       
/* 65 */       if (v == null) {
/* 66 */         this.observer.onError(new NullPointerException("The value supplied is null"));
/*    */       } else {
/* 68 */         this.observer.onSuccess(v);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 74 */       this.observer.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 79 */       this.observer.onSubscribe(d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableToSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */