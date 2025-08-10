/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableGroupJoin<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */   extends AbstractFlowableWithUpstream<TLeft, R>
/*     */ {
/*     */   final Publisher<? extends TRight> other;
/*     */   final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
/*     */   final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
/*     */   final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
/*     */   
/*     */   public FlowableGroupJoin(Flowable<TLeft> source, Publisher<? extends TRight> other, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector) {
/*  52 */     super(source);
/*  53 */     this.other = other;
/*  54 */     this.leftEnd = leftEnd;
/*  55 */     this.rightEnd = rightEnd;
/*  56 */     this.resultSelector = resultSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  62 */     GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R>(s, this.leftEnd, this.rightEnd, this.resultSelector);
/*     */ 
/*     */     
/*  65 */     s.onSubscribe(parent);
/*     */     
/*  67 */     LeftRightSubscriber left = new LeftRightSubscriber(parent, true);
/*  68 */     parent.disposables.add(left);
/*  69 */     LeftRightSubscriber right = new LeftRightSubscriber(parent, false);
/*  70 */     parent.disposables.add(right);
/*     */     
/*  72 */     this.source.subscribe(left);
/*  73 */     this.other.subscribe((Subscriber)right);
/*     */   }
/*     */ 
/*     */   
/*     */   static interface JoinSupport
/*     */   {
/*     */     void innerError(Throwable param1Throwable);
/*     */ 
/*     */     
/*     */     void innerComplete(FlowableGroupJoin.LeftRightSubscriber param1LeftRightSubscriber);
/*     */ 
/*     */     
/*     */     void innerValue(boolean param1Boolean, Object param1Object);
/*     */ 
/*     */     
/*     */     void innerClose(boolean param1Boolean, FlowableGroupJoin.LeftRightEndSubscriber param1LeftRightEndSubscriber);
/*     */ 
/*     */     
/*     */     void innerCloseError(Throwable param1Throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class GroupJoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */     extends AtomicInteger
/*     */     implements Subscription, JoinSupport
/*     */   {
/*     */     private static final long serialVersionUID = -6071216598687999801L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     
/*     */     final CompositeDisposable disposables;
/*     */     
/*     */     final Map<Integer, UnicastProcessor<TRight>> lefts;
/*     */     
/*     */     final Map<Integer, TRight> rights;
/*     */     
/*     */     final AtomicReference<Throwable> error;
/*     */     
/*     */     final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
/*     */     final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
/*     */     final BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector;
/*     */     final AtomicInteger active;
/*     */     int leftIndex;
/*     */     int rightIndex;
/*     */     volatile boolean cancelled;
/* 122 */     static final Integer LEFT_VALUE = Integer.valueOf(1);
/*     */     
/* 124 */     static final Integer RIGHT_VALUE = Integer.valueOf(2);
/*     */     
/* 126 */     static final Integer LEFT_CLOSE = Integer.valueOf(3);
/*     */     
/* 128 */     static final Integer RIGHT_CLOSE = Integer.valueOf(4);
/*     */ 
/*     */ 
/*     */     
/*     */     GroupJoinSubscription(Subscriber<? super R> actual, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super Flowable<TRight>, ? extends R> resultSelector) {
/* 133 */       this.downstream = actual;
/* 134 */       this.requested = new AtomicLong();
/* 135 */       this.disposables = new CompositeDisposable();
/* 136 */       this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
/* 137 */       this.lefts = new LinkedHashMap<Integer, UnicastProcessor<TRight>>();
/* 138 */       this.rights = new LinkedHashMap<Integer, TRight>();
/* 139 */       this.error = new AtomicReference<Throwable>();
/* 140 */       this.leftEnd = leftEnd;
/* 141 */       this.rightEnd = rightEnd;
/* 142 */       this.resultSelector = resultSelector;
/* 143 */       this.active = new AtomicInteger(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 148 */       if (SubscriptionHelper.validate(n)) {
/* 149 */         BackpressureHelper.add(this.requested, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 155 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 158 */       this.cancelled = true;
/* 159 */       cancelAll();
/* 160 */       if (getAndIncrement() == 0) {
/* 161 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 166 */       this.disposables.dispose();
/*     */     }
/*     */     
/*     */     void errorAll(Subscriber<?> a) {
/* 170 */       Throwable ex = ExceptionHelper.terminate(this.error);
/*     */       
/* 172 */       for (UnicastProcessor<TRight> up : this.lefts.values()) {
/* 173 */         up.onError(ex);
/*     */       }
/*     */       
/* 176 */       this.lefts.clear();
/* 177 */       this.rights.clear();
/*     */       
/* 179 */       a.onError(ex);
/*     */     }
/*     */     
/*     */     void fail(Throwable exc, Subscriber<?> a, SimpleQueue<?> q) {
/* 183 */       Exceptions.throwIfFatal(exc);
/* 184 */       ExceptionHelper.addThrowable(this.error, exc);
/* 185 */       q.clear();
/* 186 */       cancelAll();
/* 187 */       errorAll(a);
/*     */     }
/*     */     
/*     */     void drain() {
/* 191 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 195 */       int missed = 1;
/* 196 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 197 */       Subscriber<? super R> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 201 */         if (this.cancelled) {
/* 202 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 206 */         Throwable ex = this.error.get();
/* 207 */         if (ex != null) {
/* 208 */           q.clear();
/* 209 */           cancelAll();
/* 210 */           errorAll(a);
/*     */           
/*     */           return;
/*     */         } 
/* 214 */         boolean d = (this.active.get() == 0);
/*     */         
/* 216 */         Integer mode = (Integer)q.poll();
/*     */         
/* 218 */         boolean empty = (mode == null);
/*     */         
/* 220 */         if (d && empty) {
/* 221 */           for (UnicastProcessor<?> up : this.lefts.values()) {
/* 222 */             up.onComplete();
/*     */           }
/*     */           
/* 225 */           this.lefts.clear();
/* 226 */           this.rights.clear();
/* 227 */           this.disposables.dispose();
/*     */           
/* 229 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 233 */         if (empty)
/*     */         
/*     */         { 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 342 */           missed = addAndGet(-missed);
/* 343 */           if (missed == 0)
/*     */             break;  continue; }  Object val = q.poll(); if (mode == LEFT_VALUE) { Publisher<TLeftEnd> p; R w; TLeft left = (TLeft)val; UnicastProcessor<TRight> up = UnicastProcessor.create(); int idx = this.leftIndex++; this.lefts.put(Integer.valueOf(idx), up); try { p = (Publisher<TLeftEnd>)ObjectHelper.requireNonNull(this.leftEnd.apply(left), "The leftEnd returned a null Publisher"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  FlowableGroupJoin.LeftRightEndSubscriber end = new FlowableGroupJoin.LeftRightEndSubscriber(this, true, idx); this.disposables.add(end); p.subscribe((Subscriber)end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, up), "The resultSelector returned a null value"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  if (this.requested.get() != 0L) { a.onNext(w); BackpressureHelper.produced(this.requested, 1L); } else { fail((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests"), a, (SimpleQueue<?>)q); return; }  for (TRight right : this.rights.values())
/*     */             up.onNext(right);  continue; }  if (mode == RIGHT_VALUE) { Publisher<TRightEnd> p; TRight right = (TRight)val; int idx = this.rightIndex++; this.rights.put(Integer.valueOf(idx), right); try { p = (Publisher<TRightEnd>)ObjectHelper.requireNonNull(this.rightEnd.apply(right), "The rightEnd returned a null Publisher"); } catch (Throwable exc) { Publisher<TLeftEnd> publisher; fail((Throwable)publisher, a, (SimpleQueue<?>)q); return; }  FlowableGroupJoin.LeftRightEndSubscriber end = new FlowableGroupJoin.LeftRightEndSubscriber(this, false, idx); this.disposables.add(end); p.subscribe((Subscriber)end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  for (UnicastProcessor<TRight> up : this.lefts.values())
/*     */             up.onNext(right);  continue; }
/*     */          if (mode == LEFT_CLOSE) { FlowableGroupJoin.LeftRightEndSubscriber end = (FlowableGroupJoin.LeftRightEndSubscriber)val; UnicastProcessor<TRight> up = this.lefts.remove(Integer.valueOf(end.index)); this.disposables.remove(end); if (up != null)
/*     */             up.onComplete();  continue; }
/*     */          if (mode == RIGHT_CLOSE) { FlowableGroupJoin.LeftRightEndSubscriber end = (FlowableGroupJoin.LeftRightEndSubscriber)val; this.rights.remove(Integer.valueOf(end.index)); this.disposables.remove(end); }
/*     */       
/* 351 */       }  } public void innerError(Throwable ex) { if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 352 */         this.active.decrementAndGet();
/* 353 */         drain();
/*     */       } else {
/* 355 */         RxJavaPlugins.onError(ex);
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerComplete(FlowableGroupJoin.LeftRightSubscriber sender) {
/* 361 */       this.disposables.delete(sender);
/* 362 */       this.active.decrementAndGet();
/* 363 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerValue(boolean isLeft, Object o) {
/* 368 */       synchronized (this) {
/* 369 */         this.queue.offer(isLeft ? LEFT_VALUE : RIGHT_VALUE, o);
/*     */       } 
/* 371 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerClose(boolean isLeft, FlowableGroupJoin.LeftRightEndSubscriber index) {
/* 376 */       synchronized (this) {
/* 377 */         this.queue.offer(isLeft ? LEFT_CLOSE : RIGHT_CLOSE, index);
/*     */       } 
/* 379 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerCloseError(Throwable ex) {
/* 384 */       if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 385 */         drain();
/*     */       } else {
/* 387 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LeftRightSubscriber
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1883890389173668373L;
/*     */     
/*     */     final FlowableGroupJoin.JoinSupport parent;
/*     */     final boolean isLeft;
/*     */     
/*     */     LeftRightSubscriber(FlowableGroupJoin.JoinSupport parent, boolean isLeft) {
/* 403 */       this.parent = parent;
/* 404 */       this.isLeft = isLeft;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 409 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 414 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 419 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 424 */       this.parent.innerValue(this.isLeft, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 429 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 434 */       this.parent.innerComplete(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LeftRightEndSubscriber
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1883890389173668373L;
/*     */     
/*     */     final FlowableGroupJoin.JoinSupport parent;
/*     */     
/*     */     final boolean isLeft;
/*     */     
/*     */     final int index;
/*     */ 
/*     */     
/*     */     LeftRightEndSubscriber(FlowableGroupJoin.JoinSupport parent, boolean isLeft, int index) {
/* 453 */       this.parent = parent;
/* 454 */       this.isLeft = isLeft;
/* 455 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 460 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 465 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 470 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 475 */       if (SubscriptionHelper.cancel(this)) {
/* 476 */         this.parent.innerClose(this.isLeft, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 482 */       this.parent.innerCloseError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 487 */       this.parent.innerClose(this.isLeft, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableGroupJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */