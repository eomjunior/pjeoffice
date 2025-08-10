/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*    */ public final class CompletableConcatArray
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource[] sources;
/*    */   
/*    */   public CompletableConcatArray(CompletableSource[] sources) {
/* 26 */     this.sources = sources;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(CompletableObserver observer) {
/* 31 */     ConcatInnerObserver inner = new ConcatInnerObserver(observer, this.sources);
/* 32 */     observer.onSubscribe((Disposable)inner.sd);
/* 33 */     inner.next();
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ConcatInnerObserver
/*    */     extends AtomicInteger
/*    */     implements CompletableObserver
/*    */   {
/*    */     private static final long serialVersionUID = -7965400327305809232L;
/*    */     final CompletableObserver downstream;
/*    */     final CompletableSource[] sources;
/*    */     int index;
/*    */     final SequentialDisposable sd;
/*    */     
/*    */     ConcatInnerObserver(CompletableObserver actual, CompletableSource[] sources) {
/* 48 */       this.downstream = actual;
/* 49 */       this.sources = sources;
/* 50 */       this.sd = new SequentialDisposable();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 55 */       this.sd.replace(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 60 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 65 */       next();
/*    */     }
/*    */     
/*    */     void next() {
/* 69 */       if (this.sd.isDisposed()) {
/*    */         return;
/*    */       }
/*    */       
/* 73 */       if (getAndIncrement() != 0) {
/*    */         return;
/*    */       }
/*    */       
/* 77 */       CompletableSource[] a = this.sources;
/*    */       do {
/* 79 */         if (this.sd.isDisposed()) {
/*    */           return;
/*    */         }
/*    */         
/* 83 */         int idx = this.index++;
/* 84 */         if (idx == a.length) {
/* 85 */           this.downstream.onComplete();
/*    */           
/*    */           return;
/*    */         } 
/* 89 */         a[idx].subscribe(this);
/* 90 */       } while (decrementAndGet() != 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableConcatArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */