/*    */ package org.apache.log4j.varia;
/*    */ 
/*    */ import org.apache.log4j.AppenderSkeleton;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ public class NullAppender
/*    */   extends AppenderSkeleton
/*    */ {
/* 30 */   private static NullAppender instance = new NullAppender();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void activateOptions() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NullAppender getInstance() {
/* 48 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NullAppender getNullAppender() {
/* 56 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doAppend(LoggingEvent event) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void append(LoggingEvent event) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean requiresLayout() {
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/NullAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */