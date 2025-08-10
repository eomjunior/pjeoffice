/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
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
/*    */ public final class SingleDoOnTerminate<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Action onTerminate;
/*    */   
/*    */   public SingleDoOnTerminate(SingleSource<T> source, Action onTerminate) {
/* 31 */     this.source = source;
/* 32 */     this.onTerminate = onTerminate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 37 */     this.source.subscribe(new DoOnTerminate(observer));
/*    */   }
/*    */   
/*    */   final class DoOnTerminate
/*    */     implements SingleObserver<T> {
/*    */     final SingleObserver<? super T> downstream;
/*    */     
/*    */     DoOnTerminate(SingleObserver<? super T> observer) {
/* 45 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 50 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       try {
/* 56 */         SingleDoOnTerminate.this.onTerminate.run();
/* 57 */       } catch (Throwable ex) {
/* 58 */         Exceptions.throwIfFatal(ex);
/* 59 */         this.downstream.onError(ex);
/*    */         
/*    */         return;
/*    */       } 
/* 63 */       this.downstream.onSuccess(value);
/*    */     }
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       CompositeException compositeException;
/*    */       try {
/* 69 */         SingleDoOnTerminate.this.onTerminate.run();
/* 70 */       } catch (Throwable ex) {
/* 71 */         Exceptions.throwIfFatal(ex);
/* 72 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*    */       } 
/*    */       
/* 75 */       this.downstream.onError((Throwable)compositeException);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */