/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.djb;
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
/*     */ public class Curve25519Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { -19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE };
/*     */   
/*     */   private static final int P7 = 2147483647;
/*  19 */   private static final int[] PExt = new int[] { 361, 0, 0, 0, 0, 0, 0, 0, -19, -1, -1, -1, -1, -1, -1, 1073741823 };
/*     */ 
/*     */   
/*     */   private static final int PInv = 19;
/*     */ 
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  26 */     Nat256.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  27 */     if (Nat256.gte(paramArrayOfint3, P))
/*     */     {
/*  29 */       subPFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  35 */     Nat.add(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  36 */     if (Nat.gte(16, paramArrayOfint3, PExt))
/*     */     {
/*  38 */       subPExtFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  44 */     Nat.inc(8, paramArrayOfint1, paramArrayOfint2);
/*  45 */     if (Nat256.gte(paramArrayOfint2, P))
/*     */     {
/*  47 */       subPFrom(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  53 */     int[] arrayOfInt = Nat256.fromBigInteger(paramBigInteger);
/*  54 */     while (Nat256.gte(arrayOfInt, P))
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
/*  69 */       Nat256.add(paramArrayOfint1, P, paramArrayOfint2);
/*  70 */       Nat.shiftDownBit(8, paramArrayOfint2, 0);
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
/*  99 */     Nat256.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 100 */     if (Nat.gte(16, paramArrayOfint3, PExt))
/*     */     {
/* 102 */       subPExtFrom(paramArrayOfint3);
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
/* 125 */       paramArrayOfint[7] = paramArrayOfint[7] & Integer.MAX_VALUE;
/*     */     }
/* 127 */     while (0 == Nat.lessThan(8, paramArrayOfint, P));
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
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 143 */     int i = paramArrayOfint1[7];
/* 144 */     Nat.shiftUpBit(8, paramArrayOfint1, 8, i, paramArrayOfint2, 0);
/* 145 */     int j = Nat256.mulByWordAddTo(19, paramArrayOfint1, paramArrayOfint2) << 1;
/* 146 */     int k = paramArrayOfint2[7];
/* 147 */     j += (k >>> 31) - (i >>> 31);
/* 148 */     k &= Integer.MAX_VALUE;
/* 149 */     k += Nat.addWordTo(7, j * 19, paramArrayOfint2);
/* 150 */     paramArrayOfint2[7] = k;
/* 151 */     if (Nat256.gte(paramArrayOfint2, P))
/*     */     {
/* 153 */       subPFrom(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reduce27(int paramInt, int[] paramArrayOfint) {
/* 161 */     int i = paramArrayOfint[7];
/* 162 */     int j = paramInt << 1 | i >>> 31;
/* 163 */     i &= Integer.MAX_VALUE;
/* 164 */     i += Nat.addWordTo(7, j * 19, paramArrayOfint);
/* 165 */     paramArrayOfint[7] = i;
/* 166 */     if (Nat256.gte(paramArrayOfint, P))
/*     */     {
/* 168 */       subPFrom(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 174 */     int[] arrayOfInt = Nat256.createExt();
/* 175 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 176 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 183 */     int[] arrayOfInt = Nat256.createExt();
/* 184 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 185 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 187 */     while (--paramInt > 0) {
/*     */       
/* 189 */       Nat256.square(paramArrayOfint2, arrayOfInt);
/* 190 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 196 */     int i = Nat256.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 197 */     if (i != 0)
/*     */     {
/* 199 */       addPTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 205 */     int i = Nat.sub(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 206 */     if (i != 0)
/*     */     {
/* 208 */       addPExtTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 214 */     Nat.shiftUpBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/* 215 */     if (Nat256.gte(paramArrayOfint2, P))
/*     */     {
/* 217 */       subPFrom(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static int addPTo(int[] paramArrayOfint) {
/* 223 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 19L;
/* 224 */     paramArrayOfint[0] = (int)l;
/* 225 */     l >>= 32L;
/* 226 */     if (l != 0L)
/*     */     {
/* 228 */       l = Nat.decAt(7, paramArrayOfint, 1);
/*     */     }
/* 230 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) + 2147483648L;
/* 231 */     paramArrayOfint[7] = (int)l;
/* 232 */     l >>= 32L;
/* 233 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int addPExtTo(int[] paramArrayOfint) {
/* 238 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + (PExt[0] & 0xFFFFFFFFL);
/* 239 */     paramArrayOfint[0] = (int)l;
/* 240 */     l >>= 32L;
/* 241 */     if (l != 0L)
/*     */     {
/* 243 */       l = Nat.incAt(8, paramArrayOfint, 1);
/*     */     }
/* 245 */     l += (paramArrayOfint[8] & 0xFFFFFFFFL) - 19L;
/* 246 */     paramArrayOfint[8] = (int)l;
/* 247 */     l >>= 32L;
/* 248 */     if (l != 0L)
/*     */     {
/* 250 */       l = Nat.decAt(15, paramArrayOfint, 9);
/*     */     }
/* 252 */     l += (paramArrayOfint[15] & 0xFFFFFFFFL) + ((PExt[15] + 1) & 0xFFFFFFFFL);
/* 253 */     paramArrayOfint[15] = (int)l;
/* 254 */     l >>= 32L;
/* 255 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int subPFrom(int[] paramArrayOfint) {
/* 260 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 19L;
/* 261 */     paramArrayOfint[0] = (int)l;
/* 262 */     l >>= 32L;
/* 263 */     if (l != 0L)
/*     */     {
/* 265 */       l = Nat.incAt(7, paramArrayOfint, 1);
/*     */     }
/* 267 */     l += (paramArrayOfint[7] & 0xFFFFFFFFL) - 2147483648L;
/* 268 */     paramArrayOfint[7] = (int)l;
/* 269 */     l >>= 32L;
/* 270 */     return (int)l;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int subPExtFrom(int[] paramArrayOfint) {
/* 275 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - (PExt[0] & 0xFFFFFFFFL);
/* 276 */     paramArrayOfint[0] = (int)l;
/* 277 */     l >>= 32L;
/* 278 */     if (l != 0L)
/*     */     {
/* 280 */       l = Nat.decAt(8, paramArrayOfint, 1);
/*     */     }
/* 282 */     l += (paramArrayOfint[8] & 0xFFFFFFFFL) + 19L;
/* 283 */     paramArrayOfint[8] = (int)l;
/* 284 */     l >>= 32L;
/* 285 */     if (l != 0L)
/*     */     {
/* 287 */       l = Nat.incAt(15, paramArrayOfint, 9);
/*     */     }
/* 289 */     l += (paramArrayOfint[15] & 0xFFFFFFFFL) - ((PExt[15] + 1) & 0xFFFFFFFFL);
/* 290 */     paramArrayOfint[15] = (int)l;
/* 291 */     l >>= 32L;
/* 292 */     return (int)l;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/djb/Curve25519Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */