/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class MakeUrl
/*     */   extends Task
/*     */ {
/*     */   public static final String ERROR_MISSING_FILE = "A source file is missing: ";
/*     */   public static final String ERROR_NO_PROPERTY = "No property defined";
/*     */   public static final String ERROR_NO_FILES = "No files defined";
/*     */   private String property;
/*     */   private File file;
/*  65 */   private String separator = " ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private List<FileSet> filesets = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private List<Path> paths = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean validate = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  88 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  97 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileSet(FileSet fileset) {
/* 107 */     this.filesets.add(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeparator(String separator) {
/* 116 */     this.separator = separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidate(boolean validate) {
/* 126 */     this.validate = validate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPath(Path path) {
/* 136 */     this.paths.add(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String filesetsToURL() {
/* 145 */     if (this.filesets.isEmpty()) {
/* 146 */       return "";
/*     */     }
/* 148 */     int count = 0;
/* 149 */     StringBuilder urls = new StringBuilder();
/* 150 */     for (FileSet fs : this.filesets) {
/* 151 */       DirectoryScanner scanner = fs.getDirectoryScanner(getProject());
/* 152 */       for (String file : scanner.getIncludedFiles()) {
/* 153 */         File f = new File(scanner.getBasedir(), file);
/* 154 */         validateFile(f);
/* 155 */         String asUrl = toURL(f);
/* 156 */         urls.append(asUrl);
/* 157 */         log(asUrl, 4);
/* 158 */         urls.append(this.separator);
/* 159 */         count++;
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     return stripTrailingSeparator(urls, count);
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
/*     */   private String stripTrailingSeparator(StringBuilder urls, int count) {
/* 176 */     if (count > 0) {
/* 177 */       urls.delete(urls.length() - this.separator.length(), urls.length());
/* 178 */       return new String(urls);
/*     */     } 
/* 180 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String pathsToURL() {
/* 190 */     if (this.paths.isEmpty()) {
/* 191 */       return "";
/*     */     }
/* 193 */     int count = 0;
/* 194 */     StringBuilder urls = new StringBuilder();
/* 195 */     for (Path path : this.paths) {
/* 196 */       for (String element : path.list()) {
/* 197 */         File f = new File(element);
/* 198 */         validateFile(f);
/* 199 */         String asUrl = toURL(f);
/* 200 */         urls.append(asUrl);
/* 201 */         log(asUrl, 4);
/* 202 */         urls.append(this.separator);
/* 203 */         count++;
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     return stripTrailingSeparator(urls, count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateFile(File fileToCheck) {
/* 217 */     if (this.validate && !fileToCheck.exists()) {
/* 218 */       throw new BuildException("A source file is missing: " + fileToCheck);
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
/*     */     String url;
/* 230 */     validate();
/*     */     
/* 232 */     if (getProject().getProperty(this.property) != null) {
/*     */       return;
/*     */     }
/*     */     
/* 236 */     String filesetURL = filesetsToURL();
/* 237 */     if (this.file == null) {
/* 238 */       url = filesetURL;
/*     */     } else {
/* 240 */       validateFile(this.file);
/* 241 */       url = toURL(this.file);
/*     */       
/* 243 */       if (!filesetURL.isEmpty()) {
/* 244 */         url = url + this.separator + filesetURL;
/*     */       }
/*     */     } 
/*     */     
/* 248 */     String pathURL = pathsToURL();
/* 249 */     if (!pathURL.isEmpty()) {
/* 250 */       if (url.isEmpty()) {
/* 251 */         url = pathURL;
/*     */       } else {
/* 253 */         url = url + this.separator + pathURL;
/*     */       } 
/*     */     }
/* 256 */     log("Setting " + this.property + " to URL " + url, 3);
/* 257 */     getProject().setNewProperty(this.property, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() {
/* 266 */     if (this.property == null) {
/* 267 */       throw new BuildException("No property defined");
/*     */     }
/* 269 */     if (this.file == null && this.filesets.isEmpty() && this.paths.isEmpty()) {
/* 270 */       throw new BuildException("No files defined");
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
/*     */   private String toURL(File fileToConvert) {
/* 283 */     return FileUtils.getFileUtils().toURI(fileToConvert.getAbsolutePath());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/MakeUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */