/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public class FinalizableReferenceQueue
/*     */   implements Closeable
/*     */ {
/* 136 */   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
/*     */ 
/*     */   
/*     */   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
/*     */   
/*     */   private static final Method startFinalizer;
/*     */ 
/*     */   
/*     */   static {
/* 145 */     Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
/* 146 */     startFinalizer = getStartFinalizer(finalizer);
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
/* 160 */   final ReferenceQueue<Object> queue = new ReferenceQueue();
/* 161 */   final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue); public FinalizableReferenceQueue() {
/* 162 */     boolean threadStarted = false;
/*     */     try {
/* 164 */       startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
/* 165 */       threadStarted = true;
/* 166 */     } catch (IllegalAccessException impossible) {
/* 167 */       throw new AssertionError(impossible);
/* 168 */     } catch (Throwable t) {
/* 169 */       logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     this.threadStarted = threadStarted;
/*     */   }
/*     */   final boolean threadStarted;
/*     */   
/*     */   public void close() {
/* 181 */     this.frqRef.enqueue();
/* 182 */     cleanUp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanUp() {
/* 191 */     if (this.threadStarted) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Reference<?> reference;
/* 196 */     while ((reference = this.queue.poll()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 201 */       reference.clear();
/*     */       try {
/* 203 */         ((FinalizableReference)reference).finalizeReferent();
/* 204 */       } catch (Throwable t) {
/* 205 */         logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
/* 216 */     for (FinalizerLoader loader : loaders) {
/* 217 */       Class<?> finalizer = loader.loadFinalizer();
/* 218 */       if (finalizer != null) {
/* 219 */         return finalizer;
/*     */       }
/*     */     } 
/*     */     
/* 223 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface FinalizerLoader
/*     */   {
/*     */     @CheckForNull
/*     */     Class<?> loadFinalizer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SystemLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     @VisibleForTesting
/*     */     static boolean disabled;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Class<?> loadFinalizer() {
/*     */       ClassLoader systemLoader;
/* 250 */       if (disabled) {
/* 251 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 255 */         systemLoader = ClassLoader.getSystemClassLoader();
/* 256 */       } catch (SecurityException e) {
/* 257 */         FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
/* 258 */         return null;
/*     */       } 
/* 260 */       if (systemLoader != null) {
/*     */         try {
/* 262 */           return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 263 */         } catch (ClassNotFoundException e) {
/*     */           
/* 265 */           return null;
/*     */         } 
/*     */       }
/* 268 */       return null;
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
/*     */   static class DecoupledLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";
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
/*     */     @CheckForNull
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 298 */         ClassLoader finalizerLoader = newLoader(getBaseUrl());
/* 299 */         return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
/* 300 */       } catch (Exception e) {
/* 301 */         FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.", e);
/* 302 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     URL getBaseUrl() throws IOException {
/* 309 */       String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
/* 310 */       URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
/* 311 */       if (finalizerUrl == null) {
/* 312 */         throw new FileNotFoundException(finalizerPath);
/*     */       }
/*     */ 
/*     */       
/* 316 */       String urlString = finalizerUrl.toString();
/* 317 */       if (!urlString.endsWith(finalizerPath)) {
/* 318 */         throw new IOException("Unsupported path style: " + urlString);
/*     */       }
/* 320 */       urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
/* 321 */       return new URL(finalizerUrl, urlString);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     URLClassLoader newLoader(URL base) {
/* 329 */       return new URLClassLoader(new URL[] { base }, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DirectLoader
/*     */     implements FinalizerLoader
/*     */   {
/*     */     public Class<?> loadFinalizer() {
/*     */       try {
/* 341 */         return Class.forName("com.google.common.base.internal.Finalizer");
/* 342 */       } catch (ClassNotFoundException e) {
/* 343 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Method getStartFinalizer(Class<?> finalizer) {
/*     */     try {
/* 351 */       return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
/*     */     }
/* 353 */     catch (NoSuchMethodException e) {
/* 354 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/FinalizableReferenceQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */