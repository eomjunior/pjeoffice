/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.Comparator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
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
/*     */ public abstract class Directory
/*     */ {
/*     */   public static void deleteQuietly(File file) {
/*  46 */     Throwables.quietly(() -> delete(file));
/*     */   }
/*     */   
/*     */   public static void rmDir(Path path) throws IOException {
/*  50 */     rmDir(path, f -> true);
/*     */   }
/*     */   
/*     */   public static void mkDir(Path path) throws IOException {
/*  54 */     Args.requireNonNull(path, "path is null");
/*  55 */     mkDir(path.toFile());
/*     */   }
/*     */   
/*     */   public static void rmkDir(Path path) throws IOException {
/*  59 */     rmDir(path);
/*  60 */     mkDir(path);
/*     */   }
/*     */   
/*     */   public static void rmkDir(File path) throws IOException {
/*  64 */     Args.requireNonNull(path, "path is null");
/*  65 */     rmkDir(path.toPath());
/*     */   }
/*     */   
/*     */   public static void requireDirectory(File dir, String message) throws IOException {
/*  69 */     Args.requireNonNull(dir, "input is null");
/*  70 */     if (!dir.isDirectory())
/*  71 */       throw new IOException(message); 
/*     */   }
/*     */   
/*     */   public static File requireNotExists(File input, String message) throws IOException {
/*  75 */     Args.requireNonNull(input, "input is null");
/*  76 */     if (input.exists())
/*  77 */       throw new IOException(message); 
/*  78 */     return input;
/*     */   }
/*     */   
/*     */   public static boolean isSameFile(Path p1, Path p2) {
/*     */     try {
/*  83 */       return Files.isSameFile(p1, p2);
/*  84 */     } catch (IOException e) {
/*  85 */       e.printStackTrace();
/*  86 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void delete(File file) throws IOException {
/*  91 */     Args.requireNonNull(file, "file is null");
/*  92 */     if (file.isFile()) {
/*  93 */       file.delete();
/*     */     } else {
/*  95 */       rmDir(file.toPath());
/*     */     } 
/*     */   }
/*     */   public static Path requireExists(Path input, String message) throws IOException {
/*  99 */     Args.requireNonNull(input, "input is null");
/* 100 */     requireExists(input.toFile(), message);
/* 101 */     return input;
/*     */   }
/*     */   
/*     */   public static File requireExists(File input, String message) throws IOException {
/* 105 */     Args.requireNonNull(input, "input is null");
/* 106 */     if (!input.exists())
/* 107 */       throw new IOException(message); 
/* 108 */     return input;
/*     */   }
/*     */   
/*     */   public static void rmDir(Path path, Predicate<File> predicate) throws IOException {
/* 112 */     Args.requireNonNull(path, "path is null");
/* 113 */     Args.requireNonNull(predicate, "predicate is null");
/* 114 */     if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/* 115 */       try (Stream<Path> walk = Files.walk(path, new java.nio.file.FileVisitOption[0])) {
/* 116 */         walk.sorted(Comparator.reverseOrder())
/* 117 */           .map(Path::toFile)
/* 118 */           .filter(predicate)
/* 119 */           .forEach(File::delete);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void ifExists(File file, Consumer<File> consumer) {
/* 125 */     if (file != null && file.exists()) {
/* 126 */       consumer.accept(file);
/*     */     }
/*     */   }
/*     */   
/*     */   public static File createTempFile(String prefix) throws IOException {
/* 131 */     return createTempFile(prefix, ".util4j.tmp");
/*     */   }
/*     */   
/*     */   public static File createTempFile(String prefix, File directory) throws IOException {
/* 135 */     return createTempFile(prefix, ".util4j.tmp", directory);
/*     */   }
/*     */   
/*     */   public static File createTempFile(String prefix, String suffix) throws IOException {
/* 139 */     return createTempFile(prefix, suffix, null);
/*     */   }
/*     */   
/*     */   public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
/* 143 */     Args.requireNonNull(prefix, "prefix is null");
/*     */     try {
/* 145 */       return File.createTempFile(prefix, suffix, directory);
/* 146 */     } catch (Exception e) {
/* 147 */       throw new IOException("Não é possível criar arquivo temporário", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void mkDir(File folder) throws IOException {
/* 152 */     Args.requireNonNull(folder, "folder is null");
/* 153 */     if (folder.exists()) {
/* 154 */       if (folder.isDirectory())
/*     */         return; 
/* 156 */       if (!folder.delete())
/* 157 */         throw new IOException("Unabled to delete file " + folder); 
/*     */     } 
/* 159 */     if (!folder.mkdirs()) {
/* 160 */       throw new IOException("Unabled to create directory tree  " + folder);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void rmCopy(Path source, Path dest) throws IOException {
/* 165 */     rmDir(dest);
/* 166 */     copyFolder(source, dest);
/*     */   }
/*     */   
/*     */   public static void copyFolder(Path src, Path dest) throws IOException {
/* 170 */     Args.requireNonNull(src, "src is null");
/* 171 */     Args.requireNonNull(dest, "dest is null");
/* 172 */     try (Stream<Path> stream = Files.walk(src, new java.nio.file.FileVisitOption[0])) {
/* 173 */       stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
/* 174 */     } catch (RuntimeException e) {
/* 175 */       throw new IOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String stringPath(File file) {
/* 180 */     return stringPath(file, false);
/*     */   }
/*     */   
/*     */   public static String stringPath(Path path) {
/* 184 */     return stringPath(path, false);
/*     */   }
/*     */   
/*     */   public static String stringPath(Path path, boolean quotes) {
/* 188 */     if (path == null)
/* 189 */       return quotes ? Strings.quotes("null") : "null"; 
/* 190 */     return stringPath(path.toFile(), quotes);
/*     */   }
/*     */   public static String stringPath(File file, boolean quotes) {
/*     */     String path;
/* 194 */     if (file == null) {
/* 195 */       return quotes ? Strings.quotes("null") : "null";
/*     */     }
/*     */     try {
/* 198 */       path = file.getCanonicalPath();
/* 199 */     } catch (IOException e) {
/* 200 */       path = file.getAbsolutePath();
/*     */     } 
/* 202 */     return quotes ? Strings.quotes(path) : path;
/*     */   }
/*     */   
/*     */   private static void copy(Path source, Path dest) {
/*     */     try {
/* 207 */       Files.copy(source, dest, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 208 */     } catch (Exception e) {
/* 209 */       throw new RuntimeException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Directory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */