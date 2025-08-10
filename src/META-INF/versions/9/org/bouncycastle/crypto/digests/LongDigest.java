/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.ExtendedDigest;
/*     */ import org.bouncycastle.crypto.digests.EncodableDigest;
/*     */ import org.bouncycastle.util.Memoable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LongDigest
/*     */   implements ExtendedDigest, Memoable, EncodableDigest
/*     */ {
/*     */   private static final int BYTE_LENGTH = 128;
/*  15 */   private byte[] xBuf = new byte[8];
/*     */   
/*     */   private int xBufOff;
/*     */   
/*     */   private long byteCount1;
/*     */   private long byteCount2;
/*     */   protected long H1;
/*     */   protected long H2;
/*  23 */   private long[] W = new long[80];
/*     */   
/*     */   protected long H3;
/*     */   
/*     */   protected long H4;
/*     */   protected long H5;
/*     */   
/*     */   protected LongDigest() {
/*  31 */     this.xBufOff = 0;
/*     */     
/*  33 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected long H6;
/*     */   protected long H7;
/*     */   protected long H8;
/*     */   private int wOff;
/*     */   
/*     */   protected LongDigest(org.bouncycastle.crypto.digests.LongDigest paramLongDigest) {
/*  43 */     copyIn(paramLongDigest);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copyIn(org.bouncycastle.crypto.digests.LongDigest paramLongDigest) {
/*  48 */     System.arraycopy(paramLongDigest.xBuf, 0, this.xBuf, 0, paramLongDigest.xBuf.length);
/*     */     
/*  50 */     this.xBufOff = paramLongDigest.xBufOff;
/*  51 */     this.byteCount1 = paramLongDigest.byteCount1;
/*  52 */     this.byteCount2 = paramLongDigest.byteCount2;
/*     */     
/*  54 */     this.H1 = paramLongDigest.H1;
/*  55 */     this.H2 = paramLongDigest.H2;
/*  56 */     this.H3 = paramLongDigest.H3;
/*  57 */     this.H4 = paramLongDigest.H4;
/*  58 */     this.H5 = paramLongDigest.H5;
/*  59 */     this.H6 = paramLongDigest.H6;
/*  60 */     this.H7 = paramLongDigest.H7;
/*  61 */     this.H8 = paramLongDigest.H8;
/*     */     
/*  63 */     System.arraycopy(paramLongDigest.W, 0, this.W, 0, paramLongDigest.W.length);
/*  64 */     this.wOff = paramLongDigest.wOff;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateState(byte[] paramArrayOfbyte) {
/*  69 */     System.arraycopy(this.xBuf, 0, paramArrayOfbyte, 0, this.xBufOff);
/*  70 */     Pack.intToBigEndian(this.xBufOff, paramArrayOfbyte, 8);
/*  71 */     Pack.longToBigEndian(this.byteCount1, paramArrayOfbyte, 12);
/*  72 */     Pack.longToBigEndian(this.byteCount2, paramArrayOfbyte, 20);
/*  73 */     Pack.longToBigEndian(this.H1, paramArrayOfbyte, 28);
/*  74 */     Pack.longToBigEndian(this.H2, paramArrayOfbyte, 36);
/*  75 */     Pack.longToBigEndian(this.H3, paramArrayOfbyte, 44);
/*  76 */     Pack.longToBigEndian(this.H4, paramArrayOfbyte, 52);
/*  77 */     Pack.longToBigEndian(this.H5, paramArrayOfbyte, 60);
/*  78 */     Pack.longToBigEndian(this.H6, paramArrayOfbyte, 68);
/*  79 */     Pack.longToBigEndian(this.H7, paramArrayOfbyte, 76);
/*  80 */     Pack.longToBigEndian(this.H8, paramArrayOfbyte, 84);
/*     */     
/*  82 */     Pack.intToBigEndian(this.wOff, paramArrayOfbyte, 92);
/*  83 */     for (byte b = 0; b < this.wOff; b++)
/*     */     {
/*  85 */       Pack.longToBigEndian(this.W[b], paramArrayOfbyte, 96 + b * 8);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void restoreState(byte[] paramArrayOfbyte) {
/*  91 */     this.xBufOff = Pack.bigEndianToInt(paramArrayOfbyte, 8);
/*  92 */     System.arraycopy(paramArrayOfbyte, 0, this.xBuf, 0, this.xBufOff);
/*  93 */     this.byteCount1 = Pack.bigEndianToLong(paramArrayOfbyte, 12);
/*  94 */     this.byteCount2 = Pack.bigEndianToLong(paramArrayOfbyte, 20);
/*     */     
/*  96 */     this.H1 = Pack.bigEndianToLong(paramArrayOfbyte, 28);
/*  97 */     this.H2 = Pack.bigEndianToLong(paramArrayOfbyte, 36);
/*  98 */     this.H3 = Pack.bigEndianToLong(paramArrayOfbyte, 44);
/*  99 */     this.H4 = Pack.bigEndianToLong(paramArrayOfbyte, 52);
/* 100 */     this.H5 = Pack.bigEndianToLong(paramArrayOfbyte, 60);
/* 101 */     this.H6 = Pack.bigEndianToLong(paramArrayOfbyte, 68);
/* 102 */     this.H7 = Pack.bigEndianToLong(paramArrayOfbyte, 76);
/* 103 */     this.H8 = Pack.bigEndianToLong(paramArrayOfbyte, 84);
/*     */     
/* 105 */     this.wOff = Pack.bigEndianToInt(paramArrayOfbyte, 92);
/* 106 */     for (byte b = 0; b < this.wOff; b++)
/*     */     {
/* 108 */       this.W[b] = Pack.bigEndianToLong(paramArrayOfbyte, 96 + b * 8);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getEncodedStateSize() {
/* 114 */     return 96 + this.wOff * 8;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(byte paramByte) {
/* 120 */     this.xBuf[this.xBufOff++] = paramByte;
/*     */     
/* 122 */     if (this.xBufOff == this.xBuf.length) {
/*     */       
/* 124 */       processWord(this.xBuf, 0);
/* 125 */       this.xBufOff = 0;
/*     */     } 
/*     */     
/* 128 */     this.byteCount1++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 139 */     while (this.xBufOff != 0 && paramInt2 > 0) {
/*     */       
/* 141 */       update(paramArrayOfbyte[paramInt1]);
/*     */       
/* 143 */       paramInt1++;
/* 144 */       paramInt2--;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     while (paramInt2 > this.xBuf.length) {
/*     */       
/* 152 */       processWord(paramArrayOfbyte, paramInt1);
/*     */       
/* 154 */       paramInt1 += this.xBuf.length;
/* 155 */       paramInt2 -= this.xBuf.length;
/* 156 */       this.byteCount1 += this.xBuf.length;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     while (paramInt2 > 0) {
/*     */       
/* 164 */       update(paramArrayOfbyte[paramInt1]);
/*     */       
/* 166 */       paramInt1++;
/* 167 */       paramInt2--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void finish() {
/* 173 */     adjustByteCounts();
/*     */     
/* 175 */     long l1 = this.byteCount1 << 3L;
/* 176 */     long l2 = this.byteCount2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     update(-128);
/*     */     
/* 183 */     while (this.xBufOff != 0)
/*     */     {
/* 185 */       update((byte)0);
/*     */     }
/*     */     
/* 188 */     processLength(l1, l2);
/*     */     
/* 190 */     processBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 195 */     this.byteCount1 = 0L;
/* 196 */     this.byteCount2 = 0L;
/*     */     
/* 198 */     this.xBufOff = 0; byte b;
/* 199 */     for (b = 0; b < this.xBuf.length; b++)
/*     */     {
/* 201 */       this.xBuf[b] = 0;
/*     */     }
/*     */     
/* 204 */     this.wOff = 0;
/* 205 */     for (b = 0; b != this.W.length; b++)
/*     */     {
/* 207 */       this.W[b] = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getByteLength() {
/* 213 */     return 128;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processWord(byte[] paramArrayOfbyte, int paramInt) {
/* 220 */     this.W[this.wOff] = Pack.bigEndianToLong(paramArrayOfbyte, paramInt);
/*     */     
/* 222 */     if (++this.wOff == 16)
/*     */     {
/* 224 */       processBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustByteCounts() {
/* 234 */     if (this.byteCount1 > 2305843009213693951L) {
/*     */       
/* 236 */       this.byteCount2 += this.byteCount1 >>> 61L;
/* 237 */       this.byteCount1 &= 0x1FFFFFFFFFFFFFFFL;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLength(long paramLong1, long paramLong2) {
/* 245 */     if (this.wOff > 14)
/*     */     {
/* 247 */       processBlock();
/*     */     }
/*     */     
/* 250 */     this.W[14] = paramLong2;
/* 251 */     this.W[15] = paramLong1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processBlock() {
/* 256 */     adjustByteCounts();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     for (byte b1 = 16; b1 <= 79; b1++)
/*     */     {
/* 263 */       this.W[b1] = Sigma1(this.W[b1 - 2]) + this.W[b1 - 7] + Sigma0(this.W[b1 - 15]) + this.W[b1 - 16];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     long l1 = this.H1;
/* 270 */     long l2 = this.H2;
/* 271 */     long l3 = this.H3;
/* 272 */     long l4 = this.H4;
/* 273 */     long l5 = this.H5;
/* 274 */     long l6 = this.H6;
/* 275 */     long l7 = this.H7;
/* 276 */     long l8 = this.H8;
/*     */     
/* 278 */     byte b2 = 0; byte b3;
/* 279 */     for (b3 = 0; b3 < 10; b3++) {
/*     */ 
/*     */       
/* 282 */       l8 += Sum1(l5) + Ch(l5, l6, l7) + K[b2] + this.W[b2++];
/* 283 */       l4 += l8;
/* 284 */       l8 += Sum0(l1) + Maj(l1, l2, l3);
/*     */ 
/*     */       
/* 287 */       l7 += Sum1(l4) + Ch(l4, l5, l6) + K[b2] + this.W[b2++];
/* 288 */       l3 += l7;
/* 289 */       l7 += Sum0(l8) + Maj(l8, l1, l2);
/*     */ 
/*     */       
/* 292 */       l6 += Sum1(l3) + Ch(l3, l4, l5) + K[b2] + this.W[b2++];
/* 293 */       l2 += l6;
/* 294 */       l6 += Sum0(l7) + Maj(l7, l8, l1);
/*     */ 
/*     */       
/* 297 */       l5 += Sum1(l2) + Ch(l2, l3, l4) + K[b2] + this.W[b2++];
/* 298 */       l1 += l5;
/* 299 */       l5 += Sum0(l6) + Maj(l6, l7, l8);
/*     */ 
/*     */       
/* 302 */       l4 += Sum1(l1) + Ch(l1, l2, l3) + K[b2] + this.W[b2++];
/* 303 */       l8 += l4;
/* 304 */       l4 += Sum0(l5) + Maj(l5, l6, l7);
/*     */ 
/*     */       
/* 307 */       l3 += Sum1(l8) + Ch(l8, l1, l2) + K[b2] + this.W[b2++];
/* 308 */       l7 += l3;
/* 309 */       l3 += Sum0(l4) + Maj(l4, l5, l6);
/*     */ 
/*     */       
/* 312 */       l2 += Sum1(l7) + Ch(l7, l8, l1) + K[b2] + this.W[b2++];
/* 313 */       l6 += l2;
/* 314 */       l2 += Sum0(l3) + Maj(l3, l4, l5);
/*     */ 
/*     */       
/* 317 */       l1 += Sum1(l6) + Ch(l6, l7, l8) + K[b2] + this.W[b2++];
/* 318 */       l5 += l1;
/* 319 */       l1 += Sum0(l2) + Maj(l2, l3, l4);
/*     */     } 
/*     */     
/* 322 */     this.H1 += l1;
/* 323 */     this.H2 += l2;
/* 324 */     this.H3 += l3;
/* 325 */     this.H4 += l4;
/* 326 */     this.H5 += l5;
/* 327 */     this.H6 += l6;
/* 328 */     this.H7 += l7;
/* 329 */     this.H8 += l8;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     this.wOff = 0;
/* 335 */     for (b3 = 0; b3 < 16; b3++)
/*     */     {
/* 337 */       this.W[b3] = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long Ch(long paramLong1, long paramLong2, long paramLong3) {
/* 347 */     return paramLong1 & paramLong2 ^ (paramLong1 ^ 0xFFFFFFFFFFFFFFFFL) & paramLong3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long Maj(long paramLong1, long paramLong2, long paramLong3) {
/* 355 */     return paramLong1 & paramLong2 ^ paramLong1 & paramLong3 ^ paramLong2 & paramLong3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long Sum0(long paramLong) {
/* 361 */     return (paramLong << 36L | paramLong >>> 28L) ^ (paramLong << 30L | paramLong >>> 34L) ^ (paramLong << 25L | paramLong >>> 39L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long Sum1(long paramLong) {
/* 367 */     return (paramLong << 50L | paramLong >>> 14L) ^ (paramLong << 46L | paramLong >>> 18L) ^ (paramLong << 23L | paramLong >>> 41L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long Sigma0(long paramLong) {
/* 373 */     return (paramLong << 63L | paramLong >>> 1L) ^ (paramLong << 56L | paramLong >>> 8L) ^ paramLong >>> 7L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long Sigma1(long paramLong) {
/* 379 */     return (paramLong << 45L | paramLong >>> 19L) ^ (paramLong << 3L | paramLong >>> 61L) ^ paramLong >>> 6L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 386 */   static final long[] K = new long[] { 4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L };
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/LongDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */