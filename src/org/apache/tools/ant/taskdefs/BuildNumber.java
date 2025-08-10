/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class BuildNumber
/*     */   extends Task
/*     */ {
/*     */   private static final String DEFAULT_PROPERTY_NAME = "build.number";
/*     */   private static final String DEFAULT_FILENAME = "build.number";
/*  51 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File myFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  63 */     this.myFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  73 */     File savedFile = this.myFile;
/*     */     
/*  75 */     validate();
/*     */     
/*  77 */     Properties properties = loadProperties();
/*  78 */     int buildNumber = getBuildNumber(properties);
/*     */     
/*  80 */     properties.put("build.number", 
/*  81 */         String.valueOf(buildNumber + 1));
/*     */ 
/*     */ 
/*     */     
/*  85 */     try { OutputStream output = Files.newOutputStream(this.myFile.toPath(), new java.nio.file.OpenOption[0]); 
/*  86 */       try { properties.store(output, "Build Number for ANT. Do not edit!");
/*  87 */         if (output != null) output.close();  } catch (Throwable throwable) { if (output != null) try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/*  88 */     { throw new BuildException("Error while writing " + this.myFile, ioe); }
/*     */     finally
/*  90 */     { this.myFile = savedFile; }
/*     */ 
/*     */ 
/*     */     
/*  94 */     getProject().setNewProperty("build.number", 
/*  95 */         String.valueOf(buildNumber));
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
/*     */   private int getBuildNumber(Properties properties) throws BuildException {
/* 108 */     String buildNumber = properties.getProperty("build.number", "0").trim();
/*     */ 
/*     */     
/*     */     try {
/* 112 */       return Integer.parseInt(buildNumber);
/* 113 */     } catch (NumberFormatException nfe) {
/* 114 */       throw new BuildException(this.myFile + " contains a non integer build number: " + buildNumber, nfe);
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
/*     */   private Properties loadProperties() throws BuildException {
/*     */     
/* 127 */     try { InputStream input = Files.newInputStream(this.myFile.toPath(), new java.nio.file.OpenOption[0]); 
/* 128 */       try { Properties properties = new Properties();
/* 129 */         properties.load(input);
/* 130 */         Properties properties1 = properties;
/* 131 */         if (input != null) input.close();  return properties1; } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 132 */     { throw new BuildException(ioe); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 143 */     if (null == this.myFile) {
/* 144 */       this.myFile = FILE_UTILS.resolveFile(getProject().getBaseDir(), "build.number");
/*     */     }
/*     */     
/* 147 */     if (!this.myFile.exists()) {
/*     */       try {
/* 149 */         FILE_UTILS.createNewFile(this.myFile);
/* 150 */       } catch (IOException ioe) {
/* 151 */         throw new BuildException(this.myFile + " doesn't exist and new file can't be created.", ioe);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (!this.myFile.canRead()) {
/* 158 */       throw new BuildException("Unable to read from " + this.myFile + ".");
/*     */     }
/*     */     
/* 161 */     if (!this.myFile.canWrite())
/* 162 */       throw new BuildException("Unable to write to " + this.myFile + "."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/BuildNumber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */