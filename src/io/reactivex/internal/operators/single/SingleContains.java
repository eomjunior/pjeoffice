/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BiPredicate;
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
/*    */ public final class SingleContains<T>
/*    */   extends Single<Boolean>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Object value;
/*    */   final BiPredicate<Object, Object> comparer;
/*    */   
/*    */   public SingleContains(SingleSource<T> source, Object value, BiPredicate<Object, Object> comparer) {
/* 30 */     this.source = source;
/* 31 */     this.value = value;
/* 32 */     this.comparer = comparer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/* 38 */     this.source.subscribe(new ContainsSingleObserver(observer));
/*    */   }
/*    */   
/*    */   final class ContainsSingleObserver
/*    */     implements SingleObserver<T> {
/*    */     private final SingleObserver<? super Boolean> downstream;
/*    */     
/*    */     ContainsSingleObserver(SingleObserver<? super Boolean> observer) {
/* 46 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 51 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onSuccess(T v) {
/*    */       boolean b;
/*    */       try {
/* 59 */         b = SingleContains.this.comparer.test(v, SingleContains.this.value);
/* 60 */       } catch (Throwable ex) {
/* 61 */         Exceptions.throwIfFatal(ex);
/* 62 */         this.downstream.onError(ex);
/*    */         return;
/*    */       } 
/* 65 */       this.downstream.onSuccess(Boolean.valueOf(b));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 70 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleContains.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */