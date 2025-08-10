/*    */ package org.apache.hc.client5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.RouteInfo;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.Method;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class RequestClientConnControl
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 57 */   private static final Logger LOG = LoggerFactory.getLogger(RequestClientConnControl.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 66 */     Args.notNull(request, "HTTP request");
/*    */     
/* 68 */     String method = request.getMethod();
/* 69 */     if (Method.CONNECT.isSame(method)) {
/*    */       return;
/*    */     }
/*    */     
/* 73 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 74 */     String exchangeId = clientContext.getExchangeId();
/*    */ 
/*    */     
/* 77 */     RouteInfo route = clientContext.getHttpRoute();
/* 78 */     if (route == null) {
/* 79 */       if (LOG.isDebugEnabled()) {
/* 80 */         LOG.debug("{} Connection route not set in the context", exchangeId);
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/* 85 */     if ((route.getHopCount() == 1 || route.isTunnelled()) && 
/* 86 */       !request.containsHeader("Connection"))
/* 87 */       request.addHeader("Connection", "keep-alive"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/RequestClientConnControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */