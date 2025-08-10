/*      */ package io.reactivex.internal.operators.observable;
/*      */ 
/*      */ import io.reactivex.Observable;
/*      */ import io.reactivex.ObservableSource;
/*      */ import io.reactivex.Observer;
/*      */ import io.reactivex.Scheduler;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.exceptions.Exceptions;
/*      */ import io.reactivex.functions.Consumer;
/*      */ import io.reactivex.functions.Function;
/*      */ import io.reactivex.internal.disposables.DisposableHelper;
/*      */ import io.reactivex.internal.disposables.EmptyDisposable;
/*      */ import io.reactivex.internal.disposables.ResettableConnectable;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.internal.util.NotificationLite;
/*      */ import io.reactivex.observables.ConnectableObservable;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import io.reactivex.schedulers.Timed;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReference;
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
/*      */ public final class ObservableReplay<T>
/*      */   extends ConnectableObservable<T>
/*      */   implements HasUpstreamObservableSource<T>, ResettableConnectable
/*      */ {
/*      */   final ObservableSource<T> source;
/*      */   final AtomicReference<ReplayObserver<T>> current;
/*      */   final BufferSupplier<T> bufferFactory;
/*      */   final ObservableSource<T> onSubscribe;
/*   49 */   static final BufferSupplier DEFAULT_UNBOUNDED_FACTORY = new UnBoundedFactory();
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
/*      */   public static <U, R> Observable<R> multicastSelector(Callable<? extends ConnectableObservable<U>> connectableFactory, Function<? super Observable<U>, ? extends ObservableSource<R>> selector) {
/*   63 */     return RxJavaPlugins.onAssembly(new MulticastReplay<R, U>(connectableFactory, selector));
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
/*      */   public static <T> ConnectableObservable<T> observeOn(ConnectableObservable<T> co, Scheduler scheduler) {
/*   75 */     Observable<T> observable = co.observeOn(scheduler);
/*   76 */     return RxJavaPlugins.onAssembly(new Replay<T>(co, observable));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ConnectableObservable<T> createFrom(ObservableSource<? extends T> source) {
/*   87 */     return create((ObservableSource)source, DEFAULT_UNBOUNDED_FACTORY);
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
/*      */   public static <T> ConnectableObservable<T> create(ObservableSource<T> source, int bufferSize) {
/*   99 */     if (bufferSize == Integer.MAX_VALUE) {
/*  100 */       return createFrom(source);
/*      */     }
/*  102 */     return create(source, new ReplayBufferSupplier<T>(bufferSize));
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
/*      */   public static <T> ConnectableObservable<T> create(ObservableSource<T> source, long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  116 */     return create(source, maxAge, unit, scheduler, 2147483647);
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
/*      */   public static <T> ConnectableObservable<T> create(ObservableSource<T> source, long maxAge, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/*  131 */     return create(source, new ScheduledReplaySupplier<T>(bufferSize, maxAge, unit, scheduler));
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
/*      */   static <T> ConnectableObservable<T> create(ObservableSource<T> source, BufferSupplier<T> bufferFactory) {
/*  143 */     AtomicReference<ReplayObserver<T>> curr = new AtomicReference<ReplayObserver<T>>();
/*  144 */     ObservableSource<T> onSubscribe = new ReplaySource<T>(curr, bufferFactory);
/*  145 */     return RxJavaPlugins.onAssembly(new ObservableReplay<T>(onSubscribe, source, curr, bufferFactory));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ObservableReplay(ObservableSource<T> onSubscribe, ObservableSource<T> source, AtomicReference<ReplayObserver<T>> current, BufferSupplier<T> bufferFactory) {
/*  151 */     this.onSubscribe = onSubscribe;
/*  152 */     this.source = source;
/*  153 */     this.current = current;
/*  154 */     this.bufferFactory = bufferFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public ObservableSource<T> source() {
/*  159 */     return this.source;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetIf(Disposable connectionObject) {
/*  165 */     this.current.compareAndSet((ReplayObserver<T>)connectionObject, null);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void subscribeActual(Observer<? super T> observer) {
/*  170 */     this.onSubscribe.subscribe(observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void connect(Consumer<? super Disposable> connection) {
/*      */     ReplayObserver<T> ps;
/*      */     while (true) {
/*  180 */       ps = this.current.get();
/*      */       
/*  182 */       if (ps == null || ps.isDisposed()) {
/*      */         
/*  184 */         ReplayBuffer<T> buf = this.bufferFactory.call();
/*      */         
/*  186 */         ReplayObserver<T> u = new ReplayObserver<T>(buf);
/*      */         
/*  188 */         if (!this.current.compareAndSet(ps, u)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/*  193 */         ps = u;
/*      */       } 
/*      */       break;
/*      */     } 
/*  197 */     boolean doConnect = (!ps.shouldConnect.get() && ps.shouldConnect.compareAndSet(false, true));
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
/*      */     
/*      */     try {
/*  215 */       connection.accept(ps);
/*  216 */     } catch (Throwable ex) {
/*  217 */       if (doConnect) {
/*  218 */         ps.shouldConnect.compareAndSet(true, false);
/*      */       }
/*  220 */       Exceptions.throwIfFatal(ex);
/*  221 */       throw ExceptionHelper.wrapOrThrow(ex);
/*      */     } 
/*  223 */     if (doConnect)
/*  224 */       this.source.subscribe(ps); 
/*      */   }
/*      */   
/*      */   static interface BufferSupplier<T>
/*      */   {
/*      */     ObservableReplay.ReplayBuffer<T> call();
/*      */   }
/*      */   
/*      */   static final class ReplayObserver<T>
/*      */     extends AtomicReference<Disposable>
/*      */     implements Observer<T>, Disposable
/*      */   {
/*      */     private static final long serialVersionUID = -533785617179540163L;
/*      */     final ObservableReplay.ReplayBuffer<T> buffer;
/*      */     boolean done;
/*  239 */     static final ObservableReplay.InnerDisposable[] EMPTY = new ObservableReplay.InnerDisposable[0];
/*      */     
/*  241 */     static final ObservableReplay.InnerDisposable[] TERMINATED = new ObservableReplay.InnerDisposable[0];
/*      */ 
/*      */     
/*      */     final AtomicReference<ObservableReplay.InnerDisposable[]> observers;
/*      */ 
/*      */     
/*      */     final AtomicBoolean shouldConnect;
/*      */ 
/*      */ 
/*      */     
/*      */     ReplayObserver(ObservableReplay.ReplayBuffer<T> buffer) {
/*  252 */       this.buffer = buffer;
/*      */       
/*  254 */       this.observers = (AtomicReference)new AtomicReference<ObservableReplay.InnerDisposable>(EMPTY);
/*  255 */       this.shouldConnect = new AtomicBoolean();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDisposed() {
/*  260 */       return (this.observers.get() == TERMINATED);
/*      */     }
/*      */ 
/*      */     
/*      */     public void dispose() {
/*  265 */       this.observers.set(TERMINATED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  271 */       DisposableHelper.dispose(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean add(ObservableReplay.InnerDisposable<T> producer) {
/*      */       while (true) {
/*  284 */         ObservableReplay.InnerDisposable[] c = this.observers.get();
/*      */ 
/*      */         
/*  287 */         if (c == TERMINATED) {
/*  288 */           return false;
/*      */         }
/*      */         
/*  291 */         int len = c.length;
/*  292 */         ObservableReplay.InnerDisposable[] u = new ObservableReplay.InnerDisposable[len + 1];
/*  293 */         System.arraycopy(c, 0, u, 0, len);
/*  294 */         u[len] = producer;
/*      */         
/*  296 */         if (this.observers.compareAndSet(c, u)) {
/*  297 */           return true;
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
/*      */     void remove(ObservableReplay.InnerDisposable<T> producer) {
/*      */       ObservableReplay.InnerDisposable[] c;
/*      */       ObservableReplay.InnerDisposable[] u;
/*      */       do {
/*  312 */         c = this.observers.get();
/*      */         
/*  314 */         int len = c.length;
/*      */         
/*  316 */         if (len == 0) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  321 */         int j = -1;
/*  322 */         for (int i = 0; i < len; i++) {
/*  323 */           if (c[i].equals(producer)) {
/*  324 */             j = i;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  329 */         if (j < 0) {
/*      */           return;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  336 */         if (len == 1) {
/*  337 */           u = EMPTY;
/*      */         } else {
/*      */           
/*  340 */           u = new ObservableReplay.InnerDisposable[len - 1];
/*      */           
/*  342 */           System.arraycopy(c, 0, u, 0, j);
/*      */           
/*  344 */           System.arraycopy(c, j + 1, u, j, len - j - 1);
/*      */         }
/*      */       
/*  347 */       } while (!this.observers.compareAndSet(c, u));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onSubscribe(Disposable p) {
/*  357 */       if (DisposableHelper.setOnce(this, p)) {
/*  358 */         replay();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void onNext(T t) {
/*  364 */       if (!this.done) {
/*  365 */         this.buffer.next(t);
/*  366 */         replay();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onError(Throwable e) {
/*  374 */       if (!this.done) {
/*  375 */         this.done = true;
/*  376 */         this.buffer.error(e);
/*  377 */         replayFinal();
/*      */       } else {
/*  379 */         RxJavaPlugins.onError(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onComplete() {
/*  387 */       if (!this.done) {
/*  388 */         this.done = true;
/*  389 */         this.buffer.complete();
/*  390 */         replayFinal();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void replay() {
/*  399 */       ObservableReplay.InnerDisposable[] arrayOfInnerDisposable = this.observers.get();
/*  400 */       for (ObservableReplay.InnerDisposable<T> rp : arrayOfInnerDisposable) {
/*  401 */         this.buffer.replay(rp);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void replayFinal() {
/*  410 */       ObservableReplay.InnerDisposable[] arrayOfInnerDisposable = this.observers.getAndSet(TERMINATED);
/*  411 */       for (ObservableReplay.InnerDisposable<T> rp : arrayOfInnerDisposable) {
/*  412 */         this.buffer.replay(rp);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class InnerDisposable<T>
/*      */     extends AtomicInteger
/*      */     implements Disposable
/*      */   {
/*      */     private static final long serialVersionUID = 2728361546769921047L;
/*      */ 
/*      */     
/*      */     final ObservableReplay.ReplayObserver<T> parent;
/*      */ 
/*      */     
/*      */     final Observer<? super T> child;
/*      */ 
/*      */     
/*      */     Object index;
/*      */ 
/*      */     
/*      */     volatile boolean cancelled;
/*      */ 
/*      */ 
/*      */     
/*      */     InnerDisposable(ObservableReplay.ReplayObserver<T> parent, Observer<? super T> child) {
/*  441 */       this.parent = parent;
/*  442 */       this.child = child;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDisposed() {
/*  447 */       return this.cancelled;
/*      */     }
/*      */ 
/*      */     
/*      */     public void dispose() {
/*  452 */       if (!this.cancelled) {
/*  453 */         this.cancelled = true;
/*      */         
/*  455 */         this.parent.remove(this);
/*      */         
/*  457 */         this.index = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <U> U index() {
/*  466 */       return (U)this.index;
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
/*      */     void replay(ObservableReplay.InnerDisposable<T> param1InnerDisposable);
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
/*  511 */       super(capacityHint);
/*      */     }
/*      */ 
/*      */     
/*      */     public void next(T value) {
/*  516 */       add(NotificationLite.next(value));
/*  517 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(Throwable e) {
/*  522 */       add(NotificationLite.error(e));
/*  523 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void complete() {
/*  528 */       add(NotificationLite.complete());
/*  529 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void replay(ObservableReplay.InnerDisposable<T> output) {
/*  534 */       if (output.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  538 */       Observer<? super T> child = output.child;
/*      */       
/*  540 */       int missed = 1;
/*      */       
/*      */       do {
/*  543 */         if (output.isDisposed()) {
/*      */           return;
/*      */         }
/*  546 */         int sourceIndex = this.size;
/*      */         
/*  548 */         Integer destinationIndexObject = output.<Integer>index();
/*  549 */         int destinationIndex = (destinationIndexObject != null) ? destinationIndexObject.intValue() : 0;
/*      */         
/*  551 */         while (destinationIndex < sourceIndex) {
/*  552 */           Object o = get(destinationIndex);
/*  553 */           if (NotificationLite.accept(o, child)) {
/*      */             return;
/*      */           }
/*  556 */           if (output.isDisposed()) {
/*      */             return;
/*      */           }
/*  559 */           destinationIndex++;
/*      */         } 
/*      */         
/*  562 */         output.index = Integer.valueOf(destinationIndex);
/*  563 */         missed = output.addAndGet(-missed);
/*  564 */       } while (missed != 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class Node
/*      */     extends AtomicReference<Node>
/*      */   {
/*      */     private static final long serialVersionUID = 245354315435971818L;
/*      */     
/*      */     final Object value;
/*      */ 
/*      */     
/*      */     Node(Object value) {
/*  579 */       this.value = value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class BoundedReplayBuffer<T>
/*      */     extends AtomicReference<Node>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = 2346567790059478686L;
/*      */     
/*      */     ObservableReplay.Node tail;
/*      */     
/*      */     int size;
/*      */ 
/*      */     
/*      */     BoundedReplayBuffer() {
/*  597 */       ObservableReplay.Node n = new ObservableReplay.Node(null);
/*  598 */       this.tail = n;
/*  599 */       set(n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void addLast(ObservableReplay.Node n) {
/*  607 */       this.tail.set(n);
/*  608 */       this.tail = n;
/*  609 */       this.size++;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     final void removeFirst() {
/*  615 */       ObservableReplay.Node head = get();
/*  616 */       ObservableReplay.Node next = head.get();
/*  617 */       this.size--;
/*      */ 
/*      */       
/*  620 */       setFirst(next);
/*      */     }
/*      */     
/*      */     final void trimHead() {
/*  624 */       ObservableReplay.Node head = get();
/*  625 */       if (head.value != null) {
/*  626 */         ObservableReplay.Node n = new ObservableReplay.Node(null);
/*  627 */         n.lazySet(head.get());
/*  628 */         set(n);
/*      */       } 
/*      */     }
/*      */     
/*      */     final void removeSome(int n) {
/*  633 */       ObservableReplay.Node head = get();
/*  634 */       while (n > 0) {
/*  635 */         head = head.get();
/*  636 */         n--;
/*  637 */         this.size--;
/*      */       } 
/*      */       
/*  640 */       setFirst(head);
/*      */       
/*  642 */       head = get();
/*  643 */       if (head.get() == null) {
/*  644 */         this.tail = head;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final void setFirst(ObservableReplay.Node n) {
/*  652 */       set(n);
/*      */     }
/*      */ 
/*      */     
/*      */     public final void next(T value) {
/*  657 */       Object o = enterTransform(NotificationLite.next(value));
/*  658 */       ObservableReplay.Node n = new ObservableReplay.Node(o);
/*  659 */       addLast(n);
/*  660 */       truncate();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void error(Throwable e) {
/*  665 */       Object o = enterTransform(NotificationLite.error(e));
/*  666 */       ObservableReplay.Node n = new ObservableReplay.Node(o);
/*  667 */       addLast(n);
/*  668 */       truncateFinal();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void complete() {
/*  673 */       Object o = enterTransform(NotificationLite.complete());
/*  674 */       ObservableReplay.Node n = new ObservableReplay.Node(o);
/*  675 */       addLast(n);
/*  676 */       truncateFinal();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void replay(ObservableReplay.InnerDisposable<T> output) {
/*  681 */       if (output.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  685 */       int missed = 1;
/*      */       
/*      */       do {
/*  688 */         ObservableReplay.Node node = output.<ObservableReplay.Node>index();
/*  689 */         if (node == null) {
/*  690 */           node = getHead();
/*  691 */           output.index = node;
/*      */         } 
/*      */         
/*      */         while (true) {
/*  695 */           if (output.isDisposed()) {
/*  696 */             output.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  700 */           ObservableReplay.Node v = node.get();
/*  701 */           if (v != null) {
/*  702 */             Object o = leaveTransform(v.value);
/*  703 */             if (NotificationLite.accept(o, output.child)) {
/*  704 */               output.index = null;
/*      */               return;
/*      */             } 
/*  707 */             node = v;
/*      */             
/*      */             continue;
/*      */           } 
/*      */           break;
/*      */         } 
/*  713 */         output.index = node;
/*      */         
/*  715 */         missed = output.addAndGet(-missed);
/*  716 */       } while (missed != 0);
/*      */     }
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
/*      */     Object enterTransform(Object value) {
/*  730 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object leaveTransform(Object value) {
/*  739 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract void truncate();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void truncateFinal() {
/*  752 */       trimHead();
/*      */     }
/*      */     final void collect(Collection<? super T> output) {
/*  755 */       ObservableReplay.Node n = getHead();
/*      */       while (true) {
/*  757 */         ObservableReplay.Node next = n.get();
/*  758 */         if (next != null) {
/*  759 */           Object o = next.value;
/*  760 */           Object v = leaveTransform(o);
/*  761 */           if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
/*      */             break;
/*      */           }
/*  764 */           output.add((T)NotificationLite.getValue(v));
/*  765 */           n = next;
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     }
/*      */     boolean hasError() {
/*  772 */       return (this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value)));
/*      */     }
/*      */     boolean hasCompleted() {
/*  775 */       return (this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value)));
/*      */     }
/*      */     
/*      */     ObservableReplay.Node getHead() {
/*  779 */       return get();
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
/*  794 */       this.limit = limit;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void truncate() {
/*  800 */       if (this.size > this.limit) {
/*  801 */         removeFirst();
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
/*  821 */       this.scheduler = scheduler;
/*  822 */       this.limit = limit;
/*  823 */       this.maxAge = maxAge;
/*  824 */       this.unit = unit;
/*      */     }
/*      */ 
/*      */     
/*      */     Object enterTransform(Object value) {
/*  829 */       return new Timed(value, this.scheduler.now(this.unit), this.unit);
/*      */     }
/*      */ 
/*      */     
/*      */     Object leaveTransform(Object value) {
/*  834 */       return ((Timed)value).value();
/*      */     }
/*      */ 
/*      */     
/*      */     void truncate() {
/*  839 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/*  841 */       ObservableReplay.Node prev = get();
/*  842 */       ObservableReplay.Node next = prev.get();
/*      */       
/*  844 */       int e = 0;
/*      */       
/*  846 */       while (next != null) {
/*  847 */         if (this.size > this.limit && this.size > 1) {
/*  848 */           e++;
/*  849 */           this.size--;
/*  850 */           prev = next;
/*  851 */           next = next.get(); continue;
/*      */         } 
/*  853 */         Timed<?> v = (Timed)next.value;
/*  854 */         if (v.time() <= timeLimit) {
/*  855 */           e++;
/*  856 */           this.size--;
/*  857 */           prev = next;
/*  858 */           next = next.get();
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  867 */       if (e != 0) {
/*  868 */         setFirst(prev);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void truncateFinal() {
/*  874 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/*  876 */       ObservableReplay.Node prev = get();
/*  877 */       ObservableReplay.Node next = prev.get();
/*      */       
/*  879 */       int e = 0;
/*      */       
/*  881 */       while (next != null && this.size > 1) {
/*  882 */         Timed<?> v = (Timed)next.value;
/*  883 */         if (v.time() <= timeLimit) {
/*  884 */           e++;
/*  885 */           this.size--;
/*  886 */           prev = next;
/*  887 */           next = next.get();
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  895 */       if (e != 0) {
/*  896 */         setFirst(prev);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     ObservableReplay.Node getHead() {
/*  902 */       long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
/*  903 */       ObservableReplay.Node prev = get();
/*  904 */       ObservableReplay.Node next = prev.get();
/*      */       
/*  906 */       while (next != null) {
/*      */ 
/*      */         
/*  909 */         Timed<?> v = (Timed)next.value;
/*  910 */         if (NotificationLite.isComplete(v.value()) || NotificationLite.isError(v.value())) {
/*      */           break;
/*      */         }
/*  913 */         if (v.time() <= timeLimit) {
/*  914 */           prev = next;
/*  915 */           next = next.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  920 */       return prev;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class UnBoundedFactory
/*      */     implements BufferSupplier<Object> {
/*      */     public ObservableReplay.ReplayBuffer<Object> call() {
/*  927 */       return new ObservableReplay.UnboundedReplayBuffer(16);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class DisposeConsumer<R> implements Consumer<Disposable> {
/*      */     private final ObserverResourceWrapper<R> srw;
/*      */     
/*      */     DisposeConsumer(ObserverResourceWrapper<R> srw) {
/*  935 */       this.srw = srw;
/*      */     }
/*      */ 
/*      */     
/*      */     public void accept(Disposable r) {
/*  940 */       this.srw.setResource(r);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ReplayBufferSupplier<T> implements BufferSupplier<T> {
/*      */     private final int bufferSize;
/*      */     
/*      */     ReplayBufferSupplier(int bufferSize) {
/*  948 */       this.bufferSize = bufferSize;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObservableReplay.ReplayBuffer<T> call() {
/*  953 */       return new ObservableReplay.SizeBoundReplayBuffer<T>(this.bufferSize);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ScheduledReplaySupplier<T> implements BufferSupplier<T> {
/*      */     private final int bufferSize;
/*      */     private final long maxAge;
/*      */     private final TimeUnit unit;
/*      */     private final Scheduler scheduler;
/*      */     
/*      */     ScheduledReplaySupplier(int bufferSize, long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  964 */       this.bufferSize = bufferSize;
/*  965 */       this.maxAge = maxAge;
/*  966 */       this.unit = unit;
/*  967 */       this.scheduler = scheduler;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObservableReplay.ReplayBuffer<T> call() {
/*  972 */       return new ObservableReplay.SizeAndTimeBoundReplayBuffer<T>(this.bufferSize, this.maxAge, this.unit, this.scheduler);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class ReplaySource<T> implements ObservableSource<T> {
/*      */     private final AtomicReference<ObservableReplay.ReplayObserver<T>> curr;
/*      */     private final ObservableReplay.BufferSupplier<T> bufferFactory;
/*      */     
/*      */     ReplaySource(AtomicReference<ObservableReplay.ReplayObserver<T>> curr, ObservableReplay.BufferSupplier<T> bufferFactory) {
/*  981 */       this.curr = curr;
/*  982 */       this.bufferFactory = bufferFactory;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void subscribe(Observer<? super T> child) {
/*      */       ObservableReplay.ReplayObserver<T> r;
/*      */       while (true) {
/*  991 */         r = this.curr.get();
/*      */         
/*  993 */         if (r == null) {
/*      */           
/*  995 */           ObservableReplay.ReplayBuffer<T> buf = this.bufferFactory.call();
/*      */           
/*  997 */           ObservableReplay.ReplayObserver<T> u = new ObservableReplay.ReplayObserver<T>(buf);
/*      */           
/*  999 */           if (!this.curr.compareAndSet(null, u)) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1005 */           r = u;
/*      */         } 
/*      */         break;
/*      */       } 
/* 1009 */       ObservableReplay.InnerDisposable<T> inner = new ObservableReplay.InnerDisposable<T>(r, child);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1014 */       child.onSubscribe(inner);
/*      */ 
/*      */ 
/*      */       
/* 1018 */       r.add(inner);
/*      */       
/* 1020 */       if (inner.isDisposed()) {
/* 1021 */         r.remove(inner);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1026 */       r.buffer.replay(inner);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MulticastReplay<R, U>
/*      */     extends Observable<R>
/*      */   {
/*      */     private final Callable<? extends ConnectableObservable<U>> connectableFactory;
/*      */     private final Function<? super Observable<U>, ? extends ObservableSource<R>> selector;
/*      */     
/*      */     MulticastReplay(Callable<? extends ConnectableObservable<U>> connectableFactory, Function<? super Observable<U>, ? extends ObservableSource<R>> selector) {
/* 1038 */       this.connectableFactory = connectableFactory;
/* 1039 */       this.selector = selector;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void subscribeActual(Observer<? super R> child) {
/*      */       ConnectableObservable<U> co;
/*      */       ObservableSource<R> observable;
/*      */       try {
/* 1047 */         co = (ConnectableObservable<U>)ObjectHelper.requireNonNull(this.connectableFactory.call(), "The connectableFactory returned a null ConnectableObservable");
/* 1048 */         observable = (ObservableSource<R>)ObjectHelper.requireNonNull(this.selector.apply(co), "The selector returned a null ObservableSource");
/* 1049 */       } catch (Throwable e) {
/* 1050 */         Exceptions.throwIfFatal(e);
/* 1051 */         EmptyDisposable.error(e, child);
/*      */         
/*      */         return;
/*      */       } 
/* 1055 */       ObserverResourceWrapper<R> srw = new ObserverResourceWrapper<R>(child);
/*      */       
/* 1057 */       observable.subscribe(srw);
/*      */       
/* 1059 */       co.connect(new ObservableReplay.DisposeConsumer<R>(srw));
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Replay<T> extends ConnectableObservable<T> {
/*      */     private final ConnectableObservable<T> co;
/*      */     private final Observable<T> observable;
/*      */     
/*      */     Replay(ConnectableObservable<T> co, Observable<T> observable) {
/* 1068 */       this.co = co;
/* 1069 */       this.observable = observable;
/*      */     }
/*      */ 
/*      */     
/*      */     public void connect(Consumer<? super Disposable> connection) {
/* 1074 */       this.co.connect(connection);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void subscribeActual(Observer<? super T> observer) {
/* 1079 */       this.observable.subscribe(observer);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableReplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */