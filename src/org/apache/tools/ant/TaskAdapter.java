/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.dispatch.DispatchUtils;
/*     */ import org.apache.tools.ant.dispatch.Dispatchable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskAdapter
/*     */   extends Task
/*     */   implements TypeAdapter
/*     */ {
/*     */   private Object proxy;
/*     */   
/*     */   public TaskAdapter() {}
/*     */   
/*     */   public TaskAdapter(Object proxy) {
/*  53 */     this();
/*  54 */     setProxy(proxy);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkTaskClass(Class<?> taskClass, Project project) {
/*  80 */     if (!Dispatchable.class.isAssignableFrom(taskClass)) {
/*     */       
/*     */       try {
/*     */         
/*  84 */         Method executeM = taskClass.getMethod("execute", new Class[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         if (!void.class.equals(executeM.getReturnType())) {
/*     */           
/*  91 */           String message = "return type of execute() should be void but was \"" + executeM.getReturnType() + "\" in " + taskClass;
/*     */           
/*  93 */           project.log(message, 1);
/*     */         } 
/*  95 */       } catch (NoSuchMethodException e) {
/*  96 */         String message = "No public execute() in " + taskClass;
/*  97 */         project.log(message, 0);
/*  98 */         throw new BuildException(message);
/*  99 */       } catch (LinkageError e) {
/* 100 */         String message = "Could not load " + taskClass + ": " + e;
/* 101 */         project.log(message, 0);
/* 102 */         throw new BuildException(message, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkProxyClass(Class<?> proxyClass) {
/* 114 */     checkTaskClass(proxyClass, getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     try {
/* 126 */       Method setLocationM = this.proxy.getClass().getMethod("setLocation", new Class[] { Location.class });
/* 127 */       if (setLocationM != null) {
/* 128 */         setLocationM.invoke(this.proxy, new Object[] { getLocation() });
/*     */       }
/* 130 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */     
/* 133 */     } catch (Exception ex) {
/* 134 */       log("Error setting location in " + this.proxy.getClass(), 0);
/*     */       
/* 136 */       throw new BuildException(ex);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 141 */       Method setProjectM = this.proxy.getClass().getMethod("setProject", new Class[] { Project.class });
/* 142 */       if (setProjectM != null) {
/* 143 */         setProjectM.invoke(this.proxy, new Object[] { getProject() });
/*     */       }
/* 145 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */     
/* 148 */     } catch (Exception ex) {
/* 149 */       log("Error setting project in " + this.proxy.getClass(), 0);
/*     */       
/* 151 */       throw new BuildException(ex);
/*     */     } 
/*     */     
/*     */     try {
/* 155 */       DispatchUtils.execute(this.proxy);
/* 156 */     } catch (BuildException be) {
/* 157 */       throw be;
/* 158 */     } catch (Exception ex) {
/* 159 */       log("Error in " + this.proxy.getClass(), 3);
/* 160 */       throw new BuildException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxy(Object o) {
/* 170 */     this.proxy = o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProxy() {
/* 179 */     return this.proxy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/TaskAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */