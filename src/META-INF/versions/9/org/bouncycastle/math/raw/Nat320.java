/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Nat320
/*     */ {
/*     */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  11 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/*  12 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  13 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  14 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*  15 */     paramArrayOflong2[4] = paramArrayOflong1[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/*  20 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/*  21 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*  22 */     paramArrayOflong2[paramInt2 + 2] = paramArrayOflong1[paramInt1 + 2];
/*  23 */     paramArrayOflong2[paramInt2 + 3] = paramArrayOflong1[paramInt1 + 3];
/*  24 */     paramArrayOflong2[paramInt2 + 4] = paramArrayOflong1[paramInt1 + 4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] create64() {
/*  29 */     return new long[5];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] createExt64() {
/*  34 */     return new long[10];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  39 */     for (byte b = 4; b >= 0; b--) {
/*     */       
/*  41 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*     */       {
/*  43 */         return false;
/*     */       }
/*     */     } 
/*  46 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/*  51 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 320)
/*     */     {
/*  53 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  56 */     long[] arrayOfLong = create64();
/*     */ 
/*     */     
/*  59 */     for (byte b = 0; b < 5; b++) {
/*     */       
/*  61 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  62 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*     */     } 
/*  64 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne64(long[] paramArrayOflong) {
/*  69 */     if (paramArrayOflong[0] != 1L)
/*     */     {
/*  71 */       return false;
/*     */     }
/*  73 */     for (byte b = 1; b < 5; b++) {
/*     */       
/*  75 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  77 */         return false;
/*     */       }
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero64(long[] paramArrayOflong) {
/*  85 */     for (byte b = 0; b < 5; b++) {
/*     */       
/*  87 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  89 */         return false;
/*     */       }
/*     */     } 
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/*  97 */     byte[] arrayOfByte = new byte[40];
/*  98 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 100 */       long l = paramArrayOflong[b];
/* 101 */       if (l != 0L)
/*     */       {
/* 103 */         Pack.longToBigEndian(l, arrayOfByte, 4 - b << 3);
/*     */       }
/*     */     } 
/* 106 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat320.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */