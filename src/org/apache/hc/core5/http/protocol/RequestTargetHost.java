/*    */ package org.apache.hc.core5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.Method;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class RequestTargetHost
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 59 */   public static final HttpRequestInterceptor INSTANCE = new RequestTargetHost();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 68 */     Args.notNull(request, "HTTP request");
/* 69 */     Args.notNull(context, "HTTP context");
/*    */     
/* 71 */     ProtocolVersion ver = context.getProtocolVersion();
/* 72 */     String method = request.getMethod();
/* 73 */     if (Method.CONNECT.isSame(method) && ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */       return;
/*    */     }
/*    */     
/* 77 */     if (!request.containsHeader("Host")) {
/* 78 */       URIAuthority authority = request.getAuthority();
/* 79 */       if (authority == null) {
/* 80 */         if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*    */           return;
/*    */         }
/* 83 */         throw new ProtocolException("Target host is unknown");
/*    */       } 
/* 85 */       if (authority.getUserInfo() != null) {
/* 86 */         authority = new URIAuthority(authority.getHostName(), authority.getPort());
/*    */       }
/* 88 */       request.addHeader("Host", authority);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestTargetHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */