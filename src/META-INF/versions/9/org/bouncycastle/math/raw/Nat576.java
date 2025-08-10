/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Nat576
/*     */ {
/*     */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  11 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/*  12 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  13 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  14 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*  15 */     paramArrayOflong2[4] = paramArrayOflong1[4];
/*  16 */     paramArrayOflong2[5] = paramArrayOflong1[5];
/*  17 */     paramArrayOflong2[6] = paramArrayOflong1[6];
/*  18 */     paramArrayOflong2[7] = paramArrayOflong1[7];
/*  19 */     paramArrayOflong2[8] = paramArrayOflong1[8];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/*  24 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/*  25 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*  26 */     paramArrayOflong2[paramInt2 + 2] = paramArrayOflong1[paramInt1 + 2];
/*  27 */     paramArrayOflong2[paramInt2 + 3] = paramArrayOflong1[paramInt1 + 3];
/*  28 */     paramArrayOflong2[paramInt2 + 4] = paramArrayOflong1[paramInt1 + 4];
/*  29 */     paramArrayOflong2[paramInt2 + 5] = paramArrayOflong1[paramInt1 + 5];
/*  30 */     paramArrayOflong2[paramInt2 + 6] = paramArrayOflong1[paramInt1 + 6];
/*  31 */     paramArrayOflong2[paramInt2 + 7] = paramArrayOflong1[paramInt1 + 7];
/*  32 */     paramArrayOflong2[paramInt2 + 8] = paramArrayOflong1[paramInt1 + 8];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] create64() {
/*  37 */     return new long[9];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] createExt64() {
/*  42 */     return new long[18];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  47 */     for (byte b = 8; b >= 0; b--) {
/*     */       
/*  49 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*     */       {
/*  51 */         return false;
/*     */       }
/*     */     } 
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/*  59 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 576)
/*     */     {
/*  61 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  64 */     long[] arrayOfLong = create64();
/*     */ 
/*     */     
/*  67 */     for (byte b = 0; b < 9; b++) {
/*     */       
/*  69 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  70 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*     */     } 
/*  72 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isOne64(long[] paramArrayOflong) {
/*  77 */     if (paramArrayOflong[0] != 1L)
/*     */     {
/*  79 */       return false;
/*     */     }
/*  81 */     for (byte b = 1; b < 9; b++) {
/*     */       
/*  83 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  85 */         return false;
/*     */       }
/*     */     } 
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero64(long[] paramArrayOflong) {
/*  93 */     for (byte b = 0; b < 9; b++) {
/*     */       
/*  95 */       if (paramArrayOflong[b] != 0L)
/*     */       {
/*  97 */         return false;
/*     */       }
/*     */     } 
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/* 105 */     byte[] arrayOfByte = new byte[72];
/* 106 */     for (byte b = 0; b < 9; b++) {
/*     */       
/* 108 */       long l = paramArrayOflong[b];
/* 109 */       if (l != 0L)
/*     */       {
/* 111 */         Pack.longToBigEndian(l, arrayOfByte, 8 - b << 3);
/*     */       }
/*     */     } 
/* 114 */     return new BigInteger(1, arrayOfByte);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat576.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */