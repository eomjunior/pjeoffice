/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.codec.CodecPolicy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 6;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 4;
/*  74 */   private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_6BITS = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_4BITS = 15;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_2BITS = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] encodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(byte[] base64Data) {
/* 143 */     return (new Base64()).decode(base64Data);
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
/*     */   public static byte[] decodeBase64(String base64String) {
/* 158 */     return (new Base64()).decode(base64String);
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
/*     */   public static BigInteger decodeInteger(byte[] pArray) {
/* 171 */     return new BigInteger(1, decodeBase64(pArray));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64(byte[] binaryData) {
/* 182 */     return encodeBase64(binaryData, false);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
/* 197 */     return encodeBase64(binaryData, isChunked, false);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
/* 216 */     return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
/* 238 */     if (binaryData == null || binaryData.length == 0) {
/* 239 */       return binaryData;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 244 */     Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
/* 245 */     long len = b64.getEncodedLength(binaryData);
/* 246 */     if (len > maxResultSize) {
/* 247 */       throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     return b64.encode(binaryData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64Chunked(byte[] binaryData) {
/* 264 */     return encodeBase64(binaryData, true);
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
/*     */   public static String encodeBase64String(byte[] binaryData) {
/* 279 */     return StringUtils.newStringUsAscii(encodeBase64(binaryData, false));
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
/*     */   public static byte[] encodeBase64URLSafe(byte[] binaryData) {
/* 292 */     return encodeBase64(binaryData, false, true);
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
/*     */   public static String encodeBase64URLSafeString(byte[] binaryData) {
/* 305 */     return StringUtils.newStringUsAscii(encodeBase64(binaryData, false, true));
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
/*     */   public static byte[] encodeInteger(BigInteger bigInteger) {
/* 319 */     Objects.requireNonNull(bigInteger, "bigInteger");
/* 320 */     return encodeBase64(toIntegerBytes(bigInteger), false);
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
/*     */   @Deprecated
/*     */   public static boolean isArrayByteBase64(byte[] arrayOctet) {
/* 335 */     return isBase64(arrayOctet);
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
/*     */   public static boolean isBase64(byte octet) {
/* 347 */     return (octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1));
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
/*     */   public static boolean isBase64(byte[] arrayOctet) {
/* 361 */     for (int i = 0; i < arrayOctet.length; i++) {
/* 362 */       if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
/* 363 */         return false;
/*     */       }
/*     */     } 
/* 366 */     return true;
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
/*     */   public static boolean isBase64(String base64) {
/* 380 */     return isBase64(StringUtils.getBytesUtf8(base64));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toIntegerBytes(BigInteger bigInt) {
/* 391 */     int bitlen = bigInt.bitLength();
/*     */     
/* 393 */     bitlen = bitlen + 7 >> 3 << 3;
/* 394 */     byte[] bigBytes = bigInt.toByteArray();
/*     */     
/* 396 */     if (bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8) {
/* 397 */       return bigBytes;
/*     */     }
/*     */     
/* 400 */     int startSrc = 0;
/* 401 */     int len = bigBytes.length;
/*     */ 
/*     */     
/* 404 */     if (bigInt.bitLength() % 8 == 0) {
/* 405 */       startSrc = 1;
/* 406 */       len--;
/*     */     } 
/* 408 */     int startDst = bitlen / 8 - len;
/* 409 */     byte[] resizedBytes = new byte[bitlen / 8];
/* 410 */     System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
/* 411 */     return resizedBytes;
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
/* 422 */   private final byte[] decodeTable = DECODE_TABLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] lineSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int decodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64() {
/* 452 */     this(0);
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
/*     */   public Base64(boolean urlSafe) {
/* 471 */     this(76, CHUNK_SEPARATOR, urlSafe);
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
/*     */   public Base64(int lineLength) {
/* 494 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base64(int lineLength, byte[] lineSeparator) {
/* 521 */     this(lineLength, lineSeparator, false);
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
/*     */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
/* 552 */     this(lineLength, lineSeparator, urlSafe, DECODING_POLICY_DEFAULT);
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
/*     */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe, CodecPolicy decodingPolicy) {
/* 584 */     super(3, 4, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length, (byte)61, decodingPolicy);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 591 */     if (lineSeparator != null) {
/* 592 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 593 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 594 */         throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
/*     */       } 
/* 596 */       if (lineLength > 0) {
/* 597 */         this.encodeSize = 4 + lineSeparator.length;
/* 598 */         this.lineSeparator = new byte[lineSeparator.length];
/* 599 */         System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */       } else {
/* 601 */         this.encodeSize = 4;
/* 602 */         this.lineSeparator = null;
/*     */       } 
/*     */     } else {
/* 605 */       this.encodeSize = 4;
/* 606 */       this.lineSeparator = null;
/*     */     } 
/* 608 */     this.decodeSize = this.encodeSize - 1;
/* 609 */     this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
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
/*     */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 641 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 644 */     if (inAvail < 0) {
/* 645 */       context.eof = true;
/*     */     }
/* 647 */     for (int i = 0; i < inAvail; i++) {
/* 648 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 649 */       byte b = in[inPos++];
/* 650 */       if (b == this.pad) {
/*     */         
/* 652 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 655 */       if (b >= 0 && b < DECODE_TABLE.length) {
/* 656 */         int result = DECODE_TABLE[b];
/* 657 */         if (result >= 0) {
/* 658 */           context.modulus = (context.modulus + 1) % 4;
/* 659 */           context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
/* 660 */           if (context.modulus == 0) {
/* 661 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 16 & 0xFF);
/* 662 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 663 */             buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 672 */     if (context.eof && context.modulus != 0) {
/* 673 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */ 
/*     */       
/* 677 */       switch (context.modulus) {
/*     */         
/*     */         case 1:
/* 680 */           validateTrailingCharacter();
/*     */           return;
/*     */         case 2:
/* 683 */           validateCharacter(15, context);
/* 684 */           context.ibitWorkArea >>= 4;
/* 685 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */           return;
/*     */         case 3:
/* 688 */           validateCharacter(3, context);
/* 689 */           context.ibitWorkArea >>= 2;
/* 690 */           buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 691 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */           return;
/*     */       } 
/* 694 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 722 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 727 */     if (inAvail < 0) {
/* 728 */       context.eof = true;
/* 729 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 732 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 733 */       int savedPos = context.pos;
/* 734 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         
/*     */         case 1:
/* 739 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 2 & 0x3F];
/*     */           
/* 741 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 4 & 0x3F];
/*     */           
/* 743 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 744 */             buffer[context.pos++] = this.pad;
/* 745 */             buffer[context.pos++] = this.pad;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 2:
/* 750 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 10 & 0x3F];
/* 751 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 4 & 0x3F];
/* 752 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 2 & 0x3F];
/*     */           
/* 754 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 755 */             buffer[context.pos++] = this.pad;
/*     */           }
/*     */           break;
/*     */         default:
/* 759 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 761 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 763 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 764 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 765 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 768 */       for (int i = 0; i < inAvail; i++) {
/* 769 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 770 */         context.modulus = (context.modulus + 1) % 3;
/* 771 */         int b = in[inPos++];
/* 772 */         if (b < 0) {
/* 773 */           b += 256;
/*     */         }
/* 775 */         context.ibitWorkArea = (context.ibitWorkArea << 8) + b;
/* 776 */         if (0 == context.modulus) {
/* 777 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 18 & 0x3F];
/* 778 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 12 & 0x3F];
/* 779 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 6 & 0x3F];
/* 780 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea & 0x3F];
/* 781 */           context.currentLinePos += 4;
/* 782 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 783 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 784 */             context.pos += this.lineSeparator.length;
/* 785 */             context.currentLinePos = 0;
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */   protected boolean isInAlphabet(byte octet) {
/* 801 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUrlSafe() {
/* 811 */     return (this.encodeTable == URL_SAFE_ENCODE_TABLE);
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
/*     */   private void validateCharacter(int emptyBitsMask, BaseNCodec.Context context) {
/* 827 */     if (isStrictDecoding() && (context.ibitWorkArea & emptyBitsMask) != 0) {
/* 828 */       throw new IllegalArgumentException("Strict decoding: Last encoded character (before the paddings if any) is a valid base 64 alphabet but not a possible encoding. Expected the discarded bits from the character to be zero.");
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
/*     */   private void validateTrailingCharacter() {
/* 841 */     if (isStrictDecoding())
/* 842 */       throw new IllegalArgumentException("Strict decoding: Last encoded character (before the paddings if any) is a valid base 64 alphabet but not a possible encoding. Decoding requires at least two trailing 6-bit characters to create bytes."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/Base64.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */