/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ public final class MaybeFlatMapSingle<T, R>
/*     */   extends Single<R>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatMapSingle(MaybeSource<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  41 */     this.source = source;
/*  42 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super R> downstream) {
/*  47 */     this.source.subscribe(new FlatMapMaybeObserver<T, R>(downstream, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapMaybeObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4827726964688405508L;
/*     */     
/*     */     final SingleObserver<? super R> downstream;
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     FlatMapMaybeObserver(SingleObserver<? super R> actual, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  61 */       this.downstream = actual;
/*  62 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  67 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  72 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  77 */       if (DisposableHelper.setOnce(this, d)) {
/*  78 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       SingleSource<? extends R> ss;
/*     */       try {
/*  87 */         ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null SingleSource");
/*  88 */       } catch (Throwable ex) {
/*  89 */         Exceptions.throwIfFatal(ex);
/*  90 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  94 */       if (!isDisposed()) {
/*  95 */         ss.subscribe(new MaybeFlatMapSingle.FlatMapSingleObserver<R>(this, this.downstream));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 101 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 106 */       this.downstream.onError(new NoSuchElementException());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FlatMapSingleObserver<R>
/*     */     implements SingleObserver<R>
/*     */   {
/*     */     final AtomicReference<Disposable> parent;
/*     */     final SingleObserver<? super R> downstream;
/*     */     
/*     */     FlatMapSingleObserver(AtomicReference<Disposable> parent, SingleObserver<? super R> downstream) {
/* 117 */       this.parent = parent;
/* 118 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 123 */       DisposableHelper.replace(this.parent, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(R value) {
/* 128 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 133 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */