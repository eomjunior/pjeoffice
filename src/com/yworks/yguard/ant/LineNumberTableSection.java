/*    */ package com.yworks.yguard.ant;
/*    */ 
/*    */ import com.yworks.common.ant.YGuardBaseTask;
/*    */ import com.yworks.yguard.ObfuscatorTask;
/*    */ import com.yworks.yguard.obf.LineNumberTableMapper;
/*    */ import com.yworks.yguard.obf.YGuardRule;
/*    */ import com.yworks.yguard.obf.classfile.LineNumberTableAttrInfo;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LineNumberTableSection
/*    */   extends PatternMatchedClassesSection
/*    */   implements Mappable
/*    */ {
/*    */   protected final YGuardBaseTask obfuscatorTask;
/*    */   private LineNumberTableMapper mapper;
/*    */   
/*    */   public LineNumberTableSection(YGuardBaseTask obfuscatorTask) {
/* 25 */     this.obfuscatorTask = obfuscatorTask;
/* 26 */     this.allowMatchAllPatternSet = true;
/*    */   }
/*    */   
/*    */   public void addEntries(Collection<YGuardRule> entries, String className) {
/* 30 */     YGuardRule rule = createRule(className);
/* 31 */     entries.add(rule);
/*    */   }
/*    */ 
/*    */   
/*    */   private YGuardRule createRule(String className) {
/* 36 */     if (this.mapper == null) {
/* 37 */       this.mapper = createMapper();
/*    */     }
/* 39 */     return new YGuardRule(className, this.mapper);
/*    */   }
/*    */   private LineNumberTableMapper createMapper() {
/*    */     ObfuscatorTask.MyLineNumberTableMapper myLineNumberTableMapper;
/* 43 */     LineNumberTableMapper lineNumberTableMapper1, lntMapper = null;
/* 44 */     if (this.properties.containsKey("mapping-scheme")) {
/* 45 */       String ms = (String)this.properties.get("mapping-scheme");
/* 46 */       if ("squeeze".equals(ms)) {
/* 47 */         ObfuscatorTask.LineNumberSqueezer lineNumberSqueezer = new ObfuscatorTask.LineNumberSqueezer();
/* 48 */       } else if ("scramble".equals(ms)) {
/* 49 */         long saltValue = (long)(Math.random() * 4242.0D);
/* 50 */         if (this.properties.containsKey("scrambling-salt")) {
/* 51 */           String salt = (String)this.properties.get("scrambling-salt");
/*    */           try {
/* 53 */             saltValue = Long.parseLong(salt);
/* 54 */           } catch (NumberFormatException numberFormatException) {}
/*    */         } 
/*    */ 
/*    */         
/* 58 */         myLineNumberTableMapper = new ObfuscatorTask.MyLineNumberTableMapper(saltValue);
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 64 */     if (myLineNumberTableMapper == null) {
/* 65 */       lineNumberTableMapper1 = new LineNumberTableMapper() {
/*    */           public boolean mapLineNumberTable(String className, String methodName, String methodSignature, LineNumberTableAttrInfo lineNumberTable) {
/* 67 */             return true;
/*    */           }
/*    */ 
/*    */ 
/*    */           
/*    */           public void logProperties(PrintWriter pw) {}
/*    */         };
/*    */     }
/* 75 */     return lineNumberTableMapper1;
/*    */   }
/*    */   
/*    */   public void addMapEntries(Collection entries) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/LineNumberTableSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */