/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.entity.StringAsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.support.AbstractMessageBuilder;
/*     */ import org.apache.hc.core5.http.support.AbstractRequestBuilder;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.net.URIBuilder;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*     */ public class AsyncRequestBuilder
/*     */   extends AbstractRequestBuilder<AsyncRequestProducer>
/*     */ {
/*     */   private AsyncEntityProducer entityProducer;
/*     */   
/*     */   AsyncRequestBuilder(String method) {
/*  71 */     super(method);
/*     */   }
/*     */   
/*     */   AsyncRequestBuilder(Method method) {
/*  75 */     super(method);
/*     */   }
/*     */   
/*     */   AsyncRequestBuilder(String method, URI uri) {
/*  79 */     super(method, uri);
/*     */   }
/*     */   
/*     */   AsyncRequestBuilder(Method method, URI uri) {
/*  83 */     this(method.name(), uri);
/*     */   }
/*     */   
/*     */   AsyncRequestBuilder(Method method, String uri) {
/*  87 */     this(method.name(), (uri != null) ? URI.create(uri) : null);
/*     */   }
/*     */   
/*     */   AsyncRequestBuilder(String method, String uri) {
/*  91 */     this(method, (uri != null) ? URI.create(uri) : null);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder create(String method) {
/*  95 */     Args.notBlank(method, "HTTP method");
/*  96 */     return new AsyncRequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder get() {
/* 100 */     return new AsyncRequestBuilder(Method.GET);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder get(URI uri) {
/* 104 */     return new AsyncRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder get(String uri) {
/* 108 */     return new AsyncRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder head() {
/* 112 */     return new AsyncRequestBuilder(Method.HEAD);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder head(URI uri) {
/* 116 */     return new AsyncRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder head(String uri) {
/* 120 */     return new AsyncRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder patch() {
/* 124 */     return new AsyncRequestBuilder(Method.PATCH);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder patch(URI uri) {
/* 128 */     return new AsyncRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder patch(String uri) {
/* 132 */     return new AsyncRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder post() {
/* 136 */     return new AsyncRequestBuilder(Method.POST);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder post(URI uri) {
/* 140 */     return new AsyncRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder post(String uri) {
/* 144 */     return new AsyncRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder put() {
/* 148 */     return new AsyncRequestBuilder(Method.PUT);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder put(URI uri) {
/* 152 */     return new AsyncRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder put(String uri) {
/* 156 */     return new AsyncRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder delete() {
/* 160 */     return new AsyncRequestBuilder(Method.DELETE);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder delete(URI uri) {
/* 164 */     return new AsyncRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder delete(String uri) {
/* 168 */     return new AsyncRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder trace() {
/* 172 */     return new AsyncRequestBuilder(Method.TRACE);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder trace(URI uri) {
/* 176 */     return new AsyncRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder trace(String uri) {
/* 180 */     return new AsyncRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder options() {
/* 184 */     return new AsyncRequestBuilder(Method.OPTIONS);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder options(URI uri) {
/* 188 */     return new AsyncRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static AsyncRequestBuilder options(String uri) {
/* 192 */     return new AsyncRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setVersion(ProtocolVersion version) {
/* 197 */     super.setVersion(version);
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setUri(URI uri) {
/* 203 */     super.setUri(uri);
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setUri(String uri) {
/* 209 */     super.setUri(uri);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setScheme(String scheme) {
/* 215 */     super.setScheme(scheme);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setAuthority(URIAuthority authority) {
/* 221 */     super.setAuthority(authority);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setHttpHost(HttpHost httpHost) {
/* 230 */     super.setHttpHost(httpHost);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setPath(String path) {
/* 236 */     super.setPath(path);
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setHeaders(Header... headers) {
/* 242 */     super.setHeaders(headers);
/* 243 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder addHeader(Header header) {
/* 248 */     super.addHeader(header);
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder addHeader(String name, String value) {
/* 254 */     super.addHeader(name, value);
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder removeHeader(Header header) {
/* 260 */     super.removeHeader(header);
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder removeHeaders(String name) {
/* 266 */     super.removeHeaders(name);
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setHeader(Header header) {
/* 272 */     super.setHeader(header);
/* 273 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setHeader(String name, String value) {
/* 278 */     super.setHeader(name, value);
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setCharset(Charset charset) {
/* 284 */     super.setCharset(charset);
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder addParameter(NameValuePair nvp) {
/* 290 */     super.addParameter(nvp);
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder addParameter(String name, String value) {
/* 296 */     super.addParameter(name, value);
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder addParameters(NameValuePair... nvps) {
/* 302 */     super.addParameters(nvps);
/* 303 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncRequestBuilder setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 308 */     super.setAbsoluteRequestUri(absoluteRequestUri);
/* 309 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncEntityProducer getEntity() {
/* 313 */     return this.entityProducer;
/*     */   }
/*     */   
/*     */   public AsyncRequestBuilder setEntity(AsyncEntityProducer entityProducer) {
/* 317 */     this.entityProducer = entityProducer;
/* 318 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncRequestBuilder setEntity(String content, ContentType contentType) {
/* 322 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 323 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncRequestBuilder setEntity(String content) {
/* 327 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content);
/* 328 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncRequestBuilder setEntity(byte[] content, ContentType contentType) {
/* 332 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 333 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncRequestProducer build() {
/*     */     StringAsyncEntityProducer stringAsyncEntityProducer;
/* 338 */     String path = getPath();
/* 339 */     if (TextUtils.isEmpty(path)) {
/* 340 */       path = "/";
/*     */     }
/* 342 */     AsyncEntityProducer entityProducerCopy = this.entityProducer;
/* 343 */     String method = getMethod();
/* 344 */     List<NameValuePair> parameters = getParameters();
/* 345 */     if (parameters != null && !parameters.isEmpty()) {
/* 346 */       Charset charset = getCharset();
/* 347 */       if (entityProducerCopy == null && (Method.POST.isSame(method) || Method.PUT.isSame(method))) {
/* 348 */         String content = WWWFormCodec.format(parameters, (charset != null) ? charset : ContentType.APPLICATION_FORM_URLENCODED
/*     */             
/* 350 */             .getCharset());
/* 351 */         stringAsyncEntityProducer = new StringAsyncEntityProducer(content, ContentType.APPLICATION_FORM_URLENCODED);
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */           
/* 359 */           URI uri = (new URIBuilder(path)).setCharset(charset).addParameters(parameters).build();
/* 360 */           path = uri.toASCIIString();
/* 361 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 367 */     if (stringAsyncEntityProducer != null && Method.TRACE.isSame(method)) {
/* 368 */       throw new IllegalStateException(Method.TRACE + " requests may not include an entity");
/*     */     }
/*     */     
/* 371 */     BasicHttpRequest request = new BasicHttpRequest(method, getScheme(), getAuthority(), path);
/* 372 */     request.setVersion(getVersion());
/* 373 */     request.setHeaders(getHeaders());
/* 374 */     request.setAbsoluteRequestUri(isAbsoluteRequestUri());
/* 375 */     return new BasicRequestProducer((HttpRequest)request, (AsyncEntityProducer)stringAsyncEntityProducer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 380 */     StringBuilder builder = new StringBuilder();
/* 381 */     builder.append("AsyncRequestBuilder [method=");
/* 382 */     builder.append(getMethod());
/* 383 */     builder.append(", scheme=");
/* 384 */     builder.append(getScheme());
/* 385 */     builder.append(", authority=");
/* 386 */     builder.append(getAuthority());
/* 387 */     builder.append(", path=");
/* 388 */     builder.append(getPath());
/* 389 */     builder.append(", parameters=");
/* 390 */     builder.append(getParameters());
/* 391 */     builder.append(", headerGroup=");
/* 392 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 393 */     builder.append(", entity=");
/* 394 */     builder.append((this.entityProducer != null) ? this.entityProducer.getClass() : null);
/* 395 */     builder.append("]");
/* 396 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */