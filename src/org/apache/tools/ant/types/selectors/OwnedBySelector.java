/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.attribute.UserPrincipal;
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
/*    */ 
/*    */ 
/*    */ public class OwnedBySelector
/*    */   implements FileSelector
/*    */ {
/*    */   private String owner;
/*    */   private boolean followSymlinks = true;
/*    */   
/*    */   public void setOwner(String owner) {
/* 51 */     this.owner = owner;
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
/* 64 */     if (this.owner == null) {
/* 65 */       throw new BuildException("the owner attribute is required");
/*    */     }
/* 67 */     if (file != null) {
/*    */       
/*    */       try {
/* 70 */         UserPrincipal user = this.followSymlinks ? Files.getOwner(file.toPath(), new LinkOption[0]) : Files.getOwner(file.toPath(), new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 71 */         return (user != null && this.owner.equals(user.getName()));
/* 72 */       } catch (UnsupportedOperationException|java.io.IOException unsupportedOperationException) {}
/*    */     }
/*    */ 
/*    */     
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/OwnedBySelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */