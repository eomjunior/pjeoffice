/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeDoAfterSuccess<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   final Consumer<? super T> onAfterSuccess;
/*    */   
/*    */   public MaybeDoAfterSuccess(MaybeSource<T> source, Consumer<? super T> onAfterSuccess) {
/* 34 */     super(source);
/* 35 */     this.onAfterSuccess = onAfterSuccess;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 40 */     this.source.subscribe(new DoAfterObserver<T>(observer, this.onAfterSuccess));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DoAfterObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     final Consumer<? super T> onAfterSuccess;
/*    */     Disposable upstream;
/*    */     
/*    */     DoAfterObserver(MaybeObserver<? super T> actual, Consumer<? super T> onAfterSuccess) {
/* 52 */       this.downstream = actual;
/* 53 */       this.onAfterSuccess = onAfterSuccess;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 58 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 59 */         this.upstream = d;
/*    */         
/* 61 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T t) {
/* 67 */       this.downstream.onSuccess(t);
/*    */       
/*    */       try {
/* 70 */         this.onAfterSuccess.accept(t);
/* 71 */       } catch (Throwable ex) {
/* 72 */         Exceptions.throwIfFatal(ex);
/*    */         
/* 74 */         RxJavaPlugins.onError(ex);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 80 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 85 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 90 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 95 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDoAfterSuccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */