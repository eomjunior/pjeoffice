/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.BooleanTimeout;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
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
/*     */ public final class Signer4jContext
/*     */ {
/*  41 */   private static final BooleanTimeout ESCAPE_DISCARDING = new BooleanTimeout("signer4j-context");
/*     */ 
/*     */ 
/*     */   
/*     */   static long timeout() {
/*  46 */     return ESCAPE_DISCARDING.getTimeout();
/*     */   }
/*     */   
/*     */   static boolean isDiscarded() {
/*  50 */     return ESCAPE_DISCARDING.isTrue();
/*     */   }
/*     */   
/*     */   static void discard() {
/*  54 */     ESCAPE_DISCARDING.setTrue();
/*  55 */     throw new InterruptedOperationException();
/*     */   }
/*     */   
/*     */   static void discard(Throwable e) {
/*  59 */     ESCAPE_DISCARDING.setTrue();
/*  60 */     throw new InterruptedOperationException(e);
/*     */   }
/*     */   
/*     */   static void discard(Runnable runnable, Throwable cause) {
/*  64 */     ESCAPE_DISCARDING.setTrue(runnable);
/*  65 */     throw new InterruptedOperationException(cause);
/*     */   }
/*     */   
/*     */   static void discardQuietly() {
/*  69 */     ESCAPE_DISCARDING.setTrue();
/*     */   }
/*     */   
/*     */   static void discardQuietly(Runnable runnable) {
/*  73 */     ESCAPE_DISCARDING.setTrue(runnable);
/*     */   }
/*     */   
/*     */   static void checkDiscarded() {
/*  77 */     if (Thread.currentThread().isInterrupted()) {
/*  78 */       discard();
/*     */     }
/*  80 */     if (isDiscarded()) {
/*  81 */       throw new InterruptedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   static final Consumer<Throwable> ifNotInterruptedDiscardWith(Runnable code) {
/*  86 */     return e -> {
/*  87 */         boolean isInterrupted = (e instanceof InterruptedException || e instanceof InterruptedOperationException);
/*     */         if (!isInterrupted) {
/*     */           discardQuietly(code);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static void discardAndWaitIf(Supplier<Boolean> condition) throws InterruptedException {
/*  96 */     Args.requireNonNull(condition, "condition is null");
/*  97 */     if (((Boolean)condition.get()).booleanValue()) {
/*  98 */       discardAndWait();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void discardAndWait() throws InterruptedException {
/* 103 */     AtomicBoolean timeout = new AtomicBoolean(true);
/* 104 */     discardQuietly(() -> {
/*     */           timeout.set(false);
/*     */           synchronized (timeout) {
/*     */             timeout.notifyAll();
/*     */           } 
/*     */         });
/* 110 */     while (timeout.get()) {
/* 111 */       synchronized (timeout) {
/* 112 */         timeout.wait();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Signer4jContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */