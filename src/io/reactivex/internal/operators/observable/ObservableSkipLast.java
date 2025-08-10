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
/*    */ public final class ObservableSkipLast<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final int skip;
/*    */   
/*    */   public ObservableSkipLast(ObservableSource<T> source, int skip) {
/* 26 */     super(source);
/* 27 */     this.skip = skip;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 32 */     this.source.subscribe(new SkipLastObserver<T>(observer, this.skip));
/*    */   }
/*    */   
/*    */   static final class SkipLastObserver<T>
/*    */     extends ArrayDeque<T>
/*    */     implements Observer<T>, Disposable {
/*    */     private static final long serialVersionUID = -3807491841935125653L;
/*    */     final Observer<? super T> downstream;
/*    */     final int skip;
/*    */     Disposable upstream;
/*    */     
/*    */     SkipLastObserver(Observer<? super T> actual, int skip) {
/* 44 */       super(skip);
/* 45 */       this.downstream = actual;
/* 46 */       this.skip = skip;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 51 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 52 */         this.upstream = d;
/* 53 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 59 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 64 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 69 */       if (this.skip == size()) {
/* 70 */         this.downstream.onNext(poll());
/*    */       }
/* 72 */       offer(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 77 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 82 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSkipLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */