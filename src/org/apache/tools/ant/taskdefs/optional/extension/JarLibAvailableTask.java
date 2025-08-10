/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarLibAvailableTask
/*     */   extends Task
/*     */ {
/*     */   private File libraryFile;
/*  44 */   private final List<ExtensionSet> extensionFileSets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String propertyName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExtensionAdapter requiredExtension;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  62 */     this.propertyName = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  71 */     this.libraryFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredExtension(ExtensionAdapter extension) {
/*  80 */     if (null != this.requiredExtension) {
/*  81 */       throw new BuildException("Can not specify extension to search for multiple times.");
/*     */     }
/*     */     
/*  84 */     this.requiredExtension = extension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredExtensionSet(ExtensionSet extensionSet) {
/*  93 */     this.extensionFileSets.add(extensionSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     Stream<Extension> extensions;
/* 103 */     validate();
/*     */     
/* 105 */     Project prj = getProject();
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (!this.extensionFileSets.isEmpty()) {
/*     */       
/* 111 */       extensions = this.extensionFileSets.stream().map(xset -> xset.toExtensions(prj)).flatMap(Stream::of);
/*     */     } else {
/* 113 */       extensions = Stream.of(
/* 114 */           Extension.getAvailable(ExtensionUtil.getManifest(this.libraryFile)));
/*     */     } 
/* 116 */     Extension test = this.requiredExtension.toExtension();
/* 117 */     if (extensions.anyMatch(x -> x.isCompatibleWith(test))) {
/* 118 */       prj.setNewProperty(this.propertyName, "true");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 128 */     if (null == this.requiredExtension) {
/* 129 */       throw new BuildException("Extension element must be specified.");
/*     */     }
/* 131 */     if (null == this.libraryFile) {
/* 132 */       if (this.extensionFileSets.isEmpty())
/* 133 */         throw new BuildException("File attribute not specified."); 
/*     */     } else {
/* 135 */       if (!this.libraryFile.exists())
/* 136 */         throw new BuildException("File '%s' does not exist.", new Object[] { this.libraryFile }); 
/* 137 */       if (!this.libraryFile.isFile())
/* 138 */         throw new BuildException("'%s' is not a file.", new Object[] { this.libraryFile }); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/JarLibAvailableTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */