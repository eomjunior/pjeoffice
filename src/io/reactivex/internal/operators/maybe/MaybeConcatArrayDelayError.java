/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class MaybeConcatArrayDelayError<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final MaybeSource<? extends T>[] sources;
/*     */   
/*     */   public MaybeConcatArrayDelayError(MaybeSource<? extends T>[] sources) {
/*  38 */     this.sources = sources;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  43 */     ConcatMaybeObserver<T> parent = new ConcatMaybeObserver<T>(s, this.sources);
/*  44 */     s.onSubscribe(parent);
/*  45 */     parent.drain();
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
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     int index;
/*     */     long produced;
/*     */     
/*     */     ConcatMaybeObserver(Subscriber<? super T> actual, MaybeSource<? extends T>[] sources) {
/*  71 */       this.downstream = actual;
/*  72 */       this.sources = sources;
/*  73 */       this.requested = new AtomicLong();
/*  74 */       this.disposables = new SequentialDisposable();
/*  75 */       this.current = new AtomicReference(NotificationLite.COMPLETE);
/*  76 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  81 */       if (SubscriptionHelper.validate(n)) {
/*  82 */         BackpressureHelper.add(this.requested, n);
/*  83 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  89 */       this.disposables.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       this.disposables.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  99 */       this.current.lazySet(value);
/* 100 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 105 */       this.current.lazySet(NotificationLite.COMPLETE);
/* 106 */       if (this.errors.addThrowable(e)) {
/* 107 */         drain();
/*     */       } else {
/* 109 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 115 */       this.current.lazySet(NotificationLite.COMPLETE);
/* 116 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 121 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 125 */       AtomicReference<Object> c = this.current;
/* 126 */       Subscriber<? super T> a = this.downstream;
/* 127 */       SequentialDisposable sequentialDisposable = this.disposables;
/*     */       do {
/*     */         boolean goNextSource;
/* 130 */         if (sequentialDisposable.isDisposed()) {
/* 131 */           c.lazySet(null);
/*     */           
/*     */           return;
/*     */         } 
/* 135 */         Object o = c.get();
/*     */         
/* 137 */         if (o == null)
/*     */           continue; 
/* 139 */         if (o != NotificationLite.COMPLETE) {
/* 140 */           long p = this.produced;
/* 141 */           if (p != this.requested.get()) {
/* 142 */             this.produced = p + 1L;
/* 143 */             c.lazySet(null);
/* 144 */             goNextSource = true;
/*     */             
/* 146 */             a.onNext(o);
/*     */           } else {
/* 148 */             goNextSource = false;
/*     */           } 
/*     */         } else {
/* 151 */           goNextSource = true;
/* 152 */           c.lazySet(null);
/*     */         } 
/*     */         
/* 155 */         if (!goNextSource || sequentialDisposable.isDisposed())
/* 156 */           continue;  int i = this.index;
/* 157 */         if (i == this.sources.length) {
/* 158 */           Throwable ex = (Throwable)this.errors.get();
/* 159 */           if (ex != null) {
/* 160 */             a.onError(this.errors.terminate());
/*     */           } else {
/* 162 */             a.onComplete();
/*     */           } 
/*     */           return;
/*     */         } 
/* 166 */         this.index = i + 1;
/*     */         
/* 168 */         this.sources[i].subscribe(this);
/*     */ 
/*     */       
/*     */       }
/* 172 */       while (decrementAndGet() != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeConcatArrayDelayError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */