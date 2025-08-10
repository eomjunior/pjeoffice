/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Converter;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Objects;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collector;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Maps
/*      */ {
/*      */   private enum EntryFunction
/*      */     implements Function<Map.Entry<?, ?>, Object>
/*      */   {
/*   99 */     KEY
/*      */     {
/*      */       @CheckForNull
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  103 */         return entry.getKey();
/*      */       }
/*      */     },
/*  106 */     VALUE
/*      */     {
/*      */       @CheckForNull
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  110 */         return entry.getValue();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
/*  117 */     return EntryFunction.KEY;
/*      */   }
/*      */ 
/*      */   
/*      */   static <V> Function<Map.Entry<?, V>, V> valueFunction() {
/*  122 */     return EntryFunction.VALUE;
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  127 */     return new TransformedIterator<Map.Entry<K, V>, K>(entryIterator)
/*      */       {
/*      */         @ParametricNullness
/*      */         K transform(Map.Entry<K, V> entry) {
/*  131 */           return entry.getKey();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  138 */     return new TransformedIterator<Map.Entry<K, V>, V>(entryIterator)
/*      */       {
/*      */         @ParametricNullness
/*      */         V transform(Map.Entry<K, V> entry) {
/*  142 */           return entry.getValue();
/*      */         }
/*      */       };
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
/*      */   @GwtCompatible(serializable = true)
/*      */   @J2ktIncompatible
/*      */   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
/*  162 */     if (map instanceof ImmutableEnumMap) {
/*      */       
/*  164 */       ImmutableEnumMap<K, V> result = (ImmutableEnumMap)map;
/*  165 */       return result;
/*      */     } 
/*  167 */     Iterator<? extends Map.Entry<K, ? extends V>> entryItr = map.entrySet().iterator();
/*  168 */     if (!entryItr.hasNext()) {
/*  169 */       return ImmutableMap.of();
/*      */     }
/*  171 */     Map.Entry<K, ? extends V> entry1 = entryItr.next();
/*  172 */     Enum enum_ = (Enum)entry1.getKey();
/*  173 */     V value1 = entry1.getValue();
/*  174 */     CollectPreconditions.checkEntryNotNull(enum_, value1);
/*      */     
/*  176 */     EnumMap<K, V> enumMap = new EnumMap<>(Collections.singletonMap((K)enum_, value1));
/*  177 */     while (entryItr.hasNext()) {
/*  178 */       Map.Entry<K, ? extends V> entry = entryItr.next();
/*  179 */       Enum enum_1 = (Enum)entry.getKey();
/*  180 */       V value = entry.getValue();
/*  181 */       CollectPreconditions.checkEntryNotNull(enum_1, value);
/*  182 */       enumMap.put((K)enum_1, value);
/*      */     } 
/*  184 */     return ImmutableEnumMap.asImmutable(enumMap);
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
/*      */   @J2ktIncompatible
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  206 */     return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction);
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
/*      */   @J2ktIncompatible
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  226 */     return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction, mergeFunction);
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
/*      */   public static <K, V> HashMap<K, V> newHashMap() {
/*  244 */     return new HashMap<>();
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
/*      */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
/*  263 */     return new HashMap<>(map);
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
/*      */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  279 */     return new HashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  287 */     if (expectedSize < 3) {
/*  288 */       CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  289 */       return expectedSize + 1;
/*      */     } 
/*  291 */     if (expectedSize < 1073741824)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  304 */       return (int)Math.ceil(expectedSize / 0.75D);
/*      */     }
/*  306 */     return Integer.MAX_VALUE;
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
/*  322 */     return new LinkedHashMap<>();
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
/*  340 */     return new LinkedHashMap<>(map);
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  357 */     return new LinkedHashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
/*  366 */     return new ConcurrentHashMap<>();
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
/*      */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
/*  382 */     return new TreeMap<>();
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
/*      */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
/*  403 */     return new TreeMap<>(map);
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
/*      */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@CheckForNull Comparator<C> comparator) {
/*  426 */     return new TreeMap<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
/*  437 */     return new EnumMap<>((Class<K>)Preconditions.checkNotNull(type));
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
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
/*  454 */     return new EnumMap<>(map);
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
/*      */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  468 */     return new IdentityHashMap<>();
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
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  489 */     if (left instanceof SortedMap) {
/*      */       
/*  491 */       SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
/*  492 */       return difference(sortedLeft, right);
/*      */     } 
/*  494 */     return difference(left, right, Equivalence.equals());
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
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
/*  516 */     Preconditions.checkNotNull(valueEquivalence);
/*      */     
/*  518 */     Map<K, V> onlyOnLeft = newLinkedHashMap();
/*  519 */     Map<K, V> onlyOnRight = new LinkedHashMap<>(right);
/*  520 */     Map<K, V> onBoth = newLinkedHashMap();
/*  521 */     Map<K, MapDifference.ValueDifference<V>> differences = newLinkedHashMap();
/*  522 */     doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
/*  523 */     return new MapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
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
/*      */   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  546 */     Preconditions.checkNotNull(left);
/*  547 */     Preconditions.checkNotNull(right);
/*  548 */     Comparator<? super K> comparator = orNaturalOrder(left.comparator());
/*  549 */     SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
/*  550 */     SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
/*  551 */     onlyOnRight.putAll(right);
/*  552 */     SortedMap<K, V> onBoth = newTreeMap(comparator);
/*  553 */     SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
/*      */     
/*  555 */     doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
/*  556 */     return new SortedMapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  567 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  568 */       K leftKey = entry.getKey();
/*  569 */       V leftValue = entry.getValue();
/*  570 */       if (right.containsKey(leftKey)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  581 */         V rightValue = NullnessCasts.uncheckedCastNullableTToT(onlyOnRight.remove(leftKey));
/*  582 */         if (valueEquivalence.equivalent(leftValue, rightValue)) {
/*  583 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  585 */         differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
/*      */         continue;
/*      */       } 
/*  588 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> unmodifiableMap(Map<K, ? extends V> map) {
/*  595 */     if (map instanceof SortedMap) {
/*  596 */       return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)map);
/*      */     }
/*  598 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   static class MapDifferenceImpl<K, V>
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final Map<K, V> onlyOnLeft;
/*      */     
/*      */     final Map<K, V> onlyOnRight;
/*      */     
/*      */     final Map<K, V> onBoth;
/*      */     
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  614 */       this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
/*  615 */       this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
/*  616 */       this.onBoth = Maps.unmodifiableMap(onBoth);
/*  617 */       this.differences = (Map)Maps.unmodifiableMap((Map)differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean areEqual() {
/*  622 */       return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnLeft() {
/*  627 */       return this.onlyOnLeft;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnRight() {
/*  632 */       return this.onlyOnRight;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesInCommon() {
/*  637 */       return this.onBoth;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  642 */       return this.differences;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/*  647 */       if (object == this) {
/*  648 */         return true;
/*      */       }
/*  650 */       if (object instanceof MapDifference) {
/*  651 */         MapDifference<?, ?> other = (MapDifference<?, ?>)object;
/*  652 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && 
/*  653 */           entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && 
/*  654 */           entriesInCommon().equals(other.entriesInCommon()) && 
/*  655 */           entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*  657 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  662 */       return Objects.hashCode(new Object[] {
/*  663 */             entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()
/*      */           });
/*      */     }
/*      */     
/*      */     public String toString() {
/*  668 */       if (areEqual()) {
/*  669 */         return "equal";
/*      */       }
/*      */       
/*  672 */       StringBuilder result = new StringBuilder("not equal");
/*  673 */       if (!this.onlyOnLeft.isEmpty()) {
/*  674 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  676 */       if (!this.onlyOnRight.isEmpty()) {
/*  677 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  679 */       if (!this.differences.isEmpty()) {
/*  680 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  682 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
/*      */     @ParametricNullness
/*      */     private final V left;
/*      */     @ParametricNullness
/*      */     private final V right;
/*      */     
/*      */     static <V> MapDifference.ValueDifference<V> create(@ParametricNullness V left, @ParametricNullness V right) {
/*  693 */       return new ValueDifferenceImpl<>(left, right);
/*      */     }
/*      */     
/*      */     private ValueDifferenceImpl(@ParametricNullness V left, @ParametricNullness V right) {
/*  697 */       this.left = left;
/*  698 */       this.right = right;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V leftValue() {
/*  704 */       return this.left;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V rightValue() {
/*  710 */       return this.right;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/*  715 */       if (object instanceof MapDifference.ValueDifference) {
/*  716 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*  717 */         return (Objects.equal(this.left, that.leftValue()) && 
/*  718 */           Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*  720 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  725 */       return Objects.hashCode(new Object[] { this.left, this.right });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  730 */       return "(" + this.left + ", " + this.right + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SortedMapDifferenceImpl<K, V>
/*      */     extends MapDifferenceImpl<K, V>
/*      */     implements SortedMapDifference<K, V>
/*      */   {
/*      */     SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
/*  741 */       super(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  746 */       return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesInCommon() {
/*  751 */       return (SortedMap<K, V>)super.entriesInCommon();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnLeft() {
/*  756 */       return (SortedMap<K, V>)super.entriesOnlyOnLeft();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnRight() {
/*  761 */       return (SortedMap<K, V>)super.entriesOnlyOnRight();
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
/*      */   static <E> Comparator<? super E> orNaturalOrder(@CheckForNull Comparator<? super E> comparator) {
/*  773 */     if (comparator != null) {
/*  774 */       return comparator;
/*      */     }
/*  776 */     return Ordering.natural();
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
/*      */   public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
/*  805 */     return new AsMapView<>(set, function);
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
/*      */   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
/*  833 */     return new SortedAsMapView<>(set, function);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
/*  862 */     return new NavigableAsMapView<>(set, function);
/*      */   }
/*      */   
/*      */   private static class AsMapView<K, V>
/*      */     extends ViewCachingAbstractMap<K, V>
/*      */   {
/*      */     private final Set<K> set;
/*      */     final Function<? super K, V> function;
/*      */     
/*      */     Set<K> backingSet() {
/*  872 */       return this.set;
/*      */     }
/*      */     
/*      */     AsMapView(Set<K> set, Function<? super K, V> function) {
/*  876 */       this.set = (Set<K>)Preconditions.checkNotNull(set);
/*  877 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> createKeySet() {
/*  882 */       return Maps.removeOnlySet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/*  887 */       return Collections2.transform(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  892 */       return backingSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  897 */       return backingSet().contains(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/*  903 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
/*  909 */       if (Collections2.safeContains(backingSet(), key)) {
/*      */         
/*  911 */         K k = (K)key;
/*  912 */         return (V)this.function.apply(k);
/*      */       } 
/*  914 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/*  921 */       if (backingSet().remove(key)) {
/*      */         
/*  923 */         K k = (K)key;
/*  924 */         return (V)this.function.apply(k);
/*      */       } 
/*  926 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  932 */       backingSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/*  941 */           return Maps.AsMapView.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/*  946 */           return Maps.asMapEntryIterator(Maps.AsMapView.this.backingSet(), Maps.AsMapView.this.function);
/*      */         }
/*      */       };
/*  949 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  954 */       Preconditions.checkNotNull(action);
/*      */       
/*  956 */       backingSet().forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
/*  962 */     return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator())
/*      */       {
/*      */         Map.Entry<K, V> transform(@ParametricNullness K key) {
/*  965 */           return Maps.immutableEntry(key, (V)function.apply(key));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class SortedAsMapView<K, V>
/*      */     extends AsMapView<K, V>
/*      */     implements SortedMap<K, V> {
/*      */     SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
/*  974 */       super(set, function);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> backingSet() {
/*  979 */       return (SortedSet<K>)super.backingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/*  985 */       return backingSet().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  990 */       return Maps.removeOnlySortedSet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/*  995 */       return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/* 1000 */       return Maps.asMap(backingSet().headSet(toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/* 1005 */       return Maps.asMap(backingSet().tailSet(fromKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K firstKey() {
/* 1011 */       return backingSet().first();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K lastKey() {
/* 1017 */       return backingSet().last();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class NavigableAsMapView<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableSet<K> set;
/*      */ 
/*      */     
/*      */     private final Function<? super K, V> function;
/*      */ 
/*      */     
/*      */     NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
/* 1034 */       this.set = (NavigableSet<K>)Preconditions.checkNotNull(ks);
/* 1035 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(vFunction);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 1044 */       return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 1049 */       return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 1054 */       return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 1060 */       return this.set.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/* 1066 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
/* 1072 */       if (Collections2.safeContains(this.set, key)) {
/*      */         
/* 1074 */         K k = (K)key;
/* 1075 */         return (V)this.function.apply(k);
/*      */       } 
/* 1077 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1083 */       this.set.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1088 */       return Maps.asMapEntryIterator(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1093 */       return CollectSpliterators.map(this.set.spliterator(), e -> Maps.immutableEntry(e, this.function.apply(e)));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1098 */       this.set.forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 1103 */       return descendingMap().entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1108 */       return Maps.removeOnlyNavigableSet(this.set);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1113 */       return this.set.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1118 */       return Maps.asMap(this.set.descendingSet(), this.function);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Set<E> removeOnlySet(final Set<E> set) {
/* 1123 */     return new ForwardingSet<E>()
/*      */       {
/*      */         protected Set<E> delegate() {
/* 1126 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(@ParametricNullness E element) {
/* 1131 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1136 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
/* 1143 */     return new ForwardingSortedSet<E>()
/*      */       {
/*      */         protected SortedSet<E> delegate() {
/* 1146 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(@ParametricNullness E element) {
/* 1151 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1156 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(@ParametricNullness E toElement) {
/* 1161 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 1167 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/* 1172 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
/* 1180 */     return new ForwardingNavigableSet<E>()
/*      */       {
/*      */         protected NavigableSet<E> delegate() {
/* 1183 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(@ParametricNullness E element) {
/* 1188 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1193 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(@ParametricNullness E toElement) {
/* 1198 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 1203 */           return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 1209 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 1218 */           return Maps.removeOnlyNavigableSet(super
/* 1219 */               .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/* 1224 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 1229 */           return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> descendingSet() {
/* 1234 */           return Maps.removeOnlyNavigableSet(super.descendingSet());
/*      */         }
/*      */       };
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
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
/* 1257 */     return toMap(keys.iterator(), valueFunction);
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
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
/* 1275 */     Preconditions.checkNotNull(valueFunction);
/* 1276 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/* 1277 */     while (keys.hasNext()) {
/* 1278 */       K key = keys.next();
/* 1279 */       builder.put(key, (V)valueFunction.apply(key));
/*      */     } 
/*      */     
/* 1282 */     return builder.buildKeepingLast();
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1328 */     if (values instanceof Collection) {
/* 1329 */       return uniqueIndex(values
/* 1330 */           .iterator(), keyFunction, 
/*      */           
/* 1332 */           ImmutableMap.builderWithExpectedSize(((Collection)values).size()));
/*      */     }
/* 1334 */     return uniqueIndex(values.iterator(), keyFunction);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1369 */     return uniqueIndex(values, keyFunction, ImmutableMap.builder());
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction, ImmutableMap.Builder<K, V> builder) {
/* 1374 */     Preconditions.checkNotNull(keyFunction);
/* 1375 */     while (values.hasNext()) {
/* 1376 */       V value = values.next();
/* 1377 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/*      */     try {
/* 1380 */       return builder.buildOrThrow();
/* 1381 */     } catch (IllegalArgumentException duplicateKeys) {
/* 1382 */       throw new IllegalArgumentException(duplicateKeys
/* 1383 */           .getMessage() + ". To index multiple values under a key, use Multimaps.index.");
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/* 1401 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/* 1403 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1416 */       String key = Objects.<String>requireNonNull((String)e.nextElement());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1431 */       builder.put(key, Objects.<String>requireNonNull(properties.getProperty(key)));
/*      */     } 
/*      */     
/* 1434 */     return builder.buildOrThrow();
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K, V> Map.Entry<K, V> immutableEntry(@ParametricNullness K key, @ParametricNullness V value) {
/* 1452 */     return new ImmutableEntry<>(key, value);
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
/*      */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
/* 1465 */     return new UnmodifiableEntrySet<>(Collections.unmodifiableSet(entrySet));
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
/*      */   static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
/* 1479 */     Preconditions.checkNotNull(entry);
/* 1480 */     return new AbstractMapEntry<K, V>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public K getKey() {
/* 1484 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public V getValue() {
/* 1490 */           return (V)entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
/* 1498 */     return new UnmodifiableIterator<Map.Entry<K, V>>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1501 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public Map.Entry<K, V> next() {
/* 1506 */           return Maps.unmodifiableEntry(entryIterator.next());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>>
/*      */   {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */     
/*      */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1517 */       this.entries = entries;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<Map.Entry<K, V>> delegate() {
/* 1522 */       return this.entries;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1527 */       return Maps.unmodifiableEntryIterator(this.entries.iterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1540 */       return standardToArray();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1546 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>> {
/*      */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
/* 1554 */       super(entries);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1561 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1566 */       return Sets.hashCodeImpl(this);
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
/*      */   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
/* 1581 */     return new BiMapConverter<>(bimap);
/*      */   }
/*      */   
/*      */   private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable { private final BiMap<A, B> bimap;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     BiMapConverter(BiMap<A, B> bimap) {
/* 1588 */       this.bimap = (BiMap<A, B>)Preconditions.checkNotNull(bimap);
/*      */     }
/*      */ 
/*      */     
/*      */     protected B doForward(A a) {
/* 1593 */       return convert(this.bimap, a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected A doBackward(B b) {
/* 1598 */       return convert(this.bimap.inverse(), b);
/*      */     }
/*      */     
/*      */     private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
/* 1602 */       Y output = bimap.get(input);
/* 1603 */       Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", input);
/* 1604 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1609 */       if (object instanceof BiMapConverter) {
/* 1610 */         BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
/* 1611 */         return this.bimap.equals(that.bimap);
/*      */       } 
/* 1613 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1618 */       return this.bimap.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1624 */       return "Maps.asConverter(" + this.bimap + ")";
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
/*      */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
/* 1661 */     return Synchronized.biMap(bimap, null);
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
/*      */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
/* 1677 */     return new UnmodifiableBiMap<>(bimap, null);
/*      */   }
/*      */   private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable { final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     @RetainedWith
/*      */     BiMap<V, K> inverse;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     transient Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @CheckForNull BiMap<V, K> inverse) {
/* 1691 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/* 1692 */       this.delegate = delegate;
/* 1693 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, V> delegate() {
/* 1698 */       return this.unmodifiableMap;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
/* 1704 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 1709 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V putIfAbsent(K key, V value) {
/* 1715 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1720 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean replace(K key, V oldValue, V newValue) {
/* 1725 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V replace(K key, V value) {
/* 1731 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 1737 */       throw new UnsupportedOperationException();
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
/* 1749 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1757 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> function) {
/* 1767 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1772 */       BiMap<V, K> result = this.inverse;
/* 1773 */       return (result == null) ? (
/* 1774 */         this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : 
/* 1775 */         result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1780 */       Set<V> result = this.values;
/* 1781 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
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
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1823 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1866 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1912 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1966 */     return new TransformedEntriesMap<>(fromMap, transformer);
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
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2020 */     return new TransformedEntriesSortedMap<>(fromMap, transformer);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2076 */     return new TransformedEntriesNavigableMap<>(fromMap, transformer);
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
/*      */   static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
/* 2112 */     Preconditions.checkNotNull(function);
/* 2113 */     return new EntryTransformer<K, V1, V2>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public V2 transformEntry(@ParametricNullness K key, @ParametricNullness V1 value) {
/* 2117 */           return (V2)function.apply(value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, @ParametricNullness final K key) {
/* 2125 */     Preconditions.checkNotNull(transformer);
/* 2126 */     return new Function<V1, V2>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public V2 apply(@ParametricNullness V1 v1) {
/* 2130 */           return transformer.transformEntry(key, v1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2139 */     Preconditions.checkNotNull(transformer);
/* 2140 */     return new Function<Map.Entry<K, V1>, V2>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public V2 apply(Map.Entry<K, V1> entry) {
/* 2144 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
/* 2153 */     Preconditions.checkNotNull(transformer);
/* 2154 */     Preconditions.checkNotNull(entry);
/* 2155 */     return new AbstractMapEntry<K, V2>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public K getKey() {
/* 2159 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public V2 getValue() {
/* 2165 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2174 */     Preconditions.checkNotNull(transformer);
/* 2175 */     return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>()
/*      */       {
/*      */         public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
/* 2178 */           return Maps.transformEntry(transformer, entry);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static class TransformedEntriesMap<K, V1, V2>
/*      */     extends IteratorBasedAbstractMap<K, V2>
/*      */   {
/*      */     final Map<K, V1> fromMap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2191 */       this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
/* 2192 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2197 */       return this.fromMap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 2202 */       return this.fromMap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V2 get(@CheckForNull Object key) {
/* 2208 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V2 getOrDefault(@CheckForNull Object key, @CheckForNull V2 defaultValue) {
/* 2216 */       V1 value = this.fromMap.get(key);
/* 2217 */       if (value != null || this.fromMap.containsKey(key))
/*      */       {
/* 2219 */         return this.transformer.transformEntry((K)key, NullnessCasts.uncheckedCastNullableTToT(value));
/*      */       }
/* 2221 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V2 remove(@CheckForNull Object key) {
/* 2229 */       return this.fromMap.containsKey(key) ? 
/*      */         
/* 2231 */         this.transformer.transformEntry((K)key, NullnessCasts.uncheckedCastNullableTToT(this.fromMap.remove(key))) : 
/* 2232 */         null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2237 */       this.fromMap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 2242 */       return this.fromMap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 2247 */       return Iterators.transform(this.fromMap
/* 2248 */           .entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V2>> entrySpliterator() {
/* 2253 */       return CollectSpliterators.map(this.fromMap
/* 2254 */           .entrySet().spliterator(), (Function<?, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V2> action) {
/* 2259 */       Preconditions.checkNotNull(action);
/*      */       
/* 2261 */       this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((K)k, (V1)v1)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> values() {
/* 2266 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static class TransformedEntriesSortedMap<K, V1, V2>
/*      */     extends TransformedEntriesMap<K, V1, V2>
/*      */     implements SortedMap<K, V2>
/*      */   {
/*      */     protected SortedMap<K, V1> fromMap() {
/* 2275 */       return (SortedMap<K, V1>)this.fromMap;
/*      */     }
/*      */ 
/*      */     
/*      */     TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2280 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 2286 */       return fromMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K firstKey() {
/* 2292 */       return fromMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> headMap(@ParametricNullness K toKey) {
/* 2297 */       return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K lastKey() {
/* 2303 */       return fromMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 2308 */       return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> tailMap(@ParametricNullness K fromKey) {
/* 2313 */       return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class TransformedEntriesNavigableMap<K, V1, V2>
/*      */     extends TransformedEntriesSortedMap<K, V1, V2>
/*      */     implements NavigableMap<K, V2>
/*      */   {
/*      */     TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2324 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> ceilingEntry(@ParametricNullness K key) {
/* 2330 */       return transformEntry(fromMap().ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceilingKey(@ParametricNullness K key) {
/* 2336 */       return fromMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 2341 */       return fromMap().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> descendingMap() {
/* 2346 */       return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> firstEntry() {
/* 2352 */       return transformEntry(fromMap().firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> floorEntry(@ParametricNullness K key) {
/* 2358 */       return transformEntry(fromMap().floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floorKey(@ParametricNullness K key) {
/* 2364 */       return fromMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(@ParametricNullness K toKey) {
/* 2369 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 2374 */       return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> higherEntry(@ParametricNullness K key) {
/* 2380 */       return transformEntry(fromMap().higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higherKey(@ParametricNullness K key) {
/* 2386 */       return fromMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> lastEntry() {
/* 2392 */       return transformEntry(fromMap().lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> lowerEntry(@ParametricNullness K key) {
/* 2398 */       return transformEntry(fromMap().lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lowerKey(@ParametricNullness K key) {
/* 2404 */       return fromMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2409 */       return fromMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> pollFirstEntry() {
/* 2415 */       return transformEntry(fromMap().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V2> pollLastEntry() {
/* 2421 */       return transformEntry(fromMap().pollLastEntry());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 2430 */       return Maps.transformEntries(
/* 2431 */           fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 2436 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(@ParametricNullness K fromKey) {
/* 2441 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 2446 */       return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     private Map.Entry<K, V2> transformEntry(@CheckForNull Map.Entry<K, V1> entry) {
/* 2451 */       return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableMap<K, V1> fromMap() {
/* 2456 */       return (NavigableMap<K, V1>)super.fromMap();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
/* 2462 */     return Predicates.compose(keyPredicate, keyFunction());
/*      */   }
/*      */ 
/*      */   
/*      */   static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
/* 2467 */     return Predicates.compose(valuePredicate, valueFunction());
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
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2495 */     Preconditions.checkNotNull(keyPredicate);
/* 2496 */     Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
/* 2497 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2498 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, (Predicate)entryPredicate) : 
/* 2499 */       new FilteredKeyMap<>((Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, (Predicate)entryPredicate);
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
/*      */   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2532 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2567 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2596 */     Preconditions.checkNotNull(keyPredicate);
/* 2597 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2625 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2657 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2690 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2722 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2751 */     Preconditions.checkNotNull(entryPredicate);
/* 2752 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2753 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : 
/* 2754 */       new FilteredEntryMap<>((Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2786 */     Preconditions.checkNotNull(entryPredicate);
/* 2787 */     return (unfiltered instanceof FilteredEntrySortedMap) ? 
/* 2788 */       filterFiltered((FilteredEntrySortedMap<K, V>)unfiltered, entryPredicate) : 
/* 2789 */       new FilteredEntrySortedMap<>((SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2822 */     Preconditions.checkNotNull(entryPredicate);
/* 2823 */     return (unfiltered instanceof FilteredEntryNavigableMap) ? 
/* 2824 */       filterFiltered((FilteredEntryNavigableMap<K, V>)unfiltered, entryPredicate) : 
/* 2825 */       new FilteredEntryNavigableMap<>((NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2857 */     Preconditions.checkNotNull(unfiltered);
/* 2858 */     Preconditions.checkNotNull(entryPredicate);
/* 2859 */     return (unfiltered instanceof FilteredEntryBiMap) ? 
/* 2860 */       filterFiltered((FilteredEntryBiMap<K, V>)unfiltered, entryPredicate) : 
/* 2861 */       new FilteredEntryBiMap<>(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2870 */     return new FilteredEntryMap<>(map.unfiltered, 
/* 2871 */         Predicates.and(map.predicate, entryPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2881 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2882 */     return new FilteredEntrySortedMap<>(map.sortedMap(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2894 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
/* 2895 */     return new FilteredEntryNavigableMap<>(map.unfiltered, predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2905 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2906 */     return new FilteredEntryBiMap<>(map.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V>
/*      */     extends ViewCachingAbstractMap<K, V>
/*      */   {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2916 */       this.unfiltered = unfiltered;
/* 2917 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(@CheckForNull Object key, @ParametricNullness V value) {
/* 2924 */       K k = (K)key;
/* 2925 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(@ParametricNullness K key, @ParametricNullness V value) {
/* 2931 */       Preconditions.checkArgument(apply(key, value));
/* 2932 */       return this.unfiltered.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 2937 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 2938 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/* 2940 */       this.unfiltered.putAll(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 2945 */       return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/* 2951 */       V value = this.unfiltered.get(key);
/* 2952 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2957 */       return entrySet().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/* 2963 */       return containsKey(key) ? this.unfiltered.remove(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 2968 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.predicate);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class FilteredMapValues<K, V>
/*      */     extends Values<K, V>
/*      */   {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2980 */       super(filteredMap);
/* 2981 */       this.unfiltered = unfiltered;
/* 2982 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 2987 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2988 */       while (entryItr.hasNext()) {
/* 2989 */         Map.Entry<K, V> entry = entryItr.next();
/* 2990 */         if (this.predicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 2991 */           entryItr.remove();
/* 2992 */           return true;
/*      */         } 
/*      */       } 
/* 2995 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/* 3000 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 3001 */       boolean result = false;
/* 3002 */       while (entryItr.hasNext()) {
/* 3003 */         Map.Entry<K, V> entry = entryItr.next();
/* 3004 */         if (this.predicate.apply(entry) && collection.contains(entry.getValue())) {
/* 3005 */           entryItr.remove();
/* 3006 */           result = true;
/*      */         } 
/*      */       } 
/* 3009 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/* 3014 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 3015 */       boolean result = false;
/* 3016 */       while (entryItr.hasNext()) {
/* 3017 */         Map.Entry<K, V> entry = entryItr.next();
/* 3018 */         if (this.predicate.apply(entry) && !collection.contains(entry.getValue())) {
/* 3019 */           entryItr.remove();
/* 3020 */           result = true;
/*      */         } 
/*      */       } 
/* 3023 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 3029 */       return Lists.<V>newArrayList(iterator()).toArray();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 3035 */       return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredKeyMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Predicate<? super K> keyPredicate;
/*      */ 
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3047 */       super(unfiltered, entryPredicate);
/* 3048 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 3053 */       return Sets.filter(this.unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 3058 */       return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 3066 */       return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */ 
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3079 */       super(unfiltered, entryPredicate);
/* 3080 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 3085 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       protected Set<Map.Entry<K, V>> delegate() {
/* 3092 */         return Maps.FilteredEntryMap.this.filteredEntrySet;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 3097 */         return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
/*      */           {
/*      */             Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
/* 3100 */               return new ForwardingMapEntry<K, V>()
/*      */                 {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 3103 */                     return entry;
/*      */                   }
/*      */ 
/*      */                   
/*      */                   @ParametricNullness
/*      */                   public V setValue(@ParametricNullness V newValue) {
/* 3109 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(getKey(), newValue));
/* 3110 */                     return super.setValue(newValue);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 3120 */       return new KeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean removeAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 3125 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 3126 */       boolean result = false;
/* 3127 */       while (entryItr.hasNext()) {
/* 3128 */         Map.Entry<K, V> entry = entryItr.next();
/* 3129 */         if (entryPredicate.apply(entry) && keyCollection.contains(entry.getKey())) {
/* 3130 */           entryItr.remove();
/* 3131 */           result = true;
/*      */         } 
/*      */       } 
/* 3134 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean retainAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 3139 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 3140 */       boolean result = false;
/* 3141 */       while (entryItr.hasNext()) {
/* 3142 */         Map.Entry<K, V> entry = entryItr.next();
/* 3143 */         if (entryPredicate.apply(entry) && !keyCollection.contains(entry.getKey())) {
/* 3144 */           entryItr.remove();
/* 3145 */           result = true;
/*      */         } 
/*      */       } 
/* 3148 */       return result;
/*      */     }
/*      */     
/*      */     class KeySet
/*      */       extends Maps.KeySet<K, V> {
/*      */       KeySet() {
/* 3154 */         super(Maps.FilteredEntryMap.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object o) {
/* 3159 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 3160 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 3161 */           return true;
/*      */         } 
/* 3163 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> collection) {
/* 3168 */         return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> collection) {
/* 3173 */         return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 3179 */         return Lists.<K>newArrayList(iterator()).toArray();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 3185 */         return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredEntrySortedMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3196 */       super(unfiltered, entryPredicate);
/*      */     }
/*      */     
/*      */     SortedMap<K, V> sortedMap() {
/* 3200 */       return (SortedMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 3205 */       return (SortedSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 3210 */       return new SortedKeySet();
/*      */     }
/*      */     
/*      */     class SortedKeySet
/*      */       extends Maps.FilteredEntryMap<K, V>.KeySet
/*      */       implements SortedSet<K> {
/*      */       @CheckForNull
/*      */       public Comparator<? super K> comparator() {
/* 3218 */         return Maps.FilteredEntrySortedMap.this.sortedMap().comparator();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public SortedSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
/* 3224 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> headSet(@ParametricNullness K toElement) {
/* 3229 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> tailSet(@ParametricNullness K fromElement) {
/* 3234 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       @ParametricNullness
/*      */       public K first() {
/* 3240 */         return (K)Maps.FilteredEntrySortedMap.this.firstKey();
/*      */       }
/*      */ 
/*      */       
/*      */       @ParametricNullness
/*      */       public K last() {
/* 3246 */         return (K)Maps.FilteredEntrySortedMap.this.lastKey();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 3253 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K firstKey() {
/* 3260 */       return keySet().iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K lastKey() {
/* 3266 */       SortedMap<K, V> headMap = sortedMap();
/*      */       
/*      */       while (true) {
/* 3269 */         K key = headMap.lastKey();
/*      */         
/* 3271 */         if (apply(key, NullnessCasts.uncheckedCastNullableTToT(this.unfiltered.get(key)))) {
/* 3272 */           return key;
/*      */         }
/* 3274 */         headMap = sortedMap().headMap(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/* 3280 */       return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 3285 */       return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/* 3290 */       return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredEntryNavigableMap<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableMap<K, V> unfiltered;
/*      */ 
/*      */     
/*      */     private final Predicate<? super Map.Entry<K, V>> entryPredicate;
/*      */ 
/*      */     
/*      */     private final Map<K, V> filteredDelegate;
/*      */ 
/*      */     
/*      */     FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3310 */       this.unfiltered = (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered);
/* 3311 */       this.entryPredicate = entryPredicate;
/* 3312 */       this.filteredDelegate = new Maps.FilteredEntryMap<>(unfiltered, entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 3318 */       return this.unfiltered.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3323 */       return new Maps.NavigableKeySet<K, V>(this)
/*      */         {
/*      */           public boolean removeAll(Collection<?> collection) {
/* 3326 */             return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean retainAll(Collection<?> collection) {
/* 3331 */             return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3338 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 3343 */       return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 3348 */       return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3353 */       return this.filteredDelegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3358 */       return !Iterables.any(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/* 3364 */       return this.filteredDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 3369 */       return this.filteredDelegate.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(@ParametricNullness K key, @ParametricNullness V value) {
/* 3375 */       return this.filteredDelegate.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/* 3381 */       return this.filteredDelegate.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 3386 */       this.filteredDelegate.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3391 */       this.filteredDelegate.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3396 */       return this.filteredDelegate.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 3402 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 3408 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3413 */       return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 3422 */       return Maps.filterEntries(this.unfiltered
/* 3423 */           .subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 3428 */       return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 3433 */       return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class FilteredEntryBiMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements BiMap<K, V> {
/*      */     @RetainedWith
/*      */     private final BiMap<V, K> inverse;
/*      */     
/*      */     private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
/* 3444 */       return new Predicate<Map.Entry<V, K>>()
/*      */         {
/*      */           public boolean apply(Map.Entry<V, K> input) {
/* 3447 */             return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
/* 3453 */       super(delegate, predicate);
/* 3454 */       this
/* 3455 */         .inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
/*      */     }
/*      */ 
/*      */     
/*      */     private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
/* 3460 */       super(delegate, predicate);
/* 3461 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     BiMap<K, V> unfiltered() {
/* 3465 */       return (BiMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
/* 3471 */       Preconditions.checkArgument(apply(key, value));
/* 3472 */       return unfiltered().forcePut(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 3477 */       unfiltered()
/* 3478 */         .replaceAll((key, value) -> this.predicate.apply(Maps.immutableEntry(key, value)) ? function.apply(key, value) : value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 3487 */       return this.inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 3492 */       return this.inverse.keySet();
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> map) {
/* 3517 */     Preconditions.checkNotNull(map);
/* 3518 */     if (map instanceof UnmodifiableNavigableMap)
/*      */     {
/* 3520 */       return (NavigableMap)map;
/*      */     }
/*      */     
/* 3523 */     return new UnmodifiableNavigableMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@CheckForNull Map.Entry<K, ? extends V> entry) {
/* 3530 */     return (entry == null) ? null : unmodifiableEntry(entry);
/*      */   }
/*      */   @GwtIncompatible
/*      */   static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable { private final NavigableMap<K, ? extends V> delegate;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient UnmodifiableNavigableMap<K, V> descendingMap;
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate) {
/* 3539 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
/* 3544 */       this.delegate = delegate;
/* 3545 */       this.descendingMap = descendingMap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> delegate() {
/* 3550 */       return Collections.unmodifiableSortedMap(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lowerEntry(@ParametricNullness K key) {
/* 3556 */       return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lowerKey(@ParametricNullness K key) {
/* 3562 */       return this.delegate.lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> floorEntry(@ParametricNullness K key) {
/* 3568 */       return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floorKey(@ParametricNullness K key) {
/* 3574 */       return this.delegate.floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> ceilingEntry(@ParametricNullness K key) {
/* 3580 */       return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceilingKey(@ParametricNullness K key) {
/* 3586 */       return this.delegate.ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> higherEntry(@ParametricNullness K key) {
/* 3592 */       return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higherKey(@ParametricNullness K key) {
/* 3598 */       return this.delegate.higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> firstEntry() {
/* 3604 */       return Maps.unmodifiableOrNull(this.delegate.firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lastEntry() {
/* 3610 */       return Maps.unmodifiableOrNull(this.delegate.lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public final Map.Entry<K, V> pollFirstEntry() {
/* 3616 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public final Map.Entry<K, V> pollLastEntry() {
/* 3622 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 3627 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V putIfAbsent(K key, V value) {
/* 3633 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 3638 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean replace(K key, V oldValue, V newValue) {
/* 3643 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V replace(K key, V value) {
/* 3649 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 3655 */       throw new UnsupportedOperationException();
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
/* 3667 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 3675 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> function) {
/* 3685 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3692 */       UnmodifiableNavigableMap<K, V> result = this.descendingMap;
/* 3693 */       return (result == null) ? (
/* 3694 */         this.descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : 
/* 3695 */         result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3700 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3705 */       return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 3710 */       return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 3715 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 3724 */       return Maps.unmodifiableNavigableMap(this.delegate
/* 3725 */           .subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/* 3730 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 3735 */       return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/* 3740 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 3745 */       return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
/* 3801 */     return Synchronized.navigableMap(navigableMap);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ViewCachingAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient Set<K> keySet;
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient Collection<V> values;
/*      */     
/*      */     abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3822 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3823 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3830 */       Set<K> result = this.keySet;
/* 3831 */       return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */     }
/*      */     
/*      */     Set<K> createKeySet() {
/* 3835 */       return new Maps.KeySet<>(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3842 */       Collection<V> result = this.values;
/* 3843 */       return (result == null) ? (this.values = createValues()) : result;
/*      */     }
/*      */     
/*      */     Collection<V> createValues() {
/* 3847 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class IteratorBasedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     public abstract int size();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator();
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 3860 */       return Spliterators.spliterator(
/* 3861 */           entryIterator(), size(), 65);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3866 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/* 3869 */             return Maps.IteratorBasedAbstractMap.this;
/*      */           }
/*      */ 
/*      */           
/*      */           public Iterator<Map.Entry<K, V>> iterator() {
/* 3874 */             return Maps.IteratorBasedAbstractMap.this.entryIterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public Spliterator<Map.Entry<K, V>> spliterator() {
/* 3879 */             return Maps.IteratorBasedAbstractMap.this.entrySpliterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 3884 */             Maps.IteratorBasedAbstractMap.this.forEachEntry(action);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     void forEachEntry(Consumer<? super Map.Entry<K, V>> action) {
/* 3890 */       entryIterator().forEachRemaining(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3895 */       Iterators.clear(entryIterator());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   static <V> V safeGet(Map<?, V> map, @CheckForNull Object key) {
/* 3905 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3907 */       return map.get(key);
/* 3908 */     } catch (ClassCastException|NullPointerException e) {
/* 3909 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean safeContainsKey(Map<?, ?> map, @CheckForNull Object key) {
/* 3918 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3920 */       return map.containsKey(key);
/* 3921 */     } catch (ClassCastException|NullPointerException e) {
/* 3922 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   static <V> V safeRemove(Map<?, V> map, @CheckForNull Object key) {
/* 3932 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3934 */       return map.remove(key);
/* 3935 */     } catch (ClassCastException|NullPointerException e) {
/* 3936 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsKeyImpl(Map<?, ?> map, @CheckForNull Object key) {
/* 3942 */     return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsValueImpl(Map<?, ?> map, @CheckForNull Object value) {
/* 3947 */     return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
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
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, @CheckForNull Object o) {
/* 3964 */     if (!(o instanceof Map.Entry)) {
/* 3965 */       return false;
/*      */     }
/* 3967 */     return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
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
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, @CheckForNull Object o) {
/* 3983 */     if (!(o instanceof Map.Entry)) {
/* 3984 */       return false;
/*      */     }
/* 3986 */     return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Map<?, ?> map, @CheckForNull Object object) {
/* 3991 */     if (map == object)
/* 3992 */       return true; 
/* 3993 */     if (object instanceof Map) {
/* 3994 */       Map<?, ?> o = (Map<?, ?>)object;
/* 3995 */       return map.entrySet().equals(o.entrySet());
/*      */     } 
/* 3997 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static String toStringImpl(Map<?, ?> map) {
/* 4002 */     StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
/* 4003 */     boolean first = true;
/* 4004 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 4005 */       if (!first) {
/* 4006 */         sb.append(", ");
/*      */       }
/* 4008 */       first = false;
/* 4009 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/*      */     } 
/* 4011 */     return sb.append('}').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
/* 4017 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
/* 4018 */       self.put(entry.getKey(), entry.getValue()); 
/*      */   }
/*      */   
/*      */   static class KeySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<K> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     KeySet(Map<K, V> map) {
/* 4027 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     Map<K, V> map() {
/* 4031 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 4036 */       return Maps.keyIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/* 4041 */       Preconditions.checkNotNull(action);
/*      */       
/* 4043 */       this.map.forEach((k, v) -> action.accept(k));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4048 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4053 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 4058 */       return map().containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 4063 */       if (contains(o)) {
/* 4064 */         map().remove(o);
/* 4065 */         return true;
/*      */       } 
/* 4067 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4072 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   static <K> K keyOrNull(@CheckForNull Map.Entry<K, ?> entry) {
/* 4078 */     return (entry == null) ? null : entry.getKey();
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   static <V> V valueOrNull(@CheckForNull Map.Entry<?, V> entry) {
/* 4083 */     return (entry == null) ? null : entry.getValue();
/*      */   }
/*      */   
/*      */   static class SortedKeySet<K, V>
/*      */     extends KeySet<K, V> implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, V> map) {
/* 4089 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> map() {
/* 4094 */       return (SortedMap<K, V>)super.map();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super K> comparator() {
/* 4100 */       return map().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
/* 4105 */       return new SortedKeySet(map().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(@ParametricNullness K toElement) {
/* 4110 */       return new SortedKeySet(map().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(@ParametricNullness K fromElement) {
/* 4115 */       return new SortedKeySet(map().tailMap(fromElement));
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K first() {
/* 4121 */       return map().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K last() {
/* 4127 */       return map().lastKey();
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class NavigableKeySet<K, V>
/*      */     extends SortedKeySet<K, V> implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, V> map) {
/* 4135 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, V> map() {
/* 4140 */       return (NavigableMap<K, V>)this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lower(@ParametricNullness K e) {
/* 4146 */       return map().lowerKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floor(@ParametricNullness K e) {
/* 4152 */       return map().floorKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceiling(@ParametricNullness K e) {
/* 4158 */       return map().ceilingKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higher(@ParametricNullness K e) {
/* 4164 */       return map().higherKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K pollFirst() {
/* 4170 */       return Maps.keyOrNull(map().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K pollLast() {
/* 4176 */       return Maps.keyOrNull(map().pollLastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 4181 */       return map().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 4186 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(@ParametricNullness K fromElement, boolean fromInclusive, @ParametricNullness K toElement, boolean toInclusive) {
/* 4195 */       return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
/* 4200 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(@ParametricNullness K toElement, boolean inclusive) {
/* 4205 */       return map().headMap(toElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(@ParametricNullness K toElement) {
/* 4210 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(@ParametricNullness K fromElement, boolean inclusive) {
/* 4215 */       return map().tailMap(fromElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(@ParametricNullness K fromElement) {
/* 4220 */       return tailSet(fromElement, true);
/*      */     }
/*      */   }
/*      */   
/*      */   static class Values<K, V> extends AbstractCollection<V> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     Values(Map<K, V> map) {
/* 4229 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     final Map<K, V> map() {
/* 4233 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 4238 */       return Maps.valueIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/* 4243 */       Preconditions.checkNotNull(action);
/*      */       
/* 4245 */       this.map.forEach((k, v) -> action.accept(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/*      */       try {
/* 4251 */         return super.remove(o);
/* 4252 */       } catch (UnsupportedOperationException e) {
/* 4253 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4254 */           if (Objects.equal(o, entry.getValue())) {
/* 4255 */             map().remove(entry.getKey());
/* 4256 */             return true;
/*      */           } 
/*      */         } 
/* 4259 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 4266 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 4267 */       } catch (UnsupportedOperationException e) {
/* 4268 */         Set<K> toRemove = Sets.newHashSet();
/* 4269 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4270 */           if (c.contains(entry.getValue())) {
/* 4271 */             toRemove.add(entry.getKey());
/*      */           }
/*      */         } 
/* 4274 */         return map().keySet().removeAll(toRemove);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 4281 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 4282 */       } catch (UnsupportedOperationException e) {
/* 4283 */         Set<K> toRetain = Sets.newHashSet();
/* 4284 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4285 */           if (c.contains(entry.getValue())) {
/* 4286 */             toRetain.add(entry.getKey());
/*      */           }
/*      */         } 
/* 4289 */         return map().keySet().retainAll(toRetain);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4295 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4300 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 4305 */       return map().containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4310 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Map<K, V> map();
/*      */     
/*      */     public int size() {
/* 4320 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4325 */       map().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 4330 */       if (o instanceof Map.Entry) {
/* 4331 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4332 */         Object key = entry.getKey();
/* 4333 */         V value = Maps.safeGet(map(), key);
/* 4334 */         return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
/*      */       } 
/* 4336 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4341 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 4350 */       if (contains(o) && o instanceof Map.Entry) {
/* 4351 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4352 */         return map().keySet().remove(entry.getKey());
/*      */       } 
/* 4354 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 4360 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 4361 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4363 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 4370 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 4371 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4373 */         Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
/* 4374 */         for (Object o : c) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4379 */           if (contains(o) && o instanceof Map.Entry) {
/* 4380 */             Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4381 */             keys.add(entry.getKey());
/*      */           } 
/*      */         } 
/* 4384 */         return map().keySet().retainAll(keys);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static abstract class DescendingMap<K, V>
/*      */     extends ForwardingMap<K, V> implements NavigableMap<K, V> {
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient Comparator<? super K> comparator;
/*      */     
/*      */     protected final Map<K, V> delegate() {
/* 4397 */       return forward();
/*      */     }
/*      */     
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 4405 */       Comparator<? super K> result = this.comparator;
/* 4406 */       if (result == null) {
/* 4407 */         Comparator<? super K> forwardCmp = forward().comparator();
/* 4408 */         if (forwardCmp == null) {
/* 4409 */           forwardCmp = Ordering.natural();
/*      */         }
/* 4411 */         result = this.comparator = reverse(forwardCmp);
/*      */       } 
/* 4413 */       return result;
/*      */     } @LazyInit
/*      */     @CheckForNull
/*      */     private transient NavigableSet<K> navigableKeySet;
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 4418 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K firstKey() {
/* 4424 */       return forward().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K lastKey() {
/* 4430 */       return forward().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lowerEntry(@ParametricNullness K key) {
/* 4436 */       return forward().higherEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K lowerKey(@ParametricNullness K key) {
/* 4442 */       return forward().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> floorEntry(@ParametricNullness K key) {
/* 4448 */       return forward().ceilingEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K floorKey(@ParametricNullness K key) {
/* 4454 */       return forward().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> ceilingEntry(@ParametricNullness K key) {
/* 4460 */       return forward().floorEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K ceilingKey(@ParametricNullness K key) {
/* 4466 */       return forward().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> higherEntry(@ParametricNullness K key) {
/* 4472 */       return forward().lowerEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public K higherKey(@ParametricNullness K key) {
/* 4478 */       return forward().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> firstEntry() {
/* 4484 */       return forward().lastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> lastEntry() {
/* 4490 */       return forward().firstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 4496 */       return forward().pollLastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 4502 */       return forward().pollFirstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 4507 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 4514 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 4515 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/* 4525 */           return Maps.DescendingMap.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/* 4530 */           return Maps.DescendingMap.this.entryIterator();
/*      */         }
/*      */       };
/* 4533 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 4538 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 4545 */       NavigableSet<K> result = this.navigableKeySet;
/* 4546 */       return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet<>(this)) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 4551 */       return forward().navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 4560 */       return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 4565 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 4570 */       return forward().tailMap(toKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/* 4575 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 4580 */       return forward().headMap(fromKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/* 4585 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 4590 */       return new Maps.Values<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4595 */       return standardToString();
/*      */     }
/*      */     abstract NavigableMap<K, V> forward();
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator(); }
/*      */   
/*      */   static <E> ImmutableMap<E, Integer> indexMap(Collection<E> list) {
/* 4601 */     ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<>(list.size());
/* 4602 */     int i = 0;
/* 4603 */     for (E e : list) {
/* 4604 */       builder.put(e, Integer.valueOf(i++));
/*      */     }
/* 4606 */     return builder.buildOrThrow();
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
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(NavigableMap<K, V> map, Range<K> range) {
/* 4628 */     if (map.comparator() != null && map
/* 4629 */       .comparator() != Ordering.natural() && range
/* 4630 */       .hasLowerBound() && range
/* 4631 */       .hasUpperBound()) {
/* 4632 */       Preconditions.checkArgument(
/* 4633 */           (map.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "map is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 4636 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 4637 */       return map.subMap(range
/* 4638 */           .lowerEndpoint(), 
/* 4639 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 4640 */           .upperEndpoint(), 
/* 4641 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 4642 */     if (range.hasLowerBound())
/* 4643 */       return map.tailMap(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 4644 */     if (range.hasUpperBound()) {
/* 4645 */       return map.headMap(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 4647 */     return (NavigableMap<K, V>)Preconditions.checkNotNull(map);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface EntryTransformer<K, V1, V2> {
/*      */     @ParametricNullness
/*      */     V2 transformEntry(@ParametricNullness K param1K, @ParametricNullness V1 param1V1);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Maps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */