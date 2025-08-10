/*     */ package META-INF.versions.9.org.bouncycastle.util;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BigIntegers
/*     */ {
/*  14 */   public static final BigInteger ZERO = BigInteger.valueOf(0L);
/*  15 */   public static final BigInteger ONE = BigInteger.valueOf(1L);
/*  16 */   public static final BigInteger TWO = BigInteger.valueOf(2L);
/*     */   
/*  18 */   private static final BigInteger THREE = BigInteger.valueOf(3L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_ITERATIONS = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] asUnsignedByteArray(BigInteger paramBigInteger) {
/*  31 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/*     */     
/*  33 */     if (arrayOfByte[0] == 0 && arrayOfByte.length != 1) {
/*     */       
/*  35 */       byte[] arrayOfByte1 = new byte[arrayOfByte.length - 1];
/*     */       
/*  37 */       System.arraycopy(arrayOfByte, 1, arrayOfByte1, 0, arrayOfByte1.length);
/*     */       
/*  39 */       return arrayOfByte1;
/*     */     } 
/*     */     
/*  42 */     return arrayOfByte;
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
/*     */   public static byte[] asUnsignedByteArray(int paramInt, BigInteger paramBigInteger) {
/*  57 */     byte[] arrayOfByte1 = paramBigInteger.toByteArray();
/*  58 */     if (arrayOfByte1.length == paramInt)
/*     */     {
/*  60 */       return arrayOfByte1;
/*     */     }
/*     */     
/*  63 */     byte b = (arrayOfByte1[0] == 0 && arrayOfByte1.length != 1) ? 1 : 0;
/*  64 */     int i = arrayOfByte1.length - b;
/*     */     
/*  66 */     if (i > paramInt)
/*     */     {
/*  68 */       throw new IllegalArgumentException("standard length exceeded for value");
/*     */     }
/*     */     
/*  71 */     byte[] arrayOfByte2 = new byte[paramInt];
/*  72 */     System.arraycopy(arrayOfByte1, b, arrayOfByte2, arrayOfByte2.length - i, i);
/*  73 */     return arrayOfByte2;
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
/*     */   public static void asUnsignedByteArray(BigInteger paramBigInteger, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  91 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/*  92 */     if (arrayOfByte.length == paramInt2) {
/*     */       
/*  94 */       System.arraycopy(arrayOfByte, 0, paramArrayOfbyte, paramInt1, paramInt2);
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     byte b = (arrayOfByte[0] == 0 && arrayOfByte.length != 1) ? 1 : 0;
/*  99 */     int i = arrayOfByte.length - b;
/*     */     
/* 101 */     if (i > paramInt2)
/*     */     {
/* 103 */       throw new IllegalArgumentException("standard length exceeded for value");
/*     */     }
/*     */     
/* 106 */     int j = paramInt2 - i;
/* 107 */     Arrays.fill(paramArrayOfbyte, paramInt1, paramInt1 + j, (byte)0);
/* 108 */     System.arraycopy(arrayOfByte, b, paramArrayOfbyte, paramInt1 + j, i);
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
/*     */   public static BigInteger createRandomInRange(BigInteger paramBigInteger1, BigInteger paramBigInteger2, SecureRandom paramSecureRandom) {
/* 124 */     int i = paramBigInteger1.compareTo(paramBigInteger2);
/* 125 */     if (i >= 0) {
/*     */       
/* 127 */       if (i > 0)
/*     */       {
/* 129 */         throw new IllegalArgumentException("'min' may not be greater than 'max'");
/*     */       }
/*     */       
/* 132 */       return paramBigInteger1;
/*     */     } 
/*     */     
/* 135 */     if (paramBigInteger1.bitLength() > paramBigInteger2.bitLength() / 2)
/*     */     {
/* 137 */       return createRandomInRange(ZERO, paramBigInteger2.subtract(paramBigInteger1), paramSecureRandom).add(paramBigInteger1);
/*     */     }
/*     */     
/* 140 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 142 */       BigInteger bigInteger = createRandomBigInteger(paramBigInteger2.bitLength(), paramSecureRandom);
/* 143 */       if (bigInteger.compareTo(paramBigInteger1) >= 0 && bigInteger.compareTo(paramBigInteger2) <= 0)
/*     */       {
/* 145 */         return bigInteger;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 150 */     return createRandomBigInteger(paramBigInteger2.subtract(paramBigInteger1).bitLength() - 1, paramSecureRandom).add(paramBigInteger1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger fromUnsignedByteArray(byte[] paramArrayOfbyte) {
/* 155 */     return new BigInteger(1, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger fromUnsignedByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 160 */     byte[] arrayOfByte = paramArrayOfbyte;
/* 161 */     if (paramInt1 != 0 || paramInt2 != paramArrayOfbyte.length) {
/*     */       
/* 163 */       arrayOfByte = new byte[paramInt2];
/* 164 */       System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
/*     */     } 
/* 166 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intValueExact(BigInteger paramBigInteger) {
/* 172 */     if (paramBigInteger.bitLength() > 31)
/*     */     {
/* 174 */       throw new ArithmeticException("BigInteger out of int range");
/*     */     }
/*     */     
/* 177 */     return paramBigInteger.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long longValueExact(BigInteger paramBigInteger) {
/* 183 */     if (paramBigInteger.bitLength() > 63)
/*     */     {
/* 185 */       throw new ArithmeticException("BigInteger out of long range");
/*     */     }
/*     */     
/* 188 */     return paramBigInteger.longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger modOddInverse(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 193 */     if (!paramBigInteger1.testBit(0))
/*     */     {
/* 195 */       throw new IllegalArgumentException("'M' must be odd");
/*     */     }
/* 197 */     if (paramBigInteger1.signum() != 1)
/*     */     {
/* 199 */       throw new ArithmeticException("BigInteger: modulus not positive");
/*     */     }
/* 201 */     if (paramBigInteger2.signum() < 0 || paramBigInteger2.compareTo(paramBigInteger1) >= 0)
/*     */     {
/* 203 */       paramBigInteger2 = paramBigInteger2.mod(paramBigInteger1);
/*     */     }
/*     */     
/* 206 */     int i = paramBigInteger1.bitLength();
/* 207 */     int[] arrayOfInt1 = Nat.fromBigInteger(i, paramBigInteger1);
/* 208 */     int[] arrayOfInt2 = Nat.fromBigInteger(i, paramBigInteger2);
/* 209 */     int j = arrayOfInt1.length;
/* 210 */     int[] arrayOfInt3 = Nat.create(j);
/* 211 */     if (0 == Mod.modOddInverse(arrayOfInt1, arrayOfInt2, arrayOfInt3))
/*     */     {
/* 213 */       throw new ArithmeticException("BigInteger not invertible.");
/*     */     }
/* 215 */     return Nat.toBigInteger(j, arrayOfInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger modOddInverseVar(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 220 */     if (!paramBigInteger1.testBit(0))
/*     */     {
/* 222 */       throw new IllegalArgumentException("'M' must be odd");
/*     */     }
/* 224 */     if (paramBigInteger1.signum() != 1)
/*     */     {
/* 226 */       throw new ArithmeticException("BigInteger: modulus not positive");
/*     */     }
/* 228 */     if (paramBigInteger1.equals(ONE))
/*     */     {
/* 230 */       return ZERO;
/*     */     }
/* 232 */     if (paramBigInteger2.signum() < 0 || paramBigInteger2.compareTo(paramBigInteger1) >= 0)
/*     */     {
/* 234 */       paramBigInteger2 = paramBigInteger2.mod(paramBigInteger1);
/*     */     }
/* 236 */     if (paramBigInteger2.equals(ONE))
/*     */     {
/* 238 */       return ONE;
/*     */     }
/*     */     
/* 241 */     int i = paramBigInteger1.bitLength();
/* 242 */     int[] arrayOfInt1 = Nat.fromBigInteger(i, paramBigInteger1);
/* 243 */     int[] arrayOfInt2 = Nat.fromBigInteger(i, paramBigInteger2);
/* 244 */     int j = arrayOfInt1.length;
/* 245 */     int[] arrayOfInt3 = Nat.create(j);
/* 246 */     if (!Mod.modOddInverseVar(arrayOfInt1, arrayOfInt2, arrayOfInt3))
/*     */     {
/* 248 */       throw new ArithmeticException("BigInteger not invertible.");
/*     */     }
/* 250 */     return Nat.toBigInteger(j, arrayOfInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getUnsignedByteLength(BigInteger paramBigInteger) {
/* 255 */     if (paramBigInteger.equals(ZERO))
/*     */     {
/* 257 */       return 1;
/*     */     }
/*     */     
/* 260 */     return (paramBigInteger.bitLength() + 7) / 8;
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
/*     */   public static BigInteger createRandomBigInteger(int paramInt, SecureRandom paramSecureRandom) {
/* 272 */     return new BigInteger(1, createRandom(paramInt, paramSecureRandom));
/*     */   }
/*     */ 
/*     */   
/* 276 */   private static final BigInteger SMALL_PRIMES_PRODUCT = new BigInteger("8138e8a0fcf3a4e84a771d40fd305d7f4aa59306d7251de54d98af8fe95729a1f73d893fa424cd2edc8636a6c3285e022b0e3866a565ae8108eed8591cd4fe8d2ce86165a978d719ebf647f362d33fca29cd179fb42401cbaf3df0c614056f9c8f3cfd51e474afb6bc6974f78db8aba8e9e517fded658591ab7502bd41849462f", 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 282 */   private static final int MAX_SMALL = BigInteger.valueOf(743L).bitLength();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigInteger createRandomPrime(int paramInt1, int paramInt2, SecureRandom paramSecureRandom) {
/*     */     BigInteger bigInteger;
/* 293 */     if (paramInt1 < 2)
/*     */     {
/* 295 */       throw new IllegalArgumentException("bitLength < 2");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (paramInt1 == 2)
/*     */     {
/* 302 */       return (paramSecureRandom.nextInt() < 0) ? TWO : THREE;
/*     */     }
/*     */ 
/*     */     
/*     */     do {
/* 307 */       byte[] arrayOfByte = createRandom(paramInt1, paramSecureRandom);
/*     */       
/* 309 */       int i = 8 * arrayOfByte.length - paramInt1;
/* 310 */       byte b = (byte)(1 << 7 - i);
/*     */ 
/*     */       
/* 313 */       arrayOfByte[0] = (byte)(arrayOfByte[0] | b);
/* 314 */       arrayOfByte[arrayOfByte.length - 1] = (byte)(arrayOfByte[arrayOfByte.length - 1] | 0x1);
/*     */       
/* 316 */       bigInteger = new BigInteger(1, arrayOfByte);
/* 317 */       if (paramInt1 <= MAX_SMALL)
/*     */         continue; 
/* 319 */       while (!bigInteger.gcd(SMALL_PRIMES_PRODUCT).equals(ONE))
/*     */       {
/* 321 */         bigInteger = bigInteger.add(TWO);
/*     */       
/*     */       }
/*     */     }
/* 325 */     while (!bigInteger.isProbablePrime(paramInt2));
/*     */     
/* 327 */     return bigInteger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] createRandom(int paramInt, SecureRandom paramSecureRandom) throws IllegalArgumentException {
/* 333 */     if (paramInt < 1)
/*     */     {
/* 335 */       throw new IllegalArgumentException("bitLength must be at least 1");
/*     */     }
/*     */     
/* 338 */     int i = (paramInt + 7) / 8;
/*     */     
/* 340 */     byte[] arrayOfByte = new byte[i];
/*     */     
/* 342 */     paramSecureRandom.nextBytes(arrayOfByte);
/*     */ 
/*     */     
/* 345 */     int j = 8 * i - paramInt;
/* 346 */     arrayOfByte[0] = (byte)(arrayOfByte[0] & (byte)(255 >>> j));
/*     */     
/* 348 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/BigIntegers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */