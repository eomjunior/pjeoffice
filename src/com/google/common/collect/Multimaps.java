/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Multimaps
/*      */ {
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/*  125 */     return CollectCollectors.toMultimap(keyFunction, valueFunction, multimapSupplier);
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
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/*  170 */     return CollectCollectors.flatteningToMultimap(keyFunction, valueFunction, multimapSupplier);
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
/*      */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  212 */     return new CustomMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> { transient Supplier<? extends Collection<V>> factory;
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  220 */       super(map);
/*  221 */       this.factory = (Supplier<? extends Collection<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  226 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  231 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<V> createCollection() {
/*  236 */       return (Collection<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  242 */       if (collection instanceof NavigableSet)
/*  243 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  244 */       if (collection instanceof SortedSet)
/*  245 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection); 
/*  246 */       if (collection instanceof Set)
/*  247 */         return Collections.unmodifiableSet((Set<? extends E>)collection); 
/*  248 */       if (collection instanceof List) {
/*  249 */         return Collections.unmodifiableList((List<? extends E>)collection);
/*      */       }
/*  251 */       return Collections.unmodifiableCollection(collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
/*  257 */       if (collection instanceof List)
/*  258 */         return wrapList(key, (List<V>)collection, null); 
/*  259 */       if (collection instanceof NavigableSet)
/*  260 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  261 */       if (collection instanceof SortedSet)
/*  262 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null); 
/*  263 */       if (collection instanceof Set) {
/*  264 */         return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */       }
/*  266 */       return new AbstractMapBasedMultimap.WrappedCollection(this, key, collection, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  279 */       stream.defaultWriteObject();
/*  280 */       stream.writeObject(this.factory);
/*  281 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  288 */       stream.defaultReadObject();
/*  289 */       this.factory = (Supplier<? extends Collection<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  290 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  291 */       setMap(map);
/*      */     } }
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
/*      */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  332 */     return new CustomListMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> { transient Supplier<? extends List<V>> factory;
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  340 */       super(map);
/*  341 */       this.factory = (Supplier<? extends List<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  346 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  351 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected List<V> createCollection() {
/*  356 */       return (List<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  365 */       stream.defaultWriteObject();
/*  366 */       stream.writeObject(this.factory);
/*  367 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  374 */       stream.defaultReadObject();
/*  375 */       this.factory = (Supplier<? extends List<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  376 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  377 */       setMap(map);
/*      */     } }
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
/*      */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  417 */     return new CustomSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> { transient Supplier<? extends Set<V>> factory;
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  425 */       super(map);
/*  426 */       this.factory = (Supplier<? extends Set<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  431 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  436 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<V> createCollection() {
/*  441 */       return (Set<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  447 */       if (collection instanceof NavigableSet)
/*  448 */         return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  449 */       if (collection instanceof SortedSet) {
/*  450 */         return Collections.unmodifiableSortedSet((SortedSet<E>)collection);
/*      */       }
/*  452 */       return Collections.unmodifiableSet((Set<? extends E>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
/*  458 */       if (collection instanceof NavigableSet)
/*  459 */         return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null); 
/*  460 */       if (collection instanceof SortedSet) {
/*  461 */         return new AbstractMapBasedMultimap.WrappedSortedSet(this, key, (SortedSet<V>)collection, null);
/*      */       }
/*  463 */       return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  473 */       stream.defaultWriteObject();
/*  474 */       stream.writeObject(this.factory);
/*  475 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  482 */       stream.defaultReadObject();
/*  483 */       this.factory = (Supplier<? extends Set<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  484 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  485 */       setMap(map);
/*      */     } }
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
/*      */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  525 */     return new CustomSortedSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> { transient Supplier<? extends SortedSet<V>> factory;
/*      */     @CheckForNull
/*      */     transient Comparator<? super V> valueComparator;
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  535 */       super(map);
/*  536 */       this.factory = (Supplier<? extends SortedSet<V>>)Preconditions.checkNotNull(factory);
/*  537 */       this.valueComparator = ((SortedSet<V>)factory.get()).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/*  542 */       return createMaybeNavigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/*  547 */       return createMaybeNavigableAsMap();
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<V> createCollection() {
/*  552 */       return (SortedSet<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super V> valueComparator() {
/*  558 */       return this.valueComparator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  567 */       stream.defaultWriteObject();
/*  568 */       stream.writeObject(this.factory);
/*  569 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     @J2ktIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  576 */       stream.defaultReadObject();
/*  577 */       this.factory = (Supplier<? extends SortedSet<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  578 */       this.valueComparator = ((SortedSet<V>)this.factory.get()).comparator();
/*  579 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)Objects.<Object>requireNonNull(stream.readObject());
/*  580 */       setMap(map);
/*      */     } }
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  602 */     Preconditions.checkNotNull(dest);
/*  603 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  604 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  606 */     return dest;
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
/*      */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
/*  643 */     return Synchronized.multimap(multimap, null);
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
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  659 */     if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
/*  660 */       return delegate;
/*      */     }
/*  662 */     return new UnmodifiableMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
/*  673 */     return (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     transient Multiset<K> keys;
/*      */     
/*      */     UnmodifiableMultimap(Multimap<K, V> delegate) {
/*  686 */       this.delegate = (Multimap<K, V>)Preconditions.checkNotNull(delegate); } @LazyInit @CheckForNull
/*      */     transient Set<K> keySet; @LazyInit
/*      */     @CheckForNull
/*      */     transient Collection<V> values; @LazyInit
/*      */     @CheckForNull
/*  691 */     transient Map<K, Collection<V>> map; private static final long serialVersionUID = 0L; protected Multimap<K, V> delegate() { return this.delegate; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  696 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  701 */       Map<K, Collection<V>> result = this.map;
/*  702 */       if (result == null)
/*      */       {
/*      */         
/*  705 */         result = this.map = Collections.<K, V>unmodifiableMap(
/*  706 */             Maps.transformValues(this.delegate
/*  707 */               .asMap(), collection -> Multimaps.unmodifiableValueCollection(collection)));
/*      */       }
/*  709 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  714 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  715 */       if (result == null) {
/*  716 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  718 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> consumer) {
/*  723 */       this.delegate.forEach((BiConsumer<? super K, ? super V>)Preconditions.checkNotNull(consumer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(@ParametricNullness K key) {
/*  728 */       return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  733 */       Multiset<K> result = this.keys;
/*  734 */       if (result == null) {
/*  735 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  737 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  742 */       Set<K> result = this.keySet;
/*  743 */       if (result == null) {
/*  744 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  746 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/*  751 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
/*  756 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  761 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/*  766 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(@CheckForNull Object key) {
/*  771 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  776 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  781 */       Collection<V> result = this.values;
/*  782 */       if (result == null) {
/*  783 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  785 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V>
/*      */     implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  795 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListMultimap<K, V> delegate() {
/*  800 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(@ParametricNullness K key) {
/*  805 */       return Collections.unmodifiableList(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(@CheckForNull Object key) {
/*  810 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  815 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V>
/*      */     implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  825 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SetMultimap<K, V> delegate() {
/*  830 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(@ParametricNullness K key) {
/*  839 */       return Collections.unmodifiableSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  844 */       return Maps.unmodifiableEntrySet(delegate().entries());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(@CheckForNull Object key) {
/*  849 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  854 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V>
/*      */     extends UnmodifiableSetMultimap<K, V>
/*      */     implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  864 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSetMultimap<K, V> delegate() {
/*  869 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(@ParametricNullness K key) {
/*  874 */       return Collections.unmodifiableSortedSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(@CheckForNull Object key) {
/*  879 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/*  884 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super V> valueComparator() {
/*  890 */       return delegate().valueComparator();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
/*  908 */     return Synchronized.setMultimap(multimap, null);
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
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  924 */     if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
/*  925 */       return delegate;
/*      */     }
/*  927 */     return new UnmodifiableSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
/*  939 */     return (SetMultimap<K, V>)Preconditions.checkNotNull(delegate);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
/*  955 */     return Synchronized.sortedSetMultimap(multimap, null);
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
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  971 */     if (delegate instanceof UnmodifiableSortedSetMultimap) {
/*  972 */       return delegate;
/*      */     }
/*  974 */     return new UnmodifiableSortedSetMultimap<>(delegate);
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
/*      */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
/*  987 */     return Synchronized.listMultimap(multimap, null);
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
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/* 1003 */     if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
/* 1004 */       return delegate;
/*      */     }
/* 1006 */     return new UnmodifiableListMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
/* 1018 */     return (ListMultimap<K, V>)Preconditions.checkNotNull(delegate);
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
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/* 1031 */     if (collection instanceof SortedSet)
/* 1032 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/* 1033 */     if (collection instanceof Set)
/* 1034 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/* 1035 */     if (collection instanceof List) {
/* 1036 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/* 1038 */     return Collections.unmodifiableCollection(collection);
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
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1051 */     if (entries instanceof Set) {
/* 1052 */       return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>)entries);
/*      */     }
/* 1054 */     return new Maps.UnmodifiableEntries<>(Collections.unmodifiableCollection(entries));
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
/*      */   public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) {
/* 1067 */     return (Map)multimap.asMap();
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
/*      */   public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) {
/* 1080 */     return (Map)multimap.asMap();
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
/*      */   public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) {
/* 1093 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) {
/* 1104 */     return multimap.asMap();
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
/*      */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
/* 1124 */     return new MapMultimap<>(map);
/*      */   }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
/*      */     final Map<K, V> map;
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*      */     MapMultimap(Map<K, V> map) {
/* 1133 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1138 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1143 */       return this.map.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(@CheckForNull Object value) {
/* 1148 */       return this.map.containsValue(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
/* 1153 */       return this.map.entrySet().contains(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(@ParametricNullness final K key) {
/* 1158 */       return new Sets.ImprovedAbstractSet<V>()
/*      */         {
/*      */           public Iterator<V> iterator() {
/* 1161 */             return new Iterator<V>()
/*      */               {
/*      */                 int i;
/*      */                 
/*      */                 public boolean hasNext() {
/* 1166 */                   return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
/*      */                 }
/*      */ 
/*      */                 
/*      */                 @ParametricNullness
/*      */                 public V next() {
/* 1172 */                   if (!hasNext()) {
/* 1173 */                     throw new NoSuchElementException();
/*      */                   }
/* 1175 */                   this.i++;
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1180 */                   return NullnessCasts.uncheckedCastNullableTToT((V)Multimaps.MapMultimap.this.map.get(key));
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void remove() {
/* 1185 */                   CollectPreconditions.checkRemove((this.i == 1));
/* 1186 */                   this.i = -1;
/* 1187 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */ 
/*      */           
/*      */           public int size() {
/* 1194 */             return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/* 1201 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
/* 1206 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 1211 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 1216 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 1221 */       return this.map.entrySet().remove(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(@CheckForNull Object key) {
/* 1226 */       Set<V> values = new HashSet<>(2);
/* 1227 */       if (!this.map.containsKey(key)) {
/* 1228 */         return values;
/*      */       }
/* 1230 */       values.add(this.map.remove(key));
/* 1231 */       return values;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1236 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1241 */       return this.map.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 1246 */       return this.map.values();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/* 1251 */       return this.map.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V>> createEntries() {
/* 1256 */       throw new AssertionError("unreachable");
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1261 */       return new Multimaps.Keys<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1266 */       return this.map.entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/* 1271 */       return new Multimaps.AsMap<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1276 */       return this.map.hashCode();
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
/*      */   public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1327 */     Preconditions.checkNotNull(function);
/* 1328 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1329 */     return transformEntries(fromMultimap, transformer);
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
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1376 */     Preconditions.checkNotNull(function);
/* 1377 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1378 */     return transformEntries(fromMultimap, transformer);
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
/*      */   public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1436 */     return new TransformedEntriesMultimap<>(fromMap, transformer);
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
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1491 */     return new TransformedEntriesListMultimap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TransformedEntriesMultimap<K, V1, V2>
/*      */     extends AbstractMultimap<K, V2>
/*      */   {
/*      */     final Multimap<K, V1> fromMultimap;
/*      */     
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1503 */       this.fromMultimap = (Multimap<K, V1>)Preconditions.checkNotNull(fromMultimap);
/* 1504 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */     
/*      */     Collection<V2> transform(@ParametricNullness K key, Collection<V1> values) {
/* 1508 */       Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
/* 1509 */       if (values instanceof List) {
/* 1510 */         return Lists.transform((List<V1>)values, function);
/*      */       }
/* 1512 */       return Collections2.transform(values, function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, Collection<V2>> createAsMap() {
/* 1518 */       return Maps.transformEntries(this.fromMultimap.asMap(), (key, value) -> transform((K)key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1523 */       this.fromMultimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1528 */       return this.fromMultimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map.Entry<K, V2>> createEntries() {
/* 1533 */       return new AbstractMultimap.Entries(this);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 1538 */       return Iterators.transform(this.fromMultimap
/* 1539 */           .entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> get(@ParametricNullness K key) {
/* 1544 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1549 */       return this.fromMultimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 1554 */       return this.fromMultimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<K> createKeys() {
/* 1559 */       return this.fromMultimap.keys();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(@ParametricNullness K key, @ParametricNullness V2 value) {
/* 1564 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(@ParametricNullness K key, Iterable<? extends V2> values) {
/* 1569 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
/* 1574 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 1580 */       return get((K)key).remove(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V2> removeAll(@CheckForNull Object key) {
/* 1586 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> replaceValues(@ParametricNullness K key, Iterable<? extends V2> values) {
/* 1591 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1596 */       return this.fromMultimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V2> createValues() {
/* 1601 */       return Collections2.transform(this.fromMultimap
/* 1602 */           .entries(), Maps.asEntryToValueFunction(this.transformer));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class TransformedEntriesListMultimap<K, V1, V2>
/*      */     extends TransformedEntriesMultimap<K, V1, V2>
/*      */     implements ListMultimap<K, V2>
/*      */   {
/*      */     TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1612 */       super(fromMultimap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     List<V2> transform(@ParametricNullness K key, Collection<V1> values) {
/* 1617 */       return Lists.transform((List)values, Maps.asValueToValueFunction(this.transformer, key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> get(@ParametricNullness K key) {
/* 1622 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<V2> removeAll(@CheckForNull Object key) {
/* 1628 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> replaceValues(@ParametricNullness K key, Iterable<? extends V2> values) {
/* 1633 */       throw new UnsupportedOperationException();
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1674 */     return index(values.iterator(), keyFunction);
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1715 */     Preconditions.checkNotNull(keyFunction);
/* 1716 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/* 1717 */     while (values.hasNext()) {
/* 1718 */       V value = values.next();
/* 1719 */       Preconditions.checkNotNull(value, values);
/* 1720 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/* 1722 */     return builder.build();
/*      */   }
/*      */   
/*      */   static class Keys<K, V> extends AbstractMultiset<K> {
/*      */     @Weak
/*      */     final Multimap<K, V> multimap;
/*      */     
/*      */     Keys(Multimap<K, V> multimap) {
/* 1730 */       this.multimap = multimap;
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<K>> entryIterator() {
/* 1735 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this, this.multimap
/* 1736 */           .asMap().entrySet().iterator())
/*      */         {
/*      */           Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
/* 1739 */             return new Multisets.AbstractEntry<K>(this)
/*      */               {
/*      */                 @ParametricNullness
/*      */                 public K getElement() {
/* 1743 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public int getCount() {
/* 1748 */                   return ((Collection)backingEntry.getValue()).size();
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/* 1757 */       return CollectSpliterators.map(this.multimap.entries().spliterator(), Map.Entry::getKey);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1762 */       Preconditions.checkNotNull(consumer);
/* 1763 */       this.multimap.entries().forEach(entry -> consumer.accept(entry.getKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1768 */       return this.multimap.asMap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1773 */       return this.multimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object element) {
/* 1778 */       return this.multimap.containsKey(element);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1783 */       return Maps.keyIterator(this.multimap.entries().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@CheckForNull Object element) {
/* 1788 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/* 1789 */       return (values == null) ? 0 : values.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@CheckForNull Object element, int occurrences) {
/* 1794 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 1795 */       if (occurrences == 0) {
/* 1796 */         return count(element);
/*      */       }
/*      */       
/* 1799 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/*      */       
/* 1801 */       if (values == null) {
/* 1802 */         return 0;
/*      */       }
/*      */       
/* 1805 */       int oldCount = values.size();
/* 1806 */       if (occurrences >= oldCount) {
/* 1807 */         values.clear();
/*      */       } else {
/* 1809 */         Iterator<V> iterator = values.iterator();
/* 1810 */         for (int i = 0; i < occurrences; i++) {
/* 1811 */           iterator.next();
/* 1812 */           iterator.remove();
/*      */         } 
/*      */       } 
/* 1815 */       return oldCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1820 */       this.multimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> elementSet() {
/* 1825 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<K> elementIterator() {
/* 1830 */       throw new AssertionError("should never be called");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class Entries<K, V>
/*      */     extends AbstractCollection<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Multimap<K, V> multimap();
/*      */     
/*      */     public int size() {
/* 1841 */       return multimap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 1846 */       if (o instanceof Map.Entry) {
/* 1847 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1848 */         return multimap().containsEntry(entry.getKey(), entry.getValue());
/*      */       } 
/* 1850 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 1855 */       if (o instanceof Map.Entry) {
/* 1856 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1857 */         return multimap().remove(entry.getKey(), entry.getValue());
/*      */       } 
/* 1859 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1864 */       multimap().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static final class AsMap<K, V>
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
/*      */     @Weak
/*      */     private final Multimap<K, V> multimap;
/*      */     
/*      */     AsMap(Multimap<K, V> multimap) {
/* 1874 */       this.multimap = (Multimap<K, V>)Preconditions.checkNotNull(multimap);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1879 */       return this.multimap.keySet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1884 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     void removeValuesForKey(@CheckForNull Object key) {
/* 1888 */       this.multimap.keySet().remove(key);
/*      */     }
/*      */     
/*      */     class EntrySet
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1895 */         return Multimaps.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1900 */         return Maps.asMapEntryIterator(Multimaps.AsMap.this.multimap.keySet(), key -> Multimaps.AsMap.this.multimap.get(key));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object o) {
/* 1905 */         if (!contains(o)) {
/* 1906 */           return false;
/*      */         }
/*      */         
/* 1909 */         Map.Entry<?, ?> entry = Objects.<Map.Entry<?, ?>>requireNonNull((Map.Entry<?, ?>)o);
/* 1910 */         Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
/* 1911 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Collection<V> get(@CheckForNull Object key) {
/* 1919 */       return containsKey(key) ? this.multimap.get((K)key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Collection<V> remove(@CheckForNull Object key) {
/* 1925 */       return containsKey(key) ? this.multimap.removeAll(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1930 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1935 */       return this.multimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1940 */       return this.multimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1945 */       this.multimap.clear();
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
/*      */   public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1978 */     if (unfiltered instanceof SetMultimap)
/* 1979 */       return filterKeys((SetMultimap<K, V>)unfiltered, keyPredicate); 
/* 1980 */     if (unfiltered instanceof ListMultimap)
/* 1981 */       return filterKeys((ListMultimap<K, V>)unfiltered, keyPredicate); 
/* 1982 */     if (unfiltered instanceof FilteredKeyMultimap) {
/* 1983 */       FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap<K, V>)unfiltered;
/* 1984 */       return new FilteredKeyMultimap<>(prev.unfiltered, 
/* 1985 */           Predicates.and(prev.keyPredicate, keyPredicate));
/* 1986 */     }  if (unfiltered instanceof FilteredMultimap) {
/* 1987 */       FilteredMultimap<K, V> prev = (FilteredMultimap<K, V>)unfiltered;
/* 1988 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1990 */     return new FilteredKeyMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2024 */     if (unfiltered instanceof FilteredKeySetMultimap) {
/* 2025 */       FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap<K, V>)unfiltered;
/* 2026 */       return new FilteredKeySetMultimap<>(prev
/* 2027 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/* 2028 */     }  if (unfiltered instanceof FilteredSetMultimap) {
/* 2029 */       FilteredSetMultimap<K, V> prev = (FilteredSetMultimap<K, V>)unfiltered;
/* 2030 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 2032 */     return new FilteredKeySetMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2066 */     if (unfiltered instanceof FilteredKeyListMultimap) {
/* 2067 */       FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap<K, V>)unfiltered;
/* 2068 */       return new FilteredKeyListMultimap<>(prev
/* 2069 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 2071 */     return new FilteredKeyListMultimap<>(unfiltered, keyPredicate);
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
/*      */   public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2105 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2138 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2169 */     Preconditions.checkNotNull(entryPredicate);
/* 2170 */     if (unfiltered instanceof SetMultimap) {
/* 2171 */       return filterEntries((SetMultimap<K, V>)unfiltered, entryPredicate);
/*      */     }
/* 2173 */     return (unfiltered instanceof FilteredMultimap) ? 
/* 2174 */       filterFiltered((FilteredMultimap<K, V>)unfiltered, entryPredicate) : 
/* 2175 */       new FilteredEntryMultimap<>((Multimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2206 */     Preconditions.checkNotNull(entryPredicate);
/* 2207 */     return (unfiltered instanceof FilteredSetMultimap) ? 
/* 2208 */       filterFiltered((FilteredSetMultimap<K, V>)unfiltered, entryPredicate) : 
/* 2209 */       new FilteredEntrySetMultimap<>((SetMultimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2222 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2223 */     return new FilteredEntryMultimap<>(multimap.unfiltered(), predicate);
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
/*      */   private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2236 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2237 */     return new FilteredEntrySetMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static boolean equalsImpl(Multimap<?, ?> multimap, @CheckForNull Object object) {
/* 2241 */     if (object == multimap) {
/* 2242 */       return true;
/*      */     }
/* 2244 */     if (object instanceof Multimap) {
/* 2245 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 2246 */       return multimap.asMap().equals(that.asMap());
/*      */     } 
/* 2248 */     return false;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Multimaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */