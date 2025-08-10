/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableScalarXMap
/*     */ {
/*     */   private ObservableScalarXMap() {
/*  35 */     throw new IllegalStateException("No instances!");
/*     */   }
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
/*     */   public static <T, R> boolean tryScalarXMapSubscribe(ObservableSource<T> source, Observer<? super R> observer, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  51 */     if (source instanceof Callable) {
/*     */       T t;
/*     */       ObservableSource<? extends R> r;
/*     */       try {
/*  55 */         t = ((Callable<T>)source).call();
/*  56 */       } catch (Throwable ex) {
/*  57 */         Exceptions.throwIfFatal(ex);
/*  58 */         EmptyDisposable.error(ex, observer);
/*  59 */         return true;
/*     */       } 
/*     */       
/*  62 */       if (t == null) {
/*  63 */         EmptyDisposable.complete(observer);
/*  64 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  70 */         r = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null ObservableSource");
/*  71 */       } catch (Throwable ex) {
/*  72 */         Exceptions.throwIfFatal(ex);
/*  73 */         EmptyDisposable.error(ex, observer);
/*  74 */         return true;
/*     */       } 
/*     */       
/*  77 */       if (r instanceof Callable) {
/*     */         R u;
/*     */         
/*     */         try {
/*  81 */           u = ((Callable)r).call();
/*  82 */         } catch (Throwable ex) {
/*  83 */           Exceptions.throwIfFatal(ex);
/*  84 */           EmptyDisposable.error(ex, observer);
/*  85 */           return true;
/*     */         } 
/*     */         
/*  88 */         if (u == null) {
/*  89 */           EmptyDisposable.complete(observer);
/*  90 */           return true;
/*     */         } 
/*  92 */         ScalarDisposable<R> sd = new ScalarDisposable<R>(observer, u);
/*  93 */         observer.onSubscribe((Disposable)sd);
/*  94 */         sd.run();
/*     */       } else {
/*  96 */         r.subscribe(observer);
/*     */       } 
/*     */       
/*  99 */       return true;
/*     */     } 
/* 101 */     return false;
/*     */   }
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
/*     */   public static <T, U> Observable<U> scalarXMap(T value, Function<? super T, ? extends ObservableSource<? extends U>> mapper) {
/* 116 */     return RxJavaPlugins.onAssembly(new ScalarXMapObservable<T, U>(value, mapper));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ScalarXMapObservable<T, R>
/*     */     extends Observable<R>
/*     */   {
/*     */     final T value;
/*     */ 
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */ 
/*     */ 
/*     */     
/*     */     ScalarXMapObservable(T value, Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/* 133 */       this.value = value;
/* 134 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void subscribeActual(Observer<? super R> observer) {
/*     */       ObservableSource<? extends R> other;
/*     */       try {
/* 142 */         other = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(this.value), "The mapper returned a null ObservableSource");
/* 143 */       } catch (Throwable e) {
/* 144 */         EmptyDisposable.error(e, observer);
/*     */         return;
/*     */       } 
/* 147 */       if (other instanceof Callable) {
/*     */         R u;
/*     */         
/*     */         try {
/* 151 */           u = ((Callable)other).call();
/* 152 */         } catch (Throwable ex) {
/* 153 */           Exceptions.throwIfFatal(ex);
/* 154 */           EmptyDisposable.error(ex, observer);
/*     */           
/*     */           return;
/*     */         } 
/* 158 */         if (u == null) {
/* 159 */           EmptyDisposable.complete(observer);
/*     */           return;
/*     */         } 
/* 162 */         ObservableScalarXMap.ScalarDisposable<R> sd = new ObservableScalarXMap.ScalarDisposable<R>(observer, u);
/* 163 */         observer.onSubscribe((Disposable)sd);
/* 164 */         sd.run();
/*     */       } else {
/* 166 */         other.subscribe(observer);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ScalarDisposable<T>
/*     */     extends AtomicInteger
/*     */     implements QueueDisposable<T>, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 3880992722410194083L;
/*     */     
/*     */     final Observer<? super T> observer;
/*     */     
/*     */     final T value;
/*     */     
/*     */     static final int START = 0;
/*     */     
/*     */     static final int FUSED = 1;
/*     */     
/*     */     static final int ON_NEXT = 2;
/*     */     
/*     */     static final int ON_COMPLETE = 3;
/*     */ 
/*     */     
/*     */     public ScalarDisposable(Observer<? super T> observer, T value) {
/* 192 */       this.observer = observer;
/* 193 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T value) {
/* 198 */       throw new UnsupportedOperationException("Should not be called!");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T v1, T v2) {
/* 203 */       throw new UnsupportedOperationException("Should not be called!");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 209 */       if (get() == 1) {
/* 210 */         lazySet(3);
/* 211 */         return this.value;
/*     */       } 
/* 213 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 218 */       return (get() != 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 223 */       lazySet(3);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 228 */       set(3);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 233 */       return (get() == 3);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 238 */       if ((mode & 0x1) != 0) {
/* 239 */         lazySet(1);
/* 240 */         return 1;
/*     */       } 
/* 242 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 247 */       if (get() == 0 && compareAndSet(0, 2)) {
/* 248 */         this.observer.onNext(this.value);
/* 249 */         if (get() == 2) {
/* 250 */           lazySet(3);
/* 251 */           this.observer.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableScalarXMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */