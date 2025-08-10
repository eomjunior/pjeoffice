/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBinResponseConsumer<T>
/*    */   extends AbstractBinDataConsumer
/*    */   implements AsyncResponseConsumer<T>
/*    */ {
/*    */   private volatile FutureCallback<T> resultCallback;
/*    */   
/*    */   protected abstract void start(HttpResponse paramHttpResponse, ContentType paramContentType) throws HttpException, IOException;
/*    */   
/*    */   protected abstract T buildResult();
/*    */   
/*    */   public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {}
/*    */   
/*    */   public final void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context, FutureCallback<T> resultCallback) throws HttpException, IOException {
/* 81 */     this.resultCallback = resultCallback;
/* 82 */     if (entityDetails != null) {
/*    */       try {
/* 84 */         ContentType contentType = ContentType.parse(entityDetails.getContentType());
/* 85 */         start(response, (contentType != null) ? contentType : ContentType.DEFAULT_BINARY);
/* 86 */       } catch (UnsupportedCharsetException ex) {
/* 87 */         throw new UnsupportedEncodingException(ex.getMessage());
/*    */       } 
/*    */     } else {
/* 90 */       start(response, null);
/* 91 */       completed();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void completed() {
/* 98 */     this.resultCallback.completed(buildResult());
/*    */   }
/*    */   
/*    */   public void failed(Exception cause) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/AbstractBinResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */