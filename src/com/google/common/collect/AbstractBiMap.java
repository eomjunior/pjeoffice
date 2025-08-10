/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
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
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   @RetainedWith
/*     */   transient AbstractBiMap<V, K> inverse;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<K> keySet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<V> valueSet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Map.Entry<K, V>> entrySet;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  67 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  72 */     this.delegate = backward;
/*  73 */     this.inverse = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  78 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   K checkKey(@ParametricNullness K key) {
/*  85 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   V checkValue(@ParametricNullness V value) {
/*  92 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/* 100 */     Preconditions.checkState((this.delegate == null));
/* 101 */     Preconditions.checkState((this.inverse == null));
/* 102 */     Preconditions.checkArgument(forward.isEmpty());
/* 103 */     Preconditions.checkArgument(backward.isEmpty());
/* 104 */     Preconditions.checkArgument((forward != backward));
/* 105 */     this.delegate = forward;
/* 106 */     this.inverse = makeInverse(backward);
/*     */   }
/*     */   
/*     */   AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
/* 110 */     return new Inverse<>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(AbstractBiMap<V, K> inverse) {
/* 114 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 121 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@ParametricNullness K key, @ParametricNullness V value) {
/* 130 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
/* 137 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private V putInBothMaps(@ParametricNullness K key, @ParametricNullness V value, boolean force) {
/* 142 */     checkKey(key);
/* 143 */     checkValue(value);
/* 144 */     boolean containedKey = containsKey(key);
/* 145 */     if (containedKey && Objects.equal(value, get(key))) {
/* 146 */       return value;
/*     */     }
/* 148 */     if (force) {
/* 149 */       inverse().remove(value);
/*     */     } else {
/* 151 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
/*     */     } 
/* 153 */     V oldValue = this.delegate.put(key, value);
/* 154 */     updateInverseMap(key, containedKey, oldValue, value);
/* 155 */     return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateInverseMap(@ParametricNullness K key, boolean containedKey, @CheckForNull V oldValue, @ParametricNullness V newValue) {
/* 163 */     if (containedKey)
/*     */     {
/* 165 */       removeFromInverseMap(NullnessCasts.uncheckedCastNullableTToT(oldValue));
/*     */     }
/* 167 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@CheckForNull Object key) {
/* 174 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   private V removeFromBothMaps(@CheckForNull Object key) {
/* 181 */     V oldValue = NullnessCasts.uncheckedCastNullableTToT(this.delegate.remove(key));
/* 182 */     removeFromInverseMap(oldValue);
/* 183 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(@ParametricNullness V oldValue) {
/* 187 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 194 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 195 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 201 */     this.delegate.replaceAll(function);
/* 202 */     this.inverse.delegate.clear();
/* 203 */     Map.Entry<K, V> broken = null;
/* 204 */     Iterator<Map.Entry<K, V>> itr = this.delegate.entrySet().iterator();
/* 205 */     while (itr.hasNext()) {
/* 206 */       Map.Entry<K, V> entry = itr.next();
/* 207 */       K k = entry.getKey();
/* 208 */       V v = entry.getValue();
/* 209 */       K conflict = (K)this.inverse.delegate.putIfAbsent((K)v, (V)k);
/* 210 */       if (conflict != null) {
/* 211 */         broken = entry;
/*     */ 
/*     */         
/* 214 */         itr.remove();
/*     */       } 
/*     */     } 
/* 217 */     if (broken != null) {
/* 218 */       throw new IllegalArgumentException("value already present: " + broken.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 224 */     this.delegate.clear();
/* 225 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 232 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 239 */     Set<K> result = this.keySet;
/* 240 */     return (result == null) ? (this.keySet = new KeySet()) : result;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     private KeySet() {}
/*     */     
/*     */     protected Set<K> delegate() {
/* 247 */       return AbstractBiMap.this.delegate.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 252 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object key) {
/* 257 */       if (!contains(key)) {
/* 258 */         return false;
/*     */       }
/* 260 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 261 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 266 */       return standardRemoveAll(keysToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 271 */       return standardRetainAll(keysToRetain);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 276 */       return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
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
/*     */   public Set<V> values() {
/* 288 */     Set<V> result = this.valueSet;
/* 289 */     return (result == null) ? (this.valueSet = new ValueSet()) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet
/*     */     extends ForwardingSet<V> {
/* 294 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */ 
/*     */     
/*     */     protected Set<V> delegate() {
/* 298 */       return this.valuesDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 303 */       return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 308 */       return standardToArray();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 314 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 319 */       return standardToString();
/*     */     }
/*     */ 
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 327 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 328 */     return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*     */   }
/*     */   
/*     */   class BiMapEntry extends ForwardingMapEntry<K, V> {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     BiMapEntry(Map.Entry<K, V> delegate) {
/* 335 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map.Entry<K, V> delegate() {
/* 340 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 345 */       AbstractBiMap.this.checkValue(value);
/*     */       
/* 347 */       Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
/*     */       
/* 349 */       if (Objects.equal(value, getValue())) {
/* 350 */         return value;
/*     */       }
/* 352 */       Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
/* 353 */       V oldValue = this.delegate.setValue(value);
/* 354 */       Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
/* 355 */       AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 356 */       return oldValue;
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/* 361 */     final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
/* 362 */     return new Iterator<Map.Entry<K, V>>() {
/*     */         @CheckForNull
/*     */         Map.Entry<K, V> entry;
/*     */         
/*     */         public boolean hasNext() {
/* 367 */           return iterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 372 */           this.entry = iterator.next();
/* 373 */           return new AbstractBiMap.BiMapEntry(this.entry);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 378 */           if (this.entry == null) {
/* 379 */             throw new IllegalStateException("no calls to next() since the last call to remove()");
/*     */           }
/* 381 */           V value = this.entry.getValue();
/* 382 */           iterator.remove();
/* 383 */           AbstractBiMap.this.removeFromInverseMap(value);
/* 384 */           this.entry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ForwardingSet<Map.Entry<K, V>> {
/* 391 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 395 */       return this.esDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 400 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object object) {
/* 409 */       if (!this.esDelegate.contains(object) || !(object instanceof Map.Entry)) {
/* 410 */         return false;
/*     */       }
/*     */       
/* 413 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 414 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 420 */       this.esDelegate.remove(entry);
/* 421 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 426 */       return AbstractBiMap.this.entrySetIterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 433 */       return standardToArray();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 439 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 444 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 449 */       return standardContainsAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 454 */       return standardRemoveAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 459 */       return standardRetainAll(c);
/*     */     }
/*     */     
/*     */     private EntrySet() {}
/*     */   }
/*     */   
/*     */   static class Inverse<K, V> extends AbstractBiMap<K, V> {
/*     */     Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/* 467 */       super(backward, forward);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     K checkKey(@ParametricNullness K key) {
/* 482 */       return this.inverse.checkValue(key);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     V checkValue(@ParametricNullness V value) {
/* 488 */       return this.inverse.checkKey(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 497 */       stream.defaultWriteObject();
/* 498 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 505 */       stream.defaultReadObject();
/* 506 */       setInverse((AbstractBiMap<V, K>)Objects.<Object>requireNonNull(stream.readObject()));
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     Object readResolve() {
/* 512 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */