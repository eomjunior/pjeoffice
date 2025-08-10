/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.gm;
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
/*     */ public class SM2P256V1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { -1, -1, 0, -1, -1, -1, -1, -2 };
/*     */   
/*  18 */   private static final int[] PExt = new int[] { 1, 0, -2, 1, 1, -2, 0, 2, -2, -3, 3, -2, -1, -1, 0, -2 };
/*     */   
/*     */   private static final int P7s1 = 2147483647;
/*     */   
/*     */   private static final int PExt15s1 = 2147483647;
/*     */ 
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  26 */     int i = Nat256.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  27 */     if (i != 0 || (paramArrayOfint3[7] >>> 1 >= Integer.MAX_VALUE && Nat256.gte(paramArrayOfint3, P)))
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
/*  45 */     if (i != 0 || (paramArrayOfint2[7] >>> 1 >= Integer.MAX_VALUE && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/*  47 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  53 */     int[] arrayOfInt = Nat256.fromBigInteger(paramBigInteger);
/*  54 */     if (arrayOfInt[7] >>> 1 >= Integer.MAX_VALUE && Nat256.gte(arrayOfInt, P))
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
/* 143 */     long l9 = l1 + l2;
/* 144 */     long l10 = l3 + l4;
/* 145 */     long l11 = l5 + l8;
/* 146 */     long l12 = l6 + l7;
/* 147 */     long l13 = l12 + (l8 << 1L);
/*     */     
/* 149 */     long l14 = l9 + l12;
/* 150 */     long l15 = l10 + l11 + l14;
/*     */     
/* 152 */     long l16 = 0L;
/* 153 */     l16 += (paramArrayOfint1[0] & 0xFFFFFFFFL) + l15 + l6 + l7 + l8;
/* 154 */     paramArrayOfint2[0] = (int)l16;
/* 155 */     l16 >>= 32L;
/* 156 */     l16 += (paramArrayOfint1[1] & 0xFFFFFFFFL) + l15 - l1 + l7 + l8;
/* 157 */     paramArrayOfint2[1] = (int)l16;
/* 158 */     l16 >>= 32L;
/* 159 */     l16 += (paramArrayOfint1[2] & 0xFFFFFFFFL) - l14;
/* 160 */     paramArrayOfint2[2] = (int)l16;
/* 161 */     l16 >>= 32L;
/* 162 */     l16 += (paramArrayOfint1[3] & 0xFFFFFFFFL) + l15 - l2 - l3 + l6;
/* 163 */     paramArrayOfint2[3] = (int)l16;
/* 164 */     l16 >>= 32L;
/* 165 */     l16 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + l15 - l10 - l1 + l7;
/* 166 */     paramArrayOfint2[4] = (int)l16;
/* 167 */     l16 >>= 32L;
/* 168 */     l16 += (paramArrayOfint1[5] & 0xFFFFFFFFL) + l13 + l3;
/* 169 */     paramArrayOfint2[5] = (int)l16;
/* 170 */     l16 >>= 32L;
/* 171 */     l16 += (paramArrayOfint1[6] & 0xFFFFFFFFL) + l4 + l7 + l8;
/* 172 */     paramArrayOfint2[6] = (int)l16;
/* 173 */     l16 >>= 32L;
/* 174 */     l16 += (paramArrayOfint1[7] & 0xFFFFFFFFL) + l15 + l13 + l5;
/* 175 */     paramArrayOfint2[7] = (int)l16;
/* 176 */     l16 >>= 32L;
/*     */ 
/*     */ 
/*     */     
/* 180 */     reduce32((int)l16, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 185 */     long l = 0L;
/*     */     
/* 187 */     if (paramInt != 0) {
/*     */       
/* 189 */       long l1 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 191 */       l += (paramArrayOfint[0] & 0xFFFFFFFFL) + l1;
/* 192 */       paramArrayOfint[0] = (int)l;
/* 193 */       l >>= 32L;
/* 194 */       if (l != 0L) {
/*     */         
/* 196 */         l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 197 */         paramArrayOfint[1] = (int)l;
/* 198 */         l >>= 32L;
/*     */       } 
/* 200 */       l += (paramArrayOfint[2] & 0xFFFFFFFFL) - l1;
/* 201 */       paramArrayOfint[2] = (int)l;
/* 202 */       l >>= 32L;
/* 203 */       l += (paramArrayOfint[3] & 0xFFFFFFFFL) + l1;
/* 204 */       paramArrayOfint[3] = (int)l;
/* 205 */       l >>= 32L;
/* 206 */       if (l != 0L) {
/*     */         
/* 208 */         l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 209 */         paramArrayOfint[4] = (int)l;
/* 210 */         l >>= 32L;
/* 211 */         l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 212 */         paramArrayOfint[5] = (int)l;
/* 213 */         l >>= 32L;
/* 214 */         l += paramArrayOfint[6] & 0xFFFFFFFFL;
/* 215 */         paramArrayOfint[6] = (int)l;
/* 216 */         l >>= 32L;
/*     */       } 
/* 218 */       l += (paramArrayOfint[7] & 0xFFFFFFFFL) + l1;
/* 219 */       paramArrayOfint[7] = (int)l;
/* 220 */       l >>= 32L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 225 */     if (l != 0L || (paramArrayOfint[7] >>> 1 >= Integer.MAX_VALUE && Nat256.gte(paramArrayOfint, P)))
/*     */     {
/* 227 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 233 */     int[] arrayOfInt = Nat256.createExt();
/* 234 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 235 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 242 */     int[] arrayOfInt = Nat256.createExt();
/* 243 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 244 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 246 */     while (--paramInt > 0) {
/*     */       
/* 248 */       Nat256.square(paramArrayOfint2, arrayOfInt);
/* 249 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 255 */     int i = Nat256.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 256 */     if (i != 0)
/*     */     {
/* 258 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 264 */     int i = Nat.sub(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 265 */     if (i != 0)
/*     */     {
/* 267 */       Nat.addTo(16, PExt, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 273 */     int i = Nat.shiftUpBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/* 274 */     if (i != 0 || (paramArrayOfint2[7] >>> 1 >= Integer.MAX_VALUE && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/* 276 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 282 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 283 */     paramArrayOfint[0] = (int)l;
/* 284 */     l >>= 32L;
/* 285 */     if (l != 0L) {
/*     */       
/* 287 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 288 */       paramArrayOfint[1] = (int)l;
/* 289 */       l >>= 32L;
/*     */     } 
/* 291 */     l += (paramArrayOfint[2] & 0xFFFFFFFFL) - 1L;
/* 292 */     paramArrayOfint[2] = (int)l;
/* 293 */     l >>= 32L;
/* 294 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) + 1L;
/* 295 */     paramArrayOfint[3] = (int)l;
/* 296 */     l >>= 32L;
/* 297 */     if (l != 0L) {
/*     */       
/* 299 */       l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 300 */       paramArrayOfint[4] = (int)l;
/* 301 */       l >>= 32L;
/* 302 */       l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 303 */       paramArrayOfint[5] = (int)l;
/* 304 */       l >>= 32L;
/* 305 */       l += paramArrayOfint[6] & 0xFFFFFFFFL;
/* 306 */       paramArrayOfint[6] = (int)l;
/* 307 */       l >>= 32L;
/*     */     } 
/* 309 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) + 1L;
/* 310 */     paramArrayOfint[7] = (int)l;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 316 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 317 */     paramArrayOfint[0] = (int)l;
/* 318 */     l >>= 32L;
/* 319 */     if (l != 0L) {
/*     */       
/* 321 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 322 */       paramArrayOfint[1] = (int)l;
/* 323 */       l >>= 32L;
/*     */     } 
/* 325 */     l += (paramArrayOfint[2] & 0xFFFFFFFFL) + 1L;
/* 326 */     paramArrayOfint[2] = (int)l;
/* 327 */     l >>= 32L;
/* 328 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) - 1L;
/* 329 */     paramArrayOfint[3] = (int)l;
/* 330 */     l >>= 32L;
/* 331 */     if (l != 0L) {
/*     */       
/* 333 */       l += paramArrayOfint[4] & 0xFFFFFFFFL;
/* 334 */       paramArrayOfint[4] = (int)l;
/* 335 */       l >>= 32L;
/* 336 */       l += paramArrayOfint[5] & 0xFFFFFFFFL;
/* 337 */       paramArrayOfint[5] = (int)l;
/* 338 */       l >>= 32L;
/* 339 */       l += paramArrayOfint[6] & 0xFFFFFFFFL;
/* 340 */       paramArrayOfint[6] = (int)l;
/* 341 */       l >>= 32L;
/*     */     } 
/* 343 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) - 1L;
/* 344 */     paramArrayOfint[7] = (int)l;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/gm/SM2P256V1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */