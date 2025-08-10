/*      */ package io.reactivex.internal.operators.flowable;
/*      */ 
/*      */ import io.reactivex.Flowable;
/*      */ import io.reactivex.FlowableSubscriber;
/*      */ import io.reactivex.Scheduler;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.exceptions.Exceptions;
/*      */ import io.reactivex.flowables.ConnectableFlowable;
/*      */ import io.reactivex.functions.Consumer;
/*      */ import io.reactivex.functions.Function;
/*      */ import io.reactivex.internal.disposables.ResettableConnectable;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.fuseable.HasUpstreamPublisher;
/*      */ import io.reactivex.internal.subscribers.SubscriberResourceWrapper;
/*      */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*      */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*      */ import io.reactivex.internal.util.BackpressureHelper;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.internal.util.NotificationLite;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import io.reactivex.schedulers.Timed;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import org.reactivestreams.Publisher;
/*      */ import org.reactivestreams.Subscriber;
/*      */ import org.reactivestreams.Subscription;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class FlowableReplay<T>
/*      */   extends ConnectableFlowable<T>
/*      */   implements HasUpstreamPublisher<T>, ResettableConnectable
/*      */ {
/*      */   final Flowable<T> source;
/*      */   final AtomicReference<ReplaySubscriber<T>> current;
/*      */   final Callable<? extends ReplayBuffer<T>> bufferFactory;
/*      */   final Publisher<T> onSubscribe;
/*   47 */   static final Callable DEFAULT_UNBOUNDED_FACTORY = new DefaultUnboundedFactory();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <U, R> Flowable<R> multicastSelector(Callable<? extends ConnectableFlowable<U>> connectableFactory, Function<? super Flowable<U>, ? extends Publisher<R>> selector) {
/*   61 */     return new MulticastFlowable<R, U>(connectableFactory, selector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableFlowable<T> observeOn(ConnectableFlowable<T> cf, Scheduler scheduler) {
/*   73 */     Flowable<T> flowable = cf.observeOn(scheduler);
/*   74 */     return RxJavaPlugins.onAssembly(new ConnectableFlowableReplay<T>(cf, flowable));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableFlowable<T> createFrom(Flowable<? extends T> source) {
/*   85 */     return create((Flowable)source, DEFAULT_UNBOUNDED_FACTORY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableFlowable<T> create(Flowable<T> source, int bufferSize) {
/*   97 */     if (bufferSize == Integer.MAX_VALUE) {
/*   98 */       return createFrom(source);
/*      */     }
/*  100 */     return create(source, new ReplayBufferTask(bufferSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableFlowable<T> create(Flowable<T> source, long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  114 */     return create(source, maxAge, unit, scheduler, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableFlowable<T> create(Flowable<T> source, long maxAge, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/*  129 */     return create(source, new ScheduledReplayBufferTask(bufferSize, maxAge, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> ConnectableFlowable<T> create(Flowable<T> source, Callable<? extends ReplayBuffer<T>> bufferFactory) {
/*  141 */     AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<ReplaySubscriber<T>>();
/*  142 */     Publisher<T> onSubscribe = new ReplayPublisher<T>(curr, bufferFactory);
/*  143 */     return RxJavaPlugins.onAssembly(new FlowableReplay<T>(onSubscribe, source, curr, bufferFactory));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private FlowableReplay(Publisher<T> onSubscribe, Flowable<T> source, AtomicReference<ReplaySubscriber<T>> current, Callable<? extends ReplayBuffer<T>> bufferFactory) {
/*  149 */     this.onSubscribe = onSubscribe;
/*  150 */     this.source = source;
/*  151 */     this.current = current;
/*  152 */     this.bufferFactory = bufferFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public Publisher<T> source() {
/*  157 */     return (Publisher<T>)this.source;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void subscribeActual(Subscriber<? super T> s) {
/*  162 */     this.onSubscribe.subscribe(s);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetIf(Disposable connectionObject) {
/*  168 */     this.current.compareAndSet((ReplaySubscriber<T>)connectionObject, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void connect(Consumer<? super Disposable> connection) {
/*      */     ReplaySubscriber<T> ps;
/*      */     while (true) {
/*  178 */       ps = this.current.get();
/*      */       
/*  180 */       if (ps == null || ps.isDisposed()) {
/*      */         ReplayBuffer<T> buf;
/*      */ 
/*      */         
/*      */         try {
/*  185 */           buf = this.bufferFactory.call();
/*  186 */         } catch (Throwable ex) {
/*  187 */           Exceptions.throwIfFatal(ex);
/*  188 */           throw ExceptionHelper.wrapOrThrow(ex);
/*      */         } 
/*      */ 
/*      */         
/*  192 */         ReplaySubscriber<T> u = new ReplaySubscriber<T>(buf);
/*      */         
/*  194 */         if (!this.current.compareAndSet(ps, u)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/*  199 */         ps = u;
/*      */       } 
/*      */       break;
/*      */     } 
/*  203 */     boolean doConnect = (!ps.shouldConnect.get() && ps.shouldConnect.compareAndSet(false, true));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  220 */       connection.accept(ps);
/*  221 */     } catch (Throwable ex) {
/*  222 */       ReplayBuffer<T> buf; if (doConnect) {
/*  223 */         ps.shouldConnect.compareAndSet(true, false);
/*      */       }
/*  225 */       Exceptions.throwIfFatal((Throwable)buf);
/*  226 */       throw ExceptionHelper.wrapOrThrow(buf);
/*      */     } 
/*  228 */     if (doConnect) {
/*  229 */       this.source.subscribe(ps);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class ReplaySubscriber<T>
/*      */     extends AtomicReference<Subscription>
/*      */     implements FlowableSubscriber<T>, Disposable
/*      */   {
/*      */     private static final long serialVersionUID = 7224554242710036740L;
/*      */     
/*      */     final FlowableReplay.ReplayBuffer<T> buffer;
/*      */     
/*      */     boolean done;
/*      */     
/*  244 */     static final FlowableReplay.InnerSubscription[] EMPTY = new FlowableReplay.InnerSubscription[0];
/*      */     
/*  246 */     static final FlowableReplay.InnerSubscription[] TERMINATED = new FlowableReplay.InnerSubscription[0];
/*      */ 
/*      */     
/*      */     final AtomicReference<FlowableReplay.InnerSubscription<T>[]> subscribers;
/*      */ 
/*      */     
/*      */     final AtomicBoolean shouldConnect;
/*      */ 
/*      */     
/*      */     final AtomicInteger management;
/*      */ 
/*      */     
/*      */     long maxChildRequested;
/*      */ 
/*      */     
/*      */     long maxUpstreamRequested;
/*      */ 
/*      */     
/*      */     ReplaySubscriber(FlowableReplay.ReplayBuffer<T> buffer) {
/*  265 */       this.buffer = buffer;
/*  266 */       this.management = new AtomicInteger();
/*  267 */       this.subscribers = new AtomicReference(EMPTY);
/*  268 */       this.shouldConnect = new AtomicBoolean();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDisposed() {
/*  273 */       return (this.subscribers.get() == TERMINATED);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void dispose() {
/*  279 */       this.subscribers.set(TERMINATED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  285 */       SubscriptionHelper.cancel(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean add(FlowableReplay.InnerSubscription<T> producer) {
/*  296 */       if (producer == null) {
/*  297 */         throw new NullPointerException();
/*      */       }
/*      */ 
/*      */       
/*      */       while (true) {
/*  302 */         FlowableReplay.InnerSubscription[] arrayOfInnerSubscription1 = (FlowableReplay.InnerSubscription[])this.subscribers.get();
/*      */ 
/*      */         
/*  305 */         if (arrayOfInnerSubscription1 == TERMINATED) {
/*  306 */           return false;
/*      */         }
/*      */         
/*  309 */         int len = arrayOfInnerSubscription1.length;
/*  310 */         FlowableReplay.InnerSubscription[] arrayOfInnerSubscription2 = new FlowableReplay.InnerSubscription[len + 1];
/*  311 */         System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, len);
/*  312 */         arrayOfInnerSubscription2[len] = producer;
/*      */         
/*  314 */         if (this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2)) {
/*  315 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void remove(FlowableReplay.InnerSubscription<T> p) {
/*      */       FlowableReplay.InnerSubscription[] arrayOfInnerSubscription1;
/*      */       FlowableReplay.InnerSubscription[] arrayOfInnerSubscription2;
/*      */       do {
/*  331 */         arrayOfInnerSubscription1 = (FlowableReplay.InnerSubscription[])this.subscribers.get();
/*  332 */         int len = arrayOfInnerSubscription1.length;
/*      */         
/*  334 */         if (len == 0) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  339 */         int j = -1;
/*  340 */         for (int i = 0; i < len; i++) {
/*  341 */           if (arrayOfInnerSubscription1[i].equals(p)) {
/*  342 */             j = i;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  347 */         if (j < 0) {
/*      */           return;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  354 */         if (len == 1) {
/*  355 */           arrayOfInnerSubscription2 = EMPTY;
/*      */         } else {
/*      */           
/*  358 */           arrayOfInnerSubscription2 = new FlowableReplay.InnerSubscription[len - 1];
/*      */           
/*  360 */           System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, j);
/*      */           
/*  362 */           System.arraycopy(arrayOfInnerSubscription1, j + 1, arrayOfInnerSubscription2, j, len - j - 1);
/*      */         }
/*      */       
/*  365 */       } while (!this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onSubscribe(Subscription p) {
/*  375 */       if (SubscriptionHelper.setOnce(this, p)) {
/*  376 */         manageRequests();
/*  377 */         for (FlowableReplay.InnerSubscription<T> rp : (FlowableReplay.InnerSubscription[])this.subscribers.get()) {
/*  378 */           this.buffer.replay(rp);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void onNext(T t) {
/*  385 */       if (!this.done) {
/*  386 */         this.buffer.next(t);
/*  387 */         for (FlowableReplay.InnerSubscription<T> rp : (FlowableReplay.InnerSubscription[])this.subscribers.get()) {
/*  388 */           this.buffer.replay(rp);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onError(Throwable e) {
/*  398 */       if (!this.done) {
/*  399 */         this.done = true;
/*  400 */         this.buffer.error(e);
/*  401 */         for (FlowableReplay.InnerSubscription<T> rp : (FlowableReplay.InnerSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/*  402 */           this.buffer.replay(rp);
/*      */         }
/*      */       } else {
/*  405 */         RxJavaPlugins.onError(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onComplete() {
/*  414 */       if (!this.done) {
/*  415 */         this.done = true;
/*  416 */         this.buffer.complete();
/*  417 */         for (FlowableReplay.InnerSubscription<T> rp : (FlowableReplay.InnerSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/*  418 */           this.buffer.replay(rp);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void manageRequests() {
/*  427 */       if (this.management.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*  430 */       int missed = 1;
/*      */       
/*      */       do {
/*  433 */         if (isDisposed()) {
/*      */           return;
/*      */         }
/*      */         
/*  437 */         FlowableReplay.InnerSubscription[] arrayOfInnerSubscription = (FlowableReplay.InnerSubscription[])this.subscribers.get();
/*      */         
/*  439 */         long ri = this.maxChildRequested;
/*  440 */         long maxTotalRequests = ri;
/*      */         
/*  442 */         for (FlowableReplay.InnerSubscription<T> rp : arrayOfInnerSubscription) {
/*  443 */           maxTotalRequests = Math.max(maxTotalRequests, rp.totalRequested.get());
/*      */         }
/*      */         
/*  446 */         long ur = this.maxUpstreamRequested;
/*  447 */         Subscription p = get();
/*      */         
/*  449 */         long diff = maxTotalRequests - ri;
/*  450 */         if (diff != 0L) {
/*  451 */           this.maxChildRequested = maxTotalRequests;
/*  452 */           if (p != null) {
/*  453 */             if (ur != 0L) {
/*  454 */               this.maxUpstreamRequested = 0L;
/*  455 */               p.request(ur + diff);
/*      */             } else {
/*  457 */               p.request(diff);
/*      */             } 
/*      */           } else {
/*      */             
/*  461 */             long u = ur + diff;
/*  462 */             if (u < 0L) {
/*  463 */               u = Long.MAX_VALUE;
/*      */             }
/*  465 */             this.maxUpstreamRequested = u;
/*      */           }
/*      */         
/*      */         }
/*  469 */         else if (ur != 0L && p != null) {
/*  470 */           this.maxUpstreamRequested = 0L;
/*      */           
/*  472 */           p.request(ur);
/*      */         } 
/*      */         
/*  475 */         missed = this.management.addAndGet(-missed);
/*  476 */       } while (missed != 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class InnerSubscription<T>
/*      */     extends AtomicLong
/*      */     implements Subscription, Disposable
/*      */   {
/*      */     private static final long serialVersionUID = -4453897557930727610L;
/*      */ 
/*      */ 
/*      */     
/*      */     final FlowableReplay.ReplaySubscriber<T> parent;
/*      */ 
/*      */ 
/*      */     
/*      */     final Subscriber<? super T> child;
/*      */ 
/*      */ 
/*      */     
/*      */     Object index;
/*      */ 
/*      */ 
/*      */     
/*      */     final AtomicLong totalRequested;
/*      */ 
/*      */     
/*      */     boolean emitting;
/*      */ 
/*      */     
/*      */     boolean missed;
/*      */ 
/*      */     
/*      */     static final long CANCELLED = -9223372036854775808L;
/*      */ 
/*      */ 
/*      */     
/*      */     InnerSubscription(FlowableReplay.ReplaySubscriber<T> parent, Subscriber<? super T> child) {
/*  517 */       this.parent = parent;
/*  518 */       this.child = child;
/*  519 */       this.totalRequested = new AtomicLong();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void request(long n) {
/*  525 */       if (SubscriptionHelper.validate(n))
/*      */       {
/*      */         
/*  528 */         if (BackpressureHelper.addCancel(this, n) != Long.MIN_VALUE) {
/*      */           
/*  530 */           BackpressureHelper.add(this.totalRequested, n);
/*      */ 
/*      */           
/*  533 */           this.parent.manageRequests();
/*      */           
/*  535 */           this.parent.buffer.replay(this);
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long produced(long n) {
/*  546 */       return BackpressureHelper.producedCancel(this, n);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDisposed() {
/*  551 */       return (get() == Long.MIN_VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public void cancel() {
/*  556 */       dispose();
/*      */     }
/*      */ 
/*      */     
/*      */     public void dispose() {
/*  561 */       if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/*      */         
/*  563 */         this.parent.remove(this);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  568 */         this.parent.manageRequests();
/*      */         
/*  570 */         this.index = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <U> U index() {
/*  579 */       return (U)this.index;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface ReplayBuffer<T>
/*      */   {
/*      */     void next(T param1T);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void error(Throwable param1Throwable);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void complete();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void replay(FlowableReplay.InnerSubscription<T> param1InnerSubscription);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class UnboundedReplayBuffer<T>
/*      */     extends ArrayList<Object>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = 7063189396499112664L;
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int size;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     UnboundedReplayBuffer(int capacityHint) {
/*  624 */       super(capacityHint);
/*      */     }
/*      */ 
/*      */     
/*      */     public void next(T value) {
/*  629 */       add(NotificationLite.next(value));
/*  630 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(Throwable e) {
/*  635 */       add(NotificationLite.error(e));
/*  636 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void complete() {
/*  641 */       add(NotificationLite.complete());
/*  642 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void replay(FlowableReplay.InnerSubscription<T> output) {
/*  647 */       synchronized (output) {
/*  648 */         if (output.emitting) {
/*  649 */           output.missed = true;
/*      */           return;
/*      */         } 
/*  652 */         output.emitting = true;
/*      */       } 
/*  654 */       Subscriber<? super T> child = output.child;
/*      */       
/*      */       while (true) {
/*  657 */         if (output.isDisposed()) {
/*      */           return;
/*      */         }
/*  660 */         int sourceIndex = this.size;
/*      */         
/*  662 */         Integer destinationIndexObject = output.<Integer>index();
/*  663 */         int destinationIndex = (destinationIndexObject != null) ? destinationIndexObject.intValue() : 0;
/*      */         
/*  665 */         long r = output.get();
/*  666 */         long r0 = r;
/*  667 */         long e = 0L;
/*      */         
/*  669 */         while (r != 0L && destinationIndex < sourceIndex) {
/*  670 */           Object o = get(destinationIndex);
/*      */           try {
/*  672 */             if (NotificationLite.accept(o, child)) {
/*      */               return;
/*      */             }
/*  675 */           } catch (Throwable err) {
/*  676 */             Exceptions.throwIfFatal(err);
/*  677 */             output.dispose();
/*  678 */             if (!NotificationLite.isError(o) && !NotificationLite.isComplete(o)) {
/*  679 */               child.onError(err);
/*      */             }
/*      */             return;
/*      */           } 
/*  683 */           if (output.isDisposed()) {
/*      */             return;
/*      */           }
/*  686 */           destinationIndex++;
/*  687 */           r--;
/*  688 */           e++;
/*      */         } 
/*  690 */         if (e != 0L) {
/*  691 */           output.index = Integer.valueOf(destinationIndex);
/*  692 */           if (r0 != Long.MAX_VALUE) {
/*  693 */             output.produced(e);
/*      */           }
/*      */         } 
/*      */         
/*  697 */         synchronized (output) {
/*  698 */           if (!output.missed) {
/*  699 */             output.emitting = false;
/*      */             return;
/*      */           } 
/*  702 */           output.missed = false;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class Node
/*      */     extends AtomicReference<Node>
/*      */   {
/*      */     private static final long serialVersionUID = 245354315435971818L;
/*      */     
/*      */     final Object value;
/*      */     final long index;
/*      */     
/*      */     Node(Object value, long index) {
/*  718 */       this.value = value;
/*  719 */       this.index = index;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class BoundedReplayBuffer<T>
/*      */     extends AtomicReference<Node>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = 2346567790059478686L;
/*      */     
/*      */     FlowableReplay.Node tail;
/*      */     
/*      */     int size;
/*      */     
/*      */     long index;
/*      */ 
/*      */     
/*      */     BoundedReplayBuffer() {
/*  739 */       FlowableReplay.Node n = new FlowableReplay.Node(null, 0L);
/*  740 */       this.tail = n;
/*  741 */       set(n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void addLast(FlowableReplay.Node n) {
/*  749 */       this.tail.set(n);
/*  750 */       this.tail = n;
/*  751 */       this.size++;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     final void removeFirst() {
/*  757 */       FlowableReplay.Node head = get();
/*  758 */       FlowableReplay.Node next = head.get();
/*  759 */       if (next == null) {
/*  760 */         throw new IllegalStateException("Empty list!");
/*      */       }
/*  762 */       this.size--;
/*      */ 
/*      */       
/*  765 */       setFirst(next);
/*      */     }
/*      */     final void removeSome(int n) {
/*  768 */       FlowableReplay.Node head = get();
/*  769 */       while (n > 0) {
/*  770 */         head = head.get();
/*  771 */         n--;
/*  772 */         this.size--;
/*      */       } 
/*      */       
/*  775 */       setFirst(head);
/*      */       
/*  777 */       head = get();
/*  778 */       if (head.get() == null) {
/*  779 */         this.tail = head;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void setFirst(FlowableReplay.Node n) {
/*  787 */       set(n);
/*      */     }
/*      */ 
/*      */     
/*      */     public final void next(T value) {
/*  792 */       Object o = enterTransform(NotificationLite.next(value));
/*  793 */       FlowableReplay.Node n = new FlowableReplay.Node(o, ++this.index);
/*  794 */       addLast(n);
/*  795 */       truncate();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void error(Throwable e) {
/*  800 */       Object o = enterTransform(NotificationLite.error(e));
/*  801 */       FlowableReplay.Node n = new FlowableReplay.Node(o, ++this.index);
/*  802 */       addLast(n);
/*  803 */       truncateFinal();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void complete() {
/*  808 */       Object o = enterTransform(NotificationLite.complete());
/*  809 */       FlowableReplay.Node n = new FlowableReplay.Node(o, ++this.index);
/*  810 */       addLast(n);
/*  811 */       truncateFinal();
/*      */     }
/*      */     
/*      */     final void trimHead() {
/*  815 */       FlowableReplay.Node head = get();
/*  816 */       if (head.value != null) {
/*  817 */         FlowableReplay.Node n = new FlowableReplay.Node(null, 0L);
/*  818 */         n.lazySet(head.get());
/*  819 */         set(n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final void replay(FlowableReplay.InnerSubscription<T> output) {
/*  825 */       synchronized (output) {
/*  826 */         if (output.emitting) {
/*  827 */           output.missed = true;
/*      */           return;
/*      */         } 
/*  830 */         output.emitting = true;
/*      */       } 
/*      */       while (true) {
/*  833 */         if (output.isDisposed()) {
/*  834 */           output.index = null;
/*      */           
/*      */           return;
/*      */         } 
/*  838 */         long r = output.get();
/*  839 */         boolean unbounded = (r == Long.MAX_VALUE);
/*  840 */         long e = 0L;
/*      */         
/*  842 */         FlowableReplay.Node node = output.<FlowableReplay.Node>index();
/*  843 */         if (node == null) {
/*  844 */           node = getHead();
/*  845 */           output.index = node;
/*      */           
/*  847 */           BackpressureHelper.add(output.totalRequested, node.index);
/*      */         } 
/*      */         
/*  850 */         while (r != 0L) {
/*  851 */           FlowableReplay.Node v = node.get();
/*  852 */           if (v != null) {
/*  853 */             Object o = leaveTransform(v.value);
/*      */             try {
/*  855 */               if (NotificationLite.accept(o, output.child)) {
/*  856 */                 output.index = null;
/*      */                 return;
/*      */               } 
/*  859 */             } catch (Throwable err) {
/*  860 */               Exceptions.throwIfFatal(err);
/*  861 */               output.index = null;
/*  862 */               output.dispose();
/*  863 */               if (!NotificationLite.isError(o) && !NotificationLite.isComplete(o)) {
/*  864 */                 output.child.onError(err);
/*      */               }
/*      */               return;
/*      */             } 
/*  868 */             e++;
/*  869 */             r--;
/*  870 */             node = v;
/*      */ 
/*      */ 
/*      */             
/*  874 */             if (output.isDisposed()) {
/*  875 */               output.index = null;
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         } 
/*  880 */         if (e != 0L) {
/*  881 */           output.index = node;
/*  882 */           if (!unbounded) {
/*  883 */             output.produced(e);
/*      */           }
/*      */         } 
/*      */         
/*  887 */         synchronized (output) {
/*  888 */           if (!output.missed) {
/*  889 */             output.emitting = false;
/*      */             return;
/*      */           } 
/*  892 */           output.missed = false;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object enterTransform(Object value) {
/*  905 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object leaveTransform(Object value) {
/*  914 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void truncate() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void truncateFinal() {
/*  928 */       trimHead();
/*      */     }
/*      */     final void collect(Collection<? super T> output) {
/*  931 */       FlowableReplay.Node n = getHead();
/*      */       while (true) {
/*  933 */         FlowableReplay.Node next = n.get();
/*  934 */         if (next != null) {
/*  935 */           Object o = next.value;
/*  936 */           Object v = leaveTransform(o);
/*  937 */           if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
/*      */             break;
/*      */           }
/*  940 */           output.add((T)NotificationLite.getValue(v));
/*  941 */           n = next;
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     }
/*      */     boolean hasError() {
/*  948 */       return (this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value)));
/*      */     }
/*      */     boolean hasCompleted() {
/*  951 */       return (this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value)));
/*      */     }
/*      */     
/*      */     FlowableReplay.Node getHead() {
/*  955 */       return get();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class SizeBoundReplayBuffer<T>
/*      */     extends BoundedReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = -5898283885385201806L;
/*      */     
/*      */     final int limit;
/*      */ 
/*      */     
/*      */     SizeBoundReplayBuffer(int limit) {
/*  970 */       this.limit = limit;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void truncate() {
/*  976 */       if (this.size > this.limit) {
/*  977 */         removeFirst();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SizeAndTimeBoundReplayBuffer<T>
/*      */     extends BoundedReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = 3457957419649567404L;
/*      */     
/*      */     final Scheduler scheduler;
/*      */     
/*      */     final long maxAge;
/*      */     
/*      */     final TimeUnit unit;
/*      */     
/*      */     final int limit;
/*      */     
/*      */     SizeAndTimeBoundReplayBuffer(int limit, long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  997 */       this.scheduler = scheduler;
/*  998 */       this.limit = limit;
/*  999 */       this.maxAge = maxAge;
/* 1000 */       this.unit = unit;
/*      */     }
/*      */ 
/*      */     
/*      */     Object enterTransform(Object value) {
/* 1005 */       return new Timed(value, this.scheduler.now(this.unit), this.unit);
/*      */     }
/*      */ 
/*      */     
/*      */     Object leaveTransform(Object value) {
/* 1010 */       return ((Timed)value).value();
/*      */     }
/*      */ 
/*      */     
/*      */     void truncate() {
/* 1015 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1017 */       FlowableReplay.Node prev = get();
/* 1018 */       FlowableReplay.Node next = prev.get();
/*      */       
/* 1020 */       int e = 0;
/*      */       
/* 1022 */       while (next != null) {
/* 1023 */         if (this.size > this.limit && this.size > 1) {
/* 1024 */           e++;
/* 1025 */           this.size--;
/* 1026 */           prev = next;
/* 1027 */           next = next.get(); continue;
/*      */         } 
/* 1029 */         Timed<?> v = (Timed)next.value;
/* 1030 */         if (v.time() <= timeLimit) {
/* 1031 */           e++;
/* 1032 */           this.size--;
/* 1033 */           prev = next;
/* 1034 */           next = next.get();
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1043 */       if (e != 0) {
/* 1044 */         setFirst(prev);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void truncateFinal() {
/* 1050 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1052 */       FlowableReplay.Node prev = get();
/* 1053 */       FlowableReplay.Node next = prev.get();
/*      */       
/* 1055 */       int e = 0;
/*      */       
/* 1057 */       while (next != null && this.size > 1) {
/* 1058 */         Timed<?> v = (Timed)next.value;
/* 1059 */         if (v.time() <= timeLimit) {
/* 1060 */           e++;
/* 1061 */           this.size--;
/* 1062 */           prev = next;
/* 1063 */           next = next.get();
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1071 */       if (e != 0) {
/* 1072 */         setFirst(prev);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     FlowableReplay.Node getHead() {
/* 1078 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/* 1079 */       FlowableReplay.Node prev = get();
/* 1080 */       FlowableReplay.Node next = prev.get();
/*      */       
/* 1082 */       while (next != null) {
/*      */ 
/*      */         
/* 1085 */         Timed<?> v = (Timed)next.value;
/* 1086 */         if (NotificationLite.isComplete(v.value()) || NotificationLite.isError(v.value())) {
/*      */           break;
/*      */         }
/* 1089 */         if (v.time() <= timeLimit) {
/* 1090 */           prev = next;
/* 1091 */           next = next.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1096 */       return prev;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class MulticastFlowable<R, U> extends Flowable<R> {
/*      */     private final Callable<? extends ConnectableFlowable<U>> connectableFactory;
/*      */     private final Function<? super Flowable<U>, ? extends Publisher<R>> selector;
/*      */     
/*      */     MulticastFlowable(Callable<? extends ConnectableFlowable<U>> connectableFactory, Function<? super Flowable<U>, ? extends Publisher<R>> selector) {
/* 1105 */       this.connectableFactory = connectableFactory;
/* 1106 */       this.selector = selector;
/*      */     }
/*      */     
/*      */     protected void subscribeActual(Subscriber<? super R> child) {
/*      */       ConnectableFlowable<U> cf;
/*      */       Publisher<R> observable;
/*      */       try {
/* 1113 */         cf = (ConnectableFlowable<U>)ObjectHelper.requireNonNull(this.connectableFactory.call(), "The connectableFactory returned null");
/* 1114 */       } catch (Throwable e) {
/* 1115 */         Exceptions.throwIfFatal(e);
/* 1116 */         EmptySubscription.error(e, child);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*      */       try {
/* 1122 */         observable = (Publisher<R>)ObjectHelper.requireNonNull(this.selector.apply(cf), "The selector returned a null Publisher");
/* 1123 */       } catch (Throwable e) {
/* 1124 */         Exceptions.throwIfFatal(e);
/* 1125 */         EmptySubscription.error(e, child);
/*      */         
/*      */         return;
/*      */       } 
/* 1129 */       SubscriberResourceWrapper<R> srw = new SubscriberResourceWrapper(child);
/*      */       
/* 1131 */       observable.subscribe((Subscriber)srw);
/*      */       
/* 1133 */       cf.connect(new DisposableConsumer(srw));
/*      */     }
/*      */     
/*      */     final class DisposableConsumer implements Consumer<Disposable> {
/*      */       private final SubscriberResourceWrapper<R> srw;
/*      */       
/*      */       DisposableConsumer(SubscriberResourceWrapper<R> srw) {
/* 1140 */         this.srw = srw;
/*      */       }
/*      */ 
/*      */       
/*      */       public void accept(Disposable r) {
/* 1145 */         this.srw.setResource(r);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ConnectableFlowableReplay<T> extends ConnectableFlowable<T> {
/*      */     private final ConnectableFlowable<T> cf;
/*      */     private final Flowable<T> flowable;
/*      */     
/*      */     ConnectableFlowableReplay(ConnectableFlowable<T> cf, Flowable<T> flowable) {
/* 1155 */       this.cf = cf;
/* 1156 */       this.flowable = flowable;
/*      */     }
/*      */ 
/*      */     
/*      */     public void connect(Consumer<? super Disposable> connection) {
/* 1161 */       this.cf.connect(connection);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void subscribeActual(Subscriber<? super T> s) {
/* 1166 */       this.flowable.subscribe(s);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
/*      */     private final int bufferSize;
/*      */     
/*      */     ReplayBufferTask(int bufferSize) {
/* 1174 */       this.bufferSize = bufferSize;
/*      */     }
/*      */ 
/*      */     
/*      */     public FlowableReplay.ReplayBuffer<T> call() {
/* 1179 */       return new FlowableReplay.SizeBoundReplayBuffer<T>(this.bufferSize);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ScheduledReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
/*      */     private final int bufferSize;
/*      */     private final long maxAge;
/*      */     private final TimeUnit unit;
/*      */     private final Scheduler scheduler;
/*      */     
/*      */     ScheduledReplayBufferTask(int bufferSize, long maxAge, TimeUnit unit, Scheduler scheduler) {
/* 1190 */       this.bufferSize = bufferSize;
/* 1191 */       this.maxAge = maxAge;
/* 1192 */       this.unit = unit;
/* 1193 */       this.scheduler = scheduler;
/*      */     }
/*      */ 
/*      */     
/*      */     public FlowableReplay.ReplayBuffer<T> call() {
/* 1198 */       return new FlowableReplay.SizeAndTimeBoundReplayBuffer<T>(this.bufferSize, this.maxAge, this.unit, this.scheduler);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ReplayPublisher<T> implements Publisher<T> {
/*      */     private final AtomicReference<FlowableReplay.ReplaySubscriber<T>> curr;
/*      */     private final Callable<? extends FlowableReplay.ReplayBuffer<T>> bufferFactory;
/*      */     
/*      */     ReplayPublisher(AtomicReference<FlowableReplay.ReplaySubscriber<T>> curr, Callable<? extends FlowableReplay.ReplayBuffer<T>> bufferFactory) {
/* 1207 */       this.curr = curr;
/* 1208 */       this.bufferFactory = bufferFactory;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void subscribe(Subscriber<? super T> child) {
/*      */       FlowableReplay.ReplaySubscriber<T> r;
/*      */       while (true) {
/* 1217 */         r = this.curr.get();
/*      */         
/* 1219 */         if (r == null) {
/*      */           FlowableReplay.ReplayBuffer<T> buf;
/*      */           
/*      */           try {
/* 1223 */             buf = this.bufferFactory.call();
/* 1224 */           } catch (Throwable ex) {
/* 1225 */             Exceptions.throwIfFatal(ex);
/* 1226 */             EmptySubscription.error(ex, child);
/*      */             
/*      */             return;
/*      */           } 
/* 1230 */           FlowableReplay.ReplaySubscriber<T> u = new FlowableReplay.ReplaySubscriber<T>(buf);
/*      */           
/* 1232 */           if (!this.curr.compareAndSet(null, u)) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1238 */           r = u;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1242 */       FlowableReplay.InnerSubscription<T> inner = new FlowableReplay.InnerSubscription<T>(r, child);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1247 */       child.onSubscribe(inner);
/*      */ 
/*      */ 
/*      */       
/* 1251 */       r.add(inner);
/*      */       
/* 1253 */       if (inner.isDisposed()) {
/* 1254 */         r.remove(inner);
/*      */         
/*      */         return;
/*      */       } 
/* 1258 */       r.manageRequests();
/*      */ 
/*      */       
/* 1261 */       r.buffer.replay(inner);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class DefaultUnboundedFactory
/*      */     implements Callable<Object>
/*      */   {
/*      */     public Object call() {
/* 1271 */       return new FlowableReplay.UnboundedReplayBuffer(16);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableReplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */