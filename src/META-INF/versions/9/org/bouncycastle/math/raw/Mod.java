/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.util.Random;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Mod
/*     */ {
/*     */   private static final int M30 = 1073741823;
/*     */   private static final long M32L = 4294967295L;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int[] paramArrayOfint4) {
/*  20 */     int i = paramArrayOfint1.length;
/*  21 */     int j = Nat.add(i, paramArrayOfint2, paramArrayOfint3, paramArrayOfint4);
/*  22 */     if (j != 0)
/*     */     {
/*  24 */       Nat.subFrom(i, paramArrayOfint1, paramArrayOfint4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void checkedModOddInverse(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  30 */     if (0 == modOddInverse(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3))
/*     */     {
/*  32 */       throw new ArithmeticException("Inverse does not exist.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void checkedModOddInverseVar(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  38 */     if (!modOddInverseVar(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3))
/*     */     {
/*  40 */       throw new ArithmeticException("Inverse does not exist.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int inverse32(int paramInt) {
/*  48 */     int i = paramInt;
/*  49 */     i *= 2 - paramInt * i;
/*  50 */     i *= 2 - paramInt * i;
/*  51 */     i *= 2 - paramInt * i;
/*  52 */     i *= 2 - paramInt * i;
/*     */     
/*  54 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invert(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  60 */     checkedModOddInverseVar(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int modOddInverse(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  65 */     int i = paramArrayOfint1.length;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     int j = (i << 5) - Integers.numberOfLeadingZeros(paramArrayOfint1[i - 1]);
/*  71 */     int k = (j + 29) / 30;
/*     */     
/*  73 */     int[] arrayOfInt1 = new int[4];
/*  74 */     int[] arrayOfInt2 = new int[k];
/*  75 */     int[] arrayOfInt3 = new int[k];
/*  76 */     int[] arrayOfInt4 = new int[k];
/*  77 */     int[] arrayOfInt5 = new int[k];
/*  78 */     int[] arrayOfInt6 = new int[k];
/*     */     
/*  80 */     arrayOfInt3[0] = 1;
/*  81 */     encode30(j, paramArrayOfint2, 0, arrayOfInt5, 0);
/*  82 */     encode30(j, paramArrayOfint1, 0, arrayOfInt6, 0);
/*  83 */     System.arraycopy(arrayOfInt6, 0, arrayOfInt4, 0, k);
/*     */     
/*  85 */     int m = -1;
/*  86 */     int n = inverse32(arrayOfInt6[0]);
/*  87 */     int i1 = getMaximumDivsteps(j);
/*     */     int i2;
/*  89 */     for (i2 = 0; i2 < i1; i2 += 30) {
/*     */       
/*  91 */       m = divsteps30(m, arrayOfInt4[0], arrayOfInt5[0], arrayOfInt1);
/*  92 */       updateDE30(k, arrayOfInt2, arrayOfInt3, arrayOfInt1, n, arrayOfInt6);
/*  93 */       updateFG30(k, arrayOfInt4, arrayOfInt5, arrayOfInt1);
/*     */     } 
/*     */     
/*  96 */     i2 = arrayOfInt4[k - 1] >> 31;
/*  97 */     cnegate30(k, i2, arrayOfInt4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     cnormalize30(k, i2, arrayOfInt2, arrayOfInt6);
/*     */     
/* 106 */     decode30(j, arrayOfInt2, 0, paramArrayOfint3, 0);
/*     */ 
/*     */     
/* 109 */     return Nat.equalTo(k, arrayOfInt4, 1) & Nat.equalToZero(k, arrayOfInt5);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean modOddInverseVar(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 114 */     int i = paramArrayOfint1.length;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     int j = (i << 5) - Integers.numberOfLeadingZeros(paramArrayOfint1[i - 1]);
/* 120 */     int k = (j + 29) / 30;
/*     */     
/* 122 */     int[] arrayOfInt1 = new int[4];
/* 123 */     int[] arrayOfInt2 = new int[k];
/* 124 */     int[] arrayOfInt3 = new int[k];
/* 125 */     int[] arrayOfInt4 = new int[k];
/* 126 */     int[] arrayOfInt5 = new int[k];
/* 127 */     int[] arrayOfInt6 = new int[k];
/*     */     
/* 129 */     arrayOfInt3[0] = 1;
/* 130 */     encode30(j, paramArrayOfint2, 0, arrayOfInt5, 0);
/* 131 */     encode30(j, paramArrayOfint1, 0, arrayOfInt6, 0);
/* 132 */     System.arraycopy(arrayOfInt6, 0, arrayOfInt4, 0, k);
/*     */     
/* 134 */     int m = Integers.numberOfLeadingZeros(arrayOfInt5[k - 1] | 0x1) - k * 30 + 2 - j;
/* 135 */     int n = -1 - m;
/* 136 */     int i1 = k, i2 = k;
/* 137 */     int i3 = inverse32(arrayOfInt6[0]);
/* 138 */     int i4 = getMaximumDivsteps(j);
/*     */     
/* 140 */     byte b = 0;
/* 141 */     while (!Nat.isZero(i2, arrayOfInt5)) {
/*     */       
/* 143 */       if (b >= i4)
/*     */       {
/* 145 */         return false;
/*     */       }
/*     */       
/* 148 */       b += 30;
/*     */       
/* 150 */       n = divsteps30Var(n, arrayOfInt4[0], arrayOfInt5[0], arrayOfInt1);
/* 151 */       updateDE30(i1, arrayOfInt2, arrayOfInt3, arrayOfInt1, i3, arrayOfInt6);
/* 152 */       updateFG30(i2, arrayOfInt4, arrayOfInt5, arrayOfInt1);
/*     */       
/* 154 */       int i7 = arrayOfInt4[i2 - 1];
/* 155 */       int i8 = arrayOfInt5[i2 - 1];
/*     */       
/* 157 */       int i9 = i2 - 2 >> 31;
/* 158 */       i9 |= i7 ^ i7 >> 31;
/* 159 */       i9 |= i8 ^ i8 >> 31;
/*     */       
/* 161 */       if (i9 == 0) {
/*     */         
/* 163 */         arrayOfInt4[i2 - 2] = arrayOfInt4[i2 - 2] | i7 << 30;
/* 164 */         arrayOfInt5[i2 - 2] = arrayOfInt5[i2 - 2] | i8 << 30;
/* 165 */         i2--;
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     int i5 = arrayOfInt4[i2 - 1] >> 31;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     int i6 = arrayOfInt2[i1 - 1] >> 31;
/* 177 */     if (i6 < 0)
/*     */     {
/* 179 */       i6 = add30(i1, arrayOfInt2, arrayOfInt6);
/*     */     }
/* 181 */     if (i5 < 0) {
/*     */       
/* 183 */       i6 = negate30(i1, arrayOfInt2);
/* 184 */       i5 = negate30(i2, arrayOfInt4);
/*     */     } 
/*     */ 
/*     */     
/* 188 */     if (!Nat.isOne(i2, arrayOfInt4))
/*     */     {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     if (i6 < 0)
/*     */     {
/* 195 */       i6 = add30(i1, arrayOfInt2, arrayOfInt6);
/*     */     }
/*     */ 
/*     */     
/* 199 */     decode30(j, arrayOfInt2, 0, paramArrayOfint3, 0);
/*     */ 
/*     */     
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] random(int[] paramArrayOfint) {
/* 207 */     int i = paramArrayOfint.length;
/* 208 */     Random random = new Random();
/* 209 */     int[] arrayOfInt = Nat.create(i);
/*     */     
/* 211 */     int j = paramArrayOfint[i - 1];
/* 212 */     j |= j >>> 1;
/* 213 */     j |= j >>> 2;
/* 214 */     j |= j >>> 4;
/* 215 */     j |= j >>> 8;
/* 216 */     j |= j >>> 16;
/*     */ 
/*     */     
/*     */     do {
/* 220 */       for (int k = 0; k != i; k++)
/*     */       {
/* 222 */         arrayOfInt[k] = random.nextInt();
/*     */       }
/* 224 */       arrayOfInt[i - 1] = arrayOfInt[i - 1] & j;
/*     */     }
/* 226 */     while (Nat.gte(i, arrayOfInt, paramArrayOfint));
/*     */     
/* 228 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int[] paramArrayOfint4) {
/* 234 */     int i = paramArrayOfint1.length;
/* 235 */     int j = Nat.sub(i, paramArrayOfint2, paramArrayOfint3, paramArrayOfint4);
/* 236 */     if (j != 0)
/*     */     {
/* 238 */       Nat.addTo(i, paramArrayOfint1, paramArrayOfint4);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int add30(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 248 */     int i = 0, j = paramInt - 1;
/* 249 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 251 */       i += paramArrayOfint1[b] + paramArrayOfint2[b];
/* 252 */       paramArrayOfint1[b] = i & 0x3FFFFFFF; i >>= 30;
/*     */     } 
/* 254 */     i += paramArrayOfint1[j] + paramArrayOfint2[j];
/* 255 */     paramArrayOfint1[j] = i; i >>= 30;
/* 256 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void cnegate30(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/* 264 */     int i = 0, j = paramInt1 - 1;
/* 265 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 267 */       i += (paramArrayOfint[b] ^ paramInt2) - paramInt2;
/* 268 */       paramArrayOfint[b] = i & 0x3FFFFFFF; i >>= 30;
/*     */     } 
/* 270 */     i += (paramArrayOfint[j] ^ paramInt2) - paramInt2;
/* 271 */     paramArrayOfint[j] = i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void cnormalize30(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 280 */     int i = paramInt1 - 1;
/*     */ 
/*     */     
/* 283 */     int j = 0, k = paramArrayOfint1[i] >> 31; int m;
/* 284 */     for (m = 0; m < i; m++) {
/*     */       
/* 286 */       int n = paramArrayOfint1[m] + (paramArrayOfint2[m] & k);
/* 287 */       n = (n ^ paramInt2) - paramInt2;
/* 288 */       j += n; paramArrayOfint1[m] = j & 0x3FFFFFFF; j >>= 30;
/*     */     } 
/*     */     
/* 291 */     m = paramArrayOfint1[i] + (paramArrayOfint2[i] & k);
/* 292 */     m = (m ^ paramInt2) - paramInt2;
/* 293 */     j += m; paramArrayOfint1[i] = j;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     j = 0; k = paramArrayOfint1[i] >> 31;
/* 299 */     for (m = 0; m < i; m++) {
/*     */       
/* 301 */       int n = paramArrayOfint1[m] + (paramArrayOfint2[m] & k);
/* 302 */       j += n; paramArrayOfint1[m] = j & 0x3FFFFFFF; j >>= 30;
/*     */     } 
/*     */     
/* 305 */     m = paramArrayOfint1[i] + (paramArrayOfint2[i] & k);
/* 306 */     j += m; paramArrayOfint1[i] = j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void decode30(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 317 */     byte b = 0;
/* 318 */     long l = 0L;
/*     */     
/* 320 */     while (paramInt1 > 0) {
/*     */       
/* 322 */       while (b < Math.min(32, paramInt1)) {
/*     */         
/* 324 */         l |= paramArrayOfint1[paramInt2++] << b;
/* 325 */         b += 30;
/*     */       } 
/*     */       
/* 328 */       paramArrayOfint2[paramInt3++] = (int)l; l >>>= 32L;
/* 329 */       b -= 32;
/* 330 */       paramInt1 -= 32;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int divsteps30(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
/* 336 */     int i = 1, j = 0, k = 0, m = 1;
/* 337 */     int n = paramInt2, i1 = paramInt3;
/*     */     
/* 339 */     for (byte b = 0; b < 30; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 345 */       int i2 = paramInt1 >> 31;
/* 346 */       int i3 = -(i1 & 0x1);
/*     */       
/* 348 */       int i4 = (n ^ i2) - i2;
/* 349 */       int i5 = (i ^ i2) - i2;
/* 350 */       int i6 = (j ^ i2) - i2;
/*     */       
/* 352 */       i1 += i4 & i3;
/* 353 */       k += i5 & i3;
/* 354 */       m += i6 & i3;
/*     */       
/* 356 */       i2 &= i3;
/* 357 */       paramInt1 = (paramInt1 ^ i2) - i2 + 1;
/*     */       
/* 359 */       n += i1 & i2;
/* 360 */       i += k & i2;
/* 361 */       j += m & i2;
/*     */       
/* 363 */       i1 >>= 1;
/* 364 */       i <<= 1;
/* 365 */       j <<= 1;
/*     */     } 
/*     */     
/* 368 */     paramArrayOfint[0] = i;
/* 369 */     paramArrayOfint[1] = j;
/* 370 */     paramArrayOfint[2] = k;
/* 371 */     paramArrayOfint[3] = m;
/*     */     
/* 373 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int divsteps30Var(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
/* 378 */     int i = 1, j = 0, k = 0, m = 1;
/* 379 */     int n = paramInt2, i1 = paramInt3;
/* 380 */     int i2 = 30;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 385 */       int i3, i4 = Integers.numberOfTrailingZeros(i1 | -1 << i2);
/*     */       
/* 387 */       i1 >>= i4;
/* 388 */       i <<= i4;
/* 389 */       j <<= i4;
/* 390 */       paramInt1 -= i4;
/* 391 */       i2 -= i4;
/*     */       
/* 393 */       if (i2 <= 0) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 403 */       if (paramInt1 < 0) {
/*     */         
/* 405 */         paramInt1 = -paramInt1;
/* 406 */         int i6 = n; n = i1; i1 = -i6;
/* 407 */         int i7 = i; i = k; k = -i7;
/* 408 */         int i8 = j; j = m; m = -i8;
/*     */ 
/*     */         
/* 411 */         int i9 = (paramInt1 + 1 > i2) ? i2 : (paramInt1 + 1);
/* 412 */         int i5 = -1 >>> 32 - i9 & 0x3F;
/*     */         
/* 414 */         i3 = n * i1 * (n * n - 2) & i5;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 419 */         int i6 = (paramInt1 + 1 > i2) ? i2 : (paramInt1 + 1);
/* 420 */         int i5 = -1 >>> 32 - i6 & 0xF;
/*     */         
/* 422 */         i3 = n + ((n + 1 & 0x4) << 1);
/* 423 */         i3 = -i3 * i1 & i5;
/*     */       } 
/*     */       
/* 426 */       i1 += n * i3;
/* 427 */       k += i * i3;
/* 428 */       m += j * i3;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 433 */     paramArrayOfint[0] = i;
/* 434 */     paramArrayOfint[1] = j;
/* 435 */     paramArrayOfint[2] = k;
/* 436 */     paramArrayOfint[3] = m;
/*     */     
/* 438 */     return paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encode30(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 446 */     byte b = 0;
/* 447 */     long l = 0L;
/*     */     
/* 449 */     while (paramInt1 > 0) {
/*     */       
/* 451 */       if (b < Math.min(30, paramInt1)) {
/*     */         
/* 453 */         l |= (paramArrayOfint1[paramInt2++] & 0xFFFFFFFFL) << b;
/* 454 */         b += 32;
/*     */       } 
/*     */       
/* 457 */       paramArrayOfint2[paramInt3++] = (int)l & 0x3FFFFFFF; l >>>= 30L;
/* 458 */       b -= 30;
/* 459 */       paramInt1 -= 30;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getMaximumDivsteps(int paramInt) {
/* 465 */     return (49 * paramInt + ((paramInt < 46) ? 80 : 47)) / 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int negate30(int paramInt, int[] paramArrayOfint) {
/* 473 */     int i = 0, j = paramInt - 1;
/* 474 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 476 */       i -= paramArrayOfint[b];
/* 477 */       paramArrayOfint[b] = i & 0x3FFFFFFF; i >>= 30;
/*     */     } 
/* 479 */     i -= paramArrayOfint[j];
/* 480 */     paramArrayOfint[j] = i; i >>= 30;
/* 481 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void updateDE30(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3, int paramInt2, int[] paramArrayOfint4) {
/* 492 */     int i = paramArrayOfint3[0], j = paramArrayOfint3[1], k = paramArrayOfint3[2], m = paramArrayOfint3[3];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 501 */     int i5 = paramArrayOfint1[paramInt1 - 1] >> 31;
/* 502 */     int i6 = paramArrayOfint2[paramInt1 - 1] >> 31;
/*     */     
/* 504 */     int i2 = (i & i5) + (j & i6);
/* 505 */     int i3 = (k & i5) + (m & i6);
/*     */     
/* 507 */     int i4 = paramArrayOfint4[0];
/* 508 */     int n = paramArrayOfint1[0];
/* 509 */     int i1 = paramArrayOfint2[0];
/*     */     
/* 511 */     long l1 = i * n + j * i1;
/* 512 */     long l2 = k * n + m * i1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 519 */     i2 -= paramInt2 * (int)l1 + i2 & 0x3FFFFFFF;
/* 520 */     i3 -= paramInt2 * (int)l2 + i3 & 0x3FFFFFFF;
/*     */     
/* 522 */     l1 += i4 * i2;
/* 523 */     l2 += i4 * i3;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 528 */     l1 >>= 30L;
/* 529 */     l2 >>= 30L;
/*     */     
/* 531 */     for (byte b = 1; b < paramInt1; b++) {
/*     */       
/* 533 */       i4 = paramArrayOfint4[b];
/* 534 */       n = paramArrayOfint1[b];
/* 535 */       i1 = paramArrayOfint2[b];
/*     */       
/* 537 */       l1 += i * n + j * i1 + i4 * i2;
/* 538 */       l2 += k * n + m * i1 + i4 * i3;
/*     */       
/* 540 */       paramArrayOfint1[b - 1] = (int)l1 & 0x3FFFFFFF; l1 >>= 30L;
/* 541 */       paramArrayOfint2[b - 1] = (int)l2 & 0x3FFFFFFF; l2 >>= 30L;
/*     */     } 
/*     */     
/* 544 */     paramArrayOfint1[paramInt1 - 1] = (int)l1;
/* 545 */     paramArrayOfint2[paramInt1 - 1] = (int)l2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void updateFG30(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 554 */     int i = paramArrayOfint3[0], j = paramArrayOfint3[1], k = paramArrayOfint3[2], m = paramArrayOfint3[3];
/*     */ 
/*     */ 
/*     */     
/* 558 */     int n = paramArrayOfint1[0];
/* 559 */     int i1 = paramArrayOfint2[0];
/*     */     
/* 561 */     long l1 = i * n + j * i1;
/* 562 */     long l2 = k * n + m * i1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 567 */     l1 >>= 30L;
/* 568 */     l2 >>= 30L;
/*     */     
/* 570 */     for (byte b = 1; b < paramInt; b++) {
/*     */       
/* 572 */       n = paramArrayOfint1[b];
/* 573 */       i1 = paramArrayOfint2[b];
/*     */       
/* 575 */       l1 += i * n + j * i1;
/* 576 */       l2 += k * n + m * i1;
/*     */       
/* 578 */       paramArrayOfint1[b - 1] = (int)l1 & 0x3FFFFFFF; l1 >>= 30L;
/* 579 */       paramArrayOfint2[b - 1] = (int)l2 & 0x3FFFFFFF; l2 >>= 30L;
/*     */     } 
/*     */     
/* 582 */     paramArrayOfint1[paramInt - 1] = (int)l1;
/* 583 */     paramArrayOfint2[paramInt - 1] = (int)l2;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Mod.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */