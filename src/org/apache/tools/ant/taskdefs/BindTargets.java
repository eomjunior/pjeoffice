/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectHelper;
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
/*    */ 
/*    */ public class BindTargets
/*    */   extends Task
/*    */ {
/*    */   private String extensionPoint;
/* 37 */   private final List<String> targets = new ArrayList<>();
/*    */   
/*    */   private ProjectHelper.OnMissingExtensionPoint onMissingExtensionPoint;
/*    */   
/*    */   public void setExtensionPoint(String extensionPoint) {
/* 42 */     this.extensionPoint = extensionPoint;
/*    */   }
/*    */   
/*    */   public void setOnMissingExtensionPoint(String onMissingExtensionPoint) {
/*    */     try {
/* 47 */       this.onMissingExtensionPoint = ProjectHelper.OnMissingExtensionPoint.valueOf(onMissingExtensionPoint);
/* 48 */     } catch (IllegalArgumentException e) {
/* 49 */       throw new BuildException("Invalid onMissingExtensionPoint: " + onMissingExtensionPoint);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setOnMissingExtensionPoint(ProjectHelper.OnMissingExtensionPoint onMissingExtensionPoint) {
/* 54 */     this.onMissingExtensionPoint = onMissingExtensionPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTargets(String target) {
/* 59 */     Objects.requireNonNull(this.targets); Stream.<String>of(target.split(",")).map(String::trim).filter(s -> !s.isEmpty()).forEach(this.targets::add);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 64 */     if (this.extensionPoint == null) {
/* 65 */       throw new BuildException("extensionPoint required", getLocation());
/*    */     }
/*    */     
/* 68 */     if (getOwningTarget() == null || 
/* 69 */       !getOwningTarget().getName().isEmpty()) {
/* 70 */       throw new BuildException("bindtargets only allowed as a top-level task");
/*    */     }
/*    */     
/* 73 */     if (this.onMissingExtensionPoint == null) {
/* 74 */       this.onMissingExtensionPoint = ProjectHelper.OnMissingExtensionPoint.FAIL;
/*    */     }
/* 76 */     ProjectHelper helper = (ProjectHelper)getProject().getReference("ant.projectHelper");
/*    */     
/* 78 */     for (String target : this.targets) {
/* 79 */       helper.getExtensionStack().add(new String[] { this.extensionPoint, target, this.onMissingExtensionPoint
/* 80 */             .name() });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/BindTargets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */