/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscribers.QueueDrainSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.QueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
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
/*     */ 
/*     */ public final class FlowableBufferBoundarySupplier<T, U extends Collection<? super T>, B>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<? extends Publisher<B>> boundarySupplier;
/*     */   final Callable<U> bufferSupplier;
/*     */   
/*     */   public FlowableBufferBoundarySupplier(Flowable<T> source, Callable<? extends Publisher<B>> boundarySupplier, Callable<U> bufferSupplier) {
/*  40 */     super(source);
/*  41 */     this.boundarySupplier = boundarySupplier;
/*  42 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  47 */     this.source.subscribe(new BufferBoundarySupplierSubscriber<Object, U, B>((Subscriber<? super U>)new SerializedSubscriber(s), this.bufferSupplier, this.boundarySupplier));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferBoundarySupplierSubscriber<T, U extends Collection<? super T>, B>
/*     */     extends QueueDrainSubscriber<T, U, U>
/*     */     implements FlowableSubscriber<T>, Subscription, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     final Callable<? extends Publisher<B>> boundarySupplier;
/*     */     Subscription upstream;
/*  58 */     final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
/*     */     
/*     */     U buffer;
/*     */ 
/*     */     
/*     */     BufferBoundarySupplierSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier, Callable<? extends Publisher<B>> boundarySupplier) {
/*  64 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  65 */       this.bufferSupplier = bufferSupplier;
/*  66 */       this.boundarySupplier = boundarySupplier;
/*     */     }
/*     */     public void onSubscribe(Subscription s) {
/*     */       Collection collection;
/*     */       Publisher<B> boundary;
/*  71 */       if (!SubscriptionHelper.validate(this.upstream, s)) {
/*     */         return;
/*     */       }
/*  74 */       this.upstream = s;
/*     */       
/*  76 */       Subscriber<? super U> actual = this.downstream;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  81 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/*  82 */       } catch (Throwable e) {
/*  83 */         Exceptions.throwIfFatal(e);
/*  84 */         this.cancelled = true;
/*  85 */         s.cancel();
/*  86 */         EmptySubscription.error(e, actual);
/*     */         
/*     */         return;
/*     */       } 
/*  90 */       this.buffer = (U)collection;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  95 */         boundary = (Publisher<B>)ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary publisher supplied is null");
/*  96 */       } catch (Throwable ex) {
/*  97 */         Exceptions.throwIfFatal(ex);
/*  98 */         this.cancelled = true;
/*  99 */         s.cancel();
/* 100 */         EmptySubscription.error(ex, actual);
/*     */         
/*     */         return;
/*     */       } 
/* 104 */       FlowableBufferBoundarySupplier.BufferBoundarySubscriber<T, U, B> bs = new FlowableBufferBoundarySupplier.BufferBoundarySubscriber<T, U, B>(this);
/* 105 */       this.other.set(bs);
/*     */       
/* 107 */       actual.onSubscribe(this);
/*     */       
/* 109 */       if (!this.cancelled) {
/* 110 */         s.request(Long.MAX_VALUE);
/*     */         
/* 112 */         boundary.subscribe((Subscriber)bs);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 118 */       synchronized (this) {
/* 119 */         U b = this.buffer;
/* 120 */         if (b == null) {
/*     */           return;
/*     */         }
/* 123 */         b.add(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 129 */       cancel();
/* 130 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 136 */       synchronized (this) {
/* 137 */         b = this.buffer;
/* 138 */         if (b == null) {
/*     */           return;
/*     */         }
/* 141 */         this.buffer = null;
/*     */       } 
/* 143 */       this.queue.offer(b);
/* 144 */       this.done = true;
/* 145 */       if (enter()) {
/* 146 */         QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, this, (QueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 152 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 157 */       if (!this.cancelled) {
/* 158 */         this.cancelled = true;
/* 159 */         this.upstream.cancel();
/* 160 */         disposeOther();
/*     */         
/* 162 */         if (enter()) {
/* 163 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void disposeOther() {
/* 169 */       DisposableHelper.dispose(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     void next() {
/*     */       Collection collection;
/*     */       Publisher<B> boundary;
/*     */       try {
/* 177 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 178 */       } catch (Throwable e) {
/* 179 */         Exceptions.throwIfFatal(e);
/* 180 */         cancel();
/* 181 */         this.downstream.onError(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 188 */         boundary = (Publisher<B>)ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary publisher supplied is null");
/* 189 */       } catch (Throwable ex) {
/* 190 */         Exceptions.throwIfFatal(ex);
/* 191 */         this.cancelled = true;
/* 192 */         this.upstream.cancel();
/* 193 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 197 */       FlowableBufferBoundarySupplier.BufferBoundarySubscriber<T, U, B> bs = new FlowableBufferBoundarySupplier.BufferBoundarySubscriber<T, U, B>(this);
/*     */       
/* 199 */       if (DisposableHelper.replace(this.other, (Disposable)bs)) {
/*     */         U b;
/* 201 */         synchronized (this) {
/* 202 */           b = this.buffer;
/* 203 */           if (b == null) {
/*     */             return;
/*     */           }
/* 206 */           this.buffer = (U)collection;
/*     */         } 
/*     */         
/* 209 */         boundary.subscribe((Subscriber)bs);
/*     */         
/* 211 */         fastPathEmitMax(b, false, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 217 */       this.upstream.cancel();
/* 218 */       disposeOther();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 223 */       return (this.other.get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(Subscriber<? super U> a, U v) {
/* 228 */       this.downstream.onNext(v);
/* 229 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, B>
/*     */     extends DisposableSubscriber<B>
/*     */   {
/*     */     final FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber<T, U, B> parent;
/*     */     boolean once;
/*     */     
/*     */     BufferBoundarySubscriber(FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber<T, U, B> parent) {
/* 240 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 245 */       if (this.once) {
/*     */         return;
/*     */       }
/* 248 */       this.once = true;
/* 249 */       cancel();
/* 250 */       this.parent.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 255 */       if (this.once) {
/* 256 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 259 */       this.once = true;
/* 260 */       this.parent.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 265 */       if (this.once) {
/*     */         return;
/*     */       }
/* 268 */       this.once = true;
/* 269 */       this.parent.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBufferBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */