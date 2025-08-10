/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
/*     */ import java.util.concurrent.Callable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableMapNotification<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends R> onNextMapper;
/*     */   final Function<? super Throwable, ? extends R> onErrorMapper;
/*     */   final Callable<? extends R> onCompleteSupplier;
/*     */   
/*     */   public FlowableMapNotification(Flowable<T> source, Function<? super T, ? extends R> onNextMapper, Function<? super Throwable, ? extends R> onErrorMapper, Callable<? extends R> onCompleteSupplier) {
/*  37 */     super(source);
/*  38 */     this.onNextMapper = onNextMapper;
/*  39 */     this.onErrorMapper = onErrorMapper;
/*  40 */     this.onCompleteSupplier = onCompleteSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  45 */     this.source.subscribe((FlowableSubscriber)new MapNotificationSubscriber<T, R>(s, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MapNotificationSubscriber<T, R>
/*     */     extends SinglePostCompleteSubscriber<T, R>
/*     */   {
/*     */     private static final long serialVersionUID = 2757120512858778108L;
/*     */     
/*     */     final Function<? super T, ? extends R> onNextMapper;
/*     */     
/*     */     final Function<? super Throwable, ? extends R> onErrorMapper;
/*     */     final Callable<? extends R> onCompleteSupplier;
/*     */     
/*     */     MapNotificationSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends R> onNextMapper, Function<? super Throwable, ? extends R> onErrorMapper, Callable<? extends R> onCompleteSupplier) {
/*  60 */       super(actual);
/*  61 */       this.onNextMapper = onNextMapper;
/*  62 */       this.onErrorMapper = onErrorMapper;
/*  63 */       this.onCompleteSupplier = onCompleteSupplier;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       R p;
/*     */       try {
/*  71 */         p = (R)ObjectHelper.requireNonNull(this.onNextMapper.apply(t), "The onNext publisher returned is null");
/*  72 */       } catch (Throwable e) {
/*  73 */         Exceptions.throwIfFatal(e);
/*  74 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  78 */       this.produced++;
/*  79 */       this.downstream.onNext(p);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       R p;
/*     */       try {
/*  87 */         p = (R)ObjectHelper.requireNonNull(this.onErrorMapper.apply(t), "The onError publisher returned is null");
/*  88 */       } catch (Throwable e) {
/*  89 */         Exceptions.throwIfFatal(e);
/*  90 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         
/*     */         return;
/*     */       } 
/*  94 */       complete(p);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       R p;
/*     */       try {
/* 102 */         p = (R)ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onComplete publisher returned is null");
/* 103 */       } catch (Throwable e) {
/* 104 */         Exceptions.throwIfFatal(e);
/* 105 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 109 */       complete(p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMapNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */