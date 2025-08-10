/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LMSigParameters
/*    */ {
/* 11 */   public static final org.bouncycastle.pqc.crypto.lms.LMSigParameters lms_sha256_n32_h5 = new org.bouncycastle.pqc.crypto.lms.LMSigParameters(5, 32, 5, NISTObjectIdentifiers.id_sha256);
/* 12 */   public static final org.bouncycastle.pqc.crypto.lms.LMSigParameters lms_sha256_n32_h10 = new org.bouncycastle.pqc.crypto.lms.LMSigParameters(6, 32, 10, NISTObjectIdentifiers.id_sha256);
/* 13 */   public static final org.bouncycastle.pqc.crypto.lms.LMSigParameters lms_sha256_n32_h15 = new org.bouncycastle.pqc.crypto.lms.LMSigParameters(7, 32, 15, NISTObjectIdentifiers.id_sha256);
/* 14 */   public static final org.bouncycastle.pqc.crypto.lms.LMSigParameters lms_sha256_n32_h20 = new org.bouncycastle.pqc.crypto.lms.LMSigParameters(8, 32, 20, NISTObjectIdentifiers.id_sha256);
/* 15 */   public static final org.bouncycastle.pqc.crypto.lms.LMSigParameters lms_sha256_n32_h25 = new org.bouncycastle.pqc.crypto.lms.LMSigParameters(9, 32, 25, NISTObjectIdentifiers.id_sha256);
/*    */   
/* 17 */   private static Map<Object, org.bouncycastle.pqc.crypto.lms.LMSigParameters> paramBuilders = (Map<Object, org.bouncycastle.pqc.crypto.lms.LMSigParameters>)new Object();
/*    */ 
/*    */ 
/*    */   
/*    */   private final int type;
/*    */ 
/*    */   
/*    */   private final int m;
/*    */ 
/*    */   
/*    */   private final int h;
/*    */ 
/*    */   
/*    */   private final ASN1ObjectIdentifier digestOid;
/*    */ 
/*    */ 
/*    */   
/*    */   protected LMSigParameters(int paramInt1, int paramInt2, int paramInt3, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 35 */     this.type = paramInt1;
/* 36 */     this.m = paramInt2;
/* 37 */     this.h = paramInt3;
/* 38 */     this.digestOid = paramASN1ObjectIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getType() {
/* 43 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getH() {
/* 48 */     return this.h;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getM() {
/* 53 */     return this.m;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getDigestOID() {
/* 58 */     return this.digestOid;
/*    */   }
/*    */ 
/*    */   
/*    */   static org.bouncycastle.pqc.crypto.lms.LMSigParameters getParametersForType(int paramInt) {
/* 63 */     return paramBuilders.get(Integer.valueOf(paramInt));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSigParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */