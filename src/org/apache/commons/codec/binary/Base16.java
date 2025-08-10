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
/*     */ public class Base16
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 4;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 2;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 1;
/*  60 */   private static final byte[] UPPER_CASE_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final byte[] UPPER_CASE_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final byte[] LOWER_CASE_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static final byte[] LOWER_CASE_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_4BITS = 15;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] decodeTable;
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
/*     */   public Base16() {
/* 120 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base16(boolean lowerCase) {
/* 129 */     this(lowerCase, DECODING_POLICY_DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base16(boolean lowerCase, CodecPolicy decodingPolicy) {
/* 139 */     super(1, 2, 0, 0, (byte)61, decodingPolicy);
/*     */     
/* 141 */     if (lowerCase) {
/* 142 */       this.encodeTable = LOWER_CASE_ENCODE_TABLE;
/* 143 */       this.decodeTable = LOWER_CASE_DECODE_TABLE;
/*     */     } else {
/* 145 */       this.encodeTable = UPPER_CASE_ENCODE_TABLE;
/* 146 */       this.decodeTable = UPPER_CASE_DECODE_TABLE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void decode(byte[] data, int offset, int length, BaseNCodec.Context context) {
/* 152 */     if (context.eof || length < 0) {
/* 153 */       context.eof = true;
/* 154 */       if (context.ibitWorkArea != 0) {
/* 155 */         validateTrailingCharacter();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 160 */     int dataLen = Math.min(data.length - offset, length);
/* 161 */     int availableChars = ((context.ibitWorkArea != 0) ? 1 : 0) + dataLen;
/*     */ 
/*     */     
/* 164 */     if (availableChars == 1 && availableChars == dataLen) {
/* 165 */       context.ibitWorkArea = decodeOctet(data[offset]) + 1;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 170 */     int charsToProcess = (availableChars % 2 == 0) ? availableChars : (availableChars - 1);
/*     */     
/* 172 */     byte[] buffer = ensureBufferSize(charsToProcess / 2, context);
/*     */ 
/*     */     
/* 175 */     int i = 0;
/* 176 */     if (dataLen < availableChars) {
/*     */       
/* 178 */       int result = context.ibitWorkArea - 1 << 4;
/* 179 */       result |= decodeOctet(data[offset++]);
/* 180 */       i = 2;
/*     */       
/* 182 */       buffer[context.pos++] = (byte)result;
/*     */ 
/*     */       
/* 185 */       context.ibitWorkArea = 0;
/*     */     } 
/*     */     
/* 188 */     while (i < charsToProcess) {
/* 189 */       int result = decodeOctet(data[offset++]) << 4;
/* 190 */       result |= decodeOctet(data[offset++]);
/* 191 */       i += 2;
/* 192 */       buffer[context.pos++] = (byte)result;
/*     */     } 
/*     */ 
/*     */     
/* 196 */     if (i < dataLen) {
/* 197 */       context.ibitWorkArea = decodeOctet(data[i]) + 1;
/*     */     }
/*     */   }
/*     */   
/*     */   private int decodeOctet(byte octet) {
/* 202 */     int decoded = -1;
/* 203 */     if ((octet & 0xFF) < this.decodeTable.length) {
/* 204 */       decoded = this.decodeTable[octet];
/*     */     }
/*     */     
/* 207 */     if (decoded == -1) {
/* 208 */       throw new IllegalArgumentException("Invalid octet in encoded value: " + octet);
/*     */     }
/*     */     
/* 211 */     return decoded;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(byte[] data, int offset, int length, BaseNCodec.Context context) {
/* 216 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */     
/* 220 */     if (length < 0) {
/* 221 */       context.eof = true;
/*     */       
/*     */       return;
/*     */     } 
/* 225 */     int size = length * 2;
/* 226 */     if (size < 0) {
/* 227 */       throw new IllegalArgumentException("Input length exceeds maximum size for encoded data: " + length);
/*     */     }
/*     */     
/* 230 */     byte[] buffer = ensureBufferSize(size, context);
/*     */     
/* 232 */     int end = offset + length;
/* 233 */     for (int i = offset; i < end; i++) {
/* 234 */       int value = data[i];
/* 235 */       int high = value >> 4 & 0xF;
/* 236 */       int low = value & 0xF;
/* 237 */       buffer[context.pos++] = this.encodeTable[high];
/* 238 */       buffer[context.pos++] = this.encodeTable[low];
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
/* 251 */     return ((octet & 0xFF) < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateTrailingCharacter() {
/* 261 */     if (isStrictDecoding())
/* 262 */       throw new IllegalArgumentException("Strict decoding: Last encoded character is a valid base 16 alphabetcharacter but not a possible encoding. Decoding requires at least two characters to create one byte."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/Base16.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */