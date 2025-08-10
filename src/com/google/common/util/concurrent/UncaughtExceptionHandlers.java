/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Locale;
/*    */ import java.util.logging.Level;
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
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class UncaughtExceptionHandlers
/*    */ {
/*    */   public static Thread.UncaughtExceptionHandler systemExit() {
/* 54 */     return new Exiter(Runtime.getRuntime());
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static final class Exiter implements Thread.UncaughtExceptionHandler {
/* 59 */     private static final LazyLogger logger = new LazyLogger(Exiter.class);
/*    */     
/*    */     private final Runtime runtime;
/*    */     
/*    */     Exiter(Runtime runtime) {
/* 64 */       this.runtime = runtime;
/*    */     }
/*    */ 
/*    */     
/*    */     public void uncaughtException(Thread t, Throwable e) {
/*    */       try {
/* 70 */         logger
/* 71 */           .get()
/* 72 */           .log(Level.SEVERE, 
/*    */             
/* 74 */             String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", new Object[] { t }), e);
/*    */       }
/* 76 */       catch (Throwable errorInLogging) {
/*    */ 
/*    */         
/* 79 */         System.err.println(e.getMessage());
/* 80 */         System.err.println(errorInLogging.getMessage());
/*    */       } finally {
/* 82 */         this.runtime.exit(1);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/UncaughtExceptionHandlers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */