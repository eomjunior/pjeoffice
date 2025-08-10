/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.util.StreamUtils;
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
/*    */ public class Xor
/*    */   extends ConditionBase
/*    */   implements Condition
/*    */ {
/*    */   public boolean eval() throws BuildException {
/* 39 */     return ((Boolean)StreamUtils.enumerationAsStream(getConditions()).map(Condition::eval)
/* 40 */       .reduce((a, b) -> Boolean.valueOf(a.booleanValue() ^ b.booleanValue())).orElse(Boolean.FALSE)).booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Xor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */