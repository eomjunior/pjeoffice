/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicHttpRequest
/*     */   extends HeaderGroup
/*     */   implements HttpRequest
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String method;
/*     */   private String path;
/*     */   private String scheme;
/*     */   private URIAuthority authority;
/*     */   private ProtocolVersion version;
/*     */   private URI requestUri;
/*     */   private boolean absoluteRequestUri;
/*     */   
/*     */   public BasicHttpRequest(String method, String scheme, URIAuthority authority, String path) {
/*  71 */     this.method = (String)Args.notNull(method, "Method name");
/*  72 */     this.scheme = scheme;
/*  73 */     this.authority = authority;
/*  74 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpRequest(String method, String path) {
/*  85 */     this.method = method;
/*  86 */     if (path != null) {
/*     */       try {
/*  88 */         setUri(new URI(path));
/*  89 */       } catch (URISyntaxException ex) {
/*  90 */         this.path = path;
/*     */       } 
/*     */     }
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
/*     */   public BasicHttpRequest(String method, HttpHost host, String path) {
/* 106 */     this.method = (String)Args.notNull(method, "Method name");
/* 107 */     this.scheme = (host != null) ? host.getSchemeName() : null;
/* 108 */     this.authority = (host != null) ? new URIAuthority((NamedEndpoint)host) : null;
/* 109 */     this.path = path;
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
/*     */   public BasicHttpRequest(String method, URI requestUri) {
/* 122 */     this.method = (String)Args.notNull(method, "Method name");
/* 123 */     setUri((URI)Args.notNull(requestUri, "Request URI"));
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
/*     */   public BasicHttpRequest(Method method, String path) {
/* 136 */     this.method = ((Method)Args.notNull(method, "Method")).name();
/* 137 */     if (path != null) {
/*     */       try {
/* 139 */         setUri(new URI(path));
/* 140 */       } catch (URISyntaxException ex) {
/* 141 */         this.path = path;
/*     */       } 
/*     */     }
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
/*     */   public BasicHttpRequest(Method method, HttpHost host, String path) {
/* 157 */     this.method = ((Method)Args.notNull(method, "Method")).name();
/* 158 */     this.scheme = (host != null) ? host.getSchemeName() : null;
/* 159 */     this.authority = (host != null) ? new URIAuthority((NamedEndpoint)host) : null;
/* 160 */     this.path = path;
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
/*     */   public BasicHttpRequest(Method method, URI requestUri) {
/* 173 */     this.method = ((Method)Args.notNull(method, "Method")).name();
/* 174 */     setUri((URI)Args.notNull(requestUri, "Request URI"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, Object value) {
/* 179 */     Args.notNull(name, "Header name");
/* 180 */     addHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, Object value) {
/* 185 */     Args.notNull(name, "Header name");
/* 186 */     setHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersion(ProtocolVersion version) {
/* 191 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getVersion() {
/* 196 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethod() {
/* 201 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 206 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPath(String path) {
/* 211 */     if (path != null) {
/* 212 */       Args.check(!path.startsWith("//"), "URI path begins with multiple slashes");
/*     */     }
/* 214 */     this.path = path;
/* 215 */     this.requestUri = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScheme() {
/* 220 */     return this.scheme;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScheme(String scheme) {
/* 225 */     this.scheme = scheme;
/* 226 */     this.requestUri = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URIAuthority getAuthority() {
/* 231 */     return this.authority;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAuthority(URIAuthority authority) {
/* 236 */     this.authority = authority;
/* 237 */     this.requestUri = null;
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
/*     */   public void setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 249 */     this.absoluteRequestUri = absoluteRequestUri;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequestUri() {
/* 254 */     if (this.absoluteRequestUri) {
/* 255 */       StringBuilder buf = new StringBuilder();
/* 256 */       assembleRequestUri(buf);
/* 257 */       return buf.toString();
/*     */     } 
/* 259 */     return getPath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUri(URI requestUri) {
/* 265 */     this.scheme = requestUri.getScheme();
/* 266 */     if (requestUri.getHost() != null) {
/* 267 */       this
/* 268 */         .authority = new URIAuthority(requestUri.getRawUserInfo(), requestUri.getHost(), requestUri.getPort());
/* 269 */     } else if (requestUri.getRawAuthority() != null) {
/*     */       try {
/* 271 */         this.authority = URIAuthority.create(requestUri.getRawAuthority());
/* 272 */       } catch (URISyntaxException ignore) {
/* 273 */         this.authority = null;
/*     */       } 
/*     */     } else {
/* 276 */       this.authority = null;
/*     */     } 
/* 278 */     StringBuilder buf = new StringBuilder();
/* 279 */     String rawPath = requestUri.getRawPath();
/* 280 */     if (!TextUtils.isBlank(rawPath)) {
/* 281 */       Args.check(!rawPath.startsWith("//"), "URI path begins with multiple slashes");
/* 282 */       buf.append(rawPath);
/*     */     } else {
/* 284 */       buf.append("/");
/*     */     } 
/* 286 */     String query = requestUri.getRawQuery();
/* 287 */     if (query != null) {
/* 288 */       buf.append('?').append(query);
/*     */     }
/* 290 */     this.path = buf.toString();
/*     */   }
/*     */   
/*     */   private void assembleRequestUri(StringBuilder buf) {
/* 294 */     if (this.authority != null) {
/* 295 */       buf.append((this.scheme != null) ? this.scheme : URIScheme.HTTP.id).append("://");
/* 296 */       buf.append(this.authority.getHostName());
/* 297 */       if (this.authority.getPort() >= 0) {
/* 298 */         buf.append(":").append(this.authority.getPort());
/*     */       }
/*     */     } 
/* 301 */     if (this.path == null) {
/* 302 */       buf.append("/");
/*     */     } else {
/* 304 */       if (buf.length() > 0 && !this.path.startsWith("/")) {
/* 305 */         buf.append("/");
/*     */       }
/* 307 */       buf.append(this.path);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getUri() throws URISyntaxException {
/* 313 */     if (this.requestUri == null) {
/* 314 */       StringBuilder buf = new StringBuilder();
/* 315 */       assembleRequestUri(buf);
/* 316 */       this.requestUri = new URI(buf.toString());
/*     */     } 
/* 318 */     return this.requestUri;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 323 */     StringBuilder buf = new StringBuilder();
/* 324 */     buf.append(this.method).append(" ");
/* 325 */     assembleRequestUri(buf);
/* 326 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */