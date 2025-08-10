/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
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
/*     */ public final class Environment
/*     */ {
/*     */   public static Optional<String> valueFrom(String environmentVariableKey) {
/*  42 */     return valueFrom(environmentVariableKey, (String)null);
/*     */   }
/*     */   
/*     */   public static Optional<String> valueFrom(String environmentVariableKey, String defauValueIfEmpty) {
/*  46 */     return valueFrom(environmentVariableKey, () -> defauValueIfEmpty);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentVariableKey) {
/*  50 */     return pathFrom(environmentVariableKey, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> requirePathFrom(String environmentVariableKey) {
/*  54 */     return pathFrom(environmentVariableKey, false, true);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentVariableKey, boolean defaultToUserHome) {
/*  58 */     return pathFrom(environmentVariableKey, defaultToUserHome, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentVariableKey, boolean defaultToUserHome, boolean mustExists) {
/*  62 */     return pathFrom(environmentVariableKey, defaultToUserHome ? Paths.get(System.getProperty("user.home"), new String[0]) : null, mustExists);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentVariableKey, Path defaultIfNothing) {
/*  66 */     return pathFrom(environmentVariableKey, defaultIfNothing, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentVariableKey, Path defaultIfNothing, boolean mustExists) {
/*  70 */     return pathFrom(environmentVariableKey, () -> defaultIfNothing, mustExists);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentKey, Supplier<Path> defaultIfNothing) {
/*  74 */     return pathFrom(environmentKey, defaultIfNothing, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> resolveTo(String environmentVariableKey, String fileName) {
/*  78 */     return resolveTo(environmentVariableKey, fileName, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> resolveTo(String environmentVariableKey, String fileName, boolean defaultToUserHome) {
/*  82 */     return resolveTo(environmentVariableKey, fileName, defaultToUserHome, false);
/*     */   }
/*     */   
/*     */   public static Optional<Path> requireResolveTo(String environmentVariableKey, String fileName) {
/*  86 */     return resolveTo(environmentVariableKey, fileName, false, true);
/*     */   }
/*     */   
/*     */   public static Optional<Path> resolveTo(String environmentVariableKey, String fileName, boolean defaultToUserHome, boolean mustExists) {
/*  90 */     Optional<Path> basePath = pathFrom(environmentVariableKey, defaultToUserHome, mustExists);
/*  91 */     if (!basePath.isPresent())
/*  92 */       return Optional.empty(); 
/*  93 */     Path resolvedPath = ((Path)basePath.get()).resolve(fileName);
/*  94 */     if (mustExists && !resolvedPath.toFile().exists())
/*  95 */       return Optional.empty(); 
/*  96 */     return Optional.of(resolvedPath);
/*     */   }
/*     */   
/*     */   public static Optional<Path> pathFrom(String environmentKey, Supplier<Path> defaultIfNothing, boolean mustExists) {
/* 100 */     Optional<String> environmentKeyPath = valueFrom(environmentKey);
/* 101 */     Path basePath = null;
/* 102 */     if (environmentKeyPath.isPresent()) {
/* 103 */       basePath = Paths.get(environmentKeyPath.get(), new String[0]);
/*     */     }
/*     */     
/* 106 */     if (basePath != null && 
/* 107 */       mustExists) {
/* 108 */       if (basePath.toFile().exists())
/* 109 */         return Optional.of(basePath); 
/* 110 */       basePath = null;
/*     */     } 
/*     */ 
/*     */     
/* 114 */     if (basePath == null && defaultIfNothing != null) {
/* 115 */       basePath = defaultIfNothing.get();
/*     */     }
/*     */     
/* 118 */     if (basePath == null || (mustExists && !basePath.toFile().exists())) {
/* 119 */       return Optional.empty();
/*     */     }
/*     */     
/* 122 */     return Optional.of(basePath);
/*     */   }
/*     */   
/*     */   public static Optional<String> valueFrom(String environmentVariableKey, Supplier<String> defauValueIfEmpty) {
/* 126 */     if (environmentVariableKey == null) {
/* 127 */       if (defauValueIfEmpty == null)
/* 128 */         return Optional.empty(); 
/* 129 */       return Optional.ofNullable(defauValueIfEmpty.get());
/*     */     } 
/* 131 */     String value = System.getProperty(environmentVariableKey);
/* 132 */     if (value == null) {
/* 133 */       value = System.getProperty(environmentVariableKey.toLowerCase());
/*     */     }
/* 135 */     if (value == null) {
/* 136 */       value = System.getProperty(environmentVariableKey.toUpperCase());
/*     */     }
/* 138 */     if (value == null) {
/* 139 */       value = System.getenv(environmentVariableKey);
/*     */     }
/* 141 */     if (value == null) {
/* 142 */       value = System.getenv(environmentVariableKey.toLowerCase());
/*     */     }
/* 144 */     if (value == null) {
/* 145 */       value = System.getenv(environmentVariableKey.toUpperCase());
/*     */     }
/* 147 */     if (value == null && defauValueIfEmpty != null) {
/* 148 */       value = defauValueIfEmpty.get();
/*     */     }
/* 150 */     return Optional.ofNullable(value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Environment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */