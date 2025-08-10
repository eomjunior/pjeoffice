/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ final class OverflowAvoidingLockSupport
/*    */ {
/*    */   static final long MAX_NANOSECONDS_THRESHOLD = 2147483647999999999L;
/*    */   
/*    */   static void parkNanos(@CheckForNull Object blocker, long nanos) {
/* 38 */     LockSupport.parkNanos(blocker, Math.min(nanos, 2147483647999999999L));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/OverflowAvoidingLockSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */