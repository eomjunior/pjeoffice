/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class ObservableDebounce<T, U>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<U>> debounceSelector;
/*     */   
/*     */   public ObservableDebounce(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<U>> debounceSelector) {
/*  31 */     super(source);
/*  32 */     this.debounceSelector = debounceSelector;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  37 */     this.source.subscribe(new DebounceObserver<T, U>((Observer<? super T>)new SerializedObserver(t), this.debounceSelector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceObserver<T, U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final Function<? super T, ? extends ObservableSource<U>> debounceSelector;
/*     */     Disposable upstream;
/*  47 */     final AtomicReference<Disposable> debouncer = new AtomicReference<Disposable>();
/*     */     
/*     */     volatile long index;
/*     */     
/*     */     boolean done;
/*     */ 
/*     */     
/*     */     DebounceObserver(Observer<? super T> actual, Function<? super T, ? extends ObservableSource<U>> debounceSelector) {
/*  55 */       this.downstream = actual;
/*  56 */       this.debounceSelector = debounceSelector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  61 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  62 */         this.upstream = d;
/*  63 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<U> p;
/*  69 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  73 */       long idx = this.index + 1L;
/*  74 */       this.index = idx;
/*     */       
/*  76 */       Disposable d = this.debouncer.get();
/*  77 */       if (d != null) {
/*  78 */         d.dispose();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  84 */         p = (ObservableSource<U>)ObjectHelper.requireNonNull(this.debounceSelector.apply(t), "The ObservableSource supplied is null");
/*  85 */       } catch (Throwable e) {
/*  86 */         Exceptions.throwIfFatal(e);
/*  87 */         dispose();
/*  88 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  92 */       DebounceInnerObserver<T, U> dis = new DebounceInnerObserver<T, U>(this, idx, t);
/*     */       
/*  94 */       if (this.debouncer.compareAndSet(d, dis)) {
/*  95 */         p.subscribe((Observer)dis);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 101 */       DisposableHelper.dispose(this.debouncer);
/* 102 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 107 */       if (this.done) {
/*     */         return;
/*     */       }
/* 110 */       this.done = true;
/* 111 */       Disposable d = this.debouncer.get();
/* 112 */       if (d != DisposableHelper.DISPOSED) {
/*     */         
/* 114 */         DebounceInnerObserver<T, U> dis = (DebounceInnerObserver<T, U>)d;
/* 115 */         if (dis != null) {
/* 116 */           dis.emit();
/*     */         }
/* 118 */         DisposableHelper.dispose(this.debouncer);
/* 119 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 125 */       this.upstream.dispose();
/* 126 */       DisposableHelper.dispose(this.debouncer);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 131 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     void emit(long idx, T value) {
/* 135 */       if (idx == this.index) {
/* 136 */         this.downstream.onNext(value);
/*     */       }
/*     */     }
/*     */     
/*     */     static final class DebounceInnerObserver<T, U>
/*     */       extends DisposableObserver<U>
/*     */     {
/*     */       final ObservableDebounce.DebounceObserver<T, U> parent;
/*     */       final long index;
/*     */       final T value;
/*     */       boolean done;
/* 147 */       final AtomicBoolean once = new AtomicBoolean();
/*     */       
/*     */       DebounceInnerObserver(ObservableDebounce.DebounceObserver<T, U> parent, long index, T value) {
/* 150 */         this.parent = parent;
/* 151 */         this.index = index;
/* 152 */         this.value = value;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(U t) {
/* 157 */         if (this.done) {
/*     */           return;
/*     */         }
/* 160 */         this.done = true;
/* 161 */         dispose();
/* 162 */         emit();
/*     */       }
/*     */       
/*     */       void emit() {
/* 166 */         if (this.once.compareAndSet(false, true)) {
/* 167 */           this.parent.emit(this.index, this.value);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 173 */         if (this.done) {
/* 174 */           RxJavaPlugins.onError(t);
/*     */           return;
/*     */         } 
/* 177 */         this.done = true;
/* 178 */         this.parent.onError(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 183 */         if (this.done) {
/*     */           return;
/*     */         }
/* 186 */         this.done = true;
/* 187 */         emit();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDebounce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */