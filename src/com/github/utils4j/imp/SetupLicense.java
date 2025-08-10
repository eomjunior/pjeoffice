/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IConstants;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
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
/*    */ public class SetupLicense
/*    */ {
/*    */   public static void main(String[] args) throws IOException {
/* 46 */     Path root = Paths.get("../", new String[0]);
/* 47 */     List<String> licenseLines = Files.readAllLines(Paths.get("./LICENSE", new String[0]));
/* 48 */     try (Stream<Path> walk = Files.walk(root, new java.nio.file.FileVisitOption[0])) {
/* 49 */       walk.map(Path::toFile)
/* 50 */         .filter(f -> f.getName().endsWith(".java"))
/* 51 */         .forEach(java -> {
/*    */             List<String> javaLines;
/*    */             try {
/*    */               javaLines = Files.readAllLines(java.toPath(), IConstants.UTF_8);
/* 55 */             } catch (IOException e1) {
/*    */               return;
/*    */             } 
/*    */             if (javaLines.stream().anyMatch(()))
/*    */               return; 
/*    */             File original = java;
/*    */             java.renameTo(java = new File(java.getParent(), java.getName() + ".bkp"));
/*    */             try (PrintWriter w = new PrintWriter(original, "UTF-8")) {
/*    */               w.println("/*");
/*    */               licenseLines.forEach(());
/*    */               w.println("*/\n");
/*    */               javaLines.forEach(w::println);
/*    */               java.delete();
/* 68 */             } catch (Exception e) {
/*    */               java.renameTo(original);
/*    */             } 
/*    */           });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/SetupLicense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */