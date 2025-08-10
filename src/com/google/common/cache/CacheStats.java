/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class CacheStats
/*     */ {
/*     */   private final long hitCount;
/*     */   private final long missCount;
/*     */   private final long loadSuccessCount;
/*     */   private final long loadExceptionCount;
/*     */   private final long totalLoadTime;
/*     */   private final long evictionCount;
/*     */   
/*     */   public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
/*  86 */     Preconditions.checkArgument((hitCount >= 0L));
/*  87 */     Preconditions.checkArgument((missCount >= 0L));
/*  88 */     Preconditions.checkArgument((loadSuccessCount >= 0L));
/*  89 */     Preconditions.checkArgument((loadExceptionCount >= 0L));
/*  90 */     Preconditions.checkArgument((totalLoadTime >= 0L));
/*  91 */     Preconditions.checkArgument((evictionCount >= 0L));
/*     */     
/*  93 */     this.hitCount = hitCount;
/*  94 */     this.missCount = missCount;
/*  95 */     this.loadSuccessCount = loadSuccessCount;
/*  96 */     this.loadExceptionCount = loadExceptionCount;
/*  97 */     this.totalLoadTime = totalLoadTime;
/*  98 */     this.evictionCount = evictionCount;
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
/*     */   public long requestCount() {
/* 110 */     return LongMath.saturatedAdd(this.hitCount, this.missCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public long hitCount() {
/* 115 */     return this.hitCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hitRate() {
/* 124 */     long requestCount = requestCount();
/* 125 */     return (requestCount == 0L) ? 1.0D : (this.hitCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long missCount() {
/* 135 */     return this.missCount;
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
/*     */   public double missRate() {
/* 148 */     long requestCount = requestCount();
/* 149 */     return (requestCount == 0L) ? 0.0D : (this.missCount / requestCount);
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
/*     */   public long loadCount() {
/* 162 */     return LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
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
/*     */   public long loadSuccessCount() {
/* 176 */     return this.loadSuccessCount;
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
/*     */   public long loadExceptionCount() {
/* 190 */     return this.loadExceptionCount;
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
/*     */   public double loadExceptionRate() {
/* 203 */     long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
/* 204 */     return (totalLoadCount == 0L) ? 0.0D : (this.loadExceptionCount / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long totalLoadTime() {
/* 214 */     return this.totalLoadTime;
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
/*     */   public double averageLoadPenalty() {
/* 226 */     long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
/* 227 */     return (totalLoadCount == 0L) ? 0.0D : (this.totalLoadTime / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long evictionCount() {
/* 235 */     return this.evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats minus(CacheStats other) {
/* 244 */     return new CacheStats(
/* 245 */         Math.max(0L, LongMath.saturatedSubtract(this.hitCount, other.hitCount)), 
/* 246 */         Math.max(0L, LongMath.saturatedSubtract(this.missCount, other.missCount)), 
/* 247 */         Math.max(0L, LongMath.saturatedSubtract(this.loadSuccessCount, other.loadSuccessCount)), 
/* 248 */         Math.max(0L, LongMath.saturatedSubtract(this.loadExceptionCount, other.loadExceptionCount)), 
/* 249 */         Math.max(0L, LongMath.saturatedSubtract(this.totalLoadTime, other.totalLoadTime)), 
/* 250 */         Math.max(0L, LongMath.saturatedSubtract(this.evictionCount, other.evictionCount)));
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
/*     */   public CacheStats plus(CacheStats other) {
/* 264 */     return new CacheStats(
/* 265 */         LongMath.saturatedAdd(this.hitCount, other.hitCount), 
/* 266 */         LongMath.saturatedAdd(this.missCount, other.missCount), 
/* 267 */         LongMath.saturatedAdd(this.loadSuccessCount, other.loadSuccessCount), 
/* 268 */         LongMath.saturatedAdd(this.loadExceptionCount, other.loadExceptionCount), 
/* 269 */         LongMath.saturatedAdd(this.totalLoadTime, other.totalLoadTime), 
/* 270 */         LongMath.saturatedAdd(this.evictionCount, other.evictionCount));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 275 */     return Objects.hashCode(new Object[] {
/* 276 */           Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount)
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 281 */     if (object instanceof CacheStats) {
/* 282 */       CacheStats other = (CacheStats)object;
/* 283 */       return (this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 295 */     return MoreObjects.toStringHelper(this)
/* 296 */       .add("hitCount", this.hitCount)
/* 297 */       .add("missCount", this.missCount)
/* 298 */       .add("loadSuccessCount", this.loadSuccessCount)
/* 299 */       .add("loadExceptionCount", this.loadExceptionCount)
/* 300 */       .add("totalLoadTime", this.totalLoadTime)
/* 301 */       .add("evictionCount", this.evictionCount)
/* 302 */       .toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/CacheStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */