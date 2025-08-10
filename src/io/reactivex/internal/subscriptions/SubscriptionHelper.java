/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.exceptions.ProtocolViolationException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public enum SubscriptionHelper
/*     */   implements Subscription
/*     */ {
/*  33 */   CANCELLED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void request(long n) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validate(Subscription current, Subscription next) {
/*  54 */     if (next == null) {
/*  55 */       RxJavaPlugins.onError(new NullPointerException("next is null"));
/*  56 */       return false;
/*     */     } 
/*  58 */     if (current != null) {
/*  59 */       next.cancel();
/*  60 */       reportSubscriptionSet();
/*  61 */       return false;
/*     */     } 
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reportSubscriptionSet() {
/*  71 */     RxJavaPlugins.onError((Throwable)new ProtocolViolationException("Subscription already set!"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validate(long n) {
/*  80 */     if (n <= 0L) {
/*  81 */       RxJavaPlugins.onError(new IllegalArgumentException("n > 0 required but it was " + n));
/*  82 */       return false;
/*     */     } 
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reportMoreProduced(long n) {
/*  93 */     RxJavaPlugins.onError((Throwable)new ProtocolViolationException("More produced than requested: " + n));
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
/*     */   public static boolean set(AtomicReference<Subscription> field, Subscription s) {
/*     */     while (true) {
/* 107 */       Subscription current = field.get();
/* 108 */       if (current == CANCELLED) {
/* 109 */         if (s != null) {
/* 110 */           s.cancel();
/*     */         }
/* 112 */         return false;
/*     */       } 
/* 114 */       if (field.compareAndSet(current, s)) {
/* 115 */         if (current != null) {
/* 116 */           current.cancel();
/*     */         }
/* 118 */         return true;
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
/*     */   public static boolean setOnce(AtomicReference<Subscription> field, Subscription s) {
/* 132 */     ObjectHelper.requireNonNull(s, "s is null");
/* 133 */     if (!field.compareAndSet(null, s)) {
/* 134 */       s.cancel();
/* 135 */       if (field.get() != CANCELLED) {
/* 136 */         reportSubscriptionSet();
/*     */       }
/* 138 */       return false;
/*     */     } 
/* 140 */     return true;
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
/*     */   public static boolean replace(AtomicReference<Subscription> field, Subscription s) {
/*     */     while (true) {
/* 154 */       Subscription current = field.get();
/* 155 */       if (current == CANCELLED) {
/* 156 */         if (s != null) {
/* 157 */           s.cancel();
/*     */         }
/* 159 */         return false;
/*     */       } 
/* 161 */       if (field.compareAndSet(current, s)) {
/* 162 */         return true;
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
/*     */   public static boolean cancel(AtomicReference<Subscription> field) {
/* 176 */     Subscription current = field.get();
/* 177 */     if (current != CANCELLED) {
/* 178 */       current = field.getAndSet(CANCELLED);
/* 179 */       if (current != CANCELLED) {
/* 180 */         if (current != null) {
/* 181 */           current.cancel();
/*     */         }
/* 183 */         return true;
/*     */       } 
/*     */     } 
/* 186 */     return false;
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
/*     */   public static boolean deferredSetOnce(AtomicReference<Subscription> field, AtomicLong requested, Subscription s) {
/* 199 */     if (setOnce(field, s)) {
/* 200 */       long r = requested.getAndSet(0L);
/* 201 */       if (r != 0L) {
/* 202 */         s.request(r);
/*     */       }
/* 204 */       return true;
/*     */     } 
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deferredRequest(AtomicReference<Subscription> field, AtomicLong requested, long n) {
/* 217 */     Subscription s = field.get();
/* 218 */     if (s != null) {
/* 219 */       s.request(n);
/*     */     }
/* 221 */     else if (validate(n)) {
/* 222 */       BackpressureHelper.add(requested, n);
/*     */       
/* 224 */       s = field.get();
/* 225 */       if (s != null) {
/* 226 */         long r = requested.getAndSet(0L);
/* 227 */         if (r != 0L) {
/* 228 */           s.request(r);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean setOnce(AtomicReference<Subscription> field, Subscription s, long request) {
/* 248 */     if (setOnce(field, s)) {
/* 249 */       s.request(request);
/* 250 */       return true;
/*     */     } 
/* 252 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/SubscriptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */