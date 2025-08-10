/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.ListIterator;
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
/*    */ abstract class TransformedListIterator<F, T>
/*    */   extends TransformedIterator<F, T>
/*    */   implements ListIterator<T>
/*    */ {
/*    */   TransformedListIterator(ListIterator<? extends F> backingIterator) {
/* 35 */     super(backingIterator);
/*    */   }
/*    */   
/*    */   private ListIterator<? extends F> backingIterator() {
/* 39 */     return (ListIterator<? extends F>)this.backingIterator;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasPrevious() {
/* 44 */     return backingIterator().hasPrevious();
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final T previous() {
/* 50 */     return transform(backingIterator().previous());
/*    */   }
/*    */ 
/*    */   
/*    */   public final int nextIndex() {
/* 55 */     return backingIterator().nextIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public final int previousIndex() {
/* 60 */     return backingIterator().previousIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(@ParametricNullness T element) {
/* 65 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(@ParametricNullness T element) {
/* 70 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TransformedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */