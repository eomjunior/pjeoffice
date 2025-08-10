/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSS;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMS;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContext;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSContextBasedSigner;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignature;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.SeedDerive;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ 
/*     */ public class HSSPrivateKeyParameters extends LMSKeyParameters implements LMSContextBasedSigner {
/*     */   private final int l;
/*  25 */   private long index = 0L; private final boolean isShard; private List<LMSPrivateKeyParameters> keys;
/*     */   private List<LMSSignature> sig;
/*     */   private final long indexLimit;
/*     */   private HSSPublicKeyParameters publicKey;
/*     */   
/*     */   public HSSPrivateKeyParameters(int paramInt, List<LMSPrivateKeyParameters> paramList, List<LMSSignature> paramList1, long paramLong1, long paramLong2) {
/*  31 */     super(true);
/*     */     
/*  33 */     this.l = paramInt;
/*  34 */     this.keys = Collections.unmodifiableList(paramList);
/*  35 */     this.sig = Collections.unmodifiableList(paramList1);
/*  36 */     this.index = paramLong1;
/*  37 */     this.indexLimit = paramLong2;
/*  38 */     this.isShard = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     resetKeyToIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   private HSSPrivateKeyParameters(int paramInt, List<LMSPrivateKeyParameters> paramList, List<LMSSignature> paramList1, long paramLong1, long paramLong2, boolean paramBoolean) {
/*  48 */     super(true);
/*     */     
/*  50 */     this.l = paramInt;
/*  51 */     this.keys = Collections.unmodifiableList(paramList);
/*  52 */     this.sig = Collections.unmodifiableList(paramList1);
/*  53 */     this.index = paramLong1;
/*  54 */     this.indexLimit = paramLong2;
/*  55 */     this.isShard = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters getInstance(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException {
/*  61 */     org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters hSSPrivateKeyParameters = getInstance(paramArrayOfbyte1);
/*     */     
/*  63 */     hSSPrivateKeyParameters.publicKey = HSSPublicKeyParameters.getInstance(paramArrayOfbyte2);
/*     */     
/*  65 */     return hSSPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters getInstance(Object paramObject) throws IOException {
/*  71 */     if (paramObject instanceof org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters)
/*     */     {
/*  73 */       return (org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters)paramObject;
/*     */     }
/*  75 */     if (paramObject instanceof DataInputStream) {
/*     */       
/*  77 */       if (((DataInputStream)paramObject).readInt() != 0)
/*     */       {
/*  79 */         throw new IllegalStateException("unknown version for hss private key");
/*     */       }
/*  81 */       int i = ((DataInputStream)paramObject).readInt();
/*  82 */       long l1 = ((DataInputStream)paramObject).readLong();
/*  83 */       long l2 = ((DataInputStream)paramObject).readLong();
/*  84 */       boolean bool = ((DataInputStream)paramObject).readBoolean();
/*     */       
/*  86 */       ArrayList<LMSPrivateKeyParameters> arrayList = new ArrayList();
/*  87 */       ArrayList<LMSSignature> arrayList1 = new ArrayList();
/*     */       byte b;
/*  89 */       for (b = 0; b < i; b++)
/*     */       {
/*  91 */         arrayList.add(LMSPrivateKeyParameters.getInstance(paramObject));
/*     */       }
/*     */       
/*  94 */       for (b = 0; b < i - 1; b++)
/*     */       {
/*  96 */         arrayList1.add(LMSSignature.getInstance(paramObject));
/*     */       }
/*     */       
/*  99 */       return new org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters(i, arrayList, arrayList1, l1, l2, bool);
/*     */     } 
/* 101 */     if (paramObject instanceof byte[]) {
/*     */       
/* 103 */       DataInputStream dataInputStream = null;
/*     */       
/*     */       try {
/* 106 */         dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[])paramObject));
/* 107 */         return getInstance(dataInputStream);
/*     */       }
/*     */       finally {
/*     */         
/* 111 */         if (dataInputStream != null)
/*     */         {
/* 113 */           dataInputStream.close();
/*     */         }
/*     */       } 
/*     */     } 
/* 117 */     if (paramObject instanceof InputStream)
/*     */     {
/* 119 */       return getInstance(Streams.readAll((InputStream)paramObject));
/*     */     }
/*     */     
/* 122 */     throw new IllegalArgumentException("cannot parse " + paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getL() {
/* 127 */     return this.l;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long getIndex() {
/* 132 */     return this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized LMSParameters[] getLMSParameters() {
/* 137 */     int i = this.keys.size();
/*     */     
/* 139 */     LMSParameters[] arrayOfLMSParameters = new LMSParameters[i];
/*     */     
/* 141 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 143 */       LMSPrivateKeyParameters lMSPrivateKeyParameters = this.keys.get(b);
/*     */       
/* 145 */       arrayOfLMSParameters[b] = new LMSParameters(lMSPrivateKeyParameters.getSigParameters(), lMSPrivateKeyParameters.getOtsParameters());
/*     */     } 
/*     */     
/* 148 */     return arrayOfLMSParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void incIndex() {
/* 153 */     this.index++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters makeCopy(org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters paramHSSPrivateKeyParameters) {
/*     */     try {
/* 160 */       return getInstance(paramHSSPrivateKeyParameters.getEncoded());
/*     */     }
/* 162 */     catch (Exception exception) {
/*     */       
/* 164 */       throw new RuntimeException(exception.getMessage(), exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateHierarchy(LMSPrivateKeyParameters[] paramArrayOfLMSPrivateKeyParameters, LMSSignature[] paramArrayOfLMSSignature) {
/* 170 */     synchronized (this) {
/*     */       
/* 172 */       this.keys = Collections.unmodifiableList(Arrays.asList(paramArrayOfLMSPrivateKeyParameters));
/* 173 */       this.sig = Collections.unmodifiableList(Arrays.asList(paramArrayOfLMSSignature));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isShard() {
/* 179 */     return this.isShard;
/*     */   }
/*     */ 
/*     */   
/*     */   long getIndexLimit() {
/* 184 */     return this.indexLimit;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/* 189 */     return this.indexLimit - this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   LMSPrivateKeyParameters getRootKey() {
/* 194 */     return this.keys.get(0);
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
/*     */   public org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters extractKeyShard(int paramInt) {
/* 208 */     synchronized (this) {
/*     */ 
/*     */       
/* 211 */       if (getUsagesRemaining() < paramInt)
/*     */       {
/* 213 */         throw new IllegalArgumentException("usageCount exceeds usages remaining in current leaf");
/*     */       }
/*     */       
/* 216 */       long l1 = this.index + paramInt;
/* 217 */       long l2 = this.index;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 222 */       this.index += paramInt;
/*     */       
/* 224 */       ArrayList<LMSPrivateKeyParameters> arrayList = new ArrayList<>(getKeys());
/* 225 */       ArrayList<LMSSignature> arrayList1 = new ArrayList<>(getSig());
/*     */       
/* 227 */       org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters hSSPrivateKeyParameters = makeCopy(new org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters(this.l, arrayList, arrayList1, l2, l1, true));
/*     */       
/* 229 */       resetKeyToIndex();
/*     */       
/* 231 */       return hSSPrivateKeyParameters;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized List<LMSPrivateKeyParameters> getKeys() {
/* 238 */     return this.keys;
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized List<LMSSignature> getSig() {
/* 243 */     return this.sig;
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
/*     */   void resetKeyToIndex() {
/* 255 */     List<LMSPrivateKeyParameters> list = getKeys();
/*     */ 
/*     */     
/* 258 */     long[] arrayOfLong = new long[list.size()];
/* 259 */     long l = getIndex();
/*     */     int i;
/* 261 */     for (i = list.size() - 1; i >= 0; i--) {
/*     */       
/* 263 */       LMSigParameters lMSigParameters = ((LMSPrivateKeyParameters)list.get(i)).getSigParameters();
/* 264 */       int j = (1 << lMSigParameters.getH()) - 1;
/* 265 */       arrayOfLong[i] = l & j;
/* 266 */       l >>>= lMSigParameters.getH();
/*     */     } 
/*     */     
/* 269 */     i = 0;
/* 270 */     LMSPrivateKeyParameters[] arrayOfLMSPrivateKeyParameters = list.<LMSPrivateKeyParameters>toArray(new LMSPrivateKeyParameters[list.size()]);
/* 271 */     LMSSignature[] arrayOfLMSSignature = this.sig.<LMSSignature>toArray(new LMSSignature[this.sig.size()]);
/*     */     
/* 273 */     LMSPrivateKeyParameters lMSPrivateKeyParameters = getRootKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     if ((arrayOfLMSPrivateKeyParameters[0].getIndex() - 1) != arrayOfLong[0]) {
/*     */       
/* 281 */       arrayOfLMSPrivateKeyParameters[0] = LMS.generateKeys(lMSPrivateKeyParameters
/* 282 */           .getSigParameters(), lMSPrivateKeyParameters
/* 283 */           .getOtsParameters(), (int)arrayOfLong[0], lMSPrivateKeyParameters
/* 284 */           .getI(), lMSPrivateKeyParameters.getMasterSecret());
/* 285 */       i = 1;
/*     */     } 
/*     */ 
/*     */     
/* 289 */     for (byte b = 1; b < arrayOfLong.length; b++) {
/*     */ 
/*     */       
/* 292 */       LMSPrivateKeyParameters lMSPrivateKeyParameters1 = arrayOfLMSPrivateKeyParameters[b - 1];
/*     */       
/* 294 */       byte[] arrayOfByte1 = new byte[16];
/* 295 */       byte[] arrayOfByte2 = new byte[32];
/*     */ 
/*     */ 
/*     */       
/* 299 */       SeedDerive seedDerive = new SeedDerive(lMSPrivateKeyParameters1.getI(), lMSPrivateKeyParameters1.getMasterSecret(), DigestUtil.getDigest(lMSPrivateKeyParameters1.getOtsParameters().getDigestOID()));
/* 300 */       seedDerive.setQ((int)arrayOfLong[b - 1]);
/* 301 */       seedDerive.setJ(-2);
/*     */       
/* 303 */       seedDerive.deriveSeed(arrayOfByte2, true);
/* 304 */       byte[] arrayOfByte3 = new byte[32];
/* 305 */       seedDerive.deriveSeed(arrayOfByte3, false);
/* 306 */       System.arraycopy(arrayOfByte3, 0, arrayOfByte1, 0, arrayOfByte1.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       boolean bool1 = (b < arrayOfLong.length - 1) ? ((arrayOfLong[b] == (arrayOfLMSPrivateKeyParameters[b].getIndex() - 1)) ? true : false) : ((arrayOfLong[b] == arrayOfLMSPrivateKeyParameters[b].getIndex()) ? true : false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 321 */       boolean bool2 = (Arrays.areEqual(arrayOfByte1, arrayOfLMSPrivateKeyParameters[b].getI()) && Arrays.areEqual(arrayOfByte2, arrayOfLMSPrivateKeyParameters[b].getMasterSecret())) ? true : false;
/*     */ 
/*     */       
/* 324 */       if (!bool2) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 329 */         arrayOfLMSPrivateKeyParameters[b] = LMS.generateKeys(((LMSPrivateKeyParameters)list
/* 330 */             .get(b)).getSigParameters(), ((LMSPrivateKeyParameters)list
/* 331 */             .get(b)).getOtsParameters(), (int)arrayOfLong[b], arrayOfByte1, arrayOfByte2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 337 */         arrayOfLMSSignature[b - 1] = LMS.generateSign(arrayOfLMSPrivateKeyParameters[b - 1], arrayOfLMSPrivateKeyParameters[b].getPublicKey().toByteArray());
/* 338 */         i = 1;
/*     */       }
/* 340 */       else if (!bool1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 347 */         arrayOfLMSPrivateKeyParameters[b] = LMS.generateKeys(((LMSPrivateKeyParameters)list
/* 348 */             .get(b)).getSigParameters(), ((LMSPrivateKeyParameters)list
/* 349 */             .get(b)).getOtsParameters(), (int)arrayOfLong[b], arrayOfByte1, arrayOfByte2);
/*     */         
/* 351 */         i = 1;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (i != 0)
/*     */     {
/*     */       
/* 360 */       updateHierarchy(arrayOfLMSPrivateKeyParameters, arrayOfLMSSignature);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized HSSPublicKeyParameters getPublicKey() {
/* 367 */     return new HSSPublicKeyParameters(this.l, getRootKey().getPublicKey());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void replaceConsumedKey(int paramInt) {
/* 373 */     SeedDerive seedDerive = ((LMSPrivateKeyParameters)this.keys.get(paramInt - 1)).getCurrentOTSKey().getDerivationFunction();
/* 374 */     seedDerive.setJ(-2);
/* 375 */     byte[] arrayOfByte1 = new byte[32];
/* 376 */     seedDerive.deriveSeed(arrayOfByte1, true);
/* 377 */     byte[] arrayOfByte2 = new byte[32];
/* 378 */     seedDerive.deriveSeed(arrayOfByte2, false);
/* 379 */     byte[] arrayOfByte3 = new byte[16];
/* 380 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, arrayOfByte3.length);
/*     */     
/* 382 */     ArrayList<LMSPrivateKeyParameters> arrayList = new ArrayList<>(this.keys);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     LMSPrivateKeyParameters lMSPrivateKeyParameters = this.keys.get(paramInt);
/*     */ 
/*     */     
/* 390 */     arrayList.set(paramInt, LMS.generateKeys(lMSPrivateKeyParameters.getSigParameters(), lMSPrivateKeyParameters.getOtsParameters(), 0, arrayOfByte3, arrayOfByte1));
/*     */     
/* 392 */     ArrayList<LMSSignature> arrayList1 = new ArrayList<>(this.sig);
/*     */     
/* 394 */     arrayList1.set(paramInt - 1, LMS.generateSign(arrayList.get(paramInt - 1), ((LMSPrivateKeyParameters)arrayList.get(paramInt)).getPublicKey().toByteArray()));
/*     */ 
/*     */     
/* 397 */     this.keys = Collections.unmodifiableList(arrayList);
/* 398 */     this.sig = Collections.unmodifiableList(arrayList1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 405 */     if (this == paramObject)
/*     */     {
/* 407 */       return true;
/*     */     }
/* 409 */     if (paramObject == null || getClass() != paramObject.getClass())
/*     */     {
/* 411 */       return false;
/*     */     }
/*     */     
/* 414 */     org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters hSSPrivateKeyParameters = (org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters)paramObject;
/*     */     
/* 416 */     if (this.l != hSSPrivateKeyParameters.l)
/*     */     {
/* 418 */       return false;
/*     */     }
/* 420 */     if (this.isShard != hSSPrivateKeyParameters.isShard)
/*     */     {
/* 422 */       return false;
/*     */     }
/* 424 */     if (this.indexLimit != hSSPrivateKeyParameters.indexLimit)
/*     */     {
/* 426 */       return false;
/*     */     }
/* 428 */     if (this.index != hSSPrivateKeyParameters.index)
/*     */     {
/* 430 */       return false;
/*     */     }
/* 432 */     if (!this.keys.equals(hSSPrivateKeyParameters.keys))
/*     */     {
/* 434 */       return false;
/*     */     }
/* 436 */     return this.sig.equals(hSSPrivateKeyParameters.sig);
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
/*     */   public synchronized byte[] getEncoded() throws IOException {
/* 452 */     Composer composer = Composer.compose().u32str(0).u32str(this.l).u64str(this.index).u64str(this.indexLimit).bool(this.isShard);
/*     */     
/* 454 */     for (LMSPrivateKeyParameters lMSPrivateKeyParameters : this.keys)
/*     */     {
/* 456 */       composer.bytes((Encodable)lMSPrivateKeyParameters);
/*     */     }
/*     */     
/* 459 */     for (LMSSignature lMSSignature : this.sig)
/*     */     {
/* 461 */       composer.bytes((Encodable)lMSSignature);
/*     */     }
/*     */     
/* 464 */     return composer.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 470 */     int i = this.l;
/* 471 */     i = 31 * i + (this.isShard ? 1 : 0);
/* 472 */     i = 31 * i + this.keys.hashCode();
/* 473 */     i = 31 * i + this.sig.hashCode();
/* 474 */     i = 31 * i + (int)(this.indexLimit ^ this.indexLimit >>> 32L);
/* 475 */     i = 31 * i + (int)(this.index ^ this.index >>> 32L);
/* 476 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object clone() throws CloneNotSupportedException {
/* 483 */     return makeCopy(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSContext generateLMSContext() {
/*     */     LMSSignedPubKey[] arrayOfLMSSignedPubKey;
/*     */     LMSPrivateKeyParameters lMSPrivateKeyParameters;
/* 490 */     int i = getL();
/*     */     
/* 492 */     synchronized (this) {
/*     */       
/* 494 */       HSS.rangeTestKeys(this);
/*     */       
/* 496 */       List<LMSPrivateKeyParameters> list = getKeys();
/* 497 */       List<LMSSignature> list1 = getSig();
/*     */       
/* 499 */       lMSPrivateKeyParameters = getKeys().get(i - 1);
/*     */ 
/*     */       
/* 502 */       int j = 0;
/* 503 */       arrayOfLMSSignedPubKey = new LMSSignedPubKey[i - 1];
/* 504 */       while (j < i - 1) {
/*     */         
/* 506 */         arrayOfLMSSignedPubKey[j] = new LMSSignedPubKey(list1
/* 507 */             .get(j), ((LMSPrivateKeyParameters)list
/* 508 */             .get(j + 1)).getPublicKey());
/* 509 */         j++;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 515 */       incIndex();
/*     */     } 
/*     */     
/* 518 */     return lMSPrivateKeyParameters.generateLMSContext().withSignedPublicKeys(arrayOfLMSSignedPubKey);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generateSignature(LMSContext paramLMSContext) {
/*     */     try {
/* 525 */       return HSS.generateSignature(getL(), paramLMSContext).getEncoded();
/*     */     }
/* 527 */     catch (IOException iOException) {
/*     */       
/* 529 */       throw new IllegalStateException("unable to encode signature: " + iOException.getMessage(), iOException);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/HSSPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */