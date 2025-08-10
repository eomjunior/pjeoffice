/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPrivateKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPublicKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSException;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*     */ import org.bouncycastle.pqc.crypto.lms.SeedDerive;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ class LM_OTS {
/*     */   private static final short D_PBLC = -32640;
/*     */   private static final int ITER_K = 20;
/*     */   
/*     */   public static int coef(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  23 */     int i = paramInt1 * paramInt2 / 8;
/*  24 */     int j = 8 / paramInt2;
/*  25 */     int k = paramInt2 * ((paramInt1 ^ 0xFFFFFFFF) & j - 1);
/*  26 */     int m = (1 << paramInt2) - 1;
/*     */     
/*  28 */     return paramArrayOfbyte[i] >>> k & m;
/*     */   }
/*     */   private static final int ITER_PREV = 23; private static final int ITER_J = 22; static final int SEED_RANDOMISER_INDEX = -3; static final int SEED_LEN = 32; static final int MAX_HASH = 32;
/*     */   static final short D_MESG = -32383;
/*     */   
/*     */   public static int cksm(byte[] paramArrayOfbyte, int paramInt, LMOtsParameters paramLMOtsParameters) {
/*  34 */     int i = 0;
/*     */     
/*  36 */     int j = paramLMOtsParameters.getW();
/*     */ 
/*     */     
/*  39 */     int k = (1 << j) - 1;
/*     */     
/*  41 */     for (byte b = 0; b < paramInt * 8 / paramLMOtsParameters.getW(); b++)
/*     */     {
/*  43 */       i = i + k - coef(paramArrayOfbyte, b, paramLMOtsParameters.getW());
/*     */     }
/*  45 */     return i << paramLMOtsParameters.getLs();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LMOtsPublicKey lms_ots_generatePublicKey(LMOtsPrivateKey paramLMOtsPrivateKey) {
/*  51 */     byte[] arrayOfByte = lms_ots_generatePublicKey(paramLMOtsPrivateKey.getParameter(), paramLMOtsPrivateKey.getI(), paramLMOtsPrivateKey.getQ(), paramLMOtsPrivateKey.getMasterSecret());
/*  52 */     return new LMOtsPublicKey(paramLMOtsPrivateKey.getParameter(), paramLMOtsPrivateKey.getI(), paramLMOtsPrivateKey.getQ(), arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] lms_ots_generatePublicKey(LMOtsParameters paramLMOtsParameters, byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
/*  62 */     Digest digest1 = DigestUtil.getDigest(paramLMOtsParameters.getDigestOID());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     byte[] arrayOfByte1 = Composer.compose().bytes(paramArrayOfbyte1).u32str(paramInt).u16str(-32640).padUntil(0, 22).build();
/*  69 */     digest1.update(arrayOfByte1, 0, arrayOfByte1.length);
/*     */     
/*  71 */     Digest digest2 = DigestUtil.getDigest(paramLMOtsParameters.getDigestOID());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     byte[] arrayOfByte2 = Composer.compose().bytes(paramArrayOfbyte1).u32str(paramInt).padUntil(0, 23 + digest2.getDigestSize()).build();
/*     */ 
/*     */     
/*  80 */     SeedDerive seedDerive = new SeedDerive(paramArrayOfbyte1, paramArrayOfbyte2, DigestUtil.getDigest(paramLMOtsParameters.getDigestOID()));
/*  81 */     seedDerive.setQ(paramInt);
/*  82 */     seedDerive.setJ(0);
/*     */     
/*  84 */     int i = paramLMOtsParameters.getP();
/*  85 */     int j = paramLMOtsParameters.getN();
/*  86 */     int k = (1 << paramLMOtsParameters.getW()) - 1;
/*     */ 
/*     */     
/*  89 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  91 */       seedDerive.deriveSeed(arrayOfByte2, (b < i - 1), 23);
/*  92 */       Pack.shortToBigEndian((short)b, arrayOfByte2, 20);
/*  93 */       for (byte b1 = 0; b1 < k; b1++) {
/*     */         
/*  95 */         arrayOfByte2[22] = (byte)b1;
/*  96 */         digest2.update(arrayOfByte2, 0, arrayOfByte2.length);
/*  97 */         digest2.doFinal(arrayOfByte2, 23);
/*     */       } 
/*  99 */       digest1.update(arrayOfByte2, 23, j);
/*     */     } 
/*     */     
/* 102 */     byte[] arrayOfByte3 = new byte[digest1.getDigestSize()];
/* 103 */     digest1.doFinal(arrayOfByte3, 0);
/*     */     
/* 105 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LMOtsSignature lm_ots_generate_signature(LMSigParameters paramLMSigParameters, LMOtsPrivateKey paramLMOtsPrivateKey, byte[][] paramArrayOfbyte, byte[] paramArrayOfbyte1, boolean paramBoolean) {
/* 116 */     byte[] arrayOfByte1, arrayOfByte2 = new byte[34];
/*     */     
/* 118 */     if (!paramBoolean) {
/*     */       
/* 120 */       LMSContext lMSContext = paramLMOtsPrivateKey.getSignatureContext(paramLMSigParameters, paramArrayOfbyte);
/*     */       
/* 122 */       LmsUtils.byteArray(paramArrayOfbyte1, 0, paramArrayOfbyte1.length, (Digest)lMSContext);
/*     */       
/* 124 */       arrayOfByte1 = lMSContext.getC();
/* 125 */       arrayOfByte2 = lMSContext.getQ();
/*     */     }
/*     */     else {
/*     */       
/* 129 */       arrayOfByte1 = new byte[32];
/* 130 */       System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte2, 0, paramLMOtsPrivateKey.getParameter().getN());
/*     */     } 
/*     */     
/* 133 */     return lm_ots_generate_signature(paramLMOtsPrivateKey, arrayOfByte2, arrayOfByte1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static LMOtsSignature lm_ots_generate_signature(LMOtsPrivateKey paramLMOtsPrivateKey, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 138 */     LMOtsParameters lMOtsParameters = paramLMOtsPrivateKey.getParameter();
/*     */     
/* 140 */     int i = lMOtsParameters.getN();
/* 141 */     int j = lMOtsParameters.getP();
/* 142 */     int k = lMOtsParameters.getW();
/*     */     
/* 144 */     byte[] arrayOfByte1 = new byte[j * i];
/*     */     
/* 146 */     Digest digest = DigestUtil.getDigest(lMOtsParameters.getDigestOID());
/*     */     
/* 148 */     SeedDerive seedDerive = paramLMOtsPrivateKey.getDerivationFunction();
/*     */     
/* 150 */     int m = cksm(paramArrayOfbyte1, i, lMOtsParameters);
/* 151 */     paramArrayOfbyte1[i] = (byte)(m >>> 8 & 0xFF);
/* 152 */     paramArrayOfbyte1[i + 1] = (byte)m;
/*     */     
/* 154 */     byte[] arrayOfByte2 = Composer.compose().bytes(paramLMOtsPrivateKey.getI()).u32str(paramLMOtsPrivateKey.getQ()).padUntil(0, 23 + i).build();
/*     */     
/* 156 */     seedDerive.setJ(0);
/* 157 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 159 */       Pack.shortToBigEndian((short)b, arrayOfByte2, 20);
/* 160 */       seedDerive.deriveSeed(arrayOfByte2, (b < j - 1), 23);
/* 161 */       int n = coef(paramArrayOfbyte1, b, k);
/* 162 */       for (byte b1 = 0; b1 < n; b1++) {
/*     */         
/* 164 */         arrayOfByte2[22] = (byte)b1;
/* 165 */         digest.update(arrayOfByte2, 0, 23 + i);
/* 166 */         digest.doFinal(arrayOfByte2, 23);
/*     */       } 
/* 168 */       System.arraycopy(arrayOfByte2, 23, arrayOfByte1, i * b, i);
/*     */     } 
/*     */     
/* 171 */     return new LMOtsSignature(lMOtsParameters, paramArrayOfbyte2, arrayOfByte1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean lm_ots_validate_signature(LMOtsPublicKey paramLMOtsPublicKey, LMOtsSignature paramLMOtsSignature, byte[] paramArrayOfbyte, boolean paramBoolean) throws LMSException {
/* 177 */     if (!paramLMOtsSignature.getType().equals(paramLMOtsPublicKey.getParameter()))
/*     */     {
/* 179 */       throw new LMSException("public key and signature ots types do not match");
/*     */     }
/* 181 */     return Arrays.areEqual(lm_ots_validate_signature_calculate(paramLMOtsPublicKey, paramLMOtsSignature, paramArrayOfbyte), paramLMOtsPublicKey.getK());
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] lm_ots_validate_signature_calculate(LMOtsPublicKey paramLMOtsPublicKey, LMOtsSignature paramLMOtsSignature, byte[] paramArrayOfbyte) {
/* 186 */     LMSContext lMSContext = paramLMOtsPublicKey.createOtsContext(paramLMOtsSignature);
/*     */     
/* 188 */     LmsUtils.byteArray(paramArrayOfbyte, (Digest)lMSContext);
/*     */     
/* 190 */     return lm_ots_validate_signature_calculate(lMSContext);
/*     */   }
/*     */   
/*     */   public static byte[] lm_ots_validate_signature_calculate(LMSContext paramLMSContext) {
/*     */     LMOtsSignature lMOtsSignature;
/* 195 */     LMOtsPublicKey lMOtsPublicKey = paramLMSContext.getPublicKey();
/* 196 */     LMOtsParameters lMOtsParameters = lMOtsPublicKey.getParameter();
/* 197 */     Object object = paramLMSContext.getSignature();
/*     */     
/* 199 */     if (object instanceof LMSSignature) {
/*     */       
/* 201 */       lMOtsSignature = ((LMSSignature)object).getOtsSignature();
/*     */     }
/*     */     else {
/*     */       
/* 205 */       lMOtsSignature = (LMOtsSignature)object;
/*     */     } 
/*     */     
/* 208 */     int i = lMOtsParameters.getN();
/* 209 */     int j = lMOtsParameters.getW();
/* 210 */     int k = lMOtsParameters.getP();
/* 211 */     byte[] arrayOfByte1 = paramLMSContext.getQ();
/*     */     
/* 213 */     int m = cksm(arrayOfByte1, i, lMOtsParameters);
/* 214 */     arrayOfByte1[i] = (byte)(m >>> 8 & 0xFF);
/* 215 */     arrayOfByte1[i + 1] = (byte)m;
/*     */     
/* 217 */     byte[] arrayOfByte2 = lMOtsPublicKey.getI();
/* 218 */     int n = lMOtsPublicKey.getQ();
/*     */     
/* 220 */     Digest digest1 = DigestUtil.getDigest(lMOtsParameters.getDigestOID());
/* 221 */     LmsUtils.byteArray(arrayOfByte2, digest1);
/* 222 */     LmsUtils.u32str(n, digest1);
/* 223 */     LmsUtils.u16str((short)-32640, digest1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     byte[] arrayOfByte3 = Composer.compose().bytes(arrayOfByte2).u32str(n).padUntil(0, 23 + i).build();
/*     */     
/* 230 */     int i1 = (1 << j) - 1;
/*     */     
/* 232 */     byte[] arrayOfByte4 = lMOtsSignature.getY();
/*     */     
/* 234 */     Digest digest2 = DigestUtil.getDigest(lMOtsParameters.getDigestOID());
/* 235 */     for (byte b = 0; b < k; b++) {
/*     */       
/* 237 */       Pack.shortToBigEndian((short)b, arrayOfByte3, 20);
/* 238 */       System.arraycopy(arrayOfByte4, b * i, arrayOfByte3, 23, i);
/* 239 */       int i2 = coef(arrayOfByte1, b, j);
/*     */       
/* 241 */       for (int i3 = i2; i3 < i1; i3++) {
/*     */         
/* 243 */         arrayOfByte3[22] = (byte)i3;
/* 244 */         digest2.update(arrayOfByte3, 0, 23 + i);
/* 245 */         digest2.doFinal(arrayOfByte3, 23);
/*     */       } 
/*     */       
/* 248 */       digest1.update(arrayOfByte3, 23, i);
/*     */     } 
/*     */     
/* 251 */     byte[] arrayOfByte5 = new byte[i];
/* 252 */     digest1.doFinal(arrayOfByte5, 0);
/*     */     
/* 254 */     return arrayOfByte5;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LM_OTS.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */