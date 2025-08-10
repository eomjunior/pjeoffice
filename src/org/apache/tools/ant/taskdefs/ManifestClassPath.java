/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.launch.Locator;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class ManifestClassPath
/*     */   extends Task
/*     */ {
/*     */   private String name;
/*     */   private File dir;
/*  44 */   private int maxParentLevels = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*  56 */     if (this.name == null) {
/*  57 */       throw new BuildException("Missing 'property' attribute!");
/*     */     }
/*  59 */     if (this.dir == null) {
/*  60 */       throw new BuildException("Missing 'jarfile' attribute!");
/*     */     }
/*  62 */     if (getProject().getProperty(this.name) != null) {
/*  63 */       throw new BuildException("Property '%s' already set!", new Object[] { this.name });
/*     */     }
/*  65 */     if (this.path == null) {
/*  66 */       throw new BuildException("Missing nested <classpath>!");
/*     */     }
/*     */     
/*  69 */     StringBuilder tooLongSb = new StringBuilder();
/*  70 */     for (int i = 0; i < this.maxParentLevels + 1; i++) {
/*  71 */       tooLongSb.append("../");
/*     */     }
/*  73 */     String tooLongPrefix = tooLongSb.toString();
/*     */ 
/*     */     
/*  76 */     FileUtils fileUtils = FileUtils.getFileUtils();
/*  77 */     this.dir = fileUtils.normalize(this.dir.getAbsolutePath());
/*     */     
/*  79 */     StringBuilder buffer = new StringBuilder();
/*  80 */     for (String element : this.path.list()) {
/*     */       
/*  82 */       File pathEntry = new File(element);
/*  83 */       String fullPath = pathEntry.getAbsolutePath();
/*  84 */       pathEntry = fileUtils.normalize(fullPath);
/*     */       
/*  86 */       String relPath = null;
/*  87 */       String canonicalPath = null;
/*     */       try {
/*  89 */         if (this.dir.equals(pathEntry)) {
/*  90 */           relPath = ".";
/*     */         } else {
/*  92 */           relPath = FileUtils.getRelativePath(this.dir, pathEntry);
/*     */         } 
/*     */         
/*  95 */         canonicalPath = pathEntry.getCanonicalPath();
/*     */         
/*  97 */         if (File.separatorChar != '/')
/*     */         {
/*  99 */           canonicalPath = canonicalPath.replace(File.separatorChar, '/');
/*     */         }
/* 101 */       } catch (Exception e) {
/* 102 */         throw new BuildException("error trying to get the relative path from " + this.dir + " to " + fullPath, e);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       if (relPath.equals(canonicalPath) || relPath
/* 109 */         .startsWith(tooLongPrefix)) {
/* 110 */         throw new BuildException("No suitable relative path from %s to %s", new Object[] { this.dir, fullPath });
/*     */       }
/*     */ 
/*     */       
/* 114 */       if (pathEntry.isDirectory() && !relPath.endsWith("/")) {
/* 115 */         relPath = relPath + '/';
/*     */       }
/* 117 */       relPath = Locator.encodeURI(relPath);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       buffer.append(relPath);
/* 123 */       buffer.append(' ');
/*     */     } 
/*     */ 
/*     */     
/* 127 */     getProject().setNewProperty(this.name, buffer.toString().trim());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name) {
/* 136 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJarFile(File jarfile) {
/* 146 */     File parent = jarfile.getParentFile();
/* 147 */     if (!parent.isDirectory()) {
/* 148 */       throw new BuildException("Jar's directory not found: %s", new Object[] { parent });
/*     */     }
/* 150 */     this.dir = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxParentLevels(int levels) {
/* 160 */     if (levels < 0) {
/* 161 */       throw new BuildException("maxParentLevels must not be a negative number");
/*     */     }
/*     */     
/* 164 */     this.maxParentLevels = levels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassPath(Path path) {
/* 173 */     this.path = path;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ManifestClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */