/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtensionUtil
/*     */ {
/*     */   static ArrayList<Extension> toExtensions(List<? extends ExtensionAdapter> adapters) throws BuildException {
/*  54 */     return (ArrayList<Extension>)adapters.stream().map(ExtensionAdapter::toExtension)
/*  55 */       .collect(Collectors.toCollection(ArrayList::new));
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
/*     */   static void extractExtensions(Project project, List<Extension> libraries, List<FileSet> fileset) throws BuildException {
/*  69 */     if (!fileset.isEmpty()) {
/*  70 */       Collections.addAll(libraries, getExtensions(project, fileset));
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
/*     */   private static Extension[] getExtensions(Project project, List<FileSet> libraries) throws BuildException {
/*  84 */     List<Extension> extensions = new ArrayList<>();
/*     */     
/*  86 */     for (FileSet fileSet : libraries) {
/*  87 */       boolean includeImpl = true;
/*  88 */       boolean includeURL = true;
/*     */       
/*  90 */       if (fileSet instanceof LibFileSet) {
/*  91 */         LibFileSet libFileSet = (LibFileSet)fileSet;
/*  92 */         includeImpl = libFileSet.isIncludeImpl();
/*  93 */         includeURL = libFileSet.isIncludeURL();
/*     */       } 
/*     */       
/*  96 */       DirectoryScanner scanner = fileSet.getDirectoryScanner(project);
/*  97 */       File basedir = scanner.getBasedir();
/*  98 */       for (String fileName : scanner.getIncludedFiles()) {
/*  99 */         File file = new File(basedir, fileName);
/* 100 */         loadExtensions(file, extensions, includeImpl, includeURL);
/*     */       } 
/*     */     } 
/* 103 */     return extensions.<Extension>toArray(new Extension[0]);
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
/*     */   private static void loadExtensions(File file, List<Extension> extensionList, boolean includeImpl, boolean includeURL) throws BuildException {
/*     */     
/* 118 */     try { JarFile jarFile = new JarFile(file);
/*     */       
/* 120 */       try { for (Extension extension : Extension.getAvailable(jarFile.getManifest())) {
/* 121 */           addExtension(extensionList, extension, includeImpl, includeURL);
/*     */         }
/* 123 */         jarFile.close(); } catch (Throwable throwable) { try { jarFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 124 */     { throw new BuildException(e.getMessage(), e); }
/*     */   
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
/*     */   
/*     */   private static void addExtension(List<Extension> extensionList, Extension originalExtension, boolean includeImpl, boolean includeURL) {
/* 143 */     Extension extension = originalExtension;
/* 144 */     if (!includeURL && null != extension
/* 145 */       .getImplementationURL())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       extension = new Extension(extension.getExtensionName(), extension.getSpecificationVersion().toString(), extension.getSpecificationVendor(), extension.getImplementationVersion().toString(), extension.getImplementationVendor(), extension.getImplementationVendorID(), null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     boolean hasImplAttributes = (null != extension.getImplementationURL() || null != extension.getImplementationVersion() || null != extension.getImplementationVendorID() || null != extension.getImplementationVendor());
/*     */     
/* 162 */     if (!includeImpl && hasImplAttributes)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 170 */       extension = new Extension(extension.getExtensionName(), extension.getSpecificationVersion().toString(), extension.getSpecificationVendor(), null, null, null, extension.getImplementationURL());
/*     */     }
/*     */     
/* 173 */     extensionList.add(extension);
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
/*     */   static Manifest getManifest(File file) throws BuildException {
/*     */     
/* 186 */     try { JarFile jarFile = new JarFile(file); 
/* 187 */       try { Manifest m = jarFile.getManifest();
/* 188 */         if (m == null) {
/* 189 */           throw new BuildException("%s doesn't have a MANIFEST", new Object[] { file });
/*     */         }
/* 191 */         Manifest manifest1 = m;
/* 192 */         jarFile.close(); return manifest1; } catch (Throwable throwable) { try { jarFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 193 */     { throw new BuildException(ioe.getMessage(), ioe); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/ExtensionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */