/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.SimpleBigDecimal;
/*     */ import org.bouncycastle.math.ec.ZTauElement;
/*     */ 
/*     */ 
/*     */ class Tnaf
/*     */ {
/*  14 */   private static final BigInteger MINUS_ONE = ECConstants.ONE.negate();
/*  15 */   private static final BigInteger MINUS_TWO = ECConstants.TWO.negate();
/*  16 */   private static final BigInteger MINUS_THREE = ECConstants.THREE.negate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte WIDTH = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte POW_2_WIDTH = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static final ZTauElement[] alpha0 = new ZTauElement[] { null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, MINUS_ONE), null, new ZTauElement(MINUS_ONE, MINUS_ONE), null, new ZTauElement(ECConstants.ONE, MINUS_ONE), null };
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
/*  51 */   public static final byte[][] alpha0Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final ZTauElement[] alpha1 = new ZTauElement[] { null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, ECConstants.ONE), null, new ZTauElement(MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null };
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
/*  70 */   public static final byte[][] alpha1Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, -1 } };
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
/*     */ 
/*     */   
/*     */   public static BigInteger norm(byte paramByte, ZTauElement paramZTauElement) {
/*  87 */     BigInteger bigInteger1, bigInteger2 = paramZTauElement.u.multiply(paramZTauElement.u);
/*     */ 
/*     */     
/*  90 */     BigInteger bigInteger3 = paramZTauElement.u.multiply(paramZTauElement.v);
/*     */ 
/*     */     
/*  93 */     BigInteger bigInteger4 = paramZTauElement.v.multiply(paramZTauElement.v).shiftLeft(1);
/*     */     
/*  95 */     if (paramByte == 1) {
/*     */       
/*  97 */       bigInteger1 = bigInteger2.add(bigInteger3).add(bigInteger4);
/*     */     }
/*  99 */     else if (paramByte == -1) {
/*     */       
/* 101 */       bigInteger1 = bigInteger2.subtract(bigInteger3).add(bigInteger4);
/*     */     }
/*     */     else {
/*     */       
/* 105 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     } 
/*     */     
/* 108 */     return bigInteger1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleBigDecimal norm(byte paramByte, SimpleBigDecimal paramSimpleBigDecimal1, SimpleBigDecimal paramSimpleBigDecimal2) {
/* 129 */     SimpleBigDecimal simpleBigDecimal1, simpleBigDecimal2 = paramSimpleBigDecimal1.multiply(paramSimpleBigDecimal1);
/*     */ 
/*     */     
/* 132 */     SimpleBigDecimal simpleBigDecimal3 = paramSimpleBigDecimal1.multiply(paramSimpleBigDecimal2);
/*     */ 
/*     */     
/* 135 */     SimpleBigDecimal simpleBigDecimal4 = paramSimpleBigDecimal2.multiply(paramSimpleBigDecimal2).shiftLeft(1);
/*     */     
/* 137 */     if (paramByte == 1) {
/*     */       
/* 139 */       simpleBigDecimal1 = simpleBigDecimal2.add(simpleBigDecimal3).add(simpleBigDecimal4);
/*     */     }
/* 141 */     else if (paramByte == -1) {
/*     */       
/* 143 */       simpleBigDecimal1 = simpleBigDecimal2.subtract(simpleBigDecimal3).add(simpleBigDecimal4);
/*     */     }
/*     */     else {
/*     */       
/* 147 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     } 
/*     */     
/* 150 */     return simpleBigDecimal1;
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
/*     */ 
/*     */   
/*     */   public static ZTauElement round(SimpleBigDecimal paramSimpleBigDecimal1, SimpleBigDecimal paramSimpleBigDecimal2, byte paramByte) {
/*     */     SimpleBigDecimal simpleBigDecimal6, simpleBigDecimal7;
/* 169 */     int i = paramSimpleBigDecimal1.getScale();
/* 170 */     if (paramSimpleBigDecimal2.getScale() != i)
/*     */     {
/* 172 */       throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
/*     */     }
/*     */ 
/*     */     
/* 176 */     if (paramByte != 1 && paramByte != -1)
/*     */     {
/* 178 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     }
/*     */     
/* 181 */     BigInteger bigInteger1 = paramSimpleBigDecimal1.round();
/* 182 */     BigInteger bigInteger2 = paramSimpleBigDecimal2.round();
/*     */     
/* 184 */     SimpleBigDecimal simpleBigDecimal1 = paramSimpleBigDecimal1.subtract(bigInteger1);
/* 185 */     SimpleBigDecimal simpleBigDecimal2 = paramSimpleBigDecimal2.subtract(bigInteger2);
/*     */ 
/*     */     
/* 188 */     SimpleBigDecimal simpleBigDecimal3 = simpleBigDecimal1.add(simpleBigDecimal1);
/* 189 */     if (paramByte == 1) {
/*     */       
/* 191 */       simpleBigDecimal3 = simpleBigDecimal3.add(simpleBigDecimal2);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 196 */       simpleBigDecimal3 = simpleBigDecimal3.subtract(simpleBigDecimal2);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 201 */     SimpleBigDecimal simpleBigDecimal4 = simpleBigDecimal2.add(simpleBigDecimal2).add(simpleBigDecimal2);
/* 202 */     SimpleBigDecimal simpleBigDecimal5 = simpleBigDecimal4.add(simpleBigDecimal2);
/*     */ 
/*     */     
/* 205 */     if (paramByte == 1) {
/*     */       
/* 207 */       simpleBigDecimal6 = simpleBigDecimal1.subtract(simpleBigDecimal4);
/* 208 */       simpleBigDecimal7 = simpleBigDecimal1.add(simpleBigDecimal5);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 213 */       simpleBigDecimal6 = simpleBigDecimal1.add(simpleBigDecimal4);
/* 214 */       simpleBigDecimal7 = simpleBigDecimal1.subtract(simpleBigDecimal5);
/*     */     } 
/*     */     
/* 217 */     byte b1 = 0;
/* 218 */     byte b2 = 0;
/*     */ 
/*     */     
/* 221 */     if (simpleBigDecimal3.compareTo(ECConstants.ONE) >= 0) {
/*     */       
/* 223 */       if (simpleBigDecimal6.compareTo(MINUS_ONE) < 0)
/*     */       {
/* 225 */         b2 = paramByte;
/*     */       }
/*     */       else
/*     */       {
/* 229 */         b1 = 1;
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 235 */     else if (simpleBigDecimal7.compareTo(ECConstants.TWO) >= 0) {
/*     */       
/* 237 */       b2 = paramByte;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 242 */     if (simpleBigDecimal3.compareTo(MINUS_ONE) < 0) {
/*     */       
/* 244 */       if (simpleBigDecimal6.compareTo(ECConstants.ONE) >= 0)
/*     */       {
/* 246 */         b2 = (byte)-paramByte;
/*     */       }
/*     */       else
/*     */       {
/* 250 */         b1 = -1;
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 256 */     else if (simpleBigDecimal7.compareTo(MINUS_TWO) < 0) {
/*     */       
/* 258 */       b2 = (byte)-paramByte;
/*     */     } 
/*     */ 
/*     */     
/* 262 */     BigInteger bigInteger3 = bigInteger1.add(BigInteger.valueOf(b1));
/* 263 */     BigInteger bigInteger4 = bigInteger2.add(BigInteger.valueOf(b2));
/* 264 */     return new ZTauElement(bigInteger3, bigInteger4);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleBigDecimal approximateDivisionByN(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, byte paramByte, int paramInt1, int paramInt2) {
/* 286 */     int i = (paramInt1 + 5) / 2 + paramInt2;
/* 287 */     BigInteger bigInteger1 = paramBigInteger1.shiftRight(paramInt1 - i - 2 + paramByte);
/*     */     
/* 289 */     BigInteger bigInteger2 = paramBigInteger2.multiply(bigInteger1);
/*     */     
/* 291 */     BigInteger bigInteger3 = bigInteger2.shiftRight(paramInt1);
/*     */     
/* 293 */     BigInteger bigInteger4 = paramBigInteger3.multiply(bigInteger3);
/*     */     
/* 295 */     BigInteger bigInteger5 = bigInteger2.add(bigInteger4);
/* 296 */     BigInteger bigInteger6 = bigInteger5.shiftRight(i - paramInt2);
/* 297 */     if (bigInteger5.testBit(i - paramInt2 - 1))
/*     */     {
/*     */       
/* 300 */       bigInteger6 = bigInteger6.add(ECConstants.ONE);
/*     */     }
/*     */     
/* 303 */     return new SimpleBigDecimal(bigInteger6, paramInt2);
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
/*     */   public static byte[] tauAdicNaf(byte paramByte, ZTauElement paramZTauElement) {
/* 316 */     if (paramByte != 1 && paramByte != -1)
/*     */     {
/* 318 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     }
/*     */     
/* 321 */     BigInteger bigInteger1 = norm(paramByte, paramZTauElement);
/*     */ 
/*     */     
/* 324 */     int i = bigInteger1.bitLength();
/*     */ 
/*     */     
/* 327 */     boolean bool = (i > 30) ? (i + 4) : true;
/*     */ 
/*     */     
/* 330 */     byte[] arrayOfByte1 = new byte[bool];
/* 331 */     byte b1 = 0;
/*     */ 
/*     */     
/* 334 */     byte b2 = 0;
/*     */     
/* 336 */     BigInteger bigInteger2 = paramZTauElement.u;
/* 337 */     BigInteger bigInteger3 = paramZTauElement.v;
/*     */     
/* 339 */     while (!bigInteger2.equals(ECConstants.ZERO) || !bigInteger3.equals(ECConstants.ZERO)) {
/*     */ 
/*     */       
/* 342 */       if (bigInteger2.testBit(0)) {
/*     */         
/* 344 */         arrayOfByte1[b1] = (byte)ECConstants.TWO.subtract(bigInteger2.subtract(bigInteger3.shiftLeft(1)).mod(ECConstants.FOUR)).intValue();
/*     */ 
/*     */         
/* 347 */         if (arrayOfByte1[b1] == 1) {
/*     */           
/* 349 */           bigInteger2 = bigInteger2.clearBit(0);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 354 */           bigInteger2 = bigInteger2.add(ECConstants.ONE);
/*     */         } 
/* 356 */         b2 = b1;
/*     */       }
/*     */       else {
/*     */         
/* 360 */         arrayOfByte1[b1] = 0;
/*     */       } 
/*     */       
/* 363 */       BigInteger bigInteger4 = bigInteger2;
/* 364 */       BigInteger bigInteger5 = bigInteger2.shiftRight(1);
/* 365 */       if (paramByte == 1) {
/*     */         
/* 367 */         bigInteger2 = bigInteger3.add(bigInteger5);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 372 */         bigInteger2 = bigInteger3.subtract(bigInteger5);
/*     */       } 
/*     */       
/* 375 */       bigInteger3 = bigInteger4.shiftRight(1).negate();
/* 376 */       b1++;
/*     */     } 
/*     */     
/* 379 */     b2++;
/*     */ 
/*     */     
/* 382 */     byte[] arrayOfByte2 = new byte[b2];
/* 383 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, b2);
/* 384 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECPoint.AbstractF2m tau(ECPoint.AbstractF2m paramAbstractF2m) {
/* 395 */     return paramAbstractF2m.tau();
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
/*     */   public static byte getMu(ECCurve.AbstractF2m paramAbstractF2m) {
/* 410 */     if (!paramAbstractF2m.isKoblitz())
/*     */     {
/* 412 */       throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
/*     */     }
/*     */     
/* 415 */     if (paramAbstractF2m.getA().isZero())
/*     */     {
/* 417 */       return -1;
/*     */     }
/*     */     
/* 420 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getMu(ECFieldElement paramECFieldElement) {
/* 425 */     return (byte)(paramECFieldElement.isZero() ? -1 : 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getMu(int paramInt) {
/* 430 */     return (byte)((paramInt == 0) ? -1 : 1);
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
/*     */   
/*     */   public static BigInteger[] getLucas(byte paramByte, int paramInt, boolean paramBoolean) {
/*     */     BigInteger bigInteger1;
/*     */     BigInteger bigInteger2;
/* 449 */     if (paramByte != 1 && paramByte != -1)
/*     */     {
/* 451 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     if (paramBoolean) {
/*     */       
/* 460 */       bigInteger1 = ECConstants.TWO;
/* 461 */       bigInteger2 = BigInteger.valueOf(paramByte);
/*     */     }
/*     */     else {
/*     */       
/* 465 */       bigInteger1 = ECConstants.ZERO;
/* 466 */       bigInteger2 = ECConstants.ONE;
/*     */     } 
/*     */     
/* 469 */     for (byte b = 1; b < paramInt; b++) {
/*     */ 
/*     */       
/* 472 */       BigInteger bigInteger4 = null;
/* 473 */       if (paramByte == 1) {
/*     */         
/* 475 */         bigInteger4 = bigInteger2;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 480 */         bigInteger4 = bigInteger2.negate();
/*     */       } 
/*     */       
/* 483 */       BigInteger bigInteger3 = bigInteger4.subtract(bigInteger1.shiftLeft(1));
/* 484 */       bigInteger1 = bigInteger2;
/* 485 */       bigInteger2 = bigInteger3;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 490 */     return new BigInteger[] { bigInteger1, bigInteger2 };
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
/*     */   public static BigInteger getTw(byte paramByte, int paramInt) {
/* 504 */     if (paramInt == 4) {
/*     */       
/* 506 */       if (paramByte == 1)
/*     */       {
/* 508 */         return BigInteger.valueOf(6L);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 513 */       return BigInteger.valueOf(10L);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 519 */     BigInteger[] arrayOfBigInteger = getLucas(paramByte, paramInt, false);
/* 520 */     BigInteger bigInteger1 = ECConstants.ZERO.setBit(paramInt);
/* 521 */     BigInteger bigInteger2 = arrayOfBigInteger[1].modInverse(bigInteger1);
/*     */     
/* 523 */     return ECConstants.TWO.multiply(arrayOfBigInteger[0]).multiply(bigInteger2).mod(bigInteger1);
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
/*     */   
/*     */   public static BigInteger[] getSi(ECCurve.AbstractF2m paramAbstractF2m) {
/* 540 */     if (!paramAbstractF2m.isKoblitz())
/*     */     {
/* 542 */       throw new IllegalArgumentException("si is defined for Koblitz curves only");
/*     */     }
/*     */     
/* 545 */     int i = paramAbstractF2m.getFieldSize();
/* 546 */     int j = paramAbstractF2m.getA().toBigInteger().intValue();
/* 547 */     byte b = getMu(j);
/* 548 */     int k = getShiftsForCofactor(paramAbstractF2m.getCofactor());
/* 549 */     int m = i + 3 - j;
/* 550 */     BigInteger[] arrayOfBigInteger = getLucas(b, m, false);
/* 551 */     if (b == 1) {
/*     */       
/* 553 */       arrayOfBigInteger[0] = arrayOfBigInteger[0].negate();
/* 554 */       arrayOfBigInteger[1] = arrayOfBigInteger[1].negate();
/*     */     } 
/*     */     
/* 557 */     BigInteger bigInteger1 = ECConstants.ONE.add(arrayOfBigInteger[1]).shiftRight(k);
/* 558 */     BigInteger bigInteger2 = ECConstants.ONE.add(arrayOfBigInteger[0]).shiftRight(k).negate();
/*     */     
/* 560 */     return new BigInteger[] { bigInteger1, bigInteger2 };
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger[] getSi(int paramInt1, int paramInt2, BigInteger paramBigInteger) {
/* 565 */     byte b = getMu(paramInt2);
/* 566 */     int i = getShiftsForCofactor(paramBigInteger);
/* 567 */     int j = paramInt1 + 3 - paramInt2;
/* 568 */     BigInteger[] arrayOfBigInteger = getLucas(b, j, false);
/* 569 */     if (b == 1) {
/*     */       
/* 571 */       arrayOfBigInteger[0] = arrayOfBigInteger[0].negate();
/* 572 */       arrayOfBigInteger[1] = arrayOfBigInteger[1].negate();
/*     */     } 
/*     */     
/* 575 */     BigInteger bigInteger1 = ECConstants.ONE.add(arrayOfBigInteger[1]).shiftRight(i);
/* 576 */     BigInteger bigInteger2 = ECConstants.ONE.add(arrayOfBigInteger[0]).shiftRight(i).negate();
/*     */     
/* 578 */     return new BigInteger[] { bigInteger1, bigInteger2 };
/*     */   }
/*     */ 
/*     */   
/*     */   protected static int getShiftsForCofactor(BigInteger paramBigInteger) {
/* 583 */     if (paramBigInteger != null) {
/*     */       
/* 585 */       if (paramBigInteger.equals(ECConstants.TWO))
/*     */       {
/* 587 */         return 1;
/*     */       }
/* 589 */       if (paramBigInteger.equals(ECConstants.FOUR))
/*     */       {
/* 591 */         return 2;
/*     */       }
/*     */     } 
/*     */     
/* 595 */     throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZTauElement partModReduction(BigInteger paramBigInteger, int paramInt, byte paramByte1, BigInteger[] paramArrayOfBigInteger, byte paramByte2, byte paramByte3) {
/*     */     BigInteger bigInteger1;
/* 616 */     if (paramByte2 == 1) {
/*     */       
/* 618 */       bigInteger1 = paramArrayOfBigInteger[0].add(paramArrayOfBigInteger[1]);
/*     */     }
/*     */     else {
/*     */       
/* 622 */       bigInteger1 = paramArrayOfBigInteger[0].subtract(paramArrayOfBigInteger[1]);
/*     */     } 
/*     */     
/* 625 */     BigInteger[] arrayOfBigInteger = getLucas(paramByte2, paramInt, true);
/* 626 */     BigInteger bigInteger2 = arrayOfBigInteger[1];
/*     */     
/* 628 */     SimpleBigDecimal simpleBigDecimal1 = approximateDivisionByN(paramBigInteger, paramArrayOfBigInteger[0], bigInteger2, paramByte1, paramInt, paramByte3);
/*     */ 
/*     */     
/* 631 */     SimpleBigDecimal simpleBigDecimal2 = approximateDivisionByN(paramBigInteger, paramArrayOfBigInteger[1], bigInteger2, paramByte1, paramInt, paramByte3);
/*     */ 
/*     */     
/* 634 */     ZTauElement zTauElement = round(simpleBigDecimal1, simpleBigDecimal2, paramByte2);
/*     */ 
/*     */     
/* 637 */     BigInteger bigInteger3 = paramBigInteger.subtract(bigInteger1.multiply(zTauElement.u)).subtract(
/* 638 */         BigInteger.valueOf(2L).multiply(paramArrayOfBigInteger[1]).multiply(zTauElement.v));
/*     */ 
/*     */     
/* 641 */     BigInteger bigInteger4 = paramArrayOfBigInteger[1].multiply(zTauElement.u).subtract(paramArrayOfBigInteger[0].multiply(zTauElement.v));
/*     */     
/* 643 */     return new ZTauElement(bigInteger3, bigInteger4);
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
/*     */   public static ECPoint.AbstractF2m multiplyRTnaf(ECPoint.AbstractF2m paramAbstractF2m, BigInteger paramBigInteger) {
/* 656 */     ECCurve.AbstractF2m abstractF2m = (ECCurve.AbstractF2m)paramAbstractF2m.getCurve();
/* 657 */     int i = abstractF2m.getFieldSize();
/* 658 */     int j = abstractF2m.getA().toBigInteger().intValue();
/* 659 */     byte b = getMu(j);
/* 660 */     BigInteger[] arrayOfBigInteger = abstractF2m.getSi();
/* 661 */     ZTauElement zTauElement = partModReduction(paramBigInteger, i, (byte)j, arrayOfBigInteger, b, (byte)10);
/*     */     
/* 663 */     return multiplyTnaf(paramAbstractF2m, zTauElement);
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
/*     */   public static ECPoint.AbstractF2m multiplyTnaf(ECPoint.AbstractF2m paramAbstractF2m, ZTauElement paramZTauElement) {
/* 677 */     ECCurve.AbstractF2m abstractF2m = (ECCurve.AbstractF2m)paramAbstractF2m.getCurve();
/* 678 */     byte b = getMu(abstractF2m.getA());
/* 679 */     byte[] arrayOfByte = tauAdicNaf(b, paramZTauElement);
/*     */     
/* 681 */     return multiplyFromTnaf(paramAbstractF2m, arrayOfByte);
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
/*     */   public static ECPoint.AbstractF2m multiplyFromTnaf(ECPoint.AbstractF2m paramAbstractF2m, byte[] paramArrayOfbyte) {
/* 697 */     ECCurve eCCurve = paramAbstractF2m.getCurve();
/* 698 */     ECPoint.AbstractF2m abstractF2m1 = (ECPoint.AbstractF2m)eCCurve.getInfinity();
/* 699 */     ECPoint.AbstractF2m abstractF2m2 = (ECPoint.AbstractF2m)paramAbstractF2m.negate();
/* 700 */     byte b = 0;
/* 701 */     for (int i = paramArrayOfbyte.length - 1; i >= 0; i--) {
/*     */       
/* 703 */       b++;
/* 704 */       byte b1 = paramArrayOfbyte[i];
/* 705 */       if (b1 != 0) {
/*     */         
/* 707 */         abstractF2m1 = abstractF2m1.tauPow(b);
/* 708 */         b = 0;
/*     */         
/* 710 */         ECPoint.AbstractF2m abstractF2m = (b1 > 0) ? paramAbstractF2m : abstractF2m2;
/* 711 */         abstractF2m1 = (ECPoint.AbstractF2m)abstractF2m1.add((ECPoint)abstractF2m);
/*     */       } 
/*     */     } 
/* 714 */     if (b > 0)
/*     */     {
/* 716 */       abstractF2m1 = abstractF2m1.tauPow(b);
/*     */     }
/* 718 */     return abstractF2m1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] tauAdicWNaf(byte paramByte1, ZTauElement paramZTauElement, byte paramByte2, BigInteger paramBigInteger1, BigInteger paramBigInteger2, ZTauElement[] paramArrayOfZTauElement) {
/* 738 */     if (paramByte1 != 1 && paramByte1 != -1)
/*     */     {
/* 740 */       throw new IllegalArgumentException("mu must be 1 or -1");
/*     */     }
/*     */     
/* 743 */     BigInteger bigInteger1 = norm(paramByte1, paramZTauElement);
/*     */ 
/*     */     
/* 746 */     int i = bigInteger1.bitLength();
/*     */ 
/*     */     
/* 749 */     int j = (i > 30) ? (i + 4 + paramByte2) : (34 + paramByte2);
/*     */ 
/*     */     
/* 752 */     byte[] arrayOfByte = new byte[j];
/*     */ 
/*     */     
/* 755 */     BigInteger bigInteger2 = paramBigInteger1.shiftRight(1);
/*     */ 
/*     */     
/* 758 */     BigInteger bigInteger3 = paramZTauElement.u;
/* 759 */     BigInteger bigInteger4 = paramZTauElement.v;
/* 760 */     byte b = 0;
/*     */ 
/*     */     
/* 763 */     while (!bigInteger3.equals(ECConstants.ZERO) || !bigInteger4.equals(ECConstants.ZERO)) {
/*     */ 
/*     */       
/* 766 */       if (bigInteger3.testBit(0)) {
/*     */         byte b1;
/*     */ 
/*     */         
/* 770 */         BigInteger bigInteger5 = bigInteger3.add(bigInteger4.multiply(paramBigInteger2)).mod(paramBigInteger1);
/*     */ 
/*     */ 
/*     */         
/* 774 */         if (bigInteger5.compareTo(bigInteger2) >= 0) {
/*     */           
/* 776 */           b1 = (byte)bigInteger5.subtract(paramBigInteger1).intValue();
/*     */         }
/*     */         else {
/*     */           
/* 780 */           b1 = (byte)bigInteger5.intValue();
/*     */         } 
/*     */ 
/*     */         
/* 784 */         arrayOfByte[b] = b1;
/* 785 */         boolean bool = true;
/* 786 */         if (b1 < 0) {
/*     */           
/* 788 */           bool = false;
/* 789 */           b1 = (byte)-b1;
/*     */         } 
/*     */ 
/*     */         
/* 793 */         if (bool)
/*     */         {
/* 795 */           bigInteger3 = bigInteger3.subtract((paramArrayOfZTauElement[b1]).u);
/* 796 */           bigInteger4 = bigInteger4.subtract((paramArrayOfZTauElement[b1]).v);
/*     */         }
/*     */         else
/*     */         {
/* 800 */           bigInteger3 = bigInteger3.add((paramArrayOfZTauElement[b1]).u);
/* 801 */           bigInteger4 = bigInteger4.add((paramArrayOfZTauElement[b1]).v);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 806 */         arrayOfByte[b] = 0;
/*     */       } 
/*     */       
/* 809 */       BigInteger bigInteger = bigInteger3;
/*     */       
/* 811 */       if (paramByte1 == 1) {
/*     */         
/* 813 */         bigInteger3 = bigInteger4.add(bigInteger3.shiftRight(1));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 818 */         bigInteger3 = bigInteger4.subtract(bigInteger3.shiftRight(1));
/*     */       } 
/* 820 */       bigInteger4 = bigInteger.shiftRight(1).negate();
/* 821 */       b++;
/*     */     } 
/* 823 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECPoint.AbstractF2m[] getPreComp(ECPoint.AbstractF2m paramAbstractF2m, byte paramByte) {
/* 834 */     byte[][] arrayOfByte = (paramByte == 0) ? alpha0Tnaf : alpha1Tnaf;
/*     */     
/* 836 */     ECPoint.AbstractF2m[] arrayOfAbstractF2m = new ECPoint.AbstractF2m[arrayOfByte.length + 1 >>> 1];
/* 837 */     arrayOfAbstractF2m[0] = paramAbstractF2m;
/*     */     
/* 839 */     int i = arrayOfByte.length;
/* 840 */     for (byte b = 3; b < i; b += 2)
/*     */     {
/* 842 */       arrayOfAbstractF2m[b >>> 1] = multiplyFromTnaf(paramAbstractF2m, arrayOfByte[b]);
/*     */     }
/*     */     
/* 845 */     paramAbstractF2m.getCurve().normalizeAll((ECPoint[])arrayOfAbstractF2m);
/*     */     
/* 847 */     return arrayOfAbstractF2m;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/Tnaf.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */