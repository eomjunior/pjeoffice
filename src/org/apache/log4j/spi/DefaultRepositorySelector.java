/*    */ package org.apache.log4j.spi;
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
/*    */ public class DefaultRepositorySelector
/*    */   implements RepositorySelector
/*    */ {
/*    */   final LoggerRepository repository;
/*    */   
/*    */   public DefaultRepositorySelector(LoggerRepository repository) {
/* 25 */     this.repository = repository;
/*    */   }
/*    */   
/*    */   public LoggerRepository getLoggerRepository() {
/* 29 */     return this.repository;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/DefaultRepositorySelector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */