/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.resources.FileProvider;
/*    */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
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
/*    */ public interface FileSelector
/*    */   extends ResourceSelector
/*    */ {
/*    */   boolean isSelected(File paramFile1, String paramString, File paramFile2) throws BuildException;
/*    */   
/*    */   default boolean isSelected(Resource r) {
/* 57 */     return ((Boolean)r.asOptional(FileProvider.class).map(FileProvider::getFile)
/* 58 */       .map(f -> Boolean.valueOf(isSelected(null, null, f))).orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/FileSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */