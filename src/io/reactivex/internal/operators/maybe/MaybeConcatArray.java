/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public final class MaybeConcatArray<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final MaybeSource<? extends T>[] sources;
/*     */   
/*     */   public MaybeConcatArray(MaybeSource<? extends T>[] sources) {
/*  36 */     this.sources = sources;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  41 */     ConcatMaybeObserver<T> parent = new ConcatMaybeObserver<T>(s, this.sources);
/*  42 */     s.onSubscribe(parent);
/*  43 */     parent.drain();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatMaybeObserver<T>
/*     */     extends AtomicInteger
/*     */     implements MaybeObserver<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 3520831347801429610L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicReference<Object> current;
/*     */     
/*     */     final SequentialDisposable disposables;
/*     */     
/*     */     final MaybeSource<? extends T>[] sources;
/*     */     
/*     */     int index;
/*     */     long produced;
/*     */     
/*     */     ConcatMaybeObserver(Subscriber<? super T> actual, MaybeSource<? extends T>[] sources) {
/*  67 */       this.downstream = actual;
/*  68 */       this.sources = sources;
/*  69 */       this.requested = new AtomicLong();
/*  70 */       this.disposables = new SequentialDisposable();
/*  71 */       this.current = new AtomicReference(NotificationLite.COMPLETE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  76 */       if (SubscriptionHelper.validate(n)) {
/*  77 */         BackpressureHelper.add(this.requested, n);
/*  78 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  84 */       this.disposables.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  89 */       this.disposables.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  94 */       this.current.lazySet(value);
/*  95 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 100 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 105 */       this.current.lazySet(NotificationLite.COMPLETE);
/* 106 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 111 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 115 */       AtomicReference<Object> c = this.current;
/* 116 */       Subscriber<? super T> a = this.downstream;
/* 117 */       SequentialDisposable sequentialDisposable = this.disposables;
/*     */       do {
/*     */         boolean goNextSource;
/* 120 */         if (sequentialDisposable.isDisposed()) {
/* 121 */           c.lazySet(null);
/*     */           
/*     */           return;
/*     */         } 
/* 125 */         Object o = c.get();
/*     */         
/* 127 */         if (o == null)
/*     */           continue; 
/* 129 */         if (o != NotificationLite.COMPLETE) {
/* 130 */           long p = this.produced;
/* 131 */           if (p != this.requested.get()) {
/* 132 */             this.produced = p + 1L;
/* 133 */             c.lazySet(null);
/* 134 */             goNextSource = true;
/*     */             
/* 136 */             a.onNext(o);
/*     */           } else {
/* 138 */             goNextSource = false;
/*     */           } 
/*     */         } else {
/* 141 */           goNextSource = true;
/* 142 */           c.lazySet(null);
/*     */         } 
/*     */         
/* 145 */         if (!goNextSource || sequentialDisposable.isDisposed())
/* 146 */           continue;  int i = this.index;
/* 147 */         if (i == this.sources.length) {
/* 148 */           a.onComplete();
/*     */           return;
/*     */         } 
/* 151 */         this.index = i + 1;
/*     */         
/* 153 */         this.sources[i].subscribe(this);
/*     */ 
/*     */       
/*     */       }
/* 157 */       while (decrementAndGet() != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeConcatArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */