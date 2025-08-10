/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.LogLevel;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.LogOutputResource;
/*     */ import org.apache.tools.ant.types.resources.StringResource;
/*     */ import org.apache.tools.ant.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Echo
/*     */   extends Task
/*     */ {
/*  44 */   protected String message = "";
/*  45 */   protected File file = null;
/*     */   
/*     */   protected boolean append = false;
/*  48 */   private String encoding = "";
/*     */   
/*     */   private boolean force = false;
/*     */   
/*  52 */   protected int logLevel = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Resource output;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     try {
/*  64 */       ResourceUtils.copyResource((Resource)new StringResource(
/*  65 */             this.message.isEmpty() ? System.lineSeparator() : this.message), 
/*  66 */           (this.output == null) ? (Resource)new LogOutputResource((ProjectComponent)this, this.logLevel) : this.output, null, null, false, false, this.append, null, 
/*     */           
/*  68 */           this.encoding.isEmpty() ? null : this.encoding, getProject(), this.force);
/*  69 */     } catch (IOException ioe) {
/*  70 */       throw new BuildException(ioe, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String msg) {
/*  80 */     this.message = (msg == null) ? "" : msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  89 */     setOutput((Resource)new FileResource(getProject(), file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(Resource output) {
/*  98 */     if (this.output != null) {
/*  99 */       throw new BuildException("Cannot set > 1 output target");
/*     */     }
/* 101 */     this.output = output;
/* 102 */     FileProvider fp = (FileProvider)output.as(FileProvider.class);
/* 103 */     this.file = (fp != null) ? fp.getFile() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(boolean append) {
/* 112 */     this.append = append;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String msg) {
/* 120 */     this.message += getProject().replaceProperties(msg);
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
/*     */   public void setLevel(EchoLevel echoLevel) {
/* 137 */     this.logLevel = echoLevel.getLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 147 */     this.encoding = encoding;
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
/*     */   public void setForce(boolean f) {
/* 159 */     this.force = f;
/*     */   }
/*     */   
/*     */   public static class EchoLevel extends LogLevel {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Echo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */