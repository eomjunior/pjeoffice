/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class PropertyHelperTask
/*     */   extends Task
/*     */ {
/*     */   private PropertyHelper propertyHelper;
/*     */   private List<Object> delegates;
/*     */   
/*     */   public final class DelegateElement
/*     */   {
/*     */     private String refid;
/*     */     
/*     */     private DelegateElement() {}
/*     */     
/*     */     public String getRefid() {
/*  51 */       return this.refid;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRefid(String refid) {
/*  59 */       this.refid = refid;
/*     */     }
/*     */     
/*     */     private PropertyHelper.Delegate resolve() {
/*  63 */       if (this.refid == null) {
/*  64 */         throw new BuildException("refid required for generic delegate");
/*     */       }
/*  66 */       return (PropertyHelper.Delegate)PropertyHelperTask.this.getProject().getReference(this.refid);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addConfigured(PropertyHelper propertyHelper) {
/*  78 */     if (this.propertyHelper != null) {
/*  79 */       throw new BuildException("Only one PropertyHelper can be installed");
/*     */     }
/*  81 */     this.propertyHelper = propertyHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addConfigured(PropertyHelper.Delegate delegate) {
/*  89 */     getAddDelegateList().add(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegateElement createDelegate() {
/*  97 */     DelegateElement result = new DelegateElement();
/*  98 */     getAddDelegateList().add(result);
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 108 */     if (getProject() == null) {
/* 109 */       throw new BuildException("Project instance not set");
/*     */     }
/* 111 */     if (this.propertyHelper == null && this.delegates == null) {
/* 112 */       throw new BuildException("Either a new PropertyHelper or one or more PropertyHelper delegates are required");
/*     */     }
/*     */     
/* 115 */     PropertyHelper ph = this.propertyHelper;
/* 116 */     if (ph == null) {
/* 117 */       ph = PropertyHelper.getPropertyHelper(getProject());
/*     */     } else {
/* 119 */       ph = this.propertyHelper;
/*     */     } 
/* 121 */     synchronized (ph) {
/* 122 */       if (this.delegates != null) {
/* 123 */         for (Object o : this.delegates) {
/*     */           
/* 125 */           PropertyHelper.Delegate delegate = (o instanceof DelegateElement) ? ((DelegateElement)o).resolve() : (PropertyHelper.Delegate)o;
/* 126 */           log("Adding PropertyHelper delegate " + delegate, 4);
/* 127 */           ph.add(delegate);
/*     */         } 
/*     */       }
/*     */     } 
/* 131 */     if (this.propertyHelper != null) {
/* 132 */       log("Installing PropertyHelper " + this.propertyHelper, 4);
/*     */       
/* 134 */       getProject().addReference("ant.PropertyHelper", this.propertyHelper);
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized List<Object> getAddDelegateList() {
/* 139 */     if (this.delegates == null) {
/* 140 */       this.delegates = new ArrayList();
/*     */     }
/* 142 */     return this.delegates;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/PropertyHelperTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */