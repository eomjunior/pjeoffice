/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
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
/*     */ public class XMSSUtil
/*     */ {
/*     */   public static int log2(int paramInt) {
/*  32 */     byte b = 0;
/*  33 */     while ((paramInt >>= 1) != 0)
/*     */     {
/*  35 */       b++;
/*     */     }
/*  37 */     return b;
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
/*     */   public static byte[] toBytesBigEndian(long paramLong, int paramInt) {
/*  49 */     byte[] arrayOfByte = new byte[paramInt];
/*  50 */     for (int i = paramInt - 1; i >= 0; i--) {
/*     */       
/*  52 */       arrayOfByte[i] = (byte)(int)paramLong;
/*  53 */       paramLong >>>= 8L;
/*     */     } 
/*  55 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void longToBigEndian(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
/*  63 */     if (paramArrayOfbyte == null)
/*     */     {
/*  65 */       throw new NullPointerException("in == null");
/*     */     }
/*  67 */     if (paramArrayOfbyte.length - paramInt < 8)
/*     */     {
/*  69 */       throw new IllegalArgumentException("not enough space in array");
/*     */     }
/*  71 */     paramArrayOfbyte[paramInt] = (byte)(int)(paramLong >> 56L & 0xFFL);
/*  72 */     paramArrayOfbyte[paramInt + 1] = (byte)(int)(paramLong >> 48L & 0xFFL);
/*  73 */     paramArrayOfbyte[paramInt + 2] = (byte)(int)(paramLong >> 40L & 0xFFL);
/*  74 */     paramArrayOfbyte[paramInt + 3] = (byte)(int)(paramLong >> 32L & 0xFFL);
/*  75 */     paramArrayOfbyte[paramInt + 4] = (byte)(int)(paramLong >> 24L & 0xFFL);
/*  76 */     paramArrayOfbyte[paramInt + 5] = (byte)(int)(paramLong >> 16L & 0xFFL);
/*  77 */     paramArrayOfbyte[paramInt + 6] = (byte)(int)(paramLong >> 8L & 0xFFL);
/*  78 */     paramArrayOfbyte[paramInt + 7] = (byte)(int)(paramLong & 0xFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long bytesToXBigEndian(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  86 */     if (paramArrayOfbyte == null)
/*     */     {
/*  88 */       throw new NullPointerException("in == null");
/*     */     }
/*  90 */     long l = 0L;
/*  91 */     for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
/*     */     {
/*  93 */       l = l << 8L | (paramArrayOfbyte[i] & 0xFF);
/*     */     }
/*  95 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] cloneArray(byte[] paramArrayOfbyte) {
/* 106 */     if (paramArrayOfbyte == null)
/*     */     {
/* 108 */       throw new NullPointerException("in == null");
/*     */     }
/* 110 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length];
/* 111 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, paramArrayOfbyte.length);
/* 112 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[][] cloneArray(byte[][] paramArrayOfbyte) {
/* 123 */     if (hasNullPointer(paramArrayOfbyte))
/*     */     {
/* 125 */       throw new NullPointerException("in has null pointers");
/*     */     }
/* 127 */     byte[][] arrayOfByte = new byte[paramArrayOfbyte.length][];
/* 128 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 130 */       arrayOfByte[b] = new byte[(paramArrayOfbyte[b]).length];
/* 131 */       System.arraycopy(paramArrayOfbyte[b], 0, arrayOfByte[b], 0, (paramArrayOfbyte[b]).length);
/*     */     } 
/* 133 */     return arrayOfByte;
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
/*     */   public static boolean areEqual(byte[][] paramArrayOfbyte1, byte[][] paramArrayOfbyte2) {
/* 145 */     if (hasNullPointer(paramArrayOfbyte1) || hasNullPointer(paramArrayOfbyte2))
/*     */     {
/* 147 */       throw new NullPointerException("a or b == null");
/*     */     }
/* 149 */     for (byte b = 0; b < paramArrayOfbyte1.length; b++) {
/*     */       
/* 151 */       if (!Arrays.areEqual(paramArrayOfbyte1[b], paramArrayOfbyte2[b]))
/*     */       {
/* 153 */         return false;
/*     */       }
/*     */     } 
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpByteArray(byte[][] paramArrayOfbyte) {
/* 166 */     if (hasNullPointer(paramArrayOfbyte))
/*     */     {
/* 168 */       throw new NullPointerException("x has null pointers");
/*     */     }
/* 170 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 172 */       System.out.println(Hex.toHexString(paramArrayOfbyte[b]));
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
/*     */   public static boolean hasNullPointer(byte[][] paramArrayOfbyte) {
/* 184 */     if (paramArrayOfbyte == null)
/*     */     {
/* 186 */       return true;
/*     */     }
/* 188 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*     */       
/* 190 */       if (paramArrayOfbyte[b] == null)
/*     */       {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
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
/*     */   public static void copyBytesAtOffset(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt) {
/* 207 */     if (paramArrayOfbyte1 == null)
/*     */     {
/* 209 */       throw new NullPointerException("dst == null");
/*     */     }
/* 211 */     if (paramArrayOfbyte2 == null)
/*     */     {
/* 213 */       throw new NullPointerException("src == null");
/*     */     }
/* 215 */     if (paramInt < 0)
/*     */     {
/* 217 */       throw new IllegalArgumentException("offset hast to be >= 0");
/*     */     }
/* 219 */     if (paramArrayOfbyte2.length + paramInt > paramArrayOfbyte1.length)
/*     */     {
/* 221 */       throw new IllegalArgumentException("src length + offset must not be greater than size of destination");
/*     */     }
/* 223 */     for (byte b = 0; b < paramArrayOfbyte2.length; b++)
/*     */     {
/* 225 */       paramArrayOfbyte1[paramInt + b] = paramArrayOfbyte2[b];
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
/*     */   public static byte[] extractBytesAtOffset(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 239 */     if (paramArrayOfbyte == null)
/*     */     {
/* 241 */       throw new NullPointerException("src == null");
/*     */     }
/* 243 */     if (paramInt1 < 0)
/*     */     {
/* 245 */       throw new IllegalArgumentException("offset hast to be >= 0");
/*     */     }
/* 247 */     if (paramInt2 < 0)
/*     */     {
/* 249 */       throw new IllegalArgumentException("length hast to be >= 0");
/*     */     }
/* 251 */     if (paramInt1 + paramInt2 > paramArrayOfbyte.length)
/*     */     {
/* 253 */       throw new IllegalArgumentException("offset + length must not be greater then size of source array");
/*     */     }
/* 255 */     byte[] arrayOfByte = new byte[paramInt2];
/* 256 */     for (byte b = 0; b < arrayOfByte.length; b++)
/*     */     {
/* 258 */       arrayOfByte[b] = paramArrayOfbyte[paramInt1 + b];
/*     */     }
/* 260 */     return arrayOfByte;
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
/*     */   public static boolean isIndexValid(int paramInt, long paramLong) {
/* 272 */     if (paramLong < 0L)
/*     */     {
/* 274 */       throw new IllegalStateException("index must not be negative");
/*     */     }
/* 276 */     return (paramLong < 1L << paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDigestSize(Digest paramDigest) {
/* 287 */     if (paramDigest == null)
/*     */     {
/* 289 */       throw new NullPointerException("digest == null");
/*     */     }
/* 291 */     String str = paramDigest.getAlgorithmName();
/* 292 */     if (str.equals("SHAKE128"))
/*     */     {
/* 294 */       return 32;
/*     */     }
/* 296 */     if (str.equals("SHAKE256"))
/*     */     {
/* 298 */       return 64;
/*     */     }
/* 300 */     return paramDigest.getDigestSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getTreeIndex(long paramLong, int paramInt) {
/* 305 */     return paramLong >> paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getLeafIndex(long paramLong, int paramInt) {
/* 310 */     return (int)(paramLong & (1L << paramInt) - 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] serialize(Object paramObject) throws IOException {
/* 316 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 317 */     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
/* 318 */     objectOutputStream.writeObject(paramObject);
/* 319 */     objectOutputStream.flush();
/* 320 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object deserialize(byte[] paramArrayOfbyte, Class paramClass) throws IOException, ClassNotFoundException {
/* 326 */     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramArrayOfbyte);
/* 327 */     CheckingStream checkingStream = new CheckingStream(paramClass, byteArrayInputStream);
/*     */     
/* 329 */     Object object = checkingStream.readObject();
/*     */     
/* 331 */     if (checkingStream.available() != 0)
/*     */     {
/* 333 */       throw new IOException("unexpected data found at end of ObjectInputStream");
/*     */     }
/*     */     
/* 336 */     if (paramClass.isInstance(object))
/*     */     {
/* 338 */       return object;
/*     */     }
/*     */ 
/*     */     
/* 342 */     throw new IOException("unexpected class found in ObjectInputStream");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calculateTau(int paramInt1, int paramInt2) {
/* 348 */     byte b1 = 0;
/* 349 */     for (byte b2 = 0; b2 < paramInt2; b2++) {
/*     */       
/* 351 */       if ((paramInt1 >> b2 & 0x1) == 0) {
/*     */         
/* 353 */         b1 = b2;
/*     */         break;
/*     */       } 
/*     */     } 
/* 357 */     return b1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNewBDSInitNeeded(long paramLong, int paramInt1, int paramInt2) {
/* 362 */     if (paramLong == 0L)
/*     */     {
/* 364 */       return false;
/*     */     }
/* 366 */     return (paramLong % (long)Math.pow((1 << paramInt1), (paramInt2 + 1)) == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNewAuthenticationPathNeeded(long paramLong, int paramInt1, int paramInt2) {
/* 371 */     if (paramLong == 0L)
/*     */     {
/* 373 */       return false;
/*     */     }
/* 375 */     return ((paramLong + 1L) % (long)Math.pow((1 << paramInt1), paramInt2) == 0L);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */