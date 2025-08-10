/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.client5.http.ConnectExceptionSupport;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.SystemDefaultDnsResolver;
/*     */ import org.apache.hc.client5.http.UnsupportedSchemeException;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionOperator;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class DefaultHttpClientConnectionOperator
/*     */   implements HttpClientConnectionOperator
/*     */ {
/*     */   static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
/*  74 */   private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpClientConnectionOperator.class);
/*     */ 
/*     */   
/*     */   private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;
/*     */   
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   private final DnsResolver dnsResolver;
/*     */ 
/*     */   
/*     */   public DefaultHttpClientConnectionOperator(Lookup<ConnectionSocketFactory> socketFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/*  85 */     Args.notNull(socketFactoryRegistry, "Socket factory registry");
/*  86 */     this.socketFactoryRegistry = socketFactoryRegistry;
/*  87 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*     */     
/*  89 */     this.dnsResolver = (dnsResolver != null) ? dnsResolver : (DnsResolver)SystemDefaultDnsResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(HttpContext context) {
/*  95 */     Lookup<ConnectionSocketFactory> reg = (Lookup<ConnectionSocketFactory>)context.getAttribute("http.socket-factory-registry");
/*     */     
/*  97 */     if (reg == null) {
/*  98 */       reg = this.socketFactoryRegistry;
/*     */     }
/* 100 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ManagedHttpClientConnection conn, HttpHost host, InetSocketAddress localAddress, TimeValue connectTimeout, SocketConfig socketConfig, HttpContext context) throws IOException {
/* 111 */     Timeout timeout = (connectTimeout != null) ? Timeout.of(connectTimeout.getDuration(), connectTimeout.getTimeUnit()) : null;
/* 112 */     connect(conn, host, localAddress, timeout, socketConfig, null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ManagedHttpClientConnection conn, HttpHost host, InetSocketAddress localAddress, Timeout connectTimeout, SocketConfig socketConfig, Object attachment, HttpContext context) throws IOException {
/*     */     InetAddress[] remoteAddresses;
/* 124 */     Args.notNull(conn, "Connection");
/* 125 */     Args.notNull(host, "Host");
/* 126 */     Args.notNull(socketConfig, "Socket config");
/* 127 */     Args.notNull(context, "Context");
/* 128 */     Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry(context);
/* 129 */     ConnectionSocketFactory sf = (ConnectionSocketFactory)registry.lookup(host.getSchemeName());
/* 130 */     if (sf == null) {
/* 131 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 134 */     if (host.getAddress() != null) {
/* 135 */       remoteAddresses = new InetAddress[] { host.getAddress() };
/*     */     } else {
/* 137 */       if (LOG.isDebugEnabled()) {
/* 138 */         LOG.debug("{} resolving remote address", host.getHostName());
/*     */       }
/*     */       
/* 141 */       remoteAddresses = this.dnsResolver.resolve(host.getHostName());
/*     */       
/* 143 */       if (LOG.isDebugEnabled()) {
/* 144 */         LOG.debug("{} resolved to {}", host.getHostName(), Arrays.asList(remoteAddresses));
/*     */       }
/*     */     } 
/*     */     
/* 148 */     Timeout soTimeout = socketConfig.getSoTimeout();
/*     */     
/* 150 */     int port = this.schemePortResolver.resolve(host);
/* 151 */     for (int i = 0; i < remoteAddresses.length; i++) {
/* 152 */       InetAddress address = remoteAddresses[i];
/* 153 */       boolean last = (i == remoteAddresses.length - 1);
/*     */       
/* 155 */       Socket sock = sf.createSocket(context);
/* 156 */       if (soTimeout != null) {
/* 157 */         sock.setSoTimeout(soTimeout.toMillisecondsIntBound());
/*     */       }
/* 159 */       sock.setReuseAddress(socketConfig.isSoReuseAddress());
/* 160 */       sock.setTcpNoDelay(socketConfig.isTcpNoDelay());
/* 161 */       sock.setKeepAlive(socketConfig.isSoKeepAlive());
/* 162 */       if (socketConfig.getRcvBufSize() > 0) {
/* 163 */         sock.setReceiveBufferSize(socketConfig.getRcvBufSize());
/*     */       }
/* 165 */       if (socketConfig.getSndBufSize() > 0) {
/* 166 */         sock.setSendBufferSize(socketConfig.getSndBufSize());
/*     */       }
/*     */       
/* 169 */       int linger = socketConfig.getSoLinger().toMillisecondsIntBound();
/* 170 */       if (linger >= 0) {
/* 171 */         sock.setSoLinger(true, linger);
/*     */       }
/* 173 */       conn.bind(sock);
/*     */       
/* 175 */       InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
/* 176 */       if (LOG.isDebugEnabled()) {
/* 177 */         LOG.debug("{}:{} connecting {}->{} ({})", new Object[] { host
/* 178 */               .getHostName(), Integer.valueOf(host.getPort()), localAddress, remoteAddress, connectTimeout });
/*     */       }
/*     */       try {
/* 181 */         sock = sf.connectSocket(sock, host, remoteAddress, localAddress, connectTimeout, attachment, context);
/* 182 */         conn.bind(sock);
/* 183 */         conn.setSocketTimeout(soTimeout);
/* 184 */         if (LOG.isDebugEnabled()) {
/* 185 */           LOG.debug("{}:{} connected {}->{} as {}", new Object[] { host
/* 186 */                 .getHostName(), Integer.valueOf(host.getPort()), localAddress, remoteAddress, ConnPoolSupport.getId(conn) });
/*     */         }
/*     */         return;
/* 189 */       } catch (IOException ex) {
/* 190 */         if (last) {
/* 191 */           if (LOG.isDebugEnabled()) {
/* 192 */             LOG.debug("{}:{} connection to {} failed ({}); terminating operation", new Object[] { host
/* 193 */                   .getHostName(), Integer.valueOf(host.getPort()), remoteAddress, ex.getClass() });
/*     */           }
/* 195 */           throw ConnectExceptionSupport.enhance(ex, host, remoteAddresses);
/*     */         } 
/* 197 */         if (LOG.isDebugEnabled()) {
/* 198 */           LOG.debug("{}:{} connection to {} failed ({}); retrying connection to the next address", new Object[] { host
/* 199 */                 .getHostName(), Integer.valueOf(host.getPort()), remoteAddress, ex.getClass() });
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(ManagedHttpClientConnection conn, HttpHost host, HttpContext context) throws IOException {
/* 211 */     upgrade(conn, host, null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(ManagedHttpClientConnection conn, HttpHost host, Object attachment, HttpContext context) throws IOException {
/* 220 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 221 */     Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry((HttpContext)clientContext);
/* 222 */     ConnectionSocketFactory sf = (ConnectionSocketFactory)registry.lookup(host.getSchemeName());
/* 223 */     if (sf == null) {
/* 224 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 227 */     if (!(sf instanceof LayeredConnectionSocketFactory)) {
/* 228 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol does not support connection upgrade");
/*     */     }
/*     */     
/* 231 */     LayeredConnectionSocketFactory lsf = (LayeredConnectionSocketFactory)sf;
/* 232 */     Socket sock = conn.getSocket();
/* 233 */     if (sock == null) {
/* 234 */       throw new ConnectionClosedException("Connection is closed");
/*     */     }
/* 236 */     int port = this.schemePortResolver.resolve(host);
/* 237 */     sock = lsf.createLayeredSocket(sock, host.getHostName(), port, attachment, context);
/* 238 */     conn.bind(sock);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/DefaultHttpClientConnectionOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */