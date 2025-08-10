/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Predicate;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class ObservableSkipWhile<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Predicate<? super T> predicate;
/*    */   
/*    */   public ObservableSkipWhile(ObservableSource<T> source, Predicate<? super T> predicate) {
/* 25 */     super(source);
/* 26 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 31 */     this.source.subscribe(new SkipWhileObserver<T>(observer, this.predicate));
/*    */   }
/*    */   
/*    */   static final class SkipWhileObserver<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super T> downstream;
/*    */     final Predicate<? super T> predicate;
/*    */     
/*    */     SkipWhileObserver(Observer<? super T> actual, Predicate<? super T> predicate) {
/* 40 */       this.downstream = actual;
/* 41 */       this.predicate = predicate;
/*    */     }
/*    */     Disposable upstream; boolean notSkipping;
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 46 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 47 */         this.upstream = d;
/* 48 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 54 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 59 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 64 */       if (this.notSkipping) {
/* 65 */         this.downstream.onNext(t);
/*    */       } else {
/*    */         boolean b;
/*    */         try {
/* 69 */           b = this.predicate.test(t);
/* 70 */         } catch (Throwable e) {
/* 71 */           Exceptions.throwIfFatal(e);
/* 72 */           this.upstream.dispose();
/* 73 */           this.downstream.onError(e);
/*    */           return;
/*    */         } 
/* 76 */         if (!b) {
/* 77 */           this.notSkipping = true;
/* 78 */           this.downstream.onNext(t);
/*    */         } 
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 85 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 90 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSkipWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */