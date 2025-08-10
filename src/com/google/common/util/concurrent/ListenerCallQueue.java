/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class ListenerCallQueue<L>
/*     */ {
/*  60 */   private static final LazyLogger logger = new LazyLogger(ListenerCallQueue.class);
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final List<PerListenerQueue<L>> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(L listener, Executor executor) {
/*  77 */     Preconditions.checkNotNull(listener, "listener");
/*  78 */     Preconditions.checkNotNull(executor, "executor");
/*  79 */     this.listeners.add(new PerListenerQueue<>(listener, executor));
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
/*     */   public void enqueue(Event<L> event) {
/*  91 */     enqueueHelper(event, event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueue(Event<L> event, String label) {
/* 101 */     enqueueHelper(event, label);
/*     */   }
/*     */   
/*     */   private void enqueueHelper(Event<L> event, Object label) {
/* 105 */     Preconditions.checkNotNull(event, "event");
/* 106 */     Preconditions.checkNotNull(label, "label");
/* 107 */     synchronized (this.listeners) {
/* 108 */       for (PerListenerQueue<L> queue : this.listeners) {
/* 109 */         queue.add(event, label);
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
/*     */   public void dispatch() {
/* 121 */     for (int i = 0; i < this.listeners.size(); i++) {
/* 122 */       ((PerListenerQueue)this.listeners.get(i)).dispatch();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PerListenerQueue<L>
/*     */     implements Runnable
/*     */   {
/*     */     final L listener;
/*     */ 
/*     */     
/*     */     final Executor executor;
/*     */ 
/*     */     
/*     */     @GuardedBy("this")
/* 138 */     final Queue<ListenerCallQueue.Event<L>> waitQueue = Queues.newArrayDeque();
/*     */     
/*     */     @GuardedBy("this")
/* 141 */     final Queue<Object> labelQueue = Queues.newArrayDeque();
/*     */     
/*     */     @GuardedBy("this")
/*     */     boolean isThreadScheduled;
/*     */     
/*     */     PerListenerQueue(L listener, Executor executor) {
/* 147 */       this.listener = (L)Preconditions.checkNotNull(listener);
/* 148 */       this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */     }
/*     */ 
/*     */     
/*     */     synchronized void add(ListenerCallQueue.Event<L> event, Object label) {
/* 153 */       this.waitQueue.add(event);
/* 154 */       this.labelQueue.add(label);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void dispatch() {
/* 163 */       boolean scheduleEventRunner = false;
/* 164 */       synchronized (this) {
/* 165 */         if (!this.isThreadScheduled) {
/* 166 */           this.isThreadScheduled = true;
/* 167 */           scheduleEventRunner = true;
/*     */         } 
/*     */       } 
/* 170 */       if (scheduleEventRunner) {
/*     */         try {
/* 172 */           this.executor.execute(this);
/* 173 */         } catch (Exception e) {
/*     */           
/* 175 */           synchronized (this) {
/* 176 */             this.isThreadScheduled = false;
/*     */           } 
/*     */           
/* 179 */           ListenerCallQueue.logger
/* 180 */             .get()
/* 181 */             .log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
/*     */ 
/*     */ 
/*     */           
/* 185 */           throw e;
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 193 */       boolean stillRunning = true;
/*     */       try {
/*     */         while (true) {
/*     */           ListenerCallQueue.Event<L> nextToRun;
/*     */           Object nextLabel;
/* 198 */           synchronized (this) {
/* 199 */             Preconditions.checkState(this.isThreadScheduled);
/* 200 */             nextToRun = this.waitQueue.poll();
/* 201 */             nextLabel = this.labelQueue.poll();
/* 202 */             if (nextToRun == null) {
/* 203 */               this.isThreadScheduled = false;
/* 204 */               stillRunning = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */           
/*     */           try {
/* 211 */             nextToRun.call(this.listener);
/* 212 */           } catch (Exception e) {
/*     */             
/* 214 */             ListenerCallQueue.logger
/* 215 */               .get()
/* 216 */               .log(Level.SEVERE, "Exception while executing callback: " + this.listener + " " + nextLabel, e);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 223 */         if (stillRunning)
/*     */         {
/*     */           
/* 226 */           synchronized (this) {
/* 227 */             this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static interface Event<L> {
/*     */     void call(L param1L);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ListenerCallQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */