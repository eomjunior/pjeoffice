/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ final class IOWorkers
/*    */ {
/*    */   static Selector newSelector(SingleCoreIOReactor[] dispatchers) {
/* 40 */     return isPowerOfTwo(dispatchers.length) ? new PowerOfTwoSelector(dispatchers) : new GenericSelector(dispatchers);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isPowerOfTwo(int val) {
/* 46 */     return ((val & -val) == val);
/*    */   } static interface Selector {
/*    */     SingleCoreIOReactor next(); }
/*    */   private static void validate(SingleCoreIOReactor dispatcher) {
/* 50 */     if (dispatcher.getStatus() == IOReactorStatus.SHUT_DOWN)
/* 51 */       throw new IOReactorShutdownException("I/O reactor has been shut down"); 
/*    */   }
/*    */   
/*    */   private static final class PowerOfTwoSelector
/*    */     implements Selector
/*    */   {
/* 57 */     private final AtomicInteger idx = new AtomicInteger(0);
/*    */     private final SingleCoreIOReactor[] dispatchers;
/*    */     
/*    */     PowerOfTwoSelector(SingleCoreIOReactor[] dispatchers) {
/* 61 */       this.dispatchers = dispatchers;
/*    */     }
/*    */ 
/*    */     
/*    */     public SingleCoreIOReactor next() {
/* 66 */       SingleCoreIOReactor dispatcher = this.dispatchers[this.idx.getAndIncrement() & this.dispatchers.length - 1];
/* 67 */       IOWorkers.validate(dispatcher);
/* 68 */       return dispatcher;
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class GenericSelector
/*    */     implements Selector {
/* 74 */     private final AtomicInteger idx = new AtomicInteger(0);
/*    */     private final SingleCoreIOReactor[] dispatchers;
/*    */     
/*    */     GenericSelector(SingleCoreIOReactor[] dispatchers) {
/* 78 */       this.dispatchers = dispatchers;
/*    */     }
/*    */ 
/*    */     
/*    */     public SingleCoreIOReactor next() {
/* 83 */       SingleCoreIOReactor dispatcher = this.dispatchers[(this.idx.getAndIncrement() & Integer.MAX_VALUE) % this.dispatchers.length];
/* 84 */       IOWorkers.validate(dispatcher);
/* 85 */       return dispatcher;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOWorkers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */