/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
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
/*    */ final class SortedIterables
/*    */ {
/*    */   public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements) {
/*    */     Comparator<?> comparator2;
/* 39 */     Preconditions.checkNotNull(comparator);
/* 40 */     Preconditions.checkNotNull(elements);
/*    */     
/* 42 */     if (elements instanceof SortedSet) {
/* 43 */       comparator2 = comparator((SortedSet)elements);
/* 44 */     } else if (elements instanceof SortedIterable) {
/* 45 */       comparator2 = ((SortedIterable)elements).comparator();
/*    */     } else {
/* 47 */       return false;
/*    */     } 
/* 49 */     return comparator.equals(comparator2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet) {
/* 56 */     Comparator<? super E> result = sortedSet.comparator();
/* 57 */     if (result == null) {
/* 58 */       result = Ordering.natural();
/*    */     }
/* 60 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SortedIterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */