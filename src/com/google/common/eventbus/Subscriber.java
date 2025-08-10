/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ class Subscriber
/*     */ {
/*     */   @Weak
/*     */   private EventBus bus;
/*     */   @VisibleForTesting
/*     */   final Object target;
/*     */   private final Method method;
/*     */   private final Executor executor;
/*     */   
/*     */   static Subscriber create(EventBus bus, Object listener, Method method) {
/*  40 */     return isDeclaredThreadSafe(method) ? 
/*  41 */       new Subscriber(bus, listener, method) : 
/*  42 */       new SynchronizedSubscriber(bus, listener, method);
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
/*     */   private Subscriber(EventBus bus, Object target, Method method) {
/*  58 */     this.bus = bus;
/*  59 */     this.target = Preconditions.checkNotNull(target);
/*  60 */     this.method = method;
/*  61 */     method.setAccessible(true);
/*     */     
/*  63 */     this.executor = bus.executor();
/*     */   }
/*     */ 
/*     */   
/*     */   final void dispatchEvent(Object event) {
/*  68 */     this.executor.execute(() -> {
/*     */           
/*     */           try {
/*     */             invokeSubscriberMethod(event);
/*  72 */           } catch (InvocationTargetException e) {
/*     */             this.bus.handleSubscriberException(e.getCause(), context(event));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/*     */     try {
/*  85 */       this.method.invoke(this.target, new Object[] { Preconditions.checkNotNull(event) });
/*  86 */     } catch (IllegalArgumentException e) {
/*  87 */       throw new Error("Method rejected target/argument: " + event, e);
/*  88 */     } catch (IllegalAccessException e) {
/*  89 */       throw new Error("Method became inaccessible: " + event, e);
/*  90 */     } catch (InvocationTargetException e) {
/*  91 */       if (e.getCause() instanceof Error) {
/*  92 */         throw (Error)e.getCause();
/*     */       }
/*  94 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private SubscriberExceptionContext context(Object event) {
/* 100 */     return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 105 */     return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(@CheckForNull Object obj) {
/* 110 */     if (obj instanceof Subscriber) {
/* 111 */       Subscriber that = (Subscriber)obj;
/*     */ 
/*     */ 
/*     */       
/* 115 */       return (this.target == that.target && this.method.equals(that.method));
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDeclaredThreadSafe(Method method) {
/* 125 */     return (method.getAnnotation(AllowConcurrentEvents.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SynchronizedSubscriber
/*     */     extends Subscriber
/*     */   {
/*     */     private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
/* 136 */       super(bus, target, method);
/*     */     }
/*     */ 
/*     */     
/*     */     void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/* 141 */       synchronized (this) {
/* 142 */         super.invokeSubscriberMethod(event);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/Subscriber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */