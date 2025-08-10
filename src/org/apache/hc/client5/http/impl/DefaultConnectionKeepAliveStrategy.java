/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
/*    */ import org.apache.hc.client5.http.config.RequestConfig;
/*    */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HeaderElement;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.MessageHeaders;
/*    */ import org.apache.hc.core5.http.message.MessageSupport;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.TimeValue;
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
/*    */ public class DefaultConnectionKeepAliveStrategy
/*    */   implements ConnectionKeepAliveStrategy
/*    */ {
/* 57 */   public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();
/*    */ 
/*    */   
/*    */   public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
/* 61 */     Args.notNull(response, "HTTP response");
/* 62 */     Iterator<HeaderElement> it = MessageSupport.iterate((MessageHeaders)response, "keep-alive");
/* 63 */     while (it.hasNext()) {
/* 64 */       HeaderElement he = it.next();
/* 65 */       String param = he.getName();
/* 66 */       String value = he.getValue();
/* 67 */       if (value != null && param.equalsIgnoreCase("timeout")) {
/*    */         try {
/* 69 */           return TimeValue.ofSeconds(Long.parseLong(value));
/* 70 */         } catch (NumberFormatException numberFormatException) {}
/*    */       }
/*    */     } 
/*    */     
/* 74 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 75 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/* 76 */     return requestConfig.getConnectionKeepAlive();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultConnectionKeepAliveStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */