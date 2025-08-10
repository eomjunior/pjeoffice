/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.types.ResourceCollection;
/*    */ import org.apache.tools.ant.types.resources.FileResource;
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
/*    */ public class LoadFile
/*    */   extends LoadResource
/*    */ {
/*    */   public final void setSrcFile(File srcFile) {
/* 39 */     addConfigured((ResourceCollection)new FileResource(srcFile));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/LoadFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */