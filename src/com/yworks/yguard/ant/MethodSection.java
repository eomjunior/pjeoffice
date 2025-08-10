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
/*    */ public class MethodSection
/*    */   extends PatternMatchedClassesSection
/*    */   implements Mappable
/*    */ {
/*    */   private String name;
/*    */   private String className;
/*    */   private String mapTo;
/*    */   
/*    */   public void setName(String name) {
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClass(String name) {
/* 37 */     this.className = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMap(String mapTo) {
/* 46 */     this.mapTo = mapTo;
/*    */   }
/*    */   
/*    */   public void addEntries(Collection<YGuardRule> entries, String className) {
/* 50 */     String[] method = ObfuscatorTask.toNativeMethod(this.name);
/* 51 */     String name = ObfuscatorTask.toNativeClass(className) + '/' + method[0];
/* 52 */     String descriptor = method[1];
/* 53 */     YGuardRule entry = new YGuardRule(3, name, descriptor);
/* 54 */     entries.add(entry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addMapEntries(Collection<YGuardRule> entries) {
/* 59 */     String[] method = ObfuscatorTask.toNativeMethod(this.name);
/* 60 */     String name = ObfuscatorTask.toNativeClass(this.className) + '/' + method[0];
/* 61 */     String descriptor = method[1];
/* 62 */     YGuardRule entry = new YGuardRule(7, name, descriptor);
/* 63 */     entry.obfName = this.mapTo;
/* 64 */     entries.add(entry);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 73 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 82 */     return this.className;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/MethodSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */