/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.io.IndependentRandomAccessSource;
/*     */ import com.itextpdf.text.io.RandomAccessSource;
/*     */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomAccessFileOrArray
/*     */   implements DataInput
/*     */ {
/*     */   private final RandomAccessSource byteSource;
/*     */   private long byteSourcePosition;
/*     */   private byte back;
/*     */   private boolean isBack = false;
/*     */   
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(String filename) throws IOException {
/*  97 */     this((new RandomAccessSourceFactory())
/*  98 */         .setForceRead(false)
/*  99 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/* 100 */         .createBestSource(filename));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(RandomAccessFileOrArray source) {
/* 112 */     this((RandomAccessSource)new IndependentRandomAccessSource(source.byteSource));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessFileOrArray createView() {
/* 121 */     return new RandomAccessFileOrArray((RandomAccessSource)new IndependentRandomAccessSource(this.byteSource));
/*     */   }
/*     */   
/*     */   public RandomAccessSource createSourceView() {
/* 125 */     return (RandomAccessSource)new IndependentRandomAccessSource(this.byteSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomAccessFileOrArray(RandomAccessSource byteSource) {
/* 134 */     this.byteSource = byteSource;
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
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(String filename, boolean forceRead, boolean plainRandomAccess) throws IOException {
/* 147 */     this((new RandomAccessSourceFactory())
/* 148 */         .setForceRead(forceRead)
/* 149 */         .setUsePlainRandomAccess(plainRandomAccess)
/* 150 */         .createBestSource(filename));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(URL url) throws IOException {
/* 160 */     this((new RandomAccessSourceFactory()).createSource(url));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(InputStream is) throws IOException {
/* 170 */     this((new RandomAccessSourceFactory()).createSource(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RandomAccessFileOrArray(byte[] arrayIn) {
/* 181 */     this((new RandomAccessSourceFactory()).createSource(arrayIn));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RandomAccessSource getByteSource() {
/* 188 */     return this.byteSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushBack(byte b) {
/* 196 */     this.back = b;
/* 197 */     this.isBack = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 206 */     if (this.isBack) {
/* 207 */       this.isBack = false;
/* 208 */       return this.back & 0xFF;
/*     */     } 
/*     */     
/* 211 */     return this.byteSource.get(this.byteSourcePosition++);
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 215 */     if (len == 0)
/* 216 */       return 0; 
/* 217 */     int count = 0;
/* 218 */     if (this.isBack && len > 0) {
/* 219 */       this.isBack = false;
/* 220 */       b[off++] = this.back;
/* 221 */       len--;
/* 222 */       count++;
/*     */     } 
/* 224 */     if (len > 0) {
/* 225 */       int byteSourceCount = this.byteSource.get(this.byteSourcePosition, b, off, len);
/* 226 */       if (byteSourceCount > 0) {
/* 227 */         count += byteSourceCount;
/* 228 */         this.byteSourcePosition += byteSourceCount;
/*     */       } 
/*     */     } 
/* 231 */     if (count == 0)
/* 232 */       return -1; 
/* 233 */     return count;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 237 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/* 241 */     readFully(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/* 245 */     int n = 0;
/*     */     do {
/* 247 */       int count = read(b, off + n, len - n);
/* 248 */       if (count < 0)
/* 249 */         throw new EOFException(); 
/* 250 */       n += count;
/* 251 */     } while (n < len);
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 255 */     if (n <= 0L) {
/* 256 */       return 0L;
/*     */     }
/* 258 */     int adj = 0;
/* 259 */     if (this.isBack) {
/* 260 */       this.isBack = false;
/* 261 */       if (n == 1L) {
/* 262 */         return 1L;
/*     */       }
/*     */       
/* 265 */       n--;
/* 266 */       adj = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     long pos = getFilePointer();
/* 274 */     long len = length();
/* 275 */     long newpos = pos + n;
/* 276 */     if (newpos > len) {
/* 277 */       newpos = len;
/*     */     }
/* 279 */     seek(newpos);
/*     */ 
/*     */     
/* 282 */     return newpos - pos + adj;
/*     */   }
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/* 286 */     return (int)skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void reOpen() throws IOException {
/* 292 */     seek(0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 297 */     this.isBack = false;
/*     */     
/* 299 */     this.byteSource.close();
/*     */   }
/*     */   
/*     */   public long length() throws IOException {
/* 303 */     return this.byteSource.length();
/*     */   }
/*     */   
/*     */   public void seek(long pos) throws IOException {
/* 307 */     this.byteSourcePosition = pos;
/* 308 */     this.isBack = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFilePointer() throws IOException {
/* 313 */     return this.byteSourcePosition - (this.isBack ? 1L : 0L);
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws IOException {
/* 317 */     int ch = read();
/* 318 */     if (ch < 0)
/* 319 */       throw new EOFException(); 
/* 320 */     return (ch != 0);
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException {
/* 324 */     int ch = read();
/* 325 */     if (ch < 0)
/* 326 */       throw new EOFException(); 
/* 327 */     return (byte)ch;
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/* 331 */     int ch = read();
/* 332 */     if (ch < 0)
/* 333 */       throw new EOFException(); 
/* 334 */     return ch;
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException {
/* 338 */     int ch1 = read();
/* 339 */     int ch2 = read();
/* 340 */     if ((ch1 | ch2) < 0)
/* 341 */       throw new EOFException(); 
/* 342 */     return (short)((ch1 << 8) + ch2);
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
/*     */   public final short readShortLE() throws IOException {
/* 367 */     int ch1 = read();
/* 368 */     int ch2 = read();
/* 369 */     if ((ch1 | ch2) < 0)
/* 370 */       throw new EOFException(); 
/* 371 */     return (short)((ch2 << 8) + (ch1 << 0));
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/* 375 */     int ch1 = read();
/* 376 */     int ch2 = read();
/* 377 */     if ((ch1 | ch2) < 0)
/* 378 */       throw new EOFException(); 
/* 379 */     return (ch1 << 8) + ch2;
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
/*     */   public final int readUnsignedShortLE() throws IOException {
/* 404 */     int ch1 = read();
/* 405 */     int ch2 = read();
/* 406 */     if ((ch1 | ch2) < 0)
/* 407 */       throw new EOFException(); 
/* 408 */     return (ch2 << 8) + (ch1 << 0);
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException {
/* 412 */     int ch1 = read();
/* 413 */     int ch2 = read();
/* 414 */     if ((ch1 | ch2) < 0)
/* 415 */       throw new EOFException(); 
/* 416 */     return (char)((ch1 << 8) + ch2);
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
/*     */   public final char readCharLE() throws IOException {
/* 440 */     int ch1 = read();
/* 441 */     int ch2 = read();
/* 442 */     if ((ch1 | ch2) < 0)
/* 443 */       throw new EOFException(); 
/* 444 */     return (char)((ch2 << 8) + (ch1 << 0));
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/* 448 */     int ch1 = read();
/* 449 */     int ch2 = read();
/* 450 */     int ch3 = read();
/* 451 */     int ch4 = read();
/* 452 */     if ((ch1 | ch2 | ch3 | ch4) < 0)
/* 453 */       throw new EOFException(); 
/* 454 */     return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
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
/*     */   public final int readIntLE() throws IOException {
/* 479 */     int ch1 = read();
/* 480 */     int ch2 = read();
/* 481 */     int ch3 = read();
/* 482 */     int ch4 = read();
/* 483 */     if ((ch1 | ch2 | ch3 | ch4) < 0)
/* 484 */       throw new EOFException(); 
/* 485 */     return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
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
/*     */   public final long readUnsignedInt() throws IOException {
/* 509 */     long ch1 = read();
/* 510 */     long ch2 = read();
/* 511 */     long ch3 = read();
/* 512 */     long ch4 = read();
/* 513 */     if ((ch1 | ch2 | ch3 | ch4) < 0L)
/* 514 */       throw new EOFException(); 
/* 515 */     return (ch1 << 24L) + (ch2 << 16L) + (ch3 << 8L) + (ch4 << 0L);
/*     */   }
/*     */   
/*     */   public final long readUnsignedIntLE() throws IOException {
/* 519 */     long ch1 = read();
/* 520 */     long ch2 = read();
/* 521 */     long ch3 = read();
/* 522 */     long ch4 = read();
/* 523 */     if ((ch1 | ch2 | ch3 | ch4) < 0L)
/* 524 */       throw new EOFException(); 
/* 525 */     return (ch4 << 24L) + (ch3 << 16L) + (ch2 << 8L) + (ch1 << 0L);
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/* 529 */     return (readInt() << 32L) + (readInt() & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public final long readLongLE() throws IOException {
/* 533 */     int i1 = readIntLE();
/* 534 */     int i2 = readIntLE();
/* 535 */     return (i2 << 32L) + (i1 & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public float readFloat() throws IOException {
/* 539 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */   
/*     */   public final float readFloatLE() throws IOException {
/* 543 */     return Float.intBitsToFloat(readIntLE());
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException {
/* 547 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */   
/*     */   public final double readDoubleLE() throws IOException {
/* 551 */     return Double.longBitsToDouble(readLongLE());
/*     */   }
/*     */   
/*     */   public String readLine() throws IOException {
/* 555 */     StringBuilder input = new StringBuilder();
/* 556 */     int c = -1;
/* 557 */     boolean eol = false;
/*     */     
/* 559 */     while (!eol) {
/* 560 */       long cur; switch (c = read()) {
/*     */         case -1:
/*     */         case 10:
/* 563 */           eol = true;
/*     */           continue;
/*     */         case 13:
/* 566 */           eol = true;
/* 567 */           cur = getFilePointer();
/* 568 */           if (read() != 10) {
/* 569 */             seek(cur);
/*     */           }
/*     */           continue;
/*     */       } 
/* 573 */       input.append((char)c);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 578 */     if (c == -1 && input.length() == 0) {
/* 579 */       return null;
/*     */     }
/* 581 */     return input.toString();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException {
/* 585 */     return DataInputStream.readUTF(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readString(int length, String encoding) throws IOException {
/* 596 */     byte[] buf = new byte[length];
/* 597 */     readFully(buf);
/*     */     try {
/* 599 */       return new String(buf, encoding);
/*     */     }
/* 601 */     catch (Exception e) {
/* 602 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/RandomAccessFileOrArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */