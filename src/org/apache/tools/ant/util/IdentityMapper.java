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
/*    */ public class IdentityMapper
/*    */   implements FileNameMapper
/*    */ {
/*    */   public void setFrom(String from) {}
/*    */   
/*    */   public void setTo(String to) {}
/*    */   
/*    */   public String[] mapFileName(String sourceFileName) {
/* 53 */     if (sourceFileName == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 59 */       return null;
/*    */     }
/* 61 */     return new String[] { sourceFileName };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/IdentityMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */