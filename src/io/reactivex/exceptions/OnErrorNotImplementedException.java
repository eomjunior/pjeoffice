/*    */ package io.reactivex.exceptions;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
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
/*    */ public final class OnErrorNotImplementedException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -6298857009889503852L;
/*    */   
/*    */   public OnErrorNotImplementedException(String message, @NonNull Throwable e) {
/* 39 */     super(message, (e != null) ? e : new NullPointerException());
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
/*    */   public OnErrorNotImplementedException(@NonNull Throwable e) {
/* 51 */     this("The exception was not handled due to missing onError handler in the subscribe() method call. Further reading: https://github.com/ReactiveX/RxJava/wiki/Error-Handling | " + e, e);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/exceptions/OnErrorNotImplementedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */