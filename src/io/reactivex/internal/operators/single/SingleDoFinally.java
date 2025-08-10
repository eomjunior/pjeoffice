/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SingleDoFinally<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Action onFinally;
/*     */   
/*     */   public SingleDoFinally(SingleSource<T> source, Action onFinally) {
/*  38 */     this.source = source;
/*  39 */     this.onFinally = onFinally;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  44 */     this.source.subscribe(new DoFinallyObserver<T>(observer, this.onFinally));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoFinallyObserver<T>
/*     */     extends AtomicInteger
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4109457741734051389L;
/*     */     final SingleObserver<? super T> downstream;
/*     */     final Action onFinally;
/*     */     Disposable upstream;
/*     */     
/*     */     DoFinallyObserver(SingleObserver<? super T> actual, Action onFinally) {
/*  58 */       this.downstream = actual;
/*  59 */       this.onFinally = onFinally;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  64 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  65 */         this.upstream = d;
/*     */         
/*  67 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*  73 */       this.downstream.onSuccess(t);
/*  74 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  79 */       this.downstream.onError(t);
/*  80 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  85 */       this.upstream.dispose();
/*  86 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  91 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     void runFinally() {
/*  95 */       if (compareAndSet(0, 1))
/*     */         try {
/*  97 */           this.onFinally.run();
/*  98 */         } catch (Throwable ex) {
/*  99 */           Exceptions.throwIfFatal(ex);
/* 100 */           RxJavaPlugins.onError(ex);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */