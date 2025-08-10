/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class Dispatcher
/*     */ {
/*     */   static Dispatcher perThreadDispatchQueue() {
/*  49 */     return new PerThreadQueuedDispatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Dispatcher legacyAsync() {
/*  59 */     return new LegacyAsyncDispatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Dispatcher immediate() {
/*  68 */     return ImmediateDispatcher.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void dispatch(Object paramObject, Iterator<Subscriber> paramIterator);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PerThreadQueuedDispatcher
/*     */     extends Dispatcher
/*     */   {
/*  80 */     private final ThreadLocal<Queue<Event>> queue = new ThreadLocal<Queue<Event>>(this)
/*     */       {
/*     */         protected Queue<Dispatcher.PerThreadQueuedDispatcher.Event> initialValue()
/*     */         {
/*  84 */           return Queues.newArrayDeque();
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*  89 */     private final ThreadLocal<Boolean> dispatching = new ThreadLocal<Boolean>(this)
/*     */       {
/*     */         protected Boolean initialValue()
/*     */         {
/*  93 */           return Boolean.valueOf(false);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/*  99 */       Preconditions.checkNotNull(event);
/* 100 */       Preconditions.checkNotNull(subscribers);
/*     */       
/* 102 */       Queue<Event> queueForThread = Objects.<Queue<Event>>requireNonNull(this.queue.get());
/* 103 */       queueForThread.offer(new Event(event, subscribers));
/*     */       
/* 105 */       if (!((Boolean)this.dispatching.get()).booleanValue()) {
/* 106 */         this.dispatching.set(Boolean.valueOf(true));
/*     */         try {
/*     */           Event nextEvent;
/* 109 */           while ((nextEvent = queueForThread.poll()) != null) {
/* 110 */             while (nextEvent.subscribers.hasNext()) {
/* 111 */               ((Subscriber)nextEvent.subscribers.next()).dispatchEvent(nextEvent.event);
/*     */             }
/*     */           } 
/*     */         } finally {
/* 115 */           this.dispatching.remove();
/* 116 */           this.queue.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private PerThreadQueuedDispatcher() {}
/*     */     
/*     */     private static final class Event
/*     */     {
/*     */       private Event(Object event, Iterator<Subscriber> subscribers) {
/* 126 */         this.event = event;
/* 127 */         this.subscribers = subscribers;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private final Object event;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private final Iterator<Subscriber> subscribers;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LegacyAsyncDispatcher
/*     */     extends Dispatcher
/*     */   {
/* 155 */     private final ConcurrentLinkedQueue<EventWithSubscriber> queue = Queues.newConcurrentLinkedQueue();
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/* 159 */       Preconditions.checkNotNull(event);
/* 160 */       while (subscribers.hasNext()) {
/* 161 */         this.queue.add(new EventWithSubscriber(event, subscribers.next()));
/*     */       }
/*     */       
/*     */       EventWithSubscriber e;
/* 165 */       while ((e = this.queue.poll()) != null)
/* 166 */         e.subscriber.dispatchEvent(e.event); 
/*     */     }
/*     */     
/*     */     private LegacyAsyncDispatcher() {}
/*     */     
/*     */     private static final class EventWithSubscriber {
/*     */       private final Object event;
/*     */       
/*     */       private EventWithSubscriber(Object event, Subscriber subscriber) {
/* 175 */         this.event = event;
/* 176 */         this.subscriber = subscriber;
/*     */       }
/*     */       
/*     */       private final Subscriber subscriber; }
/*     */   }
/*     */   
/*     */   private static final class ImmediateDispatcher extends Dispatcher {
/* 183 */     private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/* 187 */       Preconditions.checkNotNull(event);
/* 188 */       while (subscribers.hasNext())
/* 189 */         ((Subscriber)subscribers.next()).dispatchEvent(event); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/Dispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */