/*     */ package org.apache.tools.ant.listener;
/*     */ 
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.SubBuildListener;
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
/*     */ public class BigProjectLogger
/*     */   extends SimpleBigProjectLogger
/*     */   implements SubBuildListener
/*     */ {
/*     */   private volatile boolean subBuildStartedRaised = false;
/*  36 */   private final Object subBuildLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String HEADER = "======================================================================";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FOOTER = "======================================================================";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBuildFailedMessage() {
/*  58 */     return super.getBuildFailedMessage() + " - at " + getTimestamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBuildSuccessfulMessage() {
/*  69 */     return super.getBuildSuccessfulMessage() + " - at " + getTimestamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetStarted(BuildEvent event) {
/*  78 */     maybeRaiseSubBuildStarted(event);
/*  79 */     super.targetStarted(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskStarted(BuildEvent event) {
/*  88 */     maybeRaiseSubBuildStarted(event);
/*  89 */     super.taskStarted(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildFinished(BuildEvent event) {
/*  98 */     maybeRaiseSubBuildStarted(event);
/*  99 */     subBuildFinished(event);
/* 100 */     super.buildFinished(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageLogged(BuildEvent event) {
/* 109 */     maybeRaiseSubBuildStarted(event);
/* 110 */     super.messageLogged(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subBuildStarted(BuildEvent event) {
/* 120 */     Project project = event.getProject();
/*     */     
/* 122 */     String path = (project == null) ? "With no base directory" : ("In " + project.getBaseDir().getAbsolutePath());
/* 123 */     printMessage(String.format("%n%s%nEntering project %s%n%s%n%s", new Object[] { getHeader(), 
/* 124 */             extractNameOrDefault(event), path, getFooter()
/*     */           
/* 126 */           }), this.out, event.getPriority());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractNameOrDefault(BuildEvent event) {
/* 136 */     String name = extractProjectName(event);
/* 137 */     if (name == null) {
/* 138 */       name = "";
/*     */     } else {
/* 140 */       name = '"' + name + '"';
/*     */     } 
/* 142 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subBuildFinished(BuildEvent event) {
/* 147 */     printMessage(String.format("%n%s%nExiting %sproject %s%n%s", new Object[] {
/* 148 */             getHeader(), (event.getException() != null) ? "failing " : "", 
/* 149 */             extractNameOrDefault(event), getFooter()
/*     */           
/* 151 */           }), this.out, event.getPriority());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getHeader() {
/* 159 */     return "======================================================================";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getFooter() {
/* 167 */     return "======================================================================";
/*     */   }
/*     */ 
/*     */   
/*     */   private void maybeRaiseSubBuildStarted(BuildEvent event) {
/* 172 */     if (!this.subBuildStartedRaised)
/* 173 */       synchronized (this.subBuildLock) {
/* 174 */         if (!this.subBuildStartedRaised) {
/* 175 */           this.subBuildStartedRaised = true;
/* 176 */           subBuildStarted(event);
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/BigProjectLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */