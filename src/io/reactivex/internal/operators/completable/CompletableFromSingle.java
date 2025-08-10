/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
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
/*    */ public final class CompletableFromSingle<T>
/*    */   extends Completable
/*    */ {
/*    */   final SingleSource<T> single;
/*    */   
/*    */   public CompletableFromSingle(SingleSource<T> single) {
/* 24 */     this.single = single;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 29 */     this.single.subscribe(new CompletableFromSingleObserver(observer));
/*    */   }
/*    */   
/*    */   static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {
/*    */     final CompletableObserver co;
/*    */     
/*    */     CompletableFromSingleObserver(CompletableObserver co) {
/* 36 */       this.co = co;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 41 */       this.co.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 46 */       this.co.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 51 */       this.co.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */