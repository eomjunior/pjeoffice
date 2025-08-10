/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(@ParametricNullness T value) {
/*  39 */     return () -> value;
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <T> AsyncCallable<T> asAsyncCallable(Callable<T> callable, ListeningExecutorService listeningExecutorService) {
/*  54 */     Preconditions.checkNotNull(callable);
/*  55 */     Preconditions.checkNotNull(listeningExecutorService);
/*  56 */     return () -> listeningExecutorService.submit(callable);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   static <T> Callable<T> threadRenaming(Callable<T> callable, Supplier<String> nameSupplier) {
/*  71 */     Preconditions.checkNotNull(nameSupplier);
/*  72 */     Preconditions.checkNotNull(callable);
/*  73 */     return () -> {
/*     */         Thread currentThread = Thread.currentThread();
/*     */         String oldName = currentThread.getName();
/*     */         boolean restoreName = trySetName((String)nameSupplier.get(), currentThread);
/*     */         try {
/*     */           return callable.call();
/*     */         } finally {
/*     */           if (restoreName) {
/*     */             boolean bool = trySetName(oldName, currentThread);
/*     */           }
/*     */         } 
/*     */       };
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   static Runnable threadRenaming(Runnable task, Supplier<String> nameSupplier) {
/*  98 */     Preconditions.checkNotNull(nameSupplier);
/*  99 */     Preconditions.checkNotNull(task);
/* 100 */     return () -> {
/*     */         Thread currentThread = Thread.currentThread();
/*     */         String oldName = currentThread.getName();
/*     */         boolean restoreName = trySetName((String)nameSupplier.get(), currentThread);
/*     */         try {
/*     */           task.run();
/*     */         } finally {
/*     */           if (restoreName) {
/*     */             boolean bool = trySetName(oldName, currentThread);
/*     */           }
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static boolean trySetName(String threadName, Thread currentThread) {
/*     */     try {
/* 123 */       currentThread.setName(threadName);
/* 124 */       return true;
/* 125 */     } catch (SecurityException e) {
/* 126 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/Callables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */