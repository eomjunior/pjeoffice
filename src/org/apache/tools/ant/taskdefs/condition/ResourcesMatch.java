/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.ResourceCollection;
/*    */ import org.apache.tools.ant.types.resources.Union;
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
/*    */ public class ResourcesMatch
/*    */   implements Condition
/*    */ {
/* 39 */   private Union resources = null;
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean asText = false;
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(boolean asText) {
/* 48 */     this.asText = asText;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(ResourceCollection rc) {
/* 56 */     if (rc == null) {
/*    */       return;
/*    */     }
/* 59 */     this.resources = (this.resources == null) ? new Union() : this.resources;
/* 60 */     this.resources.add(rc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 70 */     if (this.resources == null) {
/* 71 */       throw new BuildException("You must specify one or more nested resource collections");
/*    */     }
/*    */     
/* 74 */     if (this.resources.size() > 1) {
/* 75 */       Iterator<Resource> i = this.resources.iterator();
/* 76 */       Resource r1 = i.next();
/*    */ 
/*    */       
/* 79 */       while (i.hasNext()) {
/* 80 */         Resource r2 = i.next();
/*    */         try {
/* 82 */           if (!ResourceUtils.contentEquals(r1, r2, this.asText)) {
/* 83 */             return false;
/*    */           }
/* 85 */         } catch (IOException ioe) {
/* 86 */           throw new BuildException("when comparing resources " + r1
/* 87 */               .toString() + " and " + r2.toString(), ioe);
/*    */         } 
/* 89 */         r1 = r2;
/*    */       } 
/*    */     } 
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/ResourcesMatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */