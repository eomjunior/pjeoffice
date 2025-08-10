/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.hc.client5.http.ClientProtocolException;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnectionShutdownException;
/*     */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.ExecSupport;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.protocol.RequestClientConnControl;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*     */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.RequestContent;
/*     */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.VersionInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class MinimalHttpClient
/*     */   extends CloseableHttpClient
/*     */ {
/*  88 */   private static final Logger LOG = LoggerFactory.getLogger(MinimalHttpClient.class);
/*     */   
/*     */   private final HttpClientConnectionManager connManager;
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   MinimalHttpClient(HttpClientConnectionManager connManager) {
/*  98 */     this.connManager = (HttpClientConnectionManager)Args.notNull(connManager, "HTTP connection manager");
/*  99 */     this.reuseStrategy = (ConnectionReuseStrategy)DefaultClientConnectionReuseStrategy.INSTANCE;
/* 100 */     this.schemePortResolver = (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/* 101 */     this.requestExecutor = new HttpRequestExecutor(this.reuseStrategy);
/* 102 */     this
/*     */ 
/*     */ 
/*     */       
/* 106 */       .httpProcessor = (HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(VersionInfo.getSoftwareInfo("Apache-HttpClient", "org.apache.hc.client5", 
/* 107 */               getClass())) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CloseableHttpResponse doExecute(HttpHost target, ClassicHttpRequest request, HttpContext context) throws IOException {
/* 115 */     Args.notNull(target, "Target host");
/* 116 */     Args.notNull(request, "HTTP request");
/* 117 */     if (request.getScheme() == null) {
/* 118 */       request.setScheme(target.getSchemeName());
/*     */     }
/* 120 */     if (request.getAuthority() == null) {
/* 121 */       request.setAuthority(new URIAuthority((NamedEndpoint)target));
/*     */     }
/* 123 */     HttpClientContext clientContext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */     
/* 125 */     RequestConfig config = null;
/* 126 */     if (request instanceof Configurable) {
/* 127 */       config = ((Configurable)request).getConfig();
/*     */     }
/* 129 */     if (config != null) {
/* 130 */       clientContext.setRequestConfig(config);
/*     */     }
/*     */     
/* 133 */     HttpRoute route = new HttpRoute(RoutingSupport.normalize(target, this.schemePortResolver));
/* 134 */     String exchangeId = ExecSupport.getNextExchangeId();
/* 135 */     clientContext.setExchangeId(exchangeId);
/* 136 */     ExecRuntime execRuntime = new InternalExecRuntime(LOG, this.connManager, this.requestExecutor, (request instanceof CancellableDependency) ? (CancellableDependency)request : null);
/*     */     
/*     */     try {
/* 139 */       if (!execRuntime.isEndpointAcquired()) {
/* 140 */         execRuntime.acquireEndpoint(exchangeId, route, null, clientContext);
/*     */       }
/* 142 */       if (!execRuntime.isEndpointConnected()) {
/* 143 */         execRuntime.connectEndpoint(clientContext);
/*     */       }
/*     */       
/* 146 */       clientContext.setAttribute("http.request", request);
/* 147 */       clientContext.setAttribute("http.route", route);
/*     */       
/* 149 */       this.httpProcessor.process((HttpRequest)request, (EntityDetails)request.getEntity(), (HttpContext)clientContext);
/* 150 */       ClassicHttpResponse response = execRuntime.execute(exchangeId, request, clientContext);
/* 151 */       this.httpProcessor.process((HttpResponse)response, (EntityDetails)response.getEntity(), (HttpContext)clientContext);
/*     */       
/* 153 */       if (this.reuseStrategy.keepAlive((HttpRequest)request, (HttpResponse)response, (HttpContext)clientContext)) {
/* 154 */         execRuntime.markConnectionReusable(null, TimeValue.NEG_ONE_MILLISECOND);
/*     */       } else {
/* 156 */         execRuntime.markConnectionNonReusable();
/*     */       } 
/*     */ 
/*     */       
/* 160 */       HttpEntity entity = response.getEntity();
/* 161 */       if (entity == null || !entity.isStreaming()) {
/*     */         
/* 163 */         execRuntime.releaseEndpoint();
/* 164 */         return new CloseableHttpResponse(response, null);
/*     */       } 
/* 166 */       ResponseEntityProxy.enhance(response, execRuntime);
/* 167 */       return new CloseableHttpResponse(response, execRuntime);
/* 168 */     } catch (ConnectionShutdownException ex) {
/* 169 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/* 170 */       ioex.initCause((Throwable)ex);
/* 171 */       execRuntime.discardEndpoint();
/* 172 */       throw ioex;
/* 173 */     } catch (HttpException httpException) {
/* 174 */       execRuntime.discardEndpoint();
/* 175 */       throw new ClientProtocolException(httpException);
/* 176 */     } catch (RuntimeException|IOException ex) {
/* 177 */       execRuntime.discardEndpoint();
/* 178 */       throw ex;
/* 179 */     } catch (Error error) {
/* 180 */       this.connManager.close(CloseMode.IMMEDIATE);
/* 181 */       throw error;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 187 */     this.connManager.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 192 */     this.connManager.close(closeMode);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/MinimalHttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */