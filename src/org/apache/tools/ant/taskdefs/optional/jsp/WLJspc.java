/*     */ package org.apache.tools.ant.taskdefs.optional.jsp;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.time.Instant;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WLJspc
/*     */   extends MatchingTask
/*     */ {
/*     */   private File destinationDirectory;
/*     */   private File sourceDirectory;
/*     */   private String destinationPackage;
/*     */   private Path compileClasspath;
/* 101 */   private String pathToPackage = "";
/* 102 */   private List<String> filesToDo = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 110 */     if (!this.destinationDirectory.isDirectory()) {
/* 111 */       throw new BuildException("destination directory %s is not valid", new Object[] { this.destinationDirectory
/* 112 */             .getPath() });
/*     */     }
/*     */     
/* 115 */     if (!this.sourceDirectory.isDirectory()) {
/* 116 */       throw new BuildException("src directory %s is not valid", new Object[] { this.sourceDirectory
/* 117 */             .getPath() });
/*     */     }
/*     */     
/* 120 */     if (this.destinationPackage == null) {
/* 121 */       throw new BuildException("package attribute must be present.", 
/* 122 */           getLocation());
/*     */     }
/*     */     
/* 125 */     this
/* 126 */       .pathToPackage = this.destinationPackage.replace('.', File.separatorChar);
/*     */     
/* 128 */     DirectoryScanner ds = getDirectoryScanner(this.sourceDirectory);
/*     */ 
/*     */     
/* 131 */     if (this.compileClasspath == null) {
/* 132 */       this.compileClasspath = new Path(getProject());
/*     */     }
/*     */     
/* 135 */     this.compileClasspath = this.compileClasspath.concatSystemClasspath();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Java helperTask = new Java((Task)this);
/* 142 */     helperTask.setFork(true);
/* 143 */     helperTask.setClassname("weblogic.jspc");
/* 144 */     helperTask.setTaskName(getTaskName());
/*     */     
/* 146 */     String[] args = new String[12];
/*     */ 
/*     */     
/* 149 */     int j = 0;
/*     */     
/* 151 */     args[j++] = "-d";
/* 152 */     args[j++] = this.destinationDirectory.getAbsolutePath().trim();
/* 153 */     args[j++] = "-docroot";
/* 154 */     args[j++] = this.sourceDirectory.getAbsolutePath().trim();
/* 155 */     args[j++] = "-keepgenerated";
/*     */ 
/*     */     
/* 158 */     args[j++] = "-compilerclass";
/* 159 */     args[j++] = "sun.tools.javac.Main";
/*     */ 
/*     */ 
/*     */     
/* 163 */     args[j++] = "-classpath";
/* 164 */     args[j++] = this.compileClasspath.toString();
/*     */     
/* 166 */     scanDir(ds.getIncludedFiles());
/* 167 */     log("Compiling " + this.filesToDo.size() + " JSP files");
/*     */     
/* 169 */     for (String filename : this.filesToDo) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       File jspFile = new File(filename);
/* 175 */       args[j] = "-package";
/* 176 */       String parents = jspFile.getParent();
/* 177 */       if (parents == null || parents.isEmpty()) {
/* 178 */         args[j + 1] = this.destinationPackage;
/*     */       } else {
/* 180 */         parents = replaceString(parents, File.separator, "_.");
/* 181 */         args[j + 1] = this.destinationPackage + "._" + parents;
/*     */       } 
/*     */       
/* 184 */       args[j + 2] = this.sourceDirectory + File.separator + filename;
/* 185 */       helperTask.clearArgs();
/*     */ 
/*     */       
/* 188 */       for (int x = 0; x < j + 3; x++) {
/* 189 */         helperTask.createArg().setValue(args[x]);
/*     */       }
/*     */ 
/*     */       
/* 193 */       helperTask.setClasspath(this.compileClasspath);
/* 194 */       if (helperTask.executeJava() != 0) {
/* 195 */         log(filename + " failed to compile", 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 205 */     if (this.compileClasspath == null) {
/* 206 */       this.compileClasspath = classpath;
/*     */     } else {
/* 208 */       this.compileClasspath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 217 */     if (this.compileClasspath == null) {
/* 218 */       this.compileClasspath = new Path(getProject());
/*     */     }
/* 220 */     return this.compileClasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File dirName) {
/* 230 */     this.sourceDirectory = dirName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dirName) {
/* 240 */     this.destinationDirectory = dirName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String packageName) {
/* 249 */     this.destinationPackage = packageName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scanDir(String[] files) {
/* 258 */     long now = Instant.now().toEpochMilli();
/* 259 */     for (String file : files) {
/* 260 */       String pack; File srcFile = new File(this.sourceDirectory, file);
/*     */ 
/*     */ 
/*     */       
/* 264 */       File jspFile = new File(file);
/* 265 */       String parents = jspFile.getParent();
/*     */ 
/*     */       
/* 268 */       if (parents == null || parents.isEmpty()) {
/* 269 */         pack = this.pathToPackage;
/*     */       } else {
/* 271 */         parents = replaceString(parents, File.separator, "_/");
/* 272 */         pack = this.pathToPackage + File.separator + "_" + parents;
/*     */       } 
/*     */       
/* 275 */       String filePath = pack + File.separator + "_";
/*     */       
/* 277 */       int startingIndex = (file.lastIndexOf(File.separator) != -1) ? (file.lastIndexOf(File.separator) + 1) : 0;
/* 278 */       int endingIndex = file.indexOf(".jsp");
/* 279 */       if (endingIndex == -1) {
/* 280 */         log("Skipping " + file + ". Not a JSP", 3);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 285 */         filePath = filePath + file.substring(startingIndex, endingIndex);
/* 286 */         filePath = filePath + ".class";
/* 287 */         File classFile = new File(this.destinationDirectory, filePath);
/*     */         
/* 289 */         if (srcFile.lastModified() > now) {
/* 290 */           log("Warning: file modified in the future: " + file, 1);
/*     */         }
/*     */         
/* 293 */         if (srcFile.lastModified() > classFile.lastModified()) {
/* 294 */           this.filesToDo.add(file);
/* 295 */           log("Recompiling File " + file, 3);
/*     */         } 
/*     */       } 
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
/*     */   protected String replaceString(String inpString, String escapeChars, String replaceChars) {
/* 309 */     StringBuilder localString = new StringBuilder();
/* 310 */     StringTokenizer st = new StringTokenizer(inpString, escapeChars, true);
/* 311 */     int numTokens = st.countTokens();
/* 312 */     for (int i = 0; i < numTokens; i++) {
/* 313 */       String test = st.nextToken();
/* 314 */       test = test.equals(escapeChars) ? replaceChars : test;
/* 315 */       localString.append(test);
/*     */     } 
/* 317 */     return localString.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/WLJspc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */