/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class FlowableWithLatestFromMany<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   @Nullable
/*     */   final Publisher<?>[] otherArray;
/*     */   @Nullable
/*     */   final Iterable<? extends Publisher<?>> otherIterable;
/*     */   final Function<? super Object[], R> combiner;
/*     */   
/*     */   public FlowableWithLatestFromMany(@NonNull Flowable<T> source, @NonNull Publisher<?>[] otherArray, Function<? super Object[], R> combiner) {
/*  47 */     super(source);
/*  48 */     this.otherArray = otherArray;
/*  49 */     this.otherIterable = null;
/*  50 */     this.combiner = combiner;
/*     */   }
/*     */   
/*     */   public FlowableWithLatestFromMany(@NonNull Flowable<T> source, @NonNull Iterable<? extends Publisher<?>> otherIterable, @NonNull Function<? super Object[], R> combiner) {
/*  54 */     super(source);
/*  55 */     this.otherArray = null;
/*  56 */     this.otherIterable = otherIterable;
/*  57 */     this.combiner = combiner;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*     */     Publisher[] arrayOfPublisher;
/*  62 */     Publisher<?>[] others = this.otherArray;
/*  63 */     int n = 0;
/*  64 */     if (others == null) {
/*  65 */       arrayOfPublisher = new Publisher[8];
/*     */       
/*     */       try {
/*  68 */         for (Publisher<?> p : this.otherIterable) {
/*  69 */           if (n == arrayOfPublisher.length) {
/*  70 */             arrayOfPublisher = Arrays.<Publisher>copyOf(arrayOfPublisher, n + (n >> 1));
/*     */           }
/*  72 */           arrayOfPublisher[n++] = p;
/*     */         } 
/*  74 */       } catch (Throwable ex) {
/*  75 */         Exceptions.throwIfFatal(ex);
/*  76 */         EmptySubscription.error(ex, s);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } else {
/*  81 */       n = arrayOfPublisher.length;
/*     */     } 
/*     */     
/*  84 */     if (n == 0) {
/*  85 */       (new FlowableMap<Object, R>(this.source, new SingletonArrayFunc())).subscribeActual(s);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     WithLatestFromSubscriber<T, R> parent = new WithLatestFromSubscriber<T, R>(s, this.combiner, n);
/*  90 */     s.onSubscribe(parent);
/*  91 */     parent.subscribe((Publisher<?>[])arrayOfPublisher, n);
/*     */     
/*  93 */     this.source.subscribe((FlowableSubscriber)parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestFromSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 1577321883966341961L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super Object[], R> combiner;
/*     */     
/*     */     final FlowableWithLatestFromMany.WithLatestInnerSubscriber[] subscribers;
/*     */     
/*     */     final AtomicReferenceArray<Object> values;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     volatile boolean done;
/*     */     
/*     */     WithLatestFromSubscriber(Subscriber<? super R> actual, Function<? super Object[], R> combiner, int n) {
/* 119 */       this.downstream = actual;
/* 120 */       this.combiner = combiner;
/* 121 */       FlowableWithLatestFromMany.WithLatestInnerSubscriber[] s = new FlowableWithLatestFromMany.WithLatestInnerSubscriber[n];
/* 122 */       for (int i = 0; i < n; i++) {
/* 123 */         s[i] = new FlowableWithLatestFromMany.WithLatestInnerSubscriber(this, i);
/*     */       }
/* 125 */       this.subscribers = s;
/* 126 */       this.values = new AtomicReferenceArray(n);
/* 127 */       this.upstream = new AtomicReference<Subscription>();
/* 128 */       this.requested = new AtomicLong();
/* 129 */       this.error = new AtomicThrowable();
/*     */     }
/*     */     
/*     */     void subscribe(Publisher<?>[] others, int n) {
/* 133 */       FlowableWithLatestFromMany.WithLatestInnerSubscriber[] subscribers = this.subscribers;
/* 134 */       AtomicReference<Subscription> upstream = this.upstream;
/* 135 */       for (int i = 0; i < n; i++) {
/* 136 */         if (upstream.get() == SubscriptionHelper.CANCELLED) {
/*     */           return;
/*     */         }
/* 139 */         others[i].subscribe((Subscriber)subscribers[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 145 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 150 */       if (!tryOnNext(t) && !this.done) {
/* 151 */         ((Subscription)this.upstream.get()).request(1L);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       R v;
/* 157 */       if (this.done) {
/* 158 */         return false;
/*     */       }
/* 160 */       AtomicReferenceArray<Object> ara = this.values;
/* 161 */       int n = ara.length();
/* 162 */       Object[] objects = new Object[n + 1];
/* 163 */       objects[0] = t;
/*     */       
/* 165 */       for (int i = 0; i < n; i++) {
/* 166 */         Object o = ara.get(i);
/* 167 */         if (o == null)
/*     */         {
/* 169 */           return false;
/*     */         }
/* 171 */         objects[i + 1] = o;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 177 */         v = (R)ObjectHelper.requireNonNull(this.combiner.apply(objects), "The combiner returned a null value");
/* 178 */       } catch (Throwable ex) {
/* 179 */         Exceptions.throwIfFatal(ex);
/* 180 */         cancel();
/* 181 */         onError(ex);
/* 182 */         return false;
/*     */       } 
/*     */       
/* 185 */       HalfSerializer.onNext(this.downstream, v, this, this.error);
/* 186 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 191 */       if (this.done) {
/* 192 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 195 */       this.done = true;
/* 196 */       cancelAllBut(-1);
/* 197 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 202 */       if (!this.done) {
/* 203 */         this.done = true;
/* 204 */         cancelAllBut(-1);
/* 205 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 211 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 216 */       SubscriptionHelper.cancel(this.upstream);
/* 217 */       for (FlowableWithLatestFromMany.WithLatestInnerSubscriber s : this.subscribers) {
/* 218 */         s.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext(int index, Object o) {
/* 223 */       this.values.set(index, o);
/*     */     }
/*     */     
/*     */     void innerError(int index, Throwable t) {
/* 227 */       this.done = true;
/* 228 */       SubscriptionHelper.cancel(this.upstream);
/* 229 */       cancelAllBut(index);
/* 230 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */     
/*     */     void innerComplete(int index, boolean nonEmpty) {
/* 234 */       if (!nonEmpty) {
/* 235 */         this.done = true;
/* 236 */         SubscriptionHelper.cancel(this.upstream);
/* 237 */         cancelAllBut(index);
/* 238 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAllBut(int index) {
/* 243 */       FlowableWithLatestFromMany.WithLatestInnerSubscriber[] subscribers = this.subscribers;
/* 244 */       for (int i = 0; i < subscribers.length; i++) {
/* 245 */         if (i != index) {
/* 246 */           subscribers[i].dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestInnerSubscriber
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 3256684027868224024L;
/*     */     
/*     */     final FlowableWithLatestFromMany.WithLatestFromSubscriber<?, ?> parent;
/*     */     
/*     */     final int index;
/*     */     boolean hasValue;
/*     */     
/*     */     WithLatestInnerSubscriber(FlowableWithLatestFromMany.WithLatestFromSubscriber<?, ?> parent, int index) {
/* 265 */       this.parent = parent;
/* 266 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 271 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 276 */       if (!this.hasValue) {
/* 277 */         this.hasValue = true;
/*     */       }
/* 279 */       this.parent.innerNext(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 284 */       this.parent.innerError(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 289 */       this.parent.innerComplete(this.index, this.hasValue);
/*     */     }
/*     */     
/*     */     void dispose() {
/* 293 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */   }
/*     */   
/*     */   final class SingletonArrayFunc
/*     */     implements Function<T, R> {
/*     */     public R apply(T t) throws Exception {
/* 300 */       return (R)ObjectHelper.requireNonNull(FlowableWithLatestFromMany.this.combiner.apply(new Object[] { t }, ), "The combiner returned a null value");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWithLatestFromMany.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */