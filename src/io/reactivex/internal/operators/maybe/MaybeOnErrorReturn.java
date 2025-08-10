/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class MaybeOnErrorReturn<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   final Function<? super Throwable, ? extends T> valueSupplier;
/*    */   
/*    */   public MaybeOnErrorReturn(MaybeSource<T> source, Function<? super Throwable, ? extends T> valueSupplier) {
/* 33 */     super(source);
/* 34 */     this.valueSupplier = valueSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 39 */     this.source.subscribe(new OnErrorReturnMaybeObserver<T>(observer, this.valueSupplier));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class OnErrorReturnMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     
/*    */     final Function<? super Throwable, ? extends T> valueSupplier;
/*    */     Disposable upstream;
/*    */     
/*    */     OnErrorReturnMaybeObserver(MaybeObserver<? super T> actual, Function<? super Throwable, ? extends T> valueSupplier) {
/* 52 */       this.downstream = actual;
/* 53 */       this.valueSupplier = valueSupplier;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 58 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 63 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 68 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 69 */         this.upstream = d;
/*    */         
/* 71 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 77 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       T v;
/*    */       try {
/* 85 */         v = (T)ObjectHelper.requireNonNull(this.valueSupplier.apply(e), "The valueSupplier returned a null value");
/* 86 */       } catch (Throwable ex) {
/* 87 */         Exceptions.throwIfFatal(ex);
/* 88 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */         
/*    */         return;
/*    */       } 
/* 92 */       this.downstream.onSuccess(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 97 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */