/*    */ package org.apache.tools.ant.input;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Vector;
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
/*    */ public class MultipleChoiceInputRequest
/*    */   extends InputRequest
/*    */ {
/*    */   private final LinkedHashSet<String> choices;
/*    */   
/*    */   @Deprecated
/*    */   public MultipleChoiceInputRequest(String prompt, Vector<String> choices) {
/* 41 */     this(prompt, choices);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MultipleChoiceInputRequest(String prompt, Collection<String> choices) {
/* 50 */     super(prompt);
/* 51 */     if (choices == null) {
/* 52 */       throw new IllegalArgumentException("choices must not be null");
/*    */     }
/* 54 */     this.choices = new LinkedHashSet<>(choices);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Vector<String> getChoices() {
/* 61 */     return new Vector<>(this.choices);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isInputValid() {
/* 69 */     return (this.choices.contains(getInput()) || (
/* 70 */       getInput().isEmpty() && getDefaultValue() != null));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/MultipleChoiceInputRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */