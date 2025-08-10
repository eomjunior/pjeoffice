/*     */ package org.apache.tools.ant.taskdefs.optional.javacc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.CommandlineJava;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
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
/*     */ public class JJDoc
/*     */   extends Task
/*     */ {
/*     */   private static final String OUTPUT_FILE = "OUTPUT_FILE";
/*     */   private static final String TEXT = "TEXT";
/*     */   private static final String ONE_TABLE = "ONE_TABLE";
/*  47 */   private final Map<String, Object> optionalAttrs = new Hashtable<>();
/*     */   
/*  49 */   private String outputFile = null;
/*     */   
/*     */   private boolean plainText = false;
/*     */   
/*     */   private static final String DEFAULT_SUFFIX_HTML = ".html";
/*     */   
/*     */   private static final String DEFAULT_SUFFIX_TEXT = ".txt";
/*  56 */   private File targetFile = null;
/*  57 */   private File javaccHome = null;
/*     */   
/*  59 */   private CommandlineJava cmdl = new CommandlineJava();
/*     */   
/*  61 */   private String maxMemory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(boolean plainText) {
/*  68 */     this.optionalAttrs.put("TEXT", Boolean.valueOf(plainText));
/*  69 */     this.plainText = plainText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnetable(boolean oneTable) {
/*  77 */     this.optionalAttrs.put("ONE_TABLE", Boolean.valueOf(oneTable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputfile(String outputFile) {
/*  87 */     this.outputFile = outputFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(File target) {
/*  95 */     this.targetFile = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavacchome(File javaccHome) {
/* 103 */     this.javaccHome = javaccHome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxmemory(String max) {
/* 113 */     this.maxMemory = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JJDoc() {
/* 120 */     this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 131 */     this.optionalAttrs.forEach((name, value) -> this.cmdl.createArgument().setValue("-" + name + ":" + value.toString()));
/*     */ 
/*     */     
/* 134 */     if (this.targetFile == null || !this.targetFile.isFile()) {
/* 135 */       throw new BuildException("Invalid target: %s", new Object[] { this.targetFile });
/*     */     }
/*     */     
/* 138 */     if (this.outputFile != null) {
/* 139 */       this.cmdl.createArgument().setValue("-OUTPUT_FILE:" + this.outputFile
/* 140 */           .replace('\\', '/'));
/*     */     }
/*     */ 
/*     */     
/* 144 */     File javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, this.plainText));
/*     */ 
/*     */     
/* 147 */     if (javaFile.exists() && this.targetFile
/* 148 */       .lastModified() < javaFile.lastModified()) {
/* 149 */       log("Target is already built - skipping (" + this.targetFile + ")", 3);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 154 */     this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
/*     */     
/* 156 */     Path classpath = this.cmdl.createClasspath(getProject());
/* 157 */     File javaccJar = JavaCC.getArchiveFile(this.javaccHome);
/* 158 */     classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
/* 159 */     classpath.addJavaRuntime();
/*     */     
/* 161 */     this.cmdl.setClassname(JavaCC.getMainClass(classpath, 3));
/*     */ 
/*     */     
/* 164 */     this.cmdl.setMaxmemory(this.maxMemory);
/* 165 */     Commandline.Argument arg = this.cmdl.createVmArgument();
/* 166 */     arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
/*     */     
/* 168 */     Execute process = new Execute((ExecuteStreamHandler)new LogStreamHandler(this, 2, 2), null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     log(this.cmdl.describeCommand(), 3);
/* 174 */     process.setCommandline(this.cmdl.getCommandline());
/*     */     
/*     */     try {
/* 177 */       if (process.execute() != 0) {
/* 178 */         throw new BuildException("JJDoc failed.");
/*     */       }
/* 180 */     } catch (IOException e) {
/* 181 */       throw new BuildException("Failed to launch JJDoc", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String createOutputFileName(File destFile, String optionalOutputFile, boolean plain) {
/* 187 */     String suffix = ".html";
/* 188 */     String javaccFile = destFile.getAbsolutePath().replace('\\', '/');
/*     */     
/* 190 */     if (plain) {
/* 191 */       suffix = ".txt";
/*     */     }
/*     */     
/* 194 */     if (optionalOutputFile == null || optionalOutputFile.isEmpty()) {
/* 195 */       int filePos = javaccFile.lastIndexOf('/');
/*     */       
/* 197 */       if (filePos >= 0) {
/* 198 */         javaccFile = javaccFile.substring(filePos + 1);
/*     */       }
/*     */       
/* 201 */       int suffixPos = javaccFile.lastIndexOf('.');
/*     */       
/* 203 */       if (suffixPos == -1) {
/* 204 */         optionalOutputFile = javaccFile + suffix;
/*     */       } else {
/* 206 */         String currentSuffix = javaccFile.substring(suffixPos);
/*     */         
/* 208 */         if (currentSuffix.equals(suffix)) {
/* 209 */           optionalOutputFile = javaccFile + suffix;
/*     */         } else {
/* 211 */           optionalOutputFile = javaccFile.substring(0, suffixPos) + suffix;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 216 */       optionalOutputFile = optionalOutputFile.replace('\\', '/');
/*     */     } 
/*     */     
/* 219 */     return (getProject().getBaseDir() + "/" + optionalOutputFile)
/* 220 */       .replace('\\', '/');
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javacc/JJDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */