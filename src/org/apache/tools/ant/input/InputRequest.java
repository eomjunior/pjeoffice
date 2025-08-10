/*    */ package org.apache.tools.ant.input;
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
/*    */ public class InputRequest
/*    */ {
/*    */   private final String prompt;
/*    */   private String input;
/*    */   private String defaultValue;
/*    */   
/*    */   public InputRequest(String prompt) {
/* 36 */     if (prompt == null) {
/* 37 */       throw new IllegalArgumentException("prompt must not be null");
/*    */     }
/*    */     
/* 40 */     this.prompt = prompt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPrompt() {
/* 48 */     return this.prompt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setInput(String input) {
/* 56 */     this.input = input;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isInputValid() {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getInput() {
/* 72 */     return this.input;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDefaultValue() {
/* 81 */     return this.defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefaultValue(String d) {
/* 90 */     this.defaultValue = d;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/InputRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */