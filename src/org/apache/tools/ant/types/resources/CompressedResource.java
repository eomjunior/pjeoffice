/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import org.apache.tools.ant.types.ResourceCollection;
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
/*    */ 
/*    */ 
/*    */ public abstract class CompressedResource
/*    */   extends ContentTransformingResource
/*    */ {
/*    */   protected CompressedResource() {}
/*    */   
/*    */   protected CompressedResource(ResourceCollection other) {
/* 42 */     addConfigured(other);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getCompressionName() + " compressed " + super.toString();
/*    */   }
/*    */   
/*    */   protected abstract String getCompressionName();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/CompressedResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */