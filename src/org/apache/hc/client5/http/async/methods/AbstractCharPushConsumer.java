/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.entity.AbstractCharDataConsumer;
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
/*    */ public abstract class AbstractCharPushConsumer
/*    */   extends AbstractCharDataConsumer
/*    */   implements AsyncPushConsumer
/*    */ {
/*    */   protected abstract void start(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, ContentType paramContentType) throws HttpException, IOException;
/*    */   
/*    */   public final void consumePromise(HttpRequest promise, HttpResponse response, EntityDetails entityDetails, HttpContext context) throws HttpException, IOException {
/* 66 */     if (entityDetails != null) {
/*    */       ContentType contentType;
/*    */       try {
/* 69 */         contentType = ContentType.parse(entityDetails.getContentType());
/* 70 */       } catch (UnsupportedCharsetException ex) {
/* 71 */         throw new UnsupportedEncodingException(ex.getMessage());
/*    */       } 
/* 73 */       Charset charset = (contentType != null) ? contentType.getCharset() : null;
/* 74 */       if (charset == null) {
/* 75 */         charset = StandardCharsets.US_ASCII;
/*    */       }
/* 77 */       setCharset(charset);
/* 78 */       start(promise, response, (contentType != null) ? contentType : ContentType.DEFAULT_TEXT);
/*    */     } else {
/* 80 */       start(promise, response, (ContentType)null);
/* 81 */       completed();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void failed(Exception cause) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/AbstractCharPushConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */