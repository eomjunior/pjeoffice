/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
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
/*     */ public final class ObservableZipIterable<T, U, V>
/*     */   extends Observable<V>
/*     */ {
/*     */   final Observable<? extends T> source;
/*     */   final Iterable<U> other;
/*     */   final BiFunction<? super T, ? super U, ? extends V> zipper;
/*     */   
/*     */   public ObservableZipIterable(Observable<? extends T> source, Iterable<U> other, BiFunction<? super T, ? super U, ? extends V> zipper) {
/*  34 */     this.source = source;
/*  35 */     this.other = other;
/*  36 */     this.zipper = zipper;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super V> t) {
/*     */     Iterator<U> it;
/*     */     boolean b;
/*     */     try {
/*  44 */       it = (Iterator<U>)ObjectHelper.requireNonNull(this.other.iterator(), "The iterator returned by other is null");
/*  45 */     } catch (Throwable e) {
/*  46 */       Exceptions.throwIfFatal(e);
/*  47 */       EmptyDisposable.error(e, t);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  54 */       b = it.hasNext();
/*  55 */     } catch (Throwable e) {
/*  56 */       Exceptions.throwIfFatal(e);
/*  57 */       EmptyDisposable.error(e, t);
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     if (!b) {
/*  62 */       EmptyDisposable.complete(t);
/*     */       
/*     */       return;
/*     */     } 
/*  66 */     this.source.subscribe(new ZipIterableObserver<T, U, V>(t, it, this.zipper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipIterableObserver<T, U, V>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super V> downstream;
/*     */     final Iterator<U> iterator;
/*     */     final BiFunction<? super T, ? super U, ? extends V> zipper;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     ZipIterableObserver(Observer<? super V> actual, Iterator<U> iterator, BiFunction<? super T, ? super U, ? extends V> zipper) {
/*  80 */       this.downstream = actual;
/*  81 */       this.iterator = iterator;
/*  82 */       this.zipper = zipper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  87 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  88 */         this.upstream = d;
/*  89 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  95 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 100 */       return this.upstream.isDisposed();
/*     */     } public void onNext(T t) {
/*     */       U u;
/*     */       V v;
/*     */       boolean b;
/* 105 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 112 */         u = (U)ObjectHelper.requireNonNull(this.iterator.next(), "The iterator returned a null value");
/* 113 */       } catch (Throwable e) {
/* 114 */         Exceptions.throwIfFatal(e);
/* 115 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 121 */         v = (V)ObjectHelper.requireNonNull(this.zipper.apply(t, u), "The zipper function returned a null value");
/* 122 */       } catch (Throwable e) {
/* 123 */         Exceptions.throwIfFatal(e);
/* 124 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       this.downstream.onNext(v);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 133 */         b = this.iterator.hasNext();
/* 134 */       } catch (Throwable e) {
/* 135 */         Exceptions.throwIfFatal(e);
/* 136 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/* 140 */       if (!b) {
/* 141 */         this.done = true;
/* 142 */         this.upstream.dispose();
/* 143 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */     
/*     */     void error(Throwable e) {
/* 148 */       this.done = true;
/* 149 */       this.upstream.dispose();
/* 150 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 155 */       if (this.done) {
/* 156 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 159 */       this.done = true;
/* 160 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 165 */       if (this.done) {
/*     */         return;
/*     */       }
/* 168 */       this.done = true;
/* 169 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */