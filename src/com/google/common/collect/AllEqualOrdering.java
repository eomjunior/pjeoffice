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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class AllEqualOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 33 */   static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(@CheckForNull Object left, @CheckForNull Object right) {
/* 37 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> List<E> sortedCopy(Iterable<E> iterable) {
/* 42 */     return Lists.newArrayList(iterable);
/*    */   }
/*    */ 
/*    */   
/*    */   public <E> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
/* 47 */     return ImmutableList.copyOf(iterable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> Ordering<S> reverse() {
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 57 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "Ordering.allEqual()";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AllEqualOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */