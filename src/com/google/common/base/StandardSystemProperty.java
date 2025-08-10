/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public enum StandardSystemProperty
/*     */ {
/*  33 */   JAVA_VERSION("java.version"),
/*     */ 
/*     */   
/*  36 */   JAVA_VENDOR("java.vendor"),
/*     */ 
/*     */   
/*  39 */   JAVA_VENDOR_URL("java.vendor.url"),
/*     */ 
/*     */   
/*  42 */   JAVA_HOME("java.home"),
/*     */ 
/*     */   
/*  45 */   JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
/*     */ 
/*     */   
/*  48 */   JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
/*     */ 
/*     */   
/*  51 */   JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
/*     */ 
/*     */   
/*  54 */   JAVA_VM_VERSION("java.vm.version"),
/*     */ 
/*     */   
/*  57 */   JAVA_VM_VENDOR("java.vm.vendor"),
/*     */ 
/*     */   
/*  60 */   JAVA_VM_NAME("java.vm.name"),
/*     */ 
/*     */   
/*  63 */   JAVA_SPECIFICATION_VERSION("java.specification.version"),
/*     */ 
/*     */   
/*  66 */   JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
/*     */ 
/*     */   
/*  69 */   JAVA_SPECIFICATION_NAME("java.specification.name"),
/*     */ 
/*     */   
/*  72 */   JAVA_CLASS_VERSION("java.class.version"),
/*     */ 
/*     */   
/*  75 */   JAVA_CLASS_PATH("java.class.path"),
/*     */ 
/*     */   
/*  78 */   JAVA_LIBRARY_PATH("java.library.path"),
/*     */ 
/*     */   
/*  81 */   JAVA_IO_TMPDIR("java.io.tmpdir"),
/*     */ 
/*     */   
/*  84 */   JAVA_COMPILER("java.compiler"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   JAVA_EXT_DIRS("java.ext.dirs"),
/*     */ 
/*     */ 
/*     */   
/*  98 */   OS_NAME("os.name"),
/*     */ 
/*     */   
/* 101 */   OS_ARCH("os.arch"),
/*     */ 
/*     */   
/* 104 */   OS_VERSION("os.version"),
/*     */ 
/*     */   
/* 107 */   FILE_SEPARATOR("file.separator"),
/*     */ 
/*     */   
/* 110 */   PATH_SEPARATOR("path.separator"),
/*     */ 
/*     */   
/* 113 */   LINE_SEPARATOR("line.separator"),
/*     */ 
/*     */   
/* 116 */   USER_NAME("user.name"),
/*     */ 
/*     */   
/* 119 */   USER_HOME("user.home"),
/*     */ 
/*     */   
/* 122 */   USER_DIR("user.dir");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   StandardSystemProperty(String key) {
/* 127 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String key() {
/* 132 */     return this.key;
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
/*     */   @CheckForNull
/*     */   public String value() {
/* 160 */     return System.getProperty(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return key() + "=" + value();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/StandardSystemProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */