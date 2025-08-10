/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Suppliers;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class CacheBuilder<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*      */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*      */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*      */   
/*  205 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*      */       {
/*      */         public void recordHits(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordMisses(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadSuccess(long loadTime) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadException(long loadTime) {}
/*      */ 
/*      */         
/*      */         public void recordEviction() {}
/*      */ 
/*      */         
/*      */         public CacheStats snapshot() {
/*  226 */           return CacheBuilder.EMPTY_STATS;
/*      */         }
/*      */       });
/*  229 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  243 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*      */     {
/*      */       public AbstractCache.StatsCounter get()
/*      */       {
/*  247 */         return new AbstractCache.SimpleStatsCounter();
/*      */       }
/*      */     };
/*      */   
/*      */   enum NullListener implements RemovalListener<Object, Object> {
/*  252 */     INSTANCE;
/*      */     
/*      */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*      */   }
/*      */   
/*      */   enum OneWeigher
/*      */     implements Weigher<Object, Object> {
/*  259 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public int weigh(Object key, Object value) {
/*  263 */       return 1;
/*      */     }
/*      */   }
/*      */   
/*  267 */   static final Ticker NULL_TICKER = new Ticker()
/*      */     {
/*      */       public long read()
/*      */       {
/*  271 */         return 0L;
/*      */       }
/*      */     };
/*      */   
/*      */   static final int UNSET_INT = -1;
/*      */   
/*  277 */   private static final class LoggerHolder { static final Logger logger = Logger.getLogger(CacheBuilder.class.getName()); }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean strictParsing = true;
/*      */ 
/*      */   
/*  284 */   int initialCapacity = -1;
/*  285 */   int concurrencyLevel = -1;
/*  286 */   long maximumSize = -1L;
/*  287 */   long maximumWeight = -1L; @CheckForNull
/*      */   Weigher<? super K, ? super V> weigher;
/*      */   @CheckForNull
/*      */   LocalCache.Strength keyStrength;
/*      */   @CheckForNull
/*      */   LocalCache.Strength valueStrength;
/*  293 */   long expireAfterWriteNanos = -1L;
/*      */ 
/*      */   
/*  296 */   long expireAfterAccessNanos = -1L;
/*      */ 
/*      */   
/*  299 */   long refreshNanos = -1L;
/*      */   @CheckForNull
/*      */   Equivalence<Object> keyEquivalence;
/*      */   @CheckForNull
/*      */   Equivalence<Object> valueEquivalence;
/*      */   @CheckForNull
/*      */   RemovalListener<? super K, ? super V> removalListener;
/*      */   @CheckForNull
/*      */   Ticker ticker;
/*  308 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CacheBuilder<Object, Object> newBuilder() {
/*  320 */     return new CacheBuilder<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
/*  330 */     return spec.toCacheBuilder().lenientParsing();
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
/*      */   public static CacheBuilder<Object, Object> from(String spec) {
/*  342 */     return from(CacheBuilderSpec.parse(spec));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   CacheBuilder<K, V> lenientParsing() {
/*  353 */     this.strictParsing = false;
/*  354 */     return this;
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/*  368 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/*  369 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  370 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getKeyEquivalence() {
/*  374 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/*  389 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", this.valueEquivalence);
/*      */     
/*  391 */     this.valueEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  392 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getValueEquivalence() {
/*  396 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/*  412 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*      */ 
/*      */ 
/*      */     
/*  416 */     Preconditions.checkArgument((initialCapacity >= 0));
/*  417 */     this.initialCapacity = initialCapacity;
/*  418 */     return this;
/*      */   }
/*      */   
/*      */   int getInitialCapacity() {
/*  422 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/*  458 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*      */ 
/*      */ 
/*      */     
/*  462 */     Preconditions.checkArgument((concurrencyLevel > 0));
/*  463 */     this.concurrencyLevel = concurrencyLevel;
/*  464 */     return this;
/*      */   }
/*      */   
/*      */   int getConcurrencyLevel() {
/*  468 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> maximumSize(long maximumSize) {
/*  494 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  496 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  500 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/*  501 */     Preconditions.checkArgument((maximumSize >= 0L), "maximum size must not be negative");
/*  502 */     this.maximumSize = maximumSize;
/*  503 */     return this;
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> maximumWeight(long maximumWeight) {
/*  536 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  540 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  542 */     Preconditions.checkArgument((maximumWeight >= 0L), "maximum weight must not be negative");
/*  543 */     this.maximumWeight = maximumWeight;
/*  544 */     return this;
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
/*      */   @CanIgnoreReturnValue
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/*  580 */     Preconditions.checkState((this.weigher == null));
/*  581 */     if (this.strictParsing) {
/*  582 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size (%s provided)", this.maximumSize);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  590 */     CacheBuilder<K1, V1> me = this;
/*  591 */     me.weigher = (Weigher<? super K, ? super V>)Preconditions.checkNotNull(weigher);
/*  592 */     return me;
/*      */   }
/*      */   
/*      */   long getMaximumWeight() {
/*  596 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/*  597 */       return 0L;
/*      */     }
/*  599 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
/*  605 */     return (Weigher<K1, V1>)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> weakKeys() {
/*  627 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/*  632 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/*  633 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  634 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getKeyStrength() {
/*  638 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> weakValues() {
/*  661 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> softValues() {
/*  687 */     return setValueStrength(LocalCache.Strength.SOFT);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/*  692 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/*  693 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  694 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getValueStrength() {
/*  698 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> expireAfterWrite(Duration duration) {
/*  726 */     return expireAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/*  754 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*      */ 
/*      */ 
/*      */     
/*  758 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  759 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/*  760 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getExpireAfterWriteNanos() {
/*  765 */     return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> expireAfterAccess(Duration duration) {
/*  798 */     return expireAfterAccess(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/*  831 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*      */ 
/*      */ 
/*      */     
/*  835 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  836 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/*  837 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getExpireAfterAccessNanos() {
/*  842 */     return (this.expireAfterAccessNanos == -1L) ? 
/*  843 */       0L : 
/*  844 */       this.expireAfterAccessNanos;
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> refreshAfterWrite(Duration duration) {
/*  879 */     return refreshAfterWrite(toNanosSaturated(duration), TimeUnit.NANOSECONDS);
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
/*      */   @GwtIncompatible
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/*  916 */     Preconditions.checkNotNull(unit);
/*  917 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", this.refreshNanos);
/*  918 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", duration, unit);
/*  919 */     this.refreshNanos = unit.toNanos(duration);
/*  920 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   long getRefreshNanos() {
/*  925 */     return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/*  940 */     Preconditions.checkState((this.ticker == null));
/*  941 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/*  942 */     return this;
/*      */   }
/*      */   
/*      */   Ticker getTicker(boolean recordsTime) {
/*  946 */     if (this.ticker != null) {
/*  947 */       return this.ticker;
/*      */     }
/*  949 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/*  975 */     Preconditions.checkState((this.removalListener == null));
/*      */ 
/*      */ 
/*      */     
/*  979 */     CacheBuilder<K1, V1> me = this;
/*  980 */     me.removalListener = (RemovalListener<? super K, ? super V>)Preconditions.checkNotNull(listener);
/*  981 */     return me;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
/*  987 */     return 
/*  988 */       (RemovalListener<K1, V1>)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*      */   @CanIgnoreReturnValue
/*      */   public CacheBuilder<K, V> recordStats() {
/* 1002 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/* 1003 */     return this;
/*      */   }
/*      */   
/*      */   boolean isRecordingStats() {
/* 1007 */     return (this.statsCounterSupplier == CACHE_STATS_COUNTER);
/*      */   }
/*      */   
/*      */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/* 1011 */     return this.statsCounterSupplier;
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
/*      */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/* 1028 */     checkWeightWithWeigher();
/* 1029 */     return new LocalCache.LocalLoadingCache<>(this, loader);
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
/*      */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/* 1045 */     checkWeightWithWeigher();
/* 1046 */     checkNonLoadingCache();
/* 1047 */     return new LocalCache.LocalManualCache<>(this);
/*      */   }
/*      */   
/*      */   private void checkNonLoadingCache() {
/* 1051 */     Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache");
/*      */   }
/*      */   
/*      */   private void checkWeightWithWeigher() {
/* 1055 */     if (this.weigher == null) {
/* 1056 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*      */     }
/* 1058 */     else if (this.strictParsing) {
/* 1059 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*      */     }
/* 1061 */     else if (this.maximumWeight == -1L) {
/* 1062 */       LoggerHolder.logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
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
/*      */   public String toString() {
/* 1075 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 1076 */     if (this.initialCapacity != -1) {
/* 1077 */       s.add("initialCapacity", this.initialCapacity);
/*      */     }
/* 1079 */     if (this.concurrencyLevel != -1) {
/* 1080 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*      */     }
/* 1082 */     if (this.maximumSize != -1L) {
/* 1083 */       s.add("maximumSize", this.maximumSize);
/*      */     }
/* 1085 */     if (this.maximumWeight != -1L) {
/* 1086 */       s.add("maximumWeight", this.maximumWeight);
/*      */     }
/* 1088 */     if (this.expireAfterWriteNanos != -1L) {
/* 1089 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*      */     }
/* 1091 */     if (this.expireAfterAccessNanos != -1L) {
/* 1092 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*      */     }
/* 1094 */     if (this.keyStrength != null) {
/* 1095 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*      */     }
/* 1097 */     if (this.valueStrength != null) {
/* 1098 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*      */     }
/* 1100 */     if (this.keyEquivalence != null) {
/* 1101 */       s.addValue("keyEquivalence");
/*      */     }
/* 1103 */     if (this.valueEquivalence != null) {
/* 1104 */       s.addValue("valueEquivalence");
/*      */     }
/* 1106 */     if (this.removalListener != null) {
/* 1107 */       s.addValue("removalListener");
/*      */     }
/* 1109 */     return s.toString();
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
/*      */   @GwtIncompatible
/*      */   private static long toNanosSaturated(Duration duration) {
/*      */     try {
/* 1125 */       return duration.toNanos();
/* 1126 */     } catch (ArithmeticException tooBig) {
/* 1127 */       return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */