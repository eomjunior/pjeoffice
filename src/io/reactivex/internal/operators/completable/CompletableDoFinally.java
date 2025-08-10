/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ 
/*    */ public final class CompletableDoFinally
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Action onFinally;
/*    */   
/*    */   public CompletableDoFinally(CompletableSource source, Action onFinally) {
/* 37 */     this.source = source;
/* 38 */     this.onFinally = onFinally;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 43 */     this.source.subscribe(new DoFinallyObserver(observer, this.onFinally));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoFinallyObserver
/*    */     extends AtomicInteger
/*    */     implements CompletableObserver, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = 4109457741734051389L;
/*    */     final CompletableObserver downstream;
/*    */     final Action onFinally;
/*    */     Disposable upstream;
/*    */     
/*    */     DoFinallyObserver(CompletableObserver actual, Action onFinally) {
/* 57 */       this.downstream = actual;
/* 58 */       this.onFinally = onFinally;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 64 */         this.upstream = d;
/*    */         
/* 66 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 72 */       this.downstream.onError(t);
/* 73 */       runFinally();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 78 */       this.downstream.onComplete();
/* 79 */       runFinally();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 84 */       this.upstream.dispose();
/* 85 */       runFinally();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 90 */       return this.upstream.isDisposed();
/*    */     }
/*    */     
/*    */     void runFinally() {
/* 94 */       if (compareAndSet(0, 1))
/*    */         try {
/* 96 */           this.onFinally.run();
/* 97 */         } catch (Throwable ex) {
/* 98 */           Exceptions.throwIfFatal(ex);
/* 99 */           RxJavaPlugins.onError(ex);
/*    */         }  
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */