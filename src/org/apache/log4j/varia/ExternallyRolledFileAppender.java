/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import org.apache.log4j.RollingFileAppender;
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
/*     */ public class ExternallyRolledFileAppender
/*     */   extends RollingFileAppender
/*     */ {
/*     */   public static final String ROLL_OVER = "RollOver";
/*     */   public static final String OK = "OK";
/*  66 */   int port = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HUP hup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  80 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  87 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*  95 */     super.activateOptions();
/*  96 */     if (this.port != 0) {
/*  97 */       if (this.hup != null) {
/*  98 */         this.hup.interrupt();
/*     */       }
/* 100 */       this.hup = new HUP(this, this.port);
/* 101 */       this.hup.setDaemon(true);
/* 102 */       this.hup.start();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/ExternallyRolledFileAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */