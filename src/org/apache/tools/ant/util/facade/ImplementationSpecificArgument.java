/*    */ package org.apache.tools.ant.util.facade;
/*    */ 
/*    */ import org.apache.tools.ant.types.Commandline;
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
/*    */ public class ImplementationSpecificArgument
/*    */   extends Commandline.Argument
/*    */ {
/*    */   private String impl;
/*    */   
/*    */   public void setImplementation(String impl) {
/* 38 */     this.impl = impl;
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
/*    */   public final String[] getParts(String chosenImpl) {
/* 50 */     if (this.impl == null || this.impl.equals(chosenImpl)) {
/* 51 */       return getParts();
/*    */     }
/* 53 */     return new String[0];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/facade/ImplementationSpecificArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */