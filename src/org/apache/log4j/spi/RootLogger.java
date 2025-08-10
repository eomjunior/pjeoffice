/*    */ package org.apache.log4j.spi;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.helpers.LogLog;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RootLogger
/*    */   extends Logger
/*    */ {
/*    */   public RootLogger(Level level) {
/* 44 */     super("root");
/* 45 */     setLevel(level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Level getChainedLevel() {
/* 52 */     return this.level;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setLevel(Level level) {
/* 62 */     if (level == null) {
/* 63 */       LogLog.error("You have tried to set a null level to root.", new Throwable());
/*    */     } else {
/* 65 */       this.level = level;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/RootLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */