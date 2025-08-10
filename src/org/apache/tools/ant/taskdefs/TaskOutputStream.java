/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.tools.ant.Task;
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
/*    */ @Deprecated
/*    */ public class TaskOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private Task task;
/*    */   private StringBuffer line;
/*    */   private int msgOutputLevel;
/*    */   
/*    */   TaskOutputStream(Task task, int msgOutputLevel) {
/* 54 */     System.err.println("As of Ant 1.2 released in October 2000, the TaskOutputStream class");
/*    */     
/* 56 */     System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
/*    */     
/* 58 */     System.err.println("Don't use it!");
/*    */     
/* 60 */     this.task = task;
/* 61 */     this.msgOutputLevel = msgOutputLevel;
/*    */     
/* 63 */     this.line = new StringBuffer();
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
/*    */   public void write(int c) throws IOException {
/* 75 */     char cc = (char)c;
/* 76 */     if (cc == '\r' || cc == '\n') {
/*    */       
/* 78 */       if (this.line.length() > 0) {
/* 79 */         processLine();
/*    */       }
/*    */     } else {
/* 82 */       this.line.append(cc);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void processLine() {
/* 91 */     String s = this.line.toString();
/* 92 */     this.task.log(s, this.msgOutputLevel);
/* 93 */     this.line = new StringBuffer();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/TaskOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */