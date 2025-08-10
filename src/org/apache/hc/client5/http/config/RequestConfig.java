/*     */ package org.apache.hc.client5.http.config;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ public class RequestConfig
/*     */   implements Cloneable
/*     */ {
/*  45 */   private static final Timeout DEFAULT_CONNECTION_REQUEST_TIMEOUT = Timeout.ofMinutes(3L);
/*  46 */   private static final TimeValue DEFAULT_CONN_KEEP_ALIVE = TimeValue.ofMinutes(3L);
/*     */   
/*  48 */   public static final RequestConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final boolean expectContinueEnabled;
/*     */   
/*     */   private final HttpHost proxy;
/*     */   
/*     */   private final String cookieSpec;
/*     */   
/*     */   private final boolean redirectsEnabled;
/*     */   private final boolean circularRedirectsAllowed;
/*     */   private final int maxRedirects;
/*     */   private final boolean authenticationEnabled;
/*     */   private final Collection<String> targetPreferredAuthSchemes;
/*     */   private final Collection<String> proxyPreferredAuthSchemes;
/*     */   private final Timeout connectionRequestTimeout;
/*     */   private final Timeout connectTimeout;
/*     */   private final Timeout responseTimeout;
/*     */   private final TimeValue connectionKeepAlive;
/*     */   private final boolean contentCompressionEnabled;
/*     */   private final boolean hardCancellationEnabled;
/*     */   
/*     */   protected RequestConfig() {
/*  70 */     this(false, null, null, false, false, 0, false, null, null, DEFAULT_CONNECTION_REQUEST_TIMEOUT, null, null, DEFAULT_CONN_KEEP_ALIVE, false, false);
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
/*     */   RequestConfig(boolean expectContinueEnabled, HttpHost proxy, String cookieSpec, boolean redirectsEnabled, boolean circularRedirectsAllowed, int maxRedirects, boolean authenticationEnabled, Collection<String> targetPreferredAuthSchemes, Collection<String> proxyPreferredAuthSchemes, Timeout connectionRequestTimeout, Timeout connectTimeout, Timeout responseTimeout, TimeValue connectionKeepAlive, boolean contentCompressionEnabled, boolean hardCancellationEnabled) {
/*  91 */     this.expectContinueEnabled = expectContinueEnabled;
/*  92 */     this.proxy = proxy;
/*  93 */     this.cookieSpec = cookieSpec;
/*  94 */     this.redirectsEnabled = redirectsEnabled;
/*  95 */     this.circularRedirectsAllowed = circularRedirectsAllowed;
/*  96 */     this.maxRedirects = maxRedirects;
/*  97 */     this.authenticationEnabled = authenticationEnabled;
/*  98 */     this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/*  99 */     this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/* 100 */     this.connectionRequestTimeout = connectionRequestTimeout;
/* 101 */     this.connectTimeout = connectTimeout;
/* 102 */     this.responseTimeout = responseTimeout;
/* 103 */     this.connectionKeepAlive = connectionKeepAlive;
/* 104 */     this.contentCompressionEnabled = contentCompressionEnabled;
/* 105 */     this.hardCancellationEnabled = hardCancellationEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpectContinueEnabled() {
/* 112 */     return this.expectContinueEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpHost getProxy() {
/* 123 */     return this.proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieSpec() {
/* 130 */     return this.cookieSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectsEnabled() {
/* 137 */     return this.redirectsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCircularRedirectsAllowed() {
/* 144 */     return this.circularRedirectsAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRedirects() {
/* 151 */     return this.maxRedirects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationEnabled() {
/* 158 */     return this.authenticationEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getTargetPreferredAuthSchemes() {
/* 165 */     return this.targetPreferredAuthSchemes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getProxyPreferredAuthSchemes() {
/* 172 */     return this.proxyPreferredAuthSchemes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getConnectionRequestTimeout() {
/* 179 */     return this.connectionRequestTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Timeout getConnectTimeout() {
/* 189 */     return this.connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getResponseTimeout() {
/* 196 */     return this.responseTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getConnectionKeepAlive() {
/* 203 */     return this.connectionKeepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContentCompressionEnabled() {
/* 210 */     return this.contentCompressionEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHardCancellationEnabled() {
/* 217 */     return this.hardCancellationEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestConfig clone() throws CloneNotSupportedException {
/* 222 */     return (RequestConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 227 */     StringBuilder builder = new StringBuilder();
/* 228 */     builder.append("[");
/* 229 */     builder.append("expectContinueEnabled=").append(this.expectContinueEnabled);
/* 230 */     builder.append(", proxy=").append(this.proxy);
/* 231 */     builder.append(", cookieSpec=").append(this.cookieSpec);
/* 232 */     builder.append(", redirectsEnabled=").append(this.redirectsEnabled);
/* 233 */     builder.append(", maxRedirects=").append(this.maxRedirects);
/* 234 */     builder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
/* 235 */     builder.append(", authenticationEnabled=").append(this.authenticationEnabled);
/* 236 */     builder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
/* 237 */     builder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
/* 238 */     builder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
/* 239 */     builder.append(", connectTimeout=").append(this.connectTimeout);
/* 240 */     builder.append(", responseTimeout=").append(this.responseTimeout);
/* 241 */     builder.append(", connectionKeepAlive=").append(this.connectionKeepAlive);
/* 242 */     builder.append(", contentCompressionEnabled=").append(this.contentCompressionEnabled);
/* 243 */     builder.append(", hardCancellationEnabled=").append(this.hardCancellationEnabled);
/* 244 */     builder.append("]");
/* 245 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 249 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(RequestConfig config) {
/* 253 */     return (new Builder())
/* 254 */       .setExpectContinueEnabled(config.isExpectContinueEnabled())
/* 255 */       .setProxy(config.getProxy())
/* 256 */       .setCookieSpec(config.getCookieSpec())
/* 257 */       .setRedirectsEnabled(config.isRedirectsEnabled())
/* 258 */       .setCircularRedirectsAllowed(config.isCircularRedirectsAllowed())
/* 259 */       .setMaxRedirects(config.getMaxRedirects())
/* 260 */       .setAuthenticationEnabled(config.isAuthenticationEnabled())
/* 261 */       .setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes())
/* 262 */       .setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes())
/* 263 */       .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
/* 264 */       .setConnectTimeout(config.getConnectTimeout())
/* 265 */       .setResponseTimeout(config.getResponseTimeout())
/* 266 */       .setConnectionKeepAlive(config.getConnectionKeepAlive())
/* 267 */       .setContentCompressionEnabled(config.isContentCompressionEnabled())
/* 268 */       .setHardCancellationEnabled(config.isHardCancellationEnabled());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private boolean expectContinueEnabled;
/*     */ 
/*     */ 
/*     */     
/*     */     private HttpHost proxy;
/*     */ 
/*     */     
/*     */     private String cookieSpec;
/*     */ 
/*     */     
/*     */     private boolean redirectsEnabled = true;
/*     */ 
/*     */     
/*     */     private boolean circularRedirectsAllowed;
/*     */ 
/*     */     
/* 292 */     private int maxRedirects = 50;
/*     */     private boolean authenticationEnabled = true;
/* 294 */     private Timeout connectionRequestTimeout = RequestConfig.DEFAULT_CONNECTION_REQUEST_TIMEOUT;
/*     */ 
/*     */ 
/*     */     
/*     */     private Collection<String> targetPreferredAuthSchemes;
/*     */ 
/*     */ 
/*     */     
/*     */     private Collection<String> proxyPreferredAuthSchemes;
/*     */ 
/*     */     
/*     */     private Timeout connectTimeout;
/*     */ 
/*     */     
/*     */     private Timeout responseTimeout;
/*     */ 
/*     */     
/*     */     private TimeValue connectionKeepAlive;
/*     */ 
/*     */     
/*     */     private boolean contentCompressionEnabled = true;
/*     */ 
/*     */     
/*     */     private boolean hardCancellationEnabled = true;
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setExpectContinueEnabled(boolean expectContinueEnabled) {
/* 322 */       this.expectContinueEnabled = expectContinueEnabled;
/* 323 */       return this;
/*     */     }
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
/*     */     @Deprecated
/*     */     public Builder setProxy(HttpHost proxy) {
/* 337 */       this.proxy = proxy;
/* 338 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setCookieSpec(String cookieSpec) {
/* 349 */       this.cookieSpec = cookieSpec;
/* 350 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setRedirectsEnabled(boolean redirectsEnabled) {
/* 360 */       this.redirectsEnabled = redirectsEnabled;
/* 361 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
/* 373 */       this.circularRedirectsAllowed = circularRedirectsAllowed;
/* 374 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setMaxRedirects(int maxRedirects) {
/* 385 */       this.maxRedirects = maxRedirects;
/* 386 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setAuthenticationEnabled(boolean authenticationEnabled) {
/* 396 */       this.authenticationEnabled = authenticationEnabled;
/* 397 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
/* 408 */       this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/* 409 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
/* 420 */       this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/* 421 */       return this;
/*     */     }
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
/*     */     public Builder setConnectionRequestTimeout(Timeout connectionRequestTimeout) {
/* 434 */       this.connectionRequestTimeout = connectionRequestTimeout;
/* 435 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setConnectionRequestTimeout(long connectionRequestTimeout, TimeUnit timeUnit) {
/* 442 */       this.connectionRequestTimeout = Timeout.of(connectionRequestTimeout, timeUnit);
/* 443 */       return this;
/*     */     }
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
/*     */     @Deprecated
/*     */     public Builder setConnectTimeout(Timeout connectTimeout) {
/* 461 */       this.connectTimeout = connectTimeout;
/* 462 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setConnectTimeout(long connectTimeout, TimeUnit timeUnit) {
/* 472 */       this.connectTimeout = Timeout.of(connectTimeout, timeUnit);
/* 473 */       return this;
/*     */     }
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
/*     */     public Builder setResponseTimeout(Timeout responseTimeout) {
/* 493 */       this.responseTimeout = responseTimeout;
/* 494 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setResponseTimeout(long responseTimeout, TimeUnit timeUnit) {
/* 501 */       this.responseTimeout = Timeout.of(responseTimeout, timeUnit);
/* 502 */       return this;
/*     */     }
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
/*     */     public Builder setConnectionKeepAlive(TimeValue connectionKeepAlive) {
/* 519 */       this.connectionKeepAlive = connectionKeepAlive;
/* 520 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setDefaultKeepAlive(long defaultKeepAlive, TimeUnit timeUnit) {
/* 527 */       this.connectionKeepAlive = TimeValue.of(defaultKeepAlive, timeUnit);
/* 528 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setContentCompressionEnabled(boolean contentCompressionEnabled) {
/* 540 */       this.contentCompressionEnabled = contentCompressionEnabled;
/* 541 */       return this;
/*     */     }
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
/*     */     public Builder setHardCancellationEnabled(boolean hardCancellationEnabled) {
/* 572 */       this.hardCancellationEnabled = hardCancellationEnabled;
/* 573 */       return this;
/*     */     }
/*     */     
/*     */     public RequestConfig build() {
/* 577 */       return new RequestConfig(this.expectContinueEnabled, this.proxy, this.cookieSpec, this.redirectsEnabled, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, (this.connectionRequestTimeout != null) ? this.connectionRequestTimeout : RequestConfig
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 587 */           .DEFAULT_CONNECTION_REQUEST_TIMEOUT, this.connectTimeout, this.responseTimeout, (this.connectionKeepAlive != null) ? this.connectionKeepAlive : RequestConfig
/*     */ 
/*     */           
/* 590 */           .DEFAULT_CONN_KEEP_ALIVE, this.contentCompressionEnabled, this.hardCancellationEnabled);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/config/RequestConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */