/*     */ package com.google.common.base.internal;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class Finalizer
/*     */   implements Runnable
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeakReference<Class<?>> finalizableReferenceClassReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PhantomReference<Object> frqReference;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ReferenceQueue<Object> queue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/*  75 */     if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
/*  76 */       throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
/*     */     }
/*     */     
/*  79 */     Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
/*  80 */     String threadName = Finalizer.class.getName();
/*  81 */     Thread thread = null;
/*  82 */     if (bigThreadConstructor != null) {
/*     */       try {
/*  84 */         boolean inheritThreadLocals = false;
/*  85 */         long defaultStackSize = 0L;
/*     */         
/*  87 */         thread = bigThreadConstructor.newInstance(new Object[] {
/*  88 */               null, finalizer, threadName, Long.valueOf(defaultStackSize), Boolean.valueOf(inheritThreadLocals) });
/*  89 */       } catch (Throwable t) {
/*  90 */         logger.log(Level.INFO, "Failed to create a thread without inherited thread-local values", t);
/*     */       } 
/*     */     }
/*     */     
/*  94 */     if (thread == null) {
/*  95 */       thread = new Thread((ThreadGroup)null, finalizer, threadName);
/*     */     }
/*  97 */     thread.setDaemon(true);
/*     */     
/*     */     try {
/* 100 */       if (inheritableThreadLocals != null) {
/* 101 */         inheritableThreadLocals.set(thread, null);
/*     */       }
/* 103 */     } catch (Throwable t) {
/* 104 */       logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/* 121 */   private static final Constructor<Thread> bigThreadConstructor = getBigThreadConstructor();
/*     */ 
/*     */   
/*     */   @CheckForNull
/* 125 */   private static final Field inheritableThreadLocals = (bigThreadConstructor == null) ? getInheritableThreadLocalsField() : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference) {
/* 132 */     this.queue = queue;
/*     */     
/* 134 */     this.finalizableReferenceClassReference = new WeakReference<>(finalizableReferenceClass);
/*     */ 
/*     */     
/* 137 */     this.frqReference = frqReference;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       try {
/*     */         do {
/*     */         
/* 146 */         } while (cleanUp(this.queue.remove()));
/*     */         
/*     */         break;
/* 149 */       } catch (InterruptedException interruptedException) {}
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
/*     */   private boolean cleanUp(Reference<?> firstReference) {
/* 163 */     Method finalizeReferentMethod = getFinalizeReferentMethod();
/* 164 */     if (finalizeReferentMethod == null) {
/* 165 */       return false;
/*     */     }
/*     */     
/* 168 */     if (!finalizeReference(firstReference, finalizeReferentMethod)) {
/* 169 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 177 */       Reference<?> furtherReference = this.queue.poll();
/* 178 */       if (furtherReference == null) {
/* 179 */         return true;
/*     */       }
/* 181 */       if (!finalizeReference(furtherReference, finalizeReferentMethod)) {
/* 182 */         return false;
/*     */       }
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
/*     */   
/*     */   private boolean finalizeReference(Reference<?> reference, Method finalizeReferentMethod) {
/* 198 */     reference.clear();
/*     */     
/* 200 */     if (reference == this.frqReference)
/*     */     {
/*     */ 
/*     */       
/* 204 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 208 */       finalizeReferentMethod.invoke(reference, new Object[0]);
/* 209 */     } catch (Throwable t) {
/* 210 */       logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
/*     */     } 
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private Method getFinalizeReferentMethod() {
/* 218 */     Class<?> finalizableReferenceClass = this.finalizableReferenceClassReference.get();
/* 219 */     if (finalizableReferenceClass == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 226 */       return null;
/*     */     }
/*     */     try {
/* 229 */       return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
/* 230 */     } catch (NoSuchMethodException e) {
/* 231 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static Field getInheritableThreadLocalsField() {
/*     */     try {
/* 238 */       Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
/* 239 */       inheritableThreadLocals.setAccessible(true);
/* 240 */       return inheritableThreadLocals;
/* 241 */     } catch (Throwable t) {
/* 242 */       logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
/*     */ 
/*     */ 
/*     */       
/* 246 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static Constructor<Thread> getBigThreadConstructor() {
/*     */     try {
/* 253 */       return Thread.class.getConstructor(new Class[] { ThreadGroup.class, Runnable.class, String.class, long.class, boolean.class });
/*     */     }
/* 255 */     catch (Throwable t) {
/*     */       
/* 257 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/internal/Finalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */