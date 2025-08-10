/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.attribute.PosixFilePermissions;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.util.PermissionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PosixPermissionsSelector
/*    */   implements FileSelector
/*    */ {
/*    */   private String permissions;
/*    */   private boolean followSymlinks = true;
/*    */   
/*    */   public void setPermissions(String permissions) {
/* 50 */     if (permissions.length() == 3 && permissions.matches("^[0-7]+$")) {
/* 51 */       this.permissions = PosixFilePermissions.toString(
/* 52 */           PermissionUtils.permissionsFromMode(Integer.parseInt(permissions, 8)));
/*    */       
/*    */       return;
/*    */     } 
/*    */     try {
/* 57 */       this.permissions = PosixFilePermissions.toString(PosixFilePermissions.fromString(permissions));
/* 58 */     } catch (IllegalArgumentException ex) {
/* 59 */       throw new BuildException("the permissions attribute " + permissions + " is invalid", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFollowSymlinks(boolean followSymlinks) {
/* 69 */     this.followSymlinks = followSymlinks;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 74 */     if (this.permissions == null) {
/* 75 */       throw new BuildException("the permissions attribute is required");
/*    */     }
/*    */     try {
/* 78 */       return PosixFilePermissions.toString(this.followSymlinks ? 
/* 79 */           Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]) : 
/* 80 */           Files.getPosixFilePermissions(file.toPath(), new LinkOption[] { LinkOption.NOFOLLOW_LINKS
/* 81 */             })).equals(this.permissions);
/* 82 */     } catch (IOException iOException) {
/*    */ 
/*    */       
/* 85 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/PosixPermissionsSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */