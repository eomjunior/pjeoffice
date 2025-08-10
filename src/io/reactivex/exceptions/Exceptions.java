/*    */ package io.reactivex.exceptions;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.internal.util.ExceptionHelper;
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
/*    */ public final class Exceptions
/*    */ {
/*    */   private Exceptions() {
/* 27 */     throw new IllegalStateException("No instances!");
/*    */   }
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
/*    */   @NonNull
/*    */   public static RuntimeException propagate(@NonNull Throwable t) {
/* 46 */     throw ExceptionHelper.wrapOrThrow(t);
/*    */   }
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
/*    */   public static void throwIfFatal(@NonNull Throwable t) {
/* 68 */     if (t instanceof VirtualMachineError)
/* 69 */       throw (VirtualMachineError)t; 
/* 70 */     if (t instanceof ThreadDeath)
/* 71 */       throw (ThreadDeath)t; 
/* 72 */     if (t instanceof LinkageError)
/* 73 */       throw (LinkageError)t; 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/exceptions/Exceptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */