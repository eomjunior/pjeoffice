/*    */ package org.apache.tools.ant.input;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class SecureInputHandler
/*    */   extends DefaultInputHandler
/*    */ {
/*    */   public void handleInput(InputRequest request) throws BuildException {
/* 43 */     String prompt = getPrompt(request);
/*    */     do {
/* 45 */       char[] input = System.console().readPassword(prompt, new Object[0]);
/* 46 */       if (input == null) {
/* 47 */         throw new BuildException("unexpected end of stream while reading input");
/*    */       }
/* 49 */       request.setInput(new String(input));
/* 50 */       Arrays.fill(input, ' ');
/* 51 */     } while (!request.isInputValid());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/SecureInputHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */