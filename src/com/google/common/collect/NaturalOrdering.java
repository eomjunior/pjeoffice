/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.concurrent.LazyInit;
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
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class NaturalOrdering
/*    */   extends Ordering<Comparable<?>>
/*    */   implements Serializable
/*    */ {
/* 32 */   static final NaturalOrdering INSTANCE = new NaturalOrdering();
/*    */   
/*    */   @LazyInit
/*    */   @CheckForNull
/*    */   private transient Ordering<Comparable<?>> nullsFirst;
/*    */ 
/*    */   
/*    */   public int compare(Comparable<?> left, Comparable<?> right) {
/* 40 */     Preconditions.checkNotNull(left);
/* 41 */     Preconditions.checkNotNull(right);
/* 42 */     return left.compareTo(right);
/*    */   } @LazyInit
/*    */   @CheckForNull
/*    */   private transient Ordering<Comparable<?>> nullsLast; private static final long serialVersionUID = 0L;
/*    */   public <S extends Comparable<?>> Ordering<S> nullsFirst() {
/* 47 */     Ordering<Comparable<?>> result = this.nullsFirst;
/* 48 */     if (result == null) {
/* 49 */       result = this.nullsFirst = super.<Comparable<?>>nullsFirst();
/*    */     }
/* 51 */     return (Ordering)result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable<?>> Ordering<S> nullsLast() {
/* 56 */     Ordering<Comparable<?>> result = this.nullsLast;
/* 57 */     if (result == null) {
/* 58 */       result = this.nullsLast = super.<Comparable<?>>nullsLast();
/*    */     }
/* 60 */     return (Ordering)result;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S extends Comparable<?>> Ordering<S> reverse() {
/* 65 */     return ReverseNaturalOrdering.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 70 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "Ordering.natural()";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/NaturalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */