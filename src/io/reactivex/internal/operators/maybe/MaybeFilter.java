/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class MaybeFilter<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public MaybeFilter(MaybeSource<T> source, Predicate<? super T> predicate) {
/*  33 */     super(source);
/*  34 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  39 */     this.source.subscribe(new FilterMaybeObserver<T>(observer, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FilterMaybeObserver<T>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     
/*     */     FilterMaybeObserver(MaybeObserver<? super T> actual, Predicate<? super T> predicate) {
/*  51 */       this.downstream = actual;
/*  52 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  57 */       Disposable d = this.upstream;
/*  58 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*  59 */       d.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  64 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  69 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  70 */         this.upstream = d;
/*     */         
/*  72 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       boolean b;
/*     */       try {
/*  81 */         b = this.predicate.test(value);
/*  82 */       } catch (Throwable ex) {
/*  83 */         Exceptions.throwIfFatal(ex);
/*  84 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  88 */       if (b) {
/*  89 */         this.downstream.onSuccess(value);
/*     */       } else {
/*  91 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  97 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */