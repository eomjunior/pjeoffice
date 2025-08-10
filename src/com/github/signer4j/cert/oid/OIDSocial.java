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
/*    */ abstract class OIDSocial
/*    */   extends OIDBasic
/*    */ {
/*    */   private enum Fields
/*    */     implements OIDBasic.IMetadata
/*    */   {
/* 35 */     CEI(12);
/*    */     
/*    */     private final int length;
/*    */     
/*    */     Fields(int length) {
/* 40 */       this.length = length;
/*    */     }
/*    */ 
/*    */     
/*    */     public int length() {
/* 45 */       return this.length;
/*    */     }
/*    */   }
/*    */   
/*    */   protected OIDSocial(String oid, String content) {
/* 50 */     super(oid, content);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setup() {
/* 55 */     setup((OIDBasic.IMetadata[])Fields.values());
/*    */   }
/*    */   
/*    */   public Optional<String> getCEI() {
/* 59 */     return get(Fields.CEI);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OIDSocial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */