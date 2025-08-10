/*    */ package io.reactivex.exceptions;
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
/*    */ public final class UndeliverableException
/*    */   extends IllegalStateException
/*    */ {
/*    */   private static final long serialVersionUID = 1644750035281290266L;
/*    */   
/*    */   public UndeliverableException(Throwable cause) {
/* 31 */     super("The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with. Further reading: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling | " + cause, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/exceptions/UndeliverableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */