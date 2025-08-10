/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat128;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP128R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  17 */   static final int[] P = new int[] { -1, -1, -1, -3 };
/*  18 */   private static final int[] PExt = new int[] { 1, 0, 0, 4, -2, -1, 3, -4 };
/*     */   
/*  20 */   private static final int[] PExtInv = new int[] { -1, -1, -1, -5, 1, 0, -4, 3 };
/*     */   
/*     */   private static final int P3s1 = 2147483646;
/*     */   
/*     */   private static final int PExt7s1 = 2147483646;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  27 */     int i = Nat128.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  28 */     if (i != 0 || (paramArrayOfint3[3] >>> 1 >= 2147483646 && Nat128.gte(paramArrayOfint3, P)))
/*     */     {
/*  30 */       addPInvTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  36 */     int i = Nat256.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  37 */     if (i != 0 || (paramArrayOfint3[7] >>> 1 >= 2147483646 && Nat256.gte(paramArrayOfint3, PExt)))
/*     */     {
/*  39 */       Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  45 */     int i = Nat.inc(4, paramArrayOfint1, paramArrayOfint2);
/*  46 */     if (i != 0 || (paramArrayOfint2[3] >>> 1 >= 2147483646 && Nat128.gte(paramArrayOfint2, P)))
/*     */     {
/*  48 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  54 */     int[] arrayOfInt = Nat128.fromBigInteger(paramBigInteger);
/*  55 */     if (arrayOfInt[3] >>> 1 >= 2147483646 && Nat128.gte(arrayOfInt, P))
/*     */     {
/*  57 */       Nat128.subFrom(P, arrayOfInt);
/*     */     }
/*  59 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  64 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  66 */       Nat.shiftDownBit(4, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  70 */       int i = Nat128.add(paramArrayOfint1, P, paramArrayOfint2);
/*  71 */       Nat.shiftDownBit(4, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  77 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  82 */     int i = 0;
/*  83 */     for (byte b = 0; b < 4; b++)
/*     */     {
/*  85 */       i |= paramArrayOfint[b];
/*     */     }
/*  87 */     i = i >>> 1 | i & 0x1;
/*  88 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  93 */     int[] arrayOfInt = Nat128.createExt();
/*  94 */     Nat128.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  95 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 100 */     int i = Nat128.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 101 */     if (i != 0 || (paramArrayOfint3[7] >>> 1 >= 2147483646 && Nat256.gte(paramArrayOfint3, PExt)))
/*     */     {
/* 103 */       Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 109 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 111 */       Nat128.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 115 */       Nat128.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 121 */     byte[] arrayOfByte = new byte[16];
/*     */     
/*     */     do {
/* 124 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 125 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 4);
/*     */     }
/* 127 */     while (0 == Nat.lessThan(4, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 134 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 136 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 141 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL, l2 = paramArrayOfint1[1] & 0xFFFFFFFFL, l3 = paramArrayOfint1[2] & 0xFFFFFFFFL, l4 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/* 142 */     long l5 = paramArrayOfint1[4] & 0xFFFFFFFFL, l6 = paramArrayOfint1[5] & 0xFFFFFFFFL, l7 = paramArrayOfint1[6] & 0xFFFFFFFFL, l8 = paramArrayOfint1[7] & 0xFFFFFFFFL;
/*     */     
/* 144 */     l4 += l8; l7 += l8 << 1L;
/* 145 */     l3 += l7; l6 += l7 << 1L;
/* 146 */     l2 += l6; l5 += l6 << 1L;
/* 147 */     l1 += l5; l4 += l5 << 1L;
/*     */     
/* 149 */     paramArrayOfint2[0] = (int)l1; l2 += l1 >>> 32L;
/* 150 */     paramArrayOfint2[1] = (int)l2; l3 += l2 >>> 32L;
/* 151 */     paramArrayOfint2[2] = (int)l3; l4 += l3 >>> 32L;
/* 152 */     paramArrayOfint2[3] = (int)l4;
/*     */     
/* 154 */     reduce32((int)(l4 >>> 32L), paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 159 */     while (paramInt != 0) {
/*     */       
/* 161 */       long l2 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 163 */       long l1 = (paramArrayOfint[0] & 0xFFFFFFFFL) + l2;
/* 164 */       paramArrayOfint[0] = (int)l1; l1 >>= 32L;
/* 165 */       if (l1 != 0L) {
/*     */         
/* 167 */         l1 += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 168 */         paramArrayOfint[1] = (int)l1; l1 >>= 32L;
/* 169 */         l1 += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 170 */         paramArrayOfint[2] = (int)l1; l1 >>= 32L;
/*     */       } 
/* 172 */       l1 += (paramArrayOfint[3] & 0xFFFFFFFFL) + (l2 << 1L);
/* 173 */       paramArrayOfint[3] = (int)l1; l1 >>= 32L;
/*     */ 
/*     */ 
/*     */       
/* 177 */       paramInt = (int)l1;
/*     */     } 
/*     */     
/* 180 */     if (paramArrayOfint[3] >>> 1 >= 2147483646 && Nat128.gte(paramArrayOfint, P))
/*     */     {
/* 182 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 188 */     int[] arrayOfInt = Nat128.createExt();
/* 189 */     Nat128.square(paramArrayOfint1, arrayOfInt);
/* 190 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 197 */     int[] arrayOfInt = Nat128.createExt();
/* 198 */     Nat128.square(paramArrayOfint1, arrayOfInt);
/* 199 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 201 */     while (--paramInt > 0) {
/*     */       
/* 203 */       Nat128.square(paramArrayOfint2, arrayOfInt);
/* 204 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 210 */     int i = Nat128.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 211 */     if (i != 0)
/*     */     {
/* 213 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 219 */     int i = Nat.sub(10, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 220 */     if (i != 0)
/*     */     {
/* 222 */       Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 228 */     int i = Nat.shiftUpBit(4, paramArrayOfint1, 0, paramArrayOfint2);
/* 229 */     if (i != 0 || (paramArrayOfint2[3] >>> 1 >= 2147483646 && Nat128.gte(paramArrayOfint2, P)))
/*     */     {
/* 231 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 237 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 238 */     paramArrayOfint[0] = (int)l; l >>= 32L;
/* 239 */     if (l != 0L) {
/*     */       
/* 241 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 242 */       paramArrayOfint[1] = (int)l; l >>= 32L;
/* 243 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 244 */       paramArrayOfint[2] = (int)l; l >>= 32L;
/*     */     } 
/* 246 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) + 2L;
/* 247 */     paramArrayOfint[3] = (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 252 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 253 */     paramArrayOfint[0] = (int)l; l >>= 32L;
/* 254 */     if (l != 0L) {
/*     */       
/* 256 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 257 */       paramArrayOfint[1] = (int)l; l >>= 32L;
/* 258 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 259 */       paramArrayOfint[2] = (int)l; l >>= 32L;
/*     */     } 
/* 261 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) - 2L;
/* 262 */     paramArrayOfint[3] = (int)l;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP128R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */