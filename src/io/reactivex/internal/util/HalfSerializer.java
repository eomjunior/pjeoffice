/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HalfSerializer
/*     */ {
/*     */   private HalfSerializer() {
/*  30 */     throw new IllegalStateException("No instances!");
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
/*     */   public static <T> void onNext(Subscriber<? super T> subscriber, T value, AtomicInteger wip, AtomicThrowable error) {
/*  44 */     if (wip.get() == 0 && wip.compareAndSet(0, 1)) {
/*  45 */       subscriber.onNext(value);
/*  46 */       if (wip.decrementAndGet() != 0) {
/*  47 */         Throwable ex = error.terminate();
/*  48 */         if (ex != null) {
/*  49 */           subscriber.onError(ex);
/*     */         } else {
/*  51 */           subscriber.onComplete();
/*     */         } 
/*     */       } 
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
/*     */   
/*     */   public static void onError(Subscriber<?> subscriber, Throwable ex, AtomicInteger wip, AtomicThrowable error) {
/*  68 */     if (error.addThrowable(ex)) {
/*  69 */       if (wip.getAndIncrement() == 0) {
/*  70 */         subscriber.onError(error.terminate());
/*     */       }
/*     */     } else {
/*  73 */       RxJavaPlugins.onError(ex);
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
/*     */   public static void onComplete(Subscriber<?> subscriber, AtomicInteger wip, AtomicThrowable error) {
/*  85 */     if (wip.getAndIncrement() == 0) {
/*  86 */       Throwable ex = error.terminate();
/*  87 */       if (ex != null) {
/*  88 */         subscriber.onError(ex);
/*     */       } else {
/*  90 */         subscriber.onComplete();
/*     */       } 
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
/*     */   
/*     */   public static <T> void onNext(Observer<? super T> observer, T value, AtomicInteger wip, AtomicThrowable error) {
/* 106 */     if (wip.get() == 0 && wip.compareAndSet(0, 1)) {
/* 107 */       observer.onNext(value);
/* 108 */       if (wip.decrementAndGet() != 0) {
/* 109 */         Throwable ex = error.terminate();
/* 110 */         if (ex != null) {
/* 111 */           observer.onError(ex);
/*     */         } else {
/* 113 */           observer.onComplete();
/*     */         } 
/*     */       } 
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
/*     */   
/*     */   public static void onError(Observer<?> observer, Throwable ex, AtomicInteger wip, AtomicThrowable error) {
/* 130 */     if (error.addThrowable(ex)) {
/* 131 */       if (wip.getAndIncrement() == 0) {
/* 132 */         observer.onError(error.terminate());
/*     */       }
/*     */     } else {
/* 135 */       RxJavaPlugins.onError(ex);
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
/*     */   public static void onComplete(Observer<?> observer, AtomicInteger wip, AtomicThrowable error) {
/* 147 */     if (wip.getAndIncrement() == 0) {
/* 148 */       Throwable ex = error.terminate();
/* 149 */       if (ex != null) {
/* 150 */         observer.onError(ex);
/*     */       } else {
/* 152 */         observer.onComplete();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/HalfSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */