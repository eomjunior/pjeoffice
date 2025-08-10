/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ public final class ObservableDoFinally<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Action onFinally;
/*     */   
/*     */   public ObservableDoFinally(ObservableSource<T> source, Action onFinally) {
/*  37 */     super(source);
/*  38 */     this.onFinally = onFinally;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  43 */     this.source.subscribe(new DoFinallyObserver<T>(observer, this.onFinally));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoFinallyObserver<T>
/*     */     extends BasicIntQueueDisposable<T>
/*     */     implements Observer<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4109457741734051389L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final Action onFinally;
/*     */     Disposable upstream;
/*     */     QueueDisposable<T> qd;
/*     */     boolean syncFused;
/*     */     
/*     */     DoFinallyObserver(Observer<? super T> actual, Action onFinally) {
/*  61 */       this.downstream = actual;
/*  62 */       this.onFinally = onFinally;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         this.upstream = d;
/*  70 */         if (d instanceof QueueDisposable) {
/*  71 */           this.qd = (QueueDisposable<T>)d;
/*     */         }
/*     */         
/*  74 */         this.downstream.onSubscribe((Disposable)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  80 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  85 */       this.downstream.onError(t);
/*  86 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       this.downstream.onComplete();
/*  92 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  97 */       this.upstream.dispose();
/*  98 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 103 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 108 */       QueueDisposable<T> qd = this.qd;
/* 109 */       if (qd != null && (mode & 0x4) == 0) {
/* 110 */         int m = qd.requestFusion(mode);
/* 111 */         if (m != 0) {
/* 112 */           this.syncFused = (m == 1);
/*     */         }
/* 114 */         return m;
/*     */       } 
/* 116 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 121 */       this.qd.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 126 */       return this.qd.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 132 */       T v = (T)this.qd.poll();
/* 133 */       if (v == null && this.syncFused) {
/* 134 */         runFinally();
/*     */       }
/* 136 */       return v;
/*     */     }
/*     */     
/*     */     void runFinally() {
/* 140 */       if (compareAndSet(0, 1))
/*     */         try {
/* 142 */           this.onFinally.run();
/* 143 */         } catch (Throwable ex) {
/* 144 */           Exceptions.throwIfFatal(ex);
/* 145 */           RxJavaPlugins.onError(ex);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */