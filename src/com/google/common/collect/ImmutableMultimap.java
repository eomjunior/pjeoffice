/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends BaseImmutableMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  85 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  90 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  95 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 103 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 111 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 120 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 130 */     return new Builder<>();
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
/*     */   @DoNotMock
/*     */   public static class Builder<K, V>
/*     */   {
/* 163 */     final Map<K, Collection<V>> builderMap = Platform.preservesInsertionOrderOnPutsMap(); @CheckForNull
/*     */     Comparator<? super K> keyComparator; @CheckForNull
/*     */     Comparator<? super V> valueComparator;
/*     */     Collection<V> newMutableValueCollection() {
/* 167 */       return new ArrayList<>();
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 173 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 174 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 175 */       if (valueCollection == null) {
/* 176 */         this.builderMap.put(key, valueCollection = newMutableValueCollection());
/*     */       }
/* 178 */       valueCollection.add(value);
/* 179 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 189 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 199 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 200 */         put(entry);
/*     */       }
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 213 */       if (key == null) {
/* 214 */         throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
/*     */       }
/* 216 */       Collection<V> valueCollection = this.builderMap.get(key);
/* 217 */       if (valueCollection != null) {
/* 218 */         for (V value : values) {
/* 219 */           CollectPreconditions.checkEntryNotNull(key, value);
/* 220 */           valueCollection.add(value);
/*     */         } 
/* 222 */         return this;
/*     */       } 
/* 224 */       Iterator<? extends V> valuesItr = values.iterator();
/* 225 */       if (!valuesItr.hasNext()) {
/* 226 */         return this;
/*     */       }
/* 228 */       valueCollection = newMutableValueCollection();
/* 229 */       while (valuesItr.hasNext()) {
/* 230 */         V value = valuesItr.next();
/* 231 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 232 */         valueCollection.add(value);
/*     */       } 
/* 234 */       this.builderMap.put(key, valueCollection);
/* 235 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 246 */       return putAll(key, Arrays.asList(values));
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 260 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 261 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 263 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 273 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 274 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 284 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
/* 285 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 290 */       for (Map.Entry<K, Collection<V>> entry : other.builderMap.entrySet()) {
/* 291 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 293 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 298 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 299 */       if (this.keyComparator != null) {
/* 300 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 302 */       return ImmutableListMultimap.fromMapEntries(mapEntries, this.valueComparator);
/*     */     }
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 317 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 319 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/* 320 */       if (!kvMultimap.isPartialView()) {
/* 321 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 324 */     return ImmutableListMultimap.copyOf(multimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 337 */     return ImmutableListMultimap.copyOf(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   static class FieldSettersHolder
/*     */   {
/* 350 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */     
/* 352 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */   }
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 356 */     this.map = map;
/* 357 */     this.size = size;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public ImmutableCollection<V> removeAll(@CheckForNull Object key) {
/* 376 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 393 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void clear() {
/* 406 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final boolean put(K key, V value) {
/* 436 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final boolean putAll(K key, Iterable<? extends V> values) {
/* 450 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 464 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 478 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 488 */     return this.map.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/* 495 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 500 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 505 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 516 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 521 */     throw new AssertionError("unreachable");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 531 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 536 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 542 */     return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<Map.Entry<K, V>> createEntries() {
/* 547 */     return new EntryCollection<>(this);
/*     */   }
/*     */   private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> { @Weak
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 554 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 559 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 564 */       return this.multimap.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 569 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/* 574 */       if (object instanceof Map.Entry) {
/* 575 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 576 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 578 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 587 */       return super.writeReplace();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 595 */     return new UnmodifiableIterator<Map.Entry<K, V>>() {
/* 596 */         final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> asMapItr = ImmutableMultimap.this.map
/* 597 */           .entrySet().iterator(); @CheckForNull
/* 598 */         K currentKey = null;
/* 599 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 603 */           return (this.valueItr.hasNext() || this.asMapItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 608 */           if (!this.valueItr.hasNext()) {
/* 609 */             Map.Entry<K, ? extends ImmutableCollection<V>> entry = this.asMapItr.next();
/* 610 */             this.currentKey = entry.getKey();
/* 611 */             this.valueItr = ((ImmutableCollection<V>)entry.getValue()).iterator();
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 617 */           return Maps.immutableEntry(Objects.requireNonNull(this.currentKey), this.valueItr.next());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 624 */     return CollectSpliterators.flatMap(
/* 625 */         asMap().entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }0x40 | (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 632 */         (this instanceof SetMultimap) ? 1 : 0), 
/* 633 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 638 */     Preconditions.checkNotNull(action);
/* 639 */     asMap()
/* 640 */       .forEach((key, valueCollection) -> valueCollection.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultiset<K> keys() {
/* 651 */     return (ImmutableMultiset<K>)super.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset<K> createKeys() {
/* 656 */     return new Keys();
/*     */   }
/*     */ 
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/*     */     public boolean contains(@CheckForNull Object object) {
/* 664 */       return ImmutableMultimap.this.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int count(@CheckForNull Object element) {
/* 669 */       Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
/* 670 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<K> elementSet() {
/* 675 */       return ImmutableMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 680 */       return ImmutableMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 685 */       Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 686 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 691 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     Object writeReplace() {
/* 698 */       return new ImmutableMultimap.KeysSerializedForm(ImmutableMultimap.this);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 704 */       throw new InvalidObjectException("Use KeysSerializedForm");
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final class KeysSerializedForm implements Serializable {
/*     */     final ImmutableMultimap<?, ?> multimap;
/*     */     
/*     */     KeysSerializedForm(ImmutableMultimap<?, ?> multimap) {
/* 714 */       this.multimap = multimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 718 */       return this.multimap.keys();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 728 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 733 */     return new Values<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 738 */     return new UnmodifiableIterator<V>() {
/* 739 */         Iterator<? extends ImmutableCollection<V>> valueCollectionItr = ImmutableMultimap.this.map.values().iterator();
/* 740 */         Iterator<V> valueItr = Iterators.emptyIterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 744 */           return (this.valueItr.hasNext() || this.valueCollectionItr.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public V next() {
/* 749 */           if (!this.valueItr.hasNext()) {
/* 750 */             this.valueItr = ((ImmutableCollection<V>)this.valueCollectionItr.next()).iterator();
/*     */           }
/* 752 */           return this.valueItr.next();
/*     */         }
/*     */       };
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> { @Weak
/*     */     private final transient ImmutableMultimap<K, V> multimap;
/*     */     Values(ImmutableMultimap<K, V> multimap) {
/* 761 */       this.multimap = multimap;
/*     */     }
/*     */     @J2ktIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */     public boolean contains(@CheckForNull Object object) {
/* 766 */       return this.multimap.containsValue(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 771 */       return this.multimap.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 777 */       for (UnmodifiableIterator<ImmutableCollection<V>> unmodifiableIterator = this.multimap.map.values().iterator(); unmodifiableIterator.hasNext(); ) { ImmutableCollection<V> valueCollection = unmodifiableIterator.next();
/* 778 */         offset = valueCollection.copyIntoArray(dst, offset); }
/*     */       
/* 780 */       return offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 785 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 790 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 799 */       return super.writeReplace();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */