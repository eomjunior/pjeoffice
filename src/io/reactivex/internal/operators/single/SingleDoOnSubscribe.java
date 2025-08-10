/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ 
/*    */ public final class SingleDoOnSubscribe<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Consumer<? super Disposable> onSubscribe;
/*    */   
/*    */   public SingleDoOnSubscribe(SingleSource<T> source, Consumer<? super Disposable> onSubscribe) {
/* 35 */     this.source = source;
/* 36 */     this.onSubscribe = onSubscribe;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 41 */     this.source.subscribe(new DoOnSubscribeSingleObserver<T>(observer, this.onSubscribe));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoOnSubscribeSingleObserver<T>
/*    */     implements SingleObserver<T>
/*    */   {
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Consumer<? super Disposable> onSubscribe;
/*    */     boolean done;
/*    */     
/*    */     DoOnSubscribeSingleObserver(SingleObserver<? super T> actual, Consumer<? super Disposable> onSubscribe) {
/* 53 */       this.downstream = actual;
/* 54 */       this.onSubscribe = onSubscribe;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/*    */       try {
/* 60 */         this.onSubscribe.accept(d);
/* 61 */       } catch (Throwable ex) {
/* 62 */         Exceptions.throwIfFatal(ex);
/* 63 */         this.done = true;
/* 64 */         d.dispose();
/* 65 */         EmptyDisposable.error(ex, this.downstream);
/*    */         
/*    */         return;
/*    */       } 
/* 69 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 74 */       if (this.done) {
/*    */         return;
/*    */       }
/* 77 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 82 */       if (this.done) {
/* 83 */         RxJavaPlugins.onError(e);
/*    */         return;
/*    */       } 
/* 86 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoOnSubscribe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */