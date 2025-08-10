/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.EncodableDigest;
/*     */ import org.bouncycastle.crypto.digests.GeneralDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ public class MD5Digest
/*     */   extends GeneralDigest
/*     */   implements EncodableDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 16;
/*     */   private int H1;
/*     */   private int H2;
/*     */   private int H3;
/*     */   private int H4;
/*  18 */   private int[] X = new int[16]; private int xOff; private static final int S11 = 7; private static final int S12 = 12;
/*     */   private static final int S13 = 17;
/*     */   private static final int S14 = 22;
/*     */   private static final int S21 = 5;
/*     */   private static final int S22 = 9;
/*     */   private static final int S23 = 14;
/*     */   
/*     */   public MD5Digest() {
/*  26 */     reset();
/*     */   }
/*     */   private static final int S24 = 20; private static final int S31 = 4; private static final int S32 = 11; private static final int S33 = 16; private static final int S34 = 23; private static final int S41 = 6; private static final int S42 = 10; private static final int S43 = 15; private static final int S44 = 21;
/*     */   
/*     */   public MD5Digest(byte[] paramArrayOfbyte) {
/*  31 */     super(paramArrayOfbyte);
/*     */     
/*  33 */     this.H1 = Pack.bigEndianToInt(paramArrayOfbyte, 16);
/*  34 */     this.H2 = Pack.bigEndianToInt(paramArrayOfbyte, 20);
/*  35 */     this.H3 = Pack.bigEndianToInt(paramArrayOfbyte, 24);
/*  36 */     this.H4 = Pack.bigEndianToInt(paramArrayOfbyte, 28);
/*     */     
/*  38 */     this.xOff = Pack.bigEndianToInt(paramArrayOfbyte, 32);
/*  39 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/*  41 */       this.X[b] = Pack.bigEndianToInt(paramArrayOfbyte, 36 + b * 4);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MD5Digest(org.bouncycastle.crypto.digests.MD5Digest paramMD5Digest) {
/*  51 */     super(paramMD5Digest);
/*     */     
/*  53 */     copyIn(paramMD5Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyIn(org.bouncycastle.crypto.digests.MD5Digest paramMD5Digest) {
/*  58 */     copyIn(paramMD5Digest);
/*     */     
/*  60 */     this.H1 = paramMD5Digest.H1;
/*  61 */     this.H2 = paramMD5Digest.H2;
/*  62 */     this.H3 = paramMD5Digest.H3;
/*  63 */     this.H4 = paramMD5Digest.H4;
/*     */     
/*  65 */     System.arraycopy(paramMD5Digest.X, 0, this.X, 0, paramMD5Digest.X.length);
/*  66 */     this.xOff = paramMD5Digest.xOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  71 */     return "MD5";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  76 */     return 16;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWord(byte[] paramArrayOfbyte, int paramInt) {
/*  83 */     this.X[this.xOff++] = paramArrayOfbyte[paramInt] & 0xFF | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 8 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 3] & 0xFF) << 24;
/*     */ 
/*     */     
/*  86 */     if (this.xOff == 16)
/*     */     {
/*  88 */       processBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLength(long paramLong) {
/*  95 */     if (this.xOff > 14)
/*     */     {
/*  97 */       processBlock();
/*     */     }
/*     */     
/* 100 */     this.X[14] = (int)(paramLong & 0xFFFFFFFFFFFFFFFFL);
/* 101 */     this.X[15] = (int)(paramLong >>> 32L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void unpackWord(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
/* 109 */     paramArrayOfbyte[paramInt2] = (byte)paramInt1;
/* 110 */     paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 8);
/* 111 */     paramArrayOfbyte[paramInt2 + 2] = (byte)(paramInt1 >>> 16);
/* 112 */     paramArrayOfbyte[paramInt2 + 3] = (byte)(paramInt1 >>> 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 119 */     finish();
/*     */     
/* 121 */     unpackWord(this.H1, paramArrayOfbyte, paramInt);
/* 122 */     unpackWord(this.H2, paramArrayOfbyte, paramInt + 4);
/* 123 */     unpackWord(this.H3, paramArrayOfbyte, paramInt + 8);
/* 124 */     unpackWord(this.H4, paramArrayOfbyte, paramInt + 12);
/*     */     
/* 126 */     reset();
/*     */     
/* 128 */     return 16;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 136 */     super.reset();
/*     */     
/* 138 */     this.H1 = 1732584193;
/* 139 */     this.H2 = -271733879;
/* 140 */     this.H3 = -1732584194;
/* 141 */     this.H4 = 271733878;
/*     */     
/* 143 */     this.xOff = 0;
/*     */     
/* 145 */     for (byte b = 0; b != this.X.length; b++)
/*     */     {
/* 147 */       this.X[b] = 0;
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
/*     */   private int rotateLeft(int paramInt1, int paramInt2) {
/* 190 */     return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int F(int paramInt1, int paramInt2, int paramInt3) {
/* 201 */     return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int G(int paramInt1, int paramInt2, int paramInt3) {
/* 209 */     return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int H(int paramInt1, int paramInt2, int paramInt3) {
/* 217 */     return paramInt1 ^ paramInt2 ^ paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int K(int paramInt1, int paramInt2, int paramInt3) {
/* 225 */     return paramInt2 ^ (paramInt1 | paramInt3 ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processBlock() {
/* 230 */     int i = this.H1;
/* 231 */     int j = this.H2;
/* 232 */     int k = this.H3;
/* 233 */     int m = this.H4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     i = rotateLeft(i + F(j, k, m) + this.X[0] + -680876936, 7) + j;
/* 239 */     m = rotateLeft(m + F(i, j, k) + this.X[1] + -389564586, 12) + i;
/* 240 */     k = rotateLeft(k + F(m, i, j) + this.X[2] + 606105819, 17) + m;
/* 241 */     j = rotateLeft(j + F(k, m, i) + this.X[3] + -1044525330, 22) + k;
/* 242 */     i = rotateLeft(i + F(j, k, m) + this.X[4] + -176418897, 7) + j;
/* 243 */     m = rotateLeft(m + F(i, j, k) + this.X[5] + 1200080426, 12) + i;
/* 244 */     k = rotateLeft(k + F(m, i, j) + this.X[6] + -1473231341, 17) + m;
/* 245 */     j = rotateLeft(j + F(k, m, i) + this.X[7] + -45705983, 22) + k;
/* 246 */     i = rotateLeft(i + F(j, k, m) + this.X[8] + 1770035416, 7) + j;
/* 247 */     m = rotateLeft(m + F(i, j, k) + this.X[9] + -1958414417, 12) + i;
/* 248 */     k = rotateLeft(k + F(m, i, j) + this.X[10] + -42063, 17) + m;
/* 249 */     j = rotateLeft(j + F(k, m, i) + this.X[11] + -1990404162, 22) + k;
/* 250 */     i = rotateLeft(i + F(j, k, m) + this.X[12] + 1804603682, 7) + j;
/* 251 */     m = rotateLeft(m + F(i, j, k) + this.X[13] + -40341101, 12) + i;
/* 252 */     k = rotateLeft(k + F(m, i, j) + this.X[14] + -1502002290, 17) + m;
/* 253 */     j = rotateLeft(j + F(k, m, i) + this.X[15] + 1236535329, 22) + k;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     i = rotateLeft(i + G(j, k, m) + this.X[1] + -165796510, 5) + j;
/* 259 */     m = rotateLeft(m + G(i, j, k) + this.X[6] + -1069501632, 9) + i;
/* 260 */     k = rotateLeft(k + G(m, i, j) + this.X[11] + 643717713, 14) + m;
/* 261 */     j = rotateLeft(j + G(k, m, i) + this.X[0] + -373897302, 20) + k;
/* 262 */     i = rotateLeft(i + G(j, k, m) + this.X[5] + -701558691, 5) + j;
/* 263 */     m = rotateLeft(m + G(i, j, k) + this.X[10] + 38016083, 9) + i;
/* 264 */     k = rotateLeft(k + G(m, i, j) + this.X[15] + -660478335, 14) + m;
/* 265 */     j = rotateLeft(j + G(k, m, i) + this.X[4] + -405537848, 20) + k;
/* 266 */     i = rotateLeft(i + G(j, k, m) + this.X[9] + 568446438, 5) + j;
/* 267 */     m = rotateLeft(m + G(i, j, k) + this.X[14] + -1019803690, 9) + i;
/* 268 */     k = rotateLeft(k + G(m, i, j) + this.X[3] + -187363961, 14) + m;
/* 269 */     j = rotateLeft(j + G(k, m, i) + this.X[8] + 1163531501, 20) + k;
/* 270 */     i = rotateLeft(i + G(j, k, m) + this.X[13] + -1444681467, 5) + j;
/* 271 */     m = rotateLeft(m + G(i, j, k) + this.X[2] + -51403784, 9) + i;
/* 272 */     k = rotateLeft(k + G(m, i, j) + this.X[7] + 1735328473, 14) + m;
/* 273 */     j = rotateLeft(j + G(k, m, i) + this.X[12] + -1926607734, 20) + k;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     i = rotateLeft(i + H(j, k, m) + this.X[5] + -378558, 4) + j;
/* 279 */     m = rotateLeft(m + H(i, j, k) + this.X[8] + -2022574463, 11) + i;
/* 280 */     k = rotateLeft(k + H(m, i, j) + this.X[11] + 1839030562, 16) + m;
/* 281 */     j = rotateLeft(j + H(k, m, i) + this.X[14] + -35309556, 23) + k;
/* 282 */     i = rotateLeft(i + H(j, k, m) + this.X[1] + -1530992060, 4) + j;
/* 283 */     m = rotateLeft(m + H(i, j, k) + this.X[4] + 1272893353, 11) + i;
/* 284 */     k = rotateLeft(k + H(m, i, j) + this.X[7] + -155497632, 16) + m;
/* 285 */     j = rotateLeft(j + H(k, m, i) + this.X[10] + -1094730640, 23) + k;
/* 286 */     i = rotateLeft(i + H(j, k, m) + this.X[13] + 681279174, 4) + j;
/* 287 */     m = rotateLeft(m + H(i, j, k) + this.X[0] + -358537222, 11) + i;
/* 288 */     k = rotateLeft(k + H(m, i, j) + this.X[3] + -722521979, 16) + m;
/* 289 */     j = rotateLeft(j + H(k, m, i) + this.X[6] + 76029189, 23) + k;
/* 290 */     i = rotateLeft(i + H(j, k, m) + this.X[9] + -640364487, 4) + j;
/* 291 */     m = rotateLeft(m + H(i, j, k) + this.X[12] + -421815835, 11) + i;
/* 292 */     k = rotateLeft(k + H(m, i, j) + this.X[15] + 530742520, 16) + m;
/* 293 */     j = rotateLeft(j + H(k, m, i) + this.X[2] + -995338651, 23) + k;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     i = rotateLeft(i + K(j, k, m) + this.X[0] + -198630844, 6) + j;
/* 299 */     m = rotateLeft(m + K(i, j, k) + this.X[7] + 1126891415, 10) + i;
/* 300 */     k = rotateLeft(k + K(m, i, j) + this.X[14] + -1416354905, 15) + m;
/* 301 */     j = rotateLeft(j + K(k, m, i) + this.X[5] + -57434055, 21) + k;
/* 302 */     i = rotateLeft(i + K(j, k, m) + this.X[12] + 1700485571, 6) + j;
/* 303 */     m = rotateLeft(m + K(i, j, k) + this.X[3] + -1894986606, 10) + i;
/* 304 */     k = rotateLeft(k + K(m, i, j) + this.X[10] + -1051523, 15) + m;
/* 305 */     j = rotateLeft(j + K(k, m, i) + this.X[1] + -2054922799, 21) + k;
/* 306 */     i = rotateLeft(i + K(j, k, m) + this.X[8] + 1873313359, 6) + j;
/* 307 */     m = rotateLeft(m + K(i, j, k) + this.X[15] + -30611744, 10) + i;
/* 308 */     k = rotateLeft(k + K(m, i, j) + this.X[6] + -1560198380, 15) + m;
/* 309 */     j = rotateLeft(j + K(k, m, i) + this.X[13] + 1309151649, 21) + k;
/* 310 */     i = rotateLeft(i + K(j, k, m) + this.X[4] + -145523070, 6) + j;
/* 311 */     m = rotateLeft(m + K(i, j, k) + this.X[11] + -1120210379, 10) + i;
/* 312 */     k = rotateLeft(k + K(m, i, j) + this.X[2] + 718787259, 15) + m;
/* 313 */     j = rotateLeft(j + K(k, m, i) + this.X[9] + -343485551, 21) + k;
/*     */     
/* 315 */     this.H1 += i;
/* 316 */     this.H2 += j;
/* 317 */     this.H3 += k;
/* 318 */     this.H4 += m;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     this.xOff = 0;
/* 324 */     for (byte b = 0; b != this.X.length; b++)
/*     */     {
/* 326 */       this.X[b] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Memoable copy() {
/* 332 */     return (Memoable)new org.bouncycastle.crypto.digests.MD5Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 337 */     org.bouncycastle.crypto.digests.MD5Digest mD5Digest = (org.bouncycastle.crypto.digests.MD5Digest)paramMemoable;
/*     */     
/* 339 */     copyIn(mD5Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 344 */     byte[] arrayOfByte = new byte[36 + this.xOff * 4];
/*     */     
/* 346 */     populateState(arrayOfByte);
/*     */     
/* 348 */     Pack.intToBigEndian(this.H1, arrayOfByte, 16);
/* 349 */     Pack.intToBigEndian(this.H2, arrayOfByte, 20);
/* 350 */     Pack.intToBigEndian(this.H3, arrayOfByte, 24);
/* 351 */     Pack.intToBigEndian(this.H4, arrayOfByte, 28);
/* 352 */     Pack.intToBigEndian(this.xOff, arrayOfByte, 32);
/*     */     
/* 354 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/* 356 */       Pack.intToBigEndian(this.X[b], arrayOfByte, 36 + b * 4);
/*     */     }
/*     */     
/* 359 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/MD5Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */