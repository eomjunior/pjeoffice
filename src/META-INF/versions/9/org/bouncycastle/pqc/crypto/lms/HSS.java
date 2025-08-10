/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSKeyGenerationParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMS;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
/*     */ 
/*     */ class HSS {
/*     */   public static HSSPrivateKeyParameters generateHSSKeyPair(HSSKeyGenerationParameters paramHSSKeyGenerationParameters) {
/*  15 */     LMSPrivateKeyParameters[] arrayOfLMSPrivateKeyParameters = new LMSPrivateKeyParameters[paramHSSKeyGenerationParameters.getDepth()];
/*  16 */     LMSSignature[] arrayOfLMSSignature = new LMSSignature[paramHSSKeyGenerationParameters.getDepth() - 1];
/*     */     
/*  18 */     byte[] arrayOfByte1 = new byte[32];
/*  19 */     paramHSSKeyGenerationParameters.getRandom().nextBytes(arrayOfByte1);
/*     */     
/*  21 */     byte[] arrayOfByte2 = new byte[16];
/*  22 */     paramHSSKeyGenerationParameters.getRandom().nextBytes(arrayOfByte2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     byte[] arrayOfByte3 = new byte[0];
/*     */     
/*  32 */     long l = 1L;
/*  33 */     for (byte b = 0; b < arrayOfLMSPrivateKeyParameters.length; b++) {
/*     */       
/*  35 */       if (b == 0) {
/*     */         
/*  37 */         arrayOfLMSPrivateKeyParameters[b] = new LMSPrivateKeyParameters(paramHSSKeyGenerationParameters
/*  38 */             .getLmsParameters()[b].getLMSigParam(), paramHSSKeyGenerationParameters
/*  39 */             .getLmsParameters()[b].getLMOTSParam(), 0, arrayOfByte2, 1 << paramHSSKeyGenerationParameters
/*     */ 
/*     */             
/*  42 */             .getLmsParameters()[b].getLMSigParam().getH(), arrayOfByte1);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  47 */         arrayOfLMSPrivateKeyParameters[b] = (LMSPrivateKeyParameters)new PlaceholderLMSPrivateKey(paramHSSKeyGenerationParameters
/*  48 */             .getLmsParameters()[b].getLMSigParam(), paramHSSKeyGenerationParameters
/*  49 */             .getLmsParameters()[b].getLMOTSParam(), -1, arrayOfByte3, 1 << paramHSSKeyGenerationParameters
/*     */ 
/*     */             
/*  52 */             .getLmsParameters()[b].getLMSigParam().getH(), arrayOfByte3);
/*     */       } 
/*     */       
/*  55 */       l *= (1 << paramHSSKeyGenerationParameters.getLmsParameters()[b].getLMSigParam().getH());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (l == 0L)
/*     */     {
/*  62 */       l = Long.MAX_VALUE;
/*     */     }
/*     */     
/*  65 */     return new HSSPrivateKeyParameters(paramHSSKeyGenerationParameters
/*  66 */         .getDepth(), 
/*  67 */         Arrays.asList(arrayOfLMSPrivateKeyParameters), 
/*  68 */         Arrays.asList(arrayOfLMSSignature), 0L, l);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void incrementIndex(HSSPrivateKeyParameters paramHSSPrivateKeyParameters) {
/*  83 */     synchronized (paramHSSPrivateKeyParameters) {
/*     */       
/*  85 */       rangeTestKeys(paramHSSPrivateKeyParameters);
/*  86 */       paramHSSPrivateKeyParameters.incIndex();
/*  87 */       ((LMSPrivateKeyParameters)paramHSSPrivateKeyParameters.getKeys().get(paramHSSPrivateKeyParameters.getL() - 1)).incIndex();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void rangeTestKeys(HSSPrivateKeyParameters paramHSSPrivateKeyParameters) {
/*  94 */     synchronized (paramHSSPrivateKeyParameters) {
/*     */       
/*  96 */       if (paramHSSPrivateKeyParameters.getIndex() >= paramHSSPrivateKeyParameters.getIndexLimit())
/*     */       {
/*  98 */         throw new ExhaustedPrivateKeyException("hss private key" + (
/*     */             
/* 100 */             paramHSSPrivateKeyParameters.isShard() ? " shard" : "") + " is exhausted");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 105 */       int i = paramHSSPrivateKeyParameters.getL();
/* 106 */       int j = i;
/* 107 */       List<LMSPrivateKeyParameters> list = paramHSSPrivateKeyParameters.getKeys();
/* 108 */       while (((LMSPrivateKeyParameters)list.get(j - 1)).getIndex() == 1 << ((LMSPrivateKeyParameters)list.get(j - 1)).getSigParameters().getH()) {
/*     */         
/* 110 */         j--;
/* 111 */         if (j == 0)
/*     */         {
/* 113 */           throw new ExhaustedPrivateKeyException("hss private key" + (
/*     */               
/* 115 */               paramHSSPrivateKeyParameters.isShard() ? " shard" : "") + " is exhausted the maximum limit for this HSS private key");
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 121 */       while (j < i) {
/*     */         
/* 123 */         paramHSSPrivateKeyParameters.replaceConsumedKey(j);
/* 124 */         j++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static HSSSignature generateSignature(HSSPrivateKeyParameters paramHSSPrivateKeyParameters, byte[] paramArrayOfbyte) {
/*     */     LMSSignedPubKey[] arrayOfLMSSignedPubKey;
/*     */     LMSPrivateKeyParameters lMSPrivateKeyParameters;
/* 134 */     int i = paramHSSPrivateKeyParameters.getL();
/*     */     
/* 136 */     synchronized (paramHSSPrivateKeyParameters) {
/*     */       
/* 138 */       rangeTestKeys(paramHSSPrivateKeyParameters);
/*     */       
/* 140 */       List<LMSPrivateKeyParameters> list = paramHSSPrivateKeyParameters.getKeys();
/* 141 */       List<LMSSignature> list1 = paramHSSPrivateKeyParameters.getSig();
/*     */       
/* 143 */       lMSPrivateKeyParameters = paramHSSPrivateKeyParameters.getKeys().get(i - 1);
/*     */ 
/*     */       
/* 146 */       int j = 0;
/* 147 */       arrayOfLMSSignedPubKey = new LMSSignedPubKey[i - 1];
/* 148 */       while (j < i - 1) {
/*     */         
/* 150 */         arrayOfLMSSignedPubKey[j] = new LMSSignedPubKey(list1
/* 151 */             .get(j), ((LMSPrivateKeyParameters)list
/* 152 */             .get(j + 1)).getPublicKey());
/* 153 */         j++;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 159 */       paramHSSPrivateKeyParameters.incIndex();
/*     */     } 
/*     */     
/* 162 */     LMSContext lMSContext = lMSPrivateKeyParameters.generateLMSContext().withSignedPublicKeys(arrayOfLMSSignedPubKey);
/*     */     
/* 164 */     lMSContext.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */     
/* 166 */     return generateSignature(i, lMSContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public static HSSSignature generateSignature(int paramInt, LMSContext paramLMSContext) {
/* 171 */     return new HSSSignature(paramInt - 1, paramLMSContext.getSignedPubKeys(), LMS.generateSign(paramLMSContext));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean verifySignature(HSSPublicKeyParameters paramHSSPublicKeyParameters, HSSSignature paramHSSSignature, byte[] paramArrayOfbyte) {
/* 176 */     int i = paramHSSSignature.getlMinus1();
/* 177 */     if (i + 1 != paramHSSPublicKeyParameters.getL())
/*     */     {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     LMSSignature[] arrayOfLMSSignature = new LMSSignature[i + 1];
/* 183 */     LMSPublicKeyParameters[] arrayOfLMSPublicKeyParameters = new LMSPublicKeyParameters[i];
/*     */     
/* 185 */     for (byte b1 = 0; b1 < i; b1++) {
/*     */       
/* 187 */       arrayOfLMSSignature[b1] = paramHSSSignature.getSignedPubKey()[b1].getSignature();
/* 188 */       arrayOfLMSPublicKeyParameters[b1] = paramHSSSignature.getSignedPubKey()[b1].getPublicKey();
/*     */     } 
/* 190 */     arrayOfLMSSignature[i] = paramHSSSignature.getSignature();
/*     */     
/* 192 */     LMSPublicKeyParameters lMSPublicKeyParameters = paramHSSPublicKeyParameters.getLMSPublicKey();
/*     */     
/* 194 */     for (byte b2 = 0; b2 < i; b2++) {
/*     */       
/* 196 */       LMSSignature lMSSignature = arrayOfLMSSignature[b2];
/* 197 */       byte[] arrayOfByte = arrayOfLMSPublicKeyParameters[b2].toByteArray();
/* 198 */       if (!LMS.verifySignature(lMSPublicKeyParameters, lMSSignature, arrayOfByte))
/*     */       {
/* 200 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 204 */         lMSPublicKeyParameters = arrayOfLMSPublicKeyParameters[b2];
/*     */       }
/* 206 */       catch (Exception exception) {
/*     */         
/* 208 */         throw new IllegalStateException(exception.getMessage(), exception);
/*     */       } 
/*     */     } 
/* 211 */     return LMS.verifySignature(lMSPublicKeyParameters, arrayOfLMSSignature[i], paramArrayOfbyte);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/HSS.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */