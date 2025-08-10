/*    */ package com.yworks.yguard.obf;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObfuscationConfig
/*    */ {
/* 11 */   public static String annotationClassName = "com/yworks/util/annotation/Obfuscation";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean applyToMembers;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean exclude;
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final ObfuscationConfig DEFAULT = new ObfuscationConfig(false, false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObfuscationConfig(boolean exclude, boolean applyToMembers) {
/* 34 */     this.applyToMembers = applyToMembers;
/* 35 */     this.exclude = exclude;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/ObfuscationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */