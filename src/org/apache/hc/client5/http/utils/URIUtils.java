/*     */ package org.apache.hc.client5.http.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.net.URIBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URIUtils
/*     */ {
/*     */   @Deprecated
/*     */   public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
/*  74 */     Args.notNull(uri, "URI");
/*  75 */     if (uri.isOpaque()) {
/*  76 */       return uri;
/*     */     }
/*  78 */     URIBuilder uribuilder = new URIBuilder(uri);
/*  79 */     if (target != null) {
/*  80 */       uribuilder.setScheme(target.getSchemeName());
/*  81 */       uribuilder.setHost(target.getHostName());
/*  82 */       uribuilder.setPort(target.getPort());
/*     */     } else {
/*  84 */       uribuilder.setScheme(null);
/*  85 */       uribuilder.setHost((String)null);
/*  86 */       uribuilder.setPort(-1);
/*     */     } 
/*  88 */     if (dropFragment) {
/*  89 */       uribuilder.setFragment(null);
/*     */     }
/*  91 */     List<String> originalPathSegments = uribuilder.getPathSegments();
/*  92 */     List<String> pathSegments = new ArrayList<>(originalPathSegments);
/*  93 */     for (Iterator<String> it = pathSegments.iterator(); it.hasNext(); ) {
/*  94 */       String pathSegment = it.next();
/*  95 */       if (pathSegment.isEmpty() && it.hasNext()) {
/*  96 */         it.remove();
/*     */       }
/*     */     } 
/*  99 */     if (pathSegments.size() != originalPathSegments.size()) {
/* 100 */       uribuilder.setPathSegments(pathSegments);
/*     */     }
/* 102 */     if (pathSegments.isEmpty()) {
/* 103 */       uribuilder.setPathSegments(new String[] { "" });
/*     */     }
/* 105 */     return uribuilder.build();
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
/*     */   @Deprecated
/*     */   public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
/* 119 */     return rewriteURI(uri, target, false);
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
/*     */   @Deprecated
/*     */   public static URI rewriteURI(URI uri) throws URISyntaxException {
/* 137 */     Args.notNull(uri, "URI");
/* 138 */     if (uri.isOpaque()) {
/* 139 */       return uri;
/*     */     }
/* 141 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 142 */     if (uribuilder.getUserInfo() != null) {
/* 143 */       uribuilder.setUserInfo(null);
/*     */     }
/* 145 */     if (uribuilder.isPathEmpty()) {
/* 146 */       uribuilder.setPathSegments(new String[] { "" });
/*     */     }
/* 148 */     if (uribuilder.getHost() != null) {
/* 149 */       uribuilder.setHost(uribuilder.getHost().toLowerCase(Locale.ROOT));
/*     */     }
/* 151 */     uribuilder.setFragment(null);
/* 152 */     return uribuilder.build();
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
/*     */   public static URI resolve(URI baseURI, String reference) {
/* 164 */     return resolve(baseURI, URI.create(reference));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI resolve(URI baseURI, URI reference) {
/*     */     URI resolved;
/* 176 */     Args.notNull(baseURI, "Base URI");
/* 177 */     Args.notNull(reference, "Reference URI");
/* 178 */     String s = reference.toASCIIString();
/* 179 */     if (s.startsWith("?")) {
/* 180 */       String baseUri = baseURI.toASCIIString();
/* 181 */       int i = baseUri.indexOf('?');
/* 182 */       baseUri = (i > -1) ? baseUri.substring(0, i) : baseUri;
/* 183 */       return URI.create(baseUri + s);
/*     */     } 
/* 185 */     boolean emptyReference = s.isEmpty();
/*     */     
/* 187 */     if (emptyReference) {
/* 188 */       resolved = baseURI.resolve(URI.create("#"));
/* 189 */       String resolvedString = resolved.toASCIIString();
/* 190 */       resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf('#')));
/*     */     } else {
/* 192 */       resolved = baseURI.resolve(reference);
/*     */     } 
/*     */     try {
/* 195 */       return normalizeSyntax(resolved);
/* 196 */     } catch (URISyntaxException ex) {
/* 197 */       throw new IllegalArgumentException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static URI normalizeSyntax(URI uri) throws URISyntaxException {
/* 208 */     if (uri.isOpaque() || uri.getAuthority() == null)
/*     */     {
/* 210 */       return uri;
/*     */     }
/* 212 */     URIBuilder builder = new URIBuilder(uri);
/* 213 */     builder.normalizeSyntax();
/* 214 */     if (builder.getScheme() == null) {
/* 215 */       builder.setScheme(URIScheme.HTTP.id);
/*     */     }
/* 217 */     if (builder.isPathEmpty()) {
/* 218 */       builder.setPathSegments(new String[] { "" });
/*     */     }
/* 220 */     return builder.build();
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
/*     */   public static HttpHost extractHost(URI uri) {
/* 233 */     if (uri == null) {
/* 234 */       return null;
/*     */     }
/* 236 */     URIBuilder uriBuilder = new URIBuilder(uri);
/* 237 */     String scheme = uriBuilder.getScheme();
/* 238 */     String host = uriBuilder.getHost();
/* 239 */     int port = uriBuilder.getPort();
/* 240 */     if (!TextUtils.isBlank(host)) {
/*     */       try {
/* 242 */         return new HttpHost(scheme, host, port);
/* 243 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     }
/*     */     
/* 246 */     return null;
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
/*     */   
/*     */   public static URI resolve(URI originalURI, HttpHost target, List<URI> redirects) throws URISyntaxException {
/*     */     URIBuilder uribuilder;
/* 269 */     Args.notNull(originalURI, "Request URI");
/*     */     
/* 271 */     if (redirects == null || redirects.isEmpty()) {
/* 272 */       uribuilder = new URIBuilder(originalURI);
/*     */     } else {
/* 274 */       uribuilder = new URIBuilder(redirects.get(redirects.size() - 1));
/* 275 */       String frag = uribuilder.getFragment();
/*     */       
/* 277 */       for (int i = redirects.size() - 1; frag == null && i >= 0; i--) {
/* 278 */         frag = ((URI)redirects.get(i)).getFragment();
/*     */       }
/* 280 */       uribuilder.setFragment(frag);
/*     */     } 
/*     */     
/* 283 */     if (uribuilder.getFragment() == null) {
/* 284 */       uribuilder.setFragment(originalURI.getFragment());
/*     */     }
/*     */     
/* 287 */     if (target != null && !uribuilder.isAbsolute()) {
/* 288 */       uribuilder.setScheme(target.getSchemeName());
/* 289 */       uribuilder.setHost(target.getHostName());
/* 290 */       uribuilder.setPort(target.getPort());
/*     */     } 
/* 292 */     return uribuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static URI create(HttpHost host, String path) throws URISyntaxException {
/* 304 */     URIBuilder builder = new URIBuilder(path);
/* 305 */     if (host != null) {
/* 306 */       builder.setHost(host.getHostName()).setPort(host.getPort()).setScheme(host.getSchemeName());
/*     */     }
/* 308 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static URI create(String scheme, URIAuthority host, String path) throws URISyntaxException {
/* 320 */     URIBuilder builder = new URIBuilder(path);
/* 321 */     if (scheme != null) {
/* 322 */       builder.setScheme(scheme);
/*     */     }
/* 324 */     if (host != null) {
/* 325 */       builder.setHost(host.getHostName()).setPort(host.getPort());
/*     */     }
/* 327 */     return builder.build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/utils/URIUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */