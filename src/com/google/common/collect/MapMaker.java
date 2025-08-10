/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ @J2ktIncompatible
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class MapMaker
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   static final int UNSET_INT = -1;
/*     */   boolean useCustomMap;
/* 101 */   int initialCapacity = -1;
/* 102 */   int concurrencyLevel = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   MapMakerInternalMap.Strength keyStrength;
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   MapMakerInternalMap.Strength valueStrength;
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   Equivalence<Object> keyEquivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   MapMaker keyEquivalence(Equivalence<Object> equivalence) {
/* 125 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/* 126 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 127 */     this.useCustomMap = true;
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 132 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker initialCapacity(int initialCapacity) {
/* 147 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*     */ 
/*     */ 
/*     */     
/* 151 */     Preconditions.checkArgument((initialCapacity >= 0));
/* 152 */     this.initialCapacity = initialCapacity;
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 157 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker concurrencyLevel(int concurrencyLevel) {
/* 181 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*     */ 
/*     */ 
/*     */     
/* 185 */     Preconditions.checkArgument((concurrencyLevel > 0));
/* 186 */     this.concurrencyLevel = concurrencyLevel;
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 191 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakKeys() {
/* 208 */     return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */   
/*     */   MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
/* 212 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/* 213 */     this.keyStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
/* 214 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 216 */       this.useCustomMap = true;
/*     */     }
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getKeyStrength() {
/* 222 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakValues() {
/* 244 */     return setValueStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum Dummy
/*     */   {
/* 254 */     VALUE;
/*     */   }
/*     */   
/*     */   MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
/* 258 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/* 259 */     this.valueStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
/* 260 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 262 */       this.useCustomMap = true;
/*     */     }
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getValueStrength() {
/* 268 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
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
/*     */   public <K, V> ConcurrentMap<K, V> makeMap() {
/* 283 */     if (!this.useCustomMap) {
/* 284 */       return new ConcurrentHashMap<>(getInitialCapacity(), 0.75F, getConcurrencyLevel());
/*     */     }
/* 286 */     return MapMakerInternalMap.create(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 295 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 296 */     if (this.initialCapacity != -1) {
/* 297 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 299 */     if (this.concurrencyLevel != -1) {
/* 300 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 302 */     if (this.keyStrength != null) {
/* 303 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 305 */     if (this.valueStrength != null) {
/* 306 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 308 */     if (this.keyEquivalence != null) {
/* 309 */       s.addValue("keyEquivalence");
/*     */     }
/* 311 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MapMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */