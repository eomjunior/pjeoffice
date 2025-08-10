/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteUtils
/*     */ {
/*   9 */   private static final char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
/*     */   public static boolean equals(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  29 */     if (paramArrayOfbyte1 == null)
/*     */     {
/*  31 */       return (paramArrayOfbyte2 == null);
/*     */     }
/*  33 */     if (paramArrayOfbyte2 == null)
/*     */     {
/*  35 */       return false;
/*     */     }
/*     */     
/*  38 */     if (paramArrayOfbyte1.length != paramArrayOfbyte2.length)
/*     */     {
/*  40 */       return false;
/*     */     }
/*  42 */     int i = 1;
/*  43 */     for (int j = paramArrayOfbyte1.length - 1; j >= 0; j--)
/*     */     {
/*  45 */       i &= (paramArrayOfbyte1[j] == paramArrayOfbyte2[j]) ? 1 : 0;
/*     */     }
/*  47 */     return i;
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
/*     */   public static boolean equals(byte[][] paramArrayOfbyte1, byte[][] paramArrayOfbyte2) {
/*  59 */     if (paramArrayOfbyte1.length != paramArrayOfbyte2.length)
/*     */     {
/*  61 */       return false;
/*     */     }
/*     */     
/*  64 */     boolean bool = true;
/*  65 */     for (int i = paramArrayOfbyte1.length - 1; i >= 0; i--)
/*     */     {
/*  67 */       bool &= equals(paramArrayOfbyte1[i], paramArrayOfbyte2[i]);
/*     */     }
/*     */     
/*  70 */     return bool;
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
/*     */   public static boolean equals(byte[][][] paramArrayOfbyte1, byte[][][] paramArrayOfbyte2) {
/*  82 */     if (paramArrayOfbyte1.length != paramArrayOfbyte2.length)
/*     */     {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     boolean bool = true;
/*  88 */     for (int i = paramArrayOfbyte1.length - 1; i >= 0; i--) {
/*     */       
/*  90 */       if ((paramArrayOfbyte1[i]).length != (paramArrayOfbyte2[i]).length)
/*     */       {
/*  92 */         return false;
/*     */       }
/*  94 */       for (int j = (paramArrayOfbyte1[i]).length - 1; j >= 0; j--)
/*     */       {
/*  96 */         bool &= equals(paramArrayOfbyte1[i][j], paramArrayOfbyte2[i][j]);
/*     */       }
/*     */     } 
/*     */     
/* 100 */     return bool;
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
/*     */   public static int deepHashCode(byte[] paramArrayOfbyte) {
/* 112 */     int i = 1;
/* 113 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 115 */       i = 31 * i + paramArrayOfbyte[b];
/*     */     }
/* 117 */     return i;
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
/*     */   public static int deepHashCode(byte[][] paramArrayOfbyte) {
/* 129 */     int i = 1;
/* 130 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 132 */       i = 31 * i + deepHashCode(paramArrayOfbyte[b]);
/*     */     }
/* 134 */     return i;
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
/*     */   public static int deepHashCode(byte[][][] paramArrayOfbyte) {
/* 146 */     int i = 1;
/* 147 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 149 */       i = 31 * i + deepHashCode(paramArrayOfbyte[b]);
/*     */     }
/* 151 */     return i;
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
/*     */   public static byte[] clone(byte[] paramArrayOfbyte) {
/* 164 */     if (paramArrayOfbyte == null)
/*     */     {
/* 166 */       return null;
/*     */     }
/* 168 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length];
/* 169 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, paramArrayOfbyte.length);
/* 170 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fromHexString(String paramString) {
/* 181 */     char[] arrayOfChar = paramString.toUpperCase().toCharArray();
/*     */     
/* 183 */     byte b1 = 0;
/* 184 */     for (byte b2 = 0; b2 < arrayOfChar.length; b2++) {
/*     */       
/* 186 */       if ((arrayOfChar[b2] >= '0' && arrayOfChar[b2] <= '9') || (arrayOfChar[b2] >= 'A' && arrayOfChar[b2] <= 'F'))
/*     */       {
/*     */         
/* 189 */         b1++;
/*     */       }
/*     */     } 
/*     */     
/* 193 */     byte[] arrayOfByte = new byte[b1 + 1 >> 1];
/*     */     
/* 195 */     int i = b1 & 0x1;
/*     */     
/* 197 */     for (byte b3 = 0; b3 < arrayOfChar.length; b3++) {
/*     */       
/* 199 */       if (arrayOfChar[b3] >= '0' && arrayOfChar[b3] <= '9') {
/*     */         
/* 201 */         arrayOfByte[i >> 1] = (byte)(arrayOfByte[i >> 1] << 4);
/* 202 */         arrayOfByte[i >> 1] = (byte)(arrayOfByte[i >> 1] | arrayOfChar[b3] - 48);
/*     */       }
/* 204 */       else if (arrayOfChar[b3] >= 'A' && arrayOfChar[b3] <= 'F') {
/*     */         
/* 206 */         arrayOfByte[i >> 1] = (byte)(arrayOfByte[i >> 1] << 4);
/* 207 */         arrayOfByte[i >> 1] = (byte)(arrayOfByte[i >> 1] | arrayOfChar[b3] - 65 + 10);
/*     */       } else {
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 213 */       i++;
/*     */       continue;
/*     */     } 
/* 216 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(byte[] paramArrayOfbyte) {
/* 227 */     String str = "";
/* 228 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 230 */       str = str + str;
/* 231 */       str = str + str;
/*     */     } 
/* 233 */     return str;
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
/*     */   public static String toHexString(byte[] paramArrayOfbyte, String paramString1, String paramString2) {
/* 247 */     String str = new String(paramString1);
/* 248 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 250 */       str = str + str;
/* 251 */       str = str + str;
/* 252 */       if (b < paramArrayOfbyte.length - 1)
/*     */       {
/* 254 */         str = str + str;
/*     */       }
/*     */     } 
/* 257 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toBinaryString(byte[] paramArrayOfbyte) {
/* 268 */     String str = "";
/*     */     
/* 270 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 272 */       byte b1 = paramArrayOfbyte[b];
/* 273 */       for (byte b2 = 0; b2 < 8; b2++) {
/*     */         
/* 275 */         int i = b1 >>> b2 & 0x1;
/* 276 */         str = str + str;
/*     */       } 
/* 278 */       if (b != paramArrayOfbyte.length - 1)
/*     */       {
/* 280 */         str = str + " ";
/*     */       }
/*     */     } 
/* 283 */     return str;
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
/*     */   public static byte[] xor(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 296 */     byte[] arrayOfByte = new byte[paramArrayOfbyte1.length];
/*     */     
/* 298 */     for (int i = paramArrayOfbyte1.length - 1; i >= 0; i--)
/*     */     {
/* 300 */       arrayOfByte[i] = (byte)(paramArrayOfbyte1[i] ^ paramArrayOfbyte2[i]);
/*     */     }
/* 302 */     return arrayOfByte;
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
/*     */   public static byte[] concatenate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 315 */     byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length];
/*     */     
/* 317 */     System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
/* 318 */     System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
/*     */     
/* 320 */     return arrayOfByte;
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
/*     */   public static byte[] concatenate(byte[][] paramArrayOfbyte) {
/* 332 */     int i = (paramArrayOfbyte[0]).length;
/* 333 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length * i];
/* 334 */     int j = 0;
/* 335 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 337 */       System.arraycopy(paramArrayOfbyte[b], 0, arrayOfByte, j, i);
/* 338 */       j += i;
/*     */     } 
/* 340 */     return arrayOfByte;
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
/*     */   public static byte[][] split(byte[] paramArrayOfbyte, int paramInt) throws ArrayIndexOutOfBoundsException {
/* 356 */     if (paramInt > paramArrayOfbyte.length)
/*     */     {
/* 358 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 360 */     byte[][] arrayOfByte = new byte[2][];
/* 361 */     arrayOfByte[0] = new byte[paramInt];
/* 362 */     arrayOfByte[1] = new byte[paramArrayOfbyte.length - paramInt];
/* 363 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte[0], 0, paramInt);
/* 364 */     System.arraycopy(paramArrayOfbyte, paramInt, arrayOfByte[1], 0, paramArrayOfbyte.length - paramInt);
/* 365 */     return arrayOfByte;
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
/*     */   public static byte[] subArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 379 */     byte[] arrayOfByte = new byte[paramInt2 - paramInt1];
/* 380 */     System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2 - paramInt1);
/* 381 */     return arrayOfByte;
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
/*     */   public static byte[] subArray(byte[] paramArrayOfbyte, int paramInt) {
/* 394 */     return subArray(paramArrayOfbyte, paramInt, paramArrayOfbyte.length);
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
/*     */   public static char[] toCharArray(byte[] paramArrayOfbyte) {
/* 406 */     char[] arrayOfChar = new char[paramArrayOfbyte.length];
/* 407 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 409 */       arrayOfChar[b] = (char)paramArrayOfbyte[b];
/*     */     }
/* 411 */     return arrayOfChar;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/ByteUtils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */