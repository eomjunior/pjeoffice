/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
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
/*     */ 
/*     */ 
/*     */ public final class SingleFlatMapObservable<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMapObservable(SingleSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  41 */     this.source = source;
/*  42 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  47 */     FlatMapObserver<T, R> parent = new FlatMapObserver<T, R>(observer, this.mapper);
/*  48 */     observer.onSubscribe(parent);
/*  49 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<R>, SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8948264376121066672L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */     
/*     */     FlatMapObserver(Observer<? super R> downstream, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  63 */       this.downstream = downstream;
/*  64 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/*  69 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  74 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  79 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  84 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  89 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       DisposableHelper.replace(this, d);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       ObservableSource<? extends R> o;
/*     */       try {
/* 102 */         o = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
/* 103 */       } catch (Throwable ex) {
/* 104 */         Exceptions.throwIfFatal(ex);
/* 105 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 109 */       o.subscribe(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/SingleFlatMapObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */