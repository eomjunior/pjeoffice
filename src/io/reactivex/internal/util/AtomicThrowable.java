/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class AtomicThrowable
/*    */   extends AtomicReference<Throwable>
/*    */ {
/*    */   private static final long serialVersionUID = 3949248817947090603L;
/*    */   
/*    */   public boolean addThrowable(Throwable t) {
/* 34 */     return ExceptionHelper.addThrowable(this, t);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable terminate() {
/* 43 */     return ExceptionHelper.terminate(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminated() {
/* 47 */     return (get() == ExceptionHelper.TERMINATED);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/AtomicThrowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */