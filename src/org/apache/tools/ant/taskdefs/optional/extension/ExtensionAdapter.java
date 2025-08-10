/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.DeweyDecimal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtensionAdapter
/*     */   extends DataType
/*     */ {
/*     */   private String extensionName;
/*     */   private DeweyDecimal specificationVersion;
/*     */   private String specificationVendor;
/*     */   private String implementationVendorID;
/*     */   private String implementationVendor;
/*     */   private DeweyDecimal implementationVersion;
/*     */   private String implementationURL;
/*     */   
/*     */   public void setExtensionName(String extensionName) {
/*  79 */     verifyNotAReference();
/*  80 */     this.extensionName = extensionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpecificationVersion(String specificationVersion) {
/*  89 */     verifyNotAReference();
/*  90 */     this.specificationVersion = new DeweyDecimal(specificationVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpecificationVendor(String specificationVendor) {
/*  99 */     verifyNotAReference();
/* 100 */     this.specificationVendor = specificationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementationVendorId(String implementationVendorID) {
/* 109 */     verifyNotAReference();
/* 110 */     this.implementationVendorID = implementationVendorID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementationVendor(String implementationVendor) {
/* 119 */     verifyNotAReference();
/* 120 */     this.implementationVendor = implementationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementationVersion(String implementationVersion) {
/* 129 */     verifyNotAReference();
/* 130 */     this.implementationVersion = new DeweyDecimal(implementationVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementationUrl(String implementationURL) {
/* 139 */     verifyNotAReference();
/* 140 */     this.implementationURL = implementationURL;
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
/*     */   
/*     */   public void setRefid(Reference reference) throws BuildException {
/* 156 */     if (null != this.extensionName || null != this.specificationVersion || null != this.specificationVendor || null != this.implementationVersion || null != this.implementationVendorID || null != this.implementationVendor || null != this.implementationURL)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       throw tooManyAttributes();
/*     */     }
/* 165 */     super.setRefid(reference);
/*     */   }
/*     */ 
/*     */   
/*     */   private void verifyNotAReference() throws BuildException {
/* 170 */     if (isReference()) {
/* 171 */       throw tooManyAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Extension toExtension() throws BuildException {
/* 182 */     if (isReference()) {
/* 183 */       return getRef().toExtension();
/*     */     }
/* 185 */     dieOnCircularReference();
/* 186 */     if (null == this.extensionName) {
/* 187 */       throw new BuildException("Extension is missing name.");
/*     */     }
/*     */     
/* 190 */     String specificationVersionString = null;
/* 191 */     if (null != this.specificationVersion) {
/* 192 */       specificationVersionString = this.specificationVersion.toString();
/*     */     }
/* 194 */     String implementationVersionString = null;
/* 195 */     if (null != this.implementationVersion) {
/* 196 */       implementationVersionString = this.implementationVersion.toString();
/*     */     }
/* 198 */     return new Extension(this.extensionName, specificationVersionString, this.specificationVendor, implementationVersionString, this.implementationVendor, this.implementationVendorID, this.implementationURL);
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
/*     */   
/*     */   public String toString() {
/* 214 */     return "{" + toExtension() + "}";
/*     */   }
/*     */   
/*     */   private ExtensionAdapter getRef() {
/* 218 */     return (ExtensionAdapter)getCheckedRef(ExtensionAdapter.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/ExtensionAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */