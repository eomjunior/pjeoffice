/*     */ package org.apache.log4j.or;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.apache.log4j.helpers.Loader;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.spi.RendererSupport;
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
/*     */ public class RendererMap
/*     */ {
/*  36 */   static ObjectRenderer defaultRenderer = new DefaultRenderer();
/*     */ 
/*     */   
/*  39 */   Hashtable map = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addRenderer(RendererSupport repository, String renderedClassName, String renderingClassName) {
/*  46 */     LogLog.debug("Rendering class: [" + renderingClassName + "], Rendered class: [" + renderedClassName + "].");
/*  47 */     ObjectRenderer renderer = (ObjectRenderer)OptionConverter.instantiateByClassName(renderingClassName, ObjectRenderer.class, null);
/*     */     
/*  49 */     if (renderer == null) {
/*  50 */       LogLog.error("Could not instantiate renderer [" + renderingClassName + "].");
/*     */       return;
/*     */     } 
/*     */     try {
/*  54 */       Class renderedClass = Loader.loadClass(renderedClassName);
/*  55 */       repository.setRenderer(renderedClass, renderer);
/*  56 */     } catch (ClassNotFoundException e) {
/*  57 */       LogLog.error("Could not find class [" + renderedClassName + "].", e);
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
/*     */   public String findAndRender(Object o) {
/*  69 */     if (o == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     return get(o.getClass()).doRender(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectRenderer get(Object o) {
/*  80 */     if (o == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     return get(o.getClass());
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
/*     */   public ObjectRenderer get(Class clazz) {
/* 143 */     ObjectRenderer r = null;
/* 144 */     for (Class c = clazz; c != null; c = c.getSuperclass()) {
/*     */       
/* 146 */       r = (ObjectRenderer)this.map.get(c);
/* 147 */       if (r != null) {
/* 148 */         return r;
/*     */       }
/* 150 */       r = searchInterfaces(c);
/* 151 */       if (r != null)
/* 152 */         return r; 
/*     */     } 
/* 154 */     return defaultRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ObjectRenderer searchInterfaces(Class c) {
/* 160 */     ObjectRenderer r = (ObjectRenderer)this.map.get(c);
/* 161 */     if (r != null) {
/* 162 */       return r;
/*     */     }
/* 164 */     Class[] ia = c.getInterfaces();
/* 165 */     for (int i = 0; i < ia.length; i++) {
/* 166 */       r = searchInterfaces(ia[i]);
/* 167 */       if (r != null) {
/* 168 */         return r;
/*     */       }
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */   
/*     */   public ObjectRenderer getDefaultRenderer() {
/* 175 */     return defaultRenderer;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 179 */     this.map.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Class<?> clazz, ObjectRenderer or) {
/* 186 */     this.map.put(clazz, or);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/or/RendererMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */