/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EventBus
/*     */ {
/* 156 */   private static final Logger logger = Logger.getLogger(EventBus.class.getName());
/*     */   
/*     */   private final String identifier;
/*     */   
/*     */   private final Executor executor;
/*     */   private final SubscriberExceptionHandler exceptionHandler;
/* 162 */   private final SubscriberRegistry subscribers = new SubscriberRegistry(this);
/*     */   
/*     */   private final Dispatcher dispatcher;
/*     */   
/*     */   public EventBus() {
/* 167 */     this("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(String identifier) {
/* 177 */     this(identifier, 
/*     */         
/* 179 */         MoreExecutors.directExecutor(), 
/* 180 */         Dispatcher.perThreadDispatchQueue(), LoggingHandler.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(SubscriberExceptionHandler exceptionHandler) {
/* 191 */     this("default", 
/*     */         
/* 193 */         MoreExecutors.directExecutor(), 
/* 194 */         Dispatcher.perThreadDispatchQueue(), exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventBus(String identifier, Executor executor, Dispatcher dispatcher, SubscriberExceptionHandler exceptionHandler) {
/* 203 */     this.identifier = (String)Preconditions.checkNotNull(identifier);
/* 204 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/* 205 */     this.dispatcher = (Dispatcher)Preconditions.checkNotNull(dispatcher);
/* 206 */     this.exceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String identifier() {
/* 215 */     return this.identifier;
/*     */   }
/*     */ 
/*     */   
/*     */   final Executor executor() {
/* 220 */     return this.executor;
/*     */   }
/*     */ 
/*     */   
/*     */   void handleSubscriberException(Throwable e, SubscriberExceptionContext context) {
/* 225 */     Preconditions.checkNotNull(e);
/* 226 */     Preconditions.checkNotNull(context);
/*     */     try {
/* 228 */       this.exceptionHandler.handleException(e, context);
/* 229 */     } catch (Throwable e2) {
/*     */       
/* 231 */       logger.log(Level.SEVERE, 
/*     */           
/* 233 */           String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", new Object[] { e2, e }), e2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(Object object) {
/* 244 */     this.subscribers.register(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Object object) {
/* 254 */     this.subscribers.unregister(object);
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
/*     */   public void post(Object event) {
/* 268 */     Iterator<Subscriber> eventSubscribers = this.subscribers.getSubscribers(event);
/* 269 */     if (eventSubscribers.hasNext()) {
/* 270 */       this.dispatcher.dispatch(event, eventSubscribers);
/* 271 */     } else if (!(event instanceof DeadEvent)) {
/*     */       
/* 273 */       post(new DeadEvent(this, event));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     return MoreObjects.toStringHelper(this).addValue(this.identifier).toString();
/*     */   }
/*     */   
/*     */   static final class LoggingHandler
/*     */     implements SubscriberExceptionHandler {
/* 284 */     static final LoggingHandler INSTANCE = new LoggingHandler();
/*     */ 
/*     */     
/*     */     public void handleException(Throwable exception, SubscriberExceptionContext context) {
/* 288 */       Logger logger = logger(context);
/* 289 */       if (logger.isLoggable(Level.SEVERE)) {
/* 290 */         logger.log(Level.SEVERE, message(context), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Logger logger(SubscriberExceptionContext context) {
/* 295 */       return Logger.getLogger(EventBus.class.getName() + "." + context.getEventBus().identifier());
/*     */     }
/*     */     
/*     */     private static String message(SubscriberExceptionContext context) {
/* 299 */       Method method = context.getSubscriberMethod();
/* 300 */       return "Exception thrown by subscriber method " + method
/* 301 */         .getName() + '(' + method
/*     */         
/* 303 */         .getParameterTypes()[0].getName() + ')' + " on subscriber " + context
/*     */ 
/*     */         
/* 306 */         .getSubscriber() + " when dispatching event: " + context
/*     */         
/* 308 */         .getEvent();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/EventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */