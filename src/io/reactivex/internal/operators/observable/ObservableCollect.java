/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
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
/*     */ 
/*     */ 
/*     */ public final class ObservableCollect<T, U>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<? extends U> initialSupplier;
/*     */   final BiConsumer<? super U, ? super T> collector;
/*     */   
/*     */   public ObservableCollect(ObservableSource<T> source, Callable<? extends U> initialSupplier, BiConsumer<? super U, ? super T> collector) {
/*  30 */     super(source);
/*  31 */     this.initialSupplier = initialSupplier;
/*  32 */     this.collector = collector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*     */     U u;
/*     */     try {
/*  39 */       u = (U)ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value");
/*  40 */     } catch (Throwable e) {
/*  41 */       EmptyDisposable.error(e, t);
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     this.source.subscribe(new CollectObserver<T, U>(t, u, this.collector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CollectObserver<T, U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super U> downstream;
/*     */     final BiConsumer<? super U, ? super T> collector;
/*     */     final U u;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     CollectObserver(Observer<? super U> actual, U u, BiConsumer<? super U, ? super T> collector) {
/*  59 */       this.downstream = actual;
/*  60 */       this.collector = collector;
/*  61 */       this.u = u;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  66 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  67 */         this.upstream = d;
/*  68 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  74 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  79 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  84 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/*  88 */         this.collector.accept(this.u, t);
/*  89 */       } catch (Throwable e) {
/*  90 */         this.upstream.dispose();
/*  91 */         onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  97 */       if (this.done) {
/*  98 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 101 */       this.done = true;
/* 102 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 107 */       if (this.done) {
/*     */         return;
/*     */       }
/* 110 */       this.done = true;
/* 111 */       this.downstream.onNext(this.u);
/* 112 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */