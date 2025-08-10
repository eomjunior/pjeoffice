/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
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
/*     */ public final class ObservableWithLatestFrom<T, U, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */   final ObservableSource<? extends U> other;
/*     */   
/*     */   public ObservableWithLatestFrom(ObservableSource<T> source, BiFunction<? super T, ? super U, ? extends R> combiner, ObservableSource<? extends U> other) {
/*  31 */     super(source);
/*  32 */     this.combiner = combiner;
/*  33 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> t) {
/*  38 */     SerializedObserver<R> serial = new SerializedObserver(t);
/*  39 */     WithLatestFromObserver<T, U, R> wlf = new WithLatestFromObserver<T, U, R>((Observer<? super R>)serial, this.combiner);
/*     */     
/*  41 */     serial.onSubscribe(wlf);
/*     */     
/*  43 */     this.other.subscribe(new WithLatestFromOtherObserver(wlf));
/*     */     
/*  45 */     this.source.subscribe(wlf);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestFromObserver<T, U, R>
/*     */     extends AtomicReference<U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -312246233408980075L;
/*     */     final Observer<? super R> downstream;
/*     */     final BiFunction<? super T, ? super U, ? extends R> combiner;
/*  56 */     final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*     */     
/*  58 */     final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
/*     */     
/*     */     WithLatestFromObserver(Observer<? super R> actual, BiFunction<? super T, ? super U, ? extends R> combiner) {
/*  61 */       this.downstream = actual;
/*  62 */       this.combiner = combiner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  67 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  72 */       U u = get();
/*  73 */       if (u != null) {
/*     */         R r;
/*     */         try {
/*  76 */           r = (R)ObjectHelper.requireNonNull(this.combiner.apply(t, u), "The combiner returned a null value");
/*  77 */         } catch (Throwable e) {
/*  78 */           Exceptions.throwIfFatal(e);
/*  79 */           dispose();
/*  80 */           this.downstream.onError(e);
/*     */           return;
/*     */         } 
/*  83 */         this.downstream.onNext(r);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  89 */       DisposableHelper.dispose(this.other);
/*  90 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       DisposableHelper.dispose(this.other);
/*  96 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 101 */       DisposableHelper.dispose(this.upstream);
/* 102 */       DisposableHelper.dispose(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 107 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */     
/*     */     public boolean setOther(Disposable o) {
/* 111 */       return DisposableHelper.setOnce(this.other, o);
/*     */     }
/*     */     
/*     */     public void otherError(Throwable e) {
/* 115 */       DisposableHelper.dispose(this.upstream);
/* 116 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */   
/*     */   final class WithLatestFromOtherObserver implements Observer<U> {
/*     */     private final ObservableWithLatestFrom.WithLatestFromObserver<T, U, R> parent;
/*     */     
/*     */     WithLatestFromOtherObserver(ObservableWithLatestFrom.WithLatestFromObserver<T, U, R> parent) {
/* 124 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 129 */       this.parent.setOther(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/* 134 */       this.parent.lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 139 */       this.parent.otherError(t);
/*     */     }
/*     */     
/*     */     public void onComplete() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWithLatestFrom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */