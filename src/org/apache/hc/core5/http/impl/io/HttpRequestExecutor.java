/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpResponseInformationCallback;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class HttpRequestExecutor
/*     */ {
/*  76 */   public static final Timeout DEFAULT_WAIT_FOR_CONTINUE = Timeout.ofSeconds(3L);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Timeout waitForContinue;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConnectionReuseStrategy connReuseStrategy;
/*     */ 
/*     */   
/*     */   private final Http1StreamListener streamListener;
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestExecutor(Timeout waitForContinue, ConnectionReuseStrategy connReuseStrategy, Http1StreamListener streamListener) {
/*  92 */     this.waitForContinue = (Timeout)Args.positive((TimeValue)waitForContinue, "Wait for continue time");
/*  93 */     this.connReuseStrategy = (connReuseStrategy != null) ? connReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*  94 */     this.streamListener = streamListener;
/*     */   }
/*     */   
/*     */   public HttpRequestExecutor(ConnectionReuseStrategy connReuseStrategy) {
/*  98 */     this(DEFAULT_WAIT_FOR_CONTINUE, connReuseStrategy, null);
/*     */   }
/*     */   
/*     */   public HttpRequestExecutor() {
/* 102 */     this(DEFAULT_WAIT_FOR_CONTINUE, null, null);
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
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, HttpClientConnection conn, HttpResponseInformationCallback informationCallback, HttpContext context) throws IOException, HttpException {
/* 124 */     Args.notNull(request, "HTTP request");
/* 125 */     Args.notNull(conn, "Client connection");
/* 126 */     Args.notNull(context, "HTTP context");
/*     */     try {
/* 128 */       context.setAttribute("http.ssl-session", conn.getSSLSession());
/* 129 */       context.setAttribute("http.connection-endpoint", conn.getEndpointDetails());
/*     */       
/* 131 */       conn.sendRequestHeader(request);
/* 132 */       if (this.streamListener != null) {
/* 133 */         this.streamListener.onRequestHead((HttpConnection)conn, (HttpRequest)request);
/*     */       }
/* 135 */       boolean expectContinue = false;
/* 136 */       HttpEntity entity = request.getEntity();
/* 137 */       if (entity != null) {
/* 138 */         Header expect = request.getFirstHeader("Expect");
/* 139 */         expectContinue = (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/* 140 */         if (!expectContinue) {
/* 141 */           conn.sendRequestEntity(request);
/*     */         }
/*     */       } 
/* 144 */       conn.flush();
/* 145 */       ClassicHttpResponse response = null;
/* 146 */       while (response == null) {
/* 147 */         if (expectContinue) {
/* 148 */           if (conn.isDataAvailable(this.waitForContinue))
/* 149 */           { response = conn.receiveResponseHeader();
/* 150 */             if (this.streamListener != null) {
/* 151 */               this.streamListener.onResponseHead((HttpConnection)conn, (HttpResponse)response);
/*     */             }
/* 153 */             int i = response.getCode();
/* 154 */             if (i == 100)
/*     */             
/* 156 */             { response = null;
/* 157 */               conn.sendRequestEntity(request); }
/* 158 */             else { if (i < 200) {
/* 159 */                 if (informationCallback != null) {
/* 160 */                   informationCallback.execute((HttpResponse)response, (HttpConnection)conn, context);
/*     */                 }
/* 162 */                 response = null; continue;
/*     */               } 
/* 164 */               if (i >= 400) {
/* 165 */                 conn.terminateRequest(request);
/*     */               } else {
/* 167 */                 conn.sendRequestEntity(request);
/*     */               }  }
/*     */              }
/* 170 */           else { conn.sendRequestEntity(request); }
/*     */           
/* 172 */           conn.flush();
/* 173 */           expectContinue = false; continue;
/*     */         } 
/* 175 */         response = conn.receiveResponseHeader();
/* 176 */         if (this.streamListener != null) {
/* 177 */           this.streamListener.onResponseHead((HttpConnection)conn, (HttpResponse)response);
/*     */         }
/* 179 */         int status = response.getCode();
/* 180 */         if (status < 100) {
/* 181 */           throw new ProtocolException("Invalid response: " + new StatusLine(response));
/*     */         }
/* 183 */         if (status < 200) {
/* 184 */           if (informationCallback != null && status != 100) {
/* 185 */             informationCallback.execute((HttpResponse)response, (HttpConnection)conn, context);
/*     */           }
/* 187 */           response = null;
/*     */         } 
/*     */       } 
/*     */       
/* 191 */       if (MessageSupport.canResponseHaveBody(request.getMethod(), (HttpResponse)response)) {
/* 192 */         conn.receiveResponseEntity(response);
/*     */       }
/* 194 */       return response;
/*     */     }
/* 196 */     catch (HttpException|IOException|RuntimeException ex) {
/* 197 */       Closer.closeQuietly((Closeable)conn);
/* 198 */       throw ex;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
/* 218 */     return execute(request, conn, null, context);
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
/*     */   public void preProcess(ClassicHttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 237 */     Args.notNull(request, "HTTP request");
/* 238 */     Args.notNull(processor, "HTTP processor");
/* 239 */     Args.notNull(context, "HTTP context");
/* 240 */     ProtocolVersion transportVersion = request.getVersion();
/* 241 */     if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 242 */       throw new UnsupportedHttpVersionException(transportVersion);
/*     */     }
/* 244 */     context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 245 */     context.setAttribute("http.request", request);
/* 246 */     processor.process((HttpRequest)request, (EntityDetails)request.getEntity(), context);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(ClassicHttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 270 */     Args.notNull(response, "HTTP response");
/* 271 */     Args.notNull(processor, "HTTP processor");
/* 272 */     Args.notNull(context, "HTTP context");
/* 273 */     ProtocolVersion transportVersion = response.getVersion();
/* 274 */     context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 275 */     context.setAttribute("http.response", response);
/* 276 */     processor.process((HttpResponse)response, (EntityDetails)response.getEntity(), context);
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
/*     */   public boolean keepAlive(ClassicHttpRequest request, ClassicHttpResponse response, HttpClientConnection connection, HttpContext context) throws IOException {
/* 294 */     Args.notNull(connection, "HTTP connection");
/* 295 */     Args.notNull(request, "HTTP request");
/* 296 */     Args.notNull(response, "HTTP response");
/* 297 */     Args.notNull(context, "HTTP context");
/* 298 */     boolean keepAlive = (connection.isConsistent() && this.connReuseStrategy.keepAlive((HttpRequest)request, (HttpResponse)response, context));
/* 299 */     if (this.streamListener != null) {
/* 300 */       this.streamListener.onExchangeComplete((HttpConnection)connection, keepAlive);
/*     */     }
/* 302 */     return keepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 311 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private Timeout waitForContinue;
/*     */     
/*     */     private ConnectionReuseStrategy connReuseStrategy;
/*     */     
/*     */     private Http1StreamListener streamListener;
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */ 
/*     */     
/*     */     public Builder withWaitForContinue(Timeout waitForContinue) {
/* 328 */       this.waitForContinue = waitForContinue;
/* 329 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withConnectionReuseStrategy(ConnectionReuseStrategy connReuseStrategy) {
/* 333 */       this.connReuseStrategy = connReuseStrategy;
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withHttp1StreamListener(Http1StreamListener streamListener) {
/* 338 */       this.streamListener = streamListener;
/* 339 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpRequestExecutor build() {
/* 347 */       return new HttpRequestExecutor(this.waitForContinue, this.connReuseStrategy, this.streamListener);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/HttpRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */