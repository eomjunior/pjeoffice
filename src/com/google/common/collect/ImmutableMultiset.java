/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMultiset<E>
/*     */   extends ImmutableMultisetGwtSerializationDependencies<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient ImmutableList<E> asList;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
/*  73 */     return CollectCollectors.toImmutableMultiset((Function)Function.identity(), e -> 1);
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
/*     */   public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  91 */     return CollectCollectors.toImmutableMultiset(elementFunction, countFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of() {
/* 101 */     return (ImmutableMultiset)RegularImmutableMultiset.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E element) {
/* 111 */     return copyFromElements((E[])new Object[] { element });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2) {
/* 121 */     return copyFromElements((E[])new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
/* 132 */     return copyFromElements((E[])new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 143 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 154 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 165 */     return (new Builder<>()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
/* 176 */     return copyFromElements(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 186 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/* 188 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/* 189 */       if (!result.isPartialView()) {
/* 190 */         return result;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);
/*     */     
/* 199 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 209 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 210 */     Iterators.addAll(multiset, elements);
/* 211 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
/* 215 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 216 */     Collections.addAll(multiset, elements);
/* 217 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 222 */     if (entries.isEmpty()) {
/* 223 */       return of();
/*     */     }
/* 225 */     return RegularImmutableMultiset.create(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 233 */     final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 234 */     return new UnmodifiableIterator<E>(this) {
/*     */         int remaining;
/*     */         @CheckForNull
/*     */         E element;
/*     */         
/*     */         public boolean hasNext() {
/* 240 */           return (this.remaining > 0 || entryIterator.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/* 245 */           if (this.remaining <= 0) {
/* 246 */             Multiset.Entry<E> entry = entryIterator.next();
/* 247 */             this.element = entry.getElement();
/* 248 */             this.remaining = entry.getCount();
/*     */           } 
/* 250 */           this.remaining--;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 255 */           return Objects.requireNonNull(this.element);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> asList() {
/* 264 */     ImmutableList<E> result = this.asList;
/* 265 */     return (result == null) ? (this.asList = super.asList()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 270 */     return (count(object) > 0);
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
/*     */   public final int add(E element, int occurrences) {
/* 284 */     throw new UnsupportedOperationException();
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
/*     */   public final int remove(@CheckForNull Object element, int occurrences) {
/* 298 */     throw new UnsupportedOperationException();
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
/*     */   public final int setCount(E element, int count) {
/* 312 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean setCount(E element, int oldCount, int newCount) {
/* 326 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 332 */     for (UnmodifiableIterator<Multiset.Entry<E>> unmodifiableIterator = entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Multiset.Entry<E> entry = unmodifiableIterator.next();
/* 333 */       Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
/* 334 */       offset += entry.getCount(); }
/*     */     
/* 336 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 341 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 346 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     return entrySet().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Multiset.Entry<E>> entrySet() {
/* 362 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 363 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */   
/*     */   private ImmutableSet<Multiset.Entry<E>> createEntrySet() {
/* 367 */     return isEmpty() ? ImmutableSet.<Multiset.Entry<E>>of() : new EntrySet();
/*     */   }
/*     */   
/*     */   private final class EntrySet extends IndexedImmutableSet<Multiset.Entry<E>> { @J2ktIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private EntrySet() {}
/*     */     
/*     */     boolean isPartialView() {
/* 376 */       return ImmutableMultiset.this.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<E> get(int index) {
/* 381 */       return ImmutableMultiset.this.getEntry(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 386 */       return ImmutableMultiset.this.elementSet().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 391 */       if (o instanceof Multiset.Entry) {
/* 392 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 393 */         if (entry.getCount() <= 0) {
/* 394 */           return false;
/*     */         }
/* 396 */         int count = ImmutableMultiset.this.count(entry.getElement());
/* 397 */         return (count == entry.getCount());
/*     */       } 
/* 399 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 404 */       return ImmutableMultiset.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     Object writeReplace() {
/* 411 */       return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 417 */       throw new InvalidObjectException("Use EntrySetSerializedForm");
/*     */     } }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   static class EntrySetSerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
/* 429 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 433 */       return this.multiset.entrySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 441 */     return new SerializedForm((Multiset)this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 447 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 455 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ImmutableSet<E> elementSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset.Entry<E> getEntry(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     final Multiset<E> contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 485 */       this(LinkedHashMultiset.create());
/*     */     }
/*     */     
/*     */     Builder(Multiset<E> contents) {
/* 489 */       this.contents = contents;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 502 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 503 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 516 */       super.add(elements);
/* 517 */       return this;
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
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 533 */       this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
/* 534 */       return this;
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
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> setCount(E element, int count) {
/* 549 */       this.contents.setCount((E)Preconditions.checkNotNull(element), count);
/* 550 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 563 */       if (elements instanceof Multiset) {
/* 564 */         Multiset<? extends E> multiset = Multisets.cast(elements);
/* 565 */         multiset.forEachEntry((e, n) -> this.contents.add((E)Preconditions.checkNotNull(e), n));
/*     */       } else {
/* 567 */         super.addAll(elements);
/*     */       } 
/* 569 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 582 */       super.addAll(elements);
/* 583 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultiset<E> build() {
/* 592 */       return ImmutableMultiset.copyOf(this.contents);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableMultiset<E> buildJdkBacked() {
/* 597 */       if (this.contents.isEmpty()) {
/* 598 */         return ImmutableMultiset.of();
/*     */       }
/* 600 */       return JdkBackedImmutableMultiset.create(this.contents.entrySet());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ElementSet<E>
/*     */     extends ImmutableSet.Indexed<E> {
/*     */     private final List<Multiset.Entry<E>> entries;
/*     */     private final Multiset<E> delegate;
/*     */     
/*     */     ElementSet(List<Multiset.Entry<E>> entries, Multiset<E> delegate) {
/* 610 */       this.entries = entries;
/* 611 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     E get(int index) {
/* 616 */       return ((Multiset.Entry<E>)this.entries.get(index)).getElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/* 621 */       return this.delegate.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 626 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 631 */       return this.entries.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 640 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   static final class SerializedForm implements Serializable {
/*     */     final Object[] elements;
/*     */     final int[] counts;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Multiset<? extends Object> multiset) {
/* 651 */       int distinct = multiset.entrySet().size();
/* 652 */       this.elements = new Object[distinct];
/* 653 */       this.counts = new int[distinct];
/* 654 */       int i = 0;
/* 655 */       for (Multiset.Entry<? extends Object> entry : multiset.entrySet()) {
/* 656 */         this.elements[i] = entry.getElement();
/* 657 */         this.counts[i] = entry.getCount();
/* 658 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 663 */       LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
/* 664 */       for (int i = 0; i < this.elements.length; i++) {
/* 665 */         multiset.add(this.elements[i], this.counts[i]);
/*     */       }
/* 667 */       return ImmutableMultiset.copyOf(multiset);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */