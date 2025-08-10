/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableToListSingle<T, U extends Collection<? super T>>
/*     */   extends Single<U>
/*     */   implements FuseToObservable<U>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final Callable<U> collectionSupplier;
/*     */   
/*     */   public ObservableToListSingle(ObservableSource<T> source, int defaultCapacityHint) {
/*  39 */     this.source = source;
/*  40 */     this.collectionSupplier = Functions.createArrayList(defaultCapacityHint);
/*     */   }
/*     */   
/*     */   public ObservableToListSingle(ObservableSource<T> source, Callable<U> collectionSupplier) {
/*  44 */     this.source = source;
/*  45 */     this.collectionSupplier = collectionSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(SingleObserver<? super U> t) {
/*     */     Collection collection;
/*     */     try {
/*  52 */       collection = (Collection)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/*  53 */     } catch (Throwable e) {
/*  54 */       Exceptions.throwIfFatal(e);
/*  55 */       EmptyDisposable.error(e, t);
/*     */       return;
/*     */     } 
/*  58 */     this.source.subscribe(new ToListObserver<Object, U>(t, (U)collection));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<U> fuseToObservable() {
/*  63 */     return RxJavaPlugins.onAssembly(new ObservableToList<T, U>(this.source, this.collectionSupplier));
/*     */   }
/*     */   
/*     */   static final class ToListObserver<T, U extends Collection<? super T>>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super U> downstream;
/*     */     U collection;
/*     */     Disposable upstream;
/*     */     
/*     */     ToListObserver(SingleObserver<? super U> actual, U collection) {
/*  74 */       this.downstream = actual;
/*  75 */       this.collection = collection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  80 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  81 */         this.upstream = d;
/*  82 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  88 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  93 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  98 */       this.collection.add(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 103 */       this.collection = null;
/* 104 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 109 */       U c = this.collection;
/* 110 */       this.collection = null;
/* 111 */       this.downstream.onSuccess(c);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableToListSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */