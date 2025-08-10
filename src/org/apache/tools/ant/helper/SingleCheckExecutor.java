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
/*    */ 
/*    */ 
/*    */ public class SingleCheckExecutor
/*    */   implements Executor
/*    */ {
/*    */   public void executeTargets(Project project, String[] targetNames) throws BuildException {
/* 36 */     project.executeSortedTargets(project
/* 37 */         .topoSort(targetNames, project.getTargets(), false));
/*    */   }
/*    */ 
/*    */   
/*    */   public Executor getSubProjectExecutor() {
/* 42 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/SingleCheckExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */