/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Objects;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
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
/*      */ @GwtCompatible
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  121 */     Preconditions.checkArgument(map.isEmpty());
/*  122 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  127 */     this.map = map;
/*  128 */     this.totalSize = 0;
/*  129 */     for (Collection<V> values : map.values()) {
/*  130 */       Preconditions.checkArgument(!values.isEmpty());
/*  131 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createUnmodifiableEmptyCollection() {
/*  141 */     return unmodifiableCollectionSubclass(createCollection());
/*      */   }
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
/*      */   Collection<V> createCollection(@ParametricNullness K key) {
/*  165 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  169 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  176 */     return this.totalSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@CheckForNull Object key) {
/*  181 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/*  188 */     Collection<V> collection = this.map.get(key);
/*  189 */     if (collection == null) {
/*  190 */       collection = createCollection(key);
/*  191 */       if (collection.add(value)) {
/*  192 */         this.totalSize++;
/*  193 */         this.map.put(key, collection);
/*  194 */         return true;
/*      */       } 
/*  196 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  198 */     if (collection.add(value)) {
/*  199 */       this.totalSize++;
/*  200 */       return true;
/*      */     } 
/*  202 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@ParametricNullness K key) {
/*  207 */     Collection<V> collection = this.map.get(key);
/*  208 */     if (collection == null) {
/*  209 */       collection = createCollection(key);
/*  210 */       this.map.put(key, collection);
/*      */     } 
/*  212 */     return collection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  224 */     Iterator<? extends V> iterator = values.iterator();
/*  225 */     if (!iterator.hasNext()) {
/*  226 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  230 */     Collection<V> collection = getOrCreateCollection(key);
/*  231 */     Collection<V> oldValues = createCollection();
/*  232 */     oldValues.addAll(collection);
/*      */     
/*  234 */     this.totalSize -= collection.size();
/*  235 */     collection.clear();
/*      */     
/*  237 */     while (iterator.hasNext()) {
/*  238 */       if (collection.add(iterator.next())) {
/*  239 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  243 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@CheckForNull Object key) {
/*  253 */     Collection<V> collection = this.map.remove(key);
/*      */     
/*  255 */     if (collection == null) {
/*  256 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  259 */     Collection<V> output = createCollection();
/*  260 */     output.addAll(collection);
/*  261 */     this.totalSize -= collection.size();
/*  262 */     collection.clear();
/*      */     
/*  264 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */ 
/*      */   
/*      */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  269 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  275 */     for (Collection<V> collection : this.map.values()) {
/*  276 */       collection.clear();
/*      */     }
/*  278 */     this.map.clear();
/*  279 */     this.totalSize = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> get(@ParametricNullness K key) {
/*  291 */     Collection<V> collection = this.map.get(key);
/*  292 */     if (collection == null) {
/*  293 */       collection = createCollection(key);
/*      */     }
/*  295 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
/*  303 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */   
/*      */   final List<V> wrapList(@ParametricNullness K key, List<V> list, @CheckForNull WrappedCollection ancestor) {
/*  308 */     return (list instanceof RandomAccess) ? 
/*  309 */       new RandomAccessWrappedList(this, key, list, ancestor) : 
/*  310 */       new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     @ParametricNullness
/*      */     final K key;
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, @CheckForNull Collection<V> delegate, WrappedCollection ancestor) {
/*  339 */       this.key = key;
/*  340 */       this.delegate = delegate;
/*  341 */       this.ancestor = ancestor;
/*  342 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  353 */       if (this.ancestor != null) {
/*  354 */         this.ancestor.refreshIfEmpty();
/*  355 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  356 */           throw new ConcurrentModificationException();
/*      */         }
/*  358 */       } else if (this.delegate.isEmpty()) {
/*  359 */         Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
/*  360 */         if (newDelegate != null) {
/*  361 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  371 */       if (this.ancestor != null) {
/*  372 */         this.ancestor.removeIfEmpty();
/*  373 */       } else if (this.delegate.isEmpty()) {
/*  374 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     @ParametricNullness
/*      */     K getKey() {
/*  380 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  390 */       if (this.ancestor != null) {
/*  391 */         this.ancestor.addToMap();
/*      */       } else {
/*  393 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  399 */       refreshIfEmpty();
/*  400 */       return this.delegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/*  405 */       if (object == this) {
/*  406 */         return true;
/*      */       }
/*  408 */       refreshIfEmpty();
/*  409 */       return this.delegate.equals(object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  414 */       refreshIfEmpty();
/*  415 */       return this.delegate.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  420 */       refreshIfEmpty();
/*  421 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  425 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  430 */       refreshIfEmpty();
/*  431 */       return new WrappedIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/*  436 */       refreshIfEmpty();
/*  437 */       return this.delegate.spliterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  443 */       final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  446 */         this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  450 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  457 */         AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
/*  458 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  459 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  465 */         validateIterator();
/*  466 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       @ParametricNullness
/*      */       public V next() {
/*  472 */         validateIterator();
/*  473 */         return this.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  478 */         this.delegateIterator.remove();
/*  479 */         AbstractMapBasedMultimap.this.totalSize--;
/*  480 */         AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  484 */         validateIterator();
/*  485 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(@ParametricNullness V value) {
/*  491 */       refreshIfEmpty();
/*  492 */       boolean wasEmpty = this.delegate.isEmpty();
/*  493 */       boolean changed = this.delegate.add(value);
/*  494 */       if (changed) {
/*  495 */         AbstractMapBasedMultimap.this.totalSize++;
/*  496 */         if (wasEmpty) {
/*  497 */           addToMap();
/*      */         }
/*      */       } 
/*  500 */       return changed;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     WrappedCollection getAncestor() {
/*  505 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  512 */       if (collection.isEmpty()) {
/*  513 */         return false;
/*      */       }
/*  515 */       int oldSize = size();
/*  516 */       boolean changed = this.delegate.addAll(collection);
/*  517 */       if (changed) {
/*  518 */         int newSize = this.delegate.size();
/*  519 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  520 */         if (oldSize == 0) {
/*  521 */           addToMap();
/*      */         }
/*      */       } 
/*  524 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/*  529 */       refreshIfEmpty();
/*  530 */       return this.delegate.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  535 */       refreshIfEmpty();
/*  536 */       return this.delegate.containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  541 */       int oldSize = size();
/*  542 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  545 */       this.delegate.clear();
/*  546 */       AbstractMapBasedMultimap.this.totalSize -= oldSize;
/*  547 */       removeIfEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/*  552 */       refreshIfEmpty();
/*  553 */       boolean changed = this.delegate.remove(o);
/*  554 */       if (changed) {
/*  555 */         AbstractMapBasedMultimap.this.totalSize--;
/*  556 */         removeIfEmpty();
/*      */       } 
/*  558 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  563 */       if (c.isEmpty()) {
/*  564 */         return false;
/*      */       }
/*  566 */       int oldSize = size();
/*  567 */       boolean changed = this.delegate.removeAll(c);
/*  568 */       if (changed) {
/*  569 */         int newSize = this.delegate.size();
/*  570 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  571 */         removeIfEmpty();
/*      */       } 
/*  573 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  578 */       Preconditions.checkNotNull(c);
/*  579 */       int oldSize = size();
/*  580 */       boolean changed = this.delegate.retainAll(c);
/*  581 */       if (changed) {
/*  582 */         int newSize = this.delegate.size();
/*  583 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  584 */         removeIfEmpty();
/*      */       } 
/*  586 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
/*  592 */     return (collection instanceof List) ? (
/*  593 */       (List<E>)collection).listIterator() : 
/*  594 */       collection.iterator();
/*      */   }
/*      */   
/*      */   class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V> {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  601 */       super(key, delegate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  606 */       if (c.isEmpty()) {
/*  607 */         return false;
/*      */       }
/*  609 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  614 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  615 */       if (changed) {
/*  616 */         int newSize = this.delegate.size();
/*  617 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  618 */         removeIfEmpty();
/*      */       } 
/*  620 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*      */     WrappedSortedSet(K key, @CheckForNull SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  631 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  635 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super V> comparator() {
/*  641 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V first() {
/*  647 */       refreshIfEmpty();
/*  648 */       return getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V last() {
/*  654 */       refreshIfEmpty();
/*  655 */       return getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(@ParametricNullness V toElement) {
/*  660 */       refreshIfEmpty();
/*  661 */       return new WrappedSortedSet(
/*  662 */           getKey(), 
/*  663 */           getSortedSetDelegate().headSet(toElement), 
/*  664 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(@ParametricNullness V fromElement, @ParametricNullness V toElement) {
/*  669 */       refreshIfEmpty();
/*  670 */       return new WrappedSortedSet(
/*  671 */           getKey(), 
/*  672 */           getSortedSetDelegate().subSet(fromElement, toElement), 
/*  673 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(@ParametricNullness V fromElement) {
/*  678 */       refreshIfEmpty();
/*  679 */       return new WrappedSortedSet(
/*  680 */           getKey(), 
/*  681 */           getSortedSetDelegate().tailSet(fromElement), 
/*  682 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V>
/*      */   {
/*      */     WrappedNavigableSet(K key, @CheckForNull NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  692 */       super(key, delegate, ancestor);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<V> getSortedSetDelegate() {
/*  697 */       return (NavigableSet<V>)super.getSortedSetDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V lower(@ParametricNullness V v) {
/*  703 */       return getSortedSetDelegate().lower(v);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V floor(@ParametricNullness V v) {
/*  709 */       return getSortedSetDelegate().floor(v);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V ceiling(@ParametricNullness V v) {
/*  715 */       return getSortedSetDelegate().ceiling(v);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V higher(@ParametricNullness V v) {
/*  721 */       return getSortedSetDelegate().higher(v);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V pollFirst() {
/*  727 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V pollLast() {
/*  733 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */     
/*      */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
/*  737 */       return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> descendingSet() {
/*  742 */       return wrap(getSortedSetDelegate().descendingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> descendingIterator() {
/*  747 */       return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> subSet(@ParametricNullness V fromElement, boolean fromInclusive, @ParametricNullness V toElement, boolean toInclusive) {
/*  756 */       return wrap(
/*  757 */           getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> headSet(@ParametricNullness V toElement, boolean inclusive) {
/*  762 */       return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> tailSet(@ParametricNullness V fromElement, boolean inclusive) {
/*  767 */       return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V>
/*      */   {
/*      */     WrappedList(K key, @CheckForNull List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  776 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  780 */       return (List<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  785 */       if (c.isEmpty()) {
/*  786 */         return false;
/*      */       }
/*  788 */       int oldSize = size();
/*  789 */       boolean changed = getListDelegate().addAll(index, c);
/*  790 */       if (changed) {
/*  791 */         int newSize = getDelegate().size();
/*  792 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  793 */         if (oldSize == 0) {
/*  794 */           addToMap();
/*      */         }
/*      */       } 
/*  797 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V get(int index) {
/*  803 */       refreshIfEmpty();
/*  804 */       return getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V set(int index, @ParametricNullness V element) {
/*  810 */       refreshIfEmpty();
/*  811 */       return getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, @ParametricNullness V element) {
/*  816 */       refreshIfEmpty();
/*  817 */       boolean wasEmpty = getDelegate().isEmpty();
/*  818 */       getListDelegate().add(index, element);
/*  819 */       AbstractMapBasedMultimap.this.totalSize++;
/*  820 */       if (wasEmpty) {
/*  821 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V remove(int index) {
/*  828 */       refreshIfEmpty();
/*  829 */       V value = getListDelegate().remove(index);
/*  830 */       AbstractMapBasedMultimap.this.totalSize--;
/*  831 */       removeIfEmpty();
/*  832 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(@CheckForNull Object o) {
/*  837 */       refreshIfEmpty();
/*  838 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(@CheckForNull Object o) {
/*  843 */       refreshIfEmpty();
/*  844 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  849 */       refreshIfEmpty();
/*  850 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  855 */       refreshIfEmpty();
/*  856 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  861 */       refreshIfEmpty();
/*  862 */       return AbstractMapBasedMultimap.this.wrapList(
/*  863 */           getKey(), 
/*  864 */           getListDelegate().subList(fromIndex, toIndex), 
/*  865 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  873 */         super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  877 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*  882 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */ 
/*      */       
/*      */       @ParametricNullness
/*      */       public V previous() {
/*  888 */         return getDelegateListIterator().previous();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*  893 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  898 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public void set(@ParametricNullness V value) {
/*  903 */         getDelegateListIterator().set(value);
/*      */       }
/*      */ 
/*      */       
/*      */       public void add(@ParametricNullness V value) {
/*  908 */         boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
/*  909 */         getDelegateListIterator().add(value);
/*  910 */         AbstractMapBasedMultimap.this.totalSize++;
/*  911 */         if (wasEmpty) {
/*  912 */           AbstractMapBasedMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(@ParametricNullness AbstractMapBasedMultimap this$0, K key, @CheckForNull List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  925 */       super(this$0, key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   Set<K> createKeySet() {
/*  931 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   final Set<K> createMaybeNavigableKeySet() {
/*  935 */     if (this.map instanceof NavigableMap)
/*  936 */       return new NavigableKeySet((NavigableMap<K, Collection<V>>)this.map); 
/*  937 */     if (this.map instanceof SortedMap) {
/*  938 */       return new SortedKeySet((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/*  940 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>>
/*      */   {
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  947 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  952 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*  953 */       return new Iterator<K>() {
/*      */           @CheckForNull
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  958 */             return entryIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           @ParametricNullness
/*      */           public K next() {
/*  964 */             this.entry = entryIterator.next();
/*  965 */             return this.entry.getKey();
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  970 */             Preconditions.checkState((this.entry != null), "no calls to next() since the last call to remove()");
/*  971 */             Collection<V> collection = this.entry.getValue();
/*  972 */             entryIterator.remove();
/*  973 */             AbstractMapBasedMultimap.this.totalSize -= collection.size();
/*  974 */             collection.clear();
/*  975 */             this.entry = null;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  984 */       return map().keySet().spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key) {
/*  989 */       int count = 0;
/*  990 */       Collection<V> collection = map().remove(key);
/*  991 */       if (collection != null) {
/*  992 */         count = collection.size();
/*  993 */         collection.clear();
/*  994 */         AbstractMapBasedMultimap.this.totalSize -= count;
/*      */       } 
/*  996 */       return (count > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1001 */       Iterators.clear(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/* 1006 */       return map().keySet().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1011 */       return (this == object || map().keySet().equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1016 */       return map().keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet
/*      */     implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/* 1024 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1028 */       return (SortedMap<K, Collection<V>>)map();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 1034 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K first() {
/* 1040 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(@ParametricNullness K toElement) {
/* 1045 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K last() {
/* 1051 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
/* 1056 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(@ParametricNullness K fromElement) {
/* 1061 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */   
/*      */   private final class NavigableKeySet
/*      */     extends SortedKeySet implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
/* 1068 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1073 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lower(@ParametricNullness K k) {
/* 1079 */       return sortedMap().lowerKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floor(@ParametricNullness K k) {
/* 1085 */       return sortedMap().floorKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceiling(@ParametricNullness K k) {
/* 1091 */       return sortedMap().ceilingKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higher(@ParametricNullness K k) {
/* 1097 */       return sortedMap().higherKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K pollFirst() {
/* 1103 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K pollLast() {
/* 1109 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 1114 */       return new NavigableKeySet(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 1119 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(@ParametricNullness K toElement) {
/* 1124 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(@ParametricNullness K toElement, boolean inclusive) {
/* 1129 */       return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
/* 1135 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(@ParametricNullness K fromElement, boolean fromInclusive, @ParametricNullness K toElement, boolean toInclusive) {
/* 1144 */       return new NavigableKeySet(
/* 1145 */           sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(@ParametricNullness K fromElement) {
/* 1150 */       return tailSet(fromElement, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(@ParametricNullness K fromElement, boolean inclusive) {
/* 1155 */       return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void removeValuesForKey(@CheckForNull Object key) {
/* 1161 */     Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
/*      */     
/* 1163 */     if (collection != null) {
/* 1164 */       int count = collection.size();
/* 1165 */       collection.clear();
/* 1166 */       this.totalSize -= count;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Itr<T>
/*      */     implements Iterator<T>
/*      */   {
/* 1177 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator(); @CheckForNull
/* 1178 */     K key = null; @CheckForNull
/* 1179 */     Collection<V> collection = null;
/* 1180 */     Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     abstract T output(@ParametricNullness K param1K, @ParametricNullness V param1V);
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1187 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T next() {
/* 1193 */       if (!this.valueIterator.hasNext()) {
/* 1194 */         Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
/* 1195 */         this.key = mapEntry.getKey();
/* 1196 */         this.collection = mapEntry.getValue();
/* 1197 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1203 */       return output(NullnessCasts.uncheckedCastNullableTToT(this.key), this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1208 */       this.valueIterator.remove();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1213 */       if (((Collection)Objects.<Collection>requireNonNull(this.collection)).isEmpty()) {
/* 1214 */         this.keyIterator.remove();
/*      */       }
/* 1216 */       AbstractMapBasedMultimap.this.totalSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 1228 */     return super.values();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<V> createValues() {
/* 1233 */     return new AbstractMultimap.Values(this);
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1238 */     return new Itr<V>(this)
/*      */       {
/*      */         @ParametricNullness
/*      */         V output(@ParametricNullness K key, @ParametricNullness V value) {
/* 1242 */           return value;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<V> valueSpliterator() {
/* 1249 */     return CollectSpliterators.flatMap(this.map
/* 1250 */         .values().spliterator(), Collection::spliterator, 64, size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Multiset<K> createKeys() {
/* 1261 */     return new Multimaps.Keys<>(this);
/*      */   }
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
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1275 */     return super.entries();
/*      */   }
/*      */ 
/*      */   
/*      */   Collection<Map.Entry<K, V>> createEntries() {
/* 1280 */     if (this instanceof SetMultimap) {
/* 1281 */       return new AbstractMultimap.EntrySet(this);
/*      */     }
/* 1283 */     return new AbstractMultimap.Entries(this);
/*      */   }
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
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1297 */     return new Itr<Map.Entry<K, V>>(this)
/*      */       {
/*      */         Map.Entry<K, V> output(@ParametricNullness K key, @ParametricNullness V value) {
/* 1300 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1307 */     return CollectSpliterators.flatMap(this.map
/* 1308 */         .entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }64, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1316 */         size());
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1321 */     Preconditions.checkNotNull(action);
/* 1322 */     this.map.forEach((key, valueCollection) -> valueCollection.forEach(()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, Collection<V>> createAsMap() {
/* 1328 */     return new AsMap(this.map);
/*      */   }
/*      */   
/*      */   final Map<K, Collection<V>> createMaybeNavigableAsMap() {
/* 1332 */     if (this.map instanceof NavigableMap)
/* 1333 */       return new NavigableAsMap((NavigableMap<K, Collection<V>>)this.map); 
/* 1334 */     if (this.map instanceof SortedMap) {
/* 1335 */       return new SortedAsMap((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/* 1337 */     return new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1350 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1355 */       return new AsMapEntries();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1362 */       return Maps.safeContainsKey(this.submap, key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Collection<V> get(@CheckForNull Object key) {
/* 1368 */       Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
/* 1369 */       if (collection == null) {
/* 1370 */         return null;
/*      */       }
/*      */       
/* 1373 */       K k = (K)key;
/* 1374 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1379 */       return AbstractMapBasedMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1384 */       return this.submap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Collection<V> remove(@CheckForNull Object key) {
/* 1390 */       Collection<V> collection = this.submap.remove(key);
/* 1391 */       if (collection == null) {
/* 1392 */         return null;
/*      */       }
/*      */       
/* 1395 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1396 */       output.addAll(collection);
/* 1397 */       AbstractMapBasedMultimap.this.totalSize -= collection.size();
/* 1398 */       collection.clear();
/* 1399 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1404 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1409 */       return this.submap.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1414 */       return this.submap.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1419 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1420 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1422 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1427 */       K key = entry.getKey();
/* 1428 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1435 */         return AbstractMapBasedMultimap.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1440 */         return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
/* 1445 */         return CollectSpliterators.map(AbstractMapBasedMultimap.AsMap.this.submap.entrySet().spliterator(), AbstractMapBasedMultimap.AsMap.this::wrapEntry);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(@CheckForNull Object o) {
/* 1452 */         return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object o) {
/* 1457 */         if (!contains(o)) {
/* 1458 */           return false;
/*      */         }
/*      */         
/* 1461 */         Map.Entry<?, ?> entry = Objects.<Map.Entry<?, ?>>requireNonNull((Map.Entry<?, ?>)o);
/* 1462 */         AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
/* 1463 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1469 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       @CheckForNull
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1474 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1479 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1480 */         this.collection = entry.getValue();
/* 1481 */         return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1486 */         Preconditions.checkState((this.collection != null), "no calls to next() since the last call to remove()");
/* 1487 */         this.delegateIterator.remove();
/* 1488 */         AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
/* 1489 */         this.collection.clear();
/* 1490 */         this.collection = null;
/*      */       } } }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     @CheckForNull
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1498 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1502 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 1508 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K firstKey() {
/* 1514 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K lastKey() {
/* 1520 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(@ParametricNullness K toKey) {
/* 1525 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 1531 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey) {
/* 1536 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1545 */       SortedSet<K> result = this.sortedKeySet;
/* 1546 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 1551 */       return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
/*      */     }
/*      */   }
/*      */   
/*      */   private final class NavigableAsMap
/*      */     extends SortedAsMap implements NavigableMap<K, Collection<V>> {
/*      */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
/* 1558 */       super(submap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1563 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(@ParametricNullness K key) {
/* 1569 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1570 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lowerKey(@ParametricNullness K key) {
/* 1576 */       return sortedMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> floorEntry(@ParametricNullness K key) {
/* 1582 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1583 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floorKey(@ParametricNullness K key) {
/* 1589 */       return sortedMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(@ParametricNullness K key) {
/* 1595 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1596 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceilingKey(@ParametricNullness K key) {
/* 1602 */       return sortedMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> higherEntry(@ParametricNullness K key) {
/* 1608 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1609 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higherKey(@ParametricNullness K key) {
/* 1615 */       return sortedMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1621 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1622 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1628 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1629 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> pollFirstEntry() {
/* 1635 */       return pollAsMapEntry(entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, Collection<V>> pollLastEntry() {
/* 1641 */       return pollAsMapEntry(descendingMap().entrySet().iterator());
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1646 */       if (!entryIterator.hasNext()) {
/* 1647 */         return null;
/*      */       }
/* 1649 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 1650 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1651 */       output.addAll(entry.getValue());
/* 1652 */       entryIterator.remove();
/* 1653 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> descendingMap() {
/* 1658 */       return new NavigableAsMap(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> keySet() {
/* 1663 */       return (NavigableSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<K> createKeySet() {
/* 1668 */       return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1673 */       return keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1678 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 1684 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 1693 */       return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K toKey) {
/* 1698 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 1703 */       return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey) {
/* 1708 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 1714 */       return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractMapBasedMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */