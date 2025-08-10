/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(@ParametricNullness E o) {
/*  62 */     return delegate().offer(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/*  69 */     return delegate().poll();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E remove() {
/*  76 */     return delegate().remove();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peek() {
/*  82 */     return delegate().peek();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E element() {
/*  88 */     return delegate().element();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardOffer(@ParametricNullness E e) {
/*     */     try {
/*  99 */       return add(e);
/* 100 */     } catch (IllegalStateException caught) {
/* 101 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardPeek() {
/*     */     try {
/* 114 */       return element();
/* 115 */     } catch (NoSuchElementException caught) {
/* 116 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardPoll() {
/*     */     try {
/* 129 */       return remove();
/* 130 */     } catch (NoSuchElementException caught) {
/* 131 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Queue<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */