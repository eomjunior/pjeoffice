/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.net.Host;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ public final class HttpHost
/*     */   implements NamedEndpoint, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7529410654042457626L;
/*  60 */   public static final URIScheme DEFAULT_SCHEME = URIScheme.HTTP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String schemeName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Host host;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InetAddress address;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost(String scheme, InetAddress address, String hostname, int port) {
/*  84 */     Args.containsNoBlanks(hostname, "Host name");
/*  85 */     this.host = new Host(hostname, port);
/*  86 */     this.schemeName = (scheme != null) ? TextUtils.toLowerCase(scheme) : DEFAULT_SCHEME.id;
/*  87 */     this.address = address;
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
/*     */   public HttpHost(String scheme, String hostname, int port) {
/* 104 */     this(scheme, null, hostname, port);
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
/*     */   public HttpHost(String hostname, int port) {
/* 119 */     this((String)null, hostname, port);
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
/*     */   public HttpHost(String scheme, String hostname) {
/* 134 */     this(scheme, hostname, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpHost create(String s) throws URISyntaxException {
/* 143 */     Args.notEmpty(s, "HTTP Host");
/* 144 */     String text = s;
/* 145 */     String scheme = null;
/* 146 */     int schemeIdx = text.indexOf("://");
/* 147 */     if (schemeIdx > 0) {
/* 148 */       scheme = text.substring(0, schemeIdx);
/* 149 */       if (TextUtils.containsBlanks(scheme)) {
/* 150 */         throw new URISyntaxException(s, "scheme contains blanks");
/*     */       }
/* 152 */       text = text.substring(schemeIdx + 3);
/*     */     } 
/* 154 */     Host host = Host.create(text);
/* 155 */     return new HttpHost(scheme, (NamedEndpoint)host);
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
/*     */   public static HttpHost create(URI uri) {
/* 167 */     String scheme = uri.getScheme();
/* 168 */     return new HttpHost((scheme != null) ? scheme : URIScheme.HTTP.getId(), uri.getHost(), uri.getPort());
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
/*     */   public HttpHost(String hostname) {
/* 181 */     this((String)null, hostname, -1);
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
/*     */   public HttpHost(String scheme, InetAddress address, int port) {
/* 200 */     this(scheme, (InetAddress)Args.notNull(address, "Inet address"), address.getHostName(), port);
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
/*     */   public HttpHost(InetAddress address, int port) {
/* 218 */     this((String)null, address, port);
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
/*     */   public HttpHost(InetAddress address) {
/* 234 */     this((String)null, address, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost(String scheme, NamedEndpoint namedEndpoint) {
/* 245 */     this(scheme, ((NamedEndpoint)Args.notNull(namedEndpoint, "Named endpoint")).getHostName(), namedEndpoint.getPort());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpHost(URIAuthority authority) {
/* 255 */     this((String)null, (NamedEndpoint)authority);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHostName() {
/* 265 */     return this.host.getHostName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 275 */     return this.host.getPort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/* 284 */     return this.schemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getAddress() {
/* 295 */     return this.address;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toURI() {
/* 304 */     StringBuilder buffer = new StringBuilder();
/* 305 */     buffer.append(this.schemeName);
/* 306 */     buffer.append("://");
/* 307 */     buffer.append(this.host.toString());
/* 308 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toHostString() {
/* 318 */     return this.host.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 324 */     return toURI();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 330 */     if (this == obj) {
/* 331 */       return true;
/*     */     }
/* 333 */     if (obj instanceof HttpHost) {
/* 334 */       HttpHost that = (HttpHost)obj;
/* 335 */       return (this.schemeName.equals(that.schemeName) && this.host
/* 336 */         .equals(that.host) && 
/* 337 */         Objects.equals(this.address, that.address));
/*     */     } 
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 347 */     int hash = 17;
/* 348 */     hash = LangUtils.hashCode(hash, this.schemeName);
/* 349 */     hash = LangUtils.hashCode(hash, this.host);
/* 350 */     hash = LangUtils.hashCode(hash, this.address);
/* 351 */     return hash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */