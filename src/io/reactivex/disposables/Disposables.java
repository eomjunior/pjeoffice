/*     */ package io.reactivex.disposables;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.Future;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Disposables
/*     */ {
/*     */   private Disposables() {
/*  32 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable fromRunnable(@NonNull Runnable run) {
/*  43 */     ObjectHelper.requireNonNull(run, "run is null");
/*  44 */     return new RunnableDisposable(run);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable fromAction(@NonNull Action run) {
/*  55 */     ObjectHelper.requireNonNull(run, "run is null");
/*  56 */     return new ActionDisposable(run);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable fromFuture(@NonNull Future<?> future) {
/*  67 */     ObjectHelper.requireNonNull(future, "future is null");
/*  68 */     return fromFuture(future, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable fromFuture(@NonNull Future<?> future, boolean allowInterrupt) {
/*  80 */     ObjectHelper.requireNonNull(future, "future is null");
/*  81 */     return new FutureDisposable(future, allowInterrupt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable fromSubscription(@NonNull Subscription subscription) {
/*  92 */     ObjectHelper.requireNonNull(subscription, "subscription is null");
/*  93 */     return new SubscriptionDisposable(subscription);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable empty() {
/* 102 */     return fromRunnable(Functions.EMPTY_RUNNABLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Disposable disposed() {
/* 111 */     return (Disposable)EmptyDisposable.INSTANCE;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/Disposables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */