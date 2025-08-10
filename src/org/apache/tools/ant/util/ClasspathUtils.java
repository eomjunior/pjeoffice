/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class ClasspathUtils
/*     */ {
/*     */   public static final String REUSE_LOADER_REF = "ant.reuse.loader";
/*     */   
/*     */   public static ClassLoader getClassLoaderForPath(Project p, Reference ref) {
/*  90 */     return getClassLoaderForPath(p, ref, false);
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
/*     */   
/*     */   public static ClassLoader getClassLoaderForPath(Project p, Reference ref, boolean reverseLoader) {
/* 109 */     String pathId = ref.getRefId();
/* 110 */     Object path = p.getReference(pathId);
/* 111 */     if (!(path instanceof Path)) {
/* 112 */       throw new BuildException("The specified classpathref %s does not reference a Path.", new Object[] { pathId });
/*     */     }
/*     */ 
/*     */     
/* 116 */     String loaderId = "ant.loader." + pathId;
/* 117 */     return getClassLoaderForPath(p, (Path)path, loaderId, reverseLoader);
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
/*     */   public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId) {
/* 132 */     return getClassLoaderForPath(p, path, loaderId, false);
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
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId, boolean reverseLoader) {
/* 152 */     return getClassLoaderForPath(p, path, loaderId, reverseLoader, isMagicPropertySet(p));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoaderForPath(Project p, Path path, String loaderId, boolean reverseLoader, boolean reuseLoader) {
/* 174 */     ClassLoader cl = null;
/*     */ 
/*     */     
/* 177 */     if (loaderId != null && reuseLoader) {
/* 178 */       Object reusedLoader = p.getReference(loaderId);
/* 179 */       if (reusedLoader != null && !(reusedLoader instanceof ClassLoader)) {
/* 180 */         throw new BuildException("The specified loader id %s does not reference a class loader", new Object[] { loaderId });
/*     */       }
/*     */ 
/*     */       
/* 184 */       cl = (ClassLoader)reusedLoader;
/*     */     } 
/* 186 */     if (cl == null) {
/* 187 */       cl = getUniqueClassLoaderForPath(p, path, reverseLoader);
/* 188 */       if (loaderId != null && reuseLoader) {
/* 189 */         p.addReference(loaderId, cl);
/*     */       }
/*     */     } 
/* 192 */     return cl;
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
/*     */   public static ClassLoader getUniqueClassLoaderForPath(Project p, Path path, boolean reverseLoader) {
/* 210 */     AntClassLoader acl = p.createClassLoader(path);
/* 211 */     if (reverseLoader) {
/* 212 */       acl.setParentFirst(false);
/* 213 */       acl.addJavaLibraries();
/*     */     } 
/* 215 */     return (ClassLoader)acl;
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
/*     */   public static Object newInstance(String className, ClassLoader userDefinedLoader) {
/* 230 */     return newInstance(className, userDefinedLoader, Object.class);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T newInstance(String className, ClassLoader userDefinedLoader, Class<T> expectedType) {
/*     */     try {
/* 253 */       Class<T> clazz = (Class)Class.forName(className, true, userDefinedLoader);
/* 254 */       T o = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 255 */       if (!expectedType.isInstance(o)) {
/* 256 */         throw new BuildException("Class of unexpected Type: %s expected : %s", new Object[] { className, expectedType });
/*     */       }
/*     */ 
/*     */       
/* 260 */       return o;
/* 261 */     } catch (ClassNotFoundException e) {
/* 262 */       throw new BuildException("Class not found: " + className, e);
/* 263 */     } catch (InstantiationException e) {
/* 264 */       throw new BuildException("Could not instantiate " + className + ". Specified class should have a no argument constructor.", e);
/*     */     }
/* 266 */     catch (IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/* 267 */       throw new BuildException("Could not instantiate " + className + ". Specified class should have a public constructor.", e);
/*     */     }
/* 269 */     catch (LinkageError e) {
/* 270 */       throw new BuildException("Class " + className + " could not be loaded because of an invalid dependency.", e);
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
/*     */   public static Delegate getDelegate(ProjectComponent component) {
/* 283 */     return new Delegate(component);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isMagicPropertySet(Project p) {
/* 291 */     return (p.getProperty("ant.reuse.loader") != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Delegate
/*     */   {
/*     */     private final ProjectComponent component;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Path classpath;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String classpathId;
/*     */ 
/*     */ 
/*     */     
/*     */     private String className;
/*     */ 
/*     */ 
/*     */     
/*     */     private String loaderId;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean reverseLoader = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Delegate(ProjectComponent component) {
/* 328 */       this.component = component;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClasspath(Path classpath) {
/* 339 */       if (this.classpath == null) {
/* 340 */         this.classpath = classpath;
/*     */       } else {
/* 342 */         this.classpath.append(classpath);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Path createClasspath() {
/* 355 */       if (this.classpath == null) {
/* 356 */         this.classpath = new Path(this.component.getProject());
/*     */       }
/* 358 */       return this.classpath.createPath();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassname(String fcqn) {
/* 370 */       this.className = fcqn;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClasspathref(Reference r) {
/* 382 */       this.classpathId = r.getRefId();
/* 383 */       createClasspath().setRefid(r);
/*     */     }
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
/*     */     public void setReverseLoader(boolean reverseLoader) {
/* 402 */       this.reverseLoader = reverseLoader;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setLoaderRef(Reference r) {
/* 410 */       this.loaderId = r.getRefId();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 419 */       return ClasspathUtils.getClassLoaderForPath(getContextProject(), this.classpath, getClassLoadId(), this.reverseLoader, (this.loaderId != null || ClasspathUtils
/* 420 */           .isMagicPropertySet(getContextProject())));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Project getContextProject() {
/* 427 */       return this.component.getProject();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getClassLoadId() {
/* 435 */       if (this.loaderId == null && this.classpathId != null) {
/* 436 */         return "ant.loader." + this.classpathId;
/*     */       }
/* 438 */       return this.loaderId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object newInstance() {
/* 449 */       return ClasspathUtils.newInstance(this.className, getClassLoader());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Path getClasspath() {
/* 457 */       return this.classpath;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isReverseLoader() {
/* 465 */       return this.reverseLoader;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ClasspathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */