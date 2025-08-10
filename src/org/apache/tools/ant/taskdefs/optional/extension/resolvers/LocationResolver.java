/*    */ package org.apache.tools.ant.taskdefs.optional.extension.resolvers;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.taskdefs.optional.extension.Extension;
/*    */ import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
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
/*    */ public class LocationResolver
/*    */   implements ExtensionResolver
/*    */ {
/*    */   private String location;
/*    */   
/*    */   public void setLocation(String location) {
/* 39 */     this.location = location;
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
/*    */   public File resolve(Extension extension, Project project) throws BuildException {
/* 52 */     if (null == this.location) {
/* 53 */       throw new BuildException("No location specified for resolver");
/*    */     }
/* 55 */     return project.resolveFile(this.location);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "Location[" + this.location + "]";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/resolvers/LocationResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */