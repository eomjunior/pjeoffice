/*     */ package org.apache.tools.ant.taskdefs.optional.depend;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
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
/*     */ public class Depend
/*     */   extends MatchingTask
/*     */ {
/*     */   private static final int ONE_SECOND = 1000;
/*     */   private Path srcPath;
/*     */   private Path destPath;
/*     */   private File cache;
/*     */   private Map<String, Map<String, ClassFileInfo>> affectedClassMap;
/*     */   private Map<String, ClassFileInfo> classFileInfoMap;
/*     */   private Map<String, Set<File>> classpathDependencies;
/*     */   private Map<String, String> outOfDateClasses;
/*     */   
/*     */   private static class ClassFileInfo
/*     */   {
/*     */     private File absoluteFile;
/*     */     private String className;
/*     */     private File sourceFile;
/*     */     
/*     */     private ClassFileInfo() {}
/*     */     
/*     */     private boolean isUserWarned = false;
/*     */   }
/*     */   private boolean closure = false;
/*     */   private boolean warnOnRmiStubs = true;
/*     */   private boolean dump = false;
/*     */   private Path dependClasspath;
/*     */   private static final String CACHE_FILE_NAME = "dependencies.txt";
/*     */   private static final String CLASSNAME_PREPEND = "||:";
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 139 */     if (this.dependClasspath == null) {
/* 140 */       this.dependClasspath = classpath;
/*     */     } else {
/* 142 */       this.dependClasspath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 152 */     return this.dependClasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 161 */     if (this.dependClasspath == null) {
/* 162 */       this.dependClasspath = new Path(getProject());
/*     */     }
/* 164 */     return this.dependClasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 174 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarnOnRmiStubs(boolean warnOnRmiStubs) {
/* 184 */     this.warnOnRmiStubs = warnOnRmiStubs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, List<String>> readCachedDependencies(File depFile) throws IOException {
/* 194 */     Map<String, List<String>> dependencyMap = new HashMap<>();
/*     */     
/* 196 */     int prependLength = "||:".length();
/*     */     
/* 198 */     BufferedReader in = new BufferedReader(new FileReader(depFile)); 
/* 199 */     try { List<String> dependencyList = null;
/*     */       String line;
/* 201 */       while ((line = in.readLine()) != null) {
/* 202 */         if (line.startsWith("||:")) {
/* 203 */           String className = line.substring(prependLength);
/* 204 */           dependencyList = dependencyMap.computeIfAbsent(className, k -> new ArrayList()); continue;
/*     */         } 
/* 206 */         if (dependencyList != null) {
/* 207 */           dependencyList.add(line);
/*     */         }
/*     */       } 
/* 210 */       in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 211 */      return dependencyMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeCachedDependencies(Map<String, List<String>> dependencyMap) throws IOException {
/* 222 */     if (this.cache != null) {
/* 223 */       this.cache.mkdirs();
/* 224 */       File depFile = new File(this.cache, "dependencies.txt");
/* 225 */       BufferedWriter pw = new BufferedWriter(new FileWriter(depFile));
/*     */       try {
/* 227 */         for (Map.Entry<String, List<String>> e : dependencyMap
/* 228 */           .entrySet()) {
/* 229 */           pw.write(String.format("%s%s%n", new Object[] { "||:", e.getKey() }));
/* 230 */           for (String s : e.getValue()) {
/* 231 */             pw.write(s);
/* 232 */             pw.newLine();
/*     */           } 
/*     */         } 
/* 235 */         pw.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           pw.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     }  } private Path getCheckClassPath() { Path p;
/* 245 */     if (this.dependClasspath == null) {
/* 246 */       return null;
/*     */     }
/*     */     
/* 249 */     Set<Resource> dependNotInDest = new LinkedHashSet<>();
/* 250 */     Objects.requireNonNull(dependNotInDest); this.dependClasspath.forEach(dependNotInDest::add);
/* 251 */     Objects.requireNonNull(dependNotInDest); this.destPath.forEach(dependNotInDest::remove);
/*     */ 
/*     */     
/* 254 */     if (dependNotInDest.isEmpty()) {
/* 255 */       p = null;
/*     */     } else {
/* 257 */       p = new Path(getProject());
/* 258 */       Objects.requireNonNull(p); dependNotInDest.forEach(p::add);
/*     */     } 
/*     */     
/* 261 */     log("Classpath without dest dir is " + p, 4);
/* 262 */     return p; }
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
/*     */   private void determineDependencies() throws IOException {
/* 284 */     this.affectedClassMap = new HashMap<>();
/* 285 */     this.classFileInfoMap = new HashMap<>();
/* 286 */     boolean cacheDirty = false;
/*     */     
/* 288 */     Map<String, List<String>> dependencyMap = new HashMap<>();
/* 289 */     File cacheFile = null;
/* 290 */     boolean cacheFileExists = true;
/* 291 */     long cacheLastModified = Long.MAX_VALUE;
/*     */ 
/*     */     
/* 294 */     if (this.cache != null) {
/* 295 */       cacheFile = new File(this.cache, "dependencies.txt");
/* 296 */       cacheFileExists = cacheFile.exists();
/* 297 */       cacheLastModified = cacheFile.lastModified();
/* 298 */       if (cacheFileExists) {
/* 299 */         dependencyMap = readCachedDependencies(cacheFile);
/*     */       }
/*     */     } 
/* 302 */     for (ClassFileInfo info : getClassFiles()) {
/* 303 */       log("Adding class info for " + info.className, 4);
/* 304 */       this.classFileInfoMap.put(info.className, info);
/*     */       
/* 306 */       List<String> dependencyList = null;
/*     */       
/* 308 */       if (this.cache != null)
/*     */       {
/*     */         
/* 311 */         if (cacheFileExists && cacheLastModified > info
/* 312 */           .absoluteFile.lastModified())
/*     */         {
/*     */           
/* 315 */           dependencyList = dependencyMap.get(info.className);
/*     */         }
/*     */       }
/*     */       
/* 319 */       if (dependencyList == null) {
/*     */         
/* 321 */         AntAnalyzer antAnalyzer = new AntAnalyzer();
/* 322 */         antAnalyzer.addRootClass(info.className);
/* 323 */         antAnalyzer.addClassPath(this.destPath);
/* 324 */         antAnalyzer.setClosure(false);
/* 325 */         dependencyList = Collections.list(antAnalyzer.getClassDependencies());
/* 326 */         dependencyList.forEach(o -> log("Class " + info.className + " depends on " + o, 4));
/*     */         
/* 328 */         cacheDirty = true;
/* 329 */         dependencyMap.put(info.className, dependencyList);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 334 */       for (String dependentClass : dependencyList) {
/* 335 */         ((Map<String, ClassFileInfo>)this.affectedClassMap
/* 336 */           .computeIfAbsent(dependentClass, k -> new HashMap<>()))
/* 337 */           .put(info.className, info);
/* 338 */         log(dependentClass + " affects " + info.className, 4);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 343 */     this.classpathDependencies = null;
/* 344 */     Path checkPath = getCheckClassPath();
/* 345 */     if (checkPath != null)
/*     */     
/* 347 */     { this.classpathDependencies = new HashMap<>();
/* 348 */       AntClassLoader loader = getProject().createClassLoader(checkPath);
/*     */       
/* 350 */       try { Map<String, Object> classpathFileCache = new HashMap<>();
/* 351 */         Object nullFileMarker = new Object();
/* 352 */         for (Map.Entry<String, List<String>> e : dependencyMap.entrySet()) {
/* 353 */           String className = e.getKey();
/* 354 */           log("Determining classpath dependencies for " + className, 4);
/*     */           
/* 356 */           List<String> dependencyList = e.getValue();
/* 357 */           Set<File> dependencies = new HashSet<>();
/* 358 */           this.classpathDependencies.put(className, dependencies);
/* 359 */           for (String dependency : dependencyList) {
/* 360 */             log("Looking for " + dependency, 4);
/*     */             
/* 362 */             Object classpathFileObject = classpathFileCache.get(dependency);
/* 363 */             if (classpathFileObject == null) {
/* 364 */               classpathFileObject = nullFileMarker;
/*     */               
/* 366 */               if (!dependency.startsWith("java.") && 
/* 367 */                 !dependency.startsWith("javax.")) {
/*     */                 
/* 369 */                 URL classURL = loader.getResource(dependency.replace('.', '/') + ".class");
/* 370 */                 log("URL is " + classURL, 4);
/* 371 */                 if (classURL != null) {
/* 372 */                   if ("jar".equals(classURL.getProtocol())) {
/* 373 */                     String jarFilePath = classURL.getFile();
/* 374 */                     int classMarker = jarFilePath.indexOf('!');
/* 375 */                     jarFilePath = jarFilePath.substring(0, classMarker);
/* 376 */                     if (jarFilePath.startsWith("file:")) {
/*     */ 
/*     */                       
/* 379 */                       classpathFileObject = new File(FileUtils.getFileUtils().fromURI(jarFilePath));
/*     */                     } else {
/* 381 */                       throw new IOException("Bizarre nested path in jar: protocol: " + jarFilePath);
/*     */                     }
/*     */                   
/*     */                   }
/* 385 */                   else if ("file".equals(classURL.getProtocol())) {
/*     */                     
/* 387 */                     classpathFileObject = new File(FileUtils.getFileUtils().fromURI(classURL
/* 388 */                           .toExternalForm()));
/*     */                   } 
/* 390 */                   log("Class " + className + " depends on " + classpathFileObject + " due to " + dependency, 4);
/*     */                 }
/*     */               
/*     */               } else {
/*     */                 
/* 395 */                 log("Ignoring base classlib dependency " + dependency, 4);
/*     */               } 
/*     */               
/* 398 */               classpathFileCache.put(dependency, classpathFileObject);
/*     */             } 
/* 400 */             if (classpathFileObject != nullFileMarker) {
/*     */               
/* 402 */               File jarFile = (File)classpathFileObject;
/* 403 */               log("Adding a classpath dependency on " + jarFile, 4);
/*     */               
/* 405 */               dependencies.add(jarFile);
/*     */             } 
/*     */           } 
/*     */         } 
/* 409 */         if (loader != null) loader.close();  } catch (Throwable throwable) { if (loader != null)
/*     */           try { loader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  }
/* 411 */     else { log("No classpath to check", 4); }
/*     */ 
/*     */ 
/*     */     
/* 415 */     if (this.cache != null && cacheDirty) {
/* 416 */       writeCachedDependencies(dependencyMap);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int deleteAllAffectedFiles() {
/* 427 */     int count = 0;
/* 428 */     for (String className : this.outOfDateClasses.keySet()) {
/* 429 */       count += deleteAffectedFiles(className);
/* 430 */       ClassFileInfo classInfo = this.classFileInfoMap.get(className);
/* 431 */       if (classInfo != null && classInfo.absoluteFile.exists()) {
/* 432 */         if (classInfo.sourceFile == null) {
/* 433 */           warnOutOfDateButNotDeleted(classInfo, className, className); continue;
/*     */         } 
/* 435 */         classInfo.absoluteFile.delete();
/* 436 */         count++;
/*     */       } 
/*     */     } 
/*     */     
/* 440 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int deleteAffectedFiles(String className) {
/* 451 */     int count = 0;
/*     */     
/* 453 */     Map<String, ClassFileInfo> affectedClasses = this.affectedClassMap.get(className);
/* 454 */     if (affectedClasses == null) {
/* 455 */       return count;
/*     */     }
/* 457 */     for (Map.Entry<String, ClassFileInfo> e : affectedClasses.entrySet()) {
/* 458 */       String affectedClass = e.getKey();
/* 459 */       ClassFileInfo affectedClassInfo = e.getValue();
/*     */       
/* 461 */       if (!affectedClassInfo.absoluteFile.exists()) {
/*     */         continue;
/*     */       }
/*     */       
/* 465 */       if (affectedClassInfo.sourceFile == null) {
/* 466 */         warnOutOfDateButNotDeleted(affectedClassInfo, affectedClass, className);
/*     */         
/*     */         continue;
/*     */       } 
/* 470 */       log("Deleting file " + affectedClassInfo.absoluteFile.getPath() + " since " + className + " out of date", 3);
/*     */ 
/*     */       
/* 473 */       affectedClassInfo.absoluteFile.delete();
/* 474 */       count++;
/* 475 */       if (this.closure) {
/* 476 */         count += deleteAffectedFiles(affectedClass);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 481 */       if (!affectedClass.contains("$")) {
/*     */         continue;
/*     */       }
/*     */       
/* 485 */       String topLevelClassName = affectedClass.substring(0, affectedClass.indexOf("$"));
/* 486 */       log("Top level class = " + topLevelClassName, 3);
/*     */ 
/*     */       
/* 489 */       ClassFileInfo topLevelClassInfo = this.classFileInfoMap.get(topLevelClassName);
/* 490 */       if (topLevelClassInfo != null && topLevelClassInfo
/* 491 */         .absoluteFile.exists()) {
/* 492 */         log("Deleting file " + topLevelClassInfo
/* 493 */             .absoluteFile.getPath() + " since one of its inner classes was removed", 3);
/*     */ 
/*     */         
/* 496 */         topLevelClassInfo.absoluteFile.delete();
/* 497 */         count++;
/* 498 */         if (this.closure) {
/* 499 */           count += deleteAffectedFiles(topLevelClassName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 504 */     return count;
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
/*     */   private void warnOutOfDateButNotDeleted(ClassFileInfo affectedClassInfo, String affectedClass, String className) {
/* 518 */     if (affectedClassInfo.isUserWarned) {
/*     */       return;
/*     */     }
/* 521 */     int level = 1;
/* 522 */     if (!this.warnOnRmiStubs)
/*     */     {
/*     */ 
/*     */       
/* 526 */       if (isRmiStub(affectedClass, className)) {
/* 527 */         level = 3;
/*     */       }
/*     */     }
/* 530 */     log("The class " + affectedClass + " in file " + affectedClassInfo
/* 531 */         .absoluteFile.getPath() + " is out of date due to " + className + " but has not been deleted because its source file could not be determined", level);
/*     */ 
/*     */ 
/*     */     
/* 535 */     affectedClassInfo.isUserWarned = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRmiStub(String affectedClass, String className) {
/* 545 */     return (isStub(affectedClass, className, "_Stub") || 
/* 546 */       isStub(affectedClass, className, "_Skel") || 
/* 547 */       isStub(affectedClass, className, "_Stub") || 
/* 548 */       isStub(affectedClass, className, "_Skel"));
/*     */   }
/*     */   
/*     */   private boolean isStub(String affectedClass, String baseClass, String suffix) {
/* 552 */     return (baseClass + suffix).equals(affectedClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpDependencies() {
/* 559 */     log("Reverse Dependency Dump for " + this.affectedClassMap.size() + " classes:", 4);
/*     */ 
/*     */     
/* 562 */     this.affectedClassMap.forEach((className, affectedClasses) -> {
/*     */           log(" Class " + className + " affects:", 4);
/*     */ 
/*     */           
/*     */           affectedClasses.forEach(());
/*     */         });
/*     */     
/* 569 */     if (this.classpathDependencies != null) {
/* 570 */       log("Classpath file dependencies (Forward):", 4);
/*     */       
/* 572 */       this.classpathDependencies.forEach((className, dependencies) -> {
/*     */             log(" Class " + className + " depends on:", 4);
/*     */             dependencies.forEach(());
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void determineOutOfDateClasses() {
/* 580 */     this.outOfDateClasses = new HashMap<>();
/* 581 */     directories((ResourceCollection)this.srcPath).forEach(srcDir -> {
/*     */           DirectoryScanner ds = getDirectoryScanner(srcDir);
/*     */           
/*     */           scanDir(srcDir, ds.getIncludedFiles());
/*     */         });
/*     */     
/* 587 */     if (this.classpathDependencies == null) {
/*     */       return;
/*     */     }
/*     */     
/* 591 */     for (Map.Entry<String, Set<File>> e : this.classpathDependencies.entrySet()) {
/* 592 */       String className = e.getKey();
/* 593 */       if (this.outOfDateClasses.containsKey(className)) {
/*     */         continue;
/*     */       }
/* 596 */       ClassFileInfo info = this.classFileInfoMap.get(className);
/*     */ 
/*     */ 
/*     */       
/* 600 */       if (info != null) {
/* 601 */         for (File classpathFile : e.getValue()) {
/* 602 */           if (classpathFile.lastModified() > info.absoluteFile
/* 603 */             .lastModified()) {
/* 604 */             log("Class " + className + " is out of date with respect to " + classpathFile, 4);
/*     */ 
/*     */             
/* 607 */             this.outOfDateClasses.put(className, className);
/*     */           } 
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
/*     */   public void execute() throws BuildException {
/*     */     try {
/*     */       int summaryLogLevel;
/* 623 */       long start = System.currentTimeMillis();
/* 624 */       if (this.srcPath == null) {
/* 625 */         throw new BuildException("srcdir attribute must be set", 
/* 626 */             getLocation());
/*     */       }
/*     */       
/* 629 */       if (!directories((ResourceCollection)this.srcPath).findAny().isPresent()) {
/* 630 */         throw new BuildException("srcdir attribute must be non-empty", 
/* 631 */             getLocation());
/*     */       }
/*     */       
/* 634 */       if (this.destPath == null) {
/* 635 */         this.destPath = this.srcPath;
/*     */       }
/*     */       
/* 638 */       if (this.cache != null && this.cache.exists() && !this.cache.isDirectory()) {
/* 639 */         throw new BuildException("The cache, if specified, must point to a directory");
/*     */       }
/*     */ 
/*     */       
/* 643 */       if (this.cache != null && !this.cache.exists()) {
/* 644 */         this.cache.mkdirs();
/*     */       }
/*     */       
/* 647 */       determineDependencies();
/* 648 */       if (this.dump) {
/* 649 */         dumpDependencies();
/*     */       }
/* 651 */       determineOutOfDateClasses();
/* 652 */       int count = deleteAllAffectedFiles();
/*     */       
/* 654 */       long duration = (System.currentTimeMillis() - start) / 1000L;
/*     */ 
/*     */       
/* 657 */       if (count > 0) {
/* 658 */         summaryLogLevel = 2;
/*     */       } else {
/* 660 */         summaryLogLevel = 4;
/*     */       } 
/*     */       
/* 663 */       log("Deleted " + count + " out of date files in " + duration + " seconds", summaryLogLevel);
/*     */     }
/* 665 */     catch (Exception e) {
/* 666 */       throw new BuildException(e);
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
/*     */   protected void scanDir(File srcDir, String[] files) {
/* 680 */     for (String f : files) {
/* 681 */       File srcFile = new File(srcDir, f);
/* 682 */       if (f.endsWith(".java")) {
/* 683 */         String filePath = srcFile.getPath();
/*     */         
/* 685 */         String className = filePath.substring(srcDir.getPath().length() + 1, filePath
/* 686 */             .length() - ".java".length());
/* 687 */         className = ClassFileUtils.convertSlashName(className);
/*     */         
/* 689 */         ClassFileInfo info = this.classFileInfoMap.get(className);
/* 690 */         if (info == null) {
/*     */           
/* 692 */           this.outOfDateClasses.put(className, className);
/* 693 */         } else if (srcFile.lastModified() > info.absoluteFile
/* 694 */           .lastModified()) {
/* 695 */           this.outOfDateClasses.put(className, className);
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
/*     */   private List<ClassFileInfo> getClassFiles() {
/* 708 */     List<ClassFileInfo> classFileList = new ArrayList<>();
/*     */     
/* 710 */     directories((ResourceCollection)this.destPath)
/* 711 */       .forEach(dir -> addClassFiles(classFileList, dir, dir));
/*     */     
/* 713 */     return classFileList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File findSourceFile(String classname, File sourceFileKnownToExist) {
/*     */     String sourceFilename;
/* 725 */     int innerIndex = classname.indexOf('$');
/* 726 */     if (innerIndex != -1) {
/* 727 */       sourceFilename = classname.substring(0, innerIndex) + ".java";
/*     */     } else {
/* 729 */       sourceFilename = classname + ".java";
/*     */     } 
/*     */     
/* 732 */     return directories((ResourceCollection)this.srcPath)
/* 733 */       .<File>map(d -> new File(d, sourceFilename)).filter(
/* 734 */         Predicate.<File>isEqual(sourceFileKnownToExist).or(File::exists))
/* 735 */       .findFirst().orElse(null);
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
/*     */   private void addClassFiles(List<ClassFileInfo> classFileList, File dir, File root) {
/* 751 */     File[] children = dir.listFiles();
/*     */     
/* 753 */     if (children == null) {
/*     */       return;
/*     */     }
/*     */     
/* 757 */     int rootLength = root.getPath().length();
/* 758 */     File sourceFileKnownToExist = null;
/* 759 */     for (File file : children) {
/* 760 */       if (file.getName().endsWith(".class")) {
/* 761 */         ClassFileInfo info = new ClassFileInfo();
/* 762 */         info.absoluteFile = file;
/*     */         
/* 764 */         String relativeName = file.getPath().substring(rootLength + 1, file
/* 765 */             .getPath().length() - ".class".length());
/*     */         
/* 767 */         info.className = 
/* 768 */           ClassFileUtils.convertSlashName(relativeName);
/* 769 */         info.sourceFile = 
/* 770 */           sourceFileKnownToExist = findSourceFile(relativeName, sourceFileKnownToExist);
/* 771 */         classFileList.add(info);
/*     */       } else {
/* 773 */         addClassFiles(classFileList, file, root);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcdir(Path srcPath) {
/* 784 */     this.srcPath = srcPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestDir(Path destPath) {
/* 793 */     this.destPath = destPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(File cache) {
/* 802 */     this.cache = cache;
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
/*     */   public void setClosure(boolean closure) {
/* 814 */     this.closure = closure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDump(boolean dump) {
/* 824 */     this.dump = dump;
/*     */   }
/*     */   
/*     */   private Stream<File> directories(ResourceCollection rc) {
/* 828 */     return rc.stream().map(r -> (FileProvider)r.as(FileProvider.class))
/* 829 */       .filter(Objects::nonNull).map(FileProvider::getFile)
/* 830 */       .filter(File::isDirectory);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/Depend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */