/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.net.SocketTimeoutException;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectTimeoutException
/*    */   extends SocketTimeoutException
/*    */ {
/*    */   private static final long serialVersionUID = -4816682903149535989L;
/*    */   private final NamedEndpoint namedEndpoint;
/*    */   
/*    */   public ConnectTimeoutException(String message) {
/* 49 */     super(message);
/* 50 */     this.namedEndpoint = null;
/*    */   }
/*    */   
/*    */   public ConnectTimeoutException(String message, NamedEndpoint namedEndpoint) {
/* 54 */     super(message);
/* 55 */     this.namedEndpoint = namedEndpoint;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NamedEndpoint getHost() {
/* 62 */     return this.namedEndpoint;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ConnectTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */