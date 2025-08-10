/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.io.InterruptedIOException;
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
/*    */ public class RequestFailedException
/*    */   extends InterruptedIOException
/*    */ {
/*    */   private static final long serialVersionUID = 4973849966012490112L;
/*    */   
/*    */   public RequestFailedException(String message) {
/* 42 */     super(message);
/*    */   }
/*    */   
/*    */   public RequestFailedException(String message, Throwable cause) {
/* 46 */     super(message);
/* 47 */     if (cause != null)
/* 48 */       initCause(cause); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/RequestFailedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */