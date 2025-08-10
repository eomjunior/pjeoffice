/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*     */ import org.apache.hc.client5.http.utils.URIUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.URIBuilder;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultRedirectStrategy
/*     */   implements RedirectStrategy
/*     */ {
/*  57 */   public static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/*  64 */     Args.notNull(request, "HTTP request");
/*  65 */     Args.notNull(response, "HTTP response");
/*     */     
/*  67 */     if (!response.containsHeader("Location")) {
/*  68 */       return false;
/*     */     }
/*  70 */     int statusCode = response.getCode();
/*  71 */     switch (statusCode) {
/*     */       case 301:
/*     */       case 302:
/*     */       case 303:
/*     */       case 307:
/*     */       case 308:
/*  77 */         return true;
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException {
/*  88 */     Args.notNull(request, "HTTP request");
/*  89 */     Args.notNull(response, "HTTP response");
/*  90 */     Args.notNull(context, "HTTP context");
/*     */ 
/*     */     
/*  93 */     Header locationHeader = response.getFirstHeader("Location");
/*  94 */     if (locationHeader == null) {
/*  95 */       throw new HttpException("Redirect location is missing");
/*     */     }
/*  97 */     String location = locationHeader.getValue();
/*  98 */     URI uri = createLocationURI(location);
/*     */     try {
/* 100 */       if (!uri.isAbsolute())
/*     */       {
/* 102 */         uri = URIUtils.resolve(request.getUri(), uri);
/*     */       }
/* 104 */     } catch (URISyntaxException ex) {
/* 105 */       throw new ProtocolException(ex.getMessage(), ex);
/*     */     } 
/*     */     
/* 108 */     return uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI createLocationURI(String location) throws ProtocolException {
/*     */     try {
/* 116 */       URIBuilder b = new URIBuilder((new URI(location)).normalize());
/* 117 */       String host = b.getHost();
/* 118 */       if (host != null) {
/* 119 */         b.setHost(host.toLowerCase(Locale.ROOT));
/*     */       }
/* 121 */       if (b.isPathEmpty()) {
/* 122 */         b.setPathSegments(new String[] { "" });
/*     */       }
/* 124 */       return b.build();
/* 125 */     } catch (URISyntaxException ex) {
/* 126 */       throw new ProtocolException("Invalid redirect URI: " + location, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultRedirectStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */