/*     */ package io.reactivex.flowables;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.BackpressureKind;
/*     */ import io.reactivex.annotations.BackpressureSupport;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.SchedulerSupport;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.operators.flowable.FlowableAutoConnect;
/*     */ import io.reactivex.internal.operators.flowable.FlowablePublishAlt;
/*     */ import io.reactivex.internal.operators.flowable.FlowablePublishClassic;
/*     */ import io.reactivex.internal.operators.flowable.FlowableRefCount;
/*     */ import io.reactivex.internal.util.ConnectConsumer;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.schedulers.Schedulers;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConnectableFlowable<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   public abstract void connect(@NonNull Consumer<? super Disposable> paramConsumer);
/*     */   
/*     */   public final Disposable connect() {
/*  66 */     ConnectConsumer cc = new ConnectConsumer();
/*  67 */     connect((Consumer<? super Disposable>)cc);
/*  68 */     return cc.disposable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConnectableFlowable<T> onRefCount() {
/*  79 */     if (this instanceof FlowablePublishClassic) {
/*     */       
/*  81 */       FlowablePublishClassic<T> fp = (FlowablePublishClassic<T>)this;
/*  82 */       return RxJavaPlugins.onAssembly((ConnectableFlowable)new FlowablePublishAlt(fp
/*  83 */             .publishSource(), fp.publishBufferSize()));
/*     */     } 
/*     */     
/*  86 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("none")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   @NonNull
/*     */   public Flowable<T> refCount() {
/* 110 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRefCount(onRefCount()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("none")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   public final Flowable<T> refCount(int subscriberCount) {
/* 132 */     return refCount(subscriberCount, 0L, TimeUnit.NANOSECONDS, Schedulers.trampoline());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("io.reactivex:computation")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   public final Flowable<T> refCount(long timeout, TimeUnit unit) {
/* 157 */     return refCount(1, timeout, unit, Schedulers.computation());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("custom")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   public final Flowable<T> refCount(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 182 */     return refCount(1, timeout, unit, scheduler);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("io.reactivex:computation")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   public final Flowable<T> refCount(int subscriberCount, long timeout, TimeUnit unit) {
/* 208 */     return refCount(subscriberCount, timeout, unit, Schedulers.computation());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("custom")
/*     */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*     */   public final Flowable<T> refCount(int subscriberCount, long timeout, TimeUnit unit, Scheduler scheduler) {
/* 234 */     ObjectHelper.verifyPositive(subscriberCount, "subscriberCount");
/* 235 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 236 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 237 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRefCount(onRefCount(), subscriberCount, timeout, unit, scheduler));
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
/*     */   @NonNull
/*     */   public Flowable<T> autoConnect() {
/* 263 */     return autoConnect(1);
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
/*     */   @NonNull
/*     */   public Flowable<T> autoConnect(int numberOfSubscribers) {
/* 289 */     return autoConnect(numberOfSubscribers, Functions.emptyConsumer());
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
/*     */   @NonNull
/*     */   public Flowable<T> autoConnect(int numberOfSubscribers, @NonNull Consumer<? super Disposable> connection) {
/* 316 */     if (numberOfSubscribers <= 0) {
/* 317 */       connect(connection);
/* 318 */       return RxJavaPlugins.onAssembly(this);
/*     */     } 
/* 320 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableAutoConnect(this, numberOfSubscribers, connection));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/flowables/ConnectableFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */