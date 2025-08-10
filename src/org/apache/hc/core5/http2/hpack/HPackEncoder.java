/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.Header;
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
/*     */ @Internal
/*     */ public final class HPackEncoder
/*     */ {
/*     */   private final OutboundDynamicTable dynamicTable;
/*     */   private final ByteArrayBuffer huffmanBuf;
/*     */   private final CharsetEncoder charsetEncoder;
/*     */   private ByteBuffer tmpBuf;
/*     */   private int maxTableSize;
/*     */   
/*     */   HPackEncoder(OutboundDynamicTable dynamicTable, CharsetEncoder charsetEncoder) {
/*  60 */     this.dynamicTable = (dynamicTable != null) ? dynamicTable : new OutboundDynamicTable();
/*  61 */     this.huffmanBuf = new ByteArrayBuffer(128);
/*  62 */     this.charsetEncoder = charsetEncoder;
/*     */   }
/*     */   
/*     */   HPackEncoder(OutboundDynamicTable dynamicTable, Charset charset) {
/*  66 */     this(dynamicTable, (charset != null && !StandardCharsets.US_ASCII.equals(charset)) ? charset.newEncoder() : null);
/*     */   }
/*     */   
/*     */   public HPackEncoder(Charset charset) {
/*  70 */     this(new OutboundDynamicTable(), charset);
/*     */   }
/*     */   
/*     */   public HPackEncoder(CharsetEncoder charsetEncoder) {
/*  74 */     this(new OutboundDynamicTable(), charsetEncoder);
/*     */   }
/*     */ 
/*     */   
/*     */   static void encodeInt(ByteArrayBuffer dst, int n, int i, int mask) {
/*  79 */     int nbits = 255 >>> 8 - n;
/*  80 */     int value = i;
/*  81 */     if (value < nbits) {
/*  82 */       dst.append(i | mask);
/*     */     } else {
/*  84 */       dst.append(nbits | mask);
/*  85 */       value -= nbits;
/*     */       
/*  87 */       while (value >= 128) {
/*  88 */         dst.append(value & 0x7F | 0x80);
/*  89 */         value >>>= 7;
/*     */       } 
/*  91 */       dst.append(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void encodeHuffman(ByteArrayBuffer dst, ByteBuffer src) {
/*  97 */     Huffman.ENCODER.encode(dst, src);
/*     */   }
/*     */ 
/*     */   
/*     */   void encodeString(ByteArrayBuffer dst, ByteBuffer src, boolean huffman) {
/* 102 */     int strLen = src.remaining();
/* 103 */     if (huffman) {
/* 104 */       this.huffmanBuf.clear();
/* 105 */       this.huffmanBuf.ensureCapacity(strLen);
/* 106 */       Huffman.ENCODER.encode(this.huffmanBuf, src);
/* 107 */       dst.ensureCapacity(this.huffmanBuf.length() + 8);
/* 108 */       encodeInt(dst, 7, this.huffmanBuf.length(), 128);
/* 109 */       dst.append(this.huffmanBuf.array(), 0, this.huffmanBuf.length());
/*     */     } else {
/* 111 */       dst.ensureCapacity(strLen + 8);
/* 112 */       encodeInt(dst, 7, strLen, 0);
/* 113 */       dst.append(src);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearState() {
/* 119 */     if (this.tmpBuf != null) {
/* 120 */       this.tmpBuf.clear();
/*     */     }
/* 122 */     if (this.charsetEncoder != null) {
/* 123 */       this.charsetEncoder.reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void expandCapacity(int capacity) {
/* 129 */     ByteBuffer previous = this.tmpBuf;
/* 130 */     this.tmpBuf = ByteBuffer.allocate(capacity);
/* 131 */     previous.flip();
/* 132 */     this.tmpBuf.put(previous);
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureCapacity(int extra) {
/* 137 */     if (this.tmpBuf == null) {
/* 138 */       this.tmpBuf = ByteBuffer.allocate(Math.max(256, extra));
/*     */     }
/* 140 */     int requiredCapacity = this.tmpBuf.remaining() + extra;
/* 141 */     if (requiredCapacity > this.tmpBuf.capacity()) {
/* 142 */       expandCapacity(requiredCapacity);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int encodeString(ByteArrayBuffer dst, CharSequence charSequence, int off, int len, boolean huffman) throws CharacterCodingException {
/* 151 */     clearState();
/* 152 */     if (this.charsetEncoder == null) {
/* 153 */       if (huffman) {
/* 154 */         this.huffmanBuf.clear();
/* 155 */         this.huffmanBuf.ensureCapacity(len);
/* 156 */         Huffman.ENCODER.encode(this.huffmanBuf, charSequence, off, len);
/* 157 */         dst.ensureCapacity(this.huffmanBuf.length() + 8);
/* 158 */         encodeInt(dst, 7, this.huffmanBuf.length(), 128);
/* 159 */         dst.append(this.huffmanBuf.array(), 0, this.huffmanBuf.length());
/*     */       } else {
/* 161 */         dst.ensureCapacity(len + 8);
/* 162 */         encodeInt(dst, 7, len, 0);
/* 163 */         for (int i = 0; i < len; i++) {
/* 164 */           dst.append(charSequence.charAt(off + i));
/*     */         }
/*     */       } 
/* 167 */       return len;
/*     */     } 
/* 169 */     if (charSequence.length() > 0) {
/* 170 */       CharBuffer in = CharBuffer.wrap(charSequence, off, len);
/* 171 */       while (in.hasRemaining()) {
/* 172 */         ensureCapacity((int)(in.remaining() * this.charsetEncoder.averageBytesPerChar()) + 8);
/* 173 */         CoderResult coderResult = this.charsetEncoder.encode(in, this.tmpBuf, true);
/* 174 */         if (coderResult.isError()) {
/* 175 */           coderResult.throwException();
/*     */         }
/*     */       } 
/* 178 */       ensureCapacity(8);
/* 179 */       CoderResult result = this.charsetEncoder.flush(this.tmpBuf);
/* 180 */       if (result.isError()) {
/* 181 */         result.throwException();
/*     */       }
/*     */     } 
/* 184 */     this.tmpBuf.flip();
/* 185 */     int binaryLen = this.tmpBuf.remaining();
/* 186 */     encodeString(dst, this.tmpBuf, huffman);
/* 187 */     return binaryLen;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodeString(ByteArrayBuffer dst, String s, boolean huffman) throws CharacterCodingException {
/* 192 */     return encodeString(dst, s, 0, s.length(), huffman);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void encodeLiteralHeader(ByteArrayBuffer dst, HPackEntry existing, Header header, HPackRepresentation representation, boolean useHuffman) throws CharacterCodingException {
/* 198 */     encodeLiteralHeader(dst, existing, header.getName(), header.getValue(), header.isSensitive(), representation, useHuffman);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encodeLiteralHeader(ByteArrayBuffer dst, HPackEntry existing, String key, String value, boolean sensitive, HPackRepresentation representation, boolean useHuffman) throws CharacterCodingException {
/*     */     int n, mask, nameLen;
/* 207 */     switch (representation) {
/*     */       case WITH_INDEXING:
/* 209 */         mask = 64;
/* 210 */         n = 6;
/*     */         break;
/*     */       case WITHOUT_INDEXING:
/* 213 */         mask = 0;
/* 214 */         n = 4;
/*     */         break;
/*     */       case NEVER_INDEXED:
/* 217 */         mask = 16;
/* 218 */         n = 4;
/*     */         break;
/*     */       default:
/* 221 */         throw new IllegalStateException("Unexpected value: " + representation);
/*     */     } 
/* 223 */     int index = (existing != null) ? existing.getIndex() : 0;
/*     */     
/* 225 */     if (index <= 0) {
/* 226 */       encodeInt(dst, n, 0, mask);
/* 227 */       nameLen = encodeString(dst, key, useHuffman);
/*     */     } else {
/* 229 */       encodeInt(dst, n, index, mask);
/* 230 */       nameLen = existing.getHeader().getNameLen();
/*     */     } 
/* 232 */     int valueLen = encodeString(dst, (value != null) ? value : "", useHuffman);
/* 233 */     if (representation == HPackRepresentation.WITH_INDEXING) {
/* 234 */       this.dynamicTable.add(new HPackHeader(key, nameLen, value, valueLen, sensitive));
/*     */     }
/*     */   }
/*     */   
/*     */   void encodeIndex(ByteArrayBuffer dst, int index) {
/* 239 */     encodeInt(dst, 7, index, 128);
/*     */   }
/*     */   
/*     */   private int findFullMatch(List<HPackEntry> entries, String value) {
/* 243 */     if (entries == null || entries.isEmpty()) {
/* 244 */       return 0;
/*     */     }
/* 246 */     for (int i = 0; i < entries.size(); i++) {
/* 247 */       HPackEntry entry = entries.get(i);
/* 248 */       if (Objects.equals(value, entry.getHeader().getValue())) {
/* 249 */         return entry.getIndex();
/*     */       }
/*     */     } 
/* 252 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void encodeHeader(ByteArrayBuffer dst, Header header, boolean noIndexing, boolean useHuffman) throws CharacterCodingException {
/* 258 */     encodeHeader(dst, header.getName(), header.getValue(), header.isSensitive(), noIndexing, useHuffman);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encodeHeader(ByteArrayBuffer dst, String name, String value, boolean sensitive, boolean noIndexing, boolean useHuffman) throws CharacterCodingException {
/*     */     HPackRepresentation representation;
/* 266 */     if (sensitive) {
/* 267 */       representation = HPackRepresentation.NEVER_INDEXED;
/* 268 */     } else if (noIndexing) {
/* 269 */       representation = HPackRepresentation.WITHOUT_INDEXING;
/*     */     } else {
/* 271 */       representation = HPackRepresentation.WITH_INDEXING;
/*     */     } 
/*     */     
/* 274 */     List<HPackEntry> staticEntries = StaticTable.INSTANCE.getByName(name);
/*     */     
/* 276 */     if (representation == HPackRepresentation.WITH_INDEXING) {
/*     */       
/* 278 */       int staticIndex = findFullMatch(staticEntries, value);
/* 279 */       if (staticIndex > 0) {
/* 280 */         encodeIndex(dst, staticIndex);
/*     */         return;
/*     */       } 
/* 283 */       List<HPackEntry> dynamicEntries = this.dynamicTable.getByName(name);
/* 284 */       int dynamicIndex = findFullMatch(dynamicEntries, value);
/* 285 */       if (dynamicIndex > 0) {
/* 286 */         encodeIndex(dst, dynamicIndex);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 291 */     HPackEntry existing = null;
/* 292 */     if (staticEntries != null && !staticEntries.isEmpty()) {
/* 293 */       existing = staticEntries.get(0);
/*     */     } else {
/* 295 */       List<HPackEntry> dynamicEntries = this.dynamicTable.getByName(name);
/* 296 */       if (dynamicEntries != null && !dynamicEntries.isEmpty()) {
/* 297 */         existing = dynamicEntries.get(0);
/*     */       }
/*     */     } 
/* 300 */     encodeLiteralHeader(dst, existing, name, value, sensitive, representation, useHuffman);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void encodeHeaders(ByteArrayBuffer dst, List<? extends Header> headers, boolean noIndexing, boolean useHuffman) throws CharacterCodingException {
/* 306 */     for (int i = 0; i < headers.size(); i++) {
/* 307 */       encodeHeader(dst, headers.get(i), noIndexing, useHuffman);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeHeader(ByteArrayBuffer dst, Header header) throws CharacterCodingException {
/* 313 */     Args.notNull(dst, "ByteArrayBuffer");
/* 314 */     Args.notNull(header, "Header");
/* 315 */     encodeHeader(dst, header.getName(), header.getValue(), header.isSensitive());
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeHeader(ByteArrayBuffer dst, String name, String value, boolean sensitive) throws CharacterCodingException {
/* 320 */     Args.notNull(dst, "ByteArrayBuffer");
/* 321 */     Args.notEmpty(name, "Header name");
/* 322 */     encodeHeader(dst, name, value, sensitive, false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeHeaders(ByteArrayBuffer dst, List<? extends Header> headers, boolean useHuffman) throws CharacterCodingException {
/* 327 */     Args.notNull(dst, "ByteArrayBuffer");
/* 328 */     Args.notEmpty(headers, "Header list");
/* 329 */     encodeHeaders(dst, headers, false, useHuffman);
/*     */   }
/*     */   
/*     */   public int getMaxTableSize() {
/* 333 */     return this.maxTableSize;
/*     */   }
/*     */   
/*     */   public void setMaxTableSize(int maxTableSize) {
/* 337 */     Args.notNegative(maxTableSize, "Max table size");
/* 338 */     this.maxTableSize = maxTableSize;
/* 339 */     this.dynamicTable.setMaxSize(maxTableSize);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HPackEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */