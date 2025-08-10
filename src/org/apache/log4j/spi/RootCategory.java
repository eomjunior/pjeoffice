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
/*    */ public final class RootCategory
/*    */   extends Logger
/*    */ {
/*    */   public RootCategory(Level level) {
/* 36 */     super("root");
/* 37 */     setLevel(level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Level getChainedLevel() {
/* 44 */     return this.level;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setLevel(Level level) {
/* 54 */     if (level == null) {
/* 55 */       LogLog.error("You have tried to set a null level to root.", new Throwable());
/*    */     } else {
/* 57 */       this.level = level;
/*    */     } 
/*    */   }
/*    */   
/*    */   public final void setPriority(Level level) {
/* 62 */     setLevel(level);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/RootCategory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */