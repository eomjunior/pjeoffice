/*     */ package com.itextpdf.text.pdf.qrcode;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Encoder
/*     */ {
/*  31 */   private static final int[] ALPHANUMERIC_TABLE = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calculateMaskPenalty(ByteMatrix matrix) {
/*  48 */     int penalty = 0;
/*  49 */     penalty += MaskUtil.applyMaskPenaltyRule1(matrix);
/*  50 */     penalty += MaskUtil.applyMaskPenaltyRule2(matrix);
/*  51 */     penalty += MaskUtil.applyMaskPenaltyRule3(matrix);
/*  52 */     penalty += MaskUtil.applyMaskPenaltyRule4(matrix);
/*  53 */     return penalty;
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
/*     */   public static void encode(String content, ErrorCorrectionLevel ecLevel, QRCode qrCode) throws WriterException {
/*  69 */     encode(content, ecLevel, null, qrCode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encode(String content, ErrorCorrectionLevel ecLevel, Map<EncodeHintType, Object> hints, QRCode qrCode) throws WriterException {
/*  75 */     String encoding = (hints == null) ? null : (String)hints.get(EncodeHintType.CHARACTER_SET);
/*  76 */     if (encoding == null) {
/*  77 */       encoding = "ISO-8859-1";
/*     */     }
/*     */ 
/*     */     
/*  81 */     Mode mode = chooseMode(content, encoding);
/*     */ 
/*     */     
/*  84 */     BitVector dataBits = new BitVector();
/*  85 */     appendBytes(content, mode, dataBits, encoding);
/*     */     
/*  87 */     int numInputBytes = dataBits.sizeInBytes();
/*  88 */     initQRCode(numInputBytes, ecLevel, mode, qrCode);
/*     */ 
/*     */     
/*  91 */     BitVector headerAndDataBits = new BitVector();
/*     */ 
/*     */     
/*  94 */     if (mode == Mode.BYTE && !"ISO-8859-1".equals(encoding)) {
/*  95 */       CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(encoding);
/*  96 */       if (eci != null) {
/*  97 */         appendECI(eci, headerAndDataBits);
/*     */       }
/*     */     } 
/*     */     
/* 101 */     appendModeInfo(mode, headerAndDataBits);
/*     */     
/* 103 */     int numLetters = mode.equals(Mode.BYTE) ? dataBits.sizeInBytes() : content.length();
/* 104 */     appendLengthInfo(numLetters, qrCode.getVersion(), mode, headerAndDataBits);
/* 105 */     headerAndDataBits.appendBitVector(dataBits);
/*     */ 
/*     */     
/* 108 */     terminateBits(qrCode.getNumDataBytes(), headerAndDataBits);
/*     */ 
/*     */     
/* 111 */     BitVector finalBits = new BitVector();
/* 112 */     interleaveWithECBytes(headerAndDataBits, qrCode.getNumTotalBytes(), qrCode.getNumDataBytes(), qrCode
/* 113 */         .getNumRSBlocks(), finalBits);
/*     */ 
/*     */     
/* 116 */     ByteMatrix matrix = new ByteMatrix(qrCode.getMatrixWidth(), qrCode.getMatrixWidth());
/* 117 */     qrCode.setMaskPattern(chooseMaskPattern(finalBits, qrCode.getECLevel(), qrCode.getVersion(), matrix));
/*     */ 
/*     */ 
/*     */     
/* 121 */     MatrixUtil.buildMatrix(finalBits, qrCode.getECLevel(), qrCode.getVersion(), qrCode
/* 122 */         .getMaskPattern(), matrix);
/* 123 */     qrCode.setMatrix(matrix);
/*     */     
/* 125 */     if (!qrCode.isValid()) {
/* 126 */       throw new WriterException("Invalid QR code: " + qrCode.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getAlphanumericCode(int code) {
/* 135 */     if (code < ALPHANUMERIC_TABLE.length) {
/* 136 */       return ALPHANUMERIC_TABLE[code];
/*     */     }
/* 138 */     return -1;
/*     */   }
/*     */   
/*     */   public static Mode chooseMode(String content) {
/* 142 */     return chooseMode(content, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mode chooseMode(String content, String encoding) {
/* 150 */     if ("Shift_JIS".equals(encoding))
/*     */     {
/* 152 */       return isOnlyDoubleByteKanji(content) ? Mode.KANJI : Mode.BYTE;
/*     */     }
/* 154 */     boolean hasNumeric = false;
/* 155 */     boolean hasAlphanumeric = false;
/* 156 */     for (int i = 0; i < content.length(); i++) {
/* 157 */       char c = content.charAt(i);
/* 158 */       if (c >= '0' && c <= '9') {
/* 159 */         hasNumeric = true;
/* 160 */       } else if (getAlphanumericCode(c) != -1) {
/* 161 */         hasAlphanumeric = true;
/*     */       } else {
/* 163 */         return Mode.BYTE;
/*     */       } 
/*     */     } 
/* 166 */     if (hasAlphanumeric)
/* 167 */       return Mode.ALPHANUMERIC; 
/* 168 */     if (hasNumeric) {
/* 169 */       return Mode.NUMERIC;
/*     */     }
/* 171 */     return Mode.BYTE;
/*     */   }
/*     */   
/*     */   private static boolean isOnlyDoubleByteKanji(String content) {
/*     */     byte[] bytes;
/*     */     try {
/* 177 */       bytes = content.getBytes("Shift_JIS");
/* 178 */     } catch (UnsupportedEncodingException uee) {
/* 179 */       return false;
/*     */     } 
/* 181 */     int length = bytes.length;
/* 182 */     if (length % 2 != 0) {
/* 183 */       return false;
/*     */     }
/* 185 */     for (int i = 0; i < length; i += 2) {
/* 186 */       int byte1 = bytes[i] & 0xFF;
/* 187 */       if ((byte1 < 129 || byte1 > 159) && (byte1 < 224 || byte1 > 235)) {
/* 188 */         return false;
/*     */       }
/*     */     } 
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int chooseMaskPattern(BitVector bits, ErrorCorrectionLevel ecLevel, int version, ByteMatrix matrix) throws WriterException {
/* 197 */     int minPenalty = Integer.MAX_VALUE;
/* 198 */     int bestMaskPattern = -1;
/*     */     
/* 200 */     for (int maskPattern = 0; maskPattern < 8; maskPattern++) {
/* 201 */       MatrixUtil.buildMatrix(bits, ecLevel, version, maskPattern, matrix);
/* 202 */       int penalty = calculateMaskPenalty(matrix);
/* 203 */       if (penalty < minPenalty) {
/* 204 */         minPenalty = penalty;
/* 205 */         bestMaskPattern = maskPattern;
/*     */       } 
/*     */     } 
/* 208 */     return bestMaskPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initQRCode(int numInputBytes, ErrorCorrectionLevel ecLevel, Mode mode, QRCode qrCode) throws WriterException {
/* 217 */     qrCode.setECLevel(ecLevel);
/* 218 */     qrCode.setMode(mode);
/*     */ 
/*     */     
/* 221 */     for (int versionNum = 1; versionNum <= 40; versionNum++) {
/* 222 */       Version version = Version.getVersionForNumber(versionNum);
/*     */       
/* 224 */       int numBytes = version.getTotalCodewords();
/*     */       
/* 226 */       Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
/* 227 */       int numEcBytes = ecBlocks.getTotalECCodewords();
/*     */       
/* 229 */       int numRSBlocks = ecBlocks.getNumBlocks();
/*     */       
/* 231 */       int numDataBytes = numBytes - numEcBytes;
/*     */ 
/*     */ 
/*     */       
/* 235 */       if (numDataBytes >= numInputBytes + 3) {
/*     */         
/* 237 */         qrCode.setVersion(versionNum);
/* 238 */         qrCode.setNumTotalBytes(numBytes);
/* 239 */         qrCode.setNumDataBytes(numDataBytes);
/* 240 */         qrCode.setNumRSBlocks(numRSBlocks);
/*     */         
/* 242 */         qrCode.setNumECBytes(numEcBytes);
/*     */         
/* 244 */         qrCode.setMatrixWidth(version.getDimensionForVersion());
/*     */         return;
/*     */       } 
/*     */     } 
/* 248 */     throw new WriterException("Cannot find proper rs block info (input data too big?)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void terminateBits(int numDataBytes, BitVector bits) throws WriterException {
/* 255 */     int capacity = numDataBytes << 3;
/* 256 */     if (bits.size() > capacity) {
/* 257 */       throw new WriterException("data bits cannot fit in the QR Code" + bits.size() + " > " + capacity);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     for (int i = 0; i < 4 && bits.size() < capacity; i++) {
/* 265 */       bits.appendBit(0);
/*     */     }
/* 267 */     int numBitsInLastByte = bits.size() % 8;
/*     */     
/* 269 */     if (numBitsInLastByte > 0) {
/* 270 */       int numPaddingBits = 8 - numBitsInLastByte;
/* 271 */       for (int k = 0; k < numPaddingBits; k++) {
/* 272 */         bits.appendBit(0);
/*     */       }
/*     */     } 
/*     */     
/* 276 */     if (bits.size() % 8 != 0) {
/* 277 */       throw new WriterException("Number of bits is not a multiple of 8");
/*     */     }
/*     */     
/* 280 */     int numPaddingBytes = numDataBytes - bits.sizeInBytes();
/* 281 */     for (int j = 0; j < numPaddingBytes; j++) {
/* 282 */       if (j % 2 == 0) {
/* 283 */         bits.appendBits(236, 8);
/*     */       } else {
/* 285 */         bits.appendBits(17, 8);
/*     */       } 
/*     */     } 
/* 288 */     if (bits.size() != capacity) {
/* 289 */       throw new WriterException("Bits size does not equal capacity");
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
/*     */   static void getNumDataBytesAndNumECBytesForBlockID(int numTotalBytes, int numDataBytes, int numRSBlocks, int blockID, int[] numDataBytesInBlock, int[] numECBytesInBlock) throws WriterException {
/* 301 */     if (blockID >= numRSBlocks) {
/* 302 */       throw new WriterException("Block ID too large");
/*     */     }
/*     */     
/* 305 */     int numRsBlocksInGroup2 = numTotalBytes % numRSBlocks;
/*     */     
/* 307 */     int numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2;
/*     */     
/* 309 */     int numTotalBytesInGroup1 = numTotalBytes / numRSBlocks;
/*     */     
/* 311 */     int numTotalBytesInGroup2 = numTotalBytesInGroup1 + 1;
/*     */     
/* 313 */     int numDataBytesInGroup1 = numDataBytes / numRSBlocks;
/*     */     
/* 315 */     int numDataBytesInGroup2 = numDataBytesInGroup1 + 1;
/*     */     
/* 317 */     int numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1;
/*     */     
/* 319 */     int numEcBytesInGroup2 = numTotalBytesInGroup2 - numDataBytesInGroup2;
/*     */ 
/*     */     
/* 322 */     if (numEcBytesInGroup1 != numEcBytesInGroup2) {
/* 323 */       throw new WriterException("EC bytes mismatch");
/*     */     }
/*     */     
/* 326 */     if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
/* 327 */       throw new WriterException("RS blocks mismatch");
/*     */     }
/*     */     
/* 330 */     if (numTotalBytes != (numDataBytesInGroup1 + numEcBytesInGroup1) * numRsBlocksInGroup1 + (numDataBytesInGroup2 + numEcBytesInGroup2) * numRsBlocksInGroup2)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 335 */       throw new WriterException("Total bytes mismatch");
/*     */     }
/*     */     
/* 338 */     if (blockID < numRsBlocksInGroup1) {
/* 339 */       numDataBytesInBlock[0] = numDataBytesInGroup1;
/* 340 */       numECBytesInBlock[0] = numEcBytesInGroup1;
/*     */     } else {
/* 342 */       numDataBytesInBlock[0] = numDataBytesInGroup2;
/* 343 */       numECBytesInBlock[0] = numEcBytesInGroup2;
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
/*     */   static void interleaveWithECBytes(BitVector bits, int numTotalBytes, int numDataBytes, int numRSBlocks, BitVector result) throws WriterException {
/* 355 */     if (bits.sizeInBytes() != numDataBytes) {
/* 356 */       throw new WriterException("Number of bits and data bytes does not match");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 361 */     int dataBytesOffset = 0;
/* 362 */     int maxNumDataBytes = 0;
/* 363 */     int maxNumEcBytes = 0;
/*     */ 
/*     */     
/* 366 */     ArrayList<BlockPair> blocks = new ArrayList<BlockPair>(numRSBlocks);
/*     */     int i;
/* 368 */     for (i = 0; i < numRSBlocks; i++) {
/* 369 */       int[] numDataBytesInBlock = new int[1];
/* 370 */       int[] numEcBytesInBlock = new int[1];
/* 371 */       getNumDataBytesAndNumECBytesForBlockID(numTotalBytes, numDataBytes, numRSBlocks, i, numDataBytesInBlock, numEcBytesInBlock);
/*     */ 
/*     */ 
/*     */       
/* 375 */       ByteArray dataBytes = new ByteArray();
/* 376 */       dataBytes.set(bits.getArray(), dataBytesOffset, numDataBytesInBlock[0]);
/* 377 */       ByteArray ecBytes = generateECBytes(dataBytes, numEcBytesInBlock[0]);
/* 378 */       blocks.add(new BlockPair(dataBytes, ecBytes));
/*     */       
/* 380 */       maxNumDataBytes = Math.max(maxNumDataBytes, dataBytes.size());
/* 381 */       maxNumEcBytes = Math.max(maxNumEcBytes, ecBytes.size());
/* 382 */       dataBytesOffset += numDataBytesInBlock[0];
/*     */     } 
/* 384 */     if (numDataBytes != dataBytesOffset) {
/* 385 */       throw new WriterException("Data bytes does not match offset");
/*     */     }
/*     */ 
/*     */     
/* 389 */     for (i = 0; i < maxNumDataBytes; i++) {
/* 390 */       for (int j = 0; j < blocks.size(); j++) {
/* 391 */         ByteArray dataBytes = ((BlockPair)blocks.get(j)).getDataBytes();
/* 392 */         if (i < dataBytes.size()) {
/* 393 */           result.appendBits(dataBytes.at(i), 8);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 398 */     for (i = 0; i < maxNumEcBytes; i++) {
/* 399 */       for (int j = 0; j < blocks.size(); j++) {
/* 400 */         ByteArray ecBytes = ((BlockPair)blocks.get(j)).getErrorCorrectionBytes();
/* 401 */         if (i < ecBytes.size()) {
/* 402 */           result.appendBits(ecBytes.at(i), 8);
/*     */         }
/*     */       } 
/*     */     } 
/* 406 */     if (numTotalBytes != result.sizeInBytes()) {
/* 407 */       throw new WriterException("Interleaving error: " + numTotalBytes + " and " + result
/* 408 */           .sizeInBytes() + " differ.");
/*     */     }
/*     */   }
/*     */   
/*     */   static ByteArray generateECBytes(ByteArray dataBytes, int numEcBytesInBlock) {
/* 413 */     int numDataBytes = dataBytes.size();
/* 414 */     int[] toEncode = new int[numDataBytes + numEcBytesInBlock];
/* 415 */     for (int i = 0; i < numDataBytes; i++) {
/* 416 */       toEncode[i] = dataBytes.at(i);
/*     */     }
/* 418 */     (new ReedSolomonEncoder(GF256.QR_CODE_FIELD)).encode(toEncode, numEcBytesInBlock);
/*     */     
/* 420 */     ByteArray ecBytes = new ByteArray(numEcBytesInBlock);
/* 421 */     for (int j = 0; j < numEcBytesInBlock; j++) {
/* 422 */       ecBytes.set(j, toEncode[numDataBytes + j]);
/*     */     }
/* 424 */     return ecBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void appendModeInfo(Mode mode, BitVector bits) {
/* 431 */     bits.appendBits(mode.getBits(), 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void appendLengthInfo(int numLetters, int version, Mode mode, BitVector bits) throws WriterException {
/* 440 */     int numBits = mode.getCharacterCountBits(Version.getVersionForNumber(version));
/* 441 */     if (numLetters > (1 << numBits) - 1) {
/* 442 */       throw new WriterException(numLetters + "is bigger than" + ((1 << numBits) - 1));
/*     */     }
/* 444 */     bits.appendBits(numLetters, numBits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void appendBytes(String content, Mode mode, BitVector bits, String encoding) throws WriterException {
/* 452 */     if (mode.equals(Mode.NUMERIC)) {
/* 453 */       appendNumericBytes(content, bits);
/* 454 */     } else if (mode.equals(Mode.ALPHANUMERIC)) {
/* 455 */       appendAlphanumericBytes(content, bits);
/* 456 */     } else if (mode.equals(Mode.BYTE)) {
/* 457 */       append8BitBytes(content, bits, encoding);
/* 458 */     } else if (mode.equals(Mode.KANJI)) {
/* 459 */       appendKanjiBytes(content, bits);
/*     */     } else {
/* 461 */       throw new WriterException("Invalid mode: " + mode);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void appendNumericBytes(String content, BitVector bits) {
/* 466 */     int length = content.length();
/* 467 */     int i = 0;
/* 468 */     while (i < length) {
/* 469 */       int num1 = content.charAt(i) - 48;
/* 470 */       if (i + 2 < length) {
/*     */         
/* 472 */         int num2 = content.charAt(i + 1) - 48;
/* 473 */         int num3 = content.charAt(i + 2) - 48;
/* 474 */         bits.appendBits(num1 * 100 + num2 * 10 + num3, 10);
/* 475 */         i += 3; continue;
/* 476 */       }  if (i + 1 < length) {
/*     */         
/* 478 */         int num2 = content.charAt(i + 1) - 48;
/* 479 */         bits.appendBits(num1 * 10 + num2, 7);
/* 480 */         i += 2;
/*     */         continue;
/*     */       } 
/* 483 */       bits.appendBits(num1, 4);
/* 484 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void appendAlphanumericBytes(String content, BitVector bits) throws WriterException {
/* 490 */     int length = content.length();
/* 491 */     int i = 0;
/* 492 */     while (i < length) {
/* 493 */       int code1 = getAlphanumericCode(content.charAt(i));
/* 494 */       if (code1 == -1) {
/* 495 */         throw new WriterException();
/*     */       }
/* 497 */       if (i + 1 < length) {
/* 498 */         int code2 = getAlphanumericCode(content.charAt(i + 1));
/* 499 */         if (code2 == -1) {
/* 500 */           throw new WriterException();
/*     */         }
/*     */         
/* 503 */         bits.appendBits(code1 * 45 + code2, 11);
/* 504 */         i += 2;
/*     */         continue;
/*     */       } 
/* 507 */       bits.appendBits(code1, 6);
/* 508 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void append8BitBytes(String content, BitVector bits, String encoding) throws WriterException {
/*     */     byte[] bytes;
/*     */     try {
/* 517 */       bytes = content.getBytes(encoding);
/* 518 */     } catch (UnsupportedEncodingException uee) {
/* 519 */       throw new WriterException(uee.toString());
/*     */     } 
/* 521 */     for (int i = 0; i < bytes.length; i++) {
/* 522 */       bits.appendBits(bytes[i], 8);
/*     */     }
/*     */   }
/*     */   
/*     */   static void appendKanjiBytes(String content, BitVector bits) throws WriterException {
/*     */     byte[] bytes;
/*     */     try {
/* 529 */       bytes = content.getBytes("Shift_JIS");
/* 530 */     } catch (UnsupportedEncodingException uee) {
/* 531 */       throw new WriterException(uee.toString());
/*     */     } 
/* 533 */     int length = bytes.length;
/* 534 */     for (int i = 0; i < length; i += 2) {
/* 535 */       int byte1 = bytes[i] & 0xFF;
/* 536 */       int byte2 = bytes[i + 1] & 0xFF;
/* 537 */       int code = byte1 << 8 | byte2;
/* 538 */       int subtracted = -1;
/* 539 */       if (code >= 33088 && code <= 40956) {
/* 540 */         subtracted = code - 33088;
/* 541 */       } else if (code >= 57408 && code <= 60351) {
/* 542 */         subtracted = code - 49472;
/*     */       } 
/* 544 */       if (subtracted == -1) {
/* 545 */         throw new WriterException("Invalid byte sequence");
/*     */       }
/* 547 */       int encoded = (subtracted >> 8) * 192 + (subtracted & 0xFF);
/* 548 */       bits.appendBits(encoded, 13);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void appendECI(CharacterSetECI eci, BitVector bits) {
/* 553 */     bits.appendBits(Mode.ECI.getBits(), 4);
/*     */     
/* 555 */     bits.appendBits(eci.getValue(), 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/Encoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */