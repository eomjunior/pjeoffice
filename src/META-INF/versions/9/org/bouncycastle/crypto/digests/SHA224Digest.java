/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.digests.EncodableDigest;
/*     */ import org.bouncycastle.crypto.digests.GeneralDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SHA224Digest
/*     */   extends GeneralDigest
/*     */   implements EncodableDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 28;
/*     */   private int H1;
/*     */   private int H2;
/*     */   private int H3;
/*     */   private int H4;
/*     */   private int H5;
/*     */   private int H6;
/*     */   private int H7;
/*     */   private int H8;
/*  27 */   private int[] X = new int[64];
/*     */ 
/*     */   
/*     */   private int xOff;
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA224Digest() {
/*  35 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA224Digest(org.bouncycastle.crypto.digests.SHA224Digest paramSHA224Digest) {
/*  44 */     super(paramSHA224Digest);
/*     */     
/*  46 */     doCopy(paramSHA224Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   private void doCopy(org.bouncycastle.crypto.digests.SHA224Digest paramSHA224Digest) {
/*  51 */     copyIn(paramSHA224Digest);
/*     */     
/*  53 */     this.H1 = paramSHA224Digest.H1;
/*  54 */     this.H2 = paramSHA224Digest.H2;
/*  55 */     this.H3 = paramSHA224Digest.H3;
/*  56 */     this.H4 = paramSHA224Digest.H4;
/*  57 */     this.H5 = paramSHA224Digest.H5;
/*  58 */     this.H6 = paramSHA224Digest.H6;
/*  59 */     this.H7 = paramSHA224Digest.H7;
/*  60 */     this.H8 = paramSHA224Digest.H8;
/*     */     
/*  62 */     System.arraycopy(paramSHA224Digest.X, 0, this.X, 0, paramSHA224Digest.X.length);
/*  63 */     this.xOff = paramSHA224Digest.xOff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA224Digest(byte[] paramArrayOfbyte) {
/*  73 */     super(paramArrayOfbyte);
/*     */     
/*  75 */     this.H1 = Pack.bigEndianToInt(paramArrayOfbyte, 16);
/*  76 */     this.H2 = Pack.bigEndianToInt(paramArrayOfbyte, 20);
/*  77 */     this.H3 = Pack.bigEndianToInt(paramArrayOfbyte, 24);
/*  78 */     this.H4 = Pack.bigEndianToInt(paramArrayOfbyte, 28);
/*  79 */     this.H5 = Pack.bigEndianToInt(paramArrayOfbyte, 32);
/*  80 */     this.H6 = Pack.bigEndianToInt(paramArrayOfbyte, 36);
/*  81 */     this.H7 = Pack.bigEndianToInt(paramArrayOfbyte, 40);
/*  82 */     this.H8 = Pack.bigEndianToInt(paramArrayOfbyte, 44);
/*     */     
/*  84 */     this.xOff = Pack.bigEndianToInt(paramArrayOfbyte, 48);
/*  85 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/*  87 */       this.X[b] = Pack.bigEndianToInt(paramArrayOfbyte, 52 + b * 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  93 */     return "SHA-224";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  98 */     return 28;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWord(byte[] paramArrayOfbyte, int paramInt) {
/* 107 */     int i = paramArrayOfbyte[paramInt] << 24;
/* 108 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/* 109 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 110 */     i |= paramArrayOfbyte[++paramInt] & 0xFF;
/* 111 */     this.X[this.xOff] = i;
/*     */     
/* 113 */     if (++this.xOff == 16)
/*     */     {
/* 115 */       processBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLength(long paramLong) {
/* 122 */     if (this.xOff > 14)
/*     */     {
/* 124 */       processBlock();
/*     */     }
/*     */     
/* 127 */     this.X[14] = (int)(paramLong >>> 32L);
/* 128 */     this.X[15] = (int)(paramLong & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 135 */     finish();
/*     */     
/* 137 */     Pack.intToBigEndian(this.H1, paramArrayOfbyte, paramInt);
/* 138 */     Pack.intToBigEndian(this.H2, paramArrayOfbyte, paramInt + 4);
/* 139 */     Pack.intToBigEndian(this.H3, paramArrayOfbyte, paramInt + 8);
/* 140 */     Pack.intToBigEndian(this.H4, paramArrayOfbyte, paramInt + 12);
/* 141 */     Pack.intToBigEndian(this.H5, paramArrayOfbyte, paramInt + 16);
/* 142 */     Pack.intToBigEndian(this.H6, paramArrayOfbyte, paramInt + 20);
/* 143 */     Pack.intToBigEndian(this.H7, paramArrayOfbyte, paramInt + 24);
/*     */     
/* 145 */     reset();
/*     */     
/* 147 */     return 28;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 155 */     super.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.H1 = -1056596264;
/* 161 */     this.H2 = 914150663;
/* 162 */     this.H3 = 812702999;
/* 163 */     this.H4 = -150054599;
/* 164 */     this.H5 = -4191439;
/* 165 */     this.H6 = 1750603025;
/* 166 */     this.H7 = 1694076839;
/* 167 */     this.H8 = -1090891868;
/*     */     
/* 169 */     this.xOff = 0;
/* 170 */     for (byte b = 0; b != this.X.length; b++)
/*     */     {
/* 172 */       this.X[b] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBlock() {
/*     */     int i;
/* 181 */     for (i = 16; i <= 63; i++)
/*     */     {
/* 183 */       this.X[i] = Theta1(this.X[i - 2]) + this.X[i - 7] + Theta0(this.X[i - 15]) + this.X[i - 16];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     i = this.H1;
/* 190 */     int j = this.H2;
/* 191 */     int k = this.H3;
/* 192 */     int m = this.H4;
/* 193 */     int n = this.H5;
/* 194 */     int i1 = this.H6;
/* 195 */     int i2 = this.H7;
/* 196 */     int i3 = this.H8;
/*     */ 
/*     */     
/* 199 */     byte b1 = 0; byte b2;
/* 200 */     for (b2 = 0; b2 < 8; b2++) {
/*     */ 
/*     */       
/* 203 */       i3 += Sum1(n) + Ch(n, i1, i2) + K[b1] + this.X[b1];
/* 204 */       m += i3;
/* 205 */       i3 += Sum0(i) + Maj(i, j, k);
/* 206 */       b1++;
/*     */ 
/*     */       
/* 209 */       i2 += Sum1(m) + Ch(m, n, i1) + K[b1] + this.X[b1];
/* 210 */       k += i2;
/* 211 */       i2 += Sum0(i3) + Maj(i3, i, j);
/* 212 */       b1++;
/*     */ 
/*     */       
/* 215 */       i1 += Sum1(k) + Ch(k, m, n) + K[b1] + this.X[b1];
/* 216 */       j += i1;
/* 217 */       i1 += Sum0(i2) + Maj(i2, i3, i);
/* 218 */       b1++;
/*     */ 
/*     */       
/* 221 */       n += Sum1(j) + Ch(j, k, m) + K[b1] + this.X[b1];
/* 222 */       i += n;
/* 223 */       n += Sum0(i1) + Maj(i1, i2, i3);
/* 224 */       b1++;
/*     */ 
/*     */       
/* 227 */       m += Sum1(i) + Ch(i, j, k) + K[b1] + this.X[b1];
/* 228 */       i3 += m;
/* 229 */       m += Sum0(n) + Maj(n, i1, i2);
/* 230 */       b1++;
/*     */ 
/*     */       
/* 233 */       k += Sum1(i3) + Ch(i3, i, j) + K[b1] + this.X[b1];
/* 234 */       i2 += k;
/* 235 */       k += Sum0(m) + Maj(m, n, i1);
/* 236 */       b1++;
/*     */ 
/*     */       
/* 239 */       j += Sum1(i2) + Ch(i2, i3, i) + K[b1] + this.X[b1];
/* 240 */       i1 += j;
/* 241 */       j += Sum0(k) + Maj(k, m, n);
/* 242 */       b1++;
/*     */ 
/*     */       
/* 245 */       i += Sum1(i1) + Ch(i1, i2, i3) + K[b1] + this.X[b1];
/* 246 */       n += i;
/* 247 */       i += Sum0(j) + Maj(j, k, m);
/* 248 */       b1++;
/*     */     } 
/*     */     
/* 251 */     this.H1 += i;
/* 252 */     this.H2 += j;
/* 253 */     this.H3 += k;
/* 254 */     this.H4 += m;
/* 255 */     this.H5 += n;
/* 256 */     this.H6 += i1;
/* 257 */     this.H7 += i2;
/* 258 */     this.H8 += i3;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     this.xOff = 0;
/* 264 */     for (b2 = 0; b2 < 16; b2++)
/*     */     {
/* 266 */       this.X[b2] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int Ch(int paramInt1, int paramInt2, int paramInt3) {
/* 276 */     return paramInt1 & paramInt2 ^ (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int Maj(int paramInt1, int paramInt2, int paramInt3) {
/* 284 */     return paramInt1 & paramInt2 ^ paramInt1 & paramInt3 ^ paramInt2 & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int Sum0(int paramInt) {
/* 290 */     return (paramInt >>> 2 | paramInt << 30) ^ (paramInt >>> 13 | paramInt << 19) ^ (paramInt >>> 22 | paramInt << 10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int Sum1(int paramInt) {
/* 296 */     return (paramInt >>> 6 | paramInt << 26) ^ (paramInt >>> 11 | paramInt << 21) ^ (paramInt >>> 25 | paramInt << 7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int Theta0(int paramInt) {
/* 302 */     return (paramInt >>> 7 | paramInt << 25) ^ (paramInt >>> 18 | paramInt << 14) ^ paramInt >>> 3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int Theta1(int paramInt) {
/* 308 */     return (paramInt >>> 17 | paramInt << 15) ^ (paramInt >>> 19 | paramInt << 13) ^ paramInt >>> 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   static final int[] K = new int[] { 1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998 };
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
/*     */   public Memoable copy() {
/* 327 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA224Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 332 */     org.bouncycastle.crypto.digests.SHA224Digest sHA224Digest = (org.bouncycastle.crypto.digests.SHA224Digest)paramMemoable;
/*     */     
/* 334 */     doCopy(sHA224Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 339 */     byte[] arrayOfByte = new byte[52 + this.xOff * 4];
/*     */     
/* 341 */     populateState(arrayOfByte);
/*     */     
/* 343 */     Pack.intToBigEndian(this.H1, arrayOfByte, 16);
/* 344 */     Pack.intToBigEndian(this.H2, arrayOfByte, 20);
/* 345 */     Pack.intToBigEndian(this.H3, arrayOfByte, 24);
/* 346 */     Pack.intToBigEndian(this.H4, arrayOfByte, 28);
/* 347 */     Pack.intToBigEndian(this.H5, arrayOfByte, 32);
/* 348 */     Pack.intToBigEndian(this.H6, arrayOfByte, 36);
/* 349 */     Pack.intToBigEndian(this.H7, arrayOfByte, 40);
/* 350 */     Pack.intToBigEndian(this.H8, arrayOfByte, 44);
/* 351 */     Pack.intToBigEndian(this.xOff, arrayOfByte, 48);
/*     */     
/* 353 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/* 355 */       Pack.intToBigEndian(this.X[b], arrayOfByte, 52 + b * 4);
/*     */     }
/*     */     
/* 358 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA224Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */