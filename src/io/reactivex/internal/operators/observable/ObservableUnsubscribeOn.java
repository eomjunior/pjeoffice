/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public final class ObservableUnsubscribeOn<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public ObservableUnsubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
/* 26 */     super(source);
/* 27 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> t) {
/* 32 */     this.source.subscribe(new UnsubscribeObserver<T>(t, this.scheduler));
/*    */   }
/*    */   
/*    */   static final class UnsubscribeObserver<T>
/*    */     extends AtomicBoolean
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = 1015244841293359600L;
/*    */     final Observer<? super T> downstream;
/*    */     final Scheduler scheduler;
/*    */     Disposable upstream;
/*    */     
/*    */     UnsubscribeObserver(Observer<? super T> actual, Scheduler scheduler) {
/* 45 */       this.downstream = actual;
/* 46 */       this.scheduler = scheduler;
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
/*    */     public void onNext(T t) {
/* 59 */       if (!get()) {
/* 60 */         this.downstream.onNext(t);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 66 */       if (get()) {
/* 67 */         RxJavaPlugins.onError(t);
/*    */         return;
/*    */       } 
/* 70 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 75 */       if (!get()) {
/* 76 */         this.downstream.onComplete();
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 82 */       if (compareAndSet(false, true)) {
/* 83 */         this.scheduler.scheduleDirect(new DisposeTask());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 89 */       return get();
/*    */     }
/*    */     
/*    */     final class DisposeTask
/*    */       implements Runnable {
/*    */       public void run() {
/* 95 */         ObservableUnsubscribeOn.UnsubscribeObserver.this.upstream.dispose();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */