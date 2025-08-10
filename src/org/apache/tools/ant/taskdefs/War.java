/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class War
/*     */   extends Jar
/*     */ {
/*  51 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String XML_DESCRIPTOR_PATH = "WEB-INF/web.xml";
/*     */ 
/*     */   
/*     */   private File deploymentDescriptor;
/*     */ 
/*     */   
/*     */   private boolean needxmlfile = true;
/*     */ 
/*     */   
/*     */   private File addedWebXmlFile;
/*     */ 
/*     */ 
/*     */   
/*     */   public War() {
/*  69 */     this.archiveType = "war";
/*  70 */     this.emptyBehavior = "create";
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
/*     */   @Deprecated
/*     */   public void setWarfile(File warFile) {
/*  83 */     setDestFile(warFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWebxml(File descr) {
/*  92 */     this.deploymentDescriptor = descr;
/*  93 */     if (!this.deploymentDescriptor.exists()) {
/*  94 */       throw new BuildException("Deployment descriptor:  does not exist.", new Object[] { this.deploymentDescriptor });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  99 */     ZipFileSet fs = new ZipFileSet();
/* 100 */     fs.setFile(this.deploymentDescriptor);
/* 101 */     fs.setFullpath("WEB-INF/web.xml");
/* 102 */     addFileset((FileSet)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNeedxmlfile(boolean needxmlfile) {
/* 110 */     this.needxmlfile = needxmlfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLib(ZipFileSet fs) {
/* 119 */     fs.setPrefix("WEB-INF/lib/");
/* 120 */     addFileset((FileSet)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClasses(ZipFileSet fs) {
/* 129 */     fs.setPrefix("WEB-INF/classes/");
/* 130 */     addFileset((FileSet)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addWebinf(ZipFileSet fs) {
/* 139 */     fs.setPrefix("WEB-INF/");
/* 140 */     addFileset((FileSet)fs);
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
/*     */   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
/* 153 */     super.initZipOutputStream(zOut);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
/* 180 */     boolean addFile = true;
/* 181 */     if ("WEB-INF/web.xml".equalsIgnoreCase(vPath))
/*     */     {
/* 183 */       if (this.addedWebXmlFile != null) {
/*     */         
/* 185 */         addFile = false;
/*     */         
/* 187 */         if (!FILE_UTILS.fileNameEquals(this.addedWebXmlFile, file)) {
/* 188 */           logWhenWriting("Warning: selected " + this.archiveType + " files include a second " + "WEB-INF/web.xml" + " which will be ignored.\nThe duplicate entry is at " + file + "\nThe file that will be used is " + this.addedWebXmlFile, 1);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 196 */         this.addedWebXmlFile = file;
/*     */         
/* 198 */         addFile = true;
/*     */         
/* 200 */         this.deploymentDescriptor = file;
/*     */       } 
/*     */     }
/* 203 */     if (addFile) {
/* 204 */       super.zipFile(file, zOut, vPath, mode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanUp() {
/* 214 */     if (this.addedWebXmlFile == null && this.deploymentDescriptor == null && this.needxmlfile && 
/*     */ 
/*     */       
/* 217 */       !isInUpdateMode() && 
/* 218 */       hasUpdatedFile()) {
/* 219 */       throw new BuildException("No WEB-INF/web.xml file was added.\nIf this is your intent, set needxmlfile='false' ");
/*     */     }
/*     */     
/* 222 */     this.addedWebXmlFile = null;
/* 223 */     super.cleanUp();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/War.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */