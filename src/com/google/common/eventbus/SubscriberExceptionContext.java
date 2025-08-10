/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ public class SubscriberExceptionContext
/*    */ {
/*    */   private final EventBus eventBus;
/*    */   private final Object event;
/*    */   private final Object subscriber;
/*    */   private final Method subscriberMethod;
/*    */   
/*    */   SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
/* 42 */     this.eventBus = (EventBus)Preconditions.checkNotNull(eventBus);
/* 43 */     this.event = Preconditions.checkNotNull(event);
/* 44 */     this.subscriber = Preconditions.checkNotNull(subscriber);
/* 45 */     this.subscriberMethod = (Method)Preconditions.checkNotNull(subscriberMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EventBus getEventBus() {
/* 53 */     return this.eventBus;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getEvent() {
/* 58 */     return this.event;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getSubscriber() {
/* 63 */     return this.subscriber;
/*    */   }
/*    */ 
/*    */   
/*    */   public Method getSubscriberMethod() {
/* 68 */     return this.subscriberMethod;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/SubscriberExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */