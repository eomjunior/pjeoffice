/*    */ package org.apache.tools.ant.taskdefs.rmic;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XNewRmic
/*    */   extends ForkingSunRmic
/*    */ {
/*    */   public static final String COMPILER_NAME = "xnew";
/*    */   
/*    */   protected Commandline setupRmicCommand() {
/* 42 */     String[] options = { "-Xnew" };
/*    */ 
/*    */     
/* 45 */     return setupRmicCommand(options);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/XNewRmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */