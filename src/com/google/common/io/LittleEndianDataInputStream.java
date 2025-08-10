/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class LittleEndianDataInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   public LittleEndianDataInputStream(InputStream in) {
/*  53 */     super((InputStream)Preconditions.checkNotNull(in));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public String readLine() {
/*  61 */     throw new UnsupportedOperationException("readLine is not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/*  66 */     ByteStreams.readFully(this, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/*  71 */     ByteStreams.readFully(this, b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/*  76 */     return (int)this.in.skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int readUnsignedByte() throws IOException {
/*  82 */     int b1 = this.in.read();
/*  83 */     if (0 > b1) {
/*  84 */       throw new EOFException();
/*     */     }
/*     */     
/*  87 */     return b1;
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
/*     */   public int readUnsignedShort() throws IOException {
/* 101 */     byte b1 = readAndCheckByte();
/* 102 */     byte b2 = readAndCheckByte();
/*     */     
/* 104 */     return Ints.fromBytes((byte)0, (byte)0, b2, b1);
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
/*     */   public int readInt() throws IOException {
/* 118 */     byte b1 = readAndCheckByte();
/* 119 */     byte b2 = readAndCheckByte();
/* 120 */     byte b3 = readAndCheckByte();
/* 121 */     byte b4 = readAndCheckByte();
/*     */     
/* 123 */     return Ints.fromBytes(b4, b3, b2, b1);
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
/*     */   public long readLong() throws IOException {
/* 137 */     byte b1 = readAndCheckByte();
/* 138 */     byte b2 = readAndCheckByte();
/* 139 */     byte b3 = readAndCheckByte();
/* 140 */     byte b4 = readAndCheckByte();
/* 141 */     byte b5 = readAndCheckByte();
/* 142 */     byte b6 = readAndCheckByte();
/* 143 */     byte b7 = readAndCheckByte();
/* 144 */     byte b8 = readAndCheckByte();
/*     */     
/* 146 */     return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
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
/*     */   public float readFloat() throws IOException {
/* 160 */     return Float.intBitsToFloat(readInt());
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
/*     */   public double readDouble() throws IOException {
/* 174 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public String readUTF() throws IOException {
/* 180 */     return (new DataInputStream(this.in)).readUTF();
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
/*     */   public short readShort() throws IOException {
/* 194 */     return (short)readUnsignedShort();
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
/*     */   public char readChar() throws IOException {
/* 208 */     return (char)readUnsignedShort();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public byte readByte() throws IOException {
/* 214 */     return (byte)readUnsignedByte();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean readBoolean() throws IOException {
/* 220 */     return (readUnsignedByte() != 0);
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
/*     */   private byte readAndCheckByte() throws IOException, EOFException {
/* 232 */     int b1 = this.in.read();
/*     */     
/* 234 */     if (-1 == b1) {
/* 235 */       throw new EOFException();
/*     */     }
/*     */     
/* 238 */     return (byte)b1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/LittleEndianDataInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */