/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.RuntimeConfigurable;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.TypeAdapter;
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
/*    */ public class AugmentReference
/*    */   extends Task
/*    */   implements TypeAdapter
/*    */ {
/*    */   private String id;
/*    */   
/*    */   public void checkProxyClass(Class<?> proxyClass) {}
/*    */   
/*    */   public synchronized Object getProxy() {
/* 43 */     if (getProject() == null) {
/* 44 */       throw new IllegalStateException(getTaskName() + "Project owner unset");
/*    */     }
/* 46 */     hijackId();
/* 47 */     if (getProject().hasReference(this.id)) {
/* 48 */       Object result = getProject().getReference(this.id);
/* 49 */       log("project reference " + this.id + "=" + result, 4);
/* 50 */       return result;
/*    */     } 
/* 52 */     throw new BuildException("Unknown reference \"" + this.id + "\"");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProxy(Object o) {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   private synchronized void hijackId() {
/* 63 */     if (this.id == null) {
/* 64 */       RuntimeConfigurable wrapper = getWrapper();
/* 65 */       this.id = wrapper.getId();
/* 66 */       if (this.id == null) {
/* 67 */         throw new BuildException(getTaskName() + " attribute 'id' unset");
/*    */       }
/* 69 */       wrapper.setAttribute("id", null);
/* 70 */       wrapper.removeAttribute("id");
/* 71 */       wrapper.setElementTag("augmented reference \"" + this.id + "\"");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() {
/* 80 */     restoreWrapperId();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private synchronized void restoreWrapperId() {
/* 88 */     if (this.id != null) {
/* 89 */       log("restoring augment wrapper " + this.id, 4);
/* 90 */       RuntimeConfigurable wrapper = getWrapper();
/* 91 */       wrapper.setAttribute("id", this.id);
/* 92 */       wrapper.setElementTag(getTaskName());
/* 93 */       this.id = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AugmentReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */