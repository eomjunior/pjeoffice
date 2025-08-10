/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Predicate;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class ObservableTakeUntilPredicate<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Predicate<? super T> predicate;
/*    */   
/*    */   public ObservableTakeUntilPredicate(ObservableSource<T> source, Predicate<? super T> predicate) {
/* 26 */     super(source);
/* 27 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 32 */     this.source.subscribe(new TakeUntilPredicateObserver<T>(observer, this.predicate));
/*    */   }
/*    */   
/*    */   static final class TakeUntilPredicateObserver<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super T> downstream;
/*    */     final Predicate<? super T> predicate;
/*    */     
/*    */     TakeUntilPredicateObserver(Observer<? super T> downstream, Predicate<? super T> predicate) {
/* 41 */       this.downstream = downstream;
/* 42 */       this.predicate = predicate;
/*    */     }
/*    */     Disposable upstream; boolean done;
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 47 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 48 */         this.upstream = d;
/* 49 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 55 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 60 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 65 */       if (!this.done) {
/* 66 */         boolean b; this.downstream.onNext(t);
/*    */         
/*    */         try {
/* 69 */           b = this.predicate.test(t);
/* 70 */         } catch (Throwable e) {
/* 71 */           Exceptions.throwIfFatal(e);
/* 72 */           this.upstream.dispose();
/* 73 */           onError(e);
/*    */           return;
/*    */         } 
/* 76 */         if (b) {
/* 77 */           this.done = true;
/* 78 */           this.upstream.dispose();
/* 79 */           this.downstream.onComplete();
/*    */         } 
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 86 */       if (!this.done) {
/* 87 */         this.done = true;
/* 88 */         this.downstream.onError(t);
/*    */       } else {
/* 90 */         RxJavaPlugins.onError(t);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 96 */       if (!this.done) {
/* 97 */         this.done = true;
/* 98 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeUntilPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */