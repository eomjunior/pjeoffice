/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableDelaySubscriptionOther<T, U>
/*     */   extends Observable<T>
/*     */ {
/*     */   final ObservableSource<? extends T> main;
/*     */   final ObservableSource<U> other;
/*     */   
/*     */   public ObservableDelaySubscriptionOther(ObservableSource<? extends T> main, ObservableSource<U> other) {
/*  32 */     this.main = main;
/*  33 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> child) {
/*  38 */     SequentialDisposable serial = new SequentialDisposable();
/*  39 */     child.onSubscribe((Disposable)serial);
/*     */     
/*  41 */     Observer<U> otherObserver = new DelayObserver(serial, child);
/*     */     
/*  43 */     this.other.subscribe(otherObserver);
/*     */   }
/*     */   
/*     */   final class DelayObserver implements Observer<U> {
/*     */     final SequentialDisposable serial;
/*     */     final Observer<? super T> child;
/*     */     boolean done;
/*     */     
/*     */     DelayObserver(SequentialDisposable serial, Observer<? super T> child) {
/*  52 */       this.serial = serial;
/*  53 */       this.child = child;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  58 */       this.serial.update(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/*  63 */       onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  68 */       if (this.done) {
/*  69 */         RxJavaPlugins.onError(e);
/*     */         return;
/*     */       } 
/*  72 */       this.done = true;
/*  73 */       this.child.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  78 */       if (this.done) {
/*     */         return;
/*     */       }
/*  81 */       this.done = true;
/*     */       
/*  83 */       ObservableDelaySubscriptionOther.this.main.subscribe(new OnComplete());
/*     */     }
/*     */     
/*     */     final class OnComplete
/*     */       implements Observer<T> {
/*     */       public void onSubscribe(Disposable d) {
/*  89 */         ObservableDelaySubscriptionOther.DelayObserver.this.serial.update(d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(T value) {
/*  94 */         ObservableDelaySubscriptionOther.DelayObserver.this.child.onNext(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/*  99 */         ObservableDelaySubscriptionOther.DelayObserver.this.child.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 104 */         ObservableDelaySubscriptionOther.DelayObserver.this.child.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDelaySubscriptionOther.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */