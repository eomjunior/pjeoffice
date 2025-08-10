/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP256R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { -1, -1, -1, 0, 0, 0, 1, -1 };
/*     */   
/*  18 */   private static final int[] PExt = new int[] { 1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2 };
/*     */   
/*     */   private static final int P7 = -1;
/*     */   
/*     */   private static final int PExt15s1 = 2147483647;
/*     */ 
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  26 */     int i = Nat256.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  27 */     if (i != 0 || (paramArrayOfint3[7] == -1 && Nat256.gte(paramArrayOfint3, P)))
/*     */     {
/*  29 */       addPInvTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  35 */     int i = Nat.add(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  36 */     if (i != 0 || (paramArrayOfint3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, paramArrayOfint3, PExt)))
/*     */     {
/*  38 */       Nat.subFrom(16, PExt, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  44 */     int i = Nat.inc(8, paramArrayOfint1, paramArrayOfint2);
/*  45 */     if (i != 0 || (paramArrayOfint2[7] == -1 && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/*  47 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  53 */     int[] arrayOfInt = Nat256.fromBigInteger(paramBigInteger);
/*  54 */     if (arrayOfInt[7] == -1 && Nat256.gte(arrayOfInt, P))
/*     */     {
/*  56 */       Nat256.subFrom(P, arrayOfInt);
/*     */     }
/*  58 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  63 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  65 */       Nat.shiftDownBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  69 */       int i = Nat256.add(paramArrayOfint1, P, paramArrayOfint2);
/*  70 */       Nat.shiftDownBit(8, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  76 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  81 */     int i = 0;
/*  82 */     for (byte b = 0; b < 8; b++)
/*     */     {
/*  84 */       i |= paramArrayOfint[b];
/*     */     }
/*  86 */     i = i >>> 1 | i & 0x1;
/*  87 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  92 */     int[] arrayOfInt = Nat256.createExt();
/*  93 */     Nat256.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  94 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  99 */     int i = Nat256.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 100 */     if (i != 0 || (paramArrayOfint3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, paramArrayOfint3, PExt)))
/*     */     {
/* 102 */       Nat.subFrom(16, PExt, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 108 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 110 */       Nat256.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 114 */       Nat256.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 120 */     byte[] arrayOfByte = new byte[32];
/*     */     
/*     */     do {
/* 123 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 124 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 8);
/*     */     }
/* 126 */     while (0 == Nat.lessThan(8, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 133 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 135 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 140 */     long l1 = paramArrayOfint1[8] & 0xFFFFFFFFL, l2 = paramArrayOfint1[9] & 0xFFFFFFFFL, l3 = paramArrayOfint1[10] & 0xFFFFFFFFL, l4 = paramArrayOfint1[11] & 0xFFFFFFFFL;
/* 141 */     long l5 = paramArrayOfint1[12] & 0xFFFFFFFFL, l6 = paramArrayOfint1[13] & 0xFFFFFFFFL, l7 = paramArrayOfint1[14] & 0xFFFFFFFFL, l8 = paramArrayOfint1[15] & 0xFFFFFFFFL;
/*     */ 
/*     */ 
/*     */     
/* 145 */     l1 -= 6L;
/*     */     
/* 147 */     long l9 = l1 + l2;
/* 148 */     long l10 = l2 + l3;
/* 149 */     long l11 = l3 + l4 - l8;
/* 150 */     long l12 = l4 + l5;
/* 151 */     long l13 = l5 + l6;
/* 152 */     long l14 = l6 + l7;
/* 153 */     long l15 = l7 + l8;
/* 154 */     long l16 = l14 - l9;
/*     */     
/* 156 */     long l17 = 0L;
/* 157 */     l17 += (paramArrayOfint1[0] & 0xFFFFFFFFL) - l12 - l16;
/* 158 */     paramArrayOfint2[0] = (int)l17;
/* 159 */     l17 >>= 32L;
/* 160 */     l17 += (paramArrayOfint1[1] & 0xFFFFFFFFL) + l10 - l13 - l15;
/* 161 */     paramArrayOfint2[1] = (int)l17;
/* 162 */     l17 >>= 32L;
/* 163 */     l17 += (paramArrayOfint1[2] & 0xFFFFFFFFL) + l11 - l14;
/* 164 */     paramArrayOfint2[2] = (int)l17;
/* 165 */     l17 >>= 32L;
/* 166 */     l17 += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (l12 << 1L) + l16 - l15;
/* 167 */     paramArrayOfint2[3] = (int)l17;
/* 168 */     l17 >>= 32L;
/* 169 */     l17 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (l13 << 1L) + l7 - l10;
/* 170 */     paramArrayOfint2[4] = (int)l17;
/* 171 */     l17 >>= 32L;
/* 172 */     l17 += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (l14 << 1L) - l11;
/* 173 */     paramArrayOfint2[5] = (int)l17;
/* 174 */     l17 >>= 32L;
/* 175 */     l17 += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (l15 << 1L) + l16;
/* 176 */     paramArrayOfint2[6] = (int)l17;
/* 177 */     l17 >>= 32L;
/* 178 */     l17 += (paramArrayOfint1[7] & 0xFFFFFFFFL) + (l8 << 1L) + l1 - l11 - l13;
/* 179 */     paramArrayOfint2[7] = (int)l17;
/* 180 */     l17 >>= 32L;
/* 181 */     l17 += 6L;
/*     */ 
/*     */ 
/*     */     
/* 185 */     reduce32((int)l17, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 190 */     long l = 0L;
/*     */     
/* 192 */     if (paramInt != 0) {
/*     */       
/* 194 */       long l1 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 196 */       l += (paramArrayOfint[0] & 0xFFFFFFFFL) + l1;
/* 197 */       paramArrayOfint[0] = (int)l;
/* 198 */       l >>= 32L;
/* 199 */       if (l != 0L) {
/*     */         
/* 201 */         l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 202 */         paramArrayOfint[1] = (int)l;
/* 203 */         l >>= 32L;
/* 204 */         l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 205 */         paramArrayOfint[2] = (int)l;
/* 206 */         l >>= 32L;
/*     */       } 
/* 208 */       l += (paramArrayOfint[3] & 0xFFFFFFFFL) - l1;
/* 209 */       paramArrayOfint[3] = (int)l;
/* 210 */       l >>= 32L;
/* 211 */       if (l != 0L) {
/*     */         
/* 213 */         l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 214 */         paramArrayOfint[4] = (int)l;
/* 215 */         l >>= 32L;
/* 216 */         l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 217 */         paramArrayOfint[5] = (int)l;
/* 218 */         l >>= 32L;
/*     */       } 
/* 220 */       l += (paramArrayOfint[6] & 0xFFFFFFFFL) - l1;
/* 221 */       paramArrayOfint[6] = (int)l;
/* 222 */       l >>= 32L;
/* 223 */       l += (paramArrayOfint[7] & 0xFFFFFFFFL) + l1;
/* 224 */       paramArrayOfint[7] = (int)l;
/* 225 */       l >>= 32L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 230 */     if (l != 0L || (paramArrayOfint[7] == -1 && Nat256.gte(paramArrayOfint, P)))
/*     */     {
/* 232 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 238 */     int[] arrayOfInt = Nat256.createExt();
/* 239 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 240 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 247 */     int[] arrayOfInt = Nat256.createExt();
/* 248 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 249 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 251 */     while (--paramInt > 0) {
/*     */       
/* 253 */       Nat256.square(paramArrayOfint2, arrayOfInt);
/* 254 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 260 */     int i = Nat256.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 261 */     if (i != 0)
/*     */     {
/* 263 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 269 */     int i = Nat.sub(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 270 */     if (i != 0)
/*     */     {
/* 272 */       Nat.addTo(16, PExt, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 278 */     int i = Nat.shiftUpBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/* 279 */     if (i != 0 || (paramArrayOfint2[7] == -1 && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/* 281 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 287 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 288 */     paramArrayOfint[0] = (int)l;
/* 289 */     l >>= 32L;
/* 290 */     if (l != 0L) {
/*     */       
/* 292 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 293 */       paramArrayOfint[1] = (int)l;
/* 294 */       l >>= 32L;
/* 295 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 296 */       paramArrayOfint[2] = (int)l;
/* 297 */       l >>= 32L;
/*     */     } 
/* 299 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) - 1L;
/* 300 */     paramArrayOfint[3] = (int)l;
/* 301 */     l >>= 32L;
/* 302 */     if (l != 0L) {
/*     */       
/* 304 */       l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 305 */       paramArrayOfint[4] = (int)l;
/* 306 */       l >>= 32L;
/* 307 */       l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 308 */       paramArrayOfint[5] = (int)l;
/* 309 */       l >>= 32L;
/*     */     } 
/* 311 */     l += (paramArrayOfint[6] & 0xFFFFFFFFL) - 1L;
/* 312 */     paramArrayOfint[6] = (int)l;
/* 313 */     l >>= 32L;
/* 314 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) + 1L;
/* 315 */     paramArrayOfint[7] = (int)l;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 321 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 322 */     paramArrayOfint[0] = (int)l;
/* 323 */     l >>= 32L;
/* 324 */     if (l != 0L) {
/*     */       
/* 326 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 327 */       paramArrayOfint[1] = (int)l;
/* 328 */       l >>= 32L;
/* 329 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 330 */       paramArrayOfint[2] = (int)l;
/* 331 */       l >>= 32L;
/*     */     } 
/* 333 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) + 1L;
/* 334 */     paramArrayOfint[3] = (int)l;
/* 335 */     l >>= 32L;
/* 336 */     if (l != 0L) {
/*     */       
/* 338 */       l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 339 */       paramArrayOfint[4] = (int)l;
/* 340 */       l >>= 32L;
/* 341 */       l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 342 */       paramArrayOfint[5] = (int)l;
/* 343 */       l >>= 32L;
/*     */     } 
/* 345 */     l += (paramArrayOfint[6] & 0xFFFFFFFFL) + 1L;
/* 346 */     paramArrayOfint[6] = (int)l;
/* 347 */     l >>= 32L;
/* 348 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) - 1L;
/* 349 */     paramArrayOfint[7] = (int)l;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */