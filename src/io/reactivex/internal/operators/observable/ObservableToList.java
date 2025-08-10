/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
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
/*     */ public final class ObservableToList<T, U extends Collection<? super T>>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<U> collectionSupplier;
/*     */   
/*     */   public ObservableToList(ObservableSource<T> source, int defaultCapacityHint) {
/*  32 */     super(source);
/*  33 */     this.collectionSupplier = Functions.createArrayList(defaultCapacityHint);
/*     */   }
/*     */   
/*     */   public ObservableToList(ObservableSource<T> source, Callable<U> collectionSupplier) {
/*  37 */     super(source);
/*  38 */     this.collectionSupplier = collectionSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super U> t) {
/*     */     Collection collection;
/*     */     try {
/*  45 */       collection = (Collection)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/*  46 */     } catch (Throwable e) {
/*  47 */       Exceptions.throwIfFatal(e);
/*  48 */       EmptyDisposable.error(e, t);
/*     */       return;
/*     */     } 
/*  51 */     this.source.subscribe(new ToListObserver<Object, U>(t, (U)collection));
/*     */   }
/*     */   
/*     */   static final class ToListObserver<T, U extends Collection<? super T>>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super U> downstream;
/*     */     Disposable upstream;
/*     */     U collection;
/*     */     
/*     */     ToListObserver(Observer<? super U> actual, U collection) {
/*  62 */       this.downstream = actual;
/*  63 */       this.collection = collection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         this.upstream = d;
/*  70 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  76 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  81 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  86 */       this.collection.add(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  91 */       this.collection = null;
/*  92 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  97 */       U c = this.collection;
/*  98 */       this.collection = null;
/*  99 */       this.downstream.onNext(c);
/* 100 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableToList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */