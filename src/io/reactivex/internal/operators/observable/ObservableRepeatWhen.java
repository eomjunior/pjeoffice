/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import io.reactivex.subjects.PublishSubject;
/*     */ import io.reactivex.subjects.Subject;
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
/*     */ public final class ObservableRepeatWhen<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super Observable<Object>, ? extends ObservableSource<?>> handler;
/*     */   
/*     */   public ObservableRepeatWhen(ObservableSource<T> source, Function<? super Observable<Object>, ? extends ObservableSource<?>> handler) {
/*  37 */     super(source);
/*  38 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*     */     ObservableSource<?> other;
/*  43 */     Subject<Object> signaller = PublishSubject.create().toSerialized();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  48 */       other = (ObservableSource)ObjectHelper.requireNonNull(this.handler.apply(signaller), "The handler returned a null ObservableSource");
/*  49 */     } catch (Throwable ex) {
/*  50 */       Exceptions.throwIfFatal(ex);
/*  51 */       EmptyDisposable.error(ex, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  55 */     RepeatWhenObserver<T> parent = new RepeatWhenObserver<T>(observer, signaller, this.source);
/*  56 */     observer.onSubscribe(parent);
/*     */     
/*  58 */     other.subscribe(parent.inner);
/*     */     
/*  60 */     parent.subscribeNext();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RepeatWhenObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 802743776666017014L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final AtomicInteger wip;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final Subject<Object> signaller;
/*     */     
/*     */     final InnerRepeatObserver inner;
/*     */     final AtomicReference<Disposable> upstream;
/*     */     final ObservableSource<T> source;
/*     */     volatile boolean active;
/*     */     
/*     */     RepeatWhenObserver(Observer<? super T> actual, Subject<Object> signaller, ObservableSource<T> source) {
/*  84 */       this.downstream = actual;
/*  85 */       this.signaller = signaller;
/*  86 */       this.source = source;
/*  87 */       this.wip = new AtomicInteger();
/*  88 */       this.error = new AtomicThrowable();
/*  89 */       this.inner = new InnerRepeatObserver();
/*  90 */       this.upstream = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  95 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 100 */       HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 105 */       DisposableHelper.dispose(this.inner);
/* 106 */       HalfSerializer.onError(this.downstream, e, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 111 */       DisposableHelper.replace(this.upstream, null);
/* 112 */       this.active = false;
/* 113 */       this.signaller.onNext(Integer.valueOf(0));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 118 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 123 */       DisposableHelper.dispose(this.upstream);
/* 124 */       DisposableHelper.dispose(this.inner);
/*     */     }
/*     */     
/*     */     void innerNext() {
/* 128 */       subscribeNext();
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 132 */       DisposableHelper.dispose(this.upstream);
/* 133 */       HalfSerializer.onError(this.downstream, ex, this, this.error);
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 137 */       DisposableHelper.dispose(this.upstream);
/* 138 */       HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */     }
/*     */     
/*     */     void subscribeNext() {
/* 142 */       if (this.wip.getAndIncrement() == 0)
/*     */         do
/*     */         {
/* 145 */           if (isDisposed()) {
/*     */             return;
/*     */           }
/*     */           
/* 149 */           if (this.active)
/* 150 */             continue;  this.active = true;
/* 151 */           this.source.subscribe(this);
/*     */         }
/* 153 */         while (this.wip.decrementAndGet() != 0); 
/*     */     }
/*     */     
/*     */     final class InnerRepeatObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements Observer<Object>
/*     */     {
/*     */       private static final long serialVersionUID = 3254781284376480842L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 163 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Object t) {
/* 168 */         ObservableRepeatWhen.RepeatWhenObserver.this.innerNext();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 173 */         ObservableRepeatWhen.RepeatWhenObserver.this.innerError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 178 */         ObservableRepeatWhen.RepeatWhenObserver.this.innerComplete(); } } } final class InnerRepeatObserver extends AtomicReference<Disposable> implements Observer<Object> { public void onComplete() { ObservableRepeatWhen.RepeatWhenObserver.this.innerComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 3254781284376480842L;
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onNext(Object t) {
/*     */       ObservableRepeatWhen.RepeatWhenObserver.this.innerNext();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       ObservableRepeatWhen.RepeatWhenObserver.this.innerError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRepeatWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */