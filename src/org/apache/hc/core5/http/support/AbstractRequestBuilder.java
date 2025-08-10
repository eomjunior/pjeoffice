/*     */ package org.apache.hc.core5.http.support;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
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
/*     */ public abstract class AbstractRequestBuilder<T>
/*     */   extends AbstractMessageBuilder<T>
/*     */ {
/*     */   private final String method;
/*     */   private String scheme;
/*     */   private URIAuthority authority;
/*     */   private String path;
/*     */   private Charset charset;
/*     */   private List<NameValuePair> parameters;
/*     */   private boolean absoluteRequestUri;
/*     */   
/*     */   protected AbstractRequestBuilder(String method) {
/*  66 */     this.method = method;
/*     */   }
/*     */   
/*     */   protected AbstractRequestBuilder(Method method) {
/*  70 */     this(method.name());
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractRequestBuilder(String method, URI uri) {
/*  75 */     this.method = method;
/*  76 */     setUri(uri);
/*     */   }
/*     */   
/*     */   protected AbstractRequestBuilder(Method method, URI uri) {
/*  80 */     this(method.name(), uri);
/*     */   }
/*     */   
/*     */   protected AbstractRequestBuilder(Method method, String uri) {
/*  84 */     this(method.name(), (uri != null) ? URI.create(uri) : null);
/*     */   }
/*     */   
/*     */   protected AbstractRequestBuilder(String method, String uri) {
/*  88 */     this(method, (uri != null) ? URI.create(uri) : null);
/*     */   }
/*     */   
/*     */   protected void digest(HttpRequest request) {
/*  92 */     if (request == null) {
/*     */       return;
/*     */     }
/*  95 */     setScheme(request.getScheme());
/*  96 */     setAuthority(request.getAuthority());
/*  97 */     setPath(request.getPath());
/*  98 */     this.parameters = null;
/*  99 */     digest((HttpMessage)request);
/*     */   }
/*     */   
/*     */   public String getMethod() {
/* 103 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> setVersion(ProtocolVersion version) {
/* 108 */     super.setVersion(version);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public String getScheme() {
/* 113 */     return this.scheme;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setScheme(String scheme) {
/* 117 */     this.scheme = scheme;
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public URIAuthority getAuthority() {
/* 122 */     return this.authority;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setAuthority(URIAuthority authority) {
/* 126 */     this.authority = authority;
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setHttpHost(HttpHost httpHost) {
/* 131 */     if (httpHost == null) {
/* 132 */       return this;
/*     */     }
/* 134 */     this.authority = new URIAuthority((NamedEndpoint)httpHost);
/* 135 */     this.scheme = httpHost.getSchemeName();
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 140 */     return this.path;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setPath(String path) {
/* 144 */     this.path = path;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public URI getUri() {
/* 149 */     StringBuilder buf = new StringBuilder();
/* 150 */     if (this.authority != null) {
/* 151 */       buf.append((this.scheme != null) ? this.scheme : URIScheme.HTTP.id).append("://");
/* 152 */       buf.append(this.authority.getHostName());
/* 153 */       if (this.authority.getPort() >= 0) {
/* 154 */         buf.append(":").append(this.authority.getPort());
/*     */       }
/*     */     } 
/* 157 */     if (this.path == null) {
/* 158 */       buf.append("/");
/*     */     } else {
/* 160 */       if (buf.length() > 0 && !this.path.startsWith("/")) {
/* 161 */         buf.append("/");
/*     */       }
/* 163 */       buf.append(this.path);
/*     */     } 
/* 165 */     return URI.create(buf.toString());
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setUri(URI uri) {
/* 169 */     if (uri == null) {
/* 170 */       this.scheme = null;
/* 171 */       this.authority = null;
/* 172 */       this.path = null;
/*     */     } else {
/* 174 */       this.scheme = uri.getScheme();
/* 175 */       if (uri.getHost() != null) {
/* 176 */         this.authority = new URIAuthority(uri.getRawUserInfo(), uri.getHost(), uri.getPort());
/* 177 */       } else if (uri.getRawAuthority() != null) {
/*     */         try {
/* 179 */           this.authority = URIAuthority.create(uri.getRawAuthority());
/* 180 */         } catch (URISyntaxException ignore) {
/* 181 */           this.authority = null;
/*     */         } 
/*     */       } else {
/* 184 */         this.authority = null;
/*     */       } 
/* 186 */       StringBuilder buf = new StringBuilder();
/* 187 */       String rawPath = uri.getRawPath();
/* 188 */       if (!TextUtils.isBlank(rawPath)) {
/* 189 */         buf.append(rawPath);
/*     */       } else {
/* 191 */         buf.append("/");
/*     */       } 
/* 193 */       String query = uri.getRawQuery();
/* 194 */       if (query != null) {
/* 195 */         buf.append('?').append(query);
/*     */       }
/* 197 */       this.path = buf.toString();
/*     */     } 
/* 199 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setUri(String uri) {
/* 203 */     setUri((uri != null) ? URI.create(uri) : null);
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> setHeaders(Header... headers) {
/* 209 */     super.setHeaders(headers);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> addHeader(Header header) {
/* 215 */     super.addHeader(header);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> addHeader(String name, String value) {
/* 221 */     super.addHeader(name, value);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> removeHeader(Header header) {
/* 227 */     super.removeHeader(header);
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> removeHeaders(String name) {
/* 233 */     super.removeHeaders(name);
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> setHeader(Header header) {
/* 239 */     super.setHeader(header);
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractRequestBuilder<T> setHeader(String name, String value) {
/* 245 */     super.setHeader(name, value);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 250 */     return this.charset;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setCharset(Charset charset) {
/* 254 */     this.charset = charset;
/* 255 */     return this;
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 259 */     return (this.parameters != null) ? new ArrayList<>(this.parameters) : null;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> addParameter(NameValuePair nvp) {
/* 263 */     if (nvp == null) {
/* 264 */       return this;
/*     */     }
/* 266 */     if (this.parameters == null) {
/* 267 */       this.parameters = new LinkedList<>();
/*     */     }
/* 269 */     this.parameters.add(nvp);
/* 270 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> addParameter(String name, String value) {
/* 274 */     return addParameter((NameValuePair)new BasicNameValuePair(name, value));
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> addParameters(NameValuePair... nvps) {
/* 278 */     for (NameValuePair nvp : nvps) {
/* 279 */       addParameter(nvp);
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAbsoluteRequestUri() {
/* 285 */     return this.absoluteRequestUri;
/*     */   }
/*     */   
/*     */   public AbstractRequestBuilder<T> setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 289 */     this.absoluteRequestUri = absoluteRequestUri;
/* 290 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/support/AbstractRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */