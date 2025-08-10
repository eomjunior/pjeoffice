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
/*     */ 
/*     */ class QTesla1p
/*     */ {
/*     */   private static final int PARAM_N = 1024;
/*     */   private static final int PARAM_Q = 343576577;
/*     */   private static final int PARAM_Q_LOG = 29;
/*     */   private static final long PARAM_QINV = 2205847551L;
/*     */   private static final int PARAM_BARR_MULT = 3;
/*     */   private static final int PARAM_BARR_DIV = 30;
/*     */   private static final int PARAM_B = 524287;
/*     */   private static final int PARAM_B_BITS = 19;
/*     */   private static final int PARAM_S_BITS = 8;
/*     */   private static final int PARAM_K = 4;
/*     */   private static final int PARAM_H = 25;
/*     */   private static final int PARAM_D = 22;
/*     */   private static final int PARAM_GEN_A = 108;
/*     */   private static final int PARAM_KEYGEN_BOUND_E = 554;
/*     */   private static final int PARAM_E = 554;
/*     */   private static final int PARAM_KEYGEN_BOUND_S = 554;
/*     */   private static final int PARAM_S = 554;
/*     */   private static final int PARAM_R2_INVN = 13632409;
/*     */   private static final int CRYPTO_RANDOMBYTES = 32;
/*     */   private static final int CRYPTO_SEEDBYTES = 32;
/*     */   private static final int CRYPTO_C_BYTES = 32;
/*     */   private static final int HM_BYTES = 40;
/*     */   private static final int RADIX32 = 32;
/*     */   static final int CRYPTO_BYTES = 2592;
/*     */   static final int CRYPTO_SECRETKEYBYTES = 5224;
/*     */   static final int CRYPTO_PUBLICKEYBYTES = 14880;
/*     */   private static final int maskb1 = 1048575;
/*     */   
/*     */   static int generateKeyPair(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SecureRandom paramSecureRandom) {
/*  53 */     byte b1 = 0;
/*     */     
/*  55 */     byte[] arrayOfByte1 = new byte[32];
/*     */ 
/*     */     
/*  58 */     byte[] arrayOfByte2 = new byte[224];
/*     */     
/*  60 */     int[] arrayOfInt1 = new int[1024];
/*  61 */     int[] arrayOfInt2 = new int[4096];
/*  62 */     int[] arrayOfInt3 = new int[4096];
/*  63 */     int[] arrayOfInt4 = new int[4096];
/*     */     
/*  65 */     int[] arrayOfInt5 = new int[1024];
/*     */ 
/*     */ 
/*     */     
/*  69 */     paramSecureRandom.nextBytes(arrayOfByte1);
/*     */     
/*  71 */     HashUtils.secureHashAlgorithmKECCAK128(arrayOfByte2, 0, 224, arrayOfByte1, 0, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     byte b2;
/*     */ 
/*     */ 
/*     */     
/*  80 */     for (b2 = 0; b2 < 4;) {
/*     */       
/*     */       while (true) {
/*     */         
/*  84 */         Gaussian.sample_gauss_poly(++b1, arrayOfByte2, b2 * 32, arrayOfInt2, b2 * 1024);
/*     */         
/*  86 */         if (!checkPolynomial(arrayOfInt2, b2 * 1024, 554)) {
/*     */           b2++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/*  96 */       Gaussian.sample_gauss_poly(++b1, arrayOfByte2, 128, arrayOfInt1, 0);
/*     */     }
/*  98 */     while (checkPolynomial(arrayOfInt1, 0, 554));
/*     */     
/* 100 */     QTesla1PPolynomial.poly_uniform(arrayOfInt3, arrayOfByte2, 160);
/* 101 */     QTesla1PPolynomial.poly_ntt(arrayOfInt5, arrayOfInt1);
/*     */     
/* 103 */     for (b2 = 0; b2 < 4; b2++) {
/*     */       
/* 105 */       QTesla1PPolynomial.poly_mul(arrayOfInt4, b2 * 1024, arrayOfInt3, b2 * 1024, arrayOfInt5);
/* 106 */       QTesla1PPolynomial.poly_add_correct(arrayOfInt4, b2 * 1024, arrayOfInt4, b2 * 1024, arrayOfInt2, b2 * 1024);
/*     */     } 
/*     */ 
/*     */     
/* 110 */     encodePublicKey(paramArrayOfbyte1, arrayOfInt4, arrayOfByte2, 160);
/* 111 */     encodePrivateKey(paramArrayOfbyte2, arrayOfInt1, arrayOfInt2, arrayOfByte2, 160, paramArrayOfbyte1);
/*     */     
/* 113 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int generateSignature(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2, byte[] paramArrayOfbyte3, SecureRandom paramSecureRandom) {
/* 119 */     byte[] arrayOfByte1 = new byte[32];
/* 120 */     byte[] arrayOfByte2 = new byte[32];
/* 121 */     byte[] arrayOfByte3 = new byte[144];
/* 122 */     int[] arrayOfInt1 = new int[25];
/* 123 */     short[] arrayOfShort = new short[25];
/* 124 */     int[] arrayOfInt2 = new int[1024];
/*     */     
/* 126 */     int[] arrayOfInt3 = new int[1024];
/* 127 */     int[] arrayOfInt4 = new int[1024];
/* 128 */     int[] arrayOfInt5 = new int[1024];
/*     */     
/* 130 */     int[] arrayOfInt6 = new int[4096];
/* 131 */     int[] arrayOfInt7 = new int[4096];
/* 132 */     int[] arrayOfInt8 = new int[4096];
/*     */ 
/*     */     
/* 135 */     byte b = 0;
/* 136 */     boolean bool = false;
/*     */     
/* 138 */     System.arraycopy(paramArrayOfbyte3, 5152, arrayOfByte3, 0, 32);
/*     */ 
/*     */     
/* 141 */     byte[] arrayOfByte4 = new byte[32];
/* 142 */     paramSecureRandom.nextBytes(arrayOfByte4);
/* 143 */     System.arraycopy(arrayOfByte4, 0, arrayOfByte3, 32, 32);
/*     */ 
/*     */     
/* 146 */     HashUtils.secureHashAlgorithmKECCAK128(arrayOfByte3, 64, 40, paramArrayOfbyte2, 0, paramInt2);
/*     */ 
/*     */ 
/*     */     
/* 150 */     HashUtils.secureHashAlgorithmKECCAK128(arrayOfByte2, 0, 32, arrayOfByte3, 0, arrayOfByte3.length - 40);
/*     */ 
/*     */ 
/*     */     
/* 154 */     System.arraycopy(paramArrayOfbyte3, 5184, arrayOfByte3, arrayOfByte3.length - 40, 40);
/*     */     
/* 156 */     QTesla1PPolynomial.poly_uniform(arrayOfInt8, paramArrayOfbyte3, 5120);
/*     */ 
/*     */     
/*     */     while (true) {
/* 160 */       sample_y(arrayOfInt2, arrayOfByte2, 0, ++b);
/*     */       
/* 162 */       QTesla1PPolynomial.poly_ntt(arrayOfInt3, arrayOfInt2); byte b1;
/* 163 */       for (b1 = 0; b1 < 4; b1++)
/*     */       {
/* 165 */         QTesla1PPolynomial.poly_mul(arrayOfInt6, b1 * 1024, arrayOfInt8, b1 * 1024, arrayOfInt3);
/*     */       }
/*     */       
/* 168 */       hashFunction(arrayOfByte1, 0, arrayOfInt6, arrayOfByte3, 64);
/* 169 */       encodeC(arrayOfInt1, arrayOfShort, arrayOfByte1, 0);
/*     */       
/* 171 */       QTesla1PPolynomial.sparse_mul8(arrayOfInt4, 0, paramArrayOfbyte3, 0, arrayOfInt1, arrayOfShort);
/*     */       
/* 173 */       QTesla1PPolynomial.poly_add(arrayOfInt5, arrayOfInt2, arrayOfInt4);
/*     */       
/* 175 */       if (testRejection(arrayOfInt5)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 180 */       for (b1 = 0; b1 < 4; b1++) {
/*     */         
/* 182 */         QTesla1PPolynomial.sparse_mul8(arrayOfInt7, b1 * 1024, paramArrayOfbyte3, 1024 * (b1 + 1), arrayOfInt1, arrayOfShort);
/* 183 */         QTesla1PPolynomial.poly_sub(arrayOfInt6, b1 * 1024, arrayOfInt6, b1 * 1024, arrayOfInt7, b1 * 1024);
/* 184 */         bool = test_correctness(arrayOfInt6, b1 * 1024);
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
/* 195 */     encodeSignature(paramArrayOfbyte1, 0, arrayOfByte1, 0, arrayOfInt5);
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int verifying(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2, byte[] paramArrayOfbyte3) {
/* 203 */     byte[] arrayOfByte1 = new byte[32];
/* 204 */     byte[] arrayOfByte2 = new byte[32];
/* 205 */     byte[] arrayOfByte3 = new byte[32];
/* 206 */     byte[] arrayOfByte4 = new byte[80];
/* 207 */     int[] arrayOfInt1 = new int[25];
/* 208 */     short[] arrayOfShort = new short[25];
/* 209 */     int[] arrayOfInt2 = new int[4096];
/* 210 */     int[] arrayOfInt3 = new int[4096];
/* 211 */     int[] arrayOfInt4 = new int[4096];
/* 212 */     int[] arrayOfInt5 = new int[4096];
/*     */     
/* 214 */     int[] arrayOfInt6 = new int[1024];
/* 215 */     int[] arrayOfInt7 = new int[1024];
/*     */     
/* 217 */     byte b = 0;
/*     */     
/* 219 */     if (paramInt2 != 2592)
/*     */     {
/* 221 */       return -1;
/*     */     }
/*     */     
/* 224 */     decodeSignature(arrayOfByte1, arrayOfInt6, paramArrayOfbyte2, paramInt1);
/* 225 */     if (testZ(arrayOfInt6))
/*     */     {
/* 227 */       return -2;
/*     */     }
/* 229 */     decodePublicKey(arrayOfInt2, arrayOfByte3, 0, paramArrayOfbyte3);
/*     */ 
/*     */     
/* 232 */     HashUtils.secureHashAlgorithmKECCAK128(arrayOfByte4, 0, 40, paramArrayOfbyte1, 0, paramArrayOfbyte1.length);
/*     */ 
/*     */     
/* 235 */     HashUtils.secureHashAlgorithmKECCAK128(arrayOfByte4, 40, 40, paramArrayOfbyte3, 0, 14848);
/*     */ 
/*     */ 
/*     */     
/* 239 */     QTesla1PPolynomial.poly_uniform(arrayOfInt4, arrayOfByte3, 0);
/* 240 */     encodeC(arrayOfInt1, arrayOfShort, arrayOfByte1, 0);
/* 241 */     QTesla1PPolynomial.poly_ntt(arrayOfInt7, arrayOfInt6);
/*     */     
/* 243 */     for (b = 0; b < 4; b++) {
/*     */       
/* 245 */       QTesla1PPolynomial.sparse_mul32(arrayOfInt5, b * 1024, arrayOfInt2, b * 1024, arrayOfInt1, arrayOfShort);
/* 246 */       QTesla1PPolynomial.poly_mul(arrayOfInt3, b * 1024, arrayOfInt4, b * 1024, arrayOfInt7);
/* 247 */       QTesla1PPolynomial.poly_sub_reduce(arrayOfInt3, b * 1024, arrayOfInt3, b * 1024, arrayOfInt5, b * 1024);
/*     */     } 
/* 249 */     hashFunction(arrayOfByte2, 0, arrayOfInt3, arrayOfByte4, 0);
/*     */     
/* 251 */     if (!memoryEqual(arrayOfByte1, 0, arrayOfByte2, 0, 32))
/*     */     {
/* 253 */       return -3;
/*     */     }
/*     */     
/* 256 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodePrivateKey(byte[] paramArrayOfbyte1, int[] paramArrayOfint1, int[] paramArrayOfint2, byte[] paramArrayOfbyte2, int paramInt, byte[] paramArrayOfbyte3) {
/* 262 */     byte b2 = 0;
/* 263 */     byte b3 = 0;
/*     */     byte b1;
/* 265 */     for (b1 = 0; b1 < 'Ѐ'; b1++)
/*     */     {
/* 267 */       paramArrayOfbyte1[b3 + b1] = (byte)paramArrayOfint1[b1];
/*     */     }
/* 269 */     b3 += 1024;
/*     */     
/* 271 */     for (b2 = 0; b2 < 4; b2++) {
/*     */       
/* 273 */       for (b1 = 0; b1 < 'Ѐ'; b1++)
/*     */       {
/* 275 */         paramArrayOfbyte1[b3 + b2 * 1024 + b1] = (byte)paramArrayOfint2[b2 * 1024 + b1];
/*     */       }
/*     */     } 
/* 278 */     b3 += 4096;
/*     */     
/* 280 */     System.arraycopy(paramArrayOfbyte2, paramInt, paramArrayOfbyte1, b3, 64);
/* 281 */     b3 += 64;
/*     */ 
/*     */     
/* 284 */     HashUtils.secureHashAlgorithmKECCAK128(paramArrayOfbyte1, b3, 40, paramArrayOfbyte3, 0, 14848);
/*     */ 
/*     */     
/* 287 */     b3 += 40;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodePublicKey(byte[] paramArrayOfbyte1, int[] paramArrayOfint, byte[] paramArrayOfbyte2, int paramInt) {
/* 294 */     byte b1 = 0;
/*     */     
/* 296 */     for (byte b2 = 0; b2 < '຀'; b2 += 29) {
/*     */       
/* 298 */       at(paramArrayOfbyte1, b2, 0, paramArrayOfint[b1] | paramArrayOfint[b1 + 1] << 29);
/* 299 */       at(paramArrayOfbyte1, b2, 1, paramArrayOfint[b1 + 1] >> 3 | paramArrayOfint[b1 + 2] << 26);
/* 300 */       at(paramArrayOfbyte1, b2, 2, paramArrayOfint[b1 + 2] >> 6 | paramArrayOfint[b1 + 3] << 23);
/* 301 */       at(paramArrayOfbyte1, b2, 3, paramArrayOfint[b1 + 3] >> 9 | paramArrayOfint[b1 + 4] << 20);
/* 302 */       at(paramArrayOfbyte1, b2, 4, paramArrayOfint[b1 + 4] >> 12 | paramArrayOfint[b1 + 5] << 17);
/* 303 */       at(paramArrayOfbyte1, b2, 5, paramArrayOfint[b1 + 5] >> 15 | paramArrayOfint[b1 + 6] << 14);
/* 304 */       at(paramArrayOfbyte1, b2, 6, paramArrayOfint[b1 + 6] >> 18 | paramArrayOfint[b1 + 7] << 11);
/* 305 */       at(paramArrayOfbyte1, b2, 7, paramArrayOfint[b1 + 7] >> 21 | paramArrayOfint[b1 + 8] << 8);
/* 306 */       at(paramArrayOfbyte1, b2, 8, paramArrayOfint[b1 + 8] >> 24 | paramArrayOfint[b1 + 9] << 5);
/* 307 */       at(paramArrayOfbyte1, b2, 9, paramArrayOfint[b1 + 9] >> 27 | paramArrayOfint[b1 + 10] << 2 | paramArrayOfint[b1 + 11] << 31);
/* 308 */       at(paramArrayOfbyte1, b2, 10, paramArrayOfint[b1 + 11] >> 1 | paramArrayOfint[b1 + 12] << 28);
/* 309 */       at(paramArrayOfbyte1, b2, 11, paramArrayOfint[b1 + 12] >> 4 | paramArrayOfint[b1 + 13] << 25);
/* 310 */       at(paramArrayOfbyte1, b2, 12, paramArrayOfint[b1 + 13] >> 7 | paramArrayOfint[b1 + 14] << 22);
/* 311 */       at(paramArrayOfbyte1, b2, 13, paramArrayOfint[b1 + 14] >> 10 | paramArrayOfint[b1 + 15] << 19);
/* 312 */       at(paramArrayOfbyte1, b2, 14, paramArrayOfint[b1 + 15] >> 13 | paramArrayOfint[b1 + 16] << 16);
/* 313 */       at(paramArrayOfbyte1, b2, 15, paramArrayOfint[b1 + 16] >> 16 | paramArrayOfint[b1 + 17] << 13);
/* 314 */       at(paramArrayOfbyte1, b2, 16, paramArrayOfint[b1 + 17] >> 19 | paramArrayOfint[b1 + 18] << 10);
/* 315 */       at(paramArrayOfbyte1, b2, 17, paramArrayOfint[b1 + 18] >> 22 | paramArrayOfint[b1 + 19] << 7);
/* 316 */       at(paramArrayOfbyte1, b2, 18, paramArrayOfint[b1 + 19] >> 25 | paramArrayOfint[b1 + 20] << 4);
/* 317 */       at(paramArrayOfbyte1, b2, 19, paramArrayOfint[b1 + 20] >> 28 | paramArrayOfint[b1 + 21] << 1 | paramArrayOfint[b1 + 22] << 30);
/* 318 */       at(paramArrayOfbyte1, b2, 20, paramArrayOfint[b1 + 22] >> 2 | paramArrayOfint[b1 + 23] << 27);
/* 319 */       at(paramArrayOfbyte1, b2, 21, paramArrayOfint[b1 + 23] >> 5 | paramArrayOfint[b1 + 24] << 24);
/* 320 */       at(paramArrayOfbyte1, b2, 22, paramArrayOfint[b1 + 24] >> 8 | paramArrayOfint[b1 + 25] << 21);
/* 321 */       at(paramArrayOfbyte1, b2, 23, paramArrayOfint[b1 + 25] >> 11 | paramArrayOfint[b1 + 26] << 18);
/* 322 */       at(paramArrayOfbyte1, b2, 24, paramArrayOfint[b1 + 26] >> 14 | paramArrayOfint[b1 + 27] << 15);
/* 323 */       at(paramArrayOfbyte1, b2, 25, paramArrayOfint[b1 + 27] >> 17 | paramArrayOfint[b1 + 28] << 12);
/* 324 */       at(paramArrayOfbyte1, b2, 26, paramArrayOfint[b1 + 28] >> 20 | paramArrayOfint[b1 + 29] << 9);
/* 325 */       at(paramArrayOfbyte1, b2, 27, paramArrayOfint[b1 + 29] >> 23 | paramArrayOfint[b1 + 30] << 6);
/* 326 */       at(paramArrayOfbyte1, b2, 28, paramArrayOfint[b1 + 30] >> 26 | paramArrayOfint[b1 + 31] << 3);
/* 327 */       b1 += 32;
/*     */     } 
/*     */     
/* 330 */     System.arraycopy(paramArrayOfbyte2, paramInt, paramArrayOfbyte1, 14848, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void decodePublicKey(int[] paramArrayOfint, byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
/* 336 */     boolean bool = false;
/* 337 */     byte[] arrayOfByte = paramArrayOfbyte2;
/* 338 */     int i = 536870911;
/*     */ 
/*     */     
/* 341 */     for (byte b = 0; b < 'က'; b += 32) {
/*     */       
/* 343 */       paramArrayOfint[b] = at(arrayOfByte, bool, 0) & i;
/* 344 */       paramArrayOfint[b + 1] = (at(arrayOfByte, bool, 0) >>> 29 | at(arrayOfByte, bool, 1) << 3) & i;
/* 345 */       paramArrayOfint[b + 2] = (at(arrayOfByte, bool, 1) >>> 26 | at(arrayOfByte, bool, 2) << 6) & i;
/* 346 */       paramArrayOfint[b + 3] = (at(arrayOfByte, bool, 2) >>> 23 | at(arrayOfByte, bool, 3) << 9) & i;
/* 347 */       paramArrayOfint[b + 4] = (at(arrayOfByte, bool, 3) >>> 20 | at(arrayOfByte, bool, 4) << 12) & i;
/* 348 */       paramArrayOfint[b + 5] = (at(arrayOfByte, bool, 4) >>> 17 | at(arrayOfByte, bool, 5) << 15) & i;
/* 349 */       paramArrayOfint[b + 6] = (at(arrayOfByte, bool, 5) >>> 14 | at(arrayOfByte, bool, 6) << 18) & i;
/* 350 */       paramArrayOfint[b + 7] = (at(arrayOfByte, bool, 6) >>> 11 | at(arrayOfByte, bool, 7) << 21) & i;
/* 351 */       paramArrayOfint[b + 8] = (at(arrayOfByte, bool, 7) >>> 8 | at(arrayOfByte, bool, 8) << 24) & i;
/* 352 */       paramArrayOfint[b + 9] = (at(arrayOfByte, bool, 8) >>> 5 | at(arrayOfByte, bool, 9) << 27) & i;
/* 353 */       paramArrayOfint[b + 10] = at(arrayOfByte, bool, 9) >>> 2 & i;
/* 354 */       paramArrayOfint[b + 11] = (at(arrayOfByte, bool, 9) >>> 31 | at(arrayOfByte, bool, 10) << 1) & i;
/* 355 */       paramArrayOfint[b + 12] = (at(arrayOfByte, bool, 10) >>> 28 | at(arrayOfByte, bool, 11) << 4) & i;
/* 356 */       paramArrayOfint[b + 13] = (at(arrayOfByte, bool, 11) >>> 25 | at(arrayOfByte, bool, 12) << 7) & i;
/* 357 */       paramArrayOfint[b + 14] = (at(arrayOfByte, bool, 12) >>> 22 | at(arrayOfByte, bool, 13) << 10) & i;
/* 358 */       paramArrayOfint[b + 15] = (at(arrayOfByte, bool, 13) >>> 19 | at(arrayOfByte, bool, 14) << 13) & i;
/* 359 */       paramArrayOfint[b + 16] = (at(arrayOfByte, bool, 14) >>> 16 | at(arrayOfByte, bool, 15) << 16) & i;
/* 360 */       paramArrayOfint[b + 17] = (at(arrayOfByte, bool, 15) >>> 13 | at(arrayOfByte, bool, 16) << 19) & i;
/* 361 */       paramArrayOfint[b + 18] = (at(arrayOfByte, bool, 16) >>> 10 | at(arrayOfByte, bool, 17) << 22) & i;
/* 362 */       paramArrayOfint[b + 19] = (at(arrayOfByte, bool, 17) >>> 7 | at(arrayOfByte, bool, 18) << 25) & i;
/* 363 */       paramArrayOfint[b + 20] = (at(arrayOfByte, bool, 18) >>> 4 | at(arrayOfByte, bool, 19) << 28) & i;
/* 364 */       paramArrayOfint[b + 21] = at(arrayOfByte, bool, 19) >>> 1 & i;
/* 365 */       paramArrayOfint[b + 22] = (at(arrayOfByte, bool, 19) >>> 30 | at(arrayOfByte, bool, 20) << 2) & i;
/* 366 */       paramArrayOfint[b + 23] = (at(arrayOfByte, bool, 20) >>> 27 | at(arrayOfByte, bool, 21) << 5) & i;
/* 367 */       paramArrayOfint[b + 24] = (at(arrayOfByte, bool, 21) >>> 24 | at(arrayOfByte, bool, 22) << 8) & i;
/* 368 */       paramArrayOfint[b + 25] = (at(arrayOfByte, bool, 22) >>> 21 | at(arrayOfByte, bool, 23) << 11) & i;
/* 369 */       paramArrayOfint[b + 26] = (at(arrayOfByte, bool, 23) >>> 18 | at(arrayOfByte, bool, 24) << 14) & i;
/* 370 */       paramArrayOfint[b + 27] = (at(arrayOfByte, bool, 24) >>> 15 | at(arrayOfByte, bool, 25) << 17) & i;
/* 371 */       paramArrayOfint[b + 28] = (at(arrayOfByte, bool, 25) >>> 12 | at(arrayOfByte, bool, 26) << 20) & i;
/* 372 */       paramArrayOfint[b + 29] = (at(arrayOfByte, bool, 26) >>> 9 | at(arrayOfByte, bool, 27) << 23) & i;
/* 373 */       paramArrayOfint[b + 30] = (at(arrayOfByte, bool, 27) >>> 6 | at(arrayOfByte, bool, 28) << 26) & i;
/* 374 */       paramArrayOfint[b + 31] = at(arrayOfByte, bool, 28) >>> 3;
/* 375 */       bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 379 */     System.arraycopy(paramArrayOfbyte2, 14848, paramArrayOfbyte1, paramInt, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean testZ(int[] paramArrayOfint) {
/* 387 */     for (byte b = 0; b < 'Ѐ'; b++) {
/*     */       
/* 389 */       if (paramArrayOfint[b] < -523733 || paramArrayOfint[b] > 523733)
/*     */       {
/* 391 */         return true;
/*     */       }
/*     */     } 
/* 394 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encodeSignature(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int[] paramArrayOfint) {
/* 401 */     byte b1 = 0;
/*     */     
/* 403 */     for (byte b2 = 0; b2 < 'ʀ'; b2 += 10) {
/*     */       
/* 405 */       at(paramArrayOfbyte1, b2, 0, paramArrayOfint[b1] & 0xFFFFF | paramArrayOfint[b1 + 1] << 20);
/* 406 */       at(paramArrayOfbyte1, b2, 1, paramArrayOfint[b1 + 1] >>> 12 & 0xFF | (paramArrayOfint[b1 + 2] & 0xFFFFF) << 8 | paramArrayOfint[b1 + 3] << 28);
/* 407 */       at(paramArrayOfbyte1, b2, 2, paramArrayOfint[b1 + 3] >>> 4 & 0xFFFF | paramArrayOfint[b1 + 4] << 16);
/* 408 */       at(paramArrayOfbyte1, b2, 3, paramArrayOfint[b1 + 4] >>> 16 & 0xF | (paramArrayOfint[b1 + 5] & 0xFFFFF) << 4 | paramArrayOfint[b1 + 6] << 24);
/* 409 */       at(paramArrayOfbyte1, b2, 4, paramArrayOfint[b1 + 6] >>> 8 & 0xFFF | paramArrayOfint[b1 + 7] << 12);
/* 410 */       at(paramArrayOfbyte1, b2, 5, paramArrayOfint[b1 + 8] & 0xFFFFF | paramArrayOfint[b1 + 9] << 20);
/* 411 */       at(paramArrayOfbyte1, b2, 6, paramArrayOfint[b1 + 9] >>> 12 & 0xFF | (paramArrayOfint[b1 + 10] & 0xFFFFF) << 8 | paramArrayOfint[b1 + 11] << 28);
/* 412 */       at(paramArrayOfbyte1, b2, 7, paramArrayOfint[b1 + 11] >>> 4 & 0xFFFF | paramArrayOfint[b1 + 12] << 16);
/* 413 */       at(paramArrayOfbyte1, b2, 8, paramArrayOfint[b1 + 12] >>> 16 & 0xF | (paramArrayOfint[b1 + 13] & 0xFFFFF) << 4 | paramArrayOfint[b1 + 14] << 24);
/* 414 */       at(paramArrayOfbyte1, b2, 9, paramArrayOfint[b1 + 14] >>> 8 & 0xFFF | paramArrayOfint[b1 + 15] << 12);
/* 415 */       b1 += 16;
/*     */     } 
/*     */     
/* 418 */     System.arraycopy(paramArrayOfbyte2, paramInt2, paramArrayOfbyte1, paramInt1 + 2560, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   static void decodeSignature(byte[] paramArrayOfbyte1, int[] paramArrayOfint, byte[] paramArrayOfbyte2, int paramInt) {
/* 423 */     boolean bool = false;
/* 424 */     for (byte b = 0; b < 'Ѐ'; b += 16) {
/*     */       
/* 426 */       int i = at(paramArrayOfbyte2, bool, 0);
/* 427 */       int j = at(paramArrayOfbyte2, bool, 1);
/* 428 */       int k = at(paramArrayOfbyte2, bool, 2);
/* 429 */       int m = at(paramArrayOfbyte2, bool, 3);
/* 430 */       int n = at(paramArrayOfbyte2, bool, 4);
/* 431 */       int i1 = at(paramArrayOfbyte2, bool, 5);
/* 432 */       int i2 = at(paramArrayOfbyte2, bool, 6);
/* 433 */       int i3 = at(paramArrayOfbyte2, bool, 7);
/* 434 */       int i4 = at(paramArrayOfbyte2, bool, 8);
/* 435 */       int i5 = at(paramArrayOfbyte2, bool, 9);
/*     */       
/* 437 */       paramArrayOfint[b] = i << 12 >> 12;
/* 438 */       paramArrayOfint[b + 1] = i >>> 20 | j << 24 >> 12;
/* 439 */       paramArrayOfint[b + 2] = j << 4 >> 12;
/* 440 */       paramArrayOfint[b + 3] = j >>> 28 | k << 16 >> 12;
/* 441 */       paramArrayOfint[b + 4] = k >>> 16 | m << 28 >> 12;
/* 442 */       paramArrayOfint[b + 5] = m << 8 >> 12;
/* 443 */       paramArrayOfint[b + 6] = m >>> 24 | n << 20 >> 12;
/* 444 */       paramArrayOfint[b + 7] = n >> 12;
/* 445 */       paramArrayOfint[b + 8] = i1 << 12 >> 12;
/* 446 */       paramArrayOfint[b + 9] = i1 >>> 20 | i2 << 24 >> 12;
/* 447 */       paramArrayOfint[b + 10] = i2 << 4 >> 12;
/* 448 */       paramArrayOfint[b + 11] = i2 >>> 28 | i3 << 16 >> 12;
/* 449 */       paramArrayOfint[b + 12] = i3 >>> 16 | i4 << 28 >> 12;
/* 450 */       paramArrayOfint[b + 13] = i4 << 8 >> 12;
/* 451 */       paramArrayOfint[b + 14] = i4 >>> 24 | i5 << 20 >> 12;
/* 452 */       paramArrayOfint[b + 15] = i5 >> 12;
/* 453 */       bool += true;
/*     */     } 
/* 455 */     System.arraycopy(paramArrayOfbyte2, paramInt + 2560, paramArrayOfbyte1, 0, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   static void encodeC(int[] paramArrayOfint, short[] paramArrayOfshort, byte[] paramArrayOfbyte, int paramInt) {
/* 460 */     byte b1 = 0;
/*     */     
/* 462 */     short s = 0;
/* 463 */     short[] arrayOfShort = new short[1024];
/* 464 */     byte[] arrayOfByte = new byte[168];
/*     */ 
/*     */     
/* 467 */     s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 168, s, paramArrayOfbyte, paramInt, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     Arrays.fill(arrayOfShort, (short)0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     for (byte b2 = 0; b2 < 25; ) {
/*     */       
/* 481 */       if (b1 > '¥') {
/*     */ 
/*     */         
/* 484 */         s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 168, s, paramArrayOfbyte, paramInt, 32);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 490 */         b1 = 0;
/*     */       } 
/*     */       
/* 493 */       int i = arrayOfByte[b1] << 8 | arrayOfByte[b1 + 1] & 0xFF;
/* 494 */       i &= 0x3FF;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 499 */       if (arrayOfShort[i] == 0) {
/*     */ 
/*     */         
/* 502 */         if ((arrayOfByte[b1 + 2] & 0x1) == 1) {
/*     */ 
/*     */           
/* 505 */           arrayOfShort[i] = -1;
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 511 */           arrayOfShort[i] = 1;
/*     */         } 
/*     */ 
/*     */         
/* 515 */         paramArrayOfint[b2] = i;
/* 516 */         paramArrayOfshort[b2] = arrayOfShort[i];
/* 517 */         b2++;
/*     */       } 
/*     */ 
/*     */       
/* 521 */       b1 += 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void hashFunction(byte[] paramArrayOfbyte1, int paramInt1, int[] paramArrayOfint, byte[] paramArrayOfbyte2, int paramInt2) {
/* 530 */     byte[] arrayOfByte = new byte[4176];
/*     */     
/* 532 */     for (byte b = 0; b < 4; b++) {
/*     */       
/* 534 */       int i = b * 1024;
/* 535 */       for (byte b1 = 0; b1 < 'Ѐ'; b1++) {
/*     */         
/* 537 */         int m = paramArrayOfint[i];
/*     */         
/* 539 */         int j = 171788288 - m >> 31;
/* 540 */         m = m - 343576577 & j | m & (j ^ 0xFFFFFFFF);
/*     */         
/* 542 */         int k = m & 0x3FFFFF;
/*     */         
/* 544 */         j = 2097152 - k >> 31;
/* 545 */         k = k - 4194304 & j | k & (j ^ 0xFFFFFFFF);
/* 546 */         arrayOfByte[i++] = (byte)(m - k >> 22);
/*     */       } 
/*     */     } 
/* 549 */     System.arraycopy(paramArrayOfbyte2, paramInt2, arrayOfByte, 4096, 80);
/*     */     
/* 551 */     HashUtils.secureHashAlgorithmKECCAK128(paramArrayOfbyte1, paramInt1, 32, arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int littleEndianToInt24(byte[] paramArrayOfbyte, int paramInt) {
/* 558 */     int i = paramArrayOfbyte[paramInt] & 0xFF;
/* 559 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 560 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/* 561 */     return i;
/*     */   }
/*     */   
/* 564 */   private static int NBLOCKS_SHAKE = 56;
/* 565 */   private static int BPLUS1BYTES = 3;
/*     */ 
/*     */   
/*     */   static void sample_y(int[] paramArrayOfint, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 569 */     byte b = 0; int i = 0, j = 1024;
/* 570 */     byte[] arrayOfByte = new byte[1024 * BPLUS1BYTES + 1];
/* 571 */     int k = BPLUS1BYTES;
/* 572 */     short s = (short)(paramInt2 << 8);
/*     */     
/* 574 */     s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 1024 * k, s, paramArrayOfbyte, paramInt1, 32);
/*     */ 
/*     */ 
/*     */     
/* 578 */     while (b < 'Ѐ') {
/*     */       
/* 580 */       if (i >= j * k) {
/*     */         
/* 582 */         j = NBLOCKS_SHAKE;
/* 583 */         s = (short)(s + 1); HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(arrayOfByte, 0, 1024 * k, s, paramArrayOfbyte, paramInt1, 32);
/*     */ 
/*     */         
/* 586 */         i = 0;
/*     */       } 
/* 588 */       paramArrayOfint[b] = littleEndianToInt24(arrayOfByte, i) & 0xFFFFF;
/* 589 */       paramArrayOfint[b] = paramArrayOfint[b] - 524287;
/* 590 */       if (paramArrayOfint[b] != 524288)
/*     */       {
/* 592 */         b++;
/*     */       }
/* 594 */       i += k;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void at(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) {
/* 600 */     Pack.intToLittleEndian(paramInt3, paramArrayOfbyte, paramInt1 + paramInt2 << 2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int at(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 605 */     return Pack.littleEndianToInt(paramArrayOfbyte, paramInt1 + paramInt2 << 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean test_correctness(int[] paramArrayOfint, int paramInt) {
/* 615 */     for (byte b = 0; b < 'Ѐ'; b++) {
/*     */ 
/*     */       
/* 618 */       int i1 = paramArrayOfint[paramInt + b];
/* 619 */       int i = 171788288 - i1 >> 31;
/* 620 */       int k = i1 - 343576577 & i | i1 & (i ^ 0xFFFFFFFF);
/*     */       
/* 622 */       int m = (absolute(k) - 171787734 ^ 0xFFFFFFFF) >>> 31;
/*     */       
/* 624 */       int j = k;
/* 625 */       k = k + 2097152 - 1 >> 22;
/* 626 */       k = j - (k << 22);
/*     */       
/* 628 */       int n = (absolute(k) - 2096598 ^ 0xFFFFFFFF) >>> 31;
/*     */       
/* 630 */       if ((m | n) == 1)
/*     */       {
/* 632 */         return true;
/*     */       }
/*     */     } 
/* 635 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean testRejection(int[] paramArrayOfint) {
/* 640 */     int i = 0;
/*     */     
/* 642 */     for (byte b = 0; b < 'Ѐ'; b++)
/*     */     {
/* 644 */       i |= 523733 - absolute(paramArrayOfint[b]);
/*     */     }
/*     */     
/* 647 */     return (i >>> 31 != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int absolute(int paramInt) {
/* 652 */     int i = paramInt >> 31;
/* 653 */     return (i ^ paramInt) - i;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean checkPolynomial(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/* 658 */     int i = 0; char c = 'Ѐ';
/*     */     
/* 660 */     int[] arrayOfInt = new int[1024];
/*     */     byte b;
/* 662 */     for (b = 0; b < 'Ѐ'; b++)
/*     */     {
/* 664 */       arrayOfInt[b] = absolute(paramArrayOfint[paramInt1 + b]);
/*     */     }
/*     */     
/* 667 */     for (b = 0; b < 25; b++) {
/*     */       
/* 669 */       for (byte b1 = 0; b1 < c - 1; b1++) {
/*     */         
/* 671 */         int m = arrayOfInt[b1], n = arrayOfInt[b1 + 1];
/*     */         
/* 673 */         int k = n - m >> 31;
/* 674 */         int j = n & k | m & (k ^ 0xFFFFFFFF);
/* 675 */         arrayOfInt[b1 + 1] = m & k | n & (k ^ 0xFFFFFFFF);
/* 676 */         arrayOfInt[b1] = j;
/*     */       } 
/* 678 */       i += arrayOfInt[c - 1];
/* 679 */       c--;
/*     */     } 
/*     */     
/* 682 */     return (i > paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean memoryEqual(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
/* 688 */     if (paramInt1 + paramInt3 <= paramArrayOfbyte1.length && paramInt2 + paramInt3 <= paramArrayOfbyte2.length) {
/*     */ 
/*     */       
/* 691 */       for (byte b = 0; b < paramInt3; b++) {
/*     */ 
/*     */         
/* 694 */         if (paramArrayOfbyte1[paramInt1 + b] != paramArrayOfbyte2[paramInt2 + b])
/*     */         {
/*     */           
/* 697 */           return false;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 703 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 709 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/QTesla1p.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */