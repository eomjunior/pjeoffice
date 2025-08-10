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
/*    */ 
/*    */ public final class OID_2_16_76_1_3_2
/*    */   extends OIDBasic
/*    */ {
/*    */   public static final String OID = "2.16.76.1.3.2";
/* 38 */   public static final OID_2_16_76_1_3_2 EMPTY = new OID_2_16_76_1_3_2();
/*    */   
/*    */   private OID_2_16_76_1_3_2() {
/* 41 */     this("");
/*    */   }
/*    */   
/*    */   protected OID_2_16_76_1_3_2(String content) {
/* 45 */     super("2.16.76.1.3.2", content);
/*    */   }
/*    */   
/*    */   public Optional<String> getName() {
/* 49 */     return Strings.optional(getContent());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OID_2_16_76_1_3_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */