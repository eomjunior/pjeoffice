/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.qtesla;
/*    */ 
/*    */ import org.bouncycastle.crypto.digests.CSHAKEDigest;
/*    */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class HashUtils
/*    */ {
/*    */   static final int SECURE_HASH_ALGORITHM_KECCAK_128_RATE = 168;
/*    */   static final int SECURE_HASH_ALGORITHM_KECCAK_256_RATE = 136;
/*    */   
/*    */   static void secureHashAlgorithmKECCAK128(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
/* 17 */     SHAKEDigest sHAKEDigest = new SHAKEDigest(128);
/* 18 */     sHAKEDigest.update(paramArrayOfbyte2, paramInt3, paramInt4);
/*    */     
/* 20 */     sHAKEDigest.doFinal(paramArrayOfbyte1, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void secureHashAlgorithmKECCAK256(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
/* 28 */     SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
/* 29 */     sHAKEDigest.update(paramArrayOfbyte2, paramInt3, paramInt4);
/*    */     
/* 31 */     sHAKEDigest.doFinal(paramArrayOfbyte1, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void customizableSecureHashAlgorithmKECCAK128Simple(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
/* 39 */     CSHAKEDigest cSHAKEDigest = new CSHAKEDigest(128, null, new byte[] { (byte)paramShort, (byte)(paramShort >> 8) });
/* 40 */     cSHAKEDigest.update(paramArrayOfbyte2, paramInt3, paramInt4);
/*    */     
/* 42 */     cSHAKEDigest.doFinal(paramArrayOfbyte1, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   static void customizableSecureHashAlgorithmKECCAK256Simple(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
/* 47 */     CSHAKEDigest cSHAKEDigest = new CSHAKEDigest(256, null, new byte[] { (byte)paramShort, (byte)(paramShort >> 8) });
/* 48 */     cSHAKEDigest.update(paramArrayOfbyte2, paramInt3, paramInt4);
/*    */     
/* 50 */     cSHAKEDigest.doFinal(paramArrayOfbyte1, paramInt1, paramInt2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/HashUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */