/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.UserTokenHandler;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.client5.http.impl.ConnectionShutdownException;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class MainClientExec
/*     */   implements ExecChainHandler
/*     */ {
/*  70 */   private static final Logger LOG = LoggerFactory.getLogger(MainClientExec.class);
/*     */ 
/*     */   
/*     */   private final HttpClientConnectionManager connectionManager;
/*     */ 
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */ 
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   
/*     */   private final UserTokenHandler userTokenHandler;
/*     */ 
/*     */   
/*     */   public MainClientExec(HttpClientConnectionManager connectionManager, HttpProcessor httpProcessor, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, UserTokenHandler userTokenHandler) {
/*  87 */     this.connectionManager = (HttpClientConnectionManager)Args.notNull(connectionManager, "Connection manager");
/*  88 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP protocol processor");
/*  89 */     this.reuseStrategy = (ConnectionReuseStrategy)Args.notNull(reuseStrategy, "Connection reuse strategy");
/*  90 */     this.keepAliveStrategy = (ConnectionKeepAliveStrategy)Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
/*  91 */     this.userTokenHandler = (UserTokenHandler)Args.notNull(userTokenHandler, "User token handler");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/*  99 */     Args.notNull(request, "HTTP request");
/* 100 */     Args.notNull(scope, "Scope");
/* 101 */     String exchangeId = scope.exchangeId;
/* 102 */     HttpRoute route = scope.route;
/* 103 */     HttpClientContext context = scope.clientContext;
/* 104 */     ExecRuntime execRuntime = scope.execRuntime;
/*     */     
/* 106 */     if (LOG.isDebugEnabled()) {
/* 107 */       LOG.debug("{} executing {}", exchangeId, new RequestLine((HttpRequest)request));
/*     */     }
/*     */     
/*     */     try {
/* 111 */       context.setAttribute("http.route", route);
/* 112 */       context.setAttribute("http.request", request);
/*     */       
/* 114 */       this.httpProcessor.process((HttpRequest)request, (EntityDetails)request.getEntity(), (HttpContext)context);
/*     */       
/* 116 */       ClassicHttpResponse response = execRuntime.execute(exchangeId, request, context);
/*     */       
/* 118 */       context.setAttribute("http.response", response);
/* 119 */       this.httpProcessor.process((HttpResponse)response, (EntityDetails)response.getEntity(), (HttpContext)context);
/*     */       
/* 121 */       Object userToken = context.getUserToken();
/* 122 */       if (userToken == null) {
/* 123 */         userToken = this.userTokenHandler.getUserToken(route, (HttpRequest)request, (HttpContext)context);
/* 124 */         context.setAttribute("http.user-token", userToken);
/*     */       } 
/*     */ 
/*     */       
/* 128 */       if (this.reuseStrategy.keepAlive((HttpRequest)request, (HttpResponse)response, (HttpContext)context)) {
/*     */         
/* 130 */         TimeValue duration = this.keepAliveStrategy.getKeepAliveDuration((HttpResponse)response, (HttpContext)context);
/* 131 */         if (LOG.isDebugEnabled()) {
/*     */           String s;
/* 133 */           if (duration != null) {
/* 134 */             s = "for " + duration;
/*     */           } else {
/* 136 */             s = "indefinitely";
/*     */           } 
/* 138 */           LOG.debug("{} connection can be kept alive {}", exchangeId, s);
/*     */         } 
/* 140 */         execRuntime.markConnectionReusable(userToken, duration);
/*     */       } else {
/* 142 */         execRuntime.markConnectionNonReusable();
/*     */       } 
/*     */       
/* 145 */       HttpEntity entity = response.getEntity();
/* 146 */       if (entity == null || !entity.isStreaming()) {
/*     */         
/* 148 */         execRuntime.releaseEndpoint();
/* 149 */         return new CloseableHttpResponse(response, null);
/*     */       } 
/* 151 */       return new CloseableHttpResponse(response, execRuntime);
/* 152 */     } catch (ConnectionShutdownException ex) {
/* 153 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*     */       
/* 155 */       ioex.initCause((Throwable)ex);
/* 156 */       execRuntime.discardEndpoint();
/* 157 */       throw ioex;
/* 158 */     } catch (HttpException|RuntimeException|IOException ex) {
/* 159 */       execRuntime.discardEndpoint();
/* 160 */       throw ex;
/* 161 */     } catch (Error error) {
/* 162 */       this.connectionManager.close(CloseMode.IMMEDIATE);
/* 163 */       throw error;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/MainClientExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */