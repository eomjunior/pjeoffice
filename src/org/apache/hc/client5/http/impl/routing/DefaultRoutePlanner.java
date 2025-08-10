/*     */ package org.apache.hc.client5.http.impl.routing;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class DefaultRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   public DefaultRoutePlanner(SchemePortResolver schemePortResolver) {
/*  60 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpRoute determineRoute(HttpHost host, HttpContext context) throws HttpException {
/*  65 */     if (host == null) {
/*  66 */       throw new ProtocolException("Target host is not specified");
/*     */     }
/*  68 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  69 */     RequestConfig config = clientContext.getRequestConfig();
/*     */     
/*  71 */     HttpHost proxy = config.getProxy();
/*  72 */     if (proxy == null) {
/*  73 */       proxy = determineProxy(host, context);
/*     */     }
/*  75 */     HttpHost target = RoutingSupport.normalize(host, this.schemePortResolver);
/*  76 */     if (target.getPort() < 0) {
/*  77 */       throw new ProtocolException("Unroutable protocol scheme: " + target);
/*     */     }
/*  79 */     boolean secure = target.getSchemeName().equalsIgnoreCase(URIScheme.HTTPS.getId());
/*  80 */     if (proxy == null) {
/*  81 */       return new HttpRoute(target, determineLocalAddress(target, context), secure);
/*     */     }
/*  83 */     return new HttpRoute(target, determineLocalAddress(proxy, context), proxy, secure);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpHost determineProxy(HttpHost target, HttpContext context) throws HttpException {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InetAddress determineLocalAddress(HttpHost firstHop, HttpContext context) throws HttpException {
/* 105 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/routing/DefaultRoutePlanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */