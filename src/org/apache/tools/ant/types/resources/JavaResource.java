/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
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
/*     */ public class JavaResource
/*     */   extends AbstractClasspathResource
/*     */   implements URLProvider
/*     */ {
/*     */   public JavaResource() {}
/*     */   
/*     */   public JavaResource(String name, Path path) {
/*  49 */     setName(name);
/*  50 */     setClasspath(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream openInputStream(ClassLoader cl) throws IOException {
/*     */     InputStream inputStream;
/*  62 */     if (cl == null) {
/*  63 */       inputStream = ClassLoader.getSystemResourceAsStream(getName());
/*  64 */       if (inputStream == null) {
/*  65 */         throw new FileNotFoundException("No resource " + getName() + " on Ant's classpath");
/*     */       }
/*     */     } else {
/*     */       
/*  69 */       inputStream = cl.getResourceAsStream(getName());
/*  70 */       if (inputStream == null) {
/*  71 */         throw new FileNotFoundException("No resource " + getName() + " on the classpath " + cl);
/*     */       }
/*     */     } 
/*     */     
/*  75 */     return inputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/*  84 */     if (isReference()) {
/*  85 */       return getRef().getURL();
/*     */     }
/*     */     
/*  88 */     AbstractClasspathResource.ClassLoaderWithFlag classLoader = getClassLoader();
/*  89 */     if (classLoader.getLoader() == null) {
/*  90 */       return ClassLoader.getSystemResource(getName());
/*     */     }
/*     */     try {
/*  93 */       return classLoader.getLoader().getResource(getName());
/*     */     } finally {
/*  95 */       classLoader.cleanup();
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
/*     */   public int compareTo(Resource another) {
/* 108 */     if (isReference()) {
/* 109 */       return getRef().compareTo(another);
/*     */     }
/* 111 */     if (another.getClass().equals(getClass())) {
/* 112 */       JavaResource otherjr = (JavaResource)another;
/* 113 */       if (!getName().equals(otherjr.getName())) {
/* 114 */         return getName().compareTo(otherjr.getName());
/*     */       }
/* 116 */       if (getLoader() != otherjr.getLoader()) {
/* 117 */         if (getLoader() == null) {
/* 118 */           return -1;
/*     */         }
/* 120 */         if (otherjr.getLoader() == null) {
/* 121 */           return 1;
/*     */         }
/* 123 */         return getLoader().getRefId()
/* 124 */           .compareTo(otherjr.getLoader().getRefId());
/*     */       } 
/* 126 */       Path p = getClasspath();
/* 127 */       Path op = otherjr.getClasspath();
/* 128 */       if (p != op) {
/* 129 */         if (p == null) {
/* 130 */           return -1;
/*     */         }
/* 132 */         if (op == null) {
/* 133 */           return 1;
/*     */         }
/* 135 */         return p.toString().compareTo(op.toString());
/*     */       } 
/* 137 */       return 0;
/*     */     } 
/* 139 */     return super.compareTo(another);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JavaResource getRef() {
/* 144 */     return (JavaResource)getCheckedRef(JavaResource.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/JavaResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */