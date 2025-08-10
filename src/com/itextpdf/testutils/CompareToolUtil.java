/*     */ package com.itextpdf.testutils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class CompareToolUtil
/*     */ {
/*     */   private static final String SPLIT_REGEX = "((\".+?\"|[^'\\s]|'.+?')+)\\s*";
/*     */   
/*     */   public static String createTempCopy(String file, String tempFilePrefix, String tempFilePostfix) throws IOException {
/*  29 */     String replacementFilePath = null;
/*     */     try {
/*  31 */       replacementFilePath = File.createTempFile(tempFilePrefix, tempFilePostfix).getAbsolutePath();
/*  32 */       copy(file, replacementFilePath);
/*  33 */     } catch (IOException e) {
/*  34 */       if (null != replacementFilePath) {
/*  35 */         removeFiles(new String[] { replacementFilePath });
/*     */       }
/*  37 */       throw e;
/*     */     } 
/*  39 */     return replacementFilePath;
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
/*     */   public static void copy(String inputFile, String outputFile) throws IOException {
/*  51 */     InputStream is = null;
/*  52 */     OutputStream os = null;
/*     */     try {
/*  54 */       is = new FileInputStream(inputFile);
/*  55 */       os = new FileOutputStream(outputFile);
/*  56 */       byte[] buffer = new byte[1024];
/*     */       int length;
/*  58 */       while ((length = is.read(buffer)) > 0) {
/*  59 */         os.write(buffer, 0, length);
/*     */       }
/*     */     } finally {
/*  62 */       is.close();
/*  63 */       os.close();
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
/*     */   public static String createTempDirectory(String tempFilePrefix) throws IOException {
/*  77 */     File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
/*     */     
/*  79 */     if (!temp.delete()) {
/*  80 */       throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
/*     */     }
/*     */     
/*  83 */     if (!temp.mkdir()) {
/*  84 */       throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
/*     */     }
/*     */     
/*  87 */     return temp.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean removeFiles(String[] paths) {
/*  98 */     boolean allFilesAreRemoved = true;
/*  99 */     for (String path : paths) {
/*     */       try {
/* 101 */         if (null != path) {
/* 102 */           (new File(path)).delete();
/*     */         }
/* 104 */       } catch (Exception e) {
/* 105 */         allFilesAreRemoved = false;
/*     */       } 
/*     */     } 
/* 108 */     return allFilesAreRemoved;
/*     */   }
/*     */   
/*     */   public static Process runProcess(String execPath, String params) throws IOException, InterruptedException {
/* 112 */     List<String> cmdList = prepareProcessArguments(execPath, params);
/* 113 */     String[] cmdArray = cmdList.<String>toArray(new String[0]);
/*     */     
/* 115 */     Process p = Runtime.getRuntime().exec(cmdArray);
/*     */     
/* 117 */     return p;
/*     */   }
/*     */   
/*     */   public static List<String> prepareProcessArguments(String exec, String params) {
/*     */     List<String> cmdList;
/* 122 */     if ((new File(exec)).exists()) {
/* 123 */       cmdList = new ArrayList<String>(Collections.singletonList(exec));
/*     */     } else {
/* 125 */       cmdList = new ArrayList<String>(splitIntoProcessArguments(exec));
/*     */     } 
/* 127 */     cmdList.addAll(splitIntoProcessArguments(params));
/* 128 */     return cmdList;
/*     */   }
/*     */   
/*     */   public static List<String> splitIntoProcessArguments(String line) {
/* 132 */     List<String> list = new ArrayList<String>();
/* 133 */     Matcher m = Pattern.compile("((\".+?\"|[^'\\s]|'.+?')+)\\s*").matcher(line);
/* 134 */     while (m.find()) {
/* 135 */       list.add(m.group(1).replace("'", "").replace("\"", "").trim());
/*     */     }
/* 137 */     return list;
/*     */   }
/*     */   
/*     */   public static String buildPath(String path, String[] fragments) {
/* 141 */     if (path == null) {
/* 142 */       path = "";
/*     */     }
/*     */     
/* 145 */     if (fragments == null || fragments.length == 0) {
/* 146 */       return "";
/*     */     }
/*     */     
/* 149 */     for (int i = 0; i < fragments.length; i++) {
/* 150 */       path = (new File(path, fragments[i])).toString();
/*     */     }
/*     */     
/* 153 */     return path;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/testutils/CompareToolUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */