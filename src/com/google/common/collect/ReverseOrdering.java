/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class ReverseOrdering<T>
/*     */   extends Ordering<T>
/*     */   implements Serializable
/*     */ {
/*     */   final Ordering<? super T> forwardOrder;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   ReverseOrdering(Ordering<? super T> forwardOrder) {
/*  35 */     this.forwardOrder = (Ordering<? super T>)Preconditions.checkNotNull(forwardOrder);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(@ParametricNullness T a, @ParametricNullness T b) {
/*  40 */     return this.forwardOrder.compare(b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S extends T> Ordering<S> reverse() {
/*  46 */     return (Ordering)this.forwardOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(@ParametricNullness E a, @ParametricNullness E b) {
/*  53 */     return this.forwardOrder.max(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(@ParametricNullness E a, @ParametricNullness E b, @ParametricNullness E c, E... rest) {
/*  59 */     return this.forwardOrder.max(a, b, c, rest);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/*  64 */     return this.forwardOrder.max(iterator);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/*  69 */     return this.forwardOrder.max(iterable);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(@ParametricNullness E a, @ParametricNullness E b) {
/*  74 */     return this.forwardOrder.min(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E max(@ParametricNullness E a, @ParametricNullness E b, @ParametricNullness E c, E... rest) {
/*  80 */     return this.forwardOrder.min(a, b, c, rest);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/*  85 */     return this.forwardOrder.min(iterator);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/*  90 */     return this.forwardOrder.min(iterable);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return -this.forwardOrder.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 100 */     if (object == this) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (object instanceof ReverseOrdering) {
/* 104 */       ReverseOrdering<?> that = (ReverseOrdering)object;
/* 105 */       return this.forwardOrder.equals(that.forwardOrder);
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return this.forwardOrder + ".reverse()";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ReverseOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */