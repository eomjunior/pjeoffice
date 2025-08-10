/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*     */ public final class ObservableWithLatestFromMany<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   @Nullable
/*     */   final ObservableSource<?>[] otherArray;
/*     */   @Nullable
/*     */   final Iterable<? extends ObservableSource<?>> otherIterable;
/*     */   @NonNull
/*     */   final Function<? super Object[], R> combiner;
/*     */   
/*     */   public ObservableWithLatestFromMany(@NonNull ObservableSource<T> source, @NonNull ObservableSource<?>[] otherArray, @NonNull Function<? super Object[], R> combiner) {
/*  48 */     super(source);
/*  49 */     this.otherArray = otherArray;
/*  50 */     this.otherIterable = null;
/*  51 */     this.combiner = combiner;
/*     */   }
/*     */   
/*     */   public ObservableWithLatestFromMany(@NonNull ObservableSource<T> source, @NonNull Iterable<? extends ObservableSource<?>> otherIterable, @NonNull Function<? super Object[], R> combiner) {
/*  55 */     super(source);
/*  56 */     this.otherArray = null;
/*  57 */     this.otherIterable = otherIterable;
/*  58 */     this.combiner = combiner;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*     */     ObservableSource[] arrayOfObservableSource;
/*  63 */     ObservableSource<?>[] others = this.otherArray;
/*  64 */     int n = 0;
/*  65 */     if (others == null) {
/*  66 */       arrayOfObservableSource = new ObservableSource[8];
/*     */       
/*     */       try {
/*  69 */         for (ObservableSource<?> p : this.otherIterable) {
/*  70 */           if (n == arrayOfObservableSource.length) {
/*  71 */             arrayOfObservableSource = Arrays.<ObservableSource>copyOf(arrayOfObservableSource, n + (n >> 1));
/*     */           }
/*  73 */           arrayOfObservableSource[n++] = p;
/*     */         } 
/*  75 */       } catch (Throwable ex) {
/*  76 */         Exceptions.throwIfFatal(ex);
/*  77 */         EmptyDisposable.error(ex, observer);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } else {
/*  82 */       n = arrayOfObservableSource.length;
/*     */     } 
/*     */     
/*  85 */     if (n == 0) {
/*  86 */       (new ObservableMap<Object, R>(this.source, new SingletonArrayFunc())).subscribeActual(observer);
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     WithLatestFromObserver<T, R> parent = new WithLatestFromObserver<T, R>(observer, this.combiner, n);
/*  91 */     observer.onSubscribe(parent);
/*  92 */     parent.subscribe((ObservableSource<?>[])arrayOfObservableSource, n);
/*     */     
/*  94 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestFromObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1577321883966341961L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super Object[], R> combiner;
/*     */     
/*     */     final ObservableWithLatestFromMany.WithLatestInnerObserver[] observers;
/*     */     
/*     */     final AtomicReferenceArray<Object> values;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     volatile boolean done;
/*     */     
/*     */     WithLatestFromObserver(Observer<? super R> actual, Function<? super Object[], R> combiner, int n) {
/* 118 */       this.downstream = actual;
/* 119 */       this.combiner = combiner;
/* 120 */       ObservableWithLatestFromMany.WithLatestInnerObserver[] s = new ObservableWithLatestFromMany.WithLatestInnerObserver[n];
/* 121 */       for (int i = 0; i < n; i++) {
/* 122 */         s[i] = new ObservableWithLatestFromMany.WithLatestInnerObserver(this, i);
/*     */       }
/* 124 */       this.observers = s;
/* 125 */       this.values = new AtomicReferenceArray(n);
/* 126 */       this.upstream = new AtomicReference<Disposable>();
/* 127 */       this.error = new AtomicThrowable();
/*     */     }
/*     */     
/*     */     void subscribe(ObservableSource<?>[] others, int n) {
/* 131 */       ObservableWithLatestFromMany.WithLatestInnerObserver[] observers = this.observers;
/* 132 */       AtomicReference<Disposable> upstream = this.upstream;
/* 133 */       for (int i = 0; i < n; i++) {
/* 134 */         if (DisposableHelper.isDisposed(upstream.get()) || this.done) {
/*     */           return;
/*     */         }
/* 137 */         others[i].subscribe(observers[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 143 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       R v;
/* 148 */       if (this.done) {
/*     */         return;
/*     */       }
/* 151 */       AtomicReferenceArray<Object> ara = this.values;
/* 152 */       int n = ara.length();
/* 153 */       Object[] objects = new Object[n + 1];
/* 154 */       objects[0] = t;
/*     */       
/* 156 */       for (int i = 0; i < n; i++) {
/* 157 */         Object o = ara.get(i);
/* 158 */         if (o == null) {
/*     */           return;
/*     */         }
/*     */         
/* 162 */         objects[i + 1] = o;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 168 */         v = (R)ObjectHelper.requireNonNull(this.combiner.apply(objects), "combiner returned a null value");
/* 169 */       } catch (Throwable ex) {
/* 170 */         Exceptions.throwIfFatal(ex);
/* 171 */         dispose();
/* 172 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 176 */       HalfSerializer.onNext(this.downstream, v, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 181 */       if (this.done) {
/* 182 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 185 */       this.done = true;
/* 186 */       cancelAllBut(-1);
/* 187 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 192 */       if (!this.done) {
/* 193 */         this.done = true;
/* 194 */         cancelAllBut(-1);
/* 195 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 201 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 206 */       DisposableHelper.dispose(this.upstream);
/* 207 */       for (ObservableWithLatestFromMany.WithLatestInnerObserver observer : this.observers) {
/* 208 */         observer.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext(int index, Object o) {
/* 213 */       this.values.set(index, o);
/*     */     }
/*     */     
/*     */     void innerError(int index, Throwable t) {
/* 217 */       this.done = true;
/* 218 */       DisposableHelper.dispose(this.upstream);
/* 219 */       cancelAllBut(index);
/* 220 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */     
/*     */     void innerComplete(int index, boolean nonEmpty) {
/* 224 */       if (!nonEmpty) {
/* 225 */         this.done = true;
/* 226 */         cancelAllBut(index);
/* 227 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAllBut(int index) {
/* 232 */       ObservableWithLatestFromMany.WithLatestInnerObserver[] observers = this.observers;
/* 233 */       for (int i = 0; i < observers.length; i++) {
/* 234 */         if (i != index) {
/* 235 */           observers[i].dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestInnerObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 3256684027868224024L;
/*     */     
/*     */     final ObservableWithLatestFromMany.WithLatestFromObserver<?, ?> parent;
/*     */     
/*     */     final int index;
/*     */     boolean hasValue;
/*     */     
/*     */     WithLatestInnerObserver(ObservableWithLatestFromMany.WithLatestFromObserver<?, ?> parent, int index) {
/* 254 */       this.parent = parent;
/* 255 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 260 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 265 */       if (!this.hasValue) {
/* 266 */         this.hasValue = true;
/*     */       }
/* 268 */       this.parent.innerNext(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 273 */       this.parent.innerError(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 278 */       this.parent.innerComplete(this.index, this.hasValue);
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 282 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */   
/*     */   final class SingletonArrayFunc
/*     */     implements Function<T, R> {
/*     */     public R apply(T t) throws Exception {
/* 289 */       return (R)ObjectHelper.requireNonNull(ObservableWithLatestFromMany.this.combiner.apply(new Object[] { t }, ), "The combiner returned a null value");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWithLatestFromMany.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */