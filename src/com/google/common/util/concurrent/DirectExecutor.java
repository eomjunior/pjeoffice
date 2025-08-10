/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.concurrent.Executor;
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
/*    */ @GwtCompatible
/*    */ enum DirectExecutor
/*    */   implements Executor
/*    */ {
/* 27 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public void execute(Runnable command) {
/* 31 */     command.run();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 36 */     return "MoreExecutors.directExecutor()";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/DirectExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */