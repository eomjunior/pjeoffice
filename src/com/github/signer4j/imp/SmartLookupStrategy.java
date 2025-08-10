/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class SmartLookupStrategy
/*    */   extends StrategyWrapper
/*    */ {
/*    */   public SmartLookupStrategy() {
/* 35 */     this(SystemSupport.getDefault());
/*    */   }
/*    */   
/*    */   public SmartLookupStrategy(SystemSupport support) {
/* 39 */     super(LookupStrategy.notDuplicated(support)
/* 40 */         .more(new EnvironmentStrategy(support)));
/*    */   }
/*    */ 
/*    */   
/*    */   public SmartLookupStrategy(SystemSupport support, File config) {
/* 45 */     super(LookupStrategy.notDuplicated(support)
/* 46 */         .more(new EnvironmentStrategy(support))
/* 47 */         .more(new FileStrategy(config)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SmartLookupStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */