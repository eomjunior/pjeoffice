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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  36 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b) {
/*  43 */     update(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update(byte[] b, int off, int len) {
/*  48 */     for (int i = off; i < off + len; i++) {
/*  49 */       update(b[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update(ByteBuffer b) {
/*  55 */     if (b.hasArray()) {
/*  56 */       update(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*  57 */       Java8Compatibility.position(b, b.limit());
/*     */     } else {
/*  59 */       for (int remaining = b.remaining(); remaining > 0; remaining--) {
/*  60 */         update(b.get());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Hasher update(int bytes) {
/*     */     try {
/*  69 */       update(this.scratch.array(), 0, bytes);
/*     */     } finally {
/*  71 */       Java8Compatibility.clear(this.scratch);
/*     */     } 
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putByte(byte b) {
/*  79 */     update(b);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(byte[] bytes) {
/*  86 */     Preconditions.checkNotNull(bytes);
/*  87 */     update(bytes);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  94 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  95 */     update(bytes, off, len);
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(ByteBuffer bytes) {
/* 102 */     update(bytes);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putShort(short s) {
/* 109 */     this.scratch.putShort(s);
/* 110 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putInt(int i) {
/* 116 */     this.scratch.putInt(i);
/* 117 */     return update(4);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putLong(long l) {
/* 123 */     this.scratch.putLong(l);
/* 124 */     return update(8);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putChar(char c) {
/* 130 */     this.scratch.putChar(c);
/* 131 */     return update(2);
/*     */   }
/*     */   
/*     */   protected abstract void update(byte paramByte);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractByteHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */