/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractSetMultimap<K, V>
/*     */   extends AbstractMapBasedMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 7431625294878419160L;
/*     */   
/*     */   protected AbstractSetMultimap(Map<K, Collection<V>> map) {
/*  46 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createUnmodifiableEmptyCollection() {
/*  54 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  60 */     return Collections.unmodifiableSet((Set<? extends E>)collection);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
/*  65 */     return new AbstractMapBasedMultimap.WrappedSet(this, key, (Set<V>)collection);
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
/*     */   public Set<V> get(@ParametricNullness K key) {
/*  78 */     return (Set<V>)super.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entries() {
/*  89 */     return (Set<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> removeAll(@CheckForNull Object key) {
/* 101 */     return (Set<V>)super.removeAll(key);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 115 */     return (Set<V>)super.replaceValues(key, values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 126 */     return super.asMap();
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/* 140 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 151 */     return super.equals(object);
/*     */   }
/*     */   
/*     */   abstract Set<V> createCollection();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */