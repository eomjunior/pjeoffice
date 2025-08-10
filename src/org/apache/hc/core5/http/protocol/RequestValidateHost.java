/*    */ package org.apache.hc.core5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URISyntaxException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.ProtocolException;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.net.URIAuthority;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class RequestValidateHost
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 64 */     Args.notNull(request, "HTTP request");
/*    */     
/* 66 */     Header header = request.getHeader("Host");
/* 67 */     if (header != null) {
/*    */       URIAuthority authority;
/*    */       try {
/* 70 */         authority = URIAuthority.create(header.getValue());
/* 71 */       } catch (URISyntaxException ex) {
/* 72 */         throw new ProtocolException(ex.getMessage(), ex);
/*    */       } 
/* 74 */       request.setAuthority(authority);
/*    */     } else {
/* 76 */       ProtocolVersion version = (request.getVersion() != null) ? request.getVersion() : (ProtocolVersion)HttpVersion.HTTP_1_1;
/* 77 */       if (version.greaterEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
/* 78 */         throw new ProtocolException("Host header is absent"); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestValidateHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */