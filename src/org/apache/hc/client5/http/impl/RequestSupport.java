/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import java.net.URISyntaxException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.net.PercentCodec;
/*    */ import org.apache.hc.core5.net.URIBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Internal
/*    */ public final class RequestSupport
/*    */ {
/*    */   public static String extractPathPrefix(HttpRequest request) {
/* 47 */     String path = request.getPath();
/*    */     try {
/* 49 */       URIBuilder uriBuilder = new URIBuilder(path);
/* 50 */       uriBuilder.setFragment(null);
/* 51 */       uriBuilder.clearParameters();
/* 52 */       uriBuilder.normalizeSyntax();
/* 53 */       List<String> pathSegments = uriBuilder.getPathSegments();
/*    */       
/* 55 */       if (!pathSegments.isEmpty()) {
/* 56 */         pathSegments.remove(pathSegments.size() - 1);
/*    */       }
/* 58 */       if (pathSegments.isEmpty()) {
/* 59 */         return "/";
/*    */       }
/* 61 */       StringBuilder buf = new StringBuilder();
/* 62 */       buf.append('/');
/* 63 */       for (String pathSegment : pathSegments) {
/* 64 */         PercentCodec.encode(buf, pathSegment, StandardCharsets.US_ASCII);
/* 65 */         buf.append('/');
/*    */       } 
/* 67 */       return buf.toString();
/*    */     }
/* 69 */     catch (URISyntaxException ex) {
/* 70 */       return path;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/RequestSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */