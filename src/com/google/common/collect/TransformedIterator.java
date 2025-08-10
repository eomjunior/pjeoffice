/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
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
/*    */ abstract class TransformedIterator<F, T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   final Iterator<? extends F> backingIterator;
/*    */   
/*    */   TransformedIterator(Iterator<? extends F> backingIterator) {
/* 38 */     this.backingIterator = (Iterator<? extends F>)Preconditions.checkNotNull(backingIterator);
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   abstract T transform(@ParametricNullness F paramF);
/*    */   
/*    */   public final boolean hasNext() {
/* 46 */     return this.backingIterator.hasNext();
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final T next() {
/* 52 */     return transform(this.backingIterator.next());
/*    */   }
/*    */ 
/*    */   
/*    */   public final void remove() {
/* 57 */     this.backingIterator.remove();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TransformedIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */