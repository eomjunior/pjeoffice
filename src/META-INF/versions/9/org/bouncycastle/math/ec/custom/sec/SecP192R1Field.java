/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP192R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { -1, -1, -2, -1, -1, -1 };
/*  17 */   private static final int[] PExt = new int[] { 1, 0, 2, 0, 1, 0, -2, -1, -3, -1, -1, -1 };
/*     */   
/*  19 */   private static final int[] PExtInv = new int[] { -1, -1, -3, -1, -2, -1, 1, 0, 2 };
/*     */   
/*     */   private static final int P5 = -1;
/*     */   
/*     */   private static final int PExt11 = -1;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  26 */     int i = Nat192.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  27 */     if (i != 0 || (paramArrayOfint3[5] == -1 && Nat192.gte(paramArrayOfint3, P)))
/*     */     {
/*  29 */       addPInvTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  35 */     int i = Nat.add(12, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  36 */     if (i != 0 || (paramArrayOfint3[11] == -1 && Nat.gte(12, paramArrayOfint3, PExt)))
/*     */     {
/*  38 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  40 */         Nat.incAt(12, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  47 */     int i = Nat.inc(6, paramArrayOfint1, paramArrayOfint2);
/*  48 */     if (i != 0 || (paramArrayOfint2[5] == -1 && Nat192.gte(paramArrayOfint2, P)))
/*     */     {
/*  50 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  56 */     int[] arrayOfInt = Nat192.fromBigInteger(paramBigInteger);
/*  57 */     if (arrayOfInt[5] == -1 && Nat192.gte(arrayOfInt, P))
/*     */     {
/*  59 */       Nat192.subFrom(P, arrayOfInt);
/*     */     }
/*  61 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  66 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  68 */       Nat.shiftDownBit(6, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  72 */       int i = Nat192.add(paramArrayOfint1, P, paramArrayOfint2);
/*  73 */       Nat.shiftDownBit(6, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  79 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  84 */     int i = 0;
/*  85 */     for (byte b = 0; b < 6; b++)
/*     */     {
/*  87 */       i |= paramArrayOfint[b];
/*     */     }
/*  89 */     i = i >>> 1 | i & 0x1;
/*  90 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  95 */     int[] arrayOfInt = Nat192.createExt();
/*  96 */     Nat192.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  97 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 102 */     int i = Nat192.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 103 */     if (i != 0 || (paramArrayOfint3[11] == -1 && Nat.gte(12, paramArrayOfint3, PExt)))
/*     */     {
/* 105 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 107 */         Nat.incAt(12, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 114 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 116 */       Nat192.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 120 */       Nat192.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 126 */     byte[] arrayOfByte = new byte[24];
/*     */     
/*     */     do {
/* 129 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 130 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 6);
/*     */     }
/* 132 */     while (0 == Nat.lessThan(6, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 139 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 141 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 146 */     long l1 = paramArrayOfint1[6] & 0xFFFFFFFFL, l2 = paramArrayOfint1[7] & 0xFFFFFFFFL, l3 = paramArrayOfint1[8] & 0xFFFFFFFFL;
/* 147 */     long l4 = paramArrayOfint1[9] & 0xFFFFFFFFL, l5 = paramArrayOfint1[10] & 0xFFFFFFFFL, l6 = paramArrayOfint1[11] & 0xFFFFFFFFL;
/*     */     
/* 149 */     long l7 = l1 + l5;
/* 150 */     long l8 = l2 + l6;
/*     */     
/* 152 */     long l9 = 0L;
/* 153 */     l9 += (paramArrayOfint1[0] & 0xFFFFFFFFL) + l7;
/* 154 */     int i = (int)l9;
/* 155 */     l9 >>= 32L;
/* 156 */     l9 += (paramArrayOfint1[1] & 0xFFFFFFFFL) + l8;
/* 157 */     paramArrayOfint2[1] = (int)l9;
/* 158 */     l9 >>= 32L;
/*     */     
/* 160 */     l7 += l3;
/* 161 */     l8 += l4;
/*     */     
/* 163 */     l9 += (paramArrayOfint1[2] & 0xFFFFFFFFL) + l7;
/* 164 */     long l10 = l9 & 0xFFFFFFFFL;
/* 165 */     l9 >>= 32L;
/* 166 */     l9 += (paramArrayOfint1[3] & 0xFFFFFFFFL) + l8;
/* 167 */     paramArrayOfint2[3] = (int)l9;
/* 168 */     l9 >>= 32L;
/*     */     
/* 170 */     l7 -= l1;
/* 171 */     l8 -= l2;
/*     */     
/* 173 */     l9 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + l7;
/* 174 */     paramArrayOfint2[4] = (int)l9;
/* 175 */     l9 >>= 32L;
/* 176 */     l9 += (paramArrayOfint1[5] & 0xFFFFFFFFL) + l8;
/* 177 */     paramArrayOfint2[5] = (int)l9;
/* 178 */     l9 >>= 32L;
/*     */     
/* 180 */     l10 += l9;
/*     */     
/* 182 */     l9 += i & 0xFFFFFFFFL;
/* 183 */     paramArrayOfint2[0] = (int)l9;
/* 184 */     l9 >>= 32L;
/* 185 */     if (l9 != 0L) {
/*     */       
/* 187 */       l9 += paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 188 */       paramArrayOfint2[1] = (int)l9;
/* 189 */       l10 += l9 >> 32L;
/*     */     } 
/* 191 */     paramArrayOfint2[2] = (int)l10;
/* 192 */     l9 = l10 >> 32L;
/*     */ 
/*     */ 
/*     */     
/* 196 */     if ((l9 != 0L && Nat.incAt(6, paramArrayOfint2, 3) != 0) || (paramArrayOfint2[5] == -1 && 
/* 197 */       Nat192.gte(paramArrayOfint2, P)))
/*     */     {
/* 199 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 205 */     long l = 0L;
/*     */     
/* 207 */     if (paramInt != 0) {
/*     */       
/* 209 */       long l1 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 211 */       l += (paramArrayOfint[0] & 0xFFFFFFFFL) + l1;
/* 212 */       paramArrayOfint[0] = (int)l;
/* 213 */       l >>= 32L;
/* 214 */       if (l != 0L) {
/*     */         
/* 216 */         l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 217 */         paramArrayOfint[1] = (int)l;
/* 218 */         l >>= 32L;
/*     */       } 
/* 220 */       l += (paramArrayOfint[2] & 0xFFFFFFFFL) + l1;
/* 221 */       paramArrayOfint[2] = (int)l;
/* 222 */       l >>= 32L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 227 */     if ((l != 0L && Nat.incAt(6, paramArrayOfint, 3) != 0) || (paramArrayOfint[5] == -1 && 
/* 228 */       Nat192.gte(paramArrayOfint, P)))
/*     */     {
/* 230 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 236 */     int[] arrayOfInt = Nat192.createExt();
/* 237 */     Nat192.square(paramArrayOfint1, arrayOfInt);
/* 238 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 245 */     int[] arrayOfInt = Nat192.createExt();
/* 246 */     Nat192.square(paramArrayOfint1, arrayOfInt);
/* 247 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 249 */     while (--paramInt > 0) {
/*     */       
/* 251 */       Nat192.square(paramArrayOfint2, arrayOfInt);
/* 252 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 258 */     int i = Nat192.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 259 */     if (i != 0)
/*     */     {
/* 261 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 267 */     int i = Nat.sub(12, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 268 */     if (i != 0)
/*     */     {
/* 270 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 272 */         Nat.decAt(12, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 279 */     int i = Nat.shiftUpBit(6, paramArrayOfint1, 0, paramArrayOfint2);
/* 280 */     if (i != 0 || (paramArrayOfint2[5] == -1 && Nat192.gte(paramArrayOfint2, P)))
/*     */     {
/* 282 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 288 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 289 */     paramArrayOfint[0] = (int)l;
/* 290 */     l >>= 32L;
/* 291 */     if (l != 0L) {
/*     */       
/* 293 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 294 */       paramArrayOfint[1] = (int)l;
/* 295 */       l >>= 32L;
/*     */     } 
/* 297 */     l += (paramArrayOfint[2] & 0xFFFFFFFFL) + 1L;
/* 298 */     paramArrayOfint[2] = (int)l;
/* 299 */     l >>= 32L;
/* 300 */     if (l != 0L)
/*     */     {
/* 302 */       Nat.incAt(6, paramArrayOfint, 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 308 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 309 */     paramArrayOfint[0] = (int)l;
/* 310 */     l >>= 32L;
/* 311 */     if (l != 0L) {
/*     */       
/* 313 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 314 */       paramArrayOfint[1] = (int)l;
/* 315 */       l >>= 32L;
/*     */     } 
/* 317 */     l += (paramArrayOfint[2] & 0xFFFFFFFFL) - 1L;
/* 318 */     paramArrayOfint[2] = (int)l;
/* 319 */     l >>= 32L;
/* 320 */     if (l != 0L)
/*     */     {
/* 322 */       Nat.decAt(6, paramArrayOfint, 3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */