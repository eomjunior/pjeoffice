/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*     */ import org.apache.hc.client5.http.impl.TunnelRefusedException;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.client5.http.protocol.RequestClientConnControl;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*     */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
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
/*     */ public class ProxyClient
/*     */ {
/*     */   private final HttpConnectionFactory<ManagedHttpClientConnection> connFactory;
/*     */   private final RequestConfig requestConfig;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final HttpRequestExecutor requestExec;
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   private final HttpAuthenticator authenticator;
/*     */   private final AuthExchange proxyAuthExchange;
/*     */   private final Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   
/*     */   public ProxyClient(HttpConnectionFactory<ManagedHttpClientConnection> connFactory, Http1Config h1Config, CharCodingConfig charCodingConfig, RequestConfig requestConfig) {
/* 108 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       .connFactory = (connFactory != null) ? connFactory : (HttpConnectionFactory<ManagedHttpClientConnection>)ManagedHttpClientConnectionFactory.builder().http1Config(h1Config).charCodingConfig(charCodingConfig).build();
/* 114 */     this.requestConfig = (requestConfig != null) ? requestConfig : RequestConfig.DEFAULT;
/* 115 */     this.httpProcessor = (HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent() });
/*     */     
/* 117 */     this.requestExec = new HttpRequestExecutor();
/* 118 */     this.proxyAuthStrategy = (AuthenticationStrategy)new DefaultAuthenticationStrategy();
/* 119 */     this.authenticator = new HttpAuthenticator();
/* 120 */     this.proxyAuthExchange = new AuthExchange();
/* 121 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 127 */       .authSchemeRegistry = (Lookup<AuthSchemeFactory>)RegistryBuilder.create().register("Basic", BasicSchemeFactory.INSTANCE).register("Digest", DigestSchemeFactory.INSTANCE).register("NTLM", NTLMSchemeFactory.INSTANCE).register("Negotiate", SPNegoSchemeFactory.DEFAULT).register("Kerberos", KerberosSchemeFactory.DEFAULT).build();
/* 128 */     this.reuseStrategy = (ConnectionReuseStrategy)DefaultClientConnectionReuseStrategy.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyClient(RequestConfig requestConfig) {
/* 135 */     this(null, null, null, requestConfig);
/*     */   }
/*     */   
/*     */   public ProxyClient() {
/* 139 */     this(null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket tunnel(HttpHost proxy, HttpHost target, Credentials credentials) throws IOException, HttpException {
/*     */     ClassicHttpResponse response;
/* 146 */     Args.notNull(proxy, "Proxy host");
/* 147 */     Args.notNull(target, "Target host");
/* 148 */     Args.notNull(credentials, "Credentials");
/* 149 */     HttpHost host = target;
/* 150 */     if (host.getPort() <= 0) {
/* 151 */       host = new HttpHost(host.getSchemeName(), host.getHostName(), 80);
/*     */     }
/* 153 */     HttpRoute route = new HttpRoute(host, null, proxy, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     ManagedHttpClientConnection conn = (ManagedHttpClientConnection)this.connFactory.createConnection(null);
/* 159 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/*     */ 
/*     */     
/* 162 */     BasicClassicHttpRequest basicClassicHttpRequest = new BasicClassicHttpRequest(Method.CONNECT, proxy, host.toHostString());
/*     */     
/* 164 */     BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
/* 165 */     credsProvider.setCredentials(new AuthScope(proxy), credentials);
/*     */ 
/*     */     
/* 168 */     basicHttpContext.setAttribute("http.request", basicClassicHttpRequest);
/* 169 */     basicHttpContext.setAttribute("http.route", route);
/* 170 */     basicHttpContext.setAttribute("http.auth.credentials-provider", credsProvider);
/* 171 */     basicHttpContext.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/* 172 */     basicHttpContext.setAttribute("http.request-config", this.requestConfig);
/*     */     
/* 174 */     this.requestExec.preProcess((ClassicHttpRequest)basicClassicHttpRequest, this.httpProcessor, (HttpContext)basicHttpContext);
/*     */     
/*     */     while (true) {
/* 177 */       if (!conn.isOpen()) {
/* 178 */         Socket socket = new Socket(proxy.getHostName(), proxy.getPort());
/* 179 */         conn.bind(socket);
/*     */       } 
/*     */       
/* 182 */       this.authenticator.addAuthResponse(proxy, ChallengeType.PROXY, (HttpRequest)basicClassicHttpRequest, this.proxyAuthExchange, (HttpContext)basicHttpContext);
/*     */       
/* 184 */       response = this.requestExec.execute((ClassicHttpRequest)basicClassicHttpRequest, (HttpClientConnection)conn, (HttpContext)basicHttpContext);
/*     */       
/* 186 */       int i = response.getCode();
/* 187 */       if (i < 200) {
/* 188 */         throw new HttpException("Unexpected response to CONNECT request: " + response);
/*     */       }
/* 190 */       if (this.authenticator.isChallenged(proxy, ChallengeType.PROXY, (HttpResponse)response, this.proxyAuthExchange, (HttpContext)basicHttpContext) && 
/* 191 */         this.authenticator.updateAuthState(proxy, ChallengeType.PROXY, (HttpResponse)response, this.proxyAuthStrategy, this.proxyAuthExchange, (HttpContext)basicHttpContext)) {
/*     */ 
/*     */         
/* 194 */         if (this.reuseStrategy.keepAlive((HttpRequest)basicClassicHttpRequest, (HttpResponse)response, (HttpContext)basicHttpContext)) {
/*     */           
/* 196 */           HttpEntity entity = response.getEntity();
/* 197 */           EntityUtils.consume(entity);
/*     */         } else {
/* 199 */           conn.close();
/*     */         } 
/*     */         
/* 202 */         basicClassicHttpRequest.removeHeaders("Proxy-Authorization");
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 211 */     int status = response.getCode();
/*     */     
/* 213 */     if (status > 299) {
/*     */ 
/*     */       
/* 216 */       HttpEntity entity = response.getEntity();
/* 217 */       String responseMessage = (entity != null) ? EntityUtils.toString(entity) : null;
/* 218 */       conn.close();
/* 219 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + new StatusLine(response), responseMessage);
/*     */     } 
/* 221 */     return conn.getSocket();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ProxyClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */