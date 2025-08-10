/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StopWatch
/*    */ {
/* 35 */   private static final Logger LOGGER = LoggerFactory.getLogger(StopWatch.class);
/*    */   
/*    */   private long total;
/*    */   
/*    */   private long start;
/*    */   
/*    */   private Logger logger;
/*    */   
/*    */   public StopWatch() {
/* 44 */     this(LOGGER);
/*    */   }
/*    */   
/*    */   public StopWatch(Logger logger) {
/* 48 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public final Logger getLogger() {
/* 52 */     return this.logger;
/*    */   }
/*    */   
/*    */   public void start() {
/* 56 */     this.start = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public long stop() {
/* 60 */     long diff = System.currentTimeMillis() - this.start;
/* 61 */     this.total += diff;
/* 62 */     return diff;
/*    */   }
/*    */   
/*    */   public long stop(String message) {
/* 66 */     long time = stop();
/* 67 */     if (this.logger.isDebugEnabled()) {
/* 68 */       this.logger.debug(message + ": " + time + "ms");
/*    */     }
/* 70 */     return time;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 74 */     this.total = 0L;
/*    */   }
/*    */   
/*    */   public void reset(long total) {
/* 78 */     this.total = total;
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 82 */     return this.total;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/StopWatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */