/*     */ package org.apache.hc.core5.http.support;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.net.URIBuilder;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicRequestBuilder
/*     */   extends AbstractRequestBuilder<BasicHttpRequest>
/*     */ {
/*     */   BasicRequestBuilder(String method) {
/*  55 */     super(method);
/*     */   }
/*     */   
/*     */   BasicRequestBuilder(Method method) {
/*  59 */     super(method);
/*     */   }
/*     */   
/*     */   BasicRequestBuilder(String method, URI uri) {
/*  63 */     super(method, uri);
/*     */   }
/*     */   
/*     */   BasicRequestBuilder(Method method, URI uri) {
/*  67 */     super(method, uri);
/*     */   }
/*     */   
/*     */   BasicRequestBuilder(Method method, String uri) {
/*  71 */     super(method, uri);
/*     */   }
/*     */   
/*     */   BasicRequestBuilder(String method, String uri) {
/*  75 */     super(method, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder create(String method) {
/*  79 */     Args.notBlank(method, "HTTP method");
/*  80 */     return new BasicRequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder get() {
/*  84 */     return new BasicRequestBuilder(Method.GET);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder get(URI uri) {
/*  88 */     return new BasicRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder get(String uri) {
/*  92 */     return new BasicRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder head() {
/*  96 */     return new BasicRequestBuilder(Method.HEAD);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder head(URI uri) {
/* 100 */     return new BasicRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder head(String uri) {
/* 104 */     return new BasicRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder patch() {
/* 108 */     return new BasicRequestBuilder(Method.PATCH);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder patch(URI uri) {
/* 112 */     return new BasicRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder patch(String uri) {
/* 116 */     return new BasicRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder post() {
/* 120 */     return new BasicRequestBuilder(Method.POST);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder post(URI uri) {
/* 124 */     return new BasicRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder post(String uri) {
/* 128 */     return new BasicRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder put() {
/* 132 */     return new BasicRequestBuilder(Method.PUT);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder put(URI uri) {
/* 136 */     return new BasicRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder put(String uri) {
/* 140 */     return new BasicRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder delete() {
/* 144 */     return new BasicRequestBuilder(Method.DELETE);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder delete(URI uri) {
/* 148 */     return new BasicRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder delete(String uri) {
/* 152 */     return new BasicRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder trace() {
/* 156 */     return new BasicRequestBuilder(Method.TRACE);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder trace(URI uri) {
/* 160 */     return new BasicRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder trace(String uri) {
/* 164 */     return new BasicRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder options() {
/* 168 */     return new BasicRequestBuilder(Method.OPTIONS);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder options(URI uri) {
/* 172 */     return new BasicRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder options(String uri) {
/* 176 */     return new BasicRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static BasicRequestBuilder copy(HttpRequest request) {
/* 180 */     Args.notNull(request, "HTTP request");
/* 181 */     BasicRequestBuilder builder = new BasicRequestBuilder(request.getMethod());
/* 182 */     builder.digest(request);
/* 183 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setVersion(ProtocolVersion version) {
/* 188 */     super.setVersion(version);
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setUri(URI uri) {
/* 194 */     super.setUri(uri);
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setUri(String uri) {
/* 200 */     super.setUri(uri);
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setScheme(String scheme) {
/* 206 */     super.setScheme(scheme);
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setAuthority(URIAuthority authority) {
/* 212 */     super.setAuthority(authority);
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setHttpHost(HttpHost httpHost) {
/* 221 */     super.setHttpHost(httpHost);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setPath(String path) {
/* 227 */     super.setPath(path);
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setHeaders(Header... headers) {
/* 233 */     super.setHeaders(headers);
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder addHeader(Header header) {
/* 239 */     super.addHeader(header);
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder addHeader(String name, String value) {
/* 245 */     super.addHeader(name, value);
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder removeHeader(Header header) {
/* 251 */     super.removeHeader(header);
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder removeHeaders(String name) {
/* 257 */     super.removeHeaders(name);
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setHeader(Header header) {
/* 263 */     super.setHeader(header);
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setHeader(String name, String value) {
/* 269 */     super.setHeader(name, value);
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setCharset(Charset charset) {
/* 275 */     super.setCharset(charset);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder addParameter(NameValuePair nvp) {
/* 281 */     super.addParameter(nvp);
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder addParameter(String name, String value) {
/* 287 */     super.addParameter(name, value);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder addParameters(NameValuePair... nvps) {
/* 293 */     super.addParameters(nvps);
/* 294 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicRequestBuilder setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 299 */     super.setAbsoluteRequestUri(absoluteRequestUri);
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpRequest build() {
/* 305 */     String path = getPath();
/* 306 */     List<NameValuePair> parameters = getParameters();
/* 307 */     if (parameters != null && !parameters.isEmpty()) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 312 */         URI uri = (new URIBuilder(path)).setCharset(getCharset()).addParameters(parameters).build();
/* 313 */         path = uri.toASCIIString();
/* 314 */       } catch (URISyntaxException uRISyntaxException) {}
/*     */     }
/*     */ 
/*     */     
/* 318 */     BasicHttpRequest result = new BasicHttpRequest(getMethod(), getScheme(), getAuthority(), path);
/* 319 */     result.setVersion(getVersion());
/* 320 */     result.setHeaders(getHeaders());
/* 321 */     result.setAbsoluteRequestUri(isAbsoluteRequestUri());
/* 322 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 327 */     StringBuilder builder = new StringBuilder();
/* 328 */     builder.append("BasicRequestBuilder [method=");
/* 329 */     builder.append(getMethod());
/* 330 */     builder.append(", scheme=");
/* 331 */     builder.append(getScheme());
/* 332 */     builder.append(", authority=");
/* 333 */     builder.append(getAuthority());
/* 334 */     builder.append(", path=");
/* 335 */     builder.append(getPath());
/* 336 */     builder.append(", parameters=");
/* 337 */     builder.append(getParameters());
/* 338 */     builder.append(", headerGroup=");
/* 339 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 340 */     builder.append("]");
/* 341 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/support/BasicRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */