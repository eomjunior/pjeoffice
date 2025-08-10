/*    */ package org.apache.hc.client5.http.entity;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GzipDecompressingEntity
/*    */   extends DecompressingEntity
/*    */ {
/*    */   public GzipDecompressingEntity(HttpEntity entity) {
/* 47 */     super(entity, GZIPInputStreamFactory.getInstance());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/GzipDecompressingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */