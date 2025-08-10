/*     */ package org.apache.commons.codec.binary;
/*     */ 
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
/*     */ public class Base32
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 5;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 8;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
/*  61 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private static final byte[] ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final byte[] HEX_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final byte[] HEX_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_5BITS = 31;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MASK_4BITS = 15L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MASK_3BITS = 7L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MASK_2BITS = 3L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MASK_1BITS = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int decodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] decodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] encodeTable;
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
/*     */   public Base32() {
/* 167 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(boolean useHex) {
/* 178 */     this(0, (byte[])null, useHex, (byte)61);
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
/*     */   public Base32(boolean useHex, byte padding) {
/* 190 */     this(0, (byte[])null, useHex, padding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(byte pad) {
/* 201 */     this(false, pad);
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
/*     */   public Base32(int lineLength) {
/* 216 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator) {
/* 238 */     this(lineLength, lineSeparator, false, (byte)61);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex) {
/* 263 */     this(lineLength, lineSeparator, useHex, (byte)61);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex, byte padding) {
/* 289 */     this(lineLength, lineSeparator, useHex, padding, DECODING_POLICY_DEFAULT);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex, byte padding, CodecPolicy decodingPolicy) {
/* 317 */     super(5, 8, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length, padding, decodingPolicy);
/*     */     
/* 319 */     if (useHex) {
/* 320 */       this.encodeTable = HEX_ENCODE_TABLE;
/* 321 */       this.decodeTable = HEX_DECODE_TABLE;
/*     */     } else {
/* 323 */       this.encodeTable = ENCODE_TABLE;
/* 324 */       this.decodeTable = DECODE_TABLE;
/*     */     } 
/* 326 */     if (lineLength > 0) {
/* 327 */       if (lineSeparator == null) {
/* 328 */         throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
/*     */       }
/*     */       
/* 331 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 332 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 333 */         throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
/*     */       } 
/* 335 */       this.encodeSize = 8 + lineSeparator.length;
/* 336 */       this.lineSeparator = new byte[lineSeparator.length];
/* 337 */       System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */     } else {
/* 339 */       this.encodeSize = 8;
/* 340 */       this.lineSeparator = null;
/*     */     } 
/* 342 */     this.decodeSize = this.encodeSize - 1;
/*     */     
/* 344 */     if (isInAlphabet(padding) || isWhiteSpace(padding)) {
/* 345 */       throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
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
/*     */   
/*     */   void decode(byte[] input, int inPos, int inAvail, BaseNCodec.Context context) {
/* 374 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 377 */     if (inAvail < 0) {
/* 378 */       context.eof = true;
/*     */     }
/* 380 */     for (int i = 0; i < inAvail; i++) {
/* 381 */       byte b = input[inPos++];
/* 382 */       if (b == this.pad) {
/*     */         
/* 384 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 387 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 388 */       if (b >= 0 && b < this.decodeTable.length) {
/* 389 */         int result = this.decodeTable[b];
/* 390 */         if (result >= 0) {
/* 391 */           context.modulus = (context.modulus + 1) % 8;
/*     */           
/* 393 */           context.lbitWorkArea = (context.lbitWorkArea << 5L) + result;
/* 394 */           if (context.modulus == 0) {
/* 395 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 32L & 0xFFL);
/* 396 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 397 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 398 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 399 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 408 */     if (context.eof && context.modulus > 0) {
/* 409 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 417 */       switch (context.modulus) {
/*     */         
/*     */         case 1:
/* 420 */           validateTrailingCharacters();
/*     */         case 2:
/* 422 */           validateCharacter(3L, context);
/* 423 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 2L & 0xFFL);
/*     */           return;
/*     */         case 3:
/* 426 */           validateTrailingCharacters();
/*     */           
/* 428 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 7L & 0xFFL);
/*     */           return;
/*     */         case 4:
/* 431 */           validateCharacter(15L, context);
/* 432 */           context.lbitWorkArea >>= 4L;
/* 433 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 434 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 5:
/* 437 */           validateCharacter(1L, context);
/* 438 */           context.lbitWorkArea >>= 1L;
/* 439 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 440 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 441 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 6:
/* 444 */           validateTrailingCharacters();
/*     */           
/* 446 */           context.lbitWorkArea >>= 6L;
/* 447 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 448 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 449 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 7:
/* 452 */           validateCharacter(7L, context);
/* 453 */           context.lbitWorkArea >>= 3L;
/* 454 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 455 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 456 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 457 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */       } 
/*     */       
/* 461 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */   void encode(byte[] input, int inPos, int inAvail, BaseNCodec.Context context) {
/* 485 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 490 */     if (inAvail < 0) {
/* 491 */       context.eof = true;
/* 492 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 495 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 496 */       int savedPos = context.pos;
/* 497 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         case 1:
/* 501 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 3L) & 0x1F];
/* 502 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 2L) & 0x1F];
/* 503 */           buffer[context.pos++] = this.pad;
/* 504 */           buffer[context.pos++] = this.pad;
/* 505 */           buffer[context.pos++] = this.pad;
/* 506 */           buffer[context.pos++] = this.pad;
/* 507 */           buffer[context.pos++] = this.pad;
/* 508 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 2:
/* 511 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 11L) & 0x1F];
/* 512 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 6L) & 0x1F];
/* 513 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 1L) & 0x1F];
/* 514 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 4L) & 0x1F];
/* 515 */           buffer[context.pos++] = this.pad;
/* 516 */           buffer[context.pos++] = this.pad;
/* 517 */           buffer[context.pos++] = this.pad;
/* 518 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 3:
/* 521 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 19L) & 0x1F];
/* 522 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 14L) & 0x1F];
/* 523 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 9L) & 0x1F];
/* 524 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 4L) & 0x1F];
/* 525 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 1L) & 0x1F];
/* 526 */           buffer[context.pos++] = this.pad;
/* 527 */           buffer[context.pos++] = this.pad;
/* 528 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 4:
/* 531 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 27L) & 0x1F];
/* 532 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 22L) & 0x1F];
/* 533 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 17L) & 0x1F];
/* 534 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 12L) & 0x1F];
/* 535 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 7L) & 0x1F];
/* 536 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 2L) & 0x1F];
/* 537 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 3L) & 0x1F];
/* 538 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         default:
/* 541 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 543 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 545 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 546 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 547 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 550 */       for (int i = 0; i < inAvail; i++) {
/* 551 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 552 */         context.modulus = (context.modulus + 1) % 5;
/* 553 */         int b = input[inPos++];
/* 554 */         if (b < 0) {
/* 555 */           b += 256;
/*     */         }
/* 557 */         context.lbitWorkArea = (context.lbitWorkArea << 8L) + b;
/* 558 */         if (0 == context.modulus) {
/* 559 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 35L) & 0x1F];
/* 560 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 30L) & 0x1F];
/* 561 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 25L) & 0x1F];
/* 562 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 20L) & 0x1F];
/* 563 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 15L) & 0x1F];
/* 564 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 10L) & 0x1F];
/* 565 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 5L) & 0x1F];
/* 566 */           buffer[context.pos++] = this.encodeTable[(int)context.lbitWorkArea & 0x1F];
/* 567 */           context.currentLinePos += 8;
/* 568 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 569 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 570 */             context.pos += this.lineSeparator.length;
/* 571 */             context.currentLinePos = 0;
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
/*     */   public boolean isInAlphabet(byte octet) {
/* 587 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
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
/*     */   private void validateCharacter(long emptyBitsMask, BaseNCodec.Context context) {
/* 604 */     if (isStrictDecoding() && (context.lbitWorkArea & emptyBitsMask) != 0L) {
/* 605 */       throw new IllegalArgumentException("Strict decoding: Last encoded character (before the paddings if any) is a valid base 32 alphabet but not a possible encoding. Expected the discarded bits from the character to be zero.");
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
/*     */   private void validateTrailingCharacters() {
/* 618 */     if (isStrictDecoding())
/* 619 */       throw new IllegalArgumentException("Strict decoding: Last encoded character(s) (before the paddings if any) are valid base 32 alphabet but not a possible encoding. Decoding requires either 2, 4, 5, or 7 trailing 5-bit characters to create bytes."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/Base32.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */