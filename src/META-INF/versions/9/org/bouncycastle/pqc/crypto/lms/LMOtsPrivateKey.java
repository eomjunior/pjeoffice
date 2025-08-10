/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.pqc.crypto.lms.DigestUtil;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*    */ import org.bouncycastle.pqc.crypto.lms.SeedDerive;
/*    */ 
/*    */ class LMOtsPrivateKey {
/*    */   private final LMOtsParameters parameter;
/*    */   private final byte[] I;
/*    */   private final int q;
/*    */   private final byte[] masterSecret;
/*    */   
/*    */   public LMOtsPrivateKey(LMOtsParameters paramLMOtsParameters, byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
/* 18 */     this.parameter = paramLMOtsParameters;
/* 19 */     this.I = paramArrayOfbyte1;
/* 20 */     this.q = paramInt;
/* 21 */     this.masterSecret = paramArrayOfbyte2;
/*    */   }
/*    */ 
/*    */   
/*    */   LMSContext getSignatureContext(LMSigParameters paramLMSigParameters, byte[][] paramArrayOfbyte) {
/* 26 */     byte[] arrayOfByte = new byte[32];
/*    */     
/* 28 */     SeedDerive seedDerive = getDerivationFunction();
/* 29 */     seedDerive.setJ(-3);
/* 30 */     seedDerive.deriveSeed(arrayOfByte, false);
/*    */     
/* 32 */     Digest digest = DigestUtil.getDigest(this.parameter.getDigestOID());
/*    */     
/* 34 */     LmsUtils.byteArray(getI(), digest);
/* 35 */     LmsUtils.u32str(getQ(), digest);
/* 36 */     LmsUtils.u16str((short)-32383, digest);
/* 37 */     LmsUtils.byteArray(arrayOfByte, digest);
/*    */     
/* 39 */     return new LMSContext(this, paramLMSigParameters, digest, arrayOfByte, paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   SeedDerive getDerivationFunction() {
/* 44 */     SeedDerive seedDerive = new SeedDerive(this.I, this.masterSecret, DigestUtil.getDigest(this.parameter.getDigestOID()));
/* 45 */     seedDerive.setQ(this.q);
/* 46 */     return seedDerive;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LMOtsParameters getParameter() {
/* 52 */     return this.parameter;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getI() {
/* 57 */     return this.I;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getQ() {
/* 62 */     return this.q;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getMasterSecret() {
/* 67 */     return this.masterSecret;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMOtsPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */