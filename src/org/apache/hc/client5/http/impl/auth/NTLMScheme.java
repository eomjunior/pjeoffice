/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.MalformedChallengeException;
/*     */ import org.apache.hc.client5.http.auth.NTCredentials;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ public final class NTLMScheme
/*     */   implements AuthScheme
/*     */ {
/*     */   private final NTLMEngine engine;
/*     */   private State state;
/*     */   private String challenge;
/*     */   private NTCredentials credentials;
/*  56 */   private static final Logger LOG = LoggerFactory.getLogger(NTLMScheme.class);
/*     */   
/*     */   enum State {
/*  59 */     UNINITIATED,
/*  60 */     CHALLENGE_RECEIVED,
/*  61 */     MSG_TYPE1_GENERATED,
/*  62 */     MSG_TYPE2_RECEIVED,
/*  63 */     MSG_TYPE3_GENERATED,
/*  64 */     FAILED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTLMScheme(NTLMEngine engine) {
/*  75 */     Args.notNull(engine, "NTLM engine");
/*  76 */     this.engine = engine;
/*  77 */     this.state = State.UNINITIATED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTLMScheme() {
/*  84 */     this(new NTLMEngineImpl());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  89 */     return "NTLM";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealm() {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processChallenge(AuthChallenge authChallenge, HttpContext context) throws MalformedChallengeException {
/* 106 */     Args.notNull(authChallenge, "AuthChallenge");
/*     */     
/* 108 */     this.challenge = authChallenge.getValue();
/* 109 */     if (this.challenge == null || this.challenge.isEmpty()) {
/* 110 */       if (this.state == State.UNINITIATED) {
/* 111 */         this.state = State.CHALLENGE_RECEIVED;
/*     */       } else {
/* 113 */         this.state = State.FAILED;
/*     */       } 
/*     */     } else {
/* 116 */       if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
/* 117 */         this.state = State.FAILED;
/* 118 */         throw new MalformedChallengeException("Out of sequence NTLM response message");
/* 119 */       }  if (this.state == State.MSG_TYPE1_GENERATED) {
/* 120 */         this.state = State.MSG_TYPE2_RECEIVED;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseReady(HttpHost host, CredentialsProvider credentialsProvider, HttpContext context) throws AuthenticationException {
/* 131 */     Args.notNull(host, "Auth host");
/* 132 */     Args.notNull(credentialsProvider, "CredentialsProvider");
/*     */     
/* 134 */     AuthScope authScope = new AuthScope(host, null, getName());
/* 135 */     Credentials credentials = credentialsProvider.getCredentials(authScope, context);
/*     */     
/* 137 */     if (credentials instanceof NTCredentials) {
/* 138 */       this.credentials = (NTCredentials)credentials;
/* 139 */       return true;
/*     */     } 
/*     */     
/* 142 */     if (LOG.isDebugEnabled()) {
/* 143 */       HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 144 */       String exchangeId = clientContext.getExchangeId();
/* 145 */       LOG.debug("{} No credentials found for auth scope [{}]", exchangeId, authScope);
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 152 */     return (this.credentials != null) ? this.credentials.getUserPrincipal() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateAuthResponse(HttpHost host, HttpRequest request, HttpContext context) throws AuthenticationException {
/*     */     String response;
/* 160 */     if (this.credentials == null) {
/* 161 */       throw new AuthenticationException("NT credentials not available");
/*     */     }
/*     */     
/* 164 */     if (this.state == State.FAILED)
/* 165 */       throw new AuthenticationException("NTLM authentication failed"); 
/* 166 */     if (this.state == State.CHALLENGE_RECEIVED) {
/* 167 */       response = this.engine.generateType1Msg(this.credentials
/* 168 */           .getNetbiosDomain(), this.credentials
/* 169 */           .getWorkstation());
/* 170 */       this.state = State.MSG_TYPE1_GENERATED;
/* 171 */     } else if (this.state == State.MSG_TYPE2_RECEIVED) {
/* 172 */       response = this.engine.generateType3Msg(this.credentials
/* 173 */           .getUserName(), this.credentials
/* 174 */           .getPassword(), this.credentials
/* 175 */           .getNetbiosDomain(), this.credentials
/* 176 */           .getWorkstation(), this.challenge);
/*     */       
/* 178 */       this.state = State.MSG_TYPE3_GENERATED;
/*     */     } else {
/* 180 */       throw new AuthenticationException("Unexpected state: " + this.state);
/*     */     } 
/* 182 */     return "NTLM " + response;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChallengeComplete() {
/* 187 */     return (this.state == State.MSG_TYPE3_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 192 */     return getName() + "{" + this.state + " " + this.challenge + '}';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/NTLMScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */