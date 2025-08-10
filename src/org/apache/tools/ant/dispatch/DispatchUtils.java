/*     */ package org.apache.tools.ant.dispatch;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DispatchUtils
/*     */ {
/*     */   public static final void execute(Object task) throws BuildException {
/*  38 */     String methodName = "execute";
/*  39 */     Dispatchable dispatchable = null;
/*     */     try {
/*  41 */       if (task instanceof Dispatchable) {
/*  42 */         dispatchable = (Dispatchable)task;
/*  43 */       } else if (task instanceof UnknownElement) {
/*  44 */         UnknownElement ue = (UnknownElement)task;
/*  45 */         Object realThing = ue.getRealThing();
/*  46 */         if (realThing instanceof Dispatchable && realThing instanceof org.apache.tools.ant.Task)
/*     */         {
/*  48 */           dispatchable = (Dispatchable)realThing;
/*     */         }
/*     */       } 
/*  51 */       if (dispatchable != null) {
/*  52 */         String mName = null;
/*     */         try {
/*  54 */           String name = dispatchable.getActionParameterName();
/*  55 */           if (name == null || name.trim().isEmpty()) {
/*  56 */             throw new BuildException("Action Parameter Name must not be empty for Dispatchable Task.");
/*     */           }
/*     */           
/*  59 */           mName = "get" + name.trim().substring(0, 1).toUpperCase();
/*  60 */           if (name.length() > 1) {
/*  61 */             mName = mName + name.substring(1);
/*     */           }
/*  63 */           Class<? extends Dispatchable> c = (Class)dispatchable.getClass();
/*  64 */           Method actionM = c.getMethod(mName, new Class[0]);
/*  65 */           if (actionM != null) {
/*  66 */             Object o = actionM.invoke(dispatchable, (Object[])null);
/*  67 */             if (o == null) {
/*  68 */               throw new BuildException("Dispatchable Task attribute '" + name
/*  69 */                   .trim() + "' not set or value is empty.");
/*     */             }
/*     */             
/*  72 */             methodName = o.toString().trim();
/*  73 */             if (methodName.isEmpty()) {
/*  74 */               throw new BuildException("Dispatchable Task attribute '" + name
/*  75 */                   .trim() + "' not set or value is empty.");
/*     */             }
/*     */             
/*  78 */             Method executeM = dispatchable.getClass().getMethod(methodName, new Class[0]);
/*  79 */             if (executeM == null) {
/*  80 */               throw new BuildException("No public " + methodName + "() in " + dispatchable
/*     */                   
/*  82 */                   .getClass());
/*     */             }
/*  84 */             executeM.invoke(dispatchable, (Object[])null);
/*  85 */             if (task instanceof UnknownElement) {
/*  86 */               ((UnknownElement)task).setRealThing(null);
/*     */             }
/*     */           } 
/*  89 */         } catch (NoSuchMethodException nsme) {
/*  90 */           throw new BuildException("No public " + mName + "() in " + task.getClass());
/*     */         } 
/*     */       } else {
/*  93 */         Method executeM = null;
/*  94 */         executeM = task.getClass().getMethod(methodName, new Class[0]);
/*  95 */         if (executeM == null) {
/*  96 */           throw new BuildException("No public " + methodName + "() in " + task
/*  97 */               .getClass());
/*     */         }
/*  99 */         executeM.invoke(task, new Object[0]);
/* 100 */         if (task instanceof UnknownElement) {
/* 101 */           ((UnknownElement)task).setRealThing(null);
/*     */         }
/*     */       } 
/* 104 */     } catch (InvocationTargetException ie) {
/* 105 */       Throwable t = ie.getTargetException();
/* 106 */       if (t instanceof BuildException) {
/* 107 */         throw (BuildException)t;
/*     */       }
/* 109 */       throw new BuildException(t);
/*     */     }
/* 111 */     catch (NoSuchMethodException|IllegalAccessException e) {
/* 112 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/dispatch/DispatchUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */