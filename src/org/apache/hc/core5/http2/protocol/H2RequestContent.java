/*    */ package org.apache.hc.core5.http2.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.Method;
/*    */ import org.apache.hc.core5.http.ProtocolException;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.http.message.MessageSupport;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.http.protocol.RequestContent;
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
/*    */ public class H2RequestContent
/*    */   extends RequestContent
/*    */ {
/* 58 */   public static final H2RequestContent INSTANCE = new H2RequestContent();
/*    */ 
/*    */   
/*    */   public H2RequestContent() {}
/*    */ 
/*    */   
/*    */   public H2RequestContent(boolean overwrite) {
/* 65 */     super(overwrite);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 73 */     Args.notNull(context, "HTTP context");
/* 74 */     ProtocolVersion ver = context.getProtocolVersion();
/* 75 */     if (ver.getMajor() < 2) {
/* 76 */       super.process(request, entity, context);
/* 77 */     } else if (entity != null) {
/* 78 */       String method = request.getMethod();
/* 79 */       if (Method.TRACE.isSame(method)) {
/* 80 */         throw new ProtocolException("TRACE request may not enclose an entity");
/*    */       }
/* 82 */       MessageSupport.addContentTypeHeader((HttpMessage)request, entity);
/* 83 */       MessageSupport.addContentEncodingHeader((HttpMessage)request, entity);
/* 84 */       MessageSupport.addTrailerHeader((HttpMessage)request, entity);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/protocol/H2RequestContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */