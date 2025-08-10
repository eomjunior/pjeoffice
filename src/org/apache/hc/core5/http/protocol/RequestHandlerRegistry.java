/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.MisdirectedRequestException;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RequestHandlerRegistry<T>
/*     */   implements HttpRequestMapper<T>
/*     */ {
/*     */   private static final String LOCALHOST = "localhost";
/*     */   private static final String IP_127_0_0_1 = "127.0.0.1";
/*     */   private final String canonicalHostName;
/*     */   private final Supplier<LookupRegistry<T>> registrySupplier;
/*     */   private final LookupRegistry<T> primary;
/*     */   private final ConcurrentMap<String, LookupRegistry<T>> virtualMap;
/*     */   
/*     */   public RequestHandlerRegistry(String canonicalHostName, Supplier<LookupRegistry<T>> registrySupplier) {
/*  62 */     this.canonicalHostName = TextUtils.toLowerCase((String)Args.notNull(canonicalHostName, "Canonical hostname"));
/*  63 */     this.registrySupplier = (registrySupplier != null) ? registrySupplier : UriPatternMatcher::new;
/*  64 */     this.primary = (LookupRegistry<T>)this.registrySupplier.get();
/*  65 */     this.virtualMap = new ConcurrentHashMap<>();
/*     */   }
/*     */   
/*     */   public RequestHandlerRegistry(String canonicalHostName, UriPatternType patternType) {
/*  69 */     this(canonicalHostName, () -> UriPatternType.newMatcher(patternType));
/*     */   }
/*     */   
/*     */   public RequestHandlerRegistry(UriPatternType patternType) {
/*  73 */     this("localhost", patternType);
/*     */   }
/*     */   
/*     */   public RequestHandlerRegistry() {
/*  77 */     this("localhost", UriPatternType.URI_PATTERN);
/*     */   }
/*     */   
/*     */   private LookupRegistry<T> getPatternMatcher(String hostname) {
/*  81 */     if (hostname == null || hostname
/*  82 */       .equals(this.canonicalHostName) || hostname.equals("localhost") || hostname.equals("127.0.0.1")) {
/*  83 */       return this.primary;
/*     */     }
/*  85 */     return this.virtualMap.get(hostname);
/*     */   }
/*     */ 
/*     */   
/*     */   public T resolve(HttpRequest request, HttpContext context) throws MisdirectedRequestException {
/*  90 */     URIAuthority authority = request.getAuthority();
/*  91 */     String key = (authority != null) ? TextUtils.toLowerCase(authority.getHostName()) : null;
/*  92 */     LookupRegistry<T> patternMatcher = getPatternMatcher(key);
/*  93 */     if (patternMatcher == null) {
/*  94 */       throw new MisdirectedRequestException("Not authoritative");
/*     */     }
/*  96 */     String path = request.getPath();
/*  97 */     int i = path.indexOf('?');
/*  98 */     if (i != -1) {
/*  99 */       path = path.substring(0, i);
/*     */     }
/* 101 */     return patternMatcher.lookup(path);
/*     */   }
/*     */   
/*     */   public void register(String hostname, String uriPattern, T object) {
/* 105 */     Args.notBlank(uriPattern, "URI pattern");
/* 106 */     if (object == null) {
/*     */       return;
/*     */     }
/* 109 */     String key = TextUtils.toLowerCase(hostname);
/* 110 */     if (hostname == null || hostname.equals(this.canonicalHostName) || hostname.equals("localhost")) {
/* 111 */       this.primary.register(uriPattern, object);
/*     */     } else {
/* 113 */       LookupRegistry<T> patternMatcher = this.virtualMap.get(key);
/* 114 */       if (patternMatcher == null) {
/* 115 */         LookupRegistry<T> newPatternMatcher = (LookupRegistry<T>)this.registrySupplier.get();
/* 116 */         patternMatcher = this.virtualMap.putIfAbsent(key, newPatternMatcher);
/* 117 */         if (patternMatcher == null) {
/* 118 */           patternMatcher = newPatternMatcher;
/*     */         }
/*     */       } 
/* 121 */       patternMatcher.register(uriPattern, object);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestHandlerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */