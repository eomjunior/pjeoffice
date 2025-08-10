/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableRetryBiPredicate<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final BiPredicate<? super Integer, ? super Throwable> predicate;
/*     */   
/*     */   public ObservableRetryBiPredicate(Observable<T> source, BiPredicate<? super Integer, ? super Throwable> predicate) {
/*  29 */     super((ObservableSource<T>)source);
/*  30 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*  35 */     SequentialDisposable sa = new SequentialDisposable();
/*  36 */     observer.onSubscribe((Disposable)sa);
/*     */     
/*  38 */     RetryBiObserver<T> rs = new RetryBiObserver<T>(observer, this.predicate, sa, this.source);
/*  39 */     rs.subscribeNext();
/*     */   }
/*     */   
/*     */   static final class RetryBiObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T> {
/*     */     private static final long serialVersionUID = -7098360935104053232L;
/*     */     final Observer<? super T> downstream;
/*     */     final SequentialDisposable upstream;
/*     */     final ObservableSource<? extends T> source;
/*     */     final BiPredicate<? super Integer, ? super Throwable> predicate;
/*     */     int retries;
/*     */     
/*     */     RetryBiObserver(Observer<? super T> actual, BiPredicate<? super Integer, ? super Throwable> predicate, SequentialDisposable sa, ObservableSource<? extends T> source) {
/*  53 */       this.downstream = actual;
/*  54 */       this.upstream = sa;
/*  55 */       this.source = source;
/*  56 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  61 */       this.upstream.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  66 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       boolean b;
/*     */       try {
/*  73 */         b = this.predicate.test(Integer.valueOf(++this.retries), t);
/*  74 */       } catch (Throwable e) {
/*  75 */         Exceptions.throwIfFatal(e);
/*  76 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         return;
/*     */       } 
/*  79 */       if (!b) {
/*  80 */         this.downstream.onError(t);
/*     */         return;
/*     */       } 
/*  83 */       subscribeNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  88 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/*  95 */       if (getAndIncrement() == 0) {
/*  96 */         int missed = 1;
/*     */         do {
/*  98 */           if (this.upstream.isDisposed()) {
/*     */             return;
/*     */           }
/* 101 */           this.source.subscribe(this);
/*     */           
/* 103 */           missed = addAndGet(-missed);
/* 104 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRetryBiPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */