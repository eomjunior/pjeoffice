/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.entity.AbstractBinDataConsumer;
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
/*    */ public abstract class AbstractBinPushConsumer
/*    */   extends AbstractBinDataConsumer
/*    */   implements AsyncPushConsumer
/*    */ {
/*    */   protected abstract void start(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, ContentType paramContentType) throws HttpException, IOException;
/*    */   
/*    */   public final void consumePromise(HttpRequest promise, HttpResponse response, EntityDetails entityDetails, HttpContext context) throws HttpException, IOException {
/* 64 */     if (entityDetails != null) {
/*    */       ContentType contentType;
/*    */       try {
/* 67 */         contentType = ContentType.parse(entityDetails.getContentType());
/* 68 */       } catch (UnsupportedCharsetException ex) {
/* 69 */         throw new UnsupportedEncodingException(ex.getMessage());
/*    */       } 
/* 71 */       start(promise, response, (contentType != null) ? contentType : ContentType.DEFAULT_BINARY);
/*    */     } else {
/* 73 */       start(promise, response, null);
/* 74 */       completed();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void failed(Exception cause) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/AbstractBinPushConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */