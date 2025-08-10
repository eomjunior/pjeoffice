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
/*     */ public class Ear
/*     */   extends Jar
/*     */ {
/*  37 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File deploymentDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean descriptorAdded;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String XML_DESCRIPTOR_PATH = "META-INF/application.xml";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setEarfile(File earFile) {
/*  60 */     setDestFile(earFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppxml(File descr) {
/*  68 */     this.deploymentDescriptor = descr;
/*  69 */     if (!this.deploymentDescriptor.exists()) {
/*  70 */       throw new BuildException("Deployment descriptor: %s does not exist.", new Object[] { this.deploymentDescriptor });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     ZipFileSet fs = new ZipFileSet();
/*  77 */     fs.setFile(this.deploymentDescriptor);
/*  78 */     fs.setFullpath("META-INF/application.xml");
/*  79 */     addFileset((FileSet)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArchives(ZipFileSet fs) {
/*  90 */     fs.setPrefix("/");
/*  91 */     addFileset((FileSet)fs);
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
/* 104 */     if (this.deploymentDescriptor == null && !isInUpdateMode()) {
/* 105 */       throw new BuildException("appxml attribute is required", getLocation());
/*     */     }
/*     */     
/* 108 */     super.initZipOutputStream(zOut);
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
/*     */   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
/* 128 */     if ("META-INF/application.xml".equalsIgnoreCase(vPath)) {
/* 129 */       if (this.deploymentDescriptor == null || 
/* 130 */         !FILE_UTILS.fileNameEquals(this.deploymentDescriptor, file) || this.descriptorAdded) {
/*     */         
/* 132 */         logWhenWriting("Warning: selected " + this.archiveType + " files include a " + "META-INF/application.xml" + " which will be ignored (please use appxml attribute to " + this.archiveType + " task)", 1);
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 139 */         super.zipFile(file, zOut, vPath, mode);
/* 140 */         this.descriptorAdded = true;
/*     */       } 
/*     */     } else {
/* 143 */       super.zipFile(file, zOut, vPath, mode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanUp() {
/* 153 */     this.descriptorAdded = false;
/* 154 */     super.cleanUp();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Ear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */