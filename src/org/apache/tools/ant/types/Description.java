/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.Target;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Description
/*     */   extends DataType
/*     */ {
/*     */   public void addText(String text) {
/*  56 */     ProjectHelper ph = (ProjectHelper)getProject().getReference("ant.projectHelper");
/*  57 */     if (!(ph instanceof org.apache.tools.ant.helper.ProjectHelperImpl)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  62 */     String currentDescription = getProject().getDescription();
/*  63 */     if (currentDescription == null) {
/*  64 */       getProject().setDescription(text);
/*     */     } else {
/*  66 */       getProject().setDescription(currentDescription + text);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDescription(Project project) {
/*  79 */     List<Target> targets = (List<Target>)project.getReference("ant.targets");
/*  80 */     if (targets == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     StringBuilder description = new StringBuilder();
/*  84 */     for (Target t : targets) {
/*  85 */       concatDescriptions(project, t, description);
/*     */     }
/*  87 */     return description.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void concatDescriptions(Project project, Target t, StringBuilder description) {
/*  92 */     if (t == null) {
/*     */       return;
/*     */     }
/*  95 */     for (Task task : findElementInTarget(t, "description")) {
/*  96 */       if (task instanceof UnknownElement) {
/*  97 */         UnknownElement ue = (UnknownElement)task;
/*  98 */         String descComp = ue.getWrapper().getText().toString();
/*  99 */         if (descComp != null) {
/* 100 */           description.append(project.replaceProperties(descComp));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<Task> findElementInTarget(Target t, String name) {
/* 107 */     return (List<Task>)Stream.<Task>of(t.getTasks())
/* 108 */       .filter(task -> name.equals(task.getTaskName()))
/* 109 */       .collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Description.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */