/*    */ package org.apache.hc.core5.pool;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.SocketModalCloseable;
/*    */ import org.apache.hc.core5.io.CloseMode;
/*    */ import org.apache.hc.core5.io.ModalCloseable;
/*    */ import org.apache.hc.core5.util.TimeValue;
/*    */ import org.apache.hc.core5.util.Timeout;
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
/*    */ @Internal
/*    */ public final class DefaultDisposalCallback<T extends SocketModalCloseable>
/*    */   implements DisposalCallback<T>
/*    */ {
/* 45 */   private static final Timeout DEFAULT_CLOSE_TIMEOUT = Timeout.ofSeconds(1L);
/*    */ 
/*    */   
/*    */   public void execute(SocketModalCloseable closeable, CloseMode closeMode) {
/* 49 */     Timeout socketTimeout = closeable.getSocketTimeout();
/* 50 */     if (socketTimeout == null || socketTimeout
/* 51 */       .compareTo(TimeValue.ZERO_MILLISECONDS) <= 0 || socketTimeout
/* 52 */       .compareTo((TimeValue)DEFAULT_CLOSE_TIMEOUT) > 0) {
/* 53 */       closeable.setSocketTimeout(DEFAULT_CLOSE_TIMEOUT);
/*    */     }
/* 55 */     closeable.close(closeMode);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/DefaultDisposalCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */