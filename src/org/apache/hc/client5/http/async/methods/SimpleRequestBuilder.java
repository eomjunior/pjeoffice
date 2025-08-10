/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.support.AbstractMessageBuilder;
/*     */ import org.apache.hc.core5.http.support.AbstractRequestBuilder;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.net.URIBuilder;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleRequestBuilder
/*     */   extends AbstractRequestBuilder<SimpleHttpRequest>
/*     */ {
/*     */   private SimpleBody body;
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   SimpleRequestBuilder(String method) {
/*  68 */     super(method);
/*     */   }
/*     */   
/*     */   SimpleRequestBuilder(Method method) {
/*  72 */     super(method);
/*     */   }
/*     */   
/*     */   SimpleRequestBuilder(String method, URI uri) {
/*  76 */     super(method, uri);
/*     */   }
/*     */   
/*     */   SimpleRequestBuilder(Method method, URI uri) {
/*  80 */     super(method, uri);
/*     */   }
/*     */   
/*     */   SimpleRequestBuilder(Method method, String uri) {
/*  84 */     super(method, uri);
/*     */   }
/*     */   
/*     */   SimpleRequestBuilder(String method, String uri) {
/*  88 */     super(method, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder create(String method) {
/*  92 */     Args.notBlank(method, "HTTP method");
/*  93 */     return new SimpleRequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder create(Method method) {
/*  97 */     Args.notNull(method, "HTTP method");
/*  98 */     return new SimpleRequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder get() {
/* 102 */     return new SimpleRequestBuilder(Method.GET);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder get(URI uri) {
/* 106 */     return new SimpleRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder get(String uri) {
/* 110 */     return new SimpleRequestBuilder(Method.GET, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder head() {
/* 114 */     return new SimpleRequestBuilder(Method.HEAD);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder head(URI uri) {
/* 118 */     return new SimpleRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder head(String uri) {
/* 122 */     return new SimpleRequestBuilder(Method.HEAD, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder patch() {
/* 126 */     return new SimpleRequestBuilder(Method.PATCH);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder patch(URI uri) {
/* 130 */     return new SimpleRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder patch(String uri) {
/* 134 */     return new SimpleRequestBuilder(Method.PATCH, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder post() {
/* 138 */     return new SimpleRequestBuilder(Method.POST);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder post(URI uri) {
/* 142 */     return new SimpleRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder post(String uri) {
/* 146 */     return new SimpleRequestBuilder(Method.POST, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder put() {
/* 150 */     return new SimpleRequestBuilder(Method.PUT);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder put(URI uri) {
/* 154 */     return new SimpleRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder put(String uri) {
/* 158 */     return new SimpleRequestBuilder(Method.PUT, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder delete() {
/* 162 */     return new SimpleRequestBuilder(Method.DELETE);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder delete(URI uri) {
/* 166 */     return new SimpleRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder delete(String uri) {
/* 170 */     return new SimpleRequestBuilder(Method.DELETE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder trace() {
/* 174 */     return new SimpleRequestBuilder(Method.TRACE);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder trace(URI uri) {
/* 178 */     return new SimpleRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder trace(String uri) {
/* 182 */     return new SimpleRequestBuilder(Method.TRACE, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder options() {
/* 186 */     return new SimpleRequestBuilder(Method.OPTIONS);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder options(URI uri) {
/* 190 */     return new SimpleRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */   
/*     */   public static SimpleRequestBuilder options(String uri) {
/* 194 */     return new SimpleRequestBuilder(Method.OPTIONS, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleRequestBuilder copy(SimpleHttpRequest request) {
/* 201 */     Args.notNull(request, "HTTP request");
/* 202 */     SimpleRequestBuilder builder = new SimpleRequestBuilder(request.getMethod());
/* 203 */     builder.digest(request);
/* 204 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleRequestBuilder copy(HttpRequest request) {
/* 211 */     Args.notNull(request, "HTTP request");
/* 212 */     SimpleRequestBuilder builder = new SimpleRequestBuilder(request.getMethod());
/* 213 */     builder.digest(request);
/* 214 */     return builder;
/*     */   }
/*     */   
/*     */   protected void digest(SimpleHttpRequest request) {
/* 218 */     super.digest((HttpRequest)request);
/* 219 */     setBody(request.getBody());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void digest(HttpRequest request) {
/* 224 */     super.digest(request);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setVersion(ProtocolVersion version) {
/* 229 */     super.setVersion(version);
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setUri(URI uri) {
/* 235 */     super.setUri(uri);
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setUri(String uri) {
/* 241 */     super.setUri(uri);
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setScheme(String scheme) {
/* 247 */     super.setScheme(scheme);
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setAuthority(URIAuthority authority) {
/* 253 */     super.setAuthority(authority);
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setHttpHost(HttpHost httpHost) {
/* 259 */     super.setHttpHost(httpHost);
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setPath(String path) {
/* 265 */     super.setPath(path);
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setHeaders(Header... headers) {
/* 271 */     super.setHeaders(headers);
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder addHeader(Header header) {
/* 277 */     super.addHeader(header);
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder addHeader(String name, String value) {
/* 283 */     super.addHeader(name, value);
/* 284 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder removeHeader(Header header) {
/* 289 */     super.removeHeader(header);
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder removeHeaders(String name) {
/* 295 */     super.removeHeaders(name);
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setHeader(Header header) {
/* 301 */     super.setHeader(header);
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setHeader(String name, String value) {
/* 307 */     super.setHeader(name, value);
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setCharset(Charset charset) {
/* 313 */     super.setCharset(charset);
/* 314 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder addParameter(NameValuePair nvp) {
/* 319 */     super.addParameter(nvp);
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder addParameter(String name, String value) {
/* 325 */     super.addParameter(name, value);
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder addParameters(NameValuePair... nvps) {
/* 331 */     super.addParameters(nvps);
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleRequestBuilder setAbsoluteRequestUri(boolean absoluteRequestUri) {
/* 337 */     super.setAbsoluteRequestUri(absoluteRequestUri);
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleBody getBody() {
/* 342 */     return this.body;
/*     */   }
/*     */   
/*     */   public SimpleRequestBuilder setBody(SimpleBody body) {
/* 346 */     this.body = body;
/* 347 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleRequestBuilder setBody(String content, ContentType contentType) {
/* 351 */     this.body = SimpleBody.create(content, contentType);
/* 352 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleRequestBuilder setBody(byte[] content, ContentType contentType) {
/* 356 */     this.body = SimpleBody.create(content, contentType);
/* 357 */     return this;
/*     */   }
/*     */   
/*     */   public RequestConfig getRequestConfig() {
/* 361 */     return this.requestConfig;
/*     */   }
/*     */   
/*     */   public SimpleRequestBuilder setRequestConfig(RequestConfig requestConfig) {
/* 365 */     this.requestConfig = requestConfig;
/* 366 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleHttpRequest build() {
/* 371 */     String path = getPath();
/* 372 */     SimpleBody bodyCopy = this.body;
/* 373 */     String method = getMethod();
/* 374 */     List<NameValuePair> parameters = getParameters();
/* 375 */     if (parameters != null && !parameters.isEmpty()) {
/* 376 */       Charset charsetCopy = getCharset();
/* 377 */       if (bodyCopy == null && (Method.POST.isSame(method) || Method.PUT.isSame(method))) {
/* 378 */         String content = WWWFormCodec.format(parameters, (charsetCopy != null) ? charsetCopy : ContentType.APPLICATION_FORM_URLENCODED
/*     */             
/* 380 */             .getCharset());
/* 381 */         bodyCopy = SimpleBody.create(content, ContentType.APPLICATION_FORM_URLENCODED);
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 387 */           URI uri = (new URIBuilder(path)).setCharset(charsetCopy).addParameters(parameters).build();
/* 388 */           path = uri.toASCIIString();
/* 389 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 395 */     if (bodyCopy != null && Method.TRACE.isSame(method)) {
/* 396 */       throw new IllegalStateException(Method.TRACE + " requests may not include an entity");
/*     */     }
/*     */     
/* 399 */     SimpleHttpRequest result = new SimpleHttpRequest(method, getScheme(), getAuthority(), path);
/* 400 */     result.setVersion(getVersion());
/* 401 */     result.setHeaders(getHeaders());
/* 402 */     result.setBody(bodyCopy);
/* 403 */     result.setAbsoluteRequestUri(isAbsoluteRequestUri());
/* 404 */     result.setConfig(this.requestConfig);
/* 405 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 410 */     StringBuilder builder = new StringBuilder();
/* 411 */     builder.append("ClassicRequestBuilder [method=");
/* 412 */     builder.append(getMethod());
/* 413 */     builder.append(", scheme=");
/* 414 */     builder.append(getScheme());
/* 415 */     builder.append(", authority=");
/* 416 */     builder.append(getAuthority());
/* 417 */     builder.append(", path=");
/* 418 */     builder.append(getPath());
/* 419 */     builder.append(", parameters=");
/* 420 */     builder.append(getParameters());
/* 421 */     builder.append(", headerGroup=");
/* 422 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 423 */     builder.append(", body=");
/* 424 */     builder.append(this.body);
/* 425 */     builder.append("]");
/* 426 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */