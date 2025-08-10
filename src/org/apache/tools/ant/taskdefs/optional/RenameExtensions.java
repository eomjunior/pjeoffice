/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.taskdefs.Move;
/*     */ import org.apache.tools.ant.types.Mapper;
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
/*     */ @Deprecated
/*     */ public class RenameExtensions
/*     */   extends MatchingTask
/*     */ {
/*  49 */   private String fromExtension = "";
/*  50 */   private String toExtension = "";
/*     */   
/*     */   private boolean replace = false;
/*     */   
/*     */   private File srcDir;
/*     */   
/*     */   private Mapper.MapperType globType;
/*     */ 
/*     */   
/*     */   public RenameExtensions() {
/*  60 */     this.globType = new Mapper.MapperType();
/*  61 */     this.globType.setValue("glob");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromExtension(String from) {
/*  70 */     this.fromExtension = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToExtension(String to) {
/*  80 */     this.toExtension = to;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplace(boolean replace) {
/*  90 */     this.replace = replace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcDir(File srcDir) {
/*  99 */     this.srcDir = srcDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 110 */     if (this.fromExtension == null || this.toExtension == null || this.srcDir == null) {
/* 111 */       throw new BuildException("srcDir, fromExtension and toExtension attributes must be set!");
/*     */     }
/*     */ 
/*     */     
/* 115 */     log("DEPRECATED - The renameext task is deprecated.  Use move instead.", 1);
/*     */     
/* 117 */     log("Replace this with:", 2);
/* 118 */     log("<move todir=\"" + this.srcDir + "\" overwrite=\"" + this.replace + "\">", 2);
/*     */     
/* 120 */     log("  <fileset dir=\"" + this.srcDir + "\" />", 2);
/* 121 */     log("  <mapper type=\"glob\"", 2);
/* 122 */     log("          from=\"*" + this.fromExtension + "\"", 2);
/* 123 */     log("          to=\"*" + this.toExtension + "\" />", 2);
/* 124 */     log("</move>", 2);
/* 125 */     log("using the same patterns on <fileset> as you've used here", 2);
/*     */ 
/*     */     
/* 128 */     Move move = new Move();
/* 129 */     move.bindToOwner((Task)this);
/* 130 */     move.setOwningTarget(getOwningTarget());
/* 131 */     move.setTaskName(getTaskName());
/* 132 */     move.setLocation(getLocation());
/* 133 */     move.setTodir(this.srcDir);
/* 134 */     move.setOverwrite(this.replace);
/*     */     
/* 136 */     this.fileset.setDir(this.srcDir);
/* 137 */     move.addFileset(this.fileset);
/*     */     
/* 139 */     Mapper me = move.createMapper();
/* 140 */     me.setType(this.globType);
/* 141 */     me.setFrom("*" + this.fromExtension);
/* 142 */     me.setTo("*" + this.toExtension);
/*     */     
/* 144 */     move.execute();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/RenameExtensions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */