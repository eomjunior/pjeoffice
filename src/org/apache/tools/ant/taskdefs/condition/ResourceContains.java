/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class ResourceContains
/*     */   implements Condition
/*     */ {
/*     */   private Project project;
/*     */   private String substring;
/*     */   private Resource resource;
/*     */   private String refid;
/*     */   private boolean casesensitive = true;
/*     */   
/*     */   public void setProject(Project project) {
/*  50 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/*  58 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String r) {
/*  66 */     this.resource = (Resource)new FileResource(new File(r));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(String refid) {
/*  75 */     this.refid = refid;
/*     */   }
/*     */   
/*     */   private void resolveRefid() {
/*     */     try {
/*  80 */       if (getProject() == null) {
/*  81 */         throw new BuildException("Cannot retrieve refid; project unset");
/*     */       }
/*  83 */       Object o = getProject().getReference(this.refid);
/*  84 */       if (!(o instanceof Resource)) {
/*  85 */         if (o instanceof ResourceCollection) {
/*  86 */           ResourceCollection rc = (ResourceCollection)o;
/*  87 */           if (rc.size() == 1) {
/*  88 */             o = rc.iterator().next();
/*     */           }
/*     */         } else {
/*  91 */           throw new BuildException("Illegal value at '%s': %s", new Object[] { this.refid, o });
/*     */         } 
/*     */       }
/*  94 */       this.resource = (Resource)o;
/*     */     } finally {
/*  96 */       this.refid = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubstring(String substring) {
/* 105 */     this.substring = substring;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCasesensitive(boolean casesensitive) {
/* 113 */     this.casesensitive = casesensitive;
/*     */   }
/*     */   
/*     */   private void validate() {
/* 117 */     if (this.resource != null && this.refid != null) {
/* 118 */       throw new BuildException("Cannot set both resource and refid");
/*     */     }
/* 120 */     if (this.resource == null && this.refid != null) {
/* 121 */       resolveRefid();
/*     */     }
/* 123 */     if (this.resource == null || this.substring == null) {
/* 124 */       throw new BuildException("both resource and substring are required in <resourcecontains>");
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
/*     */   public synchronized boolean eval() throws BuildException {
/* 136 */     validate();
/*     */     
/* 138 */     if (this.substring.isEmpty()) {
/* 139 */       if (getProject() != null) {
/* 140 */         getProject().log("Substring is empty; returning true", 3);
/*     */       }
/*     */       
/* 143 */       return true;
/*     */     } 
/* 145 */     if (this.resource.getSize() == 0L) {
/* 146 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 150 */     try { BufferedReader reader = new BufferedReader(new InputStreamReader(this.resource.getInputStream())); 
/* 151 */       try { String contents = FileUtils.safeReadFully(reader);
/* 152 */         String sub = this.substring;
/* 153 */         if (!this.casesensitive) {
/* 154 */           contents = contents.toLowerCase();
/* 155 */           sub = sub.toLowerCase();
/*     */         } 
/* 157 */         boolean bool = contents.contains(sub);
/* 158 */         reader.close(); return bool; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 159 */     { throw new BuildException("There was a problem accessing resource : " + this.resource); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/ResourceContains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */