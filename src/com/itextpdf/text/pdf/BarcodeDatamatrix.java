/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.BadElementException;
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
/*      */ import java.awt.Canvas;
/*      */ import java.awt.Color;
/*      */ import java.awt.Image;
/*      */ import java.awt.image.MemoryImageSource;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BarcodeDatamatrix
/*      */ {
/*      */   public static final int DM_NO_ERROR = 0;
/*      */   public static final int DM_ERROR_TEXT_TOO_BIG = 1;
/*      */   public static final int DM_ERROR_INVALID_SQUARE = 3;
/*      */   public static final int DM_ERROR_EXTENSION = 5;
/*      */   public static final int DM_AUTO = 0;
/*      */   public static final int DM_ASCII = 1;
/*      */   public static final int DM_C40 = 2;
/*      */   public static final int DM_TEXT = 3;
/*      */   public static final int DM_B256 = 4;
/*      */   public static final int DM_X12 = 5;
/*      */   @Deprecated
/*      */   public static final int DM_X21 = 5;
/*      */   public static final int DM_EDIFACT = 6;
/*      */   public static final int DM_RAW = 7;
/*      */   public static final int DM_EXTENSION = 32;
/*      */   public static final int DM_TEST = 64;
/*      */   public static final String DEFAULT_DATA_MATRIX_ENCODING = "iso-8859-1";
/*      */   private static final byte LATCH_B256 = -25;
/*      */   private static final byte LATCH_EDIFACT = -16;
/*      */   private static final byte LATCH_X12 = -18;
/*      */   private static final byte LATCH_TEXT = -17;
/*      */   private static final byte LATCH_C40 = -26;
/*      */   private static final byte UNLATCH = -2;
/*      */   private static final byte EXTENDED_ASCII = -21;
/*      */   private static final byte PADDING = -127;
/*      */   private String encoding;
/*  145 */   private static final DmParams[] dmSizes = new DmParams[] { new DmParams(10, 10, 10, 10, 3, 3, 5), new DmParams(12, 12, 12, 12, 5, 5, 7), new DmParams(8, 18, 8, 18, 5, 5, 7), new DmParams(14, 14, 14, 14, 8, 8, 10), new DmParams(8, 32, 8, 16, 10, 10, 11), new DmParams(16, 16, 16, 16, 12, 12, 12), new DmParams(12, 26, 12, 26, 16, 16, 14), new DmParams(18, 18, 18, 18, 18, 18, 14), new DmParams(20, 20, 20, 20, 22, 22, 18), new DmParams(12, 36, 12, 18, 22, 22, 18), new DmParams(22, 22, 22, 22, 30, 30, 20), new DmParams(16, 36, 16, 18, 32, 32, 24), new DmParams(24, 24, 24, 24, 36, 36, 24), new DmParams(26, 26, 26, 26, 44, 44, 28), new DmParams(16, 48, 16, 24, 49, 49, 28), new DmParams(32, 32, 16, 16, 62, 62, 36), new DmParams(36, 36, 18, 18, 86, 86, 42), new DmParams(40, 40, 20, 20, 114, 114, 48), new DmParams(44, 44, 22, 22, 144, 144, 56), new DmParams(48, 48, 24, 24, 174, 174, 68), new DmParams(52, 52, 26, 26, 204, 102, 42), new DmParams(64, 64, 16, 16, 280, 140, 56), new DmParams(72, 72, 18, 18, 368, 92, 36), new DmParams(80, 80, 20, 20, 456, 114, 48), new DmParams(88, 88, 22, 22, 576, 144, 56), new DmParams(96, 96, 24, 24, 696, 174, 68), new DmParams(104, 104, 26, 26, 816, 136, 56), new DmParams(120, 120, 20, 20, 1050, 175, 68), new DmParams(132, 132, 22, 22, 1304, 163, 62), new DmParams(144, 144, 24, 24, 1558, 156, 62) };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String X12 = "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*      */ 
/*      */ 
/*      */   
/*      */   private int extOut;
/*      */ 
/*      */ 
/*      */   
/*      */   private short[] place;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] image;
/*      */ 
/*      */ 
/*      */   
/*      */   private int height;
/*      */ 
/*      */ 
/*      */   
/*      */   private int width;
/*      */ 
/*      */ 
/*      */   
/*      */   private int ws;
/*      */ 
/*      */ 
/*      */   
/*      */   private int options;
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[][] f;
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[][] switchMode;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean forceSquareSize = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BarcodeDatamatrix() {
/*  196 */     this.encoding = "iso-8859-1";
/*      */   }
/*      */   public BarcodeDatamatrix(String code) throws UnsupportedEncodingException {
/*  199 */     this.encoding = "iso-8859-1";
/*  200 */     generate(code);
/*      */   }
/*      */ 
/*      */   
/*      */   public BarcodeDatamatrix(String code, String encoding) throws UnsupportedEncodingException {
/*  205 */     this.encoding = encoding;
/*  206 */     generate(code);
/*      */   }
/*      */   
/*      */   private void setBit(int x, int y, int xByte) {
/*  210 */     this.image[y * xByte + x / 8] = (byte)(this.image[y * xByte + x / 8] | (byte)(128 >> (x & 0x7)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void draw(byte[] data, int dataSize, DmParams dm) {
/*  215 */     int xByte = (dm.width + this.ws * 2 + 7) / 8;
/*  216 */     Arrays.fill(this.image, (byte)0);
/*      */     
/*      */     int i;
/*  219 */     for (i = this.ws; i < dm.height + this.ws; i += dm.heightSection) {
/*  220 */       for (int j = this.ws; j < dm.width + this.ws; j += 2) {
/*  221 */         setBit(j, i, xByte);
/*      */       }
/*      */     } 
/*      */     
/*  225 */     for (i = dm.heightSection - 1 + this.ws; i < dm.height + this.ws; i += dm.heightSection) {
/*  226 */       for (int j = this.ws; j < dm.width + this.ws; j++) {
/*  227 */         setBit(j, i, xByte);
/*      */       }
/*      */     } 
/*      */     
/*  231 */     for (i = this.ws; i < dm.width + this.ws; i += dm.widthSection) {
/*  232 */       for (int j = this.ws; j < dm.height + this.ws; j++) {
/*  233 */         setBit(i, j, xByte);
/*      */       }
/*      */     } 
/*      */     
/*  237 */     for (i = dm.widthSection - 1 + this.ws; i < dm.width + this.ws; i += dm.widthSection) {
/*  238 */       for (int j = 1 + this.ws; j < dm.height + this.ws; j += 2) {
/*  239 */         setBit(i, j, xByte);
/*      */       }
/*      */     } 
/*  242 */     int p = 0; int ys;
/*  243 */     for (ys = 0; ys < dm.height; ys += dm.heightSection) {
/*  244 */       for (int y = 1; y < dm.heightSection - 1; y++) {
/*  245 */         int xs; for (xs = 0; xs < dm.width; xs += dm.widthSection) {
/*  246 */           for (int x = 1; x < dm.widthSection - 1; x++) {
/*  247 */             int z = this.place[p++];
/*  248 */             if (z == 1 || (z > 1 && (data[z / 8 - 1] & 0xFF & 128 >> z % 8) != 0)) {
/*  249 */               setBit(x + xs + this.ws, y + ys + this.ws, xByte);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void makePadding(byte[] data, int position, int count) {
/*  258 */     if (count <= 0)
/*      */       return; 
/*  260 */     data[position++] = -127;
/*  261 */     while (--count > 0) {
/*  262 */       int t = 129 + (position + 1) * 149 % 253 + 1;
/*  263 */       if (t > 254)
/*  264 */         t -= 254; 
/*  265 */       data[position++] = (byte)t;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isDigit(int c) {
/*  270 */     return (c >= 48 && c <= 57);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int asciiEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, int symbolIndex, int prevEnc, int origDataOffset) {
/*  276 */     int ptrIn = textOffset;
/*  277 */     int ptrOut = dataOffset;
/*  278 */     textLength += textOffset;
/*  279 */     dataLength += dataOffset;
/*  280 */     while (ptrIn < textLength) {
/*  281 */       int c = text[ptrIn++] & 0xFF;
/*  282 */       if (isDigit(c) && symbolIndex > 0 && prevEnc == 1 && isDigit(text[ptrIn - 2] & 0xFF) && data[dataOffset - 1] > 48 && data[dataOffset - 1] < 59) {
/*      */         
/*  284 */         data[ptrOut - 1] = (byte)(((text[ptrIn - 2] & 0xFF) - 48) * 10 + c - 48 + 130);
/*  285 */         return ptrOut - origDataOffset;
/*      */       } 
/*  287 */       if (ptrOut >= dataLength) {
/*  288 */         return -1;
/*      */       }
/*  290 */       if (isDigit(c) && symbolIndex < 0 && ptrIn < textLength && isDigit(text[ptrIn] & 0xFF)) {
/*  291 */         data[ptrOut++] = (byte)((c - 48) * 10 + (text[ptrIn++] & 0xFF) - 48 + 130); continue;
/*      */       } 
/*  293 */       if (c > 127) {
/*  294 */         if (ptrOut + 1 >= dataLength)
/*  295 */           return -1; 
/*  296 */         data[ptrOut++] = -21;
/*  297 */         data[ptrOut++] = (byte)(c - 128 + 1);
/*      */         continue;
/*      */       } 
/*  300 */       data[ptrOut++] = (byte)(c + 1);
/*      */     } 
/*      */     
/*  303 */     return ptrOut - origDataOffset;
/*      */   }
/*      */   
/*      */   private static int b256Encodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, int symbolIndex, int prevEnc, int origDataOffset) {
/*      */     int minRequiredDataIncrement;
/*  308 */     if (textLength == 0)
/*  309 */       return 0; 
/*  310 */     int simulatedDataOffset = dataOffset;
/*  311 */     if (prevEnc != 4) {
/*  312 */       if (textLength < 250 && textLength + 2 > dataLength)
/*  313 */         return -1; 
/*  314 */       if (textLength >= 250 && textLength + 3 > dataLength)
/*  315 */         return -1; 
/*  316 */       data[dataOffset] = -25;
/*      */     } else {
/*  318 */       int latestModeEntry = symbolIndex - 1;
/*  319 */       while (latestModeEntry > 0 && switchMode[3][latestModeEntry] == 4) {
/*  320 */         latestModeEntry--;
/*      */       }
/*  322 */       textLength = symbolIndex - latestModeEntry + 1;
/*  323 */       if (textLength != 250 && 1 > dataLength)
/*  324 */         return -1; 
/*  325 */       if (textLength == 250 && 2 > dataLength)
/*  326 */         return -1; 
/*  327 */       simulatedDataOffset -= textLength - 1 + ((textLength < 250) ? 2 : 3);
/*      */     } 
/*  329 */     if (textLength < 250) {
/*  330 */       data[simulatedDataOffset + 1] = (byte)textLength;
/*  331 */       minRequiredDataIncrement = (prevEnc != 4) ? 2 : 0;
/*  332 */     } else if (textLength == 250 && prevEnc == 4) {
/*  333 */       data[simulatedDataOffset + 1] = (byte)(textLength / 250 + 249);
/*  334 */       for (int i = dataOffset + 1; i > simulatedDataOffset + 2; i--)
/*  335 */         data[i] = data[i - 1]; 
/*  336 */       data[simulatedDataOffset + 2] = (byte)(textLength % 250);
/*  337 */       minRequiredDataIncrement = 1;
/*      */     } else {
/*  339 */       data[simulatedDataOffset + 1] = (byte)(textLength / 250 + 249);
/*  340 */       data[simulatedDataOffset + 2] = (byte)(textLength % 250);
/*  341 */       minRequiredDataIncrement = (prevEnc != 4) ? 3 : 0;
/*      */     } 
/*  343 */     if (prevEnc == 4)
/*  344 */       textLength = 1; 
/*  345 */     System.arraycopy(text, textOffset, data, minRequiredDataIncrement + dataOffset, textLength);
/*  346 */     for (int j = (prevEnc != 4) ? (dataOffset + 1) : dataOffset; j < minRequiredDataIncrement + textLength + dataOffset; j++) {
/*  347 */       randomizationAlgorithm255(data, j);
/*      */     }
/*  349 */     if (prevEnc == 4)
/*  350 */       randomizationAlgorithm255(data, simulatedDataOffset + 1); 
/*  351 */     return textLength + dataOffset + minRequiredDataIncrement - origDataOffset;
/*      */   }
/*      */   
/*      */   private static void randomizationAlgorithm255(byte[] data, int j) {
/*  355 */     int c = data[j] & 0xFF;
/*  356 */     int prn = 149 * (j + 1) % 255 + 1;
/*  357 */     int tv = c + prn;
/*  358 */     if (tv > 255)
/*  359 */       tv -= 256; 
/*  360 */     data[j] = (byte)tv;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int X12Encodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, int symbolIndex, int prevEnc, int origDataOffset) {
/*  365 */     boolean latch = true;
/*      */     
/*  367 */     if (textLength == 0)
/*  368 */       return 0; 
/*  369 */     int ptrIn = 0;
/*  370 */     int ptrOut = 0;
/*  371 */     byte[] x = new byte[textLength];
/*  372 */     int count = 0;
/*  373 */     for (; ptrIn < textLength; ptrIn++) {
/*  374 */       int i = "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf((char)text[ptrIn + textOffset]);
/*  375 */       if (i >= 0) {
/*  376 */         x[ptrIn] = (byte)i;
/*  377 */         count++;
/*      */       } else {
/*  379 */         x[ptrIn] = 100;
/*  380 */         if (count >= 6)
/*  381 */           count -= count / 3 * 3; 
/*  382 */         for (int j = 0; j < count; j++)
/*  383 */           x[ptrIn - j - 1] = 100; 
/*  384 */         count = 0;
/*      */       } 
/*      */     } 
/*  387 */     if (count >= 6)
/*  388 */       count -= count / 3 * 3; 
/*  389 */     for (int k = 0; k < count; k++)
/*  390 */       x[ptrIn - k - 1] = 100; 
/*  391 */     ptrIn = 0;
/*  392 */     byte c = 0;
/*  393 */     for (; ptrIn < textLength; ptrIn++) {
/*  394 */       c = x[ptrIn];
/*  395 */       if (ptrOut > dataLength)
/*      */         break; 
/*  397 */       if (c < 40) {
/*  398 */         if ((ptrIn == 0 && latch) || (ptrIn > 0 && x[ptrIn - 1] > 40))
/*  399 */           data[dataOffset + ptrOut++] = -18; 
/*  400 */         if (ptrOut + 2 > dataLength)
/*      */           break; 
/*  402 */         int n = 1600 * x[ptrIn] + 40 * x[ptrIn + 1] + x[ptrIn + 2] + 1;
/*  403 */         data[dataOffset + ptrOut++] = (byte)(n / 256);
/*  404 */         data[dataOffset + ptrOut++] = (byte)n;
/*  405 */         ptrIn += 2;
/*      */       } else {
/*  407 */         boolean enterASCII = true;
/*  408 */         if (symbolIndex <= 0) {
/*  409 */           if (ptrIn > 0 && x[ptrIn - 1] < 40)
/*  410 */             data[dataOffset + ptrOut++] = -2; 
/*  411 */         } else if (symbolIndex > 4 && prevEnc == 5 && "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf((char)text[textOffset]) >= 0 && "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf((char)text[textOffset - 1]) >= 0) {
/*  412 */           int latestModeEntry = symbolIndex - 1;
/*  413 */           while (latestModeEntry > 0 && switchMode[4][latestModeEntry] == 5 && "\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
/*  414 */             .indexOf((char)text[textOffset - symbolIndex - latestModeEntry + 1]) >= 0) {
/*  415 */             latestModeEntry--;
/*      */           }
/*  417 */           int unlatch = -1;
/*  418 */           if (symbolIndex - latestModeEntry >= 5) {
/*  419 */             for (int i = 1; i <= symbolIndex - latestModeEntry; i++) {
/*  420 */               if (data[dataOffset - i] == -2) {
/*  421 */                 unlatch = dataOffset - i;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  425 */             int amountOfEncodedWithASCII = (unlatch >= 0) ? (dataOffset - unlatch - 1) : (symbolIndex - latestModeEntry);
/*  426 */             if (amountOfEncodedWithASCII % 3 == 2) {
/*  427 */               enterASCII = false;
/*  428 */               textLength = amountOfEncodedWithASCII + 1;
/*  429 */               textOffset -= amountOfEncodedWithASCII;
/*  430 */               dataLength += (unlatch < 0) ? amountOfEncodedWithASCII : (amountOfEncodedWithASCII + 1);
/*  431 */               dataOffset -= (unlatch < 0) ? amountOfEncodedWithASCII : (amountOfEncodedWithASCII + 1);
/*  432 */               ptrIn = -1;
/*  433 */               latch = (unlatch != dataOffset);
/*  434 */               x = new byte[amountOfEncodedWithASCII + 1];
/*  435 */               for (int j = 0; j <= amountOfEncodedWithASCII; j++) {
/*  436 */                 x[j] = (byte)"\r*> 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf((char)text[textOffset + j]);
/*      */               }
/*      */             } else {
/*  439 */               x = new byte[1];
/*  440 */               x[0] = 100;
/*      */             } 
/*      */           } 
/*      */         } 
/*  444 */         if (enterASCII) {
/*  445 */           int i = asciiEncodation(text, textOffset + ptrIn, 1, data, dataOffset + ptrOut, dataLength, -1, -1, origDataOffset);
/*  446 */           if (i < 0)
/*  447 */             return -1; 
/*  448 */           if (data[dataOffset + ptrOut] == -21)
/*  449 */             ptrOut++; 
/*  450 */           ptrOut++;
/*      */         } 
/*      */       } 
/*      */     } 
/*  454 */     c = 100;
/*  455 */     if (textLength > 0)
/*  456 */       c = x[textLength - 1]; 
/*  457 */     if (ptrIn != textLength)
/*  458 */       return -1; 
/*  459 */     if (c < 40)
/*  460 */       data[dataOffset + ptrOut++] = -2; 
/*  461 */     if (ptrOut > dataLength)
/*  462 */       return -1; 
/*  463 */     return ptrOut + dataOffset - origDataOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int EdifactEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, int symbolIndex, int prevEnc, int origDataOffset, boolean sizeFixed) {
/*  468 */     if (textLength == 0)
/*  469 */       return 0; 
/*  470 */     int ptrIn = 0;
/*  471 */     int ptrOut = 0;
/*  472 */     int edi = 0;
/*  473 */     int pedi = 18;
/*  474 */     boolean ascii = true;
/*  475 */     int latestModeEntryActual = -1, latestModeEntryC40orX12 = -1, prevMode = -1;
/*  476 */     if (prevEnc == 6 && ((text[textOffset] & 0xFF & 0xE0) == 64 || (text[textOffset] & 0xFF & 0xE0) == 32) && (text[textOffset] & 0xFF) != 95 && ((text[textOffset - 1] & 0xFF & 0xE0) == 64 || (text[textOffset - 1] & 0xFF & 0xE0) == 32) && (text[textOffset - 1] & 0xFF) != 95) {
/*      */       
/*  478 */       latestModeEntryActual = symbolIndex - 1;
/*  479 */       while (latestModeEntryActual > 0 && switchMode[5][latestModeEntryActual] == 6) {
/*  480 */         int c = text[textOffset - symbolIndex - latestModeEntryActual + 1] & 0xFF;
/*  481 */         if (((c & 0xE0) == 64 || (c & 0xE0) == 32) && c != 95) {
/*  482 */           latestModeEntryActual--;
/*      */         }
/*      */       } 
/*      */       
/*  486 */       prevMode = (switchMode[5][latestModeEntryActual] == 2 || switchMode[5][latestModeEntryActual] == 5) ? switchMode[5][latestModeEntryActual] : -1;
/*      */       
/*  488 */       if (prevMode > 0)
/*  489 */         latestModeEntryC40orX12 = latestModeEntryActual; 
/*  490 */       while (prevMode > 0 && latestModeEntryC40orX12 > 0 && switchMode[prevMode - 1][latestModeEntryC40orX12] == prevMode) {
/*  491 */         int c = text[textOffset - symbolIndex - latestModeEntryC40orX12 + 1] & 0xFF;
/*  492 */         if (((c & 0xE0) == 64 || (c & 0xE0) == 32) && c != 95) {
/*  493 */           latestModeEntryC40orX12--; continue;
/*      */         } 
/*  495 */         latestModeEntryC40orX12 = -1;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  500 */     int dataSize = dataOffset + dataLength;
/*  501 */     boolean asciiOneSymbol = false;
/*  502 */     if (symbolIndex != -1)
/*  503 */       asciiOneSymbol = true; 
/*  504 */     int dataTaken = 0, dataRequired = 0;
/*  505 */     if (latestModeEntryC40orX12 >= 0 && symbolIndex - latestModeEntryC40orX12 + 1 > 9) {
/*  506 */       textLength = symbolIndex - latestModeEntryC40orX12 + 1;
/*  507 */       dataTaken = 0;
/*  508 */       dataRequired = 0;
/*  509 */       dataRequired += 1 + textLength / 4 * 3;
/*  510 */       if (!sizeFixed && (symbolIndex == text.length - 1 || symbolIndex < 0) && textLength % 4 < 3) {
/*  511 */         dataSize = Integer.MAX_VALUE;
/*  512 */         for (int j = 0; j < dmSizes.length; j++) {
/*  513 */           if ((dmSizes[j]).dataSize >= dataRequired + textLength % 4) {
/*  514 */             dataSize = (dmSizes[j]).dataSize;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*  519 */       if (dataSize - dataOffset - dataRequired <= 2 && textLength % 4 <= 2) {
/*  520 */         dataRequired += textLength % 4;
/*      */       } else {
/*  522 */         dataRequired += textLength % 4 + 1;
/*  523 */         if (textLength % 4 == 3)
/*  524 */           dataRequired--; 
/*      */       } 
/*  526 */       for (int i = dataOffset - 1; i >= 0; i--) {
/*  527 */         dataTaken++;
/*  528 */         if (data[i] == ((prevMode == 2) ? -26 : -18)) {
/*      */           break;
/*      */         }
/*      */       } 
/*  532 */       if (dataRequired <= dataTaken) {
/*  533 */         asciiOneSymbol = false;
/*  534 */         textOffset -= textLength - 1;
/*  535 */         dataOffset -= dataTaken;
/*  536 */         dataLength += dataTaken;
/*      */       } 
/*  538 */     } else if (latestModeEntryActual >= 0 && symbolIndex - latestModeEntryActual + 1 > 9) {
/*  539 */       textLength = symbolIndex - latestModeEntryActual + 1;
/*  540 */       dataRequired += 1 + textLength / 4 * 3;
/*  541 */       if (dataSize - dataOffset - dataRequired <= 2 && textLength % 4 <= 2) {
/*  542 */         dataRequired += textLength % 4;
/*      */       } else {
/*  544 */         dataRequired += textLength % 4 + 1;
/*  545 */         if (textLength % 4 == 3)
/*  546 */           dataRequired--; 
/*      */       } 
/*  548 */       int dataNewOffset = 0;
/*  549 */       int latchEdi = -1;
/*  550 */       for (int i = origDataOffset; i < dataOffset; i++) {
/*  551 */         if (data[i] == -16 && dataOffset - i <= dataRequired) {
/*  552 */           latchEdi = i; break;
/*      */         } 
/*      */       } 
/*  555 */       if (latchEdi != -1) {
/*  556 */         dataTaken += dataOffset - latchEdi;
/*  557 */         if ((text[textOffset] & 0xFF) > 127) {
/*  558 */           dataTaken += 2;
/*      */         } else {
/*  560 */           if (isDigit(text[textOffset] & 0xFF) && isDigit(text[textOffset - 1] & 0xFF) && data[dataOffset - 1] >= 49 && data[dataOffset - 1] <= 58)
/*      */           {
/*  562 */             dataTaken--;
/*      */           }
/*  564 */           dataTaken++;
/*      */         } 
/*  566 */         dataNewOffset = dataOffset - latchEdi;
/*      */       } else {
/*  568 */         for (int j = symbolIndex - latestModeEntryActual; j >= 0; j--) {
/*  569 */           if ((text[textOffset - j] & 0xFF) > 127) {
/*  570 */             dataTaken += 2;
/*      */           } else {
/*  572 */             if (j > 0 && isDigit(text[textOffset - j] & 0xFF) && isDigit(text[textOffset - j + 1] & 0xFF)) {
/*  573 */               if (j == 1)
/*  574 */                 dataNewOffset = dataTaken; 
/*  575 */               j--;
/*      */             } 
/*  577 */             dataTaken++;
/*      */           } 
/*  579 */           if (j == 1)
/*  580 */             dataNewOffset = dataTaken; 
/*      */         } 
/*      */       } 
/*  583 */       if (dataRequired <= dataTaken) {
/*  584 */         asciiOneSymbol = false;
/*  585 */         textOffset -= textLength - 1;
/*  586 */         dataOffset -= dataNewOffset;
/*  587 */         dataLength += dataNewOffset;
/*      */       } 
/*      */     } 
/*  590 */     if (asciiOneSymbol) {
/*  591 */       int c = text[textOffset] & 0xFF;
/*  592 */       if (isDigit(c) && textOffset + ptrIn > 0 && isDigit(text[textOffset - 1] & 0xFF) && prevEnc == 6 && data[dataOffset - 1] >= 49 && data[dataOffset - 1] <= 58) {
/*      */         
/*  594 */         data[dataOffset + ptrOut - 1] = (byte)(((text[textOffset - 1] & 0xFF) - 48) * 10 + c - 48 + 130);
/*  595 */         return dataOffset - origDataOffset;
/*      */       } 
/*  597 */       return asciiEncodation(text, textOffset + ptrIn, 1, data, dataOffset + ptrOut, dataLength, -1, -1, origDataOffset);
/*      */     } 
/*      */     
/*  600 */     for (; ptrIn < textLength; ptrIn++) {
/*  601 */       int c = text[ptrIn + textOffset] & 0xFF;
/*  602 */       if (((c & 0xE0) == 64 || (c & 0xE0) == 32) && c != 95)
/*  603 */       { if (ascii) {
/*  604 */           if (ptrOut + 1 > dataLength)
/*      */             break; 
/*  606 */           data[dataOffset + ptrOut++] = -16;
/*  607 */           ascii = false;
/*      */         } 
/*  609 */         c &= 0x3F;
/*  610 */         edi |= c << pedi;
/*  611 */         if (pedi == 0) {
/*  612 */           if (ptrOut + 3 > dataLength)
/*      */             break; 
/*  614 */           data[dataOffset + ptrOut++] = (byte)(edi >> 16);
/*  615 */           data[dataOffset + ptrOut++] = (byte)(edi >> 8);
/*  616 */           data[dataOffset + ptrOut++] = (byte)edi;
/*  617 */           edi = 0;
/*  618 */           pedi = 18;
/*      */         } else {
/*  620 */           pedi -= 6;
/*      */         }  }
/*  622 */       else { if (!ascii) {
/*  623 */           edi |= 31 << pedi;
/*  624 */           if (ptrOut + 3 - pedi / 8 > dataLength)
/*      */             break; 
/*  626 */           data[dataOffset + ptrOut++] = (byte)(edi >> 16);
/*  627 */           if (pedi <= 12)
/*  628 */             data[dataOffset + ptrOut++] = (byte)(edi >> 8); 
/*  629 */           if (pedi <= 6)
/*  630 */             data[dataOffset + ptrOut++] = (byte)edi; 
/*  631 */           ascii = true;
/*  632 */           pedi = 18;
/*  633 */           edi = 0;
/*      */         } 
/*  635 */         if (isDigit(c) && textOffset + ptrIn > 0 && isDigit(text[textOffset + ptrIn - 1] & 0xFF) && prevEnc == 6 && data[dataOffset - 1] >= 49 && data[dataOffset - 1] <= 58) {
/*      */           
/*  637 */           data[dataOffset + ptrOut - 1] = (byte)(((text[textOffset - 1] & 0xFF) - 48) * 10 + c - 48 + 130);
/*  638 */           ptrOut--;
/*      */         } else {
/*  640 */           int i = asciiEncodation(text, textOffset + ptrIn, 1, data, dataOffset + ptrOut, dataLength, -1, -1, origDataOffset);
/*  641 */           if (i < 0)
/*  642 */             return -1; 
/*  643 */           if (data[dataOffset + ptrOut] == -21)
/*  644 */             ptrOut++; 
/*  645 */           ptrOut++;
/*      */         }  }
/*      */     
/*      */     } 
/*  649 */     if (ptrIn != textLength)
/*  650 */       return -1; 
/*  651 */     if (!sizeFixed && (symbolIndex == text.length - 1 || symbolIndex < 0)) {
/*  652 */       dataSize = Integer.MAX_VALUE;
/*  653 */       for (int i = 0; i < dmSizes.length; i++) {
/*  654 */         if ((dmSizes[i]).dataSize >= dataOffset + ptrOut + 3 - pedi / 6) {
/*  655 */           dataSize = (dmSizes[i]).dataSize;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  660 */     if (dataSize - dataOffset - ptrOut <= 2 && pedi >= 6) {
/*  661 */       if (pedi != 18 && ptrOut + 2 - pedi / 8 > dataLength)
/*  662 */         return -1; 
/*  663 */       if (pedi <= 12) {
/*  664 */         byte val = (byte)(edi >> 18 & 0x3F);
/*  665 */         if ((val & 0x20) == 0)
/*  666 */           val = (byte)(val | 0x40); 
/*  667 */         data[dataOffset + ptrOut++] = (byte)(val + 1);
/*      */       } 
/*  669 */       if (pedi <= 6) {
/*  670 */         byte val = (byte)(edi >> 12 & 0x3F);
/*  671 */         if ((val & 0x20) == 0)
/*  672 */           val = (byte)(val | 0x40); 
/*  673 */         data[dataOffset + ptrOut++] = (byte)(val + 1);
/*      */       } 
/*  675 */     } else if (!ascii) {
/*  676 */       edi |= 31 << pedi;
/*  677 */       if (ptrOut + 3 - pedi / 8 > dataLength)
/*  678 */         return -1; 
/*  679 */       data[dataOffset + ptrOut++] = (byte)(edi >> 16);
/*  680 */       if (pedi <= 12)
/*  681 */         data[dataOffset + ptrOut++] = (byte)(edi >> 8); 
/*  682 */       if (pedi <= 6)
/*  683 */         data[dataOffset + ptrOut++] = (byte)edi; 
/*      */     } 
/*  685 */     return ptrOut + dataOffset - origDataOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int C40OrTextEncodation(byte[] text, int textOffset, int textLength, byte[] data, int dataOffset, int dataLength, boolean c40, int symbolIndex, int prevEnc, int origDataOffset) {
/*      */     String basic, shift3;
/*  692 */     if (textLength == 0)
/*  693 */       return 0; 
/*  694 */     int ptrIn = 0;
/*  695 */     int ptrOut = 0;
/*  696 */     String shift2 = "!\"#$%&'()*+,-./:;<=>?@[\\]^_";
/*  697 */     if (c40) {
/*  698 */       basic = " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*  699 */       shift3 = "`abcdefghijklmnopqrstuvwxyz{|}~";
/*      */     } else {
/*  701 */       basic = " 0123456789abcdefghijklmnopqrstuvwxyz";
/*  702 */       shift3 = "`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}~";
/*      */     } 
/*  704 */     boolean addLatch = true, usingASCII = false;
/*  705 */     int mode = c40 ? 2 : 3;
/*  706 */     if (prevEnc == mode) {
/*  707 */       usingASCII = true;
/*  708 */       int latestModeEntry = symbolIndex - 1;
/*  709 */       while (latestModeEntry > 0 && switchMode[mode - 1][latestModeEntry] == mode) {
/*  710 */         latestModeEntry--;
/*      */       }
/*  712 */       int unlatch = -1;
/*  713 */       int dataAmountOfEncodedWithASCII = 0;
/*  714 */       if (symbolIndex - latestModeEntry >= 5) {
/*  715 */         int j; for (j = symbolIndex - latestModeEntry; j > 0; j--) {
/*  716 */           int c = text[textOffset - j] & 0xFF;
/*  717 */           if (c > 127) {
/*  718 */             dataAmountOfEncodedWithASCII += 2;
/*      */           } else {
/*  720 */             dataAmountOfEncodedWithASCII++;
/*      */           } 
/*  722 */         }  for (j = 1; j <= dataAmountOfEncodedWithASCII && 
/*  723 */           j <= dataOffset; j++) {
/*      */           
/*  725 */           if (data[dataOffset - j] == -2) {
/*  726 */             unlatch = dataOffset - j;
/*      */             break;
/*      */           } 
/*      */         } 
/*  730 */         int amountOfEncodedWithASCII = 0;
/*  731 */         if (unlatch >= 0) {
/*  732 */           for (j = unlatch + 1; j < dataOffset; j++) {
/*  733 */             if (data[j] == -21)
/*  734 */               j++; 
/*  735 */             if (data[j] >= -127 && data[j] <= -27)
/*  736 */               amountOfEncodedWithASCII++; 
/*  737 */             amountOfEncodedWithASCII++;
/*      */           } 
/*      */         } else {
/*  740 */           amountOfEncodedWithASCII = symbolIndex - latestModeEntry;
/*  741 */         }  int dataOffsetNew = 0;
/*  742 */         for (j = amountOfEncodedWithASCII; j > 0; j--) {
/*  743 */           int requiredCapacityForASCII = 0;
/*  744 */           int requiredCapacityForC40orText = 0;
/*  745 */           for (int k = j; k >= 0; k--) {
/*  746 */             int c = text[textOffset - k] & 0xFF;
/*  747 */             if (c > 127) {
/*  748 */               c -= 128;
/*  749 */               requiredCapacityForC40orText += 2;
/*      */             } 
/*  751 */             requiredCapacityForC40orText += (basic.indexOf((char)c) >= 0) ? 1 : 2;
/*  752 */             if (c > 127) {
/*  753 */               requiredCapacityForASCII += 2;
/*      */             } else {
/*  755 */               if (k > 0 && isDigit(c) && isDigit(text[textOffset - k + 1] & 0xFF)) {
/*  756 */                 requiredCapacityForC40orText += (basic.indexOf((char)text[textOffset - k + 1]) >= 0) ? 1 : 2;
/*  757 */                 k--;
/*  758 */                 dataOffsetNew = requiredCapacityForASCII + 1;
/*      */               } 
/*  760 */               requiredCapacityForASCII++;
/*      */             } 
/*  762 */             if (k == 1)
/*  763 */               dataOffsetNew = requiredCapacityForASCII; 
/*      */           } 
/*  765 */           addLatch = (unlatch < 0) ? true : ((dataOffset - requiredCapacityForASCII != unlatch));
/*  766 */           if (requiredCapacityForC40orText % 3 == 0 && requiredCapacityForC40orText / 3 * 2 + (addLatch ? 2 : 0) < requiredCapacityForASCII) {
/*      */             
/*  768 */             usingASCII = false;
/*  769 */             textLength = j + 1;
/*  770 */             textOffset -= j;
/*  771 */             dataOffset -= addLatch ? dataOffsetNew : (dataOffsetNew + 1);
/*  772 */             dataLength += addLatch ? dataOffsetNew : (dataOffsetNew + 1);
/*      */             break;
/*      */           } 
/*  775 */           if (isDigit(text[textOffset - j] & 0xFF) && isDigit(text[textOffset - j + 1] & 0xFF))
/*  776 */             j--; 
/*      */         } 
/*      */       } 
/*  779 */     } else if (symbolIndex != -1) {
/*  780 */       usingASCII = true;
/*  781 */     }  if (usingASCII)
/*  782 */       return asciiEncodation(text, textOffset, 1, data, dataOffset, dataLength, (prevEnc == mode) ? 1 : -1, 1, origDataOffset); 
/*  783 */     if (addLatch)
/*  784 */       if (c40) {
/*  785 */         data[dataOffset + ptrOut++] = -26;
/*      */       } else {
/*  787 */         data[dataOffset + ptrOut++] = -17;
/*      */       }  
/*  789 */     int[] enc = new int[textLength * 4 + 10];
/*  790 */     int encPtr = 0;
/*  791 */     int last0 = 0;
/*  792 */     int last1 = 0;
/*  793 */     while (ptrIn < textLength) {
/*  794 */       if (encPtr % 3 == 0) {
/*  795 */         last0 = ptrIn;
/*  796 */         last1 = encPtr;
/*      */       } 
/*  798 */       int c = text[textOffset + ptrIn++] & 0xFF;
/*  799 */       if (c > 127) {
/*  800 */         c -= 128;
/*  801 */         enc[encPtr++] = 1;
/*  802 */         enc[encPtr++] = 30;
/*      */       } 
/*  804 */       int idx = basic.indexOf((char)c);
/*  805 */       if (idx >= 0) {
/*  806 */         enc[encPtr++] = idx + 3; continue;
/*  807 */       }  if (c < 32) {
/*  808 */         enc[encPtr++] = 0;
/*  809 */         enc[encPtr++] = c; continue;
/*  810 */       }  if ((idx = shift2.indexOf((char)c)) >= 0) {
/*  811 */         enc[encPtr++] = 1;
/*  812 */         enc[encPtr++] = idx; continue;
/*  813 */       }  if ((idx = shift3.indexOf((char)c)) >= 0) {
/*  814 */         enc[encPtr++] = 2;
/*  815 */         enc[encPtr++] = idx;
/*      */       } 
/*      */     } 
/*  818 */     if (encPtr % 3 != 0) {
/*  819 */       ptrIn = last0;
/*  820 */       encPtr = last1;
/*      */     } 
/*  822 */     if (encPtr / 3 * 2 > dataLength - 2) {
/*  823 */       return -1;
/*      */     }
/*  825 */     int i = 0;
/*  826 */     for (; i < encPtr; i += 3) {
/*  827 */       int a = 1600 * enc[i] + 40 * enc[i + 1] + enc[i + 2] + 1;
/*  828 */       data[dataOffset + ptrOut++] = (byte)(a / 256);
/*  829 */       data[dataOffset + ptrOut++] = (byte)a;
/*      */     } 
/*  831 */     if (dataLength - ptrOut > 2)
/*  832 */       data[dataOffset + ptrOut++] = -2; 
/*  833 */     if (symbolIndex < 0 && textLength > ptrIn) {
/*  834 */       i = asciiEncodation(text, textOffset + ptrIn, textLength - ptrIn, data, dataOffset + ptrOut, dataLength - ptrOut, -1, -1, origDataOffset);
/*  835 */       return i;
/*      */     } 
/*  837 */     return ptrOut + dataOffset - origDataOffset;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int minValueInColumn(int[][] array, int column) {
/*  842 */     int min = Integer.MAX_VALUE;
/*  843 */     for (int i = 0; i < 6; i++) {
/*  844 */       if (array[i][column] < min && array[i][column] >= 0)
/*  845 */         min = array[i][column]; 
/*  846 */     }  return (min != Integer.MAX_VALUE) ? min : -1;
/*      */   }
/*      */   
/*      */   private static int valuePositionInColumn(int[][] array, int column, int value) {
/*  850 */     for (int i = 0; i < 6; i++) {
/*  851 */       if (array[i][column] == value)
/*  852 */         return i; 
/*  853 */     }  return -1;
/*      */   }
/*      */   
/*      */   private static void solveFAndSwitchMode(int[] forMin, int mode, int currIndex) {
/*  857 */     if (forMin[mode] >= 0 && f[mode][currIndex - 1] >= 0) {
/*  858 */       f[mode][currIndex] = forMin[mode];
/*  859 */       switchMode[mode][currIndex] = mode + 1;
/*      */     } else {
/*  861 */       f[mode][currIndex] = Integer.MAX_VALUE;
/*      */     } 
/*  863 */     for (int i = 0; i < 6; i++) {
/*  864 */       if (forMin[i] < f[mode][currIndex] && forMin[i] >= 0 && f[i][currIndex - 1] >= 0) {
/*  865 */         f[mode][currIndex] = forMin[i];
/*  866 */         switchMode[mode][currIndex] = i + 1;
/*      */       } 
/*      */     } 
/*  869 */     if (f[mode][currIndex] == Integer.MAX_VALUE) {
/*  870 */       f[mode][currIndex] = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getEncodation(byte[] text, int textOffset, int textSize, byte[] data, int dataOffset, int dataSize, int options, boolean sizeFixed) {
/*  877 */     if (dataSize < 0)
/*  878 */       return -1; 
/*  879 */     options &= 0x7;
/*  880 */     if (options == 0) {
/*  881 */       if (textSize == 0)
/*  882 */         return 0; 
/*  883 */       byte[][] dataDynamic = new byte[6][data.length];
/*  884 */       for (int i = 0; i < 6; i++) {
/*  885 */         System.arraycopy(data, 0, dataDynamic[i], 0, data.length);
/*  886 */         switchMode[i][0] = i + 1;
/*      */       } 
/*  888 */       f[0][0] = asciiEncodation(text, textOffset, 1, dataDynamic[0], dataOffset, dataSize, 0, -1, dataOffset);
/*  889 */       f[1][0] = C40OrTextEncodation(text, textOffset, 1, dataDynamic[1], dataOffset, dataSize, true, 0, -1, dataOffset);
/*  890 */       f[2][0] = C40OrTextEncodation(text, textOffset, 1, dataDynamic[2], dataOffset, dataSize, false, 0, -1, dataOffset);
/*  891 */       f[3][0] = b256Encodation(text, textOffset, 1, dataDynamic[3], dataOffset, dataSize, 0, -1, dataOffset);
/*  892 */       f[4][0] = X12Encodation(text, textOffset, 1, dataDynamic[4], dataOffset, dataSize, 0, -1, dataOffset);
/*  893 */       f[5][0] = EdifactEncodation(text, textOffset, 1, dataDynamic[5], dataOffset, dataSize, 0, -1, dataOffset, sizeFixed);
/*  894 */       int[] dataNewOffset = new int[6];
/*  895 */       for (int j = 1; j < textSize; j++) {
/*  896 */         int[] tempForMin = new int[6];
/*  897 */         for (int k = 0; k < 6; k++) {
/*  898 */           dataNewOffset[k] = (f[k][j - 1] >= 0) ? f[k][j - 1] : Integer.MAX_VALUE;
/*      */         }
/*  900 */         for (int currEnc = 0; currEnc < 6; currEnc++) {
/*  901 */           byte[][] dataDynamicInner = new byte[6][data.length];
/*  902 */           for (int prevEnc = 0; prevEnc < 6; prevEnc++) {
/*  903 */             System.arraycopy(dataDynamic[prevEnc], 0, dataDynamicInner[prevEnc], 0, data.length);
/*  904 */             if (currEnc == 0)
/*  905 */               tempForMin[prevEnc] = asciiEncodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], j, prevEnc + 1, dataOffset); 
/*  906 */             if (currEnc == 1)
/*  907 */               tempForMin[prevEnc] = C40OrTextEncodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], true, j, prevEnc + 1, dataOffset); 
/*  908 */             if (currEnc == 2)
/*  909 */               tempForMin[prevEnc] = C40OrTextEncodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], false, j, prevEnc + 1, dataOffset); 
/*  910 */             if (currEnc == 3)
/*  911 */               tempForMin[prevEnc] = b256Encodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], j, prevEnc + 1, dataOffset); 
/*  912 */             if (currEnc == 4)
/*  913 */               tempForMin[prevEnc] = X12Encodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], j, prevEnc + 1, dataOffset); 
/*  914 */             if (currEnc == 5) {
/*  915 */               tempForMin[prevEnc] = EdifactEncodation(text, textOffset + j, 1, dataDynamicInner[prevEnc], dataNewOffset[prevEnc] + dataOffset, dataSize - dataNewOffset[prevEnc], j, prevEnc + 1, dataOffset, sizeFixed);
/*      */             }
/*      */           } 
/*  918 */           solveFAndSwitchMode(tempForMin, currEnc, j);
/*  919 */           if (switchMode[currEnc][j] != 0)
/*  920 */             System.arraycopy(dataDynamicInner[switchMode[currEnc][j] - 1], 0, dataDynamic[currEnc], 0, data.length); 
/*      */         } 
/*      */       } 
/*  923 */       int e = minValueInColumn(f, textSize - 1);
/*  924 */       if (e > dataSize || e < 0)
/*  925 */         return -1; 
/*  926 */       int bestDataDynamicResultIndex = valuePositionInColumn(f, textSize - 1, e);
/*  927 */       System.arraycopy(dataDynamic[bestDataDynamicResultIndex], 0, data, 0, data.length);
/*  928 */       return e;
/*      */     } 
/*  930 */     switch (options) {
/*      */       case 1:
/*  932 */         return asciiEncodation(text, textOffset, textSize, data, dataOffset, dataSize, -1, -1, dataOffset);
/*      */       case 2:
/*  934 */         return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, true, -1, -1, dataOffset);
/*      */       case 3:
/*  936 */         return C40OrTextEncodation(text, textOffset, textSize, data, dataOffset, dataSize, false, -1, -1, dataOffset);
/*      */       case 4:
/*  938 */         return b256Encodation(text, textOffset, textSize, data, dataOffset, dataSize, -1, -1, dataOffset);
/*      */       case 5:
/*  940 */         return X12Encodation(text, textOffset, textSize, data, dataOffset, dataSize, -1, -1, dataOffset);
/*      */       case 6:
/*  942 */         return EdifactEncodation(text, textOffset, textSize, data, dataOffset, dataSize, -1, -1, dataOffset, sizeFixed);
/*      */       case 7:
/*  944 */         if (textSize > dataSize)
/*  945 */           return -1; 
/*  946 */         System.arraycopy(text, textOffset, data, dataOffset, textSize);
/*  947 */         return textSize;
/*      */     } 
/*  949 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getNumber(byte[] text, int ptrIn, int n) {
/*  954 */     int v = 0;
/*  955 */     for (int j = 0; j < n; j++) {
/*  956 */       int c = text[ptrIn++] & 0xFF;
/*  957 */       if (c < 48 || c > 57)
/*  958 */         return -1; 
/*  959 */       v = v * 10 + c - 48;
/*      */     } 
/*  961 */     return v;
/*      */   }
/*      */ 
/*      */   
/*      */   private int processExtensions(byte[] text, int textOffset, int textSize, byte[] data) {
/*  966 */     if ((this.options & 0x20) == 0)
/*  967 */       return 0; 
/*  968 */     int order = 0;
/*  969 */     int ptrIn = 0;
/*  970 */     int ptrOut = 0;
/*  971 */     while (ptrIn < textSize) {
/*  972 */       int eci, fn, ft, fi; if (order > 20)
/*  973 */         return -1; 
/*  974 */       int c = text[textOffset + ptrIn++] & 0xFF;
/*  975 */       order++;
/*  976 */       switch (c) {
/*      */         case 46:
/*  978 */           this.extOut = ptrIn;
/*  979 */           return ptrOut;
/*      */         case 101:
/*  981 */           if (ptrIn + 6 > textSize)
/*  982 */             return -1; 
/*  983 */           eci = getNumber(text, textOffset + ptrIn, 6);
/*  984 */           if (eci < 0)
/*  985 */             return -1; 
/*  986 */           ptrIn += 6;
/*  987 */           data[ptrOut++] = -15;
/*  988 */           if (eci < 127) {
/*  989 */             data[ptrOut++] = (byte)(eci + 1); continue;
/*  990 */           }  if (eci < 16383) {
/*  991 */             data[ptrOut++] = (byte)((eci - 127) / 254 + 128);
/*  992 */             data[ptrOut++] = (byte)((eci - 127) % 254 + 1);
/*      */             continue;
/*      */           } 
/*  995 */           data[ptrOut++] = (byte)((eci - 16383) / 64516 + 192);
/*  996 */           data[ptrOut++] = (byte)((eci - 16383) / 254 % 254 + 1);
/*  997 */           data[ptrOut++] = (byte)((eci - 16383) % 254 + 1);
/*      */ 
/*      */         
/*      */         case 115:
/* 1001 */           if (order != 1)
/* 1002 */             return -1; 
/* 1003 */           if (ptrIn + 9 > textSize)
/* 1004 */             return -1; 
/* 1005 */           fn = getNumber(text, textOffset + ptrIn, 2);
/* 1006 */           if (fn <= 0 || fn > 16)
/* 1007 */             return -1; 
/* 1008 */           ptrIn += 2;
/* 1009 */           ft = getNumber(text, textOffset + ptrIn, 2);
/* 1010 */           if (ft <= 1 || ft > 16)
/* 1011 */             return -1; 
/* 1012 */           ptrIn += 2;
/* 1013 */           fi = getNumber(text, textOffset + ptrIn, 5);
/* 1014 */           if (fi < 0 || fn >= 64516)
/* 1015 */             return -1; 
/* 1016 */           ptrIn += 5;
/* 1017 */           data[ptrOut++] = -23;
/* 1018 */           data[ptrOut++] = (byte)(fn - 1 << 4 | 17 - ft);
/* 1019 */           data[ptrOut++] = (byte)(fi / 254 + 1);
/* 1020 */           data[ptrOut++] = (byte)(fi % 254 + 1);
/*      */         
/*      */         case 112:
/* 1023 */           if (order != 1)
/* 1024 */             return -1; 
/* 1025 */           data[ptrOut++] = -22;
/*      */         
/*      */         case 109:
/* 1028 */           if (order != 1)
/* 1029 */             return -1; 
/* 1030 */           if (ptrIn + 1 > textSize)
/* 1031 */             return -1; 
/* 1032 */           c = text[textOffset + ptrIn++] & 0xFF;
/* 1033 */           if (c != 53 && c != 53)
/* 1034 */             return -1; 
/* 1035 */           data[ptrOut++] = -22;
/* 1036 */           data[ptrOut++] = (byte)((c == 53) ? 236 : 237);
/*      */         
/*      */         case 102:
/* 1039 */           if (order != 1 && (order != 2 || (text[textOffset] != 115 && text[textOffset] != 109)))
/* 1040 */             return -1; 
/* 1041 */           data[ptrOut++] = -24;
/*      */       } 
/*      */     } 
/* 1044 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int generate(String text) throws UnsupportedEncodingException {
/*      */     byte[] t;
/*      */     try {
/* 1061 */       t = text.getBytes(this.encoding);
/* 1062 */     } catch (UnsupportedEncodingException exc) {
/* 1063 */       throw new IllegalArgumentException("text has to be encoded in iso-8859-1");
/*      */     } 
/* 1065 */     return generate(t, 0, t.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int generate(byte[] text, int textOffset, int textSize) {
/*      */     DmParams dm;
/* 1084 */     byte[] data = new byte[2500];
/* 1085 */     this.extOut = 0;
/* 1086 */     int extCount = processExtensions(text, textOffset, textSize, data);
/* 1087 */     if (extCount < 0) {
/* 1088 */       return 5;
/*      */     }
/* 1090 */     int e = -1;
/* 1091 */     f = new int[6][textSize - this.extOut];
/* 1092 */     switchMode = new int[6][textSize - this.extOut];
/* 1093 */     if (this.height == 0 || this.width == 0) {
/* 1094 */       DmParams last = dmSizes[dmSizes.length - 1];
/* 1095 */       e = getEncodation(text, textOffset + this.extOut, textSize - this.extOut, data, extCount, last.dataSize - extCount, this.options, false);
/* 1096 */       if (e < 0) {
/* 1097 */         return 1;
/*      */       }
/* 1099 */       e += extCount; int k;
/* 1100 */       for (k = 0; k < dmSizes.length && 
/* 1101 */         (dmSizes[k]).dataSize < e; k++);
/*      */ 
/*      */       
/* 1104 */       dm = dmSizes[k];
/* 1105 */       this.height = dm.height;
/* 1106 */       this.width = dm.width;
/*      */     } else {
/* 1108 */       int k; for (k = 0; k < dmSizes.length && (
/* 1109 */         this.height != (dmSizes[k]).height || this.width != (dmSizes[k]).width); k++);
/*      */ 
/*      */       
/* 1112 */       if (k == dmSizes.length) {
/* 1113 */         return 3;
/*      */       }
/* 1115 */       dm = dmSizes[k];
/* 1116 */       e = getEncodation(text, textOffset + this.extOut, textSize - this.extOut, data, extCount, dm.dataSize - extCount, this.options, true);
/* 1117 */       if (e < 0) {
/* 1118 */         return 1;
/*      */       }
/* 1120 */       e += extCount;
/*      */     } 
/* 1122 */     if ((this.options & 0x40) != 0) {
/* 1123 */       return 0;
/*      */     }
/* 1125 */     this.image = new byte[(dm.width + 2 * this.ws + 7) / 8 * (dm.height + 2 * this.ws)];
/* 1126 */     makePadding(data, e, dm.dataSize - e);
/* 1127 */     this.place = Placement.doPlacement(dm.height - dm.height / dm.heightSection * 2, dm.width - dm.width / dm.widthSection * 2);
/* 1128 */     int full = dm.dataSize + (dm.dataSize + 2) / dm.dataBlock * dm.errorBlock;
/* 1129 */     ReedSolomon.generateECC(data, dm.dataSize, dm.dataBlock, dm.errorBlock);
/* 1130 */     draw(data, full, dm);
/* 1131 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image createImage() throws BadElementException {
/* 1140 */     if (this.image == null)
/* 1141 */       return null; 
/* 1142 */     byte[] g4 = CCITTG4Encoder.compress(this.image, this.width + 2 * this.ws, this.height + 2 * this.ws);
/* 1143 */     return Image.getInstance(this.width + 2 * this.ws, this.height + 2 * this.ws, false, 256, 0, g4, null);
/*      */   }
/*      */   private static class DmParams { int height;
/*      */     
/*      */     DmParams(int height, int width, int heightSection, int widthSection, int dataSize, int dataBlock, int errorBlock) {
/* 1148 */       this.height = height;
/* 1149 */       this.width = width;
/* 1150 */       this.heightSection = heightSection;
/* 1151 */       this.widthSection = widthSection;
/* 1152 */       this.dataSize = dataSize;
/* 1153 */       this.dataBlock = dataBlock;
/* 1154 */       this.errorBlock = errorBlock;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int width;
/*      */     
/*      */     int heightSection;
/*      */     
/*      */     int widthSection;
/*      */     
/*      */     int dataSize;
/*      */     
/*      */     int dataBlock;
/*      */     
/*      */     int errorBlock; }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getImage() {
/* 1174 */     return this.image;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeight() {
/* 1183 */     return this.height;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeight(int height) {
/* 1223 */     this.height = height;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWidth() {
/* 1232 */     return this.width;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidth(int width) {
/* 1272 */     this.width = width;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWs() {
/* 1280 */     return this.ws;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWs(int ws) {
/* 1288 */     this.ws = ws;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOptions() {
/* 1296 */     return this.options;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOptions(int options) {
/* 1327 */     this.options = options;
/*      */   }
/*      */   
/*      */   public void setForceSquareSize(boolean forceSquareSize) {
/* 1331 */     this.forceSquareSize = forceSquareSize;
/*      */   }
/*      */   
/*      */   static class Placement {
/*      */     private int nrow;
/*      */     private int ncol;
/*      */     private short[] array;
/* 1338 */     private static final Hashtable<Integer, short[]> cache = (Hashtable)new Hashtable<Integer, short>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static short[] doPlacement(int nrow, int ncol) {
/* 1344 */       Integer key = Integer.valueOf(nrow * 1000 + ncol);
/* 1345 */       short[] pc = cache.get(key);
/* 1346 */       if (pc != null)
/* 1347 */         return pc; 
/* 1348 */       Placement p = new Placement();
/* 1349 */       p.nrow = nrow;
/* 1350 */       p.ncol = ncol;
/* 1351 */       p.array = new short[nrow * ncol];
/* 1352 */       p.ecc200();
/* 1353 */       cache.put(key, p.array);
/* 1354 */       return p.array;
/*      */     }
/*      */ 
/*      */     
/*      */     private void module(int row, int col, int chr, int bit) {
/* 1359 */       if (row < 0) { row += this.nrow; col += 4 - (this.nrow + 4) % 8; }
/* 1360 */        if (col < 0) { col += this.ncol; row += 4 - (this.ncol + 4) % 8; }
/* 1361 */        this.array[row * this.ncol + col] = (short)(8 * chr + bit);
/*      */     }
/*      */     
/*      */     private void utah(int row, int col, int chr) {
/* 1365 */       module(row - 2, col - 2, chr, 0);
/* 1366 */       module(row - 2, col - 1, chr, 1);
/* 1367 */       module(row - 1, col - 2, chr, 2);
/* 1368 */       module(row - 1, col - 1, chr, 3);
/* 1369 */       module(row - 1, col, chr, 4);
/* 1370 */       module(row, col - 2, chr, 5);
/* 1371 */       module(row, col - 1, chr, 6);
/* 1372 */       module(row, col, chr, 7);
/*      */     }
/*      */     
/*      */     private void corner1(int chr) {
/* 1376 */       module(this.nrow - 1, 0, chr, 0);
/* 1377 */       module(this.nrow - 1, 1, chr, 1);
/* 1378 */       module(this.nrow - 1, 2, chr, 2);
/* 1379 */       module(0, this.ncol - 2, chr, 3);
/* 1380 */       module(0, this.ncol - 1, chr, 4);
/* 1381 */       module(1, this.ncol - 1, chr, 5);
/* 1382 */       module(2, this.ncol - 1, chr, 6);
/* 1383 */       module(3, this.ncol - 1, chr, 7);
/*      */     }
/*      */     private void corner2(int chr) {
/* 1386 */       module(this.nrow - 3, 0, chr, 0);
/* 1387 */       module(this.nrow - 2, 0, chr, 1);
/* 1388 */       module(this.nrow - 1, 0, chr, 2);
/* 1389 */       module(0, this.ncol - 4, chr, 3);
/* 1390 */       module(0, this.ncol - 3, chr, 4);
/* 1391 */       module(0, this.ncol - 2, chr, 5);
/* 1392 */       module(0, this.ncol - 1, chr, 6);
/* 1393 */       module(1, this.ncol - 1, chr, 7);
/*      */     }
/*      */     private void corner3(int chr) {
/* 1396 */       module(this.nrow - 3, 0, chr, 0);
/* 1397 */       module(this.nrow - 2, 0, chr, 1);
/* 1398 */       module(this.nrow - 1, 0, chr, 2);
/* 1399 */       module(0, this.ncol - 2, chr, 3);
/* 1400 */       module(0, this.ncol - 1, chr, 4);
/* 1401 */       module(1, this.ncol - 1, chr, 5);
/* 1402 */       module(2, this.ncol - 1, chr, 6);
/* 1403 */       module(3, this.ncol - 1, chr, 7);
/*      */     }
/*      */     private void corner4(int chr) {
/* 1406 */       module(this.nrow - 1, 0, chr, 0);
/* 1407 */       module(this.nrow - 1, this.ncol - 1, chr, 1);
/* 1408 */       module(0, this.ncol - 3, chr, 2);
/* 1409 */       module(0, this.ncol - 2, chr, 3);
/* 1410 */       module(0, this.ncol - 1, chr, 4);
/* 1411 */       module(1, this.ncol - 3, chr, 5);
/* 1412 */       module(1, this.ncol - 2, chr, 6);
/* 1413 */       module(1, this.ncol - 1, chr, 7);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void ecc200() {
/* 1419 */       Arrays.fill(this.array, (short)0);
/*      */       
/* 1421 */       int chr = 1, row = 4, col = 0;
/*      */       
/*      */       do {
/* 1424 */         if (row == this.nrow && col == 0) corner1(chr++); 
/* 1425 */         if (row == this.nrow - 2 && col == 0 && this.ncol % 4 != 0) corner2(chr++); 
/* 1426 */         if (row == this.nrow - 2 && col == 0 && this.ncol % 8 == 4) corner3(chr++); 
/* 1427 */         if (row == this.nrow + 4 && col == 2 && this.ncol % 8 == 0) corner4(chr++);
/*      */         
/*      */         do {
/* 1430 */           if (row < this.nrow && col >= 0 && this.array[row * this.ncol + col] == 0)
/* 1431 */             utah(row, col, chr++); 
/* 1432 */           row -= 2; col += 2;
/* 1433 */         } while (row >= 0 && col < this.ncol);
/* 1434 */         row++; col += 3;
/*      */ 
/*      */         
/*      */         do {
/* 1438 */           if (row >= 0 && col < this.ncol && this.array[row * this.ncol + col] == 0)
/* 1439 */             utah(row, col, chr++); 
/* 1440 */           row += 2; col -= 2;
/* 1441 */         } while (row < this.nrow && col >= 0);
/* 1442 */         row += 3; col++;
/*      */       }
/* 1444 */       while (row < this.nrow || col < this.ncol);
/*      */       
/* 1446 */       if (this.array[this.nrow * this.ncol - 1] == 0) {
/* 1447 */         this.array[this.nrow * this.ncol - this.ncol - 2] = 1; this.array[this.nrow * this.ncol - 1] = 1;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static class ReedSolomon
/*      */   {
/* 1454 */     private static final int[] log = new int[] { 0, 255, 1, 240, 2, 225, 241, 53, 3, 38, 226, 133, 242, 43, 54, 210, 4, 195, 39, 114, 227, 106, 134, 28, 243, 140, 44, 23, 55, 118, 211, 234, 5, 219, 196, 96, 40, 222, 115, 103, 228, 78, 107, 125, 135, 8, 29, 162, 244, 186, 141, 180, 45, 99, 24, 49, 56, 13, 119, 153, 212, 199, 235, 91, 6, 76, 220, 217, 197, 11, 97, 184, 41, 36, 223, 253, 116, 138, 104, 193, 229, 86, 79, 171, 108, 165, 126, 145, 136, 34, 9, 74, 30, 32, 163, 84, 245, 173, 187, 204, 142, 81, 181, 190, 46, 88, 100, 159, 25, 231, 50, 207, 57, 147, 14, 67, 120, 128, 154, 248, 213, 167, 200, 63, 236, 110, 92, 176, 7, 161, 77, 124, 221, 102, 218, 95, 198, 90, 12, 152, 98, 48, 185, 179, 42, 209, 37, 132, 224, 52, 254, 239, 117, 233, 139, 22, 105, 27, 194, 113, 230, 206, 87, 158, 80, 189, 172, 203, 109, 175, 166, 62, 127, 247, 146, 66, 137, 192, 35, 252, 10, 183, 75, 216, 31, 83, 33, 73, 164, 144, 85, 170, 246, 65, 174, 61, 188, 202, 205, 157, 143, 169, 82, 72, 182, 215, 191, 251, 47, 178, 89, 151, 101, 94, 160, 123, 26, 112, 232, 21, 51, 238, 208, 131, 58, 69, 148, 18, 15, 16, 68, 17, 121, 149, 129, 19, 155, 59, 249, 70, 214, 250, 168, 71, 201, 156, 64, 60, 237, 130, 111, 20, 93, 122, 177, 150 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1473 */     private static final int[] alog = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 45, 90, 180, 69, 138, 57, 114, 228, 229, 231, 227, 235, 251, 219, 155, 27, 54, 108, 216, 157, 23, 46, 92, 184, 93, 186, 89, 178, 73, 146, 9, 18, 36, 72, 144, 13, 26, 52, 104, 208, 141, 55, 110, 220, 149, 7, 14, 28, 56, 112, 224, 237, 247, 195, 171, 123, 246, 193, 175, 115, 230, 225, 239, 243, 203, 187, 91, 182, 65, 130, 41, 82, 164, 101, 202, 185, 95, 190, 81, 162, 105, 210, 137, 63, 126, 252, 213, 135, 35, 70, 140, 53, 106, 212, 133, 39, 78, 156, 21, 42, 84, 168, 125, 250, 217, 159, 19, 38, 76, 152, 29, 58, 116, 232, 253, 215, 131, 43, 86, 172, 117, 234, 249, 223, 147, 11, 22, 44, 88, 176, 77, 154, 25, 50, 100, 200, 189, 87, 174, 113, 226, 233, 255, 211, 139, 59, 118, 236, 245, 199, 163, 107, 214, 129, 47, 94, 188, 85, 170, 121, 242, 201, 191, 83, 166, 97, 194, 169, 127, 254, 209, 143, 51, 102, 204, 181, 71, 142, 49, 98, 196, 165, 103, 206, 177, 79, 158, 17, 34, 68, 136, 61, 122, 244, 197, 167, 99, 198, 161, 111, 222, 145, 15, 30, 60, 120, 240, 205, 183, 67, 134, 33, 66, 132, 37, 74, 148, 5, 10, 20, 40, 80, 160, 109, 218, 153, 31, 62, 124, 248, 221, 151, 3, 6, 12, 24, 48, 96, 192, 173, 119, 238, 241, 207, 179, 75, 150, 1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1492 */     private static final int[] poly5 = new int[] { 228, 48, 15, 111, 62 };
/*      */ 
/*      */ 
/*      */     
/* 1496 */     private static final int[] poly7 = new int[] { 23, 68, 144, 134, 240, 92, 254 };
/*      */ 
/*      */ 
/*      */     
/* 1500 */     private static final int[] poly10 = new int[] { 28, 24, 185, 166, 223, 248, 116, 255, 110, 61 };
/*      */ 
/*      */ 
/*      */     
/* 1504 */     private static final int[] poly11 = new int[] { 175, 138, 205, 12, 194, 168, 39, 245, 60, 97, 120 };
/*      */ 
/*      */ 
/*      */     
/* 1508 */     private static final int[] poly12 = new int[] { 41, 153, 158, 91, 61, 42, 142, 213, 97, 178, 100, 242 };
/*      */ 
/*      */ 
/*      */     
/* 1512 */     private static final int[] poly14 = new int[] { 156, 97, 192, 252, 95, 9, 157, 119, 138, 45, 18, 186, 83, 185 };
/*      */ 
/*      */ 
/*      */     
/* 1516 */     private static final int[] poly18 = new int[] { 83, 195, 100, 39, 188, 75, 66, 61, 241, 213, 109, 129, 94, 254, 225, 48, 90, 188 };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1521 */     private static final int[] poly20 = new int[] { 15, 195, 244, 9, 233, 71, 168, 2, 188, 160, 153, 145, 253, 79, 108, 82, 27, 174, 186, 172 };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1526 */     private static final int[] poly24 = new int[] { 52, 190, 88, 205, 109, 39, 176, 21, 155, 197, 251, 223, 155, 21, 5, 172, 254, 124, 12, 181, 184, 96, 50, 193 };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1531 */     private static final int[] poly28 = new int[] { 211, 231, 43, 97, 71, 96, 103, 174, 37, 151, 170, 53, 75, 34, 249, 121, 17, 138, 110, 213, 141, 136, 120, 151, 233, 168, 93, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1536 */     private static final int[] poly36 = new int[] { 245, 127, 242, 218, 130, 250, 162, 181, 102, 120, 84, 179, 220, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, 137, 95, 119, 115, 44, 175, 184, 59, 25, 225, 98, 81, 112 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1542 */     private static final int[] poly42 = new int[] { 77, 193, 137, 31, 19, 38, 22, 153, 247, 105, 122, 2, 245, 133, 242, 8, 175, 95, 100, 9, 167, 105, 214, 111, 57, 121, 21, 1, 253, 57, 54, 101, 248, 202, 69, 50, 150, 177, 226, 5, 9, 5 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1548 */     private static final int[] poly48 = new int[] { 245, 132, 172, 223, 96, 32, 117, 22, 238, 133, 238, 231, 205, 188, 237, 87, 191, 106, 16, 147, 118, 23, 37, 90, 170, 205, 131, 88, 120, 100, 66, 138, 186, 240, 82, 44, 176, 87, 187, 147, 160, 175, 69, 213, 92, 253, 225, 19 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1554 */     private static final int[] poly56 = new int[] { 175, 9, 223, 238, 12, 17, 220, 208, 100, 29, 175, 170, 230, 192, 215, 235, 150, 159, 36, 223, 38, 200, 132, 54, 228, 146, 218, 234, 117, 203, 29, 232, 144, 238, 22, 150, 201, 117, 62, 207, 164, 13, 137, 245, 127, 67, 247, 28, 155, 43, 203, 107, 233, 53, 143, 46 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1561 */     private static final int[] poly62 = new int[] { 242, 93, 169, 50, 144, 210, 39, 118, 202, 188, 201, 189, 143, 108, 196, 37, 185, 112, 134, 230, 245, 63, 197, 190, 250, 106, 185, 221, 175, 64, 114, 71, 161, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, 130, 188, 17, 163, 31, 176, 170, 4, 107, 232, 7, 94, 166, 224, 124, 86, 47, 11, 204 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1568 */     private static final int[] poly68 = new int[] { 220, 228, 173, 89, 251, 149, 159, 56, 89, 33, 147, 244, 154, 36, 73, 127, 213, 136, 248, 180, 234, 197, 158, 177, 68, 122, 93, 213, 15, 160, 227, 236, 66, 139, 153, 185, 202, 167, 179, 25, 220, 232, 96, 210, 231, 136, 223, 239, 181, 241, 59, 52, 172, 25, 49, 232, 211, 189, 64, 54, 108, 153, 132, 63, 96, 103, 82, 186 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static int[] getPoly(int nc) {
/* 1577 */       switch (nc) {
/*      */         case 5:
/* 1579 */           return poly5;
/*      */         case 7:
/* 1581 */           return poly7;
/*      */         case 10:
/* 1583 */           return poly10;
/*      */         case 11:
/* 1585 */           return poly11;
/*      */         case 12:
/* 1587 */           return poly12;
/*      */         case 14:
/* 1589 */           return poly14;
/*      */         case 18:
/* 1591 */           return poly18;
/*      */         case 20:
/* 1593 */           return poly20;
/*      */         case 24:
/* 1595 */           return poly24;
/*      */         case 28:
/* 1597 */           return poly28;
/*      */         case 36:
/* 1599 */           return poly36;
/*      */         case 42:
/* 1601 */           return poly42;
/*      */         case 48:
/* 1603 */           return poly48;
/*      */         case 56:
/* 1605 */           return poly56;
/*      */         case 62:
/* 1607 */           return poly62;
/*      */         case 68:
/* 1609 */           return poly68;
/*      */       } 
/* 1611 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     private static void reedSolomonBlock(byte[] wd, int nd, byte[] ncout, int nc, int[] c) {
/*      */       int i;
/* 1617 */       for (i = 0; i <= nc; ) { ncout[i] = 0; i++; }
/* 1618 */        for (i = 0; i < nd; i++) {
/* 1619 */         int k = (ncout[0] ^ wd[i]) & 0xFF;
/* 1620 */         for (int j = 0; j < nc; j++) {
/* 1621 */           ncout[j] = (byte)(ncout[j + 1] ^ ((k == 0) ? 0 : (byte)alog[(log[k] + log[c[nc - j - 1]]) % 255]));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     static void generateECC(byte[] wd, int nd, int datablock, int nc) {
/* 1627 */       int blocks = (nd + 2) / datablock;
/*      */       
/* 1629 */       byte[] buf = new byte[256];
/* 1630 */       byte[] ecc = new byte[256];
/* 1631 */       int[] c = getPoly(nc);
/* 1632 */       for (int b = 0; b < blocks; b++) {
/*      */         
/* 1634 */         int p = 0; int n;
/* 1635 */         for (n = b; n < nd; n += blocks)
/* 1636 */           buf[p++] = wd[n]; 
/* 1637 */         reedSolomonBlock(buf, p, ecc, nc, c);
/* 1638 */         p = 0;
/* 1639 */         for (n = b; n < nc * blocks; n += blocks) {
/* 1640 */           wd[nd + n] = ecc[p++];
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void placeBarcode(PdfContentByte cb, BaseColor foreground, float moduleHeight, float moduleWidth) {
/* 1647 */     int w = this.width + 2 * this.ws;
/* 1648 */     int h = this.height + 2 * this.ws;
/* 1649 */     int stride = (w + 7) / 8;
/* 1650 */     int ptr = 0;
/* 1651 */     cb.setColorFill(foreground);
/* 1652 */     for (int k = 0; k < h; k++) {
/* 1653 */       int p = k * stride;
/* 1654 */       for (int j = 0; j < w; j++) {
/* 1655 */         int b = this.image[p + j / 8] & 0xFF;
/* 1656 */         b <<= j % 8;
/* 1657 */         if ((b & 0x80) != 0) {
/* 1658 */           cb.rectangle(j * moduleWidth, (h - k - 1) * moduleHeight, moduleWidth, moduleHeight);
/*      */         }
/*      */       } 
/*      */     } 
/* 1662 */     cb.fill();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image createAwtImage(Color foreground, Color background) {
/* 1675 */     if (this.image == null)
/* 1676 */       return null; 
/* 1677 */     int f = foreground.getRGB();
/* 1678 */     int g = background.getRGB();
/* 1679 */     Canvas canvas = new Canvas();
/*      */     
/* 1681 */     int w = this.width + 2 * this.ws;
/* 1682 */     int h = this.height + 2 * this.ws;
/* 1683 */     int[] pix = new int[w * h];
/* 1684 */     int stride = (w + 7) / 8;
/* 1685 */     int ptr = 0;
/* 1686 */     for (int k = 0; k < h; k++) {
/* 1687 */       int p = k * stride;
/* 1688 */       for (int j = 0; j < w; j++) {
/* 1689 */         int b = this.image[p + j / 8] & 0xFF;
/* 1690 */         b <<= j % 8;
/* 1691 */         pix[ptr++] = ((b & 0x80) == 0) ? g : f;
/*      */       } 
/*      */     } 
/* 1694 */     Image img = canvas.createImage(new MemoryImageSource(w, h, pix, 0, w));
/* 1695 */     return img;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeDatamatrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */