/*     */ package org.apache.tools.ant.util.depend;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import java.util.zip.ZipFile;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.VectorSet;
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
/*     */ public abstract class AbstractAnalyzer
/*     */   implements DependencyAnalyzer
/*     */ {
/*     */   public static final int MAX_LOOPS = 1000;
/*  39 */   private Path sourcePath = new Path(null);
/*     */ 
/*     */   
/*  42 */   private Path classPath = new Path(null);
/*     */ 
/*     */   
/*  45 */   private final Vector<String> rootClasses = (Vector<String>)new VectorSet();
/*     */ 
/*     */   
/*     */   private boolean determined = false;
/*     */ 
/*     */   
/*     */   private Vector<File> fileDependencies;
/*     */ 
/*     */   
/*     */   private Vector<String> classDependencies;
/*     */   
/*     */   private boolean closure = true;
/*     */ 
/*     */   
/*     */   protected AbstractAnalyzer() {
/*  60 */     reset();
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
/*     */   public void setClosure(boolean closure) {
/*  73 */     this.closure = closure;
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
/*     */   public Enumeration<File> getFileDependencies() {
/*  85 */     if (!supportsFileDependencies()) {
/*  86 */       throw new BuildException("File dependencies are not supported by this analyzer");
/*     */     }
/*     */     
/*  89 */     if (!this.determined) {
/*  90 */       determineDependencies(this.fileDependencies, this.classDependencies);
/*     */     }
/*  92 */     return this.fileDependencies.elements();
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
/*     */   public Enumeration<String> getClassDependencies() {
/* 104 */     if (!this.determined) {
/* 105 */       determineDependencies(this.fileDependencies, this.classDependencies);
/*     */     }
/* 107 */     return this.classDependencies.elements();
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
/*     */   public File getClassContainer(String classname) throws IOException {
/* 120 */     String classLocation = classname.replace('.', '/') + ".class";
/*     */ 
/*     */     
/* 123 */     return getResourceContainer(classLocation, this.classPath.list());
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
/*     */   public File getSourceContainer(String classname) throws IOException {
/* 136 */     String sourceLocation = classname.replace('.', '/') + ".java";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     return getResourceContainer(sourceLocation, this.sourcePath.list());
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
/*     */   public void addSourcePath(Path sourcePath) {
/* 154 */     if (sourcePath == null) {
/*     */       return;
/*     */     }
/* 157 */     this.sourcePath.append(sourcePath);
/* 158 */     this.sourcePath.setProject(sourcePath.getProject());
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
/*     */   public void addClassPath(Path classPath) {
/* 171 */     if (classPath == null) {
/*     */       return;
/*     */     }
/*     */     
/* 175 */     this.classPath.append(classPath);
/* 176 */     this.classPath.setProject(classPath.getProject());
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
/*     */   public void addRootClass(String className) {
/* 188 */     if (className == null) {
/*     */       return;
/*     */     }
/* 191 */     if (!this.rootClasses.contains(className)) {
/* 192 */       this.rootClasses.addElement(className);
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
/*     */   public void config(String name, Object info) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 214 */     this.rootClasses.removeAllElements();
/* 215 */     this.determined = false;
/* 216 */     this.fileDependencies = new Vector<>();
/* 217 */     this.classDependencies = new Vector<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Enumeration<String> getRootClasses() {
/* 227 */     return this.rootClasses.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isClosureRequired() {
/* 237 */     return this.closure;
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
/*     */   protected abstract void determineDependencies(Vector<File> paramVector, Vector<String> paramVector1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean supportsFileDependencies();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getResourceContainer(String resourceLocation, String[] paths) throws IOException {
/* 269 */     for (String path : paths) {
/* 270 */       File element = new File(path);
/* 271 */       if (element.exists())
/*     */       {
/*     */         
/* 274 */         if (element.isDirectory()) {
/* 275 */           File resource = new File(element, resourceLocation);
/* 276 */           if (resource.exists()) {
/* 277 */             return resource;
/*     */           }
/*     */         } else {
/*     */           
/* 281 */           ZipFile zipFile = new ZipFile(element); 
/* 282 */           try { if (zipFile.getEntry(resourceLocation) != null)
/* 283 */             { File file = element;
/*     */               
/* 285 */               zipFile.close(); return file; }  zipFile.close(); } catch (Throwable throwable) { try { zipFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*     */         
/*     */         }  } 
/* 288 */     }  return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/depend/AbstractAnalyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */