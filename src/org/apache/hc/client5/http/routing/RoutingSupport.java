/*    */ package org.apache.hc.client5.http.routing;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.apache.hc.client5.http.SchemePortResolver;
/*    */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*    */ import org.apache.hc.client5.http.utils.URIUtils;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.ProtocolException;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.net.URIAuthority;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RoutingSupport
/*    */ {
/*    */   public static HttpHost determineHost(HttpRequest request) throws HttpException {
/* 44 */     if (request == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     URIAuthority authority = request.getAuthority();
/* 48 */     if (authority != null) {
/* 49 */       String scheme = request.getScheme();
/* 50 */       if (scheme == null) {
/* 51 */         throw new ProtocolException("Protocol scheme is not specified");
/*    */       }
/* 53 */       return new HttpHost(scheme, (NamedEndpoint)authority);
/*    */     } 
/*    */     try {
/* 56 */       URI requestURI = request.getUri();
/* 57 */       if (requestURI.isAbsolute()) {
/* 58 */         HttpHost httpHost = URIUtils.extractHost(requestURI);
/* 59 */         if (httpHost == null) {
/* 60 */           throw new ProtocolException("URI does not specify a valid host name: " + requestURI);
/*    */         }
/* 62 */         return httpHost;
/*    */       } 
/* 64 */     } catch (URISyntaxException uRISyntaxException) {}
/*    */     
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public static HttpHost normalize(HttpHost host, SchemePortResolver schemePortResolver) {
/* 70 */     if (host == null) {
/* 71 */       return null;
/*    */     }
/* 73 */     if (host.getPort() < 0) {
/* 74 */       int port = ((schemePortResolver != null) ? schemePortResolver : DefaultSchemePortResolver.INSTANCE).resolve(host);
/* 75 */       if (port > 0) {
/* 76 */         return new HttpHost(host.getSchemeName(), host.getAddress(), host.getHostName(), port);
/*    */       }
/*    */     } 
/* 79 */     return host;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/routing/RoutingSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */