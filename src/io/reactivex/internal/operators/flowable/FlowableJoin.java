/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.disposables.CompositeDisposable;
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
/*     */ public final class FlowableJoin<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */   extends AbstractFlowableWithUpstream<TLeft, R>
/*     */ {
/*     */   final Publisher<? extends TRight> other;
/*     */   final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
/*     */   final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
/*     */   final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
/*     */   
/*     */   public FlowableJoin(Flowable<TLeft> source, Publisher<? extends TRight> other, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector) {
/*  49 */     super(source);
/*  50 */     this.other = other;
/*  51 */     this.leftEnd = leftEnd;
/*  52 */     this.rightEnd = rightEnd;
/*  53 */     this.resultSelector = resultSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  59 */     JoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new JoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R>(s, this.leftEnd, this.rightEnd, this.resultSelector);
/*     */ 
/*     */     
/*  62 */     s.onSubscribe(parent);
/*     */     
/*  64 */     FlowableGroupJoin.LeftRightSubscriber left = new FlowableGroupJoin.LeftRightSubscriber(parent, true);
/*  65 */     parent.disposables.add(left);
/*  66 */     FlowableGroupJoin.LeftRightSubscriber right = new FlowableGroupJoin.LeftRightSubscriber(parent, false);
/*  67 */     parent.disposables.add(right);
/*     */     
/*  69 */     this.source.subscribe(left);
/*  70 */     this.other.subscribe((Subscriber)right);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class JoinSubscription<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */     extends AtomicInteger
/*     */     implements Subscription, FlowableGroupJoin.JoinSupport
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
/*     */     final Map<Integer, TLeft> lefts;
/*     */     
/*     */     final Map<Integer, TRight> rights;
/*     */     
/*     */     final AtomicReference<Throwable> error;
/*     */     
/*     */     final Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd;
/*     */     
/*     */     final Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd;
/*     */     
/*     */     final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
/*     */     
/*     */     final AtomicInteger active;
/*     */     
/*     */     int leftIndex;
/*     */     
/*     */     int rightIndex;
/*     */     volatile boolean cancelled;
/* 106 */     static final Integer LEFT_VALUE = Integer.valueOf(1);
/*     */     
/* 108 */     static final Integer RIGHT_VALUE = Integer.valueOf(2);
/*     */     
/* 110 */     static final Integer LEFT_CLOSE = Integer.valueOf(3);
/*     */     
/* 112 */     static final Integer RIGHT_CLOSE = Integer.valueOf(4);
/*     */ 
/*     */ 
/*     */     
/*     */     JoinSubscription(Subscriber<? super R> actual, Function<? super TLeft, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector) {
/* 117 */       this.downstream = actual;
/* 118 */       this.requested = new AtomicLong();
/* 119 */       this.disposables = new CompositeDisposable();
/* 120 */       this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
/* 121 */       this.lefts = new LinkedHashMap<Integer, TLeft>();
/* 122 */       this.rights = new LinkedHashMap<Integer, TRight>();
/* 123 */       this.error = new AtomicReference<Throwable>();
/* 124 */       this.leftEnd = leftEnd;
/* 125 */       this.rightEnd = rightEnd;
/* 126 */       this.resultSelector = resultSelector;
/* 127 */       this.active = new AtomicInteger(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 132 */       if (SubscriptionHelper.validate(n)) {
/* 133 */         BackpressureHelper.add(this.requested, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 139 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 142 */       this.cancelled = true;
/* 143 */       cancelAll();
/* 144 */       if (getAndIncrement() == 0) {
/* 145 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 150 */       this.disposables.dispose();
/*     */     }
/*     */     
/*     */     void errorAll(Subscriber<?> a) {
/* 154 */       Throwable ex = ExceptionHelper.terminate(this.error);
/*     */       
/* 156 */       this.lefts.clear();
/* 157 */       this.rights.clear();
/*     */       
/* 159 */       a.onError(ex);
/*     */     }
/*     */     
/*     */     void fail(Throwable exc, Subscriber<?> a, SimpleQueue<?> q) {
/* 163 */       Exceptions.throwIfFatal(exc);
/* 164 */       ExceptionHelper.addThrowable(this.error, exc);
/* 165 */       q.clear();
/* 166 */       cancelAll();
/* 167 */       errorAll(a);
/*     */     }
/*     */     
/*     */     void drain() {
/* 171 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 175 */       int missed = 1;
/* 176 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 177 */       Subscriber<? super R> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 181 */         if (this.cancelled) {
/* 182 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 186 */         Throwable ex = this.error.get();
/* 187 */         if (ex != null) {
/* 188 */           q.clear();
/* 189 */           cancelAll();
/* 190 */           errorAll(a);
/*     */           
/*     */           return;
/*     */         } 
/* 194 */         boolean d = (this.active.get() == 0);
/*     */         
/* 196 */         Integer mode = (Integer)q.poll();
/*     */         
/* 198 */         boolean empty = (mode == null);
/*     */         
/* 200 */         if (d && empty) {
/*     */           
/* 202 */           this.lefts.clear();
/* 203 */           this.rights.clear();
/* 204 */           this.disposables.dispose();
/*     */           
/* 206 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 210 */         if (empty)
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
/* 351 */           missed = addAndGet(-missed);
/* 352 */           if (missed == 0)
/*     */             break;  continue; }  Object val = q.poll(); if (mode == LEFT_VALUE) { Publisher<TLeftEnd> p; TLeft left = (TLeft)val; int idx = this.leftIndex++; this.lefts.put(Integer.valueOf(idx), left); try { p = (Publisher<TLeftEnd>)ObjectHelper.requireNonNull(this.leftEnd.apply(left), "The leftEnd returned a null Publisher"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  FlowableGroupJoin.LeftRightEndSubscriber end = new FlowableGroupJoin.LeftRightEndSubscriber(this, true, idx); this.disposables.add(end); p.subscribe((Subscriber)end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  long r = this.requested.get(); long e = 0L; for (TRight right : this.rights.values()) { R w; try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, right), "The resultSelector returned a null value"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  if (e != r) { a.onNext(w); e++; continue; }  ExceptionHelper.addThrowable(this.error, (Throwable)new MissingBackpressureException("Could not emit value due to lack of requests")); q.clear(); cancelAll(); errorAll(a); return; }  if (e != 0L)
/*     */             BackpressureHelper.produced(this.requested, e);  continue; }  if (mode == RIGHT_VALUE) { Publisher<TRightEnd> p; TRight right = (TRight)val; int idx = this.rightIndex++; this.rights.put(Integer.valueOf(idx), right); try { p = (Publisher<TRightEnd>)ObjectHelper.requireNonNull(this.rightEnd.apply(right), "The rightEnd returned a null Publisher"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  FlowableGroupJoin.LeftRightEndSubscriber end = new FlowableGroupJoin.LeftRightEndSubscriber(this, false, idx); this.disposables.add(end); p.subscribe((Subscriber)end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  long r = this.requested.get(); long e = 0L; for (TLeft left : this.lefts.values()) { R w; try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, right), "The resultSelector returned a null value"); } catch (Throwable exc) { fail(exc, a, (SimpleQueue<?>)q); return; }  if (e != r) { a.onNext(w); e++; continue; }  ExceptionHelper.addThrowable(this.error, (Throwable)new MissingBackpressureException("Could not emit value due to lack of requests")); q.clear(); cancelAll(); errorAll(a); return; }
/*     */            if (e != 0L)
/*     */             BackpressureHelper.produced(this.requested, e);  continue; }
/*     */          if (mode == LEFT_CLOSE) { FlowableGroupJoin.LeftRightEndSubscriber end = (FlowableGroupJoin.LeftRightEndSubscriber)val; this.lefts.remove(Integer.valueOf(end.index)); this.disposables.remove(end); continue; }
/*     */          if (mode == RIGHT_CLOSE) { FlowableGroupJoin.LeftRightEndSubscriber end = (FlowableGroupJoin.LeftRightEndSubscriber)val; this.rights.remove(Integer.valueOf(end.index)); this.disposables.remove(end); }
/*     */       
/* 360 */       }  } public void innerError(Throwable ex) { if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 361 */         this.active.decrementAndGet();
/* 362 */         drain();
/*     */       } else {
/* 364 */         RxJavaPlugins.onError(ex);
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerComplete(FlowableGroupJoin.LeftRightSubscriber sender) {
/* 370 */       this.disposables.delete(sender);
/* 371 */       this.active.decrementAndGet();
/* 372 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerValue(boolean isLeft, Object o) {
/* 377 */       synchronized (this) {
/* 378 */         this.queue.offer(isLeft ? LEFT_VALUE : RIGHT_VALUE, o);
/*     */       } 
/* 380 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerClose(boolean isLeft, FlowableGroupJoin.LeftRightEndSubscriber index) {
/* 385 */       synchronized (this) {
/* 386 */         this.queue.offer(isLeft ? LEFT_CLOSE : RIGHT_CLOSE, index);
/*     */       } 
/* 388 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerCloseError(Throwable ex) {
/* 393 */       if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 394 */         drain();
/*     */       } else {
/* 396 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */