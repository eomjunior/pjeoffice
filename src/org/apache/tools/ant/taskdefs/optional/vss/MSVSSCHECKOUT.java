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
/*     */ public class MSVSSCHECKOUT
/*     */   extends MSVSS
/*     */ {
/*     */   protected Commandline buildCmdLine() {
/*  38 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/*  41 */     if (getVsspath() == null) {
/*  42 */       String msg = "vsspath attribute must be set!";
/*  43 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     commandLine.setExecutable(getSSCommand());
/*  50 */     commandLine.createArgument().setValue("Checkout");
/*     */ 
/*     */     
/*  53 */     commandLine.createArgument().setValue(getVsspath());
/*     */     
/*  55 */     commandLine.createArgument().setValue(getLocalpath());
/*     */     
/*  57 */     commandLine.createArgument().setValue(getAutoresponse());
/*     */     
/*  59 */     commandLine.createArgument().setValue(getRecursive());
/*     */     
/*  61 */     commandLine.createArgument().setValue(getVersionDateLabel());
/*     */     
/*  63 */     commandLine.createArgument().setValue(getLogin());
/*     */     
/*  65 */     commandLine.createArgument().setValue(getFileTimeStamp());
/*     */     
/*  67 */     commandLine.createArgument().setValue(getWritableFiles());
/*     */     
/*  69 */     commandLine.createArgument().setValue(getGetLocalCopy());
/*     */     
/*  71 */     return commandLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalpath(Path localPath) {
/*  80 */     setInternalLocalPath(localPath.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecursive(boolean recursive) {
/*  89 */     setInternalRecursive(recursive);
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
/* 100 */     setInternalVersion(version);
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
/* 111 */     setInternalDate(date);
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
/* 122 */     setInternalLabel(label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoresponse(String response) {
/* 131 */     setInternalAutoResponse(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileTimeStamp(MSVSS.CurrentModUpdated timestamp) {
/* 140 */     setInternalFileTimeStamp(timestamp);
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
/*     */   public void setWritableFiles(MSVSS.WritableFiles files) {
/* 154 */     setInternalWritableFiles(files);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGetLocalCopy(boolean get) {
/* 163 */     setInternalGetLocalCopy(get);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSCHECKOUT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */