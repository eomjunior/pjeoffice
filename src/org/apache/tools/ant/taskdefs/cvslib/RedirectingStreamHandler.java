/*    */ package org.apache.tools.ant.taskdefs.cvslib;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.tools.ant.taskdefs.PumpStreamHandler;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ class RedirectingStreamHandler
/*    */   extends PumpStreamHandler
/*    */ {
/*    */   RedirectingStreamHandler(ChangeLogParser parser) {
/* 32 */     super((OutputStream)new RedirectingOutputStream(parser), new ByteArrayOutputStream());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   String getErrors() {
/*    */     try {
/* 39 */       ByteArrayOutputStream error = (ByteArrayOutputStream)getErr();
/*    */       
/* 41 */       return error.toString("ASCII");
/* 42 */     } catch (Exception e) {
/* 43 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 49 */     super.stop();
/* 50 */     FileUtils.close(getErr());
/* 51 */     FileUtils.close(getOut());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/RedirectingStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */