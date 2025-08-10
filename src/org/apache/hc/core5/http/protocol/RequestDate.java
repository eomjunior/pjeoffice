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
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public class RequestDate
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 55 */   public static final HttpRequestInterceptor INSTANCE = new RequestDate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 64 */     Args.notNull(request, "HTTP request");
/* 65 */     if (entity != null && !request.containsHeader("Date"))
/* 66 */       request.setHeader("Date", HttpDateGenerator.INSTANCE.getCurrentDate()); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */