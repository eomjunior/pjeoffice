/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.CodecPolicy;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseNCodec
/*     */   implements BinaryEncoder, BinaryDecoder
/*     */ {
/*     */   static final int EOF = -1;
/*     */   public static final int MIME_CHUNK_SIZE = 76;
/*     */   public static final int PEM_CHUNK_SIZE = 64;
/*     */   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private static final int MAX_BUFFER_SIZE = 2147483639;
/*     */   protected static final int MASK_8BITS = 255;
/*     */   protected static final byte PAD_DEFAULT = 61;
/*     */   
/*     */   static class Context
/*     */   {
/*     */     int ibitWorkArea;
/*     */     long lbitWorkArea;
/*     */     byte[] buffer;
/*     */     int pos;
/*     */     int readPos;
/*     */     boolean eof;
/*     */     int currentLinePos;
/*     */     int modulus;
/*     */     
/*     */     public String toString() {
/* 118 */       return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", new Object[] {
/* 119 */             getClass().getSimpleName(), Arrays.toString(this.buffer), 
/* 120 */             Integer.valueOf(this.currentLinePos), Boolean.valueOf(this.eof), Integer.valueOf(this.ibitWorkArea), Long.valueOf(this.lbitWorkArea), Integer.valueOf(this.modulus), Integer.valueOf(this.pos), Integer.valueOf(this.readPos)
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   protected static final CodecPolicy DECODING_POLICY_DEFAULT = CodecPolicy.LENIENT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int compareUnsigned(int x, int y) {
/* 210 */     return Integer.compare(x + Integer.MIN_VALUE, y + Integer.MIN_VALUE);
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
/*     */   private static int createPositiveCapacity(int minCapacity) {
/* 223 */     if (minCapacity < 0)
/*     */     {
/* 225 */       throw new OutOfMemoryError("Unable to allocate array size: " + (minCapacity & 0xFFFFFFFFL));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     return (minCapacity > 2147483639) ? minCapacity : 2147483639;
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
/*     */   public static byte[] getChunkSeparator() {
/* 248 */     return (byte[])CHUNK_SEPARATOR.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isWhiteSpace(byte byteToCheck) {
/* 259 */     switch (byteToCheck) {
/*     */       case 9:
/*     */       case 10:
/*     */       case 13:
/*     */       case 32:
/* 264 */         return true;
/*     */     } 
/* 266 */     return false;
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
/*     */   private static byte[] resizeBuffer(Context context, int minCapacity) {
/* 279 */     int oldCapacity = context.buffer.length;
/* 280 */     int newCapacity = oldCapacity * 2;
/* 281 */     if (compareUnsigned(newCapacity, minCapacity) < 0) {
/* 282 */       newCapacity = minCapacity;
/*     */     }
/* 284 */     if (compareUnsigned(newCapacity, 2147483639) > 0) {
/* 285 */       newCapacity = createPositiveCapacity(minCapacity);
/*     */     }
/*     */     
/* 288 */     byte[] b = new byte[newCapacity];
/* 289 */     System.arraycopy(context.buffer, 0, b, 0, context.buffer.length);
/* 290 */     context.buffer = b;
/* 291 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 297 */   protected final byte PAD = 61;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte pad;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int unencodedBlockSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodedBlockSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int lineLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int chunkSeparatorLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CodecPolicy decodingPolicy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength) {
/* 350 */     this(unencodedBlockSize, encodedBlockSize, lineLength, chunkSeparatorLength, (byte)61);
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
/*     */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength, byte pad) {
/* 364 */     this(unencodedBlockSize, encodedBlockSize, lineLength, chunkSeparatorLength, pad, DECODING_POLICY_DEFAULT);
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
/*     */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength, byte pad, CodecPolicy decodingPolicy) {
/* 380 */     this.unencodedBlockSize = unencodedBlockSize;
/* 381 */     this.encodedBlockSize = encodedBlockSize;
/* 382 */     boolean useChunking = (lineLength > 0 && chunkSeparatorLength > 0);
/* 383 */     this.lineLength = useChunking ? (lineLength / encodedBlockSize * encodedBlockSize) : 0;
/* 384 */     this.chunkSeparatorLength = chunkSeparatorLength;
/* 385 */     this.pad = pad;
/* 386 */     this.decodingPolicy = Objects.<CodecPolicy>requireNonNull(decodingPolicy, "codecPolicy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int available(Context context) {
/* 396 */     return (context.buffer != null) ? (context.pos - context.readPos) : 0;
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
/*     */   protected boolean containsAlphabetOrPad(byte[] arrayOctet) {
/* 409 */     if (arrayOctet == null) {
/* 410 */       return false;
/*     */     }
/* 412 */     for (byte element : arrayOctet) {
/* 413 */       if (this.pad == element || isInAlphabet(element)) {
/* 414 */         return true;
/*     */       }
/*     */     } 
/* 417 */     return false;
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
/*     */   public byte[] decode(byte[] pArray) {
/* 429 */     if (pArray == null || pArray.length == 0) {
/* 430 */       return pArray;
/*     */     }
/* 432 */     Context context = new Context();
/* 433 */     decode(pArray, 0, pArray.length, context);
/* 434 */     decode(pArray, 0, -1, context);
/* 435 */     byte[] result = new byte[context.pos];
/* 436 */     readResults(result, 0, result.length, context);
/* 437 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void decode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Context paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object decode(Object obj) throws DecoderException {
/* 456 */     if (obj instanceof byte[])
/* 457 */       return decode((byte[])obj); 
/* 458 */     if (obj instanceof String) {
/* 459 */       return decode((String)obj);
/*     */     }
/* 461 */     throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
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
/*     */   public byte[] decode(String pArray) {
/* 473 */     return decode(StringUtils.getBytesUtf8(pArray));
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
/*     */   public byte[] encode(byte[] pArray) {
/* 485 */     if (pArray == null || pArray.length == 0) {
/* 486 */       return pArray;
/*     */     }
/* 488 */     return encode(pArray, 0, pArray.length);
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
/*     */   public byte[] encode(byte[] pArray, int offset, int length) {
/* 505 */     if (pArray == null || pArray.length == 0) {
/* 506 */       return pArray;
/*     */     }
/* 508 */     Context context = new Context();
/* 509 */     encode(pArray, offset, length, context);
/* 510 */     encode(pArray, offset, -1, context);
/* 511 */     byte[] buf = new byte[context.pos - context.readPos];
/* 512 */     readResults(buf, 0, buf.length, context);
/* 513 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void encode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Context paramContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object encode(Object obj) throws EncoderException {
/* 531 */     if (!(obj instanceof byte[])) {
/* 532 */       throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
/*     */     }
/* 534 */     return encode((byte[])obj);
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
/*     */   public String encodeAsString(byte[] pArray) {
/* 547 */     return StringUtils.newStringUtf8(encode(pArray));
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
/*     */   public String encodeToString(byte[] pArray) {
/* 559 */     return StringUtils.newStringUtf8(encode(pArray));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] ensureBufferSize(int size, Context context) {
/* 570 */     if (context.buffer == null) {
/* 571 */       context.buffer = new byte[Math.max(size, getDefaultBufferSize())];
/* 572 */       context.pos = 0;
/* 573 */       context.readPos = 0;
/*     */ 
/*     */     
/*     */     }
/* 577 */     else if (context.pos + size - context.buffer.length > 0) {
/* 578 */       return resizeBuffer(context, context.pos + size);
/*     */     } 
/* 580 */     return context.buffer;
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
/*     */   public CodecPolicy getCodecPolicy() {
/* 596 */     return this.decodingPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDefaultBufferSize() {
/* 605 */     return 8192;
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
/*     */   public long getEncodedLength(byte[] pArray) {
/* 619 */     long len = ((pArray.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize) * this.encodedBlockSize;
/* 620 */     if (this.lineLength > 0)
/*     */     {
/* 622 */       len += (len + this.lineLength - 1L) / this.lineLength * this.chunkSeparatorLength;
/*     */     }
/* 624 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasData(Context context) {
/* 634 */     return (context.buffer != null);
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
/*     */   protected abstract boolean isInAlphabet(byte paramByte);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInAlphabet(byte[] arrayOctet, boolean allowWSPad) {
/* 658 */     for (byte octet : arrayOctet) {
/* 659 */       if (!isInAlphabet(octet) && (!allowWSPad || (octet != this.pad && 
/* 660 */         !isWhiteSpace(octet)))) {
/* 661 */         return false;
/*     */       }
/*     */     } 
/* 664 */     return true;
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
/*     */   public boolean isInAlphabet(String basen) {
/* 677 */     return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
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
/*     */   public boolean isStrictDecoding() {
/* 693 */     return (this.decodingPolicy == CodecPolicy.STRICT);
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
/*     */   int readResults(byte[] b, int bPos, int bAvail, Context context) {
/* 713 */     if (context.buffer != null) {
/* 714 */       int len = Math.min(available(context), bAvail);
/* 715 */       System.arraycopy(context.buffer, context.readPos, b, bPos, len);
/* 716 */       context.readPos += len;
/* 717 */       if (context.readPos >= context.pos) {
/* 718 */         context.buffer = null;
/*     */       }
/* 720 */       return len;
/*     */     } 
/* 722 */     return context.eof ? -1 : 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/BaseNCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */