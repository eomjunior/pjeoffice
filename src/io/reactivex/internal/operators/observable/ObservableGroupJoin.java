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
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subjects.UnicastSubject;
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
/*     */ public final class ObservableGroupJoin<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */   extends AbstractObservableWithUpstream<TLeft, R>
/*     */ {
/*     */   final ObservableSource<? extends TRight> other;
/*     */   final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
/*     */   final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
/*     */   final BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector;
/*     */   
/*     */   public ObservableGroupJoin(ObservableSource<TLeft> source, ObservableSource<? extends TRight> other, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector) {
/*  51 */     super(source);
/*  52 */     this.other = other;
/*  53 */     this.leftEnd = leftEnd;
/*  54 */     this.rightEnd = rightEnd;
/*  55 */     this.resultSelector = resultSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  61 */     GroupJoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R> parent = new GroupJoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R>(observer, this.leftEnd, this.rightEnd, this.resultSelector);
/*     */ 
/*     */     
/*  64 */     observer.onSubscribe(parent);
/*     */     
/*  66 */     LeftRightObserver left = new LeftRightObserver(parent, true);
/*  67 */     parent.disposables.add(left);
/*  68 */     LeftRightObserver right = new LeftRightObserver(parent, false);
/*  69 */     parent.disposables.add(right);
/*     */     
/*  71 */     this.source.subscribe(left);
/*  72 */     this.other.subscribe(right);
/*     */   }
/*     */ 
/*     */   
/*     */   static interface JoinSupport
/*     */   {
/*     */     void innerError(Throwable param1Throwable);
/*     */ 
/*     */     
/*     */     void innerComplete(ObservableGroupJoin.LeftRightObserver param1LeftRightObserver);
/*     */ 
/*     */     
/*     */     void innerValue(boolean param1Boolean, Object param1Object);
/*     */ 
/*     */     
/*     */     void innerClose(boolean param1Boolean, ObservableGroupJoin.LeftRightEndObserver param1LeftRightEndObserver);
/*     */ 
/*     */     
/*     */     void innerCloseError(Throwable param1Throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class GroupJoinDisposable<TLeft, TRight, TLeftEnd, TRightEnd, R>
/*     */     extends AtomicInteger
/*     */     implements Disposable, JoinSupport
/*     */   {
/*     */     private static final long serialVersionUID = -6071216598687999801L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     
/*     */     final CompositeDisposable disposables;
/*     */     
/*     */     final Map<Integer, UnicastSubject<TRight>> lefts;
/*     */     
/*     */     final Map<Integer, TRight> rights;
/*     */     
/*     */     final AtomicReference<Throwable> error;
/*     */     
/*     */     final Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd;
/*     */     final Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd;
/*     */     final BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector;
/*     */     final AtomicInteger active;
/*     */     int leftIndex;
/*     */     int rightIndex;
/*     */     volatile boolean cancelled;
/* 119 */     static final Integer LEFT_VALUE = Integer.valueOf(1);
/*     */     
/* 121 */     static final Integer RIGHT_VALUE = Integer.valueOf(2);
/*     */     
/* 123 */     static final Integer LEFT_CLOSE = Integer.valueOf(3);
/*     */     
/* 125 */     static final Integer RIGHT_CLOSE = Integer.valueOf(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GroupJoinDisposable(Observer<? super R> actual, Function<? super TLeft, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super TLeft, ? super Observable<TRight>, ? extends R> resultSelector) {
/* 132 */       this.downstream = actual;
/* 133 */       this.disposables = new CompositeDisposable();
/* 134 */       this.queue = new SpscLinkedArrayQueue(Observable.bufferSize());
/* 135 */       this.lefts = new LinkedHashMap<Integer, UnicastSubject<TRight>>();
/* 136 */       this.rights = new LinkedHashMap<Integer, TRight>();
/* 137 */       this.error = new AtomicReference<Throwable>();
/* 138 */       this.leftEnd = leftEnd;
/* 139 */       this.rightEnd = rightEnd;
/* 140 */       this.resultSelector = resultSelector;
/* 141 */       this.active = new AtomicInteger(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 146 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 149 */       this.cancelled = true;
/* 150 */       cancelAll();
/* 151 */       if (getAndIncrement() == 0) {
/* 152 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 158 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 162 */       this.disposables.dispose();
/*     */     }
/*     */     
/*     */     void errorAll(Observer<?> a) {
/* 166 */       Throwable ex = ExceptionHelper.terminate(this.error);
/*     */       
/* 168 */       for (UnicastSubject<TRight> up : this.lefts.values()) {
/* 169 */         up.onError(ex);
/*     */       }
/*     */       
/* 172 */       this.lefts.clear();
/* 173 */       this.rights.clear();
/*     */       
/* 175 */       a.onError(ex);
/*     */     }
/*     */     
/*     */     void fail(Throwable exc, Observer<?> a, SpscLinkedArrayQueue<?> q) {
/* 179 */       Exceptions.throwIfFatal(exc);
/* 180 */       ExceptionHelper.addThrowable(this.error, exc);
/* 181 */       q.clear();
/* 182 */       cancelAll();
/* 183 */       errorAll(a);
/*     */     }
/*     */     
/*     */     void drain() {
/* 187 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 191 */       int missed = 1;
/* 192 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 193 */       Observer<? super R> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 197 */         if (this.cancelled) {
/* 198 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 202 */         Throwable ex = this.error.get();
/* 203 */         if (ex != null) {
/* 204 */           q.clear();
/* 205 */           cancelAll();
/* 206 */           errorAll(a);
/*     */           
/*     */           return;
/*     */         } 
/* 210 */         boolean d = (this.active.get() == 0);
/*     */         
/* 212 */         Integer mode = (Integer)q.poll();
/*     */         
/* 214 */         boolean empty = (mode == null);
/*     */         
/* 216 */         if (d && empty) {
/* 217 */           for (UnicastSubject<?> up : this.lefts.values()) {
/* 218 */             up.onComplete();
/*     */           }
/*     */           
/* 221 */           this.lefts.clear();
/* 222 */           this.rights.clear();
/* 223 */           this.disposables.dispose();
/*     */           
/* 225 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 229 */         if (empty)
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
/* 331 */           missed = addAndGet(-missed);
/* 332 */           if (missed == 0)
/*     */             break;  continue; }  Object val = q.poll(); if (mode == LEFT_VALUE) { ObservableSource<TLeftEnd> p; R w; TLeft left = (TLeft)val; UnicastSubject<TRight> up = UnicastSubject.create(); int idx = this.leftIndex++; this.lefts.put(Integer.valueOf(idx), up); try { p = (ObservableSource<TLeftEnd>)ObjectHelper.requireNonNull(this.leftEnd.apply(left), "The leftEnd returned a null ObservableSource"); } catch (Throwable exc) { fail(exc, a, q); return; }  ObservableGroupJoin.LeftRightEndObserver end = new ObservableGroupJoin.LeftRightEndObserver(this, true, idx); this.disposables.add(end); p.subscribe(end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  try { w = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(left, up), "The resultSelector returned a null value"); } catch (Throwable exc) { fail(exc, a, q); return; }  a.onNext(w); for (TRight right : this.rights.values())
/*     */             up.onNext(right);  continue; }  if (mode == RIGHT_VALUE) { ObservableSource<TRightEnd> p; TRight right = (TRight)val; int idx = this.rightIndex++; this.rights.put(Integer.valueOf(idx), right); try { p = (ObservableSource<TRightEnd>)ObjectHelper.requireNonNull(this.rightEnd.apply(right), "The rightEnd returned a null ObservableSource"); } catch (Throwable exc) { ObservableSource<TLeftEnd> observableSource; fail((Throwable)observableSource, a, q); return; }  ObservableGroupJoin.LeftRightEndObserver end = new ObservableGroupJoin.LeftRightEndObserver(this, false, idx); this.disposables.add(end); p.subscribe(end); ex = this.error.get(); if (ex != null) { q.clear(); cancelAll(); errorAll(a); return; }  for (UnicastSubject<TRight> up : this.lefts.values())
/*     */             up.onNext(right);  continue; }
/*     */          if (mode == LEFT_CLOSE) { ObservableGroupJoin.LeftRightEndObserver end = (ObservableGroupJoin.LeftRightEndObserver)val; UnicastSubject<TRight> up = this.lefts.remove(Integer.valueOf(end.index)); this.disposables.remove(end); if (up != null)
/*     */             up.onComplete();  continue; }
/*     */          if (mode == RIGHT_CLOSE) { ObservableGroupJoin.LeftRightEndObserver end = (ObservableGroupJoin.LeftRightEndObserver)val; this.rights.remove(Integer.valueOf(end.index)); this.disposables.remove(end); }
/*     */       
/* 340 */       }  } public void innerError(Throwable ex) { if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 341 */         this.active.decrementAndGet();
/* 342 */         drain();
/*     */       } else {
/* 344 */         RxJavaPlugins.onError(ex);
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerComplete(ObservableGroupJoin.LeftRightObserver sender) {
/* 350 */       this.disposables.delete(sender);
/* 351 */       this.active.decrementAndGet();
/* 352 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerValue(boolean isLeft, Object o) {
/* 357 */       synchronized (this) {
/* 358 */         this.queue.offer(isLeft ? LEFT_VALUE : RIGHT_VALUE, o);
/*     */       } 
/* 360 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerClose(boolean isLeft, ObservableGroupJoin.LeftRightEndObserver index) {
/* 365 */       synchronized (this) {
/* 366 */         this.queue.offer(isLeft ? LEFT_CLOSE : RIGHT_CLOSE, index);
/*     */       } 
/* 368 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerCloseError(Throwable ex) {
/* 373 */       if (ExceptionHelper.addThrowable(this.error, ex)) {
/* 374 */         drain();
/*     */       } else {
/* 376 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LeftRightObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1883890389173668373L;
/*     */     
/*     */     final ObservableGroupJoin.JoinSupport parent;
/*     */     final boolean isLeft;
/*     */     
/*     */     LeftRightObserver(ObservableGroupJoin.JoinSupport parent, boolean isLeft) {
/* 392 */       this.parent = parent;
/* 393 */       this.isLeft = isLeft;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 398 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 403 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 408 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 413 */       this.parent.innerValue(this.isLeft, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 418 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 423 */       this.parent.innerComplete(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LeftRightEndObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1883890389173668373L;
/*     */     
/*     */     final ObservableGroupJoin.JoinSupport parent;
/*     */     
/*     */     final boolean isLeft;
/*     */     
/*     */     final int index;
/*     */ 
/*     */     
/*     */     LeftRightEndObserver(ObservableGroupJoin.JoinSupport parent, boolean isLeft, int index) {
/* 442 */       this.parent = parent;
/* 443 */       this.isLeft = isLeft;
/* 444 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 449 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 454 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 459 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 464 */       if (DisposableHelper.dispose(this)) {
/* 465 */         this.parent.innerClose(this.isLeft, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 471 */       this.parent.innerCloseError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 476 */       this.parent.innerClose(this.isLeft, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableGroupJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */