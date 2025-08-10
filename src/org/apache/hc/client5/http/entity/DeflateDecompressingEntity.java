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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeflateDecompressingEntity
/*    */   extends DecompressingEntity
/*    */ {
/*    */   public DeflateDecompressingEntity(HttpEntity entity) {
/* 57 */     super(entity, DeflateInputStreamFactory.getInstance());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/DeflateDecompressingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */