/*    */ package org.apache.hc.core5.http2.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.http.message.MessageSupport;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.http.protocol.ResponseContent;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class H2ResponseContent
/*    */   extends ResponseContent
/*    */ {
/* 56 */   public static final H2ResponseContent INSTANCE = new H2ResponseContent();
/*    */ 
/*    */   
/*    */   public H2ResponseContent() {}
/*    */ 
/*    */   
/*    */   public H2ResponseContent(boolean overwrite) {
/* 63 */     super(overwrite);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 71 */     Args.notNull(context, "HTTP context");
/* 72 */     ProtocolVersion ver = context.getProtocolVersion();
/* 73 */     if (ver.getMajor() < 2) {
/* 74 */       super.process(response, entity, context);
/* 75 */     } else if (entity != null) {
/* 76 */       MessageSupport.addContentTypeHeader((HttpMessage)response, entity);
/* 77 */       MessageSupport.addContentEncodingHeader((HttpMessage)response, entity);
/* 78 */       MessageSupport.addTrailerHeader((HttpMessage)response, entity);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/protocol/H2ResponseContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */