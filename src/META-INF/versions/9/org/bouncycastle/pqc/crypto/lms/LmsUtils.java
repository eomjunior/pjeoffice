/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*    */ 
/*    */ class LmsUtils {
/*    */   static void u32str(int paramInt, Digest paramDigest) {
/*  9 */     paramDigest.update((byte)(paramInt >>> 24));
/* 10 */     paramDigest.update((byte)(paramInt >>> 16));
/* 11 */     paramDigest.update((byte)(paramInt >>> 8));
/* 12 */     paramDigest.update((byte)paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   static void u16str(short paramShort, Digest paramDigest) {
/* 17 */     paramDigest.update((byte)(paramShort >>> 8));
/* 18 */     paramDigest.update((byte)paramShort);
/*    */   }
/*    */ 
/*    */   
/*    */   static void byteArray(byte[] paramArrayOfbyte, Digest paramDigest) {
/* 23 */     paramDigest.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*    */   }
/*    */ 
/*    */   
/*    */   static void byteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Digest paramDigest) {
/* 28 */     paramDigest.update(paramArrayOfbyte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   static int calculateStrength(LMSParameters paramLMSParameters) {
/* 33 */     if (paramLMSParameters == null)
/*    */     {
/* 35 */       throw new NullPointerException("lmsParameters cannot be null");
/*    */     }
/*    */     
/* 38 */     LMSigParameters lMSigParameters = paramLMSParameters.getLMSigParam();
/* 39 */     return (1 << lMSigParameters.getH()) * lMSigParameters.getM();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LmsUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */