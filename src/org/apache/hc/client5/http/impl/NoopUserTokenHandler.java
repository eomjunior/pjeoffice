/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.client5.http.HttpRoute;
/*    */ import org.apache.hc.client5.http.UserTokenHandler;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class NoopUserTokenHandler
/*    */   implements UserTokenHandler
/*    */ {
/* 43 */   public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();
/*    */ 
/*    */   
/*    */   public Object getUserToken(HttpRoute route, HttpContext context) {
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/NoopUserTokenHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */