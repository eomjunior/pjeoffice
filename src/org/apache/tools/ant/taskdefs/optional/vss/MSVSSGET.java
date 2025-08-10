/*     */ package org.apache.tools.ant.taskdefs.optional.vss;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MSVSSGET
/*     */   extends MSVSS
/*     */ {
/*     */   Commandline buildCmdLine() {
/*  38 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     commandLine.setExecutable(getSSCommand());
/*  44 */     commandLine.createArgument().setValue("Get");
/*     */     
/*  46 */     if (getVsspath() == null) {
/*  47 */       throw new BuildException("vsspath attribute must be set!", getLocation());
/*     */     }
/*  49 */     commandLine.createArgument().setValue(getVsspath());
/*     */ 
/*     */     
/*  52 */     commandLine.createArgument().setValue(getLocalpath());
/*     */     
/*  54 */     commandLine.createArgument().setValue(getAutoresponse());
/*     */     
/*  56 */     commandLine.createArgument().setValue(getQuiet());
/*     */     
/*  58 */     commandLine.createArgument().setValue(getRecursive());
/*     */     
/*  60 */     commandLine.createArgument().setValue(getVersionDateLabel());
/*     */     
/*  62 */     commandLine.createArgument().setValue(getWritable());
/*     */     
/*  64 */     commandLine.createArgument().setValue(getLogin());
/*     */     
/*  66 */     commandLine.createArgument().setValue(getFileTimeStamp());
/*     */     
/*  68 */     commandLine.createArgument().setValue(getWritableFiles());
/*     */     
/*  70 */     return commandLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalpath(Path localPath) {
/*  79 */     setInternalLocalPath(localPath.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRecursive(boolean recursive) {
/*  88 */     setInternalRecursive(recursive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setQuiet(boolean quiet) {
/*  97 */     setInternalQuiet(quiet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setWritable(boolean writable) {
/* 106 */     setInternalWritable(writable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 117 */     setInternalVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDate(String date) {
/* 128 */     setInternalDate(date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(String label) {
/* 139 */     setInternalLabel(label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoresponse(String response) {
/* 148 */     setInternalAutoResponse(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileTimeStamp(MSVSS.CurrentModUpdated timestamp) {
/* 157 */     setInternalFileTimeStamp(timestamp);
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
/*     */   public void setWritableFiles(MSVSS.WritableFiles files) {
/* 170 */     setInternalWritableFiles(files);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSGET.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */