/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   private final Range<C> range;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
/*  44 */     super(domain);
/*  45 */     this.range = range;
/*     */   }
/*     */   
/*     */   private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
/*  49 */     return this.range.isConnected(other) ? 
/*  50 */       ContiguousSet.<C>create(this.range.intersection(other), this.domain) : 
/*  51 */       new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  56 */     return intersectionInCurrentDomain((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  62 */     if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive)
/*     */     {
/*  64 */       return new EmptyContiguousSet<>(this.domain);
/*     */     }
/*  66 */     return intersectionInCurrentDomain(
/*  67 */         (Range)Range.range((Comparable<?>)fromElement, 
/*  68 */           BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/*  69 */           BoundType.forBoolean(toInclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/*  74 */     return intersectionInCurrentDomain((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(@CheckForNull Object target) {
/*  81 */     return contains(target) ? (int)this.domain.distance(first(), (C)Objects.<Object>requireNonNull(target)) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  86 */     return new AbstractSequentialIterator<C>((Comparable)first()) {
/*  87 */         final C last = RegularContiguousSet.this.last();
/*     */ 
/*     */         
/*     */         @CheckForNull
/*     */         protected C computeNext(C previous) {
/*  92 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.last) ? null : RegularContiguousSet.this.domain.next(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/* 100 */     return new AbstractSequentialIterator<C>((Comparable)last()) {
/* 101 */         final C first = RegularContiguousSet.this.first();
/*     */ 
/*     */         
/*     */         @CheckForNull
/*     */         protected C computeNext(C previous) {
/* 106 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.first) ? null : RegularContiguousSet.this.domain.previous(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean equalsOrThrow(Comparable<?> left, @CheckForNull Comparable<?> right) {
/* 112 */     return (right != null && Range.compareOrThrow(left, right) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public C first() {
/* 123 */     return (C)Objects.<Comparable>requireNonNull((Comparable)this.range.lowerBound.leastValueAbove(this.domain));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public C last() {
/* 129 */     return (C)Objects.<Comparable>requireNonNull((Comparable)this.range.upperBound.greatestValueBelow(this.domain));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<C> createAsList() {
/* 134 */     if (this.domain.supportsFastOffset) {
/* 135 */       return new ImmutableAsList<C>()
/*     */         {
/*     */           ImmutableSortedSet<C> delegateCollection() {
/* 138 */             return RegularContiguousSet.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public C get(int i) {
/* 143 */             Preconditions.checkElementIndex(i, size());
/* 144 */             return RegularContiguousSet.this.domain.offset(RegularContiguousSet.this.first(), i);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           @J2ktIncompatible
/*     */           @GwtIncompatible
/*     */           Object writeReplace() {
/* 153 */             return super.writeReplace();
/*     */           }
/*     */         };
/*     */     }
/* 157 */     return super.createAsList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 163 */     long distance = this.domain.distance(first(), last());
/* 164 */     return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 169 */     if (object == null) {
/* 170 */       return false;
/*     */     }
/*     */     try {
/* 173 */       return this.range.contains((C)object);
/* 174 */     } catch (ClassCastException e) {
/* 175 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 181 */     return Collections2.containsAllImpl(this, targets);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/* 191 */     Preconditions.checkNotNull(other);
/* 192 */     Preconditions.checkArgument(this.domain.equals(other.domain));
/* 193 */     if (other.isEmpty()) {
/* 194 */       return other;
/*     */     }
/* 196 */     Comparable<Comparable> comparable1 = (Comparable)Ordering.<Comparable>natural().max(first(), (Comparable)other.first());
/* 197 */     Comparable<Comparable> comparable2 = (Comparable)Ordering.<Comparable>natural().min(last(), (Comparable)other.last());
/* 198 */     return (comparable1.compareTo(comparable2) <= 0) ? 
/* 199 */       ContiguousSet.<C>create((Range)Range.closed(comparable1, comparable2), this.domain) : 
/* 200 */       new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/* 206 */     return range(BoundType.CLOSED, BoundType.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/* 211 */     return (Range)Range.create(this.range.lowerBound
/* 212 */         .withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound
/* 213 */         .withUpperBoundType(upperBoundType, this.domain));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 218 */     if (object == this)
/* 219 */       return true; 
/* 220 */     if (object instanceof RegularContiguousSet) {
/* 221 */       RegularContiguousSet<?> that = (RegularContiguousSet)object;
/* 222 */       if (this.domain.equals(that.domain)) {
/* 223 */         return (first().equals(that.first()) && last().equals(that.last()));
/*     */       }
/*     */     } 
/* 226 */     return super.equals(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 232 */     return Sets.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     final Range<C> range;
/*     */     final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
/* 242 */       this.range = range;
/* 243 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 247 */       return new RegularContiguousSet<>(this.range, this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 255 */     return new SerializedForm<>(this.range, this.domain);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 261 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */