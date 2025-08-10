/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*     */   public void addFirst(@ParametricNullness E e) {
/*  58 */     delegate().addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(@ParametricNullness E e) {
/*  63 */     delegate().addLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  68 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E getFirst() {
/*  74 */     return delegate().getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E getLast() {
/*  80 */     return delegate().getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerFirst(@ParametricNullness E e) {
/*  86 */     return delegate().offerFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerLast(@ParametricNullness E e) {
/*  92 */     return delegate().offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peekFirst() {
/*  98 */     return delegate().peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peekLast() {
/* 104 */     return delegate().peekLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 111 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 118 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E pop() {
/* 125 */     return delegate().pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(@ParametricNullness E e) {
/* 130 */     delegate().push(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 137 */     return delegate().removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 144 */     return delegate().removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeFirstOccurrence(@CheckForNull Object o) {
/* 150 */     return delegate().removeFirstOccurrence(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeLastOccurrence(@CheckForNull Object o) {
/* 156 */     return delegate().removeLastOccurrence(o);
/*     */   }
/*     */   
/*     */   protected abstract Deque<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */