/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.Task;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogStreamHandler
/*    */   extends PumpStreamHandler
/*    */ {
/*    */   public LogStreamHandler(Task task, int outlevel, int errlevel) {
/* 40 */     this((ProjectComponent)task, outlevel, errlevel);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LogStreamHandler(ProjectComponent pc, int outlevel, int errlevel) {
/* 51 */     super((OutputStream)new LogOutputStream(pc, outlevel), (OutputStream)new LogOutputStream(pc, errlevel));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 60 */     super.stop();
/* 61 */     FileUtils.close(getErr());
/* 62 */     FileUtils.close(getOut());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/LogStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */