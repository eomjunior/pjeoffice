/*    */ package org.apache.tools.ant.taskdefs.optional.sos;
/*    */ 
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
/*    */ 
/*    */ public class SOSCheckout
/*    */   extends SOS
/*    */ {
/*    */   public final void setFile(String filename) {
/* 37 */     setInternalFilename(filename);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRecursive(boolean recursive) {
/* 46 */     setInternalRecursive(recursive);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Commandline buildCmdLine() {
/* 64 */     this.commandLine = new Commandline();
/*    */ 
/*    */     
/* 67 */     if (getFilename() != null) {
/*    */       
/* 69 */       this.commandLine.createArgument().setValue("-command");
/* 70 */       this.commandLine.createArgument().setValue("CheckOutFile");
/*    */       
/* 72 */       this.commandLine.createArgument().setValue("-file");
/* 73 */       this.commandLine.createArgument().setValue(getFilename());
/*    */     } else {
/*    */       
/* 76 */       this.commandLine.createArgument().setValue("-command");
/* 77 */       this.commandLine.createArgument().setValue("CheckOutProject");
/*    */       
/* 79 */       this.commandLine.createArgument().setValue(getRecursive());
/*    */     } 
/*    */     
/* 82 */     getRequiredAttributes();
/* 83 */     getOptionalAttributes();
/*    */     
/* 85 */     return this.commandLine;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/sos/SOSCheckout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */