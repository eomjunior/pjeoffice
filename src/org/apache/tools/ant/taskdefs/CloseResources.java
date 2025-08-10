/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.ResourceCollection;
/*    */ import org.apache.tools.ant.types.resources.URLProvider;
/*    */ import org.apache.tools.ant.types.resources.Union;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ public class CloseResources
/*    */   extends Task
/*    */ {
/* 40 */   private Union resources = new Union();
/*    */   
/*    */   public void add(ResourceCollection rc) {
/* 43 */     this.resources.add(rc);
/*    */   }
/*    */   
/*    */   public void execute() {
/* 47 */     for (Resource r : this.resources) {
/* 48 */       URLProvider up = (URLProvider)r.as(URLProvider.class);
/* 49 */       if (up != null) {
/* 50 */         URL u = up.getURL();
/*    */         try {
/* 52 */           FileUtils.close(u.openConnection());
/* 53 */         } catch (IOException iOException) {}
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/CloseResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */