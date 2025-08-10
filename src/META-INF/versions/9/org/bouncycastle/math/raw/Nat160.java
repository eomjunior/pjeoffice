/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ public abstract class Nat160
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
/*  26 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  27 */     paramArrayOfint3[4] = (int)l;
/*  28 */     l >>>= 32L;
/*  29 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addBothTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  34 */     long l = 0L;
/*  35 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint3[0] & 0xFFFFFFFFL);
/*  36 */     paramArrayOfint3[0] = (int)l;
/*  37 */     l >>>= 32L;
/*  38 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint3[1] & 0xFFFFFFFFL);
/*  39 */     paramArrayOfint3[1] = (int)l;
/*  40 */     l >>>= 32L;
/*  41 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint3[2] & 0xFFFFFFFFL);
/*  42 */     paramArrayOfint3[2] = (int)l;
/*  43 */     l >>>= 32L;
/*  44 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint3[3] & 0xFFFFFFFFL);
/*  45 */     paramArrayOfint3[3] = (int)l;
/*  46 */     l >>>= 32L;
/*  47 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint3[4] & 0xFFFFFFFFL);
/*  48 */     paramArrayOfint3[4] = (int)l;
/*  49 */     l >>>= 32L;
/*  50 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addTo(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  55 */     long l = 0L;
/*  56 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  57 */     paramArrayOfint2[0] = (int)l;
/*  58 */     l >>>= 32L;
/*  59 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  60 */     paramArrayOfint2[1] = (int)l;
/*  61 */     l >>>= 32L;
/*  62 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  63 */     paramArrayOfint2[2] = (int)l;
/*  64 */     l >>>= 32L;
/*  65 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  66 */     paramArrayOfint2[3] = (int)l;
/*  67 */     l >>>= 32L;
/*  68 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  69 */     paramArrayOfint2[4] = (int)l;
/*  70 */     l >>>= 32L;
/*  71 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int paramInt3) {
/*  76 */     long l = paramInt3 & 0xFFFFFFFFL;
/*  77 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  78 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  79 */     l >>>= 32L;
/*  80 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  81 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  82 */     l >>>= 32L;
/*  83 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  84 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  85 */     l >>>= 32L;
/*  86 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  87 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  88 */     l >>>= 32L;
/*  89 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  90 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  91 */     l >>>= 32L;
/*  92 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addToEachOther(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  97 */     long l = 0L;
/*  98 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  99 */     paramArrayOfint1[paramInt1 + 0] = (int)l;
/* 100 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 101 */     l >>>= 32L;
/* 102 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/* 103 */     paramArrayOfint1[paramInt1 + 1] = (int)l;
/* 104 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 105 */     l >>>= 32L;
/* 106 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/* 107 */     paramArrayOfint1[paramInt1 + 2] = (int)l;
/* 108 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 109 */     l >>>= 32L;
/* 110 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/* 111 */     paramArrayOfint1[paramInt1 + 3] = (int)l;
/* 112 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 113 */     l >>>= 32L;
/* 114 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/* 115 */     paramArrayOfint1[paramInt1 + 4] = (int)l;
/* 116 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/* 117 */     l >>>= 32L;
/* 118 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 123 */     paramArrayOfint2[0] = paramArrayOfint1[0];
/* 124 */     paramArrayOfint2[1] = paramArrayOfint1[1];
/* 125 */     paramArrayOfint2[2] = paramArrayOfint1[2];
/* 126 */     paramArrayOfint2[3] = paramArrayOfint1[3];
/* 127 */     paramArrayOfint2[4] = paramArrayOfint1[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 132 */     paramArrayOfint2[paramInt2 + 0] = paramArrayOfint1[paramInt1 + 0];
/* 133 */     paramArrayOfint2[paramInt2 + 1] = paramArrayOfint1[paramInt1 + 1];
/* 134 */     paramArrayOfint2[paramInt2 + 2] = paramArrayOfint1[paramInt1 + 2];
/* 135 */     paramArrayOfint2[paramInt2 + 3] = paramArrayOfint1[paramInt1 + 3];
/* 136 */     paramArrayOfint2[paramInt2 + 4] = paramArrayOfint1[paramInt1 + 4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] create() {
/* 141 */     return new int[5];
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] createExt() {
/* 146 */     return new int[10];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean diff(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 151 */     boolean bool = gte(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2);
/* 152 */     if (bool) {
/*     */       
/* 154 */       sub(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2, paramArrayOfint3, paramInt3);
/*     */     }
/*     */     else {
/*     */       
/* 158 */       sub(paramArrayOfint2, paramInt2, paramArrayOfint1, paramInt1, paramArrayOfint3, paramInt3);
/*     */     } 
/* 160 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 165 */     for (byte b = 4; b >= 0; b--) {
/*     */       
/* 167 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*     */       {
/* 169 */         return false;
/*     */       }
/*     */     } 
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/* 177 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 160)
/*     */     {
/* 179 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 182 */     int[] arrayOfInt = create();
/*     */ 
/*     */     
/* 185 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 187 */       arrayOfInt[b] = paramBigInteger.intValue();
/* 188 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*     */     } 
/* 190 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/* 195 */     if (paramInt == 0)
/*     */     {
/* 197 */       return paramArrayOfint[0] & 0x1;
/*     */     }
/* 199 */     int i = paramInt >> 5;
/* 200 */     if (i < 0 || i >= 5)
/*     */     {
/* 202 */       return 0;
/*     */     }
/* 204 */     int j = paramInt & 0x1F;
/* 205 */     return paramArrayOfint[i] >>> j & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean gte(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 210 */     for (byte b = 4; b >= 0; b--) {
/*     */       
/* 212 */       int i = paramArrayOfint1[b] ^ Integer.MIN_VALUE;
/* 213 */       int j = paramArrayOfint2[b] ^ Integer.MIN_VALUE;
/* 214 */       if (i < j)
/* 215 */         return false; 
/* 216 */       if (i > j)
/* 217 */         return true; 
/*     */     } 
/* 219 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean gte(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 224 */     for (byte b = 4; b >= 0; b--) {
/*     */       
/* 226 */       int i = paramArrayOfint1[paramInt1 + b] ^ Integer.MIN_VALUE;
/* 227 */       int j = paramArrayOfint2[paramInt2 + b] ^ Integer.MIN_VALUE;
/* 228 */       if (i < j)
/* 229 */         return false; 
/* 230 */       if (i > j)
/* 231 */         return true; 
/*     */     } 
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne(int[] paramArrayOfint) {
/* 238 */     if (paramArrayOfint[0] != 1)
/*     */     {
/* 240 */       return false;
/*     */     }
/* 242 */     for (byte b = 1; b < 5; b++) {
/*     */       
/* 244 */       if (paramArrayOfint[b] != 0)
/*     */       {
/* 246 */         return false;
/*     */       }
/*     */     } 
/* 249 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero(int[] paramArrayOfint) {
/* 254 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 256 */       if (paramArrayOfint[b] != 0)
/*     */       {
/* 258 */         return false;
/*     */       }
/*     */     } 
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 266 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/* 267 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 268 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/* 269 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/* 270 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 273 */     long l6 = 0L, l7 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/* 274 */     l6 += l7 * l1;
/* 275 */     paramArrayOfint3[0] = (int)l6;
/* 276 */     l6 >>>= 32L;
/* 277 */     l6 += l7 * l2;
/* 278 */     paramArrayOfint3[1] = (int)l6;
/* 279 */     l6 >>>= 32L;
/* 280 */     l6 += l7 * l3;
/* 281 */     paramArrayOfint3[2] = (int)l6;
/* 282 */     l6 >>>= 32L;
/* 283 */     l6 += l7 * l4;
/* 284 */     paramArrayOfint3[3] = (int)l6;
/* 285 */     l6 >>>= 32L;
/* 286 */     l6 += l7 * l5;
/* 287 */     paramArrayOfint3[4] = (int)l6;
/* 288 */     l6 >>>= 32L;
/* 289 */     paramArrayOfint3[5] = (int)l6;
/*     */ 
/*     */     
/* 292 */     for (byte b = 1; b < 5; b++) {
/*     */       
/* 294 */       long l8 = 0L, l9 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/* 295 */       l8 += l9 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/* 296 */       paramArrayOfint3[b + 0] = (int)l8;
/* 297 */       l8 >>>= 32L;
/* 298 */       l8 += l9 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/* 299 */       paramArrayOfint3[b + 1] = (int)l8;
/* 300 */       l8 >>>= 32L;
/* 301 */       l8 += l9 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/* 302 */       paramArrayOfint3[b + 2] = (int)l8;
/* 303 */       l8 >>>= 32L;
/* 304 */       l8 += l9 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/* 305 */       paramArrayOfint3[b + 3] = (int)l8;
/* 306 */       l8 >>>= 32L;
/* 307 */       l8 += l9 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/* 308 */       paramArrayOfint3[b + 4] = (int)l8;
/* 309 */       l8 >>>= 32L;
/* 310 */       paramArrayOfint3[b + 5] = (int)l8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void mul(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 316 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/* 317 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/* 318 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/* 319 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/* 320 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 323 */     long l6 = 0L, l7 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/* 324 */     l6 += l7 * l1;
/* 325 */     paramArrayOfint3[paramInt3 + 0] = (int)l6;
/* 326 */     l6 >>>= 32L;
/* 327 */     l6 += l7 * l2;
/* 328 */     paramArrayOfint3[paramInt3 + 1] = (int)l6;
/* 329 */     l6 >>>= 32L;
/* 330 */     l6 += l7 * l3;
/* 331 */     paramArrayOfint3[paramInt3 + 2] = (int)l6;
/* 332 */     l6 >>>= 32L;
/* 333 */     l6 += l7 * l4;
/* 334 */     paramArrayOfint3[paramInt3 + 3] = (int)l6;
/* 335 */     l6 >>>= 32L;
/* 336 */     l6 += l7 * l5;
/* 337 */     paramArrayOfint3[paramInt3 + 4] = (int)l6;
/* 338 */     l6 >>>= 32L;
/* 339 */     paramArrayOfint3[paramInt3 + 5] = (int)l6;
/*     */ 
/*     */     
/* 342 */     for (byte b = 1; b < 5; b++) {
/*     */       
/* 344 */       paramInt3++;
/* 345 */       long l8 = 0L, l9 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/* 346 */       l8 += l9 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/* 347 */       paramArrayOfint3[paramInt3 + 0] = (int)l8;
/* 348 */       l8 >>>= 32L;
/* 349 */       l8 += l9 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/* 350 */       paramArrayOfint3[paramInt3 + 1] = (int)l8;
/* 351 */       l8 >>>= 32L;
/* 352 */       l8 += l9 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/* 353 */       paramArrayOfint3[paramInt3 + 2] = (int)l8;
/* 354 */       l8 >>>= 32L;
/* 355 */       l8 += l9 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/* 356 */       paramArrayOfint3[paramInt3 + 3] = (int)l8;
/* 357 */       l8 >>>= 32L;
/* 358 */       l8 += l9 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/* 359 */       paramArrayOfint3[paramInt3 + 4] = (int)l8;
/* 360 */       l8 >>>= 32L;
/* 361 */       paramArrayOfint3[paramInt3 + 5] = (int)l8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulAddTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 367 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/* 368 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 369 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/* 370 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/* 371 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*     */     
/* 373 */     long l6 = 0L;
/* 374 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 376 */       long l7 = 0L, l8 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/* 377 */       l7 += l8 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/* 378 */       paramArrayOfint3[b + 0] = (int)l7;
/* 379 */       l7 >>>= 32L;
/* 380 */       l7 += l8 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/* 381 */       paramArrayOfint3[b + 1] = (int)l7;
/* 382 */       l7 >>>= 32L;
/* 383 */       l7 += l8 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/* 384 */       paramArrayOfint3[b + 2] = (int)l7;
/* 385 */       l7 >>>= 32L;
/* 386 */       l7 += l8 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/* 387 */       paramArrayOfint3[b + 3] = (int)l7;
/* 388 */       l7 >>>= 32L;
/* 389 */       l7 += l8 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/* 390 */       paramArrayOfint3[b + 4] = (int)l7;
/* 391 */       l7 >>>= 32L;
/*     */       
/* 393 */       l6 += l7 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/* 394 */       paramArrayOfint3[b + 5] = (int)l6;
/* 395 */       l6 >>>= 32L;
/*     */     } 
/* 397 */     return (int)l6;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulAddTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 402 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/* 403 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/* 404 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/* 405 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/* 406 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*     */     
/* 408 */     long l6 = 0L;
/* 409 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 411 */       long l7 = 0L, l8 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/* 412 */       l7 += l8 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/* 413 */       paramArrayOfint3[paramInt3 + 0] = (int)l7;
/* 414 */       l7 >>>= 32L;
/* 415 */       l7 += l8 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/* 416 */       paramArrayOfint3[paramInt3 + 1] = (int)l7;
/* 417 */       l7 >>>= 32L;
/* 418 */       l7 += l8 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/* 419 */       paramArrayOfint3[paramInt3 + 2] = (int)l7;
/* 420 */       l7 >>>= 32L;
/* 421 */       l7 += l8 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/* 422 */       paramArrayOfint3[paramInt3 + 3] = (int)l7;
/* 423 */       l7 >>>= 32L;
/* 424 */       l7 += l8 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/* 425 */       paramArrayOfint3[paramInt3 + 4] = (int)l7;
/* 426 */       l7 >>>= 32L;
/*     */       
/* 428 */       l6 += l7 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/* 429 */       paramArrayOfint3[paramInt3 + 5] = (int)l6;
/* 430 */       l6 >>>= 32L;
/* 431 */       paramInt3++;
/*     */     } 
/* 433 */     return (int)l6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long mul33Add(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/* 440 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 441 */     long l3 = paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL;
/* 442 */     l1 += l2 * l3 + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/* 443 */     paramArrayOfint3[paramInt4 + 0] = (int)l1;
/* 444 */     l1 >>>= 32L;
/* 445 */     long l4 = paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL;
/* 446 */     l1 += l2 * l4 + l3 + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/* 447 */     paramArrayOfint3[paramInt4 + 1] = (int)l1;
/* 448 */     l1 >>>= 32L;
/* 449 */     long l5 = paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL;
/* 450 */     l1 += l2 * l5 + l4 + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/* 451 */     paramArrayOfint3[paramInt4 + 2] = (int)l1;
/* 452 */     l1 >>>= 32L;
/* 453 */     long l6 = paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL;
/* 454 */     l1 += l2 * l6 + l5 + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/* 455 */     paramArrayOfint3[paramInt4 + 3] = (int)l1;
/* 456 */     l1 >>>= 32L;
/* 457 */     long l7 = paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL;
/* 458 */     l1 += l2 * l7 + l6 + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/* 459 */     paramArrayOfint3[paramInt4 + 4] = (int)l1;
/* 460 */     l1 >>>= 32L;
/* 461 */     l1 += l7;
/* 462 */     return l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordAddExt(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 469 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 470 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/* 471 */     paramArrayOfint2[paramInt3 + 0] = (int)l1;
/* 472 */     l1 >>>= 32L;
/* 473 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/* 474 */     paramArrayOfint2[paramInt3 + 1] = (int)l1;
/* 475 */     l1 >>>= 32L;
/* 476 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/* 477 */     paramArrayOfint2[paramInt3 + 2] = (int)l1;
/* 478 */     l1 >>>= 32L;
/* 479 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/* 480 */     paramArrayOfint2[paramInt3 + 3] = (int)l1;
/* 481 */     l1 >>>= 32L;
/* 482 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/* 483 */     paramArrayOfint2[paramInt3 + 4] = (int)l1;
/* 484 */     l1 >>>= 32L;
/* 485 */     return (int)l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mul33DWordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 493 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 494 */     long l3 = paramLong & 0xFFFFFFFFL;
/* 495 */     l1 += l2 * l3 + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/* 496 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/* 497 */     l1 >>>= 32L;
/* 498 */     long l4 = paramLong >>> 32L;
/* 499 */     l1 += l2 * l4 + l3 + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/* 500 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/* 501 */     l1 >>>= 32L;
/* 502 */     l1 += l4 + (paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL);
/* 503 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/* 504 */     l1 >>>= 32L;
/* 505 */     l1 += paramArrayOfint[paramInt2 + 3] & 0xFFFFFFFFL;
/* 506 */     paramArrayOfint[paramInt2 + 3] = (int)l1;
/* 507 */     l1 >>>= 32L;
/* 508 */     return (l1 == 0L) ? 0 : Nat.incAt(5, paramArrayOfint, paramInt2, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mul33WordAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 516 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/* 517 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/* 518 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/* 519 */     l1 >>>= 32L;
/* 520 */     l1 += l3 + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/* 521 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/* 522 */     l1 >>>= 32L;
/* 523 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/* 524 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/* 525 */     l1 >>>= 32L;
/* 526 */     return (l1 == 0L) ? 0 : Nat.incAt(5, paramArrayOfint, paramInt3, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordDwordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 532 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 533 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/* 534 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/* 535 */     l1 >>>= 32L;
/* 536 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/* 537 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/* 538 */     l1 >>>= 32L;
/* 539 */     l1 += paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL;
/* 540 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/* 541 */     l1 >>>= 32L;
/* 542 */     return (l1 == 0L) ? 0 : Nat.incAt(5, paramArrayOfint, paramInt2, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mulWordsAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 549 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/* 550 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/* 551 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/* 552 */     l1 >>>= 32L;
/* 553 */     l1 += paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL;
/* 554 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/* 555 */     l1 >>>= 32L;
/* 556 */     return (l1 == 0L) ? 0 : Nat.incAt(5, paramArrayOfint, paramInt3, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int mulWord(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2) {
/* 561 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/* 562 */     byte b = 0;
/*     */     
/*     */     while (true) {
/* 565 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/* 566 */       paramArrayOfint2[paramInt2 + b] = (int)l1;
/* 567 */       l1 >>>= 32L;
/*     */       
/* 569 */       if (++b >= 5)
/* 570 */         return (int)l1; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 575 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 578 */     int i = 0;
/*     */     
/* 580 */     byte b1 = 4, b2 = 10;
/*     */     
/*     */     do {
/* 583 */       long l14 = paramArrayOfint1[b1--] & 0xFFFFFFFFL;
/* 584 */       long l15 = l14 * l14;
/* 585 */       paramArrayOfint2[--b2] = i << 31 | (int)(l15 >>> 33L);
/* 586 */       paramArrayOfint2[--b2] = (int)(l15 >>> 1L);
/* 587 */       i = (int)l15;
/*     */     }
/* 589 */     while (b1 > 0);
/*     */ 
/*     */     
/* 592 */     long l4 = l1 * l1;
/* 593 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/* 594 */     paramArrayOfint2[0] = (int)l4;
/* 595 */     i = (int)(l4 >>> 32L) & 0x1;
/*     */ 
/*     */ 
/*     */     
/* 599 */     long l3 = paramArrayOfint1[1] & 0xFFFFFFFFL;
/* 600 */     l4 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 603 */     l2 += l3 * l1;
/* 604 */     int j = (int)l2;
/* 605 */     paramArrayOfint2[1] = j << 1 | i;
/* 606 */     i = j >>> 31;
/* 607 */     l4 += l2 >>> 32L;
/*     */ 
/*     */     
/* 610 */     long l5 = paramArrayOfint1[2] & 0xFFFFFFFFL;
/* 611 */     long l6 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/* 612 */     long l7 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*     */     
/* 614 */     l4 += l5 * l1;
/* 615 */     j = (int)l4;
/* 616 */     paramArrayOfint2[2] = j << 1 | i;
/* 617 */     i = j >>> 31;
/* 618 */     l6 += (l4 >>> 32L) + l5 * l3;
/* 619 */     l7 += l6 >>> 32L;
/* 620 */     l6 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 623 */     long l8 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/* 624 */     long l9 = (paramArrayOfint2[5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/* 625 */     long l10 = (paramArrayOfint2[6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*     */     
/* 627 */     l6 += l8 * l1;
/* 628 */     j = (int)l6;
/* 629 */     paramArrayOfint2[3] = j << 1 | i;
/* 630 */     i = j >>> 31;
/* 631 */     l7 += (l6 >>> 32L) + l8 * l3;
/* 632 */     l9 += (l7 >>> 32L) + l8 * l5;
/* 633 */     l7 &= 0xFFFFFFFFL;
/* 634 */     l10 += l9 >>> 32L;
/* 635 */     l9 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 638 */     long l11 = paramArrayOfint1[4] & 0xFFFFFFFFL;
/* 639 */     long l12 = (paramArrayOfint2[7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/* 640 */     long l13 = (paramArrayOfint2[8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*     */     
/* 642 */     l7 += l11 * l1;
/* 643 */     j = (int)l7;
/* 644 */     paramArrayOfint2[4] = j << 1 | i;
/* 645 */     i = j >>> 31;
/* 646 */     l9 += (l7 >>> 32L) + l11 * l3;
/* 647 */     l10 += (l9 >>> 32L) + l11 * l5;
/* 648 */     l12 += (l10 >>> 32L) + l11 * l8;
/* 649 */     l13 += l12 >>> 32L;
/*     */ 
/*     */     
/* 652 */     j = (int)l9;
/* 653 */     paramArrayOfint2[5] = j << 1 | i;
/* 654 */     i = j >>> 31;
/* 655 */     j = (int)l10;
/* 656 */     paramArrayOfint2[6] = j << 1 | i;
/* 657 */     i = j >>> 31;
/* 658 */     j = (int)l12;
/* 659 */     paramArrayOfint2[7] = j << 1 | i;
/* 660 */     i = j >>> 31;
/* 661 */     j = (int)l13;
/* 662 */     paramArrayOfint2[8] = j << 1 | i;
/* 663 */     i = j >>> 31;
/* 664 */     j = paramArrayOfint2[9] + (int)(l13 >>> 32L);
/* 665 */     paramArrayOfint2[9] = j << 1 | i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 670 */     long l1 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 673 */     int i = 0;
/*     */     
/* 675 */     byte b1 = 4, b2 = 10;
/*     */     
/*     */     do {
/* 678 */       long l14 = paramArrayOfint1[paramInt1 + b1--] & 0xFFFFFFFFL;
/* 679 */       long l15 = l14 * l14;
/* 680 */       paramArrayOfint2[paramInt2 + --b2] = i << 31 | (int)(l15 >>> 33L);
/* 681 */       paramArrayOfint2[paramInt2 + --b2] = (int)(l15 >>> 1L);
/* 682 */       i = (int)l15;
/*     */     }
/* 684 */     while (b1 > 0);
/*     */ 
/*     */     
/* 687 */     long l4 = l1 * l1;
/* 688 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/* 689 */     paramArrayOfint2[paramInt2 + 0] = (int)l4;
/* 690 */     i = (int)(l4 >>> 32L) & 0x1;
/*     */ 
/*     */ 
/*     */     
/* 694 */     long l3 = paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL;
/* 695 */     l4 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 698 */     l2 += l3 * l1;
/* 699 */     int j = (int)l2;
/* 700 */     paramArrayOfint2[paramInt2 + 1] = j << 1 | i;
/* 701 */     i = j >>> 31;
/* 702 */     l4 += l2 >>> 32L;
/*     */ 
/*     */     
/* 705 */     long l5 = paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL;
/* 706 */     long l6 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/* 707 */     long l7 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*     */     
/* 709 */     l4 += l5 * l1;
/* 710 */     j = (int)l4;
/* 711 */     paramArrayOfint2[paramInt2 + 2] = j << 1 | i;
/* 712 */     i = j >>> 31;
/* 713 */     l6 += (l4 >>> 32L) + l5 * l3;
/* 714 */     l7 += l6 >>> 32L;
/* 715 */     l6 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 718 */     long l8 = paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL;
/* 719 */     long l9 = (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/* 720 */     long l10 = (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*     */     
/* 722 */     l6 += l8 * l1;
/* 723 */     j = (int)l6;
/* 724 */     paramArrayOfint2[paramInt2 + 3] = j << 1 | i;
/* 725 */     i = j >>> 31;
/* 726 */     l7 += (l6 >>> 32L) + l8 * l3;
/* 727 */     l9 += (l7 >>> 32L) + l8 * l5;
/* 728 */     l7 &= 0xFFFFFFFFL;
/* 729 */     l10 += l9 >>> 32L;
/* 730 */     l9 &= 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 733 */     long l11 = paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL;
/* 734 */     long l12 = (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/* 735 */     long l13 = (paramArrayOfint2[paramInt2 + 8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*     */     
/* 737 */     l7 += l11 * l1;
/* 738 */     j = (int)l7;
/* 739 */     paramArrayOfint2[paramInt2 + 4] = j << 1 | i;
/* 740 */     i = j >>> 31;
/* 741 */     l9 += (l7 >>> 32L) + l11 * l3;
/* 742 */     l10 += (l9 >>> 32L) + l11 * l5;
/* 743 */     l12 += (l10 >>> 32L) + l11 * l8;
/* 744 */     l13 += l12 >>> 32L;
/*     */ 
/*     */     
/* 747 */     j = (int)l9;
/* 748 */     paramArrayOfint2[paramInt2 + 5] = j << 1 | i;
/* 749 */     i = j >>> 31;
/* 750 */     j = (int)l10;
/* 751 */     paramArrayOfint2[paramInt2 + 6] = j << 1 | i;
/* 752 */     i = j >>> 31;
/* 753 */     j = (int)l12;
/* 754 */     paramArrayOfint2[paramInt2 + 7] = j << 1 | i;
/* 755 */     i = j >>> 31;
/* 756 */     j = (int)l13;
/* 757 */     paramArrayOfint2[paramInt2 + 8] = j << 1 | i;
/* 758 */     i = j >>> 31;
/* 759 */     j = paramArrayOfint2[paramInt2 + 9] + (int)(l13 >>> 32L);
/* 760 */     paramArrayOfint2[paramInt2 + 9] = j << 1 | i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int sub(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 765 */     long l = 0L;
/* 766 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 767 */     paramArrayOfint3[0] = (int)l;
/* 768 */     l >>= 32L;
/* 769 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 770 */     paramArrayOfint3[1] = (int)l;
/* 771 */     l >>= 32L;
/* 772 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 773 */     paramArrayOfint3[2] = (int)l;
/* 774 */     l >>= 32L;
/* 775 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 776 */     paramArrayOfint3[3] = (int)l;
/* 777 */     l >>= 32L;
/* 778 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 779 */     paramArrayOfint3[4] = (int)l;
/* 780 */     l >>= 32L;
/* 781 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int sub(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 786 */     long l = 0L;
/* 787 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/* 788 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/* 789 */     l >>= 32L;
/* 790 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/* 791 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/* 792 */     l >>= 32L;
/* 793 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/* 794 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/* 795 */     l >>= 32L;
/* 796 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/* 797 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/* 798 */     l >>= 32L;
/* 799 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/* 800 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/* 801 */     l >>= 32L;
/* 802 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subBothFrom(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 807 */     long l = 0L;
/* 808 */     l += (paramArrayOfint3[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 809 */     paramArrayOfint3[0] = (int)l;
/* 810 */     l >>= 32L;
/* 811 */     l += (paramArrayOfint3[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 812 */     paramArrayOfint3[1] = (int)l;
/* 813 */     l >>= 32L;
/* 814 */     l += (paramArrayOfint3[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 815 */     paramArrayOfint3[2] = (int)l;
/* 816 */     l >>= 32L;
/* 817 */     l += (paramArrayOfint3[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 818 */     paramArrayOfint3[3] = (int)l;
/* 819 */     l >>= 32L;
/* 820 */     l += (paramArrayOfint3[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 821 */     paramArrayOfint3[4] = (int)l;
/* 822 */     l >>= 32L;
/* 823 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subFrom(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 828 */     long l = 0L;
/* 829 */     l += (paramArrayOfint2[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL);
/* 830 */     paramArrayOfint2[0] = (int)l;
/* 831 */     l >>= 32L;
/* 832 */     l += (paramArrayOfint2[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL);
/* 833 */     paramArrayOfint2[1] = (int)l;
/* 834 */     l >>= 32L;
/* 835 */     l += (paramArrayOfint2[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL);
/* 836 */     paramArrayOfint2[2] = (int)l;
/* 837 */     l >>= 32L;
/* 838 */     l += (paramArrayOfint2[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL);
/* 839 */     paramArrayOfint2[3] = (int)l;
/* 840 */     l >>= 32L;
/* 841 */     l += (paramArrayOfint2[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL);
/* 842 */     paramArrayOfint2[4] = (int)l;
/* 843 */     l >>= 32L;
/* 844 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subFrom(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 849 */     long l = 0L;
/* 850 */     l += (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL);
/* 851 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 852 */     l >>= 32L;
/* 853 */     l += (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL);
/* 854 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 855 */     l >>= 32L;
/* 856 */     l += (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL);
/* 857 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 858 */     l >>= 32L;
/* 859 */     l += (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL);
/* 860 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 861 */     l >>= 32L;
/* 862 */     l += (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL);
/* 863 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/* 864 */     l >>= 32L;
/* 865 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger(int[] paramArrayOfint) {
/* 870 */     byte[] arrayOfByte = new byte[20];
/* 871 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 873 */       int i = paramArrayOfint[b];
/* 874 */       if (i != 0)
/*     */       {
/* 876 */         Pack.intToBigEndian(i, arrayOfByte, 4 - b << 2);
/*     */       }
/*     */     } 
/* 879 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void zero(int[] paramArrayOfint) {
/* 884 */     paramArrayOfint[0] = 0;
/* 885 */     paramArrayOfint[1] = 0;
/* 886 */     paramArrayOfint[2] = 0;
/* 887 */     paramArrayOfint[3] = 0;
/* 888 */     paramArrayOfint[4] = 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat160.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */