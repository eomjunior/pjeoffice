/*      */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import java.security.SecureRandom;
/*      */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
/*      */ import org.bouncycastle.util.BigIntegers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class IntegerFunctions
/*      */ {
/*   16 */   private static final BigInteger ZERO = BigInteger.valueOf(0L);
/*      */   
/*   18 */   private static final BigInteger ONE = BigInteger.valueOf(1L);
/*      */   
/*   20 */   private static final BigInteger TWO = BigInteger.valueOf(2L);
/*      */   
/*   22 */   private static final BigInteger FOUR = BigInteger.valueOf(4L);
/*      */   
/*   24 */   private static final int[] SMALL_PRIMES = new int[] { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
/*      */ 
/*      */   
/*      */   private static final long SMALL_PRIME_PRODUCT = 152125131763605L;
/*      */ 
/*      */   
/*   30 */   private static SecureRandom sr = null;
/*      */ 
/*      */   
/*   33 */   private static final int[] jacobiTable = new int[] { 0, 1, 0, -1, 0, -1, 0, 1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int jacobi(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*   61 */     long l = 1L;
/*      */     
/*   63 */     l = 1L;
/*      */ 
/*      */     
/*   66 */     if (paramBigInteger2.equals(ZERO)) {
/*      */       
/*   68 */       BigInteger bigInteger = paramBigInteger1.abs();
/*   69 */       return bigInteger.equals(ONE) ? 1 : 0;
/*      */     } 
/*      */     
/*   72 */     if (!paramBigInteger1.testBit(0) && !paramBigInteger2.testBit(0))
/*      */     {
/*   74 */       return 0;
/*      */     }
/*      */     
/*   77 */     BigInteger bigInteger1 = paramBigInteger1;
/*   78 */     BigInteger bigInteger2 = paramBigInteger2;
/*      */     
/*   80 */     if (bigInteger2.signum() == -1) {
/*      */       
/*   82 */       bigInteger2 = bigInteger2.negate();
/*   83 */       if (bigInteger1.signum() == -1)
/*      */       {
/*   85 */         l = -1L;
/*      */       }
/*      */     } 
/*      */     
/*   89 */     BigInteger bigInteger3 = ZERO;
/*   90 */     while (!bigInteger2.testBit(0)) {
/*      */       
/*   92 */       bigInteger3 = bigInteger3.add(ONE);
/*   93 */       bigInteger2 = bigInteger2.divide(TWO);
/*      */     } 
/*      */     
/*   96 */     if (bigInteger3.testBit(0))
/*      */     {
/*   98 */       l *= jacobiTable[bigInteger1.intValue() & 0x7];
/*      */     }
/*      */     
/*  101 */     if (bigInteger1.signum() < 0) {
/*      */       
/*  103 */       if (bigInteger2.testBit(1))
/*      */       {
/*  105 */         l = -l;
/*      */       }
/*  107 */       bigInteger1 = bigInteger1.negate();
/*      */     } 
/*      */ 
/*      */     
/*  111 */     while (bigInteger1.signum() != 0) {
/*      */       
/*  113 */       bigInteger3 = ZERO;
/*  114 */       while (!bigInteger1.testBit(0)) {
/*      */         
/*  116 */         bigInteger3 = bigInteger3.add(ONE);
/*  117 */         bigInteger1 = bigInteger1.divide(TWO);
/*      */       } 
/*  119 */       if (bigInteger3.testBit(0))
/*      */       {
/*  121 */         l *= jacobiTable[bigInteger2.intValue() & 0x7];
/*      */       }
/*      */       
/*  124 */       if (bigInteger1.compareTo(bigInteger2) < 0) {
/*      */ 
/*      */         
/*  127 */         BigInteger bigInteger = bigInteger1;
/*  128 */         bigInteger1 = bigInteger2;
/*  129 */         bigInteger2 = bigInteger;
/*  130 */         if (bigInteger1.testBit(1) && bigInteger2.testBit(1))
/*      */         {
/*  132 */           l = -l;
/*      */         }
/*      */       } 
/*  135 */       bigInteger1 = bigInteger1.subtract(bigInteger2);
/*      */     } 
/*      */     
/*  138 */     return bigInteger2.equals(ONE) ? (int)l : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger ressol(BigInteger paramBigInteger1, BigInteger paramBigInteger2) throws IllegalArgumentException {
/*  155 */     BigInteger bigInteger1 = null;
/*      */     
/*  157 */     if (paramBigInteger1.compareTo(ZERO) < 0)
/*      */     {
/*  159 */       paramBigInteger1 = paramBigInteger1.add(paramBigInteger2);
/*      */     }
/*      */     
/*  162 */     if (paramBigInteger1.equals(ZERO))
/*      */     {
/*  164 */       return ZERO;
/*      */     }
/*      */     
/*  167 */     if (paramBigInteger2.equals(TWO))
/*      */     {
/*  169 */       return paramBigInteger1;
/*      */     }
/*      */ 
/*      */     
/*  173 */     if (paramBigInteger2.testBit(0) && paramBigInteger2.testBit(1)) {
/*      */       
/*  175 */       if (jacobi(paramBigInteger1, paramBigInteger2) == 1) {
/*      */         
/*  177 */         bigInteger1 = paramBigInteger2.add(ONE);
/*  178 */         bigInteger1 = bigInteger1.shiftRight(2);
/*  179 */         return paramBigInteger1.modPow(bigInteger1, paramBigInteger2);
/*      */       } 
/*      */       
/*  182 */       throw new IllegalArgumentException("No quadratic residue: " + paramBigInteger1 + ", " + paramBigInteger2);
/*      */     } 
/*      */     
/*  185 */     long l1 = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  190 */     BigInteger bigInteger2 = paramBigInteger2.subtract(ONE);
/*  191 */     long l2 = 0L;
/*  192 */     while (!bigInteger2.testBit(0)) {
/*      */       
/*  194 */       l2++;
/*  195 */       bigInteger2 = bigInteger2.shiftRight(1);
/*      */     } 
/*      */     
/*  198 */     bigInteger2 = bigInteger2.subtract(ONE);
/*  199 */     bigInteger2 = bigInteger2.shiftRight(1);
/*      */ 
/*      */     
/*  202 */     BigInteger bigInteger3 = paramBigInteger1.modPow(bigInteger2, paramBigInteger2);
/*      */     
/*  204 */     BigInteger bigInteger4 = bigInteger3.multiply(bigInteger3).remainder(paramBigInteger2);
/*  205 */     bigInteger4 = bigInteger4.multiply(paramBigInteger1).remainder(paramBigInteger2);
/*  206 */     bigInteger3 = bigInteger3.multiply(paramBigInteger1).remainder(paramBigInteger2);
/*      */     
/*  208 */     if (bigInteger4.equals(ONE))
/*      */     {
/*  210 */       return bigInteger3;
/*      */     }
/*      */ 
/*      */     
/*  214 */     BigInteger bigInteger5 = TWO;
/*  215 */     while (jacobi(bigInteger5, paramBigInteger2) == 1)
/*      */     {
/*      */       
/*  218 */       bigInteger5 = bigInteger5.add(ONE);
/*      */     }
/*      */     
/*  221 */     bigInteger1 = bigInteger2;
/*  222 */     bigInteger1 = bigInteger1.multiply(TWO);
/*  223 */     bigInteger1 = bigInteger1.add(ONE);
/*  224 */     BigInteger bigInteger6 = bigInteger5.modPow(bigInteger1, paramBigInteger2);
/*      */ 
/*      */     
/*  227 */     while (bigInteger4.compareTo(ONE) == 1) {
/*      */       
/*  229 */       bigInteger2 = bigInteger4;
/*  230 */       l1 = l2;
/*  231 */       l2 = 0L;
/*      */       
/*  233 */       while (!bigInteger2.equals(ONE)) {
/*      */         
/*  235 */         bigInteger2 = bigInteger2.multiply(bigInteger2).mod(paramBigInteger2);
/*  236 */         l2++;
/*      */       } 
/*      */       
/*  239 */       l1 -= l2;
/*  240 */       if (l1 == 0L)
/*      */       {
/*  242 */         throw new IllegalArgumentException("No quadratic residue: " + paramBigInteger1 + ", " + paramBigInteger2);
/*      */       }
/*      */       
/*  245 */       bigInteger1 = ONE; long l;
/*  246 */       for (l = 0L; l < l1 - 1L; l++)
/*      */       {
/*  248 */         bigInteger1 = bigInteger1.shiftLeft(1);
/*      */       }
/*  250 */       bigInteger6 = bigInteger6.modPow(bigInteger1, paramBigInteger2);
/*  251 */       bigInteger3 = bigInteger3.multiply(bigInteger6).remainder(paramBigInteger2);
/*  252 */       bigInteger6 = bigInteger6.multiply(bigInteger6).remainder(paramBigInteger2);
/*  253 */       bigInteger4 = bigInteger4.multiply(bigInteger6).mod(paramBigInteger2);
/*      */     } 
/*  255 */     return bigInteger3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int gcd(int paramInt1, int paramInt2) {
/*  267 */     return BigInteger.valueOf(paramInt1).gcd(BigInteger.valueOf(paramInt2)).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] extGCD(int paramInt1, int paramInt2) {
/*  279 */     BigInteger bigInteger1 = BigInteger.valueOf(paramInt1);
/*  280 */     BigInteger bigInteger2 = BigInteger.valueOf(paramInt2);
/*  281 */     BigInteger[] arrayOfBigInteger = extgcd(bigInteger1, bigInteger2);
/*  282 */     int[] arrayOfInt = new int[3];
/*  283 */     arrayOfInt[0] = arrayOfBigInteger[0].intValue();
/*  284 */     arrayOfInt[1] = arrayOfBigInteger[1].intValue();
/*  285 */     arrayOfInt[2] = arrayOfBigInteger[2].intValue();
/*  286 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger divideAndRound(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  291 */     if (paramBigInteger1.signum() < 0)
/*      */     {
/*  293 */       return divideAndRound(paramBigInteger1.negate(), paramBigInteger2).negate();
/*      */     }
/*  295 */     if (paramBigInteger2.signum() < 0)
/*      */     {
/*  297 */       return divideAndRound(paramBigInteger1, paramBigInteger2.negate()).negate();
/*      */     }
/*  299 */     return paramBigInteger1.shiftLeft(1).add(paramBigInteger2).divide(paramBigInteger2.shiftLeft(1));
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger[] divideAndRound(BigInteger[] paramArrayOfBigInteger, BigInteger paramBigInteger) {
/*  304 */     BigInteger[] arrayOfBigInteger = new BigInteger[paramArrayOfBigInteger.length];
/*  305 */     for (byte b = 0; b < paramArrayOfBigInteger.length; b++)
/*      */     {
/*  307 */       arrayOfBigInteger[b] = divideAndRound(paramArrayOfBigInteger[b], paramBigInteger);
/*      */     }
/*  309 */     return arrayOfBigInteger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int ceilLog(BigInteger paramBigInteger) {
/*  321 */     byte b = 0;
/*  322 */     BigInteger bigInteger = ONE;
/*  323 */     while (bigInteger.compareTo(paramBigInteger) < 0) {
/*      */       
/*  325 */       b++;
/*  326 */       bigInteger = bigInteger.shiftLeft(1);
/*      */     } 
/*  328 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int ceilLog(int paramInt) {
/*  340 */     byte b = 0;
/*  341 */     int i = 1;
/*  342 */     while (i < paramInt) {
/*      */       
/*  344 */       i <<= 1;
/*  345 */       b++;
/*      */     } 
/*  347 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int ceilLog256(int paramInt) {
/*      */     int i;
/*  359 */     if (paramInt == 0)
/*      */     {
/*  361 */       return 1;
/*      */     }
/*      */     
/*  364 */     if (paramInt < 0) {
/*      */       
/*  366 */       i = -paramInt;
/*      */     }
/*      */     else {
/*      */       
/*  370 */       i = paramInt;
/*      */     } 
/*      */     
/*  373 */     byte b = 0;
/*  374 */     while (i > 0) {
/*      */       
/*  376 */       b++;
/*  377 */       i >>>= 8;
/*      */     } 
/*  379 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int ceilLog256(long paramLong) {
/*      */     long l;
/*  391 */     if (paramLong == 0L)
/*      */     {
/*  393 */       return 1;
/*      */     }
/*      */     
/*  396 */     if (paramLong < 0L) {
/*      */       
/*  398 */       l = -paramLong;
/*      */     }
/*      */     else {
/*      */       
/*  402 */       l = paramLong;
/*      */     } 
/*      */     
/*  405 */     byte b = 0;
/*  406 */     while (l > 0L) {
/*      */       
/*  408 */       b++;
/*  409 */       l >>>= 8L;
/*      */     } 
/*  411 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int floorLog(BigInteger paramBigInteger) {
/*  423 */     byte b = -1;
/*  424 */     BigInteger bigInteger = ONE;
/*  425 */     while (bigInteger.compareTo(paramBigInteger) <= 0) {
/*      */       
/*  427 */       b++;
/*  428 */       bigInteger = bigInteger.shiftLeft(1);
/*      */     } 
/*  430 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int floorLog(int paramInt) {
/*  442 */     byte b = 0;
/*  443 */     if (paramInt <= 0)
/*      */     {
/*  445 */       return -1;
/*      */     }
/*  447 */     int i = paramInt >>> 1;
/*  448 */     while (i > 0) {
/*      */       
/*  450 */       b++;
/*  451 */       i >>>= 1;
/*      */     } 
/*      */     
/*  454 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int maxPower(int paramInt) {
/*  466 */     byte b = 0;
/*  467 */     if (paramInt != 0) {
/*      */       
/*  469 */       int i = 1;
/*  470 */       while ((paramInt & i) == 0) {
/*      */         
/*  472 */         b++;
/*  473 */         i <<= 1;
/*      */       } 
/*      */     } 
/*      */     
/*  477 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int bitCount(int paramInt) {
/*  487 */     int i = 0;
/*  488 */     while (paramInt != 0) {
/*      */       
/*  490 */       i += paramInt & 0x1;
/*  491 */       paramInt >>>= 1;
/*      */     } 
/*      */     
/*  494 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int order(int paramInt1, int paramInt2) {
/*  510 */     int i = paramInt1 % paramInt2;
/*  511 */     byte b = 1;
/*      */ 
/*      */     
/*  514 */     if (i == 0)
/*      */     {
/*  516 */       throw new IllegalArgumentException("" + paramInt1 + " is not an element of Z/(" + paramInt1 + "Z)^*; it is not meaningful to compute its order.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  521 */     while (i != 1) {
/*      */       
/*  523 */       i *= paramInt1;
/*  524 */       i %= paramInt2;
/*  525 */       if (i < 0)
/*      */       {
/*  527 */         i += paramInt2;
/*      */       }
/*  529 */       b++;
/*      */     } 
/*      */     
/*  532 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger reduceInto(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3) {
/*  546 */     return paramBigInteger1.subtract(paramBigInteger2).mod(paramBigInteger3.subtract(paramBigInteger2)).add(paramBigInteger2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int pow(int paramInt1, int paramInt2) {
/*  558 */     int i = 1;
/*  559 */     while (paramInt2 > 0) {
/*      */       
/*  561 */       if ((paramInt2 & 0x1) == 1)
/*      */       {
/*  563 */         i *= paramInt1;
/*      */       }
/*  565 */       paramInt1 *= paramInt1;
/*  566 */       paramInt2 >>>= 1;
/*      */     } 
/*  568 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long pow(long paramLong, int paramInt) {
/*  580 */     long l = 1L;
/*  581 */     while (paramInt > 0) {
/*      */       
/*  583 */       if ((paramInt & 0x1) == 1)
/*      */       {
/*  585 */         l *= paramLong;
/*      */       }
/*  587 */       paramLong *= paramLong;
/*  588 */       paramInt >>>= 1;
/*      */     } 
/*  590 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int modPow(int paramInt1, int paramInt2, int paramInt3) {
/*  603 */     if (paramInt3 <= 0 || paramInt3 * paramInt3 > Integer.MAX_VALUE || paramInt2 < 0)
/*      */     {
/*  605 */       return 0;
/*      */     }
/*  607 */     int i = 1;
/*  608 */     paramInt1 = (paramInt1 % paramInt3 + paramInt3) % paramInt3;
/*  609 */     while (paramInt2 > 0) {
/*      */       
/*  611 */       if ((paramInt2 & 0x1) == 1)
/*      */       {
/*  613 */         i = i * paramInt1 % paramInt3;
/*      */       }
/*  615 */       paramInt1 = paramInt1 * paramInt1 % paramInt3;
/*  616 */       paramInt2 >>>= 1;
/*      */     } 
/*  618 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger[] extgcd(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  630 */     BigInteger bigInteger1 = ONE;
/*  631 */     BigInteger bigInteger2 = ZERO;
/*  632 */     BigInteger bigInteger3 = paramBigInteger1;
/*  633 */     if (paramBigInteger2.signum() != 0) {
/*      */       
/*  635 */       BigInteger bigInteger4 = ZERO;
/*  636 */       BigInteger bigInteger5 = paramBigInteger2;
/*  637 */       while (bigInteger5.signum() != 0) {
/*      */         
/*  639 */         BigInteger[] arrayOfBigInteger = bigInteger3.divideAndRemainder(bigInteger5);
/*  640 */         BigInteger bigInteger6 = arrayOfBigInteger[0];
/*  641 */         BigInteger bigInteger7 = arrayOfBigInteger[1];
/*  642 */         BigInteger bigInteger8 = bigInteger1.subtract(bigInteger6.multiply(bigInteger4));
/*  643 */         bigInteger1 = bigInteger4;
/*  644 */         bigInteger3 = bigInteger5;
/*  645 */         bigInteger4 = bigInteger8;
/*  646 */         bigInteger5 = bigInteger7;
/*      */       } 
/*  648 */       bigInteger2 = bigInteger3.subtract(paramBigInteger1.multiply(bigInteger1)).divide(paramBigInteger2);
/*      */     } 
/*  650 */     return new BigInteger[] { bigInteger3, bigInteger1, bigInteger2 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger leastCommonMultiple(BigInteger[] paramArrayOfBigInteger) {
/*  661 */     int i = paramArrayOfBigInteger.length;
/*  662 */     BigInteger bigInteger = paramArrayOfBigInteger[0];
/*  663 */     for (byte b = 1; b < i; b++) {
/*      */       
/*  665 */       BigInteger bigInteger1 = bigInteger.gcd(paramArrayOfBigInteger[b]);
/*  666 */       bigInteger = bigInteger.multiply(paramArrayOfBigInteger[b]).divide(bigInteger1);
/*      */     } 
/*  668 */     return bigInteger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mod(long paramLong1, long paramLong2) {
/*  682 */     long l = paramLong1 % paramLong2;
/*  683 */     if (l < 0L)
/*      */     {
/*  685 */       l += paramLong2;
/*      */     }
/*  687 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int modInverse(int paramInt1, int paramInt2) {
/*  699 */     return BigInteger.valueOf(paramInt1).modInverse(BigInteger.valueOf(paramInt2))
/*  700 */       .intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long modInverse(long paramLong1, long paramLong2) {
/*  712 */     return BigInteger.valueOf(paramLong1).modInverse(BigInteger.valueOf(paramLong2))
/*  713 */       .longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int isPower(int paramInt1, int paramInt2) {
/*  726 */     if (paramInt1 <= 0)
/*      */     {
/*  728 */       return -1;
/*      */     }
/*  730 */     byte b = 0;
/*  731 */     int i = paramInt1;
/*  732 */     while (i > 1) {
/*      */       
/*  734 */       if (i % paramInt2 != 0)
/*      */       {
/*  736 */         return -1;
/*      */       }
/*  738 */       i /= paramInt2;
/*  739 */       b++;
/*      */     } 
/*  741 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int leastDiv(int paramInt) {
/*  752 */     if (paramInt < 0)
/*      */     {
/*  754 */       paramInt = -paramInt;
/*      */     }
/*  756 */     if (paramInt == 0)
/*      */     {
/*  758 */       return 1;
/*      */     }
/*  760 */     if ((paramInt & 0x1) == 0)
/*      */     {
/*  762 */       return 2;
/*      */     }
/*  764 */     byte b = 3;
/*  765 */     while (b <= paramInt / b) {
/*      */       
/*  767 */       if (paramInt % b == 0)
/*      */       {
/*  769 */         return b;
/*      */       }
/*  771 */       b += 2;
/*      */     } 
/*      */     
/*  774 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrime(int paramInt) {
/*  788 */     if (paramInt < 2)
/*      */     {
/*  790 */       return false;
/*      */     }
/*  792 */     if (paramInt == 2)
/*      */     {
/*  794 */       return true;
/*      */     }
/*  796 */     if ((paramInt & 0x1) == 0)
/*      */     {
/*  798 */       return false;
/*      */     }
/*  800 */     if (paramInt < 42)
/*      */     {
/*  802 */       for (byte b = 0; b < SMALL_PRIMES.length; b++) {
/*      */         
/*  804 */         if (paramInt == SMALL_PRIMES[b])
/*      */         {
/*  806 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  811 */     if (paramInt % 3 == 0 || paramInt % 5 == 0 || paramInt % 7 == 0 || paramInt % 11 == 0 || paramInt % 13 == 0 || paramInt % 17 == 0 || paramInt % 19 == 0 || paramInt % 23 == 0 || paramInt % 29 == 0 || paramInt % 31 == 0 || paramInt % 37 == 0 || paramInt % 41 == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  816 */       return false;
/*      */     }
/*      */     
/*  819 */     return BigInteger.valueOf(paramInt).isProbablePrime(20);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean passesSmallPrimeTest(BigInteger paramBigInteger) {
/*  832 */     int[] arrayOfInt = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*      */       
/*  856 */       if (paramBigInteger.mod(BigInteger.valueOf(arrayOfInt[b])).equals(ZERO))
/*      */       {
/*      */         
/*  859 */         return false;
/*      */       }
/*      */     } 
/*  862 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int nextSmallerPrime(int paramInt) {
/*  874 */     if (paramInt <= 2)
/*      */     {
/*  876 */       return 1;
/*      */     }
/*      */     
/*  879 */     if (paramInt == 3)
/*      */     {
/*  881 */       return 2;
/*      */     }
/*      */     
/*  884 */     if ((paramInt & 0x1) == 0) {
/*      */       
/*  886 */       paramInt--;
/*      */     }
/*      */     else {
/*      */       
/*  890 */       paramInt -= 2;
/*      */     } 
/*      */     
/*  893 */     while (paramInt > 3 && !isPrime(paramInt))
/*      */     {
/*  895 */       paramInt -= 2;
/*      */     }
/*  897 */     return paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger nextProbablePrime(BigInteger paramBigInteger, int paramInt) {
/*  911 */     if (paramBigInteger.signum() < 0 || paramBigInteger.signum() == 0 || paramBigInteger.equals(ONE))
/*      */     {
/*  913 */       return TWO;
/*      */     }
/*      */     
/*  916 */     BigInteger bigInteger = paramBigInteger.add(ONE);
/*      */ 
/*      */     
/*  919 */     if (!bigInteger.testBit(0))
/*      */     {
/*  921 */       bigInteger = bigInteger.add(ONE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  927 */       if (bigInteger.bitLength() > 6) {
/*      */ 
/*      */         
/*  930 */         long l = bigInteger.remainder(BigInteger.valueOf(152125131763605L)).longValue();
/*  931 */         if (l % 3L == 0L || l % 5L == 0L || l % 7L == 0L || l % 11L == 0L || l % 13L == 0L || l % 17L == 0L || l % 19L == 0L || l % 23L == 0L || l % 29L == 0L || l % 31L == 0L || l % 37L == 0L || l % 41L == 0L) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  936 */           bigInteger = bigInteger.add(TWO);
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/*      */       
/*  942 */       if (bigInteger.bitLength() < 4)
/*      */       {
/*  944 */         return bigInteger;
/*      */       }
/*      */ 
/*      */       
/*  948 */       if (bigInteger.isProbablePrime(paramInt))
/*      */       {
/*  950 */         return bigInteger;
/*      */       }
/*      */       
/*  953 */       bigInteger = bigInteger.add(TWO);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger nextProbablePrime(BigInteger paramBigInteger) {
/*  966 */     return nextProbablePrime(paramBigInteger, 20);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger nextPrime(long paramLong) {
/*  978 */     boolean bool = false;
/*  979 */     long l2 = 0L;
/*      */     
/*  981 */     if (paramLong <= 1L)
/*      */     {
/*  983 */       return BigInteger.valueOf(2L);
/*      */     }
/*  985 */     if (paramLong == 2L)
/*      */     {
/*  987 */       return BigInteger.valueOf(3L);
/*      */     }
/*      */     
/*  990 */     for (long l1 = paramLong + 1L + (paramLong & 0x1L); l1 <= paramLong << 1L && !bool; l1 += 2L) {
/*      */       long l;
/*  992 */       for (l = 3L; l <= l1 >> 1L && !bool; l += 2L) {
/*      */         
/*  994 */         if (l1 % l == 0L)
/*      */         {
/*  996 */           bool = true;
/*      */         }
/*      */       } 
/*  999 */       if (bool) {
/*      */         
/* 1001 */         bool = false;
/*      */       }
/*      */       else {
/*      */         
/* 1005 */         l2 = l1;
/* 1006 */         bool = true;
/*      */       } 
/*      */     } 
/* 1009 */     return BigInteger.valueOf(l2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger binomial(int paramInt1, int paramInt2) {
/* 1027 */     BigInteger bigInteger = ONE;
/*      */     
/* 1029 */     if (paramInt1 == 0) {
/*      */       
/* 1031 */       if (paramInt2 == 0)
/*      */       {
/* 1033 */         return bigInteger;
/*      */       }
/* 1035 */       return ZERO;
/*      */     } 
/*      */ 
/*      */     
/* 1039 */     if (paramInt2 > paramInt1 >>> 1)
/*      */     {
/* 1041 */       paramInt2 = paramInt1 - paramInt2;
/*      */     }
/*      */     
/* 1044 */     for (byte b = 1; b <= paramInt2; b++)
/*      */     {
/*      */       
/* 1047 */       bigInteger = bigInteger.multiply(BigInteger.valueOf((paramInt1 - b - 1))).divide(BigInteger.valueOf(b));
/*      */     }
/*      */     
/* 1050 */     return bigInteger;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger randomize(BigInteger paramBigInteger) {
/* 1055 */     if (sr == null)
/*      */     {
/* 1057 */       sr = CryptoServicesRegistrar.getSecureRandom();
/*      */     }
/* 1059 */     return randomize(paramBigInteger, sr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger randomize(BigInteger paramBigInteger, SecureRandom paramSecureRandom) {
/* 1065 */     int i = paramBigInteger.bitLength();
/* 1066 */     BigInteger bigInteger = BigInteger.valueOf(0L);
/*      */     
/* 1068 */     if (paramSecureRandom == null)
/*      */     {
/* 1070 */       paramSecureRandom = (sr != null) ? sr : CryptoServicesRegistrar.getSecureRandom();
/*      */     }
/*      */     
/* 1073 */     for (byte b = 0; b < 20; b++) {
/*      */       
/* 1075 */       bigInteger = BigIntegers.createRandomBigInteger(i, paramSecureRandom);
/* 1076 */       if (bigInteger.compareTo(paramBigInteger) < 0)
/*      */       {
/* 1078 */         return bigInteger;
/*      */       }
/*      */     } 
/* 1081 */     return bigInteger.mod(paramBigInteger);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger squareRoot(BigInteger paramBigInteger) {
/* 1095 */     if (paramBigInteger.compareTo(ZERO) < 0)
/*      */     {
/* 1097 */       throw new ArithmeticException("cannot extract root of negative number" + paramBigInteger + ".");
/*      */     }
/*      */ 
/*      */     
/* 1101 */     int i = paramBigInteger.bitLength();
/* 1102 */     BigInteger bigInteger1 = ZERO;
/* 1103 */     BigInteger bigInteger2 = ZERO;
/*      */ 
/*      */     
/* 1106 */     if ((i & 0x1) != 0) {
/*      */       
/* 1108 */       bigInteger1 = bigInteger1.add(ONE);
/* 1109 */       i--;
/*      */     } 
/*      */     
/* 1112 */     while (i > 0) {
/*      */       
/* 1114 */       bigInteger2 = bigInteger2.multiply(FOUR);
/* 1115 */       bigInteger2 = bigInteger2.add(BigInteger.valueOf(((paramBigInteger.testBit(--i) ? 2 : 
/* 1116 */             0) + (
/* 1117 */             paramBigInteger.testBit(--i) ? 1 : 0))));
/* 1118 */       BigInteger bigInteger = bigInteger1.multiply(FOUR).add(ONE);
/* 1119 */       bigInteger1 = bigInteger1.multiply(TWO);
/* 1120 */       if (bigInteger2.compareTo(bigInteger) != -1) {
/*      */         
/* 1122 */         bigInteger1 = bigInteger1.add(ONE);
/* 1123 */         bigInteger2 = bigInteger2.subtract(bigInteger);
/*      */       } 
/*      */     } 
/*      */     
/* 1127 */     return bigInteger1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float intRoot(int paramInt1, int paramInt2) {
/* 1139 */     float f1 = (paramInt1 / paramInt2);
/* 1140 */     float f2 = 0.0F;
/* 1141 */     byte b = 0;
/* 1142 */     while (Math.abs(f2 - f1) > 1.0E-4D) {
/*      */       
/* 1144 */       float f = floatPow(f1, paramInt2);
/* 1145 */       while (Float.isInfinite(f)) {
/*      */         
/* 1147 */         f1 = (f1 + f2) / 2.0F;
/* 1148 */         f = floatPow(f1, paramInt2);
/*      */       } 
/* 1150 */       b++;
/* 1151 */       f2 = f1;
/* 1152 */       f1 = f2 - (f - paramInt1) / paramInt2 * floatPow(f2, paramInt2 - 1);
/*      */     } 
/* 1154 */     return f1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float floatPow(float paramFloat, int paramInt) {
/* 1166 */     float f = 1.0F;
/* 1167 */     for (; paramInt > 0; paramInt--)
/*      */     {
/* 1169 */       f *= paramFloat;
/*      */     }
/* 1171 */     return f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double log(double paramDouble) {
/* 1183 */     if (paramDouble > 0.0D && paramDouble < 1.0D) {
/*      */       
/* 1185 */       double d = 1.0D / paramDouble;
/* 1186 */       return -log(d);
/*      */     } 
/*      */ 
/*      */     
/* 1190 */     byte b = 0;
/* 1191 */     double d1 = 1.0D;
/* 1192 */     double d2 = paramDouble;
/*      */     
/* 1194 */     while (d2 > 2.0D) {
/*      */       
/* 1196 */       d2 /= 2.0D;
/* 1197 */       b++;
/* 1198 */       d1 *= 2.0D;
/*      */     } 
/* 1200 */     double d3 = paramDouble / d1;
/* 1201 */     d3 = logBKM(d3);
/* 1202 */     return b + d3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double log(long paramLong) {
/* 1214 */     int i = floorLog(BigInteger.valueOf(paramLong));
/* 1215 */     long l = (1 << i);
/* 1216 */     double d = paramLong / l;
/* 1217 */     d = logBKM(d);
/* 1218 */     return i + d;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static double logBKM(double paramDouble) {
/* 1230 */     double[] arrayOfDouble = { 1.0D, 0.5849625007211562D, 0.32192809488736235D, 0.16992500144231237D, 0.0874628412503394D, 0.044394119358453436D, 0.02236781302845451D, 0.01122725542325412D, 0.005624549193878107D, 0.0028150156070540383D, 0.0014081943928083889D, 7.042690112466433E-4D, 3.5217748030102726E-4D, 1.7609948644250602E-4D, 8.80524301221769E-5D, 4.4026886827316716E-5D, 2.2013611360340496E-5D, 1.1006847667481442E-5D, 5.503434330648604E-6D, 2.751719789561283E-6D, 1.375860550841138E-6D, 6.879304394358497E-7D, 3.4396526072176454E-7D, 1.7198264061184464E-7D, 8.599132286866321E-8D, 4.299566207501687E-8D, 2.1497831197679756E-8D, 1.0748915638882709E-8D, 5.374457829452062E-9D, 2.687228917228708E-9D, 1.3436144592400231E-9D, 6.718072297764289E-10D, 3.3590361492731876E-10D, 1.6795180747343547E-10D, 8.397590373916176E-11D, 4.1987951870191886E-11D, 2.0993975935248694E-11D, 1.0496987967662534E-11D, 5.2484939838408146E-12D, 2.624246991922794E-12D, 1.3121234959619935E-12D, 6.56061747981146E-13D, 3.2803087399061026E-13D, 1.6401543699531447E-13D, 8.200771849765956E-14D, 4.1003859248830365E-14D, 2.0501929624415328E-14D, 1.02509648122077E-14D, 5.1254824061038595E-15D, 2.5627412030519317E-15D, 1.2813706015259665E-15D, 6.406853007629834E-16D, 3.203426503814917E-16D, 1.6017132519074588E-16D, 8.008566259537294E-17D, 4.004283129768647E-17D, 2.0021415648843235E-17D, 1.0010707824421618E-17D, 5.005353912210809E-18D, 2.5026769561054044E-18D, 1.2513384780527022E-18D, 6.256692390263511E-19D, 3.1283461951317555E-19D, 1.5641730975658778E-19D, 7.820865487829389E-20D, 3.9104327439146944E-20D, 1.9552163719573472E-20D, 9.776081859786736E-21D, 4.888040929893368E-21D, 2.444020464946684E-21D, 1.222010232473342E-21D, 6.11005116236671E-22D, 3.055025581183355E-22D, 1.5275127905916775E-22D, 7.637563952958387E-23D, 3.818781976479194E-23D, 1.909390988239597E-23D, 9.546954941197984E-24D, 4.773477470598992E-24D, 2.386738735299496E-24D, 1.193369367649748E-24D, 5.96684683824874E-25D, 2.98342341912437E-25D, 1.491711709562185E-25D, 7.458558547810925E-26D, 3.7292792739054626E-26D, 1.8646396369527313E-26D, 9.323198184763657E-27D, 4.661599092381828E-27D, 2.330799546190914E-27D, 1.165399773095457E-27D, 5.826998865477285E-28D, 2.9134994327386427E-28D, 1.4567497163693213E-28D, 7.283748581846607E-29D, 3.6418742909233034E-29D, 1.8209371454616517E-29D, 9.104685727308258E-30D, 4.552342863654129E-30D, 2.2761714318270646E-30D };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1332 */     byte b1 = 53;
/* 1333 */     double d1 = 1.0D;
/* 1334 */     double d2 = 0.0D;
/*      */     
/* 1336 */     double d3 = 1.0D;
/*      */ 
/*      */     
/* 1339 */     for (byte b2 = 0; b2 < b1; b2++) {
/*      */       
/* 1341 */       double d = d1 + d1 * d3;
/* 1342 */       if (d <= paramDouble) {
/*      */         
/* 1344 */         d1 = d;
/* 1345 */         d2 += arrayOfDouble[b2];
/*      */       } 
/* 1347 */       d3 *= 0.5D;
/*      */     } 
/* 1349 */     return d2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isIncreasing(int[] paramArrayOfint) {
/* 1354 */     for (byte b = 1; b < paramArrayOfint.length; b++) {
/*      */       
/* 1356 */       if (paramArrayOfint[b - 1] >= paramArrayOfint[b])
/*      */       {
/* 1358 */         return false;
/*      */       }
/*      */     } 
/* 1361 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] integerToOctets(BigInteger paramBigInteger) {
/* 1366 */     byte[] arrayOfByte1 = paramBigInteger.abs().toByteArray();
/*      */ 
/*      */     
/* 1369 */     if ((paramBigInteger.bitLength() & 0x7) != 0)
/*      */     {
/* 1371 */       return arrayOfByte1;
/*      */     }
/*      */     
/* 1374 */     byte[] arrayOfByte2 = new byte[paramBigInteger.bitLength() >> 3];
/* 1375 */     System.arraycopy(arrayOfByte1, 1, arrayOfByte2, 0, arrayOfByte2.length);
/* 1376 */     return arrayOfByte2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger octetsToInteger(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 1382 */     byte[] arrayOfByte = new byte[paramInt2 + 1];
/*      */     
/* 1384 */     arrayOfByte[0] = 0;
/* 1385 */     System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 1, paramInt2);
/* 1386 */     return new BigInteger(arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger octetsToInteger(byte[] paramArrayOfbyte) {
/* 1391 */     return octetsToInteger(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/IntegerFunctions.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */