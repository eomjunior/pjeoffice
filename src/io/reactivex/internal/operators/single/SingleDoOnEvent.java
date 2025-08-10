/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BiConsumer;
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
/*    */ public final class SingleDoOnEvent<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final BiConsumer<? super T, ? super Throwable> onEvent;
/*    */   
/*    */   public SingleDoOnEvent(SingleSource<T> source, BiConsumer<? super T, ? super Throwable> onEvent) {
/* 30 */     this.source = source;
/* 31 */     this.onEvent = onEvent;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 37 */     this.source.subscribe(new DoOnEvent(observer));
/*    */   }
/*    */   
/*    */   final class DoOnEvent implements SingleObserver<T> {
/*    */     private final SingleObserver<? super T> downstream;
/*    */     
/*    */     DoOnEvent(SingleObserver<? super T> observer) {
/* 44 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 49 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       try {
/* 55 */         SingleDoOnEvent.this.onEvent.accept(value, null);
/* 56 */       } catch (Throwable ex) {
/* 57 */         Exceptions.throwIfFatal(ex);
/* 58 */         this.downstream.onError(ex);
/*    */         
/*    */         return;
/*    */       } 
/* 62 */       this.downstream.onSuccess(value);
/*    */     }
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       CompositeException compositeException;
/*    */       try {
/* 68 */         SingleDoOnEvent.this.onEvent.accept(null, e);
/* 69 */       } catch (Throwable ex) {
/* 70 */         Exceptions.throwIfFatal(ex);
/* 71 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*    */       } 
/* 73 */       this.downstream.onError((Throwable)compositeException);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */