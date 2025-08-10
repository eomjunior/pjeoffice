/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.Filter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public class LevelRangeFilter
/*     */   extends Filter
/*     */ {
/*     */   boolean acceptOnMatch = false;
/*     */   Level levelMin;
/*     */   Level levelMax;
/*     */   
/*     */   public int decide(LoggingEvent event) {
/*  71 */     if (this.levelMin != null && 
/*  72 */       !event.getLevel().isGreaterOrEqual((Priority)this.levelMin))
/*     */     {
/*  74 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (this.levelMax != null && 
/*  79 */       event.getLevel().toInt() > this.levelMax.toInt())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  84 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  88 */     if (this.acceptOnMatch)
/*     */     {
/*     */       
/*  91 */       return 1;
/*     */     }
/*     */     
/*  94 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevelMax() {
/* 102 */     return this.levelMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevelMin() {
/* 109 */     return this.levelMin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAcceptOnMatch() {
/* 116 */     return this.acceptOnMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelMax(Level levelMax) {
/* 123 */     this.levelMax = levelMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevelMin(Level levelMin) {
/* 130 */     this.levelMin = levelMin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptOnMatch(boolean acceptOnMatch) {
/* 137 */     this.acceptOnMatch = acceptOnMatch;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/LevelRangeFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */