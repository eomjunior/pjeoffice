/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.ProjectHelperRepository;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.URLProvider;
/*     */ import org.apache.tools.ant.types.resources.URLResource;
/*     */ import org.apache.tools.ant.types.resources.Union;
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
/*     */ public class ImportTask
/*     */   extends Task
/*     */ {
/*  67 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private String file;
/*     */   private boolean optional;
/*  71 */   private String targetPrefix = "USE_PROJECT_NAME_AS_TARGET_PREFIX";
/*  72 */   private String prefixSeparator = ".";
/*  73 */   private final Union resources = new Union();
/*     */   
/*     */   public ImportTask() {
/*  76 */     this.resources.setCache(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptional(boolean optional) {
/*  86 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(String file) {
/*  97 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAs(String prefix) {
/* 107 */     this.targetPrefix = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixSeparator(String s) {
/* 118 */     this.prefixSeparator = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection r) {
/* 128 */     this.resources.add(r);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/* 133 */     if (this.file == null && this.resources.isEmpty()) {
/* 134 */       throw new BuildException("import requires file attribute or at least one nested resource");
/*     */     }
/*     */     
/* 137 */     if (getOwningTarget() == null || 
/* 138 */       !getOwningTarget().getName().isEmpty()) {
/* 139 */       throw new BuildException("import only allowed as a top-level task");
/*     */     }
/*     */     
/* 142 */     ProjectHelper helper = (ProjectHelper)getProject().getReference("ant.projectHelper");
/*     */     
/* 144 */     if (helper == null)
/*     */     {
/* 146 */       throw new BuildException("import requires support in ProjectHelper");
/*     */     }
/*     */     
/* 149 */     if (helper.getImportStack().isEmpty())
/*     */     {
/*     */       
/* 152 */       throw new BuildException("import requires support in ProjectHelper");
/*     */     }
/*     */     
/* 155 */     if (getLocation() == null || getLocation().getFileName() == null) {
/* 156 */       throw new BuildException("Unable to get location of import task");
/*     */     }
/*     */     
/* 159 */     Union resourcesToImport = new Union(getProject(), (ResourceCollection)this.resources);
/* 160 */     Resource fromFileAttribute = getFileAttributeResource();
/* 161 */     if (fromFileAttribute != null) {
/* 162 */       this.resources.add((ResourceCollection)fromFileAttribute);
/*     */     }
/* 164 */     for (Resource r : resourcesToImport) {
/* 165 */       importResource(helper, r);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void importResource(ProjectHelper helper, Resource importedResource) {
/* 171 */     getProject().log("Importing file " + importedResource + " from " + 
/* 172 */         getLocation().getFileName(), 3);
/*     */     
/* 174 */     if (!importedResource.isExists()) {
/*     */ 
/*     */       
/* 177 */       String message = "Cannot find " + importedResource + " imported from " + getLocation().getFileName();
/* 178 */       if (this.optional) {
/* 179 */         getProject().log(message, 3);
/*     */         return;
/*     */       } 
/* 182 */       throw new BuildException(message);
/*     */     } 
/*     */     
/* 185 */     if (!isInIncludeMode() && hasAlreadyBeenImported(importedResource, helper
/* 186 */         .getImportStack())) {
/* 187 */       getProject().log("Skipped already imported file:\n   " + importedResource + "\n", 3);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 195 */     String oldPrefix = ProjectHelper.getCurrentTargetPrefix();
/* 196 */     boolean oldIncludeMode = ProjectHelper.isInIncludeMode();
/* 197 */     String oldSep = ProjectHelper.getCurrentPrefixSeparator();
/*     */     try {
/*     */       String prefix;
/* 200 */       if (isInIncludeMode() && oldPrefix != null && this.targetPrefix != null) {
/*     */         
/* 202 */         prefix = oldPrefix + oldSep + this.targetPrefix;
/* 203 */       } else if (isInIncludeMode()) {
/* 204 */         prefix = this.targetPrefix;
/* 205 */       } else if ("USE_PROJECT_NAME_AS_TARGET_PREFIX".equals(this.targetPrefix)) {
/* 206 */         prefix = oldPrefix;
/*     */       } else {
/* 208 */         prefix = this.targetPrefix;
/*     */       } 
/* 210 */       setProjectHelperProps(prefix, this.prefixSeparator, 
/* 211 */           isInIncludeMode());
/*     */       
/* 213 */       ProjectHelper subHelper = ProjectHelperRepository.getInstance().getProjectHelperForBuildFile(importedResource);
/*     */ 
/*     */ 
/*     */       
/* 217 */       subHelper.getImportStack().addAll(helper.getImportStack());
/* 218 */       subHelper.getExtensionStack().addAll(helper.getExtensionStack());
/* 219 */       getProject().addReference("ant.projectHelper", subHelper);
/*     */       
/* 221 */       subHelper.parse(getProject(), importedResource);
/*     */ 
/*     */       
/* 224 */       getProject().addReference("ant.projectHelper", helper);
/* 225 */       helper.getImportStack().clear();
/* 226 */       helper.getImportStack().addAll(subHelper.getImportStack());
/* 227 */       helper.getExtensionStack().clear();
/* 228 */       helper.getExtensionStack().addAll(subHelper.getExtensionStack());
/* 229 */     } catch (BuildException ex) {
/* 230 */       throw ProjectHelper.addLocationToBuildException(ex, 
/* 231 */           getLocation());
/*     */     } finally {
/* 233 */       setProjectHelperProps(oldPrefix, oldSep, oldIncludeMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Resource getFileAttributeResource() {
/* 241 */     if (this.file != null) {
/* 242 */       if (isExistingAbsoluteFile(this.file)) {
/* 243 */         return (Resource)new FileResource(FILE_UTILS.normalize(this.file));
/*     */       }
/*     */ 
/*     */       
/* 247 */       File buildFile = (new File(getLocation().getFileName())).getAbsoluteFile();
/* 248 */       if (buildFile.exists()) {
/* 249 */         File buildFileParent = new File(buildFile.getParent());
/*     */         
/* 251 */         File importedFile = FILE_UTILS.resolveFile(buildFileParent, this.file);
/* 252 */         return (Resource)new FileResource(importedFile);
/*     */       } 
/*     */       
/*     */       try {
/* 256 */         URL buildFileURL = new URL(getLocation().getFileName());
/* 257 */         URL importedFile = new URL(buildFileURL, this.file);
/* 258 */         return (Resource)new URLResource(importedFile);
/* 259 */       } catch (MalformedURLException ex) {
/* 260 */         log(ex.toString(), 3);
/*     */         
/* 262 */         throw new BuildException("failed to resolve %s relative to %s", new Object[] { this.file, 
/* 263 */               getLocation().getFileName() });
/*     */       } 
/* 265 */     }  return null;
/*     */   }
/*     */   
/*     */   private boolean isExistingAbsoluteFile(String name) {
/* 269 */     File f = new File(name);
/* 270 */     return (f.isAbsolute() && f.exists());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasAlreadyBeenImported(Resource importedResource, Vector<Object> importStack) {
/* 276 */     File importedFile = importedResource.asOptional(FileProvider.class).map(FileProvider::getFile).orElse(null);
/*     */ 
/*     */     
/* 279 */     URL importedURL = importedResource.asOptional(URLProvider.class).map(URLProvider::getURL).orElse(null);
/*     */     
/* 281 */     return importStack.stream().anyMatch(o -> isOneOf(o, importedResource, importedFile, importedURL));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOneOf(Object o, Resource importedResource, File importedFile, URL importedURL) {
/* 287 */     if (o.equals(importedResource) || o.equals(importedFile) || o
/* 288 */       .equals(importedURL)) {
/* 289 */       return true;
/*     */     }
/* 291 */     if (o instanceof Resource) {
/* 292 */       if (importedFile != null) {
/* 293 */         FileProvider fp = (FileProvider)((Resource)o).as(FileProvider.class);
/* 294 */         if (fp != null && fp.getFile().equals(importedFile)) {
/* 295 */           return true;
/*     */         }
/*     */       } 
/* 298 */       if (importedURL != null) {
/* 299 */         URLProvider up = (URLProvider)((Resource)o).as(URLProvider.class);
/* 300 */         return (up != null && up.getURL().equals(importedURL));
/*     */       } 
/*     */     } 
/* 303 */     return false;
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
/*     */   protected final boolean isInIncludeMode() {
/* 322 */     return "include".equals(getTaskType());
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
/*     */   private static void setProjectHelperProps(String prefix, String prefixSep, boolean inIncludeMode) {
/* 336 */     ProjectHelper.setCurrentTargetPrefix(prefix);
/* 337 */     ProjectHelper.setCurrentPrefixSeparator(prefixSep);
/* 338 */     ProjectHelper.setInIncludeMode(inIncludeMode);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ImportTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */