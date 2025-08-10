/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Queue;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
/*     */   private static final int MAX_ARRAY_LEN = 2147483639;
/*     */   private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
/*     */   
/*     */   static byte[] createBuffer() {
/*  65 */     return new byte[8192];
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(InputStream from, OutputStream to) throws IOException {
/* 113 */     Preconditions.checkNotNull(from);
/* 114 */     Preconditions.checkNotNull(to);
/* 115 */     byte[] buf = createBuffer();
/* 116 */     long total = 0L;
/*     */     while (true) {
/* 118 */       int r = from.read(buf);
/* 119 */       if (r == -1) {
/*     */         break;
/*     */       }
/* 122 */       to.write(buf, 0, r);
/* 123 */       total += r;
/*     */     } 
/* 125 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
/* 139 */     Preconditions.checkNotNull(from);
/* 140 */     Preconditions.checkNotNull(to);
/* 141 */     if (from instanceof FileChannel) {
/* 142 */       FileChannel sourceChannel = (FileChannel)from;
/* 143 */       long oldPosition = sourceChannel.position();
/* 144 */       long position = oldPosition;
/*     */       
/*     */       while (true) {
/* 147 */         long copied = sourceChannel.transferTo(position, 524288L, to);
/* 148 */         position += copied;
/* 149 */         sourceChannel.position(position);
/* 150 */         if (copied <= 0L && position >= sourceChannel.size())
/* 151 */           return position - oldPosition; 
/*     */       } 
/*     */     } 
/* 154 */     ByteBuffer buf = ByteBuffer.wrap(createBuffer());
/* 155 */     long total = 0L;
/* 156 */     while (from.read(buf) != -1) {
/* 157 */       Java8Compatibility.flip(buf);
/* 158 */       while (buf.hasRemaining()) {
/* 159 */         total += to.write(buf);
/*     */       }
/* 161 */       Java8Compatibility.clear(buf);
/*     */     } 
/* 163 */     return total;
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
/*     */   private static byte[] toByteArrayInternal(InputStream in, Queue<byte[]> bufs, int totalLen) throws IOException {
/* 181 */     int initialBufferSize = Math.min(8192, Math.max(128, Integer.highestOneBit(totalLen) * 2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     int bufSize = initialBufferSize;
/* 188 */     for (; totalLen < 2147483639; 
/* 189 */       bufSize = IntMath.saturatedMultiply(bufSize, (bufSize < 4096) ? 4 : 2)) {
/* 190 */       byte[] buf = new byte[Math.min(bufSize, 2147483639 - totalLen)];
/* 191 */       bufs.add(buf);
/* 192 */       int off = 0;
/* 193 */       while (off < buf.length) {
/*     */         
/* 195 */         int r = in.read(buf, off, buf.length - off);
/* 196 */         if (r == -1) {
/* 197 */           return combineBuffers(bufs, totalLen);
/*     */         }
/* 199 */         off += r;
/* 200 */         totalLen += r;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 205 */     if (in.read() == -1)
/*     */     {
/* 207 */       return combineBuffers(bufs, 2147483639);
/*     */     }
/* 209 */     throw new OutOfMemoryError("input is too large to fit in a byte array");
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] combineBuffers(Queue<byte[]> bufs, int totalLen) {
/* 214 */     if (bufs.isEmpty()) {
/* 215 */       return new byte[0];
/*     */     }
/* 217 */     byte[] result = bufs.remove();
/* 218 */     if (result.length == totalLen) {
/* 219 */       return result;
/*     */     }
/* 221 */     int remaining = totalLen - result.length;
/* 222 */     result = Arrays.copyOf(result, totalLen);
/* 223 */     while (remaining > 0) {
/* 224 */       byte[] buf = bufs.remove();
/* 225 */       int bytesToCopy = Math.min(remaining, buf.length);
/* 226 */       int resultOffset = totalLen - remaining;
/* 227 */       System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
/* 228 */       remaining -= bytesToCopy;
/*     */     } 
/* 230 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(InputStream in) throws IOException {
/* 241 */     Preconditions.checkNotNull(in);
/* 242 */     return toByteArrayInternal(in, (Queue)new ArrayDeque<>(20), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toByteArray(InputStream in, long expectedSize) throws IOException {
/* 251 */     Preconditions.checkArgument((expectedSize >= 0L), "expectedSize (%s) must be non-negative", expectedSize);
/* 252 */     if (expectedSize > 2147483639L) {
/* 253 */       throw new OutOfMemoryError(expectedSize + " bytes is too large to fit in a byte array");
/*     */     }
/*     */     
/* 256 */     byte[] bytes = new byte[(int)expectedSize];
/* 257 */     int remaining = (int)expectedSize;
/*     */     
/* 259 */     while (remaining > 0) {
/* 260 */       int off = (int)expectedSize - remaining;
/* 261 */       int read = in.read(bytes, off, remaining);
/* 262 */       if (read == -1)
/*     */       {
/*     */         
/* 265 */         return Arrays.copyOf(bytes, off);
/*     */       }
/* 267 */       remaining -= read;
/*     */     } 
/*     */ 
/*     */     
/* 271 */     int b = in.read();
/* 272 */     if (b == -1) {
/* 273 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/* 277 */     Queue<byte[]> bufs = (Queue)new ArrayDeque<>(22);
/* 278 */     bufs.add(bytes);
/* 279 */     bufs.add(new byte[] { (byte)b });
/* 280 */     return toByteArrayInternal(in, bufs, bytes.length + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(InputStream in) throws IOException {
/* 291 */     long total = 0L;
/*     */     
/* 293 */     byte[] buf = createBuffer(); long read;
/* 294 */     while ((read = in.read(buf)) != -1L) {
/* 295 */       total += read;
/*     */     }
/* 297 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes) {
/* 305 */     return newDataInput(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
/* 316 */     Preconditions.checkPositionIndex(start, bytes.length);
/* 317 */     return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
/* 328 */     return new ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
/*     */     final DataInput input;
/*     */     
/*     */     ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
/* 335 */       this.input = new DataInputStream(byteArrayInputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b) {
/*     */       try {
/* 341 */         this.input.readFully(b);
/* 342 */       } catch (IOException e) {
/* 343 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b, int off, int len) {
/*     */       try {
/* 350 */         this.input.readFully(b, off, len);
/* 351 */       } catch (IOException e) {
/* 352 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int skipBytes(int n) {
/*     */       try {
/* 359 */         return this.input.skipBytes(n);
/* 360 */       } catch (IOException e) {
/* 361 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean readBoolean() {
/*     */       try {
/* 368 */         return this.input.readBoolean();
/* 369 */       } catch (IOException e) {
/* 370 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte readByte() {
/*     */       try {
/* 377 */         return this.input.readByte();
/* 378 */       } catch (EOFException e) {
/* 379 */         throw new IllegalStateException(e);
/* 380 */       } catch (IOException impossible) {
/* 381 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedByte() {
/*     */       try {
/* 388 */         return this.input.readUnsignedByte();
/* 389 */       } catch (IOException e) {
/* 390 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public short readShort() {
/*     */       try {
/* 397 */         return this.input.readShort();
/* 398 */       } catch (IOException e) {
/* 399 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedShort() {
/*     */       try {
/* 406 */         return this.input.readUnsignedShort();
/* 407 */       } catch (IOException e) {
/* 408 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char readChar() {
/*     */       try {
/* 415 */         return this.input.readChar();
/* 416 */       } catch (IOException e) {
/* 417 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readInt() {
/*     */       try {
/* 424 */         return this.input.readInt();
/* 425 */       } catch (IOException e) {
/* 426 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public long readLong() {
/*     */       try {
/* 433 */         return this.input.readLong();
/* 434 */       } catch (IOException e) {
/* 435 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public float readFloat() {
/*     */       try {
/* 442 */         return this.input.readFloat();
/* 443 */       } catch (IOException e) {
/* 444 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public double readDouble() {
/*     */       try {
/* 451 */         return this.input.readDouble();
/* 452 */       } catch (IOException e) {
/* 453 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public String readLine() {
/*     */       try {
/* 461 */         return this.input.readLine();
/* 462 */       } catch (IOException e) {
/* 463 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readUTF() {
/*     */       try {
/* 470 */         return this.input.readUTF();
/* 471 */       } catch (IOException e) {
/* 472 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteArrayDataOutput newDataOutput() {
/* 479 */     return newDataOutput(new ByteArrayOutputStream());
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
/*     */   public static ByteArrayDataOutput newDataOutput(int size) {
/* 491 */     if (size < 0) {
/* 492 */       throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[] { Integer.valueOf(size) }));
/*     */     }
/* 494 */     return newDataOutput(new ByteArrayOutputStream(size));
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
/*     */   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputStream) {
/* 510 */     return new ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataOutputStream
/*     */     implements ByteArrayDataOutput {
/*     */     final DataOutput output;
/*     */     final ByteArrayOutputStream byteArrayOutputStream;
/*     */     
/*     */     ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
/* 519 */       this.byteArrayOutputStream = byteArrayOutputStream;
/* 520 */       this.output = new DataOutputStream(byteArrayOutputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/*     */       try {
/* 526 */         this.output.write(b);
/* 527 */       } catch (IOException impossible) {
/* 528 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) {
/*     */       try {
/* 535 */         this.output.write(b);
/* 536 */       } catch (IOException impossible) {
/* 537 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) {
/*     */       try {
/* 544 */         this.output.write(b, off, len);
/* 545 */       } catch (IOException impossible) {
/* 546 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBoolean(boolean v) {
/*     */       try {
/* 553 */         this.output.writeBoolean(v);
/* 554 */       } catch (IOException impossible) {
/* 555 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeByte(int v) {
/*     */       try {
/* 562 */         this.output.writeByte(v);
/* 563 */       } catch (IOException impossible) {
/* 564 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBytes(String s) {
/*     */       try {
/* 571 */         this.output.writeBytes(s);
/* 572 */       } catch (IOException impossible) {
/* 573 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChar(int v) {
/*     */       try {
/* 580 */         this.output.writeChar(v);
/* 581 */       } catch (IOException impossible) {
/* 582 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChars(String s) {
/*     */       try {
/* 589 */         this.output.writeChars(s);
/* 590 */       } catch (IOException impossible) {
/* 591 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeDouble(double v) {
/*     */       try {
/* 598 */         this.output.writeDouble(v);
/* 599 */       } catch (IOException impossible) {
/* 600 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeFloat(float v) {
/*     */       try {
/* 607 */         this.output.writeFloat(v);
/* 608 */       } catch (IOException impossible) {
/* 609 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeInt(int v) {
/*     */       try {
/* 616 */         this.output.writeInt(v);
/* 617 */       } catch (IOException impossible) {
/* 618 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeLong(long v) {
/*     */       try {
/* 625 */         this.output.writeLong(v);
/* 626 */       } catch (IOException impossible) {
/* 627 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeShort(int v) {
/*     */       try {
/* 634 */         this.output.writeShort(v);
/* 635 */       } catch (IOException impossible) {
/* 636 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeUTF(String s) {
/*     */       try {
/* 643 */         this.output.writeUTF(s);
/* 644 */       } catch (IOException impossible) {
/* 645 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 651 */       return this.byteArrayOutputStream.toByteArray();
/*     */     }
/*     */   }
/*     */   
/* 655 */   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream()
/*     */     {
/*     */       public void write(int b) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b) {
/* 664 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b, int off, int len) {
/* 670 */         Preconditions.checkNotNull(b);
/* 671 */         Preconditions.checkPositionIndexes(off, off + len, b.length);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 676 */         return "ByteStreams.nullOutputStream()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputStream nullOutputStream() {
/* 686 */     return NULL_OUTPUT_STREAM;
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
/*     */   public static InputStream limit(InputStream in, long limit) {
/* 698 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream
/*     */     extends FilterInputStream {
/*     */     private long left;
/* 704 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/* 707 */       super(in);
/* 708 */       Preconditions.checkNotNull(in);
/* 709 */       Preconditions.checkArgument((limit >= 0L), "limit must be non-negative");
/* 710 */       this.left = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() throws IOException {
/* 715 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void mark(int readLimit) {
/* 721 */       this.in.mark(readLimit);
/* 722 */       this.mark = this.left;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 727 */       if (this.left == 0L) {
/* 728 */         return -1;
/*     */       }
/*     */       
/* 731 */       int result = this.in.read();
/* 732 */       if (result != -1) {
/* 733 */         this.left--;
/*     */       }
/* 735 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 740 */       if (this.left == 0L) {
/* 741 */         return -1;
/*     */       }
/*     */       
/* 744 */       len = (int)Math.min(len, this.left);
/* 745 */       int result = this.in.read(b, off, len);
/* 746 */       if (result != -1) {
/* 747 */         this.left -= result;
/*     */       }
/* 749 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void reset() throws IOException {
/* 754 */       if (!this.in.markSupported()) {
/* 755 */         throw new IOException("Mark not supported");
/*     */       }
/* 757 */       if (this.mark == -1L) {
/* 758 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 761 */       this.in.reset();
/* 762 */       this.left = this.mark;
/*     */     }
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 767 */       n = Math.min(n, this.left);
/* 768 */       long skipped = this.in.skip(n);
/* 769 */       this.left -= skipped;
/* 770 */       return skipped;
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
/*     */   public static void readFully(InputStream in, byte[] b) throws IOException {
/* 784 */     readFully(in, b, 0, b.length);
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
/*     */   public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
/* 800 */     int read = read(in, b, off, len);
/* 801 */     if (read != len) {
/* 802 */       throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
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
/*     */   public static void skipFully(InputStream in, long n) throws IOException {
/* 817 */     long skipped = skipUpTo(in, n);
/* 818 */     if (skipped < n) {
/* 819 */       throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long skipUpTo(InputStream in, long n) throws IOException {
/* 830 */     long totalSkipped = 0L;
/*     */     
/* 832 */     byte[] buf = null;
/*     */     
/* 834 */     while (totalSkipped < n) {
/* 835 */       long remaining = n - totalSkipped;
/* 836 */       long skipped = skipSafely(in, remaining);
/*     */       
/* 838 */       if (skipped == 0L) {
/*     */ 
/*     */         
/* 841 */         int skip = (int)Math.min(remaining, 8192L);
/* 842 */         if (buf == null)
/*     */         {
/*     */ 
/*     */           
/* 846 */           buf = new byte[skip];
/*     */         }
/* 848 */         if ((skipped = in.read(buf, 0, skip)) == -1L) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 854 */       totalSkipped += skipped;
/*     */     } 
/*     */     
/* 857 */     return totalSkipped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long skipSafely(InputStream in, long n) throws IOException {
/* 868 */     int available = in.available();
/* 869 */     return (available == 0) ? 0L : in.skip(Math.min(available, n));
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
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
/*     */     int read;
/* 885 */     Preconditions.checkNotNull(input);
/* 886 */     Preconditions.checkNotNull(processor);
/*     */     
/* 888 */     byte[] buf = createBuffer();
/*     */     
/*     */     do {
/* 891 */       read = input.read(buf);
/* 892 */     } while (read != -1 && processor.processBytes(buf, 0, read));
/* 893 */     return processor.getResult();
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
/* 924 */     Preconditions.checkNotNull(in);
/* 925 */     Preconditions.checkNotNull(b);
/* 926 */     if (len < 0) {
/* 927 */       throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", new Object[] { Integer.valueOf(len) }));
/*     */     }
/* 929 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/* 930 */     int total = 0;
/* 931 */     while (total < len) {
/* 932 */       int result = in.read(b, off + total, len - total);
/* 933 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 936 */       total += result;
/*     */     } 
/* 938 */     return total;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/ByteStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */