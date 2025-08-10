/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.ISmartIterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayIterator<T>
/*    */   implements ISmartIterator<T>
/*    */ {
/* 35 */   private int current = 0;
/*    */   private T[] array;
/*    */   
/*    */   public ArrayIterator(T... array) {
/* 39 */     this.array = (T[])Args.<Object[]>requireNonNull((Object[])array, "array is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 44 */     return (this.current < this.array.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasPrevious() {
/* 49 */     return (this.current > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public final T next() {
/* 54 */     if (this.current == this.array.length)
/* 55 */       throw new NoSuchElementException(); 
/* 56 */     return this.array[this.current++];
/*    */   }
/*    */ 
/*    */   
/*    */   public final void reset() {
/* 61 */     this.current = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public final T previous() {
/* 66 */     if (this.current == 0)
/* 67 */       throw new NoSuchElementException(); 
/* 68 */     return this.array[--this.current];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/ArrayIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */