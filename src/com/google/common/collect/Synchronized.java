/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Queue;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     final Object delegate;
/*      */     final Object mutex;
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedObject(Object delegate, @CheckForNull Object mutex) {
/*   85 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   86 */       this.mutex = (mutex == null) ? this : mutex;
/*      */     }
/*      */     
/*      */     Object delegate() {
/*   90 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   97 */       synchronized (this.mutex) {
/*   98 */         return this.delegate.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  110 */       synchronized (this.mutex) {
/*  111 */         stream.defaultWriteObject();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> collection(Collection<E> collection, @CheckForNull Object mutex) {
/*  122 */     return new SynchronizedCollection<>(collection, mutex);
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedCollection(Collection<E> delegate, @CheckForNull Object mutex) {
/*  129 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<E> delegate() {
/*  135 */       return (Collection<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E e) {
/*  140 */       synchronized (this.mutex) {
/*  141 */         return delegate().add(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> c) {
/*  147 */       synchronized (this.mutex) {
/*  148 */         return delegate().addAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  154 */       synchronized (this.mutex) {
/*  155 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/*  161 */       synchronized (this.mutex) {
/*  162 */         return delegate().contains(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  168 */       synchronized (this.mutex) {
/*  169 */         return delegate().containsAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  175 */       synchronized (this.mutex) {
/*  176 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  182 */       return delegate().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<E> spliterator() {
/*  187 */       synchronized (this.mutex) {
/*  188 */         return delegate().spliterator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/*  194 */       synchronized (this.mutex) {
/*  195 */         return delegate().stream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/*  201 */       synchronized (this.mutex) {
/*  202 */         return delegate().parallelStream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/*  208 */       synchronized (this.mutex) {
/*  209 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/*  215 */       synchronized (this.mutex) {
/*  216 */         return delegate().remove(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  222 */       synchronized (this.mutex) {
/*  223 */         return delegate().removeAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  229 */       synchronized (this.mutex) {
/*  230 */         return delegate().retainAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/*  236 */       synchronized (this.mutex) {
/*  237 */         return delegate().removeIf(filter);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  243 */       synchronized (this.mutex) {
/*  244 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  250 */       synchronized (this.mutex) {
/*  251 */         return delegate().toArray();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  258 */       synchronized (this.mutex) {
/*  259 */         return delegate().toArray(a);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <E> Set<E> set(Set<E> set, @CheckForNull Object mutex) {
/*  268 */     return new SynchronizedSet<>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSet(Set<E> delegate, @CheckForNull Object mutex) {
/*  275 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> delegate() {
/*  280 */       return (Set<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/*  285 */       if (o == this) {
/*  286 */         return true;
/*      */       }
/*  288 */       synchronized (this.mutex) {
/*  289 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  295 */       synchronized (this.mutex) {
/*  296 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @CheckForNull Object mutex) {
/*  305 */     return new SynchronizedSortedSet<>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSet(SortedSet<E> delegate, @CheckForNull Object mutex) {
/*  311 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<E> delegate() {
/*  316 */       return (SortedSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super E> comparator() {
/*  322 */       synchronized (this.mutex) {
/*  323 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  329 */       synchronized (this.mutex) {
/*  330 */         return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  336 */       synchronized (this.mutex) {
/*  337 */         return Synchronized.sortedSet(delegate().headSet(toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  343 */       synchronized (this.mutex) {
/*  344 */         return Synchronized.sortedSet(delegate().tailSet(fromElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/*  350 */       synchronized (this.mutex) {
/*  351 */         return delegate().first();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  357 */       synchronized (this.mutex) {
/*  358 */         return delegate().last();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> List<E> list(List<E> list, @CheckForNull Object mutex) {
/*  367 */     return (list instanceof RandomAccess) ? 
/*  368 */       new SynchronizedRandomAccessList<>(list, mutex) : 
/*  369 */       new SynchronizedList<>(list, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedList(List<E> delegate, @CheckForNull Object mutex) {
/*  375 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     List<E> delegate() {
/*  380 */       return (List<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/*  385 */       synchronized (this.mutex) {
/*  386 */         delegate().add(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/*  392 */       synchronized (this.mutex) {
/*  393 */         return delegate().addAll(index, c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  399 */       synchronized (this.mutex) {
/*  400 */         return delegate().get(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(@CheckForNull Object o) {
/*  406 */       synchronized (this.mutex) {
/*  407 */         return delegate().indexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(@CheckForNull Object o) {
/*  413 */       synchronized (this.mutex) {
/*  414 */         return delegate().lastIndexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator() {
/*  420 */       return delegate().listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator(int index) {
/*  425 */       return delegate().listIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/*  430 */       synchronized (this.mutex) {
/*  431 */         return delegate().remove(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/*  437 */       synchronized (this.mutex) {
/*  438 */         return delegate().set(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(UnaryOperator<E> operator) {
/*  444 */       synchronized (this.mutex) {
/*  445 */         delegate().replaceAll(operator);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void sort(Comparator<? super E> c) {
/*  451 */       synchronized (this.mutex) {
/*  452 */         delegate().sort(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<E> subList(int fromIndex, int toIndex) {
/*  458 */       synchronized (this.mutex) {
/*  459 */         return Synchronized.list(delegate().subList(fromIndex, toIndex), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/*  465 */       if (o == this) {
/*  466 */         return true;
/*      */       }
/*  468 */       synchronized (this.mutex) {
/*  469 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  475 */       synchronized (this.mutex) {
/*  476 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class SynchronizedRandomAccessList<E>
/*      */     extends SynchronizedList<E> implements RandomAccess {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedRandomAccessList(List<E> list, @CheckForNull Object mutex) {
/*  486 */       super(list, mutex);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Multiset<E> multiset(Multiset<E> multiset, @CheckForNull Object mutex) {
/*  494 */     if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
/*  495 */       return multiset;
/*      */     }
/*  497 */     return new SynchronizedMultiset<>(multiset, mutex);
/*      */   }
/*      */   static final class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E> { @CheckForNull
/*      */     transient Set<E> elementSet;
/*      */     @CheckForNull
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMultiset(Multiset<E> delegate, @CheckForNull Object mutex) {
/*  506 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<E> delegate() {
/*  511 */       return (Multiset<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@CheckForNull Object o) {
/*  516 */       synchronized (this.mutex) {
/*  517 */         return delegate().count(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@ParametricNullness E e, int n) {
/*  523 */       synchronized (this.mutex) {
/*  524 */         return delegate().add(e, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@CheckForNull Object o, int n) {
/*  530 */       synchronized (this.mutex) {
/*  531 */         return delegate().remove(o, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(@ParametricNullness E element, int count) {
/*  537 */       synchronized (this.mutex) {
/*  538 */         return delegate().setCount(element, count);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
/*  544 */       synchronized (this.mutex) {
/*  545 */         return delegate().setCount(element, oldCount, newCount);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  551 */       synchronized (this.mutex) {
/*  552 */         if (this.elementSet == null) {
/*  553 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  555 */         return this.elementSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  561 */       synchronized (this.mutex) {
/*  562 */         if (this.entrySet == null) {
/*  563 */           this.entrySet = (Set)Synchronized.typePreservingSet((Set)delegate().entrySet(), this.mutex);
/*      */         }
/*  565 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/*  571 */       if (o == this) {
/*  572 */         return true;
/*      */       }
/*  574 */       synchronized (this.mutex) {
/*  575 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  581 */       synchronized (this.mutex) {
/*  582 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @CheckForNull Object mutex) {
/*  591 */     if (multimap instanceof SynchronizedMultimap || multimap instanceof BaseImmutableMultimap) {
/*  592 */       return multimap;
/*      */     }
/*  594 */     return new SynchronizedMultimap<>(multimap, mutex);
/*      */   }
/*      */   static class SynchronizedMultimap<K, V> extends SynchronizedObject implements Multimap<K, V> { @CheckForNull
/*      */     transient Set<K> keySet; @CheckForNull
/*      */     transient Collection<V> valuesCollection;
/*      */     @CheckForNull
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     @CheckForNull
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     @CheckForNull
/*      */     transient Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     Multimap<K, V> delegate() {
/*  608 */       return (Multimap<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     SynchronizedMultimap(Multimap<K, V> delegate, @CheckForNull Object mutex) {
/*  612 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  617 */       synchronized (this.mutex) {
/*  618 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  624 */       synchronized (this.mutex) {
/*  625 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  631 */       synchronized (this.mutex) {
/*  632 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(@CheckForNull Object value) {
/*  638 */       synchronized (this.mutex) {
/*  639 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
/*  645 */       synchronized (this.mutex) {
/*  646 */         return delegate().containsEntry(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(@ParametricNullness K key) {
/*  652 */       synchronized (this.mutex) {
/*  653 */         return Synchronized.typePreservingCollection(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/*  659 */       synchronized (this.mutex) {
/*  660 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
/*  666 */       synchronized (this.mutex) {
/*  667 */         return delegate().putAll(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  673 */       synchronized (this.mutex) {
/*  674 */         return delegate().putAll(multimap);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  680 */       synchronized (this.mutex) {
/*  681 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/*  687 */       synchronized (this.mutex) {
/*  688 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(@CheckForNull Object key) {
/*  694 */       synchronized (this.mutex) {
/*  695 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  701 */       synchronized (this.mutex) {
/*  702 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  708 */       synchronized (this.mutex) {
/*  709 */         if (this.keySet == null) {
/*  710 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  712 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  718 */       synchronized (this.mutex) {
/*  719 */         if (this.valuesCollection == null) {
/*  720 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  722 */         return this.valuesCollection;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  728 */       synchronized (this.mutex) {
/*  729 */         if (this.entries == null) {
/*  730 */           this.entries = (Collection)Synchronized.typePreservingCollection((Collection)delegate().entries(), this.mutex);
/*      */         }
/*  732 */         return this.entries;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  738 */       synchronized (this.mutex) {
/*  739 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  745 */       synchronized (this.mutex) {
/*  746 */         if (this.asMap == null) {
/*  747 */           this.asMap = new Synchronized.SynchronizedAsMap<>(delegate().asMap(), this.mutex);
/*      */         }
/*  749 */         return this.asMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  755 */       synchronized (this.mutex) {
/*  756 */         if (this.keys == null) {
/*  757 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  759 */         return this.keys;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/*  765 */       if (o == this) {
/*  766 */         return true;
/*      */       }
/*  768 */       synchronized (this.mutex) {
/*  769 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  775 */       synchronized (this.mutex) {
/*  776 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @CheckForNull Object mutex) {
/*  785 */     if (multimap instanceof SynchronizedListMultimap || multimap instanceof BaseImmutableMultimap) {
/*  786 */       return multimap;
/*      */     }
/*  788 */     return new SynchronizedListMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   static final class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedListMultimap(ListMultimap<K, V> delegate, @CheckForNull Object mutex) {
/*  795 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     ListMultimap<K, V> delegate() {
/*  800 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  805 */       synchronized (this.mutex) {
/*  806 */         return Synchronized.list(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(@CheckForNull Object key) {
/*  812 */       synchronized (this.mutex) {
/*  813 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  819 */       synchronized (this.mutex) {
/*  820 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @CheckForNull Object mutex) {
/*  829 */     if (multimap instanceof SynchronizedSetMultimap || multimap instanceof BaseImmutableMultimap) {
/*  830 */       return multimap;
/*      */     }
/*  832 */     return new SynchronizedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> { @CheckForNull
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, @CheckForNull Object mutex) {
/*  840 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SetMultimap<K, V> delegate() {
/*  845 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  850 */       synchronized (this.mutex) {
/*  851 */         return Synchronized.set(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(@CheckForNull Object key) {
/*  857 */       synchronized (this.mutex) {
/*  858 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  864 */       synchronized (this.mutex) {
/*  865 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  871 */       synchronized (this.mutex) {
/*  872 */         if (this.entrySet == null) {
/*  873 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  875 */         return this.entrySet;
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @CheckForNull Object mutex) {
/*  885 */     if (multimap instanceof SynchronizedSortedSetMultimap) {
/*  886 */       return multimap;
/*      */     }
/*  888 */     return new SynchronizedSortedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   static final class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @CheckForNull Object mutex) {
/*  895 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSetMultimap<K, V> delegate() {
/*  900 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  905 */       synchronized (this.mutex) {
/*  906 */         return Synchronized.sortedSet(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(@CheckForNull Object key) {
/*  912 */       synchronized (this.mutex) {
/*  913 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  919 */       synchronized (this.mutex) {
/*  920 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super V> valueComparator() {
/*  927 */       synchronized (this.mutex) {
/*  928 */         return delegate().valueComparator();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @CheckForNull Object mutex) {
/*  937 */     if (collection instanceof SortedSet) {
/*  938 */       return sortedSet((SortedSet<E>)collection, mutex);
/*      */     }
/*  940 */     if (collection instanceof Set) {
/*  941 */       return set((Set<E>)collection, mutex);
/*      */     }
/*  943 */     if (collection instanceof List) {
/*  944 */       return list((List<E>)collection, mutex);
/*      */     }
/*  946 */     return collection(collection, mutex);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> Set<E> typePreservingSet(Set<E> set, @CheckForNull Object mutex) {
/*  951 */     if (set instanceof SortedSet) {
/*  952 */       return sortedSet((SortedSet<E>)set, mutex);
/*      */     }
/*  954 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   static final class SynchronizedAsMapEntries<K, V>
/*      */     extends SynchronizedSet<Map.Entry<K, Collection<V>>>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @CheckForNull Object mutex) {
/*  963 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/*  969 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Map.Entry<K, Collection<V>>>(super
/*  970 */           .iterator())
/*      */         {
/*      */           Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry) {
/*  973 */             return (Map.Entry)new ForwardingMapEntry<K, Collection<Collection<V>>>()
/*      */               {
/*      */                 protected Map.Entry<K, Collection<V>> delegate() {
/*  976 */                   return entry;
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public Collection<V> getValue() {
/*  981 */                   return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex);
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  992 */       synchronized (this.mutex) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  999 */         return ObjectArrays.toArrayImpl(delegate());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1006 */       synchronized (this.mutex) {
/* 1007 */         return ObjectArrays.toArrayImpl(delegate(), array);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 1013 */       synchronized (this.mutex) {
/* 1014 */         return Maps.containsEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/* 1020 */       synchronized (this.mutex) {
/* 1021 */         return Collections2.containsAllImpl(delegate(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/* 1027 */       if (o == this) {
/* 1028 */         return true;
/*      */       }
/* 1030 */       synchronized (this.mutex) {
/* 1031 */         return Sets.equalsImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 1037 */       synchronized (this.mutex) {
/* 1038 */         return Maps.removeEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/* 1044 */       synchronized (this.mutex) {
/* 1045 */         return Iterators.removeAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/* 1051 */       synchronized (this.mutex) {
/* 1052 */         return Iterators.retainAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <K, V> Map<K, V> map(Map<K, V> map, @CheckForNull Object mutex) {
/* 1062 */     return new SynchronizedMap<>(map, mutex);
/*      */   }
/*      */   static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> { @CheckForNull
/*      */     transient Set<K> keySet; @CheckForNull
/*      */     transient Collection<V> values;
/*      */     @CheckForNull
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMap(Map<K, V> delegate, @CheckForNull Object mutex) {
/* 1072 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, V> delegate() {
/* 1078 */       return (Map<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1083 */       synchronized (this.mutex) {
/* 1084 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1090 */       synchronized (this.mutex) {
/* 1091 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(@CheckForNull Object value) {
/* 1097 */       synchronized (this.mutex) {
/* 1098 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1104 */       synchronized (this.mutex) {
/* 1105 */         if (this.entrySet == null) {
/* 1106 */           this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */         }
/* 1108 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1114 */       synchronized (this.mutex) {
/* 1115 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/* 1122 */       synchronized (this.mutex) {
/* 1123 */         return delegate().get(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
/* 1130 */       synchronized (this.mutex) {
/* 1131 */         return delegate().getOrDefault(key, defaultValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1137 */       synchronized (this.mutex) {
/* 1138 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1144 */       synchronized (this.mutex) {
/* 1145 */         if (this.keySet == null) {
/* 1146 */           this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */         }
/* 1148 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(K key, V value) {
/* 1155 */       synchronized (this.mutex) {
/* 1156 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V putIfAbsent(K key, V value) {
/* 1163 */       synchronized (this.mutex) {
/* 1164 */         return delegate().putIfAbsent(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean replace(K key, V oldValue, V newValue) {
/* 1170 */       synchronized (this.mutex) {
/* 1171 */         return delegate().replace(key, oldValue, newValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V replace(K key, V value) {
/* 1178 */       synchronized (this.mutex) {
/* 1179 */         return delegate().replace(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 1185 */       synchronized (this.mutex) {
/* 1186 */         return delegate().computeIfAbsent(key, mappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1199 */       synchronized (this.mutex) {
/* 1200 */         return delegate().computeIfPresent(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1209 */       synchronized (this.mutex) {
/* 1210 */         return delegate().compute(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 1221 */       synchronized (this.mutex) {
/* 1222 */         return delegate().merge(key, value, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 1228 */       synchronized (this.mutex) {
/* 1229 */         delegate().putAll(map);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 1235 */       synchronized (this.mutex) {
/* 1236 */         delegate().replaceAll(function);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/* 1243 */       synchronized (this.mutex) {
/* 1244 */         return delegate().remove(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 1250 */       synchronized (this.mutex) {
/* 1251 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1257 */       synchronized (this.mutex) {
/* 1258 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1264 */       synchronized (this.mutex) {
/* 1265 */         if (this.values == null) {
/* 1266 */           this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/* 1268 */         return this.values;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object o) {
/* 1274 */       if (o == this) {
/* 1275 */         return true;
/*      */       }
/* 1277 */       synchronized (this.mutex) {
/* 1278 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1284 */       synchronized (this.mutex) {
/* 1285 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @CheckForNull Object mutex) {
/* 1294 */     return new SynchronizedSortedMap<>(sortedMap, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedMap(SortedMap<K, V> delegate, @CheckForNull Object mutex) {
/* 1301 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> delegate() {
/* 1306 */       return (SortedMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 1312 */       synchronized (this.mutex) {
/* 1313 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1319 */       synchronized (this.mutex) {
/* 1320 */         return delegate().firstKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1326 */       synchronized (this.mutex) {
/* 1327 */         return Synchronized.sortedMap(delegate().headMap(toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1333 */       synchronized (this.mutex) {
/* 1334 */         return delegate().lastKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1340 */       synchronized (this.mutex) {
/* 1341 */         return Synchronized.sortedMap(delegate().subMap(fromKey, toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1347 */       synchronized (this.mutex) {
/* 1348 */         return Synchronized.sortedMap(delegate().tailMap(fromKey), this.mutex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @CheckForNull Object mutex) {
/* 1357 */     if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
/* 1358 */       return bimap;
/*      */     }
/* 1360 */     return new SynchronizedBiMap<>(bimap, mutex, null);
/*      */   }
/*      */   static final class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable { @CheckForNull
/*      */     private transient Set<V> valueSet;
/*      */     @CheckForNull
/*      */     @RetainedWith
/*      */     private transient BiMap<V, K> inverse;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedBiMap(BiMap<K, V> delegate, @CheckForNull Object mutex, @CheckForNull BiMap<V, K> inverse) {
/* 1370 */       super(delegate, mutex);
/* 1371 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     BiMap<K, V> delegate() {
/* 1376 */       return (BiMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1381 */       synchronized (this.mutex) {
/* 1382 */         if (this.valueSet == null) {
/* 1383 */           this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */         }
/* 1385 */         return this.valueSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
/* 1392 */       synchronized (this.mutex) {
/* 1393 */         return delegate().forcePut(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1399 */       synchronized (this.mutex) {
/* 1400 */         if (this.inverse == null) {
/* 1401 */           this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */         }
/* 1403 */         return this.inverse;
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class SynchronizedAsMap<K, V> extends SynchronizedMap<K, Collection<V>> {
/*      */     @CheckForNull
/*      */     transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */     @CheckForNull
/*      */     transient Collection<Collection<V>> asMapValues;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMap(Map<K, Collection<V>> delegate, @CheckForNull Object mutex) {
/* 1416 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Collection<V> get(@CheckForNull Object key) {
/* 1422 */       synchronized (this.mutex) {
/* 1423 */         Collection<V> collection = super.get(key);
/* 1424 */         return (collection == null) ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1430 */       synchronized (this.mutex) {
/* 1431 */         if (this.asMapEntrySet == null) {
/* 1432 */           this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries<>(delegate().entrySet(), this.mutex);
/*      */         }
/* 1434 */         return this.asMapEntrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Collection<V>> values() {
/* 1440 */       synchronized (this.mutex) {
/* 1441 */         if (this.asMapValues == null) {
/* 1442 */           this.asMapValues = new Synchronized.SynchronizedAsMapValues<>(delegate().values(), this.mutex);
/*      */         }
/* 1444 */         return this.asMapValues;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsValue(@CheckForNull Object o) {
/* 1451 */       return values().contains(o);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class SynchronizedAsMapValues<V>
/*      */     extends SynchronizedCollection<Collection<V>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapValues(Collection<Collection<V>> delegate, @CheckForNull Object mutex) {
/* 1460 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Collection<V>> iterator() {
/* 1466 */       return new TransformedIterator<Collection<V>, Collection<V>>(super.iterator())
/*      */         {
/*      */           Collection<V> transform(Collection<V> from) {
/* 1469 */             return Synchronized.typePreservingCollection(from, Synchronized.SynchronizedAsMapValues.this.mutex);
/*      */           }
/*      */         };
/*      */     } }
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static final class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
/*      */     @CheckForNull
/*      */     transient NavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedNavigableSet(NavigableSet<E> delegate, @CheckForNull Object mutex) {
/* 1482 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<E> delegate() {
/* 1487 */       return (NavigableSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E ceiling(E e) {
/* 1493 */       synchronized (this.mutex) {
/* 1494 */         return delegate().ceiling(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1500 */       return delegate().descendingIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1507 */       synchronized (this.mutex) {
/* 1508 */         if (this.descendingSet == null) {
/* 1509 */           NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
/* 1510 */           this.descendingSet = dS;
/* 1511 */           return dS;
/*      */         } 
/* 1513 */         return this.descendingSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E floor(E e) {
/* 1520 */       synchronized (this.mutex) {
/* 1521 */         return delegate().floor(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1527 */       synchronized (this.mutex) {
/* 1528 */         return Synchronized.navigableSet(delegate().headSet(toElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1534 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E higher(E e) {
/* 1540 */       synchronized (this.mutex) {
/* 1541 */         return delegate().higher(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E lower(E e) {
/* 1548 */       synchronized (this.mutex) {
/* 1549 */         return delegate().lower(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollFirst() {
/* 1556 */       synchronized (this.mutex) {
/* 1557 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollLast() {
/* 1564 */       synchronized (this.mutex) {
/* 1565 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1572 */       synchronized (this.mutex) {
/* 1573 */         return Synchronized.navigableSet(
/* 1574 */             delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1580 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1585 */       synchronized (this.mutex) {
/* 1586 */         return Synchronized.navigableSet(delegate().tailSet(fromElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1592 */       return tailSet(fromElement, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @CheckForNull Object mutex) {
/* 1601 */     return new SynchronizedNavigableSet<>(navigableSet, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
/* 1606 */     return navigableSet(navigableSet, null);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
/* 1612 */     return navigableMap(navigableMap, null);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @CheckForNull Object mutex) {
/* 1618 */     return new SynchronizedNavigableMap<>(navigableMap, mutex);
/*      */   }
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static final class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { @CheckForNull
/*      */     transient NavigableSet<K> descendingKeySet;
/*      */     @CheckForNull
/*      */     transient NavigableMap<K, V> descendingMap;
/*      */     
/*      */     SynchronizedNavigableMap(NavigableMap<K, V> delegate, @CheckForNull Object mutex) {
/* 1628 */       super(delegate, mutex);
/*      */     }
/*      */     @CheckForNull
/*      */     transient NavigableSet<K> navigableKeySet; private static final long serialVersionUID = 0L;
/*      */     NavigableMap<K, V> delegate() {
/* 1633 */       return (NavigableMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 1639 */       synchronized (this.mutex) {
/* 1640 */         return Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceilingKey(K key) {
/* 1647 */       synchronized (this.mutex) {
/* 1648 */         return delegate().ceilingKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1656 */       synchronized (this.mutex) {
/* 1657 */         if (this.descendingKeySet == null) {
/* 1658 */           return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
/*      */         }
/* 1660 */         return this.descendingKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1668 */       synchronized (this.mutex) {
/* 1669 */         if (this.descendingMap == null) {
/* 1670 */           return this.descendingMap = Synchronized.<K, V>navigableMap(delegate().descendingMap(), this.mutex);
/*      */         }
/* 1672 */         return this.descendingMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> firstEntry() {
/* 1679 */       synchronized (this.mutex) {
/* 1680 */         return Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 1687 */       synchronized (this.mutex) {
/* 1688 */         return Synchronized.nullableSynchronizedEntry(delegate().floorEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floorKey(K key) {
/* 1695 */       synchronized (this.mutex) {
/* 1696 */         return delegate().floorKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1702 */       synchronized (this.mutex) {
/* 1703 */         return Synchronized.navigableMap(delegate().headMap(toKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1709 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 1715 */       synchronized (this.mutex) {
/* 1716 */         return Synchronized.nullableSynchronizedEntry(delegate().higherEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higherKey(K key) {
/* 1723 */       synchronized (this.mutex) {
/* 1724 */         return delegate().higherKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lastEntry() {
/* 1731 */       synchronized (this.mutex) {
/* 1732 */         return Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 1739 */       synchronized (this.mutex) {
/* 1740 */         return Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lowerKey(K key) {
/* 1747 */       synchronized (this.mutex) {
/* 1748 */         return delegate().lowerKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1754 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1761 */       synchronized (this.mutex) {
/* 1762 */         if (this.navigableKeySet == null) {
/* 1763 */           return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
/*      */         }
/* 1765 */         return this.navigableKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 1772 */       synchronized (this.mutex) {
/* 1773 */         return Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 1780 */       synchronized (this.mutex) {
/* 1781 */         return Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1788 */       synchronized (this.mutex) {
/* 1789 */         return Synchronized.navigableMap(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1795 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1800 */       synchronized (this.mutex) {
/* 1801 */         return Synchronized.navigableMap(delegate().tailMap(fromKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1807 */       return tailMap(fromKey, true);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @GwtIncompatible
/*      */   private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@CheckForNull Map.Entry<K, V> entry, @CheckForNull Object mutex) {
/* 1818 */     if (entry == null) {
/* 1819 */       return null;
/*      */     }
/* 1821 */     return new SynchronizedEntry<>(entry, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static final class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedEntry(Map.Entry<K, V> delegate, @CheckForNull Object mutex) {
/* 1829 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map.Entry<K, V> delegate() {
/* 1835 */       return (Map.Entry<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object obj) {
/* 1840 */       synchronized (this.mutex) {
/* 1841 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1847 */       synchronized (this.mutex) {
/* 1848 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 1854 */       synchronized (this.mutex) {
/* 1855 */         return delegate().getKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 1861 */       synchronized (this.mutex) {
/* 1862 */         return delegate().getValue();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/* 1868 */       synchronized (this.mutex) {
/* 1869 */         return delegate().setValue(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Queue<E> queue(Queue<E> queue, @CheckForNull Object mutex) {
/* 1877 */     return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue<>(queue, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedQueue(Queue<E> delegate, @CheckForNull Object mutex) {
/* 1884 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Queue<E> delegate() {
/* 1889 */       return (Queue<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public E element() {
/* 1894 */       synchronized (this.mutex) {
/* 1895 */         return delegate().element();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offer(E e) {
/* 1901 */       synchronized (this.mutex) {
/* 1902 */         return delegate().offer(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E peek() {
/* 1909 */       synchronized (this.mutex) {
/* 1910 */         return delegate().peek();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E poll() {
/* 1917 */       synchronized (this.mutex) {
/* 1918 */         return delegate().poll();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove() {
/* 1924 */       synchronized (this.mutex) {
/* 1925 */         return delegate().remove();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Deque<E> deque(Deque<E> deque, @CheckForNull Object mutex) {
/* 1933 */     return new SynchronizedDeque<>(deque, mutex);
/*      */   }
/*      */   
/*      */   static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedDeque(Deque<E> delegate, @CheckForNull Object mutex) {
/* 1940 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Deque<E> delegate() {
/* 1945 */       return (Deque<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFirst(E e) {
/* 1950 */       synchronized (this.mutex) {
/* 1951 */         delegate().addFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void addLast(E e) {
/* 1957 */       synchronized (this.mutex) {
/* 1958 */         delegate().addLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerFirst(E e) {
/* 1964 */       synchronized (this.mutex) {
/* 1965 */         return delegate().offerFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerLast(E e) {
/* 1971 */       synchronized (this.mutex) {
/* 1972 */         return delegate().offerLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeFirst() {
/* 1978 */       synchronized (this.mutex) {
/* 1979 */         return delegate().removeFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeLast() {
/* 1985 */       synchronized (this.mutex) {
/* 1986 */         return delegate().removeLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollFirst() {
/* 1993 */       synchronized (this.mutex) {
/* 1994 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollLast() {
/* 2001 */       synchronized (this.mutex) {
/* 2002 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getFirst() {
/* 2008 */       synchronized (this.mutex) {
/* 2009 */         return delegate().getFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getLast() {
/* 2015 */       synchronized (this.mutex) {
/* 2016 */         return delegate().getLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E peekFirst() {
/* 2023 */       synchronized (this.mutex) {
/* 2024 */         return delegate().peekFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E peekLast() {
/* 2031 */       synchronized (this.mutex) {
/* 2032 */         return delegate().peekLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeFirstOccurrence(@CheckForNull Object o) {
/* 2038 */       synchronized (this.mutex) {
/* 2039 */         return delegate().removeFirstOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeLastOccurrence(@CheckForNull Object o) {
/* 2045 */       synchronized (this.mutex) {
/* 2046 */         return delegate().removeLastOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void push(E e) {
/* 2052 */       synchronized (this.mutex) {
/* 2053 */         delegate().push(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pop() {
/* 2059 */       synchronized (this.mutex) {
/* 2060 */         return delegate().pop();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 2066 */       synchronized (this.mutex) {
/* 2067 */         return delegate().descendingIterator();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <R, C, V> Table<R, C, V> table(Table<R, C, V> table, @CheckForNull Object mutex) {
/* 2076 */     return new SynchronizedTable<>(table, mutex);
/*      */   }
/*      */   
/*      */   static final class SynchronizedTable<R, C, V>
/*      */     extends SynchronizedObject
/*      */     implements Table<R, C, V>
/*      */   {
/*      */     SynchronizedTable(Table<R, C, V> delegate, @CheckForNull Object mutex) {
/* 2084 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Table<R, C, V> delegate() {
/* 2090 */       return (Table<R, C, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 2095 */       synchronized (this.mutex) {
/* 2096 */         return delegate().contains(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsRow(@CheckForNull Object rowKey) {
/* 2102 */       synchronized (this.mutex) {
/* 2103 */         return delegate().containsRow(rowKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsColumn(@CheckForNull Object columnKey) {
/* 2109 */       synchronized (this.mutex) {
/* 2110 */         return delegate().containsColumn(columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(@CheckForNull Object value) {
/* 2116 */       synchronized (this.mutex) {
/* 2117 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 2124 */       synchronized (this.mutex) {
/* 2125 */         return delegate().get(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2131 */       synchronized (this.mutex) {
/* 2132 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2138 */       synchronized (this.mutex) {
/* 2139 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2145 */       synchronized (this.mutex) {
/* 2146 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 2156 */       synchronized (this.mutex) {
/* 2157 */         return delegate().put(rowKey, columnKey, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 2163 */       synchronized (this.mutex) {
/* 2164 */         delegate().putAll(table);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 2171 */       synchronized (this.mutex) {
/* 2172 */         return delegate().remove(rowKey, columnKey);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<C, V> row(@ParametricNullness R rowKey) {
/* 2178 */       synchronized (this.mutex) {
/* 2179 */         return Synchronized.map(delegate().row(rowKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<R, V> column(@ParametricNullness C columnKey) {
/* 2185 */       synchronized (this.mutex) {
/* 2186 */         return Synchronized.map(delegate().column(columnKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 2192 */       synchronized (this.mutex) {
/* 2193 */         return Synchronized.set(delegate().cellSet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<R> rowKeySet() {
/* 2199 */       synchronized (this.mutex) {
/* 2200 */         return Synchronized.set(delegate().rowKeySet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<C> columnKeySet() {
/* 2206 */       synchronized (this.mutex) {
/* 2207 */         return Synchronized.set(delegate().columnKeySet(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 2213 */       synchronized (this.mutex) {
/* 2214 */         return Synchronized.collection(delegate().values(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<R, Map<C, V>> rowMap() {
/* 2220 */       synchronized (this.mutex) {
/* 2221 */         return Synchronized.map(
/* 2222 */             Maps.transformValues(
/* 2223 */               delegate().rowMap(), new Function<Map<C, V>, Map<C, V>>()
/*      */               {
/*      */                 public Map<C, V> apply(Map<C, V> t)
/*      */                 {
/* 2227 */                   return Synchronized.map(t, Synchronized.SynchronizedTable.this.mutex);
/*      */                 }
/*      */               }), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<C, Map<R, V>> columnMap() {
/* 2236 */       synchronized (this.mutex) {
/* 2237 */         return Synchronized.map(
/* 2238 */             Maps.transformValues(
/* 2239 */               delegate().columnMap(), new Function<Map<R, V>, Map<R, V>>()
/*      */               {
/*      */                 public Map<R, V> apply(Map<R, V> t)
/*      */                 {
/* 2243 */                   return Synchronized.map(t, Synchronized.SynchronizedTable.this.mutex);
/*      */                 }
/*      */               }), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2252 */       synchronized (this.mutex) {
/* 2253 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object obj) {
/* 2259 */       if (this == obj) {
/* 2260 */         return true;
/*      */       }
/* 2262 */       synchronized (this.mutex) {
/* 2263 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Synchronized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */