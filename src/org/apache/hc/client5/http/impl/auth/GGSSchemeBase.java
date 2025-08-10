/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.SystemDefaultDnsResolver;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.InvalidCredentialsException;
/*     */ import org.apache.hc.client5.http.auth.KerberosConfig;
/*     */ import org.apache.hc.client5.http.auth.KerberosCredentials;
/*     */ import org.apache.hc.client5.http.auth.MalformedChallengeException;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.utils.Base64;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
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
/*     */ public abstract class GGSSchemeBase
/*     */   implements AuthScheme
/*     */ {
/*     */   enum State
/*     */   {
/*  68 */     UNINITIATED,
/*  69 */     CHALLENGE_RECEIVED,
/*  70 */     TOKEN_GENERATED,
/*  71 */     FAILED;
/*     */   }
/*     */   
/*  74 */   private static final Logger LOG = LoggerFactory.getLogger(GGSSchemeBase.class);
/*     */   
/*     */   private static final String NO_TOKEN = "";
/*     */   
/*     */   private static final String KERBEROS_SCHEME = "HTTP";
/*     */   
/*     */   private final KerberosConfig config;
/*     */   private final DnsResolver dnsResolver;
/*     */   private State state;
/*     */   private GSSCredential gssCredential;
/*     */   private String challenge;
/*     */   private byte[] token;
/*     */   
/*     */   GGSSchemeBase(KerberosConfig config, DnsResolver dnsResolver) {
/*  88 */     this.config = (config != null) ? config : KerberosConfig.DEFAULT;
/*  89 */     this.dnsResolver = (dnsResolver != null) ? dnsResolver : (DnsResolver)SystemDefaultDnsResolver.INSTANCE;
/*  90 */     this.state = State.UNINITIATED;
/*     */   }
/*     */   
/*     */   GGSSchemeBase(KerberosConfig config) {
/*  94 */     this(config, (DnsResolver)SystemDefaultDnsResolver.INSTANCE);
/*     */   }
/*     */   
/*     */   GGSSchemeBase() {
/*  98 */     this(KerberosConfig.DEFAULT, (DnsResolver)SystemDefaultDnsResolver.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processChallenge(AuthChallenge authChallenge, HttpContext context) throws MalformedChallengeException {
/* 110 */     Args.notNull(authChallenge, "AuthChallenge");
/*     */     
/* 112 */     this.challenge = (authChallenge.getValue() != null) ? authChallenge.getValue() : "";
/*     */     
/* 114 */     if (this.state == State.UNINITIATED) {
/* 115 */       this.token = Base64.decodeBase64(this.challenge.getBytes());
/* 116 */       this.state = State.CHALLENGE_RECEIVED;
/*     */     } else {
/* 118 */       if (LOG.isDebugEnabled()) {
/* 119 */         HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 120 */         String exchangeId = clientContext.getExchangeId();
/* 121 */         LOG.debug("{} Authentication already attempted", exchangeId);
/*     */       } 
/* 123 */       this.state = State.FAILED;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected GSSManager getManager() {
/* 128 */     return GSSManager.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String serviceName, String authServer) throws GSSException {
/* 136 */     GSSManager manager = getManager();
/* 137 */     GSSName serverName = manager.createName(serviceName + "@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
/*     */     
/* 139 */     GSSContext gssContext = createGSSContext(manager, oid, serverName, this.gssCredential);
/* 140 */     if (input != null) {
/* 141 */       return gssContext.initSecContext(input, 0, input.length);
/*     */     }
/* 143 */     return gssContext.initSecContext(new byte[0], 0, 0);
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
/*     */   protected GSSContext createGSSContext(GSSManager manager, Oid oid, GSSName serverName, GSSCredential gssCredential) throws GSSException {
/* 155 */     GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, gssCredential, 0);
/*     */     
/* 157 */     gssContext.requestMutualAuth(true);
/* 158 */     if (this.config.getRequestDelegCreds() != KerberosConfig.Option.DEFAULT) {
/* 159 */       gssContext.requestCredDeleg((this.config.getRequestDelegCreds() == KerberosConfig.Option.ENABLE));
/*     */     }
/* 161 */     return gssContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract byte[] generateToken(byte[] paramArrayOfbyte, String paramString1, String paramString2) throws GSSException;
/*     */ 
/*     */   
/*     */   public boolean isChallengeComplete() {
/* 170 */     return (this.state == State.TOKEN_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseReady(HttpHost host, CredentialsProvider credentialsProvider, HttpContext context) throws AuthenticationException {
/* 179 */     Args.notNull(host, "Auth host");
/* 180 */     Args.notNull(credentialsProvider, "CredentialsProvider");
/*     */     
/* 182 */     Credentials credentials = credentialsProvider.getCredentials(new AuthScope(host, null, 
/* 183 */           getName()), context);
/* 184 */     if (credentials instanceof KerberosCredentials) {
/* 185 */       this.gssCredential = ((KerberosCredentials)credentials).getGSSCredential();
/*     */     } else {
/* 187 */       this.gssCredential = null;
/*     */     } 
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateAuthResponse(HttpHost host, HttpRequest request, HttpContext context) throws AuthenticationException {
/*     */     Base64 codec;
/*     */     String tokenstr;
/* 202 */     Args.notNull(host, "HTTP host");
/* 203 */     Args.notNull(request, "HTTP request");
/* 204 */     switch (this.state) {
/*     */       case UNINITIATED:
/* 206 */         throw new AuthenticationException(getName() + " authentication has not been initiated");
/*     */       case FAILED:
/* 208 */         throw new AuthenticationException(getName() + " authentication has failed");
/*     */       
/*     */       case CHALLENGE_RECEIVED:
/*     */         try {
/* 212 */           String authServer, hostname = host.getHostName();
/* 213 */           if (this.config.getUseCanonicalHostname() != KerberosConfig.Option.DISABLE) {
/*     */             try {
/* 215 */               hostname = this.dnsResolver.resolveCanonicalHostname(host.getHostName());
/* 216 */             } catch (UnknownHostException unknownHostException) {}
/*     */           }
/*     */           
/* 219 */           if (this.config.getStripPort() != KerberosConfig.Option.DISABLE) {
/* 220 */             authServer = hostname;
/*     */           } else {
/* 222 */             authServer = hostname + ":" + host.getPort();
/*     */           } 
/*     */           
/* 225 */           if (LOG.isDebugEnabled()) {
/* 226 */             HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 227 */             String exchangeId = clientContext.getExchangeId();
/* 228 */             LOG.debug("{} init {}", exchangeId, authServer);
/*     */           } 
/* 230 */           this.token = generateToken(this.token, "HTTP", authServer);
/* 231 */           this.state = State.TOKEN_GENERATED;
/* 232 */         } catch (GSSException gsse) {
/* 233 */           this.state = State.FAILED;
/* 234 */           if (gsse.getMajor() == 9 || gsse
/* 235 */             .getMajor() == 8) {
/* 236 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 238 */           if (gsse.getMajor() == 13) {
/* 239 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 241 */           if (gsse.getMajor() == 10 || gsse
/* 242 */             .getMajor() == 19 || gsse
/* 243 */             .getMajor() == 20) {
/* 244 */             throw new AuthenticationException(gsse.getMessage(), gsse);
/*     */           }
/*     */           
/* 247 */           throw new AuthenticationException(gsse.getMessage());
/*     */         } 
/*     */       case TOKEN_GENERATED:
/* 250 */         codec = new Base64(0);
/* 251 */         tokenstr = new String(codec.encode(this.token));
/* 252 */         if (LOG.isDebugEnabled()) {
/* 253 */           HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 254 */           String exchangeId = clientContext.getExchangeId();
/* 255 */           LOG.debug("{} Sending response '{}' back to the auth server", exchangeId, tokenstr);
/*     */         } 
/* 257 */         return "Negotiate " + tokenstr;
/*     */     } 
/* 259 */     throw new IllegalStateException("Illegal state: " + this.state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 265 */     return getName() + "{" + this.state + " " + this.challenge + '}';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/GGSSchemeBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */