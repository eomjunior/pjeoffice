/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotCall;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public boolean removeAll(Collection<?> c) {
/*   84 */       return Sets.removeAllImpl(this, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*   89 */       return super.retainAll((Collection)Preconditions.checkNotNull(c));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*  108 */     return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  124 */     if (elements instanceof ImmutableEnumSet)
/*  125 */       return (ImmutableEnumSet)elements; 
/*  126 */     if (elements instanceof Collection) {
/*  127 */       Collection<E> collection = (Collection<E>)elements;
/*  128 */       if (collection.isEmpty()) {
/*  129 */         return ImmutableSet.of();
/*      */       }
/*  131 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  134 */     Iterator<E> itr = elements.iterator();
/*  135 */     if (itr.hasNext()) {
/*  136 */       EnumSet<E> enumSet = EnumSet.of(itr.next());
/*  137 */       Iterators.addAll(enumSet, itr);
/*  138 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  140 */     return ImmutableSet.of();
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
/*      */   public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  153 */     return CollectCollectors.toImmutableEnumSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  163 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  164 */     Iterables.addAll(set, iterable);
/*  165 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet() {
/*  183 */     return new HashSet<>();
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
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  200 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  201 */     Collections.addAll(set, elements);
/*  202 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/*  224 */     return (elements instanceof Collection) ? 
/*  225 */       new HashSet<>((Collection<? extends E>)elements) : 
/*  226 */       newHashSet(elements.iterator());
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
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  242 */     HashSet<E> set = newHashSet();
/*  243 */     Iterators.addAll(set, elements);
/*  244 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/*  261 */     return new HashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E> Set<E> newConcurrentHashSet() {
/*  275 */     return Platform.newConcurrentHashSet();
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
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  292 */     Set<E> set = newConcurrentHashSet();
/*  293 */     Iterables.addAll(set, elements);
/*  294 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/*  311 */     return new LinkedHashSet<>();
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  331 */     if (elements instanceof Collection) {
/*  332 */       return new LinkedHashSet<>((Collection<? extends E>)elements);
/*      */     }
/*  334 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  335 */     Iterables.addAll(set, elements);
/*  336 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  353 */     return new LinkedHashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/*  371 */     return new TreeSet<>();
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  396 */     TreeSet<E> set = newTreeSet();
/*  397 */     Iterables.addAll(set, elements);
/*  398 */     return set;
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
/*      */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/*  419 */     return new TreeSet<>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <E> Set<E> newIdentityHashSet() {
/*  432 */     return Collections.newSetFromMap(Maps.newIdentityHashMap());
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
/*  447 */     return new CopyOnWriteArraySet<>();
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  466 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? (Collection<? extends E>)elements : Lists.<E>newArrayList(elements);
/*  467 */     return new CopyOnWriteArraySet<>(elementsCollection);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  486 */     if (collection instanceof EnumSet) {
/*  487 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*      */     }
/*  489 */     Preconditions.checkArgument(
/*  490 */         !collection.isEmpty(), "collection is empty; use the other version of this method");
/*  491 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/*  492 */     return makeComplementByHand(collection, type);
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
/*      */   @GwtIncompatible
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  508 */     Preconditions.checkNotNull(collection);
/*  509 */     return (collection instanceof EnumSet) ? 
/*  510 */       EnumSet.<E>complementOf((EnumSet<E>)collection) : 
/*  511 */       makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  517 */     EnumSet<E> result = EnumSet.allOf(type);
/*  518 */     result.removeAll(collection);
/*  519 */     return result;
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
/*      */   @Deprecated
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/*  554 */     return Collections.newSetFromMap(map);
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
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> immutableCopy() {
/*  579 */       return ImmutableSet.copyOf(this);
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
/*      */     @CanIgnoreReturnValue
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  593 */       set.addAll(this);
/*  594 */       return set;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean add(@ParametricNullness E e) {
/*  608 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean remove(@CheckForNull Object object) {
/*  622 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean addAll(Collection<? extends E> newElements) {
/*  636 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean removeAll(Collection<?> oldElements) {
/*  650 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean removeIf(Predicate<? super E> filter) {
/*  664 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final boolean retainAll(Collection<?> elementsToKeep) {
/*  678 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final void clear() {
/*  691 */       throw new UnsupportedOperationException();
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
/*      */     public abstract UnmodifiableIterator<E> iterator();
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  715 */     Preconditions.checkNotNull(set1, "set1");
/*  716 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  718 */     return new SetView<E>()
/*      */       {
/*      */         public int size() {
/*  721 */           int size = set1.size();
/*  722 */           for (E e : set2) {
/*  723 */             if (!set1.contains(e)) {
/*  724 */               size++;
/*      */             }
/*      */           } 
/*  727 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  732 */           return (set1.isEmpty() && set2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public UnmodifiableIterator<E> iterator() {
/*  737 */           return new AbstractIterator() {
/*  738 */               final Iterator<? extends E> itr1 = set1.iterator();
/*  739 */               final Iterator<? extends E> itr2 = set2.iterator();
/*      */ 
/*      */               
/*      */               @CheckForNull
/*      */               protected E computeNext() {
/*  744 */                 if (this.itr1.hasNext()) {
/*  745 */                   return this.itr1.next();
/*      */                 }
/*  747 */                 while (this.itr2.hasNext()) {
/*  748 */                   E e = this.itr2.next();
/*  749 */                   if (!set1.contains(e)) {
/*  750 */                     return e;
/*      */                   }
/*      */                 } 
/*  753 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  760 */           return Stream.concat(set1.stream(), set2.stream().filter(e -> !set1.contains(e)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  765 */           return stream().parallel();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(@CheckForNull Object object) {
/*  770 */           return (set1.contains(object) || set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  775 */           set.addAll(set1);
/*  776 */           set.addAll(set2);
/*  777 */           return set;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public ImmutableSet<E> immutableCopy() {
/*  786 */           ImmutableSet.Builder<E> builder = (new ImmutableSet.Builder<>()).addAll(set1).addAll(set2);
/*  787 */           return builder.build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  821 */     Preconditions.checkNotNull(set1, "set1");
/*  822 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  824 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  827 */           return new AbstractIterator() {
/*  828 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               @CheckForNull
/*      */               protected E computeNext() {
/*  833 */                 while (this.itr.hasNext()) {
/*  834 */                   E e = this.itr.next();
/*  835 */                   if (set2.contains(e)) {
/*  836 */                     return e;
/*      */                   }
/*      */                 } 
/*  839 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  846 */           Objects.requireNonNull(set2); return set1.stream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  851 */           Objects.requireNonNull(set2); return set1.parallelStream().filter(set2::contains);
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  856 */           int size = 0;
/*  857 */           for (E e : set1) {
/*  858 */             if (set2.contains(e)) {
/*  859 */               size++;
/*      */             }
/*      */           } 
/*  862 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  867 */           return Collections.disjoint(set2, set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(@CheckForNull Object object) {
/*  872 */           return (set1.contains(object) && set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean containsAll(Collection<?> collection) {
/*  877 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  894 */     Preconditions.checkNotNull(set1, "set1");
/*  895 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  897 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  900 */           return new AbstractIterator() {
/*  901 */               final Iterator<E> itr = set1.iterator();
/*      */ 
/*      */               
/*      */               @CheckForNull
/*      */               protected E computeNext() {
/*  906 */                 while (this.itr.hasNext()) {
/*  907 */                   E e = this.itr.next();
/*  908 */                   if (!set2.contains(e)) {
/*  909 */                     return e;
/*      */                   }
/*      */                 } 
/*  912 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  919 */           return set1.stream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  924 */           return set1.parallelStream().filter(e -> !set2.contains(e));
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  929 */           int size = 0;
/*  930 */           for (E e : set1) {
/*  931 */             if (!set2.contains(e)) {
/*  932 */               size++;
/*      */             }
/*      */           } 
/*  935 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  940 */           return set2.containsAll(set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(@CheckForNull Object element) {
/*  945 */           return (set1.contains(element) && !set2.contains(element));
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
/*      */   public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  963 */     Preconditions.checkNotNull(set1, "set1");
/*  964 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  966 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  969 */           final Iterator<? extends E> itr1 = set1.iterator();
/*  970 */           final Iterator<? extends E> itr2 = set2.iterator();
/*  971 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               public E computeNext() {
/*  975 */                 while (itr1.hasNext()) {
/*  976 */                   E elem1 = itr1.next();
/*  977 */                   if (!set2.contains(elem1)) {
/*  978 */                     return elem1;
/*      */                   }
/*      */                 } 
/*  981 */                 while (itr2.hasNext()) {
/*  982 */                   E elem2 = itr2.next();
/*  983 */                   if (!set1.contains(elem2)) {
/*  984 */                     return elem2;
/*      */                   }
/*      */                 } 
/*  987 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  994 */           int size = 0;
/*  995 */           for (E e : set1) {
/*  996 */             if (!set2.contains(e)) {
/*  997 */               size++;
/*      */             }
/*      */           } 
/* 1000 */           for (E e : set2) {
/* 1001 */             if (!set1.contains(e)) {
/* 1002 */               size++;
/*      */             }
/*      */           } 
/* 1005 */           return size;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/* 1010 */           return set1.equals(set2);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(@CheckForNull Object element) {
/* 1015 */           return set1.contains(element) ^ set2.contains(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1049 */     if (unfiltered instanceof SortedSet) {
/* 1050 */       return filter((SortedSet<E>)unfiltered, predicate);
/*      */     }
/* 1052 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1055 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1056 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1057 */       return new FilteredSet<>((Set<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1060 */     return new FilteredSet<>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1089 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1092 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1093 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1094 */       return new FilteredSortedSet<>((SortedSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1097 */     return new FilteredSortedSet<>((SortedSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
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
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1128 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1131 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1132 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1133 */       return new FilteredNavigableSet<>((NavigableSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1136 */     return new FilteredNavigableSet<>((NavigableSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E>
/*      */     extends Collections2.FilteredCollection<E> implements Set<E> {
/*      */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1142 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1147 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1152 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E>
/*      */     implements SortedSet<E> {
/*      */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1160 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Comparator<? super E> comparator() {
/* 1166 */       return ((SortedSet<E>)this.unfiltered).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 1171 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered)
/* 1172 */           .subSet(fromElement, toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(@ParametricNullness E toElement) {
/* 1177 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).headSet(toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/* 1182 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).tailSet(fromElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E first() {
/* 1188 */       return Iterators.find(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E last() {
/* 1194 */       SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
/*      */       while (true) {
/* 1196 */         E element = sortedUnfiltered.last();
/* 1197 */         if (this.predicate.apply(element)) {
/* 1198 */           return element;
/*      */         }
/* 1200 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E> implements NavigableSet<E> {
/*      */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1209 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     NavigableSet<E> unfiltered() {
/* 1213 */       return (NavigableSet<E>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E lower(@ParametricNullness E e) {
/* 1219 */       return Iterators.find(unfiltered().headSet(e, false).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E floor(@ParametricNullness E e) {
/* 1225 */       return Iterators.find(unfiltered().headSet(e, true).descendingIterator(), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E ceiling(@ParametricNullness E e) {
/* 1231 */       return Iterables.find(unfiltered().tailSet(e, true), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E higher(@ParametricNullness E e) {
/* 1237 */       return Iterables.find(unfiltered().tailSet(e, false), this.predicate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollFirst() {
/* 1243 */       return Iterables.removeFirstMatching(unfiltered(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollLast() {
/* 1249 */       return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1254 */       return Sets.filter(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1259 */       return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E last() {
/* 1265 */       return Iterators.find(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 1274 */       return Sets.filter(
/* 1275 */           unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 1280 */       return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 1285 */       return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
/* 1343 */     return CartesianSet.create(sets);
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
/*      */   @SafeVarargs
/*      */   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
/* 1401 */     return cartesianProduct(Arrays.asList(sets));
/*      */   }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>> implements Set<List<E>> {
/*      */     private final transient ImmutableList<ImmutableSet<E>> axes;
/*      */     private final transient CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1410 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<>(sets.size());
/* 1411 */       for (Set<? extends E> set : sets) {
/* 1412 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1413 */         if (copy.isEmpty()) {
/* 1414 */           return ImmutableSet.of();
/*      */         }
/* 1416 */         axesBuilder.add(copy);
/*      */       } 
/* 1418 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1419 */       ImmutableList<List<E>> listAxes = (ImmutableList)new ImmutableList<List<List<E>>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1423 */             return axes.size();
/*      */           }
/*      */ 
/*      */           
/*      */           public List<E> get(int index) {
/* 1428 */             return ((ImmutableSet<E>)axes.get(index)).asList();
/*      */           }
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1433 */             return true;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           @J2ktIncompatible
/*      */           @GwtIncompatible
/*      */           Object writeReplace() {
/* 1442 */             return super.writeReplace();
/*      */           }
/*      */         };
/* 1445 */       return new CartesianSet<>(axes, new CartesianList<>(listAxes));
/*      */     }
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1449 */       this.axes = axes;
/* 1450 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<List<E>> delegate() {
/* 1455 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object object) {
/* 1460 */       if (!(object instanceof List)) {
/* 1461 */         return false;
/*      */       }
/* 1463 */       List<?> list = (List)object;
/* 1464 */       if (list.size() != this.axes.size()) {
/* 1465 */         return false;
/*      */       }
/* 1467 */       int i = 0;
/* 1468 */       for (Object o : list) {
/* 1469 */         if (!((ImmutableSet)this.axes.get(i)).contains(o)) {
/* 1470 */           return false;
/*      */         }
/* 1472 */         i++;
/*      */       } 
/* 1474 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 1481 */       if (object instanceof CartesianSet) {
/* 1482 */         CartesianSet<?> that = (CartesianSet)object;
/* 1483 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1485 */       if (object instanceof Set) {
/* 1486 */         Set<?> that = (Set)object;
/* 1487 */         return (size() == that.size() && containsAll(that));
/*      */       } 
/* 1489 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1498 */       int adjust = size() - 1;
/* 1499 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1500 */         adjust *= 31;
/* 1501 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1504 */       int hash = 1;
/* 1505 */       for (UnmodifiableIterator<ImmutableSet<E>> unmodifiableIterator = this.axes.iterator(); unmodifiableIterator.hasNext(); ) { Set<E> axis = unmodifiableIterator.next();
/* 1506 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1508 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF; }
/*      */       
/* 1510 */       hash += adjust;
/* 1511 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/*      */   public static <E> Set<Set<E>> powerSet(Set<E> set) {
/* 1541 */     return new PowerSet<>(set);
/*      */   }
/*      */   
/*      */   private static final class SubSet<E> extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1549 */       this.inputSet = inputSet;
/* 1550 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1555 */       return new UnmodifiableIterator<E>() {
/* 1556 */           final ImmutableList<E> elements = Sets.SubSet.this.inputSet.keySet().asList();
/* 1557 */           int remainingSetBits = Sets.SubSet.this.mask;
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/* 1561 */             return (this.remainingSetBits != 0);
/*      */           }
/*      */ 
/*      */           
/*      */           public E next() {
/* 1566 */             int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
/* 1567 */             if (index == 32) {
/* 1568 */               throw new NoSuchElementException();
/*      */             }
/* 1570 */             this.remainingSetBits &= 1 << index ^ 0xFFFFFFFF;
/* 1571 */             return this.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1578 */       return Integer.bitCount(this.mask);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 1583 */       Integer index = this.inputSet.get(o);
/* 1584 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1592 */       Preconditions.checkArgument(
/* 1593 */           (input.size() <= 30), "Too many elements to create power set: %s > 30", input.size());
/* 1594 */       this.inputSet = Maps.indexMap(input);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1599 */       return 1 << this.inputSet.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1604 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1609 */       return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(size())
/*      */         {
/*      */           protected Set<E> get(int setBits) {
/* 1612 */             return new Sets.SubSet<>(Sets.PowerSet.this.inputSet, setBits);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object obj) {
/* 1619 */       if (obj instanceof Set) {
/* 1620 */         Set<?> set = (Set)obj;
/* 1621 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1623 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object obj) {
/* 1628 */       if (obj instanceof PowerSet) {
/* 1629 */         PowerSet<?> that = (PowerSet)obj;
/* 1630 */         return this.inputSet.keySet().equals(that.inputSet.keySet());
/*      */       } 
/* 1632 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1642 */       return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1647 */       return "powerSet(" + this.inputSet + ")";
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
/*      */   public static <E> Set<Set<E>> combinations(Set<E> set, final int size) {
/* 1676 */     final ImmutableMap<E, Integer> index = Maps.indexMap(set);
/* 1677 */     CollectPreconditions.checkNonnegative(size, "size");
/* 1678 */     Preconditions.checkArgument((size <= index.size()), "size (%s) must be <= set.size() (%s)", size, index.size());
/* 1679 */     if (size == 0)
/* 1680 */       return ImmutableSet.of(ImmutableSet.of()); 
/* 1681 */     if (size == index.size()) {
/* 1682 */       return ImmutableSet.of(index.keySet());
/*      */     }
/* 1684 */     return (Set)new AbstractSet<Set<Set<E>>>()
/*      */       {
/*      */         public boolean contains(@CheckForNull Object o) {
/* 1687 */           if (o instanceof Set) {
/* 1688 */             Set<?> s = (Set)o;
/* 1689 */             return (s.size() == size && index.keySet().containsAll(s));
/*      */           } 
/* 1691 */           return false;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Set<E>> iterator() {
/* 1696 */           return new AbstractIterator() {
/* 1697 */               final BitSet bits = new BitSet(index.size());
/*      */ 
/*      */               
/*      */               @CheckForNull
/*      */               protected Set<E> computeNext() {
/* 1702 */                 if (this.bits.isEmpty()) {
/* 1703 */                   this.bits.set(0, size);
/*      */                 } else {
/* 1705 */                   int firstSetBit = this.bits.nextSetBit(0);
/* 1706 */                   int bitToFlip = this.bits.nextClearBit(firstSetBit);
/*      */                   
/* 1708 */                   if (bitToFlip == index.size()) {
/* 1709 */                     return endOfData();
/*      */                   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1725 */                   this.bits.set(0, bitToFlip - firstSetBit - 1);
/* 1726 */                   this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
/* 1727 */                   this.bits.set(bitToFlip);
/*      */                 } 
/* 1729 */                 final BitSet copy = (BitSet)this.bits.clone();
/* 1730 */                 return new AbstractSet<E>()
/*      */                   {
/*      */                     public boolean contains(@CheckForNull Object o) {
/* 1733 */                       Integer i = (Integer)index.get(o);
/* 1734 */                       return (i != null && copy.get(i.intValue()));
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public Iterator<E> iterator() {
/* 1739 */                       return new AbstractIterator() {
/* 1740 */                           int i = -1;
/*      */ 
/*      */                           
/*      */                           @CheckForNull
/*      */                           protected E computeNext() {
/* 1745 */                             this.i = copy.nextSetBit(this.i + 1);
/* 1746 */                             if (this.i == -1) {
/* 1747 */                               return endOfData();
/*      */                             }
/* 1749 */                             return index.keySet().asList().get(this.i);
/*      */                           }
/*      */                         };
/*      */                     }
/*      */ 
/*      */                     
/*      */                     public int size() {
/* 1756 */                       return size;
/*      */                     }
/*      */                   };
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/* 1765 */           return IntMath.binomial(index.size(), size);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1770 */           return "Sets.combinations(" + index.keySet() + ", " + size + ")";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1777 */     int hashCode = 0;
/* 1778 */     for (Object o : s) {
/* 1779 */       hashCode += (o != null) ? o.hashCode() : 0;
/*      */       
/* 1781 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1784 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, @CheckForNull Object object) {
/* 1789 */     if (s == object) {
/* 1790 */       return true;
/*      */     }
/* 1792 */     if (object instanceof Set) {
/* 1793 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1796 */         return (s.size() == o.size() && s.containsAll(o));
/* 1797 */       } catch (NullPointerException|ClassCastException ignored) {
/* 1798 */         return false;
/*      */       } 
/*      */     } 
/* 1801 */     return false;
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
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1821 */     if (set instanceof ImmutableCollection || set instanceof UnmodifiableNavigableSet) {
/* 1822 */       return set;
/*      */     }
/* 1824 */     return new UnmodifiableNavigableSet<>(set);
/*      */   }
/*      */   
/*      */   static final class UnmodifiableNavigableSet<E>
/*      */     extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
/*      */     private final NavigableSet<E> delegate;
/*      */     private final SortedSet<E> unmodifiableDelegate;
/*      */     
/*      */     UnmodifiableNavigableSet(NavigableSet<E> delegate) {
/* 1833 */       this.delegate = (NavigableSet<E>)Preconditions.checkNotNull(delegate);
/* 1834 */       this.unmodifiableDelegate = Collections.unmodifiableSortedSet(delegate);
/*      */     } @LazyInit
/*      */     @CheckForNull
/*      */     private transient UnmodifiableNavigableSet<E> descendingSet; private static final long serialVersionUID = 0L;
/*      */     protected SortedSet<E> delegate() {
/* 1839 */       return this.unmodifiableDelegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/* 1846 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/* 1851 */       return this.delegate.stream();
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/* 1856 */       return this.delegate.parallelStream();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/* 1861 */       this.delegate.forEach(action);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E lower(@ParametricNullness E e) {
/* 1867 */       return this.delegate.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E floor(@ParametricNullness E e) {
/* 1873 */       return this.delegate.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E ceiling(@ParametricNullness E e) {
/* 1879 */       return this.delegate.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E higher(@ParametricNullness E e) {
/* 1885 */       return this.delegate.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollFirst() {
/* 1891 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollLast() {
/* 1897 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1904 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1905 */       if (result == null) {
/* 1906 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/* 1907 */         result.descendingSet = this;
/*      */       } 
/* 1909 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1914 */       return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 1923 */       return Sets.unmodifiableNavigableSet(this.delegate
/* 1924 */           .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 1929 */       return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 1934 */       return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
/* 1990 */     return Synchronized.navigableSet(navigableSet);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1995 */     boolean changed = false;
/* 1996 */     while (iterator.hasNext()) {
/* 1997 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1999 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 2003 */     Preconditions.checkNotNull(collection);
/* 2004 */     if (collection instanceof Multiset) {
/* 2005 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2014 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 2015 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 2017 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E> {
/*      */     private final NavigableSet<E> forward;
/*      */     
/*      */     DescendingSet(NavigableSet<E> forward) {
/* 2026 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableSet<E> delegate() {
/* 2031 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E lower(@ParametricNullness E e) {
/* 2037 */       return this.forward.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E floor(@ParametricNullness E e) {
/* 2043 */       return this.forward.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E ceiling(@ParametricNullness E e) {
/* 2049 */       return this.forward.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E higher(@ParametricNullness E e) {
/* 2055 */       return this.forward.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollFirst() {
/* 2061 */       return this.forward.pollLast();
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E pollLast() {
/* 2067 */       return this.forward.pollFirst();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 2072 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 2077 */       return this.forward.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 2086 */       return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 2091 */       return standardSubSet(fromElement, toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 2096 */       return this.forward.tailSet(toElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(@ParametricNullness E toElement) {
/* 2101 */       return standardHeadSet(toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 2106 */       return this.forward.headSet(fromElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/* 2111 */       return standardTailSet(fromElement);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 2117 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 2118 */       if (forwardComparator == null) {
/* 2119 */         return Ordering.<Comparable>natural().reverse();
/*      */       }
/* 2121 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 2127 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E first() {
/* 2133 */       return this.forward.last();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E last() {
/* 2139 */       return this.forward.first();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 2144 */       return this.forward.descendingIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2149 */       return standardToArray();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2155 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2160 */       return standardToString();
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
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range) {
/* 2183 */     if (set.comparator() != null && set
/* 2184 */       .comparator() != Ordering.natural() && range
/* 2185 */       .hasLowerBound() && range
/* 2186 */       .hasUpperBound()) {
/* 2187 */       Preconditions.checkArgument(
/* 2188 */           (set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "set is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 2191 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 2192 */       return set.subSet(range
/* 2193 */           .lowerEndpoint(), 
/* 2194 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 2195 */           .upperEndpoint(), 
/* 2196 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 2197 */     if (range.hasLowerBound())
/* 2198 */       return set.tailSet(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 2199 */     if (range.hasUpperBound()) {
/* 2200 */       return set.headSet(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 2202 */     return (NavigableSet<K>)Preconditions.checkNotNull(set);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Sets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */