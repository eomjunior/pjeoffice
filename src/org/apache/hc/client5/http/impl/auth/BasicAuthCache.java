/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class BasicAuthCache
/*     */   implements AuthCache
/*     */ {
/*  66 */   private static final Logger LOG = LoggerFactory.getLogger(BasicAuthCache.class);
/*     */   private final Map<Key, byte[]> map;
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   static class Key
/*     */   {
/*     */     final String scheme;
/*     */     final String host;
/*     */     
/*     */     Key(String scheme, String host, int port, String pathPrefix) {
/*  76 */       Args.notBlank(scheme, "Scheme");
/*  77 */       Args.notBlank(host, "Scheme");
/*  78 */       this.scheme = scheme.toLowerCase(Locale.ROOT);
/*  79 */       this.host = host.toLowerCase(Locale.ROOT);
/*  80 */       this.port = port;
/*  81 */       this.pathPrefix = pathPrefix;
/*     */     }
/*     */     final int port; final String pathPrefix;
/*     */     
/*     */     public boolean equals(Object obj) {
/*  86 */       if (this == obj) {
/*  87 */         return true;
/*     */       }
/*  89 */       if (obj instanceof Key) {
/*  90 */         Key that = (Key)obj;
/*  91 */         return (this.scheme.equals(that.scheme) && this.host
/*  92 */           .equals(that.host) && this.port == that.port && 
/*     */           
/*  94 */           Objects.equals(this.pathPrefix, that.pathPrefix));
/*     */       } 
/*  96 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 101 */       int hash = 17;
/* 102 */       hash = LangUtils.hashCode(hash, this.scheme);
/* 103 */       hash = LangUtils.hashCode(hash, this.host);
/* 104 */       hash = LangUtils.hashCode(hash, this.port);
/* 105 */       hash = LangUtils.hashCode(hash, this.pathPrefix);
/* 106 */       return hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 111 */       StringBuilder buf = new StringBuilder();
/* 112 */       buf.append(this.scheme).append("://").append(this.host);
/* 113 */       if (this.port >= 0) {
/* 114 */         buf.append(":").append(this.port);
/*     */       }
/* 116 */       if (this.pathPrefix != null) {
/* 117 */         if (!this.pathPrefix.startsWith("/")) {
/* 118 */           buf.append("/");
/*     */         }
/* 120 */         buf.append(this.pathPrefix);
/*     */       } 
/* 122 */       return buf.toString();
/*     */     }
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
/*     */   public BasicAuthCache(SchemePortResolver schemePortResolver) {
/* 136 */     this.map = (Map)new ConcurrentHashMap<>();
/* 137 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*     */   }
/*     */   
/*     */   public BasicAuthCache() {
/* 141 */     this(null);
/*     */   }
/*     */   
/*     */   private Key key(String scheme, NamedEndpoint authority, String pathPrefix) {
/* 145 */     return new Key(scheme, authority.getHostName(), this.schemePortResolver.resolve(scheme, authority), pathPrefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(HttpHost host, AuthScheme authScheme) {
/* 150 */     put(host, null, authScheme);
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthScheme get(HttpHost host) {
/* 155 */     return get(host, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(HttpHost host) {
/* 160 */     remove(host, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(HttpHost host, String pathPrefix, AuthScheme authScheme) {
/* 165 */     Args.notNull(host, "HTTP host");
/* 166 */     if (authScheme == null) {
/*     */       return;
/*     */     }
/* 169 */     if (authScheme instanceof java.io.Serializable) {
/*     */       try {
/* 171 */         ByteArrayOutputStream buf = new ByteArrayOutputStream();
/* 172 */         try (ObjectOutputStream out = new ObjectOutputStream(buf)) {
/* 173 */           out.writeObject(authScheme);
/*     */         } 
/* 175 */         this.map.put(key(host.getSchemeName(), (NamedEndpoint)host, pathPrefix), buf.toByteArray());
/* 176 */       } catch (IOException ex) {
/* 177 */         if (LOG.isWarnEnabled()) {
/* 178 */           LOG.warn("Unexpected I/O error while serializing auth scheme", ex);
/*     */         }
/*     */       }
/*     */     
/* 182 */     } else if (LOG.isDebugEnabled()) {
/* 183 */       LOG.debug("Auth scheme {} is not serializable", authScheme.getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScheme get(HttpHost host, String pathPrefix) {
/* 190 */     Args.notNull(host, "HTTP host");
/* 191 */     byte[] bytes = this.map.get(key(host.getSchemeName(), (NamedEndpoint)host, pathPrefix));
/* 192 */     if (bytes != null) {
/*     */       try {
/* 194 */         ByteArrayInputStream buf = new ByteArrayInputStream(bytes);
/* 195 */         try (ObjectInputStream in = new ObjectInputStream(buf)) {
/* 196 */           return (AuthScheme)in.readObject();
/*     */         } 
/* 198 */       } catch (IOException ex) {
/* 199 */         if (LOG.isWarnEnabled()) {
/* 200 */           LOG.warn("Unexpected I/O error while de-serializing auth scheme", ex);
/*     */         }
/* 202 */       } catch (ClassNotFoundException ex) {
/* 203 */         if (LOG.isWarnEnabled()) {
/* 204 */           LOG.warn("Unexpected error while de-serializing auth scheme", ex);
/*     */         }
/*     */       } 
/*     */     }
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(HttpHost host, String pathPrefix) {
/* 213 */     Args.notNull(host, "HTTP host");
/* 214 */     this.map.remove(key(host.getSchemeName(), (NamedEndpoint)host, pathPrefix));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 219 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/BasicAuthCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */