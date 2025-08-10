/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
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
/*     */ public final class PolynomialRingGF2
/*     */ {
/*     */   public static int add(int paramInt1, int paramInt2) {
/*  34 */     return paramInt1 ^ paramInt2;
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
/*     */   public static long multiply(int paramInt1, int paramInt2) {
/*  47 */     long l = 0L;
/*  48 */     if (paramInt2 != 0) {
/*     */       
/*  50 */       long l1 = paramInt2 & 0xFFFFFFFFL;
/*     */       
/*  52 */       while (paramInt1 != 0) {
/*     */         
/*  54 */         byte b = (byte)(paramInt1 & 0x1);
/*  55 */         if (b == 1)
/*     */         {
/*  57 */           l ^= l1;
/*     */         }
/*  59 */         paramInt1 >>>= 1;
/*  60 */         l1 <<= 1L;
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     return l;
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
/*     */   public static int modMultiply(int paramInt1, int paramInt2, int paramInt3) {
/*  77 */     int i = 0;
/*  78 */     int j = remainder(paramInt1, paramInt3);
/*  79 */     int k = remainder(paramInt2, paramInt3);
/*  80 */     if (k != 0) {
/*     */       
/*  82 */       int m = 1 << degree(paramInt3);
/*     */       
/*  84 */       while (j != 0) {
/*     */         
/*  86 */         byte b = (byte)(j & 0x1);
/*  87 */         if (b == 1)
/*     */         {
/*  89 */           i ^= k;
/*     */         }
/*  91 */         j >>>= 1;
/*  92 */         k <<= 1;
/*  93 */         if (k >= m)
/*     */         {
/*  95 */           k ^= paramInt3;
/*     */         }
/*     */       } 
/*     */     } 
/*  99 */     return i;
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
/*     */   public static int degree(int paramInt) {
/* 111 */     byte b = -1;
/* 112 */     while (paramInt != 0) {
/*     */       
/* 114 */       b++;
/* 115 */       paramInt >>>= 1;
/*     */     } 
/* 117 */     return b;
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
/*     */   public static int degree(long paramLong) {
/* 129 */     byte b = 0;
/* 130 */     while (paramLong != 0L) {
/*     */       
/* 132 */       b++;
/* 133 */       paramLong >>>= 1L;
/*     */     } 
/* 135 */     return b - 1;
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
/*     */   public static int remainder(int paramInt1, int paramInt2) {
/* 147 */     int i = paramInt1;
/*     */     
/* 149 */     if (paramInt2 == 0) {
/*     */       
/* 151 */       System.err.println("Error: to be divided by 0");
/* 152 */       return 0;
/*     */     } 
/*     */     
/* 155 */     while (degree(i) >= degree(paramInt2))
/*     */     {
/* 157 */       i ^= paramInt2 << degree(i) - degree(paramInt2);
/*     */     }
/*     */     
/* 160 */     return i;
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
/*     */   public static int rest(long paramLong, int paramInt) {
/* 173 */     long l1 = paramLong;
/* 174 */     if (paramInt == 0) {
/*     */       
/* 176 */       System.err.println("Error: to be divided by 0");
/* 177 */       return 0;
/*     */     } 
/* 179 */     long l2 = paramInt & 0xFFFFFFFFL;
/* 180 */     while (l1 >>> 32L != 0L)
/*     */     {
/* 182 */       l1 ^= l2 << degree(l1) - degree(l2);
/*     */     }
/*     */     
/* 185 */     int i = (int)(l1 & 0xFFFFFFFFFFFFFFFFL);
/* 186 */     while (degree(i) >= degree(paramInt))
/*     */     {
/* 188 */       i ^= paramInt << degree(i) - degree(paramInt);
/*     */     }
/*     */     
/* 191 */     return i;
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
/*     */   public static int gcd(int paramInt1, int paramInt2) {
/* 205 */     int i = paramInt1;
/* 206 */     int j = paramInt2;
/* 207 */     while (j != 0) {
/*     */       
/* 209 */       int k = remainder(i, j);
/* 210 */       i = j;
/* 211 */       j = k;
/*     */     } 
/*     */     
/* 214 */     return i;
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
/*     */   public static boolean isIrreducible(int paramInt) {
/* 226 */     if (paramInt == 0)
/*     */     {
/* 228 */       return false;
/*     */     }
/* 230 */     int i = degree(paramInt) >>> 1;
/* 231 */     int j = 2;
/* 232 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 234 */       j = modMultiply(j, j, paramInt);
/* 235 */       if (gcd(j ^ 0x2, paramInt) != 1)
/*     */       {
/* 237 */         return false;
/*     */       }
/*     */     } 
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getIrreduciblePolynomial(int paramInt) {
/* 251 */     if (paramInt < 0) {
/*     */       
/* 253 */       System.err.println("The Degree is negative");
/* 254 */       return 0;
/*     */     } 
/* 256 */     if (paramInt > 31) {
/*     */       
/* 258 */       System.err.println("The Degree is more then 31");
/* 259 */       return 0;
/*     */     } 
/* 261 */     if (paramInt == 0)
/*     */     {
/* 263 */       return 1;
/*     */     }
/* 265 */     int i = 1 << paramInt;
/* 266 */     i++;
/* 267 */     int j = 1 << paramInt + 1;
/* 268 */     for (int k = i; k < j; k += 2) {
/*     */       
/* 270 */       if (isIrreducible(k))
/*     */       {
/* 272 */         return k;
/*     */       }
/*     */     } 
/* 275 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/PolynomialRingGF2.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */