/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
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
/*    */ public final class CompletableDoOnEvent
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Consumer<? super Throwable> onEvent;
/*    */   
/*    */   public CompletableDoOnEvent(CompletableSource source, Consumer<? super Throwable> onEvent) {
/* 29 */     this.source = source;
/* 30 */     this.onEvent = onEvent;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 35 */     this.source.subscribe(new DoOnEvent(observer));
/*    */   }
/*    */   
/*    */   final class DoOnEvent implements CompletableObserver {
/*    */     private final CompletableObserver observer;
/*    */     
/*    */     DoOnEvent(CompletableObserver observer) {
/* 42 */       this.observer = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/*    */       try {
/* 48 */         CompletableDoOnEvent.this.onEvent.accept(null);
/* 49 */       } catch (Throwable e) {
/* 50 */         Exceptions.throwIfFatal(e);
/* 51 */         this.observer.onError(e);
/*    */         
/*    */         return;
/*    */       } 
/* 55 */       this.observer.onComplete();
/*    */     }
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       CompositeException compositeException;
/*    */       try {
/* 61 */         CompletableDoOnEvent.this.onEvent.accept(e);
/* 62 */       } catch (Throwable ex) {
/* 63 */         Exceptions.throwIfFatal(ex);
/* 64 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*    */       } 
/*    */       
/* 67 */       this.observer.onError((Throwable)compositeException);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 72 */       this.observer.onSubscribe(d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */