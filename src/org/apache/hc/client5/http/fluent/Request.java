/*     */ package org.apache.hc.client5.http.fluent;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.utils.DateUtils;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
/*     */ import org.apache.hc.core5.http.io.entity.FileEntity;
/*     */ import org.apache.hc.core5.http.io.entity.InputStreamEntity;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Request
/*     */ {
/*     */   @Deprecated
/*     */   public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*     */   @Deprecated
/*  83 */   public static final Locale DATE_LOCALE = Locale.US;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  88 */   public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
/*     */   
/*     */   private final ClassicHttpRequest request;
/*     */   private Boolean useExpectContinue;
/*     */   private Timeout connectTimeout;
/*     */   private Timeout responseTimeout;
/*     */   private HttpHost proxy;
/*     */   
/*     */   public static Request create(Method method, URI uri) {
/*  97 */     return new Request((ClassicHttpRequest)new HttpUriRequestBase(method.name(), uri));
/*     */   }
/*     */   
/*     */   public static Request create(String methodName, String uri) {
/* 101 */     return new Request((ClassicHttpRequest)new HttpUriRequestBase(methodName, URI.create(uri)));
/*     */   }
/*     */   
/*     */   public static Request create(String methodName, URI uri) {
/* 105 */     return new Request((ClassicHttpRequest)new HttpUriRequestBase(methodName, uri));
/*     */   }
/*     */   
/*     */   public static Request get(URI uri) {
/* 109 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.GET, uri));
/*     */   }
/*     */   
/*     */   public static Request get(String uri) {
/* 113 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.GET, uri));
/*     */   }
/*     */   
/*     */   public static Request head(URI uri) {
/* 117 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.HEAD, uri));
/*     */   }
/*     */   
/*     */   public static Request head(String uri) {
/* 121 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.HEAD, uri));
/*     */   }
/*     */   
/*     */   public static Request post(URI uri) {
/* 125 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.POST, uri));
/*     */   }
/*     */   
/*     */   public static Request post(String uri) {
/* 129 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.POST, uri));
/*     */   }
/*     */   
/*     */   public static Request patch(URI uri) {
/* 133 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.PATCH, uri));
/*     */   }
/*     */   
/*     */   public static Request patch(String uri) {
/* 137 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.PATCH, uri));
/*     */   }
/*     */   
/*     */   public static Request put(URI uri) {
/* 141 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.PUT, uri));
/*     */   }
/*     */   
/*     */   public static Request put(String uri) {
/* 145 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.PUT, uri));
/*     */   }
/*     */   
/*     */   public static Request trace(URI uri) {
/* 149 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.TRACE, uri));
/*     */   }
/*     */   
/*     */   public static Request trace(String uri) {
/* 153 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.TRACE, uri));
/*     */   }
/*     */   
/*     */   public static Request delete(URI uri) {
/* 157 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.DELETE, uri));
/*     */   }
/*     */   
/*     */   public static Request delete(String uri) {
/* 161 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.DELETE, uri));
/*     */   }
/*     */   
/*     */   public static Request options(URI uri) {
/* 165 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.OPTIONS, uri));
/*     */   }
/*     */   
/*     */   public static Request options(String uri) {
/* 169 */     return new Request((ClassicHttpRequest)new BasicClassicHttpRequest(Method.OPTIONS, uri));
/*     */   }
/*     */ 
/*     */   
/*     */   Request(ClassicHttpRequest request) {
/* 174 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ClassicHttpResponse internalExecute(CloseableHttpClient client, HttpClientContext localContext) throws IOException {
/*     */     RequestConfig.Builder builder;
/* 182 */     if (client instanceof Configurable) {
/* 183 */       builder = RequestConfig.copy(((Configurable)client).getConfig());
/*     */     } else {
/* 185 */       builder = RequestConfig.custom();
/*     */     } 
/* 187 */     if (this.useExpectContinue != null) {
/* 188 */       builder.setExpectContinueEnabled(this.useExpectContinue.booleanValue());
/*     */     }
/* 190 */     if (this.connectTimeout != null) {
/* 191 */       builder.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 193 */     if (this.responseTimeout != null) {
/* 194 */       builder.setResponseTimeout(this.responseTimeout);
/*     */     }
/* 196 */     if (this.proxy != null) {
/* 197 */       builder.setProxy(this.proxy);
/*     */     }
/* 199 */     RequestConfig config = builder.build();
/* 200 */     localContext.setRequestConfig(config);
/* 201 */     return client.executeOpen(null, this.request, (HttpContext)localContext);
/*     */   }
/*     */   
/*     */   public Response execute() throws IOException {
/* 205 */     return execute(Executor.CLIENT);
/*     */   }
/*     */   
/*     */   public Response execute(CloseableHttpClient client) throws IOException {
/* 209 */     return new Response(internalExecute(client, HttpClientContext.create()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Request addHeader(Header header) {
/* 215 */     this.request.addHeader(header);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Request setHeader(Header header) {
/* 223 */     this.request.setHeader(header);
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public Request addHeader(String name, String value) {
/* 228 */     this.request.addHeader(name, value);
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Request setHeader(String name, String value) {
/* 236 */     this.request.setHeader(name, value);
/* 237 */     return this;
/*     */   }
/*     */   
/*     */   public Request removeHeader(Header header) {
/* 241 */     this.request.removeHeader(header);
/* 242 */     return this;
/*     */   }
/*     */   
/*     */   public Request removeHeaders(String name) {
/* 246 */     this.request.removeHeaders(name);
/* 247 */     return this;
/*     */   }
/*     */   
/*     */   public Request setHeaders(Header... headers) {
/* 251 */     this.request.setHeaders(headers);
/* 252 */     return this;
/*     */   }
/*     */   
/*     */   public Request setCacheControl(String cacheControl) {
/* 256 */     this.request.setHeader("Cache-Control", cacheControl);
/* 257 */     return this;
/*     */   }
/*     */   
/*     */   ClassicHttpRequest getRequest() {
/* 261 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Request setDate(Date date) {
/* 269 */     this.request.setHeader("Date", DateUtils.formatStandardDate(DateUtils.toInstant(date)));
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Request setIfModifiedSince(Date date) {
/* 278 */     this.request.setHeader("If-Modified-Since", DateUtils.formatStandardDate(DateUtils.toInstant(date)));
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Request setIfUnmodifiedSince(Date date) {
/* 287 */     this.request.setHeader("If-Unmodified-Since", DateUtils.formatStandardDate(DateUtils.toInstant(date)));
/* 288 */     return this;
/*     */   }
/*     */   
/*     */   public Request setDate(Instant instant) {
/* 292 */     this.request.setHeader("Date", DateUtils.formatStandardDate(instant));
/* 293 */     return this;
/*     */   }
/*     */   
/*     */   public Request setIfModifiedSince(Instant instant) {
/* 297 */     this.request.setHeader("If-Modified-Since", DateUtils.formatStandardDate(instant));
/* 298 */     return this;
/*     */   }
/*     */   
/*     */   public Request setIfUnmodifiedSince(Instant instant) {
/* 302 */     this.request.setHeader("If-Unmodified-Since", DateUtils.formatStandardDate(instant));
/* 303 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Request version(HttpVersion version) {
/* 309 */     this.request.setVersion((ProtocolVersion)version);
/* 310 */     return this;
/*     */   }
/*     */   
/*     */   public Request useExpectContinue() {
/* 314 */     this.useExpectContinue = Boolean.TRUE;
/* 315 */     return this;
/*     */   }
/*     */   
/*     */   public Request userAgent(String agent) {
/* 319 */     this.request.setHeader("User-Agent", agent);
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Request connectTimeout(Timeout timeout) {
/* 326 */     this.connectTimeout = timeout;
/* 327 */     return this;
/*     */   }
/*     */   
/*     */   public Request responseTimeout(Timeout timeout) {
/* 331 */     this.responseTimeout = timeout;
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Request viaProxy(HttpHost proxy) {
/* 338 */     this.proxy = proxy;
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Request viaProxy(String proxy) {
/*     */     try {
/* 347 */       this.proxy = HttpHost.create(proxy);
/* 348 */     } catch (URISyntaxException e) {
/* 349 */       throw new IllegalArgumentException("Invalid host");
/*     */     } 
/* 351 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Request body(HttpEntity entity) {
/* 357 */     this.request.setEntity(entity);
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   public Request bodyForm(Iterable<? extends NameValuePair> formParams, Charset charset) {
/* 362 */     List<NameValuePair> paramList = new ArrayList<>();
/* 363 */     for (NameValuePair param : formParams) {
/* 364 */       paramList.add(param);
/*     */     }
/*     */     
/* 367 */     ContentType contentType = (charset != null) ? ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset) : ContentType.APPLICATION_FORM_URLENCODED;
/* 368 */     String s = WWWFormCodec.format(paramList, contentType.getCharset());
/* 369 */     return bodyString(s, contentType);
/*     */   }
/*     */   
/*     */   public Request bodyForm(Iterable<? extends NameValuePair> formParams) {
/* 373 */     return bodyForm(formParams, StandardCharsets.ISO_8859_1);
/*     */   }
/*     */   
/*     */   public Request bodyForm(NameValuePair... formParams) {
/* 377 */     return bodyForm(Arrays.asList(formParams), StandardCharsets.ISO_8859_1);
/*     */   }
/*     */   
/*     */   public Request bodyString(String s, ContentType contentType) {
/* 381 */     Charset charset = (contentType != null) ? contentType.getCharset() : null;
/* 382 */     byte[] raw = (charset != null) ? s.getBytes(charset) : s.getBytes();
/* 383 */     return body((HttpEntity)new ByteArrayEntity(raw, contentType));
/*     */   }
/*     */   
/*     */   public Request bodyFile(File file, ContentType contentType) {
/* 387 */     return body((HttpEntity)new FileEntity(file, contentType));
/*     */   }
/*     */   
/*     */   public Request bodyByteArray(byte[] b) {
/* 391 */     return body((HttpEntity)new ByteArrayEntity(b, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Request bodyByteArray(byte[] b, ContentType contentType) {
/* 398 */     return body((HttpEntity)new ByteArrayEntity(b, contentType));
/*     */   }
/*     */   
/*     */   public Request bodyByteArray(byte[] b, int off, int len) {
/* 402 */     return body((HttpEntity)new ByteArrayEntity(b, off, len, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Request bodyByteArray(byte[] b, int off, int len, ContentType contentType) {
/* 409 */     return body((HttpEntity)new ByteArrayEntity(b, off, len, contentType));
/*     */   }
/*     */   
/*     */   public Request bodyStream(InputStream inStream) {
/* 413 */     return body((HttpEntity)new InputStreamEntity(inStream, -1L, null));
/*     */   }
/*     */   
/*     */   public Request bodyStream(InputStream inStream, ContentType contentType) {
/* 417 */     return body((HttpEntity)new InputStreamEntity(inStream, -1L, contentType));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 422 */     return this.request.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */