/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*    */ import org.apache.hc.core5.pool.PoolStats;
/*    */ 
/*    */ public final class PoolingStats
/*    */   implements Supplier<PoolStats>
/*    */ {
/*    */   private final PoolingHttpClientConnectionManager pool;
/*    */   
/*    */   public PoolingStats(PoolingHttpClientConnectionManager pool) {
/* 13 */     this.pool = Args.<PoolingHttpClientConnectionManager>requireNonNull(pool, "pool is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public PoolStats get() {
/* 18 */     return this.pool.getTotalStats();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/PoolingStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */