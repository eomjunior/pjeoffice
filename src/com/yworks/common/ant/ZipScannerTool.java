/*     */ package com.yworks.common.ant;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
/*     */ import org.apache.tools.ant.types.ZipScanner;
/*     */ import org.apache.tools.zip.ZipEntry;
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
/*     */ public class ZipScannerTool
/*     */ {
/*     */   public static String[] getMatches(ZipFileSet fs, DirectoryScanner scanner) throws IOException {
/*  47 */     Collection result = getMatchedCollection(fs, scanner);
/*  48 */     return (String[])result.toArray((Object[])new String[result.size()]);
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
/*     */   public static Collection getMatchedCollection(ZipFileSet fs, DirectoryScanner scanner) throws IOException {
/*  60 */     return getMatchedCollection(fs, scanner, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File zipFileSetGetSrc(ZipFileSet fs) {
/*  70 */     Method ant15 = null;
/*  71 */     Method ant16 = null;
/*     */     try {
/*  73 */       ant15 = fs.getClass().getMethod("getSrc", new Class[0]);
/*  74 */     } catch (NoSuchMethodException nsme) {
/*     */       try {
/*  76 */         ant16 = fs.getClass().getMethod("getSrc", new Class[] { Project.class });
/*  77 */       } catch (NoSuchMethodException nsme2) {
/*  78 */         throw new BuildException("Could not determine getSrc method of ZipFileSet class");
/*     */       } 
/*     */     } 
/*     */     try {
/*  82 */       if (ant16 != null) {
/*  83 */         return (File)ant16.invoke(fs, new Object[] { fs.getProject() });
/*     */       }
/*  85 */       return (File)ant15.invoke(fs, (Object[])null);
/*     */     }
/*  87 */     catch (IllegalAccessException iaex) {
/*  88 */       throw new BuildException("Could not invoke getSrc method of ZipFileSet class", iaex);
/*  89 */     } catch (InvocationTargetException itex) {
/*  90 */       if (itex.getTargetException() instanceof BuildException) {
/*  91 */         throw (BuildException)itex.getTargetException();
/*     */       }
/*  93 */       throw new BuildException("Internal error: getSrc invocation failed! " + itex.getTargetException().getMessage());
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
/*     */   public static Collection getMatchedCollection(ZipFileSet fs, DirectoryScanner scanner, String baseDir) throws IOException {
/* 108 */     Collection<String> result = new ArrayList(20);
/* 109 */     File zipSrc = zipFileSetGetSrc(fs);
/* 110 */     ZipScanner zipScanner = (ZipScanner)scanner;
/*     */ 
/*     */     
/* 113 */     ZipInputStream in = null;
/*     */     try {
/* 115 */       in = new ZipInputStream(new FileInputStream(zipSrc)); ZipEntry origEntry;
/* 116 */       while ((origEntry = in.getNextEntry()) != null) {
/* 117 */         ZipEntry entry = new ZipEntry(origEntry);
/* 118 */         String vPath = entry.getName();
/*     */         
/* 120 */         if (zipScanner.match(vPath)) {
/* 121 */           result.add(vPath);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 125 */       if (in != null) {
/* 126 */         in.close();
/*     */       }
/*     */     } 
/* 129 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/ZipScannerTool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */