/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Java6FileApiPermissionsStrategy
/*    */   implements ZTFilePermissionsStrategy
/*    */ {
/* 13 */   private final Method canExecuteMethod = ZTZipReflectionUtil.getDeclaredMethod(File.class, "canExecute", new Class[0]);
/* 14 */   private final Method setExecutableMethod = ZTZipReflectionUtil.getDeclaredMethod(File.class, "setExecutable", new Class[] { boolean.class, boolean.class });
/* 15 */   private final Method setReadableMethod = ZTZipReflectionUtil.getDeclaredMethod(File.class, "setReadable", new Class[] { boolean.class, boolean.class });
/* 16 */   private final Method setWritableMethod = ZTZipReflectionUtil.getDeclaredMethod(File.class, "setWritable", new Class[] { boolean.class, boolean.class });
/*    */ 
/*    */   
/*    */   public ZTFilePermissions getPermissions(File file) {
/* 20 */     ZTFilePermissions permissions = new ZTFilePermissions();
/*    */     
/* 22 */     permissions.setDirectory(file.isDirectory());
/*    */     
/* 24 */     if (canExecute(file))
/*    */     {
/* 26 */       permissions.setOwnerCanExecute(true);
/*    */     }
/*    */     
/* 29 */     if (file.canWrite()) {
/*    */ 
/*    */       
/* 32 */       permissions.setOwnerCanWrite(true);
/* 33 */       if (file.isDirectory()) {
/* 34 */         permissions.setGroupCanWrite(true);
/* 35 */         permissions.setOthersCanWrite(true);
/*    */       } 
/*    */     } 
/*    */     
/* 39 */     if (file.canRead()) {
/* 40 */       permissions.setOwnerCanRead(true);
/* 41 */       permissions.setGroupCanRead(true);
/* 42 */       permissions.setOthersCanRead(true);
/*    */     } 
/*    */     
/* 45 */     return permissions;
/*    */   }
/*    */   
/*    */   public void setPermissions(File file, ZTFilePermissions permissions) {
/* 49 */     setExecutable(file, permissions.isOwnerCanExecute(), (!permissions.isGroupCanExecute() && !permissions.isOthersCanExecute()));
/* 50 */     setWritable(file, permissions.isOwnerCanWrite(), (!permissions.isGroupCanWrite() && !permissions.isOthersCanWrite()));
/* 51 */     setReadable(file, permissions.isOwnerCanRead(), (!permissions.isGroupCanRead() && !permissions.isOthersCanRead()));
/*    */   }
/*    */   
/*    */   private boolean setExecutable(File file, boolean executable, boolean ownerOnly) {
/* 55 */     return ((Boolean)ZTZipReflectionUtil.invoke(this.setExecutableMethod, file, new Object[] { Boolean.valueOf(executable), Boolean.valueOf(ownerOnly) })).booleanValue();
/*    */   }
/*    */   
/*    */   private boolean setWritable(File file, boolean executable, boolean ownerOnly) {
/* 59 */     return ((Boolean)ZTZipReflectionUtil.invoke(this.setWritableMethod, file, new Object[] { Boolean.valueOf(executable), Boolean.valueOf(ownerOnly) })).booleanValue();
/*    */   }
/*    */   
/*    */   private boolean setReadable(File file, boolean executable, boolean ownerOnly) {
/* 63 */     return ((Boolean)ZTZipReflectionUtil.invoke(this.setReadableMethod, file, new Object[] { Boolean.valueOf(executable), Boolean.valueOf(ownerOnly) })).booleanValue();
/*    */   }
/*    */   
/*    */   private boolean canExecute(File file) {
/* 67 */     return ((Boolean)ZTZipReflectionUtil.invoke(this.canExecuteMethod, file, new Object[0])).booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/Java6FileApiPermissionsStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */