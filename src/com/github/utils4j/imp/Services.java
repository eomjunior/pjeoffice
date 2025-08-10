/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public final class Services
/*    */ {
/* 37 */   private static final Logger LOGGER = LoggerFactory.getLogger(Services.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void shutdownNow(ExecutorService pool) {
/* 43 */     shutdownNow(pool, 5L, 2147483647);
/*    */   }
/*    */   
/*    */   public static void shutdownNow(ExecutorService pool, long timeoutSec) {
/* 47 */     shutdownNow(pool, timeoutSec, 2147483647);
/*    */   }
/*    */   
/*    */   public static void shutdownNow(ExecutorService pool, long timeoutSec, int attemptCount) {
/* 51 */     if (pool != null) {
/* 52 */       LOGGER.debug("Impedindo que novas tarefas sejam submetidas");
/* 53 */       pool.shutdown();
/* 54 */       LOGGER.debug("Cancelando tarefas atualmente em execução");
/* 55 */       pool.shutdownNow();
/* 56 */       waitFor(pool, timeoutSec, attemptCount);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void shutdown(ExecutorService pool) {
/* 61 */     if (pool != null) {
/* 62 */       pool.shutdown();
/* 63 */       waitFor(pool, 60L, 2147483647);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void waitFor(ExecutorService pool, long timeout, int attemptCount) {
/* 68 */     boolean shutdown = false;
/*    */     do {
/*    */       try {
/* 71 */         LOGGER.debug("Aguardando enquanto houver tarefas sendo finalizadas por " + timeout + " segundos");
/* 72 */         shutdown = pool.awaitTermination(timeout, TimeUnit.SECONDS);
/* 73 */       } catch (InterruptedException ie) {
/* 74 */         LOGGER.debug("Capturada InterruptedException");
/* 75 */         Thread.currentThread().interrupt();
/*    */       } 
/* 77 */       if (shutdown)
/* 78 */         continue;  LOGGER.debug("Novo pedido de cancelamento de tarefas em execução");
/* 79 */       pool.shutdownNow();
/*    */     }
/* 81 */     while (!shutdown && --attemptCount > 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Services.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */