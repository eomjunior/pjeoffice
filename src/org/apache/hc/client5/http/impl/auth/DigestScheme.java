/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.Principal;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.MalformedChallengeException;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.utils.ByteArrayBuilder;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DigestScheme
/*     */   implements AuthScheme, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3883908186234566916L;
/*  90 */   private static final Logger LOG = LoggerFactory.getLogger(DigestScheme.class);
/*     */   
/*     */   private transient Charset defaultCharset;
/*     */   private final Map<String, String> paramMap;
/*     */   private boolean complete;
/*     */   private transient ByteArrayBuilder buffer;
/*     */   private String lastNonce;
/*     */   private long nounceCount;
/*  98 */   private static final char[] HEXADECIMAL = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */   private String cnonce;
/*     */   private byte[] a1;
/*     */   private byte[] a2;
/*     */   private String username;
/*     */   private char[] password;
/*     */   
/*     */   private enum QualityOfProtection
/*     */   {
/* 107 */     UNKNOWN, MISSING, AUTH_INT, AUTH;
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
/*     */   public DigestScheme() {
/* 125 */     this(StandardCharsets.ISO_8859_1);
/*     */   }
/*     */   
/*     */   public DigestScheme(Charset charset) {
/* 129 */     this.defaultCharset = (charset != null) ? charset : StandardCharsets.ISO_8859_1;
/* 130 */     this.paramMap = new HashMap<>();
/* 131 */     this.complete = false;
/*     */   }
/*     */   
/*     */   public void initPreemptive(Credentials credentials, String cnonce, String realm) {
/* 135 */     Args.notNull(credentials, "Credentials");
/* 136 */     this.username = credentials.getUserPrincipal().getName();
/* 137 */     this.password = credentials.getPassword();
/* 138 */     this.paramMap.put("cnonce", cnonce);
/* 139 */     this.paramMap.put("realm", realm);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 144 */     return "Digest";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 154 */     return this.paramMap.get("realm");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processChallenge(AuthChallenge authChallenge, HttpContext context) throws MalformedChallengeException {
/* 161 */     Args.notNull(authChallenge, "AuthChallenge");
/* 162 */     this.paramMap.clear();
/* 163 */     List<NameValuePair> params = authChallenge.getParams();
/* 164 */     if (params != null) {
/* 165 */       for (NameValuePair param : params) {
/* 166 */         this.paramMap.put(param.getName().toLowerCase(Locale.ROOT), param.getValue());
/*     */       }
/*     */     }
/* 169 */     if (this.paramMap.isEmpty()) {
/* 170 */       throw new MalformedChallengeException("Missing digest auth parameters");
/*     */     }
/* 172 */     this.complete = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChallengeComplete() {
/* 177 */     String s = this.paramMap.get("stale");
/* 178 */     return (!"true".equalsIgnoreCase(s) && this.complete);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResponseReady(HttpHost host, CredentialsProvider credentialsProvider, HttpContext context) throws AuthenticationException {
/* 187 */     Args.notNull(host, "Auth host");
/* 188 */     Args.notNull(credentialsProvider, "CredentialsProvider");
/*     */     
/* 190 */     AuthScope authScope = new AuthScope(host, getRealm(), getName());
/* 191 */     Credentials credentials = credentialsProvider.getCredentials(authScope, context);
/*     */     
/* 193 */     if (credentials != null) {
/* 194 */       this.username = credentials.getUserPrincipal().getName();
/* 195 */       this.password = credentials.getPassword();
/* 196 */       return true;
/*     */     } 
/*     */     
/* 199 */     if (LOG.isDebugEnabled()) {
/* 200 */       HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 201 */       String exchangeId = clientContext.getExchangeId();
/* 202 */       LOG.debug("{} No credentials found for auth scope [{}]", exchangeId, authScope);
/*     */     } 
/* 204 */     this.username = null;
/* 205 */     this.password = null;
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getPrincipal() {
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateAuthResponse(HttpHost host, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 220 */     Args.notNull(request, "HTTP request");
/* 221 */     if (this.paramMap.get("realm") == null) {
/* 222 */       throw new AuthenticationException("missing realm");
/*     */     }
/* 224 */     if (this.paramMap.get("nonce") == null) {
/* 225 */       throw new AuthenticationException("missing nonce");
/*     */     }
/* 227 */     return createDigestResponse(request);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MessageDigest createMessageDigest(String digAlg) throws UnsupportedDigestAlgorithmException {
/*     */     try {
/* 233 */       return MessageDigest.getInstance(digAlg);
/* 234 */     } catch (Exception e) {
/* 235 */       throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String createDigestResponse(HttpRequest request) throws AuthenticationException {
/*     */     MessageDigest digester;
/* 243 */     String uri = request.getRequestUri();
/* 244 */     String method = request.getMethod();
/* 245 */     String realm = this.paramMap.get("realm");
/* 246 */     String nonce = this.paramMap.get("nonce");
/* 247 */     String opaque = this.paramMap.get("opaque");
/* 248 */     String algorithm = this.paramMap.get("algorithm");
/*     */     
/* 250 */     if (algorithm == null) {
/* 251 */       algorithm = "MD5";
/*     */     }
/*     */     
/* 254 */     Set<String> qopset = new HashSet<>(8);
/* 255 */     QualityOfProtection qop = QualityOfProtection.UNKNOWN;
/* 256 */     String qoplist = this.paramMap.get("qop");
/* 257 */     if (qoplist != null) {
/* 258 */       StringTokenizer tok = new StringTokenizer(qoplist, ",");
/* 259 */       while (tok.hasMoreTokens()) {
/* 260 */         String variant = tok.nextToken().trim();
/* 261 */         qopset.add(variant.toLowerCase(Locale.ROOT));
/*     */       } 
/* 263 */       HttpEntity entity = (request instanceof ClassicHttpRequest) ? ((ClassicHttpRequest)request).getEntity() : null;
/* 264 */       if (entity != null && qopset.contains("auth-int")) {
/* 265 */         qop = QualityOfProtection.AUTH_INT;
/* 266 */       } else if (qopset.contains("auth")) {
/* 267 */         qop = QualityOfProtection.AUTH;
/* 268 */       } else if (qopset.contains("auth-int")) {
/* 269 */         qop = QualityOfProtection.AUTH_INT;
/*     */       } 
/*     */     } else {
/* 272 */       qop = QualityOfProtection.MISSING;
/*     */     } 
/*     */     
/* 275 */     if (qop == QualityOfProtection.UNKNOWN) {
/* 276 */       throw new AuthenticationException("None of the qop methods is supported: " + qoplist);
/*     */     }
/*     */     
/* 279 */     Charset charset = AuthSchemeSupport.parseCharset(this.paramMap.get("charset"), this.defaultCharset);
/* 280 */     String digAlg = algorithm;
/* 281 */     if (digAlg.equalsIgnoreCase("MD5-sess")) {
/* 282 */       digAlg = "MD5";
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 287 */       digester = createMessageDigest(digAlg);
/* 288 */     } catch (UnsupportedDigestAlgorithmException ex) {
/* 289 */       throw new AuthenticationException("Unsupported digest algorithm: " + digAlg);
/*     */     } 
/*     */     
/* 292 */     if (nonce.equals(this.lastNonce)) {
/* 293 */       this.nounceCount++;
/*     */     } else {
/* 295 */       this.nounceCount = 1L;
/* 296 */       this.cnonce = null;
/* 297 */       this.lastNonce = nonce;
/*     */     } 
/*     */     
/* 300 */     StringBuilder sb = new StringBuilder(8);
/* 301 */     try (Formatter formatter = new Formatter(sb, Locale.ROOT)) {
/* 302 */       formatter.format("%08x", new Object[] { Long.valueOf(this.nounceCount) });
/*     */     } 
/* 304 */     String nc = sb.toString();
/*     */     
/* 306 */     if (this.cnonce == null) {
/* 307 */       this.cnonce = formatHex(createCnonce());
/*     */     }
/*     */     
/* 310 */     if (this.buffer == null) {
/* 311 */       this.buffer = new ByteArrayBuilder(128);
/*     */     } else {
/* 313 */       this.buffer.reset();
/*     */     } 
/* 315 */     this.buffer.charset(charset);
/*     */     
/* 317 */     this.a1 = null;
/* 318 */     this.a2 = null;
/*     */     
/* 320 */     if (algorithm.equalsIgnoreCase("MD5-sess")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 326 */       this.buffer.append(this.username).append(":").append(realm).append(":").append(this.password);
/* 327 */       String checksum = formatHex(digester.digest(this.buffer.toByteArray()));
/* 328 */       this.buffer.reset();
/* 329 */       this.buffer.append(checksum).append(":").append(nonce).append(":").append(this.cnonce);
/*     */     } else {
/*     */       
/* 332 */       this.buffer.append(this.username).append(":").append(realm).append(":").append(this.password);
/*     */     } 
/* 334 */     this.a1 = this.buffer.toByteArray();
/*     */     
/* 336 */     String hasha1 = formatHex(digester.digest(this.a1));
/* 337 */     this.buffer.reset();
/*     */     
/* 339 */     if (qop == QualityOfProtection.AUTH) {
/*     */       
/* 341 */       this.a2 = this.buffer.append(method).append(":").append(uri).toByteArray();
/* 342 */     } else if (qop == QualityOfProtection.AUTH_INT) {
/*     */       
/* 344 */       HttpEntity entity = (request instanceof ClassicHttpRequest) ? ((ClassicHttpRequest)request).getEntity() : null;
/* 345 */       if (entity != null && !entity.isRepeatable()) {
/*     */         
/* 347 */         if (qopset.contains("auth")) {
/* 348 */           qop = QualityOfProtection.AUTH;
/* 349 */           this.a2 = this.buffer.append(method).append(":").append(uri).toByteArray();
/*     */         } else {
/* 351 */           throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
/*     */         } 
/*     */       } else {
/*     */         
/* 355 */         HttpEntityDigester entityDigester = new HttpEntityDigester(digester);
/*     */         try {
/* 357 */           if (entity != null) {
/* 358 */             entity.writeTo(entityDigester);
/*     */           }
/* 360 */           entityDigester.close();
/* 361 */         } catch (IOException ex) {
/* 362 */           throw new AuthenticationException("I/O error reading entity content", ex);
/*     */         } 
/* 364 */         this
/* 365 */           .a2 = this.buffer.append(method).append(":").append(uri).append(":").append(formatHex(entityDigester.getDigest())).toByteArray();
/*     */       } 
/*     */     } else {
/* 368 */       this.a2 = this.buffer.append(method).append(":").append(uri).toByteArray();
/*     */     } 
/*     */     
/* 371 */     String hasha2 = formatHex(digester.digest(this.a2));
/* 372 */     this.buffer.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     if (qop == QualityOfProtection.MISSING) {
/* 378 */       this.buffer.append(hasha1).append(":").append(nonce).append(":").append(hasha2);
/*     */     } else {
/* 380 */       this.buffer.append(hasha1).append(":").append(nonce).append(":").append(nc).append(":")
/* 381 */         .append(this.cnonce).append(":").append((qop == QualityOfProtection.AUTH_INT) ? "auth-int" : "auth")
/* 382 */         .append(":").append(hasha2);
/*     */     } 
/* 384 */     byte[] digestInput = this.buffer.toByteArray();
/* 385 */     this.buffer.reset();
/*     */     
/* 387 */     String digest = formatHex(digester.digest(digestInput));
/*     */     
/* 389 */     CharArrayBuffer buffer = new CharArrayBuffer(128);
/* 390 */     buffer.append("Digest ");
/*     */     
/* 392 */     List<BasicNameValuePair> params = new ArrayList<>(20);
/* 393 */     params.add(new BasicNameValuePair("username", this.username));
/* 394 */     params.add(new BasicNameValuePair("realm", realm));
/* 395 */     params.add(new BasicNameValuePair("nonce", nonce));
/* 396 */     params.add(new BasicNameValuePair("uri", uri));
/* 397 */     params.add(new BasicNameValuePair("response", digest));
/*     */     
/* 399 */     if (qop != QualityOfProtection.MISSING) {
/* 400 */       params.add(new BasicNameValuePair("qop", (qop == QualityOfProtection.AUTH_INT) ? "auth-int" : "auth"));
/* 401 */       params.add(new BasicNameValuePair("nc", nc));
/* 402 */       params.add(new BasicNameValuePair("cnonce", this.cnonce));
/*     */     } 
/*     */     
/* 405 */     params.add(new BasicNameValuePair("algorithm", algorithm));
/* 406 */     if (opaque != null) {
/* 407 */       params.add(new BasicNameValuePair("opaque", opaque));
/*     */     }
/*     */     
/* 410 */     for (int i = 0; i < params.size(); i++) {
/* 411 */       BasicNameValuePair param = params.get(i);
/* 412 */       if (i > 0) {
/* 413 */         buffer.append(", ");
/*     */       }
/* 415 */       String name = param.getName();
/*     */       
/* 417 */       boolean noQuotes = ("nc".equals(name) || "qop".equals(name) || "algorithm".equals(name));
/* 418 */       BasicHeaderValueFormatter.INSTANCE.formatNameValuePair(buffer, (NameValuePair)param, !noQuotes);
/*     */     } 
/* 420 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   @Internal
/*     */   public String getNonce() {
/* 425 */     return this.lastNonce;
/*     */   }
/*     */   
/*     */   @Internal
/*     */   public long getNounceCount() {
/* 430 */     return this.nounceCount;
/*     */   }
/*     */   
/*     */   @Internal
/*     */   public String getCnonce() {
/* 435 */     return this.cnonce;
/*     */   }
/*     */   
/*     */   String getA1() {
/* 439 */     return (this.a1 != null) ? new String(this.a1, StandardCharsets.US_ASCII) : null;
/*     */   }
/*     */   
/*     */   String getA2() {
/* 443 */     return (this.a2 != null) ? new String(this.a2, StandardCharsets.US_ASCII) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String formatHex(byte[] binaryData) {
/* 454 */     int n = binaryData.length;
/* 455 */     char[] buffer = new char[n * 2];
/* 456 */     for (int i = 0; i < n; i++) {
/* 457 */       int low = binaryData[i] & 0xF;
/* 458 */       int high = (binaryData[i] & 0xF0) >> 4;
/* 459 */       buffer[i * 2] = HEXADECIMAL[high];
/* 460 */       buffer[i * 2 + 1] = HEXADECIMAL[low];
/*     */     } 
/*     */     
/* 463 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] createCnonce() {
/* 472 */     SecureRandom rnd = new SecureRandom();
/* 473 */     byte[] tmp = new byte[8];
/* 474 */     rnd.nextBytes(tmp);
/* 475 */     return tmp;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 479 */     out.defaultWriteObject();
/* 480 */     out.writeUTF(this.defaultCharset.name());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 484 */     in.defaultReadObject();
/* 485 */     this.defaultCharset = Charset.forName(in.readUTF());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 490 */     return getName() + this.paramMap;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/DigestScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */