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
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class SingleZipArray<T, R>
/*     */   extends Single<R>
/*     */ {
/*     */   final SingleSource<? extends T>[] sources;
/*     */   final Function<? super Object[], ? extends R> zipper;
/*     */   
/*     */   public SingleZipArray(SingleSource<? extends T>[] sources, Function<? super Object[], ? extends R> zipper) {
/*  33 */     this.sources = sources;
/*  34 */     this.zipper = zipper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*  39 */     SingleSource<? extends T>[] sources = this.sources;
/*  40 */     int n = sources.length;
/*     */     
/*  42 */     if (n == 1) {
/*  43 */       sources[0].subscribe(new SingleMap.MapSingleObserver<Object, R>(observer, new SingletonArrayFunc()));
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     ZipCoordinator<T, R> parent = new ZipCoordinator<T, R>(observer, n, this.zipper);
/*     */     
/*  49 */     observer.onSubscribe(parent);
/*     */     
/*  51 */     for (int i = 0; i < n; i++) {
/*  52 */       if (parent.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  56 */       SingleSource<? extends T> source = sources[i];
/*     */       
/*  58 */       if (source == null) {
/*  59 */         parent.innerError(new NullPointerException("One of the sources is null"), i);
/*     */         
/*     */         return;
/*     */       } 
/*  63 */       source.subscribe(parent.observers[i]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipCoordinator<T, R>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5556924161382950569L;
/*     */     
/*     */     final SingleObserver<? super R> downstream;
/*     */     
/*     */     final Function<? super Object[], ? extends R> zipper;
/*     */     final SingleZipArray.ZipSingleObserver<T>[] observers;
/*     */     final Object[] values;
/*     */     
/*     */     ZipCoordinator(SingleObserver<? super R> observer, int n, Function<? super Object[], ? extends R> zipper) {
/*  81 */       super(n);
/*  82 */       this.downstream = observer;
/*  83 */       this.zipper = zipper;
/*  84 */       SingleZipArray.ZipSingleObserver[] arrayOfZipSingleObserver = new SingleZipArray.ZipSingleObserver[n];
/*  85 */       for (int i = 0; i < n; i++) {
/*  86 */         arrayOfZipSingleObserver[i] = new SingleZipArray.ZipSingleObserver<T>(this, i);
/*     */       }
/*  88 */       this.observers = (SingleZipArray.ZipSingleObserver<T>[])arrayOfZipSingleObserver;
/*  89 */       this.values = new Object[n];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  94 */       return (get() <= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  99 */       if (getAndSet(0) > 0) {
/* 100 */         for (SingleZipArray.ZipSingleObserver<?> d : this.observers) {
/* 101 */           d.dispose();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void innerSuccess(T value, int index) {
/* 107 */       this.values[index] = value;
/* 108 */       if (decrementAndGet() == 0) {
/*     */         R v;
/*     */         
/*     */         try {
/* 112 */           v = (R)ObjectHelper.requireNonNull(this.zipper.apply(this.values), "The zipper returned a null value");
/* 113 */         } catch (Throwable ex) {
/* 114 */           Exceptions.throwIfFatal(ex);
/* 115 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 119 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */     
/*     */     void disposeExcept(int index) {
/* 124 */       SingleZipArray.ZipSingleObserver<T>[] observers = this.observers;
/* 125 */       int n = observers.length; int i;
/* 126 */       for (i = 0; i < index; i++) {
/* 127 */         observers[i].dispose();
/*     */       }
/* 129 */       for (i = index + 1; i < n; i++) {
/* 130 */         observers[i].dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex, int index) {
/* 135 */       if (getAndSet(0) > 0) {
/* 136 */         disposeExcept(index);
/* 137 */         this.downstream.onError(ex);
/*     */       } else {
/* 139 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipSingleObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = 3323743579927613702L;
/*     */     
/*     */     final SingleZipArray.ZipCoordinator<T, ?> parent;
/*     */     final int index;
/*     */     
/*     */     ZipSingleObserver(SingleZipArray.ZipCoordinator<T, ?> parent, int index) {
/* 155 */       this.parent = parent;
/* 156 */       this.index = index;
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 160 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 165 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 170 */       this.parent.innerSuccess(value, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 175 */       this.parent.innerError(e, this.index);
/*     */     }
/*     */   }
/*     */   
/*     */   final class SingletonArrayFunc
/*     */     implements Function<T, R> {
/*     */     public R apply(T t) throws Exception {
/* 182 */       return (R)ObjectHelper.requireNonNull(SingleZipArray.this.zipper.apply(new Object[] { t }, ), "The zipper returned a null value");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleZipArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */