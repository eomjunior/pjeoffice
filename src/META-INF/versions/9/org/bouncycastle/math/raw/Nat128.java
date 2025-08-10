/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ public abstract class Nat128
/*     */ {
/*     */   private static final long M = 4294967295L;
/*     */   
/*     */   public static int add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  13 */     long l = 0L;
/*  14 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  15 */     paramArrayOfint3[0] = (int)l;
/*  16 */     l >>>= 32L;
/*  17 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  18 */     paramArrayOfint3[1] = (int)l;
/*  19 */     l >>>= 32L;
/*  20 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  21 */     paramArrayOfint3[2] = (int)l;
/*  22 */     l >>>= 32L;
/*  23 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  24 */     paramArrayOfint3[3] = (int)l;
/*  25 */     l >>>= 32L;
/*  26 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addBothTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  31 */     long l = 0L;
/*  32 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint3[0] & 0xFFFFFFFFL);
/*  33 */     paramArrayOfint3[0] = (int)l;
/*  34 */     l >>>= 32L;
/*  35 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint3[1] & 0xFFFFFFFFL);
/*  36 */     paramArrayOfint3[1] = (int)l;
/*  37 */     l >>>= 32L;
/*  38 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint3[2] & 0xFFFFFFFFL);
/*  39 */     paramArrayOfint3[2] = (int)l;
/*  40 */     l >>>= 32L;
/*  41 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint3[3] & 0xFFFFFFFFL);
/*  42 */     paramArrayOfint3[3] = (int)l;
/*  43 */     l >>>= 32L;
/*  44 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addTo(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  49 */     long l = 0L;
/*  50 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  51 */     paramArrayOfint2[0] = (int)l;
/*  52 */     l >>>= 32L;
/*  53 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  54 */     paramArrayOfint2[1] = (int)l;
/*  55 */     l >>>= 32L;
/*  56 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  57 */     paramArrayOfint2[2] = (int)l;
/*  58 */     l >>>= 32L;
/*  59 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  60 */     paramArrayOfint2[3] = (int)l;
/*  61 */     l >>>= 32L;
/*  62 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int paramInt3) {
/*  67 */     long l = paramInt3 & 0xFFFFFFFFL;
/*  68 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  69 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  70 */     l >>>= 32L;
/*  71 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  72 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  73 */     l >>>= 32L;
/*  74 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  75 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  76 */     l >>>= 32L;
/*  77 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  78 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  79 */     l >>>= 32L;
/*  80 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addToEachOther(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  85 */     long l = 0L;
/*  86 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  87 */     paramArrayOfint1[paramInt1 + 0] = (int)l;
/*  88 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  89 */     l >>>= 32L;
/*  90 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  91 */     paramArrayOfint1[paramInt1 + 1] = (int)l;
/*  92 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  93 */     l >>>= 32L;
/*  94 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  95 */     paramArrayOfint1[paramInt1 + 2] = (int)l;
/*  96 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  97 */     l >>>= 32L;
/*  98 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  99 */     paramArrayOfint1[paramInt1 + 3] = (int)l;
/* 100 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 101 */     l >>>= 32L;
/* 102 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 107 */     paramArrayOfint2[0] = paramArrayOfint1[0];
/* 108 */     paramArrayOfint2[1] = paramArrayOfint1[1];
/* 109 */     paramArrayOfint2[2] = paramArrayOfint1[2];
/* 110 */     paramArrayOfint2[3] = paramArrayOfint1[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 115 */     paramArrayOfint2[paramInt2 + 0] = paramArrayOfint1[paramInt1 + 0];
/* 116 */     paramArrayOfint2[paramInt2 + 1] = paramArrayOfint1[paramInt1 + 1];
/* 117 */     paramArrayOfint2[paramInt2 + 2] = paramArrayOfint1[paramInt1 + 2];
/* 118 */     paramArrayOfint2[paramInt2 + 3] = paramArrayOfint1[paramInt1 + 3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 123 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/* 124 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/* 129 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/* 130 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] create() {
/* 135 */     return new int[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] create64() {
/* 140 */     return new long[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] createExt() {
/* 145 */     return new int[8];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] createExt64() {
/* 150 */     return new long[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean diff(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 155 */     boolean bool = gte(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2);
/* 156 */     if (bool) {
/*     */       
/* 158 */       sub(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2, paramArrayOfint3, paramInt3);
/*     */     }
/*     */     else {
/*     */       
/* 162 */       sub(paramArrayOfint2, paramInt2, paramArrayOfint1, paramInt1, paramArrayOfint3, paramInt3);
/*     */     } 
/* 164 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 169 */     for (byte b = 3; b >= 0; b--) {
/*     */       
/* 171 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*     */       {
/* 173 */         return false;
/*     */       }
/*     */     } 
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 181 */     for (byte b = 1; b; b--) {
/*     */       
/* 183 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*     */       {
/* 185 */         return false;
/*     */       }
/*     */     } 
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/* 193 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 128)
/*     */     {
/* 195 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 198 */     int[] arrayOfInt = create();
/*     */ 
/*     */     
/* 201 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 203 */       arrayOfInt[b] = paramBigInteger.intValue();
/* 204 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*     */     } 
/* 206 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/* 211 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 128)
/*     */     {
/* 213 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 216 */     long[] arrayOfLong = create64();
/*     */ 
/*     */     
/* 219 */     for (byte b = 0; b < 2; b++) {
/*     */       
/* 221 */       arrayOfLong[b] = paramBigInteger.longValue();
/* 222 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*     */     } 
/* 224 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/* 229 */     if (paramInt == 0)
/*     */     {
/* 231 */       return paramArrayOfint[0] & 0x1;
/*     */     }
/* 233 */     int i = paramInt >> 5;
/* 234 */     if (i < 0 || i >= 4)
/*     */     {
/* 236 */       return 0;
/*     */     }
/* 238 */     int j = paramInt & 0x1F;
/* 239 */     return paramArrayOfint[i] >>> j & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean gte(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 244 */     for (byte b = 3; b >= 0; b--) {
/*     */       
/* 246 */       int i = paramArrayOfint1[b] ^ Integer.MIN_VALUE;
/* 247 */       int j = paramArrayOfint2[b] ^ Integer.MIN_VALUE;
/* 248 */       if (i < j)
/* 249 */         return false; 
/* 250 */       if (i > j)
/* 251 */         return true; 
/*     */     } 
/* 253 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean gte(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 258 */     for (byte b = 3; b >= 0; b--) {
/*     */       
/* 260 */       int i = paramArrayOfint1[paramInt1 + b] ^ Integer.MIN_VALUE;
/* 261 */       int j = paramArrayOfint2[paramInt2 + b] ^ Integer.MIN_VALUE;
/* 262 */       if (i < j)
/* 263 */         return false; 
/* 264 */       if (i > j)
/* 265 */         return true; 
/*     */     } 
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne(int[] paramArrayOfint) {
/* 272 */     if (paramArrayOfint[0] != 1)
/*     */     {
/* 274 */       return false;
/*     */     }
/* 276 */     for (byte b = 1; b < 4; b++) {
/*     */       
/* 278 */       if (paramArrayOfint[b] != 0)
/*     */       {
/* 280 */         return false;
/*     */       }
/*     */     } 
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne64(long[] paramArrayOflong) {
/* 288 */     if (paramArrayOflong[0] != 1L)
/*     */     {
/* 290 */       return false;
/*     */     }
/* 292 */     for (byte b = 1; b < 2; b++) {
/*     */       
/* 294 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/* 296 */         return false;
/*     */       }
/*     */     } 
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero(int[] paramArrayOfint) {
/* 304 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 306 */       if (paramArrayOfint[b] != 0)
/*     */       {
/* 308 */         return false;
/*     */       }
/*     */     } 
/* 311 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero64(long[] paramArrayOflong) {
/* 316 */     for (byte b = 0; b < 2; b++) {
/*     */       
/* 318 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/* 320 */         return false;
/*     */       }
/*     */     } 
/* 323 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 328 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/* 329 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 330 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/* 331 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 334 */     long l5 = 0L, l6 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/* 335 */     l5 += l6 * l1;
/* 336 */     paramArrayOfint3[0] = (int)l5;
/* 337 */     l5 >>>= 32L;
/* 338 */     l5 += l6 * l2;
/* 339 */     paramArrayOfint3[1] = (int)l5;
/* 340 */     l5 >>>= 32L;
/* 341 */     l5 += l6 * l3;
/* 342 */     paramArrayOfint3[2] = (int)l5;
/* 343 */     l5 >>>= 32L;
/* 344 */     l5 += l6 * l4;
/* 345 */     paramArrayOfint3[3] = (int)l5;
/* 346 */     l5 >>>= 32L;
/* 347 */     paramArrayOfint3[4] = (int)l5;
/*     */ 
/*     */     
/* 350 */     for (byte b = 1; b < 4; b++) {
/*     */       
/* 352 */       long l7 = 0L, l8 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/* 353 */       l7 += l8 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/* 354 */       paramArrayOfint3[b + 0] = (int)l7;
/* 355 */       l7 >>>= 32L;
/* 356 */       l7 += l8 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/* 357 */       paramArrayOfint3[b + 1] = (int)l7;
/* 358 */       l7 >>>= 32L;
/* 359 */       l7 += l8 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/* 360 */       paramArrayOfint3[b + 2] = (int)l7;
/* 361 */       l7 >>>= 32L;
/* 362 */       l7 += l8 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/* 363 */       paramArrayOfint3[b + 3] = (int)l7;
/* 364 */       l7 >>>= 32L;
/* 365 */       paramArrayOfint3[b + 4] = (int)l7;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void mul(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 371 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/* 372 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/* 373 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/* 374 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 377 */     long l5 = 0L, l6 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/* 378 */     l5 += l6 * l1;
/* 379 */     paramArrayOfint3[paramInt3 + 0] = (int)l5;
/* 380 */     l5 >>>= 32L;
/* 381 */     l5 += l6 * l2;
/* 382 */     paramArrayOfint3[paramInt3 + 1] = (int)l5;
/* 383 */     l5 >>>= 32L;
/* 384 */     l5 += l6 * l3;
/* 385 */     paramArrayOfint3[paramInt3 + 2] = (int)l5;
/* 386 */     l5 >>>= 32L;
/* 387 */     l5 += l6 * l4;
/* 388 */     paramArrayOfint3[paramInt3 + 3] = (int)l5;
/* 389 */     l5 >>>= 32L;
/* 390 */     paramArrayOfint3[paramInt3 + 4] = (int)l5;
/*     */ 
/*     */     
/* 393 */     for (byte b = 1; b < 4; b++) {
/*     */       
/* 395 */       paramInt3++;
/* 396 */       long l7 = 0L, l8 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/* 397 */       l7 += l8 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/* 398 */       paramArrayOfint3[paramInt3 + 0] = (int)l7;
/* 399 */       l7 >>>= 32L;
/* 400 */       l7 += l8 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/* 401 */       paramArrayOfint3[paramInt3 + 1] = (int)l7;
/* 402 */       l7 >>>= 32L;
/* 403 */       l7 += l8 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/* 404 */       paramArrayOfint3[paramInt3 + 2] = (int)l7;
/* 405 */       l7 >>>= 32L;
/* 406 */       l7 += l8 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/* 407 */       paramArrayOfint3[paramInt3 + 3] = (int)l7;
/* 408 */       l7 >>>= 32L;
/* 409 */       paramArrayOfint3[paramInt3 + 4] = (int)l7;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulAddTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 415 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/* 416 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 417 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/* 418 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*     */     
/* 420 */     long l5 = 0L;
/* 421 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 423 */       long l6 = 0L, l7 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/* 424 */       l6 += l7 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/* 425 */       paramArrayOfint3[b + 0] = (int)l6;
/* 426 */       l6 >>>= 32L;
/* 427 */       l6 += l7 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/* 428 */       paramArrayOfint3[b + 1] = (int)l6;
/* 429 */       l6 >>>= 32L;
/* 430 */       l6 += l7 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/* 431 */       paramArrayOfint3[b + 2] = (int)l6;
/* 432 */       l6 >>>= 32L;
/* 433 */       l6 += l7 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/* 434 */       paramArrayOfint3[b + 3] = (int)l6;
/* 435 */       l6 >>>= 32L;
/*     */       
/* 437 */       l5 += l6 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/* 438 */       paramArrayOfint3[b + 4] = (int)l5;
/* 439 */       l5 >>>= 32L;
/*     */     } 
/* 441 */     return (int)l5;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulAddTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 446 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/* 447 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/* 448 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/* 449 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*     */     
/* 451 */     long l5 = 0L;
/* 452 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 454 */       long l6 = 0L, l7 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/* 455 */       l6 += l7 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/* 456 */       paramArrayOfint3[paramInt3 + 0] = (int)l6;
/* 457 */       l6 >>>= 32L;
/* 458 */       l6 += l7 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/* 459 */       paramArrayOfint3[paramInt3 + 1] = (int)l6;
/* 460 */       l6 >>>= 32L;
/* 461 */       l6 += l7 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/* 462 */       paramArrayOfint3[paramInt3 + 2] = (int)l6;
/* 463 */       l6 >>>= 32L;
/* 464 */       l6 += l7 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/* 465 */       paramArrayOfint3[paramInt3 + 3] = (int)l6;
/* 466 */       l6 >>>= 32L;
/*     */       
/* 468 */       l5 += l6 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/* 469 */       paramArrayOfint3[paramInt3 + 4] = (int)l5;
/* 470 */       l5 >>>= 32L;
/* 471 */       paramInt3++;
/*     */     } 
/* 473 */     return (int)l5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long mul33Add(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/* 480 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 481 */     long l3 = paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL;
/* 482 */     l1 += l2 * l3 + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/* 483 */     paramArrayOfint3[paramInt4 + 0] = (int)l1;
/* 484 */     l1 >>>= 32L;
/* 485 */     long l4 = paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL;
/* 486 */     l1 += l2 * l4 + l3 + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/* 487 */     paramArrayOfint3[paramInt4 + 1] = (int)l1;
/* 488 */     l1 >>>= 32L;
/* 489 */     long l5 = paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL;
/* 490 */     l1 += l2 * l5 + l4 + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/* 491 */     paramArrayOfint3[paramInt4 + 2] = (int)l1;
/* 492 */     l1 >>>= 32L;
/* 493 */     long l6 = paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL;
/* 494 */     l1 += l2 * l6 + l5 + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/* 495 */     paramArrayOfint3[paramInt4 + 3] = (int)l1;
/* 496 */     l1 >>>= 32L;
/* 497 */     l1 += l6;
/* 498 */     return l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordAddExt(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 505 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 506 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/* 507 */     paramArrayOfint2[paramInt3 + 0] = (int)l1;
/* 508 */     l1 >>>= 32L;
/* 509 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/* 510 */     paramArrayOfint2[paramInt3 + 1] = (int)l1;
/* 511 */     l1 >>>= 32L;
/* 512 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/* 513 */     paramArrayOfint2[paramInt3 + 2] = (int)l1;
/* 514 */     l1 >>>= 32L;
/* 515 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/* 516 */     paramArrayOfint2[paramInt3 + 3] = (int)l1;
/* 517 */     l1 >>>= 32L;
/* 518 */     return (int)l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mul33DWordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 526 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 527 */     long l3 = paramLong & 0xFFFFFFFFL;
/* 528 */     l1 += l2 * l3 + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/* 529 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/* 530 */     l1 >>>= 32L;
/* 531 */     long l4 = paramLong >>> 32L;
/* 532 */     l1 += l2 * l4 + l3 + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/* 533 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/* 534 */     l1 >>>= 32L;
/* 535 */     l1 += l4 + (paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL);
/* 536 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/* 537 */     l1 >>>= 32L;
/* 538 */     l1 += paramArrayOfint[paramInt2 + 3] & 0xFFFFFFFFL;
/* 539 */     paramArrayOfint[paramInt2 + 3] = (int)l1;
/* 540 */     l1 >>>= 32L;
/* 541 */     return (int)l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mul33WordAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 549 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/* 550 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/* 551 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/* 552 */     l1 >>>= 32L;
/* 553 */     l1 += l3 + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/* 554 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/* 555 */     l1 >>>= 32L;
/* 556 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/* 557 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/* 558 */     l1 >>>= 32L;
/* 559 */     return (l1 == 0L) ? 0 : Nat.incAt(4, paramArrayOfint, paramInt3, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordDwordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 565 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 566 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/* 567 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/* 568 */     l1 >>>= 32L;
/* 569 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/* 570 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/* 571 */     l1 >>>= 32L;
/* 572 */     l1 += paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL;
/* 573 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/* 574 */     l1 >>>= 32L;
/* 575 */     return (l1 == 0L) ? 0 : Nat.incAt(4, paramArrayOfint, paramInt2, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordsAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 582 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/* 583 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/* 584 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/* 585 */     l1 >>>= 32L;
/* 586 */     l1 += paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL;
/* 587 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/* 588 */     l1 >>>= 32L;
/* 589 */     return (l1 == 0L) ? 0 : Nat.incAt(4, paramArrayOfint, paramInt3, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulWord(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2) {
/* 594 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 595 */     byte b = 0;
/*     */     
/*     */     while (true) {
/* 598 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/* 599 */       paramArrayOfint2[paramInt2 + b] = (int)l1;
/* 600 */       l1 >>>= 32L;
/*     */       
/* 602 */       if (++b >= 4)
/* 603 */         return (int)l1; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 608 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 611 */     int i = 0;
/*     */     
/* 613 */     byte b1 = 3, b2 = 8;
/*     */     
/*     */     do {
/* 616 */       long l11 = paramArrayOfint1[b1--] & 0xFFFFFFFFL;
/* 617 */       long l12 = l11 * l11;
/* 618 */       paramArrayOfint2[--b2] = i << 31 | (int)(l12 >>> 33L);
/* 619 */       paramArrayOfint2[--b2] = (int)(l12 >>> 1L);
/* 620 */       i = (int)l12;
/*     */     }
/* 622 */     while (b1 > 0);
/*     */ 
/*     */     
/* 625 */     long l4 = l1 * l1;
/* 626 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/* 627 */     paramArrayOfint2[0] = (int)l4;
/* 628 */     i = (int)(l4 >>> 32L) & 0x1;
/*     */ 
/*     */ 
/*     */     
/* 632 */     long l3 = paramArrayOfint1[1] & 0xFFFFFFFFL;
/* 633 */     l4 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 636 */     l2 += l3 * l1;
/* 637 */     int j = (int)l2;
/* 638 */     paramArrayOfint2[1] = j << 1 | i;
/* 639 */     i = j >>> 31;
/* 640 */     l4 += l2 >>> 32L;
/*     */ 
/*     */     
/* 643 */     long l5 = paramArrayOfint1[2] & 0xFFFFFFFFL;
/* 644 */     long l6 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/* 645 */     long l7 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*     */     
/* 647 */     l4 += l5 * l1;
/* 648 */     j = (int)l4;
/* 649 */     paramArrayOfint2[2] = j << 1 | i;
/* 650 */     i = j >>> 31;
/* 651 */     l6 += (l4 >>> 32L) + l5 * l3;
/* 652 */     l7 += l6 >>> 32L;
/* 653 */     l6 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 656 */     long l8 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/* 657 */     long l9 = (paramArrayOfint2[5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/* 658 */     long l10 = (paramArrayOfint2[6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*     */     
/* 660 */     l6 += l8 * l1;
/* 661 */     j = (int)l6;
/* 662 */     paramArrayOfint2[3] = j << 1 | i;
/* 663 */     i = j >>> 31;
/* 664 */     l7 += (l6 >>> 32L) + l8 * l3;
/* 665 */     l9 += (l7 >>> 32L) + l8 * l5;
/* 666 */     l10 += l9 >>> 32L;
/* 667 */     l9 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 670 */     j = (int)l7;
/* 671 */     paramArrayOfint2[4] = j << 1 | i;
/* 672 */     i = j >>> 31;
/* 673 */     j = (int)l9;
/* 674 */     paramArrayOfint2[5] = j << 1 | i;
/* 675 */     i = j >>> 31;
/* 676 */     j = (int)l10;
/* 677 */     paramArrayOfint2[6] = j << 1 | i;
/* 678 */     i = j >>> 31;
/* 679 */     j = paramArrayOfint2[7] + (int)(l10 >>> 32L);
/* 680 */     paramArrayOfint2[7] = j << 1 | i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 685 */     long l1 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 688 */     int i = 0;
/*     */     
/* 690 */     byte b1 = 3, b2 = 8;
/*     */     
/*     */     do {
/* 693 */       long l11 = paramArrayOfint1[paramInt1 + b1--] & 0xFFFFFFFFL;
/* 694 */       long l12 = l11 * l11;
/* 695 */       paramArrayOfint2[paramInt2 + --b2] = i << 31 | (int)(l12 >>> 33L);
/* 696 */       paramArrayOfint2[paramInt2 + --b2] = (int)(l12 >>> 1L);
/* 697 */       i = (int)l12;
/*     */     }
/* 699 */     while (b1 > 0);
/*     */ 
/*     */     
/* 702 */     long l4 = l1 * l1;
/* 703 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/* 704 */     paramArrayOfint2[paramInt2 + 0] = (int)l4;
/* 705 */     i = (int)(l4 >>> 32L) & 0x1;
/*     */ 
/*     */ 
/*     */     
/* 709 */     long l3 = paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL;
/* 710 */     l4 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 713 */     l2 += l3 * l1;
/* 714 */     int j = (int)l2;
/* 715 */     paramArrayOfint2[paramInt2 + 1] = j << 1 | i;
/* 716 */     i = j >>> 31;
/* 717 */     l4 += l2 >>> 32L;
/*     */ 
/*     */     
/* 720 */     long l5 = paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL;
/* 721 */     long l6 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/* 722 */     long l7 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*     */     
/* 724 */     l4 += l5 * l1;
/* 725 */     j = (int)l4;
/* 726 */     paramArrayOfint2[paramInt2 + 2] = j << 1 | i;
/* 727 */     i = j >>> 31;
/* 728 */     l6 += (l4 >>> 32L) + l5 * l3;
/* 729 */     l7 += l6 >>> 32L;
/* 730 */     l6 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 733 */     long l8 = paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL;
/* 734 */     long l9 = (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/* 735 */     long l10 = (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*     */     
/* 737 */     l6 += l8 * l1;
/* 738 */     j = (int)l6;
/* 739 */     paramArrayOfint2[paramInt2 + 3] = j << 1 | i;
/* 740 */     i = j >>> 31;
/* 741 */     l7 += (l6 >>> 32L) + l8 * l3;
/* 742 */     l9 += (l7 >>> 32L) + l8 * l5;
/* 743 */     l10 += l9 >>> 32L;
/*     */ 
/*     */     
/* 746 */     j = (int)l7;
/* 747 */     paramArrayOfint2[paramInt2 + 4] = j << 1 | i;
/* 748 */     i = j >>> 31;
/* 749 */     j = (int)l9;
/* 750 */     paramArrayOfint2[paramInt2 + 5] = j << 1 | i;
/* 751 */     i = j >>> 31;
/* 752 */     j = (int)l10;
/* 753 */     paramArrayOfint2[paramInt2 + 6] = j << 1 | i;
/* 754 */     i = j >>> 31;
/* 755 */     j = paramArrayOfint2[paramInt2 + 7] + (int)(l10 >>> 32L);
/* 756 */     paramArrayOfint2[paramInt2 + 7] = j << 1 | i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int sub(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 761 */     long l = 0L;
/* 762 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 763 */     paramArrayOfint3[0] = (int)l;
/* 764 */     l >>= 32L;
/* 765 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 766 */     paramArrayOfint3[1] = (int)l;
/* 767 */     l >>= 32L;
/* 768 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 769 */     paramArrayOfint3[2] = (int)l;
/* 770 */     l >>= 32L;
/* 771 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 772 */     paramArrayOfint3[3] = (int)l;
/* 773 */     l >>= 32L;
/* 774 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int sub(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 779 */     long l = 0L;
/* 780 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/* 781 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/* 782 */     l >>= 32L;
/* 783 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/* 784 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/* 785 */     l >>= 32L;
/* 786 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/* 787 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/* 788 */     l >>= 32L;
/* 789 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/* 790 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/* 791 */     l >>= 32L;
/* 792 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subBothFrom(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 797 */     long l = 0L;
/* 798 */     l += (paramArrayOfint3[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 799 */     paramArrayOfint3[0] = (int)l;
/* 800 */     l >>= 32L;
/* 801 */     l += (paramArrayOfint3[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 802 */     paramArrayOfint3[1] = (int)l;
/* 803 */     l >>= 32L;
/* 804 */     l += (paramArrayOfint3[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 805 */     paramArrayOfint3[2] = (int)l;
/* 806 */     l >>= 32L;
/* 807 */     l += (paramArrayOfint3[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 808 */     paramArrayOfint3[3] = (int)l;
/* 809 */     l >>= 32L;
/* 810 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subFrom(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 815 */     long l = 0L;
/* 816 */     l += (paramArrayOfint2[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL);
/* 817 */     paramArrayOfint2[0] = (int)l;
/* 818 */     l >>= 32L;
/* 819 */     l += (paramArrayOfint2[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL);
/* 820 */     paramArrayOfint2[1] = (int)l;
/* 821 */     l >>= 32L;
/* 822 */     l += (paramArrayOfint2[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL);
/* 823 */     paramArrayOfint2[2] = (int)l;
/* 824 */     l >>= 32L;
/* 825 */     l += (paramArrayOfint2[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL);
/* 826 */     paramArrayOfint2[3] = (int)l;
/* 827 */     l >>= 32L;
/* 828 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subFrom(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 833 */     long l = 0L;
/* 834 */     l += (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL);
/* 835 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 836 */     l >>= 32L;
/* 837 */     l += (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL);
/* 838 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 839 */     l >>= 32L;
/* 840 */     l += (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL);
/* 841 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 842 */     l >>= 32L;
/* 843 */     l += (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL);
/* 844 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 845 */     l >>= 32L;
/* 846 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger(int[] paramArrayOfint) {
/* 851 */     byte[] arrayOfByte = new byte[16];
/* 852 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 854 */       int i = paramArrayOfint[b];
/* 855 */       if (i != 0)
/*     */       {
/* 857 */         Pack.intToBigEndian(i, arrayOfByte, 3 - b << 2);
/*     */       }
/*     */     } 
/* 860 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/* 865 */     byte[] arrayOfByte = new byte[16];
/* 866 */     for (byte b = 0; b < 2; b++) {
/*     */       
/* 868 */       long l = paramArrayOflong[b];
/* 869 */       if (l != 0L)
/*     */       {
/* 871 */         Pack.longToBigEndian(l, arrayOfByte, 1 - b << 3);
/*     */       }
/*     */     } 
/* 874 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void zero(int[] paramArrayOfint) {
/* 879 */     paramArrayOfint[0] = 0;
/* 880 */     paramArrayOfint[1] = 0;
/* 881 */     paramArrayOfint[2] = 0;
/* 882 */     paramArrayOfint[3] = 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat128.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */