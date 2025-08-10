/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class MaybeFlatMapSingleElement<T, R>
/*     */   extends Maybe<R>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatMapSingleElement(MaybeSource<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  39 */     this.source = source;
/*  40 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> downstream) {
/*  45 */     this.source.subscribe(new FlatMapMaybeObserver<T, R>(downstream, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapMaybeObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4827726964688405508L;
/*     */     
/*     */     final MaybeObserver<? super R> downstream;
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     FlatMapMaybeObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  59 */       this.downstream = actual;
/*  60 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  70 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       if (DisposableHelper.setOnce(this, d)) {
/*  76 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       SingleSource<? extends R> ss;
/*     */       try {
/*  85 */         ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null SingleSource");
/*  86 */       } catch (Throwable ex) {
/*  87 */         Exceptions.throwIfFatal(ex);
/*  88 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  92 */       ss.subscribe(new MaybeFlatMapSingleElement.FlatMapSingleObserver<R>(this, this.downstream));
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
/*     */   
/*     */   static final class FlatMapSingleObserver<R>
/*     */     implements SingleObserver<R>
/*     */   {
/*     */     final AtomicReference<Disposable> parent;
/*     */     final MaybeObserver<? super R> downstream;
/*     */     
/*     */     FlatMapSingleObserver(AtomicReference<Disposable> parent, MaybeObserver<? super R> downstream) {
/* 113 */       this.parent = parent;
/* 114 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 119 */       DisposableHelper.replace(this.parent, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(R value) {
/* 124 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 129 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapSingleElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */