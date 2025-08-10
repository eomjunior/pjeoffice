/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.ByteArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ public final class HPackDecoder
/*     */ {
/*     */   private static final String UNEXPECTED_EOS = "Unexpected end of HPACK data";
/*     */   private static final String MAX_LIMIT_EXCEEDED = "Max integer exceeded";
/*     */   private final InboundDynamicTable dynamicTable;
/*     */   private final ByteArrayBuffer contentBuf;
/*     */   private final CharsetDecoder charsetDecoder;
/*     */   private CharBuffer tmpBuf;
/*     */   private int maxTableSize;
/*     */   private int maxListSize;
/*     */   
/*     */   HPackDecoder(InboundDynamicTable dynamicTable, CharsetDecoder charsetDecoder) {
/*  65 */     this.dynamicTable = (dynamicTable != null) ? dynamicTable : new InboundDynamicTable();
/*  66 */     this.contentBuf = new ByteArrayBuffer(256);
/*  67 */     this.charsetDecoder = charsetDecoder;
/*  68 */     this.maxTableSize = (dynamicTable != null) ? dynamicTable.getMaxSize() : Integer.MAX_VALUE;
/*  69 */     this.maxListSize = Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   HPackDecoder(InboundDynamicTable dynamicTable, Charset charset) {
/*  73 */     this(dynamicTable, (charset != null && !StandardCharsets.US_ASCII.equals(charset)) ? charset.newDecoder() : null);
/*     */   }
/*     */   
/*     */   public HPackDecoder(Charset charset) {
/*  77 */     this(new InboundDynamicTable(), charset);
/*     */   }
/*     */   
/*     */   public HPackDecoder(CharsetDecoder charsetDecoder) {
/*  81 */     this(new InboundDynamicTable(), charsetDecoder);
/*     */   }
/*     */ 
/*     */   
/*     */   static int readByte(ByteBuffer src) throws HPackException {
/*  86 */     if (!src.hasRemaining()) {
/*  87 */       throw new HPackException("Unexpected end of HPACK data");
/*     */     }
/*  89 */     return src.get() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   static int peekByte(ByteBuffer src) throws HPackException {
/*  94 */     if (!src.hasRemaining()) {
/*  95 */       throw new HPackException("Unexpected end of HPACK data");
/*     */     }
/*  97 */     int pos = src.position();
/*  98 */     int b = src.get() & 0xFF;
/*  99 */     src.position(pos);
/* 100 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   static int decodeInt(ByteBuffer src, int n) throws HPackException {
/* 105 */     int nbits = 255 >>> 8 - n;
/* 106 */     int value = readByte(src) & nbits;
/* 107 */     if (value < nbits) {
/* 108 */       return value;
/*     */     }
/* 110 */     int m = 0;
/* 111 */     while (m < 32) {
/* 112 */       int b = readByte(src);
/* 113 */       if ((b & 0x80) != 0) {
/* 114 */         value += (b & 0x7F) << m;
/* 115 */         m += 7; continue;
/*     */       } 
/* 117 */       if (m == 28 && (b & 0xF8) != 0) {
/*     */         break;
/*     */       }
/* 120 */       value += b << m;
/* 121 */       return value;
/*     */     } 
/*     */     
/* 124 */     throw new HPackException("Max integer exceeded");
/*     */   }
/*     */   
/*     */   static void decodePlainString(ByteArrayBuffer buffer, ByteBuffer src) throws HPackException {
/* 128 */     int strLen = decodeInt(src, 7);
/* 129 */     int remaining = src.remaining();
/* 130 */     if (strLen > remaining) {
/* 131 */       throw new HPackException("Unexpected end of HPACK data");
/*     */     }
/* 133 */     int originalLimit = src.limit();
/* 134 */     src.limit(originalLimit - remaining - strLen);
/* 135 */     buffer.append(src);
/* 136 */     src.limit(originalLimit);
/*     */   }
/*     */ 
/*     */   
/*     */   static void decodeHuffman(ByteArrayBuffer buffer, ByteBuffer src) throws HPackException {
/* 141 */     int strLen = decodeInt(src, 7);
/* 142 */     if (strLen > src.remaining()) {
/* 143 */       throw new HPackException("Unexpected end of HPACK data");
/*     */     }
/* 145 */     int limit = src.limit();
/* 146 */     src.limit(src.position() + strLen);
/* 147 */     Huffman.DECODER.decode(buffer, src);
/* 148 */     src.limit(limit);
/*     */   }
/*     */ 
/*     */   
/*     */   void decodeString(ByteArrayBuffer buffer, ByteBuffer src) throws HPackException {
/* 153 */     int firstByte = peekByte(src);
/* 154 */     if ((firstByte & 0x80) == 128) {
/* 155 */       decodeHuffman(buffer, src);
/*     */     } else {
/* 157 */       decodePlainString(buffer, src);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearState() {
/* 163 */     if (this.tmpBuf != null) {
/* 164 */       this.tmpBuf.clear();
/*     */     }
/* 166 */     if (this.charsetDecoder != null) {
/* 167 */       this.charsetDecoder.reset();
/*     */     }
/* 169 */     this.contentBuf.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private void expandCapacity(int capacity) {
/* 174 */     CharBuffer previous = this.tmpBuf;
/* 175 */     this.tmpBuf = CharBuffer.allocate(capacity);
/* 176 */     previous.flip();
/* 177 */     this.tmpBuf.put(previous);
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureCapacity(int extra) {
/* 182 */     if (this.tmpBuf == null) {
/* 183 */       this.tmpBuf = CharBuffer.allocate(Math.max(256, extra));
/*     */     }
/* 185 */     int requiredCapacity = this.tmpBuf.remaining() + extra;
/* 186 */     if (requiredCapacity > this.tmpBuf.capacity()) {
/* 187 */       expandCapacity(requiredCapacity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int decodeString(ByteBuffer src, StringBuilder buf) throws HPackException, CharacterCodingException {
/* 193 */     clearState();
/* 194 */     decodeString(this.contentBuf, src);
/* 195 */     int binaryLen = this.contentBuf.length();
/* 196 */     if (binaryLen == 0) {
/* 197 */       return 0;
/*     */     }
/* 199 */     if (this.charsetDecoder == null) {
/* 200 */       buf.ensureCapacity(binaryLen);
/* 201 */       for (int i = 0; i < binaryLen; i++) {
/* 202 */         buf.append((char)(this.contentBuf.byteAt(i) & 0xFF));
/*     */       }
/*     */     } else {
/* 205 */       ByteBuffer in = ByteBuffer.wrap(this.contentBuf.array(), 0, binaryLen);
/* 206 */       while (in.hasRemaining()) {
/* 207 */         ensureCapacity(in.remaining());
/* 208 */         CoderResult coderResult = this.charsetDecoder.decode(in, this.tmpBuf, true);
/* 209 */         if (coderResult.isError()) {
/* 210 */           coderResult.throwException();
/*     */         }
/*     */       } 
/* 213 */       ensureCapacity(8);
/* 214 */       CoderResult result = this.charsetDecoder.flush(this.tmpBuf);
/* 215 */       if (result.isError()) {
/* 216 */         result.throwException();
/*     */       }
/* 218 */       this.tmpBuf.flip();
/* 219 */       buf.append(this.tmpBuf);
/*     */     } 
/* 221 */     return binaryLen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   HPackHeader decodeLiteralHeader(ByteBuffer src, HPackRepresentation representation) throws HPackException, CharacterCodingException {
/*     */     String name;
/* 228 */     int nameLen, n = (representation == HPackRepresentation.WITH_INDEXING) ? 6 : 4;
/* 229 */     int index = decodeInt(src, n);
/*     */ 
/*     */     
/* 232 */     if (index == 0) {
/* 233 */       StringBuilder stringBuilder = new StringBuilder();
/* 234 */       nameLen = decodeString(src, stringBuilder);
/* 235 */       name = stringBuilder.toString();
/*     */     } else {
/* 237 */       HPackHeader existing = this.dynamicTable.getHeader(index);
/* 238 */       if (existing == null) {
/* 239 */         throw new HPackException("Invalid header index");
/*     */       }
/* 241 */       name = existing.getName();
/* 242 */       nameLen = existing.getNameLen();
/*     */     } 
/* 244 */     StringBuilder buf = new StringBuilder();
/* 245 */     int valueLen = decodeString(src, buf);
/* 246 */     String value = buf.toString();
/* 247 */     HPackHeader header = new HPackHeader(name, nameLen, value, valueLen, (representation == HPackRepresentation.NEVER_INDEXED));
/* 248 */     if (representation == HPackRepresentation.WITH_INDEXING) {
/* 249 */       this.dynamicTable.add(header);
/*     */     }
/* 251 */     return header;
/*     */   }
/*     */ 
/*     */   
/*     */   HPackHeader decodeIndexedHeader(ByteBuffer src) throws HPackException {
/* 256 */     int index = decodeInt(src, 7);
/* 257 */     HPackHeader existing = this.dynamicTable.getHeader(index);
/* 258 */     if (existing == null) {
/* 259 */       throw new HPackException("Invalid header index");
/*     */     }
/* 261 */     return existing;
/*     */   }
/*     */   
/*     */   public Header decodeHeader(ByteBuffer src) throws HPackException {
/* 265 */     HPackHeader header = decodeHPackHeader(src);
/* 266 */     return (header != null) ? (Header)new BasicHeader(header.getName(), header.getValue(), header.isSensitive()) : null;
/*     */   }
/*     */   
/*     */   HPackHeader decodeHPackHeader(ByteBuffer src) throws HPackException {
/*     */     try {
/* 271 */       while (src.hasRemaining()) {
/* 272 */         int b = peekByte(src);
/* 273 */         if ((b & 0x80) == 128)
/* 274 */           return decodeIndexedHeader(src); 
/* 275 */         if ((b & 0xC0) == 64)
/* 276 */           return decodeLiteralHeader(src, HPackRepresentation.WITH_INDEXING); 
/* 277 */         if ((b & 0xF0) == 0)
/* 278 */           return decodeLiteralHeader(src, HPackRepresentation.WITHOUT_INDEXING); 
/* 279 */         if ((b & 0xF0) == 16)
/* 280 */           return decodeLiteralHeader(src, HPackRepresentation.NEVER_INDEXED); 
/* 281 */         if ((b & 0xE0) == 32) {
/* 282 */           int maxSize = decodeInt(src, 5);
/* 283 */           this.dynamicTable.setMaxSize(Math.min(this.maxTableSize, maxSize)); continue;
/*     */         } 
/* 285 */         throw new HPackException("Unexpected header first byte: 0x" + Integer.toHexString(b));
/*     */       } 
/*     */       
/* 288 */       return null;
/* 289 */     } catch (CharacterCodingException ex) {
/* 290 */       throw new HPackException(ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Header> decodeHeaders(ByteBuffer src) throws HPackException {
/* 295 */     boolean enforceSizeLimit = (this.maxListSize < Integer.MAX_VALUE);
/* 296 */     int listSize = 0;
/*     */     
/* 298 */     List<Header> list = new ArrayList<>();
/* 299 */     while (src.hasRemaining()) {
/* 300 */       HPackHeader header = decodeHPackHeader(src);
/* 301 */       if (header == null) {
/*     */         break;
/*     */       }
/* 304 */       if (enforceSizeLimit) {
/* 305 */         listSize += header.getTotalSize();
/* 306 */         if (listSize >= this.maxListSize) {
/* 307 */           throw new HeaderListConstraintException("Maximum header list size exceeded");
/*     */         }
/*     */       } 
/* 310 */       list.add(new BasicHeader(header.getName(), header.getValue(), header.isSensitive()));
/*     */     } 
/* 312 */     return list;
/*     */   }
/*     */   
/*     */   public int getMaxTableSize() {
/* 316 */     return this.maxTableSize;
/*     */   }
/*     */   
/*     */   public void setMaxTableSize(int maxTableSize) {
/* 320 */     Args.notNegative(maxTableSize, "Max table size");
/* 321 */     this.maxTableSize = maxTableSize;
/* 322 */     this.dynamicTable.setMaxSize(maxTableSize);
/*     */   }
/*     */   
/*     */   public int getMaxListSize() {
/* 326 */     return this.maxListSize;
/*     */   }
/*     */   
/*     */   public void setMaxListSize(int maxListSize) {
/* 330 */     Args.notNegative(maxListSize, "Max list size");
/* 331 */     this.maxListSize = maxListSize;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HPackDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */