/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ final class ComparatorOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<T> comparator;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ComparatorOrdering(Comparator<T> comparator) {
/* 35 */     this.comparator = (Comparator<T>)Preconditions.checkNotNull(comparator);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(@ParametricNullness T a, @ParametricNullness T b) {
/* 40 */     return this.comparator.compare(a, b);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 45 */     if (object == this) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (object instanceof ComparatorOrdering) {
/* 49 */       ComparatorOrdering<?> that = (ComparatorOrdering)object;
/* 50 */       return this.comparator.equals(that.comparator);
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return this.comparator.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return this.comparator.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ComparatorOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */