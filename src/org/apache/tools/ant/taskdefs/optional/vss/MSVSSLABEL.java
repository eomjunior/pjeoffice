/*     */ package org.apache.tools.ant.taskdefs.optional.vss;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MSVSSLABEL
/*     */   extends MSVSS
/*     */ {
/*     */   Commandline buildCmdLine() {
/*  36 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/*  39 */     if (getVsspath() == null) {
/*  40 */       throw new BuildException("vsspath attribute must be set!", getLocation());
/*     */     }
/*     */     
/*  43 */     String label = getLabel();
/*  44 */     if (label.isEmpty()) {
/*  45 */       String msg = "label attribute must be set!";
/*  46 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     commandLine.setExecutable(getSSCommand());
/*  53 */     commandLine.createArgument().setValue("Label");
/*     */ 
/*     */     
/*  56 */     commandLine.createArgument().setValue(getVsspath());
/*     */     
/*  58 */     commandLine.createArgument().setValue(getComment());
/*     */     
/*  60 */     commandLine.createArgument().setValue(getAutoresponse());
/*     */     
/*  62 */     commandLine.createArgument().setValue(label);
/*     */     
/*  64 */     commandLine.createArgument().setValue(getVersion());
/*     */     
/*  66 */     commandLine.createArgument().setValue(getLogin());
/*     */     
/*  68 */     return commandLine;
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
/*  79 */     setInternalLabel(label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/*  88 */     setInternalVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/*  97 */     setInternalComment(comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoresponse(String response) {
/* 106 */     setInternalAutoResponse(response);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSLABEL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */