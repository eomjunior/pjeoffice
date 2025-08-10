/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
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
/*     */ public final class MaybeOnErrorComplete<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super Throwable> predicate;
/*     */   
/*     */   public MaybeOnErrorComplete(MaybeSource<T> source, Predicate<? super Throwable> predicate) {
/*  34 */     super(source);
/*  35 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  40 */     this.source.subscribe(new OnErrorCompleteMaybeObserver<T>(observer, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OnErrorCompleteMaybeObserver<T>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final Predicate<? super Throwable> predicate;
/*     */     Disposable upstream;
/*     */     
/*     */     OnErrorCompleteMaybeObserver(MaybeObserver<? super T> actual, Predicate<? super Throwable> predicate) {
/*  52 */       this.downstream = actual;
/*  53 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  58 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  59 */         this.upstream = d;
/*     */         
/*  61 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  67 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       boolean b;
/*     */       try {
/*  75 */         b = this.predicate.test(e);
/*  76 */       } catch (Throwable ex) {
/*  77 */         Exceptions.throwIfFatal(ex);
/*  78 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*     */         
/*     */         return;
/*     */       } 
/*  82 */       if (b) {
/*  83 */         this.downstream.onComplete();
/*     */       } else {
/*  85 */         this.downstream.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  96 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 101 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorComplete.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */