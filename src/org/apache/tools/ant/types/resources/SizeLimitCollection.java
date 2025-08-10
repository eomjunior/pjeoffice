/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public abstract class SizeLimitCollection
/*    */   extends BaseResourceCollectionWrapper
/*    */ {
/*    */   private static final String BAD_COUNT = "size-limited collection count should be set to an int >= 0";
/* 30 */   private int count = 1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setCount(int i) {
/* 37 */     checkAttributesAllowed();
/* 38 */     this.count = i;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized int getCount() {
/* 46 */     return this.count;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized int size() {
/* 55 */     return Math.min(getResourceCollection().size(), getValidCount());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getValidCount() {
/* 63 */     int ct = getCount();
/* 64 */     if (ct < 0) {
/* 65 */       throw new BuildException("size-limited collection count should be set to an int >= 0");
/*    */     }
/* 67 */     return ct;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/SizeLimitCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */