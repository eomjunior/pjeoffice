/*    */ package org.apache.hc.core5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
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
/*    */ public class RequestUserAgent
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 54 */   public static final HttpRequestInterceptor INSTANCE = new RequestUserAgent();
/*    */   
/*    */   private final String userAgent;
/*    */ 
/*    */   
/*    */   public RequestUserAgent(String userAgent) {
/* 60 */     this.userAgent = userAgent;
/*    */   }
/*    */   
/*    */   public RequestUserAgent() {
/* 64 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 70 */     Args.notNull(request, "HTTP request");
/* 71 */     if (!request.containsHeader("User-Agent") && this.userAgent != null)
/* 72 */       request.addHeader("User-Agent", this.userAgent); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestUserAgent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */