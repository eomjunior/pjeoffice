/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.pqc.crypto.xmss.KeyedHashFunctions;
/*     */ import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusSignature;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ final class WOTSPlus
/*     */ {
/*     */   private final WOTSPlusParameters params;
/*     */   private final KeyedHashFunctions khf;
/*     */   private byte[] secretKeySeed;
/*     */   private byte[] publicSeed;
/*     */   
/*     */   WOTSPlus(WOTSPlusParameters paramWOTSPlusParameters) {
/*  40 */     if (paramWOTSPlusParameters == null)
/*     */     {
/*  42 */       throw new NullPointerException("params == null");
/*     */     }
/*  44 */     this.params = paramWOTSPlusParameters;
/*  45 */     int i = paramWOTSPlusParameters.getTreeDigestSize();
/*  46 */     this.khf = new KeyedHashFunctions(paramWOTSPlusParameters.getTreeDigest(), i);
/*  47 */     this.secretKeySeed = new byte[i];
/*  48 */     this.publicSeed = new byte[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void importKeys(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  59 */     if (paramArrayOfbyte1 == null)
/*     */     {
/*  61 */       throw new NullPointerException("secretKeySeed == null");
/*     */     }
/*  63 */     if (paramArrayOfbyte1.length != this.params.getTreeDigestSize())
/*     */     {
/*  65 */       throw new IllegalArgumentException("size of secretKeySeed needs to be equal to size of digest");
/*     */     }
/*  67 */     if (paramArrayOfbyte2 == null)
/*     */     {
/*  69 */       throw new NullPointerException("publicSeed == null");
/*     */     }
/*  71 */     if (paramArrayOfbyte2.length != this.params.getTreeDigestSize())
/*     */     {
/*  73 */       throw new IllegalArgumentException("size of publicSeed needs to be equal to size of digest");
/*     */     }
/*  75 */     this.secretKeySeed = paramArrayOfbyte1;
/*  76 */     this.publicSeed = paramArrayOfbyte2;
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
/*     */   WOTSPlusSignature sign(byte[] paramArrayOfbyte, OTSHashAddress paramOTSHashAddress) {
/*  88 */     if (paramArrayOfbyte == null)
/*     */     {
/*  90 */       throw new NullPointerException("messageDigest == null");
/*     */     }
/*  92 */     if (paramArrayOfbyte.length != this.params.getTreeDigestSize())
/*     */     {
/*  94 */       throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
/*     */     }
/*  96 */     if (paramOTSHashAddress == null)
/*     */     {
/*  98 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/* 100 */     List<Integer> list1 = convertToBaseW(paramArrayOfbyte, this.params.getWinternitzParameter(), this.params.getLen1());
/*     */     
/* 102 */     int i = 0; int j;
/* 103 */     for (j = 0; j < this.params.getLen1(); j++)
/*     */     {
/* 105 */       i += this.params.getWinternitzParameter() - 1 - ((Integer)list1.get(j)).intValue();
/*     */     }
/* 107 */     i <<= 8 - this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter()) % 8;
/*     */     
/* 109 */     j = (int)Math.ceil((this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter())) / 8.0D);
/* 110 */     List<Integer> list2 = convertToBaseW(XMSSUtil.toBytesBigEndian(i, j), this.params
/* 111 */         .getWinternitzParameter(), this.params.getLen2());
/*     */ 
/*     */     
/* 114 */     list1.addAll(list2);
/*     */ 
/*     */     
/* 117 */     byte[][] arrayOfByte = new byte[this.params.getLen()][];
/* 118 */     for (byte b = 0; b < this.params.getLen(); b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 124 */       paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).withChainAddress(b).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/* 125 */       arrayOfByte[b] = chain(expandSecretKeySeed(b), 0, ((Integer)list1.get(b)).intValue(), paramOTSHashAddress);
/*     */     } 
/* 127 */     return new WOTSPlusSignature(this.params, arrayOfByte);
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
/*     */   WOTSPlusPublicKeyParameters getPublicKeyFromSignature(byte[] paramArrayOfbyte, WOTSPlusSignature paramWOTSPlusSignature, OTSHashAddress paramOTSHashAddress) {
/* 141 */     if (paramArrayOfbyte == null)
/*     */     {
/* 143 */       throw new NullPointerException("messageDigest == null");
/*     */     }
/* 145 */     if (paramArrayOfbyte.length != this.params.getTreeDigestSize())
/*     */     {
/* 147 */       throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
/*     */     }
/* 149 */     if (paramWOTSPlusSignature == null)
/*     */     {
/* 151 */       throw new NullPointerException("signature == null");
/*     */     }
/* 153 */     if (paramOTSHashAddress == null)
/*     */     {
/* 155 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/* 157 */     List<Integer> list1 = convertToBaseW(paramArrayOfbyte, this.params.getWinternitzParameter(), this.params.getLen1());
/*     */     
/* 159 */     int i = 0; int j;
/* 160 */     for (j = 0; j < this.params.getLen1(); j++)
/*     */     {
/* 162 */       i += this.params.getWinternitzParameter() - 1 - ((Integer)list1.get(j)).intValue();
/*     */     }
/* 164 */     i <<= 8 - this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter()) % 8;
/*     */     
/* 166 */     j = (int)Math.ceil((this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter())) / 8.0D);
/* 167 */     List<Integer> list2 = convertToBaseW(XMSSUtil.toBytesBigEndian(i, j), this.params
/* 168 */         .getWinternitzParameter(), this.params.getLen2());
/*     */ 
/*     */     
/* 171 */     list1.addAll(list2);
/*     */     
/* 173 */     byte[][] arrayOfByte = new byte[this.params.getLen()][];
/* 174 */     for (byte b = 0; b < this.params.getLen(); b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).withChainAddress(b).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/* 181 */       arrayOfByte[b] = chain(paramWOTSPlusSignature.toByteArray()[b], ((Integer)list1.get(b)).intValue(), this.params
/* 182 */           .getWinternitzParameter() - 1 - ((Integer)list1.get(b)).intValue(), paramOTSHashAddress);
/*     */     } 
/* 184 */     return new WOTSPlusPublicKeyParameters(this.params, arrayOfByte);
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
/*     */   private byte[] chain(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, OTSHashAddress paramOTSHashAddress) {
/* 199 */     int i = this.params.getTreeDigestSize();
/* 200 */     if (paramArrayOfbyte == null)
/*     */     {
/* 202 */       throw new NullPointerException("startHash == null");
/*     */     }
/* 204 */     if (paramArrayOfbyte.length != i)
/*     */     {
/* 206 */       throw new IllegalArgumentException("startHash needs to be " + i + "bytes");
/*     */     }
/* 208 */     if (paramOTSHashAddress == null)
/*     */     {
/* 210 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/* 212 */     if (paramOTSHashAddress.toByteArray() == null)
/*     */     {
/* 214 */       throw new NullPointerException("otsHashAddress byte array == null");
/*     */     }
/* 216 */     if (paramInt1 + paramInt2 > this.params.getWinternitzParameter() - 1)
/*     */     {
/* 218 */       throw new IllegalArgumentException("max chain length must not be greater than w");
/*     */     }
/*     */     
/* 221 */     if (paramInt2 == 0)
/*     */     {
/* 223 */       return paramArrayOfbyte;
/*     */     }
/*     */     
/* 226 */     byte[] arrayOfByte1 = chain(paramArrayOfbyte, paramInt1, paramInt2 - 1, paramOTSHashAddress);
/*     */ 
/*     */ 
/*     */     
/* 230 */     paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).withChainAddress(paramOTSHashAddress.getChainAddress()).withHashAddress(paramInt1 + paramInt2 - 1).withKeyAndMask(0)).build();
/* 231 */     byte[] arrayOfByte2 = this.khf.PRF(this.publicSeed, paramOTSHashAddress.toByteArray());
/*     */ 
/*     */ 
/*     */     
/* 235 */     paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).withChainAddress(paramOTSHashAddress.getChainAddress()).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(1)).build();
/* 236 */     byte[] arrayOfByte3 = this.khf.PRF(this.publicSeed, paramOTSHashAddress.toByteArray());
/* 237 */     byte[] arrayOfByte4 = new byte[i];
/* 238 */     for (byte b = 0; b < i; b++)
/*     */     {
/* 240 */       arrayOfByte4[b] = (byte)(arrayOfByte1[b] ^ arrayOfByte3[b]);
/*     */     }
/* 242 */     arrayOfByte1 = this.khf.F(arrayOfByte2, arrayOfByte4);
/* 243 */     return arrayOfByte1;
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
/*     */   private List<Integer> convertToBaseW(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 256 */     if (paramArrayOfbyte == null)
/*     */     {
/* 258 */       throw new NullPointerException("msg == null");
/*     */     }
/* 260 */     if (paramInt1 != 4 && paramInt1 != 16)
/*     */     {
/* 262 */       throw new IllegalArgumentException("w needs to be 4 or 16");
/*     */     }
/* 264 */     int i = XMSSUtil.log2(paramInt1);
/* 265 */     if (paramInt2 > 8 * paramArrayOfbyte.length / i)
/*     */     {
/* 267 */       throw new IllegalArgumentException("outLength too big");
/*     */     }
/*     */     
/* 270 */     ArrayList<Integer> arrayList = new ArrayList();
/* 271 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       int j;
/* 273 */       for (j = 8 - i; j >= 0; j -= i) {
/*     */         
/* 275 */         arrayList.add(Integer.valueOf(paramArrayOfbyte[b] >> j & paramInt1 - 1));
/* 276 */         if (arrayList.size() == paramInt2)
/*     */         {
/* 278 */           return arrayList;
/*     */         }
/*     */       } 
/*     */     } 
/* 282 */     return arrayList;
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
/*     */   protected byte[] getWOTSPlusSecretKey(byte[] paramArrayOfbyte, OTSHashAddress paramOTSHashAddress) {
/* 296 */     paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).build();
/* 297 */     return this.khf.PRF(paramArrayOfbyte, paramOTSHashAddress.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] expandSecretKeySeed(int paramInt) {
/* 308 */     if (paramInt < 0 || paramInt >= this.params.getLen())
/*     */     {
/* 310 */       throw new IllegalArgumentException("index out of bounds");
/*     */     }
/* 312 */     return this.khf.PRF(this.secretKeySeed, XMSSUtil.toBytesBigEndian(paramInt, 32));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WOTSPlusParameters getParams() {
/* 322 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeyedHashFunctions getKhf() {
/* 332 */     return this.khf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] getSecretKeySeed() {
/* 342 */     return Arrays.clone(this.secretKeySeed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] getPublicSeed() {
/* 352 */     return Arrays.clone(this.publicSeed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WOTSPlusPrivateKeyParameters getPrivateKey() {
/* 362 */     byte[][] arrayOfByte = new byte[this.params.getLen()][];
/* 363 */     for (byte b = 0; b < arrayOfByte.length; b++)
/*     */     {
/* 365 */       arrayOfByte[b] = expandSecretKeySeed(b);
/*     */     }
/* 367 */     return new WOTSPlusPrivateKeyParameters(this.params, arrayOfByte);
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
/*     */   WOTSPlusPublicKeyParameters getPublicKey(OTSHashAddress paramOTSHashAddress) {
/* 379 */     if (paramOTSHashAddress == null)
/*     */     {
/* 381 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/* 383 */     byte[][] arrayOfByte = new byte[this.params.getLen()][];
/*     */     
/* 385 */     for (byte b = 0; b < this.params.getLen(); b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 391 */       paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(paramOTSHashAddress.getOTSAddress()).withChainAddress(b).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/* 392 */       arrayOfByte[b] = chain(expandSecretKeySeed(b), 0, this.params.getWinternitzParameter() - 1, paramOTSHashAddress);
/*     */     } 
/* 394 */     return new WOTSPlusPublicKeyParameters(this.params, arrayOfByte);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/WOTSPlus.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */