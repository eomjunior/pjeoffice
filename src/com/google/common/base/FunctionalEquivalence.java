/*    */ package com.google.common.base;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class FunctionalEquivalence<F, T>
/*    */   extends Equivalence<F>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private final Function<? super F, ? extends T> function;
/*    */   private final Equivalence<T> resultEquivalence;
/*    */   
/*    */   FunctionalEquivalence(Function<? super F, ? extends T> function, Equivalence<T> resultEquivalence) {
/* 41 */     this.function = Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
/* 42 */     this.resultEquivalence = Preconditions.<Equivalence<T>>checkNotNull(resultEquivalence);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doEquivalent(F a, F b) {
/* 47 */     return this.resultEquivalence.equivalent(this.function.apply(a), this.function.apply(b));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int doHash(F a) {
/* 52 */     return this.resultEquivalence.hash(this.function.apply(a));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object obj) {
/* 57 */     if (obj == this) {
/* 58 */       return true;
/*    */     }
/* 60 */     if (obj instanceof FunctionalEquivalence) {
/* 61 */       FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>)obj;
/* 62 */       return (this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence));
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return Objects.hashCode(new Object[] { this.function, this.resultEquivalence });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return this.resultEquivalence + ".onResultOf(" + this.function + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/FunctionalEquivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */