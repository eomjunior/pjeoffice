/*    */ package com.yworks.yguard.ant;
/*    */ 
/*    */ import com.yworks.yguard.ObfuscatorTask;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldSection
/*    */   extends PatternMatchedClassesSection
/*    */   implements Mappable
/*    */ {
/*    */   private String name;
/*    */   private String className;
/*    */   private String mapTo;
/*    */   
/*    */   public void setName(String name) {
/* 29 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClass(String name) {
/* 38 */     this.className = name;
/*    */   }
/*    */   
/*    */   public void addEntries(Collection<YGuardRule> entries, String className) {
/* 42 */     String lname = ObfuscatorTask.toNativeClass(className) + '/' + this.name;
/* 43 */     YGuardRule entry = new YGuardRule(2, lname);
/* 44 */     entries.add(entry);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMap(String map) {
/* 53 */     this.mapTo = map;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addMapEntries(Collection<YGuardRule> entries) {
/* 58 */     String lname = ObfuscatorTask.toNativeClass(this.className) + '/' + this.name;
/* 59 */     YGuardRule entry = new YGuardRule(6, lname);
/* 60 */     entry.obfName = this.mapTo;
/* 61 */     entries.add(entry);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 70 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 79 */     return this.className;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/FieldSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */