/*    */ package org.apache.tools.zip;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.zip.ZipException;
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
/*    */ public class UnsupportedZipFeatureException
/*    */   extends ZipException
/*    */ {
/*    */   private final Feature reason;
/*    */   private final transient ZipEntry entry;
/*    */   private static final long serialVersionUID = 20161221L;
/*    */   
/*    */   public UnsupportedZipFeatureException(Feature reason, ZipEntry entry) {
/* 42 */     super("unsupported feature " + reason + " used in entry " + entry
/* 43 */         .getName());
/* 44 */     this.reason = reason;
/* 45 */     this.entry = entry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Feature getFeature() {
/* 54 */     return this.reason;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZipEntry getEntry() {
/* 63 */     return this.entry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Feature
/*    */     implements Serializable
/*    */   {
/* 74 */     public static final Feature ENCRYPTION = new Feature("encryption");
/*    */ 
/*    */ 
/*    */     
/* 78 */     public static final Feature METHOD = new Feature("compression method");
/*    */ 
/*    */ 
/*    */     
/* 82 */     public static final Feature DATA_DESCRIPTOR = new Feature("data descriptor");
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private Feature(String name) {
/* 87 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 92 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnsupportedZipFeatureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */