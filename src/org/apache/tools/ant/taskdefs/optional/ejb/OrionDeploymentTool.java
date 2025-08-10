/*    */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Hashtable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrionDeploymentTool
/*    */   extends GenericDeploymentTool
/*    */ {
/*    */   protected static final String ORION_DD = "orion-ejb-jar.xml";
/* 39 */   private String jarSuffix = ".jar";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String baseName) {
/* 49 */     String ddPrefix = usingBaseJarName() ? "" : baseName;
/* 50 */     File orionDD = new File((getConfig()).descriptorDir, ddPrefix + "orion-ejb-jar.xml");
/*    */     
/* 52 */     if (orionDD.exists()) {
/* 53 */       ejbFiles.put("META-INF/orion-ejb-jar.xml", orionDD);
/*    */     } else {
/* 55 */       log("Unable to locate Orion deployment descriptor. It was expected to be in " + orionDD.getPath(), 1);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   File getVendorOutputJarFile(String baseName) {
/* 67 */     return new File(getDestDir(), baseName + this.jarSuffix);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/OrionDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */