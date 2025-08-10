/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Threads;
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
/*    */ public class SwitchRepositoryException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -384838276379546577L;
/*    */   private long timeout;
/*    */   private final boolean waiting;
/*    */   private final Repository repository;
/*    */   private volatile boolean available = false;
/*    */   
/*    */   public SwitchRepositoryException(boolean waiting, Repository repository) {
/* 45 */     this(waiting, repository, Signer4jContext.timeout());
/*    */   }
/*    */   
/*    */   private SwitchRepositoryException(boolean waiting, Repository repository, long timeout) {
/* 49 */     this.waiting = waiting;
/* 50 */     this.repository = repository;
/* 51 */     this.timeout = timeout + 1000L;
/*    */   }
/*    */   
/*    */   public final void setAvailable() {
/* 55 */     this.available = true;
/*    */   }
/*    */   
/*    */   public final Repository getRepository() {
/* 59 */     return this.repository;
/*    */   }
/*    */   
/*    */   private boolean isAvailable(long from) {
/* 63 */     return (this.available || System.currentTimeMillis() - from >= this.timeout);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void waitFor() {
/* 70 */     if (this.waiting) {
/* 71 */       long now = System.currentTimeMillis();
/* 72 */       while (!isAvailable(now))
/* 73 */         Threads.sleep(50L); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SwitchRepositoryException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */