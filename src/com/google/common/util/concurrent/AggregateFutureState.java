/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class AggregateFutureState<OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*     */   static {
/*     */     AtomicHelper helper;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*  47 */   private volatile Set<Throwable> seenExceptions = null;
/*     */   
/*     */   private volatile int remaining;
/*     */   
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*     */   
/*  53 */   private static final LazyLogger log = new LazyLogger(AggregateFutureState.class);
/*     */ 
/*     */   
/*     */   static {
/*  57 */     Throwable thrownReflectionFailure = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  62 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
/*  63 */     } catch (Throwable reflectionFailure) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  68 */       thrownReflectionFailure = reflectionFailure;
/*  69 */       helper = new SynchronizedAtomicHelper();
/*     */     } 
/*  71 */     ATOMIC_HELPER = helper;
/*     */ 
/*     */     
/*  74 */     if (thrownReflectionFailure != null) {
/*  75 */       log.get().log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownReflectionFailure);
/*     */     }
/*     */   }
/*     */   
/*     */   AggregateFutureState(int remainingFutures) {
/*  80 */     this.remaining = remainingFutures;
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
/*     */ 
/*     */ 
/*     */   
/*     */   final Set<Throwable> getOrInitSeenExceptions() {
/* 100 */     Set<Throwable> seenExceptionsLocal = this.seenExceptions;
/* 101 */     if (seenExceptionsLocal == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       seenExceptionsLocal = Sets.newConcurrentHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       addInitialException(seenExceptionsLocal);
/*     */       
/* 125 */       ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       seenExceptionsLocal = Objects.<Set<Throwable>>requireNonNull(this.seenExceptions);
/*     */     } 
/* 137 */     return seenExceptionsLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int decrementRemainingAndGet() {
/* 144 */     return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
/*     */   }
/*     */   
/*     */   final void clearSeenExceptions() {
/* 148 */     this.seenExceptions = null;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void addInitialException(Set<Throwable> paramSet);
/*     */   
/*     */   private static abstract class AtomicHelper
/*     */   {
/*     */     private AtomicHelper() {}
/*     */     
/*     */     abstract void compareAndSetSeenExceptions(AggregateFutureState<?> param1AggregateFutureState, @CheckForNull Set<Throwable> param1Set1, Set<Throwable> param1Set2);
/*     */     
/*     */     abstract int decrementAndGetRemainingCount(AggregateFutureState<?> param1AggregateFutureState);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper
/*     */     extends AtomicHelper
/*     */   {
/*     */     final AtomicReferenceFieldUpdater<AggregateFutureState<?>, Set<Throwable>> seenExceptionsUpdater;
/*     */     final AtomicIntegerFieldUpdater<AggregateFutureState<?>> remainingCountUpdater;
/*     */     
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AggregateFutureState<?>, Set<Throwable>> seenExceptionsUpdater, AtomicIntegerFieldUpdater<AggregateFutureState<?>> remainingCountUpdater) {
/* 170 */       this.seenExceptionsUpdater = seenExceptionsUpdater;
/*     */ 
/*     */       
/* 173 */       this.remainingCountUpdater = remainingCountUpdater;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState<?> state, @CheckForNull Set<Throwable> expect, Set<Throwable> update) {
/* 180 */       this.seenExceptionsUpdater.compareAndSet(state, expect, update);
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState<?> state) {
/* 185 */       return this.remainingCountUpdater.decrementAndGet(state);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends AtomicHelper {
/*     */     private SynchronizedAtomicHelper() {}
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState<?> state, @CheckForNull Set<Throwable> expect, Set<Throwable> update) {
/* 193 */       synchronized (state) {
/* 194 */         if (state.seenExceptions == expect) {
/* 195 */           state.seenExceptions = update;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState<?> state) {
/* 202 */       synchronized (state) {
/* 203 */         return --state.remaining;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AggregateFutureState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */