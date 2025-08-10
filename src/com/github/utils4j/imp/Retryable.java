/*    */ package com.github.utils4j.imp;
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
/*    */ public final class Retryable<T>
/*    */ {
/*    */   private final Executable<T> executable;
/*    */   
/*    */   public static <T> T attempt(long interval, long timeout, Executable<T> exec) throws RetryTimeoutException, Exception {
/* 33 */     return attempt(0L, interval, timeout, exec);
/*    */   }
/*    */   
/*    */   public static <T> T attempt(long delay, long interval, long timeout, Executable<T> exec) throws RetryTimeoutException, Exception {
/* 37 */     return (new Retryable<>(exec)).execute(delay, interval, timeout);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Retryable(Executable<T> executable) {
/* 48 */     this.executable = executable;
/*    */   }
/*    */   
/*    */   private T execute(long delay, long interval, long timeout) throws RetryTimeoutException, Exception {
/* 52 */     long start = System.currentTimeMillis();
/* 53 */     if (delay > 0L)
/* 54 */       Threads.sleep(delay); 
/*    */     while (true) {
/*    */       try {
/* 57 */         return this.executable.execute();
/* 58 */       } catch (TemporaryException e) {
/* 59 */         if (System.currentTimeMillis() - start < timeout) {
/* 60 */           Threads.sleep(interval); continue;
/*    */         }  break;
/*    */       } 
/* 63 */     }  throw new RetryTimeoutException(e);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Executable<T> {
/*    */     T execute() throws TemporaryException, Exception;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Retryable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */