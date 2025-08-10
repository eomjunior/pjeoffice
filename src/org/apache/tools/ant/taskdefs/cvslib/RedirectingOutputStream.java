/*    */ package org.apache.tools.ant.taskdefs.cvslib;
/*    */ 
/*    */ import org.apache.tools.ant.util.LineOrientedOutputStream;
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
/*    */ class RedirectingOutputStream
/*    */   extends LineOrientedOutputStream
/*    */ {
/*    */   private final ChangeLogParser parser;
/*    */   
/*    */   public RedirectingOutputStream(ChangeLogParser parser) {
/* 34 */     this.parser = parser;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void processLine(String line) {
/* 44 */     this.parser.stdout(line);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/RedirectingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */