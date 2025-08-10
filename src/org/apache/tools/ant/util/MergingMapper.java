/*    */ package org.apache.tools.ant.util;
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
/*    */ public class MergingMapper
/*    */   implements FileNameMapper
/*    */ {
/* 31 */   protected String[] mergedFile = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MergingMapper() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MergingMapper(String to) {
/* 42 */     setTo(to);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFrom(String from) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTo(String to) {
/* 59 */     this.mergedFile = new String[] { to };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] mapFileName(String sourceFileName) {
/* 69 */     return this.mergedFile;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/MergingMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */