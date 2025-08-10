/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import java.util.concurrent.Executor;
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
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ public class AsyncEventBus
/*    */   extends EventBus
/*    */ {
/*    */   public AsyncEventBus(String identifier, Executor executor) {
/* 38 */     super(identifier, executor, Dispatcher.legacyAsync(), EventBus.LoggingHandler.INSTANCE);
/*    */   }
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
/*    */   public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
/* 51 */     super("default", executor, Dispatcher.legacyAsync(), subscriberExceptionHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncEventBus(Executor executor) {
/* 61 */     super("default", executor, Dispatcher.legacyAsync(), EventBus.LoggingHandler.INSTANCE);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/AsyncEventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */