/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.NavigableSet;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtIncompatible
/*    */ final class DescendingImmutableSortedMultiset<E>
/*    */   extends ImmutableSortedMultiset<E>
/*    */ {
/*    */   private final transient ImmutableSortedMultiset<E> forward;
/*    */   
/*    */   DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) {
/* 33 */     this.forward = forward;
/*    */   }
/*    */ 
/*    */   
/*    */   public int count(@CheckForNull Object element) {
/* 38 */     return this.forward.count(element);
/*    */   }
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   public Multiset.Entry<E> firstEntry() {
/* 44 */     return this.forward.lastEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   public Multiset.Entry<E> lastEntry() {
/* 50 */     return this.forward.firstEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 55 */     return this.forward.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedSet<E> elementSet() {
/* 60 */     return this.forward.elementSet().descendingSet();
/*    */   }
/*    */ 
/*    */   
/*    */   Multiset.Entry<E> getEntry(int index) {
/* 65 */     return this.forward.entrySet().asList().reverse().get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> descendingMultiset() {
/* 70 */     return this.forward;
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 75 */     return this.forward.tailMultiset(upperBound, boundType).descendingMultiset();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 80 */     return this.forward.headMultiset(lowerBound, boundType).descendingMultiset();
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 85 */     return this.forward.isPartialView();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   Object writeReplace() {
/* 93 */     return super.writeReplace();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/DescendingImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */