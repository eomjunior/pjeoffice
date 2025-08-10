/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
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
/*     */ public abstract class ForwardingMultimap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   public Map<K, Collection<V>> asMap() {
/*  53 */     return delegate().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  58 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
/*  63 */     return delegate().containsEntry(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/*  68 */     return delegate().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/*  73 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/*  78 */     return delegate().entries();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(@ParametricNullness K key) {
/*  83 */     return delegate().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  88 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/*  93 */     return delegate().keys();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/*  98 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/* 104 */     return delegate().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(@ParametricNullness K key, Iterable<? extends V> values) {
/* 110 */     return delegate().putAll(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 116 */     return delegate().putAll(multimap);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 122 */     return delegate().remove(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> removeAll(@CheckForNull Object key) {
/* 128 */     return delegate().removeAll(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 134 */     return delegate().replaceValues(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 139 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 144 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 149 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 154 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Multimap<K, V> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */