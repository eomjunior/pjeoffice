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
/*    */ 
/*    */ public class MSVSSCP
/*    */   extends MSVSS
/*    */ {
/*    */   protected Commandline buildCmdLine() {
/* 37 */     Commandline commandLine = new Commandline();
/*    */ 
/*    */     
/* 40 */     if (getVsspath() == null) {
/* 41 */       String msg = "vsspath attribute must be set!";
/* 42 */       throw new BuildException(msg, getLocation());
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     commandLine.setExecutable(getSSCommand());
/* 49 */     commandLine.createArgument().setValue("CP");
/*    */ 
/*    */     
/* 52 */     commandLine.createArgument().setValue(getVsspath());
/*    */     
/* 54 */     commandLine.createArgument().setValue(getAutoresponse());
/*    */     
/* 56 */     commandLine.createArgument().setValue(getLogin());
/*    */     
/* 58 */     return commandLine;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAutoresponse(String response) {
/* 67 */     setInternalAutoResponse(response);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */