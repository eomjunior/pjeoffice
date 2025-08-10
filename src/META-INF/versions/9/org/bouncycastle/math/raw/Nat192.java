/*      */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import org.bouncycastle.math.raw.Nat;
/*      */ import org.bouncycastle.util.Pack;
/*      */ 
/*      */ 
/*      */ public abstract class Nat192
/*      */ {
/*      */   private static final long M = 4294967295L;
/*      */   
/*      */   public static int add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   13 */     long l = 0L;
/*   14 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*   15 */     paramArrayOfint3[0] = (int)l;
/*   16 */     l >>>= 32L;
/*   17 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*   18 */     paramArrayOfint3[1] = (int)l;
/*   19 */     l >>>= 32L;
/*   20 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*   21 */     paramArrayOfint3[2] = (int)l;
/*   22 */     l >>>= 32L;
/*   23 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*   24 */     paramArrayOfint3[3] = (int)l;
/*   25 */     l >>>= 32L;
/*   26 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*   27 */     paramArrayOfint3[4] = (int)l;
/*   28 */     l >>>= 32L;
/*   29 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*   30 */     paramArrayOfint3[5] = (int)l;
/*   31 */     l >>>= 32L;
/*   32 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   37 */     long l = 0L;
/*   38 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint3[0] & 0xFFFFFFFFL);
/*   39 */     paramArrayOfint3[0] = (int)l;
/*   40 */     l >>>= 32L;
/*   41 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint3[1] & 0xFFFFFFFFL);
/*   42 */     paramArrayOfint3[1] = (int)l;
/*   43 */     l >>>= 32L;
/*   44 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint3[2] & 0xFFFFFFFFL);
/*   45 */     paramArrayOfint3[2] = (int)l;
/*   46 */     l >>>= 32L;
/*   47 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint3[3] & 0xFFFFFFFFL);
/*   48 */     paramArrayOfint3[3] = (int)l;
/*   49 */     l >>>= 32L;
/*   50 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint3[4] & 0xFFFFFFFFL);
/*   51 */     paramArrayOfint3[4] = (int)l;
/*   52 */     l >>>= 32L;
/*   53 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL) + (paramArrayOfint3[5] & 0xFFFFFFFFL);
/*   54 */     paramArrayOfint3[5] = (int)l;
/*   55 */     l >>>= 32L;
/*   56 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*   61 */     long l = 0L;
/*   62 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*   63 */     paramArrayOfint2[0] = (int)l;
/*   64 */     l >>>= 32L;
/*   65 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*   66 */     paramArrayOfint2[1] = (int)l;
/*   67 */     l >>>= 32L;
/*   68 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*   69 */     paramArrayOfint2[2] = (int)l;
/*   70 */     l >>>= 32L;
/*   71 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*   72 */     paramArrayOfint2[3] = (int)l;
/*   73 */     l >>>= 32L;
/*   74 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*   75 */     paramArrayOfint2[4] = (int)l;
/*   76 */     l >>>= 32L;
/*   77 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*   78 */     paramArrayOfint2[5] = (int)l;
/*   79 */     l >>>= 32L;
/*   80 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int paramInt3) {
/*   85 */     long l = paramInt3 & 0xFFFFFFFFL;
/*   86 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*   87 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*   88 */     l >>>= 32L;
/*   89 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*   90 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*   91 */     l >>>= 32L;
/*   92 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*   93 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*   94 */     l >>>= 32L;
/*   95 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*   96 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*   97 */     l >>>= 32L;
/*   98 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*   99 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  100 */     l >>>= 32L;
/*  101 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  102 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  103 */     l >>>= 32L;
/*  104 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addToEachOther(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  109 */     long l = 0L;
/*  110 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  111 */     paramArrayOfint1[paramInt1 + 0] = (int)l;
/*  112 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  113 */     l >>>= 32L;
/*  114 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  115 */     paramArrayOfint1[paramInt1 + 1] = (int)l;
/*  116 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  117 */     l >>>= 32L;
/*  118 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  119 */     paramArrayOfint1[paramInt1 + 2] = (int)l;
/*  120 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  121 */     l >>>= 32L;
/*  122 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  123 */     paramArrayOfint1[paramInt1 + 3] = (int)l;
/*  124 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  125 */     l >>>= 32L;
/*  126 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  127 */     paramArrayOfint1[paramInt1 + 4] = (int)l;
/*  128 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  129 */     l >>>= 32L;
/*  130 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  131 */     paramArrayOfint1[paramInt1 + 5] = (int)l;
/*  132 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  133 */     l >>>= 32L;
/*  134 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  139 */     paramArrayOfint2[0] = paramArrayOfint1[0];
/*  140 */     paramArrayOfint2[1] = paramArrayOfint1[1];
/*  141 */     paramArrayOfint2[2] = paramArrayOfint1[2];
/*  142 */     paramArrayOfint2[3] = paramArrayOfint1[3];
/*  143 */     paramArrayOfint2[4] = paramArrayOfint1[4];
/*  144 */     paramArrayOfint2[5] = paramArrayOfint1[5];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  149 */     paramArrayOfint2[paramInt2 + 0] = paramArrayOfint1[paramInt1 + 0];
/*  150 */     paramArrayOfint2[paramInt2 + 1] = paramArrayOfint1[paramInt1 + 1];
/*  151 */     paramArrayOfint2[paramInt2 + 2] = paramArrayOfint1[paramInt1 + 2];
/*  152 */     paramArrayOfint2[paramInt2 + 3] = paramArrayOfint1[paramInt1 + 3];
/*  153 */     paramArrayOfint2[paramInt2 + 4] = paramArrayOfint1[paramInt1 + 4];
/*  154 */     paramArrayOfint2[paramInt2 + 5] = paramArrayOfint1[paramInt1 + 5];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  159 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/*  160 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  161 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/*  166 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/*  167 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*  168 */     paramArrayOflong2[paramInt2 + 2] = paramArrayOflong1[paramInt1 + 2];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] create() {
/*  173 */     return new int[6];
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] create64() {
/*  178 */     return new long[3];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] createExt() {
/*  183 */     return new int[12];
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] createExt64() {
/*  188 */     return new long[6];
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean diff(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  193 */     boolean bool = gte(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2);
/*  194 */     if (bool) {
/*      */       
/*  196 */       sub(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2, paramArrayOfint3, paramInt3);
/*      */     }
/*      */     else {
/*      */       
/*  200 */       sub(paramArrayOfint2, paramInt2, paramArrayOfint1, paramInt1, paramArrayOfint3, paramInt3);
/*      */     } 
/*  202 */     return bool;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  207 */     for (byte b = 5; b >= 0; b--) {
/*      */       
/*  209 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*      */       {
/*  211 */         return false;
/*      */       }
/*      */     } 
/*  214 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  219 */     for (byte b = 2; b >= 0; b--) {
/*      */       
/*  221 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*      */       {
/*  223 */         return false;
/*      */       }
/*      */     } 
/*  226 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  231 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 192)
/*      */     {
/*  233 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  236 */     int[] arrayOfInt = create();
/*      */ 
/*      */     
/*  239 */     for (byte b = 0; b < 6; b++) {
/*      */       
/*  241 */       arrayOfInt[b] = paramBigInteger.intValue();
/*  242 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*      */     } 
/*  244 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/*  249 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 192)
/*      */     {
/*  251 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  254 */     long[] arrayOfLong = create64();
/*      */ 
/*      */     
/*  257 */     for (byte b = 0; b < 3; b++) {
/*      */       
/*  259 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  260 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*      */     } 
/*  262 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/*  267 */     if (paramInt == 0)
/*      */     {
/*  269 */       return paramArrayOfint[0] & 0x1;
/*      */     }
/*  271 */     int i = paramInt >> 5;
/*  272 */     if (i < 0 || i >= 6)
/*      */     {
/*  274 */       return 0;
/*      */     }
/*  276 */     int j = paramInt & 0x1F;
/*  277 */     return paramArrayOfint[i] >>> j & 0x1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  282 */     for (byte b = 5; b >= 0; b--) {
/*      */       
/*  284 */       int i = paramArrayOfint1[b] ^ Integer.MIN_VALUE;
/*  285 */       int j = paramArrayOfint2[b] ^ Integer.MIN_VALUE;
/*  286 */       if (i < j)
/*  287 */         return false; 
/*  288 */       if (i > j)
/*  289 */         return true; 
/*      */     } 
/*  291 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  296 */     for (byte b = 5; b >= 0; b--) {
/*      */       
/*  298 */       int i = paramArrayOfint1[paramInt1 + b] ^ Integer.MIN_VALUE;
/*  299 */       int j = paramArrayOfint2[paramInt2 + b] ^ Integer.MIN_VALUE;
/*  300 */       if (i < j)
/*  301 */         return false; 
/*  302 */       if (i > j)
/*  303 */         return true; 
/*      */     } 
/*  305 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne(int[] paramArrayOfint) {
/*  310 */     if (paramArrayOfint[0] != 1)
/*      */     {
/*  312 */       return false;
/*      */     }
/*  314 */     for (byte b = 1; b < 6; b++) {
/*      */       
/*  316 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  318 */         return false;
/*      */       }
/*      */     } 
/*  321 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne64(long[] paramArrayOflong) {
/*  326 */     if (paramArrayOflong[0] != 1L)
/*      */     {
/*  328 */       return false;
/*      */     }
/*  330 */     for (byte b = 1; b < 3; b++) {
/*      */       
/*  332 */       if (paramArrayOflong[b] != 0L)
/*      */       {
/*  334 */         return false;
/*      */       }
/*      */     } 
/*  337 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero(int[] paramArrayOfint) {
/*  342 */     for (byte b = 0; b < 6; b++) {
/*      */       
/*  344 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  346 */         return false;
/*      */       }
/*      */     } 
/*  349 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero64(long[] paramArrayOflong) {
/*  354 */     for (byte b = 0; b < 3; b++) {
/*      */       
/*  356 */       if (paramArrayOflong[b] != 0L)
/*      */       {
/*  358 */         return false;
/*      */       }
/*      */     } 
/*  361 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  366 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  367 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  368 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  369 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  370 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  371 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  374 */     long l7 = 0L, l8 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*  375 */     l7 += l8 * l1;
/*  376 */     paramArrayOfint3[0] = (int)l7;
/*  377 */     l7 >>>= 32L;
/*  378 */     l7 += l8 * l2;
/*  379 */     paramArrayOfint3[1] = (int)l7;
/*  380 */     l7 >>>= 32L;
/*  381 */     l7 += l8 * l3;
/*  382 */     paramArrayOfint3[2] = (int)l7;
/*  383 */     l7 >>>= 32L;
/*  384 */     l7 += l8 * l4;
/*  385 */     paramArrayOfint3[3] = (int)l7;
/*  386 */     l7 >>>= 32L;
/*  387 */     l7 += l8 * l5;
/*  388 */     paramArrayOfint3[4] = (int)l7;
/*  389 */     l7 >>>= 32L;
/*  390 */     l7 += l8 * l6;
/*  391 */     paramArrayOfint3[5] = (int)l7;
/*  392 */     l7 >>>= 32L;
/*  393 */     paramArrayOfint3[6] = (int)l7;
/*      */ 
/*      */     
/*  396 */     for (byte b = 1; b < 6; b++) {
/*      */       
/*  398 */       long l9 = 0L, l10 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  399 */       l9 += l10 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  400 */       paramArrayOfint3[b + 0] = (int)l9;
/*  401 */       l9 >>>= 32L;
/*  402 */       l9 += l10 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  403 */       paramArrayOfint3[b + 1] = (int)l9;
/*  404 */       l9 >>>= 32L;
/*  405 */       l9 += l10 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  406 */       paramArrayOfint3[b + 2] = (int)l9;
/*  407 */       l9 >>>= 32L;
/*  408 */       l9 += l10 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  409 */       paramArrayOfint3[b + 3] = (int)l9;
/*  410 */       l9 >>>= 32L;
/*  411 */       l9 += l10 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  412 */       paramArrayOfint3[b + 4] = (int)l9;
/*  413 */       l9 >>>= 32L;
/*  414 */       l9 += l10 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  415 */       paramArrayOfint3[b + 5] = (int)l9;
/*  416 */       l9 >>>= 32L;
/*  417 */       paramArrayOfint3[b + 6] = (int)l9;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  423 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  424 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  425 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  426 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  427 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  428 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  431 */     long l7 = 0L, l8 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*  432 */     l7 += l8 * l1;
/*  433 */     paramArrayOfint3[paramInt3 + 0] = (int)l7;
/*  434 */     l7 >>>= 32L;
/*  435 */     l7 += l8 * l2;
/*  436 */     paramArrayOfint3[paramInt3 + 1] = (int)l7;
/*  437 */     l7 >>>= 32L;
/*  438 */     l7 += l8 * l3;
/*  439 */     paramArrayOfint3[paramInt3 + 2] = (int)l7;
/*  440 */     l7 >>>= 32L;
/*  441 */     l7 += l8 * l4;
/*  442 */     paramArrayOfint3[paramInt3 + 3] = (int)l7;
/*  443 */     l7 >>>= 32L;
/*  444 */     l7 += l8 * l5;
/*  445 */     paramArrayOfint3[paramInt3 + 4] = (int)l7;
/*  446 */     l7 >>>= 32L;
/*  447 */     l7 += l8 * l6;
/*  448 */     paramArrayOfint3[paramInt3 + 5] = (int)l7;
/*  449 */     l7 >>>= 32L;
/*  450 */     paramArrayOfint3[paramInt3 + 6] = (int)l7;
/*      */ 
/*      */     
/*  453 */     for (byte b = 1; b < 6; b++) {
/*      */       
/*  455 */       paramInt3++;
/*  456 */       long l9 = 0L, l10 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  457 */       l9 += l10 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  458 */       paramArrayOfint3[paramInt3 + 0] = (int)l9;
/*  459 */       l9 >>>= 32L;
/*  460 */       l9 += l10 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  461 */       paramArrayOfint3[paramInt3 + 1] = (int)l9;
/*  462 */       l9 >>>= 32L;
/*  463 */       l9 += l10 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  464 */       paramArrayOfint3[paramInt3 + 2] = (int)l9;
/*  465 */       l9 >>>= 32L;
/*  466 */       l9 += l10 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  467 */       paramArrayOfint3[paramInt3 + 3] = (int)l9;
/*  468 */       l9 >>>= 32L;
/*  469 */       l9 += l10 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  470 */       paramArrayOfint3[paramInt3 + 4] = (int)l9;
/*  471 */       l9 >>>= 32L;
/*  472 */       l9 += l10 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  473 */       paramArrayOfint3[paramInt3 + 5] = (int)l9;
/*  474 */       l9 >>>= 32L;
/*  475 */       paramArrayOfint3[paramInt3 + 6] = (int)l9;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  481 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  482 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  483 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  484 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  485 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  486 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*      */     
/*  488 */     long l7 = 0L;
/*  489 */     for (byte b = 0; b < 6; b++) {
/*      */       
/*  491 */       long l8 = 0L, l9 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  492 */       l8 += l9 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  493 */       paramArrayOfint3[b + 0] = (int)l8;
/*  494 */       l8 >>>= 32L;
/*  495 */       l8 += l9 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  496 */       paramArrayOfint3[b + 1] = (int)l8;
/*  497 */       l8 >>>= 32L;
/*  498 */       l8 += l9 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  499 */       paramArrayOfint3[b + 2] = (int)l8;
/*  500 */       l8 >>>= 32L;
/*  501 */       l8 += l9 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  502 */       paramArrayOfint3[b + 3] = (int)l8;
/*  503 */       l8 >>>= 32L;
/*  504 */       l8 += l9 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  505 */       paramArrayOfint3[b + 4] = (int)l8;
/*  506 */       l8 >>>= 32L;
/*  507 */       l8 += l9 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  508 */       paramArrayOfint3[b + 5] = (int)l8;
/*  509 */       l8 >>>= 32L;
/*      */       
/*  511 */       l7 += l8 + (paramArrayOfint3[b + 6] & 0xFFFFFFFFL);
/*  512 */       paramArrayOfint3[b + 6] = (int)l7;
/*  513 */       l7 >>>= 32L;
/*      */     } 
/*  515 */     return (int)l7;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  520 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  521 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  522 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  523 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  524 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  525 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*      */     
/*  527 */     long l7 = 0L;
/*  528 */     for (byte b = 0; b < 6; b++) {
/*      */       
/*  530 */       long l8 = 0L, l9 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  531 */       l8 += l9 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  532 */       paramArrayOfint3[paramInt3 + 0] = (int)l8;
/*  533 */       l8 >>>= 32L;
/*  534 */       l8 += l9 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  535 */       paramArrayOfint3[paramInt3 + 1] = (int)l8;
/*  536 */       l8 >>>= 32L;
/*  537 */       l8 += l9 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  538 */       paramArrayOfint3[paramInt3 + 2] = (int)l8;
/*  539 */       l8 >>>= 32L;
/*  540 */       l8 += l9 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  541 */       paramArrayOfint3[paramInt3 + 3] = (int)l8;
/*  542 */       l8 >>>= 32L;
/*  543 */       l8 += l9 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  544 */       paramArrayOfint3[paramInt3 + 4] = (int)l8;
/*  545 */       l8 >>>= 32L;
/*  546 */       l8 += l9 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  547 */       paramArrayOfint3[paramInt3 + 5] = (int)l8;
/*  548 */       l8 >>>= 32L;
/*      */       
/*  550 */       l7 += l8 + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  551 */       paramArrayOfint3[paramInt3 + 6] = (int)l7;
/*  552 */       l7 >>>= 32L;
/*  553 */       paramInt3++;
/*      */     } 
/*  555 */     return (int)l7;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mul33Add(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  562 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  563 */     long l3 = paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL;
/*  564 */     l1 += l2 * l3 + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  565 */     paramArrayOfint3[paramInt4 + 0] = (int)l1;
/*  566 */     l1 >>>= 32L;
/*  567 */     long l4 = paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL;
/*  568 */     l1 += l2 * l4 + l3 + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  569 */     paramArrayOfint3[paramInt4 + 1] = (int)l1;
/*  570 */     l1 >>>= 32L;
/*  571 */     long l5 = paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL;
/*  572 */     l1 += l2 * l5 + l4 + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  573 */     paramArrayOfint3[paramInt4 + 2] = (int)l1;
/*  574 */     l1 >>>= 32L;
/*  575 */     long l6 = paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL;
/*  576 */     l1 += l2 * l6 + l5 + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  577 */     paramArrayOfint3[paramInt4 + 3] = (int)l1;
/*  578 */     l1 >>>= 32L;
/*  579 */     long l7 = paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL;
/*  580 */     l1 += l2 * l7 + l6 + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  581 */     paramArrayOfint3[paramInt4 + 4] = (int)l1;
/*  582 */     l1 >>>= 32L;
/*  583 */     long l8 = paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL;
/*  584 */     l1 += l2 * l8 + l7 + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  585 */     paramArrayOfint3[paramInt4 + 5] = (int)l1;
/*  586 */     l1 >>>= 32L;
/*  587 */     l1 += l8;
/*  588 */     return l1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mulWordAddExt(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  595 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  596 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  597 */     paramArrayOfint2[paramInt3 + 0] = (int)l1;
/*  598 */     l1 >>>= 32L;
/*  599 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  600 */     paramArrayOfint2[paramInt3 + 1] = (int)l1;
/*  601 */     l1 >>>= 32L;
/*  602 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  603 */     paramArrayOfint2[paramInt3 + 2] = (int)l1;
/*  604 */     l1 >>>= 32L;
/*  605 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  606 */     paramArrayOfint2[paramInt3 + 3] = (int)l1;
/*  607 */     l1 >>>= 32L;
/*  608 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  609 */     paramArrayOfint2[paramInt3 + 4] = (int)l1;
/*  610 */     l1 >>>= 32L;
/*  611 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  612 */     paramArrayOfint2[paramInt3 + 5] = (int)l1;
/*  613 */     l1 >>>= 32L;
/*  614 */     return (int)l1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33DWordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  622 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  623 */     long l3 = paramLong & 0xFFFFFFFFL;
/*  624 */     l1 += l2 * l3 + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  625 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  626 */     l1 >>>= 32L;
/*  627 */     long l4 = paramLong >>> 32L;
/*  628 */     l1 += l2 * l4 + l3 + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  629 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  630 */     l1 >>>= 32L;
/*  631 */     l1 += l4 + (paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL);
/*  632 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  633 */     l1 >>>= 32L;
/*  634 */     l1 += paramArrayOfint[paramInt2 + 3] & 0xFFFFFFFFL;
/*  635 */     paramArrayOfint[paramInt2 + 3] = (int)l1;
/*  636 */     l1 >>>= 32L;
/*  637 */     return (l1 == 0L) ? 0 : Nat.incAt(6, paramArrayOfint, paramInt2, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33WordAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*  645 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/*  646 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/*  647 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/*  648 */     l1 >>>= 32L;
/*  649 */     l1 += l3 + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/*  650 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/*  651 */     l1 >>>= 32L;
/*  652 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/*  653 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/*  654 */     l1 >>>= 32L;
/*  655 */     return (l1 == 0L) ? 0 : Nat.incAt(6, paramArrayOfint, paramInt3, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mulWordDwordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  661 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  662 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  663 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  664 */     l1 >>>= 32L;
/*  665 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  666 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  667 */     l1 >>>= 32L;
/*  668 */     l1 += paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL;
/*  669 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  670 */     l1 >>>= 32L;
/*  671 */     return (l1 == 0L) ? 0 : Nat.incAt(6, paramArrayOfint, paramInt2, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulWord(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2) {
/*  676 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  677 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  680 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/*  681 */       paramArrayOfint2[paramInt2 + b] = (int)l1;
/*  682 */       l1 >>>= 32L;
/*      */       
/*  684 */       if (++b >= 6)
/*  685 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  690 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  693 */     int i = 0;
/*      */     
/*  695 */     byte b1 = 5, b2 = 12;
/*      */     
/*      */     do {
/*  698 */       long l17 = paramArrayOfint1[b1--] & 0xFFFFFFFFL;
/*  699 */       long l18 = l17 * l17;
/*  700 */       paramArrayOfint2[--b2] = i << 31 | (int)(l18 >>> 33L);
/*  701 */       paramArrayOfint2[--b2] = (int)(l18 >>> 1L);
/*  702 */       i = (int)l18;
/*      */     }
/*  704 */     while (b1 > 0);
/*      */ 
/*      */     
/*  707 */     long l4 = l1 * l1;
/*  708 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/*  709 */     paramArrayOfint2[0] = (int)l4;
/*  710 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/*  714 */     long l3 = paramArrayOfint1[1] & 0xFFFFFFFFL;
/*  715 */     l4 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  718 */     l2 += l3 * l1;
/*  719 */     int j = (int)l2;
/*  720 */     paramArrayOfint2[1] = j << 1 | i;
/*  721 */     i = j >>> 31;
/*  722 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/*  725 */     long l5 = paramArrayOfint1[2] & 0xFFFFFFFFL;
/*  726 */     long l6 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  727 */     long l7 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*      */     
/*  729 */     l4 += l5 * l1;
/*  730 */     j = (int)l4;
/*  731 */     paramArrayOfint2[2] = j << 1 | i;
/*  732 */     i = j >>> 31;
/*  733 */     l6 += (l4 >>> 32L) + l5 * l3;
/*  734 */     l7 += l6 >>> 32L;
/*  735 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  738 */     long l8 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/*  739 */     long l9 = (paramArrayOfint2[5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/*  740 */     long l10 = (paramArrayOfint2[6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/*  742 */     l6 += l8 * l1;
/*  743 */     j = (int)l6;
/*  744 */     paramArrayOfint2[3] = j << 1 | i;
/*  745 */     i = j >>> 31;
/*  746 */     l7 += (l6 >>> 32L) + l8 * l3;
/*  747 */     l9 += (l7 >>> 32L) + l8 * l5;
/*  748 */     l7 &= 0xFFFFFFFFL;
/*  749 */     l10 += l9 >>> 32L;
/*  750 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  753 */     long l11 = paramArrayOfint1[4] & 0xFFFFFFFFL;
/*  754 */     long l12 = (paramArrayOfint2[7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/*  755 */     long l13 = (paramArrayOfint2[8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/*  757 */     l7 += l11 * l1;
/*  758 */     j = (int)l7;
/*  759 */     paramArrayOfint2[4] = j << 1 | i;
/*  760 */     i = j >>> 31;
/*  761 */     l9 += (l7 >>> 32L) + l11 * l3;
/*  762 */     l10 += (l9 >>> 32L) + l11 * l5;
/*  763 */     l9 &= 0xFFFFFFFFL;
/*  764 */     l12 += (l10 >>> 32L) + l11 * l8;
/*  765 */     l10 &= 0xFFFFFFFFL;
/*  766 */     l13 += l12 >>> 32L;
/*  767 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  770 */     long l14 = paramArrayOfint1[5] & 0xFFFFFFFFL;
/*  771 */     long l15 = (paramArrayOfint2[9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/*  772 */     long l16 = (paramArrayOfint2[10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/*  774 */     l9 += l14 * l1;
/*  775 */     j = (int)l9;
/*  776 */     paramArrayOfint2[5] = j << 1 | i;
/*  777 */     i = j >>> 31;
/*  778 */     l10 += (l9 >>> 32L) + l14 * l3;
/*  779 */     l12 += (l10 >>> 32L) + l14 * l5;
/*  780 */     l13 += (l12 >>> 32L) + l14 * l8;
/*  781 */     l15 += (l13 >>> 32L) + l14 * l11;
/*  782 */     l16 += l15 >>> 32L;
/*      */ 
/*      */     
/*  785 */     j = (int)l10;
/*  786 */     paramArrayOfint2[6] = j << 1 | i;
/*  787 */     i = j >>> 31;
/*  788 */     j = (int)l12;
/*  789 */     paramArrayOfint2[7] = j << 1 | i;
/*  790 */     i = j >>> 31;
/*  791 */     j = (int)l13;
/*  792 */     paramArrayOfint2[8] = j << 1 | i;
/*  793 */     i = j >>> 31;
/*  794 */     j = (int)l15;
/*  795 */     paramArrayOfint2[9] = j << 1 | i;
/*  796 */     i = j >>> 31;
/*  797 */     j = (int)l16;
/*  798 */     paramArrayOfint2[10] = j << 1 | i;
/*  799 */     i = j >>> 31;
/*  800 */     j = paramArrayOfint2[11] + (int)(l16 >>> 32L);
/*  801 */     paramArrayOfint2[11] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  806 */     long l1 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  809 */     int i = 0;
/*      */     
/*  811 */     byte b1 = 5, b2 = 12;
/*      */     
/*      */     do {
/*  814 */       long l17 = paramArrayOfint1[paramInt1 + b1--] & 0xFFFFFFFFL;
/*  815 */       long l18 = l17 * l17;
/*  816 */       paramArrayOfint2[paramInt2 + --b2] = i << 31 | (int)(l18 >>> 33L);
/*  817 */       paramArrayOfint2[paramInt2 + --b2] = (int)(l18 >>> 1L);
/*  818 */       i = (int)l18;
/*      */     }
/*  820 */     while (b1 > 0);
/*      */ 
/*      */     
/*  823 */     long l4 = l1 * l1;
/*  824 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/*  825 */     paramArrayOfint2[paramInt2 + 0] = (int)l4;
/*  826 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/*  830 */     long l3 = paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL;
/*  831 */     l4 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  834 */     l2 += l3 * l1;
/*  835 */     int j = (int)l2;
/*  836 */     paramArrayOfint2[paramInt2 + 1] = j << 1 | i;
/*  837 */     i = j >>> 31;
/*  838 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/*  841 */     long l5 = paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL;
/*  842 */     long l6 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  843 */     long l7 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*      */     
/*  845 */     l4 += l5 * l1;
/*  846 */     j = (int)l4;
/*  847 */     paramArrayOfint2[paramInt2 + 2] = j << 1 | i;
/*  848 */     i = j >>> 31;
/*  849 */     l6 += (l4 >>> 32L) + l5 * l3;
/*  850 */     l7 += l6 >>> 32L;
/*  851 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  854 */     long l8 = paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL;
/*  855 */     long l9 = (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/*  856 */     long l10 = (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/*  858 */     l6 += l8 * l1;
/*  859 */     j = (int)l6;
/*  860 */     paramArrayOfint2[paramInt2 + 3] = j << 1 | i;
/*  861 */     i = j >>> 31;
/*  862 */     l7 += (l6 >>> 32L) + l8 * l3;
/*  863 */     l9 += (l7 >>> 32L) + l8 * l5;
/*  864 */     l7 &= 0xFFFFFFFFL;
/*  865 */     l10 += l9 >>> 32L;
/*  866 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  869 */     long l11 = paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL;
/*  870 */     long l12 = (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/*  871 */     long l13 = (paramArrayOfint2[paramInt2 + 8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/*  873 */     l7 += l11 * l1;
/*  874 */     j = (int)l7;
/*  875 */     paramArrayOfint2[paramInt2 + 4] = j << 1 | i;
/*  876 */     i = j >>> 31;
/*  877 */     l9 += (l7 >>> 32L) + l11 * l3;
/*  878 */     l10 += (l9 >>> 32L) + l11 * l5;
/*  879 */     l9 &= 0xFFFFFFFFL;
/*  880 */     l12 += (l10 >>> 32L) + l11 * l8;
/*  881 */     l10 &= 0xFFFFFFFFL;
/*  882 */     l13 += l12 >>> 32L;
/*  883 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  886 */     long l14 = paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL;
/*  887 */     long l15 = (paramArrayOfint2[paramInt2 + 9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/*  888 */     long l16 = (paramArrayOfint2[paramInt2 + 10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/*  890 */     l9 += l14 * l1;
/*  891 */     j = (int)l9;
/*  892 */     paramArrayOfint2[paramInt2 + 5] = j << 1 | i;
/*  893 */     i = j >>> 31;
/*  894 */     l10 += (l9 >>> 32L) + l14 * l3;
/*  895 */     l12 += (l10 >>> 32L) + l14 * l5;
/*  896 */     l13 += (l12 >>> 32L) + l14 * l8;
/*  897 */     l15 += (l13 >>> 32L) + l14 * l11;
/*  898 */     l16 += l15 >>> 32L;
/*      */ 
/*      */     
/*  901 */     j = (int)l10;
/*  902 */     paramArrayOfint2[paramInt2 + 6] = j << 1 | i;
/*  903 */     i = j >>> 31;
/*  904 */     j = (int)l12;
/*  905 */     paramArrayOfint2[paramInt2 + 7] = j << 1 | i;
/*  906 */     i = j >>> 31;
/*  907 */     j = (int)l13;
/*  908 */     paramArrayOfint2[paramInt2 + 8] = j << 1 | i;
/*  909 */     i = j >>> 31;
/*  910 */     j = (int)l15;
/*  911 */     paramArrayOfint2[paramInt2 + 9] = j << 1 | i;
/*  912 */     i = j >>> 31;
/*  913 */     j = (int)l16;
/*  914 */     paramArrayOfint2[paramInt2 + 10] = j << 1 | i;
/*  915 */     i = j >>> 31;
/*  916 */     j = paramArrayOfint2[paramInt2 + 11] + (int)(l16 >>> 32L);
/*  917 */     paramArrayOfint2[paramInt2 + 11] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  922 */     long l = 0L;
/*  923 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  924 */     paramArrayOfint3[0] = (int)l;
/*  925 */     l >>= 32L;
/*  926 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  927 */     paramArrayOfint3[1] = (int)l;
/*  928 */     l >>= 32L;
/*  929 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  930 */     paramArrayOfint3[2] = (int)l;
/*  931 */     l >>= 32L;
/*  932 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  933 */     paramArrayOfint3[3] = (int)l;
/*  934 */     l >>= 32L;
/*  935 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  936 */     paramArrayOfint3[4] = (int)l;
/*  937 */     l >>= 32L;
/*  938 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*  939 */     paramArrayOfint3[5] = (int)l;
/*  940 */     l >>= 32L;
/*  941 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  946 */     long l = 0L;
/*  947 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  948 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/*  949 */     l >>= 32L;
/*  950 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  951 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/*  952 */     l >>= 32L;
/*  953 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  954 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/*  955 */     l >>= 32L;
/*  956 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  957 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/*  958 */     l >>= 32L;
/*  959 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  960 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/*  961 */     l >>= 32L;
/*  962 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  963 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/*  964 */     l >>= 32L;
/*  965 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subBothFrom(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  970 */     long l = 0L;
/*  971 */     l += (paramArrayOfint3[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  972 */     paramArrayOfint3[0] = (int)l;
/*  973 */     l >>= 32L;
/*  974 */     l += (paramArrayOfint3[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  975 */     paramArrayOfint3[1] = (int)l;
/*  976 */     l >>= 32L;
/*  977 */     l += (paramArrayOfint3[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  978 */     paramArrayOfint3[2] = (int)l;
/*  979 */     l >>= 32L;
/*  980 */     l += (paramArrayOfint3[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  981 */     paramArrayOfint3[3] = (int)l;
/*  982 */     l >>= 32L;
/*  983 */     l += (paramArrayOfint3[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  984 */     paramArrayOfint3[4] = (int)l;
/*  985 */     l >>= 32L;
/*  986 */     l += (paramArrayOfint3[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*  987 */     paramArrayOfint3[5] = (int)l;
/*  988 */     l >>= 32L;
/*  989 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  994 */     long l = 0L;
/*  995 */     l += (paramArrayOfint2[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL);
/*  996 */     paramArrayOfint2[0] = (int)l;
/*  997 */     l >>= 32L;
/*  998 */     l += (paramArrayOfint2[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL);
/*  999 */     paramArrayOfint2[1] = (int)l;
/* 1000 */     l >>= 32L;
/* 1001 */     l += (paramArrayOfint2[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL);
/* 1002 */     paramArrayOfint2[2] = (int)l;
/* 1003 */     l >>= 32L;
/* 1004 */     l += (paramArrayOfint2[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL);
/* 1005 */     paramArrayOfint2[3] = (int)l;
/* 1006 */     l >>= 32L;
/* 1007 */     l += (paramArrayOfint2[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL);
/* 1008 */     paramArrayOfint2[4] = (int)l;
/* 1009 */     l >>= 32L;
/* 1010 */     l += (paramArrayOfint2[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL);
/* 1011 */     paramArrayOfint2[5] = (int)l;
/* 1012 */     l >>= 32L;
/* 1013 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 1018 */     long l = 0L;
/* 1019 */     l += (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL);
/* 1020 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 1021 */     l >>= 32L;
/* 1022 */     l += (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL);
/* 1023 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 1024 */     l >>= 32L;
/* 1025 */     l += (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL);
/* 1026 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 1027 */     l >>= 32L;
/* 1028 */     l += (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL);
/* 1029 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 1030 */     l >>= 32L;
/* 1031 */     l += (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL);
/* 1032 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/* 1033 */     l >>= 32L;
/* 1034 */     l += (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL);
/* 1035 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/* 1036 */     l >>= 32L;
/* 1037 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger(int[] paramArrayOfint) {
/* 1042 */     byte[] arrayOfByte = new byte[24];
/* 1043 */     for (byte b = 0; b < 6; b++) {
/*      */       
/* 1045 */       int i = paramArrayOfint[b];
/* 1046 */       if (i != 0)
/*      */       {
/* 1048 */         Pack.intToBigEndian(i, arrayOfByte, 5 - b << 2);
/*      */       }
/*      */     } 
/* 1051 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/* 1056 */     byte[] arrayOfByte = new byte[24];
/* 1057 */     for (byte b = 0; b < 3; b++) {
/*      */       
/* 1059 */       long l = paramArrayOflong[b];
/* 1060 */       if (l != 0L)
/*      */       {
/* 1062 */         Pack.longToBigEndian(l, arrayOfByte, 2 - b << 3);
/*      */       }
/*      */     } 
/* 1065 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero(int[] paramArrayOfint) {
/* 1070 */     paramArrayOfint[0] = 0;
/* 1071 */     paramArrayOfint[1] = 0;
/* 1072 */     paramArrayOfint[2] = 0;
/* 1073 */     paramArrayOfint[3] = 0;
/* 1074 */     paramArrayOfint[4] = 0;
/* 1075 */     paramArrayOfint[5] = 0;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat192.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */