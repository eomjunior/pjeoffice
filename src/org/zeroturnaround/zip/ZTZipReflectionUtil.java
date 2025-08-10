/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ZTZipReflectionUtil
/*    */ {
/*    */   static <T> Class<? extends T> getClassForName(String name, Class<T> clazz) {
/*    */     try {
/* 13 */       return Class.forName(name).asSubclass(clazz);
/*    */     }
/* 15 */     catch (ClassNotFoundException e) {
/* 16 */       throw new ZipException(e);
/*    */     }
/* 18 */     catch (ClassCastException e) {
/* 19 */       throw new ZipException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
/*    */     try {
/* 25 */       return clazz.getDeclaredMethod(methodName, parameterTypes);
/*    */     }
/* 27 */     catch (NoSuchMethodException e) {
/* 28 */       throw new ZipException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   static Object invoke(Method method, Object obj, Object... args) throws ZipException {
/*    */     try {
/* 34 */       return method.invoke(obj, args);
/*    */     }
/* 36 */     catch (IllegalAccessException e) {
/* 37 */       throw new ZipException(e);
/*    */     }
/* 39 */     catch (InvocationTargetException e) {
/* 40 */       throw new ZipException(e);
/*    */     }
/* 42 */     catch (IllegalArgumentException e) {
/* 43 */       throw new ZipException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZTZipReflectionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */