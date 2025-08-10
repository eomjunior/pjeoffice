/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscribers.QueueDrainSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.QueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableBufferExactBoundary<T, U extends Collection<? super T>, B>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Publisher<B> boundary;
/*     */   final Callable<U> bufferSupplier;
/*     */   
/*     */   public FlowableBufferExactBoundary(Flowable<T> source, Publisher<B> boundary, Callable<U> bufferSupplier) {
/*  37 */     super(source);
/*  38 */     this.boundary = boundary;
/*  39 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  44 */     this.source.subscribe(new BufferExactBoundarySubscriber<Object, U, B>((Subscriber<? super U>)new SerializedSubscriber(s), this.bufferSupplier, this.boundary));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactBoundarySubscriber<T, U extends Collection<? super T>, B>
/*     */     extends QueueDrainSubscriber<T, U, U>
/*     */     implements FlowableSubscriber<T>, Subscription, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     
/*     */     final Publisher<B> boundary;
/*     */     
/*     */     Subscription upstream;
/*     */     Disposable other;
/*     */     U buffer;
/*     */     
/*     */     BufferExactBoundarySubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier, Publisher<B> boundary) {
/*  61 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  62 */       this.bufferSupplier = bufferSupplier;
/*  63 */       this.boundary = boundary;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       Collection collection;
/*  68 */       if (!SubscriptionHelper.validate(this.upstream, s)) {
/*     */         return;
/*     */       }
/*  71 */       this.upstream = s;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  76 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/*  77 */       } catch (Throwable e) {
/*  78 */         Exceptions.throwIfFatal(e);
/*  79 */         this.cancelled = true;
/*  80 */         s.cancel();
/*  81 */         EmptySubscription.error(e, this.downstream);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       this.buffer = (U)collection;
/*     */       
/*  87 */       FlowableBufferExactBoundary.BufferBoundarySubscriber<T, U, B> bs = new FlowableBufferExactBoundary.BufferBoundarySubscriber<T, U, B>(this);
/*  88 */       this.other = (Disposable)bs;
/*     */       
/*  90 */       this.downstream.onSubscribe(this);
/*     */       
/*  92 */       if (!this.cancelled) {
/*  93 */         s.request(Long.MAX_VALUE);
/*     */         
/*  95 */         this.boundary.subscribe((Subscriber)bs);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 101 */       synchronized (this) {
/* 102 */         U b = this.buffer;
/* 103 */         if (b == null) {
/*     */           return;
/*     */         }
/* 106 */         b.add(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 112 */       cancel();
/* 113 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 119 */       synchronized (this) {
/* 120 */         b = this.buffer;
/* 121 */         if (b == null) {
/*     */           return;
/*     */         }
/* 124 */         this.buffer = null;
/*     */       } 
/* 126 */       this.queue.offer(b);
/* 127 */       this.done = true;
/* 128 */       if (enter()) {
/* 129 */         QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, this, (QueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 135 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 140 */       if (!this.cancelled) {
/* 141 */         this.cancelled = true;
/* 142 */         this.other.dispose();
/* 143 */         this.upstream.cancel();
/*     */         
/* 145 */         if (enter()) {
/* 146 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void next() {
/*     */       Collection collection;
/*     */       U b;
/*     */       try {
/* 156 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 157 */       } catch (Throwable e) {
/* 158 */         Exceptions.throwIfFatal(e);
/* 159 */         cancel();
/* 160 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 165 */       synchronized (this) {
/* 166 */         b = this.buffer;
/* 167 */         if (b == null) {
/*     */           return;
/*     */         }
/* 170 */         this.buffer = (U)collection;
/*     */       } 
/*     */       
/* 173 */       fastPathEmitMax(b, false, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 178 */       cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 183 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(Subscriber<? super U> a, U v) {
/* 188 */       this.downstream.onNext(v);
/* 189 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, B>
/*     */     extends DisposableSubscriber<B> {
/*     */     final FlowableBufferExactBoundary.BufferExactBoundarySubscriber<T, U, B> parent;
/*     */     
/*     */     BufferBoundarySubscriber(FlowableBufferExactBoundary.BufferExactBoundarySubscriber<T, U, B> parent) {
/* 198 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 203 */       this.parent.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 208 */       this.parent.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 213 */       this.parent.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBufferExactBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */