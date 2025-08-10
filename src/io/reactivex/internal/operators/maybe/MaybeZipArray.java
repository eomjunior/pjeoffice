/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ public final class MaybeZipArray<T, R>
/*     */   extends Maybe<R>
/*     */ {
/*     */   final MaybeSource<? extends T>[] sources;
/*     */   final Function<? super Object[], ? extends R> zipper;
/*     */   
/*     */   public MaybeZipArray(MaybeSource<? extends T>[] sources, Function<? super Object[], ? extends R> zipper) {
/*  33 */     this.sources = sources;
/*  34 */     this.zipper = zipper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*  39 */     MaybeSource<? extends T>[] sources = this.sources;
/*  40 */     int n = sources.length;
/*     */     
/*  42 */     if (n == 1) {
/*  43 */       sources[0].subscribe(new MaybeMap.MapMaybeObserver<Object, R>(observer, new SingletonArrayFunc()));
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
/*  56 */       MaybeSource<? extends T> source = sources[i];
/*     */       
/*  58 */       if (source == null) {
/*  59 */         parent.innerError(new NullPointerException("One of the sources is null"), i);
/*     */         return;
/*     */       } 
/*  62 */       source.subscribe(parent.observers[i]);
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
/*     */     final MaybeObserver<? super R> downstream;
/*     */     
/*     */     final Function<? super Object[], ? extends R> zipper;
/*     */     final MaybeZipArray.ZipMaybeObserver<T>[] observers;
/*     */     final Object[] values;
/*     */     
/*     */     ZipCoordinator(MaybeObserver<? super R> observer, int n, Function<? super Object[], ? extends R> zipper) {
/*  80 */       super(n);
/*  81 */       this.downstream = observer;
/*  82 */       this.zipper = zipper;
/*  83 */       MaybeZipArray.ZipMaybeObserver[] arrayOfZipMaybeObserver = new MaybeZipArray.ZipMaybeObserver[n];
/*  84 */       for (int i = 0; i < n; i++) {
/*  85 */         arrayOfZipMaybeObserver[i] = new MaybeZipArray.ZipMaybeObserver<T>(this, i);
/*     */       }
/*  87 */       this.observers = (MaybeZipArray.ZipMaybeObserver<T>[])arrayOfZipMaybeObserver;
/*  88 */       this.values = new Object[n];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  93 */       return (get() <= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  98 */       if (getAndSet(0) > 0) {
/*  99 */         for (MaybeZipArray.ZipMaybeObserver<?> d : this.observers) {
/* 100 */           d.dispose();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     void innerSuccess(T value, int index) {
/* 106 */       this.values[index] = value;
/* 107 */       if (decrementAndGet() == 0) {
/*     */         R v;
/*     */         
/*     */         try {
/* 111 */           v = (R)ObjectHelper.requireNonNull(this.zipper.apply(this.values), "The zipper returned a null value");
/* 112 */         } catch (Throwable ex) {
/* 113 */           Exceptions.throwIfFatal(ex);
/* 114 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 118 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */     
/*     */     void disposeExcept(int index) {
/* 123 */       MaybeZipArray.ZipMaybeObserver<T>[] observers = this.observers;
/* 124 */       int n = observers.length; int i;
/* 125 */       for (i = 0; i < index; i++) {
/* 126 */         observers[i].dispose();
/*     */       }
/* 128 */       for (i = index + 1; i < n; i++) {
/* 129 */         observers[i].dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex, int index) {
/* 134 */       if (getAndSet(0) > 0) {
/* 135 */         disposeExcept(index);
/* 136 */         this.downstream.onError(ex);
/*     */       } else {
/* 138 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete(int index) {
/* 143 */       if (getAndSet(0) > 0) {
/* 144 */         disposeExcept(index);
/* 145 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = 3323743579927613702L;
/*     */     
/*     */     final MaybeZipArray.ZipCoordinator<T, ?> parent;
/*     */     final int index;
/*     */     
/*     */     ZipMaybeObserver(MaybeZipArray.ZipCoordinator<T, ?> parent, int index) {
/* 161 */       this.parent = parent;
/* 162 */       this.index = index;
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 166 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 171 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 176 */       this.parent.innerSuccess(value, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 181 */       this.parent.innerError(e, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 186 */       this.parent.innerComplete(this.index);
/*     */     }
/*     */   }
/*     */   
/*     */   final class SingletonArrayFunc
/*     */     implements Function<T, R> {
/*     */     public R apply(T t) throws Exception {
/* 193 */       return (R)ObjectHelper.requireNonNull(MaybeZipArray.this.zipper.apply(new Object[] { t }, ), "The zipper returned a null value");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeZipArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */