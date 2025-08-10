/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.StringUtils;
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
/*     */ public class Classloader
/*     */   extends Task
/*     */ {
/*     */   public static final String SYSTEM_LOADER_REF = "ant.coreLoader";
/*  67 */   private String name = null;
/*     */   private Path classpath;
/*     */   private boolean reset = false;
/*     */   private boolean parentFirst = true;
/*  71 */   private String parentName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  78 */     this.name = name;
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
/*     */   public void setReset(boolean b) {
/*  90 */     this.reset = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setReverse(boolean b) {
/* 100 */     this.parentFirst = !b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentFirst(boolean b) {
/* 108 */     this.parentFirst = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentName(String name) {
/* 116 */     this.parentName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference pathRef) throws BuildException {
/* 127 */     this.classpath = (Path)pathRef.getReferencedObject(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 136 */     if (this.classpath == null) {
/* 137 */       this.classpath = classpath;
/*     */     } else {
/* 139 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 148 */     if (this.classpath == null) {
/* 149 */       this.classpath = new Path(null);
/*     */     }
/* 151 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*     */     try {
/* 161 */       if ("only".equals(getProject().getProperty("build.sysclasspath")) && (this.name == null || "ant.coreLoader"
/* 162 */         .equals(this.name))) {
/* 163 */         log("Changing the system loader is disabled by build.sysclasspath=only", 1);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 168 */       String loaderName = (this.name == null) ? "ant.coreLoader" : this.name;
/*     */       
/* 170 */       Object obj = getProject().getReference(loaderName);
/* 171 */       if (this.reset)
/*     */       {
/*     */         
/* 174 */         obj = null;
/*     */       }
/*     */ 
/*     */       
/* 178 */       if (obj != null && !(obj instanceof AntClassLoader)) {
/* 179 */         log("Referenced object is not an AntClassLoader", 0);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 185 */       AntClassLoader acl = (AntClassLoader)obj;
/* 186 */       boolean existingLoader = (acl != null);
/*     */       
/* 188 */       if (acl == null) {
/*     */         
/* 190 */         Object parent = null;
/* 191 */         if (this.parentName != null) {
/* 192 */           parent = getProject().getReference(this.parentName);
/* 193 */           if (!(parent instanceof ClassLoader)) {
/* 194 */             parent = null;
/*     */           }
/*     */         } 
/*     */         
/* 198 */         if (parent == null) {
/* 199 */           parent = getClass().getClassLoader();
/*     */         }
/*     */         
/* 202 */         if (this.name == null);
/*     */ 
/*     */ 
/*     */         
/* 206 */         getProject().log("Setting parent loader " + this.name + " " + parent + " " + this.parentFirst, 4);
/*     */ 
/*     */ 
/*     */         
/* 210 */         acl = AntClassLoader.newAntClassLoader((ClassLoader)parent, 
/* 211 */             getProject(), this.classpath, this.parentFirst);
/*     */         
/* 213 */         getProject().addReference(loaderName, acl);
/*     */         
/* 215 */         if (this.name == null) {
/*     */ 
/*     */           
/* 218 */           acl.addLoaderPackageRoot("org.apache.tools.ant.taskdefs.optional");
/* 219 */           getProject().setCoreLoader((ClassLoader)acl);
/*     */         } 
/*     */       } 
/*     */       
/* 223 */       if (existingLoader && this.classpath != null) {
/* 224 */         for (String path : this.classpath.list()) {
/* 225 */           File f = new File(path);
/* 226 */           if (f.exists()) {
/* 227 */             log("Adding to class loader " + acl + " " + f.getAbsolutePath(), 4);
/*     */             
/* 229 */             acl.addPathElement(f.getAbsolutePath());
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     }
/* 236 */     catch (Exception ex) {
/* 237 */       log(StringUtils.getStackTrace(ex), 0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Classloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */