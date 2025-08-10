/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.net.ConnectException;
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
/*    */ public class HttpHostConnectException
/*    */   extends ConnectException
/*    */ {
/*    */   private static final long serialVersionUID = -3194482710275220224L;
/*    */   private final NamedEndpoint namedEndpoint;
/*    */   
/*    */   public HttpHostConnectException(String message) {
/* 48 */     super(message);
/* 49 */     this.namedEndpoint = null;
/*    */   }
/*    */   
/*    */   public HttpHostConnectException(String message, NamedEndpoint namedEndpoint) {
/* 53 */     super(message);
/* 54 */     this.namedEndpoint = namedEndpoint;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NamedEndpoint getHost() {
/* 61 */     return this.namedEndpoint;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/HttpHostConnectException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */