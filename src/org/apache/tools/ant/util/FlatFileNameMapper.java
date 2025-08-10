/*    */ package org.apache.tools.ant.util;
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
/*    */ public class FlatFileNameMapper
/*    */   implements FileNameMapper
/*    */ {
/*    */   public void setFrom(String from) {}
/*    */   
/*    */   public void setTo(String to) {}
/*    */   
/*    */   public String[] mapFileName(String sourceFileName) {
/* 58 */     (new String[1])[0] = (new File(sourceFileName)).getName(); return (sourceFileName == null) ? null : new String[1];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/FlatFileNameMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */