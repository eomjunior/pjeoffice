/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ByFunctionOrdering<F, T>
/*    */   extends Ordering<F>
/*    */   implements Serializable
/*    */ {
/*    */   final Function<F, ? extends T> function;
/*    */   final Ordering<T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
/* 40 */     this.function = (Function<F, ? extends T>)Preconditions.checkNotNull(function);
/* 41 */     this.ordering = (Ordering<T>)Preconditions.checkNotNull(ordering);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(@ParametricNullness F left, @ParametricNullness F right) {
/* 46 */     return this.ordering.compare((T)this.function.apply(left), (T)this.function.apply(right));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 51 */     if (object == this) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (object instanceof ByFunctionOrdering) {
/* 55 */       ByFunctionOrdering<?, ?> that = (ByFunctionOrdering<?, ?>)object;
/* 56 */       return (this.function.equals(that.function) && this.ordering.equals(that.ordering));
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hashCode(new Object[] { this.function, this.ordering });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return this.ordering + ".onResultOf(" + this.function + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ByFunctionOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */