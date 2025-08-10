/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractStreamingHasher
/*     */   extends AbstractHasher
/*     */ {
/*     */   private final ByteBuffer buffer;
/*     */   private final int bufferSize;
/*     */   private final int chunkSize;
/*     */   
/*     */   protected AbstractStreamingHasher(int chunkSize) {
/*  50 */     this(chunkSize, chunkSize);
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
/*     */   protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
/*  64 */     Preconditions.checkArgument((bufferSize % chunkSize == 0));
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
/*  69 */     this.bufferSize = bufferSize;
/*  70 */     this.chunkSize = chunkSize;
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
/*     */   protected void processRemaining(ByteBuffer bb) {
/*  83 */     Java8Compatibility.position(bb, bb.limit());
/*  84 */     Java8Compatibility.limit(bb, this.chunkSize + 7);
/*  85 */     while (bb.position() < this.chunkSize) {
/*  86 */       bb.putLong(0L);
/*     */     }
/*  88 */     Java8Compatibility.limit(bb, this.chunkSize);
/*  89 */     Java8Compatibility.flip(bb);
/*  90 */     process(bb);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putBytes(byte[] bytes, int off, int len) {
/*  96 */     return putBytesInternal(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putBytes(ByteBuffer readBuffer) {
/* 102 */     ByteOrder order = readBuffer.order();
/*     */     try {
/* 104 */       readBuffer.order(ByteOrder.LITTLE_ENDIAN);
/* 105 */       return putBytesInternal(readBuffer);
/*     */     } finally {
/* 107 */       readBuffer.order(order);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Hasher putBytesInternal(ByteBuffer readBuffer) {
/* 114 */     if (readBuffer.remaining() <= this.buffer.remaining()) {
/* 115 */       this.buffer.put(readBuffer);
/* 116 */       munchIfFull();
/* 117 */       return this;
/*     */     } 
/*     */ 
/*     */     
/* 121 */     int bytesToCopy = this.bufferSize - this.buffer.position();
/* 122 */     for (int i = 0; i < bytesToCopy; i++) {
/* 123 */       this.buffer.put(readBuffer.get());
/*     */     }
/* 125 */     munch();
/*     */ 
/*     */     
/* 128 */     while (readBuffer.remaining() >= this.chunkSize) {
/* 129 */       process(readBuffer);
/*     */     }
/*     */ 
/*     */     
/* 133 */     this.buffer.put(readBuffer);
/* 134 */     return this;
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
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putByte(byte b) {
/* 150 */     this.buffer.put(b);
/* 151 */     munchIfFull();
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putShort(short s) {
/* 158 */     this.buffer.putShort(s);
/* 159 */     munchIfFull();
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putChar(char c) {
/* 166 */     this.buffer.putChar(c);
/* 167 */     munchIfFull();
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putInt(int i) {
/* 174 */     this.buffer.putInt(i);
/* 175 */     munchIfFull();
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putLong(long l) {
/* 182 */     this.buffer.putLong(l);
/* 183 */     munchIfFull();
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HashCode hash() {
/* 189 */     munch();
/* 190 */     Java8Compatibility.flip(this.buffer);
/* 191 */     if (this.buffer.remaining() > 0) {
/* 192 */       processRemaining(this.buffer);
/* 193 */       Java8Compatibility.position(this.buffer, this.buffer.limit());
/*     */     } 
/* 195 */     return makeHash();
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
/*     */   private void munchIfFull() {
/* 207 */     if (this.buffer.remaining() < 8)
/*     */     {
/* 209 */       munch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void munch() {
/* 214 */     Java8Compatibility.flip(this.buffer);
/* 215 */     while (this.buffer.remaining() >= this.chunkSize)
/*     */     {
/*     */       
/* 218 */       process(this.buffer);
/*     */     }
/* 220 */     this.buffer.compact();
/*     */   }
/*     */   
/*     */   protected abstract void process(ByteBuffer paramByteBuffer);
/*     */   
/*     */   protected abstract HashCode makeHash();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractStreamingHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */