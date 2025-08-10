/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Java7Nio2ApiPermissionsStrategy
/*     */   implements ZTFilePermissionsStrategy
/*     */ {
/*     */   private final Class<? extends Enum<?>> posixFilePermissionClass;
/*     */   private final Class<?> filesClass;
/*     */   private final Class<?> pathClass;
/*     */   private final Class<? extends Enum<?>> linkOptionClass;
/*     */   private final Enum<?>[] linkOptionsArray;
/*     */   private final Method toPathMethod;
/*     */   private final Method setPosixFilePermissionsMethod;
/*     */   private final Method getPosixFilePermissionsMethod;
/*     */   private final Object OWNER_READ;
/*     */   private final Object OWNER_WRITE;
/*     */   private final Object OWNER_EXECUTE;
/*     */   private final Object GROUP_READ;
/*     */   private final Object GROUP_WRITE;
/*     */   private final Object GROUP_EXECUTE;
/*     */   private final Object OTHERS_READ;
/*     */   private final Object OTHERS_WRITE;
/*     */   private final Object OTHERS_EXECUTE;
/*     */   
/*     */   public Java7Nio2ApiPermissionsStrategy() {
/*  38 */     if (!isPosix()) {
/*  39 */       throw new ZipException("File system does not support POSIX file attributes");
/*     */     }
/*     */     
/*  42 */     this
/*  43 */       .posixFilePermissionClass = ZTZipReflectionUtil.getClassForName("java.nio.file.attribute.PosixFilePermission", (Class)Enum.class);
/*  44 */     Enum[] arrayOfEnum = (Enum[])this.posixFilePermissionClass.getEnumConstants();
/*  45 */     this.OWNER_READ = arrayOfEnum[0];
/*  46 */     this.OWNER_WRITE = arrayOfEnum[1];
/*  47 */     this.OWNER_EXECUTE = arrayOfEnum[2];
/*  48 */     this.GROUP_READ = arrayOfEnum[3];
/*  49 */     this.GROUP_WRITE = arrayOfEnum[4];
/*  50 */     this.GROUP_EXECUTE = arrayOfEnum[5];
/*  51 */     this.OTHERS_READ = arrayOfEnum[6];
/*  52 */     this.OTHERS_WRITE = arrayOfEnum[7];
/*  53 */     this.OTHERS_EXECUTE = arrayOfEnum[8];
/*     */     
/*  55 */     this
/*  56 */       .linkOptionClass = ZTZipReflectionUtil.getClassForName("java.nio.file.LinkOption", (Class)Enum.class);
/*  57 */     this.linkOptionsArray = (Enum<?>[])Array.newInstance(this.linkOptionClass, 1);
/*  58 */     this.linkOptionsArray[0] = ((Enum[])this.linkOptionClass.getEnumConstants())[0];
/*     */     
/*  60 */     this.filesClass = ZTZipReflectionUtil.getClassForName("java.nio.file.Files", Object.class);
/*  61 */     this.pathClass = ZTZipReflectionUtil.getClassForName("java.nio.file.Path", Object.class);
/*     */     
/*  63 */     this.toPathMethod = ZTZipReflectionUtil.getDeclaredMethod(File.class, "toPath", new Class[0]);
/*  64 */     this.setPosixFilePermissionsMethod = ZTZipReflectionUtil.getDeclaredMethod(this.filesClass, "setPosixFilePermissions", new Class[] { this.pathClass, Set.class });
/*  65 */     this.getPosixFilePermissionsMethod = ZTZipReflectionUtil.getDeclaredMethod(this.filesClass, "getPosixFilePermissions", new Class[] { this.pathClass, this.linkOptionsArray.getClass() });
/*     */   }
/*     */   
/*     */   public ZTFilePermissions getPermissions(File file) {
/*  69 */     ZTFilePermissions permissions = new ZTFilePermissions();
/*  70 */     permissions.setDirectory(file.isDirectory());
/*     */     
/*  72 */     Set<?> posixFilePermissions = getPosixFilePermissions(file);
/*     */     
/*  74 */     permissions.setOwnerCanRead(posixFilePermissions.contains(this.OWNER_READ));
/*  75 */     permissions.setOwnerCanWrite(posixFilePermissions.contains(this.OWNER_WRITE));
/*  76 */     permissions.setOwnerCanExecute(posixFilePermissions.contains(this.OWNER_EXECUTE));
/*     */     
/*  78 */     permissions.setGroupCanRead(posixFilePermissions.contains(this.GROUP_READ));
/*  79 */     permissions.setGroupCanWrite(posixFilePermissions.contains(this.GROUP_WRITE));
/*  80 */     permissions.setGroupCanExecute(posixFilePermissions.contains(this.GROUP_EXECUTE));
/*     */     
/*  82 */     permissions.setOthersCanRead(posixFilePermissions.contains(this.OTHERS_READ));
/*  83 */     permissions.setOthersCanWrite(posixFilePermissions.contains(this.OTHERS_WRITE));
/*  84 */     permissions.setOthersCanExecute(posixFilePermissions.contains(this.OTHERS_EXECUTE));
/*     */     
/*  86 */     return permissions;
/*     */   }
/*     */   
/*     */   public void setPermissions(File file, ZTFilePermissions permissions) {
/*  90 */     Set<Object> set = new HashSet();
/*  91 */     addIf(permissions.isOwnerCanRead(), set, this.OWNER_READ);
/*  92 */     addIf(permissions.isOwnerCanRead(), set, this.OWNER_READ);
/*  93 */     addIf(permissions.isOwnerCanWrite(), set, this.OWNER_WRITE);
/*  94 */     addIf(permissions.isOwnerCanExecute(), set, this.OWNER_EXECUTE);
/*     */     
/*  96 */     addIf(permissions.isGroupCanRead(), set, this.GROUP_READ);
/*  97 */     addIf(permissions.isGroupCanWrite(), set, this.GROUP_WRITE);
/*  98 */     addIf(permissions.isGroupCanExecute(), set, this.GROUP_EXECUTE);
/*     */     
/* 100 */     addIf(permissions.isOthersCanRead(), set, this.OTHERS_READ);
/* 101 */     addIf(permissions.isOthersCanWrite(), set, this.OTHERS_WRITE);
/* 102 */     addIf(permissions.isOthersCanExecute(), set, this.OTHERS_EXECUTE);
/*     */     
/* 104 */     setPosixFilePermissions(file, set);
/*     */   }
/*     */   
/*     */   private <E> void addIf(boolean condition, Set<E> set, E el) {
/* 108 */     if (condition) {
/* 109 */       set.add(el);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object toPath(File file) {
/* 121 */     return ZTZipReflectionUtil.invoke(this.toPathMethod, file, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setPosixFilePermissions(File file, Set<?> set) {
/* 126 */     ZTZipReflectionUtil.invoke(this.setPosixFilePermissionsMethod, null, new Object[] { toPath(file), set });
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<?> getPosixFilePermissions(File file) {
/* 131 */     return (Set)ZTZipReflectionUtil.invoke(this.getPosixFilePermissionsMethod, null, new Object[] { toPath(file), this.linkOptionsArray });
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPosix() {
/* 136 */     Method getDefaultMethod = ZTZipReflectionUtil.getDeclaredMethod(
/* 137 */         ZTZipReflectionUtil.getClassForName("java.nio.file.FileSystems", Object.class), "getDefault", new Class[0]);
/* 138 */     Method supportedFileAttributeViewsMethod = ZTZipReflectionUtil.getDeclaredMethod(
/* 139 */         ZTZipReflectionUtil.getClassForName("java.nio.file.FileSystem", Object.class), "supportedFileAttributeViews", new Class[0]);
/*     */     
/* 141 */     Object fileSystem = ZTZipReflectionUtil.invoke(getDefaultMethod, null, new Object[0]);
/*     */     
/* 143 */     Set<String> views = (Set<String>)ZTZipReflectionUtil.invoke(supportedFileAttributeViewsMethod, fileSystem, new Object[0]);
/*     */     
/* 145 */     return views.contains("posix");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/Java7Nio2ApiPermissionsStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */