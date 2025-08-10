/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   private MultimapBuilder() {}
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys() {
/*  75 */     return hashKeys(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(final int expectedKeys) {
/*  85 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  86 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/*  89 */           return Platform.newHashMapWithExpectedSize(expectedKeys);
/*     */         }
/*     */       };
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
/* 103 */     return linkedHashKeys(8);
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(final int expectedKeys) {
/* 116 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 117 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/* 120 */           return Platform.newLinkedHashMapWithExpectedSize(expectedKeys);
/*     */         }
/*     */       };
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
/*     */   public static MultimapBuilderWithKeys<Comparable> treeKeys() {
/* 137 */     return treeKeys(Ordering.natural());
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
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> comparator) {
/* 155 */     Preconditions.checkNotNull(comparator);
/* 156 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 159 */           return new TreeMap<>(comparator);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> keyClass) {
/* 170 */     Preconditions.checkNotNull(keyClass);
/* 171 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */ 
/*     */         
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap()
/*     */         {
/* 177 */           return (Map)new EnumMap<>(keyClass);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class ArrayListSupplier<V>
/*     */     implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     ArrayListSupplier(int expectedValuesPerKey) {
/* 187 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> get() {
/* 192 */       return new ArrayList<>(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum LinkedListSupplier implements Supplier<List<?>> {
/* 197 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public static <V> Supplier<List<V>> instance() {
/* 202 */       Supplier<List<V>> result = INSTANCE;
/* 203 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<?> get() {
/* 208 */       return new LinkedList();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V>
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     HashSetSupplier(int expectedValuesPerKey) {
/* 217 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 222 */       return Platform.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V>
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     LinkedHashSetSupplier(int expectedValuesPerKey) {
/* 231 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 236 */       return Platform.newLinkedHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V>
/*     */     implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/*     */     TreeSetSupplier(Comparator<? super V> comparator) {
/* 245 */       this.comparator = (Comparator<? super V>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<V> get() {
/* 250 */       return new TreeSet<>(this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>>
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final Class<V> clazz;
/*     */     
/*     */     EnumSetSupplier(Class<V> clazz) {
/* 259 */       this.clazz = (Class<V>)Preconditions.checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 264 */       return EnumSet.noneOf(this.clazz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues() {
/* 285 */       return arrayListValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey) {
/* 295 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 296 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 299 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 300 */                 .createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues() {
/* 308 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 311 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 312 */                 .createMap(), (Supplier)MultimapBuilder.LinkedListSupplier.instance());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues() {
/* 319 */       return hashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey) {
/* 329 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 330 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 333 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 334 */                 .createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues() {
/* 342 */       return linkedHashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey) {
/* 352 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 353 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 356 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 357 */                 .createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
/* 366 */       return treeSetValues(Ordering.natural());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator) {
/* 377 */       Preconditions.checkNotNull(comparator, "comparator");
/* 378 */       return new MultimapBuilder.SortedSetMultimapBuilder<K0, V0>()
/*     */         {
/*     */           public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 381 */             return Multimaps.newSortedSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 382 */                 .createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass) {
/* 389 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 390 */       return new MultimapBuilder.SetMultimapBuilder<K0, V0>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */           {
/* 396 */             Supplier<Set<V>> factory = (Supplier)new MultimapBuilder.EnumSetSupplier<>(valueClass);
/* 397 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), factory);
/*     */           }
/*     */         };
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
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 412 */     Multimap<K, V> result = build();
/* 413 */     result.putAll(multimap);
/* 414 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class ListMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 433 */       return (ListMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 453 */       return (SetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends SetMultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 473 */       return (SortedSetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MultimapBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */