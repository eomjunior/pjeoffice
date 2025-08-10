/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicFuseableObserver;
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
/*     */ 
/*     */ public final class ObservableDistinct<T, K>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, K> keySelector;
/*     */   final Callable<? extends Collection<? super K>> collectionSupplier;
/*     */   
/*     */   public ObservableDistinct(ObservableSource<T> source, Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
/*  35 */     super(source);
/*  36 */     this.keySelector = keySelector;
/*  37 */     this.collectionSupplier = collectionSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*     */     Collection<? super K> collection;
/*     */     try {
/*  45 */       collection = (Collection<? super K>)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/*  46 */     } catch (Throwable ex) {
/*  47 */       Exceptions.throwIfFatal(ex);
/*  48 */       EmptyDisposable.error(ex, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     this.source.subscribe((Observer)new DistinctObserver<T, K>(observer, this.keySelector, collection));
/*     */   }
/*     */   
/*     */   static final class DistinctObserver<T, K>
/*     */     extends BasicFuseableObserver<T, T>
/*     */   {
/*     */     final Collection<? super K> collection;
/*     */     final Function<? super T, K> keySelector;
/*     */     
/*     */     DistinctObserver(Observer<? super T> actual, Function<? super T, K> keySelector, Collection<? super K> collection) {
/*  62 */       super(actual);
/*  63 */       this.keySelector = keySelector;
/*  64 */       this.collection = collection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  69 */       if (this.done) {
/*     */         return;
/*     */       }
/*  72 */       if (this.sourceMode == 0) {
/*     */         boolean b;
/*     */ 
/*     */         
/*     */         try {
/*  77 */           K key = (K)ObjectHelper.requireNonNull(this.keySelector.apply(value), "The keySelector returned a null key");
/*  78 */           b = this.collection.add(key);
/*  79 */         } catch (Throwable ex) {
/*  80 */           fail(ex);
/*     */           
/*     */           return;
/*     */         } 
/*  84 */         if (b) {
/*  85 */           this.downstream.onNext(value);
/*     */         }
/*     */       } else {
/*  88 */         this.downstream.onNext(null);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  94 */       if (this.done) {
/*  95 */         RxJavaPlugins.onError(e);
/*     */       } else {
/*  97 */         this.done = true;
/*  98 */         this.collection.clear();
/*  99 */         this.downstream.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 105 */       if (!this.done) {
/* 106 */         this.done = true;
/* 107 */         this.collection.clear();
/* 108 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 114 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       T v;
/*     */       do {
/* 121 */         v = (T)this.qd.poll();
/*     */       }
/* 123 */       while (v != null && !this.collection.add((K)ObjectHelper.requireNonNull(this.keySelector.apply(v), "The keySelector returned a null key")));
/* 124 */       return v;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 131 */       this.collection.clear();
/* 132 */       super.clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDistinct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */