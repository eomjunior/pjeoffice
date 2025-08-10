/*     */ package org.apache.hc.client5.http;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class HttpRoute
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*     */   private final HttpHost targetHost;
/*     */   private final InetAddress localAddress;
/*     */   private final List<HttpHost> proxyChain;
/*     */   private final RouteInfo.TunnelType tunnelled;
/*     */   private final RouteInfo.LayerType layered;
/*     */   private final boolean secure;
/*     */   
/*     */   private HttpRoute(HttpHost targetHost, InetAddress local, List<HttpHost> proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/*  75 */     Args.notNull(targetHost, "Target host");
/*  76 */     Args.notNegative(targetHost.getPort(), "Target port");
/*  77 */     this.targetHost = targetHost;
/*  78 */     this.localAddress = local;
/*  79 */     if (proxies != null && !proxies.isEmpty()) {
/*  80 */       this.proxyChain = new ArrayList<>(proxies);
/*     */     } else {
/*  82 */       this.proxyChain = null;
/*     */     } 
/*  84 */     if (tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/*  85 */       Args.check((this.proxyChain != null), "Proxy required if tunnelled");
/*     */     }
/*  87 */     this.secure = secure;
/*  88 */     this.tunnelled = (tunnelled != null) ? tunnelled : RouteInfo.TunnelType.PLAIN;
/*  89 */     this.layered = (layered != null) ? layered : RouteInfo.LayerType.PLAIN;
/*     */   }
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 107 */     this(target, local, (proxies != null) ? Arrays.<HttpHost>asList(proxies) : null, secure, tunnelled, layered);
/*     */   }
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 130 */     this(target, local, (proxy != null) ? Collections.<HttpHost>singletonList(proxy) : null, secure, tunnelled, layered);
/*     */   }
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, boolean secure) {
/* 145 */     this(target, local, Collections.emptyList(), secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute(HttpHost target) {
/* 154 */     this(target, (InetAddress)null, Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
/*     */   }
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure) {
/* 172 */     this(target, local, Collections.singletonList(Args.notNull(proxy, "Proxy host")), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
/*     */   }
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
/*     */   public HttpRoute(HttpHost target, HttpHost proxy) {
/* 186 */     this(target, null, proxy, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getTargetHost() {
/* 191 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 196 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   public InetSocketAddress getLocalSocketAddress() {
/* 200 */     return (this.localAddress != null) ? new InetSocketAddress(this.localAddress, 0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHopCount() {
/* 205 */     return (this.proxyChain != null) ? (this.proxyChain.size() + 1) : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getHopTarget(int hop) {
/* 210 */     Args.notNegative(hop, "Hop index");
/* 211 */     int hopcount = getHopCount();
/* 212 */     Args.check((hop < hopcount), "Hop index exceeds tracked route length");
/* 213 */     if (hop < hopcount - 1) {
/* 214 */       return this.proxyChain.get(hop);
/*     */     }
/* 216 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getProxyHost() {
/* 221 */     return (this.proxyChain != null && !this.proxyChain.isEmpty()) ? this.proxyChain.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public RouteInfo.TunnelType getTunnelType() {
/* 226 */     return this.tunnelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTunnelled() {
/* 231 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public RouteInfo.LayerType getLayerType() {
/* 236 */     return this.layered;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLayered() {
/* 241 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 246 */     return this.secure;
/*     */   }
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
/*     */   public boolean equals(Object obj) {
/* 259 */     if (this == obj) {
/* 260 */       return true;
/*     */     }
/* 262 */     if (obj instanceof HttpRoute) {
/* 263 */       HttpRoute that = (HttpRoute)obj;
/* 264 */       return (this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 269 */         Objects.equals(this.targetHost, that.targetHost) && 
/* 270 */         Objects.equals(this.localAddress, that.localAddress) && 
/* 271 */         Objects.equals(this.proxyChain, that.proxyChain));
/*     */     } 
/* 273 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 284 */     int hash = 17;
/* 285 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 286 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 287 */     if (this.proxyChain != null) {
/* 288 */       for (HttpHost element : this.proxyChain) {
/* 289 */         hash = LangUtils.hashCode(hash, element);
/*     */       }
/*     */     }
/* 292 */     hash = LangUtils.hashCode(hash, this.secure);
/* 293 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 294 */     hash = LangUtils.hashCode(hash, this.layered);
/* 295 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 305 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/* 306 */     if (this.localAddress != null) {
/* 307 */       cab.append(this.localAddress);
/* 308 */       cab.append("->");
/*     */     } 
/* 310 */     cab.append('{');
/* 311 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/* 312 */       cab.append('t');
/*     */     }
/* 314 */     if (this.layered == RouteInfo.LayerType.LAYERED) {
/* 315 */       cab.append('l');
/*     */     }
/* 317 */     if (this.secure) {
/* 318 */       cab.append('s');
/*     */     }
/* 320 */     cab.append("}->");
/* 321 */     if (this.proxyChain != null) {
/* 322 */       for (HttpHost aProxyChain : this.proxyChain) {
/* 323 */         cab.append(aProxyChain);
/* 324 */         cab.append("->");
/*     */       } 
/*     */     }
/* 327 */     cab.append(this.targetHost);
/* 328 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 334 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/HttpRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */