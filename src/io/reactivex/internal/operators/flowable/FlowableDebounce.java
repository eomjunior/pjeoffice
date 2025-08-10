/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableDebounce<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, ? extends Publisher<U>> debounceSelector;
/*     */   
/*     */   public FlowableDebounce(Flowable<T> source, Function<? super T, ? extends Publisher<U>> debounceSelector) {
/*  35 */     super(source);
/*  36 */     this.debounceSelector = debounceSelector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  41 */     this.source.subscribe(new DebounceSubscriber<T, U>((Subscriber<? super T>)new SerializedSubscriber(s), this.debounceSelector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceSubscriber<T, U>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 6725975399620862591L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final Function<? super T, ? extends Publisher<U>> debounceSelector;
/*     */     Subscription upstream;
/*  53 */     final AtomicReference<Disposable> debouncer = new AtomicReference<Disposable>();
/*     */     
/*     */     volatile long index;
/*     */     
/*     */     boolean done;
/*     */ 
/*     */     
/*     */     DebounceSubscriber(Subscriber<? super T> actual, Function<? super T, ? extends Publisher<U>> debounceSelector) {
/*  61 */       this.downstream = actual;
/*  62 */       this.debounceSelector = debounceSelector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  67 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  68 */         this.upstream = s;
/*  69 */         this.downstream.onSubscribe(this);
/*  70 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<U> p;
/*  76 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  80 */       long idx = this.index + 1L;
/*  81 */       this.index = idx;
/*     */       
/*  83 */       Disposable d = this.debouncer.get();
/*  84 */       if (d != null) {
/*  85 */         d.dispose();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  91 */         p = (Publisher<U>)ObjectHelper.requireNonNull(this.debounceSelector.apply(t), "The publisher supplied is null");
/*  92 */       } catch (Throwable e) {
/*  93 */         Exceptions.throwIfFatal(e);
/*  94 */         cancel();
/*  95 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  99 */       DebounceInnerSubscriber<T, U> dis = new DebounceInnerSubscriber<T, U>(this, idx, t);
/*     */       
/* 101 */       if (this.debouncer.compareAndSet(d, dis)) {
/* 102 */         p.subscribe((Subscriber)dis);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 108 */       DisposableHelper.dispose(this.debouncer);
/* 109 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       if (this.done) {
/*     */         return;
/*     */       }
/* 117 */       this.done = true;
/* 118 */       Disposable d = this.debouncer.get();
/* 119 */       if (!DisposableHelper.isDisposed(d)) {
/*     */         
/* 121 */         DebounceInnerSubscriber<T, U> dis = (DebounceInnerSubscriber<T, U>)d;
/* 122 */         if (dis != null) {
/* 123 */           dis.emit();
/*     */         }
/* 125 */         DisposableHelper.dispose(this.debouncer);
/* 126 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 132 */       if (SubscriptionHelper.validate(n)) {
/* 133 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 139 */       this.upstream.cancel();
/* 140 */       DisposableHelper.dispose(this.debouncer);
/*     */     }
/*     */     
/*     */     void emit(long idx, T value) {
/* 144 */       if (idx == this.index) {
/* 145 */         long r = get();
/* 146 */         if (r != 0L) {
/* 147 */           this.downstream.onNext(value);
/* 148 */           BackpressureHelper.produced(this, 1L);
/*     */         } else {
/* 150 */           cancel();
/* 151 */           this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class DebounceInnerSubscriber<T, U>
/*     */       extends DisposableSubscriber<U>
/*     */     {
/*     */       final FlowableDebounce.DebounceSubscriber<T, U> parent;
/*     */       final long index;
/*     */       final T value;
/*     */       boolean done;
/* 163 */       final AtomicBoolean once = new AtomicBoolean();
/*     */       
/*     */       DebounceInnerSubscriber(FlowableDebounce.DebounceSubscriber<T, U> parent, long index, T value) {
/* 166 */         this.parent = parent;
/* 167 */         this.index = index;
/* 168 */         this.value = value;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(U t) {
/* 173 */         if (this.done) {
/*     */           return;
/*     */         }
/* 176 */         this.done = true;
/* 177 */         cancel();
/* 178 */         emit();
/*     */       }
/*     */       
/*     */       void emit() {
/* 182 */         if (this.once.compareAndSet(false, true)) {
/* 183 */           this.parent.emit(this.index, this.value);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 189 */         if (this.done) {
/* 190 */           RxJavaPlugins.onError(t);
/*     */           return;
/*     */         } 
/* 193 */         this.done = true;
/* 194 */         this.parent.onError(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 199 */         if (this.done) {
/*     */           return;
/*     */         }
/* 202 */         this.done = true;
/* 203 */         emit();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDebounce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */