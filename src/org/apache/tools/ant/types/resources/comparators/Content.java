/*    */ package org.apache.tools.ant.types.resources.comparators;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.util.ResourceUtils;
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
/*    */ public class Content
/*    */   extends ResourceComparator
/*    */ {
/*    */   private boolean binary = true;
/*    */   
/*    */   public void setBinary(boolean b) {
/* 42 */     this.binary = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBinary() {
/* 50 */     return this.binary;
/*    */   }
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
/*    */   protected int resourceCompare(Resource foo, Resource bar) {
/*    */     try {
/* 64 */       return ResourceUtils.compareContent(foo, bar, !this.binary);
/* 65 */     } catch (IOException e) {
/* 66 */       throw new BuildException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/Content.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */