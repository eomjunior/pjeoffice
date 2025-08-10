/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ abstract class Cut<C extends Comparable>
/*     */   implements Comparable<Cut<C>>, Serializable
/*     */ {
/*     */   final C endpoint;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Cut(C endpoint) {
/*  40 */     this.endpoint = endpoint;
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
/*     */   Cut<C> canonical(DiscreteDomain<C> domain) {
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Cut<C> that) {
/*  74 */     if (that == belowAll()) {
/*  75 */       return 1;
/*     */     }
/*  77 */     if (that == aboveAll()) {
/*  78 */       return -1;
/*     */     }
/*  80 */     int result = Range.compareOrThrow((Comparable)this.endpoint, (Comparable)that.endpoint);
/*  81 */     if (result != 0) {
/*  82 */       return result;
/*     */     }
/*     */     
/*  85 */     return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
/*     */   }
/*     */   
/*     */   C endpoint() {
/*  89 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/*  95 */     if (obj instanceof Cut) {
/*     */       
/*  97 */       Cut<C> that = (Cut<C>)obj;
/*     */       try {
/*  99 */         int compareResult = compareTo(that);
/* 100 */         return (compareResult == 0);
/* 101 */       } catch (ClassCastException wastNotComparableToOurType) {
/* 102 */         return false;
/*     */       } 
/*     */     } 
/* 105 */     return false;
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
/*     */   static <C extends Comparable> Cut<C> belowAll() {
/* 118 */     return BelowAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BelowAll
/*     */     extends Cut<Comparable<?>>
/*     */   {
/* 124 */     private static final BelowAll INSTANCE = new BelowAll();
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     private BelowAll() {
/* 133 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 138 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 143 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 148 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 153 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 159 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 165 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 170 */       sb.append("(-∞");
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 175 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 180 */       return domain.minValue();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 185 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
/*     */       try {
/* 191 */         return Cut.belowValue(domain.minValue());
/* 192 */       } catch (NoSuchElementException e) {
/* 193 */         return this;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 199 */       return (o == this) ? 0 : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 204 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 209 */       return "-∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 213 */       return INSTANCE;
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
/*     */   static <C extends Comparable> Cut<C> aboveAll() {
/* 225 */     return AboveAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class AboveAll extends Cut<Comparable<?>> {
/* 229 */     private static final AboveAll INSTANCE = new AboveAll();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AboveAll() {
/* 233 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> endpoint() {
/* 238 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(Comparable<?> value) {
/* 243 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 248 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 253 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 259 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) {
/* 265 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 270 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 275 */       sb.append("+∞)");
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) {
/* 280 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) {
/* 285 */       return domain.maxValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o) {
/* 290 */       return (o == this) ? 0 : 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 295 */       return System.identityHashCode(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 300 */       return "+∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 304 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> Cut<C> belowValue(C endpoint) {
/* 311 */     return new BelowValue<>(endpoint);
/*     */   }
/*     */   private static final class BelowValue<C extends Comparable> extends Cut<C> { private static final long serialVersionUID = 0L;
/*     */     
/*     */     BelowValue(C endpoint) {
/* 316 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isLessThan(C value) {
/* 321 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) <= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 326 */       return BoundType.CLOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 331 */       return BoundType.OPEN;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 336 */       switch (boundType) {
/*     */         case CLOSED:
/* 338 */           return this;
/*     */         case OPEN:
/* 340 */           previous = domain.previous(this.endpoint);
/* 341 */           return (previous == null) ? Cut.<C>belowAll() : new Cut.AboveValue<>(previous);
/*     */       } 
/* 343 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 349 */       switch (boundType) {
/*     */         case CLOSED:
/* 351 */           previous = domain.previous(this.endpoint);
/* 352 */           return (previous == null) ? Cut.<C>aboveAll() : new Cut.AboveValue<>(previous);
/*     */         case OPEN:
/* 354 */           return this;
/*     */       } 
/* 356 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 362 */       sb.append('[').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 367 */       sb.append(this.endpoint).append(')');
/*     */     }
/*     */ 
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 372 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 378 */       return domain.previous(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 383 */       return this.endpoint.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 388 */       return "\\" + this.endpoint + "/";
/*     */     } } abstract boolean isLessThan(C paramC); abstract BoundType typeAsLowerBound(); abstract BoundType typeAsUpperBound(); abstract Cut<C> withLowerBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain); abstract Cut<C> withUpperBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract void describeAsLowerBound(StringBuilder paramStringBuilder);
/*     */   abstract void describeAsUpperBound(StringBuilder paramStringBuilder);
/*     */   @CheckForNull
/*     */   abstract C leastValueAbove(DiscreteDomain<C> paramDiscreteDomain);
/*     */   static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
/* 395 */     return new AboveValue<>(endpoint);
/*     */   } @CheckForNull
/*     */   abstract C greatestValueBelow(DiscreteDomain<C> paramDiscreteDomain);
/*     */   public abstract int hashCode();
/*     */   private static final class AboveValue<C extends Comparable> extends Cut<C> { AboveValue(C endpoint) {
/* 400 */       super((C)Preconditions.checkNotNull(endpoint));
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     boolean isLessThan(C value) {
/* 405 */       return (Range.compareOrThrow((Comparable)this.endpoint, (Comparable)value) < 0);
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsLowerBound() {
/* 410 */       return BoundType.OPEN;
/*     */     }
/*     */ 
/*     */     
/*     */     BoundType typeAsUpperBound() {
/* 415 */       return BoundType.CLOSED;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 420 */       switch (boundType) {
/*     */         case OPEN:
/* 422 */           return this;
/*     */         case CLOSED:
/* 424 */           next = domain.next(this.endpoint);
/* 425 */           return (next == null) ? Cut.<C>belowAll() : belowValue(next);
/*     */       } 
/* 427 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 433 */       switch (boundType) {
/*     */         case OPEN:
/* 435 */           next = domain.next(this.endpoint);
/* 436 */           return (next == null) ? Cut.<C>aboveAll() : belowValue(next);
/*     */         case CLOSED:
/* 438 */           return this;
/*     */       } 
/* 440 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb) {
/* 446 */       sb.append('(').append(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb) {
/* 451 */       sb.append(this.endpoint).append(']');
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     C leastValueAbove(DiscreteDomain<C> domain) {
/* 457 */       return domain.next(this.endpoint);
/*     */     }
/*     */ 
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain) {
/* 462 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */     
/*     */     Cut<C> canonical(DiscreteDomain<C> domain) {
/* 467 */       C next = leastValueAbove(domain);
/* 468 */       return (next != null) ? belowValue(next) : Cut.<C>aboveAll();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 473 */       return this.endpoint.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 478 */       return "/" + this.endpoint + "\\";
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Cut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */