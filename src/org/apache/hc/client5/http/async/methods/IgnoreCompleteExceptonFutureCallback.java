/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
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
/*    */ @Deprecated
/*    */ public class IgnoreCompleteExceptonFutureCallback<T>
/*    */   extends IgnoreCompleteExceptionFutureCallback<T>
/*    */ {
/*    */   public IgnoreCompleteExceptonFutureCallback(FutureCallback<T> callback) {
/* 39 */     super(callback);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/IgnoreCompleteExceptonFutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */