/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.HttpResponseException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*    */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public abstract class AbstractHttpClientResponseHandler<T>
/*    */   implements HttpClientResponseHandler<T>
/*    */ {
/*    */   public T handleResponse(ClassicHttpResponse response) throws IOException {
/* 65 */     HttpEntity entity = response.getEntity();
/* 66 */     if (response.getCode() >= 300) {
/* 67 */       EntityUtils.consume(entity);
/* 68 */       throw new HttpResponseException(response.getCode(), response.getReasonPhrase());
/*    */     } 
/* 70 */     return (entity == null) ? null : handleEntity(entity);
/*    */   }
/*    */   
/*    */   public abstract T handleEntity(HttpEntity paramHttpEntity) throws IOException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/AbstractHttpClientResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */