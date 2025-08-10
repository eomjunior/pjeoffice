/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.observables.ConnectableObservable;
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
/*    */ 
/*    */ 
/*    */ public final class ObservableAutoConnect<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final ConnectableObservable<? extends T> source;
/*    */   final int numberOfObservers;
/*    */   final Consumer<? super Disposable> connection;
/*    */   final AtomicInteger clients;
/*    */   
/*    */   public ObservableAutoConnect(ConnectableObservable<? extends T> source, int numberOfObservers, Consumer<? super Disposable> connection) {
/* 38 */     this.source = source;
/* 39 */     this.numberOfObservers = numberOfObservers;
/* 40 */     this.connection = connection;
/* 41 */     this.clients = new AtomicInteger();
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> child) {
/* 46 */     this.source.subscribe(child);
/* 47 */     if (this.clients.incrementAndGet() == this.numberOfObservers)
/* 48 */       this.source.connect(this.connection); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAutoConnect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */