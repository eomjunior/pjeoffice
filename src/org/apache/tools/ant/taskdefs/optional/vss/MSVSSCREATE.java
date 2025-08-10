/*    */ package org.apache.tools.ant.taskdefs.optional.vss;
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
/*    */ public class MSVSSCREATE
/*    */   extends MSVSS
/*    */ {
/*    */   Commandline buildCmdLine() {
/* 36 */     Commandline commandLine = new Commandline();
/*    */ 
/*    */     
/* 39 */     if (getVsspath() == null) {
/* 40 */       String msg = "vsspath attribute must be set!";
/* 41 */       throw new BuildException(msg, getLocation());
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     commandLine.setExecutable(getSSCommand());
/* 49 */     commandLine.createArgument().setValue("Create");
/*    */ 
/*    */     
/* 52 */     commandLine.createArgument().setValue(getVsspath());
/*    */     
/* 54 */     commandLine.createArgument().setValue(getComment());
/*    */     
/* 56 */     commandLine.createArgument().setValue(getAutoresponse());
/*    */     
/* 58 */     commandLine.createArgument().setValue(getQuiet());
/*    */     
/* 60 */     commandLine.createArgument().setValue(getLogin());
/*    */     
/* 62 */     return commandLine;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setComment(String comment) {
/* 71 */     setInternalComment(comment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setQuiet(boolean quiet) {
/* 80 */     setInternalQuiet(quiet);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAutoresponse(String response) {
/* 89 */     setInternalAutoResponse(response);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSCREATE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */