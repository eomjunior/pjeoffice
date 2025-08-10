/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.collect.ForwardingQueue;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Collection;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingBlockingQueue<E>
/*    */   extends ForwardingQueue<E>
/*    */   implements BlockingQueue<E>
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 55 */     return delegate().drainTo(c, maxElements);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public int drainTo(Collection<? super E> c) {
/* 61 */     return delegate().drainTo(c);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 67 */     return delegate().offer(e, timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   @CanIgnoreReturnValue
/*    */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 74 */     return delegate().poll(timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void put(E e) throws InterruptedException {
/* 79 */     delegate().put(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public int remainingCapacity() {
/* 84 */     return delegate().remainingCapacity();
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public E take() throws InterruptedException {
/* 90 */     return delegate().take();
/*    */   }
/*    */   
/*    */   protected abstract BlockingQueue<E> delegate();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingBlockingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */