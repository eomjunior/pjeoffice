/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.qtesla;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.HashUtils;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Pack;
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
/*     */ class QTesla3p
/*     */ {
/*     */   private static final int PARAM_N = 2048;
/*     */   private static final int PARAM_Q = 856145921;
/*     */   private static final int PARAM_Q_LOG = 30;
/*     */   private static final long PARAM_QINV = 587710463L;
/*     */   private static final long PARAM_BARR_MULT = 5L;
/*     */   private static final int PARAM_BARR_DIV = 32;
/*     */   private static final int PARAM_B = 2097151;
/*     */   private static final int PARAM_B_BITS = 21;
/*     */   private static final int PARAM_K = 5;
/*     */   private static final int PARAM_H = 40;
/*     */   private static final int PARAM_D = 24;
/*     */   private static final int PARAM_GEN_A = 180;
/*     */   private static final int PARAM_KEYGEN_BOUND_E = 901;
/*     */   private static final int PARAM_E = 901;
/*     */   private static final int PARAM_KEYGEN_BOUND_S = 901;
/*     */   private static final int PARAM_S = 901;
/*     */   private static final int PARAM_R2_INVN = 513161157;
/*     */   private static final int CRYPTO_RANDOMBYTES = 32;
/*     */   private static final int CRYPTO_SEEDBYTES = 32;
/*     */   private static final int CRYPTO_C_BYTES = 32;
/*     */   private static final int HM_BYTES = 40;
/*     */   private static final int RADIX32 = 32;
/*     */   static final int CRYPTO_BYTES = 5664;
/*     */   static final int CRYPTO_SECRETKEYBYTES = 12392;
/*     */   static final int CRYPTO_PUBLICKEYBYTES = 38432;
/*     */   private static final int maskb1 = 4194303;
/*     */   
/*     */   static int generateKeyPair(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SecureRandom paramSecureRandom) {
/*  51 */     byte b1 = 0;
/*     */     
/*  53 */     byte[] arrayOfByte1 = new byte[32];
/*     */ 
/*     */     
/*  56 */     byte[] arrayOfByte2 = new byte[256];
/*     */     
/*  58 */     long[] arrayOfLong1 = new long[2048];
/*  59 */     long[] arrayOfLong2 = new long[10240];
/*  60 */     long[] arrayOfLong3 = new long[10240];
/*  61 */     long[] arrayOfLong4 = new long[10240];
/*     */     
/*  63 */     long[] arrayOfLong5 = new long[2048];
/*     */ 
/*     */     
/*  66 */     paramSecureRandom.nextBytes(arrayOfByte1);
/*     */     
/*  68 */     HashUtils.secureHashAlgorithmKECCAK256(arrayOfByte2, 0, 256, arrayOfByte1, 0, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     byte b2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     for (b2 = 0; b2 < 5;) {
/*     */       
/*     */       while (true) {
/*     */         
/*  82 */         Gaussian.sample_gauss_poly(++b1, arrayOfByte2, b2 * 32, arrayOfLong2, b2 * 2048);
/*     */         
/*  84 */         if (!checkPolynomial(arrayOfLong2, b2 * 2048, 901)) {
/*     */           b2++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/*  94 */       Gaussian.sample_gauss_poly(++b1, arrayOfByte2, 160, arrayOfLong1, 0);
/*     */     }
/*  96 */     while (checkPolynomial(arrayOfLong1, 0, 901));
/*     */     
/*  98 */     QTesla3PPolynomial.poly_uniform(arrayOfLong3, arrayOfByte2, 192);
/*  99 */     QTesla3PPolynomial.poly_ntt(arrayOfLong5, arrayOfLong1);
/*     */     
/* 101 */     for (b2 = 0; b2 < 5; b2++) {
/*     */       
/* 103 */       QTesla3PPolynomial.poly_mul(arrayOfLong4, b2 * 2048, arrayOfLong3, b2 * 2048, arrayOfLong5);
/* 104 */       QTesla3PPolynomial.poly_add_correct(arrayOfLong4, b2 * 2048, arrayOfLong4, b2 * 2048, arrayOfLong2, b2 * 2048);
/*     */     } 
/*     */ 
/*     */     
/* 108 */     encodePublicKey(paramArrayOfbyte1, arrayOfLong4, arrayOfByte2, 192);
/* 109 */     encodePrivateKey(paramArrayOfbyte2, arrayOfLong1, arrayOfLong2, arrayOfByte2, 192, paramArrayOfbyte1);
/*     */     
/* 111 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int generateSignature(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2, byte[] paramArrayOfbyte3, SecureRandom paramSecureRandom) {
/* 119 */     byte[] arrayOfByte1 = new byte[32];
/* 120 */     byte[] arrayOfByte2 = new byte[32];
/* 121 */     byte[] arrayOfByte3 = new byte[144];
/* 122 */     int[] arrayOfInt = new int[40];
/* 123 */     short[] arrayOfShort = new short[40];
/* 124 */     long[] arrayOfLong1 = new long[2048];
/*     */     
/* 126 */     long[] arrayOfLong2 = new long[2048];
/* 127 */     long[] arrayOfLong3 = new long[2048];
/* 128 */     long[] arrayOfLong4 = new long[2048];
/*     */     
/* 130 */     long[] arrayOfLong5 = new long[10240];
/* 131 */     long[] arrayOfLong6 = new long[10240];
/* 132 */     long[] arrayOfLong7 = new long[10240];
/*     */ 
/*     */     
/* 135 */     byte b = 0;
/* 136 */     boolean bool = false;
/*     */     
/* 138 */     System.arraycopy(paramArrayOfbyte3, 12320, arrayOfByte3, 0, 32);
/*     */ 
/*     */     
/* 141 */     byte[] arrayOfByte4 = new byte[32];
/* 142 */     paramSecureRandom.nextBytes(arrayOfByte4);
/* 143 */     System.arraycopy(arrayOfByte4, 0, arrayOfByte3, 32, 32);
/*     */ 
/*     */     
/* 146 */     HashUtils.secureHashAlgorithmKECCAK256(arrayOfByte3, 64, 40, paramArrayOfbyte2, 0, paramInt2);
/*     */ 
/*     */ 
/*     */     
/* 150 */     HashUtils.secureHashAlgorithmKECCAK256(arrayOfByte2, 0, 32, arrayOfByte3, 0, arrayOfByte3.length - 40);
/*     */ 
/*     */ 
/*     */     
/* 154 */     System.arraycopy(paramArrayOfbyte3, 12352, arrayOfByte3, arrayOfByte3.length - 40, 40);
/*     */     
/* 156 */     QTesla3PPolynomial.poly_uniform(arrayOfLong7, paramArrayOfbyte3, 12288);
/*     */ 
/*     */     
/*     */     while (true) {
/* 160 */       sample_y(arrayOfLong1, arrayOfByte2, 0, ++b);
/*     */       
/* 162 */       QTesla3PPolynomial.poly_ntt(arrayOfLong2, arrayOfLong1); byte b1;
/* 163 */       for (b1 = 0; b1 < 5; b1++)
/*     */       {
/* 165 */         QTesla3PPolynomial.poly_mul(arrayOfLong5, b1 * 2048, arrayOfLong7, b1 * 2048, arrayOfLong2);
/*     */       }
/*     */       
/* 168 */       hashFunction(arrayOfByte1, 0, arrayOfLong5, arrayOfByte3, 64);
/* 169 */       encodeC(arrayOfInt, arrayOfShort, arrayOfByte1, 0);
/*     */       
/* 171 */       QTesla3PPolynomial.sparse_mul8(arrayOfLong3, paramArrayOfbyte3, arrayOfInt, arrayOfShort);
/*     */       
/* 173 */       QTesla3PPolynomial.poly_add(arrayOfLong4, arrayOfLong1, arrayOfLong3);
/*     */       
/* 175 */       if (testRejection(arrayOfLong4)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 180 */       for (b1 = 0; b1 < 5; b1++) {
/*     */         
/* 182 */         QTesla3PPolynomial.sparse_mul8(arrayOfLong6, b1 * 2048, paramArrayOfbyte3, 2048 * (b1 + 1), arrayOfInt, arrayOfShort);
/* 183 */         QTesla3PPolynomial.poly_sub(arrayOfLong5, b1 * 2048, arrayOfLong5, b1 * 2048, arrayOfLong6, b1 * 2048);
/* 184 */         bool = test_correctness(arrayOfLong5, b1 * 2048);
/* 185 */         if (bool) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 190 */       if (bool) {
/*     */         continue;
/*     */       }
/*     */       break;
/*     */     } 
/* 195 */     encodeSignature(paramArrayOfbyte1, 0, arrayOfByte1, 0, arrayOfLong4);
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int verifying(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2, byte[] paramArrayOfbyte3) {
/* 207 */     byte[] arrayOfByte1 = new byte[32];
/* 208 */     byte[] arrayOfByte2 = new byte[32];
/* 209 */     byte[] arrayOfByte3 = new byte[32];
/* 210 */     byte[] arrayOfByte4 = new byte[80];
/* 211 */     int[] arrayOfInt1 = new int[40];
/* 212 */     short[] arrayOfShort = new short[40];
/* 213 */     int[] arrayOfInt2 = new int[10240];
/* 214 */     long[] arrayOfLong1 = new long[10240];
/* 215 */     long[] arrayOfLong2 = new long[10240];
/* 216 */     long[] arrayOfLong3 = new long[10240];
/*     */     
/* 218 */     long[] arrayOfLong4 = new long[2048];
/* 219 */     long[] arrayOfLong5 = new long[2048];
/*     */     
/* 221 */     byte b = 0;
/*     */     
/* 223 */     if (paramInt2 != 5664)
/*     */     {
/* 225 */       return -1;
/*     */     }
/*     */     
/* 228 */     decodeSignature(arrayOfByte1, arrayOfLong4, paramArrayOfbyte2, paramInt1);
/* 229 */     if (testZ(arrayOfLong4))
/*     */     {
/* 231 */       return -2;
/*     */     }
/* 233 */     decodePublicKey(arrayOfInt2, arrayOfByte3, 0, paramArrayOfbyte3);
/*     */ 
/*     */     
/* 236 */     HashUtils.secureHashAlgorithmKECCAK256(arrayOfByte4, 0, 40, paramArrayOfbyte1, 0, paramArrayOfbyte1.length);
/*     */ 
/*     */     
/* 239 */     HashUtils.secureHashAlgorithmKECCAK256(arrayOfByte4, 40, 40, paramArrayOfbyte3, 0, 38400);
/*     */ 
/*     */ 
/*     */     
/* 243 */     QTesla3PPolynomial.poly_uniform(arrayOfLong2, arrayOfByte3, 0);
/* 244 */     encodeC(arrayOfInt1, arrayOfShort, arrayOfByte1, 0);
/* 245 */     QTesla3PPolynomial.poly_ntt(arrayOfLong5, arrayOfLong4);
/*     */     
/* 247 */     for (b = 0; b < 5; b++) {
/*     */       
/* 249 */       QTesla3PPolynomial.sparse_mul32(arrayOfLong3, b * 2048, arrayOfInt2, b * 2048, arrayOfInt1, arrayOfShort);
/* 250 */       QTesla3PPolynomial.poly_mul(arrayOfLong1, b * 2048, arrayOfLong2, b * 2048, arrayOfLong5);
/* 251 */       QTesla3PPolynomial.poly_sub(arrayOfLong1, b * 2048, arrayOfLong1, b * 2048, arrayOfLong3, b * 2048);
/*     */     } 
/*     */     
/* 254 */     hashFunction(arrayOfByte2, 0, arrayOfLong1, arrayOfByte4, 0);
/*     */     
/* 256 */     if (!memoryEqual(arrayOfByte1, 0, arrayOfByte2, 0, 32))
/*     */     {
/* 258 */       return -3;
/*     */     }
/*     */     
/* 261 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodePrivateKey(byte[] paramArrayOfbyte1, long[] paramArrayOflong1, long[] paramArrayOflong2, byte[] paramArrayOfbyte2, int paramInt, byte[] paramArrayOfbyte3) {
/* 267 */     byte b2 = 0;
/* 268 */     byte b3 = 0;
/*     */     byte b1;
/* 270 */     for (b1 = 0; b1 < 'ࠀ'; b1++)
/*     */     {
/* 272 */       paramArrayOfbyte1[b3 + b1] = (byte)(int)paramArrayOflong1[b1];
/*     */     }
/* 274 */     b3 += 2048;
/*     */     
/* 276 */     for (b2 = 0; b2 < 5; b2++) {
/*     */       
/* 278 */       for (b1 = 0; b1 < 'ࠀ'; b1++)
/*     */       {
/* 280 */         paramArrayOfbyte1[b3 + b2 * 2048 + b1] = (byte)(int)paramArrayOflong2[b2 * 2048 + b1];
/*     */       }
/*     */     } 
/* 283 */     b3 += 10240;
/*     */     
/* 285 */     System.arraycopy(paramArrayOfbyte2, paramInt, paramArrayOfbyte1, b3, 64);
/* 286 */     b3 += 64;
/*     */ 
/*     */     
/* 289 */     HashUtils.secureHashAlgorithmKECCAK256(paramArrayOfbyte1, b3, 40, paramArrayOfbyte3, 0, 38400);
/*     */ 
/*     */     
/* 292 */     b3 += 40;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodePublicKey(byte[] paramArrayOfbyte1, long[] paramArrayOflong, byte[] paramArrayOfbyte2, int paramInt) {
/* 299 */     byte b1 = 0;
/*     */     
/* 301 */     for (byte b2 = 0; b2 < '▀'; b2 += 15) {
/*     */       
/* 303 */       at(paramArrayOfbyte1, b2, 0, (int)(paramArrayOflong[b1] | paramArrayOflong[b1 + 1] << 30L));
/* 304 */       at(paramArrayOfbyte1, b2, 1, (int)(paramArrayOflong[b1 + 1] >> 2L | paramArrayOflong[b1 + 2] << 28L));
/* 305 */       at(paramArrayOfbyte1, b2, 2, (int)(paramArrayOflong[b1 + 2] >> 4L | paramArrayOflong[b1 + 3] << 26L));
/* 306 */       at(paramArrayOfbyte1, b2, 3, (int)(paramArrayOflong[b1 + 3] >> 6L | paramArrayOflong[b1 + 4] << 24L));
/* 307 */       at(paramArrayOfbyte1, b2, 4, (int)(paramArrayOflong[b1 + 4] >> 8L | paramArrayOflong[b1 + 5] << 22L));
/* 308 */       at(paramArrayOfbyte1, b2, 5, (int)(paramArrayOflong[b1 + 5] >> 10L | paramArrayOflong[b1 + 6] << 20L));
/* 309 */       at(paramArrayOfbyte1, b2, 6, (int)(paramArrayOflong[b1 + 6] >> 12L | paramArrayOflong[b1 + 7] << 18L));
/* 310 */       at(paramArrayOfbyte1, b2, 7, (int)(paramArrayOflong[b1 + 7] >> 14L | paramArrayOflong[b1 + 8] << 16L));
/* 311 */       at(paramArrayOfbyte1, b2, 8, (int)(paramArrayOflong[b1 + 8] >> 16L | paramArrayOflong[b1 + 9] << 14L));
/* 312 */       at(paramArrayOfbyte1, b2, 9, (int)(paramArrayOflong[b1 + 9] >> 18L | paramArrayOflong[b1 + 10] << 12L));
/* 313 */       at(paramArrayOfbyte1, b2, 10, (int)(paramArrayOflong[b1 + 10] >> 20L | paramArrayOflong[b1 + 11] << 10L));
/* 314 */       at(paramArrayOfbyte1, b2, 11, (int)(paramArrayOflong[b1 + 11] >> 22L | paramArrayOflong[b1 + 12] << 8L));
/* 315 */       at(paramArrayOfbyte1, b2, 12, (int)(paramArrayOflong[b1 + 12] >> 24L | paramArrayOflong[b1 + 13] << 6L));
/* 316 */       at(paramArrayOfbyte1, b2, 13, (int)(paramArrayOflong[b1 + 13] >> 26L | paramArrayOflong[b1 + 14] << 4L));
/* 317 */       at(paramArrayOfbyte1, b2, 14, (int)(paramArrayOflong[b1 + 14] >> 28L | paramArrayOflong[b1 + 15] << 2L));
/* 318 */       b1 += 16;
/*     */     } 
/*     */     
/* 321 */     System.arraycopy(paramArrayOfbyte2, paramInt, paramArrayOfbyte1, 38400, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void decodePublicKey(int[] paramArrayOfint, byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
/* 329 */     boolean bool = false;
/* 330 */     byte[] arrayOfByte = paramArrayOfbyte2;
/* 331 */     int i = 1073741823;
/*     */ 
/*     */     
/* 334 */     for (byte b = 0; b < '⠀'; b += 16) {
/*     */       
/* 336 */       paramArrayOfint[b] = at(arrayOfByte, bool, 0) & i;
/* 337 */       paramArrayOfint[b + 1] = (at(arrayOfByte, bool, 0) >>> 30 | at(arrayOfByte, bool, 1) << 2) & i;
/* 338 */       paramArrayOfint[b + 2] = (at(arrayOfByte, bool, 1) >>> 28 | at(arrayOfByte, bool, 2) << 4) & i;
/* 339 */       paramArrayOfint[b + 3] = (at(arrayOfByte, bool, 2) >>> 26 | at(arrayOfByte, bool, 3) << 6) & i;
/* 340 */       paramArrayOfint[b + 4] = (at(arrayOfByte, bool, 3) >>> 24 | at(arrayOfByte, bool, 4) << 8) & i;
/* 341 */       paramArrayOfint[b + 5] = (at(arrayOfByte, bool, 4) >>> 22 | at(arrayOfByte, bool, 5) << 10) & i;
/* 342 */       paramArrayOfint[b + 6] = (at(arrayOfByte, bool, 5) >>> 20 | at(arrayOfByte, bool, 6) << 12) & i;
/* 343 */       paramArrayOfint[b + 7] = (at(arrayOfByte, bool, 6) >>> 18 | at(arrayOfByte, bool, 7) << 14) & i;
/* 344 */       paramArrayOfint[b + 8] = (at(arrayOfByte, bool, 7) >>> 16 | at(arrayOfByte, bool, 8) << 16) & i;
/* 345 */       paramArrayOfint[b + 9] = (at(arrayOfByte, bool, 8) >>> 14 | at(arrayOfByte, bool, 9) << 18) & i;
/* 346 */       paramArrayOfint[b + 10] = (at(arrayOfByte, bool, 9) >>> 12 | at(arrayOfByte, bool, 10) << 20) & i;
/* 347 */       paramArrayOfint[b + 11] = (at(arrayOfByte, bool, 10) >>> 10 | at(arrayOfByte, bool, 11) << 22) & i;
/* 348 */       paramArrayOfint[b + 12] = (at(arrayOfByte, bool, 11) >>> 8 | at(arrayOfByte, bool, 12) << 24) & i;
/* 349 */       paramArrayOfint[b + 13] = (at(arrayOfByte, bool, 12) >>> 6 | at(arrayOfByte, bool, 13) << 26) & i;
/* 350 */       paramArrayOfint[b + 14] = (at(arrayOfByte, bool, 13) >>> 4 | at(arrayOfByte, bool, 14) << 28) & i;
/* 351 */       paramArrayOfint[b + 15] = at(arrayOfByte, bool, 14) >>> 2 & i;
/* 352 */       bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 356 */     System.arraycopy(paramArrayOfbyte2, 38400, paramArrayOfbyte1, paramInt, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean testZ(long[] paramArrayOflong) {
/* 364 */     for (byte b = 0; b < 'ࠀ'; b++) {
/*     */ 
/*     */       
/* 367 */       if (paramArrayOflong[b] < -2096250L || paramArrayOflong[b] > 2096250L)
/*     */       {
/*     */         
/* 370 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 376 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodeSignature(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, long[] paramArrayOflong) {
/* 384 */     byte b1 = 0;
/*     */     
/* 386 */     for (byte b2 = 0; b2 < 'ր'; b2 += 11) {
/*     */       
/* 388 */       at(paramArrayOfbyte1, b2, 0, (int)(paramArrayOflong[b1 + 0] & 0x3FFFFFL | paramArrayOflong[b1 + 1] << 22L));
/* 389 */       at(paramArrayOfbyte1, b2, 1, (int)(paramArrayOflong[b1 + 1] >>> 10L & 0xFFFL | paramArrayOflong[b1 + 2] << 12L));
/* 390 */       at(paramArrayOfbyte1, b2, 2, (int)(paramArrayOflong[b1 + 2] >>> 20L & 0x3L | (paramArrayOflong[b1 + 3] & 0x3FFFFFL) << 2L | paramArrayOflong[b1 + 4] << 24L));
/* 391 */       at(paramArrayOfbyte1, b2, 3, (int)(paramArrayOflong[b1 + 4] >>> 8L & 0x3FFFL | paramArrayOflong[b1 + 5] << 14L));
/* 392 */       at(paramArrayOfbyte1, b2, 4, (int)(paramArrayOflong[b1 + 5] >>> 18L & 0xFL | (paramArrayOflong[b1 + 6] & 0x3FFFFFL) << 4L | paramArrayOflong[b1 + 7] << 26L));
/* 393 */       at(paramArrayOfbyte1, b2, 5, (int)(paramArrayOflong[b1 + 7] >>> 6L & 0xFFFFL | paramArrayOflong[b1 + 8] << 16L));
/* 394 */       at(paramArrayOfbyte1, b2, 6, (int)(paramArrayOflong[b1 + 8] >>> 16L & 0x3FL | (paramArrayOflong[b1 + 9] & 0x3FFFFFL) << 6L | paramArrayOflong[b1 + 10] << 28L));
/* 395 */       at(paramArrayOfbyte1, b2, 7, (int)(paramArrayOflong[b1 + 10] >>> 4L & 0x3FFFFL | paramArrayOflong[b1 + 11] << 18L));
/* 396 */       at(paramArrayOfbyte1, b2, 8, (int)(paramArrayOflong[b1 + 11] >>> 14L & 0xFFL | (paramArrayOflong[b1 + 12] & 0x3FFFFFL) << 8L | paramArrayOflong[b1 + 13] << 30L));
/* 397 */       at(paramArrayOfbyte1, b2, 9, (int)(paramArrayOflong[b1 + 13] >>> 2L & 0xFFFFFL | paramArrayOflong[b1 + 14] << 20L));
/* 398 */       at(paramArrayOfbyte1, b2, 10, (int)(paramArrayOflong[b1 + 14] >>> 12L & 0x3FFL | paramArrayOflong[b1 + 15] << 10L));
/* 399 */       b1 += 16;
/*     */     } 
/*     */     
/* 402 */     System.arraycopy(paramArrayOfbyte2, paramInt2, paramArrayOfbyte1, paramInt1 + 5632, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   static void decodeSignature(byte[] paramArrayOfbyte1, long[] paramArrayOflong, byte[] paramArrayOfbyte2, int paramInt) {
/* 407 */     boolean bool = false;
/* 408 */     for (byte b = 0; b < 'ࠀ'; b += 16) {
/*     */       
/* 410 */       int i = at(paramArrayOfbyte2, bool, 0);
/* 411 */       int j = at(paramArrayOfbyte2, bool, 1);
/* 412 */       int k = at(paramArrayOfbyte2, bool, 2);
/* 413 */       int m = at(paramArrayOfbyte2, bool, 3);
/* 414 */       int n = at(paramArrayOfbyte2, bool, 4);
/* 415 */       int i1 = at(paramArrayOfbyte2, bool, 5);
/* 416 */       int i2 = at(paramArrayOfbyte2, bool, 6);
/* 417 */       int i3 = at(paramArrayOfbyte2, bool, 7);
/* 418 */       int i4 = at(paramArrayOfbyte2, bool, 8);
/* 419 */       int i5 = at(paramArrayOfbyte2, bool, 9);
/* 420 */       int i6 = at(paramArrayOfbyte2, bool, 10);
/*     */       
/* 422 */       paramArrayOflong[b] = (i << 10 >> 10);
/* 423 */       paramArrayOflong[b + 1] = (i >>> 22 | j << 20 >> 10);
/* 424 */       paramArrayOflong[b + 2] = (j >>> 12 | k << 30 >> 10);
/* 425 */       paramArrayOflong[b + 3] = (k << 8 >> 10);
/* 426 */       paramArrayOflong[b + 4] = (k >>> 24 | m << 18 >> 10);
/* 427 */       paramArrayOflong[b + 5] = (m >>> 14 | n << 28 >> 10);
/* 428 */       paramArrayOflong[b + 6] = (n << 6 >> 10);
/* 429 */       paramArrayOflong[b + 7] = (n >>> 26 | i1 << 16 >> 10);
/* 430 */       paramArrayOflong[b + 8] = (i1 >>> 16 | i2 << 26 >> 10);
/* 431 */       paramArrayOflong[b + 9] = (i2 << 4 >> 10);
/* 432 */       paramArrayOflong[b + 10] = (i2 >>> 28 | i3 << 14 >> 10);
/* 433 */       paramArrayOflong[b + 11] = (i3 >>> 18 | i4 << 24 >> 10);
/* 434 */       paramArrayOflong[b + 12] = (i4 << 2 >> 10);
/* 435 */       paramArrayOflong[b + 13] = (i4 >>> 30 | i5 << 12 >> 10);
/* 436 */       paramArrayOflong[b + 14] = (i5 >>> 20 | i6 << 22 >> 10);
/* 437 */       paramArrayOflong[b + 15] = (i6 >> 10);
/* 438 */       bool += true;
/*     */     } 
/* 440 */     System.arraycopy(paramArrayOfbyte2, paramInt + 5632, paramArrayOfbyte1, 0, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   static void encodeC(int[] paramArrayOfint, short[] paramArrayOfshort, byte[] paramArrayOfbyte, int paramInt) {
/* 445 */     byte b1 = 0;
/*     */     
/* 447 */     short s = 0;
/* 448 */     short[] arrayOfShort = new short[2048];
/* 449 */     byte[] arrayOfByte = new byte[168];
/*     */ 
/*     */     
/* 452 */     s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 168, s, paramArrayOfbyte, paramInt, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     Arrays.fill(arrayOfShort, (short)0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 464 */     for (byte b2 = 0; b2 < 40; ) {
/*     */       
/* 466 */       if (b1 > '¥') {
/*     */ 
/*     */         
/* 469 */         s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 168, s, paramArrayOfbyte, paramInt, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 475 */         b1 = 0;
/*     */       } 
/*     */       
/* 478 */       int i = arrayOfByte[b1] << 8 | arrayOfByte[b1 + 1] & 0xFF;
/* 479 */       i &= 0x7FF;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 484 */       if (arrayOfShort[i] == 0) {
/*     */         
/* 486 */         if ((arrayOfByte[b1 + 2] & 0x1) == 1) {
/*     */           
/* 488 */           arrayOfShort[i] = -1;
/*     */         }
/*     */         else {
/*     */           
/* 492 */           arrayOfShort[i] = 1;
/*     */         } 
/*     */         
/* 495 */         paramArrayOfint[b2] = i;
/* 496 */         paramArrayOfshort[b2] = arrayOfShort[i];
/* 497 */         b2++;
/*     */       } 
/*     */       
/* 500 */       b1 += 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void hashFunction(byte[] paramArrayOfbyte1, int paramInt1, long[] paramArrayOflong, byte[] paramArrayOfbyte2, int paramInt2) {
/* 508 */     byte[] arrayOfByte = new byte[10320];
/*     */     
/* 510 */     for (byte b = 0; b < 5; b++) {
/*     */       
/* 512 */       int i = b * 2048;
/* 513 */       for (byte b1 = 0; b1 < 'ࠀ'; b1++) {
/*     */         
/* 515 */         int m = (int)paramArrayOflong[i];
/*     */         
/* 517 */         int j = 428072960 - m >> 31;
/* 518 */         m = m - 856145921 & j | m & (j ^ 0xFFFFFFFF);
/*     */         
/* 520 */         int k = m & 0xFFFFFF;
/*     */         
/* 522 */         j = 8388608 - k >> 31;
/* 523 */         k = k - 16777216 & j | k & (j ^ 0xFFFFFFFF);
/* 524 */         arrayOfByte[i++] = (byte)(m - k >> 24);
/*     */       } 
/*     */     } 
/* 527 */     System.arraycopy(paramArrayOfbyte2, paramInt2, arrayOfByte, 10240, 80);
/*     */     
/* 529 */     HashUtils.secureHashAlgorithmKECCAK256(paramArrayOfbyte1, paramInt1, 32, arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int lE24BitToInt(byte[] paramArrayOfbyte, int paramInt) {
/* 536 */     int i = paramArrayOfbyte[paramInt] & 0xFF;
/* 537 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 538 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/* 539 */     return i;
/*     */   }
/*     */ 
/*     */   
/* 543 */   private static int NBLOCKS_SHAKE = 56;
/* 544 */   private static int BPLUS1BYTES = 3;
/*     */ 
/*     */ 
/*     */   
/*     */   static void sample_y(long[] paramArrayOflong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 549 */     byte b = 0; int i = 0, j = 2048;
/* 550 */     byte[] arrayOfByte = new byte[2048 * BPLUS1BYTES + 1];
/* 551 */     int k = BPLUS1BYTES;
/* 552 */     short s = (short)(paramInt2 << 8);
/*     */     
/* 554 */     s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK256Simple(arrayOfByte, 0, 2048 * k, s, paramArrayOfbyte, paramInt1, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 559 */     while (b < 'ࠀ') {
/*     */       
/* 561 */       if (i >= j * k) {
/*     */         
/* 563 */         j = NBLOCKS_SHAKE;
/* 564 */         s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK256Simple(arrayOfByte, 0, 2048 * k, s, paramArrayOfbyte, paramInt1, 32);
/*     */ 
/*     */         
/* 567 */         i = 0;
/*     */       } 
/* 569 */       paramArrayOflong[b] = (lE24BitToInt(arrayOfByte, i) & 0x3FFFFF);
/* 570 */       paramArrayOflong[b] = paramArrayOflong[b] - 2097151L;
/* 571 */       if (paramArrayOflong[b] != 2097152L)
/*     */       {
/* 573 */         b++;
/*     */       }
/* 575 */       i += k;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void at(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
/* 582 */     Pack.intToLittleEndian(paramInt3, paramArrayOfbyte, paramInt1 * 4 + paramInt2 * 4);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int at(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 587 */     int i = paramInt1 * 4 + paramInt2 * 4;
/*     */     
/* 589 */     int j = paramArrayOfbyte[i] & 0xFF;
/* 590 */     j |= (paramArrayOfbyte[++i] & 0xFF) << 8;
/* 591 */     j |= (paramArrayOfbyte[++i] & 0xFF) << 16;
/* 592 */     j |= paramArrayOfbyte[++i] << 24;
/* 593 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean test_correctness(long[] paramArrayOflong, int paramInt) {
/* 604 */     for (byte b = 0; b < 'ࠀ'; b++) {
/*     */ 
/*     */       
/* 607 */       int i = (int)(428072960L - paramArrayOflong[paramInt + b]) >> 31;
/* 608 */       int k = (int)(paramArrayOflong[paramInt + b] - 856145921L & i | paramArrayOflong[paramInt + b] & (i ^ 0xFFFFFFFF));
/*     */       
/* 610 */       int m = (absolute(k) - 428072059 ^ 0xFFFFFFFF) >>> 31;
/*     */       
/* 612 */       int j = k;
/* 613 */       k = k + 8388608 - 1 >> 24;
/* 614 */       k = j - (k << 24);
/*     */       
/* 616 */       int n = (absolute(k) - 8387707 ^ 0xFFFFFFFF) >>> 31;
/*     */       
/* 618 */       if ((m | n) == 1)
/*     */       {
/* 620 */         return true;
/*     */       }
/*     */     } 
/* 623 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean testRejection(long[] paramArrayOflong) {
/* 630 */     int i = 0;
/*     */     
/* 632 */     for (byte b = 0; b < 'ࠀ'; b++)
/*     */     {
/* 634 */       i = (int)(i | 2096250L - absolute(paramArrayOflong[b]));
/*     */     }
/*     */ 
/*     */     
/* 638 */     return (i >>> 31 > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int absolute(int paramInt) {
/* 645 */     return (paramInt >> 31 ^ paramInt) - (paramInt >> 31);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long absolute(long paramLong) {
/* 652 */     return (paramLong >> 63L ^ paramLong) - (paramLong >> 63L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean checkPolynomial(long[] paramArrayOflong, int paramInt1, int paramInt2) {
/* 660 */     int i = 0; char c = 'ࠀ';
/*     */     
/* 662 */     long[] arrayOfLong = new long[2048];
/*     */     byte b;
/* 664 */     for (b = 0; b < 'ࠀ'; b++)
/*     */     {
/* 666 */       arrayOfLong[b] = absolute((int)paramArrayOflong[paramInt1 + b]);
/*     */     }
/*     */     
/* 669 */     for (b = 0; b < 40; b++) {
/*     */       
/* 671 */       for (byte b1 = 0; b1 < c - 1; b1++) {
/*     */ 
/*     */         
/* 674 */         long l2 = arrayOfLong[b1 + 1] - arrayOfLong[b1] >> 31L;
/* 675 */         long l1 = arrayOfLong[b1 + 1] & l2 | arrayOfLong[b1] & (l2 ^ 0xFFFFFFFFFFFFFFFFL);
/* 676 */         arrayOfLong[b1 + 1] = arrayOfLong[b1] & l2 | arrayOfLong[b1 + 1] & (l2 ^ 0xFFFFFFFFFFFFFFFFL);
/* 677 */         arrayOfLong[b1] = l1;
/*     */       } 
/* 679 */       i += (int)arrayOfLong[c - 1];
/* 680 */       c--;
/*     */     } 
/*     */     
/* 683 */     return (i > paramInt2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean memoryEqual(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
/* 851 */     if (paramInt1 + paramInt3 <= paramArrayOfbyte1.length && paramInt2 + paramInt3 <= paramArrayOfbyte2.length) {
/*     */ 
/*     */       
/* 854 */       for (byte b = 0; b < paramInt3; b++) {
/*     */ 
/*     */         
/* 857 */         if (paramArrayOfbyte1[paramInt1 + b] != paramArrayOfbyte2[paramInt2 + b])
/*     */         {
/*     */           
/* 860 */           return false;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 866 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 872 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/QTesla3p.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */