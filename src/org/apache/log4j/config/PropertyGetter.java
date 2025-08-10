/*     */ package org.apache.log4j.config;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyGetter
/*     */ {
/*  37 */   protected static final Object[] NULL_ARG = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object obj;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyDescriptor[] props;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyGetter(Object obj) throws IntrospectionException {
/*  54 */     BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
/*  55 */     this.props = bi.getPropertyDescriptors();
/*  56 */     this.obj = obj;
/*     */   }
/*     */   
/*     */   public static void getProperties(Object obj, PropertyCallback callback, String prefix) {
/*     */     try {
/*  61 */       (new PropertyGetter(obj)).getProperties(callback, prefix);
/*  62 */     } catch (IntrospectionException ex) {
/*  63 */       LogLog.error("Failed to introspect object " + obj, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getProperties(PropertyCallback callback, String prefix) {
/*  68 */     for (int i = 0; i < this.props.length; i++) {
/*  69 */       Method getter = this.props[i].getReadMethod();
/*  70 */       if (getter != null)
/*     */       {
/*  72 */         if (isHandledType(getter.getReturnType())) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  77 */           String name = this.props[i].getName();
/*     */           try {
/*  79 */             Object result = getter.invoke(this.obj, NULL_ARG);
/*     */             
/*  81 */             if (result != null) {
/*  82 */               callback.foundProperty(this.obj, prefix, name, result);
/*     */             }
/*  84 */           } catch (IllegalAccessException ex) {
/*  85 */             LogLog.warn("Failed to get value of property " + name);
/*  86 */           } catch (InvocationTargetException ex) {
/*  87 */             if (ex.getTargetException() instanceof InterruptedException || ex
/*  88 */               .getTargetException() instanceof java.io.InterruptedIOException) {
/*  89 */               Thread.currentThread().interrupt();
/*     */             }
/*  91 */             LogLog.warn("Failed to get value of property " + name);
/*  92 */           } catch (RuntimeException ex) {
/*  93 */             LogLog.warn("Failed to get value of property " + name);
/*     */           } 
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   protected boolean isHandledType(Class<?> type) {
/*  99 */     return (String.class.isAssignableFrom(type) || int.class.isAssignableFrom(type) || long.class
/* 100 */       .isAssignableFrom(type) || boolean.class.isAssignableFrom(type) || Priority.class
/* 101 */       .isAssignableFrom(type));
/*     */   }
/*     */   
/*     */   public static interface PropertyCallback {
/*     */     void foundProperty(Object param1Object1, String param1String1, String param1String2, Object param1Object2);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/config/PropertyGetter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */