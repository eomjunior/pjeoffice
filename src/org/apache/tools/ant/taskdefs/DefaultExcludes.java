/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class DefaultExcludes
/*     */   extends Task
/*     */ {
/*  37 */   private String add = "";
/*  38 */   private String remove = "";
/*     */   
/*     */   private boolean defaultrequested = false;
/*     */   
/*     */   private boolean echo = false;
/*  43 */   private int logLevel = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  52 */     if (!this.defaultrequested && this.add.isEmpty() && this.remove.isEmpty() && !this.echo) {
/*  53 */       throw new BuildException("<defaultexcludes> task must set at least one attribute (echo=\"false\" doesn't count since that is the default");
/*     */     }
/*     */     
/*  56 */     if (this.defaultrequested) {
/*  57 */       DirectoryScanner.resetDefaultExcludes();
/*     */     }
/*  59 */     if (!this.add.isEmpty()) {
/*  60 */       DirectoryScanner.addDefaultExclude(this.add);
/*     */     }
/*  62 */     if (!this.remove.isEmpty()) {
/*  63 */       DirectoryScanner.removeDefaultExclude(this.remove);
/*     */     }
/*  65 */     if (this.echo) {
/*     */ 
/*     */       
/*  68 */       String message = Arrays.<String>stream(DirectoryScanner.getDefaultExcludes()).map(exclude -> String.format("  %s%n", new Object[] { exclude })).collect(Collectors.joining("", "Current Default Excludes:%n", ""));
/*  69 */       log(message, this.logLevel);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefault(boolean def) {
/*  79 */     this.defaultrequested = def;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdd(String add) {
/*  87 */     this.add = add;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemove(String remove) {
/*  97 */     this.remove = remove;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEcho(boolean echo) {
/* 107 */     this.echo = echo;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/DefaultExcludes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */