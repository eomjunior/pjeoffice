/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.MalformedChallengeException;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.http.message.ParserCursor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public final class HttpAuthenticator
/*     */ {
/*  75 */   private static final Logger LOG = LoggerFactory.getLogger(HttpAuthenticator.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private final AuthChallengeParser parser = new AuthChallengeParser();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChallenged(HttpHost host, ChallengeType challengeType, HttpResponse response, AuthExchange authExchange, HttpContext context) {
/*     */     int challengeCode;
/* 101 */     switch (challengeType) {
/*     */       case CHALLENGED:
/* 103 */         challengeCode = 401;
/*     */         break;
/*     */       case HANDSHAKE:
/* 106 */         challengeCode = 407;
/*     */         break;
/*     */       default:
/* 109 */         throw new IllegalStateException("Unexpected challenge type: " + challengeType);
/*     */     } 
/*     */     
/* 112 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 113 */     String exchangeId = clientContext.getExchangeId();
/*     */     
/* 115 */     if (response.getCode() == challengeCode) {
/* 116 */       if (LOG.isDebugEnabled()) {
/* 117 */         LOG.debug("{} Authentication required", exchangeId);
/*     */       }
/* 119 */       return true;
/*     */     } 
/* 121 */     switch (authExchange.getState()) {
/*     */       case CHALLENGED:
/*     */       case HANDSHAKE:
/* 124 */         if (LOG.isDebugEnabled()) {
/* 125 */           LOG.debug("{} Authentication succeeded", exchangeId);
/*     */         }
/* 127 */         authExchange.setState(AuthExchange.State.SUCCESS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case SUCCESS:
/* 134 */         return false;
/*     */     } 
/*     */     authExchange.setState(AuthExchange.State.UNCHALLENGED);
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
/*     */   public boolean updateAuthState(HttpHost host, ChallengeType challengeType, HttpResponse response, AuthenticationStrategy authStrategy, AuthExchange authExchange, HttpContext context) {
/*     */     AuthScheme authScheme;
/* 158 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 159 */     String exchangeId = clientContext.getExchangeId();
/*     */     
/* 161 */     if (LOG.isDebugEnabled()) {
/* 162 */       LOG.debug("{} {} requested authentication", exchangeId, host.toHostString());
/*     */     }
/*     */     
/* 165 */     Header[] headers = response.getHeaders((challengeType == ChallengeType.PROXY) ? "Proxy-Authenticate" : "WWW-Authenticate");
/*     */     
/* 167 */     Map<String, AuthChallenge> challengeMap = new HashMap<>();
/* 168 */     for (Header header : headers) {
/*     */       CharArrayBuffer buffer; int pos;
/*     */       List<AuthChallenge> authChallenges;
/* 171 */       if (header instanceof FormattedHeader) {
/* 172 */         buffer = ((FormattedHeader)header).getBuffer();
/* 173 */         pos = ((FormattedHeader)header).getValuePos();
/*     */       } else {
/* 175 */         String s = header.getValue();
/* 176 */         if (s == null) {
/*     */           continue;
/*     */         }
/* 179 */         buffer = new CharArrayBuffer(s.length());
/* 180 */         buffer.append(s);
/* 181 */         pos = 0;
/*     */       } 
/* 183 */       ParserCursor cursor = new ParserCursor(pos, buffer.length());
/*     */       
/*     */       try {
/* 186 */         authChallenges = this.parser.parse(challengeType, (CharSequence)buffer, cursor);
/* 187 */       } catch (ParseException ex) {
/* 188 */         if (LOG.isWarnEnabled()) {
/* 189 */           LOG.warn("{} Malformed challenge: {}", exchangeId, header.getValue());
/*     */         }
/*     */       } 
/*     */       
/* 193 */       for (AuthChallenge authChallenge : authChallenges) {
/* 194 */         String schemeName = authChallenge.getSchemeName().toLowerCase(Locale.ROOT);
/* 195 */         if (!challengeMap.containsKey(schemeName))
/* 196 */           challengeMap.put(schemeName, authChallenge); 
/*     */       } 
/*     */       continue;
/*     */     } 
/* 200 */     if (challengeMap.isEmpty()) {
/* 201 */       if (LOG.isDebugEnabled()) {
/* 202 */         LOG.debug("{} Response contains no valid authentication challenges", exchangeId);
/*     */       }
/* 204 */       authExchange.reset();
/* 205 */       return false;
/*     */     } 
/*     */     
/* 208 */     switch (authExchange.getState()) {
/*     */       case FAILURE:
/* 210 */         return false;
/*     */       case SUCCESS:
/* 212 */         authExchange.reset();
/*     */         break;
/*     */       case CHALLENGED:
/*     */       case HANDSHAKE:
/* 216 */         Asserts.notNull(authExchange.getAuthScheme(), "AuthScheme");
/*     */       case UNCHALLENGED:
/* 218 */         authScheme = authExchange.getAuthScheme();
/* 219 */         if (authScheme != null) {
/* 220 */           String schemeName = authScheme.getName();
/* 221 */           AuthChallenge challenge = challengeMap.get(schemeName.toLowerCase(Locale.ROOT));
/* 222 */           if (challenge != null) {
/* 223 */             if (LOG.isDebugEnabled()) {
/* 224 */               LOG.debug("{} Authorization challenge processed", exchangeId);
/*     */             }
/*     */             try {
/* 227 */               authScheme.processChallenge(challenge, context);
/* 228 */             } catch (MalformedChallengeException ex) {
/* 229 */               if (LOG.isWarnEnabled()) {
/* 230 */                 LOG.warn("{} {}", exchangeId, ex.getMessage());
/*     */               }
/* 232 */               authExchange.reset();
/* 233 */               authExchange.setState(AuthExchange.State.FAILURE);
/* 234 */               return false;
/*     */             } 
/* 236 */             if (authScheme.isChallengeComplete()) {
/* 237 */               if (LOG.isDebugEnabled()) {
/* 238 */                 LOG.debug("{} Authentication failed", exchangeId);
/*     */               }
/* 240 */               authExchange.reset();
/* 241 */               authExchange.setState(AuthExchange.State.FAILURE);
/* 242 */               return false;
/*     */             } 
/* 244 */             authExchange.setState(AuthExchange.State.HANDSHAKE);
/* 245 */             return true;
/*     */           } 
/* 247 */           authExchange.reset();
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 252 */     List<AuthScheme> preferredSchemes = authStrategy.select(challengeType, challengeMap, context);
/* 253 */     CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
/* 254 */     if (credsProvider == null) {
/* 255 */       if (LOG.isDebugEnabled()) {
/* 256 */         LOG.debug("{} Credentials provider not set in the context", exchangeId);
/*     */       }
/* 258 */       return false;
/*     */     } 
/*     */     
/* 261 */     Queue<AuthScheme> authOptions = new LinkedList<>();
/* 262 */     if (LOG.isDebugEnabled()) {
/* 263 */       LOG.debug("{} Selecting authentication options", exchangeId);
/*     */     }
/* 265 */     for (AuthScheme authScheme1 : preferredSchemes) {
/*     */       try {
/* 267 */         String schemeName = authScheme1.getName();
/* 268 */         AuthChallenge challenge = challengeMap.get(schemeName.toLowerCase(Locale.ROOT));
/* 269 */         authScheme1.processChallenge(challenge, context);
/* 270 */         if (authScheme1.isResponseReady(host, credsProvider, context)) {
/* 271 */           authOptions.add(authScheme1);
/*     */         }
/* 273 */       } catch (AuthenticationException|MalformedChallengeException ex) {
/* 274 */         if (LOG.isWarnEnabled()) {
/* 275 */           LOG.warn(ex.getMessage());
/*     */         }
/*     */       } 
/*     */     } 
/* 279 */     if (!authOptions.isEmpty()) {
/* 280 */       if (LOG.isDebugEnabled()) {
/* 281 */         LOG.debug("{} Selected authentication options: {}", exchangeId, authOptions);
/*     */       }
/* 283 */       authExchange.reset();
/* 284 */       authExchange.setState(AuthExchange.State.CHALLENGED);
/* 285 */       authExchange.setOptions(authOptions);
/* 286 */       return true;
/*     */     } 
/* 288 */     return false;
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
/*     */   public void addAuthResponse(HttpHost host, ChallengeType challengeType, HttpRequest request, AuthExchange authExchange, HttpContext context) {
/*     */     Queue<AuthScheme> authOptions;
/* 307 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 308 */     String exchangeId = clientContext.getExchangeId();
/* 309 */     AuthScheme authScheme = authExchange.getAuthScheme();
/* 310 */     switch (authExchange.getState()) {
/*     */       case FAILURE:
/*     */         return;
/*     */       case SUCCESS:
/* 314 */         Asserts.notNull(authScheme, "AuthScheme");
/* 315 */         if (authScheme.isConnectionBased()) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */       case HANDSHAKE:
/* 320 */         Asserts.notNull(authScheme, "AuthScheme");
/*     */         break;
/*     */       case CHALLENGED:
/* 323 */         authOptions = authExchange.getAuthOptions();
/* 324 */         if (authOptions != null) {
/* 325 */           while (!authOptions.isEmpty()) {
/* 326 */             authScheme = authOptions.remove();
/* 327 */             authExchange.select(authScheme);
/* 328 */             if (LOG.isDebugEnabled()) {
/* 329 */               LOG.debug("{} Generating response to an authentication challenge using {} scheme", exchangeId, authScheme
/* 330 */                   .getName());
/*     */             }
/*     */             try {
/* 333 */               String authResponse = authScheme.generateAuthResponse(host, request, context);
/* 334 */               BasicHeader basicHeader = new BasicHeader((challengeType == ChallengeType.TARGET) ? "Authorization" : "Proxy-Authorization", authResponse);
/*     */ 
/*     */               
/* 337 */               request.addHeader((Header)basicHeader);
/*     */               break;
/* 339 */             } catch (AuthenticationException ex) {
/* 340 */               if (LOG.isWarnEnabled()) {
/* 341 */                 LOG.warn("{} {} authentication error: {}", new Object[] { exchangeId, authScheme, ex.getMessage() });
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return;
/*     */         } 
/* 347 */         Asserts.notNull(authScheme, "AuthScheme");
/*     */         break;
/*     */     } 
/* 350 */     if (authScheme != null)
/*     */       try {
/* 352 */         String authResponse = authScheme.generateAuthResponse(host, request, context);
/* 353 */         BasicHeader basicHeader = new BasicHeader((challengeType == ChallengeType.TARGET) ? "Authorization" : "Proxy-Authorization", authResponse);
/*     */ 
/*     */         
/* 356 */         request.addHeader((Header)basicHeader);
/* 357 */       } catch (AuthenticationException ex) {
/* 358 */         if (LOG.isErrorEnabled())
/* 359 */           LOG.error("{} {} authentication error: {}", new Object[] { exchangeId, authScheme, ex.getMessage() }); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/HttpAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */