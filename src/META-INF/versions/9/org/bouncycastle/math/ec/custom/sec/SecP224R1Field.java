/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat224;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP224R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { 1, 0, 0, -1, -1, -1, -1 };
/*     */   
/*  18 */   private static final int[] PExt = new int[] { 1, 0, 0, -2, -1, -1, 0, 2, 0, 0, -2, -1, -1, -1 };
/*     */   
/*  20 */   private static final int[] PExtInv = new int[] { -1, -1, -1, 1, 0, 0, -1, -3, -1, -1, 1 };
/*     */   
/*     */   private static final int P6 = -1;
/*     */   
/*     */   private static final int PExt13 = -1;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  27 */     int i = Nat224.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  28 */     if (i != 0 || (paramArrayOfint3[6] == -1 && Nat224.gte(paramArrayOfint3, P)))
/*     */     {
/*  30 */       addPInvTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  36 */     int i = Nat.add(14, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  37 */     if (i != 0 || (paramArrayOfint3[13] == -1 && Nat.gte(14, paramArrayOfint3, PExt)))
/*     */     {
/*  39 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  41 */         Nat.incAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  48 */     int i = Nat.inc(7, paramArrayOfint1, paramArrayOfint2);
/*  49 */     if (i != 0 || (paramArrayOfint2[6] == -1 && Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/*  51 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  57 */     int[] arrayOfInt = Nat224.fromBigInteger(paramBigInteger);
/*  58 */     if (arrayOfInt[6] == -1 && Nat224.gte(arrayOfInt, P))
/*     */     {
/*  60 */       Nat224.subFrom(P, arrayOfInt);
/*     */     }
/*  62 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  67 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  69 */       Nat.shiftDownBit(7, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       int i = Nat224.add(paramArrayOfint1, P, paramArrayOfint2);
/*  74 */       Nat.shiftDownBit(7, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  80 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  85 */     int i = 0;
/*  86 */     for (byte b = 0; b < 7; b++)
/*     */     {
/*  88 */       i |= paramArrayOfint[b];
/*     */     }
/*  90 */     i = i >>> 1 | i & 0x1;
/*  91 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  96 */     int[] arrayOfInt = Nat224.createExt();
/*  97 */     Nat224.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  98 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 103 */     int i = Nat224.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 104 */     if (i != 0 || (paramArrayOfint3[13] == -1 && Nat.gte(14, paramArrayOfint3, PExt)))
/*     */     {
/* 106 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 108 */         Nat.incAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 115 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 117 */       Nat224.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 121 */       Nat224.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 127 */     byte[] arrayOfByte = new byte[28];
/*     */     
/*     */     do {
/* 130 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 131 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 7);
/*     */     }
/* 133 */     while (0 == Nat.lessThan(7, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 140 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 142 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 147 */     long l1 = paramArrayOfint1[10] & 0xFFFFFFFFL, l2 = paramArrayOfint1[11] & 0xFFFFFFFFL, l3 = paramArrayOfint1[12] & 0xFFFFFFFFL, l4 = paramArrayOfint1[13] & 0xFFFFFFFFL;
/*     */ 
/*     */ 
/*     */     
/* 151 */     long l5 = (paramArrayOfint1[7] & 0xFFFFFFFFL) + l2 - 1L;
/* 152 */     long l6 = (paramArrayOfint1[8] & 0xFFFFFFFFL) + l3;
/* 153 */     long l7 = (paramArrayOfint1[9] & 0xFFFFFFFFL) + l4;
/*     */     
/* 155 */     long l8 = 0L;
/* 156 */     l8 += (paramArrayOfint1[0] & 0xFFFFFFFFL) - l5;
/* 157 */     long l9 = l8 & 0xFFFFFFFFL;
/* 158 */     l8 >>= 32L;
/* 159 */     l8 += (paramArrayOfint1[1] & 0xFFFFFFFFL) - l6;
/* 160 */     paramArrayOfint2[1] = (int)l8;
/* 161 */     l8 >>= 32L;
/* 162 */     l8 += (paramArrayOfint1[2] & 0xFFFFFFFFL) - l7;
/* 163 */     paramArrayOfint2[2] = (int)l8;
/* 164 */     l8 >>= 32L;
/* 165 */     l8 += (paramArrayOfint1[3] & 0xFFFFFFFFL) + l5 - l1;
/* 166 */     long l10 = l8 & 0xFFFFFFFFL;
/* 167 */     l8 >>= 32L;
/* 168 */     l8 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + l6 - l2;
/* 169 */     paramArrayOfint2[4] = (int)l8;
/* 170 */     l8 >>= 32L;
/* 171 */     l8 += (paramArrayOfint1[5] & 0xFFFFFFFFL) + l7 - l3;
/* 172 */     paramArrayOfint2[5] = (int)l8;
/* 173 */     l8 >>= 32L;
/* 174 */     l8 += (paramArrayOfint1[6] & 0xFFFFFFFFL) + l1 - l4;
/* 175 */     paramArrayOfint2[6] = (int)l8;
/* 176 */     l8 >>= 32L;
/* 177 */     l8++;
/*     */ 
/*     */ 
/*     */     
/* 181 */     l10 += l8;
/*     */     
/* 183 */     l9 -= l8;
/* 184 */     paramArrayOfint2[0] = (int)l9;
/* 185 */     l8 = l9 >> 32L;
/* 186 */     if (l8 != 0L) {
/*     */       
/* 188 */       l8 += paramArrayOfint2[1] & 0xFFFFFFFFL;
/* 189 */       paramArrayOfint2[1] = (int)l8;
/* 190 */       l8 >>= 32L;
/* 191 */       l8 += paramArrayOfint2[2] & 0xFFFFFFFFL;
/* 192 */       paramArrayOfint2[2] = (int)l8;
/* 193 */       l10 += l8 >> 32L;
/*     */     } 
/* 195 */     paramArrayOfint2[3] = (int)l10;
/* 196 */     l8 = l10 >> 32L;
/*     */ 
/*     */ 
/*     */     
/* 200 */     if ((l8 != 0L && Nat.incAt(7, paramArrayOfint2, 4) != 0) || (paramArrayOfint2[6] == -1 && 
/* 201 */       Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/* 203 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 209 */     long l = 0L;
/*     */     
/* 211 */     if (paramInt != 0) {
/*     */       
/* 213 */       long l1 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 215 */       l += (paramArrayOfint[0] & 0xFFFFFFFFL) - l1;
/* 216 */       paramArrayOfint[0] = (int)l;
/* 217 */       l >>= 32L;
/* 218 */       if (l != 0L) {
/*     */         
/* 220 */         l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 221 */         paramArrayOfint[1] = (int)l;
/* 222 */         l >>= 32L;
/* 223 */         l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 224 */         paramArrayOfint[2] = (int)l;
/* 225 */         l >>= 32L;
/*     */       } 
/* 227 */       l += (paramArrayOfint[3] & 0xFFFFFFFFL) + l1;
/* 228 */       paramArrayOfint[3] = (int)l;
/* 229 */       l >>= 32L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 234 */     if ((l != 0L && Nat.incAt(7, paramArrayOfint, 4) != 0) || (paramArrayOfint[6] == -1 && 
/* 235 */       Nat224.gte(paramArrayOfint, P)))
/*     */     {
/* 237 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 243 */     int[] arrayOfInt = Nat224.createExt();
/* 244 */     Nat224.square(paramArrayOfint1, arrayOfInt);
/* 245 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 252 */     int[] arrayOfInt = Nat224.createExt();
/* 253 */     Nat224.square(paramArrayOfint1, arrayOfInt);
/* 254 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 256 */     while (--paramInt > 0) {
/*     */       
/* 258 */       Nat224.square(paramArrayOfint2, arrayOfInt);
/* 259 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 265 */     int i = Nat224.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 266 */     if (i != 0)
/*     */     {
/* 268 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 274 */     int i = Nat.sub(14, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 275 */     if (i != 0)
/*     */     {
/* 277 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 279 */         Nat.decAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 286 */     int i = Nat.shiftUpBit(7, paramArrayOfint1, 0, paramArrayOfint2);
/* 287 */     if (i != 0 || (paramArrayOfint2[6] == -1 && Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/* 289 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 295 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 296 */     paramArrayOfint[0] = (int)l;
/* 297 */     l >>= 32L;
/* 298 */     if (l != 0L) {
/*     */       
/* 300 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 301 */       paramArrayOfint[1] = (int)l;
/* 302 */       l >>= 32L;
/* 303 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 304 */       paramArrayOfint[2] = (int)l;
/* 305 */       l >>= 32L;
/*     */     } 
/* 307 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) + 1L;
/* 308 */     paramArrayOfint[3] = (int)l;
/* 309 */     l >>= 32L;
/* 310 */     if (l != 0L)
/*     */     {
/* 312 */       Nat.incAt(7, paramArrayOfint, 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 318 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 319 */     paramArrayOfint[0] = (int)l;
/* 320 */     l >>= 32L;
/* 321 */     if (l != 0L) {
/*     */       
/* 323 */       l += paramArrayOfint[1] & 0xFFFFFFFFL;
/* 324 */       paramArrayOfint[1] = (int)l;
/* 325 */       l >>= 32L;
/* 326 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 327 */       paramArrayOfint[2] = (int)l;
/* 328 */       l >>= 32L;
/*     */     } 
/* 330 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) - 1L;
/* 331 */     paramArrayOfint[3] = (int)l;
/* 332 */     l >>= 32L;
/* 333 */     if (l != 0L)
/*     */     {
/* 335 */       Nat.decAt(7, paramArrayOfint, 4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP224R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */