/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*     */   @CheckForNull
/*     */   public Comparator<? super K> comparator() {
/*  68 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public K firstKey() {
/*  74 */     return delegate().firstKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/*  79 */     return delegate().headMap(toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public K lastKey() {
/*  85 */     return delegate().lastKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/*  90 */     return delegate().subMap(fromKey, toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/*  95 */     return delegate().tailMap(fromKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class StandardKeySet
/*     */     extends Maps.SortedKeySet<K, V>
/*     */   {
/*     */     public StandardKeySet(ForwardingSortedMap<K, V> this$0) {
/* 108 */       super(this$0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(@CheckForNull Comparator<?> comparator, @CheckForNull Object o1, @CheckForNull Object o2) {
/* 116 */     if (comparator == null) {
/* 117 */       return ((Comparable<Object>)o1).compareTo(o2);
/*     */     }
/* 119 */     return comparator.compare(o1, o2);
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
/*     */   protected boolean standardContainsKey(@CheckForNull Object key) {
/*     */     try {
/* 135 */       ForwardingSortedMap<K, V> forwardingSortedMap = this;
/* 136 */       Object ceilingKey = forwardingSortedMap.tailMap((K)key).firstKey();
/* 137 */       return (unsafeCompare(comparator(), ceilingKey, key) == 0);
/* 138 */     } catch (ClassCastException|java.util.NoSuchElementException|NullPointerException e) {
/* 139 */       return false;
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
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
/* 151 */     Preconditions.checkArgument((unsafeCompare(comparator(), fromKey, toKey) <= 0), "fromKey must be <= toKey");
/* 152 */     return tailMap(fromKey).headMap(toKey);
/*     */   }
/*     */   
/*     */   protected abstract SortedMap<K, V> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */