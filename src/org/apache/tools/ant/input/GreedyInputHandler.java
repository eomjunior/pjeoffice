/*    */ package org.apache.tools.ant.input;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.InputStream;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.taskdefs.StreamPumper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GreedyInputHandler
/*    */   extends DefaultInputHandler
/*    */ {
/*    */   public void handleInput(InputRequest request) throws BuildException {
/* 48 */     String prompt = getPrompt(request);
/* 49 */     InputStream in = null;
/*    */     try {
/* 51 */       in = getInputStream();
/* 52 */       System.err.println(prompt);
/* 53 */       System.err.flush();
/* 54 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 55 */       StreamPumper p = new StreamPumper(in, baos);
/* 56 */       Thread t = new Thread((Runnable)p);
/* 57 */       t.start();
/*    */       try {
/* 59 */         t.join();
/* 60 */       } catch (InterruptedException e) {
/*    */         try {
/* 62 */           t.join();
/* 63 */         } catch (InterruptedException interruptedException) {}
/*    */       } 
/*    */ 
/*    */       
/* 67 */       request.setInput(new String(baos.toByteArray()));
/* 68 */       if (!request.isInputValid()) {
/* 69 */         throw new BuildException("Received invalid console input");
/*    */       }
/*    */       
/* 72 */       if (p.getException() != null) {
/* 73 */         throw new BuildException("Failed to read input from console", p
/* 74 */             .getException());
/*    */       }
/*    */     } finally {
/* 77 */       FileUtils.close(in);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/GreedyInputHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */