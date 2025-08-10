/*    */ package org.apache.hc.core5.http2.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class H2RequestTargetHost
/*    */   extends RequestTargetHost
/*    */ {
/* 55 */   public static final H2RequestTargetHost INSTANCE = new H2RequestTargetHost();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 62 */     Args.notNull(context, "HTTP context");
/* 63 */     ProtocolVersion ver = context.getProtocolVersion();
/* 64 */     if (ver.getMajor() < 2)
/* 65 */       super.process(request, entity, context); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/protocol/H2RequestTargetHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */