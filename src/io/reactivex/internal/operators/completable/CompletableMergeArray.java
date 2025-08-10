/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.CompositeDisposable;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public final class CompletableMergeArray
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource[] sources;
/*    */   
/*    */   public CompletableMergeArray(CompletableSource[] sources) {
/* 26 */     this.sources = sources;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(CompletableObserver observer) {
/* 31 */     CompositeDisposable set = new CompositeDisposable();
/* 32 */     AtomicBoolean once = new AtomicBoolean();
/*    */     
/* 34 */     InnerCompletableObserver shared = new InnerCompletableObserver(observer, once, set, this.sources.length + 1);
/* 35 */     observer.onSubscribe((Disposable)set);
/*    */     
/* 37 */     for (CompletableSource c : this.sources) {
/* 38 */       if (set.isDisposed()) {
/*    */         return;
/*    */       }
/*    */       
/* 42 */       if (c == null) {
/* 43 */         set.dispose();
/* 44 */         NullPointerException npe = new NullPointerException("A completable source is null");
/* 45 */         shared.onError(npe);
/*    */         
/*    */         return;
/*    */       } 
/* 49 */       c.subscribe(shared);
/*    */     } 
/*    */     
/* 52 */     shared.onComplete();
/*    */   }
/*    */   
/*    */   static final class InnerCompletableObserver
/*    */     extends AtomicInteger
/*    */     implements CompletableObserver
/*    */   {
/*    */     private static final long serialVersionUID = -8360547806504310570L;
/*    */     final CompletableObserver downstream;
/*    */     final AtomicBoolean once;
/*    */     final CompositeDisposable set;
/*    */     
/*    */     InnerCompletableObserver(CompletableObserver actual, AtomicBoolean once, CompositeDisposable set, int n) {
/* 65 */       this.downstream = actual;
/* 66 */       this.once = once;
/* 67 */       this.set = set;
/* 68 */       lazySet(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 73 */       this.set.add(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 78 */       this.set.dispose();
/* 79 */       if (this.once.compareAndSet(false, true)) {
/* 80 */         this.downstream.onError(e);
/*    */       } else {
/* 82 */         RxJavaPlugins.onError(e);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 88 */       if (decrementAndGet() == 0 && 
/* 89 */         this.once.compareAndSet(false, true))
/* 90 */         this.downstream.onComplete(); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableMergeArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */