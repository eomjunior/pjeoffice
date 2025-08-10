/*    */ package com.github.signer4j.cert.oid;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OID_1_3_6_1_4_1_311_20_2_3
/*    */   extends OIDBasic
/*    */ {
/*    */   public static final String OID = "1.3.6.1.4.1.311.20.2.3";
/*    */   
/*    */   protected OID_1_3_6_1_4_1_311_20_2_3(String content) {
/* 39 */     super("1.3.6.1.4.1.311.20.2.3", content);
/*    */   }
/*    */   
/*    */   public Optional<String> getUPN() {
/* 43 */     return Strings.optional(getContent());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OID_1_3_6_1_4_1_311_20_2_3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */