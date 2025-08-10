/*    */ package org.apache.tools.ant.helper;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Executor;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.Target;
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
/*    */ public class IgnoreDependenciesExecutor
/*    */   implements Executor
/*    */ {
/* 38 */   private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();
/*    */ 
/*    */ 
/*    */   
/*    */   public void executeTargets(Project project, String[] targetNames) throws BuildException {
/* 43 */     Hashtable<String, Target> targets = project.getTargets();
/* 44 */     BuildException thrownException = null;
/* 45 */     for (String targetName : targetNames) {
/*    */       try {
/* 47 */         Target t = targets.get(targetName);
/* 48 */         if (t == null) {
/* 49 */           throw new BuildException("Unknown target " + targetName);
/*    */         }
/* 51 */         t.performTasks();
/* 52 */       } catch (BuildException ex) {
/* 53 */         if (project.isKeepGoingMode()) {
/* 54 */           thrownException = ex;
/*    */         } else {
/* 56 */           throw ex;
/*    */         } 
/*    */       } 
/*    */     } 
/* 60 */     if (thrownException != null) {
/* 61 */       throw thrownException;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Executor getSubProjectExecutor() {
/* 67 */     return SUB_EXECUTOR;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/IgnoreDependenciesExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */