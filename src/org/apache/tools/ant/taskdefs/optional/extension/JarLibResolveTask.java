/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.resolvers.AntResolver;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.resolvers.LocationResolver;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.resolvers.URLResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarLibResolveTask
/*     */   extends Task
/*     */ {
/*     */   private String propertyName;
/*     */   private Extension requiredExtension;
/*  53 */   private final List<ExtensionResolver> resolvers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkExtension = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnError = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  78 */     this.propertyName = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckExtension(boolean checkExtension) {
/*  88 */     this.checkExtension = checkExtension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failOnError) {
/*  97 */     this.failOnError = failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredLocation(LocationResolver loc) {
/* 107 */     this.resolvers.add(loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredUrl(URLResolver url) {
/* 117 */     this.resolvers.add(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredAnt(AntResolver ant) {
/* 126 */     this.resolvers.add(ant);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredExtension(ExtensionAdapter extension) {
/* 135 */     if (null != this.requiredExtension) {
/* 136 */       throw new BuildException("Can not specify extension to resolve multiple times.");
/*     */     }
/*     */     
/* 139 */     this.requiredExtension = extension.toExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 149 */     validate();
/*     */     
/* 151 */     getProject().log("Resolving extension: " + this.requiredExtension, 3);
/*     */     
/* 153 */     String candidate = getProject().getProperty(this.propertyName);
/*     */     
/* 155 */     if (null != candidate) {
/* 156 */       String message = "Property Already set to: " + candidate;
/* 157 */       if (this.failOnError) {
/* 158 */         throw new BuildException(message);
/*     */       }
/* 160 */       getProject().log(message, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 164 */     for (ExtensionResolver resolver : this.resolvers) {
/* 165 */       getProject().log("Searching for extension using Resolver:" + resolver, 3);
/*     */ 
/*     */       
/*     */       try {
/* 169 */         File file = resolver.resolve(this.requiredExtension, getProject());
/*     */         try {
/* 171 */           checkExtension(file);
/*     */           return;
/* 173 */         } catch (BuildException be) {
/* 174 */           getProject().log("File " + file + " returned by resolver failed to satisfy extension due to: " + be
/*     */               
/* 176 */               .getMessage(), 1);
/*     */         } 
/* 178 */       } catch (BuildException be) {
/* 179 */         getProject()
/* 180 */           .log("Failed to resolve extension to file using resolver " + resolver + " due to: " + be, 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 186 */     missingExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void missingExtension() {
/* 195 */     String message = "Unable to resolve extension to a file";
/* 196 */     if (this.failOnError) {
/* 197 */       throw new BuildException("Unable to resolve extension to a file");
/*     */     }
/* 199 */     getProject().log("Unable to resolve extension to a file", 0);
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
/*     */   private void checkExtension(File file) {
/* 211 */     if (!file.exists()) {
/* 212 */       throw new BuildException("File %s does not exist", new Object[] { file });
/*     */     }
/* 214 */     if (!file.isFile()) {
/* 215 */       throw new BuildException("File %s is not a file", new Object[] { file });
/*     */     }
/* 217 */     if (!this.checkExtension) {
/* 218 */       getProject().log("Setting property to " + file + " without verifying library satisfies extension", 3);
/*     */       
/* 220 */       setLibraryProperty(file);
/*     */     } else {
/* 222 */       getProject().log("Checking file " + file + " to see if it satisfies extension", 3);
/*     */       
/* 224 */       Manifest manifest = ExtensionUtil.getManifest(file);
/* 225 */       for (Extension extension : Extension.getAvailable(manifest)) {
/* 226 */         if (extension.isCompatibleWith(this.requiredExtension)) {
/* 227 */           setLibraryProperty(file);
/*     */           return;
/*     */         } 
/*     */       } 
/* 231 */       String message = "File " + file + " skipped as it does not satisfy extension";
/*     */       
/* 233 */       getProject().log(message, 3);
/* 234 */       throw new BuildException(message);
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
/*     */   private void setLibraryProperty(File file) {
/* 246 */     getProject().setNewProperty(this.propertyName, file.getAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 255 */     if (null == this.propertyName) {
/* 256 */       throw new BuildException("Property attribute must be specified.");
/*     */     }
/* 258 */     if (null == this.requiredExtension)
/* 259 */       throw new BuildException("Extension element must be specified."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/JarLibResolveTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */