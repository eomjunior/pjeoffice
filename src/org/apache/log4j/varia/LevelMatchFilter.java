/*    */ package org.apache.log4j.varia;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.apache.log4j.helpers.OptionConverter;
/*    */ import org.apache.log4j.spi.Filter;
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
/*    */ public class LevelMatchFilter
/*    */   extends Filter
/*    */ {
/*    */   boolean acceptOnMatch = true;
/*    */   Level levelToMatch;
/*    */   
/*    */   public void setLevelToMatch(String level) {
/* 53 */     this.levelToMatch = OptionConverter.toLevel(level, null);
/*    */   }
/*    */   
/*    */   public String getLevelToMatch() {
/* 57 */     return (this.levelToMatch == null) ? null : this.levelToMatch.toString();
/*    */   }
/*    */   
/*    */   public void setAcceptOnMatch(boolean acceptOnMatch) {
/* 61 */     this.acceptOnMatch = acceptOnMatch;
/*    */   }
/*    */   
/*    */   public boolean getAcceptOnMatch() {
/* 65 */     return this.acceptOnMatch;
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
/*    */   public int decide(LoggingEvent event) {
/* 79 */     if (this.levelToMatch == null) {
/* 80 */       return 0;
/*    */     }
/*    */     
/* 83 */     boolean matchOccured = false;
/* 84 */     if (this.levelToMatch.equals(event.getLevel())) {
/* 85 */       matchOccured = true;
/*    */     }
/*    */     
/* 88 */     if (matchOccured) {
/* 89 */       if (this.acceptOnMatch) {
/* 90 */         return 1;
/*    */       }
/* 92 */       return -1;
/*    */     } 
/* 94 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/LevelMatchFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */