/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.CancelledKeyException;
/*    */ import org.apache.hc.core5.io.CloseMode;
/*    */ import org.apache.hc.core5.io.ModalCloseable;
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
/*    */ abstract class InternalChannel
/*    */   implements ModalCloseable
/*    */ {
/*    */   abstract void onIOEvent(int paramInt) throws IOException;
/*    */   
/*    */   abstract void onTimeout(Timeout paramTimeout) throws IOException;
/*    */   
/*    */   abstract void onException(Exception paramException);
/*    */   
/*    */   abstract Timeout getTimeout();
/*    */   
/*    */   abstract long getLastEventTime();
/*    */   
/*    */   final void handleIOEvent(int ops) {
/*    */     try {
/* 51 */       onIOEvent(ops);
/* 52 */     } catch (CancelledKeyException ex) {
/* 53 */       close(CloseMode.GRACEFUL);
/* 54 */     } catch (Exception ex) {
/* 55 */       onException(ex);
/* 56 */       close(CloseMode.GRACEFUL);
/*    */     } 
/*    */   }
/*    */   
/*    */   final boolean checkTimeout(long currentTimeMillis) {
/* 61 */     Timeout timeout = getTimeout();
/* 62 */     if (!timeout.isDisabled()) {
/* 63 */       long timeoutMillis = timeout.toMilliseconds();
/* 64 */       long deadlineMillis = getLastEventTime() + timeoutMillis;
/* 65 */       if (currentTimeMillis > deadlineMillis) {
/*    */         try {
/* 67 */           onTimeout(timeout);
/* 68 */         } catch (CancelledKeyException ex) {
/* 69 */           close(CloseMode.GRACEFUL);
/* 70 */         } catch (Exception ex) {
/* 71 */           onException(ex);
/* 72 */           close(CloseMode.GRACEFUL);
/*    */         } 
/* 74 */         return false;
/*    */       } 
/*    */     } 
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/InternalChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */