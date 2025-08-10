/*    */ package com.yworks.yshrink.core;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
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
/*    */ public class URLCpResolver
/*    */   implements ClassResolver
/*    */ {
/*    */   URLClassLoader urlClassLoader;
/*    */   
/*    */   public URLCpResolver(URL[] urls) {
/* 24 */     this.urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
/*    */   }
/*    */   
/*    */   public Class resolve(String className) throws ClassNotFoundException {
/*    */     try {
/* 29 */       return Class.forName(className, false, this.urlClassLoader);
/* 30 */     } catch (NoClassDefFoundError ncdfe) {
/* 31 */       String message = ncdfe.getMessage();
/* 32 */       if (message == null || message.equals(className)) {
/* 33 */         message = className;
/*    */       } else {
/* 35 */         message = message + "[" + className + "]";
/*    */       } 
/* 37 */       throw new ClassNotFoundException(message, ncdfe);
/* 38 */     } catch (LinkageError le) {
/* 39 */       throw new ClassNotFoundException(className, le);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws Exception {
/* 45 */     this.urlClassLoader.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/URLCpResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */