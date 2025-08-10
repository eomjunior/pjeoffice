/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ public class ClientProtocolException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -5596590843227115865L;
/*    */   
/*    */   public ClientProtocolException() {}
/*    */   
/*    */   public ClientProtocolException(String s) {
/* 45 */     super(s);
/*    */   }
/*    */   
/*    */   public ClientProtocolException(Throwable cause) {
/* 49 */     initCause(cause);
/*    */   }
/*    */   
/*    */   public ClientProtocolException(String message, Throwable cause) {
/* 53 */     super(message);
/* 54 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ClientProtocolException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */