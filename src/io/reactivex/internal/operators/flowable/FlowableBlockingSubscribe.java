/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscribers.BlockingSubscriber;
/*     */ import io.reactivex.internal.subscribers.BoundedSubscriber;
/*     */ import io.reactivex.internal.subscribers.LambdaSubscriber;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.BlockingIgnoringReceiver;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableBlockingSubscribe
/*     */ {
/*     */   private FlowableBlockingSubscribe() {
/*  32 */     throw new IllegalStateException("No instances!");
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
/*     */   public static <T> void subscribe(Publisher<? extends T> o, Subscriber<? super T> subscriber) {
/*  44 */     BlockingQueue<Object> queue = new LinkedBlockingQueue();
/*     */     
/*  46 */     BlockingSubscriber<T> bs = new BlockingSubscriber(queue);
/*     */     
/*  48 */     o.subscribe((Subscriber)bs);
/*     */ 
/*     */     
/*     */     try {
/*  52 */       while (!bs.isCancelled()) {
/*     */ 
/*     */         
/*  55 */         Object v = queue.poll();
/*  56 */         if (v == null) {
/*  57 */           if (bs.isCancelled()) {
/*     */             break;
/*     */           }
/*  60 */           BlockingHelper.verifyNonBlocking();
/*  61 */           v = queue.take();
/*     */         } 
/*  63 */         if (bs.isCancelled()) {
/*     */           break;
/*     */         }
/*  66 */         if (v == BlockingSubscriber.TERMINATED || 
/*  67 */           NotificationLite.acceptFull(v, subscriber)) {
/*     */           break;
/*     */         }
/*     */       } 
/*  71 */     } catch (InterruptedException e) {
/*  72 */       bs.cancel();
/*  73 */       subscriber.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void subscribe(Publisher<? extends T> o) {
/*  83 */     BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
/*  84 */     LambdaSubscriber<T> ls = new LambdaSubscriber(Functions.emptyConsumer(), (Consumer)callback, (Action)callback, Functions.REQUEST_MAX);
/*     */ 
/*     */     
/*  87 */     o.subscribe((Subscriber)ls);
/*     */     
/*  89 */     BlockingHelper.awaitForComplete((CountDownLatch)callback, (Disposable)ls);
/*  90 */     Throwable e = callback.error;
/*  91 */     if (e != null) {
/*  92 */       throw ExceptionHelper.wrapOrThrow(e);
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
/*     */   public static <T> void subscribe(Publisher<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/* 106 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 107 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 108 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 109 */     subscribe(o, (Subscriber<? super T>)new LambdaSubscriber(onNext, onError, onComplete, Functions.REQUEST_MAX));
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
/*     */   
/*     */   public static <T> void subscribe(Publisher<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, int bufferSize) {
/* 123 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 124 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 125 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 126 */     ObjectHelper.verifyPositive(bufferSize, "number > 0 required");
/* 127 */     subscribe(o, (Subscriber<? super T>)new BoundedSubscriber(onNext, onError, onComplete, Functions.boundedConsumer(bufferSize), bufferSize));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBlockingSubscribe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */