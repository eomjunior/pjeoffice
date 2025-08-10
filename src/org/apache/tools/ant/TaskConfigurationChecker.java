/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class TaskConfigurationChecker
/*     */ {
/*  58 */   private List<String> errors = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Task task;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskConfigurationChecker(Task task) {
/*  68 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assertConfig(boolean condition, String errormessage) {
/*  77 */     if (!condition) {
/*  78 */       this.errors.add(errormessage);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fail(String errormessage) {
/*  87 */     this.errors.add(errormessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkErrors() throws BuildException {
/*  96 */     if (!this.errors.isEmpty()) {
/*  97 */       StringBuilder sb = new StringBuilder(String.format("Configuration error on <%s>:%n", new Object[] { this.task
/*  98 */               .getTaskName() }));
/*  99 */       for (String msg : this.errors) {
/* 100 */         sb.append(String.format("- %s%n", new Object[] { msg }));
/*     */       } 
/* 102 */       throw new BuildException(sb.toString(), this.task.getLocation());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/TaskConfigurationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */