/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.util.PermissionUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetPermissions
/*     */   extends Task
/*     */ {
/*  56 */   private final Set<PosixFilePermission> permissions = EnumSet.noneOf(PosixFilePermission.class);
/*  57 */   private Resources resources = null;
/*     */   private boolean failonerror = true;
/*  59 */   private NonPosixMode nonPosixMode = NonPosixMode.fail;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum NonPosixMode
/*     */   {
/*  67 */     fail,
/*     */     
/*  69 */     pass,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     tryDosOrFail,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     tryDosOrPass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPermissions(String perms) {
/*  87 */     if (perms != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       Objects.requireNonNull(this.permissions); Arrays.<String>stream(perms.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(s -> (PosixFilePermission)Enum.<PosixFilePermission>valueOf(PosixFilePermission.class, s)).forEach(this.permissions::add);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(String octalString) {
/* 102 */     int mode = Integer.parseInt(octalString, 8);
/* 103 */     this.permissions.addAll(PermissionUtils.permissionsFromMode(mode));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failonerror) {
/* 115 */     this.failonerror = failonerror;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNonPosixMode(NonPosixMode m) {
/* 126 */     this.nonPosixMode = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 134 */     if (this.resources == null) {
/* 135 */       this.resources = new Resources();
/*     */     }
/* 137 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/* 142 */     if (this.resources == null) {
/* 143 */       throw new BuildException("At least one resource-collection is required");
/*     */     }
/* 145 */     Resource currentResource = null;
/*     */     try {
/* 147 */       for (Resource r : this.resources) {
/* 148 */         currentResource = r;
/*     */         try {
/* 150 */           PermissionUtils.setPermissions(r, this.permissions, this::posixPermissionsNotSupported);
/* 151 */         } catch (IOException ioe) {
/* 152 */           maybeThrowException(ioe, "Failed to set permissions on '%s' due to %s", new Object[] { r, ioe.getMessage() });
/*     */         } 
/*     */       } 
/* 155 */     } catch (ClassCastException cce) {
/* 156 */       maybeThrowException((Exception)null, "some specified permissions are not of type PosixFilePermission: %s", new Object[] {
/*     */             
/* 158 */             StringUtils.join(this.permissions, ", ") });
/* 159 */     } catch (SecurityException se) {
/* 160 */       maybeThrowException((Exception)null, "the SecurityManager denies role accessUserInformation or write access for SecurityManager.checkWrite for resource '%s'", new Object[] { currentResource });
/*     */     
/*     */     }
/* 163 */     catch (BuildException be) {
/*     */       
/* 165 */       maybeThrowException((Exception)be, be.getMessage(), new Object[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeThrowException(Exception exc, String msgFormat, Object... msgArgs) {
/* 170 */     String msg = String.format(msgFormat, msgArgs);
/* 171 */     if (this.failonerror) {
/* 172 */       if (exc instanceof BuildException) {
/* 173 */         throw (BuildException)exc;
/*     */       }
/* 175 */       throw new BuildException(msg, exc);
/*     */     } 
/* 177 */     log("Warning: " + msg, 0);
/*     */   }
/*     */   
/*     */   private void posixPermissionsNotSupported(Path p) {
/* 181 */     String msg = String.format("the associated path '%s' does not support the PosixFileAttributeView", new Object[] { p });
/*     */ 
/*     */     
/* 184 */     switch (this.nonPosixMode) {
/*     */       case fail:
/* 186 */         throw new BuildException(msg);
/*     */       case pass:
/* 188 */         log("Warning: " + msg, 0);
/*     */         break;
/*     */       case tryDosOrFail:
/* 191 */         tryDos(p, true);
/*     */         break;
/*     */       case tryDosOrPass:
/* 194 */         tryDos(p, false);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryDos(Path p, boolean failIfDosIsNotSupported) {
/* 200 */     log("Falling back to DosFileAttributeView");
/* 201 */     boolean readOnly = !isWritable();
/* 202 */     DosFileAttributeView view = Files.<DosFileAttributeView>getFileAttributeView(p, DosFileAttributeView.class, new java.nio.file.LinkOption[0]);
/* 203 */     if (view != null) {
/*     */       try {
/* 205 */         view.setReadOnly(readOnly);
/* 206 */       } catch (IOException ioe) {
/* 207 */         maybeThrowException(ioe, "Failed to set permissions on '%s' due to %s", new Object[] { p, ioe
/* 208 */               .getMessage() });
/* 209 */       } catch (SecurityException uoe) {
/* 210 */         maybeThrowException((Exception)null, "the SecurityManager denies role accessUserInformation or write access for SecurityManager.checkWrite for resource '%s'", new Object[] { p });
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 215 */       String msg = String.format("the associated path '%s' does not support the DosFileAttributeView", new Object[] { p });
/*     */ 
/*     */       
/* 218 */       if (failIfDosIsNotSupported) {
/* 219 */         throw new BuildException(msg);
/*     */       }
/* 221 */       log("Warning: " + msg, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isWritable() {
/* 226 */     return (this.permissions.contains(PosixFilePermission.OWNER_WRITE) || this.permissions
/* 227 */       .contains(PosixFilePermission.GROUP_WRITE) || this.permissions
/* 228 */       .contains(PosixFilePermission.OTHERS_WRITE));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/SetPermissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */