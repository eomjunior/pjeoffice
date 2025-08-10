/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
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
/*     */ public class HasMethod
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*     */   private String classname;
/*     */   private String method;
/*     */   private String field;
/*     */   private Path classpath;
/*     */   private AntClassLoader loader;
/*     */   private boolean ignoreSystemClasses = false;
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  46 */     createClasspath().append(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  55 */     if (this.classpath == null) {
/*  56 */       this.classpath = new Path(getProject());
/*     */     }
/*  58 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/*  68 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/*  76 */     this.classname = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethod(String method) {
/*  84 */     this.method = method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(String field) {
/*  92 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreSystemClasses(boolean ignoreSystemClasses) {
/* 100 */     this.ignoreSystemClasses = ignoreSystemClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> loadClass(String classname) {
/*     */     try {
/* 108 */       if (this.ignoreSystemClasses) {
/* 109 */         this.loader = getProject().createClassLoader(this.classpath);
/* 110 */         this.loader.setParentFirst(false);
/* 111 */         this.loader.addJavaLibraries();
/*     */         try {
/* 113 */           return this.loader.findClass(classname);
/* 114 */         } catch (SecurityException se) {
/*     */           
/* 116 */           throw new BuildException("class \"" + classname + "\" was found but a SecurityException has been raised while loading it", se);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 122 */       if (this.loader != null)
/*     */       {
/* 124 */         return this.loader.loadClass(classname);
/*     */       }
/* 126 */       ClassLoader l = getClass().getClassLoader();
/*     */ 
/*     */       
/* 129 */       if (l != null) {
/* 130 */         return Class.forName(classname, true, l);
/*     */       }
/* 132 */       return Class.forName(classname);
/* 133 */     } catch (ClassNotFoundException e) {
/* 134 */       throw new BuildException("class \"" + classname + "\" was not found");
/*     */     }
/* 136 */     catch (NoClassDefFoundError e) {
/* 137 */       throw new BuildException("Could not load dependent class \"" + e
/* 138 */           .getMessage() + "\" for class \"" + classname + "\"");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/* 146 */     if (this.classname == null) {
/* 147 */       throw new BuildException("No classname defined");
/*     */     }
/* 149 */     AntClassLoader antClassLoader = this.loader;
/*     */     try {
/* 151 */       Class<?> clazz = loadClass(this.classname);
/* 152 */       if (this.method != null) {
/* 153 */         return isMethodFound(clazz);
/*     */       }
/* 155 */       if (this.field != null) {
/* 156 */         return isFieldFound(clazz);
/*     */       }
/* 158 */       throw new BuildException("Neither method nor field defined");
/*     */     } finally {
/* 160 */       if (antClassLoader != this.loader && this.loader != null) {
/* 161 */         this.loader.cleanup();
/* 162 */         this.loader = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isFieldFound(Class<?> clazz) {
/* 168 */     for (Field fieldEntry : clazz.getDeclaredFields()) {
/* 169 */       if (fieldEntry.getName().equals(this.field)) {
/* 170 */         return true;
/*     */       }
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isMethodFound(Class<?> clazz) {
/* 177 */     for (Method methodEntry : clazz.getDeclaredMethods()) {
/* 178 */       if (methodEntry.getName().equals(this.method)) {
/* 179 */         return true;
/*     */       }
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/HasMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */