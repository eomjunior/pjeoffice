/*    */ package org.apache.tools.ant.types.optional;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.apache.tools.ant.util.FileNameMapper;
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
/*    */ public class ScriptMapper
/*    */   extends AbstractScriptComponent
/*    */   implements FileNameMapper
/*    */ {
/*    */   private ArrayList<String> files;
/*    */   
/*    */   public void setFrom(String from) {}
/*    */   
/*    */   public void setTo(String to) {}
/*    */   
/*    */   public void clear() {
/* 55 */     this.files = new ArrayList<>(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addMappedName(String mapping) {
/* 63 */     this.files.add(mapping);
/*    */   }
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
/*    */   public String[] mapFileName(String sourceFileName) {
/* 82 */     initScriptRunner();
/* 83 */     getRunner().addBean("source", sourceFileName);
/* 84 */     clear();
/* 85 */     executeScript("ant_mapper");
/* 86 */     if (this.files.isEmpty()) {
/* 87 */       return null;
/*    */     }
/* 89 */     return this.files.<String>toArray(new String[0]);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/ScriptMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */