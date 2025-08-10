/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.ClientProtocolException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ import org.apache.hc.core5.http.ParseException;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicHttpClientResponseHandler
/*    */   extends AbstractHttpClientResponseHandler<String>
/*    */ {
/*    */   public String handleEntity(HttpEntity entity) throws IOException {
/*    */     try {
/* 64 */       return EntityUtils.toString(entity);
/* 65 */     } catch (ParseException ex) {
/* 66 */       throw new ClientProtocolException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String handleResponse(ClassicHttpResponse response) throws IOException {
/* 72 */     return super.handleResponse(response);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/BasicHttpClientResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */