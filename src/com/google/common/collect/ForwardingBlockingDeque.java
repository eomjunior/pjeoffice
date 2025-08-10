/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingBlockingDeque<E>
/*     */   extends ForwardingDeque<E>
/*     */   implements BlockingDeque<E>
/*     */ {
/*     */   public int remainingCapacity() {
/*  64 */     return delegate().remainingCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putFirst(E e) throws InterruptedException {
/*  69 */     delegate().putFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putLast(E e) throws InterruptedException {
/*  74 */     delegate().putLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  79 */     return delegate().offerFirst(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  84 */     return delegate().offerLast(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public E takeFirst() throws InterruptedException {
/*  89 */     return delegate().takeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E takeLast() throws InterruptedException {
/*  94 */     return delegate().takeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
/* 100 */     return delegate().pollFirst(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
/* 106 */     return delegate().pollLast(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(E e) throws InterruptedException {
/* 111 */     delegate().put(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 116 */     return delegate().offer(e, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public E take() throws InterruptedException {
/* 121 */     return delegate().take();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 127 */     return delegate().poll(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c) {
/* 132 */     return delegate().drainTo(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 137 */     return delegate().drainTo(c, maxElements);
/*     */   }
/*     */   
/*     */   protected abstract BlockingDeque<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingBlockingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */