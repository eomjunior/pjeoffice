/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseFactory;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.ServerSupport;
/*     */ import org.apache.hc.core5.http.io.HttpRequestHandler;
/*     */ import org.apache.hc.core5.http.io.HttpServerConnection;
/*     */ import org.apache.hc.core5.http.io.HttpServerRequestHandler;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*     */ import org.apache.hc.core5.http.io.support.BasicHttpServerExpectationDecorator;
/*     */ import org.apache.hc.core5.http.io.support.BasicHttpServerRequestHandler;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class HttpService
/*     */ {
/*     */   private final HttpProcessor processor;
/*     */   private final HttpServerRequestHandler requestHandler;
/*     */   private final ConnectionReuseStrategy connReuseStrategy;
/*     */   private final Http1StreamListener streamListener;
/*     */   
/*     */   public HttpService(HttpProcessor processor, HttpRequestMapper<HttpRequestHandler> handlerMapper, ConnectionReuseStrategy connReuseStrategy, HttpResponseFactory<ClassicHttpResponse> responseFactory, Http1StreamListener streamListener) {
/* 106 */     this(processor, (HttpServerRequestHandler)new BasicHttpServerExpectationDecorator((HttpServerRequestHandler)new BasicHttpServerRequestHandler(handlerMapper, responseFactory)), connReuseStrategy, streamListener);
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
/*     */   public HttpService(HttpProcessor processor, HttpRequestMapper<HttpRequestHandler> handlerMapper, ConnectionReuseStrategy connReuseStrategy, HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 127 */     this(processor, handlerMapper, connReuseStrategy, responseFactory, null);
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
/*     */   public HttpService(HttpProcessor processor, HttpServerRequestHandler requestHandler, ConnectionReuseStrategy connReuseStrategy, Http1StreamListener streamListener) {
/* 145 */     this.processor = (HttpProcessor)Args.notNull(processor, "HTTP processor");
/* 146 */     this.requestHandler = (HttpServerRequestHandler)Args.notNull(requestHandler, "Request handler");
/* 147 */     this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/* 148 */     this.streamListener = streamListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpService(HttpProcessor processor, HttpServerRequestHandler requestHandler) {
/* 159 */     this(processor, requestHandler, (ConnectionReuseStrategy)null, (Http1StreamListener)null);
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
/*     */   public void handleRequest(final HttpServerConnection conn, final HttpContext context) throws IOException, HttpException {
/* 176 */     final AtomicBoolean responseSubmitted = new AtomicBoolean(false);
/*     */     try {
/* 178 */       final ClassicHttpRequest request = conn.receiveRequestHeader();
/* 179 */       if (request == null) {
/* 180 */         conn.close();
/*     */         return;
/*     */       } 
/* 183 */       if (this.streamListener != null) {
/* 184 */         this.streamListener.onRequestHead((HttpConnection)conn, (HttpRequest)request);
/*     */       }
/* 186 */       conn.receiveRequestEntity(request);
/* 187 */       ProtocolVersion transportVersion = request.getVersion();
/* 188 */       context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 189 */       context.setAttribute("http.ssl-session", conn.getSSLSession());
/* 190 */       context.setAttribute("http.connection-endpoint", conn.getEndpointDetails());
/* 191 */       context.setAttribute("http.request", request);
/* 192 */       this.processor.process((HttpRequest)request, (EntityDetails)request.getEntity(), context);
/*     */       
/* 194 */       this.requestHandler.handle(request, new HttpServerRequestHandler.ResponseTrigger()
/*     */           {
/*     */             public void sendInformation(ClassicHttpResponse response) throws HttpException, IOException
/*     */             {
/* 198 */               if (responseSubmitted.get()) {
/* 199 */                 throw new HttpException("Response already submitted");
/*     */               }
/* 201 */               if (response.getCode() >= 200) {
/* 202 */                 throw new HttpException("Invalid intermediate response");
/*     */               }
/* 204 */               if (HttpService.this.streamListener != null) {
/* 205 */                 HttpService.this.streamListener.onResponseHead((HttpConnection)conn, (HttpResponse)response);
/*     */               }
/* 207 */               conn.sendResponseHeader(response);
/* 208 */               conn.flush();
/*     */             }
/*     */ 
/*     */             
/*     */             public void submitResponse(ClassicHttpResponse response) throws HttpException, IOException {
/*     */               try {
/* 214 */                 ProtocolVersion transportVersion = response.getVersion();
/* 215 */                 if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 216 */                   throw new UnsupportedHttpVersionException(transportVersion);
/*     */                 }
/* 218 */                 ServerSupport.validateResponse((HttpResponse)response, (EntityDetails)response.getEntity());
/* 219 */                 context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 220 */                 context.setAttribute("http.response", response);
/* 221 */                 HttpService.this.processor.process((HttpResponse)response, (EntityDetails)response.getEntity(), context);
/*     */                 
/* 223 */                 responseSubmitted.set(true);
/* 224 */                 conn.sendResponseHeader(response);
/* 225 */                 if (HttpService.this.streamListener != null) {
/* 226 */                   HttpService.this.streamListener.onResponseHead((HttpConnection)conn, (HttpResponse)response);
/*     */                 }
/* 228 */                 if (MessageSupport.canResponseHaveBody(request.getMethod(), (HttpResponse)response)) {
/* 229 */                   conn.sendResponseEntity(response);
/*     */                 }
/*     */                 
/* 232 */                 EntityUtils.consume(request.getEntity());
/* 233 */                 boolean keepAlive = HttpService.this.connReuseStrategy.keepAlive((HttpRequest)request, (HttpResponse)response, context);
/* 234 */                 if (HttpService.this.streamListener != null) {
/* 235 */                   HttpService.this.streamListener.onExchangeComplete((HttpConnection)conn, keepAlive);
/*     */                 }
/* 237 */                 if (!keepAlive) {
/* 238 */                   conn.close();
/*     */                 }
/* 240 */                 conn.flush();
/*     */               } finally {
/* 242 */                 response.close();
/*     */               }
/*     */             
/*     */             }
/*     */           }context);
/*     */     }
/* 248 */     catch (HttpException ex) {
/* 249 */       if (responseSubmitted.get()) {
/* 250 */         throw ex;
/*     */       }
/* 252 */       try (BasicClassicHttpResponse null = new BasicClassicHttpResponse(500)) {
/* 253 */         handleException(ex, (ClassicHttpResponse)basicClassicHttpResponse);
/* 254 */         basicClassicHttpResponse.setHeader("Connection", "close");
/* 255 */         context.setAttribute("http.response", basicClassicHttpResponse);
/* 256 */         this.processor.process((HttpResponse)basicClassicHttpResponse, (EntityDetails)basicClassicHttpResponse.getEntity(), context);
/*     */         
/* 258 */         conn.sendResponseHeader((ClassicHttpResponse)basicClassicHttpResponse);
/* 259 */         if (this.streamListener != null) {
/* 260 */           this.streamListener.onResponseHead((HttpConnection)conn, (HttpResponse)basicClassicHttpResponse);
/*     */         }
/* 262 */         conn.sendResponseEntity((ClassicHttpResponse)basicClassicHttpResponse);
/* 263 */         conn.close();
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
/*     */   protected void handleException(HttpException ex, ClassicHttpResponse response) {
/* 277 */     response.setCode(toStatusCode((Exception)ex));
/* 278 */     response.setEntity((HttpEntity)new StringEntity(ServerSupport.toErrorMessage((Exception)ex), ContentType.TEXT_PLAIN));
/*     */   }
/*     */   
/*     */   protected int toStatusCode(Exception ex) {
/* 282 */     return ServerSupport.toStatusCode(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 292 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private HttpProcessor processor;
/*     */     
/*     */     private HttpServerRequestHandler requestHandler;
/*     */     
/*     */     private ConnectionReuseStrategy connReuseStrategy;
/*     */     
/*     */     private Http1StreamListener streamListener;
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */     
/*     */     public Builder withHttpProcessor(HttpProcessor processor) {
/* 310 */       this.processor = processor;
/* 311 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withHttpServerRequestHandler(HttpServerRequestHandler requestHandler) {
/* 315 */       this.requestHandler = requestHandler;
/* 316 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withConnectionReuseStrategy(ConnectionReuseStrategy connReuseStrategy) {
/* 320 */       this.connReuseStrategy = connReuseStrategy;
/* 321 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withHttp1StreamListener(Http1StreamListener streamListener) {
/* 325 */       this.streamListener = streamListener;
/* 326 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpService build() {
/* 334 */       return new HttpService(this.processor, this.requestHandler, this.connReuseStrategy, this.streamListener);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/HttpService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */