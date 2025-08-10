/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.ssl.TLS;
/*     */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
/*     */ import org.apache.hc.core5.ssl.SSLInitializationException;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class SSLConnectionSocketFactory
/*     */   implements LayeredConnectionSocketFactory
/*     */ {
/*     */   private static final String WEAK_KEY_EXCHANGES = "^(TLS|SSL)_(NULL|ECDH_anon|DH_anon|DH_anon_EXPORT|DHE_RSA_EXPORT|DHE_DSS_EXPORT|DSS_EXPORT|DH_DSS_EXPORT|DH_RSA_EXPORT|RSA_EXPORT|KRB5_EXPORT)_(.*)";
/*     */   private static final String WEAK_CIPHERS = "^(TLS|SSL)_(.*)_WITH_(NULL|DES_CBC|DES40_CBC|DES_CBC_40|3DES_EDE_CBC|RC4_128|RC4_40|RC2_CBC_40)_(.*)";
/*  84 */   private static final List<Pattern> WEAK_CIPHER_SUITE_PATTERNS = Collections.unmodifiableList(Arrays.asList(new Pattern[] {
/*  85 */           Pattern.compile("^(TLS|SSL)_(NULL|ECDH_anon|DH_anon|DH_anon_EXPORT|DHE_RSA_EXPORT|DHE_DSS_EXPORT|DSS_EXPORT|DH_DSS_EXPORT|DH_RSA_EXPORT|RSA_EXPORT|KRB5_EXPORT)_(.*)", 2), 
/*  86 */           Pattern.compile("^(TLS|SSL)_(.*)_WITH_(NULL|DES_CBC|DES40_CBC|DES_CBC_40|3DES_EDE_CBC|RC4_128|RC4_40|RC2_CBC_40)_(.*)", 2)
/*     */         }));
/*  88 */   private static final Logger LOG = LoggerFactory.getLogger(SSLConnectionSocketFactory.class);
/*     */   
/*     */   private final SSLSocketFactory socketFactory;
/*     */   
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   private final String[] supportedProtocols;
/*     */   private final String[] supportedCipherSuites;
/*     */   private final TlsSessionValidator tlsSessionValidator;
/*     */   
/*     */   public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
/*  98 */     return new SSLConnectionSocketFactory(SSLContexts.createDefault(), HttpsSupport.getDefaultHostnameVerifier());
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
/*     */   public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
/* 110 */     return new SSLConnectionSocketFactory(
/* 111 */         (SSLSocketFactory)SSLSocketFactory.getDefault(), 
/* 112 */         HttpsSupport.getSystemProtocols(), 
/* 113 */         HttpsSupport.getSystemCipherSuits(), 
/* 114 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */   
/*     */   static boolean isWeakCipherSuite(String cipherSuite) {
/* 118 */     for (Pattern pattern : WEAK_CIPHER_SUITE_PATTERNS) {
/* 119 */       if (pattern.matcher(cipherSuite).matches()) {
/* 120 */         return true;
/*     */       }
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext) {
/* 133 */     this(sslContext, HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
/* 141 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), (String[])null, (String[])null, hostnameVerifier);
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
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, HostnameVerifier hostnameVerifier) {
/* 153 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketFactory, HostnameVerifier hostnameVerifier) {
/* 163 */     this(socketFactory, (String[])null, (String[])null, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketFactory, String[] supportedProtocols, String[] supportedCipherSuites, HostnameVerifier hostnameVerifier) {
/* 174 */     this.socketFactory = (SSLSocketFactory)Args.notNull(socketFactory, "SSL socket factory");
/* 175 */     this.supportedProtocols = supportedProtocols;
/* 176 */     this.supportedCipherSuites = supportedCipherSuites;
/* 177 */     this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : HttpsSupport.getDefaultHostnameVerifier();
/* 178 */     this.tlsSessionValidator = new TlsSessionValidator(LOG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareSocket(SSLSocket socket) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpContext context) throws IOException {
/* 194 */     return SocketFactory.getDefault().createSocket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(TimeValue connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
/* 205 */     Timeout timeout = (connectTimeout != null) ? Timeout.of(connectTimeout.getDuration(), connectTimeout.getTimeUnit()) : null;
/* 206 */     return connectSocket(socket, host, remoteAddress, localAddress, timeout, timeout, context);
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
/*     */   public Socket connectSocket(Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, Timeout connectTimeout, Object attachment, HttpContext context) throws IOException {
/* 218 */     Args.notNull(host, "HTTP host");
/* 219 */     Args.notNull(remoteAddress, "Remote address");
/* 220 */     Socket sock = (socket != null) ? socket : createSocket(context);
/* 221 */     if (localAddress != null) {
/* 222 */       sock.bind(localAddress);
/*     */     }
/*     */     try {
/* 225 */       if (LOG.isDebugEnabled()) {
/* 226 */         LOG.debug("Connecting socket to {} with timeout {}", remoteAddress, connectTimeout);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 231 */         AccessController.doPrivileged(() -> {
/*     */               sock.connect(remoteAddress, Timeout.defaultsToDisabled(connectTimeout).toMillisecondsIntBound());
/*     */               return null;
/*     */             });
/* 235 */       } catch (PrivilegedActionException e) {
/* 236 */         Asserts.check(e.getCause() instanceof IOException, "method contract violation only checked exceptions are wrapped: " + e
/* 237 */             .getCause());
/*     */         
/* 239 */         throw (IOException)e.getCause();
/*     */       } 
/* 241 */     } catch (IOException ex) {
/* 242 */       Closer.closeQuietly(sock);
/* 243 */       throw ex;
/*     */     } 
/*     */     
/* 246 */     if (sock instanceof SSLSocket) {
/* 247 */       SSLSocket sslsock = (SSLSocket)sock;
/* 248 */       executeHandshake(sslsock, host.getHostName(), attachment);
/* 249 */       return sock;
/*     */     } 
/* 251 */     return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), attachment, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException {
/* 260 */     return createLayeredSocket(socket, target, port, null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String target, int port, Object attachment, HttpContext context) throws IOException {
/* 270 */     SSLSocket sslsock = (SSLSocket)this.socketFactory.createSocket(socket, target, port, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     executeHandshake(sslsock, target, attachment);
/* 276 */     return sslsock;
/*     */   }
/*     */   
/*     */   private void executeHandshake(SSLSocket sslsock, String target, Object attachment) throws IOException {
/* 280 */     TlsConfig tlsConfig = (attachment instanceof TlsConfig) ? (TlsConfig)attachment : TlsConfig.DEFAULT;
/* 281 */     if (this.supportedProtocols != null) {
/* 282 */       sslsock.setEnabledProtocols(this.supportedProtocols);
/*     */     } else {
/* 284 */       sslsock.setEnabledProtocols(TLS.excludeWeak(sslsock.getEnabledProtocols()));
/*     */     } 
/* 286 */     if (this.supportedCipherSuites != null) {
/* 287 */       sslsock.setEnabledCipherSuites(this.supportedCipherSuites);
/*     */     } else {
/* 289 */       sslsock.setEnabledCipherSuites(TlsCiphers.excludeWeak(sslsock.getEnabledCipherSuites()));
/*     */     } 
/* 291 */     Timeout handshakeTimeout = tlsConfig.getHandshakeTimeout();
/* 292 */     if (handshakeTimeout != null) {
/* 293 */       sslsock.setSoTimeout(handshakeTimeout.toMillisecondsIntBound());
/*     */     }
/*     */     
/* 296 */     prepareSocket(sslsock);
/*     */     
/* 298 */     if (LOG.isDebugEnabled()) {
/* 299 */       LOG.debug("Enabled protocols: {}", sslsock.getEnabledProtocols());
/* 300 */       LOG.debug("Enabled cipher suites: {}", sslsock.getEnabledCipherSuites());
/* 301 */       LOG.debug("Starting handshake ({})", handshakeTimeout);
/*     */     } 
/* 303 */     sslsock.startHandshake();
/* 304 */     verifyHostname(sslsock, target);
/*     */   }
/*     */   
/*     */   private void verifyHostname(SSLSocket sslsock, String hostname) throws IOException {
/*     */     try {
/* 309 */       SSLSession session = sslsock.getSession();
/* 310 */       if (session == null) {
/*     */ 
/*     */ 
/*     */         
/* 314 */         InputStream in = sslsock.getInputStream();
/* 315 */         in.available();
/*     */ 
/*     */         
/* 318 */         session = sslsock.getSession();
/* 319 */         if (session == null) {
/*     */ 
/*     */           
/* 322 */           sslsock.startHandshake();
/* 323 */           session = sslsock.getSession();
/*     */         } 
/*     */       } 
/* 326 */       if (session == null) {
/* 327 */         throw new SSLHandshakeException("SSL session not available");
/*     */       }
/* 329 */       verifySession(hostname, session);
/* 330 */     } catch (IOException iox) {
/*     */       
/* 332 */       Closer.closeQuietly(sslsock);
/* 333 */       throw iox;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void verifySession(String hostname, SSLSession sslSession) throws SSLException {
/* 340 */     this.tlsSessionValidator.verifySession(hostname, sslSession, this.hostnameVerifier);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/SSLConnectionSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */