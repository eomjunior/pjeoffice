/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat384;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP384R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { -1, 0, 0, -1, -2, -1, -1, -1, -1, -1, -1, -1 };
/*     */   
/*  18 */   private static final int[] PExt = new int[] { 1, -2, 0, 2, 0, -2, 0, 2, 1, 0, 0, 0, -2, 1, 0, -2, -3, -1, -1, -1, -1, -1, -1, -1 };
/*     */ 
/*     */   
/*  21 */   private static final int[] PExtInv = new int[] { -1, 1, -1, -3, -1, 1, -1, -3, -2, -1, -1, -1, 1, -2, -1, 1, 2 };
/*     */   
/*     */   private static final int P11 = -1;
/*     */   
/*     */   private static final int PExt23 = -1;
/*     */ 
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  29 */     int i = Nat.add(12, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  30 */     if (i != 0 || (paramArrayOfint3[11] == -1 && Nat.gte(12, paramArrayOfint3, P)))
/*     */     {
/*  32 */       addPInvTo(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  38 */     int i = Nat.add(24, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  39 */     if (i != 0 || (paramArrayOfint3[23] == -1 && Nat.gte(24, paramArrayOfint3, PExt)))
/*     */     {
/*  41 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  43 */         Nat.incAt(24, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  50 */     int i = Nat.inc(12, paramArrayOfint1, paramArrayOfint2);
/*  51 */     if (i != 0 || (paramArrayOfint2[11] == -1 && Nat.gte(12, paramArrayOfint2, P)))
/*     */     {
/*  53 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     int[] arrayOfInt = Nat.fromBigInteger(384, paramBigInteger);
/*  60 */     if (arrayOfInt[11] == -1 && Nat.gte(12, arrayOfInt, P))
/*     */     {
/*  62 */       Nat.subFrom(12, P, arrayOfInt);
/*     */     }
/*  64 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  69 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  71 */       Nat.shiftDownBit(12, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  75 */       int i = Nat.add(12, paramArrayOfint1, P, paramArrayOfint2);
/*  76 */       Nat.shiftDownBit(12, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  82 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  87 */     int i = 0;
/*  88 */     for (byte b = 0; b < 12; b++)
/*     */     {
/*  90 */       i |= paramArrayOfint[b];
/*     */     }
/*  92 */     i = i >>> 1 | i & 0x1;
/*  93 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  98 */     int[] arrayOfInt = Nat.create(24);
/*  99 */     Nat384.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/* 100 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 105 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 107 */       Nat.sub(12, P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 111 */       Nat.sub(12, P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 117 */     byte[] arrayOfByte = new byte[48];
/*     */     
/*     */     do {
/* 120 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 121 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 12);
/*     */     }
/* 123 */     while (0 == Nat.lessThan(12, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 130 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 132 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 137 */     long l1 = paramArrayOfint1[16] & 0xFFFFFFFFL, l2 = paramArrayOfint1[17] & 0xFFFFFFFFL, l3 = paramArrayOfint1[18] & 0xFFFFFFFFL, l4 = paramArrayOfint1[19] & 0xFFFFFFFFL;
/* 138 */     long l5 = paramArrayOfint1[20] & 0xFFFFFFFFL, l6 = paramArrayOfint1[21] & 0xFFFFFFFFL, l7 = paramArrayOfint1[22] & 0xFFFFFFFFL, l8 = paramArrayOfint1[23] & 0xFFFFFFFFL;
/*     */ 
/*     */ 
/*     */     
/* 142 */     long l9 = (paramArrayOfint1[12] & 0xFFFFFFFFL) + l5 - 1L;
/* 143 */     long l10 = (paramArrayOfint1[13] & 0xFFFFFFFFL) + l7;
/* 144 */     long l11 = (paramArrayOfint1[14] & 0xFFFFFFFFL) + l7 + l8;
/* 145 */     long l12 = (paramArrayOfint1[15] & 0xFFFFFFFFL) + l8;
/* 146 */     long l13 = l2 + l6;
/* 147 */     long l14 = l6 - l8;
/* 148 */     long l15 = l7 - l8;
/* 149 */     long l16 = l9 + l14;
/*     */     
/* 151 */     long l17 = 0L;
/* 152 */     l17 += (paramArrayOfint1[0] & 0xFFFFFFFFL) + l16;
/* 153 */     paramArrayOfint2[0] = (int)l17;
/* 154 */     l17 >>= 32L;
/* 155 */     l17 += (paramArrayOfint1[1] & 0xFFFFFFFFL) + l8 - l9 + l10;
/* 156 */     paramArrayOfint2[1] = (int)l17;
/* 157 */     l17 >>= 32L;
/* 158 */     l17 += (paramArrayOfint1[2] & 0xFFFFFFFFL) - l6 - l10 + l11;
/* 159 */     paramArrayOfint2[2] = (int)l17;
/* 160 */     l17 >>= 32L;
/* 161 */     l17 += (paramArrayOfint1[3] & 0xFFFFFFFFL) - l11 + l12 + l16;
/* 162 */     paramArrayOfint2[3] = (int)l17;
/* 163 */     l17 >>= 32L;
/* 164 */     l17 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + l1 + l6 + l10 - l12 + l16;
/* 165 */     paramArrayOfint2[4] = (int)l17;
/* 166 */     l17 >>= 32L;
/* 167 */     l17 += (paramArrayOfint1[5] & 0xFFFFFFFFL) - l1 + l10 + l11 + l13;
/* 168 */     paramArrayOfint2[5] = (int)l17;
/* 169 */     l17 >>= 32L;
/* 170 */     l17 += (paramArrayOfint1[6] & 0xFFFFFFFFL) + l3 - l2 + l11 + l12;
/* 171 */     paramArrayOfint2[6] = (int)l17;
/* 172 */     l17 >>= 32L;
/* 173 */     l17 += (paramArrayOfint1[7] & 0xFFFFFFFFL) + l1 + l4 - l3 + l12;
/* 174 */     paramArrayOfint2[7] = (int)l17;
/* 175 */     l17 >>= 32L;
/* 176 */     l17 += (paramArrayOfint1[8] & 0xFFFFFFFFL) + l1 + l2 + l5 - l4;
/* 177 */     paramArrayOfint2[8] = (int)l17;
/* 178 */     l17 >>= 32L;
/* 179 */     l17 += (paramArrayOfint1[9] & 0xFFFFFFFFL) + l3 - l5 + l13;
/* 180 */     paramArrayOfint2[9] = (int)l17;
/* 181 */     l17 >>= 32L;
/* 182 */     l17 += (paramArrayOfint1[10] & 0xFFFFFFFFL) + l3 + l4 - l14 + l15;
/* 183 */     paramArrayOfint2[10] = (int)l17;
/* 184 */     l17 >>= 32L;
/* 185 */     l17 += (paramArrayOfint1[11] & 0xFFFFFFFFL) + l4 + l5 - l15;
/* 186 */     paramArrayOfint2[11] = (int)l17;
/* 187 */     l17 >>= 32L;
/* 188 */     l17++;
/*     */ 
/*     */ 
/*     */     
/* 192 */     reduce32((int)l17, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 197 */     long l = 0L;
/*     */     
/* 199 */     if (paramInt != 0) {
/*     */       
/* 201 */       long l1 = paramInt & 0xFFFFFFFFL;
/*     */       
/* 203 */       l += (paramArrayOfint[0] & 0xFFFFFFFFL) + l1;
/* 204 */       paramArrayOfint[0] = (int)l;
/* 205 */       l >>= 32L;
/* 206 */       l += (paramArrayOfint[1] & 0xFFFFFFFFL) - l1;
/* 207 */       paramArrayOfint[1] = (int)l;
/* 208 */       l >>= 32L;
/* 209 */       if (l != 0L) {
/*     */         
/* 211 */         l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 212 */         paramArrayOfint[2] = (int)l;
/* 213 */         l >>= 32L;
/*     */       } 
/* 215 */       l += (paramArrayOfint[3] & 0xFFFFFFFFL) + l1;
/* 216 */       paramArrayOfint[3] = (int)l;
/* 217 */       l >>= 32L;
/* 218 */       l += (paramArrayOfint[4] & 0xFFFFFFFFL) + l1;
/* 219 */       paramArrayOfint[4] = (int)l;
/* 220 */       l >>= 32L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 225 */     if ((l != 0L && Nat.incAt(12, paramArrayOfint, 5) != 0) || (paramArrayOfint[11] == -1 && 
/* 226 */       Nat.gte(12, paramArrayOfint, P)))
/*     */     {
/* 228 */       addPInvTo(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 234 */     int[] arrayOfInt = Nat.create(24);
/* 235 */     Nat384.square(paramArrayOfint1, arrayOfInt);
/* 236 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 243 */     int[] arrayOfInt = Nat.create(24);
/* 244 */     Nat384.square(paramArrayOfint1, arrayOfInt);
/* 245 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 247 */     while (--paramInt > 0) {
/*     */       
/* 249 */       Nat384.square(paramArrayOfint2, arrayOfInt);
/* 250 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 256 */     int i = Nat.sub(12, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 257 */     if (i != 0)
/*     */     {
/* 259 */       subPInvFrom(paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 265 */     int i = Nat.sub(24, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 266 */     if (i != 0)
/*     */     {
/* 268 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 270 */         Nat.decAt(24, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 277 */     int i = Nat.shiftUpBit(12, paramArrayOfint1, 0, paramArrayOfint2);
/* 278 */     if (i != 0 || (paramArrayOfint2[11] == -1 && Nat.gte(12, paramArrayOfint2, P)))
/*     */     {
/* 280 */       addPInvTo(paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addPInvTo(int[] paramArrayOfint) {
/* 286 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + 1L;
/* 287 */     paramArrayOfint[0] = (int)l;
/* 288 */     l >>= 32L;
/* 289 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) - 1L;
/* 290 */     paramArrayOfint[1] = (int)l;
/* 291 */     l >>= 32L;
/* 292 */     if (l != 0L) {
/*     */       
/* 294 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 295 */       paramArrayOfint[2] = (int)l;
/* 296 */       l >>= 32L;
/*     */     } 
/* 298 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) + 1L;
/* 299 */     paramArrayOfint[3] = (int)l;
/* 300 */     l >>= 32L;
/* 301 */     l += (paramArrayOfint[4] & 0xFFFFFFFFL) + 1L;
/* 302 */     paramArrayOfint[4] = (int)l;
/* 303 */     l >>= 32L;
/* 304 */     if (l != 0L)
/*     */     {
/* 306 */       Nat.incAt(12, paramArrayOfint, 5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void subPInvFrom(int[] paramArrayOfint) {
/* 312 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - 1L;
/* 313 */     paramArrayOfint[0] = (int)l;
/* 314 */     l >>= 32L;
/* 315 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) + 1L;
/* 316 */     paramArrayOfint[1] = (int)l;
/* 317 */     l >>= 32L;
/* 318 */     if (l != 0L) {
/*     */       
/* 320 */       l += paramArrayOfint[2] & 0xFFFFFFFFL;
/* 321 */       paramArrayOfint[2] = (int)l;
/* 322 */       l >>= 32L;
/*     */     } 
/* 324 */     l += (paramArrayOfint[3] & 0xFFFFFFFFL) - 1L;
/* 325 */     paramArrayOfint[3] = (int)l;
/* 326 */     l >>= 32L;
/* 327 */     l += (paramArrayOfint[4] & 0xFFFFFFFFL) - 1L;
/* 328 */     paramArrayOfint[4] = (int)l;
/* 329 */     l >>= 32L;
/* 330 */     if (l != 0L)
/*     */     {
/* 332 */       Nat.decAt(12, paramArrayOfint, 5);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP384R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */