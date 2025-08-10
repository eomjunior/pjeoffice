/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class ObservableSubscribeOn<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
/* 26 */     super(source);
/* 27 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 32 */     SubscribeOnObserver<T> parent = new SubscribeOnObserver<T>(observer);
/*    */     
/* 34 */     observer.onSubscribe(parent);
/*    */     
/* 36 */     parent.setDisposable(this.scheduler.scheduleDirect(new SubscribeTask(parent)));
/*    */   }
/*    */   
/*    */   static final class SubscribeOnObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements Observer<T>, Disposable {
/*    */     private static final long serialVersionUID = 8094547886072529208L;
/*    */     final Observer<? super T> downstream;
/*    */     final AtomicReference<Disposable> upstream;
/*    */     
/*    */     SubscribeOnObserver(Observer<? super T> downstream) {
/* 47 */       this.downstream = downstream;
/* 48 */       this.upstream = new AtomicReference<Disposable>();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 53 */       DisposableHelper.setOnce(this.upstream, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 58 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 63 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 68 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 73 */       DisposableHelper.dispose(this.upstream);
/* 74 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 79 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */     
/*    */     void setDisposable(Disposable d) {
/* 83 */       DisposableHelper.setOnce(this, d);
/*    */     }
/*    */   }
/*    */   
/*    */   final class SubscribeTask implements Runnable {
/*    */     private final ObservableSubscribeOn.SubscribeOnObserver<T> parent;
/*    */     
/*    */     SubscribeTask(ObservableSubscribeOn.SubscribeOnObserver<T> parent) {
/* 91 */       this.parent = parent;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 96 */       ObservableSubscribeOn.this.source.subscribe(this.parent);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */