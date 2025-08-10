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
/*    */ public class BrotliDecompressingEntity
/*    */   extends DecompressingEntity
/*    */ {
/*    */   public BrotliDecompressingEntity(HttpEntity entity) {
/* 45 */     super(entity, BrotliInputStreamFactory.getInstance());
/*    */   }
/*    */   
/*    */   public static boolean isAvailable() {
/*    */     try {
/* 50 */       Class.forName("org.brotli.dec.BrotliInputStream");
/* 51 */       return true;
/* 52 */     } catch (ClassNotFoundException|NoClassDefFoundError e) {
/* 53 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/BrotliDecompressingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */