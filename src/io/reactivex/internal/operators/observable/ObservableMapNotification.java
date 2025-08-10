/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.Callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableMapNotification<T, R>
/*     */   extends AbstractObservableWithUpstream<T, ObservableSource<? extends R>>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;
/*     */   final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
/*     */   final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
/*     */   
/*     */   public ObservableMapNotification(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier) {
/*  36 */     super(source);
/*  37 */     this.onNextMapper = onNextMapper;
/*  38 */     this.onErrorMapper = onErrorMapper;
/*  39 */     this.onCompleteSupplier = onCompleteSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super ObservableSource<? extends R>> t) {
/*  44 */     this.source.subscribe(new MapNotificationObserver<T, R>(t, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MapNotificationObserver<T, R>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super ObservableSource<? extends R>> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;
/*     */     
/*     */     final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
/*     */     final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
/*     */     Disposable upstream;
/*     */     
/*     */     MapNotificationObserver(Observer<? super ObservableSource<? extends R>> actual, Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier) {
/*  60 */       this.downstream = actual;
/*  61 */       this.onNextMapper = onNextMapper;
/*  62 */       this.onErrorMapper = onErrorMapper;
/*  63 */       this.onCompleteSupplier = onCompleteSupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         this.upstream = d;
/*  70 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  76 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  81 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<? extends R> p;
/*     */       try {
/*  89 */         p = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.onNextMapper.apply(t), "The onNext ObservableSource returned is null");
/*  90 */       } catch (Throwable e) {
/*  91 */         Exceptions.throwIfFatal(e);
/*  92 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  96 */       this.downstream.onNext(p);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       ObservableSource<? extends R> p;
/*     */       try {
/* 104 */         p = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.onErrorMapper.apply(t), "The onError ObservableSource returned is null");
/* 105 */       } catch (Throwable e) {
/* 106 */         Exceptions.throwIfFatal(e);
/* 107 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         
/*     */         return;
/*     */       } 
/* 111 */       this.downstream.onNext(p);
/* 112 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       ObservableSource<? extends R> p;
/*     */       try {
/* 120 */         p = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onComplete ObservableSource returned is null");
/* 121 */       } catch (Throwable e) {
/* 122 */         Exceptions.throwIfFatal(e);
/* 123 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 127 */       this.downstream.onNext(p);
/* 128 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMapNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */