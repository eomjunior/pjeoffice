/*    */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.util;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*    */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*    */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyUtil
/*    */ {
/*    */   public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable) {
/*    */     try {
/* 15 */       return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(paramAlgorithmIdentifier, paramASN1Encodable));
/*    */     }
/* 17 */     catch (Exception exception) {
/*    */       
/* 19 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getEncodedSubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
/*    */     try {
/* 27 */       return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(paramAlgorithmIdentifier, paramArrayOfbyte));
/*    */     }
/* 29 */     catch (Exception exception) {
/*    */       
/* 31 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getEncodedSubjectPublicKeyInfo(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
/*    */     try {
/* 39 */       return paramSubjectPublicKeyInfo.getEncoded("DER");
/*    */     }
/* 41 */     catch (Exception exception) {
/*    */       
/* 43 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getEncodedPrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable) {
/*    */     try {
/* 51 */       PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(paramAlgorithmIdentifier, (ASN1Encodable)paramASN1Encodable.toASN1Primitive());
/*    */       
/* 53 */       return getEncodedPrivateKeyInfo(privateKeyInfo);
/*    */     }
/* 55 */     catch (Exception exception) {
/*    */       
/* 57 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getEncodedPrivateKeyInfo(PrivateKeyInfo paramPrivateKeyInfo) {
/*    */     try {
/* 65 */       return paramPrivateKeyInfo.getEncoded("DER");
/*    */     }
/* 67 */     catch (Exception exception) {
/*    */       
/* 69 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/util/KeyUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */