/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.client5.http.ClientProtocolException;
/*     */ import org.apache.hc.client5.http.classic.HttpClient;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public abstract class CloseableHttpClient
/*     */   implements HttpClient, ModalCloseable
/*     */ {
/*  58 */   private static final Logger LOG = LoggerFactory.getLogger(CloseableHttpClient.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HttpHost determineTarget(ClassicHttpRequest request) throws ClientProtocolException {
/*     */     try {
/*  65 */       return RoutingSupport.determineHost((HttpRequest)request);
/*  66 */     } catch (HttpException ex) {
/*  67 */       throw new ClientProtocolException(ex);
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
/*     */   @Deprecated
/*     */   public CloseableHttpResponse execute(HttpHost target, ClassicHttpRequest request, HttpContext context) throws IOException {
/*  87 */     return doExecute(target, request, context);
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
/*     */   @Deprecated
/*     */   public CloseableHttpResponse execute(ClassicHttpRequest request, HttpContext context) throws IOException {
/* 105 */     Args.notNull(request, "HTTP request");
/* 106 */     return doExecute(determineTarget(request), request, context);
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
/*     */   @Deprecated
/*     */   public CloseableHttpResponse execute(ClassicHttpRequest request) throws IOException {
/* 123 */     return doExecute(determineTarget(request), request, null);
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
/*     */   @Deprecated
/*     */   public CloseableHttpResponse execute(HttpHost target, ClassicHttpRequest request) throws IOException {
/* 141 */     return doExecute(target, request, null);
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
/*     */   public <T> T execute(ClassicHttpRequest request, HttpClientResponseHandler<? extends T> responseHandler) throws IOException {
/* 162 */     return execute(request, (HttpContext)null, responseHandler);
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
/*     */   
/*     */   public <T> T execute(ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<? extends T> responseHandler) throws IOException {
/* 187 */     HttpHost target = determineTarget(request);
/* 188 */     return execute(target, request, context, responseHandler);
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
/*     */   
/*     */   public <T> T execute(HttpHost target, ClassicHttpRequest request, HttpClientResponseHandler<? extends T> responseHandler) throws IOException {
/* 213 */     return execute(target, request, null, responseHandler);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<? extends T> responseHandler) throws IOException {
/* 243 */     Args.notNull(responseHandler, "Response handler");
/*     */     
/* 245 */     ClassicHttpResponse response = doExecute(target, request, context); Throwable throwable = null;
/*     */     
/* 247 */     try { T result = (T)responseHandler.handleResponse(response);
/* 248 */       HttpEntity entity = response.getEntity();
/* 249 */       EntityUtils.consume(entity);
/* 250 */       return result; }
/* 251 */     catch (HttpException t)
/*     */     
/* 253 */     { HttpEntity entity = response.getEntity();
/*     */       try {
/* 255 */         EntityUtils.consume(entity);
/* 256 */       } catch (Exception t2) {
/*     */ 
/*     */         
/* 259 */         LOG.warn("Error consuming content after an exception.", t2);
/*     */       } 
/* 261 */       throw new ClientProtocolException(t); }
/*     */     catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/* 263 */     finally { if (response != null) if (throwable != null) { try { response.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  } else { response.close(); }
/*     */           }
/*     */   
/*     */   }
/*     */   
/*     */   protected abstract CloseableHttpResponse doExecute(HttpHost paramHttpHost, ClassicHttpRequest paramClassicHttpRequest, HttpContext paramHttpContext) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/CloseableHttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */