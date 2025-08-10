/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ import java.io.File;
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
/*     */ class ZTFilePermissionsUtil
/*     */ {
/*     */   private static final int OWNER_READ_FLAG = 256;
/*     */   private static final int OWNER_WRITE_FLAG = 128;
/*     */   private static final int OWNER_EXECUTE_FLAG = 64;
/*     */   private static final int GROUP_READ_FLAG = 32;
/*     */   private static final int GROUP_WRITE_FLAG = 16;
/*     */   private static final int GROUP_EXECUTE_FLAG = 8;
/*     */   private static final int OTHERS_READ_FLAG = 4;
/*     */   private static final int OTHERS_WRITE_FLAG = 2;
/*     */   private static final int OTHERS_EXECUTE_FLAG = 1;
/*     */   
/*     */   static ZTFilePermissionsStrategy getDefaultStategy() {
/*  33 */     return DEFAULT_STRATEGY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int toPosixFileMode(ZTFilePermissions permissions) {
/*  44 */     int mode = 0;
/*     */     
/*  46 */     mode |= addFlag(permissions.isOwnerCanExecute(), 64);
/*  47 */     mode |= addFlag(permissions.isGroupCanExecute(), 8);
/*  48 */     mode |= addFlag(permissions.isOthersCanExecute(), 1);
/*     */     
/*  50 */     mode |= addFlag(permissions.isOwnerCanWrite(), 128);
/*  51 */     mode |= addFlag(permissions.isGroupCanWrite(), 16);
/*  52 */     mode |= addFlag(permissions.isOthersCanWrite(), 2);
/*     */     
/*  54 */     mode |= addFlag(permissions.isOwnerCanRead(), 256);
/*  55 */     mode |= addFlag(permissions.isGroupCanRead(), 32);
/*  56 */     mode |= addFlag(permissions.isOthersCanRead(), 4);
/*     */     
/*  58 */     return mode;
/*     */   }
/*     */   
/*     */   private static int addFlag(boolean condition, int flag) {
/*  62 */     return condition ? flag : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZTFilePermissions fromPosixFileMode(int mode) {
/*  72 */     ZTFilePermissions permissions = new ZTFilePermissions();
/*     */     
/*  74 */     permissions.setOwnerCanExecute(((mode & 0x40) > 0));
/*  75 */     permissions.setGroupCanExecute(((mode & 0x8) > 0));
/*  76 */     permissions.setOthersCanExecute(((mode & 0x1) > 0));
/*     */     
/*  78 */     permissions.setOwnerCanWrite(((mode & 0x80) > 0));
/*  79 */     permissions.setGroupCanWrite(((mode & 0x10) > 0));
/*  80 */     permissions.setOthersCanWrite(((mode & 0x2) > 0));
/*     */     
/*  82 */     permissions.setOwnerCanRead(((mode & 0x100) > 0));
/*  83 */     permissions.setGroupCanRead(((mode & 0x20) > 0));
/*  84 */     permissions.setOthersCanRead(((mode & 0x4) > 0));
/*     */     
/*  86 */     return permissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final ZTFilePermissionsStrategy NOP_STRATEGY = new ZTFilePermissionsStrategy()
/*     */     {
/*     */       public void setPermissions(File file, ZTFilePermissions permissions) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public ZTFilePermissions getPermissions(File file) {
/*  99 */         return null;
/*     */       }
/*     */     };
/*     */   
/* 103 */   private static final ZTFilePermissionsStrategy DEFAULT_STRATEGY = fetchDefaultStrategy();
/*     */   
/*     */   private static ZTFilePermissionsStrategy fetchDefaultStrategy() {
/* 106 */     ZTFilePermissionsStrategy strategy = tryInstantiateStrategy((Class)Java7Nio2ApiPermissionsStrategy.class);
/*     */     
/* 108 */     if (strategy == null) {
/* 109 */       strategy = tryInstantiateStrategy((Class)Java6FileApiPermissionsStrategy.class);
/*     */     }
/*     */     
/* 112 */     if (strategy == null) {
/* 113 */       strategy = NOP_STRATEGY;
/*     */     }
/*     */     
/* 116 */     return strategy;
/*     */   }
/*     */   
/*     */   private static ZTFilePermissionsStrategy tryInstantiateStrategy(Class<? extends ZTFilePermissionsStrategy> clazz) {
/*     */     try {
/* 121 */       return clazz.newInstance();
/*     */     }
/* 123 */     catch (Exception e) {
/*     */       
/* 125 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZTFilePermissionsUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */