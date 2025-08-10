/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class IgnoreCompleteExceptionFutureCallback<T>
/*    */   implements FutureCallback<T>
/*    */ {
/*    */   private final FutureCallback<T> callback;
/* 40 */   private static final Logger LOG = LoggerFactory.getLogger(IgnoreCompleteExceptionFutureCallback.class);
/*    */ 
/*    */   
/*    */   public IgnoreCompleteExceptionFutureCallback(FutureCallback<T> callback) {
/* 44 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void completed(T result) {
/* 49 */     if (this.callback != null) {
/*    */       try {
/* 51 */         this.callback.completed(result);
/* 52 */       } catch (Exception ex) {
/* 53 */         LOG.error(ex.getMessage(), ex);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void failed(Exception ex) {
/* 60 */     if (this.callback != null) {
/* 61 */       this.callback.failed(ex);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelled() {
/* 67 */     if (this.callback != null)
/* 68 */       this.callback.cancelled(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/IgnoreCompleteExceptionFutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */