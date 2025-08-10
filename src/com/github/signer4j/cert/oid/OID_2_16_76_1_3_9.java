/*    */ package com.github.signer4j.cert.oid;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OID_2_16_76_1_3_9
/*    */   extends OIDBasic
/*    */ {
/*    */   public static final String OID = "2.16.76.1.3.9";
/*    */   
/*    */   private enum Fields
/*    */     implements OIDBasic.IMetadata
/*    */   {
/* 37 */     RIC(11);
/*    */     
/*    */     private final int length;
/*    */     
/*    */     Fields(int length) {
/* 42 */       this.length = length;
/*    */     }
/*    */ 
/*    */     
/*    */     public int length() {
/* 47 */       return this.length;
/*    */     }
/*    */   }
/*    */   
/*    */   protected OID_2_16_76_1_3_9(String content) {
/* 52 */     super("2.16.76.1.3.9", content);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setup() {
/* 57 */     setup((OIDBasic.IMetadata[])Fields.values());
/*    */   }
/*    */   
/*    */   public Optional<String> getRegistroDeIdentidadeCivil() {
/* 61 */     return get(Fields.RIC);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OID_2_16_76_1_3_9.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */