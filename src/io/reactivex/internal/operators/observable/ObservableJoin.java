/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class ObservableJoin<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */   extends AbstractObservableWithUpstream<TLeft, R>
/*     */ {
/*     */   final ObservableSource<? extends TRight> other;
/*     */   final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
/*     */   final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
/*     */   final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
/*     */   
/*     */   public ObservableJoin(ObservableSource<TLeft> source, ObservableSource<? extends TRight> other, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector) {
/*  49 */     super(source);
/*  50 */     this.other = other;
/*  51 */     this.leftEnd = leftEnd;
/*  52 */     this.rightEnd = rightEnd;
/*  53 */     this.resultSelector = resultSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  59 */     JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R>(observer, this.leftEnd, this.rightEnd, this.resultSelector);
/*     */ 
/*     */ 
/*     */     
/*  63 */     observer.onSubscribe(parent);
/*     */     
/*  65 */     ObservableGroupJoin.LeftRightObserver left = new ObservableGroupJoin.LeftRightObserver(parent, true);
/*  66 */     parent.disposables.add(left);
/*  67 */     ObservableGroupJoin.LeftRightObserver right = new ObservableGroupJoin.LeftRightObserver(parent, false);
/*  68 */     parent.disposables.add(right);
/*     */     
/*  70 */     this.source.subscribe(left);
/*  71 */     this.other.subscribe(right);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class JoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */     extends AtomicInteger
/*     */     implements Disposable, ObservableGroupJoin.JoinSupport
/*     */   {
/*     */     private static final long serialVersionUID = -6071216598687999801L;
/*     */     
/*     */     final Observer<? super R> downstream;
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
/*     */     final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
/*     */     
/*     */     final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
/*     */     
/*     */     final BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector;
/*     */     
/*     */     final AtomicInteger active;
/*     */     
/*     */     int leftIndex;
/*     */     
/*     */     int rightIndex;
/*     */     volatile boolean cancelled;
/* 105 */     static final Integer LEFT_VALUE = Integer.valueOf(1);
/*     */     
/* 107 */     static final Integer RIGHT_VALUE = Integer.valueOf(2);
/*     */     
/* 109 */     static final Integer LEFT_CLOSE = Integer.valueOf(3);
/*     */     
/* 111 */     static final Integer RIGHT_CLOSE = Integer.valueOf(4);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     JoinDisposable(Observer<? super R> actual, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super TRight, ? extends R> resultSelector) {
/* 117 */       this.downstream = actual;
/* 118 */       this.disposables = new CompositeDisposable();
/* 119 */       this.queue = new SpscLinkedArrayQueue(Observable.bufferSize());
/* 120 */       this.lefts = new LinkedHashMap<Integer, TLeft>();
/* 121 */       this.rights = new LinkedHashMap<Integer, TRight>();
/* 122 */       this.error = new AtomicReference<Throwable>();
/* 123 */       this.leftEnd = leftEnd;
/* 124 */       this.rightEnd = rightEnd;
/* 125 */       this.resultSelector = resultSelector;
/* 126 */       this.active = new AtomicInteger(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 131 */       if (!this.cancelled) {
/* 132 */         this.cancelled = true;
/* 133 */         cancelAll();
/* 134 */         if (getAndIncrement() == 0) {
/* 135 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 142 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 146 */       this.disposables.dispose();
/*     */     }
/*     */     
/*     */     void errorAll(Observer<?> a) {
/* 150 */       Throwable ex = ExceptionHelper.terminate(this.error);
/*     */       
/* 152 */       this.lefts.clear();
/* 153 */       this.rights.clear();
/*     */       
/* 155 */       a.onError(ex);
/*     */     }
/*     */     
/*     */     void fail(Throwable exc, Observer<?> a, SpscLinkedArrayQueue<?> q) {
/* 159 */       Exceptions.throwIfFatal(exc);
/* 160 */       ExceptionHelper.addThrowable(this.error, exc);
/* 161 */       q.clear();
/* 162 */       cancelAll();
/* 163 */       errorAll(a);
/*     */     }
/*     */     
/*     */     void drain() {
/* 167 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 171 */       int missed = 1;
/* 172 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 173 */       Observer<? super R> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 177 */         if (this.cancelled) {
/* 178 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 182 */         Throwable ex = this.error.get();
/* 183 */         if (ex != null) {
/* 184 */           q.clear();
/* 185 */           cancelAll();
/* 186 */           errorAll(a);
/*     */           
/*     */           return;
/*     */         } 
/* 190 */         boolean d = (this.active.get() == 0);
/*     */         
/* 192 */         Integer mode = (Integer)q.poll();
/*     */         
/* 194 */         boolean empty = (mode == null);
/*     */         
/* 196 */         if (d && empty) {
/*     */           
/* 198 */           this.lefts.clear();
/* 199 */           this.rights.clear();
/* 200 */           this.disposables.dispose();
/*     */           
/* 202 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 206 */         if (empty)
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
/* 312 */           missed = addAndGet(-missed);
/* 313 */           if (missed == 0)
/*     */             break;  continue; }  Object val = q.poll(); if (mode == LEFT_VALUE) { ObservableSource<TLeftEnd> p; TLeft left = (TLeft)val; int idx = this.leftIndex++; this.lefts.put(Integer.valueOf(idx), left); try { p = (ObservableSource<TLeftEnd>)ObjectHelper.requireNonNull(this.leftEnd.apply(left), "The leftEnd returned a null ObservableSource"); } catch (Throwable exc) { fail(exc, a, q); return; }  ObservableGroupJoin.LeftRightEndObserver leftRightEndObserver = new ObservableGroupJoin.LeftRightEndObserver(this, true, idx); this.disposables.add(leftRightEndObserver); p.subscribe(leftRightEndObserver); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  for (TRight right : this.rights.values()) { R w; try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, right), "The resultSelector returned a null value"); } catch (Throwable exc) { fail(exc, a, q); return; }  a.onNext(w); }  continue; }  if (mode == RIGHT_VALUE) { ObservableSource<TRightEnd> p; TRight right = (TRight)val; int idx = this.rightIndex++; this.rights.put(Integer.valueOf(idx), right); try { p = (ObservableSource<TRightEnd>)ObjectHelper.requireNonNull(this.rightEnd.apply(right), "The rightEnd returned a null ObservableSource"); } catch (Throwable exc) { fail(exc, a, q); return; }  ObservableGroupJoin.LeftRightEndObserver leftRightEndObserver = new ObservableGroupJoin.LeftRightEndObserver(this, false, idx); this.disposables.add(leftRightEndObserver); p.subscribe(leftRightEndObserver); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }
/*     */            for (TLeft left : this.lefts.values()) { R w; try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, right), "The resultSelector returned a null value"); }
/*     */             catch (Throwable exc) { fail(exc, a, q); return; }
/*     */              a.onNext(w); }
/*     */            continue; }
/*     */          if (mode == LEFT_CLOSE) { ObservableGroupJoin.LeftRightEndObserver leftRightEndObserver = (ObservableGroupJoin.LeftRightEndObserver)val; this.lefts.remove(Integer.valueOf(leftRightEndObserver.index)); this.disposables.remove(leftRightEndObserver); continue; }
/*     */          ObservableGroupJoin.LeftRightEndObserver end = (ObservableGroupJoin.LeftRightEndObserver)val; this.rights.remove(Integer.valueOf(end.index)); this.disposables.remove(end);
/* 321 */       }  } public void innerError(Throwable ex) { if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 322 */         this.active.decrementAndGet();
/* 323 */         drain();
/*     */       } else {
/* 325 */         RxJavaPlugins.onError(ex);
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerComplete(ObservableGroupJoin.LeftRightObserver sender) {
/* 331 */       this.disposables.delete(sender);
/* 332 */       this.active.decrementAndGet();
/* 333 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerValue(boolean isLeft, Object o) {
/* 338 */       synchronized (this) {
/* 339 */         this.queue.offer(isLeft ? LEFT_VALUE : RIGHT_VALUE, o);
/*     */       } 
/* 341 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerClose(boolean isLeft, ObservableGroupJoin.LeftRightEndObserver index) {
/* 346 */       synchronized (this) {
/* 347 */         this.queue.offer(isLeft ? LEFT_CLOSE : RIGHT_CLOSE, index);
/*     */       } 
/* 349 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerCloseError(Throwable ex) {
/* 354 */       if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 355 */         drain();
/*     */       } else {
/* 357 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */