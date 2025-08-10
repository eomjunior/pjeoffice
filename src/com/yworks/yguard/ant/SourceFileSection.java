/*    */ package com.yworks.yguard.ant;
/*    */ 
/*    */ import com.yworks.common.ant.YGuardBaseTask;
/*    */ import com.yworks.yguard.obf.YGuardRule;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SourceFileSection
/*    */   extends PatternMatchedClassesSection
/*    */   implements Mappable
/*    */ {
/*    */   protected final YGuardBaseTask obfuscatorTask;
/*    */   
/*    */   public SourceFileSection(YGuardBaseTask obfuscatorTask) {
/* 22 */     this.obfuscatorTask = obfuscatorTask;
/* 23 */     this.allowMatchAllPatternSet = true;
/*    */   }
/*    */   
/*    */   public void addEntries(Collection<YGuardRule> entries, String className) {
/* 27 */     YGuardRule rule = createRule(className);
/* 28 */     entries.add(rule);
/*    */   }
/*    */   
/*    */   private YGuardRule createRule(String className) {
/* 32 */     if (this.properties.containsKey("mapping")) {
/* 33 */       YGuardRule sourceRule = new YGuardRule(8, className);
/* 34 */       sourceRule.obfName = (String)this.properties.get("mapping");
/* 35 */       return sourceRule;
/*    */     } 
/* 37 */     YGuardRule rule = new YGuardRule(10, "SourceFile", className);
/* 38 */     return rule;
/*    */   }
/*    */   
/*    */   public void addMapEntries(Collection entries) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/SourceFileSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */