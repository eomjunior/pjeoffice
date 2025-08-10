/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableFlatMapCompletable<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableFlatMapCompletable(ObservableSource<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  48 */     this.source.subscribe(new FlatMapCompletableMainObserver<T>(observer, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapCompletableMainObserver<T>
/*     */     extends BasicIntQueueDisposable<T>
/*     */     implements Observer<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8443155186132538303L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     final CompositeDisposable set;
/*     */     Disposable upstream;
/*     */     volatile boolean disposed;
/*     */     
/*     */     FlatMapCompletableMainObserver(Observer<? super T> observer, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  70 */       this.downstream = observer;
/*  71 */       this.mapper = mapper;
/*  72 */       this.delayErrors = delayErrors;
/*  73 */       this.errors = new AtomicThrowable();
/*  74 */       this.set = new CompositeDisposable();
/*  75 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  80 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  81 */         this.upstream = d;
/*     */         
/*  83 */         this.downstream.onSubscribe((Disposable)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*     */       CompletableSource cs;
/*     */       try {
/*  92 */         cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
/*  93 */       } catch (Throwable ex) {
/*  94 */         Exceptions.throwIfFatal(ex);
/*  95 */         this.upstream.dispose();
/*  96 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 100 */       getAndIncrement();
/*     */       
/* 102 */       InnerObserver inner = new InnerObserver();
/*     */       
/* 104 */       if (!this.disposed && this.set.add(inner)) {
/* 105 */         cs.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 111 */       if (this.errors.addThrowable(e)) {
/* 112 */         if (this.delayErrors) {
/* 113 */           if (decrementAndGet() == 0) {
/* 114 */             Throwable ex = this.errors.terminate();
/* 115 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } else {
/* 118 */           dispose();
/* 119 */           if (getAndSet(0) > 0) {
/* 120 */             Throwable ex = this.errors.terminate();
/* 121 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 125 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 131 */       if (decrementAndGet() == 0) {
/* 132 */         Throwable ex = this.errors.terminate();
/* 133 */         if (ex != null) {
/* 134 */           this.downstream.onError(ex);
/*     */         } else {
/* 136 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 143 */       this.disposed = true;
/* 144 */       this.upstream.dispose();
/* 145 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 150 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 156 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 161 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 171 */       return mode & 0x2;
/*     */     }
/*     */     
/*     */     void innerComplete(InnerObserver inner) {
/* 175 */       this.set.delete(inner);
/* 176 */       onComplete();
/*     */     }
/*     */     
/*     */     void innerError(InnerObserver inner, Throwable e) {
/* 180 */       this.set.delete(inner);
/* 181 */       onError(e);
/*     */     }
/*     */     
/*     */     final class InnerObserver
/*     */       extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
/*     */       private static final long serialVersionUID = 8606673141535671828L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 189 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 194 */         ObservableFlatMapCompletable.FlatMapCompletableMainObserver.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 199 */         ObservableFlatMapCompletable.FlatMapCompletableMainObserver.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 204 */         DisposableHelper.dispose(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 209 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */