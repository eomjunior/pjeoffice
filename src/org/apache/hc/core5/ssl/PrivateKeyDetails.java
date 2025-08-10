/*    */ package org.apache.hc.core5.ssl;
/*    */ 
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Arrays;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public final class PrivateKeyDetails
/*    */ {
/*    */   private final String type;
/*    */   private final X509Certificate[] certChain;
/*    */   
/*    */   public PrivateKeyDetails(String type, X509Certificate[] certChain) {
/* 46 */     this.type = (String)Args.notNull(type, "Private key type");
/* 47 */     this.certChain = certChain;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 51 */     return this.type;
/*    */   }
/*    */   
/*    */   public X509Certificate[] getCertChain() {
/* 55 */     return this.certChain;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return this.type + ':' + Arrays.toString((Object[])this.certChain);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/ssl/PrivateKeyDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */