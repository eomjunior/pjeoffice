/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ import org.apache.tools.zip.ZipEntry;
/*     */ import org.apache.tools.zip.ZipFile;
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
/*     */ public class IsSigned
/*     */   extends DataType
/*     */   implements Condition
/*     */ {
/*     */   private static final String SIG_START = "META-INF/";
/*     */   private static final String SIG_END = ".SF";
/*     */   private static final int SHORT_SIG_LIMIT = 8;
/*     */   private String name;
/*     */   private File file;
/*     */   
/*     */   public void setFile(File file) {
/*  51 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  59 */     this.name = name;
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
/*     */   public static boolean isSigned(File zipFile, String name) throws IOException {
/*  73 */     ZipFile jarFile = new ZipFile(zipFile); try {
/*  74 */       if (null == name)
/*     */       
/*  76 */       { boolean bool1 = StreamUtils.enumerationAsStream(jarFile.getEntries()).anyMatch(e -> (e.getName().startsWith("META-INF/") && e.getName().endsWith(".SF")));
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
/*  91 */         jarFile.close(); return bool1; }  name = replaceInvalidChars(name); boolean shortSig = (jarFile.getEntry("META-INF/" + name.toUpperCase() + ".SF") != null); boolean longSig = false; if (name.length() > 8) longSig = (jarFile.getEntry("META-INF/" + name.substring(0, 8).toUpperCase() + ".SF") != null);  boolean bool = (shortSig || longSig) ? true : false; jarFile.close();
/*     */       return bool;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         jarFile.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   } public boolean eval() {
/* 102 */     if (this.file == null) {
/* 103 */       throw new BuildException("The file attribute must be set.");
/*     */     }
/* 105 */     if (!this.file.exists()) {
/* 106 */       log("The file \"" + this.file.getAbsolutePath() + "\" does not exist.", 3);
/*     */       
/* 108 */       return false;
/*     */     } 
/*     */     
/* 111 */     boolean r = false;
/*     */     try {
/* 113 */       r = isSigned(this.file, this.name);
/* 114 */     } catch (IOException e) {
/* 115 */       log("Got IOException reading file \"" + this.file.getAbsolutePath() + "\"" + e, 1);
/*     */     } 
/*     */ 
/*     */     
/* 119 */     if (r) {
/* 120 */       log("File \"" + this.file.getAbsolutePath() + "\" is signed.", 3);
/*     */     }
/*     */     
/* 123 */     return r;
/*     */   }
/*     */   
/*     */   private static String replaceInvalidChars(String name) {
/* 127 */     StringBuilder sb = new StringBuilder();
/* 128 */     boolean changes = false;
/* 129 */     for (char ch : name.toCharArray()) {
/* 130 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_".indexOf(ch) < 0) {
/* 131 */         sb.append("_");
/* 132 */         changes = true;
/*     */       } else {
/* 134 */         sb.append(ch);
/*     */       } 
/*     */     } 
/* 137 */     return changes ? sb.toString() : name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsSigned.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */