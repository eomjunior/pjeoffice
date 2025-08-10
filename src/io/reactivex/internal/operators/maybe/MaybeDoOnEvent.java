/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiConsumer;
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
/*     */ public final class MaybeDoOnEvent<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final BiConsumer<? super T, ? super Throwable> onEvent;
/*     */   
/*     */   public MaybeDoOnEvent(MaybeSource<T> source, BiConsumer<? super T, ? super Throwable> onEvent) {
/*  33 */     super(source);
/*  34 */     this.onEvent = onEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  39 */     this.source.subscribe(new DoOnEventMaybeObserver<T>(observer, this.onEvent));
/*     */   }
/*     */   
/*     */   static final class DoOnEventMaybeObserver<T>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final BiConsumer<? super T, ? super Throwable> onEvent;
/*     */     Disposable upstream;
/*     */     
/*     */     DoOnEventMaybeObserver(MaybeObserver<? super T> actual, BiConsumer<? super T, ? super Throwable> onEvent) {
/*  50 */       this.downstream = actual;
/*  51 */       this.onEvent = onEvent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  56 */       this.upstream.dispose();
/*  57 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  62 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  67 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  68 */         this.upstream = d;
/*     */         
/*  70 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  76 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/*     */       try {
/*  79 */         this.onEvent.accept(value, null);
/*  80 */       } catch (Throwable ex) {
/*  81 */         Exceptions.throwIfFatal(ex);
/*  82 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  86 */       this.downstream.onSuccess(value);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       CompositeException compositeException;
/*  91 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/*     */       try {
/*  94 */         this.onEvent.accept(null, e);
/*  95 */       } catch (Throwable ex) {
/*  96 */         Exceptions.throwIfFatal(ex);
/*  97 */         compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */       } 
/*     */       
/* 100 */       this.downstream.onError((Throwable)compositeException);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 105 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/*     */       try {
/* 108 */         this.onEvent.accept(null, null);
/* 109 */       } catch (Throwable ex) {
/* 110 */         Exceptions.throwIfFatal(ex);
/* 111 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 115 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDoOnEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */