/*     */ package org.apache.tools.ant.taskdefs.optional.depend;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.types.resources.ZipResource;
/*     */ import org.apache.tools.ant.util.depend.AbstractAnalyzer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntAnalyzer
/*     */   extends AbstractAnalyzer
/*     */ {
/*     */   protected void determineDependencies(Vector<File> files, Vector<String> classes) {
/*  53 */     Set<String> dependencies = new HashSet<>();
/*  54 */     Set<File> containers = new HashSet<>();
/*  55 */     Set<String> toAnalyze = new HashSet<>(Collections.list(getRootClasses()));
/*  56 */     Set<String> analyzedDeps = new HashSet<>();
/*     */     
/*  58 */     int count = 0;
/*  59 */     int maxCount = isClosureRequired() ? 1000 : 1;
/*  60 */     while (!toAnalyze.isEmpty() && count++ < maxCount) {
/*  61 */       analyzedDeps.clear();
/*  62 */       for (String classname : toAnalyze) {
/*  63 */         dependencies.add(classname);
/*  64 */         File container = null;
/*     */         try {
/*  66 */           container = getClassContainer(classname);
/*  67 */         } catch (IOException iOException) {}
/*     */ 
/*     */         
/*  70 */         if (container != null) {
/*  71 */           containers.add(container);
/*     */ 
/*     */ 
/*     */           
/*  75 */           try { InputStream inStream = container.getName().endsWith(".class") ? Files.newInputStream(Paths.get(container.getPath(), new String[0]), new java.nio.file.OpenOption[0]) : ZipResource.getZipEntryStream(new ZipFile(container.getPath(), "UTF-8"), classname
/*  76 */                 .replace('.', '/') + ".class"); 
/*  77 */             try { ClassFile classFile = new ClassFile();
/*  78 */               classFile.read(inStream);
/*  79 */               analyzedDeps.addAll(classFile.getClassRefs());
/*  80 */               if (inStream != null) inStream.close();  } catch (Throwable throwable) { if (inStream != null) try { inStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  86 */       toAnalyze.clear();
/*     */ 
/*     */       
/*  89 */       Objects.requireNonNull(toAnalyze); analyzedDeps.stream().filter(className -> !dependencies.contains(className)).forEach(toAnalyze::add);
/*     */     } 
/*     */ 
/*     */     
/*  93 */     dependencies.addAll(analyzedDeps);
/*     */     
/*  95 */     files.removeAllElements();
/*  96 */     files.addAll(containers);
/*  97 */     classes.removeAllElements();
/*  98 */     classes.addAll(dependencies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supportsFileDependencies() {
/* 108 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/AntAnalyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */