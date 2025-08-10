/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*    */ import org.bouncycastle.util.Encodable;
/*    */ 
/*    */ class LMSSignedPubKey
/*    */   implements Encodable {
/*    */   private final LMSSignature signature;
/*    */   private final LMSPublicKeyParameters publicKey;
/*    */   
/*    */   public LMSSignedPubKey(LMSSignature paramLMSSignature, LMSPublicKeyParameters paramLMSPublicKeyParameters) {
/* 15 */     this.signature = paramLMSSignature;
/* 16 */     this.publicKey = paramLMSPublicKeyParameters;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LMSSignature getSignature() {
/* 22 */     return this.signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public LMSPublicKeyParameters getPublicKey() {
/* 27 */     return this.publicKey;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 33 */     if (this == paramObject)
/*    */     {
/* 35 */       return true;
/*    */     }
/* 37 */     if (paramObject == null || getClass() != paramObject.getClass())
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey lMSSignedPubKey = (org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey)paramObject;
/*    */     
/* 44 */     if ((this.signature != null) ? !this.signature.equals(lMSSignedPubKey.signature) : (lMSSignedPubKey.signature != null))
/*    */     {
/* 46 */       return false;
/*    */     }
/* 48 */     return (this.publicKey != null) ? this.publicKey.equals(lMSSignedPubKey.publicKey) : ((lMSSignedPubKey.publicKey == null));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     int i = (this.signature != null) ? this.signature.hashCode() : 0;
/* 55 */     i = 31 * i + ((this.publicKey != null) ? this.publicKey.hashCode() : 0);
/* 56 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getEncoded() throws IOException {
/* 62 */     return Composer.compose()
/* 63 */       .bytes(this.signature.getEncoded())
/* 64 */       .bytes(this.publicKey.getEncoded())
/* 65 */       .build();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSSignedPubKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */