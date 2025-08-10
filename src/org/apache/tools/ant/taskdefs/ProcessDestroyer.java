/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ class ProcessDestroyer
/*     */   implements Runnable
/*     */ {
/*     */   private static final int THREAD_DIE_TIMEOUT = 20000;
/*  34 */   private final Set<Process> processes = new HashSet<>();
/*     */   
/*     */   private Method addShutdownHookMethod;
/*     */   private Method removeShutdownHookMethod;
/*  38 */   private ProcessDestroyerImpl destroyProcessThread = null;
/*     */   
/*     */   private boolean added = false;
/*     */   
/*     */   private boolean running = false;
/*     */ 
/*     */   
/*     */   private class ProcessDestroyerImpl
/*     */     extends Thread
/*     */   {
/*     */     private boolean shouldDestroy = true;
/*     */     
/*     */     public ProcessDestroyerImpl() {
/*  51 */       super("ProcessDestroyer Shutdown Hook");
/*     */     }
/*     */     
/*     */     public void run() {
/*  55 */       if (this.shouldDestroy) {
/*  56 */         ProcessDestroyer.this.run();
/*     */       }
/*     */     }
/*     */     
/*     */     public void setShouldDestroy(boolean shouldDestroy) {
/*  61 */       this.shouldDestroy = shouldDestroy;
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
/*     */ 
/*     */   
/*     */   ProcessDestroyer() {
/*     */     try {
/*  79 */       this
/*  80 */         .addShutdownHookMethod = Runtime.class.getMethod("addShutdownHook", new Class[] { Thread.class });
/*     */       
/*  82 */       this
/*  83 */         .removeShutdownHookMethod = Runtime.class.getMethod("removeShutdownHook", new Class[] { Thread.class });
/*     */     }
/*  85 */     catch (NoSuchMethodException noSuchMethodException) {
/*     */     
/*  87 */     } catch (Exception e) {
/*  88 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addShutdownHook() {
/*  97 */     if (this.addShutdownHookMethod != null && !this.running) {
/*  98 */       this.destroyProcessThread = new ProcessDestroyerImpl();
/*     */       try {
/* 100 */         this.addShutdownHookMethod.invoke(Runtime.getRuntime(), new Object[] { this.destroyProcessThread });
/* 101 */         this.added = true;
/* 102 */       } catch (IllegalAccessException e) {
/* 103 */         e.printStackTrace();
/* 104 */       } catch (InvocationTargetException e) {
/* 105 */         Throwable t = e.getTargetException();
/* 106 */         if (t != null && t.getClass() == IllegalStateException.class) {
/*     */           
/* 108 */           this.running = true;
/*     */         } else {
/* 110 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeShutdownHook() {
/* 121 */     if (this.removeShutdownHookMethod != null && this.added && !this.running) {
/*     */       try {
/* 123 */         if (!Boolean.TRUE.equals(this.removeShutdownHookMethod
/* 124 */             .invoke(Runtime.getRuntime(), new Object[] { this.destroyProcessThread }))) {
/* 125 */           System.err.println("Could not remove shutdown hook");
/*     */         }
/* 127 */       } catch (IllegalAccessException e) {
/* 128 */         e.printStackTrace();
/* 129 */       } catch (InvocationTargetException e) {
/* 130 */         Throwable t = e.getTargetException();
/* 131 */         if (t != null && t.getClass() == IllegalStateException.class) {
/*     */           
/* 133 */           this.running = true;
/*     */         } else {
/* 135 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 141 */       this.destroyProcessThread.setShouldDestroy(false);
/* 142 */       if (!this.destroyProcessThread.getThreadGroup().isDestroyed())
/*     */       {
/*     */         
/* 145 */         this.destroyProcessThread.start();
/*     */       }
/*     */       
/*     */       try {
/* 149 */         this.destroyProcessThread.join(20000L);
/* 150 */       } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */       
/* 154 */       this.destroyProcessThread = null;
/* 155 */       this.added = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAddedAsShutdownHook() {
/* 165 */     return this.added;
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
/*     */   public boolean add(Process process) {
/* 177 */     synchronized (this.processes) {
/*     */       
/* 179 */       if (this.processes.isEmpty()) {
/* 180 */         addShutdownHook();
/*     */       }
/* 182 */       return this.processes.add(process);
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
/*     */   public boolean remove(Process process) {
/* 195 */     synchronized (this.processes) {
/* 196 */       boolean processRemoved = this.processes.remove(process);
/* 197 */       if (processRemoved && this.processes.isEmpty()) {
/* 198 */         removeShutdownHook();
/*     */       }
/* 200 */       return processRemoved;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 209 */     synchronized (this.processes) {
/* 210 */       this.running = true;
/* 211 */       this.processes.forEach(Process::destroy);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ProcessDestroyer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */