/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class CompoundOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<? super T>[] comparators;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
/* 35 */     this.comparators = (Comparator<? super T>[])new Comparator[] { primary, secondary };
/*    */   }
/*    */ 
/*    */   
/*    */   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
/* 40 */     this.comparators = Iterables.<Comparator<? super T>>toArray(comparators, (Comparator<? super T>[])new Comparator[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(@ParametricNullness T left, @ParametricNullness T right) {
/* 45 */     for (int i = 0; i < this.comparators.length; i++) {
/* 46 */       int result = this.comparators[i].compare(left, right);
/* 47 */       if (result != 0) {
/* 48 */         return result;
/*    */       }
/*    */     } 
/* 51 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 56 */     if (object == this) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (object instanceof CompoundOrdering) {
/* 60 */       CompoundOrdering<?> that = (CompoundOrdering)object;
/* 61 */       return Arrays.equals((Object[])this.comparators, (Object[])that.comparators);
/*    */     } 
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return Arrays.hashCode((Object[])this.comparators);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return "Ordering.compound(" + Arrays.toString((Object[])this.comparators) + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompoundOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */