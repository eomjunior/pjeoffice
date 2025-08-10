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
/*     */ 
/*     */ public class WeblogicTOPLinkDeploymentTool
/*     */   extends WeblogicDeploymentTool
/*     */ {
/*     */   private static final String TL_DTD_LOC = "http://www.objectpeople.com/tlwl/dtd/toplink-cmp_2_5_1.dtd";
/*     */   private String toplinkDescriptor;
/*     */   private String toplinkDTD;
/*     */   
/*     */   public void setToplinkdescriptor(String inString) {
/*  43 */     this.toplinkDescriptor = inString;
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
/*     */   public void setToplinkdtd(String inString) {
/*  57 */     this.toplinkDTD = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getDescriptorHandler(File srcDir) {
/*  67 */     DescriptorHandler handler = super.getDescriptorHandler(srcDir);
/*  68 */     if (this.toplinkDTD != null) {
/*  69 */       handler.registerDTD("-//The Object People, Inc.//DTD TOPLink for WebLogic CMP 2.5.1//EN", this.toplinkDTD);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       handler.registerDTD("-//The Object People, Inc.//DTD TOPLink for WebLogic CMP 2.5.1//EN", "http://www.objectpeople.com/tlwl/dtd/toplink-cmp_2_5_1.dtd");
/*     */     } 
/*     */ 
/*     */     
/*  77 */     return handler;
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
/*  88 */     super.addVendorFiles(ejbFiles, ddPrefix);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     File toplinkDD = new File((getConfig()).descriptorDir, ddPrefix + this.toplinkDescriptor);
/*     */     
/*  95 */     if (toplinkDD.exists()) {
/*  96 */       ejbFiles.put("META-INF/" + this.toplinkDescriptor, toplinkDD);
/*     */     } else {
/*     */       
/*  99 */       log("Unable to locate toplink deployment descriptor. It was expected to be in " + toplinkDD
/* 100 */           .getPath(), 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateConfigured() throws BuildException {
/* 110 */     super.validateConfigured();
/* 111 */     if (this.toplinkDescriptor == null)
/* 112 */       throw new BuildException("The toplinkdescriptor attribute must be specified"); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/WeblogicTOPLinkDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */