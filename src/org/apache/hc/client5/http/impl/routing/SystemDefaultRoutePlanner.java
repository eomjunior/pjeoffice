/*     */ package org.apache.hc.client5.http.impl.routing;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class SystemDefaultRoutePlanner
/*     */   extends DefaultRoutePlanner
/*     */ {
/*     */   private final ProxySelector proxySelector;
/*     */   
/*     */   public SystemDefaultRoutePlanner(SchemePortResolver schemePortResolver, ProxySelector proxySelector) {
/*  63 */     super(schemePortResolver);
/*  64 */     this.proxySelector = proxySelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SystemDefaultRoutePlanner(ProxySelector proxySelector) {
/*  71 */     this(null, proxySelector);
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpHost determineProxy(HttpHost target, HttpContext context) throws HttpException {
/*     */     URI targetURI;
/*     */     try {
/*  78 */       targetURI = new URI(target.toURI());
/*  79 */     } catch (URISyntaxException ex) {
/*  80 */       throw new HttpException("Cannot convert host to URI: " + target, ex);
/*     */     } 
/*  82 */     ProxySelector proxySelectorInstance = this.proxySelector;
/*  83 */     if (proxySelectorInstance == null) {
/*  84 */       proxySelectorInstance = ProxySelector.getDefault();
/*     */     }
/*  86 */     if (proxySelectorInstance == null)
/*     */     {
/*  88 */       return null;
/*     */     }
/*  90 */     List<Proxy> proxies = proxySelectorInstance.select(targetURI);
/*  91 */     Proxy p = chooseProxy(proxies);
/*  92 */     HttpHost result = null;
/*  93 */     if (p.type() == Proxy.Type.HTTP) {
/*     */       
/*  95 */       if (!(p.address() instanceof InetSocketAddress)) {
/*  96 */         throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
/*     */       }
/*  98 */       InetSocketAddress isa = (InetSocketAddress)p.address();
/*     */       
/* 100 */       result = new HttpHost(null, isa.getAddress(), isa.getHostString(), isa.getPort());
/*     */     } 
/*     */     
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   private Proxy chooseProxy(List<Proxy> proxies) {
/* 107 */     Proxy result = null;
/*     */     
/* 109 */     for (int i = 0; result == null && i < proxies.size(); i++) {
/* 110 */       Proxy p = proxies.get(i);
/* 111 */       switch (p.type()) {
/*     */         
/*     */         case DIRECT:
/*     */         case HTTP:
/* 115 */           result = p;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 124 */     if (result == null)
/*     */     {
/*     */ 
/*     */       
/* 128 */       result = Proxy.NO_PROXY;
/*     */     }
/* 130 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/routing/SystemDefaultRoutePlanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */