/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Predicate;
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
/*    */ public final class CompletableOnErrorComplete
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Predicate<? super Throwable> predicate;
/*    */   
/*    */   public CompletableOnErrorComplete(CompletableSource source, Predicate<? super Throwable> predicate) {
/* 28 */     this.source = source;
/* 29 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 35 */     this.source.subscribe(new OnError(observer));
/*    */   }
/*    */   
/*    */   final class OnError
/*    */     implements CompletableObserver {
/*    */     private final CompletableObserver downstream;
/*    */     
/*    */     OnError(CompletableObserver observer) {
/* 43 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 48 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       boolean b;
/*    */       try {
/* 56 */         b = CompletableOnErrorComplete.this.predicate.test(e);
/* 57 */       } catch (Throwable ex) {
/* 58 */         Exceptions.throwIfFatal(ex);
/* 59 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */         
/*    */         return;
/*    */       } 
/* 63 */       if (b) {
/* 64 */         this.downstream.onComplete();
/*    */       } else {
/* 66 */         this.downstream.onError(e);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 72 */       this.downstream.onSubscribe(d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableOnErrorComplete.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */