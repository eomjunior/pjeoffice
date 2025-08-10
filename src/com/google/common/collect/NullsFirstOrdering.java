/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ final class NullsFirstOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Ordering<? super T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   NullsFirstOrdering(Ordering<? super T> ordering) {
/* 33 */     this.ordering = ordering;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(@CheckForNull T left, @CheckForNull T right) {
/* 38 */     if (left == right) {
/* 39 */       return 0;
/*    */     }
/* 41 */     if (left == null) {
/* 42 */       return -1;
/*    */     }
/* 44 */     if (right == null) {
/* 45 */       return 1;
/*    */     }
/* 47 */     return this.ordering.compare(left, right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> reverse() {
/* 54 */     return this.ordering.<T>reverse().nullsLast();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> nullsFirst() {
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends T> Ordering<S> nullsLast() {
/* 65 */     return this.ordering.nullsLast();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 70 */     if (object == this) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (object instanceof NullsFirstOrdering) {
/* 74 */       NullsFirstOrdering<?> that = (NullsFirstOrdering)object;
/* 75 */       return this.ordering.equals(that.ordering);
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     return this.ordering.hashCode() ^ 0x39153A74;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return this.ordering + ".nullsFirst()";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/NullsFirstOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */