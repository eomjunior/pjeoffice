/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public final class CacheBuilderSpec
/*     */ {
/*  91 */   private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
/*     */ 
/*     */   
/*  94 */   private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder()
/*  99 */     .put("initialCapacity", new InitialCapacityParser())
/* 100 */     .put("maximumSize", new MaximumSizeParser())
/* 101 */     .put("maximumWeight", new MaximumWeightParser())
/* 102 */     .put("concurrencyLevel", new ConcurrencyLevelParser())
/* 103 */     .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
/* 104 */     .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
/* 105 */     .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
/* 106 */     .put("recordStats", new RecordStatsParser())
/* 107 */     .put("expireAfterAccess", new AccessDurationParser())
/* 108 */     .put("expireAfterWrite", new WriteDurationParser())
/* 109 */     .put("refreshAfterWrite", new RefreshDurationParser())
/* 110 */     .put("refreshInterval", new RefreshDurationParser())
/* 111 */     .buildOrThrow(); @CheckForNull
/*     */   @VisibleForTesting
/*     */   Integer initialCapacity; @CheckForNull
/*     */   @VisibleForTesting
/*     */   Long maximumSize; @CheckForNull
/*     */   @VisibleForTesting
/*     */   Long maximumWeight; @CheckForNull
/*     */   @VisibleForTesting
/*     */   Integer concurrencyLevel;
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength keyStrength;
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength valueStrength;
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   Boolean recordStats;
/*     */   
/* 130 */   private CacheBuilderSpec(String specification) { this.specification = specification; }
/*     */   
/*     */   @VisibleForTesting
/*     */   long writeExpirationDuration; @CheckForNull
/*     */   @VisibleForTesting
/*     */   TimeUnit writeExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long accessExpirationDuration;
/*     */   
/* 139 */   public static CacheBuilderSpec parse(String cacheBuilderSpecification) { CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
/* 140 */     if (!cacheBuilderSpecification.isEmpty()) {
/* 141 */       for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
/* 142 */         ImmutableList<String> immutableList = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
/* 143 */         Preconditions.checkArgument(!immutableList.isEmpty(), "blank key-value pair");
/* 144 */         Preconditions.checkArgument(
/* 145 */             (immutableList.size() <= 2), "key-value pair %s with more than one equals sign", keyValuePair);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 150 */         String key = immutableList.get(0);
/* 151 */         ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
/* 152 */         Preconditions.checkArgument((valueParser != null), "unknown key %s", key);
/*     */         
/* 154 */         String value = (immutableList.size() == 1) ? null : immutableList.get(1);
/* 155 */         valueParser.parse(spec, key, value);
/*     */       } 
/*     */     }
/*     */     
/* 159 */     return spec; }
/*     */    @CheckForNull
/*     */   @VisibleForTesting
/*     */   TimeUnit accessExpirationTimeUnit; @VisibleForTesting
/*     */   long refreshDuration;
/*     */   public static CacheBuilderSpec disableCaching() {
/* 165 */     return parse("maximumSize=0");
/*     */   } @CheckForNull
/*     */   @VisibleForTesting
/*     */   TimeUnit refreshTimeUnit; private final String specification;
/*     */   CacheBuilder<Object, Object> toCacheBuilder() {
/* 170 */     CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
/* 171 */     if (this.initialCapacity != null) {
/* 172 */       builder.initialCapacity(this.initialCapacity.intValue());
/*     */     }
/* 174 */     if (this.maximumSize != null) {
/* 175 */       builder.maximumSize(this.maximumSize.longValue());
/*     */     }
/* 177 */     if (this.maximumWeight != null) {
/* 178 */       builder.maximumWeight(this.maximumWeight.longValue());
/*     */     }
/* 180 */     if (this.concurrencyLevel != null) {
/* 181 */       builder.concurrencyLevel(this.concurrencyLevel.intValue());
/*     */     }
/* 183 */     if (this.keyStrength != null) {
/* 184 */       switch (this.keyStrength) {
/*     */         case WEAK:
/* 186 */           builder.weakKeys();
/*     */           break;
/*     */         default:
/* 189 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 192 */     if (this.valueStrength != null) {
/* 193 */       switch (this.valueStrength) {
/*     */         case SOFT:
/* 195 */           builder.softValues();
/*     */           break;
/*     */         case WEAK:
/* 198 */           builder.weakValues();
/*     */           break;
/*     */         default:
/* 201 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 204 */     if (this.recordStats != null && this.recordStats.booleanValue()) {
/* 205 */       builder.recordStats();
/*     */     }
/* 207 */     if (this.writeExpirationTimeUnit != null) {
/* 208 */       builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
/*     */     }
/* 210 */     if (this.accessExpirationTimeUnit != null) {
/* 211 */       builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
/*     */     }
/* 213 */     if (this.refreshTimeUnit != null) {
/* 214 */       builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
/*     */     }
/*     */     
/* 217 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toParsableString() {
/* 226 */     return this.specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 235 */     return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 240 */     return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 248 */           durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 249 */           durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 250 */           durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 255 */     if (this == obj) {
/* 256 */       return true;
/*     */     }
/* 258 */     if (!(obj instanceof CacheBuilderSpec)) {
/* 259 */       return false;
/*     */     }
/* 261 */     CacheBuilderSpec that = (CacheBuilderSpec)obj;
/* 262 */     return (Objects.equal(this.initialCapacity, that.initialCapacity) && 
/* 263 */       Objects.equal(this.maximumSize, that.maximumSize) && 
/* 264 */       Objects.equal(this.maximumWeight, that.maximumWeight) && 
/* 265 */       Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && 
/* 266 */       Objects.equal(this.keyStrength, that.keyStrength) && 
/* 267 */       Objects.equal(this.valueStrength, that.valueStrength) && 
/* 268 */       Objects.equal(this.recordStats, that.recordStats) && 
/* 269 */       Objects.equal(
/* 270 */         durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 271 */         durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && 
/* 272 */       Objects.equal(
/* 273 */         durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 274 */         durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && 
/* 275 */       Objects.equal(
/* 276 */         durationInNanos(this.refreshDuration, this.refreshTimeUnit), 
/* 277 */         durationInNanos(that.refreshDuration, that.refreshTimeUnit)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Long durationInNanos(long duration, @CheckForNull TimeUnit unit) {
/* 286 */     return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
/*     */   }
/*     */   
/*     */   static abstract class IntegerParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseInteger(CacheBuilderSpec param1CacheBuilderSpec, int param1Int);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 295 */       if (Strings.isNullOrEmpty(value)) {
/* 296 */         throw new IllegalArgumentException("value of key " + key + " omitted");
/*     */       }
/*     */       try {
/* 299 */         parseInteger(spec, Integer.parseInt(value));
/* 300 */       } catch (NumberFormatException e) {
/* 301 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 302 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class LongParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseLong(CacheBuilderSpec param1CacheBuilderSpec, long param1Long);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 313 */       if (Strings.isNullOrEmpty(value)) {
/* 314 */         throw new IllegalArgumentException("value of key " + key + " omitted");
/*     */       }
/*     */       try {
/* 317 */         parseLong(spec, Long.parseLong(value));
/* 318 */       } catch (NumberFormatException e) {
/* 319 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 320 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class InitialCapacityParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 329 */       Preconditions.checkArgument((spec.initialCapacity == null), "initial capacity was already set to %s", spec.initialCapacity);
/*     */ 
/*     */ 
/*     */       
/* 333 */       spec.initialCapacity = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumSizeParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 341 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to %s", spec.maximumSize);
/*     */       
/* 343 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to %s", spec.maximumWeight);
/*     */       
/* 345 */       spec.maximumSize = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumWeightParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 353 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to %s", spec.maximumWeight);
/*     */       
/* 355 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to %s", spec.maximumSize);
/*     */       
/* 357 */       spec.maximumWeight = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConcurrencyLevelParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 365 */       Preconditions.checkArgument((spec.concurrencyLevel == null), "concurrency level was already set to %s", spec.concurrencyLevel);
/*     */ 
/*     */ 
/*     */       
/* 369 */       spec.concurrencyLevel = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public KeyStrengthParser(LocalCache.Strength strength) {
/* 378 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @CheckForNull String value) {
/* 383 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 384 */       Preconditions.checkArgument((spec.keyStrength == null), "%s was already set to %s", key, spec.keyStrength);
/* 385 */       spec.keyStrength = this.strength;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ValueStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public ValueStrengthParser(LocalCache.Strength strength) {
/* 394 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @CheckForNull String value) {
/* 399 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 400 */       Preconditions.checkArgument((spec.valueStrength == null), "%s was already set to %s", key, spec.valueStrength);
/*     */ 
/*     */       
/* 403 */       spec.valueStrength = this.strength;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecordStatsParser
/*     */     implements ValueParser
/*     */   {
/*     */     public void parse(CacheBuilderSpec spec, String key, @CheckForNull String value) {
/* 412 */       Preconditions.checkArgument((value == null), "recordStats does not take values");
/* 413 */       Preconditions.checkArgument((spec.recordStats == null), "recordStats already set");
/* 414 */       spec.recordStats = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class DurationParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseDuration(CacheBuilderSpec param1CacheBuilderSpec, long param1Long, TimeUnit param1TimeUnit);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @CheckForNull String value) {
/* 424 */       if (Strings.isNullOrEmpty(value))
/* 425 */         throw new IllegalArgumentException("value of key " + key + " omitted"); 
/*     */       try {
/*     */         TimeUnit timeUnit;
/* 428 */         char lastChar = value.charAt(value.length() - 1);
/*     */         
/* 430 */         switch (lastChar) {
/*     */           case 'd':
/* 432 */             timeUnit = TimeUnit.DAYS;
/*     */             break;
/*     */           case 'h':
/* 435 */             timeUnit = TimeUnit.HOURS;
/*     */             break;
/*     */           case 'm':
/* 438 */             timeUnit = TimeUnit.MINUTES;
/*     */             break;
/*     */           case 's':
/* 441 */             timeUnit = TimeUnit.SECONDS;
/*     */             break;
/*     */           default:
/* 444 */             throw new IllegalArgumentException(CacheBuilderSpec
/* 445 */                 .format("key %s invalid unit: was %s, must end with one of [dhms]", new Object[] { key, value }));
/*     */         } 
/*     */         
/* 448 */         long duration = Long.parseLong(value.substring(0, value.length() - 1));
/* 449 */         parseDuration(spec, duration, timeUnit);
/* 450 */       } catch (NumberFormatException e) {
/* 451 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 452 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AccessDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 461 */       Preconditions.checkArgument((spec.accessExpirationTimeUnit == null), "expireAfterAccess already set");
/* 462 */       spec.accessExpirationDuration = duration;
/* 463 */       spec.accessExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class WriteDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 471 */       Preconditions.checkArgument((spec.writeExpirationTimeUnit == null), "expireAfterWrite already set");
/* 472 */       spec.writeExpirationDuration = duration;
/* 473 */       spec.writeExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RefreshDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 481 */       Preconditions.checkArgument((spec.refreshTimeUnit == null), "refreshAfterWrite already set");
/* 482 */       spec.refreshDuration = duration;
/* 483 */       spec.refreshTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 488 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */   
/*     */   private static interface ValueParser {
/*     */     void parse(CacheBuilderSpec param1CacheBuilderSpec, String param1String1, @CheckForNull String param1String2);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/CacheBuilderSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */