/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
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
/*    */ public class ReflectWrapper
/*    */ {
/*    */   private Object obj;
/*    */   
/*    */   public ReflectWrapper(ClassLoader loader, String name) {
/*    */     try {
/* 39 */       Class<?> clazz = Class.forName(name, true, loader);
/* 40 */       Constructor<?> constructor = clazz.getConstructor(new Class[0]);
/* 41 */       this.obj = constructor.newInstance(new Object[0]);
/* 42 */     } catch (Exception t) {
/* 43 */       ReflectUtil.throwBuildException(t);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ReflectWrapper(Object obj) {
/* 52 */     this.obj = obj;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getObject() {
/* 61 */     return (T)this.obj;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T invoke(String methodName) {
/* 71 */     return ReflectUtil.invoke(this.obj, methodName);
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
/*    */   public <T> T invoke(String methodName, Class<?> argType, Object arg) {
/* 83 */     return ReflectUtil.invoke(this.obj, methodName, argType, arg);
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
/*    */   public <T> T invoke(String methodName, Class<?> argType1, Object arg1, Class<?> argType2, Object arg2) {
/* 98 */     return ReflectUtil.invoke(this.obj, methodName, argType1, arg1, argType2, arg2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ReflectWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */