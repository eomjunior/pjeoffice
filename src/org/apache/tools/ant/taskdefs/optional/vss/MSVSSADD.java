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
/*     */ public class MSVSSADD
/*     */   extends MSVSS
/*     */ {
/*  32 */   private String localPath = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline buildCmdLine() {
/*  39 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/*  42 */     if (getLocalpath() == null) {
/*  43 */       String msg = "localPath attribute must be set!";
/*  44 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     commandLine.setExecutable(getSSCommand());
/*  51 */     commandLine.createArgument().setValue("Add");
/*     */ 
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
/*     */   protected String getLocalpath() {
/*  75 */     return this.localPath;
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
/*     */   public final void setWritable(boolean writable) {
/*  93 */     setInternalWritable(writable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoresponse(String response) {
/* 102 */     setInternalAutoResponse(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 111 */     setInternalComment(comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalpath(Path localPath) {
/* 120 */     this.localPath = localPath.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSADD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */