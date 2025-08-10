/*    */ package org.apache.tools.ant.taskdefs.optional.sos;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Commandline;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SOSLabel
/*    */   extends SOS
/*    */ {
/*    */   public void setVersion(String version) {
/* 36 */     setInternalVersion(version);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLabel(String label) {
/* 47 */     setInternalLabel(label);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setComment(String comment) {
/* 56 */     setInternalComment(comment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Commandline buildCmdLine() {
/* 70 */     this.commandLine = new Commandline();
/*    */ 
/*    */     
/* 73 */     this.commandLine.createArgument().setValue("-command");
/* 74 */     this.commandLine.createArgument().setValue("AddLabel");
/*    */     
/* 76 */     getRequiredAttributes();
/*    */ 
/*    */     
/* 79 */     if (getLabel() == null) {
/* 80 */       throw new BuildException("label attribute must be set!", getLocation());
/*    */     }
/* 82 */     this.commandLine.createArgument().setValue("-label");
/* 83 */     this.commandLine.createArgument().setValue(getLabel());
/*    */ 
/*    */     
/* 86 */     this.commandLine.createArgument().setValue(getVerbose());
/*    */     
/* 88 */     if (getComment() != null) {
/* 89 */       this.commandLine.createArgument().setValue("-log");
/* 90 */       this.commandLine.createArgument().setValue(getComment());
/*    */     } 
/* 92 */     return this.commandLine;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/sos/SOSLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */