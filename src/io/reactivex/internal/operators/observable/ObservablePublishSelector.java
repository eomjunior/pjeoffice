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
/*     */ import io.reactivex.subjects.PublishSubject;
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
/*     */ public final class ObservablePublishSelector<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super Observable<T>, ? extends ObservableSource<R>> selector;
/*     */   
/*     */   public ObservablePublishSelector(ObservableSource<T> source, Function<? super Observable<T>, ? extends ObservableSource<R>> selector) {
/*  37 */     super(source);
/*  38 */     this.selector = selector;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*     */     ObservableSource<? extends R> target;
/*  43 */     PublishSubject<T> subject = PublishSubject.create();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  48 */       target = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.selector.apply(subject), "The selector returned a null ObservableSource");
/*  49 */     } catch (Throwable ex) {
/*  50 */       Exceptions.throwIfFatal(ex);
/*  51 */       EmptyDisposable.error(ex, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  55 */     TargetObserver<T, R> o = new TargetObserver<T, R>(observer);
/*     */     
/*  57 */     target.subscribe(o);
/*     */     
/*  59 */     this.source.subscribe(new SourceObserver<T, Object>(subject, o));
/*     */   }
/*     */   
/*     */   static final class SourceObserver<T, R>
/*     */     implements Observer<T>
/*     */   {
/*     */     final PublishSubject<T> subject;
/*     */     final AtomicReference<Disposable> target;
/*     */     
/*     */     SourceObserver(PublishSubject<T> subject, AtomicReference<Disposable> target) {
/*  69 */       this.subject = subject;
/*  70 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       DisposableHelper.setOnce(this.target, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  80 */       this.subject.onNext(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  85 */       this.subject.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  90 */       this.subject.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TargetObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<R>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 854110278590336484L;
/*     */     final Observer<? super R> downstream;
/*     */     Disposable upstream;
/*     */     
/*     */     TargetObserver(Observer<? super R> downstream) {
/* 103 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 108 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 109 */         this.upstream = d;
/*     */         
/* 111 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R value) {
/* 117 */       this.downstream.onNext(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 122 */       DisposableHelper.dispose(this);
/* 123 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 128 */       DisposableHelper.dispose(this);
/* 129 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 134 */       this.upstream.dispose();
/* 135 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 140 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservablePublishSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */