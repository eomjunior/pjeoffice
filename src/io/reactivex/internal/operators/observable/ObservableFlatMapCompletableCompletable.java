/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
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
/*     */ 
/*     */ public final class ObservableFlatMapCompletableCompletable<T>
/*     */   extends Completable
/*     */   implements FuseToObservable<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableFlatMapCompletableCompletable(ObservableSource<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  42 */     this.source = source;
/*  43 */     this.mapper = mapper;
/*  44 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  49 */     this.source.subscribe(new FlatMapCompletableMainObserver<T>(observer, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<T> fuseToObservable() {
/*  54 */     return RxJavaPlugins.onAssembly(new ObservableFlatMapCompletable<T>(this.source, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapCompletableMainObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable, Observer<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8443155186132538303L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     final boolean delayErrors;
/*     */     final CompositeDisposable set;
/*     */     Disposable upstream;
/*     */     volatile boolean disposed;
/*     */     
/*     */     FlatMapCompletableMainObserver(CompletableObserver observer, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  75 */       this.downstream = observer;
/*  76 */       this.mapper = mapper;
/*  77 */       this.delayErrors = delayErrors;
/*  78 */       this.errors = new AtomicThrowable();
/*  79 */       this.set = new CompositeDisposable();
/*  80 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  85 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  86 */         this.upstream = d;
/*     */         
/*  88 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*     */       CompletableSource cs;
/*     */       try {
/*  97 */         cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
/*  98 */       } catch (Throwable ex) {
/*  99 */         Exceptions.throwIfFatal(ex);
/* 100 */         this.upstream.dispose();
/* 101 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 105 */       getAndIncrement();
/*     */       
/* 107 */       InnerObserver inner = new InnerObserver();
/*     */       
/* 109 */       if (!this.disposed && this.set.add(inner)) {
/* 110 */         cs.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 116 */       if (this.errors.addThrowable(e)) {
/* 117 */         if (this.delayErrors) {
/* 118 */           if (decrementAndGet() == 0) {
/* 119 */             Throwable ex = this.errors.terminate();
/* 120 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } else {
/* 123 */           dispose();
/* 124 */           if (getAndSet(0) > 0) {
/* 125 */             Throwable ex = this.errors.terminate();
/* 126 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 130 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 136 */       if (decrementAndGet() == 0) {
/* 137 */         Throwable ex = this.errors.terminate();
/* 138 */         if (ex != null) {
/* 139 */           this.downstream.onError(ex);
/*     */         } else {
/* 141 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 148 */       this.disposed = true;
/* 149 */       this.upstream.dispose();
/* 150 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 155 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     void innerComplete(InnerObserver inner) {
/* 159 */       this.set.delete(inner);
/* 160 */       onComplete();
/*     */     }
/*     */     
/*     */     void innerError(InnerObserver inner, Throwable e) {
/* 164 */       this.set.delete(inner);
/* 165 */       onError(e);
/*     */     }
/*     */     
/*     */     final class InnerObserver
/*     */       extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
/*     */       private static final long serialVersionUID = 8606673141535671828L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 173 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 178 */         ObservableFlatMapCompletableCompletable.FlatMapCompletableMainObserver.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 183 */         ObservableFlatMapCompletableCompletable.FlatMapCompletableMainObserver.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 188 */         DisposableHelper.dispose(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 193 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFlatMapCompletableCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */