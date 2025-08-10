/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
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
/*     */ public final class ObservableSampleWithObservable<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final ObservableSource<?> other;
/*     */   final boolean emitLast;
/*     */   
/*     */   public ObservableSampleWithObservable(ObservableSource<T> source, ObservableSource<?> other, boolean emitLast) {
/*  30 */     super(source);
/*  31 */     this.other = other;
/*  32 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  37 */     SerializedObserver<T> serial = new SerializedObserver(t);
/*  38 */     if (this.emitLast) {
/*  39 */       this.source.subscribe(new SampleMainEmitLast<T>((Observer<? super T>)serial, this.other));
/*     */     } else {
/*  41 */       this.source.subscribe(new SampleMainNoLast<T>((Observer<? super T>)serial, this.other));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class SampleMainObserver<T>
/*     */     extends AtomicReference<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -3517602651313910099L;
/*     */     final Observer<? super T> downstream;
/*     */     final ObservableSource<?> sampler;
/*  53 */     final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     SampleMainObserver(Observer<? super T> actual, ObservableSource<?> other) {
/*  58 */       this.downstream = actual;
/*  59 */       this.sampler = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  64 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  65 */         this.upstream = d;
/*  66 */         this.downstream.onSubscribe(this);
/*  67 */         if (this.other.get() == null) {
/*  68 */           this.sampler.subscribe(new ObservableSampleWithObservable.SamplerObserver<T>(this));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  75 */       lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  80 */       DisposableHelper.dispose(this.other);
/*  81 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  86 */       DisposableHelper.dispose(this.other);
/*  87 */       completion();
/*     */     }
/*     */     
/*     */     boolean setOther(Disposable o) {
/*  91 */       return DisposableHelper.setOnce(this.other, o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  96 */       DisposableHelper.dispose(this.other);
/*  97 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 102 */       return (this.other.get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */     
/*     */     public void error(Throwable e) {
/* 106 */       this.upstream.dispose();
/* 107 */       this.downstream.onError(e);
/*     */     }
/*     */     
/*     */     public void complete() {
/* 111 */       this.upstream.dispose();
/* 112 */       completion();
/*     */     }
/*     */     
/*     */     void emit() {
/* 116 */       T value = getAndSet(null);
/* 117 */       if (value != null)
/* 118 */         this.downstream.onNext(value); 
/*     */     }
/*     */     
/*     */     abstract void completion();
/*     */     
/*     */     abstract void run();
/*     */   }
/*     */   
/*     */   static final class SamplerObserver<T> implements Observer<Object> {
/*     */     final ObservableSampleWithObservable.SampleMainObserver<T> parent;
/*     */     
/*     */     SamplerObserver(ObservableSampleWithObservable.SampleMainObserver<T> parent) {
/* 130 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 136 */       this.parent.setOther(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 141 */       this.parent.run();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 146 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 151 */       this.parent.complete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SampleMainNoLast<T>
/*     */     extends SampleMainObserver<T> {
/*     */     private static final long serialVersionUID = -3029755663834015785L;
/*     */     
/*     */     SampleMainNoLast(Observer<? super T> actual, ObservableSource<?> other) {
/* 160 */       super(actual, other);
/*     */     }
/*     */ 
/*     */     
/*     */     void completion() {
/* 165 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void run() {
/* 170 */       emit();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SampleMainEmitLast<T>
/*     */     extends SampleMainObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3029755663834015785L;
/*     */     final AtomicInteger wip;
/*     */     volatile boolean done;
/*     */     
/*     */     SampleMainEmitLast(Observer<? super T> actual, ObservableSource<?> other) {
/* 183 */       super(actual, other);
/* 184 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     void completion() {
/* 189 */       this.done = true;
/* 190 */       if (this.wip.getAndIncrement() == 0) {
/* 191 */         emit();
/* 192 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void run() {
/* 198 */       if (this.wip.getAndIncrement() == 0)
/*     */         do {
/* 200 */           boolean d = this.done;
/* 201 */           emit();
/* 202 */           if (d) {
/* 203 */             this.downstream.onComplete();
/*     */             return;
/*     */           } 
/* 206 */         } while (this.wip.decrementAndGet() != 0); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSampleWithObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */