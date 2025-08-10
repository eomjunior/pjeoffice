/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
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
/*     */ public abstract class AbstractClasspathResource
/*     */   extends Resource
/*     */ {
/*     */   private Path classpath;
/*     */   private Reference loader;
/*     */   private boolean parentFirst = true;
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  49 */     checkAttributesAllowed();
/*  50 */     if (this.classpath == null) {
/*  51 */       this.classpath = classpath;
/*     */     } else {
/*  53 */       this.classpath.append(classpath);
/*     */     } 
/*  55 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  63 */     checkChildrenAllowed();
/*  64 */     if (this.classpath == null) {
/*  65 */       this.classpath = new Path(getProject());
/*     */     }
/*  67 */     setChecked(false);
/*  68 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/*  77 */     checkAttributesAllowed();
/*  78 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/*  86 */     if (isReference()) {
/*  87 */       return getRef().getClasspath();
/*     */     }
/*  89 */     dieOnCircularReference();
/*  90 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference getLoader() {
/*  98 */     if (isReference()) {
/*  99 */       return getRef().getLoader();
/*     */     }
/* 101 */     dieOnCircularReference();
/* 102 */     return this.loader;
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
/*     */   public void setLoaderRef(Reference r) {
/* 118 */     checkAttributesAllowed();
/* 119 */     this.loader = r;
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
/*     */   public void setParentFirst(boolean b) {
/* 131 */     this.parentFirst = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 140 */     if (this.loader != null || this.classpath != null) {
/* 141 */       throw tooManyAttributes();
/*     */     }
/* 143 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 153 */     if (isReference()) {
/* 154 */       return getRef().isExists();
/*     */     }
/* 156 */     dieOnCircularReference(); 
/* 157 */     try { InputStream is = getInputStream(); 
/* 158 */       try { boolean bool = (is != null) ? true : false;
/* 159 */         if (is != null) is.close();  return bool; } catch (Throwable throwable) { if (is != null) try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 160 */     { return false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 171 */     if (isReference()) {
/* 172 */       return getRef().getInputStream();
/*     */     }
/* 174 */     dieOnCircularReference();
/*     */     
/* 176 */     final ClassLoaderWithFlag classLoader = getClassLoader();
/* 177 */     return !classLoader.needsCleanup() ? 
/* 178 */       openInputStream(classLoader.getLoader()) : 
/* 179 */       new FilterInputStream(openInputStream(classLoader.getLoader()))
/*     */       {
/*     */         public void close() throws IOException {
/* 182 */           FileUtils.close(this.in);
/* 183 */           classLoader.cleanup();
/*     */         }
/*     */ 
/*     */         
/*     */         protected void finalize() throws Throwable {
/*     */           try {
/* 189 */             close();
/*     */           } finally {
/* 191 */             super.finalize();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoaderWithFlag getClassLoader() {
/* 205 */     ClassLoader cl = null;
/* 206 */     if (this.loader != null) {
/* 207 */       cl = (ClassLoader)this.loader.getReferencedObject();
/*     */     }
/* 209 */     boolean clNeedsCleanup = false;
/* 210 */     if (cl == null) {
/* 211 */       if (getClasspath() != null) {
/* 212 */         Path p = getClasspath().concatSystemClasspath("ignore");
/* 213 */         if (this.parentFirst) {
/* 214 */           AntClassLoader antClassLoader = getProject().createClassLoader(p);
/*     */         } else {
/* 216 */           AntClassLoader antClassLoader = AntClassLoader.newAntClassLoader(getProject()
/* 217 */               .getCoreLoader(), 
/* 218 */               getProject(), p, false);
/*     */         } 
/*     */         
/* 221 */         clNeedsCleanup = (this.loader == null);
/*     */       } else {
/* 223 */         cl = JavaResource.class.getClassLoader();
/*     */       } 
/* 225 */       if (this.loader != null && cl != null) {
/* 226 */         getProject().addReference(this.loader.getRefId(), cl);
/*     */       }
/*     */     } 
/* 229 */     return new ClassLoaderWithFlag(cl, clNeedsCleanup);
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
/* 242 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 245 */     if (isReference()) {
/* 246 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 248 */       if (this.classpath != null) {
/* 249 */         pushAndInvokeCircularReferenceCheck((DataType)this.classpath, stk, p);
/*     */       }
/* 251 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractClasspathResource getRef() {
/* 257 */     return (AbstractClasspathResource)getCheckedRef(AbstractClasspathResource.class);
/*     */   }
/*     */   protected abstract InputStream openInputStream(ClassLoader paramClassLoader) throws IOException;
/*     */   
/*     */   public static class ClassLoaderWithFlag { private final ClassLoader loader;
/*     */     private final boolean cleanup;
/*     */     
/*     */     ClassLoaderWithFlag(ClassLoader l, boolean needsCleanup) {
/* 265 */       this.loader = l;
/* 266 */       this.cleanup = (needsCleanup && l instanceof AntClassLoader);
/*     */     }
/*     */     
/*     */     public ClassLoader getLoader() {
/* 270 */       return this.loader;
/*     */     }
/*     */     
/*     */     public boolean needsCleanup() {
/* 274 */       return this.cleanup;
/*     */     }
/*     */     
/*     */     public void cleanup() {
/* 278 */       if (this.cleanup)
/* 279 */         ((AntClassLoader)this.loader).cleanup(); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/AbstractClasspathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */