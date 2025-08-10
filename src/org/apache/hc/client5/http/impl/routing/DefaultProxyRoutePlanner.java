/*    */ package org.apache.hc.client5.http.impl.routing;
/*    */ 
/*    */ import org.apache.hc.client5.http.SchemePortResolver;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public class DefaultProxyRoutePlanner
/*    */   extends DefaultRoutePlanner
/*    */ {
/*    */   private final HttpHost proxy;
/*    */   
/*    */   public DefaultProxyRoutePlanner(HttpHost proxy, SchemePortResolver schemePortResolver) {
/* 50 */     super(schemePortResolver);
/* 51 */     this.proxy = (HttpHost)Args.notNull(proxy, "Proxy host");
/*    */   }
/*    */   
/*    */   public DefaultProxyRoutePlanner(HttpHost proxy) {
/* 55 */     this(proxy, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpHost determineProxy(HttpHost target, HttpContext context) throws HttpException {
/* 62 */     return this.proxy;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/routing/DefaultProxyRoutePlanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */