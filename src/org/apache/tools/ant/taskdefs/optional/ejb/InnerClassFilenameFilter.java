/*    */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
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
/*    */ public class InnerClassFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   private String baseClassName;
/*    */   
/*    */   InnerClassFilenameFilter(String baseclass) {
/* 34 */     int extidx = baseclass.lastIndexOf(".class");
/* 35 */     if (extidx == -1) {
/* 36 */       extidx = baseclass.length() - 1;
/*    */     }
/* 38 */     this.baseClassName = baseclass.substring(0, extidx);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(File dir, String filename) {
/* 49 */     return (filename.lastIndexOf('.') == filename.lastIndexOf(".class") && filename
/* 50 */       .indexOf(this.baseClassName + "$") == 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/InnerClassFilenameFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */