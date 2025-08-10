/*     */ package org.apache.tools.ant.types.optional.depend;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ import org.apache.tools.ant.util.depend.DependencyAnalyzer;
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
/*     */ public class DependScanner
/*     */   extends DirectoryScanner
/*     */ {
/*     */   public static final String DEFAULT_ANALYZER_CLASS = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
/*     */   private Vector<String> rootClasses;
/*     */   private Vector<String> included;
/*  52 */   private Vector<File> additionalBaseDirs = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DirectoryScanner parentScanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DependScanner(DirectoryScanner parentScanner) {
/*  69 */     this.parentScanner = parentScanner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setRootClasses(Vector<String> rootClasses) {
/*  78 */     this.rootClasses = rootClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getIncludedFiles() {
/*  88 */     return this.included.<String>toArray(new String[getIncludedFilesCount()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getIncludedFilesCount() {
/*  94 */     if (this.included == null) {
/*  95 */       throw new IllegalStateException();
/*     */     }
/*  97 */     return this.included.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void scan() throws IllegalStateException {
/*     */     DependencyAnalyzer analyzer;
/* 107 */     this.included = new Vector<>();
/* 108 */     String analyzerClassName = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 113 */       Class<? extends DependencyAnalyzer> analyzerClass = Class.forName(analyzerClassName).asSubclass(DependencyAnalyzer.class);
/* 114 */       analyzer = analyzerClass.newInstance();
/* 115 */     } catch (Exception e) {
/* 116 */       throw new BuildException("Unable to load dependency analyzer: " + analyzerClassName, e);
/*     */     } 
/*     */     
/* 119 */     analyzer.addClassPath(new Path(null, this.basedir.getPath()));
/*     */     
/* 121 */     Objects.requireNonNull(analyzer); this.additionalBaseDirs.stream().map(File::getPath).map(p -> new Path(null, p)).forEach(analyzer::addClassPath);
/*     */     
/* 123 */     Objects.requireNonNull(analyzer); this.rootClasses.forEach(analyzer::addRootClass);
/*     */ 
/*     */     
/* 126 */     Set<String> parentSet = (Set<String>)Stream.<String>of(this.parentScanner.getIncludedFiles()).collect(Collectors.toSet());
/*     */ 
/*     */     
/* 129 */     StreamUtils.enumerationAsStream(analyzer.getClassDependencies())
/* 130 */       .map(cName -> cName.replace('.', File.separatorChar) + ".class")
/* 131 */       .filter(fName -> ((new File(this.basedir, fName)).exists() && parentSet.contains(fName)))
/* 132 */       .forEach(fName -> this.included.addElement(fName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDefaultExcludes() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludedDirectories() {
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludedFiles() {
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getIncludedDirectories() {
/* 166 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIncludedDirsCount() {
/* 175 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getNotIncludedDirectories() {
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getNotIncludedFiles() {
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludes(String[] excludes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludes(String[] includes) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean isCaseSensitive) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBasedir(File baseDir) {
/* 221 */     this.additionalBaseDirs.addElement(baseDir);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/depend/DependScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */