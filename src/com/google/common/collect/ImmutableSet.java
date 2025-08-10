/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(serializable = true, emulated = true)
/*      */ public abstract class ImmutableSet<E>
/*      */   extends ImmutableCollection<E>
/*      */   implements Set<E>
/*      */ {
/*      */   static final int SPLITERATOR_CHARACTERISTICS = 1297;
/*      */   static final int MAX_TABLE_SIZE = 1073741824;
/*      */   private static final double DESIRED_LOAD_FACTOR = 0.7D;
/*      */   private static final int CUTOFF = 751619276;
/*      */   private static final long serialVersionUID = -889275714L;
/*      */   
/*      */   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*   72 */     return CollectCollectors.toImmutableSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of() {
/*   83 */     return RegularImmutableSet.EMPTY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of(E element) {
/*   92 */     return new SingletonImmutableSet<>(element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of(E e1, E e2) {
/*  101 */     return construct(2, 2, new Object[] { e1, e2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
/*  110 */     return construct(3, 3, new Object[] { e1, e2, e3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
/*  119 */     return construct(4, 4, new Object[] { e1, e2, e3, e4 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/*  128 */     return construct(5, 5, new Object[] { e1, e2, e3, e4, e5 });
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
/*      */   @SafeVarargs
/*      */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/*  142 */     Preconditions.checkArgument((others.length <= 2147483641), "the total number of elements must fit in an int");
/*      */     
/*  144 */     int paramCount = 6;
/*  145 */     Object[] elements = new Object[6 + others.length];
/*  146 */     elements[0] = e1;
/*  147 */     elements[1] = e2;
/*  148 */     elements[2] = e3;
/*  149 */     elements[3] = e4;
/*  150 */     elements[4] = e5;
/*  151 */     elements[5] = e6;
/*  152 */     System.arraycopy(others, 0, elements, 6, others.length);
/*  153 */     return construct(elements.length, elements.length, elements);
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
/*      */   private static <E> ImmutableSet<E> constructUnknownDuplication(int n, Object... elements) {
/*  173 */     return construct(n, 
/*      */         
/*  175 */         Math.max(4, 
/*      */           
/*  177 */           IntMath.sqrt(n, RoundingMode.CEILING)), elements);
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
/*      */   private static <E> ImmutableSet<E> construct(int n, int expectedSize, Object... elements) {
/*      */     E elem;
/*  196 */     switch (n) {
/*      */       case 0:
/*  198 */         return of();
/*      */       
/*      */       case 1:
/*  201 */         elem = (E)elements[0];
/*  202 */         return of(elem);
/*      */     } 
/*  204 */     SetBuilderImpl<E> builder = new RegularSetBuilderImpl<>(expectedSize);
/*  205 */     for (int i = 0; i < n; i++) {
/*      */       
/*  207 */       E e = (E)Preconditions.checkNotNull(elements[i]);
/*  208 */       builder = builder.add(e);
/*      */     } 
/*  210 */     return builder.review().build();
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
/*      */   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
/*  232 */     if (elements instanceof ImmutableSet && !(elements instanceof java.util.SortedSet)) {
/*      */       
/*  234 */       ImmutableSet<E> set = (ImmutableSet)elements;
/*  235 */       if (!set.isPartialView()) {
/*  236 */         return set;
/*      */       }
/*  238 */     } else if (elements instanceof EnumSet) {
/*  239 */       return copyOfEnumSet((EnumSet)elements);
/*      */     } 
/*  241 */     Object[] array = elements.toArray();
/*  242 */     if (elements instanceof Set)
/*      */     {
/*  244 */       return construct(array.length, array.length, array);
/*      */     }
/*  246 */     return constructUnknownDuplication(array.length, array);
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
/*      */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
/*  263 */     return (elements instanceof Collection) ? 
/*  264 */       copyOf((Collection<? extends E>)elements) : 
/*  265 */       copyOf(elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
/*  276 */     if (!elements.hasNext()) {
/*  277 */       return of();
/*      */     }
/*  279 */     E first = elements.next();
/*  280 */     if (!elements.hasNext()) {
/*  281 */       return of(first);
/*      */     }
/*  283 */     return (new Builder<>()).add(first).addAll(elements).build();
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
/*      */   public static <E> ImmutableSet<E> copyOf(E[] elements) {
/*  295 */     switch (elements.length) {
/*      */       case 0:
/*  297 */         return of();
/*      */       case 1:
/*  299 */         return of(elements[0]);
/*      */     } 
/*  301 */     return constructUnknownDuplication(elements.length, (Object[])elements.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ImmutableSet copyOfEnumSet(EnumSet<Enum> enumSet) {
/*  307 */     return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isHashCodeFast() {
/*  314 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(@CheckForNull Object object) {
/*  319 */     if (object == this) {
/*  320 */       return true;
/*      */     }
/*  322 */     if (object instanceof ImmutableSet && 
/*  323 */       isHashCodeFast() && ((ImmutableSet)object)
/*  324 */       .isHashCodeFast() && 
/*  325 */       hashCode() != object.hashCode()) {
/*  326 */       return false;
/*      */     }
/*  328 */     return Sets.equalsImpl(this, object);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  333 */     return Sets.hashCodeImpl(this);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class CachingAsList<E>
/*      */     extends ImmutableSet<E>
/*      */   {
/*      */     @LazyInit
/*      */     @CheckForNull
/*      */     @RetainedWith
/*      */     private transient ImmutableList<E> asList;
/*      */     
/*      */     public ImmutableList<E> asList() {
/*  347 */       ImmutableList<E> result = this.asList;
/*  348 */       if (result == null) {
/*  349 */         return this.asList = createAsList();
/*      */       }
/*  351 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<E> createAsList() {
/*  356 */       return new RegularImmutableAsList<>(this, toArray());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     Object writeReplace() {
/*  365 */       return super.writeReplace();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class Indexed<E>
/*      */     extends CachingAsList<E>
/*      */   {
/*      */     public UnmodifiableIterator<E> iterator() {
/*  374 */       return asList().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<E> spliterator() {
/*  379 */       return CollectSpliterators.indexed(size(), 1297, this::get);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> consumer) {
/*  384 */       Preconditions.checkNotNull(consumer);
/*  385 */       int n = size();
/*  386 */       for (int i = 0; i < n; i++) {
/*  387 */         consumer.accept(get(i));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int copyIntoArray(Object[] dst, int offset) {
/*  393 */       return asList().copyIntoArray(dst, offset);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<E> createAsList() {
/*  398 */       return new ImmutableAsList<E>()
/*      */         {
/*      */           public E get(int index) {
/*  401 */             return ImmutableSet.Indexed.this.get(index);
/*      */           }
/*      */ 
/*      */           
/*      */           ImmutableSet.Indexed<E> delegateCollection() {
/*  406 */             return ImmutableSet.Indexed.this;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           @J2ktIncompatible
/*      */           @GwtIncompatible
/*      */           Object writeReplace() {
/*  415 */             return super.writeReplace();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     Object writeReplace() {
/*  426 */       return super.writeReplace();
/*      */     }
/*      */ 
/*      */     
/*      */     abstract E get(int param1Int);
/*      */   }
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private static class SerializedForm
/*      */     implements Serializable
/*      */   {
/*      */     final Object[] elements;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SerializedForm(Object[] elements) {
/*  442 */       this.elements = elements;
/*      */     }
/*      */     
/*      */     Object readResolve() {
/*  446 */       return ImmutableSet.copyOf(this.elements);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   Object writeReplace() {
/*  455 */     return new SerializedForm(toArray());
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/*  460 */     throw new InvalidObjectException("Use SerializedForm");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Builder<E> builder() {
/*  468 */     return new Builder<>();
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
/*      */   public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
/*  484 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  485 */     return new Builder<>(expectedSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Builder<E>
/*      */     extends ImmutableCollection.Builder<E>
/*      */   {
/*      */     @CheckForNull
/*      */     private ImmutableSet.SetBuilderImpl<E> impl;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean forceCopy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder() {
/*  516 */       this(0);
/*      */     }
/*      */     
/*      */     Builder(int capacity) {
/*  520 */       if (capacity > 0) {
/*  521 */         this.impl = new ImmutableSet.RegularSetBuilderImpl<>(capacity);
/*      */       } else {
/*  523 */         this.impl = ImmutableSet.EmptySetBuilderImpl.instance();
/*      */       } 
/*      */     }
/*      */     
/*      */     Builder(boolean subclass) {
/*  528 */       this.impl = null;
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     void forceJdk() {
/*  533 */       Objects.requireNonNull(this.impl);
/*  534 */       this.impl = new ImmutableSet.JdkBackedSetBuilderImpl<>(this.impl);
/*      */     }
/*      */     
/*      */     final void copyIfNecessary() {
/*  538 */       if (this.forceCopy) {
/*  539 */         copy();
/*  540 */         this.forceCopy = false;
/*      */       } 
/*      */     }
/*      */     
/*      */     void copy() {
/*  545 */       Objects.requireNonNull(this.impl);
/*  546 */       this.impl = this.impl.copy();
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> add(E element) {
/*  552 */       Objects.requireNonNull(this.impl);
/*  553 */       Preconditions.checkNotNull(element);
/*  554 */       copyIfNecessary();
/*  555 */       this.impl = this.impl.add(element);
/*  556 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> add(E... elements) {
/*  562 */       super.add(elements);
/*  563 */       return this;
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
/*      */     public Builder<E> addAll(Iterable<? extends E> elements) {
/*  577 */       super.addAll(elements);
/*  578 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> addAll(Iterator<? extends E> elements) {
/*  584 */       super.addAll(elements);
/*  585 */       return this;
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     Builder<E> combine(Builder<E> other) {
/*  590 */       Objects.requireNonNull(this.impl);
/*  591 */       Objects.requireNonNull(other.impl);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  600 */       copyIfNecessary();
/*  601 */       this.impl = this.impl.combine(other.impl);
/*  602 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> build() {
/*  607 */       Objects.requireNonNull(this.impl);
/*  608 */       this.forceCopy = true;
/*  609 */       this.impl = this.impl.review();
/*  610 */       return this.impl.build();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class SetBuilderImpl<E>
/*      */   {
/*      */     E[] dedupedElements;
/*      */     
/*      */     int distinct;
/*      */ 
/*      */     
/*      */     SetBuilderImpl(int expectedCapacity) {
/*  623 */       this.dedupedElements = (E[])new Object[expectedCapacity];
/*  624 */       this.distinct = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     SetBuilderImpl(SetBuilderImpl<E> toCopy) {
/*  629 */       this.dedupedElements = Arrays.copyOf(toCopy.dedupedElements, toCopy.dedupedElements.length);
/*  630 */       this.distinct = toCopy.distinct;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void ensureCapacity(int minCapacity) {
/*  638 */       if (minCapacity > this.dedupedElements.length) {
/*      */         
/*  640 */         int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.dedupedElements.length, minCapacity);
/*  641 */         this.dedupedElements = Arrays.copyOf(this.dedupedElements, newCapacity);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     final void addDedupedElement(E e) {
/*  647 */       ensureCapacity(this.distinct + 1);
/*  648 */       this.dedupedElements[this.distinct++] = e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract SetBuilderImpl<E> add(E param1E);
/*      */ 
/*      */ 
/*      */     
/*      */     final SetBuilderImpl<E> combine(SetBuilderImpl<E> other) {
/*  659 */       SetBuilderImpl<E> result = this;
/*  660 */       for (int i = 0; i < other.distinct; i++)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  665 */         result = result.add(Objects.requireNonNull(other.dedupedElements[i]));
/*      */       }
/*  667 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract SetBuilderImpl<E> copy();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SetBuilderImpl<E> review() {
/*  681 */       return this;
/*      */     }
/*      */     
/*      */     abstract ImmutableSet<E> build();
/*      */   }
/*      */   
/*      */   private static final class EmptySetBuilderImpl<E> extends SetBuilderImpl<E> {
/*  688 */     private static final EmptySetBuilderImpl<Object> INSTANCE = new EmptySetBuilderImpl();
/*      */ 
/*      */     
/*      */     static <E> ImmutableSet.SetBuilderImpl<E> instance() {
/*  692 */       return INSTANCE;
/*      */     }
/*      */     
/*      */     private EmptySetBuilderImpl() {
/*  696 */       super(0);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/*  701 */       return (new ImmutableSet.RegularSetBuilderImpl<>(4)).add(e);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> copy() {
/*  706 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<E> build() {
/*  711 */       return ImmutableSet.of();
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
/*      */   static int chooseTableSize(int setSize) {
/*  731 */     setSize = Math.max(setSize, 2);
/*      */     
/*  733 */     if (setSize < 751619276) {
/*      */       
/*  735 */       int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/*  736 */       while (tableSize * 0.7D < setSize) {
/*  737 */         tableSize <<= 1;
/*      */       }
/*  739 */       return tableSize;
/*      */     } 
/*      */ 
/*      */     
/*  743 */     Preconditions.checkArgument((setSize < 1073741824), "collection too large");
/*  744 */     return 1073741824;
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract UnmodifiableIterator<E> iterator();
/*      */ 
/*      */   
/*      */   private static final class RegularSetBuilderImpl<E>
/*      */     extends SetBuilderImpl<E>
/*      */   {
/*      */     @CheckForNull
/*      */     private Object[] hashTable;
/*      */     
/*      */     private int maxRunBeforeFallback;
/*      */     private int expandTableThreshold;
/*      */     private int hashCode;
/*      */     static final int MAX_RUN_MULTIPLIER = 13;
/*      */     
/*      */     RegularSetBuilderImpl(int expectedCapacity) {
/*  763 */       super(expectedCapacity);
/*  764 */       this.hashTable = null;
/*  765 */       this.maxRunBeforeFallback = 0;
/*  766 */       this.expandTableThreshold = 0;
/*      */     }
/*      */     
/*      */     RegularSetBuilderImpl(RegularSetBuilderImpl<E> toCopy) {
/*  770 */       super(toCopy);
/*  771 */       this.hashTable = (toCopy.hashTable == null) ? null : (Object[])toCopy.hashTable.clone();
/*  772 */       this.maxRunBeforeFallback = toCopy.maxRunBeforeFallback;
/*  773 */       this.expandTableThreshold = toCopy.expandTableThreshold;
/*  774 */       this.hashCode = toCopy.hashCode;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/*  779 */       Preconditions.checkNotNull(e);
/*  780 */       if (this.hashTable == null) {
/*  781 */         if (this.distinct == 0) {
/*  782 */           addDedupedElement(e);
/*  783 */           return this;
/*      */         } 
/*  785 */         ensureTableCapacity(this.dedupedElements.length);
/*  786 */         E elem = this.dedupedElements[0];
/*  787 */         this.distinct--;
/*  788 */         return insertInHashTable(elem).add(e);
/*      */       } 
/*      */       
/*  791 */       return insertInHashTable(e);
/*      */     }
/*      */     
/*      */     private ImmutableSet.SetBuilderImpl<E> insertInHashTable(E e) {
/*  795 */       Objects.requireNonNull(this.hashTable);
/*  796 */       int eHash = e.hashCode();
/*  797 */       int i0 = Hashing.smear(eHash);
/*  798 */       int mask = this.hashTable.length - 1;
/*  799 */       for (int i = i0; i - i0 < this.maxRunBeforeFallback; i++) {
/*  800 */         int index = i & mask;
/*  801 */         Object tableEntry = this.hashTable[index];
/*  802 */         if (tableEntry == null) {
/*  803 */           addDedupedElement(e);
/*  804 */           this.hashTable[index] = e;
/*  805 */           this.hashCode += eHash;
/*  806 */           ensureTableCapacity(this.distinct);
/*  807 */           return this;
/*  808 */         }  if (tableEntry.equals(e)) {
/*  809 */           return this;
/*      */         }
/*      */       } 
/*      */       
/*  813 */       return (new ImmutableSet.JdkBackedSetBuilderImpl<>(this)).add(e);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> copy() {
/*  818 */       return new RegularSetBuilderImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> review() {
/*  823 */       if (this.hashTable == null) {
/*  824 */         return this;
/*      */       }
/*  826 */       int targetTableSize = ImmutableSet.chooseTableSize(this.distinct);
/*  827 */       if (targetTableSize * 2 < this.hashTable.length) {
/*  828 */         this.hashTable = rebuildHashTable(targetTableSize, (Object[])this.dedupedElements, this.distinct);
/*  829 */         this.maxRunBeforeFallback = maxRunBeforeFallback(targetTableSize);
/*  830 */         this.expandTableThreshold = (int)(0.7D * targetTableSize);
/*      */       } 
/*  832 */       return hashFloodingDetected(this.hashTable) ? new ImmutableSet.JdkBackedSetBuilderImpl<>(this) : this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<E> build() {
/*  837 */       switch (this.distinct) {
/*      */         case 0:
/*  839 */           return ImmutableSet.of();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/*  845 */           return ImmutableSet.of(Objects.requireNonNull(this.dedupedElements[0]));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  855 */       Object[] elements = (this.distinct == this.dedupedElements.length) ? (Object[])this.dedupedElements : Arrays.<Object>copyOf((Object[])this.dedupedElements, this.distinct);
/*  856 */       return new RegularImmutableSet<>(elements, this.hashCode, 
/*  857 */           Objects.<Object[]>requireNonNull(this.hashTable), this.hashTable.length - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     static Object[] rebuildHashTable(int newTableSize, Object[] elements, int n) {
/*  863 */       Object[] hashTable = new Object[newTableSize];
/*  864 */       int mask = hashTable.length - 1;
/*  865 */       for (int i = 0; i < n; ) {
/*      */         
/*  867 */         Object e = Objects.requireNonNull(elements[i]);
/*  868 */         int j0 = Hashing.smear(e.hashCode());
/*  869 */         int j = j0; for (;; i++) {
/*  870 */           int index = j & mask;
/*  871 */           if (hashTable[index] == null) {
/*  872 */             hashTable[index] = e;
/*      */           } else {
/*      */             j++; continue;
/*      */           } 
/*      */         } 
/*  877 */       }  return hashTable;
/*      */     }
/*      */     
/*      */     void ensureTableCapacity(int minCapacity) {
/*      */       int newTableSize;
/*  882 */       if (this.hashTable == null) {
/*  883 */         newTableSize = ImmutableSet.chooseTableSize(minCapacity);
/*  884 */         this.hashTable = new Object[newTableSize];
/*  885 */       } else if (minCapacity > this.expandTableThreshold && this.hashTable.length < 1073741824) {
/*  886 */         newTableSize = this.hashTable.length * 2;
/*  887 */         this.hashTable = rebuildHashTable(newTableSize, (Object[])this.dedupedElements, this.distinct);
/*      */       } else {
/*      */         return;
/*      */       } 
/*  891 */       this.maxRunBeforeFallback = maxRunBeforeFallback(newTableSize);
/*  892 */       this.expandTableThreshold = (int)(0.7D * newTableSize);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static boolean hashFloodingDetected(Object[] hashTable) {
/*  924 */       int maxRunBeforeFallback = maxRunBeforeFallback(hashTable.length);
/*  925 */       int mask = hashTable.length - 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  931 */       int knownRunStart = 0;
/*  932 */       int knownRunEnd = 0;
/*      */ 
/*      */       
/*  935 */       label22: while (knownRunStart < hashTable.length) {
/*  936 */         if (knownRunStart == knownRunEnd && hashTable[knownRunStart] == null) {
/*  937 */           if (hashTable[knownRunStart + maxRunBeforeFallback - 1 & mask] == null) {
/*      */ 
/*      */ 
/*      */             
/*  941 */             knownRunStart += maxRunBeforeFallback;
/*      */           } else {
/*  943 */             knownRunStart++;
/*      */           } 
/*      */           
/*  946 */           knownRunEnd = knownRunStart; continue;
/*      */         } 
/*  948 */         for (int j = knownRunStart + maxRunBeforeFallback - 1; j >= knownRunEnd; j--) {
/*  949 */           if (hashTable[j & mask] == null) {
/*  950 */             knownRunEnd = knownRunStart + maxRunBeforeFallback;
/*  951 */             knownRunStart = j + 1;
/*      */             continue label22;
/*      */           } 
/*      */         } 
/*  955 */         return true;
/*      */       } 
/*      */       
/*  958 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static int maxRunBeforeFallback(int tableSize) {
/*  967 */       return 13 * IntMath.log2(tableSize, RoundingMode.UNNECESSARY);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class JdkBackedSetBuilderImpl<E>
/*      */     extends SetBuilderImpl<E>
/*      */   {
/*      */     private final Set<Object> delegate;
/*      */     
/*      */     JdkBackedSetBuilderImpl(ImmutableSet.SetBuilderImpl<E> toCopy) {
/*  978 */       super(toCopy);
/*  979 */       this.delegate = Sets.newHashSetWithExpectedSize(this.distinct);
/*  980 */       for (int i = 0; i < this.distinct; i++)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  985 */         this.delegate.add(Objects.requireNonNull(this.dedupedElements[i]));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> add(E e) {
/*  991 */       Preconditions.checkNotNull(e);
/*  992 */       if (this.delegate.add(e)) {
/*  993 */         addDedupedElement(e);
/*      */       }
/*  995 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet.SetBuilderImpl<E> copy() {
/* 1000 */       return new JdkBackedSetBuilderImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<E> build() {
/* 1005 */       switch (this.distinct) {
/*      */         case 0:
/* 1007 */           return ImmutableSet.of();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 1:
/* 1013 */           return ImmutableSet.of(Objects.requireNonNull(this.dedupedElements[0]));
/*      */       } 
/* 1015 */       return new JdkBackedImmutableSet<>(this.delegate, 
/* 1016 */           ImmutableList.asImmutableList((Object[])this.dedupedElements, this.distinct));
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */