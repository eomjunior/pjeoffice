/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
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
/*     */ public class ExtendSelector
/*     */   extends BaseSelector
/*     */ {
/*  41 */   private String classname = null;
/*  42 */   private FileSelector dynselector = null;
/*     */   
/*  44 */   private List<Parameter> parameters = Collections.synchronizedList(new ArrayList<>());
/*  45 */   private Path classpath = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/*  53 */     this.classname = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectorCreate() {
/*  60 */     if (this.classname != null && !this.classname.isEmpty()) {
/*     */       try {
/*     */         Class<?> c;
/*  63 */         if (this.classpath == null) {
/*  64 */           c = Class.forName(this.classname);
/*     */         }
/*     */         else {
/*     */           
/*  68 */           AntClassLoader al = getProject().createClassLoader(this.classpath);
/*  69 */           c = Class.forName(this.classname, true, (ClassLoader)al);
/*     */         } 
/*  71 */         this.dynselector = c.<FileSelector>asSubclass(FileSelector.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  72 */         Project p = getProject();
/*  73 */         if (p != null) {
/*  74 */           p.setProjectReference(this.dynselector);
/*     */         }
/*  76 */       } catch (ClassNotFoundException cnfexcept) {
/*  77 */         setError("Selector " + this.classname + " not initialized, no such class");
/*     */       }
/*  79 */       catch (InstantiationException|NoSuchMethodException|java.lang.reflect.InvocationTargetException iexcept) {
/*     */         
/*  81 */         setError("Selector " + this.classname + " not initialized, could not create class");
/*     */       }
/*  83 */       catch (IllegalAccessException iaexcept) {
/*  84 */         setError("Selector " + this.classname + " not initialized, class not accessible");
/*     */       } 
/*     */     } else {
/*     */       
/*  88 */       setError("There is no classname specified");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(Parameter p) {
/*  98 */     this.parameters.add(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setClasspath(Path classpath) {
/* 106 */     if (isReference()) {
/* 107 */       throw tooManyAttributes();
/*     */     }
/* 109 */     if (this.classpath == null) {
/* 110 */       this.classpath = classpath;
/*     */     } else {
/* 112 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Path createClasspath() {
/* 121 */     if (isReference()) {
/* 122 */       throw noChildrenAllowed();
/*     */     }
/* 124 */     if (this.classpath == null) {
/* 125 */       this.classpath = new Path(getProject());
/*     */     }
/* 127 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Path getClasspath() {
/* 135 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathref(Reference r) {
/* 144 */     if (isReference()) {
/* 145 */       throw tooManyAttributes();
/*     */     }
/* 147 */     createClasspath().setRefid(r);
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
/*     */   public void verifySettings() {
/* 159 */     if (this.dynselector == null) {
/* 160 */       selectorCreate();
/*     */     }
/* 162 */     if (this.classname == null || this.classname.length() < 1) {
/* 163 */       setError("The classname attribute is required");
/* 164 */     } else if (this.dynselector == null) {
/* 165 */       setError("Internal Error: The custom selector was not created");
/* 166 */     } else if (!(this.dynselector instanceof ExtendFileSelector) && 
/* 167 */       !this.parameters.isEmpty()) {
/* 168 */       setError("Cannot set parameters on custom selector that does not implement ExtendFileSelector");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) throws BuildException {
/* 187 */     validate();
/* 188 */     if (!this.parameters.isEmpty() && this.dynselector instanceof ExtendFileSelector)
/*     */     {
/* 190 */       ((ExtendFileSelector)this.dynselector).setParameters(this.parameters
/* 191 */           .<Parameter>toArray(new Parameter[0]));
/*     */     }
/* 193 */     return this.dynselector.isSelected(basedir, filename, file);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/ExtendSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */