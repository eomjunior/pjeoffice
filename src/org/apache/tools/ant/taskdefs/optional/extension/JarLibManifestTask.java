/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JarLibManifestTask
/*     */   extends Task
/*     */ {
/*     */   private static final String MANIFEST_VERSION = "1.0";
/*     */   private static final String CREATED_BY = "Created-By";
/*     */   private File destFile;
/*     */   private Extension extension;
/*  77 */   private final List<ExtensionSet> dependencies = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private final List<ExtensionSet> optionals = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private final List<ExtraAttribute> extraAttributes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File destFile) {
/*  97 */     this.destFile = destFile;
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
/*     */   public void addConfiguredExtension(ExtensionAdapter extensionAdapter) throws BuildException {
/* 110 */     if (null != this.extension) {
/* 111 */       throw new BuildException("Can not have multiple extensions defined in one library.");
/*     */     }
/*     */     
/* 114 */     this.extension = extensionAdapter.toExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredDepends(ExtensionSet extensionSet) {
/* 123 */     this.dependencies.add(extensionSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredOptions(ExtensionSet extensionSet) {
/* 132 */     this.optionals.add(extensionSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredAttribute(ExtraAttribute attribute) {
/* 141 */     this.extraAttributes.add(attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 151 */     validate();
/*     */     
/* 153 */     Manifest manifest = new Manifest();
/* 154 */     Attributes attributes = manifest.getMainAttributes();
/*     */     
/* 156 */     attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
/* 157 */     attributes.putValue("Created-By", "Apache Ant " + 
/* 158 */         getProject().getProperty("ant.version"));
/*     */     
/* 160 */     appendExtraAttributes(attributes);
/*     */     
/* 162 */     if (null != this.extension) {
/* 163 */       Extension.addExtension(this.extension, attributes);
/*     */     }
/*     */ 
/*     */     
/* 167 */     List<Extension> depends = toExtensions(this.dependencies);
/* 168 */     appendExtensionList(attributes, Extension.EXTENSION_LIST, "lib", depends.size());
/* 169 */     appendLibraryList(attributes, "lib", depends);
/*     */ 
/*     */ 
/*     */     
/* 173 */     List<Extension> option = toExtensions(this.optionals);
/* 174 */     appendExtensionList(attributes, Extension.OPTIONAL_EXTENSION_LIST, "opt", option.size());
/* 175 */     appendLibraryList(attributes, "opt", option);
/*     */     
/*     */     try {
/* 178 */       log("Generating manifest " + this.destFile.getAbsoluteFile(), 2);
/* 179 */       writeManifest(manifest);
/* 180 */     } catch (IOException ioe) {
/* 181 */       throw new BuildException(ioe.getMessage(), ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 191 */     if (null == this.destFile) {
/* 192 */       throw new BuildException("Destfile attribute not specified.");
/*     */     }
/* 194 */     if (this.destFile.exists() && !this.destFile.isFile()) {
/* 195 */       throw new BuildException("%s is not a file.", new Object[] { this.destFile });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendExtraAttributes(Attributes attributes) {
/* 206 */     for (ExtraAttribute attribute : this.extraAttributes) {
/* 207 */       attributes.putValue(attribute.getName(), attribute.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeManifest(Manifest manifest) throws IOException {
/* 218 */     OutputStream output = Files.newOutputStream(this.destFile.toPath(), new java.nio.file.OpenOption[0]); try {
/* 219 */       manifest.write(output);
/* 220 */       output.flush();
/* 221 */       if (output != null) output.close();
/*     */     
/*     */     } catch (Throwable throwable) {
/*     */       if (output != null) {
/*     */         try {
/*     */           output.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */       }
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendLibraryList(Attributes attributes, String listPrefix, List<Extension> extensions) throws BuildException {
/* 237 */     int size = extensions.size();
/* 238 */     for (int i = 0; i < size; i++) {
/* 239 */       Extension.addExtension(extensions.get(i), listPrefix + i + "-", attributes);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendExtensionList(Attributes attributes, Attributes.Name extensionKey, String listPrefix, int size) {
/* 259 */     attributes.put(extensionKey, IntStream.range(0, size)
/* 260 */         .<CharSequence>mapToObj(i -> listPrefix + i).collect(Collectors.joining(" ")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Extension> toExtensions(List<ExtensionSet> extensionSets) throws BuildException {
/* 271 */     Project prj = getProject();
/* 272 */     return (List<Extension>)extensionSets.stream().map(xset -> xset.toExtensions(prj))
/* 273 */       .flatMap(Stream::of).collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/JarLibManifestTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */