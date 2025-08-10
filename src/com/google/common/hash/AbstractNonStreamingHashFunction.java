/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractNonStreamingHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   public Hasher newHasher() {
/*  37 */     return newHasher(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  42 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  43 */     return new BufferingHasher(expectedInputSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  48 */     return hashBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(input).array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  53 */     return hashBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(input).array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  58 */     int len = input.length();
/*  59 */     ByteBuffer buffer = ByteBuffer.allocate(len * 2).order(ByteOrder.LITTLE_ENDIAN);
/*  60 */     for (int i = 0; i < len; i++) {
/*  61 */       buffer.putChar(input.charAt(i));
/*     */     }
/*  63 */     return hashBytes(buffer.array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  68 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract HashCode hashBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(ByteBuffer input) {
/*  76 */     return newHasher(input.remaining()).putBytes(input).hash();
/*     */   }
/*     */   
/*     */   private final class BufferingHasher
/*     */     extends AbstractHasher {
/*     */     final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
/*     */     
/*     */     BufferingHasher(int expectedInputSize) {
/*  84 */       this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/*  89 */       this.stream.write(b);
/*  90 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/*  95 */       this.stream.write(bytes, off, len);
/*  96 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(ByteBuffer bytes) {
/* 101 */       this.stream.write(bytes);
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 107 */       return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExposedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream {
/*     */     ExposedByteArrayOutputStream(int expectedInputSize) {
/* 114 */       super(expectedInputSize);
/*     */     }
/*     */     
/*     */     void write(ByteBuffer input) {
/* 118 */       int remaining = input.remaining();
/* 119 */       if (this.count + remaining > this.buf.length) {
/* 120 */         this.buf = Arrays.copyOf(this.buf, this.count + remaining);
/*     */       }
/* 122 */       input.get(this.buf, this.count, remaining);
/* 123 */       this.count += remaining;
/*     */     }
/*     */     
/*     */     byte[] byteArray() {
/* 127 */       return this.buf;
/*     */     }
/*     */     
/*     */     int length() {
/* 131 */       return this.count;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractNonStreamingHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */