/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
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
/*     */ public final class AntFilterReader
/*     */   extends DataType
/*     */ {
/*     */   private String className;
/*  35 */   private final List<Parameter> parameters = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private Path classpath;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassName(String className) {
/*  45 */     if (isReference()) {
/*  46 */       throw tooManyAttributes();
/*     */     }
/*  48 */     this.className = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  57 */     if (isReference()) {
/*  58 */       return getRef().getClassName();
/*     */     }
/*  60 */     dieOnCircularReference();
/*  61 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(Parameter param) {
/*  70 */     if (isReference()) {
/*  71 */       throw noChildrenAllowed();
/*     */     }
/*  73 */     this.parameters.add(param);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  81 */     if (isReference()) {
/*  82 */       throw tooManyAttributes();
/*     */     }
/*  84 */     if (this.classpath == null) {
/*  85 */       this.classpath = classpath;
/*     */     } else {
/*  87 */       this.classpath.append(classpath);
/*     */     } 
/*  89 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  97 */     if (isReference()) {
/*  98 */       throw noChildrenAllowed();
/*     */     }
/* 100 */     if (this.classpath == null) {
/* 101 */       this.classpath = new Path(getProject());
/*     */     }
/* 103 */     setChecked(false);
/* 104 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 112 */     if (isReference()) {
/* 113 */       getRef().getClasspath();
/*     */     }
/* 115 */     dieOnCircularReference();
/* 116 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 125 */     if (isReference()) {
/* 126 */       throw tooManyAttributes();
/*     */     }
/* 128 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parameter[] getParams() {
/* 137 */     if (isReference()) {
/* 138 */       getRef().getParams();
/*     */     }
/* 140 */     dieOnCircularReference();
/* 141 */     return this.parameters.<Parameter>toArray(new Parameter[0]);
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
/*     */   public void setRefid(Reference r) throws BuildException {
/* 155 */     if (!this.parameters.isEmpty() || this.className != null || this.classpath != null)
/*     */     {
/* 157 */       throw tooManyAttributes();
/*     */     }
/* 159 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 164 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 167 */     if (isReference()) {
/* 168 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 170 */       if (this.classpath != null) {
/* 171 */         pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
/*     */       }
/* 173 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private AntFilterReader getRef() {
/* 178 */     return getCheckedRef(AntFilterReader.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/AntFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */