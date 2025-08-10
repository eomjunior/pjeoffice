/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.io.Serializable;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public enum NotificationLite
/*     */ {
/*  27 */   COMPLETE;
/*     */ 
/*     */   
/*     */   static final class ErrorNotification
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8759979445933046293L;
/*     */     
/*     */     final Throwable e;
/*     */     
/*     */     ErrorNotification(Throwable e) {
/*  38 */       this.e = e;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  43 */       return "NotificationLite.Error[" + this.e + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  48 */       return this.e.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  53 */       if (obj instanceof ErrorNotification) {
/*  54 */         ErrorNotification n = (ErrorNotification)obj;
/*  55 */         return ObjectHelper.equals(this.e, n.e);
/*     */       } 
/*  57 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SubscriptionNotification
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -1322257508628817540L;
/*     */     final Subscription upstream;
/*     */     
/*     */     SubscriptionNotification(Subscription s) {
/*  69 */       this.upstream = s;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  74 */       return "NotificationLite.Subscription[" + this.upstream + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DisposableNotification
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7482590109178395495L;
/*     */     
/*     */     final Disposable upstream;
/*     */     
/*     */     DisposableNotification(Disposable d) {
/*  87 */       this.upstream = d;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  92 */       return "NotificationLite.Disposable[" + this.upstream + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Object next(T value) {
/* 103 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object complete() {
/* 111 */     return COMPLETE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object error(Throwable e) {
/* 120 */     return new ErrorNotification(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object subscription(Subscription s) {
/* 129 */     return new SubscriptionNotification(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object disposable(Disposable d) {
/* 138 */     return new DisposableNotification(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isComplete(Object o) {
/* 147 */     return (o == COMPLETE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isError(Object o) {
/* 156 */     return o instanceof ErrorNotification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSubscription(Object o) {
/* 165 */     return o instanceof SubscriptionNotification;
/*     */   }
/*     */   
/*     */   public static boolean isDisposable(Object o) {
/* 169 */     return o instanceof DisposableNotification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getValue(Object o) {
/* 180 */     return (T)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Throwable getError(Object o) {
/* 189 */     return ((ErrorNotification)o).e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Subscription getSubscription(Object o) {
/* 198 */     return ((SubscriptionNotification)o).upstream;
/*     */   }
/*     */   
/*     */   public static Disposable getDisposable(Object o) {
/* 202 */     return ((DisposableNotification)o).upstream;
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
/*     */   public static <T> boolean accept(Object o, Subscriber<? super T> s) {
/* 216 */     if (o == COMPLETE) {
/* 217 */       s.onComplete();
/* 218 */       return true;
/*     */     } 
/* 220 */     if (o instanceof ErrorNotification) {
/* 221 */       s.onError(((ErrorNotification)o).e);
/* 222 */       return true;
/*     */     } 
/* 224 */     s.onNext(o);
/* 225 */     return false;
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
/*     */   public static <T> boolean accept(Object o, Observer<? super T> observer) {
/* 238 */     if (o == COMPLETE) {
/* 239 */       observer.onComplete();
/* 240 */       return true;
/*     */     } 
/* 242 */     if (o instanceof ErrorNotification) {
/* 243 */       observer.onError(((ErrorNotification)o).e);
/* 244 */       return true;
/*     */     } 
/* 246 */     observer.onNext(o);
/* 247 */     return false;
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
/*     */   public static <T> boolean acceptFull(Object o, Subscriber<? super T> s) {
/* 260 */     if (o == COMPLETE) {
/* 261 */       s.onComplete();
/* 262 */       return true;
/*     */     } 
/* 264 */     if (o instanceof ErrorNotification) {
/* 265 */       s.onError(((ErrorNotification)o).e);
/* 266 */       return true;
/*     */     } 
/* 268 */     if (o instanceof SubscriptionNotification) {
/* 269 */       s.onSubscribe(((SubscriptionNotification)o).upstream);
/* 270 */       return false;
/*     */     } 
/* 272 */     s.onNext(o);
/* 273 */     return false;
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
/*     */   public static <T> boolean acceptFull(Object o, Observer<? super T> observer) {
/* 286 */     if (o == COMPLETE) {
/* 287 */       observer.onComplete();
/* 288 */       return true;
/*     */     } 
/* 290 */     if (o instanceof ErrorNotification) {
/* 291 */       observer.onError(((ErrorNotification)o).e);
/* 292 */       return true;
/*     */     } 
/* 294 */     if (o instanceof DisposableNotification) {
/* 295 */       observer.onSubscribe(((DisposableNotification)o).upstream);
/* 296 */       return false;
/*     */     } 
/* 298 */     observer.onNext(o);
/* 299 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 304 */     return "NotificationLite.Complete";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/NotificationLite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */