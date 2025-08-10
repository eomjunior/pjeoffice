/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceFactory;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.MergingMapper;
/*     */ import org.apache.tools.ant.util.ResourceUtils;
/*     */ import org.apache.tools.ant.util.SourceFileScanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpToDate
/*     */   extends Task
/*     */   implements Condition
/*     */ {
/*     */   private String property;
/*     */   private String value;
/*     */   private File sourceFile;
/*     */   private File targetFile;
/*  53 */   private List<FileSet> sourceFileSets = new Vector<>();
/*  54 */   private Union sourceResources = new Union();
/*     */ 
/*     */   
/*  57 */   protected Mapper mapperElement = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  67 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  77 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getValue() {
/*  84 */     return (this.value != null) ? this.value : "true";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetFile(File file) {
/*  94 */     this.targetFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcfile(File file) {
/* 104 */     this.sourceFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSrcfiles(FileSet fs) {
/* 112 */     this.sourceFileSets.add(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Union createSrcResources() {
/* 121 */     return this.sourceResources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/* 130 */     if (this.mapperElement != null) {
/* 131 */       throw new BuildException("Cannot define more than one mapper", 
/* 132 */           getLocation());
/*     */     }
/* 134 */     this.mapperElement = new Mapper(getProject());
/* 135 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 144 */     createMapper().add(fileNameMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() {
/* 154 */     if (this.sourceFileSets.isEmpty() && this.sourceResources.isEmpty() && this.sourceFile == null)
/*     */     {
/* 156 */       throw new BuildException("At least one srcfile or a nested <srcfiles> or <srcresources> element must be set.");
/*     */     }
/*     */ 
/*     */     
/* 160 */     if ((!this.sourceFileSets.isEmpty() || !this.sourceResources.isEmpty()) && this.sourceFile != null)
/*     */     {
/* 162 */       throw new BuildException("Cannot specify both the srcfile attribute and a nested <srcfiles> or <srcresources> element.");
/*     */     }
/*     */ 
/*     */     
/* 166 */     if (this.targetFile == null && this.mapperElement == null) {
/* 167 */       throw new BuildException("The targetfile attribute or a nested mapper element must be set.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (this.targetFile != null && !this.targetFile.exists()) {
/* 173 */       log("The targetfile \"" + this.targetFile.getAbsolutePath() + "\" does not exist.", 3);
/*     */       
/* 175 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 179 */     if (this.sourceFile != null && !this.sourceFile.exists()) {
/* 180 */       throw new BuildException("%s not found.", new Object[] { this.sourceFile
/* 181 */             .getAbsolutePath() });
/*     */     }
/*     */     
/* 184 */     boolean upToDate = true;
/* 185 */     if (this.sourceFile != null) {
/* 186 */       if (this.mapperElement == null) {
/* 187 */         upToDate = (this.targetFile.lastModified() >= this.sourceFile.lastModified());
/*     */       } else {
/* 189 */         SourceFileScanner sfs = new SourceFileScanner(this);
/* 190 */         upToDate = ((sfs.restrict(new String[] { this.sourceFile.getAbsolutePath() }, null, null, this.mapperElement
/*     */             
/* 192 */             .getImplementation())).length == 0);
/*     */       } 
/* 194 */       if (!upToDate) {
/* 195 */         log(this.sourceFile.getAbsolutePath() + " is newer than (one of) its target(s).", 3);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     for (FileSet fs : this.sourceFileSets) {
/* 207 */       if (!scanDir(fs.getDir(getProject()), fs
/* 208 */           .getDirectoryScanner(getProject()).getIncludedFiles())) {
/* 209 */         upToDate = false;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 214 */     if (upToDate) {
/* 215 */       Resource[] r = this.sourceResources.listResources();
/* 216 */       if (r.length > 0) {
/* 217 */         upToDate = ((ResourceUtils.selectOutOfDateSources((ProjectComponent)this, r, 
/* 218 */             getMapper(), (ResourceFactory)getProject())).length == 0);
/*     */       }
/*     */     } 
/*     */     
/* 222 */     return upToDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 232 */     if (this.property == null) {
/* 233 */       throw new BuildException("property attribute is required.", 
/* 234 */           getLocation());
/*     */     }
/* 236 */     boolean upToDate = eval();
/* 237 */     if (upToDate) {
/* 238 */       getProject().setNewProperty(this.property, getValue());
/* 239 */       if (this.mapperElement == null) {
/* 240 */         log("File \"" + this.targetFile.getAbsolutePath() + "\" is up-to-date.", 3);
/*     */       } else {
/*     */         
/* 243 */         log("All target files are up-to-date.", 3);
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
/*     */   
/*     */   protected boolean scanDir(File srcDir, String[] files) {
/* 256 */     SourceFileScanner sfs = new SourceFileScanner(this);
/* 257 */     FileNameMapper mapper = getMapper();
/* 258 */     File dir = srcDir;
/* 259 */     if (this.mapperElement == null) {
/* 260 */       dir = null;
/*     */     }
/* 262 */     return ((sfs.restrict(files, srcDir, dir, mapper)).length == 0);
/*     */   }
/*     */   
/*     */   private FileNameMapper getMapper() {
/* 266 */     if (this.mapperElement == null) {
/* 267 */       MergingMapper mm = new MergingMapper();
/* 268 */       mm.setTo(this.targetFile.getAbsolutePath());
/* 269 */       return (FileNameMapper)mm;
/*     */     } 
/* 271 */     return this.mapperElement.getImplementation();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/UpToDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */