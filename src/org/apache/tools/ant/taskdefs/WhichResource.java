/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.net.URL;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public class WhichResource
/*     */   extends Task
/*     */ {
/*     */   private Path classpath;
/*     */   private String classname;
/*     */   private String resource;
/*     */   private String property;
/*     */   
/*     */   public void setClasspath(Path cp) {
/*  68 */     if (this.classpath == null) {
/*  69 */       this.classpath = cp;
/*     */     } else {
/*  71 */       this.classpath.append(cp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  80 */     if (this.classpath == null) {
/*  81 */       this.classpath = new Path(getProject());
/*     */     }
/*  83 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/*  93 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() {
/* 100 */     int setcount = 0;
/* 101 */     if (this.classname != null) {
/* 102 */       setcount++;
/*     */     }
/* 104 */     if (this.resource != null) {
/* 105 */       setcount++;
/*     */     }
/*     */     
/* 108 */     if (setcount == 0) {
/* 109 */       throw new BuildException("One of classname or resource must be specified");
/*     */     }
/* 111 */     if (setcount > 1) {
/* 112 */       throw new BuildException("Only one of classname or resource can be specified");
/*     */     }
/* 114 */     if (this.property == null) {
/* 115 */       throw new BuildException("No property defined");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 125 */     validate();
/* 126 */     if (this.classpath != null) {
/* 127 */       this.classpath = this.classpath.concatSystemClasspath("ignore");
/* 128 */       getProject().log("using user supplied classpath: " + this.classpath, 4);
/*     */     } else {
/*     */       
/* 131 */       this.classpath = new Path(getProject());
/* 132 */       this.classpath = this.classpath.concatSystemClasspath("only");
/* 133 */       getProject().log("using system classpath: " + this.classpath, 4);
/*     */     } 
/*     */ 
/*     */     
/* 137 */     AntClassLoader loader = AntClassLoader.newAntClassLoader(getProject().getCoreLoader(), 
/* 138 */         getProject(), this.classpath, false);
/*     */     try {
/* 140 */       String loc = null;
/* 141 */       if (this.classname != null)
/*     */       {
/* 143 */         this.resource = this.classname.replace('.', '/') + ".class";
/*     */       }
/*     */       
/* 146 */       if (this.resource == null) {
/* 147 */         throw new BuildException("One of class or resource is required");
/*     */       }
/*     */       
/* 150 */       if (this.resource.startsWith("/")) {
/* 151 */         this.resource = this.resource.substring(1);
/*     */       }
/*     */       
/* 154 */       log("Searching for " + this.resource, 3);
/* 155 */       URL url = loader.getResource(this.resource);
/* 156 */       if (url != null) {
/*     */         
/* 158 */         loc = url.toExternalForm();
/* 159 */         getProject().setNewProperty(this.property, loc);
/*     */       } 
/* 161 */       if (loader != null) loader.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (loader != null)
/*     */         try {
/*     */           loader.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/* 170 */     }  } public void setResource(String resource) { this.resource = resource; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClass(String classname) {
/* 179 */     this.classname = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/* 188 */     this.property = property;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/WhichResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */