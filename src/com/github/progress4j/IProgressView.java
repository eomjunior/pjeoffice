/*    */ package com.github.progress4j;
/*    */ 
/*    */ import com.github.utils4j.ICanceller;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Threads;
/*    */ import java.util.function.Supplier;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IProgressView
/*    */   extends IProgress, ICanceller
/*    */ {
/*    */   default void displayAsync(long delay, Supplier<Boolean> condition) {
/* 50 */     Args.requireNonNull(condition, "condition is null");
/* 51 */     Threads.startDaemon("progress display async", () -> { if (((Boolean)condition.get()).booleanValue()) display();  }delay);
/*    */   }
/*    */   
/*    */   void display();
/*    */   
/*    */   void undisplay();
/*    */   
/*    */   void cancel();
/*    */   
/*    */   boolean isFrom(Thread paramThread);
/*    */   
/*    */   IProgressView reset();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IProgressView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */