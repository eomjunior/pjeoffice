/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import java.util.Iterator;
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
/*     */ public final class MaybeConcatIterable<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Iterable<? extends MaybeSource<? extends T>> sources;
/*     */   
/*     */   public MaybeConcatIterable(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  39 */     this.sources = sources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*     */     Iterator<? extends MaybeSource<? extends T>> it;
/*     */     try {
/*  48 */       it = (Iterator<? extends MaybeSource<? extends T>>)ObjectHelper.requireNonNull(this.sources.iterator(), "The sources Iterable returned a null Iterator");
/*  49 */     } catch (Throwable ex) {
/*  50 */       Exceptions.throwIfFatal(ex);
/*  51 */       EmptySubscription.error(ex, s);
/*     */       
/*     */       return;
/*     */     } 
/*  55 */     ConcatMaybeObserver<T> parent = new ConcatMaybeObserver<T>(s, it);
/*  56 */     s.onSubscribe(parent);
/*  57 */     parent.drain();
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
/*     */     final Iterator<? extends MaybeSource<? extends T>> sources;
/*     */     long produced;
/*     */     
/*     */     ConcatMaybeObserver(Subscriber<? super T> actual, Iterator<? extends MaybeSource<? extends T>> sources) {
/*  79 */       this.downstream = actual;
/*  80 */       this.sources = sources;
/*  81 */       this.requested = new AtomicLong();
/*  82 */       this.disposables = new SequentialDisposable();
/*  83 */       this.current = new AtomicReference(NotificationLite.COMPLETE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  88 */       if (SubscriptionHelper.validate(n)) {
/*  89 */         BackpressureHelper.add(this.requested, n);
/*  90 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  96 */       this.disposables.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 101 */       this.disposables.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 106 */       this.current.lazySet(value);
/* 107 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 112 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 117 */       this.current.lazySet(NotificationLite.COMPLETE);
/* 118 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 123 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 127 */       AtomicReference<Object> c = this.current;
/* 128 */       Subscriber<? super T> a = this.downstream;
/* 129 */       SequentialDisposable sequentialDisposable = this.disposables;
/*     */       do {
/*     */         boolean goNextSource, b;
/* 132 */         if (sequentialDisposable.isDisposed()) {
/* 133 */           c.lazySet(null);
/*     */           
/*     */           return;
/*     */         } 
/* 137 */         Object o = c.get();
/*     */         
/* 139 */         if (o == null)
/*     */           continue; 
/* 141 */         if (o != NotificationLite.COMPLETE) {
/* 142 */           long p = this.produced;
/* 143 */           if (p != this.requested.get()) {
/* 144 */             this.produced = p + 1L;
/* 145 */             c.lazySet(null);
/* 146 */             goNextSource = true;
/*     */             
/* 148 */             a.onNext(o);
/*     */           } else {
/* 150 */             goNextSource = false;
/*     */           } 
/*     */         } else {
/* 153 */           goNextSource = true;
/* 154 */           c.lazySet(null);
/*     */         } 
/*     */         
/* 157 */         if (!goNextSource || sequentialDisposable.isDisposed()) {
/*     */           continue;
/*     */         }
/*     */         try {
/* 161 */           b = this.sources.hasNext();
/* 162 */         } catch (Throwable ex) {
/* 163 */           Exceptions.throwIfFatal(ex);
/* 164 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 168 */         if (b) {
/*     */           MaybeSource<? extends T> source;
/*     */           
/*     */           try {
/* 172 */             source = (MaybeSource<? extends T>)ObjectHelper.requireNonNull(this.sources.next(), "The source Iterator returned a null MaybeSource");
/* 173 */           } catch (Throwable ex) {
/* 174 */             Exceptions.throwIfFatal(ex);
/* 175 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 179 */           source.subscribe(this);
/*     */         } else {
/* 181 */           a.onComplete();
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 186 */       while (decrementAndGet() != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeConcatIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */