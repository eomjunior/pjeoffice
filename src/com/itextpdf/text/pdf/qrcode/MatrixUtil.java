/*     */ package com.itextpdf.text.pdf.qrcode;
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
/*     */ public final class MatrixUtil
/*     */ {
/*  30 */   private static final int[][] POSITION_DETECTION_PATTERN = new int[][] { { 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 1, 1, 0, 1 }, { 1, 0, 1, 1, 1, 0, 1 }, { 1, 0, 1, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final int[][] HORIZONTAL_SEPARATION_PATTERN = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0 } };
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static final int[][] VERTICAL_SEPARATION_PATTERN = new int[][] { { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final int[][] POSITION_ADJUSTMENT_PATTERN = new int[][] { { 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1 }, { 1, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final int[][] POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE = new int[][] { { -1, -1, -1, -1, -1, -1, -1 }, { 6, 18, -1, -1, -1, -1, -1 }, { 6, 22, -1, -1, -1, -1, -1 }, { 6, 26, -1, -1, -1, -1, -1 }, { 6, 30, -1, -1, -1, -1, -1 }, { 6, 34, -1, -1, -1, -1, -1 }, { 6, 22, 38, -1, -1, -1, -1 }, { 6, 24, 42, -1, -1, -1, -1 }, { 6, 26, 46, -1, -1, -1, -1 }, { 6, 28, 50, -1, -1, -1, -1 }, { 6, 30, 54, -1, -1, -1, -1 }, { 6, 32, 58, -1, -1, -1, -1 }, { 6, 34, 62, -1, -1, -1, -1 }, { 6, 26, 46, 66, -1, -1, -1 }, { 6, 26, 48, 70, -1, -1, -1 }, { 6, 26, 50, 74, -1, -1, -1 }, { 6, 30, 54, 78, -1, -1, -1 }, { 6, 30, 56, 82, -1, -1, -1 }, { 6, 30, 58, 86, -1, -1, -1 }, { 6, 34, 62, 90, -1, -1, -1 }, { 6, 28, 50, 72, 94, -1, -1 }, { 6, 26, 50, 74, 98, -1, -1 }, { 6, 30, 54, 78, 102, -1, -1 }, { 6, 28, 54, 80, 106, -1, -1 }, { 6, 32, 58, 84, 110, -1, -1 }, { 6, 30, 58, 86, 114, -1, -1 }, { 6, 34, 62, 90, 118, -1, -1 }, { 6, 26, 50, 74, 98, 122, -1 }, { 6, 30, 54, 78, 102, 126, -1 }, { 6, 26, 52, 78, 104, 130, -1 }, { 6, 30, 56, 82, 108, 134, -1 }, { 6, 34, 60, 86, 112, 138, -1 }, { 6, 30, 58, 86, 114, 142, -1 }, { 6, 34, 62, 90, 118, 146, -1 }, { 6, 30, 54, 78, 102, 126, 150 }, { 6, 24, 50, 76, 102, 128, 154 }, { 6, 28, 54, 80, 106, 132, 158 }, { 6, 32, 58, 84, 110, 136, 162 }, { 6, 26, 54, 82, 110, 138, 166 }, { 6, 30, 58, 86, 114, 142, 170 } };
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
/* 101 */   private static final int[][] TYPE_INFO_COORDINATES = new int[][] { { 8, 0 }, { 8, 1 }, { 8, 2 }, { 8, 3 }, { 8, 4 }, { 8, 5 }, { 8, 7 }, { 8, 8 }, { 7, 8 }, { 5, 8 }, { 4, 8 }, { 3, 8 }, { 2, 8 }, { 1, 8 }, { 0, 8 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int VERSION_INFO_POLY = 7973;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TYPE_INFO_POLY = 1335;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TYPE_INFO_MASK_PATTERN = 21522;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearMatrix(ByteMatrix matrix) {
/* 131 */     matrix.clear((byte)-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void buildMatrix(BitVector dataBits, ErrorCorrectionLevel ecLevel, int version, int maskPattern, ByteMatrix matrix) throws WriterException {
/* 138 */     clearMatrix(matrix);
/* 139 */     embedBasicPatterns(version, matrix);
/*     */     
/* 141 */     embedTypeInfo(ecLevel, maskPattern, matrix);
/*     */     
/* 143 */     maybeEmbedVersionInfo(version, matrix);
/*     */     
/* 145 */     embedDataBits(dataBits, maskPattern, matrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void embedBasicPatterns(int version, ByteMatrix matrix) throws WriterException {
/* 156 */     embedPositionDetectionPatternsAndSeparators(matrix);
/*     */     
/* 158 */     embedDarkDotAtLeftBottomCorner(matrix);
/*     */ 
/*     */     
/* 161 */     maybeEmbedPositionAdjustmentPatterns(version, matrix);
/*     */     
/* 163 */     embedTimingPatterns(matrix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void embedTypeInfo(ErrorCorrectionLevel ecLevel, int maskPattern, ByteMatrix matrix) throws WriterException {
/* 169 */     BitVector typeInfoBits = new BitVector();
/* 170 */     makeTypeInfoBits(ecLevel, maskPattern, typeInfoBits);
/*     */     
/* 172 */     for (int i = 0; i < typeInfoBits.size(); i++) {
/*     */ 
/*     */       
/* 175 */       int bit = typeInfoBits.at(typeInfoBits.size() - 1 - i);
/*     */ 
/*     */       
/* 178 */       int x1 = TYPE_INFO_COORDINATES[i][0];
/* 179 */       int y1 = TYPE_INFO_COORDINATES[i][1];
/* 180 */       matrix.set(x1, y1, bit);
/*     */       
/* 182 */       if (i < 8) {
/*     */         
/* 184 */         int x2 = matrix.getWidth() - i - 1;
/* 185 */         int y2 = 8;
/* 186 */         matrix.set(x2, y2, bit);
/*     */       } else {
/*     */         
/* 189 */         int x2 = 8;
/* 190 */         int y2 = matrix.getHeight() - 7 + i - 8;
/* 191 */         matrix.set(x2, y2, bit);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void maybeEmbedVersionInfo(int version, ByteMatrix matrix) throws WriterException {
/* 199 */     if (version < 7) {
/*     */       return;
/*     */     }
/* 202 */     BitVector versionInfoBits = new BitVector();
/* 203 */     makeVersionInfoBits(version, versionInfoBits);
/*     */     
/* 205 */     int bitIndex = 17;
/* 206 */     for (int i = 0; i < 6; i++) {
/* 207 */       for (int j = 0; j < 3; j++) {
/*     */         
/* 209 */         int bit = versionInfoBits.at(bitIndex);
/* 210 */         bitIndex--;
/*     */         
/* 212 */         matrix.set(i, matrix.getHeight() - 11 + j, bit);
/*     */         
/* 214 */         matrix.set(matrix.getHeight() - 11 + j, i, bit);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void embedDataBits(BitVector dataBits, int maskPattern, ByteMatrix matrix) throws WriterException {
/* 224 */     int bitIndex = 0;
/* 225 */     int direction = -1;
/*     */     
/* 227 */     int x = matrix.getWidth() - 1;
/* 228 */     int y = matrix.getHeight() - 1;
/* 229 */     while (x > 0) {
/*     */       
/* 231 */       if (x == 6) {
/* 232 */         x--;
/*     */       }
/* 234 */       while (y >= 0 && y < matrix.getHeight()) {
/* 235 */         for (int i = 0; i < 2; i++) {
/* 236 */           int xx = x - i;
/*     */           
/* 238 */           if (isEmpty(matrix.get(xx, y))) {
/*     */             int bit;
/*     */ 
/*     */             
/* 242 */             if (bitIndex < dataBits.size()) {
/* 243 */               bit = dataBits.at(bitIndex);
/* 244 */               bitIndex++;
/*     */             }
/*     */             else {
/*     */               
/* 248 */               bit = 0;
/*     */             } 
/*     */ 
/*     */             
/* 252 */             if (maskPattern != -1 && 
/* 253 */               MaskUtil.getDataMaskBit(maskPattern, xx, y)) {
/* 254 */               bit ^= 0x1;
/*     */             }
/*     */             
/* 257 */             matrix.set(xx, y, bit);
/*     */           } 
/* 259 */         }  y += direction;
/*     */       } 
/* 261 */       direction = -direction;
/* 262 */       y += direction;
/* 263 */       x -= 2;
/*     */     } 
/*     */     
/* 266 */     if (bitIndex != dataBits.size()) {
/* 267 */       throw new WriterException("Not all bits consumed: " + bitIndex + '/' + dataBits.size());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findMSBSet(int value) {
/* 277 */     int numDigits = 0;
/* 278 */     while (value != 0) {
/* 279 */       value >>>= 1;
/* 280 */       numDigits++;
/*     */     } 
/* 282 */     return numDigits;
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
/*     */   public static int calculateBCHCode(int value, int poly) {
/* 313 */     int msbSetInPoly = findMSBSet(poly);
/* 314 */     value <<= msbSetInPoly - 1;
/*     */     
/* 316 */     while (findMSBSet(value) >= msbSetInPoly) {
/* 317 */       value ^= poly << findMSBSet(value) - msbSetInPoly;
/*     */     }
/*     */     
/* 320 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeTypeInfoBits(ErrorCorrectionLevel ecLevel, int maskPattern, BitVector bits) throws WriterException {
/* 328 */     if (!QRCode.isValidMaskPattern(maskPattern)) {
/* 329 */       throw new WriterException("Invalid mask pattern");
/*     */     }
/* 331 */     int typeInfo = ecLevel.getBits() << 3 | maskPattern;
/* 332 */     bits.appendBits(typeInfo, 5);
/*     */     
/* 334 */     int bchCode = calculateBCHCode(typeInfo, 1335);
/* 335 */     bits.appendBits(bchCode, 10);
/*     */     
/* 337 */     BitVector maskBits = new BitVector();
/* 338 */     maskBits.appendBits(21522, 15);
/* 339 */     bits.xor(maskBits);
/*     */     
/* 341 */     if (bits.size() != 15) {
/* 342 */       throw new WriterException("should not happen but we got: " + bits.size());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeVersionInfoBits(int version, BitVector bits) throws WriterException {
/* 349 */     bits.appendBits(version, 6);
/* 350 */     int bchCode = calculateBCHCode(version, 7973);
/* 351 */     bits.appendBits(bchCode, 12);
/*     */     
/* 353 */     if (bits.size() != 18) {
/* 354 */       throw new WriterException("should not happen but we got: " + bits.size());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isEmpty(int value) {
/* 360 */     return (value == -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValidValue(int value) {
/* 365 */     return (value == -1 || value == 0 || value == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedTimingPatterns(ByteMatrix matrix) throws WriterException {
/* 373 */     for (int i = 8; i < matrix.getWidth() - 8; i++) {
/* 374 */       int bit = (i + 1) % 2;
/*     */       
/* 376 */       if (!isValidValue(matrix.get(i, 6))) {
/* 377 */         throw new WriterException();
/*     */       }
/* 379 */       if (isEmpty(matrix.get(i, 6))) {
/* 380 */         matrix.set(i, 6, bit);
/*     */       }
/*     */       
/* 383 */       if (!isValidValue(matrix.get(6, i))) {
/* 384 */         throw new WriterException();
/*     */       }
/* 386 */       if (isEmpty(matrix.get(6, i))) {
/* 387 */         matrix.set(6, i, bit);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void embedDarkDotAtLeftBottomCorner(ByteMatrix matrix) throws WriterException {
/* 394 */     if (matrix.get(8, matrix.getHeight() - 8) == 0) {
/* 395 */       throw new WriterException();
/*     */     }
/* 397 */     matrix.set(8, matrix.getHeight() - 8, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedHorizontalSeparationPattern(int xStart, int yStart, ByteMatrix matrix) throws WriterException {
/* 403 */     if ((HORIZONTAL_SEPARATION_PATTERN[0]).length != 8 || HORIZONTAL_SEPARATION_PATTERN.length != 1) {
/* 404 */       throw new WriterException("Bad horizontal separation pattern");
/*     */     }
/* 406 */     for (int x = 0; x < 8; x++) {
/* 407 */       if (!isEmpty(matrix.get(xStart + x, yStart))) {
/* 408 */         throw new WriterException();
/*     */       }
/* 410 */       matrix.set(xStart + x, yStart, HORIZONTAL_SEPARATION_PATTERN[0][x]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedVerticalSeparationPattern(int xStart, int yStart, ByteMatrix matrix) throws WriterException {
/* 417 */     if ((VERTICAL_SEPARATION_PATTERN[0]).length != 1 || VERTICAL_SEPARATION_PATTERN.length != 7) {
/* 418 */       throw new WriterException("Bad vertical separation pattern");
/*     */     }
/* 420 */     for (int y = 0; y < 7; y++) {
/* 421 */       if (!isEmpty(matrix.get(xStart, yStart + y))) {
/* 422 */         throw new WriterException();
/*     */       }
/* 424 */       matrix.set(xStart, yStart + y, VERTICAL_SEPARATION_PATTERN[y][0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedPositionAdjustmentPattern(int xStart, int yStart, ByteMatrix matrix) throws WriterException {
/* 434 */     if ((POSITION_ADJUSTMENT_PATTERN[0]).length != 5 || POSITION_ADJUSTMENT_PATTERN.length != 5) {
/* 435 */       throw new WriterException("Bad position adjustment");
/*     */     }
/* 437 */     for (int y = 0; y < 5; y++) {
/* 438 */       for (int x = 0; x < 5; x++) {
/* 439 */         if (!isEmpty(matrix.get(xStart + x, yStart + y))) {
/* 440 */           throw new WriterException();
/*     */         }
/* 442 */         matrix.set(xStart + x, yStart + y, POSITION_ADJUSTMENT_PATTERN[y][x]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedPositionDetectionPattern(int xStart, int yStart, ByteMatrix matrix) throws WriterException {
/* 450 */     if ((POSITION_DETECTION_PATTERN[0]).length != 7 || POSITION_DETECTION_PATTERN.length != 7) {
/* 451 */       throw new WriterException("Bad position detection pattern");
/*     */     }
/* 453 */     for (int y = 0; y < 7; y++) {
/* 454 */       for (int x = 0; x < 7; x++) {
/* 455 */         if (!isEmpty(matrix.get(xStart + x, yStart + y))) {
/* 456 */           throw new WriterException();
/*     */         }
/* 458 */         matrix.set(xStart + x, yStart + y, POSITION_DETECTION_PATTERN[y][x]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void embedPositionDetectionPatternsAndSeparators(ByteMatrix matrix) throws WriterException {
/* 466 */     int pdpWidth = (POSITION_DETECTION_PATTERN[0]).length;
/*     */     
/* 468 */     embedPositionDetectionPattern(0, 0, matrix);
/*     */     
/* 470 */     embedPositionDetectionPattern(matrix.getWidth() - pdpWidth, 0, matrix);
/*     */     
/* 472 */     embedPositionDetectionPattern(0, matrix.getWidth() - pdpWidth, matrix);
/*     */ 
/*     */     
/* 475 */     int hspWidth = (HORIZONTAL_SEPARATION_PATTERN[0]).length;
/*     */     
/* 477 */     embedHorizontalSeparationPattern(0, hspWidth - 1, matrix);
/*     */     
/* 479 */     embedHorizontalSeparationPattern(matrix.getWidth() - hspWidth, hspWidth - 1, matrix);
/*     */ 
/*     */     
/* 482 */     embedHorizontalSeparationPattern(0, matrix.getWidth() - hspWidth, matrix);
/*     */ 
/*     */     
/* 485 */     int vspSize = VERTICAL_SEPARATION_PATTERN.length;
/*     */     
/* 487 */     embedVerticalSeparationPattern(vspSize, 0, matrix);
/*     */     
/* 489 */     embedVerticalSeparationPattern(matrix.getHeight() - vspSize - 1, 0, matrix);
/*     */     
/* 491 */     embedVerticalSeparationPattern(vspSize, matrix.getHeight() - vspSize, matrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void maybeEmbedPositionAdjustmentPatterns(int version, ByteMatrix matrix) throws WriterException {
/* 498 */     if (version < 2) {
/*     */       return;
/*     */     }
/* 501 */     int index = version - 1;
/* 502 */     int[] coordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index];
/* 503 */     int numCoordinates = (POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index]).length;
/* 504 */     for (int i = 0; i < numCoordinates; i++) {
/* 505 */       for (int j = 0; j < numCoordinates; j++) {
/* 506 */         int y = coordinates[i];
/* 507 */         int x = coordinates[j];
/* 508 */         if (x != -1 && y != -1)
/*     */         {
/*     */ 
/*     */           
/* 512 */           if (isEmpty(matrix.get(x, y)))
/*     */           {
/*     */             
/* 515 */             embedPositionAdjustmentPattern(x - 2, y - 2, matrix);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/MatrixUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */