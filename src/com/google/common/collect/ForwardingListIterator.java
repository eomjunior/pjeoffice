/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
/*    */ import java.util.ListIterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   public void add(@ParametricNullness E element) {
/* 52 */     delegate().add(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPrevious() {
/* 57 */     return delegate().hasPrevious();
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextIndex() {
/* 62 */     return delegate().nextIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   @CanIgnoreReturnValue
/*    */   public E previous() {
/* 69 */     return delegate().previous();
/*    */   }
/*    */ 
/*    */   
/*    */   public int previousIndex() {
/* 74 */     return delegate().previousIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(@ParametricNullness E element) {
/* 79 */     delegate().set(element);
/*    */   }
/*    */   
/*    */   protected abstract ListIterator<E> delegate();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */