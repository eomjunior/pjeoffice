/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class TabSettings
/*     */ {
/*     */   public static final float DEFAULT_TAB_INTERVAL = 36.0F;
/*     */   
/*     */   public static TabStop getTabStopNewInstance(float currentPosition, TabSettings tabSettings) {
/*  53 */     if (tabSettings != null)
/*  54 */       return tabSettings.getTabStopNewInstance(currentPosition); 
/*  55 */     return TabStop.newInstance(currentPosition, 36.0F);
/*     */   }
/*     */   
/*  58 */   private List<TabStop> tabStops = new ArrayList<TabStop>();
/*  59 */   private float tabInterval = 36.0F;
/*     */   
/*     */   public TabSettings() {}
/*     */   
/*     */   public TabSettings(List<TabStop> tabStops) {
/*  64 */     this.tabStops = tabStops;
/*     */   }
/*     */   
/*     */   public TabSettings(float tabInterval) {
/*  68 */     this.tabInterval = tabInterval;
/*     */   }
/*     */   
/*     */   public TabSettings(List<TabStop> tabStops, float tabInterval) {
/*  72 */     this.tabStops = tabStops;
/*  73 */     this.tabInterval = tabInterval;
/*     */   }
/*     */   
/*     */   public List<TabStop> getTabStops() {
/*  77 */     return this.tabStops;
/*     */   }
/*     */   
/*     */   public void setTabStops(List<TabStop> tabStops) {
/*  81 */     this.tabStops = tabStops;
/*     */   }
/*     */   
/*     */   public float getTabInterval() {
/*  85 */     return this.tabInterval;
/*     */   }
/*     */   
/*     */   public void setTabInterval(float tabInterval) {
/*  89 */     this.tabInterval = tabInterval;
/*     */   }
/*     */   
/*     */   public TabStop getTabStopNewInstance(float currentPosition) {
/*  93 */     TabStop tabStop = null;
/*  94 */     if (this.tabStops != null) {
/*  95 */       for (TabStop currentTabStop : this.tabStops) {
/*  96 */         if ((currentTabStop.getPosition() - currentPosition) > 0.001D) {
/*  97 */           tabStop = new TabStop(currentTabStop);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 103 */     if (tabStop == null) {
/* 104 */       tabStop = TabStop.newInstance(currentPosition, this.tabInterval);
/*     */     }
/*     */     
/* 107 */     return tabStop;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/TabSettings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */