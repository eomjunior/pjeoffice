/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow.util;
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
/*     */ public class RainbowUtil
/*     */ {
/*     */   public static int[] convertArraytoInt(byte[] paramArrayOfbyte) {
/*  20 */     int[] arrayOfInt = new int[paramArrayOfbyte.length];
/*  21 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/*  23 */       arrayOfInt[b] = paramArrayOfbyte[b] & 0xFF;
/*     */     }
/*  25 */     return arrayOfInt;
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
/*     */   public static short[] convertArray(byte[] paramArrayOfbyte) {
/*  38 */     short[] arrayOfShort = new short[paramArrayOfbyte.length];
/*  39 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/*  41 */       arrayOfShort[b] = (short)(paramArrayOfbyte[b] & 0xFF);
/*     */     }
/*  43 */     return arrayOfShort;
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
/*     */   public static short[][] convertArray(byte[][] paramArrayOfbyte) {
/*  55 */     short[][] arrayOfShort = new short[paramArrayOfbyte.length][(paramArrayOfbyte[0]).length];
/*  56 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/*  58 */       for (byte b1 = 0; b1 < (paramArrayOfbyte[0]).length; b1++)
/*     */       {
/*  60 */         arrayOfShort[b][b1] = (short)(paramArrayOfbyte[b][b1] & 0xFF);
/*     */       }
/*     */     } 
/*  63 */     return arrayOfShort;
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
/*     */   public static short[][][] convertArray(byte[][][] paramArrayOfbyte) {
/*  75 */     short[][][] arrayOfShort = new short[paramArrayOfbyte.length][(paramArrayOfbyte[0]).length][(paramArrayOfbyte[0][0]).length];
/*  76 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/*  78 */       for (byte b1 = 0; b1 < (paramArrayOfbyte[0]).length; b1++) {
/*     */         
/*  80 */         for (byte b2 = 0; b2 < (paramArrayOfbyte[0][0]).length; b2++)
/*     */         {
/*  82 */           arrayOfShort[b][b1][b2] = (short)(paramArrayOfbyte[b][b1][b2] & 0xFF);
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     return arrayOfShort;
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
/*     */   public static byte[] convertIntArray(int[] paramArrayOfint) {
/*  98 */     byte[] arrayOfByte = new byte[paramArrayOfint.length];
/*  99 */     for (byte b = 0; b < paramArrayOfint.length; b++)
/*     */     {
/* 101 */       arrayOfByte[b] = (byte)paramArrayOfint[b];
/*     */     }
/* 103 */     return arrayOfByte;
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
/*     */   public static byte[] convertArray(short[] paramArrayOfshort) {
/* 116 */     byte[] arrayOfByte = new byte[paramArrayOfshort.length];
/* 117 */     for (byte b = 0; b < paramArrayOfshort.length; b++)
/*     */     {
/* 119 */       arrayOfByte[b] = (byte)paramArrayOfshort[b];
/*     */     }
/* 121 */     return arrayOfByte;
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
/*     */   public static byte[][] convertArray(short[][] paramArrayOfshort) {
/* 133 */     byte[][] arrayOfByte = new byte[paramArrayOfshort.length][(paramArrayOfshort[0]).length];
/* 134 */     for (byte b = 0; b < paramArrayOfshort.length; b++) {
/*     */       
/* 136 */       for (byte b1 = 0; b1 < (paramArrayOfshort[0]).length; b1++)
/*     */       {
/* 138 */         arrayOfByte[b][b1] = (byte)paramArrayOfshort[b][b1];
/*     */       }
/*     */     } 
/* 141 */     return arrayOfByte;
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
/*     */   public static byte[][][] convertArray(short[][][] paramArrayOfshort) {
/* 153 */     byte[][][] arrayOfByte = new byte[paramArrayOfshort.length][(paramArrayOfshort[0]).length][(paramArrayOfshort[0][0]).length];
/* 154 */     for (byte b = 0; b < paramArrayOfshort.length; b++) {
/*     */       
/* 156 */       for (byte b1 = 0; b1 < (paramArrayOfshort[0]).length; b1++) {
/*     */         
/* 158 */         for (byte b2 = 0; b2 < (paramArrayOfshort[0][0]).length; b2++)
/*     */         {
/* 160 */           arrayOfByte[b][b1][b2] = (byte)paramArrayOfshort[b][b1][b2];
/*     */         }
/*     */       } 
/*     */     } 
/* 164 */     return arrayOfByte;
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
/*     */   public static boolean equals(short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
/* 176 */     if (paramArrayOfshort1.length != paramArrayOfshort2.length)
/*     */     {
/* 178 */       return false;
/*     */     }
/* 180 */     int i = 1;
/* 181 */     for (int j = paramArrayOfshort1.length - 1; j >= 0; j--)
/*     */     {
/* 183 */       i &= (paramArrayOfshort1[j] == paramArrayOfshort2[j]) ? 1 : 0;
/*     */     }
/* 185 */     return i;
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
/*     */   public static boolean equals(short[][] paramArrayOfshort1, short[][] paramArrayOfshort2) {
/* 197 */     if (paramArrayOfshort1.length != paramArrayOfshort2.length)
/*     */     {
/* 199 */       return false;
/*     */     }
/* 201 */     boolean bool = true;
/* 202 */     for (int i = paramArrayOfshort1.length - 1; i >= 0; i--)
/*     */     {
/* 204 */       bool &= equals(paramArrayOfshort1[i], paramArrayOfshort2[i]);
/*     */     }
/* 206 */     return bool;
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
/*     */   public static boolean equals(short[][][] paramArrayOfshort1, short[][][] paramArrayOfshort2) {
/* 218 */     if (paramArrayOfshort1.length != paramArrayOfshort2.length)
/*     */     {
/* 220 */       return false;
/*     */     }
/* 222 */     boolean bool = true;
/* 223 */     for (int i = paramArrayOfshort1.length - 1; i >= 0; i--)
/*     */     {
/* 225 */       bool &= equals(paramArrayOfshort1[i], paramArrayOfshort2[i]);
/*     */     }
/* 227 */     return bool;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/util/RainbowUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */