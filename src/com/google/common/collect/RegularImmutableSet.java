/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableSet<E>
/*     */   extends ImmutableSet.CachingAsList<E>
/*     */ {
/*  37 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*  38 */   static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(EMPTY_ARRAY, 0, EMPTY_ARRAY, 0);
/*     */   
/*     */   private final transient Object[] elements;
/*     */   
/*     */   private final transient int hashCode;
/*     */   
/*     */   @VisibleForTesting
/*     */   final transient Object[] table;
/*     */   private final transient int mask;
/*     */   
/*     */   RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
/*  49 */     this.elements = elements;
/*  50 */     this.hashCode = hashCode;
/*  51 */     this.table = table;
/*  52 */     this.mask = mask;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object target) {
/*  57 */     Object[] table = this.table;
/*  58 */     if (target == null || table.length == 0) {
/*  59 */       return false;
/*     */     }
/*  61 */     for (int i = Hashing.smearedHash(target);; i++) {
/*  62 */       i &= this.mask;
/*  63 */       Object candidate = table[i];
/*  64 */       if (candidate == null)
/*  65 */         return false; 
/*  66 */       if (candidate.equals(target)) {
/*  67 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  74 */     return this.elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  79 */     return Iterators.forArray((E[])this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  84 */     return Spliterators.spliterator(this.elements, 1297);
/*     */   }
/*     */ 
/*     */   
/*     */   Object[] internalArray() {
/*  89 */     return this.elements;
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/*  94 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/*  99 */     return this.elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 104 */     System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
/* 105 */     return offset + this.elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 110 */     return (this.table.length == 0) ? 
/* 111 */       ImmutableList.<E>of() : 
/* 112 */       new RegularImmutableAsList<>(this, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 136 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */