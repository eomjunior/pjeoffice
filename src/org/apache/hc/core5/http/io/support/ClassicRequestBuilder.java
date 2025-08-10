/*     */ package org.apache.hc.core5.http.io.support;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
/*     */ import org.apache.hc.core5.http.io.entity.HttpEntities;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.support.AbstractMessageBuilder;
/*     */ import org.apache.hc.core5.http.support.AbstractRequestBuilder;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.net.URIBuilder;
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
/*     */ public class ClassicRequestBuilder
/*     */   extends AbstractRequestBuilder<ClassicHttpRequest>
/*     */ {
/*     */   private HttpEntity entity;
/*     */   
/*     */   ClassicRequestBuilder(String method) {
/*  71 */     super(method);
/*     */   }
/*     */   
/*     */   ClassicRequestBuilder(Method method) {
/*  75 */     super(method);
/*     */   }
/*     */   
/*     */   ClassicRequestBuilder(String method, URI uri) {
/*  79 */     super(method, uri);
/*     */   }
/*     */   
/*     */   ClassicRequestBuilder(Method method, URI uri) {
/*  83 */     super(method, uri);
/*     */   }
/*     */   
/*     */   ClassicRequestBuilder(Method method, String uri) {
/*  87 */     super(method, uri);
/*     */   }
/*     */   
/*     */   ClassicRequestBuilder(String method, String uri) {
/*  91 */     super(method, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder create(String method) {
/*  95 */     Args.notBlank(method, "HTTP method");
/*  96 */     return new ClassicRequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder get() {
/* 100 */     return new ClassicRequestBuilder(Method.GET);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder get(URI uri) {
/* 104 */     return new ClassicRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder get(String uri) {
/* 108 */     return new ClassicRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder head() {
/* 112 */     return new ClassicRequestBuilder(Method.HEAD);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder head(URI uri) {
/* 116 */     return new ClassicRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder head(String uri) {
/* 120 */     return new ClassicRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder patch() {
/* 124 */     return new ClassicRequestBuilder(Method.PATCH);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder patch(URI uri) {
/* 128 */     return new ClassicRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder patch(String uri) {
/* 132 */     return new ClassicRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder post() {
/* 136 */     return new ClassicRequestBuilder(Method.POST);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder post(URI uri) {
/* 140 */     return new ClassicRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder post(String uri) {
/* 144 */     return new ClassicRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder put() {
/* 148 */     return new ClassicRequestBuilder(Method.PUT);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder put(URI uri) {
/* 152 */     return new ClassicRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder put(String uri) {
/* 156 */     return new ClassicRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder delete() {
/* 160 */     return new ClassicRequestBuilder(Method.DELETE);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder delete(URI uri) {
/* 164 */     return new ClassicRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder delete(String uri) {
/* 168 */     return new ClassicRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder trace() {
/* 172 */     return new ClassicRequestBuilder(Method.TRACE);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder trace(URI uri) {
/* 176 */     return new ClassicRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder trace(String uri) {
/* 180 */     return new ClassicRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder options() {
/* 184 */     return new ClassicRequestBuilder(Method.OPTIONS);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder options(URI uri) {
/* 188 */     return new ClassicRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static ClassicRequestBuilder options(String uri) {
/* 192 */     return new ClassicRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassicRequestBuilder copy(ClassicHttpRequest request) {
/* 199 */     Args.notNull(request, "HTTP request");
/* 200 */     ClassicRequestBuilder builder = new ClassicRequestBuilder(request.getMethod());
/* 201 */     builder.digest(request);
/* 202 */     return builder;
/*     */   }
/*     */   
/*     */   protected void digest(ClassicHttpRequest request) {
/* 206 */     digest((HttpRequest)request);
/* 207 */     setEntity(request.getEntity());
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setVersion(ProtocolVersion version) {
/* 212 */     super.setVersion(version);
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setUri(URI uri) {
/* 218 */     super.setUri(uri);
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setUri(String uri) {
/* 224 */     super.setUri(uri);
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setScheme(String scheme) {
/* 230 */     super.setScheme(scheme);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setAuthority(URIAuthority authority) {
/* 236 */     super.setAuthority(authority);
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setHttpHost(HttpHost httpHost) {
/* 245 */     super.setHttpHost(httpHost);
/* 246 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setPath(String path) {
/* 251 */     super.setPath(path);
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setHeaders(Header... headers) {
/* 257 */     super.setHeaders(headers);
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder addHeader(Header header) {
/* 263 */     super.addHeader(header);
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder addHeader(String name, String value) {
/* 269 */     super.addHeader(name, value);
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder removeHeader(Header header) {
/* 275 */     super.removeHeader(header);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder removeHeaders(String name) {
/* 281 */     super.removeHeaders(name);
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setHeader(Header header) {
/* 287 */     super.setHeader(header);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setHeader(String name, String value) {
/* 293 */     super.setHeader(name, value);
/* 294 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setCharset(Charset charset) {
/* 299 */     super.setCharset(charset);
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder addParameter(NameValuePair nvp) {
/* 305 */     super.addParameter(nvp);
/* 306 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder addParameter(String name, String value) {
/* 311 */     super.addParameter(name, value);
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder addParameters(NameValuePair... nvps) {
/* 317 */     super.addParameters(nvps);
/* 318 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicRequestBuilder setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 323 */     super.setAbsoluteRequestUri(absoluteRequestUri);
/* 324 */     return this;
/*     */   }
/*     */   
/*     */   public HttpEntity getEntity() {
/* 328 */     return this.entity;
/*     */   }
/*     */   
/*     */   public ClassicRequestBuilder setEntity(HttpEntity entity) {
/* 332 */     this.entity = entity;
/* 333 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicRequestBuilder setEntity(String content, ContentType contentType) {
/* 337 */     this.entity = (HttpEntity)new StringEntity(content, contentType);
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicRequestBuilder setEntity(String content) {
/* 342 */     this.entity = (HttpEntity)new StringEntity(content);
/* 343 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicRequestBuilder setEntity(byte[] content, ContentType contentType) {
/* 347 */     this.entity = (HttpEntity)new ByteArrayEntity(content, contentType);
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicHttpRequest build() {
/* 353 */     String path = getPath();
/* 354 */     if (TextUtils.isEmpty(path)) {
/* 355 */       path = "/";
/*     */     }
/* 357 */     HttpEntity entityCopy = this.entity;
/* 358 */     String method = getMethod();
/* 359 */     List<NameValuePair> parameters = getParameters();
/* 360 */     if (parameters != null && !parameters.isEmpty()) {
/* 361 */       if (entityCopy == null && (Method.POST.isSame(method) || Method.PUT.isSame(method))) {
/* 362 */         entityCopy = HttpEntities.createUrlEncoded(parameters, getCharset());
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 368 */           URI uri = (new URIBuilder(path)).setCharset(getCharset()).addParameters(parameters).build();
/* 369 */           path = uri.toASCIIString();
/* 370 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 376 */     if (entityCopy != null && Method.TRACE.isSame(method)) {
/* 377 */       throw new IllegalStateException(Method.TRACE + " requests may not include an entity");
/*     */     }
/*     */     
/* 380 */     BasicClassicHttpRequest result = new BasicClassicHttpRequest(method, getScheme(), getAuthority(), path);
/* 381 */     result.setVersion(getVersion());
/* 382 */     result.setHeaders(getHeaders());
/* 383 */     result.setEntity(entityCopy);
/* 384 */     result.setAbsoluteRequestUri(isAbsoluteRequestUri());
/* 385 */     return (ClassicHttpRequest)result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 390 */     StringBuilder builder = new StringBuilder();
/* 391 */     builder.append("ClassicRequestBuilder [method=");
/* 392 */     builder.append(getMethod());
/* 393 */     builder.append(", scheme=");
/* 394 */     builder.append(getScheme());
/* 395 */     builder.append(", authority=");
/* 396 */     builder.append(getAuthority());
/* 397 */     builder.append(", path=");
/* 398 */     builder.append(getPath());
/* 399 */     builder.append(", parameters=");
/* 400 */     builder.append(getParameters());
/* 401 */     builder.append(", headerGroup=");
/* 402 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 403 */     builder.append(", entity=");
/* 404 */     builder.append((this.entity != null) ? this.entity.getClass() : null);
/* 405 */     builder.append("]");
/* 406 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/ClassicRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */