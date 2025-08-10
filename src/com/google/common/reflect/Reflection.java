/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Proxy;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ public final class Reflection
/*    */ {
/*    */   public static String getPackageName(Class<?> clazz) {
/* 37 */     return getPackageName(clazz.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getPackageName(String classFullName) {
/* 46 */     int lastDot = classFullName.lastIndexOf('.');
/* 47 */     return (lastDot < 0) ? "" : classFullName.substring(0, lastDot);
/*    */   }
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
/*    */   public static void initialize(Class<?>... classes) {
/* 62 */     for (Class<?> clazz : classes) {
/*    */       try {
/* 64 */         Class.forName(clazz.getName(), true, clazz.getClassLoader());
/* 65 */       } catch (ClassNotFoundException e) {
/* 66 */         throw new AssertionError(e);
/*    */       } 
/*    */     } 
/*    */   }
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
/*    */   public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 81 */     Preconditions.checkNotNull(handler);
/* 82 */     Preconditions.checkArgument(interfaceType.isInterface(), "%s is not an interface", interfaceType);
/*    */     
/* 84 */     Object object = Proxy.newProxyInstance(interfaceType
/* 85 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 86 */     return interfaceType.cast(object);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/Reflection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */