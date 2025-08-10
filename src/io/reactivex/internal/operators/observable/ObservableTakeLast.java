/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.ArrayDeque;
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
/*    */ public final class ObservableTakeLast<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final int count;
/*    */   
/*    */   public ObservableTakeLast(ObservableSource<T> source, int count) {
/* 26 */     super(source);
/* 27 */     this.count = count;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> t) {
/* 32 */     this.source.subscribe(new TakeLastObserver<T>(t, this.count));
/*    */   }
/*    */   
/*    */   static final class TakeLastObserver<T>
/*    */     extends ArrayDeque<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = 7240042530241604978L;
/*    */     final Observer<? super T> downstream;
/*    */     final int count;
/*    */     Disposable upstream;
/*    */     volatile boolean cancelled;
/*    */     
/*    */     TakeLastObserver(Observer<? super T> actual, int count) {
/* 46 */       this.downstream = actual;
/* 47 */       this.count = count;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 52 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 53 */         this.upstream = d;
/* 54 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 60 */       if (this.count == size()) {
/* 61 */         poll();
/*    */       }
/* 63 */       offer(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 68 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 73 */       Observer<? super T> a = this.downstream;
/*    */       while (true) {
/* 75 */         if (this.cancelled) {
/*    */           return;
/*    */         }
/* 78 */         T v = poll();
/* 79 */         if (v == null) {
/* 80 */           if (!this.cancelled) {
/* 81 */             a.onComplete();
/*    */           }
/*    */           return;
/*    */         } 
/* 85 */         a.onNext(v);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 91 */       if (!this.cancelled) {
/* 92 */         this.cancelled = true;
/* 93 */         this.upstream.dispose();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 99 */       return this.cancelled;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */