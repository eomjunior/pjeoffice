/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class RetryHandler
/*    */ {
/* 32 */   private int retriesAllowed = 0;
/*    */ 
/*    */ 
/*    */   
/*    */   private Task task;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RetryHandler(int retriesAllowed, Task task) {
/* 42 */     this.retriesAllowed = retriesAllowed;
/* 43 */     this.task = task;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(Retryable exe, String desc) throws IOException {
/* 54 */     int retries = 0;
/*    */     while (true) {
/*    */       try {
/* 57 */         exe.execute();
/*    */         break;
/* 59 */       } catch (IOException e) {
/* 60 */         retries++;
/* 61 */         if (retries > this.retriesAllowed && this.retriesAllowed > -1) {
/* 62 */           this.task.log("try #" + retries + ": IO error (" + desc + "), number of maximum retries reached (" + this.retriesAllowed + "), giving up", 1);
/*    */ 
/*    */           
/* 65 */           throw e;
/*    */         } 
/* 67 */         this.task.log("try #" + retries + ": IO error (" + desc + "), retrying", 1);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/RetryHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */