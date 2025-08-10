/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultimap<K, V>
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Collection<Map.Entry<K, V>> entries;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<K> keySet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Multiset<K> keys;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Collection<V> values;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Map<K, Collection<V>> asMap;
/*     */   
/*     */   public boolean isEmpty() {
/*  47 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/*  52 */     for (Collection<V> collection : asMap().values()) {
/*  53 */       if (collection.contains(value)) {
/*  54 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
/*  63 */     Collection<V> collection = asMap().get(key);
/*  64 */     return (collection != null && collection.contains(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/*  70 */     Collection<V> collection = asMap().get(key);
/*  71 */     return (collection != null && collection.remove(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/*  77 */     return get(key).add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
/*  83 */     Preconditions.checkNotNull(values);
/*     */ 
/*     */     
/*  86 */     if (values instanceof Collection) {
/*  87 */       Collection<? extends V> valueCollection = (Collection<? extends V>)values;
/*  88 */       return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
/*     */     } 
/*  90 */     Iterator<? extends V> valueItr = values.iterator();
/*  91 */     return (valueItr.hasNext() && Iterators.addAll(get(key), valueItr));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  98 */     boolean changed = false;
/*  99 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/* 100 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/* 102 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 108 */     Preconditions.checkNotNull(values);
/* 109 */     Collection<V> result = removeAll(key);
/* 110 */     putAll(key, values);
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 118 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 119 */     return (result == null) ? (this.entries = createEntries()) : result;
/*     */   }
/*     */   
/*     */   abstract Collection<Map.Entry<K, V>> createEntries();
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> entryIterator();
/*     */   
/*     */   class Entries extends Multimaps.Entries<K, V> {
/*     */     Multimap<K, V> multimap() {
/* 128 */       return AbstractMultimap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 133 */       return AbstractMultimap.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/* 138 */       return AbstractMultimap.this.entrySpliterator();
/*     */     } }
/*     */   
/*     */   class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
/*     */     EntrySet(AbstractMultimap this$0) {
/* 143 */       super(this$0);
/*     */     }
/*     */     public int hashCode() {
/* 146 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 151 */       return Sets.equalsImpl(this, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 158 */     return Spliterators.spliterator(
/* 159 */         entryIterator(), size(), (this instanceof SetMultimap) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 166 */     Set<K> result = this.keySet;
/* 167 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Set<K> createKeySet();
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/* 176 */     Multiset<K> result = this.keys;
/* 177 */     return (result == null) ? (this.keys = createKeys()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset<K> createKeys();
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 186 */     Collection<V> result = this.values;
/* 187 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   abstract Collection<V> createValues();
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     public Iterator<V> iterator() {
/* 196 */       return AbstractMultimap.this.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 201 */       return AbstractMultimap.this.valueSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 206 */       return AbstractMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 211 */       return AbstractMultimap.this.containsValue(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 216 */       AbstractMultimap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 221 */     return Maps.valueIterator(entries().iterator());
/*     */   }
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 225 */     return Spliterators.spliterator(valueIterator(), size(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 232 */     Map<K, Collection<V>> result = this.asMap;
/* 233 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Map<K, Collection<V>> createAsMap();
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 242 */     return Multimaps.equalsImpl(this, object);
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
/*     */   public int hashCode() {
/* 255 */     return asMap().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 266 */     return asMap().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */