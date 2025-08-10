/*    */ package org.apache.tools.ant;
/*    */ 
/*    */ import java.util.function.Supplier;
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
/*    */ public interface Evaluable<T>
/*    */   extends Supplier<T>
/*    */ {
/*    */   T eval();
/*    */   
/*    */   default T get() {
/* 33 */     return eval();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Evaluable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */