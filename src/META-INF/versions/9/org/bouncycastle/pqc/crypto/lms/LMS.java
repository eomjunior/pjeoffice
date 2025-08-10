/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ 
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.lms.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LM_OTS;
/*     */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LMS
/*     */ {
/*     */   static final short D_LEAF = -32126;
/*     */   static final short D_INTR = -31869;
/*     */   
/*     */   public static LMSPrivateKeyParameters generateKeys(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters, int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IllegalArgumentException {
/*  26 */     if (paramArrayOfbyte2 == null || paramArrayOfbyte2.length < paramLMSigParameters.getM())
/*     */     {
/*  28 */       throw new IllegalArgumentException("root seed is less than " + paramLMSigParameters.getM());
/*     */     }
/*     */     
/*  31 */     int i = 1 << paramLMSigParameters.getH();
/*     */     
/*  33 */     return new LMSPrivateKeyParameters(paramLMSigParameters, paramLMOtsParameters, paramInt, paramArrayOfbyte1, i, paramArrayOfbyte2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LMSSignature generateSign(LMSPrivateKeyParameters paramLMSPrivateKeyParameters, byte[] paramArrayOfbyte) {
/*  45 */     LMSContext lMSContext = paramLMSPrivateKeyParameters.generateLMSContext();
/*     */     
/*  47 */     lMSContext.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */     
/*  49 */     return generateSign(lMSContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LMSSignature generateSign(LMSContext paramLMSContext) {
/*  61 */     LMOtsSignature lMOtsSignature = LM_OTS.lm_ots_generate_signature(paramLMSContext.getPrivateKey(), paramLMSContext.getQ(), paramLMSContext.getC());
/*     */     
/*  63 */     return new LMSSignature(paramLMSContext.getPrivateKey().getQ(), lMOtsSignature, paramLMSContext.getSigParams(), paramLMSContext.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean verifySignature(LMSPublicKeyParameters paramLMSPublicKeyParameters, LMSSignature paramLMSSignature, byte[] paramArrayOfbyte) {
/*  75 */     LMSContext lMSContext = paramLMSPublicKeyParameters.generateOtsContext(paramLMSSignature);
/*     */     
/*  77 */     LmsUtils.byteArray(paramArrayOfbyte, (Digest)lMSContext);
/*     */     
/*  79 */     return verifySignature(paramLMSPublicKeyParameters, lMSContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean verifySignature(LMSPublicKeyParameters paramLMSPublicKeyParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  84 */     LMSContext lMSContext = paramLMSPublicKeyParameters.generateLMSContext(paramArrayOfbyte1);
/*     */     
/*  86 */     LmsUtils.byteArray(paramArrayOfbyte2, (Digest)lMSContext);
/*     */     
/*  88 */     return verifySignature(paramLMSPublicKeyParameters, lMSContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean verifySignature(LMSPublicKeyParameters paramLMSPublicKeyParameters, LMSContext paramLMSContext) {
/*  93 */     LMSSignature lMSSignature = (LMSSignature)paramLMSContext.getSignature();
/*  94 */     LMSigParameters lMSigParameters = lMSSignature.getParameter();
/*  95 */     int i = lMSigParameters.getH();
/*  96 */     byte[][] arrayOfByte = lMSSignature.getY();
/*  97 */     byte[] arrayOfByte1 = LM_OTS.lm_ots_validate_signature_calculate(paramLMSContext);
/*     */ 
/*     */     
/* 100 */     int j = (1 << i) + lMSSignature.getQ();
/*     */ 
/*     */     
/* 103 */     byte[] arrayOfByte2 = paramLMSPublicKeyParameters.getI();
/* 104 */     Digest digest = DigestUtil.getDigest(lMSigParameters.getDigestOID());
/* 105 */     byte[] arrayOfByte3 = new byte[digest.getDigestSize()];
/*     */     
/* 107 */     digest.update(arrayOfByte2, 0, arrayOfByte2.length);
/* 108 */     LmsUtils.u32str(j, digest);
/* 109 */     LmsUtils.u16str((short)-32126, digest);
/* 110 */     digest.update(arrayOfByte1, 0, arrayOfByte1.length);
/* 111 */     digest.doFinal(arrayOfByte3, 0);
/*     */     
/* 113 */     byte b = 0;
/*     */     
/* 115 */     while (j > 1) {
/*     */       
/* 117 */       if ((j & 0x1) == 1) {
/*     */ 
/*     */         
/* 120 */         digest.update(arrayOfByte2, 0, arrayOfByte2.length);
/* 121 */         LmsUtils.u32str(j / 2, digest);
/* 122 */         LmsUtils.u16str((short)-31869, digest);
/* 123 */         digest.update(arrayOfByte[b], 0, (arrayOfByte[b]).length);
/* 124 */         digest.update(arrayOfByte3, 0, arrayOfByte3.length);
/* 125 */         digest.doFinal(arrayOfByte3, 0);
/*     */       }
/*     */       else {
/*     */         
/* 129 */         digest.update(arrayOfByte2, 0, arrayOfByte2.length);
/* 130 */         LmsUtils.u32str(j / 2, digest);
/* 131 */         LmsUtils.u16str((short)-31869, digest);
/* 132 */         digest.update(arrayOfByte3, 0, arrayOfByte3.length);
/* 133 */         digest.update(arrayOfByte[b], 0, (arrayOfByte[b]).length);
/* 134 */         digest.doFinal(arrayOfByte3, 0);
/*     */       } 
/* 136 */       j /= 2;
/* 137 */       b++;
/*     */     } 
/* 139 */     byte[] arrayOfByte4 = arrayOfByte3;
/* 140 */     return paramLMSPublicKeyParameters.matchesT1(arrayOfByte4);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMS.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */