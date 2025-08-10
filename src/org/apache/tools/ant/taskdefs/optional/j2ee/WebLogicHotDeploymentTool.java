/*     */ package org.apache.tools.ant.taskdefs.optional.j2ee;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Java;
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
/*     */ public class WebLogicHotDeploymentTool
/*     */   extends AbstractHotDeploymentTool
/*     */   implements HotDeploymentTool
/*     */ {
/*     */   private static final int STRING_BUFFER_SIZE = 1024;
/*     */   private static final String WEBLOGIC_DEPLOY_CLASS_NAME = "weblogic.deploy";
/*  44 */   private static final String[] VALID_ACTIONS = new String[] { "delete", "deploy", "list", "undeploy", "update" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean debug;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String application;
/*     */ 
/*     */ 
/*     */   
/*     */   private String component;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deploy() {
/*  64 */     Java java = new Java(getTask());
/*  65 */     java.setFork(true);
/*  66 */     java.setFailonerror(true);
/*  67 */     java.setClasspath(getClasspath());
/*     */     
/*  69 */     java.setClassname("weblogic.deploy");
/*  70 */     java.createArg().setLine(getArguments());
/*  71 */     java.execute();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateAttributes() throws BuildException {
/*  86 */     super.validateAttributes();
/*     */     
/*  88 */     String action = getTask().getAction();
/*     */ 
/*     */     
/*  91 */     if (getPassword() == null) {
/*  92 */       throw new BuildException("The password attribute must be set.");
/*     */     }
/*     */ 
/*     */     
/*  96 */     if ((action.equals("deploy") || action.equals("update")) && this.application == null)
/*     */     {
/*  98 */       throw new BuildException("The application attribute must be set if action = %s", new Object[] { action });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     if ((action.equals("deploy") || action.equals("update")) && 
/* 104 */       getTask().getSource() == null) {
/* 105 */       throw new BuildException("The source attribute must be set if action = %s", new Object[] { action });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 110 */     if ((action.equals("delete") || action.equals("undeploy")) && this.application == null)
/*     */     {
/* 112 */       throw new BuildException("The application attribute must be set if action = %s", new Object[] { action });
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
/*     */   public String getArguments() throws BuildException {
/* 124 */     String action = getTask().getAction();
/*     */     
/* 126 */     if (action.equals("deploy") || action.equals("update")) {
/* 127 */       return buildDeployArgs();
/*     */     }
/* 129 */     if (action.equals("delete") || action.equals("undeploy")) {
/* 130 */       return buildUndeployArgs();
/*     */     }
/* 132 */     if (action.equals("list")) {
/* 133 */       return buildListArgs();
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isActionValid() {
/* 145 */     String action = getTask().getAction();
/*     */     
/* 147 */     for (String validAction : VALID_ACTIONS) {
/* 148 */       if (action.equals(validAction)) {
/* 149 */         return true;
/*     */       }
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringBuffer buildArgsPrefix() {
/* 162 */     ServerDeploy task = getTask();
/*     */ 
/*     */     
/* 165 */     return (new StringBuffer(1024))
/* 166 */       .append((getServer() != null) ? (
/* 167 */         "-url " + getServer()) : 
/* 168 */         "")
/* 169 */       .append(" ")
/* 170 */       .append(this.debug ? "-debug " : "")
/* 171 */       .append((getUserName() != null) ? (
/* 172 */         "-username " + getUserName()) : 
/* 173 */         "")
/* 174 */       .append(" ")
/* 175 */       .append(task.getAction()).append(" ")
/* 176 */       .append(getPassword()).append(" ");
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
/*     */   protected String buildDeployArgs() {
/* 188 */     String args = buildArgsPrefix().append(this.application).append(" ").append(getTask().getSource()).toString();
/*     */     
/* 190 */     if (this.component != null) {
/* 191 */       args = "-component " + this.component + " " + args;
/*     */     }
/*     */     
/* 194 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String buildUndeployArgs() {
/* 203 */     return buildArgsPrefix()
/* 204 */       .append(this.application).append(" ")
/* 205 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String buildListArgs() {
/* 213 */     return buildArgsPrefix()
/* 214 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 223 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplication(String application) {
/* 232 */     this.application = application;
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
/*     */   
/*     */   public void setComponent(String component) {
/* 245 */     this.component = component;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/j2ee/WebLogicHotDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */