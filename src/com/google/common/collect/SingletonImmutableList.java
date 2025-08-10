/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Spliterator;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class SingletonImmutableList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/*    */   final transient E element;
/*    */   
/*    */   SingletonImmutableList(E element) {
/* 41 */     this.element = (E)Preconditions.checkNotNull(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 46 */     Preconditions.checkElementIndex(index, 1);
/* 47 */     return this.element;
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<E> iterator() {
/* 52 */     return Iterators.singletonIterator(this.element);
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 57 */     return Collections.<E>singleton(this.element).spliterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 62 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 67 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
/* 68 */     return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return '[' + this.element.toString() + ']';
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 87 */     return super.writeReplace();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SingletonImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */