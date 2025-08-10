/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ public final class MaybeDoFinally<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Action onFinally;
/*     */   
/*     */   public MaybeDoFinally(MaybeSource<T> source, Action onFinally) {
/*  36 */     super(source);
/*  37 */     this.onFinally = onFinally;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  42 */     this.source.subscribe(new DoFinallyObserver<T>(observer, this.onFinally));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoFinallyObserver<T>
/*     */     extends AtomicInteger
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4109457741734051389L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final Action onFinally;
/*     */     Disposable upstream;
/*     */     
/*     */     DoFinallyObserver(MaybeObserver<? super T> actual, Action onFinally) {
/*  56 */       this.downstream = actual;
/*  57 */       this.onFinally = onFinally;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  62 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  63 */         this.upstream = d;
/*     */         
/*  65 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*  71 */       this.downstream.onSuccess(t);
/*  72 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  77 */       this.downstream.onError(t);
/*  78 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  83 */       this.downstream.onComplete();
/*  84 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  89 */       this.upstream.dispose();
/*  90 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  95 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     void runFinally() {
/*  99 */       if (compareAndSet(0, 1))
/*     */         try {
/* 101 */           this.onFinally.run();
/* 102 */         } catch (Throwable ex) {
/* 103 */           Exceptions.throwIfFatal(ex);
/* 104 */           RxJavaPlugins.onError(ex);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */