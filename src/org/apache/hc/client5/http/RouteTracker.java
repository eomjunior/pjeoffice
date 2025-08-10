/*     */ package org.apache.hc.client5.http;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ public final class RouteTracker
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*     */   private final HttpHost targetHost;
/*     */   private final InetAddress localAddress;
/*     */   private boolean connected;
/*     */   private HttpHost[] proxyChain;
/*     */   private RouteInfo.TunnelType tunnelled;
/*     */   private RouteInfo.LayerType layered;
/*     */   private boolean secure;
/*     */   
/*     */   public RouteTracker(HttpHost target, InetAddress local) {
/*  81 */     Args.notNull(target, "Target host");
/*  82 */     this.targetHost = target;
/*  83 */     this.localAddress = local;
/*  84 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  85 */     this.layered = RouteInfo.LayerType.PLAIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  92 */     this.connected = false;
/*  93 */     this.proxyChain = null;
/*  94 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  95 */     this.layered = RouteInfo.LayerType.PLAIN;
/*  96 */     this.secure = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteTracker(HttpRoute route) {
/* 107 */     this(route.getTargetHost(), route.getLocalAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connectTarget(boolean secure) {
/* 117 */     Asserts.check(!this.connected, "Already connected");
/* 118 */     this.connected = true;
/* 119 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connectProxy(HttpHost proxy, boolean secure) {
/* 130 */     Args.notNull(proxy, "Proxy host");
/* 131 */     Asserts.check(!this.connected, "Already connected");
/* 132 */     this.connected = true;
/* 133 */     this.proxyChain = new HttpHost[] { proxy };
/* 134 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tunnelTarget(boolean secure) {
/* 144 */     Asserts.check(this.connected, "No tunnel unless connected");
/* 145 */     Asserts.notNull(this.proxyChain, "No tunnel without proxy");
/* 146 */     this.tunnelled = RouteInfo.TunnelType.TUNNELLED;
/* 147 */     this.secure = secure;
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
/*     */   public void tunnelProxy(HttpHost proxy, boolean secure) {
/* 160 */     Args.notNull(proxy, "Proxy host");
/* 161 */     Asserts.check(this.connected, "No tunnel unless connected");
/* 162 */     Asserts.notNull(this.proxyChain, "No tunnel without proxy");
/*     */     
/* 164 */     HttpHost[] proxies = new HttpHost[this.proxyChain.length + 1];
/* 165 */     System.arraycopy(this.proxyChain, 0, proxies, 0, this.proxyChain.length);
/*     */     
/* 167 */     proxies[proxies.length - 1] = proxy;
/*     */     
/* 169 */     this.proxyChain = proxies;
/* 170 */     this.secure = secure;
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
/*     */   public void layerProtocol(boolean secure) {
/* 182 */     Asserts.check(this.connected, "No layered protocol unless connected");
/* 183 */     this.layered = RouteInfo.LayerType.LAYERED;
/* 184 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getTargetHost() {
/* 189 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 194 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHopCount() {
/* 199 */     int hops = 0;
/* 200 */     if (this.connected) {
/* 201 */       if (this.proxyChain == null) {
/* 202 */         hops = 1;
/*     */       } else {
/* 204 */         hops = this.proxyChain.length + 1;
/*     */       } 
/*     */     }
/* 207 */     return hops;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getHopTarget(int hop) {
/* 212 */     Args.notNegative(hop, "Hop index");
/* 213 */     int hopcount = getHopCount();
/* 214 */     Args.check((hop < hopcount), "Hop index exceeds tracked route length");
/* 215 */     HttpHost result = null;
/* 216 */     if (hop < hopcount - 1) {
/* 217 */       result = this.proxyChain[hop];
/*     */     } else {
/* 219 */       result = this.targetHost;
/*     */     } 
/*     */     
/* 222 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHost getProxyHost() {
/* 227 */     return (this.proxyChain == null) ? null : this.proxyChain[0];
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 231 */     return this.connected;
/*     */   }
/*     */ 
/*     */   
/*     */   public RouteInfo.TunnelType getTunnelType() {
/* 236 */     return this.tunnelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTunnelled() {
/* 241 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public RouteInfo.LayerType getLayerType() {
/* 246 */     return this.layered;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLayered() {
/* 251 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 256 */     return this.secure;
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
/*     */   public HttpRoute toRoute() {
/* 268 */     return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
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
/*     */   public boolean equals(Object o) {
/* 284 */     if (o == this) {
/* 285 */       return true;
/*     */     }
/* 287 */     if (!(o instanceof RouteTracker)) {
/* 288 */       return false;
/*     */     }
/*     */     
/* 291 */     RouteTracker that = (RouteTracker)o;
/* 292 */     return (this.connected == that.connected && this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 298 */       Objects.equals(this.targetHost, that.targetHost) && 
/* 299 */       Objects.equals(this.localAddress, that.localAddress) && 
/* 300 */       Objects.equals(this.proxyChain, that.proxyChain));
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
/*     */   public int hashCode() {
/* 313 */     int hash = 17;
/* 314 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 315 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 316 */     if (this.proxyChain != null) {
/* 317 */       for (HttpHost element : this.proxyChain) {
/* 318 */         hash = LangUtils.hashCode(hash, element);
/*     */       }
/*     */     }
/* 321 */     hash = LangUtils.hashCode(hash, this.connected);
/* 322 */     hash = LangUtils.hashCode(hash, this.secure);
/* 323 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 324 */     hash = LangUtils.hashCode(hash, this.layered);
/* 325 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 335 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/*     */     
/* 337 */     cab.append("RouteTracker[");
/* 338 */     if (this.localAddress != null) {
/* 339 */       cab.append(this.localAddress);
/* 340 */       cab.append("->");
/*     */     } 
/* 342 */     cab.append('{');
/* 343 */     if (this.connected) {
/* 344 */       cab.append('c');
/*     */     }
/* 346 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
/* 347 */       cab.append('t');
/*     */     }
/* 349 */     if (this.layered == RouteInfo.LayerType.LAYERED) {
/* 350 */       cab.append('l');
/*     */     }
/* 352 */     if (this.secure) {
/* 353 */       cab.append('s');
/*     */     }
/* 355 */     cab.append("}->");
/* 356 */     if (this.proxyChain != null) {
/* 357 */       for (HttpHost element : this.proxyChain) {
/* 358 */         cab.append(element);
/* 359 */         cab.append("->");
/*     */       } 
/*     */     }
/* 362 */     cab.append(this.targetHost);
/* 363 */     cab.append(']');
/*     */     
/* 365 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 372 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/RouteTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */