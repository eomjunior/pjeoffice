/*    */ package com.yworks.yguard.ant;
/*    */ 
/*    */ import com.yworks.common.ant.YGuardBaseTask;
/*    */ import com.yworks.yguard.obf.YGuardRule;
/*    */ import java.util.Collection;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributesSection
/*    */   extends PatternMatchedClassesSection
/*    */   implements Mappable
/*    */ {
/*    */   protected final YGuardBaseTask obfuscatorTask;
/*    */   private String attributes;
/*    */   
/*    */   public AttributesSection(YGuardBaseTask obfuscatorTask) {
/* 22 */     this.obfuscatorTask = obfuscatorTask;
/* 23 */     this.allowMatchAllPatternSet = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String attributes) {
/* 34 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public void addEntries(Collection<YGuardRule> entries, String className) {
/* 38 */     StringTokenizer st = new StringTokenizer(this.attributes, ", ", false);
/* 39 */     while (st.hasMoreTokens()) {
/* 40 */       String token = st.nextToken().trim();
/* 41 */       YGuardRule entry = new YGuardRule(10, token, className);
/* 42 */       entries.add(entry);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMapEntries(Collection entries) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAttributes() {
/* 56 */     return this.attributes;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/AttributesSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */