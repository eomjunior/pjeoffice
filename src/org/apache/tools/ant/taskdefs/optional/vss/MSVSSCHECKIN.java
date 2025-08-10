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
/*     */ public class MSVSSCHECKIN
/*     */   extends MSVSS
/*     */ {
/*     */   protected Commandline buildCmdLine() {
/*  37 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/*  40 */     if (getVsspath() == null) {
/*  41 */       String msg = "vsspath attribute must be set!";
/*  42 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     commandLine.setExecutable(getSSCommand());
/*  49 */     commandLine.createArgument().setValue("Checkin");
/*     */ 
/*     */     
/*  52 */     commandLine.createArgument().setValue(getVsspath());
/*     */     
/*  54 */     commandLine.createArgument().setValue(getLocalpath());
/*     */     
/*  56 */     commandLine.createArgument().setValue(getAutoresponse());
/*     */     
/*  58 */     commandLine.createArgument().setValue(getRecursive());
/*     */     
/*  60 */     commandLine.createArgument().setValue(getWritable());
/*     */     
/*  62 */     commandLine.createArgument().setValue(getLogin());
/*     */     
/*  64 */     commandLine.createArgument().setValue(getComment());
/*     */     
/*  66 */     return commandLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalpath(Path localPath) {
/*  75 */     setInternalLocalPath(localPath.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecursive(boolean recursive) {
/*  84 */     setInternalRecursive(recursive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setWritable(boolean writable) {
/*  94 */     setInternalWritable(writable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoresponse(String response) {
/* 103 */     setInternalAutoResponse(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 112 */     setInternalComment(comment);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSCHECKIN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */