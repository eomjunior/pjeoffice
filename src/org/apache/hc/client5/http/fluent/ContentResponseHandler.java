/*    */ package org.apache.hc.client5.http.fluent;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.HttpEntity;
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
/*    */ public class ContentResponseHandler
/*    */   extends AbstractHttpClientResponseHandler<Content>
/*    */ {
/*    */   public Content handleEntity(HttpEntity entity) throws IOException {
/* 49 */     return (entity != null) ? new Content(
/* 50 */         EntityUtils.toByteArray(entity), ContentType.parse(entity.getContentType())) : Content.NO_CONTENT;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/ContentResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */