/*     */ package org.apache.tools.ant.taskdefs.optional.sos;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class SOSCheckin
/*     */   extends SOS
/*     */ {
/*     */   public final void setFile(String filename) {
/*  37 */     setInternalFilename(filename);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecursive(boolean recursive) {
/*  46 */     setInternalRecursive(recursive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/*  55 */     setInternalComment(comment);
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
/*     */   protected Commandline buildCmdLine() {
/*  75 */     this.commandLine = new Commandline();
/*     */ 
/*     */     
/*  78 */     if (getFilename() != null) {
/*     */       
/*  80 */       this.commandLine.createArgument().setValue("-command");
/*  81 */       this.commandLine.createArgument().setValue("CheckInFile");
/*     */       
/*  83 */       this.commandLine.createArgument().setValue("-file");
/*  84 */       this.commandLine.createArgument().setValue(getFilename());
/*     */     } else {
/*     */       
/*  87 */       this.commandLine.createArgument().setValue("-command");
/*  88 */       this.commandLine.createArgument().setValue("CheckInProject");
/*     */       
/*  90 */       this.commandLine.createArgument().setValue(getRecursive());
/*     */     } 
/*     */     
/*  93 */     getRequiredAttributes();
/*  94 */     getOptionalAttributes();
/*     */ 
/*     */     
/*  97 */     if (getComment() != null) {
/*  98 */       this.commandLine.createArgument().setValue("-log");
/*  99 */       this.commandLine.createArgument().setValue(getComment());
/*     */     } 
/* 101 */     return this.commandLine;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/sos/SOSCheckin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */