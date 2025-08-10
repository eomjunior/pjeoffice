/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.Files;
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
/*    */ public class ExecutableSelector
/*    */   implements FileSelector
/*    */ {
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 37 */     return (file != null && Files.isExecutable(file.toPath()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/ExecutableSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */