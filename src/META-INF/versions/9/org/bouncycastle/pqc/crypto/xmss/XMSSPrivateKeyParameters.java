/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDS;
/*     */ import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.Pack;
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
/*     */ 
/*     */ 
/*     */ public final class XMSSPrivateKeyParameters
/*     */   extends XMSSKeyParameters
/*     */   implements XMSSStoreableObjectInterface, Encodable
/*     */ {
/*     */   private final XMSSParameters params;
/*     */   private final byte[] secretKeySeed;
/*     */   private final byte[] secretKeyPRF;
/*     */   private final byte[] publicSeed;
/*     */   private final byte[] root;
/*     */   private volatile BDS bdsState;
/*     */   
/*     */   private XMSSPrivateKeyParameters(Builder paramBuilder) {
/*  45 */     super(true, Builder.access$000(paramBuilder).getTreeDigest());
/*  46 */     this.params = Builder.access$000(paramBuilder);
/*  47 */     if (this.params == null)
/*     */     {
/*  49 */       throw new NullPointerException("params == null");
/*     */     }
/*  51 */     int i = this.params.getTreeDigestSize();
/*  52 */     byte[] arrayOfByte = Builder.access$100(paramBuilder);
/*  53 */     if (arrayOfByte != null) {
/*     */ 
/*     */       
/*  56 */       int j = this.params.getHeight();
/*  57 */       byte b = 4;
/*  58 */       int k = i;
/*  59 */       int m = i;
/*  60 */       int n = i;
/*  61 */       int i1 = i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  68 */       int i2 = 0;
/*  69 */       int i3 = Pack.bigEndianToInt(arrayOfByte, i2);
/*  70 */       if (!XMSSUtil.isIndexValid(j, i3))
/*     */       {
/*  72 */         throw new IllegalArgumentException("index out of bounds");
/*     */       }
/*  74 */       i2 += b;
/*  75 */       this.secretKeySeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, i2, k);
/*  76 */       i2 += k;
/*  77 */       this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(arrayOfByte, i2, m);
/*  78 */       i2 += m;
/*  79 */       this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, i2, n);
/*  80 */       i2 += n;
/*  81 */       this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, i2, i1);
/*  82 */       i2 += i1;
/*     */       
/*  84 */       byte[] arrayOfByte1 = XMSSUtil.extractBytesAtOffset(arrayOfByte, i2, arrayOfByte.length - i2);
/*     */       
/*     */       try {
/*  87 */         BDS bDS = (BDS)XMSSUtil.deserialize(arrayOfByte1, BDS.class);
/*  88 */         if (bDS.getIndex() != i3)
/*     */         {
/*  90 */           throw new IllegalStateException("serialized BDS has wrong index");
/*     */         }
/*  92 */         this.bdsState = bDS.withWOTSDigest(Builder.access$000(paramBuilder).getTreeDigestOID());
/*     */       }
/*  94 */       catch (IOException iOException) {
/*     */         
/*  96 */         throw new IllegalArgumentException(iOException.getMessage(), iOException);
/*     */       }
/*  98 */       catch (ClassNotFoundException classNotFoundException) {
/*     */         
/* 100 */         throw new IllegalArgumentException(classNotFoundException.getMessage(), classNotFoundException);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 106 */       byte[] arrayOfByte1 = Builder.access$200(paramBuilder);
/* 107 */       if (arrayOfByte1 != null) {
/*     */         
/* 109 */         if (arrayOfByte1.length != i)
/*     */         {
/* 111 */           throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
/*     */         }
/* 113 */         this.secretKeySeed = arrayOfByte1;
/*     */       }
/*     */       else {
/*     */         
/* 117 */         this.secretKeySeed = new byte[i];
/*     */       } 
/* 119 */       byte[] arrayOfByte2 = Builder.access$300(paramBuilder);
/* 120 */       if (arrayOfByte2 != null) {
/*     */         
/* 122 */         if (arrayOfByte2.length != i)
/*     */         {
/* 124 */           throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
/*     */         }
/* 126 */         this.secretKeyPRF = arrayOfByte2;
/*     */       }
/*     */       else {
/*     */         
/* 130 */         this.secretKeyPRF = new byte[i];
/*     */       } 
/* 132 */       byte[] arrayOfByte3 = Builder.access$400(paramBuilder);
/* 133 */       if (arrayOfByte3 != null) {
/*     */         
/* 135 */         if (arrayOfByte3.length != i)
/*     */         {
/* 137 */           throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
/*     */         }
/* 139 */         this.publicSeed = arrayOfByte3;
/*     */       }
/*     */       else {
/*     */         
/* 143 */         this.publicSeed = new byte[i];
/*     */       } 
/* 145 */       byte[] arrayOfByte4 = Builder.access$500(paramBuilder);
/* 146 */       if (arrayOfByte4 != null) {
/*     */         
/* 148 */         if (arrayOfByte4.length != i)
/*     */         {
/* 150 */           throw new IllegalArgumentException("size of root needs to be equal size of digest");
/*     */         }
/* 152 */         this.root = arrayOfByte4;
/*     */       }
/*     */       else {
/*     */         
/* 156 */         this.root = new byte[i];
/*     */       } 
/* 158 */       BDS bDS = Builder.access$600(paramBuilder);
/* 159 */       if (bDS != null) {
/*     */         
/* 161 */         this.bdsState = bDS;
/*     */ 
/*     */       
/*     */       }
/* 165 */       else if (Builder.access$700(paramBuilder) < (1 << this.params.getHeight()) - 2 && arrayOfByte3 != null && arrayOfByte1 != null) {
/*     */         
/* 167 */         this.bdsState = new BDS(this.params, arrayOfByte3, arrayOfByte1, (OTSHashAddress)(new OTSHashAddress.Builder()).build(), Builder.access$700(paramBuilder));
/*     */       }
/*     */       else {
/*     */         
/* 171 */         this.bdsState = new BDS(this.params, (1 << this.params.getHeight()) - 1, Builder.access$700(paramBuilder));
/*     */       } 
/*     */       
/* 174 */       if (Builder.access$800(paramBuilder) >= 0 && Builder.access$800(paramBuilder) != this.bdsState.getMaxIndex())
/*     */       {
/* 176 */         throw new IllegalArgumentException("maxIndex set but not reflected in state");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/* 183 */     synchronized (this) {
/*     */       
/* 185 */       return (this.bdsState.getMaxIndex() - getIndex() + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 192 */     synchronized (this) {
/*     */       
/* 194 */       return toByteArray();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters rollKey() {
/* 200 */     synchronized (this) {
/*     */ 
/*     */       
/* 203 */       if (this.bdsState.getIndex() < this.bdsState.getMaxIndex()) {
/*     */         
/* 205 */         this.bdsState = this.bdsState.getNextState(this.publicSeed, this.secretKeySeed, (OTSHashAddress)(new OTSHashAddress.Builder()).build());
/*     */       }
/*     */       else {
/*     */         
/* 209 */         this.bdsState = new BDS(this.params, this.bdsState.getMaxIndex(), this.bdsState.getMaxIndex() + 1);
/*     */       } 
/*     */       
/* 212 */       return this;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters getNextKey() {
/* 218 */     synchronized (this) {
/*     */       
/* 220 */       return extractKeyShard(1);
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
/*     */   public org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters extractKeyShard(int paramInt) {
/* 236 */     if (paramInt < 1)
/*     */     {
/* 238 */       throw new IllegalArgumentException("cannot ask for a shard with 0 keys");
/*     */     }
/* 240 */     synchronized (this) {
/*     */ 
/*     */       
/* 243 */       if (paramInt <= getUsagesRemaining()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 250 */         org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters xMSSPrivateKeyParameters = (new Builder(this.params)).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root).withIndex(getIndex()).withBDSState(this.bdsState.withMaxIndex(this.bdsState.getIndex() + paramInt - 1, this.params.getTreeDigestOID())).build();
/*     */         
/* 252 */         if (paramInt == getUsagesRemaining()) {
/*     */           
/* 254 */           this.bdsState = new BDS(this.params, this.bdsState.getMaxIndex(), getIndex() + paramInt);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 259 */           OTSHashAddress oTSHashAddress = (OTSHashAddress)(new OTSHashAddress.Builder()).build();
/* 260 */           for (int i = 0; i != paramInt; i++)
/*     */           {
/* 262 */             this.bdsState = this.bdsState.getNextState(this.publicSeed, this.secretKeySeed, oTSHashAddress);
/*     */           }
/*     */         } 
/*     */         
/* 266 */         return xMSSPrivateKeyParameters;
/*     */       } 
/*     */ 
/*     */       
/* 270 */       throw new IllegalArgumentException("usageCount exceeds usages remaining");
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
/*     */   public byte[] toByteArray() {
/* 355 */     synchronized (this) {
/*     */ 
/*     */       
/* 358 */       int i = this.params.getTreeDigestSize();
/* 359 */       byte b = 4;
/* 360 */       int j = i;
/* 361 */       int k = i;
/* 362 */       int m = i;
/* 363 */       int n = i;
/* 364 */       int i1 = b + j + k + m + n;
/* 365 */       byte[] arrayOfByte1 = new byte[i1];
/* 366 */       int i2 = 0;
/*     */       
/* 368 */       Pack.intToBigEndian(this.bdsState.getIndex(), arrayOfByte1, i2);
/* 369 */       i2 += b;
/*     */       
/* 371 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.secretKeySeed, i2);
/* 372 */       i2 += j;
/*     */       
/* 374 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.secretKeyPRF, i2);
/* 375 */       i2 += k;
/*     */       
/* 377 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.publicSeed, i2);
/* 378 */       i2 += m;
/*     */       
/* 380 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.root, i2);
/*     */       
/* 382 */       byte[] arrayOfByte2 = null;
/*     */       
/*     */       try {
/* 385 */         arrayOfByte2 = XMSSUtil.serialize(this.bdsState);
/*     */       }
/* 387 */       catch (IOException iOException) {
/*     */         
/* 389 */         throw new RuntimeException("error serializing bds state: " + iOException.getMessage());
/*     */       } 
/*     */       
/* 392 */       return Arrays.concatenate(arrayOfByte1, arrayOfByte2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 398 */     return this.bdsState.getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeySeed() {
/* 403 */     return XMSSUtil.cloneArray(this.secretKeySeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeyPRF() {
/* 408 */     return XMSSUtil.cloneArray(this.secretKeyPRF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicSeed() {
/* 413 */     return XMSSUtil.cloneArray(this.publicSeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRoot() {
/* 418 */     return XMSSUtil.cloneArray(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   BDS getBDSState() {
/* 423 */     return this.bdsState;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSParameters getParameters() {
/* 428 */     return this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */