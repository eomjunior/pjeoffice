/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IPathCollectionStrategy;
/*     */ import com.github.signer4j.IWindowLockDettector;
/*     */ import com.github.utils4j.imp.Jvms;
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
/*     */ public enum SystemSupport
/*     */   implements IModuleExtension
/*     */ {
/*  35 */   WINDOWS(".dll")
/*     */   {
/*     */     protected IPathCollectionStrategy createDriverLookupStrategy() {
/*  38 */       return new ForWindowsStrategy();
/*     */     }
/*     */ 
/*     */     
/*     */     protected IWindowLockDettector getWindowLockDettector() {
/*  43 */       return WindowLockDettector.WINDOWS;
/*     */     }
/*     */   },
/*     */   
/*  47 */   LINUX(".so")
/*     */   {
/*     */     protected IPathCollectionStrategy createDriverLookupStrategy() {
/*  50 */       return new ForLinuxStrategy();
/*     */     }
/*     */ 
/*     */     
/*     */     protected IWindowLockDettector getWindowLockDettector() {
/*  55 */       return WindowLockDettector.LINUX;
/*     */     }
/*     */   },
/*     */   
/*  59 */   MAC(".dylib")
/*     */   {
/*     */     protected IPathCollectionStrategy createDriverLookupStrategy() {
/*  62 */       return new ForMacStrategy();
/*     */     }
/*     */ 
/*     */     
/*     */     protected IWindowLockDettector getWindowLockDettector() {
/*  67 */       return WindowLockDettector.MAC;
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  72 */   UNKNOWN(".unknown")
/*     */   {
/*     */     protected IPathCollectionStrategy createDriverLookupStrategy() {
/*  75 */       return IPathCollectionStrategy.NOTHING;
/*     */     }
/*     */ 
/*     */     
/*     */     protected IWindowLockDettector getWindowLockDettector() {
/*  80 */       return WindowLockDettector.IDLE;
/*     */     }
/*     */   };
/*     */   
/*     */   private static SystemSupport SYSTEM;
/*     */   
/*     */   public static SystemSupport getDefault() {
/*  87 */     return (SYSTEM != null) ? SYSTEM : (
/*     */ 
/*     */       
/*  90 */       SYSTEM = Jvms.isWindows() ? WINDOWS : (Jvms.isUnix() ? LINUX : (Jvms.isMac() ? MAC : UNKNOWN)));
/*     */   }
/*     */ 
/*     */   
/*     */   private final String extension;
/*     */   
/*     */   private IPathCollectionStrategy finder;
/*     */   
/*     */   SystemSupport(String extension) {
/*  99 */     this.extension = extension;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getExtension() {
/* 104 */     return this.extension;
/*     */   }
/*     */   
/*     */   public final IPathCollectionStrategy getStrategy() {
/* 108 */     return (this.finder == null) ? (this.finder = createDriverLookupStrategy()) : this.finder;
/*     */   }
/*     */   
/*     */   protected abstract IPathCollectionStrategy createDriverLookupStrategy();
/*     */   
/*     */   protected abstract IWindowLockDettector getWindowLockDettector();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SystemSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */