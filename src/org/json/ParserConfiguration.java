/*     */ package org.json;
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
/*     */ public class ParserConfiguration
/*     */ {
/*     */   public static final int UNDEFINED_MAXIMUM_NESTING_DEPTH = -1;
/*     */   public static final int DEFAULT_MAXIMUM_NESTING_DEPTH = 512;
/*     */   protected boolean keepStrings;
/*     */   protected int maxNestingDepth;
/*     */   
/*     */   public ParserConfiguration() {
/*  33 */     this.keepStrings = false;
/*  34 */     this.maxNestingDepth = 512;
/*     */   }
/*     */   
/*     */   protected ParserConfiguration(boolean keepStrings, int maxNestingDepth) {
/*  38 */     this.keepStrings = keepStrings;
/*  39 */     this.maxNestingDepth = maxNestingDepth;
/*     */   }
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
/*     */   protected ParserConfiguration clone() {
/*  52 */     return new ParserConfiguration(this.keepStrings, this.maxNestingDepth);
/*     */   }
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
/*     */   public boolean isKeepStrings() {
/*  65 */     return this.keepStrings;
/*     */   }
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
/*     */   public <T extends ParserConfiguration> T withKeepStrings(boolean newVal) {
/*  78 */     ParserConfiguration parserConfiguration = clone();
/*  79 */     parserConfiguration.keepStrings = newVal;
/*  80 */     return (T)parserConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxNestingDepth() {
/*  89 */     return this.maxNestingDepth;
/*     */   }
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
/*     */   public <T extends ParserConfiguration> T withMaxNestingDepth(int maxNestingDepth) {
/* 102 */     ParserConfiguration parserConfiguration = clone();
/*     */     
/* 104 */     if (maxNestingDepth > -1) {
/* 105 */       parserConfiguration.maxNestingDepth = maxNestingDepth;
/*     */     } else {
/* 107 */       parserConfiguration.maxNestingDepth = -1;
/*     */     } 
/*     */     
/* 110 */     return (T)parserConfiguration;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/ParserConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */