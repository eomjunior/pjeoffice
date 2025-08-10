/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class UnmodifiableSortedMultiset<E>
/*     */   extends Multisets.UnmodifiableMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient UnmodifiableSortedMultiset<E> descendingMultiset;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   UnmodifiableSortedMultiset(SortedMultiset<E> delegate) {
/*  39 */     super(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMultiset<E> delegate() {
/*  44 */     return (SortedMultiset<E>)super.delegate();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  49 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   NavigableSet<E> createElementSet() {
/*  54 */     return Sets.unmodifiableNavigableSet(delegate().elementSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  59 */     return (NavigableSet<E>)super.elementSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  66 */     UnmodifiableSortedMultiset<E> result = this.descendingMultiset;
/*  67 */     if (result == null) {
/*  68 */       result = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
/*  69 */       result.descendingMultiset = this;
/*  70 */       return this.descendingMultiset = result;
/*     */     } 
/*  72 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> firstEntry() {
/*  78 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> lastEntry() {
/*  84 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  90 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
/* 101 */     return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(@ParametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType) {
/* 110 */     return Multisets.unmodifiableSortedMultiset(
/* 111 */         delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
/* 116 */     return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/UnmodifiableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */