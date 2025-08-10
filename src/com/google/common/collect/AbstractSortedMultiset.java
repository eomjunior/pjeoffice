/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractSortedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @GwtTransient
/*     */   final Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient SortedMultiset<E> descendingMultiset;
/*     */   
/*     */   AbstractSortedMultiset() {
/*  46 */     this(Ordering.natural());
/*     */   }
/*     */   
/*     */   AbstractSortedMultiset(Comparator<? super E> comparator) {
/*  50 */     this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  55 */     return (NavigableSet<E>)super.elementSet();
/*     */   }
/*     */ 
/*     */   
/*     */   NavigableSet<E> createElementSet() {
/*  60 */     return new SortedMultisets.NavigableElementSet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  65 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> firstEntry() {
/*  71 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  72 */     return entryIterator.hasNext() ? entryIterator.next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> lastEntry() {
/*  78 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  79 */     return entryIterator.hasNext() ? entryIterator.next() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  85 */     Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
/*  86 */     if (entryIterator.hasNext()) {
/*  87 */       Multiset.Entry<E> result = entryIterator.next();
/*  88 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/*  89 */       entryIterator.remove();
/*  90 */       return result;
/*     */     } 
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  98 */     Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
/*  99 */     if (entryIterator.hasNext()) {
/* 100 */       Multiset.Entry<E> result = entryIterator.next();
/* 101 */       result = Multisets.immutableEntry(result.getElement(), result.getCount());
/* 102 */       entryIterator.remove();
/* 103 */       return result;
/*     */     } 
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(@ParametricNullness E fromElement, BoundType fromBoundType, @ParametricNullness E toElement, BoundType toBoundType) {
/* 115 */     Preconditions.checkNotNull(fromBoundType);
/* 116 */     Preconditions.checkNotNull(toBoundType);
/* 117 */     return tailMultiset(fromElement, fromBoundType).headMultiset(toElement, toBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<E> descendingIterator() {
/* 123 */     return Multisets.iteratorImpl(descendingMultiset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/* 130 */     SortedMultiset<E> result = this.descendingMultiset;
/* 131 */     return (result == null) ? (this.descendingMultiset = createDescendingMultiset()) : result;
/*     */   }
/*     */   
/*     */   SortedMultiset<E> createDescendingMultiset() {
/*     */     class DescendingMultisetImpl
/*     */       extends DescendingMultiset<E>
/*     */     {
/*     */       SortedMultiset<E> forwardMultiset() {
/* 139 */         return AbstractSortedMultiset.this;
/*     */       }
/*     */ 
/*     */       
/*     */       Iterator<Multiset.Entry<E>> entryIterator() {
/* 144 */         return AbstractSortedMultiset.this.descendingEntryIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<E> iterator() {
/* 149 */         return AbstractSortedMultiset.this.descendingIterator();
/*     */       }
/*     */     };
/* 152 */     return new DescendingMultisetImpl();
/*     */   }
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */