/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Reflections
/*     */ {
/*     */   public static <T extends java.lang.annotation.Annotation> boolean isAnnotationPresent(Class<?> clazz, Class<T> annotationType) {
/*  38 */     return (getAnnotation(clazz, annotationType) != null);
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> boolean isAnnotationPresent(Class<?> clazz, Method method, Class<T> annotationType) {
/*  42 */     return (getAnnotation(clazz, method, annotationType) != null);
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> boolean isAnnotationPresent(Object instance, Method method, Class<T> annotationType) {
/*  46 */     return (getAnnotation(instance, method, annotationType) != null);
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> T getAnnotation(Object instance, Method method, Class<T> annotationType) {
/*  50 */     return getAnnotation(instance.getClass(), method, annotationType);
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> T getAnnotation(Class<?> clazz, Method method, Class<T> annotationType) {
/*  54 */     T a = method.getAnnotation(annotationType);
/*  55 */     if (a != null) {
/*  56 */       return a;
/*     */     }
/*  58 */     return getAnnotation(clazz, annotationType, method.getName(), method.getParameterTypes());
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationType, String methodName, Class<?>[] parameterTypes) {
/*     */     Method m;
/*     */     try {
/*  64 */       m = clazz.getMethod(methodName, parameterTypes);
/*  65 */     } catch (Exception e) {
/*  66 */       m = null;
/*     */     } 
/*  68 */     if (m != null) {
/*  69 */       T a = m.getAnnotation(annotationType);
/*  70 */       if (a != null) {
/*  71 */         return a;
/*     */       }
/*     */     } 
/*  74 */     Class<?> superclass = clazz.getSuperclass();
/*  75 */     if (superclass == null) {
/*  76 */       return null;
/*     */     }
/*  78 */     return getAnnotation(superclass, annotationType, methodName, parameterTypes);
/*     */   }
/*     */   
/*     */   public static <T extends java.lang.annotation.Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationType) {
/*  82 */     T result = clazz.getAnnotation(annotationType);
/*  83 */     if (result == null) {
/*  84 */       Class<?> superclass = clazz.getSuperclass();
/*  85 */       if (superclass != null) {
/*  86 */         return getAnnotation(superclass, annotationType);
/*     */       }
/*  88 */       return null;
/*     */     } 
/*     */     
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String signature(Method method, Object[] args) {
/*  96 */     StringBuilder b = new StringBuilder();
/*  97 */     b.append(method.getReturnType().getSimpleName())
/*  98 */       .append(' ')
/*  99 */       .append(method.getName())
/* 100 */       .append('(');
/* 101 */     if (args != null) {
/* 102 */       for (int i = 0; i < args.length; i++) {
/* 103 */         Object o = args[i];
/* 104 */         if (i > 0) {
/* 105 */           b.append(", ");
/*     */         }
/* 107 */         Class<?> type = o.getClass();
/* 108 */         if (type.isPrimitive() || Number.class.isInstance(o)) {
/* 109 */           b.append(o.toString());
/*     */         } else {
/* 111 */           b.append("object");
/*     */         } 
/*     */       } 
/*     */     }
/* 115 */     b.append(')');
/* 116 */     return b.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Reflections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */