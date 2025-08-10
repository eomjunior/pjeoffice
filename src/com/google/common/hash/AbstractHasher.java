/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractHasher
/*     */   implements Hasher
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putBoolean(boolean b) {
/*  34 */     return putByte(b ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putDouble(double d) {
/*  40 */     return putLong(Double.doubleToRawLongBits(d));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Hasher putFloat(float f) {
/*  46 */     return putInt(Float.floatToRawIntBits(f));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putUnencodedChars(CharSequence charSequence) {
/*  52 */     for (int i = 0, len = charSequence.length(); i < len; i++) {
/*  53 */       putChar(charSequence.charAt(i));
/*     */     }
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putString(CharSequence charSequence, Charset charset) {
/*  61 */     return putBytes(charSequence.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(byte[] bytes) {
/*  67 */     return putBytes(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  73 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  74 */     for (int i = 0; i < len; i++) {
/*  75 */       putByte(bytes[off + i]);
/*     */     }
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putBytes(ByteBuffer b) {
/*  83 */     if (b.hasArray()) {
/*  84 */       putBytes(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*  85 */       Java8Compatibility.position(b, b.limit());
/*     */     } else {
/*  87 */       for (int remaining = b.remaining(); remaining > 0; remaining--) {
/*  88 */         putByte(b.get());
/*     */       }
/*     */     } 
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putShort(short s) {
/*  97 */     putByte((byte)s);
/*  98 */     putByte((byte)(s >>> 8));
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putInt(int i) {
/* 105 */     putByte((byte)i);
/* 106 */     putByte((byte)(i >>> 8));
/* 107 */     putByte((byte)(i >>> 16));
/* 108 */     putByte((byte)(i >>> 24));
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putLong(long l) {
/* 115 */     for (int i = 0; i < 64; i += 8) {
/* 116 */       putByte((byte)(int)(l >>> i));
/*     */     }
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Hasher putChar(char c) {
/* 124 */     putByte((byte)c);
/* 125 */     putByte((byte)(c >>> 8));
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> Hasher putObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
/* 133 */     funnel.funnel(instance, this);
/* 134 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */