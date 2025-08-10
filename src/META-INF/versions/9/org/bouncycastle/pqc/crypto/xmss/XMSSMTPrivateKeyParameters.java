/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDSStateMap;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ 
/*     */ 
/*     */ public final class XMSSMTPrivateKeyParameters
/*     */   extends XMSSMTKeyParameters
/*     */   implements XMSSStoreableObjectInterface, Encodable
/*     */ {
/*     */   private final XMSSMTParameters params;
/*     */   private final byte[] secretKeySeed;
/*     */   private final byte[] secretKeyPRF;
/*     */   private final byte[] publicSeed;
/*     */   private final byte[] root;
/*     */   private volatile long index;
/*     */   private volatile BDSStateMap bdsState;
/*     */   private volatile boolean used;
/*     */   
/*     */   private XMSSMTPrivateKeyParameters(Builder paramBuilder) {
/*  27 */     super(true, Builder.access$000(paramBuilder).getTreeDigest());
/*  28 */     this.params = Builder.access$000(paramBuilder);
/*     */     
/*  30 */     if (this.params == null)
/*     */     {
/*  32 */       throw new NullPointerException("params == null");
/*     */     }
/*  34 */     int i = this.params.getTreeDigestSize();
/*  35 */     byte[] arrayOfByte = Builder.access$100(paramBuilder);
/*  36 */     if (arrayOfByte != null) {
/*     */       
/*  38 */       if (Builder.access$200(paramBuilder) == null)
/*     */       {
/*  40 */         throw new NullPointerException("xmss == null");
/*     */       }
/*     */       
/*  43 */       int j = this.params.getHeight();
/*  44 */       int k = (j + 7) / 8;
/*  45 */       int m = i;
/*  46 */       int n = i;
/*  47 */       int i1 = i;
/*  48 */       int i2 = i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  55 */       int i3 = 0;
/*  56 */       this.index = XMSSUtil.bytesToXBigEndian(arrayOfByte, i3, k);
/*  57 */       if (!XMSSUtil.isIndexValid(j, this.index))
/*     */       {
/*  59 */         throw new IllegalArgumentException("index out of bounds");
/*     */       }
/*  61 */       i3 += k;
/*  62 */       this.secretKeySeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, i3, m);
/*  63 */       i3 += m;
/*  64 */       this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(arrayOfByte, i3, n);
/*  65 */       i3 += n;
/*  66 */       this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, i3, i1);
/*  67 */       i3 += i1;
/*  68 */       this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, i3, i2);
/*  69 */       i3 += i2;
/*     */       
/*  71 */       byte[] arrayOfByte1 = XMSSUtil.extractBytesAtOffset(arrayOfByte, i3, arrayOfByte.length - i3);
/*     */ 
/*     */       
/*     */       try {
/*  75 */         BDSStateMap bDSStateMap = (BDSStateMap)XMSSUtil.deserialize(arrayOfByte1, BDSStateMap.class);
/*     */         
/*  77 */         this.bdsState = bDSStateMap.withWOTSDigest(Builder.access$200(paramBuilder).getTreeDigestOID());
/*     */       }
/*  79 */       catch (IOException iOException) {
/*     */         
/*  81 */         throw new IllegalArgumentException(iOException.getMessage(), iOException);
/*     */       }
/*  83 */       catch (ClassNotFoundException classNotFoundException) {
/*     */         
/*  85 */         throw new IllegalArgumentException(classNotFoundException.getMessage(), classNotFoundException);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  91 */       this.index = Builder.access$300(paramBuilder);
/*  92 */       byte[] arrayOfByte1 = Builder.access$400(paramBuilder);
/*  93 */       if (arrayOfByte1 != null) {
/*     */         
/*  95 */         if (arrayOfByte1.length != i)
/*     */         {
/*  97 */           throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
/*     */         }
/*  99 */         this.secretKeySeed = arrayOfByte1;
/*     */       }
/*     */       else {
/*     */         
/* 103 */         this.secretKeySeed = new byte[i];
/*     */       } 
/* 105 */       byte[] arrayOfByte2 = Builder.access$500(paramBuilder);
/* 106 */       if (arrayOfByte2 != null) {
/*     */         
/* 108 */         if (arrayOfByte2.length != i)
/*     */         {
/* 110 */           throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
/*     */         }
/* 112 */         this.secretKeyPRF = arrayOfByte2;
/*     */       }
/*     */       else {
/*     */         
/* 116 */         this.secretKeyPRF = new byte[i];
/*     */       } 
/* 118 */       byte[] arrayOfByte3 = Builder.access$600(paramBuilder);
/* 119 */       if (arrayOfByte3 != null) {
/*     */         
/* 121 */         if (arrayOfByte3.length != i)
/*     */         {
/* 123 */           throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
/*     */         }
/* 125 */         this.publicSeed = arrayOfByte3;
/*     */       }
/*     */       else {
/*     */         
/* 129 */         this.publicSeed = new byte[i];
/*     */       } 
/* 131 */       byte[] arrayOfByte4 = Builder.access$700(paramBuilder);
/* 132 */       if (arrayOfByte4 != null) {
/*     */         
/* 134 */         if (arrayOfByte4.length != i)
/*     */         {
/* 136 */           throw new IllegalArgumentException("size of root needs to be equal size of digest");
/*     */         }
/* 138 */         this.root = arrayOfByte4;
/*     */       }
/*     */       else {
/*     */         
/* 142 */         this.root = new byte[i];
/*     */       } 
/* 144 */       BDSStateMap bDSStateMap = Builder.access$800(paramBuilder);
/* 145 */       if (bDSStateMap != null) {
/*     */         
/* 147 */         this.bdsState = bDSStateMap;
/*     */       }
/*     */       else {
/*     */         
/* 151 */         long l = Builder.access$300(paramBuilder);
/* 152 */         int j = this.params.getHeight();
/*     */         
/* 154 */         if (XMSSUtil.isIndexValid(j, l) && arrayOfByte3 != null && arrayOfByte1 != null) {
/*     */           
/* 156 */           this.bdsState = new BDSStateMap(this.params, Builder.access$300(paramBuilder), arrayOfByte3, arrayOfByte1);
/*     */         }
/*     */         else {
/*     */           
/* 160 */           this.bdsState = new BDSStateMap(Builder.access$900(paramBuilder) + 1L);
/*     */         } 
/*     */       } 
/* 163 */       if (Builder.access$900(paramBuilder) >= 0L && Builder.access$900(paramBuilder) != this.bdsState.getMaxIndex())
/*     */       {
/* 165 */         throw new IllegalArgumentException("maxIndex set but not reflected in state");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 173 */     synchronized (this) {
/*     */       
/* 175 */       return toByteArray();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 267 */     synchronized (this) {
/*     */ 
/*     */       
/* 270 */       int i = this.params.getTreeDigestSize();
/* 271 */       int j = (this.params.getHeight() + 7) / 8;
/* 272 */       int k = i;
/* 273 */       int m = i;
/* 274 */       int n = i;
/* 275 */       int i1 = i;
/* 276 */       int i2 = j + k + m + n + i1;
/* 277 */       byte[] arrayOfByte1 = new byte[i2];
/* 278 */       int i3 = 0;
/*     */       
/* 280 */       byte[] arrayOfByte2 = XMSSUtil.toBytesBigEndian(this.index, j);
/* 281 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, arrayOfByte2, i3);
/* 282 */       i3 += j;
/*     */       
/* 284 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.secretKeySeed, i3);
/* 285 */       i3 += k;
/*     */       
/* 287 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.secretKeyPRF, i3);
/* 288 */       i3 += m;
/*     */       
/* 290 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.publicSeed, i3);
/* 291 */       i3 += n;
/*     */       
/* 293 */       XMSSUtil.copyBytesAtOffset(arrayOfByte1, this.root, i3);
/*     */ 
/*     */       
/*     */       try {
/* 297 */         return Arrays.concatenate(arrayOfByte1, XMSSUtil.serialize(this.bdsState));
/*     */       }
/* 299 */       catch (IOException iOException) {
/*     */         
/* 301 */         throw new IllegalStateException("error serializing bds state: " + iOException.getMessage(), iOException);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIndex() {
/* 308 */     return this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/* 313 */     synchronized (this) {
/*     */       
/* 315 */       return this.bdsState.getMaxIndex() - getIndex() + 1L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeySeed() {
/* 321 */     return XMSSUtil.cloneArray(this.secretKeySeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeyPRF() {
/* 326 */     return XMSSUtil.cloneArray(this.secretKeyPRF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicSeed() {
/* 331 */     return XMSSUtil.cloneArray(this.publicSeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRoot() {
/* 336 */     return XMSSUtil.cloneArray(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   BDSStateMap getBDSState() {
/* 341 */     return this.bdsState;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSMTParameters getParameters() {
/* 346 */     return this.params;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters getNextKey() {
/* 351 */     synchronized (this) {
/*     */       
/* 353 */       return extractKeyShard(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters rollKey() {
/* 359 */     synchronized (this) {
/*     */       
/* 361 */       if (getIndex() < this.bdsState.getMaxIndex()) {
/*     */         
/* 363 */         this.bdsState.updateState(this.params, this.index, this.publicSeed, this.secretKeySeed);
/* 364 */         this.index++;
/* 365 */         this.used = false;
/*     */       }
/*     */       else {
/*     */         
/* 369 */         this.index = this.bdsState.getMaxIndex() + 1L;
/* 370 */         this.bdsState = new BDSStateMap(this.bdsState.getMaxIndex());
/* 371 */         this.used = false;
/*     */       } 
/*     */       
/* 374 */       return this;
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
/*     */   public org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters extractKeyShard(int paramInt) {
/* 388 */     if (paramInt < 1)
/*     */     {
/* 390 */       throw new IllegalArgumentException("cannot ask for a shard with 0 keys");
/*     */     }
/* 392 */     synchronized (this) {
/*     */ 
/*     */       
/* 395 */       if (paramInt <= getUsagesRemaining()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 401 */         org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParameters = (new Builder(this.params)).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root).withIndex(getIndex()).withBDSState(new BDSStateMap(this.bdsState, getIndex() + paramInt - 1L)).build();
/*     */         
/* 403 */         for (int i = 0; i != paramInt; i++)
/*     */         {
/* 405 */           rollKey();
/*     */         }
/*     */         
/* 408 */         return xMSSMTPrivateKeyParameters;
/*     */       } 
/*     */ 
/*     */       
/* 412 */       throw new IllegalArgumentException("usageCount exceeds usages remaining");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSMTPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */