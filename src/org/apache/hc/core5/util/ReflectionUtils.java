/*    */ package org.apache.hc.core5.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.AccessController;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Internal
/*    */ public final class ReflectionUtils
/*    */ {
/*    */   public static void callSetter(Object object, String setterName, Class<?> type, Object value) {
/*    */     try {
/* 41 */       Class<?> clazz = object.getClass();
/* 42 */       Method method = clazz.getMethod("set" + setterName, new Class[] { type });
/* 43 */       setAccessible(method);
/* 44 */       method.invoke(object, new Object[] { value });
/* 45 */     } catch (Exception exception) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T callGetter(Object object, String getterName, Class<T> resultType) {
/*    */     try {
/* 51 */       Class<?> clazz = object.getClass();
/* 52 */       Method method = clazz.getMethod("get" + getterName, new Class[0]);
/* 53 */       setAccessible(method);
/* 54 */       return resultType.cast(method.invoke(object, new Object[0]));
/* 55 */     } catch (Exception ignore) {
/* 56 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void setAccessible(Method method) {
/* 61 */     AccessController.doPrivileged(() -> {
/*    */           method.setAccessible(true);
/*    */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   public static int determineJRELevel() {
/* 68 */     String s = System.getProperty("java.version");
/* 69 */     String[] parts = s.split("\\.");
/* 70 */     if (parts.length > 0) {
/*    */       try {
/* 72 */         int majorVersion = Integer.parseInt(parts[0]);
/* 73 */         if (majorVersion > 1)
/* 74 */           return majorVersion; 
/* 75 */         if (majorVersion == 1 && parts.length > 1) {
/* 76 */           return Integer.parseInt(parts[1]);
/*    */         }
/* 78 */       } catch (NumberFormatException numberFormatException) {}
/*    */     }
/*    */     
/* 81 */     return 7;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/ReflectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */