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
/*     */ 
/*     */ public class SOSGet
/*     */   extends SOS
/*     */ {
/*     */   public final void setFile(String filename) {
/*  38 */     setInternalFilename(filename);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecursive(boolean recursive) {
/*  47 */     setInternalRecursive(recursive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/*  57 */     setInternalVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(String label) {
/*  66 */     setInternalLabel(label);
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
/*     */   protected Commandline buildCmdLine() {
/*  84 */     this.commandLine = new Commandline();
/*     */ 
/*     */     
/*  87 */     if (getFilename() != null) {
/*     */       
/*  89 */       this.commandLine.createArgument().setValue("-command");
/*  90 */       this.commandLine.createArgument().setValue("GetFile");
/*     */       
/*  92 */       this.commandLine.createArgument().setValue("-file");
/*  93 */       this.commandLine.createArgument().setValue(getFilename());
/*     */       
/*  95 */       if (getVersion() != null) {
/*     */         
/*  97 */         this.commandLine.createArgument().setValue("-revision");
/*  98 */         this.commandLine.createArgument().setValue(getVersion());
/*     */       } 
/*     */     } else {
/*     */       
/* 102 */       this.commandLine.createArgument().setValue("-command");
/* 103 */       this.commandLine.createArgument().setValue("GetProject");
/*     */       
/* 105 */       this.commandLine.createArgument().setValue(getRecursive());
/*     */       
/* 107 */       if (getLabel() != null) {
/* 108 */         this.commandLine.createArgument().setValue("-label");
/* 109 */         this.commandLine.createArgument().setValue(getLabel());
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     getRequiredAttributes();
/* 114 */     getOptionalAttributes();
/*     */     
/* 116 */     return this.commandLine;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/sos/SOSGet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */