/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ExplicitOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final ImmutableMap<T, Integer> rankMap;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ExplicitOrdering(List<T> valuesInOrder) {
/* 31 */     this(Maps.indexMap(valuesInOrder));
/*    */   }
/*    */   
/*    */   ExplicitOrdering(ImmutableMap<T, Integer> rankMap) {
/* 35 */     this.rankMap = rankMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 40 */     return rank(left) - rank(right);
/*    */   }
/*    */   
/*    */   private int rank(T value) {
/* 44 */     Integer rank = this.rankMap.get(value);
/* 45 */     if (rank == null) {
/* 46 */       throw new Ordering.IncomparableValueException(value);
/*    */     }
/* 48 */     return rank.intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 53 */     if (object instanceof ExplicitOrdering) {
/* 54 */       ExplicitOrdering<?> that = (ExplicitOrdering)object;
/* 55 */       return this.rankMap.equals(that.rankMap);
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return this.rankMap.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return "Ordering.explicit(" + this.rankMap.keySet() + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ExplicitOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */