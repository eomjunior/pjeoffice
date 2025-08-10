/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*    */ import org.apache.tools.ant.types.Resource;
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
/*    */ public class LogOutputResource
/*    */   extends Resource
/*    */   implements Appendable
/*    */ {
/*    */   private static final String NAME = "[Ant log]";
/*    */   private LogOutputStream outputStream;
/*    */   
/*    */   public LogOutputResource(ProjectComponent managingComponent) {
/* 41 */     super("[Ant log]");
/* 42 */     this.outputStream = new LogOutputStream(managingComponent);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LogOutputResource(ProjectComponent managingComponent, int level) {
/* 51 */     super("[Ant log]");
/* 52 */     this.outputStream = new LogOutputStream(managingComponent, level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream getAppendOutputStream() throws IOException {
/* 60 */     return (OutputStream)this.outputStream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream getOutputStream() throws IOException {
/* 68 */     return (OutputStream)this.outputStream;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/LogOutputResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */