/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   @CheckForNull
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   @CheckForNull
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  45 */     C c1 = range.hasLowerBound() ? (C)range.lowerEndpoint() : null;
/*  46 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */     
/*  48 */     C c2 = range.hasUpperBound() ? (C)range.upperEndpoint() : null;
/*  49 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  50 */     return new GeneralRange<>(
/*  51 */         Ordering.natural(), range
/*  52 */         .hasLowerBound(), (T)c1, lowerBoundType, range
/*     */ 
/*     */         
/*  55 */         .hasUpperBound(), (T)c2, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
/*  62 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @ParametricNullness T endpoint, BoundType boundType) {
/*  71 */     return new GeneralRange<>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @ParametricNullness T endpoint, BoundType boundType) {
/*  80 */     return new GeneralRange<>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
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
/*     */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, @ParametricNullness T lower, BoundType lowerType, @ParametricNullness T upper, BoundType upperType) {
/*  93 */     return new GeneralRange<>(comparator, true, lower, lowerType, true, upper, upperType);
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
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @CheckForNull T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @CheckForNull T upperEndpoint, BoundType upperBoundType) {
/* 112 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator);
/* 113 */     this.hasLowerBound = hasLowerBound;
/* 114 */     this.hasUpperBound = hasUpperBound;
/* 115 */     this.lowerEndpoint = lowerEndpoint;
/* 116 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 117 */     this.upperEndpoint = upperEndpoint;
/* 118 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (hasLowerBound)
/*     */     {
/* 127 */       int i = comparator.compare(
/* 128 */           NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint));
/*     */     }
/* 130 */     if (hasUpperBound)
/*     */     {
/* 132 */       int i = comparator.compare(
/* 133 */           NullnessCasts.uncheckedCastNullableTToT(upperEndpoint), NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
/*     */     }
/*     */     
/* 136 */     if (hasLowerBound && hasUpperBound) {
/*     */       
/* 138 */       int cmp = comparator.compare(
/* 139 */           NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
/*     */       
/* 141 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
/*     */       
/* 143 */       if (cmp == 0) {
/* 144 */         Preconditions.checkArgument((lowerBoundType != BoundType.OPEN || upperBoundType != BoundType.OPEN));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Comparator<? super T> comparator() {
/* 150 */     return this.comparator;
/*     */   }
/*     */   
/*     */   boolean hasLowerBound() {
/* 154 */     return this.hasLowerBound;
/*     */   }
/*     */   
/*     */   boolean hasUpperBound() {
/* 158 */     return this.hasUpperBound;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isEmpty() {
/* 163 */     return ((hasUpperBound() && tooLow(NullnessCasts.uncheckedCastNullableTToT(getUpperEndpoint()))) || (
/* 164 */       hasLowerBound() && tooHigh(NullnessCasts.uncheckedCastNullableTToT(getLowerEndpoint()))));
/*     */   }
/*     */   
/*     */   boolean tooLow(@ParametricNullness T t) {
/* 168 */     if (!hasLowerBound()) {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     T lbound = NullnessCasts.uncheckedCastNullableTToT(getLowerEndpoint());
/* 173 */     int cmp = this.comparator.compare(t, lbound);
/* 174 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0)) & ((getLowerBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean tooHigh(@ParametricNullness T t) {
/* 178 */     if (!hasUpperBound()) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     T ubound = NullnessCasts.uncheckedCastNullableTToT(getUpperEndpoint());
/* 183 */     int cmp = this.comparator.compare(t, ubound);
/* 184 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0)) & ((getUpperBoundType() == BoundType.OPEN));
/*     */   }
/*     */   
/*     */   boolean contains(@ParametricNullness T t) {
/* 188 */     return (!tooLow(t) && !tooHigh(t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 196 */     Preconditions.checkNotNull(other);
/* 197 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 199 */     boolean hasLowBound = this.hasLowerBound;
/* 200 */     T lowEnd = getLowerEndpoint();
/* 201 */     BoundType lowType = getLowerBoundType();
/* 202 */     if (!hasLowerBound()) {
/* 203 */       hasLowBound = other.hasLowerBound;
/* 204 */       lowEnd = other.getLowerEndpoint();
/* 205 */       lowType = other.getLowerBoundType();
/* 206 */     } else if (other.hasLowerBound()) {
/* 207 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 208 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 209 */         lowEnd = other.getLowerEndpoint();
/* 210 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     boolean hasUpBound = this.hasUpperBound;
/* 215 */     T upEnd = getUpperEndpoint();
/* 216 */     BoundType upType = getUpperBoundType();
/* 217 */     if (!hasUpperBound()) {
/* 218 */       hasUpBound = other.hasUpperBound;
/* 219 */       upEnd = other.getUpperEndpoint();
/* 220 */       upType = other.getUpperBoundType();
/* 221 */     } else if (other.hasUpperBound()) {
/* 222 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 223 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 224 */         upEnd = other.getUpperEndpoint();
/* 225 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     if (hasLowBound && hasUpBound) {
/* 230 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 231 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 233 */         lowEnd = upEnd;
/* 234 */         lowType = BoundType.OPEN;
/* 235 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 239 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 244 */     if (obj instanceof GeneralRange) {
/* 245 */       GeneralRange<?> r = (GeneralRange)obj;
/* 246 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && 
/*     */ 
/*     */         
/* 249 */         getLowerBoundType().equals(r.getLowerBoundType()) && 
/* 250 */         getUpperBoundType().equals(r.getUpperBoundType()) && 
/* 251 */         Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && 
/* 252 */         Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 259 */     return Objects.hashCode(new Object[] { this.comparator, 
/*     */           
/* 261 */           getLowerEndpoint(), 
/* 262 */           getLowerBoundType(), 
/* 263 */           getUpperEndpoint(), 
/* 264 */           getUpperBoundType() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 271 */     GeneralRange<T> result = this.reverse;
/* 272 */     if (result == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 281 */       result = new GeneralRange(Ordering.<T>from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/* 282 */       result.reverse = this;
/* 283 */       return this.reverse = result;
/*     */     } 
/* 285 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 290 */     return this.comparator + ":" + (
/*     */       
/* 292 */       (this.lowerBoundType == BoundType.CLOSED) ? 91 : 40) + (
/* 293 */       this.hasLowerBound ? (String)this.lowerEndpoint : "-∞") + ',' + (
/*     */       
/* 295 */       this.hasUpperBound ? (String)this.upperEndpoint : "∞") + (
/* 296 */       (this.upperBoundType == BoundType.CLOSED) ? 93 : 41);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   T getLowerEndpoint() {
/* 301 */     return this.lowerEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getLowerBoundType() {
/* 305 */     return this.lowerBoundType;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   T getUpperEndpoint() {
/* 310 */     return this.upperEndpoint;
/*     */   }
/*     */   
/*     */   BoundType getUpperBoundType() {
/* 314 */     return this.upperBoundType;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/GeneralRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */