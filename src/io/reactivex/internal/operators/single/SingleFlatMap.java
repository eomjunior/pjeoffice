/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
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
/*     */ public final class SingleFlatMap<T, R>
/*     */   extends Single<R>
/*     */ {
/*     */   final SingleSource<? extends T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMap(SingleSource<? extends T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  30 */     this.mapper = mapper;
/*  31 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super R> downstream) {
/*  36 */     this.source.subscribe(new SingleFlatMapCallback<T, R>(downstream, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleFlatMapCallback<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 3258103020495908596L;
/*     */     
/*     */     final SingleObserver<? super R> downstream;
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     SingleFlatMapCallback(SingleObserver<? super R> actual, Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  50 */       this.downstream = actual;
/*  51 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  56 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  61 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  66 */       if (DisposableHelper.setOnce(this, d)) {
/*  67 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       SingleSource<? extends R> o;
/*     */       try {
/*  76 */         o = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The single returned by the mapper is null");
/*  77 */       } catch (Throwable e) {
/*  78 */         Exceptions.throwIfFatal(e);
/*  79 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  83 */       if (!isDisposed()) {
/*  84 */         o.subscribe(new FlatMapSingleObserver<R>(this, this.downstream));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  90 */       this.downstream.onError(e);
/*     */     }
/*     */     
/*     */     static final class FlatMapSingleObserver<R>
/*     */       implements SingleObserver<R>
/*     */     {
/*     */       final AtomicReference<Disposable> parent;
/*     */       final SingleObserver<? super R> downstream;
/*     */       
/*     */       FlatMapSingleObserver(AtomicReference<Disposable> parent, SingleObserver<? super R> downstream) {
/* 100 */         this.parent = parent;
/* 101 */         this.downstream = downstream;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 106 */         DisposableHelper.replace(this.parent, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 111 */         this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 116 */         this.downstream.onError(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */