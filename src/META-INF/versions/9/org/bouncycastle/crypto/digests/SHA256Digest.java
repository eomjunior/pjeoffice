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
/*     */ public class SHA256Digest
/*     */   extends GeneralDigest
/*     */   implements EncodableDigest
/*     */ {
/*     */   private static final int DIGEST_LENGTH = 32;
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
/*     */   public SHA256Digest() {
/*  35 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA256Digest(org.bouncycastle.crypto.digests.SHA256Digest paramSHA256Digest) {
/*  44 */     super(paramSHA256Digest);
/*     */     
/*  46 */     copyIn(paramSHA256Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyIn(org.bouncycastle.crypto.digests.SHA256Digest paramSHA256Digest) {
/*  51 */     copyIn(paramSHA256Digest);
/*     */     
/*  53 */     this.H1 = paramSHA256Digest.H1;
/*  54 */     this.H2 = paramSHA256Digest.H2;
/*  55 */     this.H3 = paramSHA256Digest.H3;
/*  56 */     this.H4 = paramSHA256Digest.H4;
/*  57 */     this.H5 = paramSHA256Digest.H5;
/*  58 */     this.H6 = paramSHA256Digest.H6;
/*  59 */     this.H7 = paramSHA256Digest.H7;
/*  60 */     this.H8 = paramSHA256Digest.H8;
/*     */     
/*  62 */     System.arraycopy(paramSHA256Digest.X, 0, this.X, 0, paramSHA256Digest.X.length);
/*  63 */     this.xOff = paramSHA256Digest.xOff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SHA256Digest(byte[] paramArrayOfbyte) {
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
/*     */   
/*     */   public String getAlgorithmName() {
/*  94 */     return "SHA-256";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  99 */     return 32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWord(byte[] paramArrayOfbyte, int paramInt) {
/* 108 */     int i = paramArrayOfbyte[paramInt] << 24;
/* 109 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/* 110 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 111 */     i |= paramArrayOfbyte[++paramInt] & 0xFF;
/* 112 */     this.X[this.xOff] = i;
/*     */     
/* 114 */     if (++this.xOff == 16)
/*     */     {
/* 116 */       processBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLength(long paramLong) {
/* 123 */     if (this.xOff > 14)
/*     */     {
/* 125 */       processBlock();
/*     */     }
/*     */     
/* 128 */     this.X[14] = (int)(paramLong >>> 32L);
/* 129 */     this.X[15] = (int)(paramLong & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 136 */     finish();
/*     */     
/* 138 */     Pack.intToBigEndian(this.H1, paramArrayOfbyte, paramInt);
/* 139 */     Pack.intToBigEndian(this.H2, paramArrayOfbyte, paramInt + 4);
/* 140 */     Pack.intToBigEndian(this.H3, paramArrayOfbyte, paramInt + 8);
/* 141 */     Pack.intToBigEndian(this.H4, paramArrayOfbyte, paramInt + 12);
/* 142 */     Pack.intToBigEndian(this.H5, paramArrayOfbyte, paramInt + 16);
/* 143 */     Pack.intToBigEndian(this.H6, paramArrayOfbyte, paramInt + 20);
/* 144 */     Pack.intToBigEndian(this.H7, paramArrayOfbyte, paramInt + 24);
/* 145 */     Pack.intToBigEndian(this.H8, paramArrayOfbyte, paramInt + 28);
/*     */     
/* 147 */     reset();
/*     */     
/* 149 */     return 32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 157 */     super.reset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     this.H1 = 1779033703;
/* 165 */     this.H2 = -1150833019;
/* 166 */     this.H3 = 1013904242;
/* 167 */     this.H4 = -1521486534;
/* 168 */     this.H5 = 1359893119;
/* 169 */     this.H6 = -1694144372;
/* 170 */     this.H7 = 528734635;
/* 171 */     this.H8 = 1541459225;
/*     */     
/* 173 */     this.xOff = 0;
/* 174 */     for (byte b = 0; b != this.X.length; b++)
/*     */     {
/* 176 */       this.X[b] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBlock() {
/*     */     int i;
/* 185 */     for (i = 16; i <= 63; i++)
/*     */     {
/* 187 */       this.X[i] = Theta1(this.X[i - 2]) + this.X[i - 7] + Theta0(this.X[i - 15]) + this.X[i - 16];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     i = this.H1;
/* 194 */     int j = this.H2;
/* 195 */     int k = this.H3;
/* 196 */     int m = this.H4;
/* 197 */     int n = this.H5;
/* 198 */     int i1 = this.H6;
/* 199 */     int i2 = this.H7;
/* 200 */     int i3 = this.H8;
/*     */     
/* 202 */     byte b1 = 0; byte b2;
/* 203 */     for (b2 = 0; b2 < 8; b2++) {
/*     */ 
/*     */       
/* 206 */       i3 += Sum1(n) + Ch(n, i1, i2) + K[b1] + this.X[b1];
/* 207 */       m += i3;
/* 208 */       i3 += Sum0(i) + Maj(i, j, k);
/* 209 */       b1++;
/*     */ 
/*     */       
/* 212 */       i2 += Sum1(m) + Ch(m, n, i1) + K[b1] + this.X[b1];
/* 213 */       k += i2;
/* 214 */       i2 += Sum0(i3) + Maj(i3, i, j);
/* 215 */       b1++;
/*     */ 
/*     */       
/* 218 */       i1 += Sum1(k) + Ch(k, m, n) + K[b1] + this.X[b1];
/* 219 */       j += i1;
/* 220 */       i1 += Sum0(i2) + Maj(i2, i3, i);
/* 221 */       b1++;
/*     */ 
/*     */       
/* 224 */       n += Sum1(j) + Ch(j, k, m) + K[b1] + this.X[b1];
/* 225 */       i += n;
/* 226 */       n += Sum0(i1) + Maj(i1, i2, i3);
/* 227 */       b1++;
/*     */ 
/*     */       
/* 230 */       m += Sum1(i) + Ch(i, j, k) + K[b1] + this.X[b1];
/* 231 */       i3 += m;
/* 232 */       m += Sum0(n) + Maj(n, i1, i2);
/* 233 */       b1++;
/*     */ 
/*     */       
/* 236 */       k += Sum1(i3) + Ch(i3, i, j) + K[b1] + this.X[b1];
/* 237 */       i2 += k;
/* 238 */       k += Sum0(m) + Maj(m, n, i1);
/* 239 */       b1++;
/*     */ 
/*     */       
/* 242 */       j += Sum1(i2) + Ch(i2, i3, i) + K[b1] + this.X[b1];
/* 243 */       i1 += j;
/* 244 */       j += Sum0(k) + Maj(k, m, n);
/* 245 */       b1++;
/*     */ 
/*     */       
/* 248 */       i += Sum1(i1) + Ch(i1, i2, i3) + K[b1] + this.X[b1];
/* 249 */       n += i;
/* 250 */       i += Sum0(j) + Maj(j, k, m);
/* 251 */       b1++;
/*     */     } 
/*     */     
/* 254 */     this.H1 += i;
/* 255 */     this.H2 += j;
/* 256 */     this.H3 += k;
/* 257 */     this.H4 += m;
/* 258 */     this.H5 += n;
/* 259 */     this.H6 += i1;
/* 260 */     this.H7 += i2;
/* 261 */     this.H8 += i3;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     this.xOff = 0;
/* 267 */     for (b2 = 0; b2 < 16; b2++)
/*     */     {
/* 269 */       this.X[b2] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int Ch(int paramInt1, int paramInt2, int paramInt3) {
/* 276 */     return paramInt1 & paramInt2 ^ (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int Maj(int paramInt1, int paramInt2, int paramInt3) {
/* 283 */     return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int Sum0(int paramInt) {
/* 288 */     return (paramInt >>> 2 | paramInt << 30) ^ (paramInt >>> 13 | paramInt << 19) ^ (paramInt >>> 22 | paramInt << 10);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int Sum1(int paramInt) {
/* 293 */     return (paramInt >>> 6 | paramInt << 26) ^ (paramInt >>> 11 | paramInt << 21) ^ (paramInt >>> 25 | paramInt << 7);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int Theta0(int paramInt) {
/* 298 */     return (paramInt >>> 7 | paramInt << 25) ^ (paramInt >>> 18 | paramInt << 14) ^ paramInt >>> 3;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int Theta1(int paramInt) {
/* 303 */     return (paramInt >>> 17 | paramInt << 15) ^ (paramInt >>> 19 | paramInt << 13) ^ paramInt >>> 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   static final int[] K = new int[] { 1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998 };
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
/*     */   public Memoable copy() {
/* 323 */     return (Memoable)new org.bouncycastle.crypto.digests.SHA256Digest(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(Memoable paramMemoable) {
/* 328 */     org.bouncycastle.crypto.digests.SHA256Digest sHA256Digest = (org.bouncycastle.crypto.digests.SHA256Digest)paramMemoable;
/*     */     
/* 330 */     copyIn(sHA256Digest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedState() {
/* 335 */     byte[] arrayOfByte = new byte[52 + this.xOff * 4];
/*     */     
/* 337 */     populateState(arrayOfByte);
/*     */     
/* 339 */     Pack.intToBigEndian(this.H1, arrayOfByte, 16);
/* 340 */     Pack.intToBigEndian(this.H2, arrayOfByte, 20);
/* 341 */     Pack.intToBigEndian(this.H3, arrayOfByte, 24);
/* 342 */     Pack.intToBigEndian(this.H4, arrayOfByte, 28);
/* 343 */     Pack.intToBigEndian(this.H5, arrayOfByte, 32);
/* 344 */     Pack.intToBigEndian(this.H6, arrayOfByte, 36);
/* 345 */     Pack.intToBigEndian(this.H7, arrayOfByte, 40);
/* 346 */     Pack.intToBigEndian(this.H8, arrayOfByte, 44);
/* 347 */     Pack.intToBigEndian(this.xOff, arrayOfByte, 48);
/*     */     
/* 349 */     for (byte b = 0; b != this.xOff; b++)
/*     */     {
/* 351 */       Pack.intToBigEndian(this.X[b], arrayOfByte, 52 + b * 4);
/*     */     }
/*     */     
/* 354 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA256Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */