/*     */ package org.apache.log4j;
/*     */ 
/*     */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*     */ import org.apache.log4j.helpers.BoundedFIFO;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Dispatcher
/*     */   extends Thread
/*     */ {
/*     */   private BoundedFIFO bf;
/*     */   private AppenderAttachableImpl aai;
/*     */   private boolean interrupted = false;
/*     */   AsyncAppender container;
/*     */   
/*     */   Dispatcher(BoundedFIFO bf, AsyncAppender container) {
/*  44 */     this.bf = bf;
/*  45 */     this.container = container;
/*  46 */     this.aai = container.aai;
/*     */ 
/*     */ 
/*     */     
/*  50 */     setDaemon(true);
/*     */ 
/*     */     
/*  53 */     setPriority(1);
/*  54 */     setName("Dispatcher-" + getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void close() {
/*  62 */     synchronized (this.bf) {
/*  63 */       this.interrupted = true;
/*     */ 
/*     */ 
/*     */       
/*  67 */       if (this.bf.length() == 0) {
/*  68 */         this.bf.notify();
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
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       LoggingEvent event;
/*  88 */       synchronized (this.bf) {
/*  89 */         if (this.bf.length() == 0) {
/*     */           
/*  91 */           if (this.interrupted) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*  98 */             this.bf.wait();
/*  99 */           } catch (InterruptedException e) {
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 104 */         event = this.bf.get();
/*     */         
/* 106 */         if (this.bf.wasFull())
/*     */         {
/* 108 */           this.bf.notify();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 113 */       synchronized (this.container.aai) {
/* 114 */         if (this.aai != null && event != null) {
/* 115 */           this.aai.appendLoopOnAppenders(event);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 122 */     this.aai.removeAllAppenders();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/Dispatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */