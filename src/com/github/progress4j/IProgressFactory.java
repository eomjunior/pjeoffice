/*    */ package com.github.progress4j;
/*    */ 
/*    */ import com.github.utils4j.IInterruptable;
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
/*    */ public interface IProgressFactory
/*    */   extends Supplier<IProgressView>, IInterruptable
/*    */ {
/*    */   default boolean ifCanceller(Runnable code) {
/* 37 */     return false;
/*    */   }
/*    */   
/*    */   void cancel(Thread paramThread);
/*    */   
/*    */   void display();
/*    */   
/*    */   void undisplay();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IProgressFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */