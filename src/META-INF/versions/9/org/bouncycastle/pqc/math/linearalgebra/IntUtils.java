/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import org.bouncycastle.pqc.math.linearalgebra.BigEndianConversions;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
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
/*     */ public final class IntUtils
/*     */ {
/*     */   public static boolean equals(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  23 */     if (paramArrayOfint1.length != paramArrayOfint2.length)
/*     */     {
/*  25 */       return false;
/*     */     }
/*  27 */     int i = 1;
/*  28 */     for (int j = paramArrayOfint1.length - 1; j >= 0; j--)
/*     */     {
/*  30 */       i &= (paramArrayOfint1[j] == paramArrayOfint2[j]) ? 1 : 0;
/*     */     }
/*  32 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] clone(int[] paramArrayOfint) {
/*  43 */     int[] arrayOfInt = new int[paramArrayOfint.length];
/*  44 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, paramArrayOfint.length);
/*  45 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fill(int[] paramArrayOfint, int paramInt) {
/*  56 */     for (int i = paramArrayOfint.length - 1; i >= 0; i--)
/*     */     {
/*  58 */       paramArrayOfint[i] = paramInt;
/*     */     }
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
/*     */ 
/*     */   
/*     */   public static void quicksort(int[] paramArrayOfint) {
/*  75 */     quicksort(paramArrayOfint, 0, paramArrayOfint.length - 1);
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
/*     */   public static void quicksort(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/*  88 */     if (paramInt2 > paramInt1) {
/*     */       
/*  90 */       int i = partition(paramArrayOfint, paramInt1, paramInt2, paramInt2);
/*  91 */       quicksort(paramArrayOfint, paramInt1, i - 1);
/*  92 */       quicksort(paramArrayOfint, i + 1, paramInt2);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int partition(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
/* 112 */     int i = paramArrayOfint[paramInt3];
/* 113 */     paramArrayOfint[paramInt3] = paramArrayOfint[paramInt2];
/* 114 */     paramArrayOfint[paramInt2] = i;
/*     */     
/* 116 */     int j = paramInt1;
/*     */     int k;
/* 118 */     for (k = paramInt1; k < paramInt2; k++) {
/*     */       
/* 120 */       if (paramArrayOfint[k] <= i) {
/*     */         
/* 122 */         int m = paramArrayOfint[j];
/* 123 */         paramArrayOfint[j] = paramArrayOfint[k];
/* 124 */         paramArrayOfint[k] = m;
/* 125 */         j++;
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     k = paramArrayOfint[j];
/* 130 */     paramArrayOfint[j] = paramArrayOfint[paramInt2];
/* 131 */     paramArrayOfint[paramInt2] = k;
/*     */     
/* 133 */     return j;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] subArray(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/* 151 */     int[] arrayOfInt = new int[paramInt2 - paramInt1];
/* 152 */     System.arraycopy(paramArrayOfint, paramInt1, arrayOfInt, 0, paramInt2 - paramInt1);
/* 153 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int[] paramArrayOfint) {
/* 162 */     String str = "";
/* 163 */     for (byte b = 0; b < paramArrayOfint.length; b++)
/*     */     {
/* 165 */       str = str + str + " ";
/*     */     }
/* 167 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(int[] paramArrayOfint) {
/* 176 */     return ByteUtils.toHexString(BigEndianConversions.toByteArray(paramArrayOfint));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/IntUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */