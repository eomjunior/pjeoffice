/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*    */ import org.apache.hc.core5.http.nio.support.AbstractAsyncResponseConsumer;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleResponseConsumer
/*    */   extends AbstractAsyncResponseConsumer<SimpleHttpResponse, byte[]>
/*    */ {
/*    */   SimpleResponseConsumer(AsyncEntityConsumer<byte[]> entityConsumer) {
/* 57 */     super(entityConsumer);
/*    */   }
/*    */   
/*    */   public static SimpleResponseConsumer create() {
/* 61 */     return new SimpleResponseConsumer((AsyncEntityConsumer<byte[]>)new SimpleAsyncEntityConsumer());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {}
/*    */ 
/*    */   
/*    */   protected SimpleHttpResponse buildResult(HttpResponse response, byte[] entity, ContentType contentType) {
/* 70 */     SimpleHttpResponse simpleResponse = SimpleHttpResponse.copy(response);
/* 71 */     if (entity != null) {
/* 72 */       simpleResponse.setBody(entity, contentType);
/*    */     }
/* 74 */     return simpleResponse;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */