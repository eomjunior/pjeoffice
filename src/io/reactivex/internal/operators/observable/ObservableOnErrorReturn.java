/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
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
/*    */ public final class ObservableOnErrorReturn<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Function<? super Throwable, ? extends T> valueSupplier;
/*    */   
/*    */   public ObservableOnErrorReturn(ObservableSource<T> source, Function<? super Throwable, ? extends T> valueSupplier) {
/* 25 */     super(source);
/* 26 */     this.valueSupplier = valueSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> t) {
/* 31 */     this.source.subscribe(new OnErrorReturnObserver<T>(t, this.valueSupplier));
/*    */   }
/*    */   
/*    */   static final class OnErrorReturnObserver<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super T> downstream;
/*    */     final Function<? super Throwable, ? extends T> valueSupplier;
/*    */     Disposable upstream;
/*    */     
/*    */     OnErrorReturnObserver(Observer<? super T> actual, Function<? super Throwable, ? extends T> valueSupplier) {
/* 41 */       this.downstream = actual;
/* 42 */       this.valueSupplier = valueSupplier;
/*    */     }
/*    */ 
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
/* 65 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/*    */       T v;
/*    */       try {
/* 72 */         v = (T)this.valueSupplier.apply(t);
/* 73 */       } catch (Throwable e) {
/* 74 */         Exceptions.throwIfFatal(e);
/* 75 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*    */         
/*    */         return;
/*    */       } 
/* 79 */       if (v == null) {
/* 80 */         NullPointerException e = new NullPointerException("The supplied value is null");
/* 81 */         e.initCause(t);
/* 82 */         this.downstream.onError(e);
/*    */         
/*    */         return;
/*    */       } 
/* 86 */       this.downstream.onNext(v);
/* 87 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 92 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */