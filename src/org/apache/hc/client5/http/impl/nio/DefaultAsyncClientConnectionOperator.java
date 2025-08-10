/*     */ package org.apache.hc.client5.http.impl.nio;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionOperator;
/*     */ import org.apache.hc.client5.http.nio.ManagedAsyncClientConnection;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.concurrent.FutureContribution;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ final class DefaultAsyncClientConnectionOperator
/*     */   implements AsyncClientConnectionOperator
/*     */ {
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   private final MultihomeIOSessionRequester sessionRequester;
/*     */   private final Lookup<TlsStrategy> tlsStrategyLookup;
/*     */   
/*     */   DefaultAsyncClientConnectionOperator(Lookup<TlsStrategy> tlsStrategyLookup, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/*  67 */     this.tlsStrategyLookup = (Lookup<TlsStrategy>)Args.notNull(tlsStrategyLookup, "TLS strategy lookup");
/*  68 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*  69 */     this.sessionRequester = new MultihomeIOSessionRequester(dnsResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ManagedAsyncClientConnection> connect(ConnectionInitiator connectionInitiator, HttpHost host, SocketAddress localAddress, Timeout connectTimeout, Object attachment, FutureCallback<ManagedAsyncClientConnection> callback) {
/*  80 */     return connect(connectionInitiator, host, localAddress, connectTimeout, attachment, null, callback);
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
/*     */   public Future<ManagedAsyncClientConnection> connect(ConnectionInitiator connectionInitiator, final HttpHost host, SocketAddress localAddress, final Timeout connectTimeout, final Object attachment, HttpContext context, FutureCallback<ManagedAsyncClientConnection> callback) {
/*  93 */     Args.notNull(connectionInitiator, "Connection initiator");
/*  94 */     Args.notNull(host, "Host");
/*  95 */     final ComplexFuture<ManagedAsyncClientConnection> future = new ComplexFuture(callback);
/*  96 */     HttpHost remoteEndpoint = RoutingSupport.normalize(host, this.schemePortResolver);
/*  97 */     InetAddress remoteAddress = host.getAddress();
/*  98 */     final TlsStrategy tlsStrategy = (this.tlsStrategyLookup != null) ? (TlsStrategy)this.tlsStrategyLookup.lookup(host.getSchemeName()) : null;
/*  99 */     final TlsConfig tlsConfig = (attachment instanceof TlsConfig) ? (TlsConfig)attachment : TlsConfig.DEFAULT;
/* 100 */     Future<IOSession> sessionFuture = this.sessionRequester.connect(connectionInitiator, (NamedEndpoint)remoteEndpoint, (remoteAddress != null) ? new InetSocketAddress(remoteAddress, remoteEndpoint
/*     */ 
/*     */           
/* 103 */           .getPort()) : null, localAddress, connectTimeout, tlsConfig
/*     */ 
/*     */         
/* 106 */         .getHttpVersionPolicy(), new FutureCallback<IOSession>()
/*     */         {
/*     */           
/*     */           public void completed(IOSession session)
/*     */           {
/* 111 */             final DefaultManagedAsyncClientConnection connection = new DefaultManagedAsyncClientConnection(session);
/* 112 */             if (tlsStrategy != null && URIScheme.HTTPS.same(host.getSchemeName())) {
/*     */               try {
/* 114 */                 final Timeout socketTimeout = connection.getSocketTimeout();
/* 115 */                 Timeout handshakeTimeout = tlsConfig.getHandshakeTimeout();
/* 116 */                 tlsStrategy.upgrade((TransportSecurityLayer)connection, (NamedEndpoint)host, attachment, (handshakeTimeout != null) ? handshakeTimeout : connectTimeout, (FutureCallback)new FutureContribution<TransportSecurityLayer>((BasicFuture)future)
/*     */                     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/*     */                       public void completed(TransportSecurityLayer transportSecurityLayer)
/*     */                       {
/* 125 */                         connection.setSocketTimeout(socketTimeout);
/* 126 */                         future.completed(connection);
/*     */                       }
/*     */                     });
/*     */               }
/* 130 */               catch (Exception ex) {
/* 131 */                 future.failed(ex);
/*     */               } 
/*     */             } else {
/* 134 */               future.completed(connection);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 140 */             future.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 145 */             future.cancel();
/*     */           }
/*     */         });
/*     */     
/* 149 */     future.setDependency(sessionFuture);
/* 150 */     return (Future<ManagedAsyncClientConnection>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(ManagedAsyncClientConnection connection, HttpHost host, Object attachment) {
/* 158 */     upgrade(connection, host, attachment, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(ManagedAsyncClientConnection connection, HttpHost host, Object attachment, HttpContext context) {
/* 167 */     upgrade(connection, host, attachment, context, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(final ManagedAsyncClientConnection connection, HttpHost host, Object attachment, HttpContext context, final FutureCallback<ManagedAsyncClientConnection> callback) {
/* 177 */     TlsStrategy tlsStrategy = (this.tlsStrategyLookup != null) ? (TlsStrategy)this.tlsStrategyLookup.lookup(host.getSchemeName()) : null;
/* 178 */     if (tlsStrategy != null)
/* 179 */       tlsStrategy.upgrade((TransportSecurityLayer)connection, (NamedEndpoint)host, attachment, null, (FutureCallback)new CallbackContribution<TransportSecurityLayer>(callback)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void completed(TransportSecurityLayer transportSecurityLayer)
/*     */             {
/* 188 */               if (callback != null)
/* 189 */                 callback.completed(connection); 
/*     */             }
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/DefaultAsyncClientConnectionOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */