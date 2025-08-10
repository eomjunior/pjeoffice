/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Nat448
/*     */ {
/*     */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  11 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/*  12 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  13 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  14 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*  15 */     paramArrayOflong2[4] = paramArrayOflong1[4];
/*  16 */     paramArrayOflong2[5] = paramArrayOflong1[5];
/*  17 */     paramArrayOflong2[6] = paramArrayOflong1[6];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/*  22 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/*  23 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*  24 */     paramArrayOflong2[paramInt2 + 2] = paramArrayOflong1[paramInt1 + 2];
/*  25 */     paramArrayOflong2[paramInt2 + 3] = paramArrayOflong1[paramInt1 + 3];
/*  26 */     paramArrayOflong2[paramInt2 + 4] = paramArrayOflong1[paramInt1 + 4];
/*  27 */     paramArrayOflong2[paramInt2 + 5] = paramArrayOflong1[paramInt1 + 5];
/*  28 */     paramArrayOflong2[paramInt2 + 6] = paramArrayOflong1[paramInt1 + 6];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] create64() {
/*  33 */     return new long[7];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] createExt64() {
/*  38 */     return new long[14];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  43 */     for (byte b = 6; b >= 0; b--) {
/*     */       
/*  45 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*     */       {
/*  47 */         return false;
/*     */       }
/*     */     } 
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/*  55 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 448)
/*     */     {
/*  57 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  60 */     long[] arrayOfLong = create64();
/*     */ 
/*     */     
/*  63 */     for (byte b = 0; b < 7; b++) {
/*     */       
/*  65 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  66 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*     */     } 
/*  68 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne64(long[] paramArrayOflong) {
/*  73 */     if (paramArrayOflong[0] != 1L)
/*     */     {
/*  75 */       return false;
/*     */     }
/*  77 */     for (byte b = 1; b < 7; b++) {
/*     */       
/*  79 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  81 */         return false;
/*     */       }
/*     */     } 
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero64(long[] paramArrayOflong) {
/*  89 */     for (byte b = 0; b < 7; b++) {
/*     */       
/*  91 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  93 */         return false;
/*     */       }
/*     */     } 
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/* 101 */     byte[] arrayOfByte = new byte[56];
/* 102 */     for (byte b = 0; b < 7; b++) {
/*     */       
/* 104 */       long l = paramArrayOflong[b];
/* 105 */       if (l != 0L)
/*     */       {
/* 107 */         Pack.longToBigEndian(l, arrayOfByte, 6 - b << 3);
/*     */       }
/*     */     } 
/* 110 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat448.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */