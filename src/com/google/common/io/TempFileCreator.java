/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.StandardSystemProperty;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.AclEntry;
/*     */ import java.nio.file.attribute.AclEntryFlag;
/*     */ import java.nio.file.attribute.AclEntryPermission;
/*     */ import java.nio.file.attribute.AclEntryType;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ abstract class TempFileCreator
/*     */ {
/*  56 */   static final TempFileCreator INSTANCE = pickSecureCreator();
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
/*     */   private static TempFileCreator pickSecureCreator() {
/*     */     try {
/*  69 */       Class.forName("java.nio.file.Path");
/*  70 */       return new JavaNioCreator();
/*  71 */     } catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  76 */         int version = ((Integer)Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null)).intValue();
/*     */         
/*  78 */         int jellyBean = ((Integer)Class.forName("android.os.Build$VERSION_CODES").getField("JELLY_BEAN").get(null)).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  83 */         if (version < jellyBean) {
/*  84 */           return new ThrowingCreator();
/*     */         
/*     */         }
/*     */       
/*     */       }
/*  89 */       catch (NoSuchFieldException e) {
/*     */         
/*  91 */         return new ThrowingCreator();
/*  92 */       } catch (ClassNotFoundException e) {
/*     */         
/*  94 */         return new ThrowingCreator();
/*  95 */       } catch (IllegalAccessException e) {
/*     */         
/*  97 */         return new ThrowingCreator();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 103 */       return new JavaIoCreator();
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
/*     */   @IgnoreJRERequirement
/*     */   @VisibleForTesting
/*     */   static void testMakingUserPermissionsFromScratch() throws IOException {
/* 117 */     FileAttribute<?> unused = JavaNioCreator.userPermissions().get();
/*     */   } @IgnoreJRERequirement
/*     */   private static interface PermissionSupplier {
/*     */     FileAttribute<?> get() throws IOException; }
/*     */   @IgnoreJRERequirement
/*     */   private static final class JavaNioCreator extends TempFileCreator { private static final PermissionSupplier filePermissions;
/*     */     File createTempDir() {
/*     */       try {
/* 125 */         return Files.createTempDirectory(
/* 126 */             Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value(), new String[0]), null, (FileAttribute<?>[])new FileAttribute[] { directoryPermissions.get()
/* 127 */             }).toFile();
/* 128 */       } catch (IOException e) {
/* 129 */         throw new IllegalStateException("Failed to create directory", e);
/*     */       } 
/*     */     }
/*     */     private static final PermissionSupplier directoryPermissions;
/*     */     private JavaNioCreator() {}
/*     */     File createTempFile(String prefix) throws IOException {
/* 135 */       return Files.createTempFile(
/* 136 */           Paths.get(StandardSystemProperty.JAVA_IO_TMPDIR.value(), new String[0]), prefix, null, (FileAttribute<?>[])new FileAttribute[] { filePermissions
/*     */ 
/*     */             
/* 139 */             .get()
/* 140 */           }).toFile();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 152 */       Set<String> views = FileSystems.getDefault().supportedFileAttributeViews();
/* 153 */       if (views.contains("posix")) {
/* 154 */         filePermissions = (() -> PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------")));
/* 155 */         directoryPermissions = (() -> PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
/* 156 */       } else if (views.contains("acl")) {
/* 157 */         filePermissions = directoryPermissions = userPermissions();
/*     */       } else {
/* 159 */         filePermissions = directoryPermissions = (() -> {
/*     */             throw new IOException("unrecognized FileSystem type " + FileSystems.getDefault());
/*     */           });
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static PermissionSupplier userPermissions() {
/*     */       try {
/* 172 */         UserPrincipal user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(getUsername());
/*     */         
/* 174 */         final ImmutableList<AclEntry> acl = ImmutableList.of(
/* 175 */             AclEntry.newBuilder()
/* 176 */             .setType(AclEntryType.ALLOW)
/* 177 */             .setPrincipal(user)
/* 178 */             .setPermissions(EnumSet.allOf(AclEntryPermission.class))
/* 179 */             .setFlags(new AclEntryFlag[] { AclEntryFlag.DIRECTORY_INHERIT, AclEntryFlag.FILE_INHERIT
/* 180 */               }).build());
/* 181 */         FileAttribute<ImmutableList<AclEntry>> attribute = new FileAttribute<ImmutableList<AclEntry>>()
/*     */           {
/*     */             public String name()
/*     */             {
/* 185 */               return "acl:acl";
/*     */             }
/*     */ 
/*     */             
/*     */             public ImmutableList<AclEntry> value() {
/* 190 */               return acl;
/*     */             }
/*     */           };
/* 193 */         return () -> attribute;
/* 194 */       } catch (IOException e) {
/*     */         
/* 196 */         return () -> {
/*     */             throw new IOException("Could not find user", e);
/*     */           };
/*     */       } 
/*     */     }
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     private static interface PermissionSupplier {
/*     */       FileAttribute<?> get() throws IOException;
/*     */     }
/*     */     
/*     */     private static String getUsername() {
/* 208 */       String fromSystemProperty = Objects.<String>requireNonNull(StandardSystemProperty.USER_NAME.value());
/*     */       
/*     */       try {
/* 211 */         Class<?> processHandleClass = Class.forName("java.lang.ProcessHandle");
/* 212 */         Class<?> processHandleInfoClass = Class.forName("java.lang.ProcessHandle$Info");
/* 213 */         Class<?> optionalClass = Class.forName("java.util.Optional");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 221 */         Method currentMethod = processHandleClass.getMethod("current", new Class[0]);
/* 222 */         Method infoMethod = processHandleClass.getMethod("info", new Class[0]);
/* 223 */         Method userMethod = processHandleInfoClass.getMethod("user", new Class[0]);
/* 224 */         Method orElseMethod = optionalClass.getMethod("orElse", new Class[] { Object.class });
/*     */         
/* 226 */         Object current = currentMethod.invoke(null, new Object[0]);
/* 227 */         Object info = infoMethod.invoke(current, new Object[0]);
/* 228 */         Object user = userMethod.invoke(info, new Object[0]);
/* 229 */         return (String)Objects.<Object>requireNonNull(orElseMethod.invoke(user, new Object[] { fromSystemProperty }));
/* 230 */       } catch (ClassNotFoundException runningUnderAndroidOrJava8) {
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
/* 242 */         return fromSystemProperty;
/* 243 */       } catch (InvocationTargetException e) {
/* 244 */         Throwables.throwIfUnchecked(e.getCause());
/* 245 */         return fromSystemProperty;
/* 246 */       } catch (NoSuchMethodException shouldBeImpossible) {
/* 247 */         return fromSystemProperty;
/* 248 */       } catch (IllegalAccessException shouldBeImpossible) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 254 */         return fromSystemProperty;
/*     */       } 
/*     */     } }
/*     */   private static final class JavaIoCreator extends TempFileCreator { private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */     
/*     */     private JavaIoCreator() {}
/*     */     
/*     */     File createTempDir() {
/* 262 */       File baseDir = new File(StandardSystemProperty.JAVA_IO_TMPDIR.value());
/*     */       
/* 264 */       String baseName = System.currentTimeMillis() + "-";
/*     */       
/* 266 */       for (int counter = 0; counter < 10000; counter++) {
/* 267 */         File tempDir = new File(baseDir, baseName + counter);
/* 268 */         if (tempDir.mkdir()) {
/* 269 */           return tempDir;
/*     */         }
/*     */       } 
/* 272 */       throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
/*     */     }
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
/*     */     File createTempFile(String prefix) throws IOException {
/* 285 */       return File.createTempFile(prefix, null, null);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ThrowingCreator
/*     */     extends TempFileCreator
/*     */   {
/*     */     private static final String MESSAGE = "Guava cannot securely create temporary files or directories under SDK versions before Jelly Bean. You can create one yourself, either in the insecure default directory or in a more secure directory, such as context.getCacheDir(). For more information, see the Javadoc for Files.createTempDir().";
/*     */ 
/*     */ 
/*     */     
/*     */     private ThrowingCreator() {}
/*     */ 
/*     */ 
/*     */     
/*     */     File createTempDir() {
/* 304 */       throw new IllegalStateException("Guava cannot securely create temporary files or directories under SDK versions before Jelly Bean. You can create one yourself, either in the insecure default directory or in a more secure directory, such as context.getCacheDir(). For more information, see the Javadoc for Files.createTempDir().");
/*     */     }
/*     */ 
/*     */     
/*     */     File createTempFile(String prefix) throws IOException {
/* 309 */       throw new IOException("Guava cannot securely create temporary files or directories under SDK versions before Jelly Bean. You can create one yourself, either in the insecure default directory or in a more secure directory, such as context.getCacheDir(). For more information, see the Javadoc for Files.createTempDir().");
/*     */     }
/*     */   }
/*     */   
/*     */   private TempFileCreator() {}
/*     */   
/*     */   abstract File createTempDir();
/*     */   
/*     */   abstract File createTempFile(String paramString) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/TempFileCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */