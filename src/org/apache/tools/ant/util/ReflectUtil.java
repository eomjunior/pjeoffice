/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectUtil
/*     */ {
/*     */   public static <T> T newInstance(Class<T> ofClass, Class<?>[] argTypes, Object[] args) {
/*     */     try {
/*  56 */       Constructor<T> con = ofClass.getConstructor(argTypes);
/*  57 */       return con.newInstance(args);
/*  58 */     } catch (Exception t) {
/*  59 */       throwBuildException(t);
/*  60 */       return null;
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
/*     */   public static <T> T invoke(Object obj, String methodName) {
/*     */     try {
/*  74 */       Method method = obj.getClass().getMethod(methodName, new Class[0]);
/*  75 */       return (T)method.invoke(obj, new Object[0]);
/*  76 */     } catch (Exception t) {
/*  77 */       throwBuildException(t);
/*  78 */       return null;
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
/*     */   public static <T> T invokeStatic(Object obj, String methodName) {
/*     */     try {
/*  94 */       Method method = ((Class)obj).getMethod(methodName, new Class[0]);
/*  95 */       return (T)method.invoke(obj, new Object[0]);
/*  96 */     } catch (Exception t) {
/*  97 */       throwBuildException(t);
/*  98 */       return null;
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
/*     */   public static <T> T invoke(Object obj, String methodName, Class<?> argType, Object arg) {
/*     */     try {
/* 115 */       Method method = obj.getClass().getMethod(methodName, new Class[] { argType });
/* 116 */       return (T)method.invoke(obj, new Object[] { arg });
/* 117 */     } catch (Exception t) {
/* 118 */       throwBuildException(t);
/* 119 */       return null;
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
/*     */   public static <T> T invoke(Object obj, String methodName, Class<?> argType1, Object arg1, Class<?> argType2, Object arg2) {
/*     */     try {
/* 140 */       Method method = obj.getClass().getMethod(methodName, new Class[] { argType1, argType2 });
/* 141 */       return (T)method.invoke(obj, new Object[] { arg1, arg2 });
/* 142 */     } catch (Exception t) {
/* 143 */       throwBuildException(t);
/* 144 */       return null;
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
/*     */   public static <T> T getField(Object obj, String fieldName) throws BuildException {
/*     */     try {
/* 160 */       Field field = obj.getClass().getDeclaredField(fieldName);
/* 161 */       field.setAccessible(true);
/* 162 */       return (T)field.get(obj);
/* 163 */     } catch (Exception t) {
/* 164 */       throwBuildException(t);
/* 165 */       return null;
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
/*     */   public static void throwBuildException(Exception t) throws BuildException {
/* 177 */     throw toBuildException(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BuildException toBuildException(Exception t) {
/* 188 */     if (t instanceof InvocationTargetException) {
/*     */       
/* 190 */       Throwable t2 = ((InvocationTargetException)t).getTargetException();
/* 191 */       if (t2 instanceof BuildException) {
/* 192 */         return (BuildException)t2;
/*     */       }
/* 194 */       return new BuildException(t2);
/*     */     } 
/* 196 */     return new BuildException(t);
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
/*     */   public static boolean respondsTo(Object o, String methodName) throws BuildException {
/*     */     try {
/* 210 */       return Stream.<Method>of(o.getClass().getMethods()).map(Method::getName)
/* 211 */         .anyMatch(Predicate.isEqual(methodName));
/* 212 */     } catch (Exception t) {
/* 213 */       throw toBuildException(t);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ReflectUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */