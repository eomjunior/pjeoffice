/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ExecutionList
/*     */ {
/*  47 */   private static final LazyLogger log = new LazyLogger(ExecutionList.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @GuardedBy("this")
/*     */   private RunnableExecutorPair runnables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean executed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Runnable runnable, Executor executor) {
/*  74 */     Preconditions.checkNotNull(runnable, "Runnable was null.");
/*  75 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     synchronized (this) {
/*  81 */       if (!this.executed) {
/*  82 */         this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  90 */     executeListener(runnable, executor);
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
/*     */   public void execute() {
/*     */     RunnableExecutorPair list;
/* 108 */     synchronized (this) {
/* 109 */       if (this.executed) {
/*     */         return;
/*     */       }
/* 112 */       this.executed = true;
/* 113 */       list = this.runnables;
/* 114 */       this.runnables = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     RunnableExecutorPair reversedList = null;
/* 126 */     while (list != null) {
/* 127 */       RunnableExecutorPair tmp = list;
/* 128 */       list = list.next;
/* 129 */       tmp.next = reversedList;
/* 130 */       reversedList = tmp;
/*     */     } 
/* 132 */     while (reversedList != null) {
/* 133 */       executeListener(reversedList.runnable, reversedList.executor);
/* 134 */       reversedList = reversedList.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void executeListener(Runnable runnable, Executor executor) {
/*     */     try {
/* 145 */       executor.execute(runnable);
/* 146 */     } catch (Exception e) {
/*     */ 
/*     */ 
/*     */       
/* 150 */       log.get()
/* 151 */         .log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RunnableExecutorPair
/*     */   {
/*     */     final Runnable runnable;
/*     */     
/*     */     final Executor executor;
/*     */     
/*     */     @CheckForNull
/*     */     RunnableExecutorPair next;
/*     */ 
/*     */     
/*     */     RunnableExecutorPair(Runnable runnable, Executor executor, @CheckForNull RunnableExecutorPair next) {
/* 168 */       this.runnable = runnable;
/* 169 */       this.executor = executor;
/* 170 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ExecutionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */