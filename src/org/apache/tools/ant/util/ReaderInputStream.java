/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReaderInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   private final Reader reader;
/*     */   private final CharsetEncoder encoder;
/*     */   private final CharBuffer encoderIn;
/*     */   private final ByteBuffer encoderOut;
/*     */   private CoderResult lastCoderResult;
/*     */   private boolean endOfInput;
/*     */   
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder) {
/*  66 */     this(reader, encoder, 1024);
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
/*     */   public ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
/*  78 */     this.reader = reader;
/*  79 */     this.encoder = encoder;
/*  80 */     this.encoderIn = CharBuffer.allocate(bufferSize);
/*  81 */     this.encoderIn.flip();
/*  82 */     this.encoderOut = ByteBuffer.allocate(128);
/*  83 */     this.encoderOut.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReaderInputStream(Reader reader) {
/*  93 */     this(reader, Charset.defaultCharset());
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
/*     */   public ReaderInputStream(Reader reader, String encoding) {
/* 105 */     this(reader, Charset.forName(encoding));
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
/*     */   public ReaderInputStream(Reader reader, Charset charset) {
/* 118 */     this(reader, charset
/* 119 */         .newEncoder()
/* 120 */         .onMalformedInput(CodingErrorAction.REPLACE)
/* 121 */         .onUnmappableCharacter(CodingErrorAction.REPLACE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws IOException {
/* 131 */     if (!this.endOfInput && (this.lastCoderResult == null || this.lastCoderResult.isUnderflow())) {
/* 132 */       this.encoderIn.compact();
/* 133 */       int position = this.encoderIn.position();
/*     */ 
/*     */ 
/*     */       
/* 137 */       int c = this.reader.read(this.encoderIn.array(), position, this.encoderIn.remaining());
/* 138 */       if (c == -1) {
/* 139 */         this.endOfInput = true;
/*     */       } else {
/* 141 */         this.encoderIn.position(position + c);
/*     */       } 
/* 143 */       this.encoderIn.flip();
/*     */     } 
/* 145 */     this.encoderOut.compact();
/* 146 */     this.lastCoderResult = this.encoder.encode(this.encoderIn, this.encoderOut, this.endOfInput);
/* 147 */     this.encoderOut.flip();
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
/*     */   public int read(byte[] array, int off, int len) throws IOException {
/* 162 */     Objects.requireNonNull(array, "array");
/* 163 */     if (len < 0 || off < 0 || off + len > array.length) {
/* 164 */       throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + off + ", length=" + len);
/*     */     }
/*     */     
/* 167 */     int read = 0;
/* 168 */     if (len == 0) {
/* 169 */       return 0;
/*     */     }
/* 171 */     while (len > 0) {
/* 172 */       if (this.encoderOut.hasRemaining()) {
/* 173 */         int c = Math.min(this.encoderOut.remaining(), len);
/* 174 */         this.encoderOut.get(array, off, c);
/* 175 */         off += c;
/* 176 */         len -= c;
/* 177 */         read += c; continue;
/*     */       } 
/* 179 */       fillBuffer();
/* 180 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 185 */     return (read == 0 && this.endOfInput) ? -1 : read;
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
/*     */   public int read(byte[] b) throws IOException {
/* 198 */     return read(b, 0, b.length);
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
/*     */   public int read() throws IOException {
/*     */     while (true) {
/* 211 */       if (this.encoderOut.hasRemaining()) {
/* 212 */         return this.encoderOut.get() & 0xFF;
/*     */       }
/* 214 */       fillBuffer();
/* 215 */       if (this.endOfInput && !this.encoderOut.hasRemaining()) {
/* 216 */         return -1;
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
/*     */   public void close() throws IOException {
/* 228 */     this.reader.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ReaderInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */