/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectHelper;
/*    */ import org.apache.tools.ant.ProjectHelperRepository;
/*    */ import org.apache.tools.ant.Task;
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
/*    */ public class ProjectHelperTask
/*    */   extends Task
/*    */ {
/* 35 */   private List<ProjectHelper> projectHelpers = new ArrayList<>();
/*    */   
/*    */   public synchronized void addConfigured(ProjectHelper projectHelper) {
/* 38 */     this.projectHelpers.add(projectHelper);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 44 */     Objects.requireNonNull(ProjectHelperRepository.getInstance()); this.projectHelpers.stream().map(Object::getClass).forEach(ProjectHelperRepository.getInstance()::registerProjectHelper);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ProjectHelperTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */