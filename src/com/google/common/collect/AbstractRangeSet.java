/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @GwtIncompatible
/*     */ abstract class AbstractRangeSet<C extends Comparable>
/*     */   implements RangeSet<C>
/*     */ {
/*     */   public boolean contains(C value) {
/*  32 */     return (rangeContaining(value) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public abstract Range<C> rangeContaining(C paramC);
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  41 */     return asRanges().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> range) {
/*  46 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> range) {
/*  51 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  56 */     remove((Range)Range.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enclosesAll(RangeSet<C> other) {
/*  61 */     return enclosesAll(other.asRanges());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(RangeSet<C> other) {
/*  66 */     addAll(other.asRanges());
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(RangeSet<C> other) {
/*  71 */     removeAll(other.asRanges());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> otherRange) {
/*  76 */     return !subRangeSet(otherRange).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean encloses(Range<C> paramRange);
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/*  84 */     if (obj == this)
/*  85 */       return true; 
/*  86 */     if (obj instanceof RangeSet) {
/*  87 */       RangeSet<?> other = (RangeSet)obj;
/*  88 */       return asRanges().equals(other.asRanges());
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  95 */     return asRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 100 */     return asRanges().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */