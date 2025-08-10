/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.Method;
/*    */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
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
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class DefaultClientConnectionReuseStrategy
/*    */   extends DefaultConnectionReuseStrategy
/*    */ {
/* 47 */   public static final DefaultClientConnectionReuseStrategy INSTANCE = new DefaultClientConnectionReuseStrategy();
/*    */ 
/*    */   
/*    */   public boolean keepAlive(HttpRequest request, HttpResponse response, HttpContext context) {
/* 51 */     if (Method.CONNECT.isSame(request.getMethod()) && response.getCode() == 200) {
/* 52 */       return true;
/*    */     }
/* 54 */     return super.keepAlive(request, response, context);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultClientConnectionReuseStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */