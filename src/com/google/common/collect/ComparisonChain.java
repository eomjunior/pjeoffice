/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ComparisonChain
/*     */ {
/*     */   private ComparisonChain() {}
/*     */   
/*     */   public static ComparisonChain start() {
/* 100 */     return ACTIVE;
/*     */   }
/*     */   
/* 103 */   private static final ComparisonChain ACTIVE = new ComparisonChain()
/*     */     {
/*     */       
/*     */       public ComparisonChain compare(Comparable<?> left, Comparable<?> right)
/*     */       {
/* 108 */         return classify(left.compareTo(right));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> ComparisonChain compare(@ParametricNullness T left, @ParametricNullness T right, Comparator<T> comparator) {
/* 114 */         return classify(comparator.compare(left, right));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compare(int left, int right) {
/* 119 */         return classify(Ints.compare(left, right));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compare(long left, long right) {
/* 124 */         return classify(Longs.compare(left, right));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compare(float left, float right) {
/* 129 */         return classify(Float.compare(left, right));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compare(double left, double right) {
/* 134 */         return classify(Double.compare(left, right));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compareTrueFirst(boolean left, boolean right) {
/* 139 */         return classify(Booleans.compare(right, left));
/*     */       }
/*     */ 
/*     */       
/*     */       public ComparisonChain compareFalseFirst(boolean left, boolean right) {
/* 144 */         return classify(Booleans.compare(left, right));
/*     */       }
/*     */       
/*     */       ComparisonChain classify(int result) {
/* 148 */         return (result < 0) ? ComparisonChain.LESS : ((result > 0) ? ComparisonChain.GREATER : ComparisonChain.ACTIVE);
/*     */       }
/*     */ 
/*     */       
/*     */       public int result() {
/* 153 */         return 0;
/*     */       }
/*     */     };
/*     */   
/* 157 */   private static final ComparisonChain LESS = new InactiveComparisonChain(-1);
/*     */   
/* 159 */   private static final ComparisonChain GREATER = new InactiveComparisonChain(1);
/*     */   
/*     */   private static final class InactiveComparisonChain extends ComparisonChain {
/*     */     final int result;
/*     */     
/*     */     InactiveComparisonChain(int result) {
/* 165 */       this.result = result;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compare(Comparable<?> left, Comparable<?> right) {
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> ComparisonChain compare(@ParametricNullness T left, @ParametricNullness T right, Comparator<T> comparator) {
/* 176 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compare(int left, int right) {
/* 181 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compare(long left, long right) {
/* 186 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compare(float left, float right) {
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compare(double left, double right) {
/* 196 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compareTrueFirst(boolean left, boolean right) {
/* 201 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ComparisonChain compareFalseFirst(boolean left, boolean right) {
/* 206 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int result() {
/* 211 */       return this.result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final ComparisonChain compare(Boolean left, Boolean right) {
/* 273 */     return compareFalseFirst(left.booleanValue(), right.booleanValue());
/*     */   }
/*     */   
/*     */   public abstract ComparisonChain compare(Comparable<?> paramComparable1, Comparable<?> paramComparable2);
/*     */   
/*     */   public abstract <T> ComparisonChain compare(@ParametricNullness T paramT1, @ParametricNullness T paramT2, Comparator<T> paramComparator);
/*     */   
/*     */   public abstract ComparisonChain compare(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract ComparisonChain compare(long paramLong1, long paramLong2);
/*     */   
/*     */   public abstract ComparisonChain compare(float paramFloat1, float paramFloat2);
/*     */   
/*     */   public abstract ComparisonChain compare(double paramDouble1, double paramDouble2);
/*     */   
/*     */   public abstract ComparisonChain compareTrueFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public abstract ComparisonChain compareFalseFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public abstract int result();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ComparisonChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */