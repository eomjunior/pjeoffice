/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NoSuchElementException;
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
/*     */ abstract class AbstractIndexedListIterator<E>
/*     */   extends UnmodifiableListIterator<E>
/*     */ {
/*     */   private final int size;
/*     */   private int position;
/*     */   
/*     */   @ParametricNullness
/*     */   protected abstract E get(int paramInt);
/*     */   
/*     */   protected AbstractIndexedListIterator(int size) {
/*  52 */     this(size, 0);
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
/*     */   protected AbstractIndexedListIterator(int size, int position) {
/*  66 */     Preconditions.checkPositionIndex(position, size);
/*  67 */     this.size = size;
/*  68 */     this.position = position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNext() {
/*  73 */     return (this.position < this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public final E next() {
/*  79 */     if (!hasNext()) {
/*  80 */       throw new NoSuchElementException();
/*     */     }
/*  82 */     return get(this.position++);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int nextIndex() {
/*  87 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasPrevious() {
/*  92 */     return (this.position > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public final E previous() {
/*  98 */     if (!hasPrevious()) {
/*  99 */       throw new NoSuchElementException();
/*     */     }
/* 101 */     return get(--this.position);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int previousIndex() {
/* 106 */     return this.position - 1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractIndexedListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */