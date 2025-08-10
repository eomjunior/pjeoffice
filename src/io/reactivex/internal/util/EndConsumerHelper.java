/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.ProtocolViolationException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EndConsumerHelper
/*     */ {
/*     */   private EndConsumerHelper() {
/*  38 */     throw new IllegalStateException("No instances!");
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
/*     */   public static boolean validate(Disposable upstream, Disposable next, Class<?> observer) {
/*  53 */     ObjectHelper.requireNonNull(next, "next is null");
/*  54 */     if (upstream != null) {
/*  55 */       next.dispose();
/*  56 */       if (upstream != DisposableHelper.DISPOSED) {
/*  57 */         reportDoubleSubscription(observer);
/*     */       }
/*  59 */       return false;
/*     */     } 
/*  61 */     return true;
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
/*     */   public static boolean setOnce(AtomicReference<Disposable> upstream, Disposable next, Class<?> observer) {
/*  75 */     ObjectHelper.requireNonNull(next, "next is null");
/*  76 */     if (!upstream.compareAndSet(null, next)) {
/*  77 */       next.dispose();
/*  78 */       if (upstream.get() != DisposableHelper.DISPOSED) {
/*  79 */         reportDoubleSubscription(observer);
/*     */       }
/*  81 */       return false;
/*     */     } 
/*  83 */     return true;
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
/*     */   public static boolean validate(Subscription upstream, Subscription next, Class<?> subscriber) {
/*  98 */     ObjectHelper.requireNonNull(next, "next is null");
/*  99 */     if (upstream != null) {
/* 100 */       next.cancel();
/* 101 */       if (upstream != SubscriptionHelper.CANCELLED) {
/* 102 */         reportDoubleSubscription(subscriber);
/*     */       }
/* 104 */       return false;
/*     */     } 
/* 106 */     return true;
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
/*     */   public static boolean setOnce(AtomicReference<Subscription> upstream, Subscription next, Class<?> subscriber) {
/* 120 */     ObjectHelper.requireNonNull(next, "next is null");
/* 121 */     if (!upstream.compareAndSet(null, next)) {
/* 122 */       next.cancel();
/* 123 */       if (upstream.get() != SubscriptionHelper.CANCELLED) {
/* 124 */         reportDoubleSubscription(subscriber);
/*     */       }
/* 126 */       return false;
/*     */     } 
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String composeMessage(String consumer) {
/* 137 */     return "It is not allowed to subscribe with a(n) " + consumer + " multiple times. Please create a fresh instance of " + consumer + " and subscribe that to the target source instead.";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reportDoubleSubscription(Class<?> consumer) {
/* 148 */     RxJavaPlugins.onError((Throwable)new ProtocolViolationException(composeMessage(consumer.getName())));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/EndConsumerHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */