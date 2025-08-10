/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BlockingObserver;
/*     */ import io.reactivex.internal.observers.LambdaObserver;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.BlockingIgnoringReceiver;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableBlockingSubscribe
/*     */ {
/*     */   private ObservableBlockingSubscribe() {
/*  31 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void subscribe(ObservableSource<? extends T> o, Observer<? super T> observer) {
/*  43 */     BlockingQueue<Object> queue = new LinkedBlockingQueue();
/*     */     
/*  45 */     BlockingObserver<T> bs = new BlockingObserver(queue);
/*  46 */     observer.onSubscribe((Disposable)bs);
/*     */     
/*  48 */     o.subscribe((Observer)bs);
/*     */     
/*  50 */     while (!bs.isDisposed()) {
/*     */ 
/*     */       
/*  53 */       Object v = queue.poll();
/*  54 */       if (v == null) {
/*     */         try {
/*  56 */           v = queue.take();
/*  57 */         } catch (InterruptedException ex) {
/*  58 */           bs.dispose();
/*  59 */           observer.onError(ex);
/*     */           return;
/*     */         } 
/*     */       }
/*  63 */       if (bs.isDisposed() || v == BlockingObserver.TERMINATED || 
/*     */         
/*  65 */         NotificationLite.acceptFull(v, observer)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void subscribe(ObservableSource<? extends T> o) {
/*  77 */     BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
/*     */     
/*  79 */     LambdaObserver<T> ls = new LambdaObserver(Functions.emptyConsumer(), (Consumer)callback, (Action)callback, Functions.emptyConsumer());
/*     */     
/*  81 */     o.subscribe((Observer)ls);
/*     */     
/*  83 */     BlockingHelper.awaitForComplete((CountDownLatch)callback, (Disposable)ls);
/*  84 */     Throwable e = callback.error;
/*  85 */     if (e != null) {
/*  86 */       throw ExceptionHelper.wrapOrThrow(e);
/*     */     }
/*     */   }
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
/*     */   public static <T> void subscribe(ObservableSource<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/* 100 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 101 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 102 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 103 */     subscribe(o, (Observer<? super T>)new LambdaObserver(onNext, onError, onComplete, Functions.emptyConsumer()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBlockingSubscribe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */