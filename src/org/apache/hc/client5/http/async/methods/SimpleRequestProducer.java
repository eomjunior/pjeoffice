/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*    */ import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
/*    */ import org.apache.hc.core5.http.nio.entity.StringAsyncEntityProducer;
/*    */ import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
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
/*    */ public final class SimpleRequestProducer
/*    */   extends BasicRequestProducer
/*    */ {
/*    */   SimpleRequestProducer(SimpleHttpRequest request, AsyncEntityProducer entityProducer) {
/* 54 */     super((HttpRequest)request, entityProducer);
/*    */   }
/*    */   public static SimpleRequestProducer create(SimpleHttpRequest request) {
/*    */     AsyncEntityProducer entityProducer;
/* 58 */     Args.notNull(request, "Request");
/* 59 */     SimpleBody body = request.getBody();
/*    */     
/* 61 */     if (body != null) {
/* 62 */       if (body.isText()) {
/* 63 */         StringAsyncEntityProducer stringAsyncEntityProducer = new StringAsyncEntityProducer(body.getBodyText(), body.getContentType());
/*    */       } else {
/* 65 */         BasicAsyncEntityProducer basicAsyncEntityProducer = new BasicAsyncEntityProducer(body.getBodyBytes(), body.getContentType());
/*    */       } 
/*    */     } else {
/* 68 */       entityProducer = null;
/*    */     } 
/* 70 */     return new SimpleRequestProducer(request, entityProducer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleRequestProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */