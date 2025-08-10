/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntTypeDefinition
/*     */ {
/*     */   private String name;
/*     */   private Class<?> clazz;
/*     */   private Class<?> adapterClass;
/*     */   private Class<?> adaptToClass;
/*     */   private String className;
/*     */   private ClassLoader classLoader;
/*     */   private boolean restrict = false;
/*     */   
/*     */   public void setRestrict(boolean restrict) {
/*  48 */     this.restrict = restrict;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRestrict() {
/*  56 */     return this.restrict;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  64 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  72 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClass(Class<?> clazz) {
/*  81 */     this.clazz = clazz;
/*  82 */     if (clazz == null) {
/*     */       return;
/*     */     }
/*  85 */     this
/*  86 */       .classLoader = (this.classLoader == null) ? clazz.getClassLoader() : this.classLoader;
/*  87 */     this.className = (this.className == null) ? clazz.getName() : this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassName(String className) {
/*  95 */     this.className = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 103 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdapterClass(Class<?> adapterClass) {
/* 113 */     this.adapterClass = adapterClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdaptToClass(Class<?> adaptToClass) {
/* 122 */     this.adaptToClass = adaptToClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader classLoader) {
/* 131 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 139 */     return this.classLoader;
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
/*     */   public Class<?> getExposedClass(Project project) {
/* 152 */     if (this.adaptToClass != null) {
/* 153 */       Class<?> z = getTypeClass(project);
/* 154 */       if (z == null || this.adaptToClass.isAssignableFrom(z)) {
/* 155 */         return z;
/*     */       }
/*     */     } 
/* 158 */     return (this.adapterClass == null) ? getTypeClass(project) : this.adapterClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTypeClass(Project project) {
/*     */     try {
/* 168 */       return innerGetTypeClass();
/* 169 */     } catch (NoClassDefFoundError ncdfe) {
/* 170 */       project.log("Could not load a dependent class (" + ncdfe
/* 171 */           .getMessage() + ") for type " + this.name, 4);
/*     */     }
/* 173 */     catch (ClassNotFoundException cnfe) {
/* 174 */       project.log("Could not load class (" + this.className + ") for type " + this.name, 4);
/*     */     } 
/*     */     
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> innerGetTypeClass() throws ClassNotFoundException {
/* 188 */     if (this.clazz != null) {
/* 189 */       return this.clazz;
/*     */     }
/* 191 */     if (this.classLoader == null) {
/* 192 */       this.clazz = Class.forName(this.className);
/*     */     } else {
/* 194 */       this.clazz = this.classLoader.loadClass(this.className);
/*     */     } 
/* 196 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object create(Project project) {
/* 206 */     return icreate(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object icreate(Project project) {
/* 215 */     Class<?> c = getTypeClass(project);
/* 216 */     if (c == null) {
/* 217 */       return null;
/*     */     }
/* 219 */     Object o = createAndSet(project, c);
/* 220 */     if (this.adapterClass == null || (this.adaptToClass != null && this.adaptToClass
/* 221 */       .isAssignableFrom(o.getClass()))) {
/* 222 */       return o;
/*     */     }
/* 224 */     TypeAdapter adapterObject = (TypeAdapter)createAndSet(project, this.adapterClass);
/*     */     
/* 226 */     adapterObject.setProxy(o);
/* 227 */     return adapterObject;
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
/*     */   public void checkClass(Project project) {
/* 241 */     if (this.clazz == null) {
/* 242 */       this.clazz = getTypeClass(project);
/* 243 */       if (this.clazz == null) {
/* 244 */         throw new BuildException("Unable to create class for " + 
/* 245 */             getName());
/*     */       }
/*     */     } 
/*     */     
/* 249 */     if (this.adapterClass != null && (this.adaptToClass == null || 
/* 250 */       !this.adaptToClass.isAssignableFrom(this.clazz))) {
/* 251 */       TypeAdapter adapter = (TypeAdapter)createAndSet(project, this.adapterClass);
/*     */       
/* 253 */       adapter.checkProxyClass(this.clazz);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object createAndSet(Project project, Class<?> c) {
/*     */     try {
/* 264 */       return innerCreateAndSet(c, project);
/* 265 */     } catch (InvocationTargetException ex) {
/* 266 */       Throwable t = ex.getTargetException();
/* 267 */       throw new BuildException("Could not create type " + this.name + " due to " + t, t);
/*     */     }
/* 269 */     catch (NoClassDefFoundError ncdfe) {
/*     */       
/* 271 */       String msg = "Type " + this.name + ": A class needed by class " + c + " cannot be found: " + ncdfe.getMessage();
/* 272 */       throw new BuildException(msg, ncdfe);
/* 273 */     } catch (NoSuchMethodException nsme) {
/* 274 */       throw new BuildException("Could not create type " + this.name + " as the class " + c + " has no compatible constructor");
/*     */     }
/* 276 */     catch (InstantiationException nsme) {
/* 277 */       throw new BuildException("Could not create type " + this.name + " as the class " + c + " is abstract");
/*     */     }
/* 279 */     catch (IllegalAccessException e) {
/* 280 */       throw new BuildException("Could not create type " + this.name + " as the constructor " + c + " is not accessible");
/*     */     }
/* 282 */     catch (Throwable t) {
/* 283 */       throw new BuildException("Could not create type " + this.name + " due to " + t, t);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T innerCreateAndSet(Class<T> newclass, Project project) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
/*     */     Constructor<T> ctor;
/* 306 */     boolean noArg = false;
/*     */ 
/*     */     
/*     */     try {
/* 310 */       ctor = newclass.getConstructor(new Class[0]);
/* 311 */       noArg = true;
/* 312 */     } catch (NoSuchMethodException nse) {
/*     */       
/* 314 */       ctor = newclass.getConstructor(new Class[] { Project.class });
/* 315 */       noArg = false;
/*     */     } 
/*     */ 
/*     */     
/* 319 */     (new Object[1])[0] = project; T o = ctor.newInstance(noArg ? new Object[0] : new Object[1]);
/*     */ 
/*     */     
/* 322 */     project.setProjectReference(o);
/* 323 */     return o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean sameDefinition(AntTypeDefinition other, Project project) {
/* 334 */     return (other != null && other.getClass() == getClass() && other
/* 335 */       .getTypeClass(project).equals(getTypeClass(project)) && other
/* 336 */       .getExposedClass(project).equals(getExposedClass(project)) && other.restrict == this.restrict && other.adapterClass == this.adapterClass && other.adaptToClass == this.adaptToClass);
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
/*     */   public boolean similarDefinition(AntTypeDefinition other, Project project) {
/* 354 */     if (other == null || 
/* 355 */       getClass() != other.getClass() || 
/* 356 */       !getClassName().equals(other.getClassName()) || 
/* 357 */       !extractClassname(this.adapterClass).equals(
/* 358 */         extractClassname(other.adapterClass)) || 
/* 359 */       !extractClassname(this.adaptToClass).equals(
/* 360 */         extractClassname(other.adaptToClass)) || this.restrict != other.restrict)
/*     */     {
/* 362 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 366 */     ClassLoader oldLoader = other.getClassLoader();
/* 367 */     ClassLoader newLoader = getClassLoader();
/* 368 */     return (oldLoader == newLoader || (oldLoader instanceof AntClassLoader && newLoader instanceof AntClassLoader && ((AntClassLoader)oldLoader)
/*     */ 
/*     */       
/* 371 */       .getClasspath()
/* 372 */       .equals(((AntClassLoader)newLoader).getClasspath())));
/*     */   }
/*     */   
/*     */   private String extractClassname(Class<?> c) {
/* 376 */     return (c == null) ? "<null>" : c.getName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/AntTypeDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */