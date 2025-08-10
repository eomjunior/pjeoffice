/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.attribute.GroupPrincipal;
/*    */ import java.nio.file.attribute.PosixFileAttributes;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class PosixGroupSelector
/*    */   implements FileSelector
/*    */ {
/*    */   private String group;
/*    */   private boolean followSymlinks = true;
/*    */   
/*    */   public void setGroup(String group) {
/* 51 */     this.group = group;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFollowSymlinks(boolean followSymlinks) {
/* 59 */     this.followSymlinks = followSymlinks;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 64 */     if (this.group == null) {
/* 65 */       throw new BuildException("the group attribute is required");
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 70 */       GroupPrincipal actualGroup = this.followSymlinks ? ((PosixFileAttributes)Files.<PosixFileAttributes>readAttributes(file.toPath(), PosixFileAttributes.class, new LinkOption[0])).group() : ((PosixFileAttributes)Files.<PosixFileAttributes>readAttributes(file.toPath(), PosixFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })).group();
/* 71 */       return (actualGroup != null && actualGroup.getName().equals(this.group));
/* 72 */     } catch (IOException iOException) {
/*    */ 
/*    */       
/* 75 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/PosixGroupSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */