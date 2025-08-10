/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.Condition;
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
/*    */ abstract class ForwardingCondition
/*    */   implements Condition
/*    */ {
/*    */   abstract Condition delegate();
/*    */   
/*    */   public void await() throws InterruptedException {
/* 30 */     delegate().await();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean await(long time, TimeUnit unit) throws InterruptedException {
/* 35 */     return delegate().await(time, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void awaitUninterruptibly() {
/* 40 */     delegate().awaitUninterruptibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public long awaitNanos(long nanosTimeout) throws InterruptedException {
/* 45 */     return delegate().awaitNanos(nanosTimeout);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean awaitUntil(Date deadline) throws InterruptedException {
/* 50 */     return delegate().awaitUntil(deadline);
/*    */   }
/*    */ 
/*    */   
/*    */   public void signal() {
/* 55 */     delegate().signal();
/*    */   }
/*    */ 
/*    */   
/*    */   public void signalAll() {
/* 60 */     delegate().signalAll();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */