/*    */ package org.apache.tools.ant.helper;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Executor;
/*    */ import org.apache.tools.ant.Project;
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
/*    */ public class DefaultExecutor
/*    */   implements Executor
/*    */ {
/* 33 */   private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();
/*    */ 
/*    */ 
/*    */   
/*    */   public void executeTargets(Project project, String[] targetNames) throws BuildException {
/* 38 */     BuildException thrownException = null;
/* 39 */     for (String targetName : targetNames) {
/*    */       try {
/* 41 */         project.executeTarget(targetName);
/* 42 */       } catch (BuildException ex) {
/* 43 */         if (project.isKeepGoingMode()) {
/* 44 */           thrownException = ex;
/*    */         } else {
/* 46 */           throw ex;
/*    */         } 
/*    */       } 
/*    */     } 
/* 50 */     if (thrownException != null) {
/* 51 */       throw thrownException;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Executor getSubProjectExecutor() {
/* 57 */     return SUB_EXECUTOR;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/DefaultExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */