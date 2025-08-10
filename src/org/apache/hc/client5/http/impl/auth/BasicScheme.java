/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.security.Principal;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.AuthStateCacheable;
/*     */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.MalformedChallengeException;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.utils.Base64;
/*     */ import org.apache.hc.client5.http.utils.ByteArrayBuilder;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.NameValuePair;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AuthStateCacheable
/*     */ public class BasicScheme
/*     */   implements AuthScheme, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1931571557597830536L;
/*  72 */   private static final Logger LOG = LoggerFactory.getLogger(BasicScheme.class);
/*     */   
/*     */   private final Map<String, String> paramMap;
/*     */   
/*     */   private transient Charset defaultCharset;
/*     */   
/*     */   private transient ByteArrayBuilder buffer;
/*     */   
/*     */   private transient Base64 base64codec;
/*     */   
/*     */   private boolean complete;
/*     */   private String username;
/*     */   private char[] password;
/*     */   
/*     */   public BasicScheme(Charset charset) {
/*  87 */     this.paramMap = new HashMap<>();
/*  88 */     this.defaultCharset = (charset != null) ? charset : StandardCharsets.US_ASCII;
/*  89 */     this.complete = false;
/*     */   }
/*     */   
/*     */   public BasicScheme() {
/*  93 */     this(StandardCharsets.US_ASCII);
/*     */   }
/*     */   
/*     */   private void applyCredentials(Credentials credentials) {
/*  97 */     this.username = credentials.getUserPrincipal().getName();
/*  98 */     this.password = credentials.getPassword();
/*     */   }
/*     */   
/*     */   private void clearCredentials() {
/* 102 */     this.username = null;
/* 103 */     this.password = null;
/*     */   }
/*     */   
/*     */   public void initPreemptive(Credentials credentials) {
/* 107 */     if (credentials != null) {
/* 108 */       applyCredentials(credentials);
/*     */     } else {
/* 110 */       clearCredentials();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 116 */     return "Basic";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 126 */     return this.paramMap.get("realm");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processChallenge(AuthChallenge authChallenge, HttpContext context) throws MalformedChallengeException {
/* 133 */     this.paramMap.clear();
/* 134 */     List<NameValuePair> params = authChallenge.getParams();
/* 135 */     if (params != null) {
/* 136 */       for (NameValuePair param : params) {
/* 137 */         this.paramMap.put(param.getName().toLowerCase(Locale.ROOT), param.getValue());
/*     */       }
/*     */     }
/* 140 */     this.complete = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChallengeComplete() {
/* 145 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseReady(HttpHost host, CredentialsProvider credentialsProvider, HttpContext context) throws AuthenticationException {
/* 154 */     Args.notNull(host, "Auth host");
/* 155 */     Args.notNull(credentialsProvider, "CredentialsProvider");
/*     */     
/* 157 */     AuthScope authScope = new AuthScope(host, getRealm(), getName());
/* 158 */     Credentials credentials = credentialsProvider.getCredentials(authScope, context);
/*     */     
/* 160 */     if (credentials != null) {
/* 161 */       applyCredentials(credentials);
/* 162 */       return true;
/*     */     } 
/*     */     
/* 165 */     if (LOG.isDebugEnabled()) {
/* 166 */       HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 167 */       String exchangeId = clientContext.getExchangeId();
/* 168 */       LOG.debug("{} No credentials found for auth scope [{}]", exchangeId, authScope);
/*     */     } 
/* 170 */     clearCredentials();
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 176 */     return null;
/*     */   }
/*     */   
/*     */   private void validateUsername() throws AuthenticationException {
/* 180 */     if (this.username == null) {
/* 181 */       throw new AuthenticationException("User credentials not set");
/*     */     }
/* 183 */     for (int i = 0; i < this.username.length(); i++) {
/* 184 */       char ch = this.username.charAt(i);
/* 185 */       if (Character.isISOControl(ch)) {
/* 186 */         throw new AuthenticationException("Username must not contain any control characters");
/*     */       }
/* 188 */       if (ch == ':') {
/* 189 */         throw new AuthenticationException("Username contains a colon character and is invalid");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateAuthResponse(HttpHost host, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 199 */     validateUsername();
/* 200 */     if (this.buffer == null) {
/* 201 */       this.buffer = new ByteArrayBuilder(64);
/*     */     } else {
/* 203 */       this.buffer.reset();
/*     */     } 
/* 205 */     Charset charset = AuthSchemeSupport.parseCharset(this.paramMap.get("charset"), this.defaultCharset);
/* 206 */     this.buffer.charset(charset);
/* 207 */     this.buffer.append(this.username).append(":").append(this.password);
/* 208 */     if (this.base64codec == null) {
/* 209 */       this.base64codec = new Base64();
/*     */     }
/* 211 */     byte[] encodedCreds = this.base64codec.encode(this.buffer.toByteArray());
/* 212 */     this.buffer.reset();
/* 213 */     return "Basic " + new String(encodedCreds, 0, encodedCreds.length, StandardCharsets.US_ASCII);
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 217 */     out.defaultWriteObject();
/* 218 */     out.writeUTF(this.defaultCharset.name());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 223 */     in.defaultReadObject();
/*     */     try {
/* 225 */       this.defaultCharset = Charset.forName(in.readUTF());
/* 226 */     } catch (UnsupportedCharsetException ex) {
/* 227 */       this.defaultCharset = StandardCharsets.US_ASCII;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObjectNoData() {}
/*     */ 
/*     */   
/*     */   public String toString() {
/* 236 */     return getName() + this.paramMap;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/BasicScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */