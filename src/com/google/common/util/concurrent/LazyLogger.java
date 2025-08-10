/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.logging.Logger;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class LazyLogger
/*    */ {
/*    */   private final String loggerName;
/*    */   private volatile Logger logger;
/*    */   
/*    */   LazyLogger(Class<?> ownerOfLogger) {
/* 29 */     this.loggerName = ownerOfLogger.getName();
/*    */   }
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
/*    */   Logger get() {
/* 44 */     Logger local = this.logger;
/* 45 */     if (local != null) {
/* 46 */       return local;
/*    */     }
/* 48 */     synchronized (this) {
/* 49 */       local = this.logger;
/* 50 */       if (local != null) {
/* 51 */         return local;
/*    */       }
/* 53 */       return this.logger = Logger.getLogger(this.loggerName);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/LazyLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */