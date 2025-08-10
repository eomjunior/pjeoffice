/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*  36 */   static final ImmutableList<Object> EMPTY = new RegularImmutableList(new Object[0]);
/*     */   @VisibleForTesting
/*     */   final transient Object[] array;
/*     */   
/*     */   RegularImmutableList(Object[] array) {
/*  41 */     this.array = array;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  46 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  51 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   Object[] internalArray() {
/*  56 */     return this.array;
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/*  61 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/*  66 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int dstOff) {
/*  71 */     System.arraycopy(this.array, 0, dst, dstOff, this.array.length);
/*  72 */     return dstOff + this.array.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  79 */     return (E)this.array[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/*  87 */     return Iterators.forArrayWithPosition((E[])this.array, index);
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  92 */     return Spliterators.spliterator(this.array, 1296);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 103 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */