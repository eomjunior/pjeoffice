/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Collection;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class DescendingMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient NavigableSet<E> elementSet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  45 */     Comparator<? super E> result = this.comparator;
/*  46 */     if (result == null) {
/*  47 */       return this.comparator = Ordering.from(forwardMultiset().comparator()).<Object>reverse();
/*     */     }
/*  49 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  56 */     NavigableSet<E> result = this.elementSet;
/*  57 */     if (result == null) {
/*  58 */       return this.elementSet = new SortedMultisets.NavigableElementSet<>(this);
/*     */     }
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  66 */     return forwardMultiset().pollLastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  72 */     return forwardMultiset().pollFirstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(@ParametricNullness E toElement, BoundType boundType) {
/*  77 */     return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(@ParametricNullness E fromElement, BoundType fromBoundType, @ParametricNullness E toElement, BoundType toBoundType) {
/*  86 */     return forwardMultiset()
/*  87 */       .subMultiset(toElement, toBoundType, fromElement, fromBoundType)
/*  88 */       .descendingMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(@ParametricNullness E fromElement, BoundType boundType) {
/*  93 */     return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Multiset<E> delegate() {
/*  98 */     return forwardMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/* 103 */     return forwardMultiset();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> firstEntry() {
/* 109 */     return forwardMultiset().lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> lastEntry() {
/* 115 */     return forwardMultiset().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 124 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 125 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/*     */     class EntrySetImpl
/*     */       extends Multisets.EntrySet<E>
/*     */     {
/*     */       Multiset<E> multiset() {
/* 133 */         return DescendingMultiset.this;
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<Multiset.Entry<E>> iterator() {
/* 138 */         return DescendingMultiset.this.entryIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 143 */         return DescendingMultiset.this.forwardMultiset().entrySet().size();
/*     */       }
/*     */     };
/* 146 */     return new EntrySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 151 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 156 */     return standardToArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 162 */     return (T[])standardToArray((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */   abstract SortedMultiset<E> forwardMultiset();
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/DescendingMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */