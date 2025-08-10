/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.ExtendedDigest;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Pack;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeccakDigest
/*     */   implements ExtendedDigest
/*     */ {
/*  16 */   private static long[] KeccakRoundConstants = new long[] { 1L, 32898L, -9223372036854742902L, -9223372034707259392L, 32907L, 2147483649L, -9223372034707259263L, -9223372036854743031L, 138L, 136L, 2147516425L, 2147483658L, 2147516555L, -9223372036854775669L, -9223372036854742903L, -9223372036854743037L, -9223372036854743038L, -9223372036854775680L, 32778L, -9223372034707292150L, -9223372034707259263L, -9223372036854742912L, 2147483649L, -9223372034707259384L };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   protected long[] state = new long[25];
/*  24 */   protected byte[] dataQueue = new byte[192];
/*     */   
/*     */   protected int rate;
/*     */   protected int bitsInQueue;
/*     */   protected int fixedOutputLength;
/*     */   protected boolean squeezing;
/*     */   
/*     */   public KeccakDigest() {
/*  32 */     this(288);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeccakDigest(int paramInt) {
/*  37 */     init(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public KeccakDigest(org.bouncycastle.crypto.digests.KeccakDigest paramKeccakDigest) {
/*  43 */     System.arraycopy(paramKeccakDigest.state, 0, this.state, 0, paramKeccakDigest.state.length);
/*  44 */     System.arraycopy(paramKeccakDigest.dataQueue, 0, this.dataQueue, 0, paramKeccakDigest.dataQueue.length);
/*  45 */     this.rate = paramKeccakDigest.rate;
/*  46 */     this.bitsInQueue = paramKeccakDigest.bitsInQueue;
/*  47 */     this.fixedOutputLength = paramKeccakDigest.fixedOutputLength;
/*  48 */     this.squeezing = paramKeccakDigest.squeezing;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  53 */     return "Keccak-" + this.fixedOutputLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDigestSize() {
/*  58 */     return this.fixedOutputLength / 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(byte paramByte) {
/*  63 */     absorb(paramByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  68 */     absorb(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/*  73 */     squeeze(paramArrayOfbyte, paramInt, this.fixedOutputLength);
/*     */     
/*  75 */     reset();
/*     */     
/*  77 */     return getDigestSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int doFinal(byte[] paramArrayOfbyte, int paramInt1, byte paramByte, int paramInt2) {
/*  85 */     if (paramInt2 > 0)
/*     */     {
/*  87 */       absorbBits(paramByte, paramInt2);
/*     */     }
/*     */     
/*  90 */     squeeze(paramArrayOfbyte, paramInt1, this.fixedOutputLength);
/*     */     
/*  92 */     reset();
/*     */     
/*  94 */     return getDigestSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  99 */     init(this.fixedOutputLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getByteLength() {
/* 109 */     return this.rate / 8;
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(int paramInt) {
/* 114 */     switch (paramInt) {
/*     */       
/*     */       case 128:
/*     */       case 224:
/*     */       case 256:
/*     */       case 288:
/*     */       case 384:
/*     */       case 512:
/* 122 */         initSponge(1600 - (paramInt << 1));
/*     */         return;
/*     */     } 
/* 125 */     throw new IllegalArgumentException("bitLength must be one of 128, 224, 256, 288, 384, or 512.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initSponge(int paramInt) {
/* 131 */     if (paramInt <= 0 || paramInt >= 1600 || paramInt % 64 != 0)
/*     */     {
/* 133 */       throw new IllegalStateException("invalid rate value");
/*     */     }
/*     */     
/* 136 */     this.rate = paramInt;
/* 137 */     for (byte b = 0; b < this.state.length; b++)
/*     */     {
/* 139 */       this.state[b] = 0L;
/*     */     }
/* 141 */     Arrays.fill(this.dataQueue, (byte)0);
/* 142 */     this.bitsInQueue = 0;
/* 143 */     this.squeezing = false;
/* 144 */     this.fixedOutputLength = (1600 - paramInt) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void absorb(byte paramByte) {
/* 149 */     if (this.bitsInQueue % 8 != 0)
/*     */     {
/* 151 */       throw new IllegalStateException("attempt to absorb with odd length queue");
/*     */     }
/* 153 */     if (this.squeezing)
/*     */     {
/* 155 */       throw new IllegalStateException("attempt to absorb while squeezing");
/*     */     }
/*     */     
/* 158 */     this.dataQueue[this.bitsInQueue >>> 3] = paramByte;
/* 159 */     if ((this.bitsInQueue += 8) == this.rate) {
/*     */       
/* 161 */       KeccakAbsorb(this.dataQueue, 0);
/* 162 */       this.bitsInQueue = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void absorb(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 168 */     if (this.bitsInQueue % 8 != 0)
/*     */     {
/* 170 */       throw new IllegalStateException("attempt to absorb with odd length queue");
/*     */     }
/* 172 */     if (this.squeezing)
/*     */     {
/* 174 */       throw new IllegalStateException("attempt to absorb while squeezing");
/*     */     }
/*     */     
/* 177 */     int i = this.bitsInQueue >>> 3;
/* 178 */     int j = this.rate >>> 3;
/*     */     
/* 180 */     int k = j - i;
/* 181 */     if (paramInt2 < k) {
/*     */       
/* 183 */       System.arraycopy(paramArrayOfbyte, paramInt1, this.dataQueue, i, paramInt2);
/* 184 */       this.bitsInQueue += paramInt2 << 3;
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     int m = 0;
/* 189 */     if (i > 0) {
/*     */       
/* 191 */       System.arraycopy(paramArrayOfbyte, paramInt1, this.dataQueue, i, k);
/* 192 */       m += k;
/* 193 */       KeccakAbsorb(this.dataQueue, 0);
/*     */     } 
/*     */     
/*     */     int n;
/* 197 */     while ((n = paramInt2 - m) >= j) {
/*     */       
/* 199 */       KeccakAbsorb(paramArrayOfbyte, paramInt1 + m);
/* 200 */       m += j;
/*     */     } 
/*     */     
/* 203 */     System.arraycopy(paramArrayOfbyte, paramInt1 + m, this.dataQueue, 0, n);
/* 204 */     this.bitsInQueue = n << 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void absorbBits(int paramInt1, int paramInt2) {
/* 209 */     if (paramInt2 < 1 || paramInt2 > 7)
/*     */     {
/* 211 */       throw new IllegalArgumentException("'bits' must be in the range 1 to 7");
/*     */     }
/* 213 */     if (this.bitsInQueue % 8 != 0)
/*     */     {
/* 215 */       throw new IllegalStateException("attempt to absorb with odd length queue");
/*     */     }
/* 217 */     if (this.squeezing)
/*     */     {
/* 219 */       throw new IllegalStateException("attempt to absorb while squeezing");
/*     */     }
/*     */     
/* 222 */     int i = (1 << paramInt2) - 1;
/* 223 */     this.dataQueue[this.bitsInQueue >>> 3] = (byte)(paramInt1 & i);
/*     */ 
/*     */     
/* 226 */     this.bitsInQueue += paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] dumpState() {
/* 232 */     byte[] arrayOfByte = new byte[this.state.length * 8];
/* 233 */     boolean bool = false;
/* 234 */     for (byte b = 0; b != this.state.length; b++) {
/*     */       
/* 236 */       Pack.longToLittleEndian(this.state[b], arrayOfByte, bool);
/* 237 */       bool += true;
/*     */     } 
/*     */     
/* 240 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void padAndSwitchToSqueezingPhase() {
/* 246 */     this.dataQueue[this.bitsInQueue >>> 3] = (byte)(this.dataQueue[this.bitsInQueue >>> 3] | (byte)(1 << (this.bitsInQueue & 0x7)));
/*     */     
/* 248 */     if (++this.bitsInQueue == this.rate) {
/*     */       
/* 250 */       KeccakAbsorb(this.dataQueue, 0);
/*     */     }
/*     */     else {
/*     */       
/* 254 */       int i = this.bitsInQueue >>> 6, j = this.bitsInQueue & 0x3F;
/* 255 */       boolean bool = false;
/* 256 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 258 */         this.state[b] = this.state[b] ^ Pack.littleEndianToLong(this.dataQueue, bool);
/* 259 */         bool += true;
/*     */       } 
/*     */       
/* 262 */       byte[] arrayOfByte = dumpState();
/*     */       
/* 264 */       if (j > 0) {
/*     */         
/* 266 */         long l = (1L << j) - 1L;
/* 267 */         this.state[i] = this.state[i] ^ Pack.littleEndianToLong(this.dataQueue, bool) & l;
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     this.state[this.rate - 1 >>> 6] = this.state[this.rate - 1 >>> 6] ^ Long.MIN_VALUE;
/*     */     
/* 273 */     this.bitsInQueue = 0;
/* 274 */     this.squeezing = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void squeeze(byte[] paramArrayOfbyte, int paramInt, long paramLong) {
/* 279 */     if (!this.squeezing)
/*     */     {
/* 281 */       padAndSwitchToSqueezingPhase();
/*     */     }
/*     */     
/* 284 */     byte[] arrayOfByte = dumpState();
/*     */     
/* 286 */     if (paramLong % 8L != 0L)
/*     */     {
/* 288 */       throw new IllegalStateException("outputLength not a multiple of 8");
/*     */     }
/*     */     
/* 291 */     long l = 0L;
/* 292 */     while (l < paramLong) {
/*     */       
/* 294 */       if (this.bitsInQueue == 0)
/*     */       {
/* 296 */         KeccakExtract();
/*     */       }
/* 298 */       int i = (int)Math.min(this.bitsInQueue, paramLong - l);
/* 299 */       System.arraycopy(this.dataQueue, (this.rate - this.bitsInQueue) / 8, paramArrayOfbyte, paramInt + (int)(l / 8L), i / 8);
/* 300 */       this.bitsInQueue -= i;
/* 301 */       l += i;
/*     */     } 
/*     */     
/* 304 */     arrayOfByte = dumpState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void KeccakAbsorb(byte[] paramArrayOfbyte, int paramInt) {
/* 312 */     int i = this.rate >>> 6;
/* 313 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 315 */       this.state[b] = this.state[b] ^ Pack.littleEndianToLong(paramArrayOfbyte, paramInt);
/* 316 */       paramInt += 8;
/*     */     } 
/* 318 */     String str = Hex.toHexString(dumpState()).toLowerCase();
/* 319 */     KeccakPermutation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void KeccakExtract() {
/* 326 */     KeccakPermutation();
/*     */     
/* 328 */     byte[] arrayOfByte = dumpState();
/*     */     
/* 330 */     Pack.longToLittleEndian(this.state, 0, this.rate >>> 6, this.dataQueue, 0);
/*     */     
/* 332 */     this.bitsInQueue = this.rate;
/*     */   }
/*     */ 
/*     */   
/*     */   private void KeccakPermutation() {
/* 337 */     long[] arrayOfLong = this.state;
/*     */     
/* 339 */     long l1 = arrayOfLong[0], l2 = arrayOfLong[1], l3 = arrayOfLong[2], l4 = arrayOfLong[3], l5 = arrayOfLong[4];
/* 340 */     long l6 = arrayOfLong[5], l7 = arrayOfLong[6], l8 = arrayOfLong[7], l9 = arrayOfLong[8], l10 = arrayOfLong[9];
/* 341 */     long l11 = arrayOfLong[10], l12 = arrayOfLong[11], l13 = arrayOfLong[12], l14 = arrayOfLong[13], l15 = arrayOfLong[14];
/* 342 */     long l16 = arrayOfLong[15], l17 = arrayOfLong[16], l18 = arrayOfLong[17], l19 = arrayOfLong[18], l20 = arrayOfLong[19];
/* 343 */     long l21 = arrayOfLong[20], l22 = arrayOfLong[21], l23 = arrayOfLong[22], l24 = arrayOfLong[23], l25 = arrayOfLong[24];
/*     */     
/* 345 */     for (byte b = 0; b < 24; b++) {
/*     */ 
/*     */       
/* 348 */       long l26 = l1 ^ l6 ^ l11 ^ l16 ^ l21;
/* 349 */       long l27 = l2 ^ l7 ^ l12 ^ l17 ^ l22;
/* 350 */       long l28 = l3 ^ l8 ^ l13 ^ l18 ^ l23;
/* 351 */       long l29 = l4 ^ l9 ^ l14 ^ l19 ^ l24;
/* 352 */       long l30 = l5 ^ l10 ^ l15 ^ l20 ^ l25;
/*     */       
/* 354 */       long l31 = (l27 << 1L | l27 >>> -1L) ^ l30;
/* 355 */       long l32 = (l28 << 1L | l28 >>> -1L) ^ l26;
/* 356 */       long l33 = (l29 << 1L | l29 >>> -1L) ^ l27;
/* 357 */       long l34 = (l30 << 1L | l30 >>> -1L) ^ l28;
/* 358 */       long l35 = (l26 << 1L | l26 >>> -1L) ^ l29;
/*     */       
/* 360 */       l1 ^= l31;
/* 361 */       l6 ^= l31;
/* 362 */       l11 ^= l31;
/* 363 */       l16 ^= l31;
/* 364 */       l21 ^= l31;
/* 365 */       l2 ^= l32;
/* 366 */       l7 ^= l32;
/* 367 */       l12 ^= l32;
/* 368 */       l17 ^= l32;
/* 369 */       l22 ^= l32;
/* 370 */       l3 ^= l33;
/* 371 */       l8 ^= l33;
/* 372 */       l13 ^= l33;
/* 373 */       l18 ^= l33;
/* 374 */       l23 ^= l33;
/* 375 */       l4 ^= l34;
/* 376 */       l9 ^= l34;
/* 377 */       l14 ^= l34;
/* 378 */       l19 ^= l34;
/* 379 */       l24 ^= l34;
/* 380 */       l5 ^= l35;
/* 381 */       l10 ^= l35;
/* 382 */       l15 ^= l35;
/* 383 */       l20 ^= l35;
/* 384 */       l25 ^= l35;
/*     */ 
/*     */       
/* 387 */       l27 = l2 << 1L | l2 >>> 63L;
/* 388 */       l2 = l7 << 44L | l7 >>> 20L;
/* 389 */       l7 = l10 << 20L | l10 >>> 44L;
/* 390 */       l10 = l23 << 61L | l23 >>> 3L;
/* 391 */       l23 = l15 << 39L | l15 >>> 25L;
/* 392 */       l15 = l21 << 18L | l21 >>> 46L;
/* 393 */       l21 = l3 << 62L | l3 >>> 2L;
/* 394 */       l3 = l13 << 43L | l13 >>> 21L;
/* 395 */       l13 = l14 << 25L | l14 >>> 39L;
/* 396 */       l14 = l20 << 8L | l20 >>> 56L;
/* 397 */       l20 = l24 << 56L | l24 >>> 8L;
/* 398 */       l24 = l16 << 41L | l16 >>> 23L;
/* 399 */       l16 = l5 << 27L | l5 >>> 37L;
/* 400 */       l5 = l25 << 14L | l25 >>> 50L;
/* 401 */       l25 = l22 << 2L | l22 >>> 62L;
/* 402 */       l22 = l9 << 55L | l9 >>> 9L;
/* 403 */       l9 = l17 << 45L | l17 >>> 19L;
/* 404 */       l17 = l6 << 36L | l6 >>> 28L;
/* 405 */       l6 = l4 << 28L | l4 >>> 36L;
/* 406 */       l4 = l19 << 21L | l19 >>> 43L;
/* 407 */       l19 = l18 << 15L | l18 >>> 49L;
/* 408 */       l18 = l12 << 10L | l12 >>> 54L;
/* 409 */       l12 = l8 << 6L | l8 >>> 58L;
/* 410 */       l8 = l11 << 3L | l11 >>> 61L;
/* 411 */       l11 = l27;
/*     */ 
/*     */       
/* 414 */       l26 = l1 ^ (l2 ^ 0xFFFFFFFFFFFFFFFFL) & l3;
/* 415 */       l27 = l2 ^ (l3 ^ 0xFFFFFFFFFFFFFFFFL) & l4;
/* 416 */       l3 ^= (l4 ^ 0xFFFFFFFFFFFFFFFFL) & l5;
/* 417 */       l4 ^= (l5 ^ 0xFFFFFFFFFFFFFFFFL) & l1;
/* 418 */       l5 ^= (l1 ^ 0xFFFFFFFFFFFFFFFFL) & l2;
/* 419 */       l1 = l26;
/* 420 */       l2 = l27;
/*     */       
/* 422 */       l26 = l6 ^ (l7 ^ 0xFFFFFFFFFFFFFFFFL) & l8;
/* 423 */       l27 = l7 ^ (l8 ^ 0xFFFFFFFFFFFFFFFFL) & l9;
/* 424 */       l8 ^= (l9 ^ 0xFFFFFFFFFFFFFFFFL) & l10;
/* 425 */       l9 ^= (l10 ^ 0xFFFFFFFFFFFFFFFFL) & l6;
/* 426 */       l10 ^= (l6 ^ 0xFFFFFFFFFFFFFFFFL) & l7;
/* 427 */       l6 = l26;
/* 428 */       l7 = l27;
/*     */       
/* 430 */       l26 = l11 ^ (l12 ^ 0xFFFFFFFFFFFFFFFFL) & l13;
/* 431 */       l27 = l12 ^ (l13 ^ 0xFFFFFFFFFFFFFFFFL) & l14;
/* 432 */       l13 ^= (l14 ^ 0xFFFFFFFFFFFFFFFFL) & l15;
/* 433 */       l14 ^= (l15 ^ 0xFFFFFFFFFFFFFFFFL) & l11;
/* 434 */       l15 ^= (l11 ^ 0xFFFFFFFFFFFFFFFFL) & l12;
/* 435 */       l11 = l26;
/* 436 */       l12 = l27;
/*     */       
/* 438 */       l26 = l16 ^ (l17 ^ 0xFFFFFFFFFFFFFFFFL) & l18;
/* 439 */       l27 = l17 ^ (l18 ^ 0xFFFFFFFFFFFFFFFFL) & l19;
/* 440 */       l18 ^= (l19 ^ 0xFFFFFFFFFFFFFFFFL) & l20;
/* 441 */       l19 ^= (l20 ^ 0xFFFFFFFFFFFFFFFFL) & l16;
/* 442 */       l20 ^= (l16 ^ 0xFFFFFFFFFFFFFFFFL) & l17;
/* 443 */       l16 = l26;
/* 444 */       l17 = l27;
/*     */       
/* 446 */       l26 = l21 ^ (l22 ^ 0xFFFFFFFFFFFFFFFFL) & l23;
/* 447 */       l27 = l22 ^ (l23 ^ 0xFFFFFFFFFFFFFFFFL) & l24;
/* 448 */       l23 ^= (l24 ^ 0xFFFFFFFFFFFFFFFFL) & l25;
/* 449 */       l24 ^= (l25 ^ 0xFFFFFFFFFFFFFFFFL) & l21;
/* 450 */       l25 ^= (l21 ^ 0xFFFFFFFFFFFFFFFFL) & l22;
/* 451 */       l21 = l26;
/* 452 */       l22 = l27;
/*     */ 
/*     */       
/* 455 */       l1 ^= KeccakRoundConstants[b];
/*     */     } 
/*     */     
/* 458 */     arrayOfLong[0] = l1;
/* 459 */     arrayOfLong[1] = l2;
/* 460 */     arrayOfLong[2] = l3;
/* 461 */     arrayOfLong[3] = l4;
/* 462 */     arrayOfLong[4] = l5;
/* 463 */     arrayOfLong[5] = l6;
/* 464 */     arrayOfLong[6] = l7;
/* 465 */     arrayOfLong[7] = l8;
/* 466 */     arrayOfLong[8] = l9;
/* 467 */     arrayOfLong[9] = l10;
/* 468 */     arrayOfLong[10] = l11;
/* 469 */     arrayOfLong[11] = l12;
/* 470 */     arrayOfLong[12] = l13;
/* 471 */     arrayOfLong[13] = l14;
/* 472 */     arrayOfLong[14] = l15;
/* 473 */     arrayOfLong[15] = l16;
/* 474 */     arrayOfLong[16] = l17;
/* 475 */     arrayOfLong[17] = l18;
/* 476 */     arrayOfLong[18] = l19;
/* 477 */     arrayOfLong[19] = l20;
/* 478 */     arrayOfLong[20] = l21;
/* 479 */     arrayOfLong[21] = l22;
/* 480 */     arrayOfLong[22] = l23;
/* 481 */     arrayOfLong[23] = l24;
/* 482 */     arrayOfLong[24] = l25;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/KeccakDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */