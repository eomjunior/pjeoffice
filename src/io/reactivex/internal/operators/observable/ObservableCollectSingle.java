/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ObservableCollectSingle<T, U>
/*     */   extends Single<U>
/*     */   implements FuseToObservable<U>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final Callable<? extends U> initialSupplier;
/*     */   final BiConsumer<? super U, ? super T> collector;
/*     */   
/*     */   public ObservableCollectSingle(ObservableSource<T> source, Callable<? extends U> initialSupplier, BiConsumer<? super U, ? super T> collector) {
/*  34 */     this.source = source;
/*  35 */     this.initialSupplier = initialSupplier;
/*  36 */     this.collector = collector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super U> t) {
/*     */     U u;
/*     */     try {
/*  43 */       u = (U)ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value");
/*  44 */     } catch (Throwable e) {
/*  45 */       EmptyDisposable.error(e, t);
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     this.source.subscribe(new CollectObserver<T, U>(t, u, this.collector));
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<U> fuseToObservable() {
/*  54 */     return RxJavaPlugins.onAssembly(new ObservableCollect<T, U>(this.source, this.initialSupplier, this.collector));
/*     */   }
/*     */   
/*     */   static final class CollectObserver<T, U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super U> downstream;
/*     */     final BiConsumer<? super U, ? super T> collector;
/*     */     final U u;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     CollectObserver(SingleObserver<? super U> actual, U u, BiConsumer<? super U, ? super T> collector) {
/*  67 */       this.downstream = actual;
/*  68 */       this.collector = collector;
/*  69 */       this.u = u;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  75 */         this.upstream = d;
/*  76 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  82 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  87 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  92 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/*  96 */         this.collector.accept(this.u, t);
/*  97 */       } catch (Throwable e) {
/*  98 */         this.upstream.dispose();
/*  99 */         onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 105 */       if (this.done) {
/* 106 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 109 */       this.done = true;
/* 110 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 115 */       if (this.done) {
/*     */         return;
/*     */       }
/* 118 */       this.done = true;
/* 119 */       this.downstream.onSuccess(this.u);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCollectSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */