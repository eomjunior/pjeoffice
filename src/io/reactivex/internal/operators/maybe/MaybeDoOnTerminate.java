/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
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
/*    */ public final class MaybeDoOnTerminate<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   final Action onTerminate;
/*    */   
/*    */   public MaybeDoOnTerminate(MaybeSource<T> source, Action onTerminate) {
/* 31 */     this.source = source;
/* 32 */     this.onTerminate = onTerminate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 37 */     this.source.subscribe(new DoOnTerminate(observer));
/*    */   }
/*    */   
/*    */   final class DoOnTerminate implements MaybeObserver<T> {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     
/*    */     DoOnTerminate(MaybeObserver<? super T> observer) {
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
/* 55 */         MaybeDoOnTerminate.this.onTerminate.run();
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
/* 68 */         MaybeDoOnTerminate.this.onTerminate.run();
/* 69 */       } catch (Throwable ex) {
/* 70 */         Exceptions.throwIfFatal(ex);
/* 71 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*    */       } 
/*    */       
/* 74 */       this.downstream.onError((Throwable)compositeException);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/*    */       try {
/* 80 */         MaybeDoOnTerminate.this.onTerminate.run();
/* 81 */       } catch (Throwable ex) {
/* 82 */         Exceptions.throwIfFatal(ex);
/* 83 */         this.downstream.onError(ex);
/*    */         
/*    */         return;
/*    */       } 
/* 87 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDoOnTerminate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */