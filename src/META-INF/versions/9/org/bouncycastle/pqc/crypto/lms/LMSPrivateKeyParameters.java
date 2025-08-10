/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.pqc.crypto.ExhaustedPrivateKeyException;
/*     */ import org.bouncycastle.pqc.crypto.lms.DigestUtil;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMOtsPrivateKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContextBasedSigner;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class LMSPrivateKeyParameters extends LMSKeyParameters implements LMSContextBasedSigner {
/*  19 */   private static CacheKey T1 = new CacheKey(1);
/*  20 */   private static CacheKey[] internedKeys = new CacheKey[129]; private final byte[] I; private final LMSigParameters parameters; private final LMOtsParameters otsParameters; private final int maxQ;
/*     */   private final byte[] masterSecret;
/*     */   
/*     */   static {
/*  24 */     internedKeys[1] = T1;
/*  25 */     for (byte b = 2; b < internedKeys.length; b++)
/*     */     {
/*  27 */       internedKeys[b] = new CacheKey(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<CacheKey, byte[]> tCache;
/*     */ 
/*     */   
/*     */   private final int maxCacheR;
/*     */ 
/*     */   
/*     */   private final Digest tDigest;
/*     */ 
/*     */   
/*     */   private int q;
/*     */ 
/*     */   
/*     */   private LMSPublicKeyParameters publicKey;
/*     */ 
/*     */ 
/*     */   
/*     */   public LMSPrivateKeyParameters(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters, int paramInt1, byte[] paramArrayOfbyte1, int paramInt2, byte[] paramArrayOfbyte2) {
/*  51 */     super(true);
/*  52 */     this.parameters = paramLMSigParameters;
/*  53 */     this.otsParameters = paramLMOtsParameters;
/*  54 */     this.q = paramInt1;
/*  55 */     this.I = Arrays.clone(paramArrayOfbyte1);
/*  56 */     this.maxQ = paramInt2;
/*  57 */     this.masterSecret = Arrays.clone(paramArrayOfbyte2);
/*  58 */     this.maxCacheR = 1 << this.parameters.getH() + 1;
/*  59 */     this.tCache = (Map)new WeakHashMap<>();
/*  60 */     this.tDigest = DigestUtil.getDigest(paramLMSigParameters.getDigestOID());
/*     */   }
/*     */ 
/*     */   
/*     */   private LMSPrivateKeyParameters(org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters paramLMSPrivateKeyParameters, int paramInt1, int paramInt2) {
/*  65 */     super(true);
/*  66 */     this.parameters = paramLMSPrivateKeyParameters.parameters;
/*  67 */     this.otsParameters = paramLMSPrivateKeyParameters.otsParameters;
/*  68 */     this.q = paramInt1;
/*  69 */     this.I = paramLMSPrivateKeyParameters.I;
/*  70 */     this.maxQ = paramInt2;
/*  71 */     this.masterSecret = paramLMSPrivateKeyParameters.masterSecret;
/*  72 */     this.maxCacheR = 1 << this.parameters.getH();
/*  73 */     this.tCache = paramLMSPrivateKeyParameters.tCache;
/*  74 */     this.tDigest = DigestUtil.getDigest(this.parameters.getDigestOID());
/*  75 */     this.publicKey = paramLMSPrivateKeyParameters.publicKey;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters getInstance(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException {
/*  81 */     org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters lMSPrivateKeyParameters = getInstance(paramArrayOfbyte1);
/*     */     
/*  83 */     lMSPrivateKeyParameters.publicKey = LMSPublicKeyParameters.getInstance(paramArrayOfbyte2);
/*     */     
/*  85 */     return lMSPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters getInstance(Object paramObject) throws IOException {
/*  91 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters)
/*     */     {
/*  93 */       return (org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters)paramObject;
/*     */     }
/*  95 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  97 */       DataInputStream dataInputStream = (DataInputStream)paramObject;
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
/*     */ 
/*     */       
/* 112 */       if (dataInputStream.readInt() != 0)
/*     */       {
/* 114 */         throw new IllegalStateException("expected version 0 lms private key");
/*     */       }
/*     */       
/* 117 */       LMSigParameters lMSigParameters = LMSigParameters.getParametersForType(dataInputStream.readInt());
/* 118 */       LMOtsParameters lMOtsParameters = LMOtsParameters.getParametersForType(dataInputStream.readInt());
/* 119 */       byte[] arrayOfByte1 = new byte[16];
/* 120 */       dataInputStream.readFully(arrayOfByte1);
/*     */       
/* 122 */       int i = dataInputStream.readInt();
/* 123 */       int j = dataInputStream.readInt();
/* 124 */       int k = dataInputStream.readInt();
/* 125 */       if (k < 0)
/*     */       {
/* 127 */         throw new IllegalStateException("secret length less than zero");
/*     */       }
/* 129 */       if (k > dataInputStream.available())
/*     */       {
/* 131 */         throw new IOException("secret length exceeded " + dataInputStream.available());
/*     */       }
/* 133 */       byte[] arrayOfByte2 = new byte[k];
/* 134 */       dataInputStream.readFully(arrayOfByte2);
/*     */       
/* 136 */       return new org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters(lMSigParameters, lMOtsParameters, i, arrayOfByte1, j, arrayOfByte2);
/*     */     } 
/*     */     
/* 139 */     if (paramObject instanceof byte[]) {
/*     */       
/* 141 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/* 144 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/* 145 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/* 149 */         if (dataInputStream != null)
/*     */         {
/* 151 */           dataInputStream.close();
/*     */         }
/*     */       } 
/*     */     } 
/* 155 */     if (paramObject instanceof InputStream)
/*     */     {
/* 157 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/* 160 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LMOtsPrivateKey getCurrentOTSKey() {
/* 166 */     synchronized (this) {
/*     */       
/* 168 */       if (this.q >= this.maxQ)
/*     */       {
/* 170 */         throw new ExhaustedPrivateKeyException("ots private keys expired");
/*     */       }
/* 172 */       return new LMOtsPrivateKey(this.otsParameters, this.I, this.q, this.masterSecret);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getIndex() {
/* 183 */     return this.q;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void incIndex() {
/* 188 */     this.q++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LMSContext generateLMSContext() {
/* 194 */     LMSigParameters lMSigParameters = getSigParameters();
/*     */ 
/*     */     
/* 197 */     int i = lMSigParameters.getH();
/* 198 */     int j = getIndex();
/* 199 */     LMOtsPrivateKey lMOtsPrivateKey = getNextOtsPrivateKey();
/*     */     
/* 201 */     byte b = 0;
/* 202 */     int k = (1 << i) + j;
/* 203 */     byte[][] arrayOfByte = new byte[i][];
/*     */     
/* 205 */     while (b < i) {
/*     */       
/* 207 */       int m = k / (1 << b) ^ 0x1;
/*     */       
/* 209 */       arrayOfByte[b] = findT(m);
/* 210 */       b++;
/*     */     } 
/*     */     
/* 213 */     return lMOtsPrivateKey.getSignatureContext(getSigParameters(), arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generateSignature(LMSContext paramLMSContext) {
/*     */     try {
/* 220 */       return LMS.generateSign(paramLMSContext).getEncoded();
/*     */     }
/* 222 */     catch (IOException iOException) {
/*     */       
/* 224 */       throw new IllegalStateException("unable to encode signature: " + iOException.getMessage(), iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   LMOtsPrivateKey getNextOtsPrivateKey() {
/* 230 */     synchronized (this) {
/*     */       
/* 232 */       if (this.q >= this.maxQ)
/*     */       {
/* 234 */         throw new ExhaustedPrivateKeyException("ots private key exhausted");
/*     */       }
/* 236 */       LMOtsPrivateKey lMOtsPrivateKey = new LMOtsPrivateKey(this.otsParameters, this.I, this.q, this.masterSecret);
/* 237 */       incIndex();
/* 238 */       return lMOtsPrivateKey;
/*     */     } 
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
/*     */   public org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters extractKeyShard(int paramInt) {
/* 254 */     synchronized (this) {
/*     */       
/* 256 */       if (this.q + paramInt >= this.maxQ)
/*     */       {
/* 258 */         throw new IllegalArgumentException("usageCount exceeds usages remaining");
/*     */       }
/* 260 */       org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters lMSPrivateKeyParameters = new org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters(this, this.q, this.q + paramInt);
/* 261 */       this.q += paramInt;
/*     */       
/* 263 */       return lMSPrivateKeyParameters;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSigParameters getSigParameters() {
/* 269 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public LMOtsParameters getOtsParameters() {
/* 274 */     return this.otsParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getI() {
/* 279 */     return Arrays.clone(this.I);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getMasterSecret() {
/* 284 */     return Arrays.clone(this.masterSecret);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/* 289 */     return (this.maxQ - this.q);
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSPublicKeyParameters getPublicKey() {
/* 294 */     synchronized (this) {
/*     */       
/* 296 */       if (this.publicKey == null)
/*     */       {
/* 298 */         this.publicKey = new LMSPublicKeyParameters(this.parameters, this.otsParameters, findT(T1), this.I);
/*     */       }
/* 300 */       return this.publicKey;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] findT(int paramInt) {
/* 306 */     if (paramInt < this.maxCacheR)
/*     */     {
/* 308 */       return findT((paramInt < internedKeys.length) ? internedKeys[paramInt] : new CacheKey(paramInt));
/*     */     }
/*     */     
/* 311 */     return calcT(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] findT(CacheKey paramCacheKey) {
/* 316 */     synchronized (this.tCache) {
/*     */       
/* 318 */       byte[] arrayOfByte = this.tCache.get(paramCacheKey);
/*     */       
/* 320 */       if (arrayOfByte != null)
/*     */       {
/* 322 */         return arrayOfByte;
/*     */       }
/*     */       
/* 325 */       arrayOfByte = calcT(CacheKey.access$000(paramCacheKey));
/* 326 */       this.tCache.put(paramCacheKey, arrayOfByte);
/*     */       
/* 328 */       return arrayOfByte;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] calcT(int paramInt) {
/* 334 */     int i = getSigParameters().getH();
/*     */     
/* 336 */     int j = 1 << i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     if (paramInt >= j) {
/*     */       
/* 344 */       LmsUtils.byteArray(getI(), this.tDigest);
/* 345 */       LmsUtils.u32str(paramInt, this.tDigest);
/* 346 */       LmsUtils.u16str((short)-32126, this.tDigest);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 351 */       byte[] arrayOfByte5 = LM_OTS.lms_ots_generatePublicKey(getOtsParameters(), getI(), paramInt - j, getMasterSecret());
/*     */       
/* 353 */       LmsUtils.byteArray(arrayOfByte5, this.tDigest);
/* 354 */       byte[] arrayOfByte4 = new byte[this.tDigest.getDigestSize()];
/* 355 */       this.tDigest.doFinal(arrayOfByte4, 0);
/* 356 */       return arrayOfByte4;
/*     */     } 
/*     */     
/* 359 */     byte[] arrayOfByte2 = findT(2 * paramInt);
/* 360 */     byte[] arrayOfByte3 = findT(2 * paramInt + 1);
/*     */     
/* 362 */     LmsUtils.byteArray(getI(), this.tDigest);
/* 363 */     LmsUtils.u32str(paramInt, this.tDigest);
/* 364 */     LmsUtils.u16str((short)-31869, this.tDigest);
/* 365 */     LmsUtils.byteArray(arrayOfByte2, this.tDigest);
/* 366 */     LmsUtils.byteArray(arrayOfByte3, this.tDigest);
/* 367 */     byte[] arrayOfByte1 = new byte[this.tDigest.getDigestSize()];
/* 368 */     this.tDigest.doFinal(arrayOfByte1, 0);
/*     */     
/* 370 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 376 */     if (this == paramObject)
/*     */     {
/* 378 */       return true;
/*     */     }
/* 380 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/* 382 */       return false;
/*     */     }
/*     */     
/* 385 */     org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters lMSPrivateKeyParameters = (org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters)paramObject;
/*     */     
/* 387 */     if (this.q != lMSPrivateKeyParameters.q)
/*     */     {
/* 389 */       return false;
/*     */     }
/* 391 */     if (this.maxQ != lMSPrivateKeyParameters.maxQ)
/*     */     {
/* 393 */       return false;
/*     */     }
/* 395 */     if (!Arrays.areEqual(this.I, lMSPrivateKeyParameters.I))
/*     */     {
/* 397 */       return false;
/*     */     }
/* 399 */     if ((this.parameters != null) ? !this.parameters.equals(lMSPrivateKeyParameters.parameters) : (lMSPrivateKeyParameters.parameters != null))
/*     */     {
/* 401 */       return false;
/*     */     }
/* 403 */     if ((this.otsParameters != null) ? !this.otsParameters.equals(lMSPrivateKeyParameters.otsParameters) : (lMSPrivateKeyParameters.otsParameters != null))
/*     */     {
/* 405 */       return false;
/*     */     }
/* 407 */     if (!Arrays.areEqual(this.masterSecret, lMSPrivateKeyParameters.masterSecret))
/*     */     {
/* 409 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     if (this.publicKey != null && lMSPrivateKeyParameters.publicKey != null)
/*     */     {
/* 418 */       return this.publicKey.equals(lMSPrivateKeyParameters.publicKey);
/*     */     }
/*     */     
/* 421 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 427 */     int i = this.q;
/* 428 */     i = 31 * i + Arrays.hashCode(this.I);
/* 429 */     i = 31 * i + ((this.parameters != null) ? this.parameters.hashCode() : 0);
/* 430 */     i = 31 * i + ((this.otsParameters != null) ? this.otsParameters.hashCode() : 0);
/* 431 */     i = 31 * i + this.maxQ;
/* 432 */     i = 31 * i + Arrays.hashCode(this.masterSecret);
/* 433 */     i = 31 * i + ((this.publicKey != null) ? this.publicKey.hashCode() : 0);
/* 434 */     return i;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 455 */     return Composer.compose()
/* 456 */       .u32str(0)
/* 457 */       .u32str(this.parameters.getType())
/* 458 */       .u32str(this.otsParameters.getType())
/* 459 */       .bytes(this.I)
/* 460 */       .u32str(this.q)
/* 461 */       .u32str(this.maxQ)
/* 462 */       .u32str(this.masterSecret.length)
/* 463 */       .bytes(this.masterSecret)
/* 464 */       .build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */