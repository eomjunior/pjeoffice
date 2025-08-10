/*     */ package org.apache.hc.client5.http.auth;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class AuthScope
/*     */ {
/*     */   private final String protocol;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final String realm;
/*     */   private final String schemeName;
/*     */   
/*     */   public AuthScope(String protocol, String host, int port, String realm, String schemeName) {
/*  75 */     this.protocol = (protocol != null) ? protocol.toLowerCase(Locale.ROOT) : null;
/*  76 */     this.host = (host != null) ? host.toLowerCase(Locale.ROOT) : null;
/*  77 */     this.port = (port >= 0) ? port : -1;
/*  78 */     this.realm = realm;
/*  79 */     this.schemeName = schemeName;
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
/*     */   public AuthScope(HttpHost origin, String realm, String schemeName) {
/*  97 */     Args.notNull(origin, "Host");
/*  98 */     this.protocol = origin.getSchemeName().toLowerCase(Locale.ROOT);
/*  99 */     this.host = origin.getHostName().toLowerCase(Locale.ROOT);
/* 100 */     this.port = (origin.getPort() >= 0) ? origin.getPort() : -1;
/* 101 */     this.realm = realm;
/* 102 */     this.schemeName = schemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(HttpHost origin) {
/* 113 */     this(origin, null, null);
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
/*     */   public AuthScope(String host, int port) {
/* 125 */     this(null, host, port, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScope(AuthScope authScope) {
/* 133 */     Args.notNull(authScope, "Scope");
/* 134 */     this.protocol = authScope.getProtocol();
/* 135 */     this.host = authScope.getHost();
/* 136 */     this.port = authScope.getPort();
/* 137 */     this.realm = authScope.getRealm();
/* 138 */     this.schemeName = authScope.getSchemeName();
/*     */   }
/*     */   
/*     */   public String getProtocol() {
/* 142 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public String getHost() {
/* 146 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 150 */     return this.port;
/*     */   }
/*     */   
/*     */   public String getRealm() {
/* 154 */     return this.realm;
/*     */   }
/*     */   
/*     */   public String getSchemeName() {
/* 158 */     return this.schemeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int match(AuthScope that) {
/* 169 */     int factor = 0;
/* 170 */     if (Objects.equals(toNullSafeLowerCase(this.schemeName), 
/* 171 */         toNullSafeLowerCase(that.schemeName))) {
/* 172 */       factor++;
/*     */     }
/* 174 */     else if (this.schemeName != null && that.schemeName != null) {
/* 175 */       return -1;
/*     */     } 
/*     */     
/* 178 */     if (Objects.equals(this.realm, that.realm)) {
/* 179 */       factor += 2;
/*     */     }
/* 181 */     else if (this.realm != null && that.realm != null) {
/* 182 */       return -1;
/*     */     } 
/*     */     
/* 185 */     if (this.port == that.port) {
/* 186 */       factor += 4;
/*     */     }
/* 188 */     else if (this.port != -1 && that.port != -1) {
/* 189 */       return -1;
/*     */     } 
/*     */     
/* 192 */     if (Objects.equals(this.protocol, that.protocol)) {
/* 193 */       factor += 8;
/*     */     }
/* 195 */     else if (this.protocol != null && that.protocol != null) {
/* 196 */       return -1;
/*     */     } 
/*     */     
/* 199 */     if (Objects.equals(this.host, that.host)) {
/* 200 */       factor += 16;
/*     */     }
/* 202 */     else if (this.host != null && that.host != null) {
/* 203 */       return -1;
/*     */     } 
/*     */     
/* 206 */     return factor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 211 */     if (this == obj) {
/* 212 */       return true;
/*     */     }
/* 214 */     if (obj instanceof AuthScope) {
/* 215 */       AuthScope that = (AuthScope)obj;
/* 216 */       return (Objects.equals(this.protocol, that.protocol) && 
/* 217 */         Objects.equals(this.host, that.host) && this.port == that.port && 
/*     */         
/* 219 */         Objects.equals(this.realm, that.realm) && 
/* 220 */         Objects.equals(toNullSafeLowerCase(this.schemeName), 
/* 221 */           toNullSafeLowerCase(that.schemeName)));
/*     */     } 
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 228 */     int hash = 17;
/* 229 */     hash = LangUtils.hashCode(hash, this.protocol);
/* 230 */     hash = LangUtils.hashCode(hash, this.host);
/* 231 */     hash = LangUtils.hashCode(hash, this.port);
/* 232 */     hash = LangUtils.hashCode(hash, this.realm);
/* 233 */     hash = LangUtils.hashCode(hash, toNullSafeLowerCase(this.schemeName));
/* 234 */     return hash;
/*     */   }
/*     */   
/*     */   private String toNullSafeLowerCase(String str) {
/* 238 */     return (str != null) ? str.toLowerCase(Locale.ROOT) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     StringBuilder buffer = new StringBuilder();
/* 244 */     if (this.schemeName != null) {
/* 245 */       buffer.append(this.schemeName);
/*     */     } else {
/* 247 */       buffer.append("<any auth scheme>");
/*     */     } 
/* 249 */     buffer.append(' ');
/* 250 */     if (this.realm != null) {
/* 251 */       buffer.append('\'');
/* 252 */       buffer.append(this.realm);
/* 253 */       buffer.append('\'');
/*     */     } else {
/* 255 */       buffer.append("<any realm>");
/*     */     } 
/* 257 */     buffer.append(' ');
/* 258 */     if (this.protocol != null) {
/* 259 */       buffer.append(this.protocol);
/*     */     } else {
/* 261 */       buffer.append("<any protocol>");
/*     */     } 
/* 263 */     buffer.append("://");
/* 264 */     if (this.host != null) {
/* 265 */       buffer.append(this.host);
/*     */     } else {
/* 267 */       buffer.append("<any host>");
/*     */     } 
/* 269 */     buffer.append(':');
/* 270 */     if (this.port >= 0) {
/* 271 */       buffer.append(this.port);
/*     */     } else {
/* 273 */       buffer.append("<any port>");
/*     */     } 
/* 275 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */