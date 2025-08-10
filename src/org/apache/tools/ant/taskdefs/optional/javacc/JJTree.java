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
/*     */ 
/*     */ public class JJTree
/*     */   extends Task
/*     */ {
/*     */   private static final String OUTPUT_FILE = "OUTPUT_FILE";
/*     */   private static final String BUILD_NODE_FILES = "BUILD_NODE_FILES";
/*     */   private static final String MULTI = "MULTI";
/*     */   private static final String NODE_DEFAULT_VOID = "NODE_DEFAULT_VOID";
/*     */   private static final String NODE_FACTORY = "NODE_FACTORY";
/*     */   private static final String NODE_SCOPE_HOOK = "NODE_SCOPE_HOOK";
/*     */   private static final String NODE_USES_PARSER = "NODE_USES_PARSER";
/*     */   private static final String STATIC = "STATIC";
/*     */   private static final String VISITOR = "VISITOR";
/*     */   private static final String NODE_PACKAGE = "NODE_PACKAGE";
/*     */   private static final String VISITOR_EXCEPTION = "VISITOR_EXCEPTION";
/*     */   private static final String NODE_PREFIX = "NODE_PREFIX";
/*  57 */   private final Map<String, Object> optionalAttrs = new Hashtable<>();
/*     */   
/*  59 */   private String outputFile = null;
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_SUFFIX = ".jj";
/*     */   
/*  64 */   private File outputDirectory = null;
/*  65 */   private File targetFile = null;
/*  66 */   private File javaccHome = null;
/*     */   
/*  68 */   private CommandlineJava cmdl = new CommandlineJava();
/*     */   
/*  70 */   private String maxMemory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildnodefiles(boolean buildNodeFiles) {
/*  77 */     this.optionalAttrs.put("BUILD_NODE_FILES", Boolean.valueOf(buildNodeFiles));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMulti(boolean multi) {
/*  85 */     this.optionalAttrs.put("MULTI", Boolean.valueOf(multi));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodedefaultvoid(boolean nodeDefaultVoid) {
/*  93 */     this.optionalAttrs.put("NODE_DEFAULT_VOID", Boolean.valueOf(nodeDefaultVoid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodefactory(boolean nodeFactory) {
/* 101 */     this.optionalAttrs.put("NODE_FACTORY", Boolean.valueOf(nodeFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodescopehook(boolean nodeScopeHook) {
/* 109 */     this.optionalAttrs.put("NODE_SCOPE_HOOK", Boolean.valueOf(nodeScopeHook));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeusesparser(boolean nodeUsesParser) {
/* 117 */     this.optionalAttrs.put("NODE_USES_PARSER", Boolean.valueOf(nodeUsesParser));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatic(boolean staticParser) {
/* 125 */     this.optionalAttrs.put("STATIC", Boolean.valueOf(staticParser));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisitor(boolean visitor) {
/* 133 */     this.optionalAttrs.put("VISITOR", Boolean.valueOf(visitor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodepackage(String nodePackage) {
/* 141 */     this.optionalAttrs.put("NODE_PACKAGE", nodePackage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisitorException(String visitorException) {
/* 149 */     this.optionalAttrs.put("VISITOR_EXCEPTION", visitorException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNodeprefix(String nodePrefix) {
/* 157 */     this.optionalAttrs.put("NODE_PREFIX", nodePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputdirectory(File outputDirectory) {
/* 167 */     this.outputDirectory = outputDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputfile(String outputFile) {
/* 177 */     this.outputFile = outputFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(File targetFile) {
/* 185 */     this.targetFile = targetFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavacchome(File javaccHome) {
/* 193 */     this.javaccHome = javaccHome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxmemory(String max) {
/* 203 */     this.maxMemory = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JJTree() {
/* 210 */     this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     File javaFile;
/* 221 */     this.optionalAttrs.forEach((name, value) -> this.cmdl.createArgument().setValue("-" + name + ":" + value.toString()));
/*     */ 
/*     */     
/* 224 */     if (this.targetFile == null || !this.targetFile.isFile()) {
/* 225 */       throw new BuildException("Invalid target: %s", new Object[] { this.targetFile });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (this.outputDirectory == null) {
/*     */ 
/*     */       
/* 234 */       this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + 
/* 235 */           getDefaultOutputDirectory());
/*     */       
/* 237 */       javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, (String)null));
/*     */     } else {
/*     */       
/* 240 */       if (!this.outputDirectory.isDirectory()) {
/* 241 */         throw new BuildException("'outputdirectory' " + this.outputDirectory + " is not a directory.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 247 */       this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + this.outputDirectory
/* 248 */           .getAbsolutePath()
/* 249 */           .replace('\\', '/'));
/*     */       
/* 251 */       javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, this.outputDirectory
/*     */             
/* 253 */             .getPath()));
/*     */     } 
/*     */     
/* 256 */     if (javaFile.exists() && this.targetFile
/* 257 */       .lastModified() < javaFile.lastModified()) {
/* 258 */       log("Target is already built - skipping (" + this.targetFile + ")", 3);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 263 */     if (this.outputFile != null) {
/* 264 */       this.cmdl.createArgument().setValue("-OUTPUT_FILE:" + this.outputFile
/* 265 */           .replace('\\', '/'));
/*     */     }
/*     */     
/* 268 */     this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
/*     */     
/* 270 */     Path classpath = this.cmdl.createClasspath(getProject());
/* 271 */     File javaccJar = JavaCC.getArchiveFile(this.javaccHome);
/* 272 */     classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
/* 273 */     classpath.addJavaRuntime();
/*     */     
/* 275 */     this.cmdl.setClassname(JavaCC.getMainClass(classpath, 2));
/*     */ 
/*     */     
/* 278 */     this.cmdl.setMaxmemory(this.maxMemory);
/* 279 */     Commandline.Argument arg = this.cmdl.createVmArgument();
/* 280 */     arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
/*     */     
/* 282 */     Execute process = new Execute((ExecuteStreamHandler)new LogStreamHandler(this, 2, 2), null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     log(this.cmdl.describeCommand(), 3);
/* 288 */     process.setCommandline(this.cmdl.getCommandline());
/*     */     
/*     */     try {
/* 291 */       if (process.execute() != 0) {
/* 292 */         throw new BuildException("JJTree failed.");
/*     */       }
/* 294 */     } catch (IOException e) {
/* 295 */       throw new BuildException("Failed to launch JJTree", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String createOutputFileName(File destFile, String optionalOutputFile, String outputDir) {
/* 301 */     optionalOutputFile = validateOutputFile(optionalOutputFile, outputDir);
/*     */     
/* 303 */     String jjtreeFile = destFile.getAbsolutePath().replace('\\', '/');
/*     */     
/* 305 */     if (optionalOutputFile == null || optionalOutputFile.isEmpty()) {
/* 306 */       int filePos = jjtreeFile.lastIndexOf('/');
/*     */       
/* 308 */       if (filePos >= 0) {
/* 309 */         jjtreeFile = jjtreeFile.substring(filePos + 1);
/*     */       }
/*     */       
/* 312 */       int suffixPos = jjtreeFile.lastIndexOf('.');
/*     */       
/* 314 */       if (suffixPos == -1) {
/* 315 */         optionalOutputFile = jjtreeFile + ".jj";
/*     */       } else {
/* 317 */         String currentSuffix = jjtreeFile.substring(suffixPos);
/*     */         
/* 319 */         if (currentSuffix.equals(".jj")) {
/* 320 */           optionalOutputFile = jjtreeFile + ".jj";
/*     */         } else {
/* 322 */           optionalOutputFile = jjtreeFile.substring(0, suffixPos) + ".jj";
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 328 */     if (outputDir == null || outputDir.isEmpty()) {
/* 329 */       outputDir = getDefaultOutputDirectory();
/*     */     }
/*     */     
/* 332 */     return (outputDir + "/" + optionalOutputFile).replace('\\', '/');
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
/*     */   private String validateOutputFile(String destFile, String outputDir) throws BuildException {
/* 349 */     if (destFile == null) {
/* 350 */       return null;
/*     */     }
/*     */     
/* 353 */     if (outputDir == null && (destFile
/* 354 */       .startsWith("/") || destFile.startsWith("\\"))) {
/* 355 */       String relativeOutputFile = makeOutputFileRelative(destFile);
/* 356 */       setOutputfile(relativeOutputFile);
/*     */       
/* 358 */       return relativeOutputFile;
/*     */     } 
/*     */     
/* 361 */     String root = getRoot(new File(destFile)).getAbsolutePath();
/*     */     
/* 363 */     if (root.length() > 1 && destFile
/* 364 */       .startsWith(root.substring(0, root.length() - 1))) {
/* 365 */       throw new BuildException("Drive letter in 'outputfile' not supported: %s", new Object[] { destFile });
/*     */     }
/*     */ 
/*     */     
/* 369 */     return destFile;
/*     */   }
/*     */   
/*     */   private String makeOutputFileRelative(String destFile) {
/* 373 */     StringBuilder relativePath = new StringBuilder();
/* 374 */     String defaultOutputDirectory = getDefaultOutputDirectory();
/* 375 */     int nextPos = defaultOutputDirectory.indexOf('/');
/* 376 */     int startPos = nextPos + 1;
/*     */     
/* 378 */     while (startPos > -1 && startPos < defaultOutputDirectory.length()) {
/* 379 */       relativePath.append("/..");
/* 380 */       nextPos = defaultOutputDirectory.indexOf('/', startPos);
/*     */       
/* 382 */       if (nextPos == -1) {
/* 383 */         startPos = nextPos; continue;
/*     */       } 
/* 385 */       startPos = nextPos + 1;
/*     */     } 
/*     */     
/* 388 */     return relativePath.append(destFile).toString();
/*     */   }
/*     */   
/*     */   private String getDefaultOutputDirectory() {
/* 392 */     return getProject().getBaseDir().getAbsolutePath().replace('\\', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getRoot(File file) {
/* 402 */     File root = file.getAbsoluteFile();
/*     */     
/* 404 */     while (root.getParent() != null) {
/* 405 */       root = root.getParentFile();
/*     */     }
/* 407 */     return root;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javacc/JJTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */