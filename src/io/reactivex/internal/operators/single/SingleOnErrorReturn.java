/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
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
/*    */ public final class SingleOnErrorReturn<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   final Function<? super Throwable, ? extends T> valueSupplier;
/*    */   final T value;
/*    */   
/*    */   public SingleOnErrorReturn(SingleSource<? extends T> source, Function<? super Throwable, ? extends T> valueSupplier, T value) {
/* 30 */     this.source = source;
/* 31 */     this.valueSupplier = valueSupplier;
/* 32 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 38 */     this.source.subscribe(new OnErrorReturn(observer));
/*    */   }
/*    */   
/*    */   final class OnErrorReturn
/*    */     implements SingleObserver<T> {
/*    */     private final SingleObserver<? super T> observer;
/*    */     
/*    */     OnErrorReturn(SingleObserver<? super T> observer) {
/* 46 */       this.observer = observer;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       T v;
/* 53 */       if (SingleOnErrorReturn.this.valueSupplier != null) {
/*    */         try {
/* 55 */           v = (T)SingleOnErrorReturn.this.valueSupplier.apply(e);
/* 56 */         } catch (Throwable ex) {
/* 57 */           Exceptions.throwIfFatal(ex);
/* 58 */           this.observer.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */           return;
/*    */         } 
/*    */       } else {
/* 62 */         v = SingleOnErrorReturn.this.value;
/*    */       } 
/*    */       
/* 65 */       if (v == null) {
/* 66 */         NullPointerException npe = new NullPointerException("Value supplied was null");
/* 67 */         npe.initCause(e);
/* 68 */         this.observer.onError(npe);
/*    */         
/*    */         return;
/*    */       } 
/* 72 */       this.observer.onSuccess(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 77 */       this.observer.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 82 */       this.observer.onSuccess(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */