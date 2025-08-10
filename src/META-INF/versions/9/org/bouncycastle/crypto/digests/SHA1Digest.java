/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.EncodableDigest;
/*     */ import org.bouncycastle.crypto.digests.GeneralDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SHA1Digest
/*     */   extends GeneralDigest
/*     */   implements EncodableDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 20;
/*     */   private int H1;
/*     */   private int H2;
/*     */   private int H3;
/*     */   private int H4;
/*     */   private int H5;
/*  20 */   private int[] X = new int[80];
/*     */   private int xOff;
/*     */   private static final int Y1 = 1518500249;
/*     */   private static final int Y2 = 1859775393;
/*     */   private static final int Y3 = -1894007588;
/*     */   private static final int Y4 = -899497514;
/*     */   
/*     */   public SHA1Digest() {
/*  28 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA1Digest(org.bouncycastle.crypto.digests.SHA1Digest paramSHA1Digest) {
/*  37 */     super(paramSHA1Digest);
/*     */     
/*  39 */     copyIn(paramSHA1Digest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA1Digest(byte[] paramArrayOfbyte) {
/*  49 */     super(paramArrayOfbyte);
/*     */     
/*  51 */     this.H1 = Pack.bigEndianToInt(paramArrayOfbyte, 16);
/*  52 */     this.H2 = Pack.bigEndianToInt(paramArrayOfbyte, 20);
/*  53 */     this.H3 = Pack.bigEndianToInt(paramArrayOfbyte, 24);
/*  54 */     this.H4 = Pack.bigEndianToInt(paramArrayOfbyte, 28);
/*  55 */     this.H5 = Pack.bigEndianToInt(paramArrayOfbyte, 32);
/*     */     
/*  57 */     this.xOff = Pack.bigEndianToInt(paramArrayOfbyte, 36);
/*  58 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/*  60 */       this.X[b] = Pack.bigEndianToInt(paramArrayOfbyte, 40 + b * 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyIn(org.bouncycastle.crypto.digests.SHA1Digest paramSHA1Digest) {
/*  66 */     this.H1 = paramSHA1Digest.H1;
/*  67 */     this.H2 = paramSHA1Digest.H2;
/*  68 */     this.H3 = paramSHA1Digest.H3;
/*  69 */     this.H4 = paramSHA1Digest.H4;
/*  70 */     this.H5 = paramSHA1Digest.H5;
/*     */     
/*  72 */     System.arraycopy(paramSHA1Digest.X, 0, this.X, 0, paramSHA1Digest.X.length);
/*  73 */     this.xOff = paramSHA1Digest.xOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  78 */     return "SHA-1";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  83 */     return 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWord(byte[] paramArrayOfbyte, int paramInt) {
/*  92 */     int i = paramArrayOfbyte[paramInt] << 24;
/*  93 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/*  94 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/*  95 */     i |= paramArrayOfbyte[++paramInt] & 0xFF;
/*  96 */     this.X[this.xOff] = i;
/*     */     
/*  98 */     if (++this.xOff == 16)
/*     */     {
/* 100 */       processBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLength(long paramLong) {
/* 107 */     if (this.xOff > 14)
/*     */     {
/* 109 */       processBlock();
/*     */     }
/*     */     
/* 112 */     this.X[14] = (int)(paramLong >>> 32L);
/* 113 */     this.X[15] = (int)paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 120 */     finish();
/*     */     
/* 122 */     Pack.intToBigEndian(this.H1, paramArrayOfbyte, paramInt);
/* 123 */     Pack.intToBigEndian(this.H2, paramArrayOfbyte, paramInt + 4);
/* 124 */     Pack.intToBigEndian(this.H3, paramArrayOfbyte, paramInt + 8);
/* 125 */     Pack.intToBigEndian(this.H4, paramArrayOfbyte, paramInt + 12);
/* 126 */     Pack.intToBigEndian(this.H5, paramArrayOfbyte, paramInt + 16);
/*     */     
/* 128 */     reset();
/*     */     
/* 130 */     return 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 138 */     super.reset();
/*     */     
/* 140 */     this.H1 = 1732584193;
/* 141 */     this.H2 = -271733879;
/* 142 */     this.H3 = -1732584194;
/* 143 */     this.H4 = 271733878;
/* 144 */     this.H5 = -1009589776;
/*     */     
/* 146 */     this.xOff = 0;
/* 147 */     for (byte b = 0; b != this.X.length; b++)
/*     */     {
/* 149 */       this.X[b] = 0;
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
/*     */   private int f(int paramInt1, int paramInt2, int paramInt3) {
/* 166 */     return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int h(int paramInt1, int paramInt2, int paramInt3) {
/* 174 */     return paramInt1 ^ paramInt2 ^ paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int g(int paramInt1, int paramInt2, int paramInt3) {
/* 182 */     return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBlock() {
/*     */     int i;
/* 190 */     for (i = 16; i < 80; i++) {
/*     */       
/* 192 */       int i1 = this.X[i - 3] ^ this.X[i - 8] ^ this.X[i - 14] ^ this.X[i - 16];
/* 193 */       this.X[i] = i1 << 1 | i1 >>> 31;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     i = this.H1;
/* 200 */     int j = this.H2;
/* 201 */     int k = this.H3;
/* 202 */     int m = this.H4;
/* 203 */     int n = this.H5;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     byte b1 = 0;
/*     */     byte b2;
/* 210 */     for (b2 = 0; b2 < 4; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 214 */       n += (i << 5 | i >>> 27) + f(j, k, m) + this.X[b1++] + 1518500249;
/* 215 */       j = j << 30 | j >>> 2;
/*     */       
/* 217 */       m += (n << 5 | n >>> 27) + f(i, j, k) + this.X[b1++] + 1518500249;
/* 218 */       i = i << 30 | i >>> 2;
/*     */       
/* 220 */       k += (m << 5 | m >>> 27) + f(n, i, j) + this.X[b1++] + 1518500249;
/* 221 */       n = n << 30 | n >>> 2;
/*     */       
/* 223 */       j += (k << 5 | k >>> 27) + f(m, n, i) + this.X[b1++] + 1518500249;
/* 224 */       m = m << 30 | m >>> 2;
/*     */       
/* 226 */       i += (j << 5 | j >>> 27) + f(k, m, n) + this.X[b1++] + 1518500249;
/* 227 */       k = k << 30 | k >>> 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     for (b2 = 0; b2 < 4; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 237 */       n += (i << 5 | i >>> 27) + h(j, k, m) + this.X[b1++] + 1859775393;
/* 238 */       j = j << 30 | j >>> 2;
/*     */       
/* 240 */       m += (n << 5 | n >>> 27) + h(i, j, k) + this.X[b1++] + 1859775393;
/* 241 */       i = i << 30 | i >>> 2;
/*     */       
/* 243 */       k += (m << 5 | m >>> 27) + h(n, i, j) + this.X[b1++] + 1859775393;
/* 244 */       n = n << 30 | n >>> 2;
/*     */       
/* 246 */       j += (k << 5 | k >>> 27) + h(m, n, i) + this.X[b1++] + 1859775393;
/* 247 */       m = m << 30 | m >>> 2;
/*     */       
/* 249 */       i += (j << 5 | j >>> 27) + h(k, m, n) + this.X[b1++] + 1859775393;
/* 250 */       k = k << 30 | k >>> 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     for (b2 = 0; b2 < 4; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 260 */       n += (i << 5 | i >>> 27) + g(j, k, m) + this.X[b1++] + -1894007588;
/* 261 */       j = j << 30 | j >>> 2;
/*     */       
/* 263 */       m += (n << 5 | n >>> 27) + g(i, j, k) + this.X[b1++] + -1894007588;
/* 264 */       i = i << 30 | i >>> 2;
/*     */       
/* 266 */       k += (m << 5 | m >>> 27) + g(n, i, j) + this.X[b1++] + -1894007588;
/* 267 */       n = n << 30 | n >>> 2;
/*     */       
/* 269 */       j += (k << 5 | k >>> 27) + g(m, n, i) + this.X[b1++] + -1894007588;
/* 270 */       m = m << 30 | m >>> 2;
/*     */       
/* 272 */       i += (j << 5 | j >>> 27) + g(k, m, n) + this.X[b1++] + -1894007588;
/* 273 */       k = k << 30 | k >>> 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     for (b2 = 0; b2 <= 3; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 283 */       n += (i << 5 | i >>> 27) + h(j, k, m) + this.X[b1++] + -899497514;
/* 284 */       j = j << 30 | j >>> 2;
/*     */       
/* 286 */       m += (n << 5 | n >>> 27) + h(i, j, k) + this.X[b1++] + -899497514;
/* 287 */       i = i << 30 | i >>> 2;
/*     */       
/* 289 */       k += (m << 5 | m >>> 27) + h(n, i, j) + this.X[b1++] + -899497514;
/* 290 */       n = n << 30 | n >>> 2;
/*     */       
/* 292 */       j += (k << 5 | k >>> 27) + h(m, n, i) + this.X[b1++] + -899497514;
/* 293 */       m = m << 30 | m >>> 2;
/*     */       
/* 295 */       i += (j << 5 | j >>> 27) + h(k, m, n) + this.X[b1++] + -899497514;
/* 296 */       k = k << 30 | k >>> 2;
/*     */     } 
/*     */ 
/*     */     
/* 300 */     this.H1 += i;
/* 301 */     this.H2 += j;
/* 302 */     this.H3 += k;
/* 303 */     this.H4 += m;
/* 304 */     this.H5 += n;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     this.xOff = 0;
/* 310 */     for (b2 = 0; b2 < 16; b2++)
/*     */     {
/* 312 */       this.X[b2] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Memoable copy() {
/* 318 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA1Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 323 */     org.bouncycastle.crypto.digests.SHA1Digest sHA1Digest = (org.bouncycastle.crypto.digests.SHA1Digest)paramMemoable;
/*     */     
/* 325 */     copyIn(sHA1Digest);
/* 326 */     copyIn(sHA1Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 331 */     byte[] arrayOfByte = new byte[40 + this.xOff * 4];
/*     */     
/* 333 */     populateState(arrayOfByte);
/*     */     
/* 335 */     Pack.intToBigEndian(this.H1, arrayOfByte, 16);
/* 336 */     Pack.intToBigEndian(this.H2, arrayOfByte, 20);
/* 337 */     Pack.intToBigEndian(this.H3, arrayOfByte, 24);
/* 338 */     Pack.intToBigEndian(this.H4, arrayOfByte, 28);
/* 339 */     Pack.intToBigEndian(this.H5, arrayOfByte, 32);
/* 340 */     Pack.intToBigEndian(this.xOff, arrayOfByte, 36);
/*     */     
/* 342 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/* 344 */       Pack.intToBigEndian(this.X[b], arrayOfByte, 40 + b * 4);
/*     */     }
/*     */     
/* 347 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA1Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */