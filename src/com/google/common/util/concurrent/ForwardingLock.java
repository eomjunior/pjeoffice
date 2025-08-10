/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.Lock;
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
/*    */ abstract class ForwardingLock
/*    */   implements Lock
/*    */ {
/*    */   abstract Lock delegate();
/*    */   
/*    */   public void lock() {
/* 30 */     delegate().lock();
/*    */   }
/*    */ 
/*    */   
/*    */   public void lockInterruptibly() throws InterruptedException {
/* 35 */     delegate().lockInterruptibly();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryLock() {
/* 40 */     return delegate().tryLock();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
/* 45 */     return delegate().tryLock(time, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void unlock() {
/* 50 */     delegate().unlock();
/*    */   }
/*    */ 
/*    */   
/*    */   public Condition newCondition() {
/* 55 */     return delegate().newCondition();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */