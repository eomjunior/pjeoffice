/*     */ package io.reactivex.observables;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.SchedulerSupport;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.operators.observable.ObservableAutoConnect;
/*     */ import io.reactivex.internal.operators.observable.ObservablePublishAlt;
/*     */ import io.reactivex.internal.operators.observable.ObservablePublishClassic;
/*     */ import io.reactivex.internal.operators.observable.ObservableRefCount;
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
/*     */ public abstract class ConnectableObservable<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   public abstract void connect(@NonNull Consumer<? super Disposable> paramConsumer);
/*     */   
/*     */   public final Disposable connect() {
/*  64 */     ConnectConsumer cc = new ConnectConsumer();
/*  65 */     connect((Consumer<? super Disposable>)cc);
/*  66 */     return cc.disposable;
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
/*     */   private ConnectableObservable<T> onRefCount() {
/*  78 */     if (this instanceof ObservablePublishClassic) {
/*  79 */       return RxJavaPlugins.onAssembly((ConnectableObservable)new ObservablePublishAlt(((ObservablePublishClassic)this)
/*  80 */             .publishSource()));
/*     */     }
/*     */     
/*  83 */     return this;
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
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("none")
/*     */   @NonNull
/*     */   public Observable<T> refCount() {
/* 103 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRefCount(onRefCount()));
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
/*     */   @CheckReturnValue
/*     */   @SchedulerSupport("none")
/*     */   public final Observable<T> refCount(int subscriberCount) {
/* 121 */     return refCount(subscriberCount, 0L, TimeUnit.NANOSECONDS, Schedulers.trampoline());
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
/*     */   @SchedulerSupport("io.reactivex:computation")
/*     */   public final Observable<T> refCount(long timeout, TimeUnit unit) {
/* 142 */     return refCount(1, timeout, unit, Schedulers.computation());
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
/*     */   @SchedulerSupport("custom")
/*     */   public final Observable<T> refCount(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 163 */     return refCount(1, timeout, unit, scheduler);
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
/*     */   @SchedulerSupport("io.reactivex:computation")
/*     */   public final Observable<T> refCount(int subscriberCount, long timeout, TimeUnit unit) {
/* 185 */     return refCount(subscriberCount, timeout, unit, Schedulers.computation());
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
/*     */   @SchedulerSupport("custom")
/*     */   public final Observable<T> refCount(int subscriberCount, long timeout, TimeUnit unit, Scheduler scheduler) {
/* 207 */     ObjectHelper.verifyPositive(subscriberCount, "subscriberCount");
/* 208 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 209 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 210 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRefCount(onRefCount(), subscriberCount, timeout, unit, scheduler));
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
/*     */   @NonNull
/*     */   public Observable<T> autoConnect() {
/* 234 */     return autoConnect(1);
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
/*     */   public Observable<T> autoConnect(int numberOfSubscribers) {
/* 261 */     return autoConnect(numberOfSubscribers, Functions.emptyConsumer());
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
/*     */   public Observable<T> autoConnect(int numberOfSubscribers, @NonNull Consumer<? super Disposable> connection) {
/* 288 */     if (numberOfSubscribers <= 0) {
/* 289 */       connect(connection);
/* 290 */       return RxJavaPlugins.onAssembly(this);
/*     */     } 
/* 292 */     return RxJavaPlugins.onAssembly((Observable)new ObservableAutoConnect(this, numberOfSubscribers, connection));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observables/ConnectableObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */