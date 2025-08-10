/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class ObservableRetryPredicate<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super Throwable> predicate;
/*     */   final long count;
/*     */   
/*     */   public ObservableRetryPredicate(Observable<T> source, long count, Predicate<? super Throwable> predicate) {
/*  30 */     super((ObservableSource<T>)source);
/*  31 */     this.predicate = predicate;
/*  32 */     this.count = count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*  37 */     SequentialDisposable sa = new SequentialDisposable();
/*  38 */     observer.onSubscribe((Disposable)sa);
/*     */     
/*  40 */     RepeatObserver<T> rs = new RepeatObserver<T>(observer, this.count, this.predicate, sa, this.source);
/*  41 */     rs.subscribeNext();
/*     */   }
/*     */   
/*     */   static final class RepeatObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T> {
/*     */     private static final long serialVersionUID = -7098360935104053232L;
/*     */     final Observer<? super T> downstream;
/*     */     final SequentialDisposable upstream;
/*     */     final ObservableSource<? extends T> source;
/*     */     final Predicate<? super Throwable> predicate;
/*     */     long remaining;
/*     */     
/*     */     RepeatObserver(Observer<? super T> actual, long count, Predicate<? super Throwable> predicate, SequentialDisposable sa, ObservableSource<? extends T> source) {
/*  55 */       this.downstream = actual;
/*  56 */       this.upstream = sa;
/*  57 */       this.source = source;
/*  58 */       this.predicate = predicate;
/*  59 */       this.remaining = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  64 */       this.upstream.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  69 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  74 */       long r = this.remaining;
/*  75 */       if (r != Long.MAX_VALUE) {
/*  76 */         this.remaining = r - 1L;
/*     */       }
/*  78 */       if (r == 0L) {
/*  79 */         this.downstream.onError(t);
/*     */       } else {
/*     */         boolean b;
/*     */         try {
/*  83 */           b = this.predicate.test(t);
/*  84 */         } catch (Throwable e) {
/*  85 */           Exceptions.throwIfFatal(e);
/*  86 */           this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */           return;
/*     */         } 
/*  89 */         if (!b) {
/*  90 */           this.downstream.onError(t);
/*     */           return;
/*     */         } 
/*  93 */         subscribeNext();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/* 106 */       if (getAndIncrement() == 0) {
/* 107 */         int missed = 1;
/*     */         do {
/* 109 */           if (this.upstream.isDisposed()) {
/*     */             return;
/*     */           }
/* 112 */           this.source.subscribe(this);
/*     */           
/* 114 */           missed = addAndGet(-missed);
/* 115 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRetryPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */