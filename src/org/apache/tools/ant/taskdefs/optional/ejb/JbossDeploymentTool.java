/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Hashtable;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class JbossDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */ {
/*     */   protected static final String JBOSS_DD = "jboss.xml";
/*     */   protected static final String JBOSS_CMP10D = "jaws.xml";
/*     */   protected static final String JBOSS_CMP20D = "jbosscmp-jdbc.xml";
/*  40 */   private String jarSuffix = ".jar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String inString) {
/*  47 */     this.jarSuffix = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
/*  58 */     File jbossDD = new File((getConfig()).descriptorDir, ddPrefix + "jboss.xml");
/*  59 */     if (jbossDD.exists()) {
/*  60 */       ejbFiles.put("META-INF/jboss.xml", jbossDD);
/*     */     } else {
/*  62 */       log("Unable to locate jboss deployment descriptor. It was expected to be in " + jbossDD
/*  63 */           .getPath(), 1);
/*     */       return;
/*     */     } 
/*  66 */     String descriptorFileName = "jaws.xml";
/*  67 */     if ("2.0".equals(getParent().getCmpversion())) {
/*  68 */       descriptorFileName = "jbosscmp-jdbc.xml";
/*     */     }
/*     */     
/*  71 */     File jbossCMPD = new File((getConfig()).descriptorDir, ddPrefix + descriptorFileName);
/*     */     
/*  73 */     if (jbossCMPD.exists()) {
/*  74 */       ejbFiles.put("META-INF/" + descriptorFileName, jbossCMPD);
/*     */     } else {
/*  76 */       log("Unable to locate jboss cmp descriptor. It was expected to be in " + jbossCMPD
/*  77 */           .getPath(), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File getVendorOutputJarFile(String baseName) {
/*  87 */     if (getDestDir() == null && getParent().getDestdir() == null) {
/*  88 */       throw new BuildException("DestDir not specified");
/*     */     }
/*  90 */     if (getDestDir() == null) {
/*  91 */       return new File(getParent().getDestdir(), baseName + this.jarSuffix);
/*     */     }
/*  93 */     return new File(getDestDir(), baseName + this.jarSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateConfigured() throws BuildException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EjbJar getParent() {
/* 108 */     return (EjbJar)getTask();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/JbossDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */