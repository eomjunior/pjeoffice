/*     */ package io.reactivex.internal.operators.single;
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
/*     */ public final class SingleFlatMapMaybe<T, R>
/*     */   extends Maybe<R>
/*     */ {
/*     */   final SingleSource<? extends T> source;
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMapMaybe(SingleSource<? extends T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  35 */     this.mapper = mapper;
/*  36 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> downstream) {
/*  41 */     this.source.subscribe(new FlatMapSingleObserver<T, R>(downstream, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapSingleObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5843758257109742742L;
/*     */     
/*     */     final MaybeObserver<? super R> downstream;
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     FlatMapSingleObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  55 */       this.downstream = actual;
/*  56 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  61 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  66 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  71 */       if (DisposableHelper.setOnce(this, d)) {
/*  72 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       MaybeSource<? extends R> ms;
/*     */       try {
/*  81 */         ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null MaybeSource");
/*  82 */       } catch (Throwable ex) {
/*  83 */         Exceptions.throwIfFatal(ex);
/*  84 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  88 */       if (!isDisposed()) {
/*  89 */         ms.subscribe(new SingleFlatMapMaybe.FlatMapMaybeObserver<R>(this, this.downstream));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  95 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FlatMapMaybeObserver<R>
/*     */     implements MaybeObserver<R>
/*     */   {
/*     */     final AtomicReference<Disposable> parent;
/*     */     final MaybeObserver<? super R> downstream;
/*     */     
/*     */     FlatMapMaybeObserver(AtomicReference<Disposable> parent, MaybeObserver<? super R> downstream) {
/* 106 */       this.parent = parent;
/* 107 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 112 */       DisposableHelper.replace(this.parent, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(R value) {
/* 117 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 122 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 127 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */