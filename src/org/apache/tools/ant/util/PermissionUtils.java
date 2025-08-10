/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.PosixFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.ArchiveResource;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermissionUtils
/*     */ {
/*     */   public static int modeFromPermissions(Set<PosixFilePermission> permissions, FileType type) {
/*  56 */     switch (type)
/*     */     { case SYMLINK:
/*  58 */         mode = 10;
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
/*  71 */         mode <<= 3;
/*  72 */         mode <<= 3;
/*  73 */         mode = (int)(mode | modeFromPermissions(permissions, "OWNER"));
/*  74 */         mode <<= 3;
/*  75 */         mode = (int)(mode | modeFromPermissions(permissions, "GROUP"));
/*  76 */         mode <<= 3;
/*  77 */         mode = (int)(mode | modeFromPermissions(permissions, "OTHERS"));
/*  78 */         return mode;case REGULAR_FILE: mode = 8; mode <<= 3; mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OWNER")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "GROUP")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OTHERS")); return mode;case DIR: mode = 4; mode <<= 3; mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OWNER")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "GROUP")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OTHERS")); return mode; }  int mode = 0; mode <<= 3; mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OWNER")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "GROUP")); mode <<= 3; mode = (int)(mode | modeFromPermissions(permissions, "OTHERS")); return mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<PosixFilePermission> permissionsFromMode(int mode) {
/*  88 */     Set<PosixFilePermission> permissions = EnumSet.noneOf(PosixFilePermission.class);
/*  89 */     addPermissions(permissions, "OTHERS", mode);
/*  90 */     addPermissions(permissions, "GROUP", (mode >> 3));
/*  91 */     addPermissions(permissions, "OWNER", (mode >> 6));
/*  92 */     return permissions;
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
/*     */   public static void setPermissions(Resource r, Set<PosixFilePermission> permissions, Consumer<Path> posixNotSupportedCallback) throws IOException {
/* 116 */     FileProvider f = (FileProvider)r.as(FileProvider.class);
/* 117 */     if (f != null) {
/* 118 */       Path p = f.getFile().toPath();
/*     */       
/* 120 */       PosixFileAttributeView view = Files.<PosixFileAttributeView>getFileAttributeView(p, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/* 121 */       if (view != null) {
/* 122 */         view.setPermissions(permissions);
/* 123 */       } else if (posixNotSupportedCallback != null) {
/* 124 */         posixNotSupportedCallback.accept(p);
/*     */       } 
/* 126 */     } else if (r instanceof ArchiveResource) {
/* 127 */       ((ArchiveResource)r).setMode(modeFromPermissions(permissions, 
/* 128 */             FileType.of(r)));
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
/*     */   public static Set<PosixFilePermission> getPermissions(Resource r, Function<Path, Set<PosixFilePermission>> posixNotSupportedFallback) throws IOException {
/* 155 */     FileProvider f = (FileProvider)r.as(FileProvider.class);
/* 156 */     if (f != null) {
/* 157 */       Path p = f.getFile().toPath();
/*     */       
/* 159 */       PosixFileAttributeView view = Files.<PosixFileAttributeView>getFileAttributeView(p, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/* 160 */       if (view != null)
/* 161 */         return view.readAttributes().permissions(); 
/* 162 */       if (posixNotSupportedFallback != null) {
/* 163 */         return posixNotSupportedFallback.apply(p);
/*     */       }
/* 165 */     } else if (r instanceof ArchiveResource) {
/* 166 */       return permissionsFromMode(((ArchiveResource)r).getMode());
/*     */     } 
/* 168 */     return EnumSet.noneOf(PosixFilePermission.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long modeFromPermissions(Set<PosixFilePermission> permissions, String prefix) {
/* 173 */     long mode = 0L;
/* 174 */     if (permissions.contains(PosixFilePermission.valueOf(prefix + "_READ"))) {
/* 175 */       mode |= 0x4L;
/*     */     }
/* 177 */     if (permissions.contains(PosixFilePermission.valueOf(prefix + "_WRITE"))) {
/* 178 */       mode |= 0x2L;
/*     */     }
/* 180 */     if (permissions.contains(PosixFilePermission.valueOf(prefix + "_EXECUTE"))) {
/* 181 */       mode |= 0x1L;
/*     */     }
/* 183 */     return mode;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPermissions(Set<PosixFilePermission> permissions, String prefix, long mode) {
/* 188 */     if ((mode & 0x1L) == 1L) {
/* 189 */       permissions.add(PosixFilePermission.valueOf(prefix + "_EXECUTE"));
/*     */     }
/* 191 */     if ((mode & 0x2L) == 2L) {
/* 192 */       permissions.add(PosixFilePermission.valueOf(prefix + "_WRITE"));
/*     */     }
/* 194 */     if ((mode & 0x4L) == 4L) {
/* 195 */       permissions.add(PosixFilePermission.valueOf(prefix + "_READ"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum FileType
/*     */   {
/* 205 */     REGULAR_FILE,
/*     */     
/* 207 */     DIR,
/*     */     
/* 209 */     SYMLINK,
/*     */     
/* 211 */     OTHER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static FileType of(Path p) throws IOException {
/* 222 */       BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
/* 223 */       if (attrs.isRegularFile())
/* 224 */         return REGULAR_FILE; 
/* 225 */       if (attrs.isDirectory())
/* 226 */         return DIR; 
/* 227 */       if (attrs.isSymbolicLink()) {
/* 228 */         return SYMLINK;
/*     */       }
/* 230 */       return OTHER;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static FileType of(Resource r) {
/* 240 */       if (r.isDirectory()) {
/* 241 */         return DIR;
/*     */       }
/* 243 */       return REGULAR_FILE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/PermissionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */