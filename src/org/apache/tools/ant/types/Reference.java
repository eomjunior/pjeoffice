/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class Reference
/*     */ {
/*     */   private String refid;
/*     */   private Project project;
/*     */   
/*     */   @Deprecated
/*     */   public Reference() {}
/*     */   
/*     */   @Deprecated
/*     */   public Reference(String id) {
/*  52 */     setRefId(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference(Project p, String id) {
/*  62 */     setRefId(id);
/*  63 */     setProject(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefId(String id) {
/*  72 */     this.refid = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRefId() {
/*  80 */     return this.refid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project p) {
/*  90 */     this.project = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/*  99 */     return this.project;
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
/*     */   public <T> T getReferencedObject(Project fallback) throws BuildException {
/* 112 */     if (this.refid == null) {
/* 113 */       throw new BuildException("No reference specified");
/*     */     }
/*     */     
/* 116 */     T o = (this.project == null) ? (T)fallback.getReference(this.refid) : (T)this.project.getReference(this.refid);
/* 117 */     if (o == null) {
/* 118 */       throw new BuildException("Reference " + this.refid + " not found.");
/*     */     }
/* 120 */     return o;
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
/*     */   public <T> T getReferencedObject() throws BuildException {
/* 132 */     if (this.project == null) {
/* 133 */       throw new BuildException("No project set on reference to " + this.refid);
/*     */     }
/* 135 */     return getReferencedObject(this.project);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Reference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */