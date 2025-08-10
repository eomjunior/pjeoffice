/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.ListIterator;
/*     */ import java.util.function.Consumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ class RegularImmutableAsList<E>
/*     */   extends ImmutableAsList<E>
/*     */ {
/*     */   private final ImmutableCollection<E> delegate;
/*     */   private final ImmutableList<? extends E> delegateList;
/*     */   
/*     */   RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList) {
/*  40 */     this.delegate = delegate;
/*  41 */     this.delegateList = delegateList;
/*     */   }
/*     */   
/*     */   RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
/*  45 */     this(delegate, ImmutableList.asImmutableList(array));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<E> delegateCollection() {
/*  50 */     return this.delegate;
/*     */   }
/*     */   
/*     */   ImmutableList<? extends E> delegateList() {
/*  54 */     return this.delegateList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/*  60 */     return (UnmodifiableListIterator)this.delegateList.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public void forEach(Consumer<? super E> action) {
/*  66 */     this.delegateList.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  72 */     return this.delegateList.copyIntoArray(dst, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   Object[] internalArray() {
/*  78 */     return this.delegateList.internalArray();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/*  83 */     return this.delegateList.internalArrayStart();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/*  88 */     return this.delegateList.internalArrayEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  93 */     return this.delegateList.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 102 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */