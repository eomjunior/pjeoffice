/*     */ package org.apache.hc.client5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.hc.client5.http.ConnectExceptionSupport;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.SystemDefaultDnsResolver;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ final class MultihomeIOSessionRequester
/*     */ {
/*  53 */   private static final Logger LOG = LoggerFactory.getLogger(MultihomeIOSessionRequester.class);
/*     */   private final DnsResolver dnsResolver;
/*     */   
/*     */   MultihomeIOSessionRequester(DnsResolver dnsResolver) {
/*  57 */     this.dnsResolver = (dnsResolver != null) ? dnsResolver : (DnsResolver)SystemDefaultDnsResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<IOSession> connect(final ConnectionInitiator connectionInitiator, final NamedEndpoint remoteEndpoint, final SocketAddress remoteAddress, final SocketAddress localAddress, final Timeout connectTimeout, final Object attachment, FutureCallback<IOSession> callback) {
/*     */     final InetAddress[] remoteAddresses;
/*  69 */     final ComplexFuture<IOSession> future = new ComplexFuture(callback);
/*  70 */     if (remoteAddress != null) {
/*  71 */       if (LOG.isDebugEnabled()) {
/*  72 */         LOG.debug("{}:{} connecting {} to {} ({})", new Object[] { remoteEndpoint
/*  73 */               .getHostName(), Integer.valueOf(remoteEndpoint.getPort()), localAddress, remoteAddress, connectTimeout });
/*     */       }
/*  75 */       Future<IOSession> sessionFuture = connectionInitiator.connect(remoteEndpoint, remoteAddress, localAddress, connectTimeout, attachment, new FutureCallback<IOSession>()
/*     */           {
/*     */             public void completed(IOSession session) {
/*  78 */               future.completed(session);
/*     */             }
/*     */ 
/*     */             
/*     */             public void failed(Exception cause) {
/*  83 */               if (MultihomeIOSessionRequester.LOG.isDebugEnabled()) {
/*  84 */                 MultihomeIOSessionRequester.LOG.debug("{}:{} connection to {} failed ({}); terminating operation", new Object[] { this.val$remoteEndpoint
/*  85 */                       .getHostName(), Integer.valueOf(this.val$remoteEndpoint.getPort()), this.val$remoteAddress, cause.getClass() });
/*     */               }
/*  87 */               if (cause instanceof IOException) {
/*  88 */                 (new InetAddress[1])[0] = ((InetSocketAddress)remoteAddress)
/*     */                   
/*  90 */                   .getAddress(); future.failed(ConnectExceptionSupport.enhance((IOException)cause, remoteEndpoint, (remoteAddress instanceof InetSocketAddress) ? new InetAddress[1] : new InetAddress[0]));
/*     */               } else {
/*     */                 
/*  93 */                 future.failed(cause);
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public void cancelled() {
/*  99 */               future.cancel();
/*     */             }
/*     */           });
/*     */       
/* 103 */       future.setDependency(sessionFuture);
/* 104 */       return (Future<IOSession>)future;
/*     */     } 
/*     */     
/* 107 */     if (LOG.isDebugEnabled()) {
/* 108 */       LOG.debug("{} resolving remote address", remoteEndpoint.getHostName());
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 113 */       remoteAddresses = this.dnsResolver.resolve(remoteEndpoint.getHostName());
/* 114 */     } catch (UnknownHostException ex) {
/* 115 */       future.failed(ex);
/* 116 */       return (Future<IOSession>)future;
/*     */     } 
/*     */     
/* 119 */     if (LOG.isDebugEnabled()) {
/* 120 */       LOG.debug("{} resolved to {}", remoteEndpoint.getHostName(), Arrays.asList(remoteAddresses));
/*     */     }
/*     */     
/* 123 */     Runnable runnable = new Runnable()
/*     */       {
/* 125 */         private final AtomicInteger attempt = new AtomicInteger(0);
/*     */         
/*     */         void executeNext() {
/* 128 */           int index = this.attempt.getAndIncrement();
/* 129 */           final InetSocketAddress remoteAddress = new InetSocketAddress(remoteAddresses[index], remoteEndpoint.getPort());
/*     */           
/* 131 */           if (MultihomeIOSessionRequester.LOG.isDebugEnabled()) {
/* 132 */             MultihomeIOSessionRequester.LOG.debug("{}:{} connecting {}->{} ({})", new Object[] { this.val$remoteEndpoint
/* 133 */                   .getHostName(), Integer.valueOf(this.val$remoteEndpoint.getPort()), this.val$localAddress, remoteAddress, this.val$connectTimeout });
/*     */           }
/*     */           
/* 136 */           Future<IOSession> sessionFuture = connectionInitiator.connect(remoteEndpoint, remoteAddress, localAddress, connectTimeout, attachment, new FutureCallback<IOSession>()
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void completed(IOSession session)
/*     */                 {
/* 146 */                   if (MultihomeIOSessionRequester.LOG.isDebugEnabled()) {
/* 147 */                     MultihomeIOSessionRequester.LOG.debug("{}:{} connected {}->{} as {}", new Object[] { this.this$1.val$remoteEndpoint
/* 148 */                           .getHostName(), Integer.valueOf(this.this$1.val$remoteEndpoint.getPort()), this.this$1.val$localAddress, this.val$remoteAddress, session.getId() });
/*     */                   }
/* 150 */                   future.completed(session);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void failed(Exception cause) {
/* 155 */                   if (MultihomeIOSessionRequester.null.this.attempt.get() >= remoteAddresses.length) {
/* 156 */                     if (MultihomeIOSessionRequester.LOG.isDebugEnabled()) {
/* 157 */                       MultihomeIOSessionRequester.LOG.debug("{}:{} connection to {} failed ({}); terminating operation", new Object[] { this.this$1.val$remoteEndpoint
/* 158 */                             .getHostName(), Integer.valueOf(this.this$1.val$remoteEndpoint.getPort()), this.val$remoteAddress, cause.getClass() });
/*     */                     }
/* 160 */                     if (cause instanceof IOException) {
/* 161 */                       future.failed(ConnectExceptionSupport.enhance((IOException)cause, remoteEndpoint, remoteAddresses));
/*     */                     } else {
/* 163 */                       future.failed(cause);
/*     */                     } 
/*     */                   } else {
/* 166 */                     if (MultihomeIOSessionRequester.LOG.isDebugEnabled()) {
/* 167 */                       MultihomeIOSessionRequester.LOG.debug("{}:{} connection to {} failed ({}); retrying connection to the next address", new Object[] { this.this$1.val$remoteEndpoint
/* 168 */                             .getHostName(), Integer.valueOf(this.this$1.val$remoteEndpoint.getPort()), this.val$remoteAddress, cause.getClass() });
/*     */                     }
/* 170 */                     MultihomeIOSessionRequester.null.this.executeNext();
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void cancelled() {
/* 176 */                   future.cancel();
/*     */                 }
/*     */               });
/*     */           
/* 180 */           future.setDependency(sessionFuture);
/*     */         }
/*     */ 
/*     */         
/*     */         public void run() {
/* 185 */           executeNext();
/*     */         }
/*     */       };
/*     */     
/* 189 */     runnable.run();
/* 190 */     return (Future<IOSession>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<IOSession> connect(ConnectionInitiator connectionInitiator, NamedEndpoint remoteEndpoint, SocketAddress localAddress, Timeout connectTimeout, Object attachment, FutureCallback<IOSession> callback) {
/* 200 */     return connect(connectionInitiator, remoteEndpoint, null, localAddress, connectTimeout, attachment, callback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/MultihomeIOSessionRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */