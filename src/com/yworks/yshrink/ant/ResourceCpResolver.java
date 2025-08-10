/*    */ package com.yworks.yshrink.ant;
/*    */ 
/*    */ import com.yworks.logging.Logger;
/*    */ import com.yworks.yshrink.core.ClassResolver;
/*    */ import java.io.File;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.types.Path;
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
/*    */ public class ResourceCpResolver
/*    */   implements ClassResolver
/*    */ {
/*    */   private Path resource;
/*    */   URLClassLoader urlClassLoader;
/*    */   
/*    */   public ResourceCpResolver(Path resources, Task target) {
/* 35 */     this.resource = resources;
/* 36 */     String[] list = resources.list();
/* 37 */     List<URL> listUrls = new ArrayList();
/* 38 */     for (int i = 0; i < list.length; i++) {
/*    */       try {
/* 40 */         URL url = (new File(list[i])).toURL();
/* 41 */         listUrls.add(url);
/* 42 */       } catch (MalformedURLException mfue) {
/* 43 */         Logger.err("Could not resolve resource: " + mfue);
/* 44 */         target.getProject().log(target, "Could not resolve resource: " + mfue, 1);
/*    */       } 
/*    */     } 
/* 47 */     URL[] urls = new URL[listUrls.size()];
/* 48 */     listUrls.toArray(urls);
/* 49 */     this.urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
/*    */   }
/*    */   
/*    */   public Class resolve(String className) throws ClassNotFoundException {
/*    */     try {
/* 54 */       return Class.forName(className, false, this.urlClassLoader);
/* 55 */     } catch (NoClassDefFoundError ncdfe) {
/* 56 */       String message = ncdfe.getMessage();
/* 57 */       if (message == null || message.equals(className)) {
/* 58 */         message = className;
/*    */       } else {
/* 60 */         message = message + "[" + className + "]";
/*    */       } 
/* 62 */       throw new ClassNotFoundException(message, ncdfe);
/* 63 */     } catch (LinkageError le) {
/* 64 */       throw new ClassNotFoundException(className, le);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws Exception {
/* 70 */     this.urlClassLoader.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/ResourceCpResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */