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
/*    */ abstract class OIDPerson
/*    */   extends OIDBasic
/*    */ {
/*    */   private enum Fields
/*    */     implements OIDBasic.IMetadata
/*    */   {
/* 35 */     BIRTH_DATE(8),
/* 36 */     CPF(11),
/* 37 */     NIS(11),
/* 38 */     RG(15),
/* 39 */     UF_ISSUING_AGENCY_RG(6);
/*    */     
/*    */     private final int length;
/*    */     
/*    */     Fields(int length) {
/* 44 */       this.length = length;
/*    */     }
/*    */ 
/*    */     
/*    */     public int length() {
/* 49 */       return this.length;
/*    */     }
/*    */   }
/*    */   
/*    */   protected OIDPerson(String oid, String content) {
/* 54 */     super(oid, content);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setup() {
/* 59 */     setup((OIDBasic.IMetadata[])Fields.values());
/*    */   }
/*    */   
/*    */   public final Optional<String> getBirthDate() {
/* 63 */     return get(Fields.BIRTH_DATE);
/*    */   }
/*    */   
/*    */   public final Optional<String> getCPF() {
/* 67 */     return get(Fields.CPF);
/*    */   }
/*    */   
/*    */   public final Optional<String> getNIS() {
/* 71 */     return get(Fields.NIS);
/*    */   }
/*    */   
/*    */   public final Optional<String> getRg() {
/* 75 */     return get(Fields.RG);
/*    */   }
/*    */   
/*    */   public final Optional<String> getIssuingAgencyRg() {
/* 79 */     Optional<String> value = get(Fields.UF_ISSUING_AGENCY_RG);
/* 80 */     if (value.isPresent()) {
/* 81 */       String s = value.get();
/* 82 */       int len = s.length();
/* 83 */       if (len > 2) {
/* 84 */         return Optional.of(s.substring(0, len - 2));
/*    */       }
/*    */     } 
/* 87 */     return value;
/*    */   }
/*    */   
/*    */   public final Optional<String> getUfIssuingAgencyRg() {
/* 91 */     Optional<String> value = get(Fields.UF_ISSUING_AGENCY_RG);
/* 92 */     if (value.isPresent()) {
/* 93 */       String s = value.get();
/* 94 */       int len = s.length();
/* 95 */       if (len > 1) {
/* 96 */         return Optional.of(s.substring(len - 2, len));
/*    */       }
/*    */     } 
/* 99 */     return value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OIDPerson.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */